package com.colourfulchina.bigan.controller;

import com.colourfulchina.bigan.api.entity.ClfSingleTea;
import com.colourfulchina.bigan.api.vo.ClfSingleTeaExportVo;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.inf.base.vo.PageVo;
import com.colourfulchina.bigan.service.ClfSingleTeaService;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * User: Ryan
 * Date: 2018/8/6
 */
@Slf4j
@RestController
@RequestMapping("/singleTea")
public class ClfSingleTeaController {

    @Autowired
    ClfSingleTeaService clfSingleTeaService;
//    @ApiImplicitParams({
//            @ApiImplicitParam(paramType="pageNumber",name="pageNumber",dataType="Integer",required=true,value="当前页数",defaultValue="1"),
//            @ApiImplicitParam(paramType="pageSize",name="pageSize",dataType="Integer",required=true,value="当页条数",defaultValue="100")
//    })
    @ApiOperation(value = "单杯茶饮分页查询", notes = "单杯茶饮分页查询")
    @ResponseBody
    @PostMapping("/page")
    public PageVo<ClfSingleTea> selectPage(@RequestBody PageVo page){


        page = clfSingleTeaService.selectClfSingleTeaPage(page);
        return page;
    }

    @ApiOperation(value = "新增单杯茶资源", notes = "新增单杯茶资源")
    @PostMapping("/add")
    public CommonResultVo<Boolean> add(@RequestBody ClfSingleTea clfSingleTea){
        CommonResultVo<Boolean> resultVo=new CommonResultVo<>();
//        clfSingleTea.setCreateUser();
//        clfSingleTea.setUpdateUser();
        final boolean result = clfSingleTeaService.insert(clfSingleTea);
        resultVo.setResult(result);
        return resultVo;
    }

    @ApiOperation(value = "修改单杯茶资源", notes = "修改单杯茶资源")
    @PostMapping("/update")
    public CommonResultVo<Boolean> update(@RequestBody ClfSingleTea clfSingleTea){
        CommonResultVo<Boolean> resultVo=new CommonResultVo<>();
        clfSingleTea.setUpdateTime(new Date());
//        clfSingleTea.setUpdateUser();
        final boolean result = clfSingleTeaService.updateById(clfSingleTea);
        resultVo.setResult(result);
        return resultVo;
    }

    @ApiOperation(value = "查看单杯茶资源详情", notes = "查看单杯茶资源详情")
    @PostMapping("/form")
    public CommonResultVo<ClfSingleTea> form(@RequestBody ClfSingleTea clfSingleTea){
        CommonResultVo<ClfSingleTea> resultVo=new CommonResultVo<>();
        clfSingleTea = clfSingleTeaService.selectById(clfSingleTea.getId());
        resultVo.setResult(clfSingleTea);
        return resultVo;
    }

    @ApiOperation(value = "删除单杯茶资源", notes = "删除单杯茶资源")
    @PostMapping("/del/{id}")
    public CommonResultVo<String> del(@PathVariable Integer id){
        CommonResultVo<String> resultVo=new CommonResultVo<>();
        final int row = clfSingleTeaService.logicDelById(id);
        return resultVo;
    }

    @PostMapping("/download")
    public CommonResultVo<PageVo> download(@RequestBody PageVo<ClfSingleTea> pageVo){
        pageVo.setCurrent(1);
        pageVo.setSize(Integer.MAX_VALUE);
        CommonResultVo<PageVo> resultVo=new CommonResultVo<>();
        final PageVo<ClfSingleTeaExportVo> result=new PageVo<>();
        final PageVo<ClfSingleTea> pageResult = clfSingleTeaService.selectClfSingleTeaPage(pageVo);
        if (!CollectionUtils.isEmpty(pageResult.getRecords())){
            List<ClfSingleTeaExportVo> newRecords= Lists.newArrayList();
            pageResult.getRecords().forEach(record->{
                ClfSingleTeaExportVo clfSingleTeaExportVo=new ClfSingleTeaExportVo();
                BeanUtils.copyProperties(record,clfSingleTeaExportVo);
                newRecords.add(clfSingleTeaExportVo);
            });
            result.setRecords(newRecords);
        }
        resultVo.setResult(result);
        return resultVo;
    }
}
