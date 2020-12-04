package com.colourfulchina.pangu.taishang.controller;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.colourfulchina.god.door.api.annotation.SysGodDoorLog;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.pangu.taishang.api.entity.SysFileType;
import com.colourfulchina.pangu.taishang.service.SysFileTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sysFileType")
@Slf4j
@Api(tags = {"文件类型操作接口"})
public class SysFileTypeController {
    @Autowired
    private SysFileTypeService sysFileTypeService;

    @SysGodDoorLog("查询文件类型列表")
    @ApiOperation("查询文件类型列表")
    @PostMapping("/list")
    public CommonResultVo<List<SysFileType>> list(){
        CommonResultVo<List<SysFileType>> resultVo=new CommonResultVo<>();
        try {
            Wrapper<SysFileType> wrapper=new Wrapper<SysFileType>() {
                @Override
                public String getSqlSegment() {
                    return null;
                }
            };
            final List<SysFileType> fileTypeList = sysFileTypeService.selectList(wrapper);
            resultVo.setResult(fileTypeList);
        }catch (Exception e){
            log.error("查询文件类型列表失败",e);
            resultVo.setCode(200);
            resultVo.setMsg(e.getMessage());
        }
        return resultVo;
    }
}