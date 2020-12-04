package com.colourfulchina.bigan.controller;

import com.colourfulchina.bigan.api.entity.Hotel;
import com.colourfulchina.bigan.api.vo.HotelOldDetailVo;
import com.colourfulchina.bigan.service.HotelService;
import com.colourfulchina.bigan.service.ShopService;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hotel")
public class HotelController {
    @Autowired
    HotelService hotelService;
    @Autowired
    private ShopService shopService;

    @GetMapping("/get/{id}")
    public Hotel get(@PathVariable Long id){
        return hotelService.selectById(id);
    }

    /**
     * 根据酒店名称和城市检查酒店是否存在
     * @param hotel
     * @return flag(true:存在,false:不存在)
     */
    @GetMapping(value = "/check")
    public boolean checkHotelByNameAndCity(Hotel hotel){
         boolean flag = hotelService.checkHotelByNameAndCity(hotel);
        return flag;
    }

    /**
     * 查询酒店列表
     * @return
     */
    @PostMapping(value = "/selectHotelList")
    public CommonResultVo<List<Hotel>> selectHotelList(){
        CommonResultVo<List<Hotel>> result = new CommonResultVo<>();
        List<Hotel> hotelList = hotelService.selectList(null);
        result.setResult(hotelList);
        return result;
    }

    /**
     * 新增酒店
     * @return
     */
    @PostMapping("/addHotel")
    public CommonResultVo<Hotel> addHotel(@RequestBody Hotel hotel){
        CommonResultVo<Hotel> result = new CommonResultVo<>();
        hotel.setId(shopService.selectShopSeqNextValue());
        hotelService.insert(hotel);
        result.setResult(hotel);
        return result;
    }

    /**
     * 同步更新酒店信息
     * @return
     */
    @PostMapping("/updHotel")
    public CommonResultVo<Hotel> updHotel(@RequestBody HotelOldDetailVo hotelOldDetailVo){
        CommonResultVo<Hotel> result = new CommonResultVo<>();
        Hotel hotel = hotelService.updHotel(hotelOldDetailVo);
        result.setResult(hotel);
        return result;
    }
}
