package com.colourfulchina.bigan.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.bigan.api.dto.GoodsInfo;
import com.colourfulchina.bigan.api.entity.Shop;
import com.colourfulchina.bigan.api.entity.ShopItem;
import com.colourfulchina.bigan.api.entity.ShopSection;
import com.colourfulchina.bigan.api.vo.RemoteUpdShopBaseMsgVo;
import com.colourfulchina.bigan.api.vo.ShopDetailVo;
import com.colourfulchina.bigan.api.vo.ShopSaveInfoVo;
import com.colourfulchina.bigan.enums.GoodsEnums;
import com.colourfulchina.bigan.mapper.ShopItemMapper;
import com.colourfulchina.bigan.mapper.ShopMapper;
import com.colourfulchina.bigan.mapper.ShopSectionMapper;
import com.colourfulchina.bigan.service.ShopService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Service
public class ShopServiceImpl extends ServiceImpl<ShopMapper,Shop> implements ShopService {

    @Autowired
    private ShopMapper shopMapper;

    @Autowired
    private ShopSectionMapper shopSectionMapper;

    @Autowired
    private ShopItemMapper shopItemMapper;

    /**
     * 根据酒店名称、门店名称和酒店城市检测是否存在门店
     * @param shop
     * @return flag(true:存在,false:不存在)
     */
    @Override
    public boolean checkShopByNameAndCityAndHotel(Shop shop) {
        boolean flag = true;
       List<Shop> shopList = shopMapper.checkShopByNameAndCityAndHotel(shop);
       if (shopList.size()==0 || shop == null){
           flag = false;
       }
        return flag;
    }

    /**
     * 获取序列中的主键
     * @return
     */
    @Override
    public long getIdForSeq() {
        return shopMapper.getIdForSeq();
    }

    /**
     * 插入商户信息，主键序列自增
     * @param shop
     * @return
     */
    @Override
    public boolean insert(Shop shop){
        shop.setId(shopMapper.getIdForSeq());
        return retBool(shopMapper.insert(shop));
    }
    /**
     * 检查酒店是否存在
     * @param tempshop
     * @return
     */
    @Override
    public Shop checkShopIsExist(Shop tempshop){
        Shop shop   =  shopMapper.checkShopIsExist(tempshop);
        return shop != null?shop :null;
    }

    /**
     * 获取商店下一个的值
     *
     * @return
     */
    @Override
    public Long selectShopSeqNextValue() {
        return shopMapper.selectShopSeqNextValue();
    }

    /**
     * 调用存储过程保存
     *
     * @param vo
     * @return
     */
    @Override
    public Shop saveShopInfo(ShopSaveInfoVo vo) {

        Shop shop=null;

        Map params = obj2Map(vo);
        shopMapper.saveShopInfo(params);
        if(params.get("id") != null ){
            shop = shopMapper.selectById((Serializable) params.get("id"));
        }else{
            log.info(params.get("error")+"");
        }

        return shop;
    }

    /**
     * 查询酒店
     *
     * @param goodsVo
     * @param serviceCode
     * @return
     */
    @Override
    public Shop selectListFor(GoodsInfo goodsVo, String serviceCode) {
        List<Shop> shopList = new ArrayList<>();
        Shop shop = null;
        if(serviceCode.equals(GoodsEnums.service.ACCOM.getCode())){
            //判断酒店是否存在
            Wrapper<Shop> shopWrapper=new Wrapper<Shop>() {
                @Override
                public String getSqlSegment() {
                    return "where type='"+GoodsEnums.service.ACCOM.getCode()
//                                        +"' and city='"+goodsVo.getRows().get(ProductImportSetMenuEnums.setMenu.CITY.getCellName())
                            +"' and hotel like '" + goodsVo.getHotelCh()
                            +"%' and name ='"+goodsVo.getShopCh()
                            +"'";
                }
            };
            shopList = shopMapper.selectList(shopWrapper);
        }else{
            //判断酒店是否存在
            Wrapper<Shop> shopWrapper=new Wrapper<Shop>() {
                @Override
                public String getSqlSegment() {
                    return "where  1=1"
//                                        +"and city='"+goodsVo.getRows().get(ProductImportSetMenuEnums.setMenu.CITY.getCellName())
                            +" and hotel='" + goodsVo.getHotelCh()
                            +"' and name like '"+goodsVo.getShopCh()
                            +"%'";
                }
            };
            shopList = shopMapper.selectList(shopWrapper);
        }
        if(null != shopList && shopList.size() == 1 ){
            shop = shopList.get(0);
//                        log.info("***数据一条***：{}-{}",goodsVo.getHotelCh(),goodsVo.getShopCh());
        }else{
//                        log.info("***数据多条***：{}-{}",goodsVo.getHotelCh(),goodsVo.getShopCh());
            for(Shop sh :shopList){
                if(sh.getType().equals(serviceCode)){
                    shop = sh;
                    break;
                }else{
                    shop  = sh;
                }
            }
        }

        return shop;
    }

    @Override
    public ShopDetailVo getShopDetail(Integer id) {
        final Shop shop = shopMapper.selectById(id);
        if (shop != null){
            ShopDetailVo shopDetailVo=new ShopDetailVo();
            shopDetailVo.setShop(shop);
            Wrapper<ShopItem> itemWrapper=new Wrapper<ShopItem>() {
                @Override
                public String getSqlSegment() {
                    return "where shop_id="+id;
                }
            };
            final List<ShopItem> shopItems = shopItemMapper.selectList(itemWrapper);
            shopDetailVo.setShopItemList(shopItems);
            Wrapper<ShopSection> sectionWrapper=new Wrapper<ShopSection>() {
                @Override
                public String getSqlSegment() {
                    return "where shop_id="+id;
                }
            };
            final List<ShopSection> shopSections = shopSectionMapper.selectList(sectionWrapper);
            shopDetailVo.setShopSectionList(shopSections);
            return shopDetailVo;
        }
        return null;
    }

    /**
     * 根据酒店id查询商户列表
     * @param hotelId
     * @return
     */
    @Override
    public List<Shop> selectListByHotelId(Integer hotelId) {
        Wrapper wrapper = new Wrapper() {
            @Override
            public String getSqlSegment() {
                return "where hotel_id = "+hotelId;
            }
        };
        return shopMapper.selectList(wrapper);
    }

    private static Map<String, String> obj2Map(Object obj) {

        Map<String, String> map = new HashMap<String, String>();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (int i = 0, len = fields.length; i < len; i++) {
            String varName = fields[i].getName();
            varName = varName.toLowerCase();//将key置为小写，默认为对象的属性
            try {
                boolean accessFlag = fields[i].isAccessible();
                fields[i].setAccessible(true);
                Object o = fields[i].get(obj);
                if (o != null) {
                    map.put(varName, o.toString());
                }else{
                    map.put(varName, null);
                }

                fields[i].setAccessible(accessFlag);
            } catch (IllegalArgumentException ex) {
                ex.printStackTrace();
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
            }
        }
        return map;
    }



//    public void add (){
//        if (shop == null){
//            isExsit = false;
//            log.info("***商户不存在***：未查到商户信息 row_id={}",goodsVo.getServiceCode());
//
//            ShopSaveInfoVo vo = new ShopSaveInfoVo();
//            Hotel hotelParam = new Hotel();
//            hotelParam.setName(map.get(ProductImportSetMenuEnums.setMenu.HOTEL_NAME_CN.getCellName())+"");
//            hotelParam.setCity(getValue(map,ProductImportSetMenuEnums.setMenu.CITY.getCellName()));
//            hotelParam.setAddress(getValue(map,ProductImportSetMenuEnums.setMenu.ADDRESS.getCellName()));
//            hotelParam.setNotes(getValue(map,ProductImportSetMenuEnums.setMenu.CLAUSE.getCellName()));
//            Wrapper<Hotel> hotelParamWapper = new Wrapper<Hotel>() {
//                @Override
//                public String getSqlSegment() {
//                    return "where city='"+map.get(ProductImportSetMenuEnums.setMenu.CITY.getCellName()) + "' and name='" + map.get(ProductImportSetMenuEnums.setMenu.HOTEL_NAME_CN.getCellName())+"' ";
//                }
//            };
//            Hotel hotel = hotelService.selectOne(hotelParamWapper);
////                        vo.setHotel_id(hotel.getId());
//            vo.setHotel(map.get(ProductImportSetMenuEnums.setMenu.HOTEL_NAME_CN.getCellName())+"");
////                        vo.setCity_id(hotel.getCityId().longValue());
//            vo.setType(serviceCode);
//            vo.setName(map.get(ProductImportSetMenuEnums.setMenu.SHOP_NAME.getCellName())+"");
//            vo.setAddress(getValue(map,ProductImportSetMenuEnums.setMenu.ADDRESS.getCellName()));
//
//            vo.setNotes(getValue(map,ProductImportSetMenuEnums.setMenu.CLAUSE.getCellName()));
////                        vo.setAccount("hotel_"+hotel.getId()+((int)Math.random()*100));
//
//
////                        vo.setAddress(map.get(ProductImportSetMenuEnums.setMenu.ADDRESS.getCellName())+"");
//            vo.setPhone(getValue(map,ProductImportSetMenuEnums.setMenu.PHONE.getCellName()));
////                        vo.setNotes(map.get(ProductImportSetMenuEnums.setMenu.CLAUSE.getCellName())+"");
////                        vo.setHotel(map.get(ProductImportSetMenuEnums.setMenu.HOTEL_NAME_CN.getCellName())+"");
////                        vo.setType(GoodsEnums.service.ACCOM.getCode());
////                        vo.setName(map.get(ProductImportSetMenuEnums.setMenu.SHOP_NAME.getCellName())+"");
////                        vo.setPwd("123456");
//            String hotelKey = vo.getHotel() +"-"+vo.getName();
//            String account  =getValue(map,ProductImportSetMenuEnums.setMenu.ACCOUNT.getCellName());//map.get(ProductImportSetMenuEnums.setMenu.ACCOUNT.getCellName())==null?null:map.get(ProductImportSetMenuEnums.setMenu.ACCOUNT.getCellName())+"";
//            String pwd  =getValue(map,ProductImportSetMenuEnums.setMenu.PWD.getCellName());//map.get(ProductImportSetMenuEnums.setMenu.PWD.getCellName())==null?null:map.get(ProductImportSetMenuEnums.setMenu.PWD.getCellName())+"";
//            if(StringUtils.isNotEmpty(account)&&StringUtils.isNotEmpty(pwd)){
//                vo.setAccount(account);
//                vo.setPwd(pwd);
//            }else{
//                if(serviceCodeTemp.equals(GoodsEnums.service.BUFFET.getCode())) {
//
//                    if(buffetAccountListMap.containsKey(hotelKey)){
//                        String[] user = buffetAccountListMap.get(hotelKey).toString().split("-");
//                        vo.setAccount(user[0]);
//                        vo.setPwd(user[1]);
//                    }else{
//                        log.error("***账户信息不存在***：{}-账户为空",hotelKey);
//                        continue;
//                    }
//                }
//                if(serviceCodeTemp.equals(GoodsEnums.service.SETMENU.getCode())){
//                    if(setMenuAccountListMap.containsKey(hotelKey)){
//                        String[] user = setMenuAccountListMap.get(hotelKey).toString().split("-");
//                        vo.setAccount(user[0]);
//                        vo.setPwd(user[1]);
//                    }else{
//                        log.error("***账户信息不存在***：{}-账户为空",hotelKey);
//                        continue;
//                    }
//                }
//                if(serviceCodeTemp.equals(GoodsEnums.service.TEA.getCode())){
//
//                    if(teaAccountListMap.containsKey(hotelKey)){
//                        String[] user = teaAccountListMap.get(hotelKey).toString().split("-");
//                        log.error("***账户信息不存在***：{}-账户为空",hotelKey);
//                        continue;
//                    }else{
//                        log.error("***账户信息不存在***：{}-账户为空",hotelKey);
//                        continue;
//                    }
//                }
//                //住宿不需要开账户
//                if(serviceCodeTemp.equals(GoodsEnums.service.ACCOM.getCode())){
//
//                }
//            }
//            if (hotel==null){
//
//                String cityName=map.get(ProductImportSetMenuEnums.setMenu.CITY.getCellName())+"";
//                Wrapper<City> cityWrapper=new Wrapper<City>() {
//                    @Override
//                    public String getSqlSegment() {
//                        return "where name='"+cityName+"'";
//                    }
//                };
//                final List<City> cityList = cityService.selectList(cityWrapper);
//                if (!CollectionUtils.isEmpty(cityList)){
//                    vo.setCity_id(cityList.get(0).getId());
//                }
//            }
//            if(null !=hotel){
//                vo.setHotel_id(hotel.getId());
////                                    vo.setHotel(hotel.getName());
//                vo.setCity_id(hotel.getCityId().longValue());
//                //vo.setAccount("hotel_"+hotel.getId()+((int)(Math.random()*100)));
//            }
//
//            if(accountListMap.containsKey(vo.getAccount())){
//                vo.setAccount(null );
//                vo.setPwd(null);
//            }
//            shop = shopService.saveShopInfo(vo);
//            if(null == shop){
//                resultVo.setCode(300);
//                resultVo.setMsg("***商户添加失败***：未成功添加商户 row_id="+goodsVo.getHotelCh()+goodsVo.toString());
//                log.error("***商户添加失败***：未成功添加商户 row_id="+goodsVo.getHotelCh()+goodsVo.toString());
//                resultMsg.put(goodsVo.getHotelCh(),"未成功添加商户");
//            }else {
//                accountListMap.put(vo.getAccount(), vo.getPwd());
//                if (serviceCodeTemp.equals(GoodsEnums.service.ACCOM.getCode())) {
//                    shop.setChannelId(7L);
//                    shop.setProtocol(1);
//                    shopService.updateById(shop);
//                }
//            }
//
////                                return resultVo;
//        }
//    }
}
