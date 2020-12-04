package com.colourfulchina.pangu.taishang.controller.bigan;

import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.pangu.taishang.api.vo.res.bigan.AlipayBookPriceVo;
import com.colourfulchina.pangu.taishang.api.vo.res.bigan.BookOrderReqVo;
import com.colourfulchina.pangu.taishang.api.vo.res.bigan.GoodsBlockVo;
import com.colourfulchina.pangu.taishang.service.BlockRuleService;
import com.colourfulchina.pangu.taishang.service.ProjectService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/bigan-replace/bookOrder")
public class BookOrderController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private BlockRuleService blockRuleService;


    @ApiOperation("查询商品block详情")
    @PostMapping("/queryGiftBlockList")
    public CommonResultVo<List<GoodsBlockVo>> getGoodsBlock(@RequestBody BookOrderReqVo bookOrderReqVo){
        CommonResultVo<List<GoodsBlockVo>> result = new CommonResultVo<List<GoodsBlockVo>>();
        List<GoodsBlockVo> list = projectService.queryGiftBlockList(bookOrderReqVo);
        result.setResult(list);
        return result;
    }

    @ApiOperation("支付宝产品预约时间范围及价格组装")
    @PostMapping("/prepareAlipayBookPrice")
    public CommonResultVo<AlipayBookPriceVo> prepareAlipayBookPrice(@RequestBody Integer productGroupProductId){
        CommonResultVo<AlipayBookPriceVo> result = new CommonResultVo<>();
        Assert.notNull(productGroupProductId,"产品组产品id不能为空");
        try {
            AlipayBookPriceVo alipayBookPriceVo = projectService.prepareAlipayBookPrice(productGroupProductId);
            result.setResult(alipayBookPriceVo);
        }catch (Exception e){
            log.error("支付宝产品预约时间范围及价格组装失败",e);
            result.setCode(200);
            result.setMsg("支付宝产品预约时间范围及价格组装失败");
        }
        return result;
    }

    @GetMapping("/getBlockRule/{pgpId}")
    public CommonResultVo<String> getBlockRule(@PathVariable(value = "pgpId") Integer pgpId){
        CommonResultVo<String> result = new CommonResultVo<String>();
        String blockRule = blockRuleService.getBlockRule(pgpId);
        result.setResult(blockRule);
        return result;
    }
}
