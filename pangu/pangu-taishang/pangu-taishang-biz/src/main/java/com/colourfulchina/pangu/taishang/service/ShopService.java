package com.colourfulchina.pangu.taishang.service;

import com.baomidou.mybatisplus.service.IService;
import com.colourfulchina.inf.base.vo.PageVo;
import com.colourfulchina.pangu.taishang.api.entity.Shop;
import com.colourfulchina.pangu.taishang.api.vo.OldShopMoreVo;
import com.colourfulchina.pangu.taishang.api.vo.RepeatInfoVo;
import com.colourfulchina.pangu.taishang.api.vo.ShopBaseMsgVo;
import com.colourfulchina.pangu.taishang.api.vo.ShopVo;
import com.colourfulchina.pangu.taishang.api.vo.req.ShopOrderDetailReq;
import com.colourfulchina.pangu.taishang.api.vo.req.ShopPageListReq;
import com.colourfulchina.pangu.taishang.api.vo.res.HotelShopListRes;
import com.colourfulchina.pangu.taishang.api.vo.res.ShopListRes;
import com.colourfulchina.pangu.taishang.api.vo.res.ShopOrderDetailRes;
import com.colourfulchina.pangu.taishang.api.vo.res.ShopPageListRes;
import com.colourfulchina.pangu.taishang.api.vo.res.bigan.ShopDetailVo;

import java.util.List;

public interface ShopService extends IService<Shop> {

    /**
     * 商户列表模糊分页查询
     * @param pageVoReq
     * @return
     */
    PageVo<ShopPageListRes> findPageList(PageVo<ShopPageListReq> pageVoReq);

    /**
     * 查询所有商户列表
     * @return
     */
    List<ShopListRes> selectShopList();

    /**
     * 查询商户基本信息
     * @param shopId
     * @return
     */
    ShopBaseMsgVo selectShopBaseMsg(Integer shopId);

    /**
     * 根据商户id查询商户vo
     * @param shopId
     * @return
     */
    ShopVo selectShopVoById(Integer shopId);

    /**
     * 酒店关联商户查询
     * @param hotelId
     * @return
     */
    List<HotelShopListRes> hotelsShop(Integer hotelId);

    /**
     * 解析老系统中shop表的more字段
     * @param more
     * @return
     */
     OldShopMoreVo anslyisOldShopMore(String more);

    /**
     * 解析老系统shop表中block字段
     * @param block
     * @return
     */
     String anslyisOldShopBlock(String block);

    /**
     * 根据老系统商户id查询新系统商户id
     * @param oldId
     * @return
     */
     Shop selectByOldId(Integer oldId);

    /**
     * 根据酒店名称和商户名称查询商户
     * @param shopName
     * @param hotelName
     * @return
     */
     Shop checkShopByHotelAndName(String shopName, String hotelName);

    /**
     * 新增商户
     * @param shop
     * @return
     */
     Shop addShop(Shop shop);

    /**
     * 商户基础信息添加更新
     * @param shop
     * @return
     */
     Shop updShopBaseMsg(Shop shop);

     /**
      * @title:selectShopOrderDetail
      * @Description: 获取商户详情
      * @Param: [shopOrderDetailReq]
      * @return: com.colourfulchina.pangu.taishang.api.vo.res.ShopOrderDetailRes
      * @Auther: 图南
      * @Date: 2019/6/18 17:19
      */
    ShopOrderDetailRes selectShopOrderDetail(ShopOrderDetailReq shopOrderDetailReq);

    /**
     * @title:getShopInfoByGoodsId
     * @Description: 根据goods获取商品详情信息
     * @Param: [goodsId]
     * @return: com.colourfulchina.pangu.taishang.api.entity.Shop
     * @Auther: 图南
     * @Date: 2019/6/18 17:39
     */
    Shop getShopInfoByGoodsId(Integer goodsId);

    List<ShopListRes> selectShopListByName(ShopListRes params);

    /**
     * 模糊查询所有商户
     * @param params
     * @return
     */
    List<ShopListRes> seachShopListByName(ShopListRes params);

    /**
     * 获取重复的规格数据
     * @return
     */
    List<RepeatInfoVo> getRepeatInfoVo();

    /**
     * 生成定位信息
     */
    void generateGeo() throws Exception;

    ShopDetailVo getShopDetail(Long id);

    /**
     * 商户预约成功销售次数增加
     * @param shopId
     * @return
     * @throws Exception
     */
    Boolean shopSalesUp(Integer shopId)throws Exception;

    /**
     * 商户查看点击次数增加
     * @param shopId
     * @return
     * @throws Exception
     */
    Boolean shopPointUp(Integer shopId)throws Exception;
}