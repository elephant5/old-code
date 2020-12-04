package com.colourfulchina.bigan.controller;

import com.colourfulchina.bigan.api.entity.CselfHelp;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.inf.base.vo.PageVo;
import com.colourfulchina.bigan.service.CselfHelpService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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
@RequestMapping("/selfHelp")
public class CselfHelpController {

    @Autowired
    CselfHelpService cselfHelpService;
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="pageNumber",name="pageNumber",dataType="Integer",required=true,value="当前页数",defaultValue="1"),
            @ApiImplicitParam(paramType="pageSize",name="pageSize",dataType="Integer",required=true,value="当页条数",defaultValue="100")
    })
    @ApiOperation(value = "c端项目_自助分页查询", notes = "c端项目_自助分页查询")
    @ResponseBody
    @PostMapping("/page")
    public PageVo selectPage(@RequestBody PageVo page){


//        Page page=new Page<>(1,10);
        page = cselfHelpService.selectCselfHelpPage(page);
        return page;
    }

    @ApiOperation(value = "新增c端项目_自助餐资源", notes = "新增c端项目_自助餐资源")
    @PostMapping("/add")
    public CommonResultVo<Boolean> add(@RequestBody CselfHelp cselfHelp){
        CommonResultVo<Boolean> resultVo=new CommonResultVo<>();
//        cselfHelp.setCreateUser();
//        cselfHelp.setUpdateUser();
        final boolean result = cselfHelpService.insert(cselfHelp);
        resultVo.setResult(result);
        return resultVo;
    }

    @ApiOperation(value = "修改c端项目_自助餐资源", notes = "修改c端项目_自助餐资源")
    @PostMapping("/update")
    public CommonResultVo<Boolean> update(@RequestBody CselfHelp cselfHelp){
        CommonResultVo<Boolean> resultVo=new CommonResultVo<>();
        cselfHelp.setUpdateTime(new Date());
//        cselfHelp.setUpdateUser();
        final boolean result = cselfHelpService.updateById(cselfHelp);
        resultVo.setResult(result);
        return resultVo;
    }

    @ApiOperation(value = "查看c端项目_自助餐资源详情", notes = "查看c端项目_自助餐资源详情")
    @PostMapping("/form")
    public CommonResultVo<CselfHelp> form(@RequestBody CselfHelp cselfHelp){
        CommonResultVo<CselfHelp> resultVo=new CommonResultVo<>();
        cselfHelp = cselfHelpService.selectById(cselfHelp.getId());
        resultVo.setResult(cselfHelp);
        return resultVo;
    }

    @ApiOperation(value = "删除c端项目_自助餐资源", notes = "删除c端项目_自助餐资源")
    @PostMapping("/del/{id}")
    public CommonResultVo<String> del(@PathVariable Integer id){
        CommonResultVo<String> resultVo=new CommonResultVo<>();
        final int row = cselfHelpService.logicDelById(id);
        return resultVo;
    }
}
