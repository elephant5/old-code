package com.colourfulchina.bigan.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.colourfulchina.bigan.api.entity.City;
import com.colourfulchina.bigan.api.entity.Hotel;
import com.colourfulchina.bigan.api.entity.Shop;
import com.colourfulchina.bigan.api.entity.ShopSection;
import com.colourfulchina.bigan.api.vo.RemoteAddShopVo;
import com.colourfulchina.bigan.api.vo.RemoteUpdShopBaseMsgVo;
import com.colourfulchina.bigan.api.vo.RemoteUpdShopProtocolVo;
import com.colourfulchina.bigan.api.vo.ShopDetailVo;
import com.colourfulchina.bigan.service.CityService;
import com.colourfulchina.bigan.service.HotelService;
import com.colourfulchina.bigan.service.ShopSectionService;
import com.colourfulchina.bigan.service.ShopService;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/shop")
public class ShopController {
    public final static String PARKING_STR = "泊车信息";
    public final static String CHILDREN_STR = "儿童政策";
    public final static String NOTICE_STR = "重要通知";
    @Autowired
    private ShopService shopService;
    @Autowired
    private HotelService hotelService;
    @Autowired
    private CityService cityService;
    @Autowired
    private ShopSectionService shopSectionService;

    @GetMapping("/get/{id}")
    public Shop get(@PathVariable Long id){
        return shopService.selectById(id);
    }

    /**
     * 根据酒店名称、城市和门店名称检查门店是否存在
     * @param shop
     * @return flag(true:存在,false:不存在)
     */
    @GetMapping(value = "/check")
    public boolean checkShopByNameAndCityAndHotel(Shop shop){
         boolean flag = shopService.checkShopByNameAndCityAndHotel(shop);
        return flag;
    }

    /**
     * 插入商户信息
     * @param shop
     * @return
     */
    @PostMapping(value = "/insert")
    public Shop insert(Shop shop){
        shopService.insert(shop);
        return shop;
    }

    /**
     * 查询商户及相关详情
     * @param id
     * @return
     */
    @GetMapping("/getShopDetail/{id}")
    public CommonResultVo<ShopDetailVo> getShopDetail(@PathVariable Integer id){
        CommonResultVo<ShopDetailVo> resultVo=new CommonResultVo<>();
        try {
            resultVo.setResult(shopService.getShopDetail(id));
        }catch (Exception e){
            log.error("查询商户及相关详情出错",e);
            resultVo.setCode(200);
            resultVo.setMsg(e.getMessage());
        }
        return resultVo;
    }

    /**
     * 查询老系统所有商户列表
     * @return
     */
    @PostMapping("/selectShopList")
    public CommonResultVo<List<Shop>> selectShopList(){
        CommonResultVo<List<Shop>> result = new CommonResultVo<>();
        List<Shop> shopList = shopService.selectList(null);
        result.setResult(shopList);
        return result;
    }

    /**
     * 远程新增商户时老系统同步新增
     * @param remoteAddShopVo
     * @return
     */
    @PostMapping("/remoteAddShop")
    public CommonResultVo<Shop> remoteAddShop(@RequestBody RemoteAddShopVo remoteAddShopVo){
        CommonResultVo<Shop> result = new CommonResultVo<>();
        //根据hotelId查询酒店信息
        Hotel hotel = hotelService.selectById(remoteAddShopVo.getHotelId());
        //根据cityid查询城市信息
        City city = cityService.selectById(remoteAddShopVo.getCityId());
        Shop shop = new Shop();
        shop.setId(shopService.getIdForSeq());
        shop.setHotelId(remoteAddShopVo.getHotelId() == null ? null :remoteAddShopVo.getHotelId().longValue());
        shop.setHotel(hotel == null ? null : hotel.getName());
        shop.setPy(remoteAddShopVo.getShopPy());
        shop.setCityId(remoteAddShopVo.getCityId().longValue());
        shop.setCity(city.getName());
        if (city.getCountry() != null && city.getCountry().equals("CN")){
            shop.setOversea("0");
        }else {
            shop.setOversea("1");
        }
        String title  = hotel == null ? null : hotel.getName();
        if (!"accom".equalsIgnoreCase(remoteAddShopVo.getShopType())){
            title = title == null ? remoteAddShopVo.getShopName() : (title + "|" + remoteAddShopVo.getShopName());
        }
        shop.setTitle(title);
        shop.setName(remoteAddShopVo.getShopName());
        shop.setAddress(remoteAddShopVo.getAddress());
        shop.setNotes(remoteAddShopVo.getNotes());
        shop.setType(remoteAddShopVo.getShopType());
        shop.setCreateTime(new Date());
        shopService.insert(shop);
        result.setResult(shop);
        return result;
    }

    /**
     * 同步新增商户时商户协议到老系统shop表中
     * @param remoteUpdShopProtocolVo
     * @return
     */
    @Transactional
    @PostMapping("/remoteUpdShopProtocol")
    public CommonResultVo<Shop> remoteUpdShopProtocol(@RequestBody RemoteUpdShopProtocolVo remoteUpdShopProtocolVo){
        CommonResultVo<Shop> result = new CommonResultVo<>();
        Shop shop = shopService.selectById(remoteUpdShopProtocolVo.getShopId());
        shop.setChannelId(remoteUpdShopProtocolVo.getChannelId() == null ? null : remoteUpdShopProtocolVo.getChannelId().longValue());
        shop.setCurrency(remoteUpdShopProtocolVo.getCurrency());
        shop.setSettleMethod(remoteUpdShopProtocolVo.getSettleMethod());
        //decimal principal需要解析more字段
        shop.setMore(giveUpdShopProtocolMore(shop.getMore(),remoteUpdShopProtocolVo));
        shop.setContractExpiry(remoteUpdShopProtocolVo.getContractExpiry());
        shop.setBlock(remoteUpdShopProtocolVo.getBlockRule());
        //停车策略 儿童政策 重要通知 需要单独处理    该商户的数据全部删除，重新添加
        List<ShopSection> shopSectionList = Lists.newLinkedList();
        if (StringUtils.isNotEmpty(remoteUpdShopProtocolVo.getParking())){
            ShopSection shopSection = new ShopSection();
            shopSection.setShopId(shop.getId());
            shopSection.setTitle(PARKING_STR);
            shopSection.setContent(remoteUpdShopProtocolVo.getParking());
            shopSectionList.add(shopSection);
        }
        if (StringUtils.isNotEmpty(remoteUpdShopProtocolVo.getChildren())){
            ShopSection shopSection = new ShopSection();
            shopSection.setShopId(shop.getId());
            shopSection.setTitle(CHILDREN_STR);
            shopSection.setContent(remoteUpdShopProtocolVo.getChildren());
            shopSectionList.add(shopSection);
        }
        if (StringUtils.isNotEmpty(remoteUpdShopProtocolVo.getNotice())){
            ShopSection shopSection = new ShopSection();
            shopSection.setShopId(shop.getId());
            shopSection.setTitle(NOTICE_STR);
            shopSection.setContent(remoteUpdShopProtocolVo.getNotice());
            shopSectionList.add(shopSection);
        }
        shopSectionService.delSection(shop.getId().intValue());
        if (!CollectionUtils.isEmpty(shopSectionList)){
            shopSectionService.insertBatch(shopSectionList);
        }
        shopService.updateById(shop);
        result.setResult(shop);
        return result;
    }

    /**
     * 新系统商户基本信息新增修改时同步老系统
     * @param remoteUpdShopBaseMsgVo
     * @return
     */
    @PostMapping("/remoteUpdShopBaseMsg")
    public CommonResultVo<Shop> remoteUpdShopBaseMsg(@RequestBody RemoteUpdShopBaseMsgVo remoteUpdShopBaseMsgVo){
        //根据cityId查询城市信息
        City city = cityService.selectById(remoteUpdShopBaseMsgVo.getCityId());
        CommonResultVo<Shop> result = new CommonResultVo<>();
        Shop shop = shopService.selectById(remoteUpdShopBaseMsgVo.getShopId());
        shop.setName(remoteUpdShopBaseMsgVo.getShopName());
        shop.setNameEn(remoteUpdShopBaseMsgVo.getShopNameEn());
        shop.setPy(remoteUpdShopBaseMsgVo.getShopPy());
        shop.setTitle(remoteUpdShopBaseMsgVo.getShopTitle());
        shop.setCityId(remoteUpdShopBaseMsgVo.getCityId().longValue());
        shop.setCity(city.getName());
        if (city.getCountry() != null && city.getCountry().equals("CN")){
            shop.setOversea("0");
        }else {
            shop.setOversea("1");
        }
        shop.setAddress(remoteUpdShopBaseMsgVo.getAddress());
        shop.setAddressEn(remoteUpdShopBaseMsgVo.getAddressEn());
        shop.setPhone(remoteUpdShopBaseMsgVo.getPhone());
        shop.setOpenTime(remoteUpdShopBaseMsgVo.getOpenTime()+"~"+remoteUpdShopBaseMsgVo.getCloseTime());
        //此处checkIn和checkOut、tips需要解析more字段，并重新生成
        shop.setMore(giveShopBaseMsgMore(shop.getMore(),remoteUpdShopBaseMsgVo));
        shop.setNotes(remoteUpdShopBaseMsgVo.getNotes());
        shop.setLevel(remoteUpdShopBaseMsgVo.getLevel());
        shop.setSummary(remoteUpdShopBaseMsgVo.getSummary());
        shopService.updateById(shop);
        result.setResult(shop);
        return result;
    }

    /**
     * 新系统商户基本信息新增修改时同步老系统的more字段重组
     * @param oldMore
     * @param remoteUpdShopBaseMsgVo
     * @return
     */
    public String giveShopBaseMsgMore(String oldMore,RemoteUpdShopBaseMsgVo remoteUpdShopBaseMsgVo) {
        JSONObject jsonObject = new JSONObject();
        if (StringUtils.isNotEmpty(oldMore)){
            jsonObject = JSON.parseObject(oldMore);
        }
        if (StringUtils.isNotEmpty(remoteUpdShopBaseMsgVo.getCheckInTime())){
            jsonObject.put("checkin_time",remoteUpdShopBaseMsgVo.getCheckInTime());
        }else {
            jsonObject.remove("checkin_time");
        }
        if (StringUtils.isNotEmpty(remoteUpdShopBaseMsgVo.getCheckOutTime())){
            jsonObject.put("checkout_time",remoteUpdShopBaseMsgVo.getCheckOutTime());
        }else {
            jsonObject.remove("checkout_time");
        }
        if (remoteUpdShopBaseMsgVo.getMinBookDays() != null){
            jsonObject.put("book_min_days",remoteUpdShopBaseMsgVo.getMinBookDays());
        }else {
            jsonObject.remove("book_min_days");
        }
        if (remoteUpdShopBaseMsgVo.getMaxBookDays() != null){
            jsonObject.put("book_max_days",remoteUpdShopBaseMsgVo.getMaxBookDays());
        }else {
            jsonObject.remove("book_max_days");
        }
        if (StringUtils.isNotEmpty(remoteUpdShopBaseMsgVo.getTips())){
            jsonObject.put("tips",remoteUpdShopBaseMsgVo.getTips());
        }else {
            jsonObject.remove("tips");
        }
        return jsonObject.toJSONString();
    }

    /**
     * 同步新增商户时商户协议到老系统shop表中的more字段重组
     * @param oldMore
     * @param remoteUpdShopProtocolVo
     * @return
     */
    public String giveUpdShopProtocolMore(String oldMore,RemoteUpdShopProtocolVo remoteUpdShopProtocolVo) {
        JSONObject jsonObject = new JSONObject();
        if (StringUtils.isNotEmpty(oldMore)){
            jsonObject = JSON.parseObject(oldMore);
        }
        if (remoteUpdShopProtocolVo.getDecimal() != null){
            jsonObject.put("decimal",remoteUpdShopProtocolVo.getDecimal());
        }else {
            jsonObject.remove("decimal");
        }
        if (StringUtils.isNotEmpty(remoteUpdShopProtocolVo.getPrincipal())){
            jsonObject.put("principal",remoteUpdShopProtocolVo.getPrincipal());
        }else {
            jsonObject.remove("principal");
        }
        return jsonObject.toJSONString();
    }

}
