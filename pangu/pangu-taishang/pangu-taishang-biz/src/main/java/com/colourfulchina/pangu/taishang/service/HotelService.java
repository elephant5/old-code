package com.colourfulchina.pangu.taishang.service;

import com.baomidou.mybatisplus.service.IService;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.inf.base.vo.PageVo;
import com.colourfulchina.pangu.taishang.api.entity.Hotel;
import com.colourfulchina.pangu.taishang.api.vo.HotelDetailVo;
import com.colourfulchina.pangu.taishang.api.vo.req.HotelPageListReq;
import com.colourfulchina.pangu.taishang.api.vo.res.HotelPageListRes;
import com.colourfulchina.pangu.taishang.api.vo.res.hotel.HotelInfoQueryRes;

import java.util.List;

public interface HotelService extends IService<Hotel> {
    /**
     * 酒店列表查询
     * @param pageVoReq
     * @return
     */
    PageVo<HotelPageListRes> findPageList(PageVo<HotelPageListReq> pageVoReq);

    /**
     * 根据酒店中文名查询酒店信息
     * @param hotelName
     * @return
     */
    Hotel selectByHotelName(String hotelName);

    /**
     * 新增酒店
     * @param hotel
     * @return
     */
    Hotel addHotel(Hotel hotel);

    /**
     * 酒店是否存在检查
     * @param hotelChName
     * @return
     */
    Hotel checkHotelIsExist(String hotelChName);

    /**
     * 旧系统酒店修改同步更新
     * @param hotelDetailVo
     * @return
     */
    CommonResultVo<com.colourfulchina.bigan.api.entity.Hotel> syncOldHotel(HotelDetailVo hotelDetailVo);

    /**
     * 根据老系统酒店id查询酒店信息
     * @param oldId
     * @return
     */
    Hotel selectByOldId(Integer oldId);

    /**
     * 酒店详情查询
     * @param hotelId
     * @return
     */
    HotelDetailVo queryHotelDetail(Integer hotelId);

    /**
     * 检车是否存在相同的中文名酒店
     * @return
     */
    Hotel checkHotelByNameCh(Hotel hotel);

    /**
     * 检车是否存在相同的英文名酒店
     * @return
     */
    Hotel checkHotelByNameEn(Hotel hotel);

    /**
     * 酒店的模糊查询
     * @param hotelName
     * @return
     */
    List<Hotel> selectByHotelNameList(String hotelName);

    /**
     * @title:selectHotelByShopId
     * @Description: 根据商户ID查询酒店详细信息
     * @Param: [shopId]
     * @return: com.colourfulchina.pangu.taishang.api.vo.res.hotel.HotelInfoQueryRes
     * @Auther: 图南
     * @Date: 2019/6/20 19:10
     */
    HotelInfoQueryRes selectHotelByShopId(Integer shopId);
}