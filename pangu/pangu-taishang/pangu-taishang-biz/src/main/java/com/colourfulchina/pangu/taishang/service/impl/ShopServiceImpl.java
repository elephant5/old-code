package com.colourfulchina.pangu.taishang.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.bigan.api.feign.RemoteShopService;
import com.colourfulchina.bigan.api.vo.RemoteAddShopVo;
import com.colourfulchina.bigan.api.vo.RemoteUpdShopBaseMsgVo;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.inf.base.vo.PageVo;
import com.colourfulchina.pangu.taishang.api.entity.City;
import com.colourfulchina.pangu.taishang.api.entity.Geo;
import com.colourfulchina.pangu.taishang.api.entity.Hotel;
import com.colourfulchina.pangu.taishang.api.entity.Shop;
import com.colourfulchina.pangu.taishang.api.enums.ShopNatureEnums;
import com.colourfulchina.pangu.taishang.api.enums.ShopTypeEnums;
import com.colourfulchina.pangu.taishang.api.vo.OldShopMoreVo;
import com.colourfulchina.pangu.taishang.api.vo.RepeatInfoVo;
import com.colourfulchina.pangu.taishang.api.vo.ShopBaseMsgVo;
import com.colourfulchina.pangu.taishang.api.vo.req.ShopListPageReq;
import com.colourfulchina.pangu.taishang.api.vo.req.ShopListReq;
import com.colourfulchina.pangu.taishang.api.vo.req.ShopOrderDetailReq;
import com.colourfulchina.pangu.taishang.api.vo.ShopVo;
import com.colourfulchina.pangu.taishang.api.vo.req.ShopPageListReq;
import com.colourfulchina.pangu.taishang.api.vo.res.HotelShopListRes;
import com.colourfulchina.pangu.taishang.api.vo.res.ShopListRes;
import com.colourfulchina.pangu.taishang.api.vo.res.ShopOrderDetailRes;
import com.colourfulchina.pangu.taishang.api.vo.res.ShopPageListRes;
import com.colourfulchina.pangu.taishang.api.vo.res.bigan.ShopDetailVo;
import com.colourfulchina.pangu.taishang.api.vo.res.bigan.ShopSection;
import com.colourfulchina.pangu.taishang.api.vo.res.shopProtocol.ShopProtocolRes;
import com.colourfulchina.pangu.taishang.constant.OldShopMoreConstant;
import com.colourfulchina.pangu.taishang.constant.ShopConstant;
import com.colourfulchina.pangu.taishang.mapper.GeoMapper;
import com.colourfulchina.pangu.taishang.mapper.ShopItemMapper;
import com.colourfulchina.pangu.taishang.mapper.ShopMapper;
import com.colourfulchina.pangu.taishang.mapper.ShopProtocolMapper;
import com.colourfulchina.pangu.taishang.service.CityService;
import com.colourfulchina.pangu.taishang.service.HotelService;
import com.colourfulchina.pangu.taishang.service.ShopService;
import com.colourfulchina.pangu.taishang.utils.PinYinUtils;
import com.colourfulchina.pangu.taishang.utils.ToInterfaceUtils;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class ShopServiceImpl extends ServiceImpl<ShopMapper,Shop> implements ShopService {

    @Autowired
    private ShopMapper shopMapper;

    @Autowired
    private HotelService hotelService;

    @Autowired
    private CityService cityService;

    @Autowired
    private GeoMapper geoMapper;

    @Autowired
    private RemoteShopService remoteShopService;

    @Autowired
    private ShopItemMapper shopItemMapper;

    @Autowired
    private ShopProtocolMapper shopProtocolMapper;

    /**
     * 商户列表模糊分页查询
     * @param pageVoReq
     * @return
     */
    @Override
    public PageVo<ShopPageListRes> findPageList(PageVo<ShopPageListReq> pageVoReq) {
        PageVo<ShopPageListRes> pageVoRes = new PageVo<>();
        List<ShopPageListRes> listRes = shopMapper.findPageList(pageVoReq,pageVoReq.getCondition());
        BeanUtils.copyProperties(pageVoReq,pageVoRes);
        return pageVoRes.setRecords(listRes);
    }

    /**
     * 查询所有商户列表
     * @return
     */
    @Override
    public List<ShopListRes> selectShopList() {
        return shopMapper.selectShopList();
    }
    /**
     * 查询所有商户列表
     * @return
     */
    @Override
    public List<ShopListRes> selectShopListByName(ShopListRes params) {
        return shopMapper.selectShopListByName(params);
    }

    /**
     * 模糊查询所有商户
     *
     * @param params
     * @return
     */
    @Override
    public List<ShopListRes> seachShopListByName(ShopListRes params) {
        return shopMapper.seachShopListByName(params);
    }

    /**
     * 获取重复的规格数据
     *
     * @return
     */
    @Override
    public List<RepeatInfoVo> getRepeatInfoVo() {
        return shopMapper.getRepeatInfoVo();
    }

    /**
     * 生成定位信息
     */
    @Override
    public void generateGeo() throws Exception{
        //获取商户
        List<Shop> shopList = shopMapper.selectCnList();
        if (!CollectionUtils.isEmpty(shopList)){
            for (Shop shop : shopList) {
                log.info("开始生成商户id为:{}",shop.getId());
                if (StringUtils.isNotBlank(shop.getAddress())){
                    if (shop.getGeoId() == null){
                        StringBuffer stringBuffer = new StringBuffer();
                        stringBuffer.append("http://api.map.baidu.com/geocoding/v3/?address=");
                        stringBuffer.append(URLEncoder.encode(shop.getAddress(),"UTF-8")+"&output=json&ak=7aiRGqB0g5NmjwAG1Yy2L2E23pe8Oa0I");
                        String res = ToInterfaceUtils.httpURLConectionGET(stringBuffer.toString());
                        JSONObject rowDate = JSONObject.parseObject(res);
                        if (rowDate.getString("status").equals("0")){
                            String lng = rowDate.getJSONObject("result").getJSONObject("location").getString("lng");
                            String lat = rowDate.getJSONObject("result").getJSONObject("location").getString("lat");
                            Geo geo = new Geo();
                            geo.setAddressCh(shop.getAddress());
                            geo.setAddressEn(shop.getAddressEn());
                            geo.setLat(lat);
                            geo.setLng(lng);
                            geoMapper.insert(geo);
                            shop.setGeoId(geo.getId());
                            shopMapper.updateById(shop);
                        }
                    }
                }
            }
        }
    }

    @Override
    public ShopDetailVo getShopDetail(Long id) {
        com.colourfulchina.pangu.taishang.api.vo.res.bigan.Shop shop = shopMapper.selectShopById(id);
        if(shop!=null){
            ShopDetailVo shopDetailVo=new ShopDetailVo();
            shopDetailVo.setShop(shop);

            List<Long> items = new ArrayList<Long>();
            items.add(shop.getId());
            List<com.colourfulchina.pangu.taishang.api.vo.res.bigan.ShopItem> shopItemList = shopItemMapper.selectShopItems(items);
            shopDetailVo.setShopItemList(shopItemList);

            List<ShopSection> shopSectionList = new ArrayList<ShopSection>();
            List<ShopProtocolRes> protocolResList = shopProtocolMapper.selectShopProtocol(shop.getId().intValue());

            if (protocolResList != null && protocolResList.size() > 0) {
                String children = protocolResList.get(0).getChildren();
                String parking = protocolResList.get(0).getParking();
                String notice = protocolResList.get(0).getNotice();
                ShopSection s1 = new ShopSection(shop.getId(), "儿童政策", children);
                ShopSection s2 = new ShopSection(shop.getId(), "泊车信息", parking);
                ShopSection s3 = new ShopSection(shop.getId(), "重要通知", notice);
                shopSectionList.add(s1);
                shopSectionList.add(s2);
                shopSectionList.add(s3);
                shopDetailVo.setShopSectionList(shopSectionList);
            }
            return shopDetailVo;
        }
        return null;
    }

    @Override
    public Boolean shopSalesUp(Integer shopId) throws Exception {
        if (null != shopId){
            String setStr = "sales_count = sales_count + 1";
            Wrapper wrapper = new Wrapper() {
                @Override
                public String getSqlSegment() {
                    return "where id ="+shopId;
                }
            };
            Integer i = shopMapper.updateForSet(setStr,wrapper);
            return retBool(i);
        }
        return false;
    }

    @Override
    public Boolean shopPointUp(Integer shopId) throws Exception {
        if (null != shopId){
            String setStr = "point_count = point_count + 1";
            Wrapper wrapper = new Wrapper() {
                @Override
                public String getSqlSegment() {
                    return "where id ="+shopId;
                }
            };
            Integer i = shopMapper.updateForSet(setStr,wrapper);
            return retBool(i);
        }
        return false;
    }

    /**
     * 查询商户基本信息
     * @param shopId
     * @return
     */
    @Override
    public ShopBaseMsgVo selectShopBaseMsg(Integer shopId) {
        return shopMapper.selectShopBaseMsg(shopId);
    }

    /**
     * 根据商户id查询商户vo
     * @param shopId
     * @return
     */
    @Override
    public ShopVo selectShopVoById(Integer shopId) {
        return shopMapper.selectShopVoById(shopId);
    }

    /**
     * 酒店关联商户查询
     * @param hotelId
     * @return
     */
    @Override
    public List<HotelShopListRes> hotelsShop(Integer hotelId) {
        return shopMapper.hotelsShop(hotelId);
    }

    /**
     * 解析老系统中shop表的more字段
     * @param more
     * @return
     */
    @Override
    public OldShopMoreVo anslyisOldShopMore(String more) {
        OldShopMoreVo oldShopMoreVo = new OldShopMoreVo();
        JSONObject jsonObject = JSON.parseObject(more);
        String principal = jsonObject.getString(OldShopMoreConstant.PRINCIPAL);
        String tips = jsonObject.getString(OldShopMoreConstant.TIPS);
        String decimal = jsonObject.getString(OldShopMoreConstant.DECIMAL);
        String roundup = jsonObject.getString(OldShopMoreConstant.ROUNDUP);
        String checkInTime = jsonObject.getString(OldShopMoreConstant.CHECK_IN_TIME);
        String checkOutTime = jsonObject.getString(OldShopMoreConstant.CHECK_OUT_TIME);
        String bookMinDays = jsonObject.getString(OldShopMoreConstant.BOOK_MIN_DAYS);
        String bookMaxDays = jsonObject.getString(OldShopMoreConstant.BOOK_MAX_DAYS);
        oldShopMoreVo.setPrincipal(principal);
        oldShopMoreVo.setTips(tips);
        if (decimal != null){
            oldShopMoreVo.setDecimal(Integer.valueOf(decimal));
        }
        if (roundup != null){
            oldShopMoreVo.setRoundup(roundup);
        }
        oldShopMoreVo.setCheckInTime(checkInTime);
        oldShopMoreVo.setCheckOutTime(checkOutTime);
        if (bookMinDays != null){
            oldShopMoreVo.setBookMinDays(Integer.valueOf(bookMinDays));
        }
        if (bookMaxDays != null){
            oldShopMoreVo.setBookMaxDays(Integer.valueOf(bookMaxDays));
        }
        return oldShopMoreVo;
    }

    /**
     * 解析老系统shop表中block字段
     * @param block
     * @return
     */
    @Override
    public String anslyisOldShopBlock(String block) {
        String newBlock = null;
        if (StringUtils.isNotEmpty(block)){
            //TODO
            String[] blockArr = block.split(", ");
            for (String s : blockArr) {
                
            }
        }
        return newBlock;
    }

    /**
     * 根据老系统商户id查询新系统商户id
     * @param oldId
     * @return
     */
    @Override
    public Shop selectByOldId(Integer oldId) {
        Wrapper<Shop> wrapper = new Wrapper<Shop>() {
            @Override
            public String getSqlSegment() {
                return "where old_shop_id ="+oldId;
            }
        };
        return shopMapper.selectList(wrapper).get(0);
    }

    /**
     * 根据酒店名称和商户名称查询商户
     * @param shopName
     * @param hotelName
     * @return
     */
    @Override
    public Shop checkShopByHotelAndName(String shopName, String hotelName) {
        Map<String,String> map = Maps.newHashMap();
        Hotel hotel = new Hotel();
        //hotelName不为空则检查酒店是否存在，为空则为独立商户
        if (StringUtils.isNotEmpty(hotelName)){
            hotel = hotelService.checkHotelIsExist(hotelName);
            //hotel不存在，则直接返回null
            if (hotel == null){
                return null;
            }
        }
        //如果为独立商户或者已存在酒店的附属商户，则设置酒店id为查询参数
        map.put("hotelId",hotel.getId()==null?null:hotel.getId()+"");
        //商户名称为空且酒店名称不为空，认为商户类型为住宿
        if (StringUtils.isEmpty(shopName) && StringUtils.isNotEmpty(hotelName)){
            map.put("shopName",ShopConstant.ACCOM_SHOP_NAME);
        }else {
            map.put("shopName",shopName);
        }
        List<Shop> shopList = shopMapper.checkShopByHotelAndName(map);
        return CollectionUtils.isEmpty(shopList) ? null:shopList.get(0);
    }

    /**
     * 新增商户
     * @param shop
     * @return
     */
    @Override
    public Shop addShop(Shop shop) {
        //出行和兑换券不同步到老系统
        if (!ShopTypeEnums.COUPON.getCode().equals(shop.getShopType())
                && !ShopTypeEnums.TRIP.getCode().equals(shop.getShopType())
                && !ShopTypeEnums.OTHER.getCode().equals(shop.getShopType())){
            com.colourfulchina.bigan.api.entity.Shop oldShop = remoteAddShop(shop);
            shop.setOldShopId(oldShop.getId().intValue());
        }
        shopMapper.insert(shop);
        return shop;
    }

    /**
     * 商户基础信息添加更新
     * @param shop
     * @return
     */
    @Override
    public Shop updShopBaseMsg(Shop shop) {
        com.colourfulchina.bigan.api.entity.Shop oldshop = remoteUpdShopBaseMsg(shop);
        shopMapper.updateById(shop);
        return shop;
    }

    @Override
    public ShopOrderDetailRes selectShopOrderDetail(ShopOrderDetailReq shopOrderDetailReq) {
        ShopOrderDetailRes shopOrderDetailRes = shopMapper.selectShopOrderDetail(shopOrderDetailReq);
        return shopOrderDetailRes;
    }

    @Override
    public Shop getShopInfoByGoodsId(Integer goodsId) {
        Shop shop = shopMapper.getShopInfoByGoodsId(goodsId);
        return shop;
    }

    /**
     * 商户基础信息添加修改同步到老系统
     * @param shop
     * @return
     */
    public com.colourfulchina.bigan.api.entity.Shop remoteUpdShopBaseMsg(Shop shop){
        //兑换券和出行类型商户不同步到老系统
        if (!ShopTypeEnums.COUPON.getCode().equals(shop.getShopType())
                && !ShopTypeEnums.TRIP.getCode().equals(shop.getShopType())
                && !ShopTypeEnums.OTHER.getCode().equals(shop.getShopType())){
            //根据新系统酒店id查询酒店信息
            Hotel hotel = new Hotel();
            if (shop.getHotelId() != null){
                hotel = hotelService.selectById(shop.getHotelId());
            }
            //根据shopid查询商户信息
            Shop shopFlag = shopMapper.selectById(shop.getId());
            RemoteUpdShopBaseMsgVo remoteUpdShopBaseMsgVo = new RemoteUpdShopBaseMsgVo();
            remoteUpdShopBaseMsgVo.setShopId(shopFlag.getOldShopId());
            remoteUpdShopBaseMsgVo.setShopName(shop.getName());
            remoteUpdShopBaseMsgVo.setShopNameEn(shop.getNameEn());
            if (shop.getShopType().equals(ShopTypeEnums.ACCOM.getCode())){
                remoteUpdShopBaseMsgVo.setShopPy(PinYinUtils.getPinYinHeadChar(hotel.getNameCh()));
                remoteUpdShopBaseMsgVo.setShopTitle(hotel.getNameCh());
            }else {
                if (shop.getShopNature().equals(ShopNatureEnums.SUBSIDIARY_SHOP.getCode())){
                    remoteUpdShopBaseMsgVo.setShopPy(PinYinUtils.getPinYinHeadChar(hotel.getNameCh())+","+shop.getName());
                    remoteUpdShopBaseMsgVo.setShopTitle(hotel.getNameCh()+"|"+shop.getName());
                }else {
                    remoteUpdShopBaseMsgVo.setShopPy(PinYinUtils.getPinYinHeadChar(shop.getName()));
                    remoteUpdShopBaseMsgVo.setShopTitle(shop.getName());
                }
            }
            //如果商户为附属商户，城市同酒店城市
            if (shop.getShopNature().equals(ShopNatureEnums.SUBSIDIARY_SHOP.getCode())){
                City city = cityService.selectById(hotel.getCityId());
                remoteUpdShopBaseMsgVo.setCityId(city.getOldCityId());
            }else {
                City city = cityService.selectById(shop.getCityId());
                remoteUpdShopBaseMsgVo.setCityId(city.getOldCityId());
            }
            remoteUpdShopBaseMsgVo.setAddress(shop.getAddress());
            remoteUpdShopBaseMsgVo.setAddressEn(shop.getAddressEn());
            remoteUpdShopBaseMsgVo.setPhone(shop.getPhone());
            remoteUpdShopBaseMsgVo.setOpenTime(shop.getOpenTime());
            remoteUpdShopBaseMsgVo.setCloseTime(shop.getCloseTime());
            remoteUpdShopBaseMsgVo.setCheckInTime(shop.getCheckInTime());
            remoteUpdShopBaseMsgVo.setCheckOutTime(shop.getCheckOutTime());
            remoteUpdShopBaseMsgVo.setMinBookDays(shop.getMinBookDays());
            remoteUpdShopBaseMsgVo.setMaxBookDays(shop.getMaxBookDays());
            remoteUpdShopBaseMsgVo.setTips(shop.getTips());
            remoteUpdShopBaseMsgVo.setNotes(shop.getNotes());
            remoteUpdShopBaseMsgVo.setLevel(shop.getLevel());
            CommonResultVo<com.colourfulchina.bigan.api.entity.Shop> remoteResult = remoteShopService.remoteUpdShopBaseMsg(remoteUpdShopBaseMsgVo);
            return remoteResult.getResult();
        }
        return null;
    }

    /**
     * 新增商户远程同步
     * @param shop
     * @return
     */
    public com.colourfulchina.bigan.api.entity.Shop remoteAddShop(Shop shop){
        //出行和兑换券不同步到老系统
        if (!ShopTypeEnums.COUPON.getCode().equals(shop.getShopType()) && !ShopTypeEnums.TRIP.getCode().equals(shop.getShopType())){
            //根据新系统酒店id查询酒店信息
            Hotel hotel = new Hotel();
            if (shop.getHotelId() != null){
                hotel = hotelService.selectById(shop.getHotelId());
            }
            //根据新系统城市id查询城市信息
            City city = new City();
            if (shop.getShopNature().equals(ShopNatureEnums.SUBSIDIARY_SHOP.getCode())){
                city = cityService.selectById(hotel.getCityId());
            }else {
                city = cityService.selectById(shop.getCityId());
            }
            RemoteAddShopVo remoteAddShopVo = new RemoteAddShopVo();
            remoteAddShopVo.setHotelId(hotel.getOldHotelId());
            remoteAddShopVo.setCityId(city.getOldCityId());
            remoteAddShopVo.setShopType(shop.getShopType());
            remoteAddShopVo.setShopName(shop.getName());
            remoteAddShopVo.setAddress(shop.getAddress());
            remoteAddShopVo.setNotes(shop.getNotes());
            if (shop.getShopType().equals(ShopTypeEnums.ACCOM.getCode())){
                remoteAddShopVo.setShopPy(PinYinUtils.getPinYinHeadChar(hotel.getNameCh()));
            }else {
                if (shop.getShopNature().equals(ShopNatureEnums.SUBSIDIARY_SHOP.getCode())){
                    remoteAddShopVo.setShopPy(PinYinUtils.getPinYinHeadChar(hotel.getNameCh()+","+shop.getName()));
                }else {
                    remoteAddShopVo.setShopPy(PinYinUtils.getPinYinHeadChar(shop.getName()));
                }
            }
            CommonResultVo<com.colourfulchina.bigan.api.entity.Shop> remoteResult = remoteShopService.remoteAddShop(remoteAddShopVo);
            return remoteResult.getResult();
        }
        return null;
    }
}