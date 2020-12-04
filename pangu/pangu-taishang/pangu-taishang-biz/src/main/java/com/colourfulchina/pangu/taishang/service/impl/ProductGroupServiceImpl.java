package com.colourfulchina.pangu.taishang.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.SqlHelper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.god.door.api.util.SecurityUtils;
import com.colourfulchina.inf.base.enums.SysDictTypeEnums;
import com.colourfulchina.inf.base.vo.PageVo;
import com.colourfulchina.pangu.taishang.api.dto.BlockRule;
import com.colourfulchina.pangu.taishang.api.dto.BookNum;
import com.colourfulchina.pangu.taishang.api.entity.*;
import com.colourfulchina.pangu.taishang.api.enums.*;
import com.colourfulchina.pangu.taishang.api.vo.*;
import com.colourfulchina.pangu.taishang.api.vo.req.*;
import com.colourfulchina.pangu.taishang.api.vo.res.*;
import com.colourfulchina.pangu.taishang.api.vo.res.bookBasePayment.BookBasePaymentRes;
import com.colourfulchina.pangu.taishang.mapper.GoodsMapper;
import com.colourfulchina.pangu.taishang.mapper.ProductGroupMapper;
import com.colourfulchina.pangu.taishang.mapper.ServiceGiftMapper;
import com.colourfulchina.pangu.taishang.mapper.ShopItemMapper;
import com.colourfulchina.pangu.taishang.service.*;
import com.colourfulchina.pangu.taishang.utils.BookMinMaxDayUtils;
import com.colourfulchina.pangu.taishang.utils.DateUtils;
import com.colourfulchina.tianyan.admin.api.entity.SysDict;
import com.colourfulchina.tianyan.admin.api.feign.RemoteDictService;
import com.colourfulchina.tianyan.common.core.util.R;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class ProductGroupServiceImpl extends ServiceImpl<ProductGroupMapper,ProductGroup> implements ProductGroupService {
    @Autowired
    private ProductGroupMapper productGroupMapper;
    @Autowired
    private ProductGroupGiftService productGroupGiftService;
    @Autowired
    private ProductGroupResourceService productGroupResourceService;
    @Autowired
    private BlockRuleService blockRuleService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductGroupProductService productGroupProductService;
    @Autowired
    private ShopProtocolService shopProtocolService;
    @Autowired
    private HotelService hotelService;
    @Autowired
    private SalesChannelService salesChannelService;
    @Autowired
    private ShopItemNetPriceRuleService shopItemNetPriceRuleService;
    @Autowired
    private ShopItemSettlePriceRuleService shopItemSettlePriceRuleService;
    @Autowired
    private ShopChannelService shopChannelService;
    @Autowired
    private ShopItemSettleExpressService shopItemSettleExpressService;
    @Autowired
    private SysServiceService sysServiceService;
    @Autowired
    private GiftService giftService;
    @Autowired
    private ShopItemMapper shopItemMapper;
    @Autowired
    private ServiceGiftMapper serviceGiftMapper;
    @Autowired
    private GoodsMapper goodsMapper;
    private RemoteDictService remoteDictService;
    @Autowired
    private BookBasePaymentService bookBasePaymentService;
    @Autowired
    private ProductItemService productItemService;
    @Autowired
    private GroupProductBlockDateService groupProductBlockDateService;
    /**
     * 新增产品组
     * @param groupSaveReq
     * @return
     */
    @Override
    @Transactional(rollbackFor=Exception.class)
    public ProductGroup save(GroupSaveReq groupSaveReq) throws Exception {
        //产品组入库
        if (groupSaveReq.getDiscountRate() != null){
            groupSaveReq.setDiscountRate(groupSaveReq.getDiscountRate().divide(new BigDecimal(100)));
        }
        ProductGroup productGroup = new ProductGroup();
        BeanUtils.copyProperties(groupSaveReq,productGroup);
        productGroup.setDelFlag(DelFlagEnums.NORMAL.getCode());
        productGroup.setCreateUser(SecurityUtils.getLoginName());
        //使用类型
        if (StringUtils.isNotBlank(productGroup.getUseLimitId())){
            if (productGroup.getUseLimitId().equals("fixed_point")){
                productGroup.setUseType(GroupUseTypeEnums.USE_POINT.getCode());
            }
            if (productGroup.getUseLimitId().equals("fixed_times")){
                productGroup.setUseType(GroupUseTypeEnums.USE_TIMES.getCode());
            }
        }
        productGroupMapper.insert(productGroup);
        //产品组对应权益关系入库
        List<ProductGroupGift> giftList = productGroupGiftService.storage(groupSaveReq.getGift(),productGroup.getId());
        //产品组对应资源类型入库
        List<ProductGroupResource> resourceList = productGroupResourceService.storage(groupSaveReq.getService(),productGroup.getId(),groupSaveReq.getGoodsId());
        return productGroup;
    }

    /**
     * 根据id查询产品组
     * @param productGroupId
     * @return
     * @throws Exception
     */
    @Override
    public GroupQueryOneRes findOneById(Integer productGroupId) throws Exception {
        GroupQueryOneRes res = new GroupQueryOneRes();
        List<String> serviceList = Lists.newLinkedList();
        List<String> giftList = Lists.newLinkedList();
        List<String> serviceNameList = Lists.newLinkedList();
        List<String> giftNameList = Lists.newLinkedList();
        ProductGroup productGroup = productGroupMapper.selectById(productGroupId);
        BeanUtils.copyProperties(productGroup,res);
        List<ProductGroupResource> services = productGroupResourceService.selectByProductGroupId(productGroupId);
        List<ProductGroupGift> gifts = productGroupGiftService.selectByProductGroupId(productGroupId);
        if (!CollectionUtils.isEmpty(services)){
            for (ProductGroupResource service : services) {
                SysService sysService = sysServiceService.selectById(service.getService());
                serviceList.add(service.getService());
                serviceNameList.add(sysService.getName());
            }
        }
        if (!CollectionUtils.isEmpty(gifts)){
            for (ProductGroupGift gift : gifts) {
                Gift g = giftService.selectById(gift.getGift());
                giftList.add(gift.getGift());
                giftNameList.add(g.getShortName());
            }
        }
        res.setGift(giftList);
        res.setService(serviceList);
        res.setGiftList(giftNameList);
        res.setServiceList(serviceNameList);
        return res;
    }
    public    List<SysDict> selectSysDict(String type)  {
        R<List<SysDict>> resultVO = remoteDictService.findDictByType(type);
        return resultVO.getData();
    }
    /**
     * 编辑产品组
     * @param groupUpdReq
     * @return
     */
    @Override
    @Transactional(rollbackFor=Exception.class)
    public ProductGroup updGroup(GroupUpdReq groupUpdReq) throws Exception {
        //产品组修改入库
        if (groupUpdReq.getDiscountRate() != null){
            groupUpdReq.setDiscountRate(groupUpdReq.getDiscountRate().divide(new BigDecimal(100)));
        }
        ProductGroup productGroup = new ProductGroup();
        BeanUtils.copyProperties(groupUpdReq,productGroup);
        productGroup.setUpdateUser(SecurityUtils.getLoginName());
        //使用类型
        if (StringUtils.isNotBlank(productGroup.getUseLimitId())){
            if (productGroup.getUseLimitId().equals("fixed_point")){
                productGroup.setUseType(GroupUseTypeEnums.USE_POINT.getCode());
            }
            if (productGroup.getUseLimitId().equals("fixed_times")){
                productGroup.setUseType(GroupUseTypeEnums.USE_TIMES.getCode());
            }
        }
        productGroupMapper.updateById(productGroup);
        //产品组对应权益关系入库
        List<ProductGroupGift> giftList = productGroupGiftService.storage(groupUpdReq.getGift(),productGroup.getId());
        //产品组对应资源类型入库
        List<ProductGroupResource> resourceList = productGroupResourceService.storage(groupUpdReq.getService(),productGroup.getId(),groupUpdReq.getGoodsId());
        return productGroup;
    }

    /**
     * 删除产品组
     * @param id
     * @return
     */
    @Override
    public Boolean delGroup(Integer id) throws Exception {
        ProductGroup productGroup = productGroupMapper.selectById(id);
        productGroup.setDelFlag(DelFlagEnums.DELETE.getCode());
        Integer result = productGroupMapper.updateById(productGroup);
        return SqlHelper.retBool(result);
    }

    /**
     * 产品组分页查询
     * @param pageReq
     * @return
     * @throws Exception
     */
    @Override
    public PageVo<GroupPageRes> selectPageList(PageVo<GroupPageReq> pageReq) throws Exception {
        PageVo<GroupPageRes> pageRes = new PageVo<>();
        Map<String, Object> params = pageReq.getCondition() == null ? Maps.newHashMap(): pageReq.getCondition();
        if (params.containsKey("salesChannel") && params.get("salesChannel") != null ) {
            List<Integer> salesChannelIds = (List) params.get("salesChannel");
            if (salesChannelIds.size() > 0) {
                SalesChannel salesChannel = new SalesChannel();
                salesChannel.setBankId(salesChannelIds.get(0)+"");
                salesChannel.setSalesChannelId(salesChannelIds.get(1)+"");
                salesChannel.setSalesWayId(salesChannelIds.get(2)+"");
                List<SalesChannel> channels = salesChannelService.selectByBCW(salesChannel);
                if (!CollectionUtils.isEmpty(channels)){
                    params.put("salesChannel",channels.get(0).getId());
                }
            }
        }
        List<GroupPageRes> list = productGroupMapper.selectPageList(pageReq,params);
        BeanUtils.copyProperties(pageReq,pageRes);
        List<SysDict> bankList = selectSysDict(SysDictTypeEnums.BANK_TYPE.getType());
        List<SysDict> salesChannelList =selectSysDict(SysDictTypeEnums.SALES_CHANNEL_TYPE.getType());
        List<SysDict> salesWayList = selectSysDict(SysDictTypeEnums.SALES_WAY_TYPE.getType());
        Map<String, SysDict> bankMap = bankList.stream().collect(Collectors.toMap(SysDict::getValue, bank -> bank));
        Map<String, SysDict> salesChannelMap = salesChannelList.stream().collect(Collectors.toMap(SysDict::getValue, bank -> bank));
        Map<String, SysDict> salesWayMap = salesWayList.stream().collect(Collectors.toMap(SysDict::getValue, bank -> bank));
        //TODO:调用tianyan字典表接口，获取银行、销售渠道、销售方式存map，循环遍历list重新赋值
        for(GroupPageRes listRBaseVo : list){
            SysDict sysDict1 = bankMap.get(listRBaseVo.getBankId());
            if(null  == sysDict1 ){
                listRBaseVo.setBankId("-");
            }else{
                listRBaseVo.setBankId(sysDict1.getLabel());
            }
            SysDict sysDict2 = salesChannelMap.get(listRBaseVo.getSalesChannelId());
            if(null  == sysDict2 ){
                listRBaseVo.setSalesChannelId("-");
            }else{
                listRBaseVo.setSalesChannelId(sysDict2.getLabel());
            }
            SysDict sysDict3 = salesWayMap.get(listRBaseVo.getSalesWayId());
            if(null  == sysDict3 ){
                listRBaseVo.setSalesWayId("-");
            }else{
                listRBaseVo.setSalesWayId(sysDict3.getLabel());
            }
        }
        pageRes.setRecords(list);
        return pageRes;
    }

    /**
     * 产品组详情查询
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public GroupDetailRes groupDetail(Integer id) throws Exception {
        GroupDetailRes res = productGroupMapper.queryGroupDetail(id);
        List<BlockRule> blockRuleList = Lists.newLinkedList();
        if (StringUtils.isNotBlank(res.getBlockRule())){
            blockRuleList = blockRuleService.blockRuleStr2list(res.getBlockRule());
        }
        res.setBlockRules(blockRuleList);
        List<GroupProductVo> productVoList = productService.selectByGroupId(id);
        res.setProductVoList(productVoList);
        List<SysDict> sysDicts = salesChannelService.selectSysDict(SysDictTypeEnums.USE_LIMIT_TYPE.getType());
        for (SysDict sysDict : sysDicts) {
            if (sysDict.getValue().equals(res.getUseLimitId())){
                res.setUseLimit(sysDict.getLabel());
            }
        }
        return res;
    }

    /**
     * 复制产品组
     * @param groupCopyReq
     * @return
     * @throws Exception
     */
    @Override
    public void copyGroup(GroupCopyReq groupCopyReq) throws Exception {
        List<Integer> ids = Lists.newLinkedList();
        for (Integer groupId : groupCopyReq.getGroupIds()) {
            //产品组product_group表复制
            ProductGroup productGroup = productGroupMapper.selectById(groupId);
            productGroup.setGoodsId(groupCopyReq.getGoodsId());
            productGroup.setId(null);
            ProductGroup productGroupNew = new ProductGroup();
            BeanUtils.copyProperties(productGroup,productGroupNew);
            productGroupNew.setCreateTime(new Date());
            productGroupNew.setUpdateTime(new Date());
            productGroupMapper.insert(productGroupNew);
            //产品组和权益关系product_group_gift表复制
            Wrapper wrapperGift = new Wrapper() {
                @Override
                public String getSqlSegment() {
                    return "where del_flag = 0 and product_group_id ="+groupId;
                }
            };
            List<ProductGroupGift> giftList = productGroupGiftService.selectList(wrapperGift);
            for (ProductGroupGift productGroupGift : giftList) {
                productGroupGift.setId(null);
                productGroupGift.setProductGroupId(productGroupNew.getId());
                productGroupGiftService.insert(productGroupGift);
            }
            //产品组和资源类型关系product_group_resource表复制
            Wrapper wrapperService = new Wrapper() {
                @Override
                public String getSqlSegment() {
                    return "where del_flag = 0 and product_group_id ="+groupId;
                }
            };
            List<ProductGroupResource> serviceList = productGroupResourceService.selectList(wrapperService);
            for (ProductGroupResource productGroupResource : serviceList) {
                ProductGroupResource newProductGroupResource = new ProductGroupResource();
                BeanUtils.copyProperties(productGroupResource,newProductGroupResource);
                newProductGroupResource.setId(null);
                newProductGroupResource.setProductGroupId(productGroupNew.getId());
                newProductGroupResource.setCreateTime(new Date());
                newProductGroupResource.setUpdateTime(new Date());
                newProductGroupResource.setGoodsId(groupCopyReq.getGoodsId());
                productGroupResourceService.insert(newProductGroupResource);
            }


            //产品组和产品的关系product_group_product表复制
            Wrapper wrapperProduct = new Wrapper() {
                @Override
                public String getSqlSegment() {
                    return "where del_flag = 0 and product_group_id ="+groupId;
                }
            };
            List<ProductGroupProduct> productList = productGroupProductService.selectList(wrapperProduct);
            for (ProductGroupProduct productGroupProduct : productList) {
                Integer oldProductGroupProductId = productGroupProduct.getId();
                productGroupProduct.setId(null);
                productGroupProduct.setProductGroupId(productGroupNew.getId());
                productGroupProductService.insert(productGroupProduct);
                //预约需支付价格复制
                Wrapper wrapper = new Wrapper() {
                    @Override
                    public String getSqlSegment() {
                        return "where del_flag = 0 and product_group_product_id ="+oldProductGroupProductId;
                    }
                };
                List<BookBasePayment> paymentList = bookBasePaymentService.selectList(wrapper);
                if (!CollectionUtils.isEmpty(paymentList)){
                    for (BookBasePayment bookBasePayment : paymentList) {
                        bookBasePayment.setId(null);
                        bookBasePayment.setProductGroupProductId(productGroupProduct.getId());
                        bookBasePaymentService.insert(bookBasePayment);
                    }
                }
                ids.add(productGroupProduct.getId());
            }
        }
        if (!CollectionUtils.isEmpty(ids)){
            //产品组的产品成本价格区间更新
            productGroupProductService.updCost(ids);
            //产品组产品block日期添加
            groupProductBlockDateService.updBlockDate(ids);
        }
    }

    /**
     * 产品组添加产品
     * @param groupAddProductReq
     * @throws Exception
     */
    @Override
    public void groupAddProduct(GroupAddProductReq groupAddProductReq) throws Exception {
        List<Integer> productGroupProductIds = Lists.newLinkedList();
        Wrapper sgWrapper = new Wrapper() {
            @Override
            public String getSqlSegment() {
                return "where del_flag = 0 and service_id = '"+ShopTypeEnums.ACCOM.getCode()+"'";
            }
        };
        List<ServiceGift> sgList = serviceGiftMapper.selectList(sgWrapper);
        if (!CollectionUtils.isEmpty(groupAddProductReq.getPackProducts())){
            for (PackProductVo packProductVo : groupAddProductReq.getPackProducts()) {
                String block = null;
                //合并产品子项，生成block
                if (!CollectionUtils.isEmpty(packProductVo.getProductItemIds())){
                    Wrapper wrapper = new Wrapper() {
                        @Override
                        public String getSqlSegment() {
                            return "where del_flag = 0 and id in ("+StringUtils.join(packProductVo.getProductItemIds(),",")+")";
                        }
                    };
                    List<ProductItem> productItemList = productItemService.selectList(wrapper);
                    Wrapper otherWrapper = new Wrapper() {
                        @Override
                        public String getSqlSegment() {
                            return "where del_flag = 0 and product_id = "+packProductVo.getProductId()+" and id not in ("+StringUtils.join(packProductVo.getProductItemIds(),",")+")";
                        }
                    };
                    List<ProductItem> otherItemList = productItemService.selectList(otherWrapper);
                    List<ProductItem> blockItem = productItemService.margeItem(productItemList);
                    if (!CollectionUtils.isEmpty(otherItemList)){
                        blockItem.addAll(otherItemList);
                    }
                    block = productItemService.item2Block(blockItem);
                }
                Integer productId = packProductVo.getProductId();
                String accomGift = null;
                //住宿类型的权益类型取产品组的权益类型
                Wrapper gWrapper = new Wrapper() {
                    @Override
                    public String getSqlSegment() {
                        return "where del_flag = 0 and product_group_id ="+groupAddProductReq.getProductGroupId();
                    }
                };
                List<ProductGroupGift> giftList = productGroupGiftService.selectList(gWrapper);
                for (ServiceGift serviceGift : sgList) {
                    boolean flag = false;
                    for (ProductGroupGift productGroupGift : giftList) {
                        if (serviceGift.getGiftId().equals(productGroupGift.getGift())){
                            accomGift = serviceGift.getGiftId();
                            flag = true;
                            break;
                        }
                    }
                    if (flag == true){
                        break;
                    }
                }
                Product product = productService.selectById(productId);
                ShopItem shopItem = shopItemMapper.selectById(product.getShopItemId());

                Integer sort = productGroupProductService.queryMaxSort();
                if (sort == null){
                    sort = 0;
                }
                ProductGroupProduct productGroupProduct = new ProductGroupProduct();
                productGroupProduct.setProductGroupId(groupAddProductReq.getProductGroupId());
                productGroupProduct.setProductId(productId);
                productGroupProduct.setDelFlag(DelFlagEnums.NORMAL.getCode());
                productGroupProduct.setSort(sort+1);
                productGroupProduct.setPoint(new BigDecimal(1));
                if (ShopTypeEnums.ACCOM.getCode().equals(shopItem.getServiceType())){
                    productGroupProduct.setGift(accomGift);
                }else {
                    productGroupProduct.setGift(product.getGift());
                }
                productGroupProduct.setStatus(ProductStatusEnums.ONSALE.getCode());
                productGroupProduct.setCreateUser(SecurityUtils.getLoginName());
                productGroupProduct.setBlockRule(block);
                productGroupProductService.insert(productGroupProduct);
                productGroupProductIds.add(productGroupProduct.getId());
                log.info("添加产品{}",productGroupProduct.getId());
            }
        }
        //产品组的产品成本价格区间更新
        productGroupProductService.updCost(productGroupProductIds);
        //产品组产品block日期添加
        groupProductBlockDateService.updBlockDate(productGroupProductIds);
    }
    /**
     * 产品组添加产品
     * @param groupAddProductReq
     * @throws Exception
     */
    @Override
    public void groupAddProductAndBlock(GroupAddProductAndBlockReq groupAddProductReq) throws Exception {
        List<Integer> productGroupProductIds = Lists.newLinkedList();
        Wrapper sgWrapper = new Wrapper() {
            @Override
            public String getSqlSegment() {
                return "where del_flag = 0 and service_id = '"+ShopTypeEnums.ACCOM.getCode()+"'";
            }
        };
        List<ServiceGift> sgList = serviceGiftMapper.selectList(sgWrapper);
        Map<Integer,String> productBlockMap=groupAddProductReq.getProductBlockMap();
        final Map<Integer, BigDecimal> productPointMap = groupAddProductReq.getProductPointMap();
        for (Integer productId : productBlockMap.keySet()) {
            String accomGift = null;
            //住宿类型的权益类型取产品组的权益类型
            Wrapper gWrapper = new Wrapper() {
                @Override
                public String getSqlSegment() {
                    return "where del_flag = 0 and product_group_id ="+groupAddProductReq.getProductGroupId();
                }
            };
            List<ProductGroupGift> giftList = productGroupGiftService.selectList(gWrapper);
            for (ServiceGift serviceGift : sgList) {
                boolean flag = false;
                for (ProductGroupGift productGroupGift : giftList) {
                    if (serviceGift.getGiftId().equals(productGroupGift.getGift())){
                        accomGift = serviceGift.getGiftId();
                        flag = true;
                        break;
                    }
                }
                if (flag == true){
                    break;
                }
            }
            //判断是不是存在产品 如果有不改变  没有替换成新的
            Integer newProductId = null;
              newProductId = productService.selectNewProductId(productId);
              log.info("productId{} and newProductId{}",productId,newProductId);
            if(newProductId != null){
                productId = newProductId;
            }
            Product product = productService.selectById(productId);
            ShopItem shopItem = shopItemMapper.selectById(product.getShopItemId());

            Integer sort = productGroupProductService.queryMaxSort();
            if (sort == null){
                sort = 0;
            }
            ProductGroupProduct productGroupProduct = new ProductGroupProduct();
            productGroupProduct.setProductGroupId(groupAddProductReq.getProductGroupId());
            productGroupProduct.setProductId(productId);
            productGroupProduct.setSort(sort+1);
            BigDecimal point=new BigDecimal(1);
            if (!CollectionUtils.isEmpty(productPointMap) && productPointMap.containsKey(productId)){
                point=productPointMap.get(productId);
            }
            productGroupProduct.setPoint(point);
            if (ShopTypeEnums.ACCOM.getCode().equals(shopItem.getServiceType())){
                productGroupProduct.setGift(accomGift);
            }else {
                productGroupProduct.setGift(product.getGift());
            }
            productGroupProduct.setBlockRule(productBlockMap.get(productId));
            //productGroupProduct.setStatus(ProductStatusEnums.ONSALE.getCode());
            productGroupProduct.setStatus(product.getStatus());
            productGroupProduct.setCreateUser(SecurityUtils.getLoginName());
            productGroupProduct.setDelFlag(product.getDelFlag());
            productGroupProductService.insert(productGroupProduct);
            productGroupProductIds.add(productGroupProduct.getId());
            log.info("添加产品{}",productGroupProduct.getId());
        }
        //产品组的产品成本价格区间更新
        productGroupProductService.updCost(productGroupProductIds);
        //产品组产品block日期添加
        groupProductBlockDateService.updBlockDate(productGroupProductIds);
    }

    /**
     * 产品组移除产品
     * @param groupDelProductReq
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor=Exception.class)
    public void groupDelProduct(GroupDelProductReq groupDelProductReq) throws Exception {
        for (Integer productGroupProductId : groupDelProductReq.getProductGroupProductIds()) {
            ProductGroupProduct productGroupProduct = productGroupProductService.selectById(productGroupProductId);
            productGroupProduct.setDelFlag(DelFlagEnums.DELETE.getCode());
            productGroupProduct.setUpdateUser(SecurityUtils.getLoginName());
            productGroupProductService.updateById(productGroupProduct);
        }
    }

    /**
     * 产品组产品编辑
     * @param groupEditProductReq
     * @return
     * @throws Exception
     */
    @Override
    public GroupEditProductReq groupEditProduct(GroupEditProductReq groupEditProductReq) throws Exception {
        ProductGroupProduct productGroupProduct = productGroupProductService.selectById(groupEditProductReq.getProductGroupProductId());
        productGroupProduct.setSort(groupEditProductReq.getSort());
        productGroupProduct.setPoint(groupEditProductReq.getPointOrTimes());
        if (!CollectionUtils.isEmpty(groupEditProductReq.getBlockRuleList())){
            productGroupProduct.setBlockRule(blockRuleService.blockRuleList2str(groupEditProductReq.getBlockRuleList()));
        }else {
            productGroupProduct.setBlockRule(null);
        }
        productGroupProductService.updateById(productGroupProduct);
        //预约支付金额处理
        if (!CollectionUtils.isEmpty(groupEditProductReq.getBookPaymentVoList())){
            bookBasePaymentService.optBookPaymentList(groupEditProductReq.getBookPaymentVoList());
        }else{
            EntityWrapper<BookBasePayment> local = new EntityWrapper<>();
            local.eq("product_group_product_id",productGroupProduct.getId() );
            bookBasePaymentService.delete(local);
        }
        //返回
        groupEditProductReq.setSort(productGroupProduct.getSort());
        groupEditProductReq.setPoint(productGroupProduct.getPoint());
        groupEditProductReq.setProductGroupProductId(productGroupProduct.getId());
        if (StringUtils.isNotBlank(productGroupProduct.getBlockRule())){
            groupEditProductReq.setBlockRuleList(blockRuleService.blockRuleStr2list(productGroupProduct.getBlockRule()));
        }
        return groupEditProductReq;
    }

    /**
     * 添加产品组block
     * @param productGroupBlockRuleVo
     * @throws Exception
     */
    @Override
    public List<BlockRule> addGroupBlock(ProductGroupBlockRuleVo productGroupBlockRuleVo) throws Exception {
        List<BlockRule> blockRuleList = Lists.newLinkedList();
        ProductGroup productGroup = productGroupMapper.selectById(productGroupBlockRuleVo.getProductGroupId());
        if (StringUtils.isNotBlank(productGroup.getBlockRule())){
            List<BlockRule> list1 = blockRuleService.blockRuleStr2list(productGroup.getBlockRule());
            blockRuleList.addAll(list1);
        }
        if (productGroupBlockRuleVo.getCalendar()!=null && productGroupBlockRuleVo.getRepeat()!=null&&(!ArrayUtils.isEmpty(productGroupBlockRuleVo.getContainWeek()) || !ArrayUtils.isEmpty(productGroupBlockRuleVo.getBlockTime()))){
            List<BlockRule> list2 = blockRuleService.generateBlockRule(productGroupBlockRuleVo);
            if (!CollectionUtils.isEmpty(list2)){
                blockRuleList.addAll(list2);
            }
        }
        if (!CollectionUtils.isEmpty(productGroupBlockRuleVo.getSpecialBlocks())){
            blockRuleList.addAll(productGroupBlockRuleVo.getSpecialBlocks());
        }
        if (!CollectionUtils.isEmpty(blockRuleList)){
            String blockRule = blockRuleService.blockRuleList2str(blockRuleList);
            productGroup.setBlockRule(blockRule);
            productGroupMapper.updateById(productGroup);
        }
        List<BlockRule> list = blockRuleService.blockRuleStr2list(productGroup.getBlockRule());
        return list;
    }

    /**
     * 编辑产品组block
     * @param productGroupBlockEditReq
     * @return
     * @throws Exception
     */
    @Override
    public List<BlockRule> editGroupBlock(ProductGroupBlockEditReq productGroupBlockEditReq) throws Exception {
        ProductGroup productGroup = productGroupMapper.selectById(productGroupBlockEditReq.getProductGroupId());
        if (CollectionUtils.isEmpty(productGroupBlockEditReq.getBlockRuleList())){
            productGroup.setBlockRule(null);
        }else {
            String blockRule = blockRuleService.blockRuleList2str(productGroupBlockEditReq.getBlockRuleList());
            productGroup.setBlockRule(blockRule);
        }
        productGroupMapper.updateById(productGroup);
        if(StringUtils.isBlank(productGroup.getBlockRule())){
            return null;
        }
        List<BlockRule> list = blockRuleService.blockRuleStr2list(productGroup.getBlockRule());
        return list;
    }

    /**
     * 查询商品下面的产品组信息
     * @param goodsId
     * @return
     * @throws Exception
     */
    @Override
    public List<GoodsGroupListRes> selectGoodsGroup(Integer goodsId) throws Exception {
        //获取商品信息
        Goods goods = goodsMapper.selectById(goodsId);
        List<GoodsGroupListRes> result = productGroupMapper.selectGoodsGroup(goodsId);
        if (!CollectionUtils.isEmpty(result)){
            packProductGroup(result, goods);
        }
        return result;
    }

    /**
     * 查询商品下面的产品组信息
     * @param groupIds
     * @return
     * @throws Exception
     */
    @Override
    public List<GoodsGroupListRes> selectGoodsGroup(List<String> groupIds) throws Exception {
        //获取商品信息
        List<GoodsGroupListRes> result = productGroupMapper.selectGoodsGroupByIds(groupIds);
        if (!CollectionUtils.isEmpty(result)){
            Goods goods = goodsMapper.selectById(result.get(0).getGoodsId());
            packProductGroup(result, goods);
        }
        return result;
    }

    private void packProductGroup(List<GoodsGroupListRes> result, Goods goods) throws Exception {
        int hour = DateUtil.hour(new Date(),true);
        if (!CollectionUtils.isEmpty(result)){
            for (GoodsGroupListRes goodsGroupListRes : result) {
                if (StringUtils.isNotBlank(goodsGroupListRes.getBlockRule())){
                    List<BlockRule> blockRuleList = blockRuleService.blockRuleStr2list(goodsGroupListRes.getBlockRule());
                    goodsGroupListRes.setBlockRuleList(blockRuleList);
                }
                //获取资源类型
                Wrapper serviceWrapper = new Wrapper() {
                    @Override
                    public String getSqlSegment() {
                        return "where del_flag = 0 and product_group_id ="+goodsGroupListRes.getId();
                    }
                };
                List<ProductGroupResource> resourceList = productGroupResourceService.selectList(serviceWrapper);
                if (!CollectionUtils.isEmpty(resourceList)){
                    for (ProductGroupResource productGroupResource : resourceList) {
                        //获取最小预约时间和最大预约时间
                        //商品中最小提前预约天数
                        Integer goodsMinBookDay = goods.getMinBookDays();
                        if (hour>=17){
                            if (goodsMinBookDay != null){
                                goodsMinBookDay = goodsMinBookDay + 1;
                            }
                        }
                        //商品中最大提前预约天数
                        Integer goodsMaxBookDay = null;
                        if (goodsMinBookDay != null && goods.getMaxBookDays() != null){
                            goodsMaxBookDay = goods.getMaxBookDays()+goodsMinBookDay;
                        }
                        //产品组中最小提前预约天数
                        Integer productGroupMinBookDay = goodsGroupListRes.getMinBookDays();
                        if (hour>=17){
                            if (productGroupMinBookDay != null){
                                productGroupMinBookDay = productGroupMinBookDay + 1;
                            }
                        }
                        //产品组中最大提前预约天数
                        Integer productGroupMaxBookDay = null;
                        if (productGroupMinBookDay != null && goodsGroupListRes.getMaxBookDays() != null){
                            productGroupMaxBookDay = goodsGroupListRes.getMaxBookDays()+productGroupMinBookDay;
                        }
                        //综合最小提前预约天数
                        Integer minBookDay = maxInteger(goodsMinBookDay,productGroupMinBookDay);
                        //综合最大提前预约天数
                        Integer maxBookDay = minInteger(goodsMaxBookDay,productGroupMaxBookDay);
                        //获取资源默认的最小最大提前预约时间
                        BookNum bookNum = BookMinMaxDayUtils.getBookByService(productGroupResource.getService(),"CN");
                        if (minBookDay == null){
                            minBookDay = bookNum.getMinBook();
                        }
                        if (maxBookDay == null){
                            maxBookDay = bookNum.getMaxBook();
                        }
                        goodsGroupListRes.setMinBookDays(minBookDay);
                        goodsGroupListRes.setMaxBookDays(maxBookDay);
                        if (ShopTypeEnums.ACCOM.getCode().equals(productGroupResource.getService())){
                            //获取住宿的权益类型
                            Wrapper giftWrapper = new Wrapper() {
                                @Override
                                public String getSqlSegment() {
                                    return "where del_flag = 0 and product_group_id ="+goodsGroupListRes.getId();
                                }
                            };
                            List<ProductGroupGift> giftList = productGroupGiftService.selectList(giftWrapper);
                            if (!CollectionUtils.isEmpty(giftList)){
                                for (ProductGroupGift productGroupGift : giftList) {
                                    if ("N".equals(productGroupGift.getGift().substring(0,1))){
                                        goodsGroupListRes.setGiftCode(productGroupGift.getGift());
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
                List<GroupProductVo> groupProductList = productService.selectByGroupId(goodsGroupListRes.getId());
                //产品组里的产品blockList组装
                if (!CollectionUtils.isEmpty(groupProductList)){
                    for (GroupProductVo groupProductVo : groupProductList) {
                        if (StringUtils.isNotBlank(groupProductVo.getBlockRule())){
                            List<BlockRule> productBlocks = blockRuleService.blockRuleStr2list(groupProductVo.getBlockRule());
                            groupProductVo.setBlockRuleList(productBlocks);
                        }
                    }
                }
                goodsGroupListRes.setGroupProductList(groupProductList);
            }
        }
    }

    /**
     * 查询商品下面的产品组信息
     * @param goodsId
     * @return
     * @throws Exception
     */
    @Override
    public List<GoodsGroupListRes> selectGoodsGroupById(Integer goodsId) throws Exception {
        List<GoodsGroupListRes> result = productGroupMapper.selectGoodsGroupById(goodsId);
        if (!CollectionUtils.isEmpty(result)){
            for (GoodsGroupListRes goodsGroupListRes : result) {
                if (StringUtils.isNotBlank(goodsGroupListRes.getBlockRule())){
                    List<BlockRule> blockRuleList = blockRuleService.blockRuleStr2list(goodsGroupListRes.getBlockRule());
                    goodsGroupListRes.setBlockRuleList(blockRuleList);
                }
                List<GroupProductVo> groupProductList = productService.selectByGroupId(goodsGroupListRes.getId());
                //产品组里的产品blockList组装
                if (!CollectionUtils.isEmpty(groupProductList)){
                    for (GroupProductVo groupProductVo : groupProductList) {
                        if (StringUtils.isNotBlank(groupProductVo.getBlockRule())){
                            List<BlockRule> productBlocks = blockRuleService.blockRuleStr2list(groupProductVo.getBlockRule());
                            groupProductVo.setBlockRuleList(productBlocks);
                        }
                    }
                }
                goodsGroupListRes.setGroupProductList(groupProductList);
            }
        }
        return result;
    }

    @Override
    public List<GoodsGroupListRes> selectGoodsGroupByGoodsId(Integer goodsId) {
        List<GoodsGroupListRes> goodsGroupListResList = productGroupMapper.selectGoodsGroupByGoodsId(goodsId);
        return goodsGroupListResList;
    }

    /**
     * 根据产品组id查询产品组下商户和规格
     * @param productGroupId
     * @return
     */
    @Override
    public List<ProductGroupResVO> selectProductGroupById(QueryProductGroupInfoReqVo reqVo) {
        return productGroupMapper.queryProductGroupById(reqVo);
    }

    /**
     * 根据产品组id查询产品组下的资源类型
     * @param productGroupId
     * @return
     * @throws Exception
     */
    @Override
    public List<SysService> selectGroupService(Integer productGroupId) throws Exception {
        return productGroupMapper.selectGroupService(productGroupId);
    }

    /**
     * 根据产品组和资源类型id查询商户列表
     * @param selectShopByGroupServiceReq
     * @return
     * @throws Exception
     */
    @Override
    public List<SelectShopByGroupServiceRes> selectShopByGroupService(SelectShopByGroupServiceReq selectShopByGroupServiceReq) throws Exception {
        List<SelectShopByGroupServiceRes> res = Lists.newLinkedList();
        List<Shop> list = productGroupMapper.selectShopByGroupService(selectShopByGroupServiceReq);
        if (!CollectionUtils.isEmpty(list)){
            for (Shop shop : list) {
                SelectShopByGroupServiceRes selectShopByGroupServiceRes = new SelectShopByGroupServiceRes();
                BeanUtils.copyProperties(shop,selectShopByGroupServiceRes);
                if (shop.getHotelId() != null){
                    Hotel hotel = hotelService.selectById(shop.getHotelId());
                    selectShopByGroupServiceRes.setHotelName(hotel.getNameCh());
                }
                ShopChannel shopChannel = shopProtocolService.selectShopChannel(shop.getId());
                selectShopByGroupServiceRes.setShopChannelId(shopChannel.getId());
                selectShopByGroupServiceRes.setShopChannel(shopChannel.getName());
                res.add(selectShopByGroupServiceRes);
            }
        }
        return res;
    }

    /**
     * 根据产品组id合资源类型查询产品组产品详情
     * @param selectShopByGroupServiceReq
     * @return
     * @throws Exception
     */
    @Override
    public List<SelectProductByGroupServiceRes> selectProductByGroupService(SelectShopByGroupServiceReq selectShopByGroupServiceReq) throws Exception {
        List<SelectProductByGroupServiceRes> list = productGroupMapper.selectProductByGroupService(selectShopByGroupServiceReq);
        if (!CollectionUtils.isEmpty(list)){
            for (SelectProductByGroupServiceRes selectProductByGroupServiceRes : list) {
                List<String> allBlock = Lists.newLinkedList();
                if (StringUtils.isNotBlank(selectProductByGroupServiceRes.getShopBlock())){
                    allBlock.add(selectProductByGroupServiceRes.getShopBlock());
                }
                if (StringUtils.isNotBlank(selectProductByGroupServiceRes.getShopItemBlock())){
                    allBlock.add(selectProductByGroupServiceRes.getShopItemBlock());
                }
                if (StringUtils.isNotBlank(selectProductByGroupServiceRes.getGoodsBlock())){
                    allBlock.add(selectProductByGroupServiceRes.getGoodsBlock());
                }
                if (StringUtils.isNotBlank(selectProductByGroupServiceRes.getGroupBlock())){
                    allBlock.add(selectProductByGroupServiceRes.getGroupBlock());
                }
                if (StringUtils.isNotBlank(selectProductByGroupServiceRes.getProductBlock())){
                    allBlock.add(selectProductByGroupServiceRes.getProductBlock());
                }
                if (!CollectionUtils.isEmpty(allBlock)){
                    List<BlockRule> blockRuleList = blockRuleService.blockRuleStr2list(StringUtils.join(allBlock,","));
                    selectProductByGroupServiceRes.setBlockRule(blockRuleList.stream().map(blockRule -> blockRule.getNatural()).collect(Collectors.joining(";")));
                }
                SelectBookPayReq selectBookPayReq = new SelectBookPayReq();
                List<Date> dates = Lists.newLinkedList();
                dates.add(new Date());
                selectBookPayReq.setBookDates(dates);
                selectBookPayReq.setProductGroupProductId(selectProductByGroupServiceRes.getProductGroupProductId());
                List<BookBasePaymentRes> bookBasePayments = bookBasePaymentService.selectBookPay(selectBookPayReq);
                if (!CollectionUtils.isEmpty(bookBasePayments)){
                    selectProductByGroupServiceRes.setBookPrice(bookBasePayments.get(0).getBookPrice());
                }
            }
        }
        return list;
    }

    /**
     * 预约时选择资源的列表查询
     * @param selectBookShopItemReq
     * @return
     * @throws Exception
     */
    @Override
    public List<SelectBookShopItemRes> selectBookShopItem(SelectBookShopItemReq selectBookShopItemReq) throws Exception {
        List<SelectBookShopItemRes> shopItemList = productGroupMapper.selectShopItemByGroupServiceShop(selectBookShopItemReq);
        return shopItemList;
    }
    /**
     * 预约时选择资源的列表查询 分页
     * @param selectBookShopItemReq
     * @return
     * @throws Exception
     */
    @Override
    public PageVo<SelectBookShopItemRes> selectBookShopItemPaging(PageVo<SelectBookShopItemReq> pageVo) throws Exception {
        PageVo<SelectBookShopItemRes> pageVoRes = new PageVo<>();
        List<SelectBookShopItemRes> shopItemList = productGroupMapper.selectShopItemByGroupServiceShopPaging(pageVo,pageVo.getCondition());
        BeanUtils.copyProperties(pageVo, pageVoRes);
        return pageVoRes.setRecords(shopItemList);
    }

    /**
     * 根据产品组id查询产品组信息
     * @param productGroupId
     * @return
     * @throws Exception
     */
    @Override
    public GoodsGroupListRes findByGroupId(Integer productGroupId) throws Exception {
        ProductGroup productGroup = productGroupMapper.selectById(productGroupId);
        GoodsGroupListRes group = new GoodsGroupListRes();
        BeanUtils.copyProperties(productGroup,group);
        List<GroupProductVo> groupProductList = productService.selectByGroupId(group.getId());
        group.setGroupProductList(groupProductList);
        return group;
    }

    @Override
    public List<ProductGroup> selectDiscountByIds(List<Integer> groups) throws Exception {
        List<ProductGroup> list = Lists.newLinkedList();
        if (!CollectionUtils.isEmpty(groups)){
            list = productGroupMapper.selectDiscountByIds(groups);
        }
        return list;
    }

    /**
     * 预约获取商户结算信息
     * @param shopSettleMsgReq
     * @return
     * @throws Exception
     */
    @Override
    public ShopSettleMsgRes shopSettleMsg(ShopSettleMsgReq shopSettleMsgReq) throws Exception {
        ShopSettleMsgRes shopSettleMsgRes = new ShopSettleMsgRes();
        shopSettleMsgRes.setBookDate(shopSettleMsgReq.getBookDate());
        //查询该商户的协议信息
        ShopProtocol shopProtocol = shopProtocolService.selectById(shopSettleMsgReq.getShopId());
        //查询商户的渠道信息
        ShopChannel rootChannel = shopChannelService.selectById(shopProtocol.getChannelId());
        //查询预约选择的渠道信息
        ShopChannel bookChannel = shopChannelService.selectById(shopSettleMsgReq.getShopChannelId());
        //选择的渠道和商户渠道相同
        if (rootChannel.getId().compareTo(bookChannel.getId()) == 0){
            //渠道资源
            if (rootChannel.getInternal().compareTo(ShopChannelEnums.CHANNEL_RESOURCE.getCode()) == 0){
                shopSettleMsgRes.setSettleMethod(rootChannel.getSettleMethod());
                shopSettleMsgRes.setCurrency(rootChannel.getCurrency());
            }
            //公司资源
            else if (rootChannel.getInternal().compareTo(ShopChannelEnums.COMPANY_RESOURCE.getCode()) == 0){
                shopSettleMsgRes.setSettleMethod(shopProtocol.getSettleMethod());
                shopSettleMsgRes.setCurrency(shopProtocol.getCurrency());
                Wrapper<ShopItemNetPriceRule> priceWrapper = new Wrapper<ShopItemNetPriceRule>() {
                    @Override
                    public String getSqlSegment() {
                        return "where del_flag = 0 and shop_item_id ="+shopSettleMsgReq.getShopItemId();
                    }
                };
                List<ShopItemNetPriceRule> priceList = shopItemNetPriceRuleService.selectList(priceWrapper);
                Wrapper<ShopItemSettlePriceRule> ruleWrapper = new Wrapper<ShopItemSettlePriceRule>() {
                    @Override
                    public String getSqlSegment() {
                        StringBuffer stringBuffer = new StringBuffer();
                        stringBuffer.append("where del_flag = 0 and shop_item_id ="+shopSettleMsgReq.getShopItemId());
                        if (StringUtils.isNotBlank(shopSettleMsgReq.getGift())){
                            stringBuffer.append(" and gift ='"+shopSettleMsgReq.getGift()+"'");
                        }else {
                            stringBuffer.append(" and (gift IS NULL OR gift = '')");
                        }
                        return stringBuffer.toString();
                    }
                };
                List<ShopItemSettlePriceRule> ruleList = shopItemSettlePriceRuleService.selectList(ruleWrapper);
                //找到对应的价格
                ShopItemNetPriceRule price = shopItemNetPriceRuleService.foundPriceByTime(shopSettleMsgReq.getBookDate(),priceList);
                if (price != null){
                    shopSettleMsgRes.setNetPrice(price.getNetPrice()+"");
                    shopSettleMsgRes.setServiceRate((price.getServiceRate() == null? null:price.getServiceRate().setScale(2,BigDecimal.ROUND_HALF_UP)) +"");
                    shopSettleMsgRes.setTaxRate((price.getTaxRate()==null?null:price.getTaxRate().setScale(2,BigDecimal.ROUND_HALF_UP))+"");
                }
                //找到对应的结算规则
                ShopItemSettlePriceRule rule = foundRuleByTime(shopSettleMsgReq.getBookDate(),ruleList);
                if (rule != null){
                    if (rule.getSettleExpressId() != null){
                        ShopItemSettleExpress shopItemSettleExpress = shopItemSettleExpressService.selectById(rule.getSettleExpressId());
                        SettleExpressTranslateVo settleExpressTranslateVo = new SettleExpressTranslateVo();
                        SettleExpressBriefVo settleExpressBriefVo = new SettleExpressBriefVo();
                        BeanUtils.copyProperties(shopItemSettleExpress,settleExpressBriefVo);
                        settleExpressTranslateVo.setExpressVo(settleExpressBriefVo);
                        settleExpressTranslateVo = shopItemSettlePriceRuleService.translateSettleExpress(settleExpressTranslateVo);
                        shopSettleMsgRes.setSettleRule(settleExpressTranslateVo.getExpressVo().getSettleExpress()+""+settleExpressTranslateVo.getExpressVo().getCustomTaxExpress());
                        BigDecimal protocolPrice = shopItemSettlePriceRuleService.calProtocolPrice(price,shopItemSettleExpress);
                        if (protocolPrice != null){
                            Integer decimal = 2;
                            if (shopProtocol.getDecimal() != null){
                                decimal = shopProtocol.getDecimal();
                            }
                            if (CarryRuleEnums.ROUND_HALF_UP.getCode().equals(shopProtocol.getRoundup())){
                                protocolPrice = protocolPrice.setScale(decimal,BigDecimal.ROUND_HALF_UP);
                            }else if (CarryRuleEnums.ROUND_UP.getCode().equals(shopProtocol.getRoundup())){
                                protocolPrice = protocolPrice.setScale(decimal,BigDecimal.ROUND_UP);
                            }else if (CarryRuleEnums.ROUND_DOWN.getCode().equals(shopProtocol.getRoundup())){
                                protocolPrice = protocolPrice.setScale(decimal,BigDecimal.ROUND_DOWN);
                            }else {
                                protocolPrice = protocolPrice.setScale(decimal,BigDecimal.ROUND_HALF_UP);
                            }
                        }
                        shopSettleMsgRes.setProtocolPrice(protocolPrice);
                    }else {
                        shopSettleMsgRes.setSettleRule("无");
                        shopSettleMsgRes.setProtocolPrice(new BigDecimal(0));
                    }
                }

            }
        }
        //选择的渠道和商户渠道不同
        else {
            //预约选择的为渠道资源
            if (bookChannel.getInternal().compareTo(ShopChannelEnums.CHANNEL_RESOURCE.getCode()) == 0){
                shopSettleMsgRes.setSettleMethod(bookChannel.getSettleMethod());
                shopSettleMsgRes.setCurrency(bookChannel.getCurrency());
            }
            //预约选择的为公司资源，此时啥都不返回，具体不会存在此种业务场景
            else if (bookChannel.getInternal().compareTo(ShopChannelEnums.COMPANY_RESOURCE.getCode()) == 0){
            }
        }
        return shopSettleMsgRes;
    }

    /**
     * 预约时间对应的结算规则
     * @param bookDate
     * @param ruleList
     * @return
     */
    @Override
    public ShopItemSettlePriceRule foundRuleByTime(Date bookDate,List<ShopItemSettlePriceRule> ruleList)throws Exception{
        ShopItemSettlePriceRule res = null;
        Integer week = DateUtils.dateForWeek(bookDate);
        if (!CollectionUtils.isEmpty(ruleList)){
            for (ShopItemSettlePriceRule shopItemSettlePriceRule : ruleList) {
                boolean dateFlag = DateUtils.belongCalendar(bookDate,shopItemSettlePriceRule.getStartDate(),shopItemSettlePriceRule.getEndDate());
                if (dateFlag){
                    if (week.compareTo(2) == 0){
                        if (shopItemSettlePriceRule.getMonday() == 1){
                            res = shopItemSettlePriceRule;
                            break;
                        }
                    }
                    if (week.compareTo(3) == 0){
                        if (shopItemSettlePriceRule.getTuesday() == 1){
                            res = shopItemSettlePriceRule;
                            break;
                        }
                    }
                    if (week.compareTo(4) == 0){
                        if (shopItemSettlePriceRule.getWednesday() == 1){
                            res = shopItemSettlePriceRule;
                            break;
                        }
                    }
                    if (week.compareTo(5) == 0){
                        if (shopItemSettlePriceRule.getThursday() == 1){
                            res = shopItemSettlePriceRule;
                            break;
                        }
                    }
                    if (week.compareTo(6) == 0){
                        if (shopItemSettlePriceRule.getFriday() == 1){
                            res = shopItemSettlePriceRule;
                            break;
                        }
                    }
                    if (week.compareTo(7) == 0){
                        if (shopItemSettlePriceRule.getSaturday() == 1){
                            res = shopItemSettlePriceRule;
                            break;
                        }
                    }
                    if (week.compareTo(1) == 0){
                        if (shopItemSettlePriceRule.getSunday() == 1){
                            res = shopItemSettlePriceRule;
                            break;
                        }
                    }
                }
            }
        }
        return res;
    }

    /**
     * 查询商品下的产品组关联信息
     *
     * @param goodsId
     * @return
     */
    @Override
    public List<ProductGroup> selectByGoodsId(Integer goodsId) {
        Wrapper<ProductGroup> localWrapper = new Wrapper<ProductGroup>() {
            @Override
            public String getSqlSegment() {
                return "where del_flag = 0 and goods_id = " + goodsId;
            }
        };
        return productGroupMapper.selectList(localWrapper);
    }

    @Override
    public void  saveSort(List<GroupProductVo> groupProductVoList) {
        productGroupProductService.saveSort(groupProductVoList);
    }

    /**
     * 获取俩个integer数据中小的数据
     * @param one
     * @param two
     * @return
     */
    public Integer minInteger(Integer one,Integer two){
        if (one == null && two != null){
            return two;
        }
        if (one != null && two == null){
            return one;
        }
        if (one != null && two != null){
            if (one.compareTo(two) > 0){
                return two;
            }else {
                return one;
            }
        }
        return null;
    }

    /**
     * 获取俩个integer数据中大的数据
     * @param one
     * @param two
     * @return
     */
    public Integer maxInteger(Integer one,Integer two){
        if (one == null && two != null){
            return two;
        }
        if (one != null && two == null){
            return one;
        }
        if (one != null && two != null){
            if (one.compareTo(two) > 0){
                return one;
            }else {
                return two;
            }
        }
        return null;
    }
}
