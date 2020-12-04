package com.colourfulchina.bigan.controller;

import com.colourfulchina.bigan.api.entity.ClfRoomUp;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.inf.base.vo.PageVo;
import com.colourfulchina.bigan.service.ClfRoomUpService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * User: Ryan
 * Date: 2018/8/5
 */
@Slf4j
@RestController
@RequestMapping("/roomUp")
public class ClfRoomUpController {

    @Autowired
    ClfRoomUpService roomUpService;
    @ApiOperation(value = "客乐芙酒店客房升等分页查询", notes = "客乐芙酒店客房升等分页查询")
    @PostMapping("/page")
    public PageVo<ClfRoomUp> selectPage(@RequestBody PageVo page){


        page = roomUpService.selectListPage(page);
        return page;
    }

    @ApiOperation(value = "新增客乐芙酒店客房升等资源", notes = "新增客乐芙酒店客房升等资源")
    @PostMapping("/add")
    public CommonResultVo<Boolean> add(@RequestBody ClfRoomUp clfRoomUp){
        CommonResultVo<Boolean> resultVo=new CommonResultVo<>();
//        clfRoomUp.setCreateUser();
//        clfRoomUp.setUpdateUser();
        final boolean result = roomUpService.insert(clfRoomUp);
        resultVo.setResult(result);
        return resultVo;
    }

    @ApiOperation(value = "修改客乐芙酒店客房升等资源", notes = "修改客乐芙酒店客房升等资源")
    @PostMapping("/update")
    public CommonResultVo<Boolean> update(@RequestBody ClfRoomUp clfRoomUp){
        CommonResultVo<Boolean> resultVo=new CommonResultVo<>();
        clfRoomUp.setUpdateTime(new Date());
//        clfRoomUp.setUpdateUser();
        final boolean result = roomUpService.updateById(clfRoomUp);
        resultVo.setResult(result);
        return resultVo;
    }

    @ApiOperation(value = "查看客乐芙酒店客房升等资源详情", notes = "查看客乐芙酒店客房升等资源详情")
    @PostMapping("/form")
    public CommonResultVo<ClfRoomUp> form(@RequestBody ClfRoomUp clfRoomUp){
        CommonResultVo<ClfRoomUp> resultVo=new CommonResultVo<>();
        clfRoomUp = roomUpService.selectById(clfRoomUp.getId());
        resultVo.setResult(clfRoomUp);
        return resultVo;
    }

    @ApiOperation(value = "删除客乐芙酒店客房升等资源", notes = "删除客乐芙酒店客房升等资源")
    @PostMapping("/del/{id}")
    public CommonResultVo<String> del(@PathVariable Integer id){
        CommonResultVo<String> resultVo=new CommonResultVo<>();
        final int row = roomUpService.logicDelById(id);
        return resultVo;
    }
}
