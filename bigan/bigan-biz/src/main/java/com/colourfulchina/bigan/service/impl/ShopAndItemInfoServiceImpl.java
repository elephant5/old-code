package com.colourfulchina.bigan.service.impl;

import com.colourfulchina.bigan.api.dto.GoodsInfo;
import com.colourfulchina.bigan.api.dto.ShopAndItemInfoDto;
import com.colourfulchina.bigan.api.entity.Goods;
import com.colourfulchina.bigan.api.entity.Shop;
import com.colourfulchina.bigan.api.entity.ShopItem;
import com.colourfulchina.bigan.enums.GoodsEnums;
import com.colourfulchina.bigan.enums.ProductImportSetMenuEnums;
import com.colourfulchina.bigan.mapper.GoodsMapper;
import com.colourfulchina.bigan.mapper.ShopItemMapper;
import com.colourfulchina.bigan.service.ShopAndItemInfoService;
import com.colourfulchina.bigan.service.ShopService;
import com.colourfulchina.bigan.service.ShopitemsService;
import com.colourfulchina.bigan.utils.ClfCommondUtils;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * User: Ryan
 * Date: 2018/7/30
 */
@Slf4j
@Service
public class ShopAndItemInfoServiceImpl implements ShopAndItemInfoService {


    @Autowired
    ShopService shopService;
    @Autowired
    ShopItemMapper shopItemMapper;
    @Autowired
    GoodsMapper goodsMapper;
    @Autowired
    ShopitemsService shopitemsService;

    public  static String rgexString = "国家法定节假日(及前夜)?|"
                + "元旦(长假)?及?(前夜)?|(农历)?春节(长假)?及?(前夜)?|清明节(长假)?及?(前夜)?|劳动节(长假)?及?(前夜)?|端午节?(长假)?及?(前夜)?|中秋节?(长假)?及?(前夜)?|国庆节?(长假)?及?(前夜)?|"
                + "新年除夕|(农历)?除夕夜?|农历新年(期间|前夜)?|公历新年(前夜|当日)?|"
                + "七夕(节)?|农历正月十五|"
                + "圣诞节(及?前夜|当天)?|情人节|平安夜|复活节|万圣节|感恩节|儿童节|母亲节|父亲节"
                 +"|\\d{1,2}/\\d{1,2}-\\d{1,2}/\\d{1,2}|(\\d{4}(/|年)?)?\\d{1,2}(/|月)\\d{1,2}日?|" ;

    public static String dateRgexString = "\\d{1,2}/\\d{1,2}-\\d{1,2}/\\d{1,2}|(\\d{4}(/|年)?)?\\d{1,2}(/|月)\\d{1,2}日?|";

    /**
     *  3.检查商品是否存在，检查逻辑为酒店+餐厅名+权益项目+餐段完全一致(shop.hotel+shop.name+shop.type,如果存在，
     *  则再比对餐段(shop_item,items都一致代表已存在))，不一致则新增goods,shop_item,shop_price
     * @param shopAndItemInfoDto
     */
    public void aboutShopAperate(ShopAndItemInfoDto shopAndItemInfoDto){
        Shop tempshop = Optional.ofNullable(shopAndItemInfoDto).
                        map(shop-> shop.getShop()).
                orElseThrow(()->new IllegalArgumentException("商品信息不能为空！！！"));


        Shop shop   =  shopService.checkShopIsExist(tempshop);
        if(null == shop){
//                shopService.saveShopInfo(vo)

        }else{

            Boolean isExistItem = false;
            StringBuffer goodsStr = new StringBuffer("<xml>");
            List<ShopItem> items = new ArrayList<>();

            Stream<ShopItem> stream= shopAndItemInfoDto.getShopItem().stream();
            stream.forEach(item -> {
                item = shopItemMapper.checkShopItemIsExist(item);
                if(null == item){

                    goodsStr.append("<item id=\"");
                    Long nextId = shopItemMapper.selectShopItemSeqNextValue();
                    goodsStr.append("\"");
                    goodsStr.append("name =\""+item.getName()+"\"");

                    item.setId(nextId);
                    item.setShopId(shop.getId());
                    goodsStr.append("</item>");
                }

            });
            goodsStr.append("</xml>");
            if(items.size() > 0 ){
                Goods goods = new Goods();
                goods.setItems(goodsStr.toString());
                Long goodsSeqId = goodsMapper.selectGoodsSeqNextValue();
                goods.setId(goodsSeqId);
                goods.setShopId(shop.getId());
//                goods.setItems(JSONArray.toJSONString(items));
//                goodsMapper.insert(goods);
//                goodsMapper.saveGoodsInfo();//调用存储过程保存
                for(ShopItem item :items){
//                    shopItemMapper.insert(item);
                }
//                保存儿童政策等信息
            }

        }

    }



//    private void saveShopItem(String serviceCode ,Map map, Shop shop,  String itemName ) {
//        ShopItem temp = new ShopItem();
//        temp.setName(itemName);
//        temp.setShopId(shop.getId());
//        temp.setType(shop.getType());
//
//        if(itemName.contains("午")){
////            temp.setOpentime(map.get(ProductImportEnums.buffet.LUNCH_OPEN_RANGE.getCellName())+"");
//            String time  =getValue(map,ProductImportSetMenuEnums.setMenu.LUNCH_OPEN_RANGE.getCellName());//map.get(ProductImportSetMenuEnums.setMenu.SUPPER_OPEN_RANGE.getCellName()) == null?null:map.get(ProductImportSetMenuEnums.setMenu.SUPPER_OPEN_RANGE.getCellName())+"";
//            temp.setOpentime(ClfCommondUtils.replaceTime(time));
//        }else{
////            temp.setOpentime(map.get(ProductImportEnums.buffet.SUPPER_OPEN_RANGE.getCellName())+"");
//            String time  =getValue(map,ProductImportSetMenuEnums.setMenu.SUPPER_OPEN_RANGE.getCellName());//map.get(ProductImportSetMenuEnums.setMenu.SUPPER_OPEN_RANGE.getCellName()) == null?null:map.get(ProductImportSetMenuEnums.setMenu.SUPPER_OPEN_RANGE.getCellName())+"";
//            temp.setOpentime(ClfCommondUtils.replaceTime(time));
//        }
//        final Long nextValue = shopItemMapper.selectShopItemSeqNextValue();
//        temp.setId(nextValue);
////        getBlockStr(shop,temp,itemName);
//        shopItemMapper.insert(temp);
//    }


    /**
     *保存和分析定制套餐，下午茶的items
     * @param serviceCode
     * @param map
     * @param items
     * @param shop
     * @param blockMap
     * @param vo
     * @param params
     * @return
     */
    @Override
    public List<ShopItem> analysisSetMenuItem(String serviceCode ,Map map,List<ShopItem> items,Shop shop ,Map blockMap,GoodsInfo vo ,Map params){
        params.put("giftCode",vo.getGiftCode());
        Set<String> itemNames =vo.getMeatName();
        List<ShopItem>tempItems = new ArrayList<>(0);
        Set<String> temp = new HashSet<>(0);

//        Set<String> result  =vo.getWeekBlockMap().keySet();

        //对比已经存在的items，提取出来，并保存新的item
        for(ShopItem item :items){
            if(itemNames.contains(item.getName()) ){
                temp.add(item.getName());
                tempItems.add(item);
            }
        }
        itemNames.removeAll(temp);
        if(itemNames.size() > 0 && false){
            itemNames.forEach(itemName ->{
                ShopItem item =  saveShopItemSetMenu( serviceCode ,map,shop,itemName,params,vo);
                if(item.getId()!= null){
                    tempItems.add(item);
                }
            });
        }

        return tempItems;
    }

    /**
     * 分析一下buffet的item
     *
     * @param map
     * @param items
     * @param shop
     * @param goodsVo
     * @return
     */
    @Override
    public List<ShopItem> analysisBuffetItem(String serviceCode ,Map map, List<ShopItem> items, Shop shop, GoodsInfo goodsVo,Map params) {
        ShopItem temp = new ShopItem();
        Set<String> excelItem = goodsVo.getWeekBlockMap().keySet();
        Map<String,String> blockMap = goodsVo.getWeekBlockMap();
        Set<String> addItems = new HashSet<>(0);
        List<ShopItem> tempItems = new ArrayList<>(0);
        if(null == items || items.size() == 0){
            for(String can :excelItem){

                addItems.add(can);
            }

        }
        if(excelItem.size() > items.size()){
            for(String can :excelItem){
                String name = can;
                String block  = blockMap.get(can);

              for(ShopItem item : items){
                  if(item.getName().contains(name)){
                      item.setBlock(block);
                      tempItems.add(item);
                  }else{
                      excelItem.add(can);
                  }
              }
            }

        }
        if(excelItem.size() < items.size()){

            for(ShopItem item :items){

                for(String can :excelItem){
                    String name  =can;
                    String block  = blockMap.get(can);
                    if(item.getName().contains(name)){
                        item.setBlock(block);
                        tempItems.add(item);
                    }
                }
            }
        }
        if(excelItem.size() == items.size()){

            for(ShopItem item :items){

                for(String can :excelItem){
                    String name  =can;
                    String block  = blockMap.get(can);
                    if(item.getName().contains(name)){
                        item.setName(name);
                        item.setBlock(block);
                        tempItems.add(item);
                    }
                }
            }
//            items.clear();
//            items = shopitemsService.getShopitemsByShopId(shop, serviceCode);

        }

        if(addItems.size() > 0 && false){
            for(String add : addItems){
                String name  =add;
                String block  = blockMap.get(add);
                // 新增商品
                if(name.contains("午餐")||name.contains("晚餐")){
                    ShopItem newItem = saveShopItemSetMenu( serviceCode ,map,shop,name,params,goodsVo);
                    if(StringUtils.isNotEmpty(newItem.getBlock())){
                        newItem.setBlock(newItem.getBlock());
                    }
//
                    tempItems.add(newItem);
                }
            }
        }
        return tempItems;
    }

//    private void setTempItemsBlock(GoodsInfo goodsVo,ShopItem item) {
////        if(StringUtils.isNotEmpty(item.getBlock())){
////            item.setBlock(item.getBlock()+","+goodsVo.getClauseBlock());
////        }else{
////            item.setBlock(goodsVo.getClauseBlock());
////        }
////    }

    /**
     * 分析住宿的items
     * @param serviceCode
     * @param map
     * @param items
     * @param shop
     * @param blockMap
     * @param vo
     * @param params
     * @return
     */
    @Override
    public List<ShopItem> analysisAccomItem(String serviceCode, Map map, List<ShopItem> items, Shop shop, Map blockMap, GoodsInfo vo, Map params) {
        params.put("giftCode",vo.getGiftCode());
        List<ShopItem>tempItems = new ArrayList<>(0);
        Set<String> temp = new HashSet<>(0);
        String roomType =vo.getRoomType();// getValue(map,ProductImportSetMenuEnums.setMenu.ROOM_TYPE.getCellName());
        String can_range_type = getValue(map,ProductImportSetMenuEnums.setMenu.CAN_RANGE_TYPE.getCellName());
        String bed_type = getValue(map,ProductImportSetMenuEnums.setMenu.BED_TYPE.getCellName());
        String blockCodeStr = vo.getWeekBlockMap().containsKey(roomType) ? vo.getWeekBlockMap().get(roomType)+"":null;
//        if(StringUtils.isNotEmpty(vo.getClauseBlock())){
//            List<String> listsDate= Arrays.asList(vo.getClauseBlock().split(","));
//
//            Collections.sort(listsDate);
//            blockCodeStr = Joiner.on(",").join(listsDate);
//        }

        for(ShopItem item :items){
            if(roomType.equals(item.getName())
                    && (item.getAddon() == can_range_type || (StringUtils.isNotEmpty(item.getAddon())&&  item.getAddon().equals(can_range_type)))
                    && (item.getNeeds() == bed_type || (StringUtils.isNotEmpty(item.getNeeds())&&  item.getNeeds().equals(bed_type)))
            ){

                item.setBlock(blockCodeStr == null ?null:blockCodeStr);
                tempItems.add(item);
                break;

            }else{
                temp.add(roomType);

            }
        }

        if(tempItems.size() == 0 && false){
            params.put("addon",can_range_type);//餐型
            params.put("needs",bed_type);//餐型
            params.put("blocks",blockCodeStr == null ?null : blockCodeStr);//
            ShopItem itemNew =  saveShopItemAccom( serviceCode ,shop,roomType,params);
            if(itemNew.getId()!= null){
                tempItems.add(itemNew);
            }
        }

        return tempItems;
    }

    /**
     * 分析单杯饮品的items
     *
     * @param serviceCode
     * @param map
     * @param items
     * @param shop
     * @param goodsVo
     * @param params
     * @return
     */
    @Override
    public List<ShopItem> analysisDrinkItem(String serviceCode, Map map, List<ShopItem> items, Shop shop, GoodsInfo goodsVo, Map params) {
        List<ShopItem>tempItems = new ArrayList<>(0);
        Set<String> temp = new HashSet<>(0);
        String tea_open_range = getValue(map,ProductImportSetMenuEnums.setMenu.TEA_OPEN_RANGE.getCellName());
        if(StringUtils.isNotEmpty(tea_open_range)){
            tea_open_range = ClfCommondUtils.replaceTime(tea_open_range);
        }
//        Set<String> tempNameSet = goodsVo.getBuffetWeekBlock();//block

        Map<String,String> tempNameSetTemp = goodsVo.getWeekBlockMap();

        for(ShopItem item :items){
            //判断住宿的item的名字，餐型，床型来匹配是否添加当前Item
            //&&
            //                    (item.getBlock() == tempNameSetTemp.get(item.getName())
            //                            || tempNameSetTemp.get(item.getName()) != null && item.getBlock() != null && item.getBlock().equals(tempNameSetTemp.get(item.getName())))
            if(tempNameSetTemp.containsKey(item.getName()) ){
                tempItems.add(item);
                break;
            }else{
                temp.add(goodsVo.getServiceName());

            }
        }

        if(tempItems.size() == 0){
            params.put("opentime",tea_open_range);//开餐时间
            params.put("blocks",tempNameSetTemp.get(goodsVo.getServiceName()) );//
            ShopItem itemNew =  saveDrinkAccom( serviceCode ,shop,goodsVo.getServiceName(),params);
            if(itemNew.getId()!= null){
                tempItems.add(itemNew);
            }
        }

        return tempItems;

    }

    private ShopItem saveShopItemAccom(String serviceCode,  Shop shop, String itemName,  Map params) {
        ShopItem temp = new ShopItem();
        temp.setName(itemName);
        temp.setShopId(shop.getId());
        temp.setType(serviceCode);
        temp.setNeeds(params.get("needs") == null ?null : params.get("needs")+"");
        temp.setAddon(params.get("addon") == null ?null :params.get("addon")+"");
        if(params.get("blocks") != null){
            temp.setBlock(params.get("blocks") == null ?null :params.get("blocks")+"");
        }
//        temp.setBlock();
        final Long nextValue = shopItemMapper.selectShopItemSeqNextValue();
        temp.setId(nextValue);
        shopItemMapper.insert(temp);
        return temp;
    }


    private ShopItem saveDrinkAccom(String serviceCode,  Shop shop, String itemName,  Map params) {
        ShopItem temp = new ShopItem();
        temp.setName(itemName);
        temp.setShopId(shop.getId());
        temp.setType(serviceCode);
        temp.setNeeds(params.get("needs") == null ?null : params.get("needs")+"");

        temp.setOpentime(params.get("opentime") == null ?null :params.get("opentime")+"");
        if(params.get("blocks") != null){
            temp.setBlock(params.get("blocks") == null ?null :params.get("blocks")+"");
        }
//        temp.setBlock();
        final Long nextValue = shopItemMapper.selectShopItemSeqNextValue();
        temp.setId(nextValue);
        shopItemMapper.insert(temp);
        return temp;
    }

    /**
     * 新增自助餐、下午茶、定制套餐
     * @param serviceCode
     * @param map
     * @param shop
     * @param itemName
     * @param params
     * @return
     */
    private ShopItem saveShopItemSetMenu(String serviceCode ,Map map, Shop shop,  String itemName ,Map params,GoodsInfo vo) {
        ShopItem temp = new ShopItem();
        temp.setName(itemName);
        temp.setShopId(shop.getId());
        temp.setType(serviceCode);

        if (serviceCode.equals(GoodsEnums.service.SETMENU.getCode())){
            if(itemName.contains("午")){
                String time  =getValue(map,ProductImportSetMenuEnums.setMenu.LUNCH_OPEN_RANGE.getCellName());//map.get(ProductImportSetMenuEnums.setMenu.LUNCH_OPEN_RANGE.getCellName()) == null?null:map.get(ProductImportSetMenuEnums.setMenu.LUNCH_OPEN_RANGE.getCellName())+"";
                if(StringUtils.isNotEmpty(time) && !time.equals("null")&& !time.equals("无")){
                    temp.setOpentime(ClfCommondUtils.replaceTime(time));
                }else {
                    log.error("{}的午餐开餐时间为空",shop.getId());
                }
            }else{
                String time  =getValue(map,ProductImportSetMenuEnums.setMenu.SUPPER_OPEN_RANGE.getCellName());//map.get(ProductImportSetMenuEnums.setMenu.SUPPER_OPEN_RANGE.getCellName()) == null?null:map.get(ProductImportSetMenuEnums.setMenu.SUPPER_OPEN_RANGE.getCellName())+"";
                if(StringUtils.isNotEmpty(time) && !time.equals("null")&& !time.equals("无")){
                    temp.setOpentime(ClfCommondUtils.replaceTime(time));
                }else {
                    log.error("{}的晚餐开餐时间为空",shop.getId());
                }

            }
        }

        if (serviceCode.equals(GoodsEnums.service.TEA.getCode())){
                String time  =getValue(map,ProductImportSetMenuEnums.setMenu.TEA_OPEN_RANGE.getCellName());//map.get(ProductImportSetMenuEnums.setMenu.TEA_OPEN_RANGE.getCellName()) == null?null:map.get(ProductImportSetMenuEnums.setMenu.TEA_OPEN_RANGE.getCellName())+"";
                if(StringUtils.isNotEmpty(time) && !time.equals("null")&& !time.equals("无")){
                    temp.setOpentime(ClfCommondUtils.replaceTime(time));
                }else {
                    log.error("{}的下午茶开餐时间为空",shop.getId());
                }


        }

        if(vo.getWeekBlockMap().containsKey(itemName)){
            temp.setBlock(vo.getWeekBlockMap().get(itemName));
        }
        //定制套餐的暂时结算价放：其他
        if(serviceCode.equals(GoodsEnums.service.SETMENU.getCode())){
            String giftCode = params.get("giftCode")+"";
            temp.setPrice("{\""+giftCode+"\":\"其他\"}");
        }
        final Long nextValue = shopItemMapper.selectShopItemSeqNextValue();
        temp.setId(nextValue);
//        String openWeekday = getValue(map,ProductImportSetMenuEnums.setMenu.OPENWEEKDAY.getCellName());//map.get(ProductImportSetMenuEnums.setMenu.OPENWEEKDAY.getCellName())+"";

        shopItemMapper.insert(temp);
        return temp;
    }

    public String getValue(Map map,String name){
//        map.get(ProductImportSetMenuEnums.setMenu.PACKAGENAME.getCellName())==null?null:map.get(ProductImportSetMenuEnums.setMenu.PACKAGENAME.getCellName())+"");
        String value  = map.get(name.toLowerCase()) == null?null:map.get(name.toLowerCase())+"";
        if(StringUtils.isNotEmpty(value)&&!value.equals("null")){
            return value.trim().replaceAll("\n","").replaceAll("\r","");
        }
        if(StringUtils.isNotEmpty(value)&&value.contains("null")){

            return null;
        }
        return null;
    }

    /**
     * 把节假日等转义成系统可以识别的block code
     * @param blcokText
     * @return 根据特定日期返回Block Code
     *      * 5/1	特定日期
     *      * 5/1-5/5	日期范围
     *      * 2016/5/1	特定完整日期
     *      * 2016/5/1-2016/5/5	含年份的完整日期范围
     *      * 2016/5/1-2016/5/5/W1-W2/W5/W7	含年份的完整日期范围内的某几周
     *      * EVE	特定农历(除夕), 等同于 C1/1-1
     *      * C1/5	具体的农历日期
     *      * C1/1-C1/16	农历日期范围
     *      * W1	周一
     *      * W1-W5	周一至周五
     *      * W1/W2-W3/W6-W7	复杂的星期组合
     *      * M3/5/7/W5-7	3/5/7月的周5至周7
     *      * M1-2/W5-7	1月到2月的周五至周日
     *      * 母亲节(五月的第二个星期天)
     *      * 父亲节(六月的第三个星期天)
     */
    public static String getBlockCodeWithBlockText(String blcokText){
        if(isDate(blcokText)){
            blcokText = blcokText.replaceAll("年","/").replaceAll("月","/").replaceAll("日","");
            return blcokText;
        }else{
            switch (blcokText) {
                case "一":return "W1";
                case "二":return "W2";
                case "三":return "W3";
                case "四":return "W4";
                case "五":return "W5";
                case "六":return "W6";
                case "七":return "W7";
                case "日":return "W7";
                case "天":return "W7";
                case "至":return "-";
                case "-":return "-";
                case "春节":return "C1/1-C1/6";
                case "农历春节": return "C1/1-C1/6";
                case "农历春节长假及前夜": return "EVE,C1/1-C1/6";
                case "农历新年期间": return "EVE,C1/1-C1/6";
                case "农历正月十五": return "C1/15";
                case "元旦": return "1/1";
                case "元旦长假": return "1/1";
                case "元旦长假及前夜": return "12/31-1/1";
                // 每年都要更新清明节日期
                case "清明节": return "4/5";
                case "清明节长假及前夜": return "4/4-4/7";
                case "劳动节": return "5/1";
                case "劳动节长假及前夜": return "4/30-5/1";
                case "中秋": return "C8/15";
                case "中秋节": return "C8/15";
                case "中秋节长假及前夜": return "C8/14-C8/15";
                case "端午": return "C5/5";
                case "端午节": return "C5/5";
                case "端午节长假及前夜": return "C5/4-C5/5";
                case "国庆": return "10/1-10/7";
                case "国庆节": return "10/1-10/7";
                case "国庆节长假及前夜": return "9/30-10/7";
                case "除夕夜": return "EVE";
                case "农历除夕": return "EVE";
                case "农历除夕夜": return "EVE";
                case "圣诞节": return "12/25";
                case "圣诞节前夜": return "12/24";
                case "圣诞节当天": return "12/25";
                case "平安夜": return "12/24";
                case "圣诞节及前夜": return "12/24-12/25";
                case "圣诞节当天及前夜": return "12/24-12/25";
                case "新年除夕": return "EVE";
                case "农历新年前夜": return "EVE";
                case "公历新年前夜": return "12/31";
                case "公历新年当日": return "1/1";
                case "公历新年": return "1/1";
                case "情人节": return "2/14";
                case "七夕": return "C7/7";
                case "七夕节": return "C7/7";
                case "儿童节": return "6/1";
                case "母亲节": return "";
                case "父亲节": return "";
                case "国家法定节假日(及前夜)" :return "";
                default:
                    return "";
            }
        }

    }


    public static String getWeekList(String b){
        List ssb = new ArrayList<>();
        for(String sss:b.replaceAll("[^一,二,三,四,五,六,七,日,末,至]", ",").split(",")){
            if (sss.length()>0) {
                String temp ="";
                if(sss.length()>1){
                    char[] ar =  sss.toCharArray();

                    for(int i =0;i<ar.length;i++){
                        temp= temp + getBlockCodeWithBlockText(ar[i]+"");
                    }
                    ssb.add(temp);
                }else{
//                    if(ssb.)
                    ssb.add(getBlockCodeWithBlockText(sss));
                }
            }
        }
       return  Joiner.on(",").join(ssb).replaceAll("-,","-");
    }




    /**
     * 正则表达式匹配两个指定字符串中间的内容
     * @param soap 指定字符串
     * @param rgex 字符串匹配正则
     * @return 根据正则匹配到的字符串列表
     */
    public static List<String> getSubUtil(String soap,String rgex){
        List<String> list = new ArrayList<>();
        // 匹配的模式
        Pattern pattern = Pattern.compile(rgex);
        Matcher m = pattern.matcher(soap);
        while (m.find()) {
            int i = 0;
            list.add(m.group(i));
            i++;
        }
        return list;
    }

    /**
     * 正则表达式匹配两个指定字符串中间的内容
     * @param str 指定字符串
     * @return 根据正则匹配到的字符串列表
     */
    public static boolean isDate(String str){
        Pattern pattern = Pattern.compile(dateRgexString);
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

    public static String getClausBlockStr(String clause){
        List<String> lists = ShopAndItemInfoServiceImpl.getSubUtil(clause, rgexString);;
        List<String> listsDate= new ArrayList<>();
        for (String reg : lists) {
            String str  = ShopAndItemInfoServiceImpl.getBlockCodeWithBlockText(reg);
            if(str.length() > 0){
                listsDate.add(str);
            }else {
//                log.info("***节假日未匹配正则日期***:" + reg);
            }

        }
        Collections.sort(listsDate);

        return Joiner.on(",").join(listsDate);
    }

//    public static void main(String[] args) {
//        Map<String,List<String>> map  = new HashMap();
//        String blcokText = "农历春节、春节、元旦长假、元旦长假及前夜、农历春节长假及前夜、清明节长假及前夜、元旦、劳动节长假及前夜、端午节长假及前夜、中秋节长假及前夜、国庆节长假及前夜、端午、端午节、国庆、国庆节、农历除夕、除夕夜、农历除夕夜、圣诞节及前夜,圣诞节，圣诞节当天,公历新年前夜";
//
//        String rgexString = "国家法定节假日|"
//                + "元旦(长假)?及?(前夜)?|(农历)?春节(长假)?及?(前夜)?|清明节(长假)?及?(前夜)?|劳动节(长假)?及?(前夜)?|端午节?(长假)?及?(前夜)?|中秋节?(长假)?及?(前夜)?|国庆节?(长假)?及?(前夜)?|"
//                + "新年除夕|(农历)?除夕夜?|农历新年(期间|前夜)?|公历新年(前夜|当日)?|"
//                + "七夕(节)?|农历正月十五|"
//                + "圣诞节(及?前夜|当天)?|情人节|平安夜|复活节|万圣节|感恩节|儿童节|母亲节|父亲节";
//
////        周[一二三四五六日末]至?(、，)?(周[一二三四五六日末])?[、|，]?(午餐(和|以?及)晚餐|晚餐(和|以?及)午餐|晚餐|午餐)?
//
//        List<String> lists = getSubUtil(blcokText,rgexString);;
//        List<String> listsdate = new ArrayList<>();
//        for (String string : lists) {
//            listsdate.add(getBlockCodeWithBlockText(string));
//        }
//        String blockCodeStr = Joiner.on(",").join(listsdate);
//        System.out.println(blockCodeStr);
////        Arrays.sort(listsdate.toArray());
//        Collections.sort(listsdate);
//        String blockCodeStr2 = Joiner.on(",").join(listsdate);
//        System.out.println(blockCodeStr2);

//        String str ="周日,周一至周三";
//        str = getWeekList(str);
//        System.out.println(str);
//    }

}
