package com.colourfulchina.bigan.controller;

import com.colourfulchina.bigan.api.entity.ClfSetMenu;
import com.colourfulchina.bigan.api.vo.ClfSetMenuExportVo;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.inf.base.vo.PageVo;
import com.colourfulchina.bigan.service.ClfSetMenuService;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * User: Ryan
 * Date: 2018/8/5
 */
@Slf4j
@RestController
@RequestMapping("/setMenu")
public class ClfSetMenuController {

    @Autowired
    ClfSetMenuService clfSetMenuService;
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="pageNumber",name="pageNumber",dataType="Integer",required=true,value="当前页数",defaultValue="1"),
            @ApiImplicitParam(paramType="pageSize",name="pageSize",dataType="Integer",required=true,value="当页条数",defaultValue="100")
    })
    @ApiOperation(value = "定制套餐资源分页查询", notes = "定制套餐资源分页查询")
    @ResponseBody
    @PostMapping("/page")
    public PageVo<ClfSetMenu> selectPage(@RequestBody PageVo page){
        page = clfSetMenuService.selectClfSetMenuPage(page);
        return page;
    }

    @ApiOperation(value = "新增定制套餐资源", notes = "新增定制套餐资源")
    @PostMapping("/add")
    public CommonResultVo<Boolean> add(@RequestBody ClfSetMenu clfSetMenu){
        CommonResultVo<Boolean> resultVo=new CommonResultVo<>();
//        clfSetMenu.setCreateUser();
//        clfSetMenu.setUpdateUser();
        final boolean result = clfSetMenuService.insert(clfSetMenu);
        resultVo.setResult(result);
        return resultVo;
    }

    @ApiOperation(value = "修改定制套餐资源", notes = "修改定制套餐资源")
    @PostMapping("/update")
    public CommonResultVo<Boolean> update(@RequestBody ClfSetMenu clfSetMenu){
        CommonResultVo<Boolean> resultVo=new CommonResultVo<>();
        clfSetMenu.setUpdateTime(new Date());
//        clfSetMenu.setUpdateUser();
        final boolean result = clfSetMenuService.updateById(clfSetMenu);
        resultVo.setResult(result);
        return resultVo;
    }

    @ApiOperation(value = "查看定制套餐资源详情", notes = "查看定制套餐资源详情")
    @PostMapping("/form")
    public CommonResultVo<ClfSetMenu> form(@RequestBody ClfSetMenu clfSetMenu){
        CommonResultVo<ClfSetMenu> resultVo=new CommonResultVo<>();
        clfSetMenu = clfSetMenuService.selectById(clfSetMenu.getId());
        resultVo.setResult(clfSetMenu);
        return resultVo;
    }

    @ApiOperation(value = "删除自助餐资源", notes = "删除自助餐资源")
    @PostMapping("/del/{id}")
    public CommonResultVo<String> del(@PathVariable Integer id){
        CommonResultVo<String> resultVo=new CommonResultVo<>();
        final int row = clfSetMenuService.logicDelById(id);
        return resultVo;
    }

    @PostMapping("/download")
    public CommonResultVo<PageVo> download(@RequestBody PageVo<ClfSetMenu> pageVo){
        pageVo.setCurrent(1);
        pageVo.setSize(Integer.MAX_VALUE);
        CommonResultVo<PageVo> resultVo=new CommonResultVo<>();
        final PageVo<ClfSetMenuExportVo> result=new PageVo<>();
        final PageVo<ClfSetMenu> pageResult = clfSetMenuService.selectClfSetMenuPage(pageVo);
        if (!CollectionUtils.isEmpty(pageResult.getRecords())){
            List<ClfSetMenuExportVo> newRecords= Lists.newArrayList();
            pageResult.getRecords().forEach(record->{
                ClfSetMenuExportVo clfSetMenuExportVo=new ClfSetMenuExportVo();
                BeanUtils.copyProperties(record,clfSetMenuExportVo);
                newRecords.add(clfSetMenuExportVo);
            });
            result.setRecords(newRecords);
        }
        resultVo.setResult(result);
        return resultVo;
    }
}
