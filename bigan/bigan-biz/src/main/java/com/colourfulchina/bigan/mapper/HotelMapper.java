package com.colourfulchina.bigan.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.colourfulchina.bigan.api.entity.Hotel;

import java.util.List;
import java.util.Map;

public interface HotelMapper extends BaseMapper<Hotel> {
    /**
     * 根据酒店名和城市检测酒店列表
     * @param hotel
     * @return
     */
    public List<Hotel> checkHotelByNameAndCity(Hotel hotel);

    /**
     * 酒店更新
     * @param hotel
     */
    void updHotel(Hotel hotel);

    /**
     * 插入文件记录
     * @param map
     * @return
     */
    Map<String,String> sysFileAdd(Map<String,String> map);

    /**
     * 更新酒店信息
     * @param map
     * @return
     */
    Map<String,String> hotelUpdate(Map<String,String> map);
}
