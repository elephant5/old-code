package com.colourfulchina.bigan.controller;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.colourfulchina.bigan.api.dto.GoodsInfo;
import com.colourfulchina.bigan.api.entity.*;
import com.colourfulchina.bigan.api.vo.*;
import com.colourfulchina.bigan.enums.GoodsEnums;
import com.colourfulchina.bigan.enums.ProductImportEnums;
import com.colourfulchina.bigan.enums.ProductImportSetMenuEnums;
import com.colourfulchina.bigan.service.*;
import com.colourfulchina.bigan.service.impl.ShopAndItemInfoServiceImpl;
import com.colourfulchina.bigan.utils.ClfCommondUtils;
import com.colourfulchina.bigan.utils.FileUtils;
import com.colourfulchina.bigan.utils.XML;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    private ProductImportWorkbookService productImportWorkbookService;

    @Autowired
    private ProductImportSheetService productImportSheetService;

    @Autowired
    private ProductImportRowService productImportRowService;

    @Autowired
    private PrjGroupGoodsService prjGroupGoodsService;

    @Autowired
    private PrjGroupService prjGroupService;

    @Autowired
    private ShopService shopService;

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private ShopitemsService shopitemsService;

    @Autowired
    private ProjectGoodsService projectGoodsService;


    @Autowired
    private   WeekBlockService weekBlockService;
    @Autowired
    ShopAndItemInfoService  shopAndItemInfoService;
    @Autowired
    HotelService hotelService;
    @Autowired
    ClfSetMenuService clfSetMenuService;


    @Autowired
    CityService cityService;
    @Autowired
    ShopAccountService shopAccountService;

    // 自助餐，下午茶，住宿，定制套餐
    @Autowired
    ClfBuffetService clfBuffetService;
    @Autowired
    ClfTeaService clfTeaService;
    @Autowired
    AccomListService accomListService;



    @PostMapping(value = "/import")
    public GoodsImportResultVo importGoods(Integer bankId,Integer projectId,String productGroupName,MultipartFile multipartFile){
        GoodsImportResultVo resultVo=new GoodsImportResultVo();
        try {
            Map<String,String> resultMsg=Maps.newTreeMap();
            // 传递参数不全，报300错误
            if (bankId == null || projectId == null || multipartFile == null){
                log.info("***导入报错***:输入参数不完整");
                resultVo.setCode(300);
                resultVo.setMsg("输入参数不完整");
                return resultVo;
            }

            GoodsImportVo goodsImportVo=new GoodsImportVo();
            goodsImportVo.setBankId(bankId);
            goodsImportVo.setProjectId(projectId);

            //由于bankId、projectId是由已存在信息传入  暂不校验项目是否存在

            //读取上传的Excel并入库
            String batchId=readFileAndSave(goodsImportVo,multipartFile,productGroupName);

            log.info("*****************END 临时表入库*****************");
            //把表中的数据转换成以下对象
            //自助餐处理流程
            //酒店、门店、商户、商品
            //然后分别判断是否存在，不存在则新增
            //返回具体处理结果
            Wrapper<ProductImportSheet> sheepWrapper=new Wrapper<ProductImportSheet>() {
                @Override
                public String getSqlSegment() {
                    return "where state = 0 and batch_id = '" + batchId+"'" + " and sheet_name='" + productGroupName + "'";
                }
            };
            // 查询所有临时表中本批次导入的数据
            final List<ProductImportSheet> productImportSheets = productImportSheetService.selectList(sheepWrapper);
            Wrapper<WeekBlock> blockwapper=new Wrapper<WeekBlock>() {
                @Override
                public String getSqlSegment() {
                    return "";
                }
            };
            // 查询sys_block_week 所有已设置的Block信息
//            List<WeekBlock> blockList =weekBlockService.selectList(blockwapper);
//            Map blockMap = new HashMap(0);
//            for (WeekBlock block :blockList){
//                blockMap.put(block.getName(),block.getCode());
//            }
            // 查询商户的账户信息
            List<ShopAccount> accountList = shopAccountService.selectList(null);
            Map buffetAccountListMap = new HashMap(0);  // 自助餐
            Map teaAccountListMap = new HashMap(0);     // 下午茶
            Map setMenuAccountListMap = new HashMap(0); // 定制套餐
            Map accomAccountListMap = new HashMap(0);   // 住宿
            Map accountListMap = new HashMap(0);        // 商户账户信息
            // 填充账户信息数据到Map
            for (ShopAccount block :accountList){
                accountListMap.put(block.getAccount(),block.getPwd());
            }
            for (ProductImportSheet productImportSheet:productImportSheets){
                // 查询所有本批次导入数据，按酒店中文名、餐厅名排序
                Wrapper<ProductImportRow> rowWrapper=new Wrapper<ProductImportRow>() {
                    @Override
                    public String getSqlSegment() {
                        return "where state = 0 and batch_id = '" + batchId +"' and sheet_name = '" + productGroupName+"' order by JSON_VALUE([value],'$.\"酒店中文名\"'),JSON_VALUE([value],'$.\"餐厅名\"')";
                    }
                };

                //作为产品组名称prj_group.title
                final String sheetName = productImportSheet.getSheetName();
                // 根据项目ID及Sheet名 获取设置的产品组信息
                Wrapper<PrjGroup> groupWrapper=new Wrapper<PrjGroup>() {
                    @Override
                    public String getSqlSegment() {
                        return "where project_id = "+goodsImportVo.getProjectId() + " and title = '" + sheetName + "' ";
                    }
                };
                // 判断产品组信息是否存在，不存在就抛300提示
                final PrjGroup prjGroup = prjGroupService.selectOne(groupWrapper);
                if (prjGroup == null){
                    // TODO:根据Sheet名自动创建产品组
                    //新增产品组 需要表格中提供相关数据
                    log.info("***项目设置错误提示***：未找到对应的产品组project_id:{},title:{}",goodsImportVo.getProjectId(),sheetName);
                    resultVo.setCode(300);
                    resultVo.setMsg("***项目设置错误提示***：项目ID:"+goodsImportVo.getProjectId()+",产品组名称:"+sheetName);
                    continue;
                }

                // 所有导入数据
                final List<ProductImportRow> productImportRows = productImportRowService.selectList(rowWrapper);
                // 商品信息List
                List<GoodsInfo> goodsInfoList=new ArrayList<>(0);
                // 获取导入数据的权益类型 Code
                String serviceCodeTemp=productImportSheet.getServiceCode();
                // 获取对应权益服务的用户名和密码并填充商户账户信息的MAP
                if(StringUtils.isNotEmpty(serviceCodeTemp) &&serviceCodeTemp.equals(GoodsEnums.service.BUFFET.getCode())) {
                        List<ClfBuffet> list = clfBuffetService.selectList(null);
                        list.forEach(buf ->{
                            buffetAccountListMap.put(buf.getHotelCh()+"-"+buf.getRestaurantName(),buf.getUserName()+"-"+buf.getPwd());
                        });
                }
                if(StringUtils.isNotEmpty(serviceCodeTemp) &&serviceCodeTemp.equals(GoodsEnums.service.SETMENU.getCode())){
                    List<ClfSetMenu> list = clfSetMenuService.selectList(null);
                    list.forEach(buf ->{
                        setMenuAccountListMap.put(buf.getHotelCh()+"-"+buf.getRestaurantNameCh(),buf.getUserName()+"-"+buf.getPwd());
                    });
                }
                if(StringUtils.isNotEmpty(serviceCodeTemp) && serviceCodeTemp.equals(GoodsEnums.service.TEA.getCode())){
                    List<ClfTea> list = clfTeaService.selectList(null);
                    list.forEach(buf ->{
                        teaAccountListMap.put(buf.getHotelCh()+"-"+buf.getRestaurantName(),buf.getUserName()+"-"+buf.getPwd());
                    });
                }
                //住宿不需要核销 也无需开账户
                if(StringUtils.isNotEmpty(serviceCodeTemp) &&serviceCodeTemp.equals(GoodsEnums.service.ACCOM.getCode())){
                }

                for (ProductImportRow productImportRow : productImportRows){
                    try {
                        final String value = productImportRow.getValue();
                        final Map map = JSON.toJavaObject(JSON.parseObject(value), Map.class);
                        //查询商品信息goods.title等
//                        final String serviceCode=productImportSheet.getServiceCode();
                        String name = map.get(ProductImportSetMenuEnums.setMenu.SERVICE.getCellName())+"";
                        final String serviceCode = GoodsEnums.service.findByName(name)==null?null:GoodsEnums.service.findByName(name).getCode();
                        final String giftCode = GoodsEnums.gift.findByNameOrShort(map.get(ProductImportSetMenuEnums.setMenu.DISCOUNT.getCellName()) + "") == null?"":GoodsEnums.gift.findByNameOrShort(map.get(ProductImportSetMenuEnums.setMenu.DISCOUNT.getCellName()) + "").getCode();
                        final String hotel_name_cn = getValue(map,ProductImportSetMenuEnums.setMenu.HOTEL_NAME_CN.getCellName());//map.get(ProductImportSetMenuEnums.setMenu.HOTEL_NAME_CN.getCellName())+"").trim().replaceAll("\n","");
                        final String shop_name =getValue(map,ProductImportSetMenuEnums.setMenu.SHOP_NAME.getCellName());// (map.get(ProductImportSetMenuEnums.setMenu.SHOP_NAME.getCellName())+"").trim().replaceAll("\n","");
                        final String discount = getValue(map,ProductImportSetMenuEnums.setMenu.DISCOUNT.getCellName());//(map.get(ProductImportSetMenuEnums.setMenu.DISCOUNT.getCellName())+"").trim().replaceAll("\n","");
                        final String mealSelection = getValue(map,ProductImportSetMenuEnums.setMenu.CAN_RANGE.getCellName());// (map.get(ProductImportSetMenuEnums.setMenu.CAN_RANGE.getCellName())+"").trim().replaceAll("\n","");
                        String clause = getValue(map,ProductImportSetMenuEnums.setMenu.CLAUSE.getCellName());
                         String weekBlock =  getValue(map,ProductImportSetMenuEnums.setMenu.WEEKBLOCK.getCellName());//(map.get(ProductImportSetMenuEnums.setMenu.WEEKBLOCK.getCellName())+"").trim().replaceAll("\n","");
                        String clauseBlockStr = null;
                        if(StringUtils.isNotEmpty(clause)){
                            clauseBlockStr = ShopAndItemInfoServiceImpl.getClausBlockStr(clause);
                        }
                        if(StringUtils.isNotEmpty(weekBlock)){
                            //clause = clause +"。本权益适用于" + weekBlock;
                            weekBlock = ShopAndItemInfoServiceImpl.getWeekList(weekBlock);
                        }


                        //酒店名+餐厅名+券折扣(权益类型)
                        String keyStr = hotel_name_cn+"-"+shop_name+"-"+giftCode;

                        //自助餐判断逻辑
                        if(serviceCode.equals(GoodsEnums.service.BUFFET.getCode())){
                            String lunctime  = getValue(map,ProductImportSetMenuEnums.setMenu.LUNCH_OPEN_RANGE.getCellName());// map.get(ProductImportSetMenuEnums.setMenu.LUNCH_OPEN_RANGE.getCellName())==null?null:map.get(ProductImportSetMenuEnums.setMenu.LUNCH_OPEN_RANGE.getCellName())+"";
                            String dinnerTime  = getValue(map,ProductImportSetMenuEnums.setMenu.SUPPER_OPEN_RANGE.getCellName());// map.get(ProductImportSetMenuEnums.setMenu.SUPPER_OPEN_RANGE.getCellName())==null?null:map.get(ProductImportSetMenuEnums.setMenu.SUPPER_OPEN_RANGE.getCellName())+"";
                            if(mealSelection.indexOf("午餐")>-1 ){
                                if(StringUtils.isEmpty(lunctime) ){
                                    log.error("***数据多条***：{}-{}-{}午餐开餐时间为空",hotel_name_cn,shop_name,discount);
                                    continue;
                                }


                            }
                            if(mealSelection.indexOf("晚餐")>-1 ) {
                                if(StringUtils.isEmpty(dinnerTime) ){
                                    log.error("***数据多条***：{}-{}-{}午餐开餐时间为空",hotel_name_cn,shop_name,discount);
                                    continue;
                                }
                            }
                        }

                        //定制套餐判断逻辑
                        if(serviceCode.equals(GoodsEnums.service.SETMENU.getCode())){
                            String packageName=getValue(map,ProductImportSetMenuEnums.setMenu.PACKAGENAME.getCellName());//map.get(ProductImportSetMenuEnums.setMenu.PACKAGENAME.getCellName())==null?null:map.get(ProductImportSetMenuEnums.setMenu.PACKAGENAME.getCellName())+"";
                            String lunctime  = getValue(map,ProductImportSetMenuEnums.setMenu.LUNCH_OPEN_RANGE.getCellName());//map.get(ProductImportSetMenuEnums.setMenu.LUNCH_OPEN_RANGE.getCellName())==null?null:map.get(ProductImportSetMenuEnums.setMenu.LUNCH_OPEN_RANGE.getCellName())+"";
                            String dinnerTime  =getValue(map,ProductImportSetMenuEnums.setMenu.SUPPER_OPEN_RANGE.getCellName());// map.get(ProductImportSetMenuEnums.setMenu.SUPPER_OPEN_RANGE.getCellName())==null?null:map.get(ProductImportSetMenuEnums.setMenu.SUPPER_OPEN_RANGE.getCellName())+"";
                            if((packageName.indexOf("午餐")>-1 || packageName.indexOf("午市")>-1) ){
                                if(StringUtils.isEmpty(lunctime) ){
                                    log.error("***数据多条***：{}-{}-{}午餐开餐时间为空",hotel_name_cn,shop_name,discount);
                                    continue;
                                }else{
                                    lunctime = ClfCommondUtils.replaceTime(lunctime);
                                }
                            }
                            if((packageName.indexOf("晚餐")>-1 || packageName.indexOf("晚市")>-1) ){
                                if(StringUtils.isEmpty(dinnerTime) ){
                                    log.error("***数据多条***：{}-{}-{}午餐开餐时间为空",hotel_name_cn,shop_name,discount);
                                    continue;
                                }
                            }

                        }

                        //下午茶判断逻辑
                        if(serviceCode.equals(GoodsEnums.service.TEA.getCode())){

                            String tea_open_range  = getValue(map,ProductImportSetMenuEnums.setMenu.TEA_OPEN_RANGE.getCellName());//map.get(ProductImportSetMenuEnums.setMenu.TEA_OPEN_RANGE.getCellName())==null?null:map.get(ProductImportSetMenuEnums.setMenu.TEA_OPEN_RANGE.getCellName())+"";
                            if(StringUtils.isEmpty(tea_open_range)){
                                log.error("***数据多条***：{}-{}-{}下午茶开餐时间为空",hotel_name_cn,shop_name,discount);
                                continue;
                            }
                        }

                        //单杯判断逻辑
                        if(serviceCode.equals(GoodsEnums.service.DRINK.getCode())){
                            keyStr=hotel_name_cn+"-"+shop_name+"-"+serviceCode;
                            String tea_open_range  = getValue(map,ProductImportSetMenuEnums.setMenu.TEA_OPEN_RANGE.getCellName());//map.get(ProductImportSetMenuEnums.setMenu.TEA_OPEN_RANGE.getCellName())==null?null:map.get(ProductImportSetMenuEnums.setMenu.TEA_OPEN_RANGE.getCellName())+"";
                            if(StringUtils.isEmpty(tea_open_range)){
                                log.error("***数据多条***：{}-{}-{}单杯茶饮时间为空",hotel_name_cn,shop_name,discount);
                                continue;
                            }
                        }

                        //住宿判断逻辑
                        if (serviceCode.equals(GoodsEnums.service.ACCOM.getCode())){
                            String roomType = getValue(map,ProductImportSetMenuEnums.setMenu.ROOM_TYPE.getCellName());
                            keyStr=hotel_name_cn+"-"+roomType;
//                            GoodsInfo goodsInfo=new GoodsInfo();
//                            goodsInfo.setHotelCh(hotel_name_cn);
//                            goodsInfo.setShopCh(shop_name);
//                            goodsInfo.setGift(discount);
//                            goodsInfo.setRows(map);
//                            goodsInfo.setServiceCode(serviceCode);
//                            goodsInfo.setGiftCode(giftCode);
//                            goodsInfo.setGoodsImportVo(goodsImportVo);
//                            goodsInfoList.add(goodsInfo);
//                            continue;
                        }

                        // TODO 组装去重数据 除了住宿都需要去重
                        if(goodsInfoList.size() == 0 ){


                            GoodsInfo goodsInfo=new GoodsInfo();
                            goodsInfo.setHotelCh(hotel_name_cn);
                            goodsInfo.setShopCh(shop_name);
                            goodsInfo.setGift(discount);
                            goodsInfo.setRows(map);
                            //自助餐判断餐段
                            if(serviceCode.equals(GoodsEnums.service.BUFFET.getCode())){
                                goodsInfo.getMeatName().add(mealSelection);
                                goodsInfo.setMealSelection(mealSelection);
                                // 拼接Blcok信息：午餐|周六-周一
                                if(StringUtils.isNotEmpty(weekBlock)&&!weekBlock.equals("null")){

                                    Map<String,String> weekBlockMap = goodsInfo.getWeekBlockMap();
                                    if(weekBlockMap.containsKey(mealSelection)){
                                        String weeKtemp = weekBlockMap.get(mealSelection);
                                        String newBlock = getNewWeekBlockStr(weekBlock,weeKtemp);

                                        goodsInfo.getWeekBlockMap().put(mealSelection,newBlock);
                                    }else{
                                        goodsInfo.getWeekBlockMap().put(mealSelection,weekBlock);
                                    }
                                }else{
                                    goodsInfo.getWeekBlockMap().put(mealSelection,weekBlock);
                                }

                            }
                            //定制套餐判断套餐名称
                            if(serviceCode.equals(GoodsEnums.service.SETMENU.getCode())||serviceCode.equals(GoodsEnums.service.TEA.getCode())){
                                String packageName  = getValue(map,ProductImportSetMenuEnums.setMenu.PACKAGENAME.getCellName());
                                goodsInfo.getMeatName().add(packageName);
                                // 拼接Blcok信息：午餐|周六-周一
                                if(StringUtils.isNotEmpty(weekBlock)&&!weekBlock.equals("null")){
                                    Map<String,String> weekBlockMap = goodsInfo.getWeekBlockMap();
                                    if(weekBlockMap.containsKey(packageName)){
                                        String weeKtemp = weekBlockMap.get(packageName);
                                        if(StringUtils.isNotEmpty(weeKtemp) ){
                                            weekBlock = getNewWeekBlockStr(weekBlock,weeKtemp);
                                        }else{
                                            weekBlock = null;
                                        }

                                        goodsInfo.getWeekBlockMap().put(mealSelection,weekBlock);
                                    }else{
                                        goodsInfo.getWeekBlockMap().put(mealSelection,weekBlock);
                                    }
                                    goodsInfo.getWeekBlockMap().put(packageName,weekBlock);
                                }else{
                                    goodsInfo.getWeekBlockMap().put(packageName,weekBlock);
                                }

                            }

                            //单杯茶饮判断套餐名称
                            if (serviceCode.equals(GoodsEnums.service.DRINK.getCode())){
                                goodsInfo.setServiceName(name);
                                // 拼接Blcok信息：午餐|周六-周一
                                if(StringUtils.isNotEmpty(weekBlock)&&!weekBlock.equals("null")){
                                    goodsInfo.getWeekBlockMap().put(name,weekBlock);
                                }else{
                                    goodsInfo.getWeekBlockMap().put(name,weekBlock);
                                }

                            }

                            //住宿判断房型
                            if (serviceCode.equals(GoodsEnums.service.ACCOM.getCode())){

                                goodsInfo.setRoomType(getValue(map,ProductImportSetMenuEnums.setMenu.ROOM_TYPE.getCellName()));
                                goodsInfo.getBedTypeMap().put(getValue(map,ProductImportSetMenuEnums.setMenu.CAN_RANGE_TYPE.getCellName()),getValue(map,ProductImportSetMenuEnums.setMenu.BED_TYPE.getCellName()));
                                if(StringUtils.isNotEmpty(weekBlock)&&!weekBlock.equals("null")){
                                    goodsInfo.getWeekBlockMap().put(goodsInfo.getRoomType(),weekBlock);
                                }else{
                                    goodsInfo.getWeekBlockMap().put(goodsInfo.getRoomType(),weekBlock);
                                }
                            }
                            if(StringUtils.isNotEmpty(clauseBlockStr)){
                                goodsInfo.setClauseBlock(clauseBlockStr.toString());
                            }
                            goodsInfo.setServiceCode(serviceCode);
                            goodsInfo.setGiftCode(giftCode);
                            goodsInfo.setGoodsImportVo(goodsImportVo);
                            goodsInfoList.add(goodsInfo);
                        }else{
                            int i = 0 ;
                            for (GoodsInfo vo :goodsInfoList) {
                                i ++ ;
                                if(vo.toString().equals(keyStr)){
                                    if(serviceCode.equals(GoodsEnums.service.BUFFET.getCode())){
                                        vo.getMeatName().add(mealSelection);
                                        vo.setMealSelection(mealSelection);
                                        // 拼接Blcok信息：午餐|周六-周一
                                        if(StringUtils.isNotEmpty(weekBlock)&&!weekBlock.equals("null")){
                                            Map<String,String> weekBlockMap = vo.getWeekBlockMap();
                                            if(weekBlockMap.containsKey(mealSelection)){
                                                String weeKtemp = weekBlockMap.get(mealSelection);
                                                if(StringUtils.isNotEmpty(weeKtemp) ){
                                                    weekBlock =  getNewWeekBlockStr(weekBlock,weeKtemp);
                                                }else{
                                                    weekBlock = null;
                                                }
                                                vo.getWeekBlockMap().put(mealSelection,weekBlock);
                                            }else{
                                                vo.getWeekBlockMap().put(mealSelection,weekBlock);
                                            }
                                        }else{
                                            vo.getWeekBlockMap().put(mealSelection,weekBlock);
                                        }
                                        if(StringUtils.isNotEmpty(clauseBlockStr)){
                                            vo.setClauseBlock(clauseBlockStr.toString());
                                        }
                                    }
                                    //定制套餐判断套餐名称
                                    if(serviceCode.equals(GoodsEnums.service.SETMENU.getCode())||serviceCode.equals(GoodsEnums.service.TEA.getCode())||serviceCode.equals(GoodsEnums.service.SPA.getCode())){
                                        String packageName  = getValue(map,ProductImportSetMenuEnums.setMenu.PACKAGENAME.getCellName());
                                        vo.getMeatName().add(packageName);
                                        // 拼接Blcok信息：午餐|周六-周一
                                        if(StringUtils.isNotEmpty(weekBlock)&&!weekBlock.equals("null")){
                                            vo.getWeekBlockMap().put(packageName,weekBlock);
                                        }else{
                                            vo.getWeekBlockMap().put(packageName,weekBlock);
                                        }
                                    }
                                    //单杯茶饮判断套餐名称
                                    if (serviceCode.equals(GoodsEnums.service.DRINK.getCode())){
                                        vo.setServiceName(name);
                                        // 拼接Blcok信息：午餐|周六-周一
                                        if(StringUtils.isNotEmpty(weekBlock)&&!weekBlock.equals("null")){
//                                            vo.getBuffetWeek().add(name+"|"+weekBlock);
//                                            vo.getBuffetWeekBlock().add(name+"|"+weekBlock );
                                            vo.getWeekBlockMap().put(name,weekBlock);
                                        }else{
//                                            vo.getBuffetWeek().add(name);
//                                            vo.getBuffetWeekBlock().add(name);
                                            vo.getWeekBlockMap().put(name,weekBlock);
                                        }

                                    }
                                    //下午茶判断餐段
//                                    if (serviceCode.equals(GoodsEnums.service.TEA.getCode())){
//                                        vo.getMeatName().add(getValue(map,ProductImportSetMenuEnums.setMenu.PACKAGENAME.getCellName()));
//                                    }

                                    //住宿判断房型
                                    if (serviceCode.equals(GoodsEnums.service.ACCOM.getCode())){
                                        vo.setRoomType(getValue(map,ProductImportSetMenuEnums.setMenu.ROOM_TYPE.getCellName()));
                                        vo.getBedTypeMap().put(getValue(map,ProductImportSetMenuEnums.setMenu.CAN_RANGE_TYPE.getCellName()),getValue(map,ProductImportSetMenuEnums.setMenu.BED_TYPE.getCellName()));
                                        if(StringUtils.isNotEmpty(weekBlock)&&!weekBlock.equals("null")){
                                            vo.getWeekBlockMap().put(vo.getRoomType(),weekBlock);
                                        }else{
                                            vo.getWeekBlockMap().put(vo.getRoomType(),weekBlock);
                                        }

                                    }
                                    if(StringUtils.isNotEmpty(clauseBlockStr)){
                                        vo.setClauseBlock(clauseBlockStr.toString());
                                    }
                                }else{
                                    if(goodsInfoList.size() == i){
                                        GoodsInfo goodsInfo=new GoodsInfo();
                                        goodsInfo.setHotelCh(hotel_name_cn);
                                        goodsInfo.setShopCh(shop_name);
                                        goodsInfo.setGift(discount);
                                        goodsInfo.setRows(map);
                                        if(serviceCode.equals(GoodsEnums.service.BUFFET.getCode())){//自助餐
                                            goodsInfo.getMeatName().add(mealSelection);
                                            goodsInfo.setMealSelection(mealSelection);
                                            // 拼接Blcok信息：午餐|周六-周一
                                            if(StringUtils.isNotEmpty(weekBlock)&&!weekBlock.equals("null")){
                                                Map<String,String> weekBlockMap = goodsInfo.getWeekBlockMap();
                                                if(weekBlockMap.containsKey(mealSelection)){
                                                    String weeKtemp = weekBlockMap.get(mealSelection);
                                                    if(StringUtils.isNotEmpty(weeKtemp) ){
                                                        weekBlock = getNewWeekBlockStr(weekBlock,weeKtemp);

                                                    }else{
                                                        weekBlock = null;
                                                    }
                                                    goodsInfo.getWeekBlockMap().put(mealSelection,weekBlock);
                                                }else{
                                                    goodsInfo.getWeekBlockMap().put(mealSelection,weekBlock);
                                                }
//                                                goodsInfo.getBuffetWeek().add(mealSelection+"|"+weekBlock);
//                                                goodsInfo.getBuffetWeekBlock().add(mealSelection+"|"+weekBlock );
                                            }else{
//                                                goodsInfo.getBuffetWeek().add(mealSelection);
                                                goodsInfo.getWeekBlockMap().put(mealSelection,weekBlock);
                                            }

                                        }
                                        //定制套餐判断套餐名称

                                        if(serviceCode.equals(GoodsEnums.service.SETMENU.getCode())||serviceCode.equals(GoodsEnums.service.TEA.getCode())||serviceCode.equals(GoodsEnums.service.SPA.getCode())){
                                            String packageName  = getValue(map,ProductImportSetMenuEnums.setMenu.PACKAGENAME.getCellName());
                                            goodsInfo.getMeatName().add(packageName);
                                            // 拼接Blcok信息：午餐|周六-周一
                                            if(StringUtils.isNotEmpty(weekBlock)&&!weekBlock.equals("null")){
//                                                goodsInfo.getBuffetWeek().add(packageName+"|"+weekBlock);
//                                                goodsInfo.getBuffetWeekBlock().add(packageName+"|"+weekBlock );
                                                goodsInfo.getWeekBlockMap().put(packageName,weekBlock);
                                            }else{
//                                                goodsInfo.getBuffetWeek().add(mealSelection);
//                                                goodsInfo.getBuffetWeekBlock().add(name);
                                                goodsInfo.getWeekBlockMap().put(packageName,weekBlock);
                                            }
                                        }

                                        //单杯茶饮判断套餐名称
                                        if (serviceCode.equals(GoodsEnums.service.DRINK.getCode())){
                                            vo.setServiceName(name);
                                            // 拼接Blcok信息：午餐|周六-周一
                                            if(StringUtils.isNotEmpty(weekBlock)&&!weekBlock.equals("null")){
//                                                vo.getBuffetWeek().add(name+"|"+weekBlock);
//                                                vo.getBuffetWeekBlock().add(name+"|"+weekBlock );
                                                goodsInfo.getWeekBlockMap().put(name,weekBlock);
                                            }else{
//                                                vo.getBuffetWeek().add(name);
//                                                goodsInfo.getBuffetWeekBlock().add(name);
                                                goodsInfo.getWeekBlockMap().put(name,weekBlock);
                                            }

                                        }

                                        //住宿判断房型
                                        if (serviceCode.equals(GoodsEnums.service.ACCOM.getCode())){
                                            goodsInfo.setRoomType(getValue(map,ProductImportSetMenuEnums.setMenu.ROOM_TYPE.getCellName()));
                                            goodsInfo.getBedTypeMap().put(getValue(map,ProductImportSetMenuEnums.setMenu.CAN_RANGE_TYPE.getCellName()),getValue(map,ProductImportSetMenuEnums.setMenu.BED_TYPE.getCellName()));
                                            if(StringUtils.isNotEmpty(weekBlock)&&!weekBlock.equals("null")){
                                                goodsInfo.getWeekBlockMap().put(goodsInfo.getRoomType(),weekBlock);
                                            }else{
                                                goodsInfo.getWeekBlockMap().put(goodsInfo.getRoomType(),weekBlock);
                                            }

                                        }
                                        if(StringUtils.isNotEmpty(clauseBlockStr)){
                                            goodsInfo.setClauseBlock(clauseBlockStr.toString());
                                        }
                                        goodsInfo.setServiceCode(serviceCode);
                                        goodsInfo.setGiftCode(giftCode);
                                        goodsInfo.setGoodsImportVo(goodsImportVo);
                                        goodsInfoList.add(goodsInfo);
                                        break;
                                    }
                                }
                            }

                        }
                        //处理当前页的状态
                        //把row记录更新为成功
                        productImportRow.setState(2);
                        productImportRow.setUpdateTime(new Date());
                        productImportRowService.updateById(productImportRow);
                    }catch (Exception e){
                        log.info("***数据多条***：导入数据判断处理逻辑异常");
                        log.error(e.getMessage(),e);
                    }
                }
                //逻辑处理
                String serviceCode=productImportSheet.getServiceCode();
                for (GoodsInfo goodsVo :goodsInfoList){
                    Map map = goodsVo.getRows();
                    final Shop shop = shopService.selectListFor(goodsVo,serviceCode);
                    //正常情况是会查到一条数据的  如果未查到则新增  现阶段认为已存在
                    Boolean isExsit = true;

                    //TODO shop以后手动添加
                    if (shop == null) {
                        isExsit = false;
                        log.info("***商户不存在***：未查到商户信息 row_id={},请手动添加", JSONArray.toJSONString(goodsVo));
                        continue;
                    }
                    //上面成功添加商户信息的代码已经暂时保存在了shopserviceimpl中，处于注释状态。
                    List<ShopItem> items = new ArrayList<>(0);
                    Goods goods = null;
                    Long goods_id=0L;
                    // TODO 商户已存在处理商品
                    // SHOP表示已经存在
                    items = shopitemsService.getShopitemsByShopId(shop,serviceCode);
                    //平铺goods
                    Map<Object,Object> tmpMap=Maps.newHashMap();
                    final List<Goods> goodsList = goodsService.selectForList(goodsVo,serviceCode,shop);
                    for (Goods gds :goodsList){
                        try {
                            //解析goods的items，返回map<name,item>对象
                            Map maptemp =XML.xmlStr2Map(gds.getItems());
                            tmpMap.put(gds,maptemp);
                        } catch (Exception e) {
                            log.info("***格式异常***: goodsVo = "  + gds.getId());
                            log.error(e.getMessage(),e);
                            resultVo.setCode(200);
                            resultVo.setMsg(e.getMessage());
                        }
                    }

                    if(tmpMap.size() > 0){
                        Set<String> tempNameSet = goodsVo.getMeatName();// new HashSet(0);
                        if(serviceCodeTemp.equals(GoodsEnums.service.BUFFET.getCode())) {
//                            tempNameSet = goodsVo.getWeekBlockMap().keySet();//餐段集合


                            goods =   getGoods(tmpMap,goodsVo);

                        }else if(serviceCodeTemp.equals(GoodsEnums.service.SETMENU.getCode()) || serviceCodeTemp.equals(GoodsEnums.service.TEA.getCode()) ||serviceCode.equals(GoodsEnums.service.SPA.getCode())){
                            //判断items名字是否一样

                            /**
                             * 对比goods的所有数据结构
                             */
                            goods =   getGoods(tmpMap,goodsVo);
                        }else if(serviceCodeTemp.equals(GoodsEnums.service.ACCOM.getCode())) {
                            /**
                             * 匹配住宿的goods中的item和excel的item和shop的item是否一致
                             */
                            items.clear();
                            items = shopitemsService.getShopitems(shop,serviceCode,goodsVo);

//                            Set roomTypeSet = new HashSet();
//                            roomTypeSet.add(goodsVo.getRoomType());
//                            for (Object o : tmpMap.keySet()) {
//                                Goods tempGoods = (Goods) o;
//                                Map tempMap = (Map) tmpMap.get(o);
//
//
//                                if (!tempMap.keySet().containsAll(roomTypeSet)) {
//                                   continue;
//                                }
//
//                                List<String> shopItemsTemp = Lists.newArrayList();
//                                for (Object obj : tempMap.values()) {
//                                    ShopItem it = (ShopItem) obj;
//                                    if (StringUtils.isNotEmpty(it.getBlock())) {
//                                        shopItemsTemp.add(it.getBlock());
//                                    }
//                                }
//
//                                if (shopItemsTemp.size() == 0 && StringUtils.isNotEmpty(goodsVo.getClauseBlock())) {
//                                    continue;
//                                }
//                                if (shopItemsTemp.size() > 0 && StringUtils.isEmpty(goodsVo.getClauseBlock())) {
//                                    continue;
//                                }
//                                String shopItemsTempStr = String.join(",",shopItemsTemp);
//
//
//                                    if (StringUtils.isNotEmpty(tempGoods.getBlockCode()) && StringUtils.isNotEmpty(goodsVo.getClauseBlock())) {
//                                        List<String> tempgoodsBlock = Arrays.asList(tempGoods.getBlockCode().split(","));
//                                        List<String> tempItemBlock =  Arrays.asList(shopItemsTempStr.split(","));
//                                        if(!tempgoodsBlock.containsAll(tempItemBlock)){
//                                            continue;
//                                        }
//
//                                    }
//
//                                goods = tempGoods;
//
//                            }
                            goods =   getGoods(tmpMap,goodsVo);


                        }else if(serviceCodeTemp.equals(GoodsEnums.service.DRINK.getCode()) ){
                            //判断items名字是否一样
                            tempNameSet = goodsVo.getWeekBlockMap().keySet();//餐段集合

                            Map<String,String> tempNameSetTemp = goodsVo.getWeekBlockMap();


                            for (Object o : tmpMap.keySet()) {
                                Goods tempGoods = (Goods) o;
                                Map tempMap = (Map) tmpMap.get(o);
                                //暂时认为item.name一致就是同一个产品，在同一个set集合中
                                /**
                                 *取出查询出来的goods的items的block和excel中block是否一致，做匹配
                                 */

//                                            tempGoodsVoBlock.append(goodsVo.getClauseBlock());

                                for (Object obj : tempMap.values()) {

                                    ShopItem it = (ShopItem) obj;
                                    if (StringUtils.isNotEmpty(it.getBlock())) {
                                        if (tempNameSetTemp.containsKey(it.getName()) && it.getBlock().equals(tempNameSetTemp.get(it.getName())+"")) {
                                            goods = tempGoods;

                                        }

                                    } else if( null  == tempNameSetTemp.get(it.getName())){
                                        if (tempNameSetTemp.containsKey(it.getName())) {
                                            goods = tempGoods;
                                            break;
                                        }

                                    }
                                }
                            }

                        }else if(serviceCodeTemp.equals(GoodsEnums.service.GYM.getCode()) ){
                            goods = goodsList.get(0);

                        }


                    }
                        //----------------------------------------------------

                    if(goods == null){//items不能等于0
                        Map goodsMap = new HashMap(0);
                        Map params = new HashMap(0);
                        //否则表示没有，新增shop，直接保存item和goods
                        //TODO 数据处理分析逻辑
                        List<ShopItem> result = new ArrayList<>(0);
                        if(serviceCode.equals(GoodsEnums.service.SETMENU.getCode())||serviceCode.equals(GoodsEnums.service.TEA.getCode())||serviceCode.equals(GoodsEnums.service.SPA.getCode())){
                            result= shopAndItemInfoService.analysisSetMenuItem(serviceCode,map,items,shop,null,goodsVo, params);
                        }
                        //----------------------------------------------------
                        if(serviceCode.equals(GoodsEnums.service.BUFFET.getCode())){
                            result= shopAndItemInfoService.analysisBuffetItem( serviceCode ,map,items,shop,goodsVo,params);
//                            result= shopAndItemInfoService.analysisSetMenuItem(map,items,shop,blockMap,goodsVo.getMeatName());
                        }
                        if (serviceCode.equals(GoodsEnums.service.ACCOM.getCode())){
                            result= shopAndItemInfoService.analysisAccomItem( serviceCode ,map,items,shop,null,goodsVo,params);
                        }

                        if (serviceCode.equals(GoodsEnums.service.DRINK.getCode())){
                            result= shopAndItemInfoService.analysisDrinkItem( serviceCode ,map,items,shop,goodsVo,params);
                        }
                        if(result.size() == 0 ){
                            log.info("****当前产品没有Item****{}-{}",goodsVo.getHotelCh(),goodsVo.getShopCh());
                            continue;
                        }
                        //----------------------------------------------------

                        goodsMap = goodsService.saveGoodsInfoVo(shop,result,map,null,goodsImportVo.getProjectId(),serviceCode,goodsVo);

                        if(goodsMap.containsKey("goods_id")&& goodsMap.get("goods_id") != null){
                            goods_id = Long.parseLong(goodsMap.get("goods_id")+"");
                            insertPrjGoos(prjGroup,goods_id);
                        }else {
                            //goodsMap中包含error和shopId，error：表示失败，goodsId不为空则为true；
                            //表示页面的产品组需要设置
                            final Object resultStr = goodsMap.get("result");
                            try {
                                if (resultStr != null && resultStr.toString().contains("#")){
                                    String str1=resultStr.toString();
                                    String[] s = str1.split("\\D+");
                                    String goodsIdStr=str1.substring(str1.indexOf("j")+1,str1.indexOf(")"));
                                    if(s.length > 0 && s[1].length() > 0){
                                        goods_id=Long.parseLong(s[1]);
                                        insertPrjGoos(prjGroup, goods_id);
                                    }else{
                                        log.info("************"+str1);
                                    }

                                }else {

                                    log.error("goods==null处理异常 :{}- {}",resultStr,goodsMap.get("error") );
                                }
                            }catch (Exception e){
                                log.error(e.getMessage(),e);
                                log.info("goodsMap:{}",goodsMap);
                            }

                        }
                    }else{
                        if (serviceCode.equals(GoodsEnums.service.ACCOM.getCode())){
                            goods.setBlockCode(goodsVo.getClauseBlock());
                            goodsService.updateById(goods);
                        }


                        goods_id=goods.getId();
                        insertPrjGoos(prjGroup,goods_id);
                    }

                }

                productImportSheet.setState(2);
                productImportSheet.setUpdateTime(new Date());
                productImportSheetService.updateById(productImportSheet);
            }

            final ProductImportWorkbook productImportWorkbook = productImportWorkbookService.selectById(batchId);
            productImportWorkbook.setState(2);
            productImportWorkbook.setUpdateTime(new Date());
            productImportWorkbookService.updateById(productImportWorkbook);
            resultVo.setCode(100);
            resultVo.setMsg("操作成功");
            resultVo.setResult(resultMsg);
            log.info("***数据导入成功***\n");
        } catch (Exception e) {
            log.error("***数据导入失败***：\n" + e.getMessage(),e);
            resultVo.setCode(200);
            resultVo.setMsg("***数据导入失败***：\n" + e.getMessage());
            return resultVo;
        }
        return resultVo;
    }

    private String getXmlString(List<ShopItem> result) {
        StringBuffer goodsStr = new StringBuffer("<xml>");
        result.forEach(item -> {
            goodsStr.append("<item id=\"" +item.getId());
            goodsStr.append("\"");
            goodsStr.append("name =\""+item.getName()+"\"");
            if(StringUtils.isNotEmpty(item.getBlock())){
                goodsStr.append("block =\""+item.getBlock()+"\"");
            }
            goodsStr.append("</item>");
        });
        goodsStr.append("</xml>");
        return goodsStr.toString();
    }

    private void insertPrjGoos(PrjGroup prjGroup, Long goods_id) {
        //检查project_goods是否存在  不存在则新增
        Wrapper<ProjectGoods> projectGoodsWrapper=new Wrapper<ProjectGoods>() {
            @Override
            public String getSqlSegment() {
                return "where project_id="+prjGroup.getProjectId() + " and goods_id="+goods_id;
            }
        };
        final ProjectGoods projectGoods = projectGoodsService.selectOne(projectGoodsWrapper);
        if (projectGoods == null){
            ProjectGoods projectGoodsTmp=new ProjectGoods();
            projectGoodsTmp.setGoodsId(goods_id);
            projectGoodsTmp.setProjectId(prjGroup.getProjectId());
            projectGoodsService.insert(projectGoodsTmp);
        }

        //检查prj_group_goods是否存在  不存在则新增
        Wrapper<PrjGroupGoods> prjGroupGoodsWrapper=new Wrapper<PrjGroupGoods>() {
            @Override
            public String getSqlSegment() {
                return "where project_id="+prjGroup.getProjectId() + " and goods_id="+goods_id + " and group_id="+prjGroup.getId() + " and online=1";
            }
        };
        final PrjGroupGoods groupGoods = prjGroupGoodsService.selectOne(prjGroupGoodsWrapper);
        if (groupGoods == null){
            //插入产品组信息
            PrjGroupGoods prjGroupGoods=new PrjGroupGoods();
            prjGroupGoods.setGoodsId(goods_id);
            prjGroupGoods.setGroupId(prjGroup.getId());
            prjGroupGoods.setProjectId(prjGroup.getProjectId());
            prjGroupGoods.setOnline(1);
            prjGroupGoodsService.insert(prjGroupGoods);
        }else{
            log.info("数据已存在！！！不重复添加s ：goodsid = " + goods_id.toString() );
        }
    }

    /**
     * 读取上传的Excel并入库
     * @param goodsImportVo
     * @return batchId
     */
    private String readFileAndSave(GoodsImportVo goodsImportVo,MultipartFile multipartFile, String productGroupName) throws Exception{
        //1.读取表格数据并入库
        final File file = FileUtils.multiTransferToFile(multipartFile, "/tmp");
        final ExcelReader excelReader = ExcelUtil.getReader(file);
        final Workbook workbook = excelReader.getWorkbook();

//        final Workbook workbook = ExcelUtils.getWorkbook(multipartFile, "/tmp");
        List<ProductSheetVo> sheetVoList=Lists.newArrayList();
        Integer totalRow=0;
        //获取sheet总数
        int numberOfSheets=workbook.getNumberOfSheets();
        for (int i=0;i<numberOfSheets;i++){
            //获取sheet
            final Sheet sheet = workbook.getSheetAt(i);
            String sheetName = sheet.getSheetName();
            log.info("sheetName -> " + sheetName);
            // 判断Sheet是否和产品组一致
            if(!sheetName.trim().equals(productGroupName.trim())) {
                continue;
            }
            //获取sheet名  作为产品组名
            ProductSheetVo productSheetVo=new ProductSheetVo();
            productSheetVo.setSheetIndex(i);
            productSheetVo.setSheetName(sheet.getSheetName());

            final int physicalNumberOfRows = sheet.getPhysicalNumberOfRows();
            if (physicalNumberOfRows<2){
                continue;
            }
            totalRow += (physicalNumberOfRows-1);
            List<ProductRowVo> rowVoList=Lists.newArrayList();
            List<String> titleList=Lists.newArrayList();
            int serviceIndex=-1;
            String serviceName="";
            for (int j=0;j<physicalNumberOfRows;j++){
                final Row row = sheet.getRow(j);
                if (StringUtils.isBlank(row.getCell(0).getStringCellValue())){
                    log.info("该行第一个单元格数据为空");
                    continue;
                }
                int physicalNumberOfCells = row.getPhysicalNumberOfCells();
                Map<String,String> rowMap=Maps.newHashMap();
                Integer k1 = 0;
                for (int k=0;k<physicalNumberOfCells;k++){
                    //标题行
                    if (j==0){
                        log.info("*********** = 标题行 = " + row.getCell(k).getStringCellValue().trim());

                        if (row.getCell(k).getStringCellValue().equalsIgnoreCase(ProductImportSetMenuEnums.setMenu.SERVICE.getCellName())){
                            serviceIndex=k;
                        }
                        titleList.add(row.getCell(k).getStringCellValue().trim());
                    }else {
                        //如果已获取serviceIndex 且k=serviceIndex 且serviceName不为空
                        if (serviceIndex>-1 && k==serviceIndex && StringUtils.isBlank(serviceName)){
                            serviceName=row.getCell(k).getStringCellValue();
                        }
                        try {

                            //如果单元格为空且不是最后一个单元格  则把cells数量加1
                            if (row.getCell(k) == null && k<titleList.size()-1){
                                physicalNumberOfCells++;
                                continue;
                            }
                            CellType cellTypeEnum =row.getCell(k).getCellTypeEnum();
                            if (cellTypeEnum.equals(CellType.STRING)){
                                 rowMap.put(titleList.get(k).toLowerCase(),row.getCell(k)==null?null:row.getCell(k).getStringCellValue().trim().replaceAll("\n","").replaceAll("\r",""));
                            }
                            if (cellTypeEnum.equals(CellType.NUMERIC)){
                                rowMap.put(titleList.get(k),row.getCell(k)==null?null:((int)row.getCell(k).getNumericCellValue())+"");
                            }

                        }catch (Exception e){
                            log.info("**********************************");
                            log.info("**********  读取标题行出错 *********");
                            log.info("**********************************");
                            log.error(e.getMessage(),e);
                        }
                    }
                }


                if (j>0){
                    //设置行信息
                    ProductRowVo productRowVo=new ProductRowVo();
                    productRowVo.setRowIndex(j);
                    productRowVo.setRowMap(rowMap);
                    productRowVo.setId(UUID.randomUUID().toString());
                    rowVoList.add(productRowVo);
                }
            }

            //设置sheet标题
            productSheetVo.setTitleList(titleList);
            productSheetVo.setRowVoList(rowVoList);
            productSheetVo.setId(UUID.randomUUID().toString());
            productSheetVo.setServiceCode(GoodsEnums.service.findByName(serviceName)==null?null:GoodsEnums.service.findByName(serviceName).getCode());
            sheetVoList.add(productSheetVo);
        }
        ProductWorkbookVo productWorkbookVo=new ProductWorkbookVo();
        productWorkbookVo.setBatchId(UUID.randomUUID().toString());
        productWorkbookVo.setFileName(multipartFile.getName());
        productWorkbookVo.setBankId(goodsImportVo.getBankId());
        productWorkbookVo.setProjectId(goodsImportVo.getProjectId());
        productWorkbookVo.setTotalSheet(numberOfSheets);
        productWorkbookVo.setTotalRow(totalRow);
        productWorkbookVo.setCreateTime(new Date());
        productWorkbookVo.setUpdateTime(new Date());

        //2.把表格中的数据插入到数据库中  一个上传批次表(批次id为上传年月日时分、批次上传时间、一共多少条数据、处理成功多少条数据、处理失败多少条数据)，一个批次详情表(ID、批次ID、sheet名、行数据json、处理结果、备注、处理时间)
        //insert ProductWorkbookVo
        ProductImportWorkbook productImportWorkbook=new ProductImportWorkbook();
        BeanUtils.copyProperties(productWorkbookVo,productImportWorkbook);
        productImportWorkbook.setState(0);//未处理
        productImportWorkbookService.insert(productImportWorkbook);

        //insert ProductSheetVo
        for (ProductSheetVo productSheetVo:sheetVoList){
            productSheetVo.setBatchId(productWorkbookVo.getBatchId());
            ProductImportSheet productImportSheet=new ProductImportSheet();
            BeanUtils.copyProperties(productSheetVo,productImportSheet);
            productImportSheet.setTitle(JSON.toJSONString(productImportSheet.getTitleList()));
            productImportSheet.setState(0);//未处理
            productImportSheet.setCreateTime(new Date());
            productImportSheet.setUpdateTime(new Date());
            productImportSheetService.insert(productImportSheet);

            //insert productRowVo
            for (ProductRowVo productRowVo: productSheetVo.getRowVoList()){
                productRowVo.setBatchId(productWorkbookVo.getBatchId());
                ProductImportRow productImportRow=new ProductImportRow();
                BeanUtils.copyProperties(productRowVo,productImportRow);
                //把rowMap 转换成value
                productImportRow.setSheetName(productGroupName);
                productImportRow.setValue(JSON.toJSONString(productImportRow.getRowMap()));
                productImportRow.setState(0);//未处理
                productImportRow.setCreateTime(new Date());
                productImportRow.setUpdateTime(new Date());
                productImportRowService.insert(productImportRow);
            }
        }
        return productWorkbookVo.getBatchId();
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
     *
     * @param tmpMap
     * @param goodsVo excle的对象
     * @return
     */
    private Goods getGoods( Map tmpMap,GoodsInfo goodsVo) {
        Goods goods  = null;
        Map<String,String> weekBlock = goodsVo.getWeekBlockMap();
        for (Object o : tmpMap.keySet()) {//已存在的goods
            Goods tempGoods = (Goods) o;
            Map<String,ShopItem> tempMap = (Map) tmpMap.get(o);
            //暂时认为item.name一致就是同一个产品，在同一个set集合中
            /**
             *取出查询出来的goods的items的block和excel中block是否一致，做匹配
             */
            //先判断item的名字是否一样
            if(!weekBlock.keySet().containsAll(tempMap.keySet())){
                continue;
            }
            Map<String,String> itemBlock = new HashMap<>();
            //在匹配block
            tempMap.values().forEach(item ->{
                itemBlock.put(item.getName(),item.getBlock());
            });
            if(!weekBlock.values().containsAll(itemBlock.values())){
                continue;
            }
            if(tempGoods.getBlockCode() == goodsVo.getClauseBlock()){
                goods = tempGoods;
                break;
            }
            if(tempGoods.getBlockCode() == null  && goodsVo.getClauseBlock() != null && goodsVo.getClauseBlock().length()>0){
                continue;
            }
            if(tempGoods.getBlockCode() != null && tempGoods.getBlockCode().length() > 0 && goodsVo.getClauseBlock() != null && goodsVo.getClauseBlock().length()>0){
                List<String> temp = Arrays.asList(tempGoods.getBlockCode().split(","));
                List<String> vo = Arrays.asList(goodsVo.getClauseBlock().split(","));
                if(!temp.containsAll(vo)){
                    continue;
                }
            }
            goods = tempGoods;



        }

        return goods;

    }

    /**
     * 获取新的block
     * @param weekBlock 当前excel的block
     * @param weeKtemp 以前存在的Block
     * @return
     */
    private static String getNewWeekBlockStr(String weekBlock,String weeKtemp ){
        String newBlock = null;
        List<Integer> newList = ClfCommondUtils.getNumberList(weekBlock);

        newList.addAll(ClfCommondUtils.getNumberList(weeKtemp));
        Integer max = Collections.max(newList);
        Integer min = Collections.min(newList);
        Set<String> allBlock = new HashSet<>(0);
        if(min.equals(1) && max.equals(7) && newList.size() == 4){
            newBlock = null;
        }else if(newList.size() != 4 ){
            if(min.equals(1) && max.equals(7) && (newList.contains(2) || newList.contains(6)) ){
                newBlock = null;
            }else{
                newBlock = weekBlock +","+weeKtemp;
            }

        }else{
            if(weeKtemp.contains("-")){
                for (String s : weeKtemp.split("-")) {
                    allBlock.add(s);
                }
            }
            if(weekBlock.contains("-")){
                for (String s : weekBlock.split("-")) {
                    allBlock.add(s);
                }
            }
            String maxStr ="";
            String minStr ="";
            for (String str : allBlock) {
                if(str.contains(min+"") ){
                    minStr = str;
                }
                if(str.contains(max+"") ){
                    maxStr = str;
                }
            }

            newBlock = minStr +"-"+maxStr;
        }
        return newBlock;
    }


}
