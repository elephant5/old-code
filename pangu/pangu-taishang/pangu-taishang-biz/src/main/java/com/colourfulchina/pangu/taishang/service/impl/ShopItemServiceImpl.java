package com.colourfulchina.pangu.taishang.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.bigan.api.feign.RemoteShopItemService;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.pangu.taishang.api.entity.Product;
import com.colourfulchina.pangu.taishang.api.entity.ProductGroupProduct;
import com.colourfulchina.pangu.taishang.api.entity.Shop;
import com.colourfulchina.pangu.taishang.api.entity.ShopItem;
import com.colourfulchina.pangu.taishang.api.enums.ShopTypeEnums;
import com.colourfulchina.pangu.taishang.api.vo.SettleVo;
import com.colourfulchina.pangu.taishang.api.vo.ShopItemPriceVo;
import com.colourfulchina.pangu.taishang.api.vo.ShopItemSettleExpressVo;
import com.colourfulchina.pangu.taishang.api.vo.res.ShopItemPriceRes;
import com.colourfulchina.pangu.taishang.api.vo.res.ShopItemRes;
import com.colourfulchina.pangu.taishang.api.vo.res.shop.ShopListQueryResVo;
import com.colourfulchina.pangu.taishang.api.vo.res.shopItem.*;
import com.colourfulchina.pangu.taishang.mapper.ProductMapper;
import com.colourfulchina.pangu.taishang.mapper.ShopItemMapper;
import com.colourfulchina.pangu.taishang.mapper.ShopMapper;
import com.colourfulchina.pangu.taishang.service.BlockRuleService;
import com.colourfulchina.pangu.taishang.service.ShopItemService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class ShopItemServiceImpl extends ServiceImpl<ShopItemMapper,ShopItem> implements ShopItemService {
    @Autowired
    private ShopItemMapper shopItemMapper;
    @Autowired
    private BlockRuleService blockRuleService;
    private final RemoteShopItemService remoteShopItemService;
    @Autowired
    private ShopMapper shopMapper;
    @Autowired
    private ProductMapper productMapper;

    private static final Map<Integer,String> WEEK_MAP= Maps.newHashMap();
    //星期中净价包括的值
    private static final String CONTAIN_VAL = "1";
    static {
        WEEK_MAP.put(1,"monday");
        WEEK_MAP.put(2,"tuesday");
        WEEK_MAP.put(3,"wednesday");
        WEEK_MAP.put(4,"thursday");
        WEEK_MAP.put(5,"friday");
        WEEK_MAP.put(6,"saturday");
        WEEK_MAP.put(7,"sunday");
    }
    /**
     * 根据商户id查询规格列表
     * @param shopId
     * @return
     */
    @Override
    public List<ShopItem> selectListByShopId(Integer shopId) {
        Wrapper wrapper = new Wrapper() {
            @Override
            public String getSqlSegment() {
                return "where shop_id ="+shopId+" and del_flag = '0'";
            }
        };
        return shopItemMapper.selectList(wrapper);
    }

    /**
     * 新增规格
     * @param shopItem
     * @return
     * @throws Exception
     */
    @Override
    public ShopItem add(ShopItem shopItem) throws Exception {
        Shop shop = shopMapper.selectById(shopItem.getShopId());
        //出行和兑换券不同步到老系统
        if (!ShopTypeEnums.COUPON.getCode().equals(shop.getShopType())
                && !ShopTypeEnums.TRIP.getCode().equals(shop.getShopType())
                && !ShopTypeEnums.OTHER.getCode().equals(shop.getShopType())){
            //老系统规格同步新增
            com.colourfulchina.bigan.api.entity.ShopItem oldShopItem = addOldShopItem(shopItem);
            shopItem.setOldItemId(oldShopItem.getId().intValue());
        }
        shopItemMapper.insert(shopItem);
        return shopItem;
    }

    /**
     * 远程更新老系统商户规格
     * @param shopItem
     * @return
     * @throws Exception
     */
    @Override
    public com.colourfulchina.bigan.api.entity.ShopItem remoteUpd(ShopItem shopItem) throws Exception {
        Shop shop = shopMapper.selectById(shopItem.getShopId());
        //出行和兑换券不同步到老系统
        if (!ShopTypeEnums.COUPON.getCode().equals(shop.getShopType())
                && !ShopTypeEnums.TRIP.getCode().equals(shop.getShopType())
                && !ShopTypeEnums.OTHER.getCode().equals(shop.getShopType())){
            com.colourfulchina.bigan.api.entity.ShopItem oldShopItem = updOldShopItem(shopItem);
            return oldShopItem;
        }
        return null;
    }

    /**
     * 解析老系统shopItem中的price字段
     * @param price
     * @return
     */
    @Override
    public ShopItemPriceVo analysisPrice(String price) {
        ShopItemPriceVo shopItemPriceVo = new ShopItemPriceVo();
        //净价列表
        List<Map<String,String>> netList = Lists.newLinkedList();
        //服务费
        BigDecimal serviceFee = null;
        //税费
        BigDecimal taxation = null;
        JSONObject jsonObject = JSON.parseObject(price);
        String net = jsonObject.getString("net");
        String fee = jsonObject.getString("fee");
        String vat = jsonObject.getString("vat");
        String gift2F1 = jsonObject.getString("2F1");
        String gift3F1 = jsonObject.getString("3F1");
        String giftD5 = jsonObject.getString("D5");
        String giftF1 = jsonObject.getString("F1");
        String giftF2 = jsonObject.getString("F2");
        //解析净价
        if (StringUtils.isNotBlank(net)){
            String netStr = net.trim();
            //净价为一个数字，则范围为整周
            if (NumberUtils.isNumber(netStr)){
                Map<String,String> map = Maps.newHashMap();
                map.put("net_price",netStr);
                map.put("monday",CONTAIN_VAL);
                map.put("tuesday",CONTAIN_VAL);
                map.put("wednesday",CONTAIN_VAL);
                map.put("thursday",CONTAIN_VAL);
                map.put("friday",CONTAIN_VAL);
                map.put("saturday",CONTAIN_VAL);
                map.put("sunday",CONTAIN_VAL);
                netList.add(map);
            }else {
                final String[] nets = netStr.split(";");
                for (String n : nets) {
                    Map<String,String> map = Maps.newHashMap();
                    final String[] ns = n.split(":");
                    String weekStr = ns[0].replaceAll("\n", "");
                    String priceStr = ns[1].trim();
                    map.put("net_price",priceStr);
                    //净价为一个星期范围
                    if (weekStr.contains("-")){
                        int num1 = NumberUtils.toInt(weekStr.substring(1,2));
                        int num2 = NumberUtils.toInt(weekStr.substring(4,5));
                        if (num1>num2){
                            for (int i = num1;i <= 7;i++){
                                map.put(WEEK_MAP.get(i),CONTAIN_VAL);
                            }
                            for (int i = 1;i<= num2;i++){
                                map.put(WEEK_MAP.get(i),CONTAIN_VAL);
                            }
                        }else {
                            for (int i = num1;i<=num2;i++){
                                map.put(WEEK_MAP.get(i),CONTAIN_VAL);
                            }
                        }
                    }else {
                        //净价为一个指定的星期
                        int num = NumberUtils.toInt(weekStr.substring(1,2));
                        map.put(WEEK_MAP.get(num),CONTAIN_VAL);
                    }
                    netList.add(map);
                }
            }
        }

        //解析服务费
        if (StringUtils.isNotBlank(fee)){
            String feeStr=fee.trim();
            if (feeStr.endsWith("%")){
                serviceFee = new BigDecimal(feeStr.replaceAll("%","")).divide(new BigDecimal(100));
            }else {
                serviceFee = new BigDecimal(feeStr);
            }
        }

        //解析税费
        if (StringUtils.isNotBlank(vat) && "true".equalsIgnoreCase(vat.trim())){
            taxation = new BigDecimal("0.06");
        }

        shopItemPriceVo.setNetList(netList);
        shopItemPriceVo.setServiceFee(serviceFee);
        shopItemPriceVo.setTaxation(taxation);
        shopItemPriceVo.setGift2F1(gift2F1);
        shopItemPriceVo.setGift3F1(gift3F1);
        shopItemPriceVo.setGiftD5(giftD5);
        shopItemPriceVo.setGiftF1(giftF1);
        shopItemPriceVo.setGiftF2(giftF2);
        return shopItemPriceVo;
    }

    /**
     * 解析老系统shopItem中的price字段中的结算规则
     * @param settleExpress
     * @return
     */
    @Override
    public List<ShopItemSettleExpressVo> analysisSettleExpress(String settleExpress) {
        List<ShopItemSettleExpressVo> resultList = Lists.newLinkedList();
        //去空格
        settleExpress = settleExpress.replaceAll("\\s","");
        //去掉\n
        settleExpress = settleExpress.replaceAll("\\\\n","");
        //将小写的w全部格式化为大写
        settleExpress = settleExpress.replaceAll("w","W");
        //将中文；格式化为英文;
        settleExpress = settleExpress.replaceAll("；",";");
        //将中文：格式化为英文:
        settleExpress = settleExpress.replaceAll("：",":");
        //将中文（格式化为英文(
        settleExpress = settleExpress.replaceAll("（","(");
        //将中文）格式化为英文)
        settleExpress = settleExpress.replaceAll("）",")");
        if (!"无".equals(settleExpress) && !"其他".equals(settleExpress) && !"0".equals(settleExpress)){
            String[] settleRules = settleExpress.split(";");
            //权益有多条结算规则
            if (settleRules.length > 1){
                //区分开自定义增值税基数的数据
                if (settleExpress.contains("增值税基数")){
                    ShopItemSettleExpressVo shopItemSettleExpressVo = new ShopItemSettleExpressVo();
                    for (String settleRule : settleRules) {
                        //自定义增值税公式
                        if (settleRule.contains("增值税基数")){
                            String customVAT = settleRule.substring(7,settleRule.length()-1);
                            SettleVo settleVo = expressStr2ExpressVo(customVAT);
                            List<String> stringList = Lists.newLinkedList();
                            if (settleVo.getNetNum().compareTo(new BigDecimal(0)) != 0){
                                stringList.add("net_price*tax_net_price_per");
                            }
                            if (settleVo.getRateNum().compareTo(new BigDecimal(0)) != 0){
                                stringList.add("service_fee*tax_service_fee_per");
                            }
                            if (settleVo.getFixedAmount() != null){
                                stringList.add("adjust");
                            }
                            shopItemSettleExpressVo.setAdjust(settleVo.getFixedAmount());
                            shopItemSettleExpressVo.setTaxNetPricePer(settleVo.getNetNum());
                            shopItemSettleExpressVo.setTaxServiceFeePer(settleVo.getRateNum());
                            shopItemSettleExpressVo.setCustomTaxExpress(StringUtils.join(stringList,"+"));
                        }else {
                            //有自定义增值税情况下 的 结算规则
                            SettleVo settleVo = expressStr2ExpressVo(settleRule);
                            List<String> stringList = Lists.newLinkedList();
                            if (settleVo.getNetNum().compareTo(new BigDecimal(0)) != 0){
                                stringList.add("net_price*net_price_per");
                            }
                            if (settleVo.getRateNum().compareTo(new BigDecimal(0)) != 0){
                                stringList.add("service_fee*service_fee_per");
                            }
                            if (settleVo.getTaxNum().compareTo(new BigDecimal(0)) != 0){
                                stringList.add("custom_tax_fee*custom_tax_fee_per");
                            }
                            if (settleVo.getFixedAmount() != null){
                                stringList.add("discount");
                            }
                            shopItemSettleExpressVo.setMonday(1);
                            shopItemSettleExpressVo.setTuesday(1);
                            shopItemSettleExpressVo.setWednesday(1);
                            shopItemSettleExpressVo.setThursday(1);
                            shopItemSettleExpressVo.setFriday(1);
                            shopItemSettleExpressVo.setSaturday(1);
                            shopItemSettleExpressVo.setSunday(1);
                            shopItemSettleExpressVo.setDiscount(settleVo.getFixedAmount());
                            shopItemSettleExpressVo.setNetPricePer(settleVo.getNetNum());
                            shopItemSettleExpressVo.setServiceFeePer(settleVo.getRateNum());
                            shopItemSettleExpressVo.setCustomTaxFeePer(settleVo.getTaxNum());
                            shopItemSettleExpressVo.setSettleExpress(StringUtils.join(stringList,"+"));
                        }
                    }
                    resultList.add(shopItemSettleExpressVo);
                }else {
                    //权益多条结算规则 且 没有自定义增值税
                    for (String settleRule : settleRules) {
                        ShopItemSettleExpressVo shopItemSettleExpressVo = new ShopItemSettleExpressVo();
                        if (settleRule.contains("W")){
                            SettleVo settleVo = expressStr2ExpressVo(settleRule);
                            List<String> stringList = Lists.newLinkedList();
                            if (settleVo.getNetNum().compareTo(new BigDecimal(0)) != 0){
                                stringList.add("net_price*net_price_per");
                            }
                            if (settleVo.getRateNum().compareTo(new BigDecimal(0)) != 0){
                                stringList.add("service_fee*service_fee_per");
                            }
                            if (settleVo.getTaxNum().compareTo(new BigDecimal(0)) != 0){
                                stringList.add("tax_fee*tax_fee_per");
                            }
                            if (settleVo.getFixedAmount() != null){
                                stringList.add("discount");
                            }
                            shopItemSettleExpressVo.setMonday(settleVo.getMonday());
                            shopItemSettleExpressVo.setTuesday(settleVo.getTuesday());
                            shopItemSettleExpressVo.setWednesday(settleVo.getWednesday());
                            shopItemSettleExpressVo.setThursday(settleVo.getThursday());
                            shopItemSettleExpressVo.setFriday(settleVo.getFriday());
                            shopItemSettleExpressVo.setSaturday(settleVo.getSaturday());
                            shopItemSettleExpressVo.setSunday(settleVo.getSunday());
                            shopItemSettleExpressVo.setDiscount(settleVo.getFixedAmount());
                            shopItemSettleExpressVo.setNetPricePer(settleVo.getNetNum());
                            shopItemSettleExpressVo.setServiceFeePer(settleVo.getRateNum());
                            shopItemSettleExpressVo.setTaxFeePer(settleVo.getTaxNum());
                            shopItemSettleExpressVo.setSettleExpress(StringUtils.join(stringList,"+"));
                            resultList.add(shopItemSettleExpressVo);
                        }
                    }
                }
            }
            //权益只有一条结算规则
            else {
                ShopItemSettleExpressVo shopItemSettleExpressVo = new ShopItemSettleExpressVo();
                SettleVo settleVo = expressStr2ExpressVo(settleExpress);
                List<String> stringList = Lists.newLinkedList();
                if (settleVo.getNetNum().compareTo(new BigDecimal(0)) != 0){
                    stringList.add("net_price*net_price_per");
                }
                if (settleVo.getRateNum().compareTo(new BigDecimal(0)) != 0){
                    stringList.add("service_fee*service_fee_per");
                }
                if (settleVo.getTaxNum().compareTo(new BigDecimal(0)) != 0){
                    stringList.add("tax_fee*tax_fee_per");
                }
                if (settleVo.getFixedAmount() != null){
                    stringList.add("discount");
                }
                shopItemSettleExpressVo.setMonday(settleVo.getMonday());
                shopItemSettleExpressVo.setTuesday(settleVo.getTuesday());
                shopItemSettleExpressVo.setWednesday(settleVo.getWednesday());
                shopItemSettleExpressVo.setThursday(settleVo.getThursday());
                shopItemSettleExpressVo.setFriday(settleVo.getFriday());
                shopItemSettleExpressVo.setSaturday(settleVo.getSaturday());
                shopItemSettleExpressVo.setSunday(settleVo.getSunday());
                shopItemSettleExpressVo.setDiscount(settleVo.getFixedAmount());
                shopItemSettleExpressVo.setNetPricePer(settleVo.getNetNum());
                shopItemSettleExpressVo.setServiceFeePer(settleVo.getRateNum());
                shopItemSettleExpressVo.setTaxFeePer(settleVo.getTaxNum());
                shopItemSettleExpressVo.setSettleExpress(StringUtils.join(stringList,"+"));
                resultList.add(shopItemSettleExpressVo);
            }
        }else {
            ShopItemSettleExpressVo shopItemSettleExpressVo = new ShopItemSettleExpressVo();
            shopItemSettleExpressVo.setMonday(1);
            shopItemSettleExpressVo.setTuesday(1);
            shopItemSettleExpressVo.setWednesday(1);
            shopItemSettleExpressVo.setThursday(1);
            shopItemSettleExpressVo.setFriday(1);
            shopItemSettleExpressVo.setSaturday(1);
            shopItemSettleExpressVo.setSunday(1);
            resultList.add(shopItemSettleExpressVo);
        }
        return resultList;
    }

    @Override
    public List<ShopItemPriceRes> selectPriceList(List<Integer> shopIdlist) {

        List<ShopItemPriceRes> shopItemPriceResList = shopItemMapper.selectPriceList(shopIdlist);
        return shopItemPriceResList;
    }

    @Override
    public List<ShopItemRes> getShopItemInfo(List<Integer> shopItemIdList) {
        List<ShopItemRes> shopItemResList = shopItemMapper.getShopItemInfo(shopItemIdList);
        return shopItemResList;
    }

    @Override
    public GoodsShopItemIdRes getShopItemId(Integer goodsId) {
        GoodsShopItemIdRes goodsShopItemIdRes = shopItemMapper.getShopItemId(goodsId);
        return goodsShopItemIdRes;
    }

    @Override
    public List<ShopListQueryResVo> getItems(List<Integer> goodsIdList) {
        List<ShopListQueryResVo> shopListQueryResList = shopItemMapper.getItems(goodsIdList);
        return shopListQueryResList;
    }

    @Override
    public List<ShopItemInfoRes> selectShopItemPrice(List<Integer> itemsIdList, Date date) {
        List<ShopItemInfoRes> shopItemResVoList =  shopItemMapper.selectShopItemPrice(itemsIdList, date);
        return shopItemResVoList;
    }

    @Override
    public ShopItemSetmenuInfo getShopItemSetmenuInfo(Integer shopItemId) {
        ShopItemSetmenuInfo  shopItemSetmenuInfo = shopItemMapper.getShopItemSetmenuInfo(shopItemId);
        return shopItemSetmenuInfo;
    }

    @Override
    public List<ShopItemConciseRes> selectByItems(List<Integer> items) throws Exception {
        List<ShopItemConciseRes> list = shopItemMapper.selectByItems(items);
        return list;
    }

    /**
     * 拉取大批量block的商户规格数据
     * @throws Exception
     */
    @Override
    public String export() throws Exception {
        String result = null;
        //获取当前时间
        Calendar calendar = Calendar.getInstance();
        Date startDate = DateUtil.parse(DateUtil.format(new Date(),"yyyy-MM-dd"),"yyyy-MM-dd");
        calendar.setTime(startDate);
        calendar.add(Calendar.DAY_OF_MONTH,1);
        startDate = DateUtil.parse(DateUtil.format(calendar.getTime(),"yyyy-MM-dd"),"yyyy-MM-dd");
        calendar.add(Calendar.DAY_OF_MONTH,60);
        Date endDate = DateUtil.parse(DateUtil.format(calendar.getTime(),"yyyy-MM-dd"),"yyyy-MM-dd");


        List<ShopItemExportVo> list = shopItemMapper.export();
        if (!CollectionUtils.isEmpty(list)){

            //Excel表头
            List<String> titleList=Lists.newArrayList();
            titleList.add("城市");
            titleList.add("酒店名称");
            titleList.add("商户名称");
            titleList.add("房型/餐型");
            titleList.add("床型");
            titleList.add("早餐");
            titleList.add("权益类型");
            titleList.add("资源类型");
            titleList.add("地址");
            titleList.add("联系方式");
            titleList.add("不可预定日期");
            titleList.add("成本价");

            ExcelWriter excelWriter=new ExcelWriter(true);
            String fileName="可预订商户规格数据-"+DateUtil.format(new Date(),"yyyyMMddHHmmss")+"-"+list.size()+".xlsx";
            excelWriter.setDestFile(new File("/tmp/download/"+fileName));
            excelWriter.setOrCreateSheet("sheet1");
            excelWriter.writeHeadRow(titleList);

            for (ShopItemExportVo shopItemExportVo : list) {
                log.info("开始处理产品id为{}",shopItemExportVo.getProductId());
                //可预订标识
                boolean flag = true;
                List<String> allBlock = Lists.newLinkedList();
                String block = null;
                if (StringUtils.isNotBlank(shopItemExportVo.getShopBlock())){
                    allBlock.add(shopItemExportVo.getShopBlock());
                }
                if (StringUtils.isNotBlank(shopItemExportVo.getShopItemBlock())){
                    allBlock.add(shopItemExportVo.getShopItemBlock());
                }
                if (!CollectionUtils.isEmpty(allBlock)){
                    block = StringUtils.join(allBlock,", ");
                }
                if (StringUtils.isNotBlank(block)){
                    HashSet<Date> dates = blockRuleService.generateBlockDate(startDate,endDate,block);
                    if (!CollectionUtils.isEmpty(dates)){
                        if (dates.size() >= 60){
                            flag = false;
                        }
                    }
                }
                if (flag){
                    List<Object> rowData=Lists.newArrayList();
                    rowData.add(shopItemExportVo.getCityName());
                    rowData.add(shopItemExportVo.getHotelName());
                    rowData.add(shopItemExportVo.getShopName());
                    rowData.add(shopItemExportVo.getShopItemName());
                    rowData.add(shopItemExportVo.getNeeds());
                    rowData.add(shopItemExportVo.getAddon());
                    rowData.add(shopItemExportVo.getGiftName());
                    rowData.add(shopItemExportVo.getServiceName());
                    rowData.add(shopItemExportVo.getAddress());
                    rowData.add(shopItemExportVo.getPhone());
                    rowData.add(StringUtils.isNotBlank(block) ? blockRuleService.blockRuleStr2list(block).stream().map(blockRule -> blockRule.getNatural()).collect(Collectors.joining("; ")) : null);
                    rowData.add(shopItemExportVo.getPrice());
                    excelWriter.writeRow(rowData);
                }
            }
            result = "http://pangu.icolourful.net/download/"+fileName;
            excelWriter.flush();
        }
        return result;
    }

    /**
     * 大批量商户停售跑批
     * @return
     * @throws Exception
     */
    @Override
    public String offShopItem() throws Exception {
        List<ShopItem> list = shopItemMapper.offShopItem();
        if (!CollectionUtils.isEmpty(list)){
            for (ShopItem shopItem : list) {
                log.info("开始处理规格的id为:{}",shopItem.getId());
                //规格停售
                shopItemMapper.updateShopItemStatus(shopItem.getId());
                //产品停售
                shopItemMapper.updateProductStatus(shopItem.getId());
                //产品组产品停售
                Wrapper wrapper = new Wrapper() {
                    @Override
                    public String getSqlSegment() {
                        return "where shop_item_id="+shopItem.getId();
                    }
                };
                List<Product> products = productMapper.selectList(wrapper);
                if (!CollectionUtils.isEmpty(products)){
                    for (Product product : products) {
                        shopItemMapper.updateGroupProductStatus(product.getId());
                    }
                }
            }
            log.info("跑批停售结束");
        }
        return null;
    }

    /**
     * 检查规格是否被包进商品售卖
     * @param id
     * @return
     */
    @Override
    public List<ProductGroupProduct> checkItemIsSale(Integer id) {
        List<ProductGroupProduct> groupProducts = shopItemMapper.checkItemIsSale(id);
        return groupProducts;
    }

    /**
     * 将老系统单条结算规则字符串解析成新系统数据表表对象
     * @param expressStr
     * @return
     */
    private SettleVo expressStr2ExpressVo(String expressStr){
        SettleVo settleVo = new SettleVo();
        //星期默认至零
        settleVo.setMonday(0);
        settleVo.setTuesday(0);
        settleVo.setWednesday(0);
        settleVo.setThursday(0);
        settleVo.setFriday(0);
        settleVo.setSaturday(0);
        settleVo.setSunday(0);
        if (expressStr.contains("W")){
            String week = expressStr.substring(0,expressStr.indexOf(":"));
            expressStr = expressStr.substring(expressStr.indexOf(":")+1);
            if (Pattern.matches("^W[1-7]$",week)){
                Integer weekDetail = Integer.valueOf(week.substring(1));
                if (weekDetail.compareTo(1) == 0){
                    settleVo.setMonday(1);
                }
                if (weekDetail.compareTo(2) == 0){
                    settleVo.setTuesday(1);
                }
                if (weekDetail.compareTo(3) == 0){
                    settleVo.setWednesday(1);
                }
                if (weekDetail.compareTo(4) == 0){
                    settleVo.setThursday(1);
                }
                if (weekDetail.compareTo(5) == 0){
                    settleVo.setFriday(1);
                }
                if (weekDetail.compareTo(6) == 0){
                    settleVo.setSaturday(1);
                }
                if (weekDetail.compareTo(7) == 0){
                    settleVo.setSunday(1);
                }
            }
            if (Pattern.matches("^W[1-7]-W[1-7]$",week)){
                List<Integer> stringList = Lists.newLinkedList();
                int num1 = NumberUtils.toInt(week.substring(1,2));
                int num2 = NumberUtils.toInt(week.substring(4,5));
                if (num1>num2){
                    for (int i = num1;i <= 7;i++){
                        stringList.add(i);
                    }
                    for (int i = 1;i<= num2;i++){
                        stringList.add(i);
                    }
                }else {
                    for (int i = num1;i<=num2;i++){
                        stringList.add(i);
                    }
                }
                for (Integer integer : stringList) {
                    if (integer.compareTo(1) == 0){
                        settleVo.setMonday(1);
                    }
                    if (integer.compareTo(2) == 0){
                        settleVo.setTuesday(1);
                    }
                    if (integer.compareTo(3) == 0){
                        settleVo.setWednesday(1);
                    }
                    if (integer.compareTo(4) == 0){
                        settleVo.setThursday(1);
                    }
                    if (integer.compareTo(5) == 0){
                        settleVo.setFriday(1);
                    }
                    if (integer.compareTo(6) == 0){
                        settleVo.setSaturday(1);
                    }
                    if (integer.compareTo(7) == 0){
                        settleVo.setSunday(1);
                    }
                }
            }
        }else {
            settleVo.setMonday(1);
            settleVo.setTuesday(1);
            settleVo.setWednesday(1);
            settleVo.setThursday(1);
            settleVo.setFriday(1);
            settleVo.setSaturday(1);
            settleVo.setSunday(1);
        }
        String[] customParam = expressStr.split("\\+");
        BigDecimal netNum = new BigDecimal(0);
        BigDecimal rateNum = new BigDecimal(0);
        BigDecimal taxNum = new BigDecimal(0);
        BigDecimal fixedAmount = null;
        for (String param : customParam) {
            if (param.contains("*")){
                String[] mul = param.split("\\*");
                String param1 = mul[0];
                String param2 = mul[1];
                param2 = param2.substring(0,param2.indexOf("%"));
                BigDecimal num = new BigDecimal(param2.replaceAll("%","")).divide(new BigDecimal(100));
                if (param1.contains("双人")||param1.contains("两人")){
                    num = num.multiply(new BigDecimal(2));
                }
                if (param1.contains("净价")){
                    netNum = netNum.add(num);
                }
                if (param1.contains("服务费")){
                    rateNum = rateNum.add(num);
                }
            }else {
                if (param.contains("净价")){
                    BigDecimal num = new BigDecimal(0);
                    if (param.contains("单人")||param.contains("一人")||param.contains("套餐")||param.equals("净价")){
                        num = new BigDecimal(1);
                    }
                    if (param.contains("双人")||param.contains("两人")){
                        num = new BigDecimal(2);
                    }
                    netNum = netNum.add(num);
                }else if (param.contains("服务费")){
                    BigDecimal num = new BigDecimal(0);
                    if (param.contains("单人")||param.contains("一人")||param.contains("套餐")||param.equals("服务费")){
                        num = new BigDecimal(1);
                    }
                    if (param.contains("双人")||param.contains("两人")){
                        num = new BigDecimal(2);
                    }
                    rateNum = rateNum.add(num);
                }else if (param.contains("税")){
                    BigDecimal num = new BigDecimal(0);
                    if (param.contains("单人")||param.contains("一人")||param.contains("套餐")||param.equals("增值税")){
                        num = new BigDecimal(1);
                    }
                    if (param.contains("双人")||param.contains("两人")){
                        num = new BigDecimal(2);
                    }
                    taxNum = taxNum.add(num);
                } else {
                    if (param.contains("(")){
                        param = param.substring(0,param.indexOf("("));
                    }
                    if (NumberUtils.isNumber(param)){
                        fixedAmount = new BigDecimal(param);
                    }
                }
            }
        }
        settleVo.setFixedAmount(fixedAmount);
        settleVo.setNetNum(netNum);
        settleVo.setRateNum(rateNum);
        settleVo.setTaxNum(taxNum);
        return settleVo;
    }

    /**
     * 老系统商户规格同步新增
     * @param shopItem
     * @return
     */
    private com.colourfulchina.bigan.api.entity.ShopItem addOldShopItem(ShopItem shopItem)throws Exception{
        Shop shop = shopMapper.selectById(shopItem.getShopId());
        com.colourfulchina.bigan.api.entity.ShopItem oldShopItem = new com.colourfulchina.bigan.api.entity.ShopItem();
        oldShopItem.setAddon(shopItem.getAddon());
        oldShopItem.setName(shopItem.getName());
        oldShopItem.setNeeds(shopItem.getNeeds());
        if (StringUtils.isNotBlank(shopItem.getOpenTime()) && StringUtils.isNotBlank(shopItem.getCloseTime())){
            oldShopItem.setOpentime(shopItem.getOpenTime()+"~"+shopItem.getCloseTime());
        }
        oldShopItem.setShopId(shop.getOldShopId().longValue());
        oldShopItem.setType(shopItem.getServiceType());
        CommonResultVo<com.colourfulchina.bigan.api.entity.ShopItem> remoteResult = remoteShopItemService.remoteAddShopItem(oldShopItem);
        if (remoteResult.getCode() == 100 && remoteResult.getResult() != null){
            return remoteResult.getResult();
        }
        return null;
    }

    /**
     * 老系统商户规格同步更新
     * @param shopItem
     * @return
     * @throws Exception
     */
    private com.colourfulchina.bigan.api.entity.ShopItem updOldShopItem(ShopItem shopItem)throws Exception{
        Shop shop = shopMapper.selectById(shopItem.getShopId());
        com.colourfulchina.bigan.api.entity.ShopItem oldShopItem = new com.colourfulchina.bigan.api.entity.ShopItem();
        oldShopItem.setId(shopItem.getOldItemId().longValue());
        oldShopItem.setAddon(shopItem.getAddon());
        oldShopItem.setName(shopItem.getName());
        oldShopItem.setNeeds(shopItem.getNeeds());
        if (StringUtils.isNotBlank(shopItem.getOpenTime()) && StringUtils.isNotBlank(shopItem.getCloseTime())){
            oldShopItem.setOpentime(shopItem.getOpenTime()+"~"+shopItem.getCloseTime());
        }
        oldShopItem.setShopId(shop.getOldShopId().longValue());
        oldShopItem.setType(shopItem.getServiceType());
        oldShopItem.setBlock(shopItem.getBlockRule());
        CommonResultVo<com.colourfulchina.bigan.api.entity.ShopItem> remoteResult = remoteShopItemService.remoteUpdShopItem(oldShopItem);
        if (remoteResult.getCode() == 100 && remoteResult.getResult() != null){
            return remoteResult.getResult();
        }
        return null;
    }
}