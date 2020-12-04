package com.colourfulchina.bigan.controller;

import com.colourfulchina.bigan.api.entity.ClfTea;
import com.colourfulchina.bigan.api.vo.ClfTeaExportVo;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.inf.base.vo.PageVo;
import com.colourfulchina.bigan.service.ClfTeaService;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiImplicitParam;
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
 * Date: 2018/8/5
 */
@Slf4j
@RestController
@RequestMapping("/tea")
public class ClfTeaController {

    @Autowired
    ClfTeaService clfTeaService;
//    @ApiImplicitParams({
//            @ApiImplicitParam(paramType="pageNumber",name="pageNumber",dataType="Integer",required=true,value="当前页数",defaultValue="1"),
//            @ApiImplicitParam(paramType="pageSize",name="pageSize",dataType="Integer",required=true,value="当页条数",defaultValue="100")
//    })
    @ApiOperation(value = "下午茶分页查询", notes = "下午茶分页查询")
    @ApiImplicitParam(name = "page", value = "Page详细实体Page", required = true, dataType = "Page")
    @ResponseBody
    @PostMapping("/page")
    public PageVo<ClfTea> selectPage(@RequestBody PageVo page){


        page = clfTeaService.selectClfTeaPage(page);
        return page;
    }

    @ApiOperation(value = "新增下午茶资源", notes = "新增下午茶资源")
    @PostMapping("/add")
    public CommonResultVo<Boolean> add(@RequestBody ClfTea clfTea){
        CommonResultVo<Boolean> resultVo=new CommonResultVo<>();
//        clfTea.setCreateUser();
//        clfTea.setUpdateUser();
        final boolean result = clfTeaService.insert(clfTea);
        resultVo.setResult(result);
        return resultVo;
    }

    @ApiOperation(value = "修改下午茶资源", notes = "修改下午茶资源")
    @PostMapping("/update")
    public CommonResultVo<Boolean> update(@RequestBody ClfTea clfTea){
        CommonResultVo<Boolean> resultVo=new CommonResultVo<>();
        clfTea.setUpdateTime(new Date());
//        clfTea.setUpdateUser();
        final boolean result = clfTeaService.updateById(clfTea);
        resultVo.setResult(result);
        return resultVo;
    }

    @ApiOperation(value = "查看下午茶资源详情", notes = "查看下午茶资源详情")
    @PostMapping("/form")
    public CommonResultVo<ClfTea> form(@RequestBody ClfTea clfTea){
        CommonResultVo<ClfTea> resultVo=new CommonResultVo<>();
        clfTea = clfTeaService.selectById(clfTea.getId());
        resultVo.setResult(clfTea);
        return resultVo;
    }

    @ApiOperation(value = "删除下午茶资源", notes = "删除下午茶资源")
    @PostMapping("/del/{id}")
    public CommonResultVo<String> del(@PathVariable Integer id){
        CommonResultVo<String> resultVo=new CommonResultVo<>();
        final int row = clfTeaService.logicDelById(id);
        return resultVo;
    }

    @PostMapping("/download")
    public CommonResultVo<PageVo> download(@RequestBody PageVo<ClfTea> pageVo){
        pageVo.setCurrent(1);
        pageVo.setSize(Integer.MAX_VALUE);
        CommonResultVo<PageVo> resultVo=new CommonResultVo<>();
        final PageVo<ClfTeaExportVo> result=new PageVo<>();
        final PageVo<ClfTea> pageResult = clfTeaService.selectClfTeaPage(pageVo);
        if (!CollectionUtils.isEmpty(pageResult.getRecords())){
            List<ClfTeaExportVo> newRecords= Lists.newArrayList();
            pageResult.getRecords().forEach(record->{
                ClfTeaExportVo clfTeaExportVo=new ClfTeaExportVo();
                BeanUtils.copyProperties(record,clfTeaExportVo);
                newRecords.add(clfTeaExportVo);
            });
            result.setRecords(newRecords);
        }
        resultVo.setResult(result);
        return resultVo;
    }
}
