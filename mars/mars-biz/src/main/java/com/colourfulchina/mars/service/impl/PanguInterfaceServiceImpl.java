package com.colourfulchina.mars.service.impl;

import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.mars.service.PanguInterfaceService;
import com.colourfulchina.pangu.taishang.api.entity.*;
import com.colourfulchina.pangu.taishang.api.feign.*;
import com.colourfulchina.pangu.taishang.api.vo.ShopBaseMsgVo;
import com.colourfulchina.pangu.taishang.api.vo.req.GoodsBaseVo;
import com.colourfulchina.pangu.taishang.api.vo.req.SelectBookPayReq;
import com.colourfulchina.pangu.taishang.api.vo.req.SelectShopByGroupServiceReq;
import com.colourfulchina.pangu.taishang.api.vo.req.ShopSettleMsgReq;
import com.colourfulchina.pangu.taishang.api.vo.res.*;
import com.colourfulchina.pangu.taishang.api.vo.res.bookBasePayment.BookBasePaymentRes;
import com.colourfulchina.pangu.taishang.api.vo.res.shopItem.ShopItemConciseRes;
import com.colourfulchina.pangu.taishang.api.vo.res.shopProtocol.ShopProtocolRes;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 写盘古的接口调用基础类
 */
@Service
@Slf4j
@AllArgsConstructor
public class PanguInterfaceServiceImpl  implements PanguInterfaceService {

      private  RemoteGoodsService remoteGoodsService;

      private RemoteProductGroupService remoteProductGroupService;

      private RemoteShopService remoteShopService;

      private RemoteProductGroupProductService remoteProductGroupProductService;

       private RemoteHotelService remoteHotelService;

   public ShopDetailRes  getShopDetail( Integer shopId){
//        CommonResultVo<ShopDetailRes> shopVo =null;
//        Assert.isTrue(shopVo != null && shopVo.getCode() == 100, "根据id获取商户信息失败"+shopId);
        CommonResultVo<ShopDetailRes> result = null;
        while (true){
            try {
                result =   remoteShopService.shopDetail(shopId);
                if(result == null){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }else{
                    break;
                }
            } catch (Exception e) {
                log.error(e.getMessage(),e);
            }
        }
        return result.getResult() == null?null: result.getResult();
//        return shopVo.getResult();
    }


    /**
     * 根据商品ID查找
     * @param id
     * @return
     */
    public GoodsBaseVo selectGoodsById( Integer id){
        CommonResultVo<GoodsBaseVo> productVo = remoteGoodsService.selectById(id);
        Assert.isTrue(productVo != null && productVo.getCode() == 100, "根据id获取商品信息失败");
        return productVo.getResult();
    }

    @Override
    public ShopProtocolRes selectShopProtocol( Integer shopId){
        CommonResultVo<List<ShopProtocolRes>> ShopProtocolRes = remoteHotelService.selectShopProtocol(shopId);
        Assert.isTrue(ShopProtocolRes != null && ShopProtocolRes.getCode() == 100, "根据id获取商品信息失败");
        List<ShopProtocolRes> list  = ShopProtocolRes.getResult();
        return list.size() == 0 ? null: list.get(0);
    }


    @Override
    public Goods findGoodsById(Integer id) {
        return remoteGoodsService.findGoodsById(id).getResult();
    }

    public List<Goods> selectNameByIds(List<Integer> ids ){
        return remoteGoodsService.selectNameByIds(ids).getResult();
    }

    /**
     * 查询商品下面的产品组信息
     *
     * @param goodsId
     * @return
     */
    @Override
    public List<GoodsGroupListRes> selectGoodsGroup(Integer goodsId) {
        return remoteProductGroupService.selectGoodsGroup(goodsId).getResult();
    }

    /**
     * 根据ids查询产品组信息
     *
     * @param groupIds
     * @return
     */
    @Override
    public List<GoodsGroupListRes> selectGoodsGroupByIds(String groupIds) {
        return remoteProductGroupService.selectGoodsGroupByIds(groupIds).getResult();
    }

    /**
     * 查询产品组的资源类型
     * @param productGroupId
     * @return
     */
    @Override
    public List<SysService> selectGroupService(Integer productGroupId) {
        return remoteProductGroupService.selectGroupService(productGroupId).getResult();
    }

    /**
     * 根据商品ids列表查询商品名称
     * @param ids
     * @return
     */
    @Override
    public List<Goods> selectGoodsNameByIds(List<Integer> ids) {
        return remoteGoodsService.selectNameByIds(ids).getResult();
    }

    /**
     * 查询商品的销售渠道
     * @param goodsId
     * @return
     */
    @Override
    public List<GoodsChannelRes> selectGoodsChannel(Integer goodsId) {
        return remoteGoodsService.selectGoodsChannel(goodsId).getResult();
    }

    /**
     * 根据大客户、销售渠道销售方式查询salesChannel
     * @param salesChannel
     * @return
     */
    @Override
    public List<SalesChannel> selectByBCW(SalesChannel salesChannel) {
        return remoteGoodsService.selectByBCW(salesChannel).getResult();
    }
    /**
     * 根据大客户、销售渠道销售方式查询salesChannel
     * @param salesChannel
     * @return
     */
    @Override
    public List<SalesChannel> selectByBankIds(SalesChannel salesChannel) {
        CommonResultVo<List<SalesChannel>>  result  =  remoteGoodsService.selectByBankIds(salesChannel);
        return result == null ? null : result.getResult();
    }
    /**
     * 根据销售渠道id查询销售渠道详情
     * @param id
     * @return
     */
    @Override
    public GoodsChannelRes findChannelById(Integer id) {
        return remoteGoodsService.findById(id).getResult();
    }

    /**
     * 根据商品id查询商品预约限制
     * @param id
     * @return
     */
    @Override
    public GoodsSetting selectGoodsSettingById(Integer id) {
        return remoteGoodsService.selectGoodsSettingById(id).getResult();
    }

    @Override
    public List<ShopBaseMsgVo> shopDetailList(List<Integer> shopIds) {
        List<ShopDetailRes> result =  remoteShopService.shopDetailList(shopIds).getResult();
        List<ShopBaseMsgVo> shopList  = Lists.newArrayList();
        for(ShopDetailRes vo : result){
            shopList.add(vo.getShop());
        }
        return shopList;
    }

    public ShopSettleMsgRes shopSettleMsg( ShopSettleMsgReq shopSettleMsgReq){

        return remoteProductGroupService.shopSettleMsg(shopSettleMsgReq).getResult();
    }

    @Override
    public GroupProductDetailRes selectProductDetail(Integer productGroupProductId){
        CommonResultVo<GroupProductDetailRes> result  = remoteProductGroupProductService.selectProductDetail(productGroupProductId);
        Assert.isTrue(result != null && result.getCode() == 100, "根据id获取信息失败");
        return result.getResult();
    }


    public ShopItemRes getShopItemById(Integer id){

        return remoteShopService.get(id).getResult();
    }

    @Override
    public Hotel selectHotelByShopId(Integer shopId){
        CommonResultVo<Hotel> result  = remoteShopService.selectHotelByShopId(shopId);
        Assert.isTrue(result != null && result.getCode() == 100, "根据id获取酒店信息失败");
        return result.getResult();
    }

    @Override
    public List<GoodsBaseVo> selectGoodsByIds(List<Integer> goodsIds) {
        CommonResultVo<List<GoodsBaseVo>> result  = remoteGoodsService.selectGoodsByIds(goodsIds);
        Assert.isTrue(result != null && result.getCode() == 100, "根据id获取酒店信息失败");
        return result.getResult();
    }

    @Override
    public List<SelectProductByGroupServiceRes> selectProductByGroupService(SelectShopByGroupServiceReq selectShopByGroupServiceReq) {
        CommonResultVo<List<SelectProductByGroupServiceRes>> result = remoteProductGroupService.selectProductByGroupService(selectShopByGroupServiceReq);
        return result.getResult();
    }

    @Override
    public List<BookBasePaymentRes> selectBookPay(SelectBookPayReq selectBookPayReq) {
        CommonResultVo<List<BookBasePaymentRes>> result = remoteProductGroupProductService.selectBookPay(selectBookPayReq);
        return result.getResult();
    }

    @Override
    public List<Goods> selectGoodsList() {
        CommonResultVo<List<Goods>> result = remoteGoodsService.selectGoodsList();
        return result.getResult();
    }

    @Override
    public List<GoodsChannelRes> selectSalesChannelList() {
        CommonResultVo<List<GoodsChannelRes>> result = remoteGoodsService.selectSalesChannelList();
        return result.getResult();
    }

    @Override
    public List<ShopChannel> selectChannelList() {
        CommonResultVo<List<ShopChannel>> result = remoteShopService.selectChannelList();
        return result.getResult();
    }

    @Override
    public List<ShopItemNetPriceRule> selectProductPrices(Integer productGroupProduct) {
        CommonResultVo<List<ShopItemNetPriceRule>> result = remoteProductGroupProductService.selectProductPrices(productGroupProduct);
        return result.getResult();
    }

    @Override
    public List<ShopItemConciseRes> selectByItems(List<Integer> items) {
        CommonResultVo<List<ShopItemConciseRes>> result = remoteShopService.selectByItems(items);
        return result.getResult();
    }

    /**
     * 查询产品
     *
     * @param product
     * @return
     */
    @Override
    public Product getProductByShop(Product product) {
        CommonResultVo<Product> result = remoteProductGroupService.getProductByShop(product);
        return result.getResult();
    }

	@Override
	public GoodsBaseVo getGoodsBaseById(Integer id) {
        CommonResultVo<GoodsBaseVo> result  = remoteGoodsService.getGoodsBaseById(id);
        Assert.isTrue(result != null && result.getCode() == 100, "根据id获取酒店信息失败");
        return result.getResult();
	}



    @Override
    public ProductGroupProduct findProductGroupProductById(Integer id) {
        final CommonResultVo<ProductGroupProduct> resultVo = remoteProductGroupProductService.findById(id);
        Assert.notNull(resultVo,"查询产品信息失败");
        Assert.isTrue(resultVo.getCode()==100,"查询产品信息失败");
        Assert.notNull(resultVo.getResult(),"未查到产品信息"+id);
        return resultVo.getResult();
    }

    @Override
    public List<BookBasePaymentRes> selectBookPayList(List<Integer> productGroupProductIdList) {
        CommonResultVo<List<BookBasePaymentRes>> result = remoteProductGroupProductService.selectBookPayList(productGroupProductIdList);
        return result.getResult();
    }
}
