package com.colourfulchina.bigan.controller;

import com.colourfulchina.bigan.api.entity.ClfGym;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.inf.base.vo.PageVo;
import com.colourfulchina.bigan.service.ClfGymService;
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
@RequestMapping("/gym")
public class ClfGymController {

    @Autowired
    ClfGymService gymService;
    @ApiOperation(value = "客乐芙_健身分页查询", notes = "客乐芙_健身分页查询")
    @PostMapping("/page")
    public PageVo<ClfGym> selectPage(@RequestBody PageVo page){


        page = gymService.selectListPage(page);
        return page;
    }

    @ApiOperation(value = "新增客乐芙健身资源", notes = "新增客乐芙健身资源")
    @PostMapping("/add")
    public CommonResultVo<Boolean> add(@RequestBody ClfGym clfGym){
        CommonResultVo<Boolean> resultVo=new CommonResultVo<>();
//        clfGym.setCreateUser();
//        clfGym.setUpdateUser();
        final boolean result = gymService.insert(clfGym);
        resultVo.setResult(result);
        return resultVo;
    }

    @ApiOperation(value = "修改客乐芙健身资源", notes = "修改客乐芙健身资源")
    @PostMapping("/update")
    public CommonResultVo<Boolean> update(@RequestBody ClfGym clfGym){
        CommonResultVo<Boolean> resultVo=new CommonResultVo<>();
        clfGym.setUpdateTime(new Date());
//        clfGym.setUpdateUser();
        final boolean result = gymService.updateById(clfGym);
        resultVo.setResult(result);
        return resultVo;
    }

    @ApiOperation(value = "查看客乐芙健身资源详情", notes = "查看客乐芙健身资源详情")
    @PostMapping("/form")
    public CommonResultVo<ClfGym> form(@RequestBody ClfGym clfGym){
        CommonResultVo<ClfGym> resultVo=new CommonResultVo<>();
        clfGym = gymService.selectById(clfGym.getId());
        resultVo.setResult(clfGym);
        return resultVo;
    }

    @ApiOperation(value = "删除客乐芙健身资源", notes = "删除客乐芙健身资源")
    @PostMapping("/del/{id}")
    public CommonResultVo<String> del(@PathVariable Integer id){
        CommonResultVo<String> resultVo=new CommonResultVo<>();
        final int row = gymService.logicDelById(id);
        return resultVo;
    }
}
