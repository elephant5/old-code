package com.colourfulchina.bigan.controller;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.colourfulchina.bigan.api.dto.GoodsInfo;
import com.colourfulchina.bigan.api.entity.Goods;
import com.colourfulchina.bigan.api.entity.Hotel;
import com.colourfulchina.bigan.api.entity.PrjGroup;
import com.colourfulchina.bigan.api.entity.PrjGroupGoods;
import com.colourfulchina.bigan.api.entity.ProductImportRow;
import com.colourfulchina.bigan.api.entity.ProductImportSheet;
import com.colourfulchina.bigan.api.entity.ProductImportWorkbook;
import com.colourfulchina.bigan.api.entity.ProjectGoods;
import com.colourfulchina.bigan.api.entity.Shop;
import com.colourfulchina.bigan.api.entity.ShopItem;
import com.colourfulchina.bigan.api.entity.WeekBlock;
import com.colourfulchina.bigan.api.vo.GoodsImportResultVo;
import com.colourfulchina.bigan.api.vo.GoodsImportVo;
import com.colourfulchina.bigan.api.vo.ProductRowVo;
import com.colourfulchina.bigan.api.vo.ProductSheetVo;
import com.colourfulchina.bigan.api.vo.ProductWorkbookVo;
import com.colourfulchina.bigan.api.vo.ShopSaveInfoVo;
import com.colourfulchina.bigan.enums.GoodsEnums;
import com.colourfulchina.bigan.enums.ProductImportEnums;
import com.colourfulchina.bigan.enums.ProductImportSetMenuEnums;
import com.colourfulchina.bigan.service.GoodsService;
import com.colourfulchina.bigan.service.HotelService;
import com.colourfulchina.bigan.service.PrjGroupGoodsService;
import com.colourfulchina.bigan.service.PrjGroupService;
import com.colourfulchina.bigan.service.ProductImportRowService;
import com.colourfulchina.bigan.service.ProductImportSheetService;
import com.colourfulchina.bigan.service.ProductImportWorkbookService;
import com.colourfulchina.bigan.service.ProjectGoodsService;
import com.colourfulchina.bigan.service.ShopAndItemInfoService;
import com.colourfulchina.bigan.service.ShopService;
import com.colourfulchina.bigan.service.ShopitemsService;
import com.colourfulchina.bigan.service.WeekBlockService;
import com.colourfulchina.bigan.utils.FileUtils;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
//@RequestMapping("/goods")
public class GoodsBakController {
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
    ShopAndItemInfoService  shopAndItemInfoService;
    @Autowired
    HotelService hotelService;



    @PostMapping(value = "/temp")
    public GoodsImportResultVo importGoods(Integer bankId,Integer projectId,MultipartFile multipartFile){
        GoodsImportResultVo resultVo=new GoodsImportResultVo();
        try {
            Map<String,String> resultMsg=Maps.newTreeMap();
            if ( projectId == null || multipartFile == null){
                log.info("输入参数不完整");
                resultVo.setCode(300);
                resultVo.setMsg("输入参数不完整");
                return resultVo;
            }

            GoodsImportVo goodsImportVo=new GoodsImportVo();
            goodsImportVo.setBankId(bankId);
            goodsImportVo.setProjectId(projectId);

            //由于bankId、projectId是由已存在信息传入  暂不校验项目是否存在

            //读取上传的Excel并入库
            String batchId=readFileAndSave(goodsImportVo,multipartFile);

            //把表中的数据转换成以下对象
            //自助餐处理流程
            //酒店、门店、商户、商品
            //然后分别判断是否存在，不存在则新增
            //返回具体处理结果
            Wrapper<ProductImportSheet> sheepWrapper=new Wrapper<ProductImportSheet>() {
                @Override
                public String getSqlSegment() {
                    return "where state = 0 and batch_id = '" + batchId+"'";
                }
            };
            final List<ProductImportSheet> productImportSheets = productImportSheetService.selectList(sheepWrapper);



            for (ProductImportSheet productImportSheet:productImportSheets){
                Wrapper<ProductImportRow> rowWrapper=new Wrapper<ProductImportRow>() {
                    @Override
                    public String getSqlSegment() {
                        return "where state = 0 and batch_id = '" + batchId +"' and sheet_index = " + productImportSheet.getSheetIndex()+" order by create_time";
                    }
                };

                //作为产品组名称prj_group.title
                final String sheetName = productImportSheet.getSheetName();
                //获取产品组信息  不存在则新增prj_group
                Wrapper<PrjGroup> groupWrapper=new Wrapper<PrjGroup>() {
                    @Override
                    public String getSqlSegment() {
                        return "where project_id = "+goodsImportVo.getProjectId() + " and title = '" + sheetName + "' order by create_time";
                    }
                };
                final PrjGroup prjGroup = prjGroupService.selectOne(groupWrapper);
                if (prjGroup == null){
                    //新增产品组 需要表格中提供相关数据
                    log.info("未找到对应的产品组project_id:{},title:{}",goodsImportVo.getProjectId(),sheetName);
                    resultVo.setCode(300);
                    resultVo.setMsg("未找到对应的产品组！项目ID:"+goodsImportVo.getProjectId()+",产品组名称:"+sheetName);
                    continue;
//                    return resultVo;
                }


                final List<ProductImportRow> productImportRows = productImportRowService.selectList(rowWrapper);
                List<GoodsInfo> goodsInfoList = new LinkedList<GoodsInfo>();
                GoodsInfo goodsInfo=null;



                for (ProductImportRow productImportRow : productImportRows){
                    try {
                        //现阶段认为导入的产品方案为已录入产品，不做新增处理
                        //project_goods、prj_group_goods、prj_package_groups

                        //把行数据转换成Map
                        final String value = productImportRow.getValue();
                        final Map map = JSON.toJavaObject(JSON.parseObject(value), Map.class);
                        //查询商品信息goods.title等
                        final String serviceCode=productImportSheet.getServiceCode();
                        final String giftCode = GoodsEnums.gift.findByNameOrShort(map.get(ProductImportSetMenuEnums.setMenu.DISCOUNT.getCellName()) + "") == null?"":GoodsEnums.gift.findByNameOrShort(map.get(ProductImportSetMenuEnums.setMenu.DISCOUNT.getCellName()) + "").getCode();
                        //解析相同的商户
                        if(goodsInfo == null){
                            goodsInfo=new GoodsInfo();
                            goodsInfo.setHotelCh(map.get(ProductImportSetMenuEnums.setMenu.HOTEL_NAME_CN.getCellName())+"");
                            goodsInfo.setShopCh(map.get(ProductImportSetMenuEnums.setMenu.SHOP_NAME.getCellName())+"");
                            goodsInfo.setGift(map.get(ProductImportSetMenuEnums.setMenu.DISCOUNT.getCellName())+"");
                            goodsInfo.getRows().put(goodsInfo.getHotelCh()+"-"+goodsInfo.getShopCh()+"-"+goodsInfo.getGift(),map);
                            goodsInfo.getMeatName().add(map.get(ProductImportSetMenuEnums.setMenu.PACKAGENAME.getCellName())+"");

                            goodsInfoList.add(goodsInfo);
                        }else if (map.get(ProductImportSetMenuEnums.setMenu.HOTEL_NAME_CN.getCellName()+"").equals(goodsInfo.getHotelCh())
                                && (map.get(ProductImportSetMenuEnums.setMenu.SHOP_NAME.getCellName())+"").equals(goodsInfo.getShopCh())
                                && (map.get(ProductImportSetMenuEnums.setMenu.DISCOUNT.getCellName())+"").equals(goodsInfo.getGift())){
                            String key1 = map.get(ProductImportSetMenuEnums.setMenu.HOTEL_NAME_CN.getCellName())+"";
                            String key2 = map.get(ProductImportSetMenuEnums.setMenu.SHOP_NAME.getCellName())+"";
                            String key3 = map.get(ProductImportSetMenuEnums.setMenu.DISCOUNT.getCellName())+"";
                            goodsInfoList.forEach(vo ->{
                                    if(vo.toString().equals(key1+"-"+key2+"-"+key3)){
                                    vo.getMeatName().add(map.get(ProductImportSetMenuEnums.setMenu.PACKAGENAME.getCellName())+"")   ;
                                }

                            });

                        }else {
                            goodsInfoList.add(goodsInfo);
                            goodsInfo = null;

                        //解析相同的商户

                            //先把之前合并的数据进行处理
                            Wrapper<Shop> shopWrapper=new Wrapper<Shop>() {
                                @Override
                                public String getSqlSegment() {
                                    return "where type='" + serviceCode + "' and city='"+map.get(ProductImportSetMenuEnums.setMenu.CITY.getCellName()) + "' and hotel='" + map.get(ProductImportSetMenuEnums.setMenu.HOTEL_NAME_CN.getCellName())+"' and name='"+map.get(ProductImportSetMenuEnums.setMenu.SHOP_NAME.getCellName())+"'";
                                }
                            };
                            Shop shop = shopService.selectOne(shopWrapper);
                            //正常情况是会查到一条数据的  如果未查到则新增  现阶段认为已存在
                            if (shop == null){
                                log.info("未查到商户信息 row_id={}",productImportRow.getId());
                                ShopSaveInfoVo vo = new ShopSaveInfoVo();
                                Hotel hotelParam = new Hotel();
                                hotelParam.setName(map.get(ProductImportSetMenuEnums.setMenu.HOTEL_NAME_CN.getCellName())+"");
                                hotelParam.setCity(map.get(ProductImportSetMenuEnums.setMenu.CITY.getCellName())+"");

                                Wrapper<Hotel> hotelParamWapper = new Wrapper<Hotel>() {
                                    @Override
                                    public String getSqlSegment() {
                                        return "where city='"+map.get(ProductImportSetMenuEnums.setMenu.CITY.getCellName()) + "' and name='" + map.get(ProductImportSetMenuEnums.setMenu.HOTEL_NAME_CN.getCellName())+"' ";
                                    }
                                };
                                Hotel hotel = hotelService.selectOne(hotelParamWapper);
                                if(null !=hotel){
                                    vo.setHotel_id(hotel.getId());
                                    vo.setHotel(hotel.getName());
                                    vo.setCity_id(hotel.getCityId().longValue());
                                    vo.setType(GoodsEnums.service.BUFFET.getCode());
                                    vo.setName(map.get(ProductImportSetMenuEnums.setMenu.SHOP_NAME.getCellName())+"");
                                    vo.setAccount("hotel_"+hotel.getId()+((int)Math.random()*100));
                                    vo.setPwd("123456");
                                    log.info("HotelUserAcount===="+vo.getAccount());
                                    shop = shopService.saveShopInfo(vo);

                                    if(null == shop){
                                        resultVo.setCode(300);
                                        resultVo.setMsg("未成功添加商户 row_id="+productImportRow.getId());
                                        log.error("未成功添加商户 row_id="+productImportRow.getId());
                                        resultMsg.put(map.get(ProductImportSetMenuEnums.setMenu.HOTEL_NAME_CN.getCellName()).toString()+"-"+map.get(ProductImportSetMenuEnums.setMenu.SHOP_NAME.getCellName()).toString(),"未成功添加商户");
                                        continue;
                                    }
                                }

//                                return resultVo;
                            }

                            if (GoodsEnums.service.BUFFET.getCode().equalsIgnoreCase(serviceCode)){
                                final Shop tempshop = shop;
                                //根据shop_id、serviceCode、giftCode查询所有goods
                                Wrapper<Goods> goodsWrapper=new Wrapper<Goods>() {
                                    @Override
                                    public String getSqlSegment() {
                                        return "where type='"+serviceCode+"' and gift='"+giftCode + "' and shop_id="+tempshop.getId();
                                    }
                                };
                                final List<Goods> goodsList = goodsService.selectList(goodsWrapper);
                                Map<Object,Object> tmpMap=Maps.newHashMap();
                                //未查到goodsList，则进行新增

                                String canRange=map.get(ProductImportSetMenuEnums.setMenu.CAN_RANGE.getCellName())+"";
                                //解析餐段  餐段名 block
                                if (StringUtils.isBlank(canRange)){
                                    log.info("餐段信息为空row_id:{}",productImportRow.getId());
                                    resultMsg.put(map.get(ProductImportSetMenuEnums.setMenu.HOTEL_NAME_CN.getCellName()).toString()+"-"+map.get(ProductImportSetMenuEnums.setMenu.SHOP_NAME.getCellName()).toString(),"餐段信息为空");
                                    continue;
                                }
                                final String replacedCanRange = canRange.replaceAll("\\s", "");
                                //查询出所有到餐段
                                //调用goods存储过程进行新增  并获取goods_id
                                List<ShopItem> items = shopitemsService.getShopitemsByShopId(shop,serviceCode);

                                StringBuffer goodsStr = new StringBuffer("<xml>");

                                List<ShopItem> result= null;//shopAndItemInfoService.analysisItem(serviceCode,map,items,shop,blockMap);
                                result.forEach(item -> {
                                    goodsStr.append("<item id=\"" +item.getId());
                                    goodsStr.append("\"");
                                    goodsStr.append("name =\""+item.getName()+"\"");

                                    goodsStr.append("block =\""+item.getBlock()+"\"");
                                    goodsStr.append("</item>");

                                });
                                goodsStr.append("</xml>");

                                for (Goods goods:goodsList){
                                    tmpMap.put(goods,XMLUtils.xmlToMap(goods.getItems()));

                                }

                                //根据type gift shop_id title查询goods
                                Wrapper<Goods> goodsWrappers=new Wrapper<Goods>() {
                                    @Override
                                    public String getSqlSegment() {
                                        return "where type='"+serviceCode+"' and gift='"+giftCode + "' and shop_id="+tempshop.getId()+" and convert(varchar(500),items)='"+goodsStr.toString()+"'";
                                    }
                                };
                                final Goods goods = goodsService.selectOne(goodsWrappers);

                                Long goods_id=0L;
                                if (goods == null){


                                    Map goodsMap = goodsService.saveGoodsInfoVo(shop,result,map,null,goodsImportVo.getProjectId(),serviceCode,null);
                                    log.info(goodsMap.toString());
                                    if(goodsMap.containsKey("goods_id")&& goodsMap.get("goods_id") != null){

                                        goods_id = Long.parseLong(goodsMap.get("goods_id")+"");
                                    }else {
                                        //goodsMap中包含error和shopId，error：表示失败，goodsId不为空则为true；
                                        final Object resultStr = goodsMap.get("result");
                                        try {
                                            if (resultStr != null){
                                                String str1=resultStr.toString();
                                                String goodsIdStr=str1.substring(str1.indexOf("#")+1,str1.indexOf(")"));
                                                goods_id=Long.parseLong(goodsIdStr);
                                                insertPrjGoos(prjGroup, goods_id);
                                            }else {
                                                log.error("goods==null处理异常");
                                            }
                                        }catch (Exception e){
                                            log.error(e.getMessage(),e);
                                            log.info("goodsMap:{}",goodsMap);
                                        }

                                    }
                                }else {
                                    goods_id=goods.getId();
                                    insertPrjGoos(prjGroup,goods_id);
                                }
                            }

                            //清空goodsInfo
                            //新的goodsInfo
                        }
                        //把row记录更新为成功
                        productImportRow.setState(2);
                        productImportRow.setUpdateTime(new Date());
                        productImportRowService.updateById(productImportRow);
                    }catch (Exception e){
                        log.error(e.getMessage(),e);
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
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            resultVo.setCode(200);
            resultVo.setMsg(e.getMessage());
            return resultVo;
        }
        return resultVo;
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
        }
    }

    /**
     * 读取上传的Excel并入库
     * @param goodsImportVo
     * @return batchId
     */
    private String readFileAndSave(GoodsImportVo goodsImportVo,MultipartFile multipartFile) throws Exception{
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
                productImportRow.setValue(JSON.toJSONString(productImportRow.getRowMap()));
                productImportRow.setState(0);//未处理
                productImportRow.setCreateTime(new Date());
                productImportRow.setUpdateTime(new Date());
                productImportRowService.insert(productImportRow);
            }
        }
        return productWorkbookVo.getBatchId();
    }
}
