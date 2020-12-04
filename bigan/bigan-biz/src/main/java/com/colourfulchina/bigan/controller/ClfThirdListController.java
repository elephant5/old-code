package com.colourfulchina.bigan.controller;

import com.colourfulchina.bigan.api.entity.ClfThirdList;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.inf.base.vo.PageVo;
import com.colourfulchina.bigan.service.ClfThirdListService;
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
@RequestMapping("/thirdList")
public class ClfThirdListController {

    @Autowired
    ClfThirdListService clfThirdListService;
    @ApiOperation(value = "第三方列表分页查询", notes = "第三方列表分页查询")
    @PostMapping("/page")
    public PageVo<ClfThirdList> selectPage(@RequestBody PageVo page){


        page = clfThirdListService.selectListPage(page);
        return page;
    }

    @ApiOperation(value = "新增第三方列表资源", notes = "新增第三方列表资源")
    @PostMapping("/add")
    public CommonResultVo<Boolean> add(@RequestBody ClfThirdList clfThirdList){
        CommonResultVo<Boolean> resultVo=new CommonResultVo<>();
//        clfThirdList.setCreateUser();
//        clfThirdList.setUpdateUser();
        final boolean result = clfThirdListService.insert(clfThirdList);
        resultVo.setResult(result);
        return resultVo;
    }

    @ApiOperation(value = "修改第三方列表资源", notes = "修改第三方列表资源")
    @PostMapping("/update")
    public CommonResultVo<Boolean> update(@RequestBody ClfThirdList clfThirdList){
        CommonResultVo<Boolean> resultVo=new CommonResultVo<>();
        clfThirdList.setUpdateTime(new Date());
//        clfThirdList.setUpdateUser();
        final boolean result = clfThirdListService.updateById(clfThirdList);
        resultVo.setResult(result);
        return resultVo;
    }

    @ApiOperation(value = "查看第三方列表资源详情", notes = "查看第三方列表资源详情")
    @PostMapping("/form")
    public CommonResultVo<ClfThirdList> form(@RequestBody ClfThirdList clfThirdList){
        CommonResultVo<ClfThirdList> resultVo=new CommonResultVo<>();
        clfThirdList = clfThirdListService.selectById(clfThirdList.getId());
        resultVo.setResult(clfThirdList);
        return resultVo;
    }

    @ApiOperation(value = "删除第三方列表资源", notes = "删除第三方列表资源")
    @PostMapping("/del/{id}")
    public CommonResultVo<String> del(@PathVariable Integer id){
        CommonResultVo<String> resultVo=new CommonResultVo<>();
        final int row = clfThirdListService.logicDelById(id);
        return resultVo;
    }
}
