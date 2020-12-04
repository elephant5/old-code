package com.colourfulchina.pangu.taishang.controller;

import com.colourfulchina.god.door.api.annotation.SysGodDoorLog;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.pangu.taishang.api.entity.GoodsSetting;
import com.colourfulchina.pangu.taishang.service.GoodsSettingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/goodsSetting")
@Slf4j
@Api(tags = {"商品预约限制"})
public class GoodsSettingController {
    @Autowired
    private GoodsSettingService goodsSettingService;

    @SysGodDoorLog("根据商品id查询商品预约限制")
    @ApiOperation("根据商品id查询商品预约限制")
    @PostMapping("/selectGoodsSettingById")
    public CommonResultVo<GoodsSetting> selectGoodsSettingById(@RequestBody Integer goodsId){
        CommonResultVo<GoodsSetting> result = new CommonResultVo<>();
        try {
            Assert.notNull(goodsId,"商品id不能为空");
            GoodsSetting goodsSetting = goodsSettingService.selectById(goodsId);
            result.setResult(goodsSetting);
        }catch (Exception e){
            log.error("查询商品预约限制失败",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }
}