package com.colourfulchina.pangu.taishang.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.colourfulchina.god.door.api.annotation.SysGodDoorLog;
import com.colourfulchina.god.door.api.util.SecurityUtils;
import com.colourfulchina.god.door.api.vo.KltSysUser;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.inf.base.vo.PageVo;
import com.colourfulchina.pangu.taishang.api.entity.Product;
import com.colourfulchina.pangu.taishang.api.entity.ShopItem;
import com.colourfulchina.pangu.taishang.api.vo.req.ProductPageReq;
import com.colourfulchina.pangu.taishang.api.vo.res.ProductPackPageRes;
import com.colourfulchina.pangu.taishang.api.vo.res.ProductPageRes;
import com.colourfulchina.pangu.taishang.service.ProductItemService;
import com.colourfulchina.pangu.taishang.service.ProductService;
import com.colourfulchina.pangu.taishang.service.ShopItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/product")
@Slf4j
@Api(tags = {"产品操作接口"})
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private ShopItemService shopItemService;
    @Autowired
    private ProductItemService productItemService;

    /**
     * 产品分页查询列表
     * @param pageReq
     * @return
     */
    @ApiOperation("产品分页查询")
    @PostMapping("/selectPage")
    public CommonResultVo<PageVo<ProductPageRes>> selectPage(@RequestBody PageVo<ProductPageReq> pageReq){
        CommonResultVo<PageVo<ProductPageRes>> result = new CommonResultVo();
        try {
            PageVo<ProductPageRes> pageRes = productService.selectPageList(pageReq);
            result.setResult(pageRes);
        }catch (Exception e){
            log.error("产品分页查询失败",e);
            result.setCode(200);
            result.setMsg("产品分页查询失败");
        }
        return result;
    }

    /**
     * 打包产品组产品时产品分页查询列表
     * @param pageReq
     * @return
     */
    @ApiOperation("打包产品组产品时产品分页查询列表")
    @PostMapping("/selectPackGoodsPage")
    public CommonResultVo<PageVo<ProductPackPageRes>> selectPackGoodsPage(@RequestBody PageVo<ProductPageReq> pageReq){
        CommonResultVo<PageVo<ProductPackPageRes>> result = new CommonResultVo();
        try {
            Assert.isTrue(!CollectionUtils.isEmpty(pageReq.getCondition()) &&
                    ((pageReq.getCondition().containsKey("serviceList") && !CollectionUtils.isEmpty((List<String>) pageReq.getCondition().get("serviceList")))
                    ||(pageReq.getCondition().containsKey("service") && !CollectionUtils.isEmpty((List<String>) pageReq.getCondition().get("service")))),"请选择资源类型");
            if (pageReq.getCondition().containsKey("serviceList")){
                List<String> ser = (List<String>) pageReq.getCondition().get("serviceList");
                if (!CollectionUtils.isEmpty(ser)){
                    Assert.isTrue(!(ser.contains("住宿") && ser.size() > 1),"住宿和非住宿不能混合选择");
                }
            }
            if (pageReq.getCondition().containsKey("service")){
                List<String> ser = (List<String>) pageReq.getCondition().get("service");
                if (!CollectionUtils.isEmpty(ser)){
                    Assert.isTrue(!(ser.contains("住宿") && ser.size() > 1),"住宿和非住宿不能混合选择");
                }
            }
            PageVo<ProductPackPageRes> pageRes = productService.selectPackPageList(pageReq);
            result.setResult(pageRes);
        }catch (Exception e){
            log.error("打包产品组产品时产品分页查询失败",e);
            result.setCode(200);
            result.setMsg("打包产品组产品时产品分页查询失败");
        }
        return result;
    }

    @SysGodDoorLog("导出产品为Excel表")
    @ApiOperation("导出产品为Excel表")
    @PostMapping("/export")
    public CommonResultVo<String> export(@RequestBody PageVo<ProductPageReq> pageReq){
        CommonResultVo<String> result = new CommonResultVo<>();
        try {
            Assert.isTrue(!CollectionUtils.isEmpty(pageReq.getCondition()) && pageReq.getCondition().containsKey("service") && !CollectionUtils.isEmpty((List<String>) pageReq.getCondition().get("service")),"请选择资源类型");
            if (pageReq.getCondition().containsKey("service")){
                List<String> ser = (List<String>) pageReq.getCondition().get("service");
                if (!CollectionUtils.isEmpty(ser)){
                    Assert.isTrue(!(ser.contains("住宿") && ser.size() > 1),"住宿和非住宿不能混合选择");
                }
            }
            KltSysUser sysUser = SecurityUtils.getAttributeUser();
            Assert.isTrue(sysUser != null && sysUser.getEmail() != null , "收件人不能为空");
            String url = productService.exportProduct(pageReq,sysUser);
            result.setResult(url);
        } catch (Exception e) {
            log.error("产品导出失败",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    /**
     * 生成所有产品
     * @return
     */
    @ApiOperation("生成所有产品")
    @PostMapping("/generateProduct")
    public CommonResultVo generateProduct(HttpServletRequest request){
        CommonResultVo result = new CommonResultVo();
        try {
            List<ShopItem> shopItems = shopItemService.selectList(null);
            for (ShopItem shopItem : shopItems) {
//                productService.generateProduct(shopItem.getId(),request);
            }
        }catch (Exception e){
            log.error("产品生成失败",e);
            result.setCode(200);
            result.setMsg("产品生成失败");
        }
        return result;
    }

    /**
     * 生成所有产品子项
     * @return
     */
    @ApiOperation("生成所有产品子项")
    @PostMapping("/generateProductItem")
    public CommonResultVo generateProductItem(HttpServletRequest request){
        CommonResultVo result = new CommonResultVo();
        try {
            Wrapper wrapper = new Wrapper() {
                @Override
                public String getSqlSegment() {
                    return "where del_flag = 0";
                }
            };
            List<Product> products = productService.selectList(wrapper);
            for (Product product : products) {
//                productItemService.generateProductItem(product.getId(),request);
            }
        }catch (Exception e){
            log.error("产品子项生成失败",e);
            result.setCode(200);
            result.setMsg("产品子项生成失败");
        }
        return result;
    }


    /**
     * 住宿只有固定贴现结算规则时产品子项 及成本显示问题
     * @return
     */
    @PostMapping("/discountProduct")
    public CommonResultVo discountProduct(HttpServletRequest request){
        CommonResultVo result = new CommonResultVo();
        try {
            List<Integer> list = productService.discountProduct();
            for (Integer integer : list) {
//                productService.generateProduct(integer,request);
            }
        }catch (Exception e){
            log.error("产品生成失败",e);
            result.setCode(200);
            result.setMsg("产品生成失败");
        }
        return result;
    }

    /**
     * 查询产品
     * @return
     */
    @PostMapping("/getProductByShop")
    public CommonResultVo<Product> getProductByShop(@RequestBody Product p){
        CommonResultVo<Product> result = new CommonResultVo();
        try {
            Assert.notNull(p.getShopId(),"商户ID不能为空");
            Assert.notNull(p.getShopItemId(),"商户规格ID不能为空");
//            Assert.notNull(p.getGift(),"权益类型");
            EntityWrapper<Product> local = new EntityWrapper<>();
            local.eq("shop_id",p.getShopId());
            local.eq("shop_item_id",p.getShopItemId());
            if (p.getGift() != null){
                local.eq("gift",p.getGift());
            }
            List<Product> list = productService.selectList(local);
            result.setResult(list.size()>0?list.get(0):null);
        }catch (Exception e){
            log.error("查询产品失败",e);
            result.setCode(200);
            result.setMsg("查询产品失败");
        }
        return result;
    }

}
