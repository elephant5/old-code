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
import com.colourfulchina.bigan.enums.ProductImportSetMenuEnums;
import com.colourfulchina.bigan.mapper.GoodsMapper;
import com.colourfulchina.bigan.mapper.ShopItemMapper;
import com.colourfulchina.bigan.service.*;
import com.colourfulchina.bigan.utils.FileUtils;
import com.colourfulchina.bigan.utils.XML;
import com.colourfulchina.bigan.utils.XMLUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.xml.Jdbc4SqlXmlHandler;
import org.springframework.jdbc.support.xml.SqlXmlValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.lang.reflect.Field;
import java.sql.SQLXML;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/temp")
public class ImportTempBakController {
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
    ShopItemMapper shopItemMapper;
    @Autowired
    GoodsMapper goodsMapper;



    @PostMapping(value = "/export")
    public GoodsImportResultVo importGoods(Integer bankId,Long projectId,MultipartFile multipartFile,Long groupId ){
        GoodsImportResultVo resultVo=new GoodsImportResultVo();
        try {
            Map<String,String> resultMsg=Maps.newTreeMap();
            if ( projectId == null || multipartFile == null){
                log.info("输入参数不完整");
                resultVo.setCode(300);
                resultVo.setMsg("输入参数不完整");
                return resultVo;
            }



            Wrapper<PrjGroupGoods> prjGroupGoodsWrapper=new Wrapper<PrjGroupGoods>() {
                @Override
                public String getSqlSegment() {
                    return "where project_id="+projectId + " and online=1 and group_id = " + groupId;
                }
            };
            final  List<PrjGroupGoods> groupGoods = prjGroupGoodsService.selectList(prjGroupGoodsWrapper);
//            Map<Long,Long> goodsProjectMap = new HashMap<>();
//            groupGoods.forEach(pro ->{
//                goodsProjectMap.put(pro.getGoodsId(),pro.getProjectId());
//            });
            //读取上传的Excel并入库
            List<ProductRowVo> rowVoList =readFileAndSave(multipartFile);
            List<ProductRowVo> lessList = new ArrayList<>();
            for (ProductRowVo row: rowVoList) {
                final Map map = row.getRowMap();
                Wrapper<Shop> shopWrapper=new Wrapper<Shop>() {
                    @Override
                    public String getSqlSegment() {
                        return "where type='accom' and city='"+getValue(map,ProductImportSetMenuEnums.setMenu.CITY.getCellName())
                                + "' and hotel='" + getValue(map,ProductImportSetMenuEnums.setMenu.HOTEL_NAME_CN.getCellName())+"' and name='客房部'";
                    }
                };
                List<Shop> shopList = shopService.selectList(shopWrapper);
                Shop shops =  null;
                if(shopList.size() > 1){
                    log.info("多商户，信息{}",JSON.toJSONString(map));
                    shops = shopList.get(0);
                }else if(shopList.size() == 1){
                    shops = shopList.get(0);
                }
                if(null != shops){
                    final Shop shop = shops;
                    Wrapper<Goods> goodsWrapper = new Wrapper<Goods>() {
                        @Override
                        public String getSqlSegment() {
                            String sql  =  "where type='accom'  and gift ='NX' and shop_id = " + shop.getId();
                            if(StringUtils.isNotEmpty(getValue(map,"房型"))){
                               sql = sql + " and  title like  '" + getValue(map,"房型") +"'";
                            }

                            return sql;
                        }
                    };
                    List<Goods> goodsList = goodsService.selectList(goodsWrapper);
                    if(goodsList.size() > 0){
                        Boolean isExist = true;
                        Goods delGoods = null;
                        String block = getValue(map,"block");

                        //还有一种情况，shopID相同但是其他全部不相同。并且不在列表里，则匹配查询出来的goods；
                        if(isExist && delGoods == null){
                            for(Goods goods : goodsList) {

                                Map maptemp = XML.xmlStr2Map(goods.getItems());
                                for (Object o : maptemp.keySet()) {
                                    ShopItem item = (ShopItem) maptemp.get(o + "");
                                    if (item.getName().equals(getValue(map, "房型"))
                                            && ((item.getAddon() == null && getValue(map, "早餐") == null)
                                            || (StringUtils.isNotEmpty(item.getAddon()) && StringUtils.isNotEmpty(getValue(map, "早餐")) && item.getAddon().equals(getValue(map, "早餐"))))) {

                                        Wrapper<PrjGroupGoods> prjGroupGoodsWrappers=new Wrapper<PrjGroupGoods>() {
                                            @Override
                                            public String getSqlSegment() {
                                                return "where goods_id="+goods.getId() + " and  project_id != "+projectId +" and online=1";
                                            }
                                        };
                                        final  List<PrjGroupGoods> groupGoodss = prjGroupGoodsService.selectList(prjGroupGoodsWrappers);
                                        if(groupGoodss.size() == 0){
                                            goods.setClause(getValue(map,ProductImportSetMenuEnums.setMenu.CLAUSE.getCellName()));
                                            goods.setBlockCode(block);
                                            log.info("更新的goods:" + goods.getId() + "===" + JSON.toJSONString(goods));
                                            goodsService.updateById(goods);
                                            isExist = false;
                                            insertPrjGoos(projectId, goods.getId(), groupId);
                                            break;

                                        }

                                    }
                                }
                            }
                        }
//                        if(goodsList.size() == 1){
//                            insertPrjGoos(projectId,goodsList.get(0).getId(),groupId);
//                        }

                        if(isExist ){
                            //删除当前goods的关系，并重新创建
                            List<ShopItem> items  = getShopitems(shop,"accom",map.get("房型")+"",map.get("早餐")+"");
                            if(items.size() == 0 ){
                                log.info("****当前产品没有Item****{}",getValue(map,ProductImportSetMenuEnums.setMenu.HOTEL_NAME_CN.getCellName()));
                                continue;
                            }
                            createGoods(shop,map,items,projectId,groupId);
                            if(null != delGoods){
                                deletePrjGoos(projectId,delGoods.getId(),groupId);
                            }


                        }
                    }else{
                        List<ShopItem> items  = getShopitems(shop,"accom",map.get("房型")+"",map.get("早餐")+"");
                        createGoods(shop,map,items,projectId,groupId);

                    }
                }else{
                    log.info("****商户不存在，信息****{}",map.get(ProductImportSetMenuEnums.setMenu.HOTEL_NAME_CN.getCellName()));
                }


            }

            rowVoList.removeAll(lessList);
            log.info(""+rowVoList.size());
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            resultVo.setCode(200);
            resultVo.setMsg(e.getMessage());
        }

        resultVo.setCode(100);
        resultVo.setMsg("更新成功");
        return resultVo;
    }

    private void createGoods(Shop shop,Map map,List<ShopItem> items,Long project_id,Long groupId) {

        GoodsSaveVo goodsVo  = new GoodsSaveVo();
        goodsVo.setType("accom");
        goodsVo.setGift("NX");
        goodsVo.setProjects("{\"id\":"+project_id+"}");
        String addon = getValue(map,"早餐");
        String block = getValue(map,"block");
        String roomType = getValue(map,"房型");
        if(null != items && items.size() > 0    ){

            items.stream().filter(it -> it.getAddon().equals(addon)).collect(Collectors.toList());
            if(items.size() >1){
                items.stream().filter(it -> StringUtils.isNotEmpty(it.getNeeds())).collect(Collectors.toList());
            }

        }else{
//            Map params = new HashMap(   );
//            params.put("addon",addon);//餐型
//            params.put("needs",null);
////            params.put("blocks",block);//
//            ShopItem itemNew =  saveShopItemAccom( "accom" ,shop,roomType,params);
//            if(itemNew.getId()!= null){
////                itemNew.setBlock(block);
//                items.add(itemNew);
//            }
            log.info("****当前产品没有Item****{}",shop.getHotel());

        }
        goodsVo.setItems(JSONArray.toJSONString(items));
        goodsVo.setTitle(items.get(0).getName());
        goodsVo.setShop_id(shop.getId());
//        goodsVo.setChannel_id(7L);
        goodsVo.setClause(getValue(map,ProductImportSetMenuEnums.setMenu.CLAUSE.getCellName()));
        goodsVo.setBlock(block);


//        Jdbc4SqlXmlHandler jdb = new Jdbc4SqlXmlHandler();
//        SqlXmlValue re = jdb.newSqlXmlValue("");
//        jdb.
//        goodsVo.setSqlxml();

        Map goodsMap = obj2Map(goodsVo);
        goodsMapper.saveGoodsInfo(goodsMap);
        Long goods_id=0L;
        if(goodsMap.containsKey("goods_id")&& goodsMap.get("goods_id") != null){
            goods_id = Long.parseLong(goodsMap.get("goods_id")+"");
            insertPrjGoos(project_id,goods_id,groupId);
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
                        insertPrjGoos(project_id,goods_id,groupId);
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
    }

    private void insertPrjGoos(Long project, Long goods_id,Long groupId) {
        //检查project_goods是否存在  不存在则新增
        Wrapper<ProjectGoods> projectGoodsWrapper=new Wrapper<ProjectGoods>() {
            @Override
            public String getSqlSegment() {
                return "where project_id="+project + " and goods_id="+goods_id;
            }
        };
        final ProjectGoods projectGoods = projectGoodsService.selectOne(projectGoodsWrapper);
        if (projectGoods == null){
            ProjectGoods projectGoodsTmp=new ProjectGoods();
            projectGoodsTmp.setGoodsId(goods_id);
            projectGoodsTmp.setProjectId(project);
            projectGoodsService.insert(projectGoodsTmp);
        }

        //检查prj_group_goods是否存在  不存在则新增
        Wrapper<PrjGroupGoods> prjGroupGoodsWrapper=new Wrapper<PrjGroupGoods>() {
            @Override
            public String getSqlSegment() {
                return "where project_id="+project + " and goods_id="+goods_id + " and group_id="+groupId + " and online=1";
            }
        };
        final PrjGroupGoods groupGoods = prjGroupGoodsService.selectOne(prjGroupGoodsWrapper);
        if (groupGoods == null){
            //插入产品组信息
            PrjGroupGoods prjGroupGoods=new PrjGroupGoods();
            prjGroupGoods.setGoodsId(goods_id);
            prjGroupGoods.setGroupId(groupId);
            prjGroupGoods.setProjectId(project);
            prjGroupGoods.setOnline(1);
            prjGroupGoodsService.insert(prjGroupGoods);
        }
    }

    private void deletePrjGoos(Long project, Long goods_id,Long groupId) {
        //检查project_goods是否存在  不存在则新增
        Wrapper<ProjectGoods> projectGoodsWrapper=new Wrapper<ProjectGoods>() {
            @Override
            public String getSqlSegment() {
                return "where project_id="+project + " and goods_id="+goods_id;
            }
        };
        final ProjectGoods projectGoods = projectGoodsService.selectOne(projectGoodsWrapper);
        if (projectGoods != null){

            projectGoodsService.delete(projectGoodsWrapper);
        }

        //检查prj_group_goods是否存在  不存在则新增
        Wrapper<PrjGroupGoods> prjGroupGoodsWrapper=new Wrapper<PrjGroupGoods>() {
            @Override
            public String getSqlSegment() {
                return "where project_id="+project + " and goods_id="+goods_id + " and group_id="+groupId + " and online=1";
            }
        };
        final PrjGroupGoods groupGoods = prjGroupGoodsService.selectOne(prjGroupGoodsWrapper);
        if (groupGoods != null){

            prjGroupGoodsService.delete(prjGroupGoodsWrapper);
        }
    }


    public List<ShopItem> getShopitems(Shop shop,String serviceCode,String roomType,String addon){
        Wrapper<ShopItem> shopitemWrapper=new Wrapper<ShopItem>() {
            @Override
            public String getSqlSegment() {
                String str  =  "where shop_id=" +shop.getId()+" and type ='"+serviceCode+"'" ;
                    str = str  + " and name  = '"+roomType+"' and addon = '"+addon+"'  and needs is null" ;
//and needs is null
                return str ;
            }
        };
        List<ShopItem> items = shopItemMapper.selectList(shopitemWrapper);
        return items;
    }
    /**
     * 读取上传的Excel并入库
     * @return batchId
     */
    private List<ProductRowVo> readFileAndSave(MultipartFile multipartFile) throws Exception{
        //1.读取表格数据并入库
        final File file = FileUtils.multiTransferToFile(multipartFile, "/tmp");
        final ExcelReader excelReader = ExcelUtil.getReader(file);
        final Workbook workbook = excelReader.getWorkbook();
        List<ProductRowVo> rowVoList=Lists.newArrayList();
//        final Workbook workbook = ExcelUtils.getWorkbook(multipartFile, "/tmp");
        Integer totalRow=0;
        //获取sheet总数
        int numberOfSheets=workbook.getNumberOfSheets();
        for (int i=0;i<numberOfSheets;i++){
            //获取sheet
            final Sheet sheet = workbook.getSheetAt(i);
            //获取sheet名  作为产品组名
            ProductSheetVo productSheetVo=new ProductSheetVo();
            productSheetVo.setSheetIndex(i);
            productSheetVo.setSheetName(sheet.getSheetName());

            final int physicalNumberOfRows = sheet.getPhysicalNumberOfRows();
            if (physicalNumberOfRows<2){
                continue;
            }
            totalRow += (physicalNumberOfRows-1);

            List<String> titleList=Lists.newArrayList();
            int serviceIndex=-1;
            String serviceName="";
            for (int j=0;j<physicalNumberOfRows;j++){
                final Row row = sheet.getRow(j);
                if (StringUtils.isBlank(row.getCell(0).getStringCellValue())){
                    log.info("该行第一个单元格数据为空");
                    continue;
                }
                final int physicalNumberOfCells = row.getPhysicalNumberOfCells();
                Map<String,String> rowMap=Maps.newHashMap();
                for (int k=0;k<physicalNumberOfCells;k++){
                    //标题行
                    if (j==0){
                        if (row.getCell(k).getStringCellValue().equalsIgnoreCase(ProductImportSetMenuEnums.setMenu.SERVICE.getCellName())){
                            serviceIndex=k;
                        }
                        titleList.add(row.getCell(k).getStringCellValue());
                    }else {
                        //如果已获取serviceIndex 且k=serviceIndex 且serviceName不为空
                        if (serviceIndex>0 && k==serviceIndex && StringUtils.isBlank(serviceName)){
                            serviceName=row.getCell(k).getStringCellValue();
                        }
                        try {
                            rowMap.put(titleList.get(k),row.getCell(k)==null?null:row.getCell(k).getStringCellValue());
                        }catch (Exception e){
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


        }

        return rowVoList;
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
                } else {
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

    private ShopItem saveShopItemAccom(String serviceCode,  Shop shop, String itemName,  Map params) {
        ShopItem temp = new ShopItem();
        temp.setName(itemName);
        temp.setShopId(shop.getId());
        temp.setType(serviceCode);
        temp.setNeeds(params.get("needs") == null ?null : params.get("needs")+"");
        temp.setAddon(params.get("addon") == null ?null :params.get("addon")+"");
//        if(params.get("blocks") != null){
//            temp.setBlock(params.get("blocks") == null ?null :params.get("blocks")+"");
//        }
//        temp.setBlock();
        final Long nextValue = shopItemMapper.selectShopItemSeqNextValue();
        temp.setId(nextValue);
        shopItemMapper.insert(temp);
        return temp;


    }

}
