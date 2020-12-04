package com.colourfulchina.pangu.taishang.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.pangu.taishang.api.entity.*;
import com.colourfulchina.pangu.taishang.api.enums.DelFlagEnums;
import com.colourfulchina.pangu.taishang.mapper.*;
import com.colourfulchina.pangu.taishang.service.BlockRuleService;
import com.colourfulchina.pangu.taishang.service.GroupProductBlockDateService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

@Slf4j
@Service
public class GroupProductBlockDateServiceImpl extends ServiceImpl<GroupProductBlockDateMapper,GroupProductBlockDate> implements GroupProductBlockDateService {
    @Autowired
    private GroupProductBlockDateMapper groupProductBlockDateMapper;
    @Autowired
    private ProductGroupProductMapper productGroupProductMapper;
    @Autowired
    private ProductGroupMapper productGroupMapper;
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private ShopItemMapper shopItemMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ShopProtocolMapper shopProtocolMapper;
    @Autowired
    private BlockRuleService blockRuleService;


    /**
     * 产品组产品block日期的生成添加
     * @param productGroupProductId
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public void addBlockDate(Integer productGroupProductId) throws Exception{
        log.info("产品组产品block日期生成,产品组产品id:{}",productGroupProductId);
        Assert.notNull(productGroupProductId,"产品组产品id不能为空");
        Calendar calendar = Calendar.getInstance();
        Date startDate = new Date();
        calendar.add(Calendar.MONTH,4);
        Date endDate = calendar.getTime();
        startDate = DateUtil.parse(DateUtil.format(startDate,"yyyy-MM-dd"),"yyyy-MM-dd");
        endDate = DateUtil.parse(DateUtil.format(endDate,"yyyy-MM-dd"),"yyyy-MM-dd");
        //产品组产品信息
        ProductGroupProduct productGroupProduct = productGroupProductMapper.selectById(productGroupProductId);
        //产品信息
        Product product = productMapper.selectById(productGroupProduct.getProductId());
        //产品组信息
        ProductGroup productGroup = productGroupMapper.selectById(productGroupProduct.getProductGroupId());
        //商品信息
        Goods goods = goodsMapper.selectById(productGroup.getGoodsId());
        //规格信息
        ShopItem shopItem = shopItemMapper.selectById(product.getShopItemId());
        //商户协议信息
        ShopProtocol shopProtocol = shopProtocolMapper.selectById(product.getShopId());
        List<String> blockList = Lists.newLinkedList();
        String allBlock = null;
        if (StringUtils.isNotBlank(productGroupProduct.getBlockRule())){
            blockList.add(productGroupProduct.getBlockRule());
        }
        if (StringUtils.isNotBlank(productGroup.getBlockRule())){
            blockList.add(productGroup.getBlockRule());
        }
        if (StringUtils.isNotBlank(goods.getBlock())){
            blockList.add(goods.getBlock());
        }
        if (StringUtils.isNotBlank(shopItem.getBlockRule())){
            blockList.add(shopItem.getBlockRule());
        }
        if (StringUtils.isNotBlank(shopProtocol.getBlockRule())){
            blockList.add(shopProtocol.getBlockRule());
        }
        if (!CollectionUtils.isEmpty(blockList)){
            allBlock = StringUtils.join(blockList,", ");
        }
        //删除本次开始时间之后的该产品组产品block日期，以便于重新生成
        Date finalStartDate = startDate;
        Wrapper delWrapper = new Wrapper() {
            @Override
            public String getSqlSegment() {
                return "where product_group_product_id ="+productGroupProductId+" and block_date >= str_to_date('"+DateUtil.format(finalStartDate,"yyyy-MM-dd")+"',"+"'%Y-%m-%d'"+")";
            }
        };
        groupProductBlockDateMapper.delete(delWrapper);
        if (StringUtils.isNotBlank(allBlock)){
            HashSet<Date> blockSet = blockRuleService.generateBlockDate(startDate,endDate,allBlock);
            //生成block时间
            if (!CollectionUtils.isEmpty(blockSet)){
                for (Date date : blockSet) {
                    GroupProductBlockDate groupProductBlockDate = new GroupProductBlockDate();
                    groupProductBlockDate.setProductGroupProductId(productGroupProductId);
                    groupProductBlockDate.setBlockDate(date);
                    groupProductBlockDate.setDelFlag(DelFlagEnums.NORMAL.getCode());
                    groupProductBlockDateMapper.insert(groupProductBlockDate);
                }
            }
        }
    }

    /**
     * 生成所有产品组产品的block日期
     * @throws Exception
     */
    @Override
    public void generateBothBlockDate() throws Exception {
        log.info("产品组产品生成block日期开始");
        //获取所有产品组产品
        Wrapper allWrapper = new Wrapper() {
            @Override
            public String getSqlSegment() {
                return "where del_flag = 0";
            }
        };
        List<ProductGroupProduct> list = productGroupProductMapper.selectList(allWrapper);
        if (!CollectionUtils.isEmpty(list)){
            for (ProductGroupProduct productGroupProduct : list) {
                addBlockDate(productGroupProduct.getId());
            }
        }
        log.info("产品组产品生成block日期结束");
    }

    /**
     * 根据产品组产品id重新生成block日期
     * @param ids
     * @throws Exception
     */
    @Override
    @Async("taskExecutorPool")
    public void updBlockDate(List<Integer> ids) throws Exception {
        if (!CollectionUtils.isEmpty(ids)){
            for (Integer id : ids) {
                addBlockDate(id);
            }
        }
    }

    /**
     * 根据产品组id重新生成block日期
     * @param productGroupId
     * @throws Exception
     */
    @Override
    @Async("taskExecutorPool")
    public void updBlockDateByGroupId(Integer productGroupId) throws Exception {
        Wrapper wrapper = new Wrapper() {
            @Override
            public String getSqlSegment() {
                return "where del_flag = 0 and product_group_id ="+productGroupId;
            }
        };
        List<ProductGroupProduct> productGroupProducts = productGroupProductMapper.selectList(wrapper);
        List<Integer> ids = Lists.newLinkedList();
        if (!CollectionUtils.isEmpty(productGroupProducts)){
            for (ProductGroupProduct productGroupProduct : productGroupProducts) {
                ids.add(productGroupProduct.getId());
            }
        }
        if (!CollectionUtils.isEmpty(ids)){
            updBlockDate(ids);
        }
    }

    /**
     * 根据商品id重新生成block日期
     * @param goodsId
     * @throws Exception
     */
    @Override
    @Async("taskExecutorPool")
    public void updBlockDateByGoodsId(Integer goodsId) throws Exception {
        Wrapper productGroupWrapper = new Wrapper() {
            @Override
            public String getSqlSegment() {
                return "where del_flag = 0 and goods_id ="+goodsId;
            }
        };
        List<ProductGroup> groups = productGroupMapper.selectList(productGroupWrapper);
        if (!CollectionUtils.isEmpty(groups)){
            for (ProductGroup group : groups) {
                updBlockDateByGroupId(group.getId());
            }
        }
    }

    /**
     * 根据商户规格id重新生成block日期
     * @param shopItemId
     * @throws Exception
     */
    @Override
    @Async("taskExecutorPool")
    public void updBlockDateByShopItemId(Integer shopItemId) throws Exception {
        List<Integer> ids = Lists.newLinkedList();
        Wrapper productWrapper = new Wrapper() {
            @Override
            public String getSqlSegment() {
                return "where del_flag = 0 and shop_item_id ="+shopItemId;
            }
        };
        List<Product> products = productMapper.selectList(productWrapper);
        if (!CollectionUtils.isEmpty(products)){
            for (Product product : products) {
                Wrapper productGroupProductWrapper = new Wrapper() {
                    @Override
                    public String getSqlSegment() {
                        return "where del_flag = 0 and product_id ="+product.getId();
                    }
                };
                List<ProductGroupProduct> groupProducts = productGroupProductMapper.selectList(productGroupProductWrapper);
                if (!CollectionUtils.isEmpty(groupProducts)){
                    for (ProductGroupProduct productGroupProduct : groupProducts) {
                        ids.add(productGroupProduct.getId());
                    }
                }
            }
            updBlockDate(ids);
        }
    }

    /**
     * 根据商户id重新生成block日期
     * @param shopId
     * @throws Exception
     */
    @Override
    @Async("taskExecutorPool")
    public void updBlockDateByShopId(Integer shopId) throws Exception {
        Wrapper shopItemWrapper = new Wrapper() {
            @Override
            public String getSqlSegment() {
                return "where del_flag = 0 and shop_id ="+shopId;
            }
        };
        List<ShopItem> shopItems = shopItemMapper.selectList(shopItemWrapper);
        if (!CollectionUtils.isEmpty(shopItems)){
            for (ShopItem shopItem : shopItems) {
                updBlockDateByShopItemId(shopItem.getId());
            }
        }
    }
}