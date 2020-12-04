package com.colourfulchina.mars.controller;


import com.alibaba.fastjson.JSON;
import com.colourfulchina.god.door.api.annotation.SysGodDoorLog;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.mars.api.entity.EquityCodeDetail;
import com.colourfulchina.mars.api.vo.EquityListVo;
import com.colourfulchina.mars.api.vo.req.CheckGiftCodeReq;
import com.colourfulchina.mars.api.vo.req.EquityCodeDetailReq;
import com.colourfulchina.mars.api.vo.res.GiftTimesVo;
import com.colourfulchina.mars.service.EquityCodeDetailService;
import com.colourfulchina.pangu.taishang.api.vo.res.bigan.BookOrderReqVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/equityCodeDetail")
@Api(value = "权益使用详情相关Controller",tags = {"权益使用详情相关操作接口"})
public class EquityCodeDetailController {


    @Autowired
    EquityCodeDetailService equityCodeDetailService;

    /**
     * 匹配权益列表
     * @param equityListVo
     * @return
     */
    @SysGodDoorLog("匹配权益列表")
    @ApiOperation("匹配权益列表")
    @PostMapping("/selectEquityCodeList")
    public CommonResultVo<List<EquityListVo>> selectEquityList(@RequestBody EquityListVo equityListVo){
        CommonResultVo<List<EquityListVo>> result = new CommonResultVo();
        try {

        }catch (Exception e){
            log.error("匹配权益列表查询失败" ,e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    @SysGodDoorLog("获取商品产品组使用记录信息")
    @ApiOperation("获取商品产品组使用记录信息")
    @PostMapping("/selectByEquityCode")
    public CommonResultVo<EquityCodeDetail> selectByEquityCode(@RequestBody EquityCodeDetailReq equityCodeDetailReq){
        CommonResultVo<EquityCodeDetail> result = new CommonResultVo();
        try {
            log.info("查询当前权益剩余的使用次数:{}", JSON.toJSON(equityCodeDetailReq));
            EquityCodeDetail equityCodeDetail = equityCodeDetailService.selectByEquityCode(equityCodeDetailReq.getMemberId(),equityCodeDetailReq.getGoodsId(),equityCodeDetailReq.getGroupId(),equityCodeDetailReq.getGiftCodeId(),new Date());
            result.setResult(equityCodeDetail);
        }catch (Exception e){
            log.error("获取商品产品组使用记录信息异常:" ,e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    @SysGodDoorLog("根据gift_code的id判断权益是否使用过")
    @ApiOperation("根据gift_code的id判断权益是否使用过")
    @PostMapping("/checkGiftCode")
    public CommonResultVo<Boolean> checkGiftCode(@RequestBody CheckGiftCodeReq checkGiftCodeReq){
        CommonResultVo<Boolean> result = new CommonResultVo();
        try {
            Boolean f = equityCodeDetailService.checkGiftCodeUse(checkGiftCodeReq.getGiftCodeId());
            result.setResult(f);
        }catch (Exception e){
            log.error("根据gift_code的id判断权益是否使用过失败" ,e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    @SysGodDoorLog("根据gift_code的id查询权益信息")
    @ApiOperation("根据gift_code的id查询权益信息")
    @PostMapping("/selectEquityCodeDetailList")
    public CommonResultVo< List<EquityCodeDetail>> selectEquityCodeDetailListByGiftCodeId(@RequestBody CheckGiftCodeReq checkGiftCodeReq){
        CommonResultVo< List<EquityCodeDetail>> result = new CommonResultVo();
        try {
            List<EquityCodeDetail> list = equityCodeDetailService.selectEquityCodeDetailListByGiftCodeId(checkGiftCodeReq.getGiftCodeId());
            result.setResult(list);
        }catch (Exception e){
            log.error("根据gift_code的id查询权益信息失败" ,e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    //权益次数查询
    @ApiOperation("查询权益次数限制")
    @PostMapping("/queryUnitGroups")
    public CommonResultVo<List<GiftTimesVo>> queryGiftTimesVo(@RequestBody BookOrderReqVo bookOrderReqVo){
        CommonResultVo<List<GiftTimesVo>> result = new CommonResultVo<List<GiftTimesVo>>();
        List<GiftTimesVo> giftTimesVoList = equityCodeDetailService.selectGiftTimesVoList(bookOrderReqVo.getGroupId(), bookOrderReqVo.getUnitId());
        result.setResult(giftTimesVoList);
        return result;
    }
}
