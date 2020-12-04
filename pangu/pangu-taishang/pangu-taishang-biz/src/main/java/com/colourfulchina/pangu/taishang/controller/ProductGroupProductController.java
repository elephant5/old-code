package com.colourfulchina.pangu.taishang.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.colourfulchina.god.door.api.annotation.SysGodDoorLog;
import com.colourfulchina.god.door.api.util.SecurityUtils;
import com.colourfulchina.god.door.api.vo.KltSysUser;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.inf.base.vo.PageVo;
import com.colourfulchina.pangu.taishang.api.dto.SysFileDto;
import com.colourfulchina.pangu.taishang.api.entity.*;
import com.colourfulchina.pangu.taishang.api.enums.FileTypeEnums;
import com.colourfulchina.pangu.taishang.api.vo.BookProductVo;
import com.colourfulchina.pangu.taishang.api.vo.GroupProductVo;
import com.colourfulchina.pangu.taishang.api.vo.ShopBaseMsgVo;
import com.colourfulchina.pangu.taishang.api.vo.ShopVo;
import com.colourfulchina.pangu.taishang.api.vo.req.*;
import com.colourfulchina.pangu.taishang.api.vo.res.GroupProductDetailRes;
import com.colourfulchina.pangu.taishang.api.vo.res.SelectBookProductRes;
import com.colourfulchina.pangu.taishang.api.vo.res.SelectBookShopItemRes;
import com.colourfulchina.pangu.taishang.api.vo.res.ShopListQueryRes;
import com.colourfulchina.pangu.taishang.config.FtpProperties;
import com.colourfulchina.pangu.taishang.service.*;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/productGroupProduct")
@Slf4j
@Api(tags = {"产品组和产品关系操作接口"})
public class ProductGroupProductController {
    @Autowired
    private ProductGroupProductService productGroupProductService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ShopItemService shopItemService;
    @Autowired
    private SysFileService sysFileService;
    @Autowired
    private ShopItemNetPriceRuleService shopItemNetPriceRuleService;
    @Autowired
    private ProductGroupService productGroupService;
    @Autowired
    private GoodsSettingService goodsSettingService;
    @Autowired
    private GoodsClauseService goodsClauseService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private HotelService hotelService;
    @Autowired
    private ShopProtocolService shopProtocolService;
    @Autowired
    private BlockRuleService blockRuleService;
    @Autowired
    FtpProperties ftpProperties;

    /**
     * 产品组的所属产品条件查询
     *
     * @param queryGroupProductReq
     * @return
     */
    @SysGodDoorLog("产品组的所属产品条件查询")
    @ApiOperation("产品组的所属产品条件查询")
    @PostMapping("/queryGroupProduct")
    public CommonResultVo<List<GroupProductVo>> queryGroupProduct(@RequestBody QueryGroupProductReq queryGroupProductReq) {
        CommonResultVo<List<GroupProductVo>> result = new CommonResultVo<>();
        try {
            List<GroupProductVo> list = productGroupProductService.queryGroupProduct(queryGroupProductReq);
            result.setResult(list);
        } catch (Exception e) {
            log.error("产品组的所属产品条件查询失败", e);
            result.setCode(200);
            result.setMsg("产品组的所属产品条件查询失败");
        }
        return result;
    }

    @SysGodDoorLog("导出产品组产品为Excel表")
    @ApiOperation("导出产品组产品为Excel表")
    @PostMapping("/export")
    public CommonResultVo<String> export(@RequestBody QueryGroupProductReq queryGroupProductReq){
        CommonResultVo<String> result = new CommonResultVo<>();
        try {
            Assert.notNull(queryGroupProductReq.getGroupId(),"产品组id不能为空");
            KltSysUser sysUser = SecurityUtils.getAttributeUser();
            Assert.isTrue(sysUser != null && sysUser.getEmail() != null , "收件人不能为空");
            String url = productGroupProductService.exportGroupProduct(queryGroupProductReq,sysUser);
            result.setResult(url);
        } catch (Exception e) {
            log.error("产品组产品导出失败",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    @SysGodDoorLog("查询产品组产品的价格")
    @ApiOperation("查询产品组产品的价格")
    @PostMapping("/selectProductPrices")
    @Cacheable(value = "ProductGroupProduct", key = "'selectProductPrices_'+#productGroupProductId", unless = "#result == null")
    public CommonResultVo<List<ShopItemNetPriceRule>> selectProductPrices(@RequestBody Integer productGroupProductId) {
        CommonResultVo<List<ShopItemNetPriceRule>> result = new CommonResultVo<>();
        try {
            Assert.notNull(productGroupProductId, "产品组产品id不能为空");
            //获取产品组下的产品信息
            ProductGroupProduct productGroupProduct = productGroupProductService.selectById(productGroupProductId);
            //获取产品信息
            Product product = productService.selectById(productGroupProduct.getProductId());
            //查询规格价格
            Wrapper priceWrapper = new Wrapper() {
                @Override
                public String getSqlSegment() {
                    return "where del_flag = 0 and shop_item_id =" + product.getShopItemId();
                }
            };
            List<ShopItemNetPriceRule> priceRules = shopItemNetPriceRuleService.selectList(priceWrapper);
            result.setResult(priceRules);
        } catch (Exception e) {
            log.error("查询产品组的产品价格失败", e);
            result.setCode(200);
            result.setMsg("查询价格失败");
        }
        return result;
    }

    /**
     * 查询产品组下面产品的详情
     *
     * @param productGroupProductId
     * @return
     */
    @SysGodDoorLog("查询产品组下面产品的详情")
    @ApiOperation("查询产品组下面产品的详情")
    @PostMapping("/selectProductDetail")
    @Cacheable(value = "ProductGroupProduct", key = "'selectProductDetail_'+#productGroupProductId", unless = "#result == null")
    public CommonResultVo<GroupProductDetailRes> selectProductDetail(@RequestBody Integer productGroupProductId) {
        CommonResultVo<GroupProductDetailRes> result = new CommonResultVo();
        GroupProductDetailRes groupProductDetailRes = new GroupProductDetailRes();
        try {
            Assert.notNull(productGroupProductId, "产品关系id不能为空");
            //获取产品组下的产品信息
            ProductGroupProduct productGroupProduct = productGroupProductService.selectById(productGroupProductId);
            //获取产品信息
            Product product = productService.selectById(productGroupProduct.getProductId());
            //查询商户规格信息
            ShopItem shopItem = shopItemService.selectById(product.getShopItemId());
            //查询商户信息
            ShopBaseMsgVo shop = shopService.selectShopBaseMsg(shopItem.getShopId());
            //查询商户协议信息
            ShopProtocol shopProtocol = shopProtocolService.selectById(shopItem.getShopId());
            //查询酒店信息
            Hotel hotel = null;
            if (shop.getHotelId() != null) {
                hotel = hotelService.selectById(shop.getHotelId());
            }
            //查询商户图片
            ListSysFileReq shopFile = new ListSysFileReq();
            shopFile.setObjId(shop.getId());
            shopFile.setType(FileTypeEnums.SHOP_PIC.getCode());
            List<SysFileDto> shopFileList = sysFileService.listSysFileDto(shopFile);
            //查询规格图片
            ListSysFileReq sysFileReq = new ListSysFileReq();
            sysFileReq.setObjId(shopItem.getId());
            sysFileReq.setType(FileTypeEnums.SHOP_ITEM_PIC.getCode());
            List<SysFileDto> fileDtoList = sysFileService.listSysFileDto(sysFileReq);
            //查询规格价格
            Wrapper priceWrapper = new Wrapper() {
                @Override
                public String getSqlSegment() {
                    return "where del_flag = 0 and shop_item_id =" + shopItem.getId();
                }
            };
            List<ShopItemNetPriceRule> priceRules = shopItemNetPriceRuleService.selectList(priceWrapper);
            //查询产品组信息
            ProductGroup productGroup = productGroupService.selectById(productGroupProduct.getProductGroupId());
            //查询商品使用限制信息
            GoodsSetting goodsSetting = goodsSettingService.selectById(productGroup.getGoodsId());
            //查询商品使用规则信息
            List<GoodsClause> goodsClauseList = goodsClauseService.selectByGoodsId(productGroup.getGoodsId());

            groupProductDetailRes.setShopItem(shopItem);
            groupProductDetailRes.setShopItemPics(fileDtoList);
            groupProductDetailRes.setPriceRuleList(priceRules);
            groupProductDetailRes.setProductGroup(productGroup);
            groupProductDetailRes.setGoodsSetting(goodsSetting);
            groupProductDetailRes.setGoodsClauseList(goodsClauseList);
            groupProductDetailRes.setProductGroupProduct(productGroupProduct);
            groupProductDetailRes.setShop(shop);
            groupProductDetailRes.setHotel(hotel);
            groupProductDetailRes.setShopProtocol(shopProtocol);
            groupProductDetailRes.setShopPics(shopFileList);
            result.setResult(groupProductDetailRes);
        } catch (Exception e) {
            log.error("查询产品组下面产品详情失败", e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }


    /**
     * 查询产品列表
     *
     * @param selectBookProductReq
     * @return
     */
    @SysGodDoorLog("查询产品列表")
    @ApiOperation("查询产品列表")
    @PostMapping("/selectBookProduct")
    public CommonResultVo<List<SelectBookProductRes>> selectBookProduct(@RequestBody SelectBookProductReq selectBookProductReq) {
        CommonResultVo<List<SelectBookProductRes>> result = new CommonResultVo();
        List<SelectBookProductRes> list = Lists.newLinkedList();
        try {
            Assert.notNull(selectBookProductReq, "参数不能为空");
            Assert.notNull(selectBookProductReq.getProductGroupId(), "产品组id不能为空");
            Assert.notNull(selectBookProductReq.getServiceType(), "资源类型不能为空");
            SelectBookShopItemReq selectBookShopItemReq = new SelectBookShopItemReq();
            selectBookShopItemReq.setProductGroupId(selectBookProductReq.getProductGroupId());
            selectBookShopItemReq.setService(selectBookProductReq.getServiceType());
            List<SelectBookShopItemRes> bookShopItemRes = productGroupService.selectBookShopItem(selectBookShopItemReq);
            for (SelectBookShopItemRes bookItem : bookShopItemRes) {
                SelectBookProductRes selectBookProductRes = new SelectBookProductRes();
                //获取产品组下的产品信息
                ProductGroupProduct productGroupProduct = productGroupProductService.selectById(bookItem.getId());
                //获取产品信息
                Product product = productService.selectById(productGroupProduct.getProductId());
                //查询商户规格信息
                ShopItem shopItem = shopItemService.selectById(product.getShopItemId());
                //查询商户信息
                ShopVo shopVo = shopService.selectShopVoById(shopItem.getShopId());
                //查询酒店信息
                Hotel hotel = null;
                if (shopVo.getHotelId() != null) {
                    hotel = hotelService.selectById(shopVo.getHotelId());
                }
                //查询商户图片
                ListSysFileReq shopFile = new ListSysFileReq();
                shopFile.setObjId(shopVo.getId());
                shopFile.setType(FileTypeEnums.SHOP_PIC.getCode());
                List<SysFileDto> shopFileList = sysFileService.listSysFileDto(shopFile);
                //查询规格图片
                ListSysFileReq sysFileReq = new ListSysFileReq();
                sysFileReq.setObjId(shopItem.getId());
                sysFileReq.setType(FileTypeEnums.SHOP_ITEM_PIC.getCode());
                List<SysFileDto> fileDtoList = sysFileService.listSysFileDto(sysFileReq);
                //查询规格价格
                Wrapper priceWrapper = new Wrapper() {
                    @Override
                    public String getSqlSegment() {
                        return "where del_flag = 0 and shop_item_id =" + shopItem.getId();
                    }
                };
                List<ShopItemNetPriceRule> priceRules = shopItemNetPriceRuleService.selectList(priceWrapper);
                //查询产品组信息
                ProductGroup productGroup = productGroupService.selectById(productGroupProduct.getProductGroupId());
                selectBookProductRes.setShopItem(shopItem);
                selectBookProductRes.setShopItemPics(fileDtoList);
                selectBookProductRes.setPriceRuleList(priceRules);
                selectBookProductRes.setProductGroup(productGroup);
                selectBookProductRes.setProductGroupProduct(productGroupProduct);
                selectBookProductRes.setShop(shopVo);
                selectBookProductRes.setHotel(hotel);
                selectBookProductRes.setShopPics(shopFileList);
                list.add(selectBookProductRes);
            }
            result.setResult(list);
        } catch (Exception e) {
            log.error("查询产品列表失败", e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    /**
     * 查询产品列表(优化)
     *
     * @param selectBookProductReq
     * @return
     */
    @SysGodDoorLog("查询产品列表(优化)")
    @ApiOperation("查询产品列表(优化)")
    @PostMapping("/selectBookProductNew")
    public CommonResultVo<List<SelectBookProductRes>> selectBookProductNew(@RequestBody SelectBookProductReq selectBookProductReq) {
        CommonResultVo<List<SelectBookProductRes>> result = new CommonResultVo();
        List<SelectBookProductRes> list = Lists.newLinkedList();
        try {
            Assert.notNull(selectBookProductReq, "参数不能为空");
            Assert.notNull(selectBookProductReq.getProductGroupId(), "产品组id不能为空");
            Assert.notNull(selectBookProductReq.getServiceType(), "资源类型不能为空");
            SelectBookShopItemReq selectBookShopItemReq = new SelectBookShopItemReq();
            selectBookShopItemReq.setProductGroupId(selectBookProductReq.getProductGroupId());
            selectBookShopItemReq.setService(selectBookProductReq.getServiceType());
            List<BookProductVo> bookList = productGroupProductService.selectBookProduct(selectBookShopItemReq);
            for (BookProductVo vo : bookList) {
                QueryBookBlockReq queryBookBlockReq = new QueryBookBlockReq();
                queryBookBlockReq.setProductGroupProductId(vo.getProductGroupProductId());
                queryBookBlockReq.setActExpireTime(selectBookProductReq.getActExpireTime());
                List<Date> bookDates = blockRuleService.queryBookBlockNew(queryBookBlockReq);

                SelectBookProductRes selectBookProductRes = new SelectBookProductRes();
                selectBookProductRes.setOrderFlag(false);
                if(!CollectionUtils.isEmpty(bookDates)) {
                    // 起始日期筛选
                    if(selectBookProductReq.getBeginDate()!=null) {
                        if(bookDates!=null && bookDates.contains(DateUtil.parse(selectBookProductReq.getBeginDate(),"yyyy-MM-dd"))) {
                            selectBookProductRes.setOrderFlag(true);
                        }
                    }
                    // 截止日期筛选
                    if(selectBookProductReq.getEndDate()!=null) {
                        List<Date> dateList = this.findDates(DateUtil.parse(selectBookProductReq.getBeginDate(),"yyyy-MM-dd"), DateUtil.parse(selectBookProductReq.getEndDate(),"yyyy-MM-dd"));
                        if(bookDates!=null && bookDates.containsAll(dateList)) {
                            selectBookProductRes.setOrderFlag(true);
                        }
                    }
                }

                ShopItem shopItem = new ShopItem();
                shopItem.setShopId(vo.getShopId());
                shopItem.setId(vo.getShopItemId());
                shopItem.setName(vo.getShopItemName());
                shopItem.setNeeds(vo.getNeeds());
                shopItem.setAddon(vo.getAddon());
                shopItem.setServiceType(vo.getServiceType());
                shopItem.setServiceName(vo.getServiceName());

                ProductGroupProduct productGroupProduct = new ProductGroupProduct();
                productGroupProduct.setId(vo.getProductGroupProductId());
                productGroupProduct.setGift(vo.getGift());

                ProductGroup productGroup = new ProductGroup();
                productGroup.setId(vo.getProductGroupId());
                productGroup.setGoodsId(vo.getGoodsId());

                ShopVo shop = new ShopVo();
                shop.setId(vo.getShopId());
                shop.setName(vo.getShopName());
                shop.setAddress(vo.getAddress());

                Hotel hotel = new Hotel();
                hotel.setNameCh(vo.getHotelName());
                hotel.setAddressCh(vo.getAddress());

                List<SysFileDto> pics = Lists.newLinkedList();
                SysFileDto sysFileDto = new SysFileDto();
                sysFileDto.setGuid(vo.getShopPic());
                sysFileDto.setPgCdnUrl(ftpProperties.getPgCdnUrl());
                sysFileDto.setErpCdnUrl(ftpProperties.getErpCdnUrl());
                pics.add(sysFileDto);

                selectBookProductRes.setShopItem(shopItem);
                selectBookProductRes.setProductGroupProduct(productGroupProduct);
                selectBookProductRes.setProductGroup(productGroup);
                selectBookProductRes.setShop(shop);
                selectBookProductRes.setHotel(hotel);
                selectBookProductRes.setShopPics(pics);
                selectBookProductRes.setCityName(vo.getCityName());
                list.add(selectBookProductRes);
            }
            result.setResult(list);
        } catch (Exception e) {
            log.error("查询产品列表失败", e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    /**
     * @throws
     * @Title: findDates
     * @Description: JAVA获取某段时间内的所有日期
     * @author: sunny.wang
     * @date: 2019年6月18日 下午1:39:43
     * @param: @param dStart
     * @param: @param dEnd
     * @param: @return
     * @return: List<Date>
     */
    private List<Date> findDates(Date dStart, Date dEnd) {
        Calendar cStart = Calendar.getInstance();
        cStart.setTime(dStart);
        List<Date> dateList = new ArrayList<Date>();
        // 别忘了，把起始日期加上
        dateList.add(dStart);
        // 此日期是否在指定日期之后
        while (dEnd.after(cStart.getTime())) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            cStart.add(Calendar.DAY_OF_MONTH, 1);
            dateList.add(cStart.getTime());
        }
        return dateList;
    }

    /**
     * 分页查询产品列表
     *
     * @param selectBookProductReq
     * @return
     */
    @SysGodDoorLog("分页查询产品列表")
    @ApiOperation("分页查询产品列表")
    @PostMapping("/selectBookProductPaging")
    public CommonResultVo<PageVo<SelectBookProductRes>> selectBookProductPaging(@RequestBody PageVo<SelectBookShopItemReq> pageVo) {
        CommonResultVo<PageVo<SelectBookProductRes>> result = new CommonResultVo();
        List<SelectBookProductRes> list = Lists.newLinkedList();
        PageVo<SelectBookProductRes> returnPage = new PageVo<>();
        try {
            Map<String, Object> map = pageVo.getCondition();
            Assert.notNull(map, "参数不能为空");
            Assert.notNull(map.get("productGroupId"), "产品组id不能为空");
            Assert.notNull(map.get("service"), "资源类型不能为空");
            PageVo<SelectBookShopItemRes> resPageVo = productGroupService.selectBookShopItemPaging(pageVo);
            List<SelectBookShopItemRes> bookShopItemRes = null;
            Assert.isTrue(resPageVo != null && (bookShopItemRes = resPageVo.getRecords()) != null, "没有查询到产品");
            for (SelectBookShopItemRes bookItem : bookShopItemRes) {
                SelectBookProductRes selectBookProductRes = new SelectBookProductRes();
                //获取产品组下的产品信息
                ProductGroupProduct productGroupProduct = productGroupProductService.selectById(bookItem.getId());
                //获取产品信息
                Product product = productService.selectById(productGroupProduct.getProductId());
                //查询商户规格信息
                ShopItem shopItem = shopItemService.selectById(product.getShopItemId());
                //查询商户信息
                ShopVo shopVo = shopService.selectShopVoById(shopItem.getShopId());
                //查询酒店信息
                Hotel hotel = null;
                if (shopVo.getHotelId() != null) {
                    hotel = hotelService.selectById(shopVo.getHotelId());
                }
                //查询商户图片
                ListSysFileReq shopFile = new ListSysFileReq();
                shopFile.setObjId(shopVo.getId());
                shopFile.setType(FileTypeEnums.SHOP_PIC.getCode());
                List<SysFileDto> shopFileList = sysFileService.listSysFileDto(shopFile);
                //查询规格图片
                ListSysFileReq sysFileReq = new ListSysFileReq();
                sysFileReq.setObjId(shopItem.getId());
                sysFileReq.setType(FileTypeEnums.SHOP_ITEM_PIC.getCode());
                List<SysFileDto> fileDtoList = sysFileService.listSysFileDto(sysFileReq);
                //查询规格价格
                Wrapper priceWrapper = new Wrapper() {
                    @Override
                    public String getSqlSegment() {
                        return "where del_flag = 0 and shop_item_id =" + shopItem.getId();
                    }
                };
                List<ShopItemNetPriceRule> priceRules = shopItemNetPriceRuleService.selectList(priceWrapper);
                //查询产品组信息
                ProductGroup productGroup = productGroupService.selectById(productGroupProduct.getProductGroupId());
                selectBookProductRes.setShopItem(shopItem);
                selectBookProductRes.setShopItemPics(fileDtoList);
                selectBookProductRes.setPriceRuleList(priceRules);
                selectBookProductRes.setProductGroup(productGroup);
                selectBookProductRes.setProductGroupProduct(productGroupProduct);
                selectBookProductRes.setShop(shopVo);
                selectBookProductRes.setHotel(hotel);
                selectBookProductRes.setShopPics(shopFileList);
                list.add(selectBookProductRes);
            }
            BeanUtils.copyProperties(resPageVo,returnPage);
            returnPage.setRecords(list);
            result.setResult(returnPage);
        } catch (Exception e) {
            log.error("查询产品列表失败", e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    /**
     * 产品组对应的产品停售
     *
     * @param groupProductVo
     * @return
     */
    @SysGodDoorLog("产品组对应的产品停售")
    @ApiOperation("产品组对应的产品停售")
    @PostMapping("/updateStatus")
    @CacheEvict(value = {"ProductGroupProduct", "ProductGroup", "Goods"}, allEntries = true)
    public CommonResultVo<ProductGroupProduct> updateStatus(@RequestBody GroupProductVo groupProductVo) {
        CommonResultVo<ProductGroupProduct> result = new CommonResultVo<>();
        try {
            ProductGroupProduct productGroupProduct = productGroupProductService.selectById(groupProductVo.getId());
            productGroupProduct.setStatus(1);
            productGroupProductService.updateById(productGroupProduct);
            result.setResult(productGroupProduct);
        } catch (Exception e) {
            log.error("产品组对应的产品停售失败", e);
            result.setCode(200);
            result.setMsg("产品组对应的产品停售失败");
        }
        return result;
    }

    /**
     * 产品组对应的产品推荐或者取消
     *
     * @param groupProductVo
     * @return
     */
    @ApiOperation("产品组对应的产品推荐或者取消")
    @PostMapping("/updateRecommend")
    @CacheEvict(value = {"ProductGroupProduct", "ProductGroup", "Goods"}, allEntries = true)
    public CommonResultVo<ProductGroupProduct> updateRecommend(@RequestBody GroupProductVo groupProductVo) {
        CommonResultVo<ProductGroupProduct> result = new CommonResultVo<>();
        try {
            ProductGroupProduct productGroupProduct = productGroupProductService.selectById(groupProductVo.getId());
            productGroupProduct.setRecommend(groupProductVo.getRecommend());
            productGroupProductService.updateById(productGroupProduct);
            result.setResult(productGroupProduct);
        } catch (Exception e) {
            log.error("产品组对应的产品推荐或者取消失败", e);
            result.setCode(200);
            result.setMsg("产品组对应的产品推荐或者取消失败");
        }
        return result;
    }

    @SysGodDoorLog("查询产品组下面的产品信息（商区）")
    @ApiOperation("查询产品组下面的产品信息（商区）")
    @PostMapping("/selectGoodsListByGroupId")
    public CommonResultVo<PageVo<ShopListQueryRes>> selectGoodsListByGroupId(@RequestBody PageVo<ShopListPageQueryReq> pageVo) {
        CommonResultVo<PageVo<ShopListQueryRes>> result = new CommonResultVo();
        try {
            PageVo<ShopListQueryRes> shopListQueryResList = productGroupProductService.selectGoodsListByGroupId(pageVo);
            //图片地址封装
            List<ShopListQueryRes> shopListQueryRes = shopListQueryResList.getRecords();
            for (ShopListQueryRes shopListQueryRe : shopListQueryRes) {
                ListSysFileReq sysFileReq = new ListSysFileReq();
                sysFileReq.setType(FileTypeEnums.SHOP_PIC.getCode());
                sysFileReq.setObjId(shopListQueryRe.getShopId());
                List<SysFileDto> fileDtoList = sysFileService.listSysFileDto(sysFileReq);
                if (CollectionUtil.isNotEmpty(fileDtoList)) {
                    shopListQueryRe.setUrl(fileDtoList.get(0).getPgCdnNoHttpFullUrl());
                }
            }
            result.setResult(shopListQueryResList);
        } catch (Exception e) {
            log.error("查询商品信息", e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    @SysGodDoorLog("根据id查询产品关系")
    @ApiOperation("根据id查询产品关系")
    @PostMapping("/findById")
    @Cacheable(value = "ProductGroupProduct", key = "'findById_'+#id", unless = "#result == null")
    public CommonResultVo<ProductGroupProduct> findById(@RequestBody Integer id) {
        CommonResultVo<ProductGroupProduct> result = new CommonResultVo<>();
        try {
            Assert.notNull(id, "id不能为空");
            ProductGroupProduct productGroupProduct = productGroupProductService.selectById(id);
            result.setResult(productGroupProduct);
        } catch (Exception e) {
            log.error("根据id查询产品关系失败", e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }


}
