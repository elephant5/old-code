package com.colourfulchina.pangu.taishang.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.colourfulchina.bigan.api.feign.RemoteShopService;
import com.colourfulchina.god.door.api.annotation.SysGodDoorLog;
import com.colourfulchina.god.door.api.util.SecurityUtils;
import com.colourfulchina.inf.base.entity.SysOperateLogDetail;
import com.colourfulchina.inf.base.enums.SysOperateLogEnums;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.inf.base.vo.PageVo;
import com.colourfulchina.pangu.taishang.api.entity.*;
import com.colourfulchina.pangu.taishang.api.entity.ShopTypeService;
import com.colourfulchina.pangu.taishang.api.enums.*;
import com.colourfulchina.pangu.taishang.api.vo.OldShopMoreVo;
import com.colourfulchina.pangu.taishang.api.vo.RepeatInfoVo;
import com.colourfulchina.pangu.taishang.api.vo.ShopBaseMsgVo;
import com.colourfulchina.pangu.taishang.api.vo.ShopProtocolMsgVo;
import com.colourfulchina.pangu.taishang.api.vo.req.*;
import com.colourfulchina.pangu.taishang.api.vo.res.*;
import com.colourfulchina.pangu.taishang.constant.DateConstant;
import com.colourfulchina.pangu.taishang.constant.ShopConstant;
import com.colourfulchina.pangu.taishang.constant.SysLogConstant;
import com.colourfulchina.pangu.taishang.service.*;
import com.colourfulchina.pangu.taishang.utils.PinYinUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/shop")
@Slf4j
@AllArgsConstructor
@Api(value = "商户相关controller",tags = {"商户操作接口"})
public class ShopController {
    @Autowired
    private ShopService shopService;
    @Autowired
    private HotelService hotelService;
    @Autowired
    private ShopProtocolService shopProtocolService;
    @Autowired
    private ShopItemService shopItemService;
    @Autowired
    private CityService cityService;
    @Autowired
    private CountryService countryService;
    @Autowired
    private SysOperateLogService sysOperateLogService;
    @Autowired
    private ShopAccountService shopAccountService;
    @Autowired
    private ShopTypeServiceService shopTypeServiceService;
    @Autowired
    private ShopChannelService shopChannelService;
    @Autowired
    private BlockRuleService blockRuleService;
    @Autowired
    private BlockReasonService blockReasonService;
    private final RemoteShopService remoteShopService;
    @Autowired
    private GeoService geoService;
    @Autowired
    private ProductGroupProductService productGroupProductService;
    @Autowired
    private GroupProductBlockDateService groupProductBlockDateService;

    @Autowired
    private GoodsClauseService goodsClauseService;
    /**
     * 商户列表模糊分页查询
     * @param pageVoReq
     * @return
     */
    @SysGodDoorLog("商户列表")
    @ApiOperation("商户列表")
    @PostMapping("/selectShopPageList")
    public CommonResultVo<PageVo<ShopPageListRes>> selectShopPageList(@RequestBody PageVo<ShopPageListReq> pageVoReq){
        CommonResultVo<PageVo<ShopPageListRes>> result = new CommonResultVo<>();
        PageVo<ShopPageListRes> pageVoRes = shopService.findPageList(pageVoReq);
        result.setResult(pageVoRes);
        return result;
    }


    /**
     * 查询所有商户列表，无入参
     * @return
     */
    @SysGodDoorLog("查询所有商户列表")
    @ApiOperation("查询所有商户列表，无入参")
    @PostMapping("/selectShopList")
    @Cacheable(value = "Shop",key = "'selectShopList'",unless = "#result == null")
    public CommonResultVo<List<ShopListRes>> selectShopList(){
        CommonResultVo<List<ShopListRes>> result = new CommonResultVo<>();
        try {
            List<ShopListRes> list = shopService.selectShopList();
            result.setResult(list);
        }catch (Exception e){
            log.error("查询所有商户列表失败",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    /**
     * 查询所有商户列表，无入参
     * @return
     */
    @SysGodDoorLog("模糊查询所有商户列表")
    @ApiOperation("模糊查询所有商户列表")
    @PostMapping("/selectShopListByName")
    public CommonResultVo<List<ShopListRes>> selectShopListByName(@RequestBody ShopListRes params){
        CommonResultVo<List<ShopListRes>> result = new CommonResultVo<>();
        try {
            params.setShopType(ResourceTypeEnums.findByName(params.getShopType()).getCode());
            List<ShopListRes> list = shopService.selectShopListByName(params);
            if(!list.isEmpty()){
                for(ShopListRes res : list){
                    String temp = null;
                    if(StringUtils.isNotBlank(res.getGift())){
                        temp="("+ GiftTypeEnum.findByCode(res.getGift()).getName()+ ")";
                    }
                    temp = (temp == null? "" : temp+"-")+"调剂";
                    res.setGift(temp);
                }
            }
            result.setResult(list);
        }catch (Exception e){
            log.error("查询所有商户列表失败",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }
    /**
     * 查询所有商户列表，无入参
     * @return
     */
    @SysGodDoorLog("模糊查询所有商户列表")
    @ApiOperation("模糊查询所有商户列表")
    @PostMapping("/seachShopListByName")
    public CommonResultVo<List<ShopListRes>> seachShopListByName(@RequestBody ShopListRes params){
        CommonResultVo<List<ShopListRes>> result = new CommonResultVo<>();
        try {
            List<ShopListRes> list = shopService.seachShopListByName(params);
//            if(!list.isEmpty()){
////                for(ShopListRes res : list){
////                    String temp = null;
////                    if(StringUtils.isNotBlank(res.getGift())){
////                        temp="("+ GiftTypeEnum.findByCode(res.getGift()).getName()+ ")";
////                    }
////                    temp = (temp == null? "" : temp+"-")+"调剂";
////                    res.setGift(temp);
////                }
////            }
            result.setResult(list);
        }catch (Exception e){
            log.error("查询所有商户列表失败",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }
    @SysGodDoorLog("查询所有商户列表")
    @ApiOperation("查询所有商户列表，无入参")
    @PostMapping("/selectShops")
    @Cacheable(value = "Shop",key = "'selectShops'",unless = "#result == null")
    public CommonResultVo<List<Shop>> selectShops(){
        CommonResultVo<List<Shop>> result = new CommonResultVo<>();
        try {
            Wrapper<Shop> shopWrapper=new Wrapper<Shop>() {
                @Override
                public String getSqlSegment() {
                    return null;
                }
            };
            final List<Shop> shopList = shopService.selectList(shopWrapper);
            result.setResult(shopList);
        }catch (Exception e){
            log.error("查询所有商户列表失败",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }
    /**
     * 根据商户id查询酒店信息
     * @return
     */
    @SysGodDoorLog("根据商户id查询酒店信息")
    @ApiOperation("根据商户id查询酒店信息")
    @PostMapping("/selectHotelByShopId")
    @Cacheable(value = "Shop",key = "'selectHotelByShopId_'+#shopId")
    public CommonResultVo<Hotel> selectHotelByShopId(@RequestBody Integer shopId){
        CommonResultVo<Hotel> result = new CommonResultVo<>();
        try {
            Assert.notNull(shopId,"商户id不能为空");
            Shop shop = shopService.selectById(shopId);
            if (shop.getHotelId() != null){
                Hotel hotel = hotelService.selectById(shop.getHotelId());
                result.setResult(hotel);
            }
        }catch (Exception e){
            log.error("根据商户id查询酒店信息失败",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    /**
     * 商户操作日志分页查询接口
     * @param pageVoReq
     * @return
     */
    @SysGodDoorLog("商户操作日志记录分页接口")
    @ApiOperation("商户操作日志记录分页接口")
    @PostMapping("/selectShopLogPageList")
    public CommonResultVo<PageVo<ShopLogPageListRes>> selectShopLogPageList(@RequestBody PageVo<ShopLogPageListReq> pageVoReq){
        CommonResultVo<PageVo<ShopLogPageListRes>> result = new CommonResultVo<>();
        PageVo<ShopLogPageListRes> pageVoRes = sysOperateLogService.queryShopLogPage(pageVoReq);
        result.setResult(pageVoRes);
        return result;
    }

    /**
     * 商户中文名修改记录分页查询接口
     * @param shopId
     * @return
     */
    @SysGodDoorLog("商户中文名修改记录接口")
    @ApiOperation("商户中文名修改记录接口")
    @PostMapping("/selectNameChLogPageList")
    @Cacheable(value = "Shop",key = "'selectNameChLogPageList_'+#shopId",unless = "#result == null")
    public CommonResultVo<PageVo<SysOperateLogDetail>> selectNameChLogPageList(@RequestBody Integer shopId){
        CommonResultVo<PageVo<SysOperateLogDetail>> result = new CommonResultVo<>();
        if (shopId == null){
            result.setCode(200);
            result.setMsg("商户中文名修改记录参数有误！");
            return result;
        }
        PageVo<SysOperateLogDetail> pageVo = new PageVo<>();
        Map map = Maps.newHashMap();
        map.put(SysLogConstant.LOG_TABLE_NAME,Shop.class.getAnnotation(TableName.class).value());
        map.put(SysLogConstant.LOG_OPT_TYPE, SysOperateLogEnums.Type.UPDATE.getCode());
        map.put(SysLogConstant.LOG_ROW_KEY,shopId);
        try {
            map.put(SysLogConstant.LOG_FIELD_NAME,Shop.class.getDeclaredField("name").getAnnotation(TableField.class).value());
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        pageVo.setCondition(map);
        pageVo = sysOperateLogService.querySysOperateLogDetailPage(pageVo);
        result.setResult(pageVo);
        return result;
    }

    /**
     * 商户英文名修改记录分页查询接口
     * @param shopId
     * @return
     */
    @SysGodDoorLog("商户英文名修改记录接口")
    @ApiOperation("商户英文名修改记录接口")
    @PostMapping("/selectNameEnLogPageList")
    @Cacheable(value = "Shop",key = "'selectNameEnLogPageList_'+#shopId",unless = "#result == null")
    public CommonResultVo<PageVo<SysOperateLogDetail>> selectNameEnLogPageList(@RequestBody Integer shopId){
        CommonResultVo<PageVo<SysOperateLogDetail>> result = new CommonResultVo<>();
        if (shopId == null){
            result.setCode(200);
            result.setMsg("商户英文名修改记录参数有误！");
            return result;
        }
        PageVo<SysOperateLogDetail> pageVo = new PageVo<>();
        Map map = Maps.newHashMap();
        map.put(SysLogConstant.LOG_TABLE_NAME,Shop.class.getAnnotation(TableName.class).value());
        map.put(SysLogConstant.LOG_OPT_TYPE,SysOperateLogEnums.Type.UPDATE.getCode());
        map.put(SysLogConstant.LOG_ROW_KEY,shopId);
        try {
            map.put(SysLogConstant.LOG_FIELD_NAME,Shop.class.getDeclaredField("nameEn").getAnnotation(TableField.class).value());
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        pageVo.setCondition(map);
        pageVo = sysOperateLogService.querySysOperateLogDetailPage(pageVo);
        result.setResult(pageVo);
        return result;
    }

    /**
     * 商户详情查询
     * @param shopId
     * @return
     */
    @SysGodDoorLog("商户详情查询")
    @ApiOperation("商户详情查询")
    @PostMapping("/shopDetail")
    @Cacheable(value = "Shop",key = "'shopDetail_'+#shopId",unless = "#result == null")
    public CommonResultVo<ShopDetailRes> shopDetail(@RequestBody Integer shopId){
        CommonResultVo<ShopDetailRes> result = new CommonResultVo<>();
        ShopDetailRes shopDetailRes = new ShopDetailRes();
        ShopBaseMsgVo shopBaseMsgVo = shopService.selectShopBaseMsg(shopId);
        ShopProtocolMsgVo shopProtocolMsgVo = shopProtocolService.selectProtocolMsg(shopId);
        List<ShopItem> shopItemList = shopItemService.selectListByShopId(shopId);
        shopDetailRes.setShop(shopBaseMsgVo);
        shopDetailRes.setShopProtocol(shopProtocolMsgVo);
        shopDetailRes.setShopItemList(shopItemList);
        result.setResult(shopDetailRes);
        return result;
    }


    /**
     * 商户详情查询
     * @param shopIds
     * @return
     */
    @SysGodDoorLog("商户详情查询")
    @ApiOperation("商户详情查询")
    @PostMapping("/shopDetailList")
    @Cacheable(value = "Shop",key = "'shopDetailList_'+#shopIds",unless = "#result == null")
    public CommonResultVo<List<ShopDetailRes>> shopDetailList(@RequestBody List<Integer> shopIds){
        CommonResultVo<List<ShopDetailRes>> result = new CommonResultVo<>();
        List<ShopDetailRes> results  = Lists.newArrayList();
        for(Integer shopId : shopIds){
            ShopDetailRes shopDetailRes = new ShopDetailRes();
            ShopBaseMsgVo shopBaseMsgVo = shopService.selectShopBaseMsg(shopId);
//            ShopProtocolMsgVo shopProtocolMsgVo = shopProtocolService.selectProtocolMsg(shopId);
//            List<ShopItem> shopItemList = shopItemService.selectListByShopId(shopId);
            shopDetailRes.setShop(shopBaseMsgVo);
//            shopDetailRes.setShopProtocol(shopProtocolMsgVo);
//            shopDetailRes.setShopItemList(shopItemList);
            results.add(shopDetailRes);
        }
        result.setResult(results);
        return result;
    }
    /**
     * 商户详情更新
     * @param params
     * @return
     */
    @ApiOperation("商户详情查询")
    @PostMapping("/addShopDetail")
    @CacheEvict(value = {"Shop","Goods","ProductGroup","ProductGroupProduct"},allEntries = true)
    public CommonResultVo<Shop> addShopDetail(@RequestBody Shop params){
        CommonResultVo<Shop> result = new CommonResultVo<>();
        Shop shop = shopService.selectById(params.getId());
        shop.setDetail(params.getDetail());
        shopService.updateById(shop);
        result.setResult(shop);
        return result;
    }

    /**
     * 获取商户关房关餐
     * @param params
     * @return
     */
    @SysGodDoorLog("获取商户关房关餐")
    @ApiOperation("获取商户关房关餐")
    @PostMapping("/getBlockReason")
    @Cacheable(value = "Shop",key = "'getBlockReason_'+#params.id",unless = "#result == null")
    public CommonResultVo< List<BlockReason>> getBlockReason(@RequestBody ShopItem params){
        CommonResultVo< List<BlockReason>> result = new CommonResultVo<>();
        List<BlockReason> blockReasons = blockReasonService.getBlockReason(params.getId());
        result.setResult(blockReasons);
        return result;
    }
    /**
     * 根据酒店名称和商户名称查询商户
     * @param shopName
     * @param hotelName
     * @return
     */
    @SysGodDoorLog("根据酒店名称和商户名称查询商户")
    @ApiOperation("根据酒店名称和商户名称查询商户")
    @PostMapping("/checkShopByHotelAndName")
    @Cacheable(value = "Shop",key = "'checkShopByHotelAndName_'+#shopName+'_'+#hotelName",unless = "#result == null")
    public CommonResultVo<Shop> checkShopByHotelAndName(String shopName, String hotelName){
        CommonResultVo<Shop> result = new CommonResultVo<>();
        Shop shop = shopService.checkShopByHotelAndName(shopName,hotelName);
        if (shop != null){
            result.setCode(200);
            result.setMsg("商户已存在");
            result.setResult(shop);
        }
        result.setResult(shop);
        return result;
    }

    /**
     * 新增商户第一步
     * @param addShopOneReq
     * @return
     */
    @SysGodDoorLog("新增商户第一步")
    @ApiOperation("新增商户第一步")
    @PostMapping("/addShopOne")
    @Transactional(rollbackFor=Exception.class)
    @CacheEvict(value = {"Shop","Goods","ProductGroup","ProductGroupProduct"},allEntries = true)
    public CommonResultVo<AddShopOneRes> addShopOne(@RequestBody AddShopOneReq addShopOneReq){
        CommonResultVo<AddShopOneRes> result = new CommonResultVo<>();
        //根据酒店名称和商户名称检查商户是否存在
        Shop shopFlag = shopService.checkShopByHotelAndName(addShopOneReq.getShopName(),addShopOneReq.getHotelNameCh());
        if (shopFlag != null){
            result.setCode(200);
            result.setMsg("商户已存在");
            return result;
        }
        AddShopOneRes addShopOneRes = new AddShopOneRes();
        Hotel hotelFlag;
        Shop shop = new Shop();
        City city = cityService.selectById(addShopOneReq.getCityId());
        //所属酒店存在，即为附属商户
        if (StringUtils.isNotEmpty(addShopOneReq.getHotelNameCh())){
            //檢查商戶所屬酒店是否存在
            hotelFlag = hotelService.checkHotelIsExist(addShopOneReq.getHotelNameCh());
            if (hotelFlag == null){
                hotelFlag = new Hotel();
                hotelFlag.setNameCh(addShopOneReq.getHotelNameCh());
                hotelFlag.setCityId(addShopOneReq.getCityId());
                hotelFlag.setAddressCh(addShopOneReq.getAddress());
                hotelService.addHotel(hotelFlag);
            }
            shop.setHotelId(hotelFlag.getId());
        }else {//所属酒店不存在，则为独立商户
            shop.setHotelId(null);
            shop.setCityId(addShopOneReq.getCityId());
        }
        shop.setShopType(addShopOneReq.getShopTypeId());
        shop.setName(addShopOneReq.getShopName());
        if (addShopOneReq.getShopTypeId().equals(ShopTypeEnums.ACCOM.getCode())){
            shop.setName(ShopConstant.ACCOM_SHOP_NAME);
        }
        shop.setPy(PinYinUtils.getPinYinHeadChar(shop.getName()));
        shop.setAddress(addShopOneReq.getAddress());
        shop.setNotes(addShopOneReq.getNotes());
        shop.setShopNature(addShopOneReq.getShopNature());
        shop.setDelFlag(DelFlagEnums.NORMAL.getCode());
        shop.setShopStatus(ShopStatusEnums.UPSALES.getCode());
        shop.setStatusTime(new Date());
        shop.setCreateTime(new Date());

        shop.setCreateUser(SecurityUtils.getLoginName());
        shop.setUpdateTime(new Date());
        shop.setUpdateUser(SecurityUtils.getLoginName());
        shopService.addShop(shop);
        //商户协议插入信息
        ShopProtocol shopProtocol = new ShopProtocol();
        shopProtocol.setId(shop.getId());
        shopProtocol.setChannelId(addShopOneReq.getShopChannelId());
        shopProtocol.setCurrency(addShopOneReq.getSettleCurrency());
        shopProtocol.setSettleMethod(addShopOneReq.getSettleMethod());
        shopProtocol.setDelFlag(DelFlagEnums.NORMAL.getCode());
        shopProtocol.setCreateTime(new Date());

        shopProtocol.setCreateUser(SecurityUtils.getLoginName());
        shopProtocol.setUpdateTime(new Date());
        shopProtocol.setUpdateUser(SecurityUtils.getLoginName());
        shopProtocolService.addShopProtocol(shopProtocol);
        addShopOneRes.setShop(shop);
        result.setResult(addShopOneRes);
        return result;
    }

    /**
     * 商户基础信息修改新增/修改
     * @param addShopBaseMsgReq
     * @return
     */
    @SysGodDoorLog("商户基础信息修改")
    @Transactional(rollbackFor=Exception.class)
    @PostMapping("/addShopBaseMsg")
    @ApiOperation("商户基础信息修改")
    @CacheEvict(value = {"Shop","Goods","ProductGroup","ProductGroupProduct"},allEntries = true)
    public CommonResultVo<Shop> addShopBaseMsg(@RequestBody AddShopBaseMsgReq addShopBaseMsgReq){
        CommonResultVo<Shop> result = new CommonResultVo<>();
        Shop shop = shopService.selectById(addShopBaseMsgReq.getId());
        City city = new City();
        Country country = new Country();
        if (addShopBaseMsgReq.getCityId()!= null){
            city = cityService.selectById(addShopBaseMsgReq.getCityId());
            if (city != null){
                country = countryService.selectById(city.getCountryId());
            }
        }

        shop.setName(addShopBaseMsgReq.getShopNameCh());
        shop.setNameEn(addShopBaseMsgReq.getShopNameEn());
        if (shop.getShopNature() == ShopNatureEnums.INDEPENDENT_SHOP.getCode()){
            shop.setCityId(addShopBaseMsgReq.getCityId());
        }
        //定位信息
        Geo geo =shop.getGeoId()== null ? new Geo(): geoService.selectById(shop.getGeoId());
        if(StringUtils.isNotEmpty(addShopBaseMsgReq.getCoordinate())){
            geo.setCity(city.getNameCh());
//            geo.setProvince(city.getNameCh());
            geo.setCountry(country.getId());
            geo.setLng(addShopBaseMsgReq.getCoordinate().split(",")[0]);
            geo.setLat(addShopBaseMsgReq.getCoordinate().split(",")[1]);
            geo.setAddressCh(addShopBaseMsgReq.getAddressCh());
            geo.setAddressEn(addShopBaseMsgReq.getAddressEn());
            if(geo.getId() == null){

                geoService.insert(geo);
            }else{
                geoService.updateById(geo);
            }
            shop.setGeoId(geo.getId());
        }else {
            shop.setGeoId(null);
        }
        shop.setAddress(addShopBaseMsgReq.getAddressCh());
        shop.setAddressEn(addShopBaseMsgReq.getAddressEn());
        shop.setPhone(addShopBaseMsgReq.getPhone());
        shop.setOpenTime(addShopBaseMsgReq.getOpenTime());
        shop.setCloseTime(addShopBaseMsgReq.getCloseTime());
        shop.setCheckInTime(addShopBaseMsgReq.getCheckInTime());
        shop.setCheckOutTime(addShopBaseMsgReq.getCheckOutTime());
        shop.setTips(addShopBaseMsgReq.getTips());
        shop.setNotes(addShopBaseMsgReq.getNotes());
        shop.setLevel(addShopBaseMsgReq.getLevel());
        shop.setSummary(addShopBaseMsgReq.getSummary());
        shop.setMaxBookDays(addShopBaseMsgReq.getMaxBookDays());
        shop.setMinBookDays(addShopBaseMsgReq.getMinBookDays());
        shop = shopService.updShopBaseMsg(shop);
        result.setResult(shop);
        return result;
    }

    /**
     * 生成定位信息
     * @return
     */
    @PostMapping("/generateGeo")
    @SysGodDoorLog("生成定位信息")
    @ApiOperation("生成定位信息")
    public CommonResultVo generateGeo(){
        CommonResultVo result = new CommonResultVo();
        try {
            shopService.generateGeo();
        }catch (Exception e){
            log.error("生成定位信息失败",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    /**
     * 商户协议信息新增/修改
     * @param addShopProtocolReq
     * @return
     */
    @SysGodDoorLog("商户协议信息修改")
    @PostMapping("/addShopProtocol")
    @ApiOperation("商户协议信息修改")
    @CacheEvict(value = {"Shop","Goods","ProductGroup","ProductGroupProduct"},allEntries = true)
    public CommonResultVo<ShopProtocol> addShopProtocol(@RequestBody AddShopProtocolReq addShopProtocolReq){
        CommonResultVo<ShopProtocol> result = new CommonResultVo<>();
        try {
            //商户协议信息新增修改
            ShopProtocol shopProtocol = new ShopProtocol();
            shopProtocol.setId(addShopProtocolReq.getShopId());
            shopProtocol.setChannelId(addShopProtocolReq.getShopChannelId());
            shopProtocol.setCurrency(addShopProtocolReq.getCurrency());
            shopProtocol.setSettleMethod(addShopProtocolReq.getSettleMethod());
            shopProtocol.setDecimal(addShopProtocolReq.getDecimal());
            shopProtocol.setRoundup(addShopProtocolReq.getRoundup());
            shopProtocol.setPrincipal(addShopProtocolReq.getPrincipal());
            shopProtocol.setImprest(addShopProtocolReq.getImprest());
            shopProtocol.setDeposit(addShopProtocolReq.getDeposit());
            shopProtocol.setContractStart(addShopProtocolReq.getContractStart());
            shopProtocol.setContractExpiry(addShopProtocolReq.getContractExpiry());
            if (addShopProtocolReq.getContractStart() != null && addShopProtocolReq.getContractExpiry() != null){
                shopProtocol.setContractEffective(DateUtils.formatDate(addShopProtocolReq.getContractStart(),DateConstant.DATEFORMAT_Y_M_D)+"-"+DateUtils.formatDate(addShopProtocolReq.getContractExpiry(),DateConstant.DATEFORMAT_Y_M_D));
            }
            shopProtocol.setBlockRule(CollectionUtils.isEmpty(addShopProtocolReq.getBlockRuleList()) ? null : blockRuleService.blockRuleList2str(addShopProtocolReq.getBlockRuleList()));
            shopProtocol.setParking(addShopProtocolReq.getParking());
            shopProtocol.setChildren(addShopProtocolReq.getChildren());
            shopProtocol.setNotice(addShopProtocolReq.getNotice());
            shopProtocol.setAccountName(addShopProtocolReq.getAccountName());
            shopProtocol.setOpeningBank(addShopProtocolReq.getOpeningBank());
            shopProtocol.setBankAccount(addShopProtocolReq.getBankAccount());
            shopProtocol.setChangePrice(addShopProtocolReq.getChangeRice());
            shopProtocol.setUpdateUser(SecurityUtils.getLoginName());
            shopProtocol.setDelFlag(DelFlagEnums.NORMAL.getCode());
            //block原因插入
            if (!CollectionUtils.isEmpty(addShopProtocolReq.getBlockRuleList())){
                blockReasonService.addReason(BlockReasonEnums.ReasonType.TYPE_SHOP_PROTOCOL.getCode(),addShopProtocolReq.getShopId(),addShopProtocolReq.getBlockRuleList());
            }
            //删除block的原因同步删除
            Wrapper delWrapper = new Wrapper() {
                @Override
                public String getSqlSegment() {
                    StringBuffer stringBuffer = new StringBuffer();
                    stringBuffer.append("where type = 'shop_protocol' and value ="+shopProtocol.getId());
                    if (!CollectionUtils.isEmpty(addShopProtocolReq.getBlockRuleList())){
                        List<String> blocks = addShopProtocolReq.getBlockRuleList().stream().map(blockRule -> blockRule.getRule()).collect(Collectors.toList());
                        stringBuffer.append(" and block not in ('" + StringUtils.join(blocks,"','") + "')");
                    }
                    return stringBuffer.toString();
                }
            };
            blockReasonService.delete(delWrapper);

            if (addShopProtocolReq.getProtocolId() == null){
                shopProtocol.setCreateTime(new Date());

                shopProtocol.setCreateUser(SecurityUtils.getLoginName());
                shopProtocol.setUpdateTime(new Date());
                shopProtocolService.addShopProtocol(shopProtocol);
            }else {
                shopProtocol.setId(addShopProtocolReq.getProtocolId());
                shopProtocolService.updShopProtocol(shopProtocol);
            }
            //商户账号密码新增修改
            if (StringUtils.isNotEmpty(addShopProtocolReq.getShopAccount()) && StringUtils.isNotEmpty(addShopProtocolReq.getShopPassword())){
                ShopAccount shopAccount = shopAccountService.getShopAccount(addShopProtocolReq.getShopId());
                EntityWrapper<ShopAccount> local = new EntityWrapper<>();
                local.eq("username",addShopProtocolReq.getShopAccount());
                ShopAccount  accountNum = shopAccountService.selectOne(local);
                if(null != accountNum && !accountNum.getShopId().equals(addShopProtocolReq.getShopId())  ){
                    result.setCode(200);
                    result.setMsg("当前账号已存在，请重新更换！");
                    log.info("当前账号已存在{}",addShopProtocolReq.toString());
                    return result;
                }


                Shop shop  = shopService.selectById(shopProtocol.getId());
                if(shopAccount == null ){
                    shopAccount = new ShopAccount();
                }

                shopAccount.setUsername(addShopProtocolReq.getShopAccount());
                shopAccount.setPassword(addShopProtocolReq.getShopPassword());
                shopAccount.setDelFlag(DelFlagEnums.NORMAL.getCode());
                shopAccount.setShopId(addShopProtocolReq.getShopId());
                shopAccount.setHotelId(shop.getHotelId());
                shopAccount.setAccountType(ShopAccountTypeEnums.SHOP.getCode());
                shopAccount.setUpdateUser(SecurityUtils.getLoginName());

                if (shopAccount.getId() == null ){
                    shopAccount.setCreateTime(new Date());
                    shopAccount.setCreateUser(SecurityUtils.getLoginName());
                    shopAccount.setUpdateTime(new Date());
                    shopAccount.setStatus(0);
                    shopAccountService.addShopAccount(shopAccount);
                }else {
                    shopAccount.setId(addShopProtocolReq.getShopAccountId());
                    shopAccountService.updShopAccount(shopAccount);
                }
            }
            //产品组下面的产品成本价格区间更新
            productGroupProductService.updCostByShopId(shopProtocol.getId());
            //产品组产品block日期更新添加
            groupProductBlockDateService.updBlockDateByShopId(shopProtocol.getId());
            result.setResult(shopProtocol);
        }catch (Exception e){
            log.error("商户协议操作失败",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    /**
     * 同步老系统商户到新系统中
     * @return
     */
    @SysGodDoorLog("同步老系统商户到新系统")
    @ApiOperation("同步老系统商户到新系统中")
    @PostMapping("/syncShopList")
    @Transactional(rollbackFor=Exception.class)
    public CommonResultVo<List<Shop>> syncShopList(){
        CommonResultVo<List<Shop>> result = new CommonResultVo<>();
        List<Shop> shopList = Lists.newLinkedList();
        CommonResultVo<List<com.colourfulchina.bigan.api.entity.Shop>> remoteResult = remoteShopService.selectShopList();
        if (!CollectionUtils.isEmpty(remoteResult.getResult())){
            for (com.colourfulchina.bigan.api.entity.Shop remoteShop : remoteResult.getResult()) {
                //礼品兑换不同步到新系统
                if (ShopTypeEnums.EXCHANGE.getCode().equals(remoteShop.getType())){
                    continue;
                }
                //根据老系统商户类型查询新系统商户类型和服务类型关系信息
                ShopTypeService shopTypeService = new ShopTypeService();
                if (remoteShop.getType() != null){
                    shopTypeService = shopTypeServiceService.selectByService(remoteShop.getType());
                }
                //根据老系统酒店id查询新系统酒店信息
                Hotel hotel = new Hotel();
                if (remoteShop.getHotelId() != null){
                    hotel = hotelService.selectByOldId(remoteShop.getHotelId().intValue());
                }
                //根据老系统城市id查询新系统城市信息
                City city = new City();
                if (remoteShop.getCityId() != null){
                    city = cityService.selectByOldId(remoteShop.getCityId().intValue());
                }
                //解析老系统中商户的more字段
                OldShopMoreVo oldShopMoreVo = new OldShopMoreVo();
                if (remoteShop.getMore() != null){
                    oldShopMoreVo = shopService.anslyisOldShopMore(remoteShop.getMore());
                }
                //根据老系统商户渠道id查询新系统商户渠道信息
                ShopChannel shopChannel = new ShopChannel();
                if (remoteShop.getChannelId() != null){
                    shopChannel = shopChannelService.selectByOldChannel(remoteShop.getChannelId().intValue());
                }

                //插入shop信息
                Shop shop = new Shop();
                if (remoteShop.getId() != null){
                    shop.setOldShopId(remoteShop.getId().intValue());
                }
                shop.setShopType(shopTypeService.getShopType());
                shop.setHotelId(hotel.getId());
                shop.setName(remoteShop.getName());
                shop.setNameEn(remoteShop.getNameEn());
                shop.setPy(PinYinUtils.getPinYinHeadChar(remoteShop.getName()));
                if (remoteShop.getHotelId() == null){
                    shop.setCityId(city.getId());
                }
                shop.setAddress(remoteShop.getAddress());
                shop.setAddressEn(remoteShop.getAddressEn());
                shop.setPhone(remoteShop.getPhone());
                shop.setNotes(remoteShop.getNotes());
                shop.setLevel(remoteShop.getLevel());
                if (StringUtils.isNotEmpty(remoteShop.getOpenTime())){
                    String openTime = remoteShop.getOpenTime().split("~")[0];
                    String closeTime = remoteShop.getOpenTime().split("~")[1];
                    shop.setOpenTime(openTime);
                    shop.setCloseTime(closeTime);
                }
                shop.setCheckInTime(oldShopMoreVo.getCheckInTime());
                shop.setCheckOutTime(oldShopMoreVo.getCheckOutTime());
                shop.setTips(oldShopMoreVo.getTips());
                if (remoteShop.getHotelId() == null){
                    shop.setShopNature(ShopNatureEnums.INDEPENDENT_SHOP.getCode());
                }else {
                    shop.setShopNature(ShopNatureEnums.SUBSIDIARY_SHOP.getCode());
                }
                shop.setDelFlag(DelFlagEnums.NORMAL.getCode());
                shop.setSummary(remoteShop.getSummary());
                shop.setShopStatus(ShopStatusEnums.UPSALES.getCode());
                shop.setCreateTime(new Date());

                shop.setCreateUser(SecurityUtils.getLoginName());
                shop.setUpdateTime(new Date());
                shop.setUpdateUser(SecurityUtils.getLoginName());
                shopService.insert(shop);
                shopList.add(shop);

                //针对健身的商户，在新系统中直接生成健身的规格
                if (ShopTypeEnums.GYM.getCode().equals(shopTypeService.getShopType())){
                    ShopItem shopItem = new ShopItem();
                    shopItem.setShopId(shop.getId());
                    shopItem.setServiceType(shopTypeService.getShopType());
                    shopItem.setServiceName(shopTypeService.getName());
                    shopItem.setName(remoteShop.getName());
                    shopItem.setOpenTime(StringUtils.isBlank(remoteShop.getOpenTime()) ? null : remoteShop.getOpenTime().split("~")[0]);
                    shopItem.setCloseTime(StringUtils.isBlank(remoteShop.getOpenTime()) ? null : remoteShop.getOpenTime().split("~")[1]);
                    shopItem.setEnable(0);
                    shopItem.setDelFlag(DelFlagEnums.NORMAL.getCode());
                    shopItem.setCreateTime(new Date());
                    shopItem.setCreateUser(SecurityUtils.getLoginName());
                    shopItem.setUpdateTime(new Date());
                    shopItem.setUpdateUser(SecurityUtils.getLoginName());
                    shopItem.setGift("F1");
                    shopItemService.insert(shopItem);
                }

                //插入商户协议信息
                ShopProtocol shopProtocol = new ShopProtocol();
                shopProtocol.setId(shop.getId());
                //如果老系统商户渠道为null，则默认为第三方渠道-1
                if (shopChannel.getId() == null){
                    shopProtocol.setChannelId(ThirdChannelEnums.THIRD_CHANNEL.getCode());
                }else {
                    shopProtocol.setChannelId(shopChannel.getId());
                }
                shopProtocol.setCurrency(remoteShop.getCurrency());
                shopProtocol.setSettleMethod(remoteShop.getSettleMethod());
                shopProtocol.setDecimal(oldShopMoreVo.getDecimal());
                shopProtocol.setRoundup(oldShopMoreVo.getRoundup());
                shopProtocol.setPrincipal(oldShopMoreVo.getPrincipal());
                shopProtocol.setContractExpiry(remoteShop.getContractExpiry());
                shopProtocol.setBlockRule(remoteShop.getBlock());
                shopProtocol.setDelFlag(DelFlagEnums.NORMAL.getCode());
                shopProtocol.setCreateTime(new Date());

                shopProtocol.setCreateUser(SecurityUtils.getLoginName());
                shopProtocol.setUpdateTime(new Date());
                shopProtocol.setUpdateUser(SecurityUtils.getLoginName());
                shopProtocolService.insert(shopProtocol);
            }
        }
        return result;
    }

    @SysGodDoorLog("获取商户信息")
    @ApiOperation("获取商户信息")
    @PostMapping("/selectShopInfoById")
    @Cacheable(value = "Shop",key = "'selectShopInfoById_'+#shopId",unless = "#result == null")
    public CommonResultVo<Shop> selectShopInfoById(Integer shopId){
        CommonResultVo<Shop> resultVo = new CommonResultVo<Shop>();
        if (shopId == null ){
            log.error("获取商品信息参数有误:{}",shopId);
            resultVo.setCode(200);
            resultVo.setMsg("获取商品信息异常");
            return resultVo;
        }
        Shop shop = shopService.selectById(shopId);
        resultVo.setResult(shop);
        return resultVo;
    }


    @SysGodDoorLog("获取商户详情信息")
    @ApiOperation("获取商户详情信息")
    @PostMapping("/selectShopOrderDetail")
    @Cacheable(value = "Shop",key = "'selectShopOrderDetail_'+#shopOrderDetailReq.goodsId+'_'+#shopOrderDetailReq.projectId+'_'+#shopOrderDetailReq.groupId+'_'+#shopOrderDetailReq.salesOrderId",unless = "#result == null")
    public CommonResultVo<ShopOrderDetailRes> selectShopOrderDetail(@RequestBody ShopOrderDetailReq shopOrderDetailReq){
        CommonResultVo<ShopOrderDetailRes> resultVo = new CommonResultVo<>();
        try {
            log.info("获取商户信息:{}", JSON.toJSON(shopOrderDetailReq));
            ShopOrderDetailRes shopOrderDetailRes = shopService.selectShopOrderDetail(shopOrderDetailReq);
            //获取不可预约日期
            QueryBookBlockRes queryBookBlockRes = blockRuleService.queryAllBlock(shopOrderDetailRes.getProductGroupProductId());
            if (queryBookBlockRes != null){
                shopOrderDetailRes.setClause(queryBookBlockRes.getBlockRule());
            }
            //获取商品使用规则
            List<GoodsClause> goodsClauseList = goodsClauseService.selectGoodsClauseById(shopOrderDetailReq.getProjectId());
            shopOrderDetailRes.setGoodsClauseList(goodsClauseList);
            resultVo.setResult(shopOrderDetailRes);
        }catch (Exception e){
            log.error("获取商品详细信息异常:{}",JSON.toJSONString(shopOrderDetailReq),e);
            resultVo.setCode(200);
            resultVo.setMsg("获取商品详细信息异常");
        }
        return resultVo;
    }

    @SysGodDoorLog("根据goodsId获取商品信息")
    @ApiOperation("根据goodsId获取商品信息")
    @PostMapping("/getShopInfoByGoodsId")
    @Cacheable(value = "Shop",key = "'getShopInfoByGoodsId_'+#goodsId")
    public CommonResultVo<Shop> getShopInfoByGoodsId(Integer goodsId){
        CommonResultVo<Shop> resultVo = new CommonResultVo<Shop>();
        Shop shop = shopService.getShopInfoByGoodsId(goodsId);
        resultVo.setResult(shop);
        return resultVo;
    }


    @SysGodDoorLog("获取重复的规格数据")
    @ApiOperation("获取重复的规格数据")
    @GetMapping("/getRepeatInfoVo")
    public CommonResultVo<List<RepeatInfoVo>> getRepeatInfoVo(){
        CommonResultVo<List<RepeatInfoVo>> resultVo = new CommonResultVo<List<RepeatInfoVo>>();
        List<RepeatInfoVo> repeatInfoVos = shopService.getRepeatInfoVo();
        resultVo.setResult(repeatInfoVos);
        return resultVo;
    }

    @ApiOperation("商户预约成功销售次数增加")
    @SysGodDoorLog("商户预约成功销售次数增加")
    @PostMapping("/shopSalesUp")
    public CommonResultVo<Boolean> shopSalesUp(@RequestBody Integer shopId){
        CommonResultVo<Boolean> result = new CommonResultVo<>();
        try {
            Assert.notNull(shopId,"商户id不能为空");
            Boolean b = shopService.shopSalesUp(shopId);
            result.setResult(b);
        }catch (Exception e){
            log.error("商户预约成功销售次数增加失败",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
            result.setResult(Boolean.FALSE);
        }
        return result;
    }

    @ApiOperation("商户查看点击次数增加")
    @SysGodDoorLog("商户查看点击次数增加")
    @PostMapping("/shopPointUp")
    public CommonResultVo<Boolean> shopPointUp(@RequestBody Integer shopId){
        CommonResultVo<Boolean> result = new CommonResultVo<>();
        try {
            Assert.notNull(shopId,"商户id不能为空");
            Boolean b = shopService.shopPointUp(shopId);
            result.setResult(b);
        }catch (Exception e){
            log.error("商户查看点击次数增加失败",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
            result.setResult(Boolean.FALSE);
        }
        return result;
    }

}