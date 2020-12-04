package com.colourfulchina.bigan.controller;

import com.colourfulchina.bigan.api.entity.ClfSpa;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.inf.base.vo.PageVo;
import com.colourfulchina.bigan.service.ClfSpaService;
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
@RequestMapping("/spa")
public class ClfSpaController  {


    @Autowired
    ClfSpaService clfSpaService;
    @ApiOperation(value = "客乐芙_SPA分页查询", notes = "客乐芙_SPA分页查询")
    @PostMapping("/page")
    public PageVo<ClfSpa> selectPage(@RequestBody PageVo page){
        page = clfSpaService.selectListPage(page);
        return page;
    }

    @ApiOperation(value = "新增客乐芙spa资源", notes = "新增客乐芙spa资源")
    @PostMapping("/add")
    public CommonResultVo<Boolean> add(@RequestBody ClfSpa clfSpa){
        CommonResultVo<Boolean> resultVo=new CommonResultVo<>();
//        clfSpa.setCreateUser();
//        clfSpa.setUpdateUser();
        final boolean result = clfSpaService.insert(clfSpa);
        resultVo.setResult(result);
        return resultVo;
    }

    @ApiOperation(value = "修改客乐芙spa资源", notes = "修改客乐芙spa资源")
    @PostMapping("/update")
    public CommonResultVo<Boolean> update(@RequestBody ClfSpa clfSpa){
        CommonResultVo<Boolean> resultVo=new CommonResultVo<>();
        clfSpa.setUpdateTime(new Date());
//        clfSpa.setUpdateUser();
        final boolean result = clfSpaService.updateById(clfSpa);
        resultVo.setResult(result);
        return resultVo;
    }

    @ApiOperation(value = "查看客乐芙spa资源详情", notes = "查看客乐芙spa资源详情")
    @PostMapping("/form")
    public CommonResultVo<ClfSpa> form(@RequestBody ClfSpa clfSpa){
        CommonResultVo<ClfSpa> resultVo=new CommonResultVo<>();
        clfSpa = clfSpaService.selectById(clfSpa.getId());
        resultVo.setResult(clfSpa);
        return resultVo;
    }

    @ApiOperation(value = "删除客乐芙spa资源", notes = "删除客乐芙spa资源")
    @PostMapping("/del/{id}")
    public CommonResultVo<String> del(@PathVariable Integer id){
        CommonResultVo<String> resultVo=new CommonResultVo<>();
        final int row = clfSpaService.logicDelById(id);
        return resultVo;
    }
}
