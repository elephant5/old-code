package com.colourfulchina.pangu.taishang.controller;

import com.colourfulchina.god.door.api.annotation.SysGodDoorLog;
import com.colourfulchina.inf.base.entity.SysOperateLogDetail;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.inf.base.vo.PageVo;
import com.colourfulchina.pangu.taishang.service.SysOperateLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/operateLog")
@Api(tags = {"操作日志接口"})
public class OperateLogController {
    @Autowired
    private SysOperateLogService sysOperateLogService;


    @SysGodDoorLog("查询业务操作日志")
    @ApiOperation("查询业务操作日志")
    @PostMapping("/queryOperateLogDetailPage")
    public CommonResultVo<PageVo<SysOperateLogDetail>> queryOperateLogDetailPage(@RequestBody PageVo<SysOperateLogDetail> detailPageVo){
        CommonResultVo<PageVo<SysOperateLogDetail>> resultVo=new CommonResultVo<>();
        try {
            if (detailPageVo == null || CollectionUtils.isEmpty(detailPageVo.getCondition())){
                throw new Exception("查询参数不能为空");
            }
            final PageVo<SysOperateLogDetail> logDetailPageVo = sysOperateLogService.querySysOperateLogDetailPage(detailPageVo);
            resultVo.setResult(logDetailPageVo);
        }catch (Exception e){
            log.error("queryOperateLogDetailPage error:{}",e.getMessage(),e);
            resultVo.setCode(200);
            resultVo.setMsg(e.getMessage()==null?"查询操作日志详情失败":e.getMessage());
        }
        return resultVo;
    }
}
