package com.colourfulchina.bigan.controller;

import com.colourfulchina.bigan.api.entity.ClfBreakfast;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.inf.base.vo.PageVo;
import com.colourfulchina.bigan.service.ClfBreakfastService;
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
@RequestMapping("/breakfast")
public class ClfBreakfastController {


    @Autowired
    ClfBreakfastService clfBreakfastService;
    
    @ApiOperation(value = "客乐芙酒店早餐分页查询", notes = "客乐芙酒店早餐分页查询")
    @PostMapping("/page")
    public PageVo<ClfBreakfast> selectPage(@RequestBody PageVo page){

        page = clfBreakfastService.selectListPage(page);
        return page;
    }

    @ApiOperation(value = "新增客乐芙酒店早餐资源", notes = "新增客乐芙酒店早餐资源")
    @PostMapping("/add")
    public CommonResultVo<Boolean> add(@RequestBody ClfBreakfast clfBreakfast){
        CommonResultVo<Boolean> resultVo=new CommonResultVo<>();
//        clfBreakfast.setCreateUser();
//        clfBreakfast.setUpdateUser();
        final boolean result = clfBreakfastService.insert(clfBreakfast);
        resultVo.setResult(result);
        return resultVo;
    }

    @ApiOperation(value = "修改客乐芙酒店早餐资源", notes = "修改客乐芙酒店早餐资源")
    @PostMapping("/update")
    public CommonResultVo<Boolean> update(@RequestBody ClfBreakfast clfBreakfast){
        CommonResultVo<Boolean> resultVo=new CommonResultVo<>();
        clfBreakfast.setUpdateTime(new Date());
//        clfBreakfast.setUpdateUser();
        final boolean result = clfBreakfastService.updateById(clfBreakfast);
        resultVo.setResult(result);
        return resultVo;
    }

    @ApiOperation(value = "查看客乐芙酒店早餐资源详情", notes = "查看客乐芙酒店早餐资源详情")
    @PostMapping("/form")
    public CommonResultVo<ClfBreakfast> form(@RequestBody ClfBreakfast clfBreakfast){
        CommonResultVo<ClfBreakfast> resultVo=new CommonResultVo<>();
        clfBreakfast = clfBreakfastService.selectById(clfBreakfast.getId());
        resultVo.setResult(clfBreakfast);
        return resultVo;
    }

    @ApiOperation(value = "删除客乐芙酒店早餐资源", notes = "删除客乐芙酒店早餐资源")
    @PostMapping("/del/{id}")
    public CommonResultVo<String> del(@PathVariable Integer id){
        CommonResultVo<String> resultVo=new CommonResultVo<>();
        final int row = clfBreakfastService.logicDelById(id);
        return resultVo;
    }
}
