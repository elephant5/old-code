package com.colourfulchina.bigan.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.bigan.api.entity.City;
import com.colourfulchina.bigan.api.entity.Hotel;
import com.colourfulchina.bigan.api.entity.Shop;
import com.colourfulchina.bigan.api.entity.SysFile;
import com.colourfulchina.bigan.api.vo.HotelOldDetailVo;
import com.colourfulchina.bigan.api.vo.HotelOldPortalVo;
import com.colourfulchina.bigan.mapper.HotelMapper;
import com.colourfulchina.bigan.service.CityService;
import com.colourfulchina.bigan.service.HotelService;
import com.colourfulchina.bigan.service.ShopService;
import com.colourfulchina.bigan.utils.PinYinUtils;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;


@Slf4j
@Service
public class HotelServiceImpl extends ServiceImpl<HotelMapper,Hotel> implements HotelService {
    @Autowired
    private HotelMapper hotelMapper;
    @Autowired
    private CityService cityService;
    @Autowired
    private ShopService shopService;


    /**
     * 根据酒店名称和酒店城市检测是否存在酒店
     * @param hotel
     * @return flag(true:存在,false:不存在)
     */
    @Override
    public boolean checkHotelByNameAndCity(Hotel hotel) {
        boolean flag = true;
       List<Hotel> hotelList = hotelMapper.checkHotelByNameAndCity(hotel);
       if (hotelList.size()==0 || hotel == null){
           flag = false;
       }
        return flag;
    }

    /**
     * 同步更新酒店信息
     * @param hotelOldDetailVo
     */
    @Override
    @Transactional
    public Hotel updHotel(HotelOldDetailVo hotelOldDetailVo) {
        try {
            City city = cityService.selectById(hotelOldDetailVo.getHotel().getCityId());
            Hotel hotel = hotelMapper.selectById(hotelOldDetailVo.getHotel().getId());
            //酒店名更改时，要同步修改酒店下面的商户信息
            if (!hotel.getName().equals(hotelOldDetailVo.getHotel().getName())){
                hotelReName(hotelOldDetailVo.getHotel());
            }
            hotel.setName(hotelOldDetailVo.getHotel().getName());
            hotel.setNameEn(hotelOldDetailVo.getHotel().getNameEn());
            hotel.setPy(hotelOldDetailVo.getHotel().getPy());
            hotel.setCityId(hotelOldDetailVo.getHotel().getCityId());
            hotel.setCity(hotelOldDetailVo.getHotel().getCity());
            hotel.setAddress(hotelOldDetailVo.getHotel().getAddress());
            hotel.setAddressEn(hotelOldDetailVo.getHotel().getAddressEn());
            hotel.setPortal(hotelOldDetailVo.getHotel().getPortal());
            if (city.getCountry().equals("CN")){
                hotel.setOversea(0);
            }else {
                hotel.setOversea(1);
            }
            hotelMapper.updateById(hotel);
            return hotel;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 插入文件记录
     * @param map
     * @return
     */
    @Override
    public Map<String, String> sysFileAdd(Map<String, String> map) {
        return hotelMapper.sysFileAdd(map);
    }

    /**
     * 更新酒店信息
     * @param map
     * @return
     */
    @Override
    public Map<String, String> hotelUpdate(Map<String, String> map) {
        return hotelMapper.hotelUpdate(map);
    }

    /**
     * 酒店名改变同步修改旗下商户信息
     * @param hotel
     */
    public void hotelReName(Hotel hotel){
        City city = cityService.selectById(hotel.getCityId());
        List<Shop> shopList = shopService.selectListByHotelId(hotel.getId().intValue());
        if (!CollectionUtils.isEmpty(shopList)){
            for (Shop shop : shopList) {
                shop.setHotel(hotel.getName());
                shop.setCity(hotel.getCity());
                shop.setCityId(hotel.getCityId().longValue());
                if (city.getCountry().equals("CN")){
                    shop.setOversea("0");
                }else {
                    shop.setOversea("1");
                }
                if (shop.getType().equals("accom")){
                    shop.setPy(PinYinUtils.getPinYinHeadChar(hotel.getName()));
                    shop.setTitle(hotel.getName());
                }else {
                    shop.setPy(PinYinUtils.getPinYinHeadChar(hotel.getName())+","+PinYinUtils.getPinYinHeadChar(shop.getName()));
                    shop.setTitle(hotel.getName()+"|"+shop.getName());
                }
                shopService.updateById(shop);
            }
        }
    }
}
