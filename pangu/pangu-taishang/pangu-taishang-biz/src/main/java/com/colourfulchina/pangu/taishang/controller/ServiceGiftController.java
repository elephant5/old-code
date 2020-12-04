package com.colourfulchina.pangu.taishang.controller;

import com.colourfulchina.god.door.api.annotation.SysGodDoorLog;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.pangu.taishang.api.entity.ServiceGift;
import com.colourfulchina.pangu.taishang.api.entity.ShopType;
import com.colourfulchina.pangu.taishang.service.ServiceGiftService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/serviceGift")
@Slf4j
public class ServiceGiftController {
    @Autowired
    private ServiceGiftService serviceGiftService;


    @SysGodDoorLog("根据code资源类型对应的权益类型")
    @ApiOperation("根据code资源类型对应的权益类型")
    @PostMapping("/get")
    public CommonResultVo<List<ServiceGift>> get(@RequestBody String code){
        CommonResultVo<List<ServiceGift>> resultVo=new CommonResultVo<>();
        try {
            final List<ServiceGift> shopTypeList = serviceGiftService.selectByCode(code);
            resultVo.setResult(shopTypeList);
        }catch (Exception e){
            log.error("根据code资源类型对应的权益类型失败",e);
            resultVo.setCode(200);
            resultVo.setMsg(e.getMessage());
        }
        return resultVo;
    }

}