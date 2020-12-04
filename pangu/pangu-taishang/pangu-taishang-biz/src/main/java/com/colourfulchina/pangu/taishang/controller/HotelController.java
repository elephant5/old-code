package com.colourfulchina.pangu.taishang.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.colourfulchina.god.door.api.annotation.SysGodDoorLog;
import com.colourfulchina.god.door.api.util.SecurityUtils;
import com.colourfulchina.inf.base.entity.SysOperateLogDetail;
import com.colourfulchina.inf.base.enums.SysOperateLogEnums;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.inf.base.vo.PageVo;
import com.colourfulchina.pangu.taishang.api.entity.*;
import com.colourfulchina.pangu.taishang.api.enums.DelFlagEnums;
import com.colourfulchina.pangu.taishang.api.enums.ShopAccountTypeEnums;
import com.colourfulchina.pangu.taishang.api.vo.HotelDetailVo;
import com.colourfulchina.pangu.taishang.api.vo.HotelVo;
import com.colourfulchina.pangu.taishang.api.vo.req.*;
import com.colourfulchina.pangu.taishang.api.vo.res.HotelDetailRes;
import com.colourfulchina.pangu.taishang.api.vo.res.HotelLogRes;
import com.colourfulchina.pangu.taishang.api.vo.res.HotelPageListRes;
import com.colourfulchina.pangu.taishang.api.vo.res.HotelShopListRes;
import com.colourfulchina.pangu.taishang.api.vo.res.hotel.HotelInfoQueryRes;
import com.colourfulchina.pangu.taishang.constant.SysLogConstant;
import com.colourfulchina.pangu.taishang.service.*;
import com.colourfulchina.pangu.taishang.utils.PinYinUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/hotel")
@Slf4j
@Api(value = "酒店相关Controller",tags = {"酒店操作接口"})
public class HotelController {
    @Autowired
    private HotelService hotelService;
    @Autowired
    private HotelPortalService hotelPortalService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private RemoteHotelService remoteHotelService;
    @Autowired
    private CityService cityService;
    @Autowired
    private HotelPortalImgService hotelPortalImgService;
    @Autowired
    private GeoService geoService;
    @Autowired
    private SysOperateLogService sysOperateLogService;
    @Autowired
    private CountryService countryService;

    @Autowired
    private ShopAccountService shopAccountService;

    /**
     * 酒店列表分页查询接口
     * @param pageVoReq
     * @return
     */
    @SysGodDoorLog("酒店列表模糊分页查询接口")
    @ApiOperation("酒店列表模糊分页查询接口")
    @PostMapping("/selectPageList")
    public CommonResultVo<PageVo<HotelPageListRes>> selectPageList(@RequestBody PageVo<HotelPageListReq> pageVoReq){
        CommonResultVo<PageVo<HotelPageListRes>> result = new CommonResultVo();
        PageVo<HotelPageListRes> pageVoRes = hotelService.findPageList(pageVoReq);
        result.setResult(pageVoRes);
        return result;
    }

    /**
     * 酒店名修改记录分页查询接口
     * @param hotelId
     * @return
     */
    @SysGodDoorLog("酒店名修改记录分页查询接口")
    @ApiOperation("酒店名修改记录分页查询接口")
    @PostMapping("/selectNameLogPageList")
    @Cacheable(value = "Hotel",key = "'selectNameLogPageList_'+#hotelId",unless = "#result == null")
    public CommonResultVo<PageVo<HotelLogRes>> selectNameLogPageList(@RequestBody Integer hotelId){
        CommonResultVo<PageVo<HotelLogRes>> result = new CommonResultVo<>();
        PageVo<HotelLogRes> pageRes = new PageVo<>();
        if (hotelId == null){
            result.setCode(200);
            result.setMsg("酒店名修改记录参数有误！");
            return result;
        }
        PageVo<SysOperateLogDetail> PageVo = new PageVo<>();
        Map map = Maps.newHashMap();
        map.put(SysLogConstant.LOG_TABLE_NAME,Hotel.class.getAnnotation(TableName.class).value());
        map.put(SysLogConstant.LOG_OPT_TYPE,SysOperateLogEnums.Type.UPDATE.getCode());
        map.put(SysLogConstant.LOG_ROW_KEY,hotelId);
        try {
            map.put(SysLogConstant.LOG_HOTEL_NAME_CH,Hotel.class.getDeclaredField("nameCh").getAnnotation(TableField.class).value());
            map.put(SysLogConstant.LOG_HOTEL_NAME_EN,Hotel.class.getDeclaredField("nameEn").getAnnotation(TableField.class).value());
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        PageVo.setCondition(map);
        pageRes = sysOperateLogService.queryHotelNameLogPage(PageVo);
        result.setResult(pageRes);
        return result;
    }

    /**
     * 酒店中文名修改记录分页查询接口
     * @param hotelId
     * @return
     */
    @SysGodDoorLog("酒店中文名修改记录分页查询接口")
    @ApiOperation("酒店中文名修改记录分页查询接口")
    @PostMapping("/selectNameChLogPageList")
    @Cacheable(value = "Hotel",key = "'selectNameChLogPageList_'+#hotelId",unless = "#result == null")
    public CommonResultVo<PageVo<SysOperateLogDetail>> selectNameChLogPageList(@RequestBody Integer hotelId){
        CommonResultVo<PageVo<SysOperateLogDetail>> result = new CommonResultVo<>();
        if (hotelId == null){
            result.setCode(200);
            result.setMsg("酒店中文名修改记录参数有误！");
            return result;
        }
        PageVo<SysOperateLogDetail> PageVo = new PageVo<>();
        Map map = Maps.newHashMap();
        map.put(SysLogConstant.LOG_TABLE_NAME,Hotel.class.getAnnotation(TableName.class).value());
        map.put(SysLogConstant.LOG_OPT_TYPE,SysOperateLogEnums.Type.UPDATE.getCode());
        map.put(SysLogConstant.LOG_ROW_KEY,hotelId);
        try {
            map.put(SysLogConstant.LOG_FIELD_NAME,Hotel.class.getDeclaredField("nameCh").getAnnotation(TableField.class).value());
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        PageVo.setCondition(map);
        PageVo = sysOperateLogService.querySysOperateLogDetailPage(PageVo);
        result.setResult(PageVo);
        return result;
    }

    /**
     * 酒店英文名修改记录分页查询接口
     * @param hotelId
     * @return
     */
    @SysGodDoorLog("酒店英文名修改记录分页查询接口")
    @ApiOperation("酒店英文名修改记录分页查询接口")
    @PostMapping("/selectNameEnLogPageList")
    @Cacheable(value = "Hotel",key = "'selectNameEnLogPageList_'+#hotelId",unless = "#result == null")
    public CommonResultVo<PageVo<SysOperateLogDetail>> selectNameEnLogPageList(@RequestBody Integer hotelId){
        CommonResultVo<PageVo<SysOperateLogDetail>> result = new CommonResultVo<>();
        if (hotelId == null){
            result.setCode(200);
            result.setMsg("酒店英文名修改记录参数有误！");
            return result;
        }
        PageVo<SysOperateLogDetail> PageVo = new PageVo<>();
        Map map = Maps.newHashMap();
        map.put(SysLogConstant.LOG_TABLE_NAME,Hotel.class.getAnnotation(TableName.class).value());
        map.put(SysLogConstant.LOG_OPT_TYPE, SysOperateLogEnums.Type.UPDATE.getCode());
        map.put(SysLogConstant.LOG_ROW_KEY,hotelId);
        try {
            map.put(SysLogConstant.LOG_FIELD_NAME,Hotel.class.getDeclaredField("nameEn").getAnnotation(TableField.class).value());
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        PageVo.setCondition(map);
        PageVo = sysOperateLogService.querySysOperateLogDetailPage(PageVo);
        result.setResult(PageVo);
        return result;
    }

    /**
     * 根据酒店中文名称查询酒店信息
     * @param hotelName
     * @return
     */
    @SysGodDoorLog("根据酒店中文名称查询酒店信息")
    @ApiOperation("根据酒店中文名称查询酒店信息,用于商户新增第一步选择关联商户")
    @PostMapping("/selectByHotelName")
    @Cacheable(value = "Hotel",key = "'selectByHotelName_'+#hotelName",unless = "#result == null")
    public CommonResultVo<Hotel> selectByHotelName(@RequestBody String hotelName){
        CommonResultVo<Hotel> result = new CommonResultVo<>();
        Hotel hotel = hotelService.selectByHotelName(hotelName);
        result.setResult(hotel);
        return result;
    }
    /**
     * 根据酒店中文名称查询酒店信息
     * @param hotelName
     * @return
     */
    @SysGodDoorLog("根据酒店名称模糊搜索列表")
    @ApiOperation("根据酒店名称模糊搜索列表")
    @PostMapping("/selectByHotelNameList")
    @Cacheable(value = "Hotel",key = "'selectByHotelNameList_'+#hotelName",unless = "#result == null")
    public CommonResultVo<List<Hotel>> selectByHotelNameList(@RequestBody String hotelName){
        CommonResultVo<List<Hotel>> result = new CommonResultVo<>();
        List<Hotel> hotelList = hotelService.selectByHotelNameList(hotelName);
        result.setResult(hotelList);
        return result;
    }
    /**
     * 酒店详情信息接口
     * @param hotelId
     * @return
     *
     */
    @SysGodDoorLog("酒店详情信息查询接口")
    @ApiOperation("酒店详情信息查询接口")
    @PostMapping("/hotelDetail")
    @Cacheable(value = "Hotel",key = "'hotelDetail_'+#hotelId",unless = "#result == null")
    public CommonResultVo<HotelDetailRes> hotelDetail(@RequestBody Integer hotelId){
        CommonResultVo<HotelDetailRes> result = new CommonResultVo<>();
        HotelDetailRes hotelDetailRes = new HotelDetailRes();
        if (hotelId == null){
            result.setCode(200);
            result.setMsg("参数有误");
            log.info("酒店详情传入参数有误{}",hotelId);
            return result;
        }
        HotelDetailVo hotelDetailVo = hotelService.queryHotelDetail(hotelId);
        ShopAccount hotelAccount  = shopAccountService.getHotelAccount(hotelDetailVo.getHotel().getId());
//        if(null != hotelAccount){
//            hotelDetailVo.setAccountType(hotelAccount.getAccountType());
//            hotelDetailVo.setUsername(hotelAccount.getUsername());
//            hotelDetailVo.setPassword(hotelAccount.getPassword());
//        }
        if (hotelDetailVo != null){
            HotelVo hotelVo = new HotelVo();
            if(null != hotelAccount){
                hotelVo.setAccountType(hotelAccount.getAccountType());
                hotelVo.setUsername(hotelAccount.getUsername());
                hotelVo.setPassword(hotelAccount.getPassword());
            }
            City city = cityService.selectById(hotelDetailVo.getHotel().getCityId());

            BeanUtils.copyProperties(hotelDetailVo.getHotel(),hotelVo);
            hotelVo.setCountryId(city.getCountryId());
            hotelDetailRes.setHotel(hotelVo);
            hotelDetailRes.setHotelPortalVoList(hotelDetailVo.getHotelPortalVoList());
            result.setResult(hotelDetailRes);
        }else {
            result.setCode(200);
            result.setMsg("酒店详情查询有误");
            log.info("酒店详情查询有误-id-{}",hotelId);
        }
        return result;
    }

    /**
     * 酒店关联商户查询接口
     * @param hotelId
     * @return
     */
    @SysGodDoorLog("酒店关联商户列表查询接口")
    @ApiOperation("酒店关联商户列表查询接口")
    @PostMapping("/hotelsShop")
    @Cacheable(value = "Hotel",key = "'hotelsShop_'+#hotelId",unless = "#result == null")
    public CommonResultVo<List<HotelShopListRes>> hotelsShop(@RequestBody Integer hotelId){
        CommonResultVo<List<HotelShopListRes>> result = new CommonResultVo<>();
        if (hotelId == null){
            result.setCode(200);
            result.setMsg("参数有误");
            log.info("酒店关联商户参数有误{}",hotelId);
            return result;
        }
        List<HotelShopListRes> shopList = shopService.hotelsShop(hotelId);
        result.setResult(shopList);
        return result;
    }

    /**
     * 酒店新增接口
     * @param hotel
     * @return
     */
    @SysGodDoorLog("酒店新增接口")
    @PostMapping("/addHotel")
    @ApiOperation("酒店新增接口")
    public CommonResultVo<Hotel> addHotel(@RequestBody Hotel hotel){
        CommonResultVo<Hotel> result = new CommonResultVo<>();
        hotelService.addHotel(hotel);
        result.setResult(hotel);
        return result;
    }

    /**
     * 酒店基本信息修改接口
     * @param updHotelBaseMsgReq
     * @return
     */
    @SysGodDoorLog("酒店基本信息修改接口")
    @PostMapping("/updateHotelBaseMsg")
    @Transactional(rollbackFor=Exception.class)
    @ApiOperation("酒店基本信息修改接口")
    @CacheEvict(value = {"Hotel","Shop"},allEntries = true)
    public CommonResultVo<Hotel> updateHotelBaseMsg(@RequestBody UpdHotelBaseMsgReq updHotelBaseMsgReq){
        CommonResultVo<Hotel> result = new CommonResultVo<>();
        try {
            if (updHotelBaseMsgReq.getId() == null){
                result.setCode(200);
                result.setMsg("参数异常");
                log.info("酒店基本信息修改参数异常{}",updHotelBaseMsgReq.toString());
                return result;
            }
            ShopAccount account = shopAccountService.getHotelAccount(updHotelBaseMsgReq.getId());

            if(StringUtils.isNotBlank(updHotelBaseMsgReq.getUsername()) ){
                EntityWrapper<ShopAccount> local = new EntityWrapper<>();
                local.eq("username",updHotelBaseMsgReq.getUsername());
                int  accountNum = shopAccountService.selectCount(local);
                if(accountNum >= 1 && account == null ){
                    result.setCode(200);
                    result.setMsg("当前账号已存在，请重新更换！");
                    log.info("当前账号已存在{}",updHotelBaseMsgReq.toString());
                    return result;
                }
                if(StringUtils.isBlank(updHotelBaseMsgReq.getPassword())){
                    result.setCode(200);
                    result.setMsg("酒店账号和密码必须同时输入！");
                    log.info("酒店账号和密码必须同时输入{}",updHotelBaseMsgReq.toString());
                    return result;
                }else{

                    if(null == account){
                        account = new ShopAccount();
                        account.setAccountType(ShopAccountTypeEnums.HOTEL.getCode());
                        account.setHotelId(updHotelBaseMsgReq.getId());
                        account.setUsername(updHotelBaseMsgReq.getUsername());
                        account.setPassword(updHotelBaseMsgReq.getPassword());
                        account.setStatus(0);
                        shopAccountService.insert(account);
                    }
                }

            }
            if(account != null){
                account.setUsername(updHotelBaseMsgReq.getUsername());
                account.setPassword(updHotelBaseMsgReq.getPassword());
                shopAccountService.updateById(account);
            }


            //获取城市信息
            City city = cityService.selectById(updHotelBaseMsgReq.getCityId());
            Country country = countryService.selectById(city.getCountryId());
            Hotel hotel = hotelService.selectById(updHotelBaseMsgReq.getId());
            hotel.setNameCh(updHotelBaseMsgReq.getHotelNameCh());
            hotel.setNameEn(updHotelBaseMsgReq.getHotelNameEn());
            hotel.setStar(updHotelBaseMsgReq.getStar());
            hotel.setCityId(updHotelBaseMsgReq.getCityId());
            hotel.setAddressCh(updHotelBaseMsgReq.getAddressCh());
            hotel.setAddressEn(updHotelBaseMsgReq.getAddressEn());
            hotel.setUpdateUser(SecurityUtils.getLoginName());
            if (StringUtils.isNotEmpty(updHotelBaseMsgReq.getHotelNameCh())){
                hotel.setNamePy(PinYinUtils.getPinYinHeadChar(updHotelBaseMsgReq.getHotelNameCh()));
            }
            if (city.getCountryId().equals("CN")){
                hotel.setOversea(0);
            }else {
                hotel.setOversea(1);
            }
            //检查酒店中文名是否重复
            Hotel nameChFlag = hotelService.checkHotelByNameCh(hotel);
            if (nameChFlag != null){
                result.setCode(200);
                result.setMsg("酒店中文名已存在");
                return result;
            }
            //检查酒店英文名是否重复
            if (StringUtils.isNotEmpty(hotel.getNameEn())){
                Hotel nameEnFlag = hotelService.checkHotelByNameEn(hotel);
                if (nameEnFlag != null){
                    result.setCode(200);
                    result.setMsg("酒店英文名已存在");
                    return result;
                }
            }
            //定位信息
            if (StringUtils.isNotBlank(updHotelBaseMsgReq.getCoordinate())){
                Geo hotelGeo = new Geo();
                hotelGeo.setCity(city.getNameCh());
                hotelGeo.setCountry(country.getId());
                hotelGeo.setLng(updHotelBaseMsgReq.getCoordinate().split(",")[0]);
                hotelGeo.setLat(updHotelBaseMsgReq.getCoordinate().split(",")[1]);
                hotelGeo.setAddressCh(updHotelBaseMsgReq.getAddressCh());
                hotelGeo.setAddressEn(updHotelBaseMsgReq.getAddressEn());
                if (hotel.getGeoId() == null){
                    //插入
                    hotelGeo = geoService.addGeo(hotelGeo);
                    hotel.setGeoId(hotelGeo.getId());
                }else {
                    //判断是否改变 做update操作
                    Geo oldGeo = geoService.selectById(hotel.getGeoId());
                    if (!oldGeo.getLat().equals(hotelGeo.getLat()) || !oldGeo.getLng().equals(hotelGeo.getLng())){
                        oldGeo.setCity(hotelGeo.getCity());
                        oldGeo.setCountry(hotelGeo.getCountry());
                        oldGeo.setLng(hotelGeo.getLng());
                        oldGeo.setLat(hotelGeo.getLat());
                        oldGeo.setAddressCh(hotelGeo.getAddressCh());
                        oldGeo.setAddressEn(hotelGeo.getAddressEn());
                        geoService.updGeo(oldGeo);
                    }
                }
            }else {
                hotel.setGeoId(null);
            }
            hotelService.updateById(hotel);
            //同步修改SqlServer酒店
            HotelDetailVo hotelDetailVo = hotelService.queryHotelDetail(hotel.getId());
            hotelService.syncOldHotel(hotelDetailVo);
            result.setResult(hotel);
        }catch (Exception e){
            log.error("酒店基本信息修改失败",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    /**
     * 酒店名称修改接口
     * @param updHotelNameReq
     * @return
     */
    @SysGodDoorLog("酒店列表酒店名称修改接口")
    @PostMapping("updHotelName")
    @ApiOperation("酒店列表酒店名称修改接口")
    @CacheEvict(value = {"Hotel","Shop"},allEntries = true)
    public CommonResultVo<Hotel> updHotelName(@RequestBody UpdHotelNameReq updHotelNameReq){
        CommonResultVo<Hotel> result = new CommonResultVo();
        if (updHotelNameReq.getId() == null || StringUtils.isEmpty(updHotelNameReq.getHotelNameCh())){
            result.setCode(200);
            result.setMsg("参数异常");
            return result;
        }
        Hotel hotel = hotelService.selectById(updHotelNameReq.getId());
        hotel.setNameCh(updHotelNameReq.getHotelNameCh());
        hotel.setNameEn(updHotelNameReq.getHotelNameEn());
        hotel.setNamePy(PinYinUtils.getPinYinHeadChar(updHotelNameReq.getHotelNameCh()));

        hotel.setUpdateUser(SecurityUtils.getLoginName());
        //检查酒店中文名是否重复
        Hotel nameChFlag = hotelService.checkHotelByNameCh(hotel);
        if (nameChFlag != null){
            result.setCode(200);
            result.setMsg("酒店中文名已存在");
            return result;
        }
        //检查酒店英文名是否重复
        if (StringUtils.isNotEmpty(hotel.getNameEn())){
            Hotel nameEnFlag = hotelService.checkHotelByNameEn(hotel);
            if (nameEnFlag != null){
                result.setCode(200);
                result.setMsg("酒店英文名已存在");
                return result;
            }
        }
        hotelService.updateById(hotel);
        //同步修改SqlServer酒店
        HotelDetailVo hotelDetailVo = hotelService.queryHotelDetail(hotel.getId());
        hotelService.syncOldHotel(hotelDetailVo);
        result.setResult(hotel);
        return result;
    }
    /**
     * 酒店探索章节增加/修改接口
     * @param optHotelPortalReq
     * @return
     */
    @SysGodDoorLog("酒店探索章节增加/修改接口")
    @PostMapping("/optHotelPortal")
    @Transactional(rollbackFor=Exception.class)
    @ApiOperation("酒店探索章节增加/修改接口")
    @CacheEvict(value = {"Hotel","Shop"},allEntries = true)
    public CommonResultVo<HotelPortal> optHotelPortal(@RequestBody OptHotelPortalReq optHotelPortalReq){
        CommonResultVo<HotelPortal> result = new CommonResultVo<>();
        if (optHotelPortalReq.getHotelId() == null){
            result.setCode(200);
            result.setMsg("参数异常");
            log.info("酒店探索章节编辑异常{}",optHotelPortalReq.toString());
            return result;
        }
        HotelPortal hotelPortal = new HotelPortal();
        if (optHotelPortalReq.getId() != null){
            hotelPortal.setId(optHotelPortalReq.getId());
            hotelPortal.setUpdateUser(SecurityUtils.getLoginName());
        }else {
            hotelPortal.setCreateTime(new Date());
            hotelPortal.setCreateUser(SecurityUtils.getLoginName());
        }
        hotelPortal.setHotelId(optHotelPortalReq.getHotelId());
        hotelPortal.setTitle(optHotelPortalReq.getTitle());
        hotelPortal.setContent(optHotelPortalReq.getContent());
        hotelPortal.setDelFlag(DelFlagEnums.NORMAL.getCode());
        hotelPortalService.insertOrUpdate(hotelPortal);
        //同步修改SqlServer酒店
        HotelDetailVo hotelDetailVo = hotelService.queryHotelDetail(optHotelPortalReq.getHotelId());
        hotelService.syncOldHotel(hotelDetailVo);
        result.setResult(hotelPortal);
        return result;
    }

    /**
     * 酒店探索章节删除接口
     * @param delHotelPortalReq
     * @return
     */
    @SysGodDoorLog("酒店探索章节删除接口")
    @PostMapping("/delHotelPortal")
    @Transactional(rollbackFor=Exception.class)
    @ApiOperation("酒店探索章节删除接口")
    @CacheEvict(value ={"Hotel","Shop"},allEntries = true)
    public CommonResultVo delHotelPortal(@RequestBody DelHotelPortalReq delHotelPortalReq){
        CommonResultVo result = new CommonResultVo<>();
        if (delHotelPortalReq.getId() == null || delHotelPortalReq.getHotelId() == null){
            result.setCode(200);
            result.setMsg("参数异常");
            log.info("酒店探索章节删除异常");
            return result;
        }
        HotelPortal hotelPortal = hotelPortalService.selectById(delHotelPortalReq.getId());
        if (hotelPortal != null && hotelPortal.getHotelId().compareTo(delHotelPortalReq.getHotelId()) == 0){
            hotelPortal.setDelFlag(DelFlagEnums.DELETE.getCode());
            hotelPortalService.updateById(hotelPortal);
        }
        //同步修改SqlServer酒店
        HotelDetailVo hotelDetailVo = hotelService.queryHotelDetail(delHotelPortalReq.getHotelId());
        hotelService.syncOldHotel(hotelDetailVo);
        result.setResult(true);
        return result;
    }

    /**
     * 老系统sqlserver酒店同步mysql
     * @return
     */
    @SysGodDoorLog("老系统酒店同步新系统")
    @PostMapping("/syncHotelList")
    @Transactional(rollbackFor=Exception.class)
    @ApiOperation("老系统酒店同步新系统")
    public CommonResultVo<List<Hotel>> syncHotelList(){
        CommonResultVo<List<Hotel>> result = new CommonResultVo<>();
        List<Hotel> hotelList = Lists.newLinkedList();
        CommonResultVo<List<com.colourfulchina.bigan.api.entity.Hotel>> remoteResult = remoteHotelService.selectHotelList();
        if (!CollectionUtils.isEmpty(remoteResult.getResult())){
            for (com.colourfulchina.bigan.api.entity.Hotel remoteHotel : remoteResult.getResult()) {
                Wrapper cityWrapper = new Wrapper() {
                    @Override
                    public String getSqlSegment() {
                        return "where old_city_id ="+remoteHotel.getCityId();
                    }
                };
                Wrapper geoWrapper = new Wrapper() {
                    @Override
                    public String getSqlSegment() {
                        return "where old_id ="+remoteHotel.getGeoId();
                    }
                };
                City city = cityService.selectOne(cityWrapper);
                Geo geo = geoService.selectOne(geoWrapper);
                Hotel hotel = new Hotel();
                hotel.setOldHotelId(Integer.valueOf(remoteHotel.getId()+""));
                hotel.setNameCh(remoteHotel.getName());
                hotel.setNameEn(remoteHotel.getNameEn());
                hotel.setNamePy(remoteHotel.getPy());
                if (city != null){
                    hotel.setCityId(city.getId());
                }
                hotel.setOversea(remoteHotel.getOversea());
                hotel.setAddressCh(remoteHotel.getAddress());
                hotel.setAddressEn(remoteHotel.getAddressEn());
                hotel.setPhone(remoteHotel.getPhone());
                hotel.setNotes(remoteHotel.getNotes());
                hotel.setSummary(remoteHotel.getSummary());
                if (geo != null){
                    hotel.setGeoId(geo.getId());
                }
                hotel.setDelFlag(DelFlagEnums.NORMAL.getCode());
                hotel.setCreateTime(new Date());
                hotel.setCreateUser(SecurityUtils.getLoginName());
                hotelService.insert(hotel);
                hotelList.add(hotel);
                if (!StringUtils.isEmpty(remoteHotel.getPortal())){
                    JSONObject jsonObject = JSONObject.parseObject(remoteHotel.getPortal());
                    JSONArray jsonArray = jsonObject.getJSONArray("sections");
                    if (jsonArray.size()!=0){
                        for (Object o : jsonArray) {
                            JSONObject json = (JSONObject) o;
                            String title = json.getString("title");
                            JSONArray ja = json.getJSONArray("pics");
                            String content = json.getString("content");
                            HotelPortal hotelPortal = new HotelPortal();
                            hotelPortal.setHotelId(hotel.getId());
                            hotelPortal.setTitle(title);
                            hotelPortal.setContent(content);
                            hotelPortal.setCreateTime(new Date());
                            hotelPortal.setCreateUser(SecurityUtils.getLoginName());
                            hotelPortal.setDelFlag(DelFlagEnums.NORMAL.getCode());
                            hotelPortalService.insert(hotelPortal);
                            //图片
                            if (ja.size()!=0){
                                for (Object o1 : ja) {
                                    String pic = (String) o1;
                                    HotelPortalImg hotelPortalImg = new HotelPortalImg();
                                    hotelPortalImg.setDelFlag(DelFlagEnums.NORMAL.getCode());
                                    hotelPortalImg.setHotelPortalId(hotelPortal.getId());
                                    hotelPortalImg.setImage(pic);
                                    hotelPortalImg.setCreateTime(new Date());
                                    hotelPortalImg.setCreateUser(SecurityUtils.getLoginName());
                                    hotelPortalImgService.insert(hotelPortalImg);
                                }
                            }
                        }
                    }
                }
            }
        }
        result.setResult(hotelList);
        return result;
    }

    @SysGodDoorLog("根据商户ID查询酒店详细信息")
    @ApiOperation("根据商户ID查询酒店详细信息")
    @PostMapping("/selectHotelByShopId")
    @Cacheable(value = "Hotel",key = "'selectHotelByShopId_'+#shopId",unless = "#result == null")
    public CommonResultVo<HotelInfoQueryRes> selectHotelByShopId(Integer shopId){
        CommonResultVo<HotelInfoQueryRes> resultVo = new CommonResultVo<HotelInfoQueryRes>();
        try {
            HotelInfoQueryRes hotelInfoQueryRes = hotelService.selectHotelByShopId(shopId);
            resultVo.setResult(hotelInfoQueryRes);
        }catch (Exception e){
            log.error("根据商户ID查询酒店详细信息异常:",e);
            resultVo.setCode(200);
            resultVo.setMsg("根据商户ID查询酒店详细信息异常");
        }
        return resultVo;
    }

}