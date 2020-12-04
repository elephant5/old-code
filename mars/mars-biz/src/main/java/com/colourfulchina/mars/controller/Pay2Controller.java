package com.colourfulchina.mars.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.colourfulchina.god.door.api.annotation.SysGodDoorLog;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.mars.api.vo.req.AggregatePayParamsReqVo;
import com.colourfulchina.mars.api.vo.res.PayParamsResVo;
import com.colourfulchina.mars.service.PayInfoService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Author: 罗幸
 * @Description:
 * @Date: 2020/10/27 14:12
 */
@RestController
@RequestMapping("/pay2")
@Slf4j
public class Pay2Controller {
    @Autowired
    PayInfoService payInfoService;
    @SysGodDoorLog("封装聚合支付入参(new copy 2)")
    @ApiOperation("封装聚合支付入参(new copy2)")
    @RequestMapping("/getAggregatePayParams")
    public CommonResultVo<PayParamsResVo> getAggregatePayParams(@RequestBody AggregatePayParamsReqVo reqVo) {
        CommonResultVo<PayParamsResVo> resultVo = new CommonResultVo<PayParamsResVo>();
        try {
            PayParamsResVo resVo = payInfoService.getAggregatePayParams(reqVo);
            if (!StringUtils.isEmpty(resVo) && !StringUtils.isEmpty(resVo.getParams()) && !StringUtils.isEmpty(resVo.getMwebUrl())) {
                resultVo.setResult(resVo);
                resultVo.setCode(100);
                resultVo.setMsg("成功");
            } else {
                resultVo.setCode(200);
                resultVo.setMsg("失败");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            resultVo.setCode(200);
            resultVo.setMsg(e.getMessage());
        }
        return resultVo;
    }

    @SysGodDoorLog("聚合支付参数入参 map版本2")
    @ApiOperation("聚合支付参数入参 map版本2")
    @PostMapping("/getAggregateParamsByMap")
    public CommonResultVo<PayParamsResVo> getAggregateParamsByMap(@RequestBody Map<String,Object> param){
        log.info("传入参数为：{}",JSONObject.toJSONString(param));
        AggregatePayParamsReqVo reqVo = JSON.parseObject(JSON.toJSONString(param), AggregatePayParamsReqVo.class);
        log.info("转换后的对象为：{}",JSONObject.toJSONString(reqVo));
        CommonResultVo<PayParamsResVo> resultVo = new CommonResultVo<PayParamsResVo>();
        try {
            PayParamsResVo resVo = payInfoService.getAggregatePayParams(reqVo);
            if (!StringUtils.isEmpty(resVo) && !StringUtils.isEmpty(resVo.getParams()) && !StringUtils.isEmpty(resVo.getMwebUrl())) {
                resultVo.setResult(resVo);
                resultVo.setCode(100);
                resultVo.setMsg("成功");
            } else {
                resultVo.setCode(200);
                resultVo.setMsg("失败");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            resultVo.setCode(200);
            resultVo.setMsg(e.getMessage());
        }
        return resultVo;

    }
}
