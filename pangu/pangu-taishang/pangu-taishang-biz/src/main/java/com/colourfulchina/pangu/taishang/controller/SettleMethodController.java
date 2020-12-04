package com.colourfulchina.pangu.taishang.controller;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.colourfulchina.god.door.api.annotation.SysGodDoorLog;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.pangu.taishang.api.entity.SettleMethod;
import com.colourfulchina.pangu.taishang.service.SettleMethodService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/settleMethod")
@Slf4j
@Api(tags = {"结算方式操作接口"})
public class SettleMethodController {
    @Autowired
    private SettleMethodService settleMethodService;

    /**
     * 结算方式列表查询
     * @return
     */
    @SysGodDoorLog("结算方式列表查询")
    @ApiOperation("结算方式列表查询")
    @PostMapping("/selectSettleMethodList")
    public CommonResultVo<List<SettleMethod>> selectSettleMethodList(){
        CommonResultVo<List<SettleMethod>> result = new CommonResultVo<>();
        try {
            Wrapper wrapper = new Wrapper() {
                @Override
                public String getSqlSegment() {
                    return "where enabled = 0";
                }
            };
            List<SettleMethod> settleMethodList = settleMethodService.selectList(wrapper);
            result.setResult(settleMethodList);
        }catch (Exception e){
            log.error("结算方式列表查询失败",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }
}