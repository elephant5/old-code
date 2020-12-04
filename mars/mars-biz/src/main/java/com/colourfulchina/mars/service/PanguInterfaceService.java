package com.colourfulchina.mars.service;

import com.colourfulchina.pangu.taishang.api.entity.*;
import com.colourfulchina.pangu.taishang.api.vo.ShopBaseMsgVo;
import com.colourfulchina.pangu.taishang.api.vo.req.GoodsBaseVo;
import com.colourfulchina.pangu.taishang.api.vo.req.SelectBookPayReq;
import com.colourfulchina.pangu.taishang.api.vo.req.SelectShopByGroupServiceReq;
import com.colourfulchina.pangu.taishang.api.vo.req.ShopSettleMsgReq;
import com.colourfulchina.pangu.taishang.api.vo.res.*;
import com.colourfulchina.pangu.taishang.api.vo.res.bookBasePayment.BookBasePaymentRes;
import com.colourfulchina.pangu.taishang.api.vo.res.shopItem.ShopItemConciseRes;
import com.colourfulchina.pangu.taishang.api.vo.res.shopProtocol.ShopProtocolRes;

import java.util.List;

public interface PanguInterfaceService {


    /**
     * 商户详情
     * @param shopId
     * @return
     */
    ShopDetailRes getShopDetail(Integer shopId);
    /**
     * 根据商品ID查找
     * @param id
     * @return
     */
    GoodsBaseVo selectGoodsById(Integer id);

    /**
     * 根据商户id查询商户，返回实体类
     * @param id
     * @return
     */
    Goods findGoodsById(Integer id);

    /**
     * 查找商户协议信息
     * @param shopId
     * @return
     */
    public ShopProtocolRes selectShopProtocol(Integer shopId);
    /**
     * 查询商品下面的产品组信息
     * @param goodsId
     * @return
     */
    List<GoodsGroupListRes> selectGoodsGroup( Integer goodsId);

    /**
     * 根据ids查询产品组信息
     * @param groupIds
     * @return
     */
    List<GoodsGroupListRes> selectGoodsGroupByIds( String groupIds);

    /**
     * 查询产品组的资源类型
     * @param productGroupId
     * @return
     */
    List<SysService> selectGroupService( Integer productGroupId);

    /**
     * 根据商品ids查询商品名称
     * @param ids
     * @return
     */
    List<Goods> selectGoodsNameByIds(List<Integer> ids);

    /**
     * 查询商品的销售渠道
     * @param goodsId
     * @return
     */
    List<GoodsChannelRes> selectGoodsChannel(Integer goodsId);

    /**
     * 根据大客户、销售渠道销售方式查询salesChannel
     * @param salesChannel
     * @return
     */
    List<SalesChannel> selectByBCW(SalesChannel salesChannel);

    List<SalesChannel> selectByBankIds(SalesChannel salesChannel);

    /**
     * 根据销售渠道id查询销售渠道详情
     * @param id
     * @return
     */
    GoodsChannelRes findChannelById(Integer id);

    /**
     * 根据商品id查询商品预约限制
     * @param id
     * @return
     */
    GoodsSetting selectGoodsSettingById(Integer id);


    List<ShopBaseMsgVo> shopDetailList(List<Integer> shopIds);


    ShopSettleMsgRes shopSettleMsg(ShopSettleMsgReq shopSettleMsgReq);


    GroupProductDetailRes selectProductDetail(Integer productGroupProductId);


    ShopItemRes getShopItemById(Integer id);

    Hotel selectHotelByShopId(Integer shopId);


    List<GoodsBaseVo> selectGoodsByIds( List<Integer> goodsIds);

    List<SelectProductByGroupServiceRes> selectProductByGroupService(SelectShopByGroupServiceReq selectShopByGroupServiceReq);

    /**
     * 查询预约支付金额
     * @param selectBookPayReq
     * @return
     */
    List<BookBasePaymentRes> selectBookPay(SelectBookPayReq selectBookPayReq);

    /**
     * 查询商品列表
     * @return
     */
    List<Goods> selectGoodsList();

    /**
     * 查询所有销售渠道列表详情
     * @return
     */
    List<GoodsChannelRes> selectSalesChannelList();

    /**
     * 查询所有商户渠道
     * @return
     */
    List<ShopChannel> selectChannelList();

    /**
     * 查询产品组产品的价格
     * @param productGroupProduct
     * @return
     */
    List<ShopItemNetPriceRule> selectProductPrices(Integer productGroupProduct);

    /**
     * 查询产品
     * @param product
     * @return
     */
    Product getProductByShop(Product product);

    /**
     * 根据shopitemids 查询
     * @param items
     * @return
     */
    List<ShopItemConciseRes> selectByItems(List<Integer> items);
    
    /**
     * 根据商品ID查找
     * @return
     */
    public GoodsBaseVo getGoodsBaseById(Integer goodsId);


    /**
     * 根据产品ID查找
     * @param id
     * @return
     */
    ProductGroupProduct findProductGroupProductById(Integer id);

    /**
     * @title:selectBookPayList
     * @Description: 批量查询预约支付金额
     * @Param: [productGroupProductIdList]
     * @return: java.util.List<com.colourfulchina.pangu.taishang.api.vo.res.bookBasePayment.BookBasePaymentRes>
     * @Auther: 图南
     * @Date: 2019/9/26 16:09
     */
    List<BookBasePaymentRes> selectBookPayList(List<Integer> productGroupProductIdList);
}
