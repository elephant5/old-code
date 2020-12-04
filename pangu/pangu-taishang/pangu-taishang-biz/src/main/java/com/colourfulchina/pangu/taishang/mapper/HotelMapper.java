package com.colourfulchina.pangu.taishang.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.colourfulchina.inf.base.vo.PageVo;
import com.colourfulchina.pangu.taishang.api.entity.Hotel;
import com.colourfulchina.pangu.taishang.api.vo.req.HotelPageListReq;
import com.colourfulchina.pangu.taishang.api.vo.res.HotelPageListRes;
import com.colourfulchina.pangu.taishang.api.vo.res.hotel.HotelInfoQueryRes;

import java.util.List;
import java.util.Map;

public interface HotelMapper extends BaseMapper<Hotel> {
    /**
     * 酒店列表查询
     * @param hotel
     * @return
     */
    List<HotelPageListRes> findPageList(PageVo<HotelPageListReq> hotel, Map map);

    /**
     * 根据酒店中文名查询酒店信息
     * @param hotelName
     * @return
     */
    Hotel selectByHotelName(String hotelName);

    /**
     * 酒店是否存在检查（根据酒店中文名）
     * @param hotelChName
     * @return
     */
    Hotel checkHotelIsExist(String hotelChName);

    /**
     * 检车是否存在相同的中文名酒店
     * @param hotel
     * @return
     */
    Hotel checkHotelByNameCh(Hotel hotel);

    /**
     * 检车是否存在相同的英文名酒店
     * @param hotel
     * @return
     */
    Hotel checkHotelByNameEn(Hotel hotel);

    /**
     * @title:selectHotelByShopId
     * @Description: 根据商户ID查询酒店详细信息
     * @Param: [shopId]
     * @return: com.colourfulchina.pangu.taishang.api.vo.res.hotel.HotelInfoQueryRes
     * @Auther: 图南
     * @Date: 2019/6/20 19:11
     */
    HotelInfoQueryRes selectHotelByShopId(Integer shopId);
}