package com.colourfulchina.bigan.controller;

import com.colourfulchina.bigan.api.entity.AccomList;
import com.colourfulchina.bigan.api.vo.AccomListExportVo;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.inf.base.vo.PageVo;
import com.colourfulchina.bigan.service.AccomListService;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * User: Ryan
 * Date: 2018/8/5
 * bu
 */
@Slf4j
@RestController
@RequestMapping("/accom")
public class AccomController {

    @Autowired
    AccomListService accomListService;

    @ApiOperation(value = "住宿列表分页查询", notes = "住宿列表分页查询")
    @PostMapping("/page")
    public PageVo selectPage(@RequestBody PageVo page){

    page = accomListService.selectAccomListPage(page);
    return page;
    }

    @ApiOperation(value = "新增住宿资源", notes = "新增住宿资源")
    @PostMapping("/add")
    public CommonResultVo<Boolean> add(@RequestBody AccomList accomList){
        CommonResultVo<Boolean> resultVo=new CommonResultVo<>();
//        accomList.setCreateUser();
//        accomList.setUpdateUser();
        final boolean result = accomListService.insert(accomList);
        resultVo.setResult(result);
        return resultVo;
    }

    @ApiOperation(value = "修改住宿资源", notes = "修改住宿资源")
    @PostMapping("/update")
    public CommonResultVo<Boolean> update(@RequestBody AccomList accomList){
        CommonResultVo<Boolean> resultVo=new CommonResultVo<>();
        accomList.setUpdateTime(new Date());
//        accomList.setUpdateUser();
        final boolean result = accomListService.updateById(accomList);
        resultVo.setResult(result);
        return resultVo;
    }

    @ApiOperation(value = "查看住宿资源详情", notes = "查看住宿资源详情")
    @PostMapping("/form")
    public CommonResultVo<AccomList> form(@RequestBody AccomList accomList){
        CommonResultVo<AccomList> resultVo=new CommonResultVo<>();
        accomList = accomListService.selectById(accomList.getId());
        resultVo.setResult(accomList);
        return resultVo;
    }
    @ApiOperation(value = "删除住宿资源", notes = "删除住宿资源")
    @PostMapping("/del/{id}")
    public CommonResultVo<String> del(@PathVariable Integer id){
        CommonResultVo<String> resultVo=new CommonResultVo<>();
        final int row = accomListService.logicDelById(id);
        return resultVo;
    }

    @PostMapping("/download")
    public CommonResultVo<PageVo> download(@RequestBody PageVo<AccomList> pageVo){
        pageVo.setCurrent(1);
        pageVo.setSize(Integer.MAX_VALUE);
        CommonResultVo<PageVo> resultVo=new CommonResultVo<>();
        final PageVo<AccomListExportVo> result=new PageVo<>();
        final PageVo<AccomList> pageResult = accomListService.selectAccomListPage(pageVo);
        if (!CollectionUtils.isEmpty(pageResult.getRecords())){
            List<AccomListExportVo> newRecords= Lists.newArrayList();
            pageResult.getRecords().forEach(record->{
                AccomListExportVo accomListExportVo=new AccomListExportVo();
                BeanUtils.copyProperties(record,accomListExportVo);
                newRecords.add(accomListExportVo);
            });
            result.setRecords(newRecords);
        }
        resultVo.setResult(result);
        return resultVo;
    }

}
