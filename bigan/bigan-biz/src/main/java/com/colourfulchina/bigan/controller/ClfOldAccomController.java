package com.colourfulchina.bigan.controller;

import com.colourfulchina.bigan.api.entity.ClfOldBuffet;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.inf.base.vo.PageVo;
import com.colourfulchina.bigan.service.ClfOldBuffetService;
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
@RequestMapping("/oldAccom")
public class ClfOldAccomController {
    @Autowired
    ClfOldBuffetService oldBuffetService;
    @ApiOperation(value = "遨乐网与客乐芙_总住宿(旧)分页查询", notes = "遨乐网与客乐芙_总住宿(旧)分页查询")
    @PostMapping("/page")
    public PageVo<ClfOldBuffet> selectPage(@RequestBody PageVo page){


        page = oldBuffetService.selectListPage(page);
        return page;
    }

    @ApiOperation(value = "新增傲乐网与客乐芙_总住宿(旧)资源", notes = "新增傲乐网与客乐芙_总住宿(旧)资源")
    @PostMapping("/add")
    public CommonResultVo<Boolean> add(@RequestBody ClfOldBuffet clfOldBuffet){
        CommonResultVo<Boolean> resultVo=new CommonResultVo<>();
//        clfOldBuffet.setCreateUser();
//        clfOldBuffet.setUpdateUser();
        final boolean result = oldBuffetService.insert(clfOldBuffet);
        resultVo.setResult(result);
        return resultVo;
    }

    @ApiOperation(value = "修改傲乐网与客乐芙_总住宿(旧)资源", notes = "修改傲乐网与客乐芙_总住宿(旧)资源")
    @PostMapping("/update")
    public CommonResultVo<Boolean> update(@RequestBody ClfOldBuffet clfOldBuffet){
        CommonResultVo<Boolean> resultVo=new CommonResultVo<>();
        clfOldBuffet.setUpdateTime(new Date());
//        clfOldBuffet.setUpdateUser();
        final boolean result = oldBuffetService.updateById(clfOldBuffet);
        resultVo.setResult(result);
        return resultVo;
    }

    @ApiOperation(value = "查看傲乐网与客乐芙_总住宿(旧)资源详情", notes = "查看傲乐网与客乐芙_总住宿(旧)资源详情")
    @PostMapping("/form")
    public CommonResultVo<ClfOldBuffet> form(@RequestBody ClfOldBuffet clfOldBuffet){
        CommonResultVo<ClfOldBuffet> resultVo=new CommonResultVo<>();
        clfOldBuffet = oldBuffetService.selectById(clfOldBuffet.getId());
        resultVo.setResult(clfOldBuffet);
        return resultVo;
    }

    @ApiOperation(value = "删除傲乐网与客乐芙_总住宿(旧)资源", notes = "删除傲乐网与客乐芙_总住宿(旧)资源")
    @PostMapping("/del/{id}")
    public CommonResultVo<String> del(@PathVariable Integer id){
        CommonResultVo<String> resultVo=new CommonResultVo<>();
        final int row = oldBuffetService.logicDelById(id);
        return resultVo;
    }
}
