package com.colourfulchina.bigan.service;

import com.baomidou.mybatisplus.service.IService;
import com.colourfulchina.bigan.api.entity.Hotel;
import com.colourfulchina.bigan.api.vo.HotelOldDetailVo;

import java.util.Map;

public interface HotelService extends IService<Hotel> {
    /**
     * 根据酒店名称和酒店城市检测是否存在酒店
     * @param hotel
     * @return
     */
    boolean checkHotelByNameAndCity(Hotel hotel);

    /**
     * 同步更新酒店信息
     * @param hotelOldDetailVo
     */
    Hotel updHotel(HotelOldDetailVo hotelOldDetailVo);

    /**
     * 插入文件记录
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
