package com.colourfulchina.bigan.controller;

import com.colourfulchina.bigan.api.entity.ClfBuffet;
import com.colourfulchina.bigan.api.vo.ClfBuffetExportVo;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.inf.base.vo.PageVo;
import com.colourfulchina.bigan.service.ClfBuffetService;
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
 * Date: 2018/8/5
 */
@Slf4j
@RestController
@RequestMapping("/clfBuffet")
public class ClfBuffetController {

    @Autowired
    ClfBuffetService clfBuffetService;
//    @ApiImplicitParams({
//            @ApiImplicitParam(paramType="pageNumber",name="pageNumber",dataType="Integer",required=true,value="当前页数",defaultValue="1"),
//            @ApiImplicitParam(paramType="pageSize",name="pageSize",dataType="Integer",required=true,value="当页条数",defaultValue="100")
//    })
    @ApiOperation(value = "自助餐资源分页查询", notes = "自助餐资源分页查询")
    @ResponseBody
    @PostMapping("/page")
    public PageVo<ClfBuffet> selectPage(@RequestBody PageVo page){


        page = clfBuffetService.selectClfBuffetPage(page);
        return page;
    }

    @ApiOperation(value = "自助餐资源修改", notes = "自助餐资源修改")
    @ResponseBody
    @PostMapping("/update")
    public CommonResultVo<Boolean> update(@RequestBody ClfBuffet clfBuffet){
        CommonResultVo<Boolean> resultVo=new CommonResultVo<>();
        clfBuffet.setUpdateTime(new Date());
//        clfBuffet.setUpdateUser();
        Boolean result = clfBuffetService.updateById(clfBuffet);
        resultVo.setResult(result);
        return resultVo;
    }

    @ApiOperation(value = "自助餐资源新增", notes = "自助餐资源新增")
    @ResponseBody
    @PostMapping("/add")
    public CommonResultVo<Boolean> add(@RequestBody ClfBuffet clfBuffet){
        CommonResultVo<Boolean> resultVo=new CommonResultVo<>();
//        clfBuffet.setCreateUser();
//        clfBuffet.setUpdateUser();
        Boolean result = clfBuffetService.insert(clfBuffet);
        resultVo.setResult(result);
        return resultVo;
    }

    @ApiOperation(value = "查看自助餐资源详情", notes = "查看自助餐资源详情")
    @PostMapping("/form")
    public CommonResultVo<ClfBuffet> form(@RequestBody ClfBuffet clfBuffet){
        CommonResultVo<ClfBuffet> resultVo=new CommonResultVo<>();
        clfBuffet = clfBuffetService.selectById(clfBuffet.getId());
        resultVo.setResult(clfBuffet);
        return resultVo;
    }

    @ApiOperation(value = "删除自助餐资源", notes = "删除自助餐资源")
    @PostMapping("/del/{id}")
    public CommonResultVo<String> del(@PathVariable Integer id){
        CommonResultVo<String> resultVo=new CommonResultVo<>();
        final int row = clfBuffetService.logicDelById(id);
        return resultVo;
    }

    @PostMapping("/download")
    public CommonResultVo<PageVo> download(@RequestBody PageVo<ClfBuffet> pageVo){
        pageVo.setCurrent(1);
        pageVo.setSize(Integer.MAX_VALUE);
        CommonResultVo<PageVo> resultVo=new CommonResultVo<>();
        final PageVo<ClfBuffetExportVo> result=new PageVo<>();
        final PageVo<ClfBuffet> pageResult = clfBuffetService.selectClfBuffetPage(pageVo);
        if (!CollectionUtils.isEmpty(pageResult.getRecords())){
            List<ClfBuffetExportVo> newRecords= Lists.newArrayList();
            pageResult.getRecords().forEach(record->{
                ClfBuffetExportVo clfBuffetExportVo=new ClfBuffetExportVo();
                BeanUtils.copyProperties(record,clfBuffetExportVo);
                newRecords.add(clfBuffetExportVo);
            });
            result.setRecords(newRecords);
        }
        resultVo.setResult(result);
        return resultVo;
    }
}
