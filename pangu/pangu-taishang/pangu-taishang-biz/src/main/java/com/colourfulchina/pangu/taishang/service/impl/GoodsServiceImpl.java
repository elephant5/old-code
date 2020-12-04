package com.colourfulchina.pangu.taishang.service.impl;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.inf.base.enums.SysDictTypeEnums;
import com.colourfulchina.inf.base.vo.PageVo;
import com.colourfulchina.pangu.taishang.api.entity.*;
import com.colourfulchina.pangu.taishang.api.utils.GoodsUtils;
import com.colourfulchina.pangu.taishang.api.vo.req.GoodsBaseVo;
import com.colourfulchina.pangu.taishang.api.vo.req.QueryOrderExpireTimeReq;
import com.colourfulchina.pangu.taishang.mapper.GoodsMapper;
import com.colourfulchina.pangu.taishang.mapper.GoodsSettingMapper;
import com.colourfulchina.pangu.taishang.service.*;
import com.colourfulchina.tianyan.admin.api.entity.SysDict;
import com.colourfulchina.tianyan.admin.api.feign.RemoteDictService;
import com.colourfulchina.tianyan.common.core.util.R;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
@Component
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper,Goods> implements GoodsService {

    @Autowired
    GoodsMapper goodsMapper;
    @Autowired
    GoodsSettingMapper goodsSettingMapper;
    @Autowired
    GoodsChannelsService goodsChannelsService;
    @Autowired
    ProductGroupService productGroupService;

    @Autowired
    ProductGroupGiftService productGroupGiftService;
    @Autowired
    ProductGroupResourceService productGroupResourceService;

    @Autowired
    ProductGroupProductService productGroupProductService;

    @Autowired
    ProductService productService;
    @Autowired
    GoodsClauseService goodsClauseService;
    @Autowired
    GoodsDetailService goodsDetailService;

    @Autowired
    GoodsPriceService goodsPriceService;

    @Autowired
    SalesChannelService salesChannelService;
    @Autowired
    private GroupProductBlockDateService groupProductBlockDateService;
    @Autowired
    private BookBasePaymentService bookBasePaymentService;

    private RemoteDictService remoteDictService;
    /**
     * 分页查找商品
     *
     * @param pageVoReq
     * @return
     */
    @Override
    public PageVo<GoodsBaseVo> findPageList(PageVo<GoodsBaseVo> pageVoReq) {
        PageVo<GoodsBaseVo> pageVoRes = new PageVo<>();
        Map<String, Object> params = pageVoReq.getCondition() == null ?Maps.newHashMap(): pageVoReq.getCondition();
        if (params.containsKey("salesChannelIds") && params.get("salesChannelIds") != null ){
            List<Integer> salesChannelIds = (List) params.get("salesChannelIds");
            if(salesChannelIds.size() > 0){
//                for(int i = 0 ; i <  salesChannelIds.size() ; i ++ ){
//                    if(i == 0  && salesChannelIds.get(i) != null ){
//                        params.put("salesChannelId",salesChannelIds.get(i));
//                    }
//                    if(salesChannelIds.size() >= 2 && i == 1  && salesChannelIds.get(i) != null ){
//                        params.put("bankId",salesChannelIds.get(i));
//                    }
//                    if(salesChannelIds.size() == 3 && i == 2  && salesChannelIds.get(i) != null ){
//                        params.put("salesWayId",salesChannelIds.get(i));
//                    }
//                }
                params.put("salesChannelId", salesChannelIds.get(1));
                params.put("bankId", salesChannelIds.get(0));
                params.put("salesWayId", salesChannelIds.get(2));
            }
        }
        if(params.containsKey("field")){
            String field = params.get("field")+"";
            if(field.indexOf("salesPrice")>0){
                params.put("field","g.sales_price");
            }else{
                params.put("field","g."+field);
            }
        }
        if(params.containsKey("order")){
            String order = params.get("order")+"";
            if(order.indexOf("descend")>0){
                params.put("order","desc");
            }else{
                params.put("order","asc");
            }
        }
        List<SysDict> bankList = selectSysDict(SysDictTypeEnums.BANK_TYPE.getType());
        List<SysDict> salesChannelList = selectSysDict(SysDictTypeEnums.SALES_CHANNEL_TYPE.getType());
        List<SysDict> salesWayList = selectSysDict(SysDictTypeEnums.SALES_WAY_TYPE.getType());
        Map<String, SysDict> bankMap = bankList.stream().collect(Collectors.toMap(SysDict::getValue, bank -> bank));
        Map<String, SysDict> salesChannelMap = salesChannelList.stream().collect(Collectors.toMap(SysDict::getValue, bank -> bank));
        Map<String, SysDict> salesWayMap = salesWayList.stream().collect(Collectors.toMap(SysDict::getValue, bank -> bank));

        List<GoodsBaseVo> listRes = goodsMapper.findPageList(pageVoReq,params);

        for(GoodsBaseVo listRBaseVo : listRes){
            //TODO 去字典表里查询对应的数据
            SysDict sysDict1 = bankMap.get(listRBaseVo.getBankId());
            if(null  == sysDict1 ){
                listRBaseVo.setBankName("-");
            }else{
                listRBaseVo.setBankName(sysDict1.getLabel());
            }
            SysDict sysDict2 = salesChannelMap.get(listRBaseVo.getSalesChannelId());
            if(null  == sysDict2 ){
                listRBaseVo.setSalesChannelName("-");
            }else{
                listRBaseVo.setSalesChannelName(sysDict2.getLabel());
            }
            SysDict sysDict3 = salesWayMap.get(listRBaseVo.getSalesWayId());
            if(null  == sysDict3 ){
                listRBaseVo.setSalesWayName("-");
            }else{
                listRBaseVo.setSalesWayName(sysDict3.getLabel());
            }
        }
        BeanUtils.copyProperties(pageVoReq,pageVoRes);
        return pageVoRes.setRecords(listRes);
    }
    @Override
    public List<SysDict> selectSysDict(String type)  {
        R<List<SysDict>> resultVO = remoteDictService.findDictByType(type);
        return resultVO.getData();
    }

    @Override
    public void insertGoodsAndSetting(GoodsBaseVo goods) {
        Goods insertGoods = new Goods();
        BeanUtils.copyProperties(goods,insertGoods);
        insertGoods.setCreateTime(new Date());
        insertGoods.setUpdateTime(new Date());
        insertGoods.setStatus("0");
        if(!CollectionUtils.isEmpty(goods.getBlockRuleList())){
            List<String> blockList =goods.getBlockRuleList().stream().map(block -> block.getRule()).collect(Collectors.toList());
            String block  = Joiner.on(",").join(blockList);
            insertGoods.setBlock(block);
        }
        if(StringUtils.isBlank(goods.getHotline())){
            goods.setHotline("400-921-6918");
        }
        Integer goodsInsert=goodsMapper.insert(insertGoods);
        Assert.notNull(goodsInsert,"商品保存失败 null");
        //如果goods.id不为空  则把insertGoods.id更新为goods.id
        if (goods.getGoodsId() != null){
            goods.setId(insertGoods.getId());
            goodsMapper.updateIdByGoodsId(goods);
            insertGoods.setId(goods.getGoodsId());
        }
        Assert.isTrue(goodsInsert.compareTo(1)==0,"商品保存失败 not 1");
        log.info(insertGoods.getId().toString());
        GoodsSetting goodsSetting = new GoodsSetting();
        BeanUtils.copyProperties(goods,goodsSetting);
        goodsSetting.setGoodsId(insertGoods.getId());
        goodsSetting.setAllYear(goodsSetting.getAllYear() == "true"?"1":"0");
        goodsSetting.setSuperposition(goodsSetting.getSuperposition() == "true"?"1":"0");
        goodsSetting.setSingleThread(goodsSetting.getSingleThread() == "true"?"1":"0");
        goodsSetting.setEnableMaxNight(goodsSetting.getEnableMaxNight() == "true"?"1":"0");
        goodsSetting.setEnableMinNight(goodsSetting.getEnableMinNight() == "true"?"1":"0");
        goodsSetting.setDisableCall(goodsSetting.getDisableCall() == "true"?"1":"0");
        goodsSetting.setDisableWechat(goodsSetting.getDisableWechat() == "true"?"1":"0");
        goodsSetting.setOnlySelf(goodsSetting.getOnlySelf()== "true"?"1":"0");
        Integer settingInsert=goodsSettingMapper.insert(goodsSetting);
        Assert.notNull(settingInsert,"商品设置保存失败 null");
        Assert.isTrue(settingInsert.compareTo(1)==0,"商品设置保存失败 not 1");
        goods.setId(insertGoods.getId());
    }

    @Override
    public void insertSyncGoodsCode(Integer projectId, int targetGoodsId, Integer id,Integer salesChannelId, Integer charlieChannel) {
        Map<String,Integer> map = new HashMap<>();
        map.put("projectId",projectId);
        map.put("targetGoodsId",targetGoodsId);
        map.put("id",id);
        map.put("salesChannelId",salesChannelId);
        map.put("charlieChannel",charlieChannel);
        goodsMapper.insertSyncGoodsCode(map);
    }

    /**
     * 商品数据copy
     *
     * @param goodsId
     */
    @Override
//    @Async("executorPool")
    public void copyGoodsDate(Goods newGoods,Integer goodsId) {
        //渠道复制
        List<GoodsChannels> goodsChannelsList  = goodsChannelsService.selectGoodsChannelsByGoodsId(goodsId);

        if(goodsChannelsList.size() > 0 ){
            for(GoodsChannels goodsChannels : goodsChannelsList){
                goodsChannels.setGoodsId(newGoods.getId());
                goodsChannels.setCreateTime(new Date());
                goodsChannels.setUpdateTime(new Date());
                goodsChannelsService.insert(goodsChannels);
            }
        }

        //使用条款复制goods_clause
        List<GoodsClause> goodsClauses = goodsClauseService.selectByGoodsId(goodsId);
        if(goodsClauses.size() > 0 ){
            for(GoodsClause goodsClause : goodsClauses){
                goodsClause.setId(null);
                goodsClause.setGoodsId(newGoods.getId());
                goodsClause.setCreateTime(new Date());
                goodsClause.setUpdateTime(new Date());
                goodsClause.setGoodsId(newGoods.getId());
                goodsClauseService.insert(goodsClause);
            }
        }

        //商品复制goods_detail
        List<GoodsDetail> goodsDetails = goodsDetailService.selectByGoodsId(goodsId);
        if(goodsDetails.size() > 0 ){
            for(GoodsDetail goodsDetail : goodsDetails){
                goodsDetail.setGoodsId(newGoods.getId());
                goodsDetail.setCreateTime(new Date());
                goodsDetail.setUpdateTime(new Date());
                goodsDetail.setGoodsId(newGoods.getId());
                goodsDetailService.insert(goodsDetail);
            }
        }
        //商品价格goods_price
        List<GoodsPrice> goodsPrices = goodsPriceService.selectByGoodsId(goodsId);
        if(goodsPrices.size() > 0 ){
            for(GoodsPrice  goodsPrice: goodsPrices){
                goodsPrice.setId(null);
                goodsPrice.setGoodsId(newGoods.getId());
                goodsPrice.setCreateTime(new Date());
                goodsPrice.setUpdateTime(new Date());
                goodsPrice.setGoodsId(newGoods.getId());
                goodsPriceService.insert(goodsPrice);
            }
        }
    }
    @Async("taskExecutorPool")
    public void copyGoodsAndProductDate(Goods newGoods,Integer goodsId)throws Exception{
        List<ProductGroup> productGroupsList =productGroupService.selectByGoodsId(goodsId);
        if(productGroupsList.size() > 0 ){
            for (ProductGroup productGroup : productGroupsList ){
                Integer oldproductGroup = productGroup.getId();
                ProductGroup newProductGroup = new ProductGroup();
                BeanUtils.copyProperties(productGroup,newProductGroup);
                newProductGroup.setGoodsId(newGoods.getId());
                newProductGroup.setId(null);
                newProductGroup.setCreateTime(new Date());
                newProductGroup.setUpdateTime(new Date());
                productGroupService.insert(newProductGroup);

                List<ProductGroupGift> productGroupGifts = productGroupGiftService.selectByProductGroupId(oldproductGroup);
                if(productGroupGifts.size() > 0 ){
                    for(ProductGroupGift productGroupGift : productGroupGifts){
                        ProductGroupGift newProductGroupGift = new ProductGroupGift();
                        BeanUtils.copyProperties(productGroupGift,newProductGroupGift);
                        newProductGroupGift.setId(null);
                        newProductGroupGift.setProductGroupId(newProductGroup.getId());
                        productGroupGiftService.insert(newProductGroupGift);
                    }
                }
                List<ProductGroupResource> productGroupResources = productGroupResourceService.selectByProductGroupId(oldproductGroup);
                if(productGroupResources.size() > 0 ){
                    for(ProductGroupResource productGroupResource : productGroupResources){
                        ProductGroupResource newProductGroupResource = new ProductGroupResource();
                        BeanUtils.copyProperties(productGroupResource,newProductGroupResource);
                        newProductGroupResource.setId(null);
                        newProductGroupResource.setProductGroupId(newProductGroup.getId());
                        newProductGroupResource.setCreateTime(new Date());
                        newProductGroupResource.setUpdateTime(new Date());
                        newProductGroupResource.setGoodsId(newGoods.getId());
                        productGroupResourceService.insert(newProductGroupResource);
                    }
                }
                List<ProductGroupProduct> productGroupProducts = productGroupProductService.selectByProductGroupId(oldproductGroup);
                if(productGroupProducts.size() > 0 ){
//                    Map<Integer,ProductGroupProduct> productMaps = productGroupProducts.stream().collect(Collectors.toMap(ProductGroupProduct::getProductId, bank -> bank));
//                    List<Product> productList = productService.selectBatchIds(idList);
//                    Map<Integer,Product> productMaps = Maps.newHashMap();
//                    for(Product product : productList){
////                        Product newProduct =new Product();
////                        BeanUtils.copyProperties(product,newProduct);
////                        newProduct.setCreateTime(new Date());
////                        newProduct.setUpdateTime(new Date());
////                        newProduct.setId(null);
////                        productService.insert(newProduct);
////                        productMaps.put(product.getId(),newProduct);
////                    }
////                    List<ProductGroupProduct> newProductGroupProducts = Lists.newArrayList();
                    List<Integer> ids = Lists.newLinkedList();
                    for(ProductGroupProduct productGroupProduct : productGroupProducts){
                        Integer oldProductGroupProductId = productGroupProduct.getId();
                        ProductGroupProduct  newProductGroupProduct  =new ProductGroupProduct();
                        BeanUtils.copyProperties(productGroupProduct,newProductGroupProduct);
//                        if(productMaps.containsKey(productGroupProduct.getProductId())){
//                            Product newProduct =productMaps.get(productGroupProduct.getProductId());
//                            newProductGroupProduct.setProductId(newProduct.getId());
//                        }
                        newProductGroupProduct.setProductId(productGroupProduct.getProductId());
                        newProductGroupProduct.setId(null);
                        newProductGroupProduct.setCreateTime(new Date());
                        newProductGroupProduct.setUpdateTime(new Date());
                        newProductGroupProduct.setProductGroupId(newProductGroup.getId());
//                        newProductGroupProducts.add(productGroupProduct);
                        productGroupProductService.insert(newProductGroupProduct);
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
                                bookBasePayment.setProductGroupProductId(newProductGroupProduct.getId());
                                bookBasePaymentService.insert(bookBasePayment);
                            }
                        }
                        ids.add(newProductGroupProduct.getId());
                    }
                    //产品组产品block日期添加
                    if (!CollectionUtils.isEmpty(ids)){
                        groupProductBlockDateService.updBlockDate(ids);
                    }
                }
            }

        }
    }

    /**
     * 增加商品的其他数据
     *
     * @param goods
     */
    @Override
    public void insertOrUpdateAnotherData(GoodsBaseVo goods){
        this.insertOrUpdateAnotherData(goods,null);
    }
    /**
     * 增加商品的其他数据
     *
     * @param goods
     * @param channelId
     */
    @Override
    public void insertOrUpdateAnotherData(GoodsBaseVo goods,Integer channelId) {

        SalesChannel salesChannel = null;
        if (channelId == null){
            salesChannel=salesChannelService.selectOneDate(goods.getSalesChannelIds().get(0),goods.getSalesChannelIds().get(1) ,goods.getSalesChannelIds().get(2));
        }else {
            salesChannel=salesChannelService.selectById(channelId);
        }

        List<GoodsChannels> goodsChannelsList = goodsChannelsService.selectGoodsChannelsByGoodsId(goods.getId());
        if(CollectionUtils.isEmpty(goodsChannelsList) ){
            GoodsChannels goodsChannels = new GoodsChannels();
            goodsChannels.setGoodsId(goods.getId());
            goodsChannels.setChannelId(salesChannel.getId());
            goodsChannels.setCreateTime(new Date());
            goodsChannels.setUpdateTime(new Date());
            goodsChannelsService.insert(goodsChannels);
        }else{
            GoodsChannels goodsChannels = goodsChannelsList.get(0);
            goodsChannels.setChannelId(salesChannel.getId());
            goodsChannelsService.updateById(goodsChannels);
        }

        List<GoodsDetail> goodsDetails  = goodsDetailService.selectByGoodsId(goods.getId());
        GoodsDetail goodsDetail = goodsDetails.size() == 0 ? null : goodsDetails.get(0);
        if(null == goodsDetail) {
            goodsDetail = new GoodsDetail();
            goodsDetail.setGoodsId(goods.getId());
            goodsDetail.setDetail(goods.getDetail());
            goodsDetailService.insert(goodsDetail);
        }else {
            goodsDetail.setDetail(goods.getDetail());
            goodsDetailService.updateById(goodsDetail);
        }


//        List<GoodsClause> goodsClauseList = goodsClauseService.selectByGoodsId(goods.getId());
////        GoodsClause goodsClause = goodsClauseList.size() == 0 ? null : goodsClauseList.get(0);
//        if(goodsClauseList.size() > 0 ) {
//            goodsClauseService.deleteByGoodsId(goods.getId());
//
//        }
//        if(goods.getGoodsClauseList().size() > 0 ){
//            goodsClauseService.insertBatch(goods.getGoodsClauseList());
//        }

//        List<GoodsPrice> goodsPrices = goodsPriceService.selectByGoodsId(goods.getId());
//
////        Map<String, GoodsPrice> map = goodsPrices.stream().collect(Collectors.toMap(GoodsPrice::getLevel, price -> price));
////        goodsPrices.stream().collect(Collectors.toMap(GoodsPrice::getId, a -> a));
//        if( goodsPrices.size() > 0 ){
//            goodsPriceService.deleteByGoodsId(goods.getId());
//        }
//        if(goods.getGoodsPriceList().size() > 0 ){
//            goodsPriceService.insertBatch(goods.getGoodsPriceList());
//        }
    }

    @Override
    public List<Goods> selectNameByIds(List<Integer> ids) {
        List<Goods> list = goodsMapper.selectNameByIds(ids);
        return list;
    }

    @Override
    public List<String> getGoodsGiftType(String type) {
        return null;
    }

    /**
     * 查询客户权益的到期时间
     * @param queryOrderExpireTimeReq
     * @return
     * @throws Exception
     */
    @Override
    public GoodsBaseVo queryOrderExpireTime(QueryOrderExpireTimeReq queryOrderExpireTimeReq) throws Exception {
        GoodsBaseVo goodsBaseVo = new GoodsBaseVo();
        Goods goods = goodsMapper.selectById(queryOrderExpireTimeReq.getGoodsId());
        BeanUtils.copyProperties(goods,goodsBaseVo);
        goodsBaseVo = GoodsUtils.analyCodeDate(goodsBaseVo,queryOrderExpireTimeReq.getActivationDate(),queryOrderExpireTimeReq.getOutDate());
        return goodsBaseVo;
    }
}