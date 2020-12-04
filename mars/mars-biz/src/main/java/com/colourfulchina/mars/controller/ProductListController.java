package com.colourfulchina.mars.controller;

import com.colourfulchina.god.door.api.annotation.SysGodDoorLog;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.inf.base.vo.PageVo;
import com.colourfulchina.mars.api.vo.GoodsListReqVo;
import com.colourfulchina.mars.api.vo.res.ProductDetailResVo;
import com.colourfulchina.mars.api.vo.res.QueryListResDto;
import com.colourfulchina.mars.service.ProductListService;
import com.colourfulchina.pangu.taishang.api.vo.res.SelectBookProductRes;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: Nickal
 * @Description:
 * @Date: 2019/5/17 15:49
 */
@Slf4j
@RestController
@RequestMapping("/product")
public class ProductListController {
    private final static int SUCCESS_CODE = 100;

    private final static int ERROR_CODE = 200;
    @Autowired
    private ProductListService productListService;

    @SysGodDoorLog("产品列表1")
    @ApiOperation("产品列表1")
    @PostMapping(value = "/list")
    public CommonResultVo<List<QueryListResDto>> getGoodsDetail(Integer groupId , String service) throws Exception {
        CommonResultVo<List<QueryListResDto>> commResultVo = new CommonResultVo<>();
        try {
            List<QueryListResDto> vo = productListService.getProductList(groupId,service);
            commResultVo.setResult(vo);
            commResultVo.setCode(SUCCESS_CODE);
            commResultVo.setMsg("成功");
        } catch (Exception e) {
            log.error("产品列表查询失败",e);
            commResultVo.setCode(ERROR_CODE);
            commResultVo.setMsg(e.getMessage());
        }
        return commResultVo;

    }
    @SysGodDoorLog("产品列表2")
    @ApiOperation("产品列表2")
    @PostMapping(value = "/goodsList")
    public CommonResultVo<List<QueryListResDto>> getGoodsList(@RequestBody GoodsListReqVo goodsListReqVo) throws Exception {
        CommonResultVo<List<QueryListResDto>> commResultVo = new CommonResultVo<>();
        try {
            List<QueryListResDto> vo = productListService.getGoddsList(goodsListReqVo);
            commResultVo.setResult(vo);
            commResultVo.setCode(SUCCESS_CODE);
            commResultVo.setMsg("成功");
        } catch (Exception e) {
            log.error("产品列表查询失败",e);
            commResultVo.setCode(ERROR_CODE);
            commResultVo.setMsg(e.getMessage());
        }
        return commResultVo;

    }

    @SysGodDoorLog("产品列表3")
    @ApiOperation("产品列表3(高迪安用)")
    @PostMapping(value = "/goodsListNEW2")
    public CommonResultVo<List<SelectBookProductRes>> getGoodsListNEW2(@RequestBody Integer goodId) throws Exception {
        CommonResultVo<List<SelectBookProductRes>> commResultVo = new CommonResultVo<>();
        try {
            List<SelectBookProductRes> vo = productListService.getGoddsListNEW2(goodId);
            commResultVo.setResult(vo);
            commResultVo.setCode(SUCCESS_CODE);
            commResultVo.setMsg("成功");
        } catch (Exception e) {
            log.error("产品列表查询失败",e);
            commResultVo.setCode(ERROR_CODE);
            commResultVo.setMsg(e.getMessage());
        }
        return commResultVo;

    }

    @SysGodDoorLog("产品列表2优化")
    @ApiOperation("产品列表2优化")
    @PostMapping(value = "/goodsListNew")
    public CommonResultVo<List<QueryListResDto>> getGoodsListNew(@RequestBody GoodsListReqVo goodsListReqVo) throws Exception {
        CommonResultVo<List<QueryListResDto>> commResultVo = new CommonResultVo<>();
        try {
            List<QueryListResDto> vo = productListService.getGoddsListNew(goodsListReqVo);
            commResultVo.setResult(vo);
            commResultVo.setCode(SUCCESS_CODE);
            commResultVo.setMsg("成功");
        } catch (Exception e) {
            log.error("产品列表查询失败",e);
            commResultVo.setCode(ERROR_CODE);
            commResultVo.setMsg(e.getMessage());
        }
        return commResultVo;

    }

    @SysGodDoorLog("产品列表2分页")
    @ApiOperation("产品列表2分页")
    @PostMapping(value = "/goodsListPaging")
    public CommonResultVo<PageVo<QueryListResDto>> getGoodsListPaging(@RequestBody GoodsListReqVo goodsListReqVo) throws Exception {
        CommonResultVo<PageVo<QueryListResDto>> commResultVo = new CommonResultVo<>();
        try {
            PageVo<QueryListResDto> vo = productListService.getGoddsListPaging(goodsListReqVo);
            commResultVo.setResult(vo);
            commResultVo.setCode(SUCCESS_CODE);
            commResultVo.setMsg("成功");
        } catch (Exception e) {
            log.error("产品列表查询失败",e);
            commResultVo.setCode(ERROR_CODE);
            commResultVo.setMsg(e.getMessage());
        }
        return commResultVo;

    }

    @SysGodDoorLog("查询产品组下面产品的详情")
    @ApiOperation("查询产品组下面产品的详情")
    @PostMapping(value = "/detail")
    public CommonResultVo<ProductDetailResVo> selectProductDetail(Integer productGroupProductId, Integer giftCodeId) throws Exception {
        CommonResultVo<ProductDetailResVo> commResultVo = new CommonResultVo<ProductDetailResVo>();
        try {
        	ProductDetailResVo vo = productListService.selectProductDetail(productGroupProductId, giftCodeId);
            commResultVo.setResult(vo);
            commResultVo.setCode(SUCCESS_CODE);
            commResultVo.setMsg("成功");
        } catch (Exception e) {
            log.error("产品组下面的产品详情查询失败",e);
            commResultVo.setCode(ERROR_CODE);
            commResultVo.setMsg(e.getMessage());
        }
        return commResultVo;

    }
}
