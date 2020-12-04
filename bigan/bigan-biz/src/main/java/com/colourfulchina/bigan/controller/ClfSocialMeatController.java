package com.colourfulchina.bigan.controller;

import com.colourfulchina.bigan.api.entity.ClfSocialMeat;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.inf.base.vo.PageVo;
import com.colourfulchina.bigan.service.ClfSocialMeatService;
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
@RequestMapping("/socialMeat")
public class ClfSocialMeatController {

    @Autowired
    ClfSocialMeatService clfSocialMeatService;
//    @ApiImplicitParams({
//            @ApiImplicitParam(paramType="pageNumber",name="pageNumber",dataType="Integer",required=true,value="当前页数",defaultValue="1"),
//            @ApiImplicitParam(paramType="pageSize",name="pageSize",dataType="Integer",required=true,value="当页条数",defaultValue="100"),
//            @ApiImplicitParam(paramType="params",name="params",dataType="Map",required=false,value="参数")
//    })
    @ApiOperation(value = "社会餐分页查询", notes = "社会餐分页查询")
    @ResponseBody
    @PostMapping("/page")
    public PageVo<ClfSocialMeat> selectPage(@RequestBody PageVo page){


//        Page page=new Page<>(1,10);
//        page.setCondition(params);
        page = clfSocialMeatService.selectClfSocialMeatPage(page);
        return page;
    }

    @ApiOperation(value = "新增社会餐资源", notes = "新增社会餐资源")
    @PostMapping("/add")
    public CommonResultVo<Boolean> add(@RequestBody ClfSocialMeat clfSocialMeat){
        CommonResultVo<Boolean> resultVo=new CommonResultVo<>();
//        clfSocialMeat.setCreateUser();
//        clfSocialMeat.setUpdateUser();
        final boolean result = clfSocialMeatService.insert(clfSocialMeat);
        resultVo.setResult(result);
        return resultVo;
    }

    @ApiOperation(value = "修改社会餐资源", notes = "修改社会餐资源")
    @PostMapping("/update")
    public CommonResultVo<Boolean> update(@RequestBody ClfSocialMeat clfSocialMeat){
        CommonResultVo<Boolean> resultVo=new CommonResultVo<>();
        clfSocialMeat.setUpdateTime(new Date());
//        clfSocialMeat.setUpdateUser();
        final boolean result = clfSocialMeatService.updateById(clfSocialMeat);
        resultVo.setResult(result);
        return resultVo;
    }

    @ApiOperation(value = "查看社会餐资源详情", notes = "查看社会餐资源详情")
    @PostMapping("/form")
    public CommonResultVo<ClfSocialMeat> form(@RequestBody ClfSocialMeat clfSocialMeat){
        CommonResultVo<ClfSocialMeat> resultVo=new CommonResultVo<>();
        clfSocialMeat = clfSocialMeatService.selectById(clfSocialMeat.getId());
        resultVo.setResult(clfSocialMeat);
        return resultVo;
    }

    @ApiOperation(value = "删除社会餐资源", notes = "删除社会餐资源")
    @PostMapping("/del/{id}")
    public CommonResultVo<String> del(@PathVariable Integer id){
        CommonResultVo<String> resultVo=new CommonResultVo<>();
        final int row = clfSocialMeatService.logicDelById(id);
        return resultVo;
    }
}
