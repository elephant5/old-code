package com.colourfulchina.bigan.controller;

import com.colourfulchina.bigan.api.entity.Caccom;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.inf.base.vo.PageVo;
import com.colourfulchina.bigan.service.CaccomService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * User: Ryan
 * Date: 2018/8/3
 */

@Slf4j
@RestController
@RequestMapping("/caccom")
public class CaccomController {

    @Autowired
    CaccomService caccomService;
    @ApiOperation(value = "C端项目住宿含套餐分页查询", notes = "C端项目住宿含套餐分页查询")
    @PostMapping("/page")  
    public PageVo<Caccom> selectPage(@RequestBody PageVo page){
        page = caccomService.selectListPage(page);
        return page;
    }

    @ApiOperation(value = "新增C端住宿资源", notes = "新增C端住宿资源")
    @PostMapping("/add")
    public CommonResultVo<Boolean> add(@RequestBody Caccom caccom){
        CommonResultVo<Boolean> resultVo=new CommonResultVo<>();
//        caccom.setCreateUser();
//        caccom.setUpdateUser();
        final boolean result = caccomService.insert(caccom);
        resultVo.setResult(result);
        return resultVo;
    }

    @ApiOperation(value = "修改C端住宿资源", notes = "修改C端住宿资源")
    @PostMapping("/update")
    public CommonResultVo<Boolean> update(@RequestBody Caccom caccom){
        CommonResultVo<Boolean> resultVo=new CommonResultVo<>();
        caccom.setUpdateTime(new Date());
//        caccom.setUpdateUser();
        final boolean result = caccomService.updateById(caccom);
        resultVo.setResult(result);
        return resultVo;
    }

    @ApiOperation(value = "查看C端住宿资源详情", notes = "查看C端住宿资源详情")
    @PostMapping("/form")
    public CommonResultVo<Caccom> form(@RequestBody Caccom caccom){
        CommonResultVo<Caccom> resultVo=new CommonResultVo<>();
        caccom = caccomService.selectById(caccom.getId());
        resultVo.setResult(caccom);
        return resultVo;
    }

    @ApiOperation(value = "删除C端住宿资源", notes = "删除C端住宿资源")
    @PostMapping("/del/{id}")
    public CommonResultVo<String> del(@PathVariable Integer id){
        CommonResultVo<String> resultVo=new CommonResultVo<>();
        final int row = caccomService.logicDelById(id);
        return resultVo;
    }
    
}
