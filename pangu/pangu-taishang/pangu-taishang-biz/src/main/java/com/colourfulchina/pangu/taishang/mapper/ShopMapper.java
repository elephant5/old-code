package com.colourfulchina.pangu.taishang.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.colourfulchina.inf.base.vo.PageVo;
import com.colourfulchina.pangu.taishang.api.entity.Shop;
import com.colourfulchina.pangu.taishang.api.vo.RepeatInfoVo;
import com.colourfulchina.pangu.taishang.api.vo.ShopBaseMsgVo;
import com.colourfulchina.pangu.taishang.api.vo.ShopVo;
import com.colourfulchina.pangu.taishang.api.vo.req.ShopOrderDetailReq;
import com.colourfulchina.pangu.taishang.api.vo.req.ShopPageListReq;
import com.colourfulchina.pangu.taishang.api.vo.res.HotelShopListRes;
import com.colourfulchina.pangu.taishang.api.vo.res.ShopListRes;
import com.colourfulchina.pangu.taishang.api.vo.res.ShopOrderDetailRes;
import com.colourfulchina.pangu.taishang.api.vo.res.ShopPageListRes;

import java.util.List;
import java.util.Map;

public interface ShopMapper extends BaseMapper<Shop> {
    /**
     * 酒店关联商户查询
     * @param hotelId
     * @return
     */
    List<HotelShopListRes> hotelsShop(Integer hotelId);

    /**
     * 查询所有商户列表
     * @return
     */
    List<ShopListRes> selectShopList();

    /**
     * 商户列表模糊分页查询
     * @param pageVoReq
     * @param map
     * @return
     */
    List<ShopPageListRes> findPageList(PageVo<ShopPageListReq> pageVoReq, Map map);

    /**
     * 根据酒店id和商户名称查询商户
     * @param map
     * @return
     */
    List<Shop> checkShopByHotelAndName(Map map);

    /**
     * 查询商户基本信息
     * @param shopId
     * @return
     */
    ShopBaseMsgVo selectShopBaseMsg(Integer shopId);

    ShopOrderDetailRes selectShopOrderDetail(ShopOrderDetailReq shopOrderDetailReq);

    Shop getShopInfoByGoodsId(Integer goodsId);

    /**
     * 根据商户id查询商户vo
     * @param shopId
     * @return
     */
    ShopVo selectShopVoById(Integer shopId);

    List<ShopListRes> selectShopListByName(ShopListRes params);

    List<ShopListRes> seachShopListByName(ShopListRes params);

    List<RepeatInfoVo> getRepeatInfoVo();

    List<Shop> selectCnList();

    com.colourfulchina.pangu.taishang.api.vo.res.bigan.Shop selectShopById(Long shopId);
}