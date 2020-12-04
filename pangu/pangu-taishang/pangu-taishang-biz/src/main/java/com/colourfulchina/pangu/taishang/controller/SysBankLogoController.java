package com.colourfulchina.pangu.taishang.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.colourfulchina.bigan.api.entity.SysOption;
import com.colourfulchina.bigan.api.feign.RemoteSysOptionService;
import com.colourfulchina.god.door.api.annotation.SysGodDoorLog;
import com.colourfulchina.god.door.api.util.SecurityUtils;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.pangu.taishang.api.dto.SysBiganLogoDto;
import com.colourfulchina.pangu.taishang.api.dto.SysFileDto;
import com.colourfulchina.pangu.taishang.api.entity.SysBankLogo;
import com.colourfulchina.pangu.taishang.api.vo.SysBankLogoDto;
import com.colourfulchina.pangu.taishang.service.SysBankLogoService;
import com.colourfulchina.pangu.taishang.service.SysFileQuoteService;
import com.colourfulchina.pangu.taishang.service.SysFileService;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/sysBankLogo")
@Api(tags = {"银行Logo接口"})
public class SysBankLogoController {
    @Resource
    private SysBankLogoService sysBankLogoService;
    @Autowired
    private SysFileService sysFileService;
    @Autowired
    SysFileQuoteService sysFileQuoteService;

    private final RemoteSysOptionService remoteSysOptionService;
    
    /**
     *
     * @param sysBankLogo
     * @return
     */
    @SysGodDoorLog("查询Logo列表")
    @ApiOperation("查询Logo列表")
    @PostMapping("/list")
    public CommonResultVo<List<SysBankLogo>> list(@RequestBody SysBankLogo sysBankLogo){
        CommonResultVo<List<SysBankLogo>> resultVo=new CommonResultVo<>();
        try {
            Wrapper<SysBankLogo> configWrapper=new Wrapper<SysBankLogo>() {
                @Override
                public String getSqlSegment() {
                    if (sysBankLogo == null){
                        return null;
                    }
                    StringBuffer sql=new StringBuffer("where 1=1");
                    if (sysBankLogo.getFileId() != null){
                        sql.append(" and ").append("file_id = ").append(sysBankLogo.getFileId());
                    }
                    if (StringUtils.isNotBlank(sysBankLogo.getName())){
                        sql.append(" and ").append("name = '").append(sysBankLogo.getName()).append("'");
                    }
                    if (sysBankLogo.getBankId() != null){
                        sql.append(" and ").append("bank_id = ").append(sysBankLogo.getBankId());
                    }
                    if (sysBankLogo.getDelFlag() != null){
                        sql.append(" and ").append("del_flag = ").append(sysBankLogo.getDelFlag());
                    }
                    return sql.toString();
                }
            };
            final List<SysBankLogo> sysBankLogoList = sysBankLogoService.selectList(configWrapper);
            resultVo.setResult(sysBankLogoList);
        }catch (Exception e){
            log.error("查询Logo列表失败",e);
            resultVo.setCode(200);
            resultVo.setMsg(e.getMessage());
        }

        return resultVo;
    }

    @SysGodDoorLog("查询Logo列表")
    @ApiOperation("查询Logo列表")
    @PostMapping("/listWithPic")
    public CommonResultVo<List<SysBankLogoDto>> listWithPic(@RequestBody SysBankLogo sysBankLogo){
        CommonResultVo<List<SysBankLogoDto>> resultVo=new CommonResultVo<>();
        try {
            List<SysBankLogoDto> result=Lists.newArrayList();
            Wrapper<SysBankLogo> configWrapper=new Wrapper<SysBankLogo>() {
                @Override
                public String getSqlSegment() {
                    if (sysBankLogo == null){
                        return null;
                    }
                    StringBuffer sql=new StringBuffer("where 1=1");
                    if (sysBankLogo.getFileId() != null){
                        sql.append(" and ").append("file_id = ").append(sysBankLogo.getFileId());
                    }
                    if (StringUtils.isNotBlank(sysBankLogo.getName())){
                        sql.append(" and ").append("name = '").append(sysBankLogo.getName()).append("'");
                    }
                    if (sysBankLogo.getBankId() != null){
                        sql.append(" and ").append("bank_id = ").append(sysBankLogo.getBankId());
                    }
                    if (sysBankLogo.getDelFlag() != null){
                        sql.append(" and ").append("del_flag = ").append(sysBankLogo.getDelFlag());
                    }
                    return sql.toString();
                }
            };
            final List<SysBankLogo> sysBankLogoList = sysBankLogoService.selectList(configWrapper);
            if (!CollectionUtils.isEmpty(sysBankLogoList)){

                sysBankLogoList.forEach(bankLogo->{
                    SysBankLogoDto sysBankLogoDto =new SysBankLogoDto();
                    BeanUtils.copyProperties(bankLogo, sysBankLogoDto);
                    final SysFileDto sysFileDto = sysFileService.getSysFileDtoById(bankLogo.getFileId());
                    sysBankLogoDto.setSysFileDto(sysFileDto);
                    result.add(sysBankLogoDto);
                });
                resultVo.setResult(result);
            }
        }catch (Exception e){
            log.error("查询Logo列表失败",e);
            resultVo.setCode(200);
            resultVo.setMsg(e.getMessage());
        }

        return resultVo;
    }

    @SysGodDoorLog("根据id查询Logo")
    @ApiOperation("根据id查询Logo")
    @GetMapping("/get/{id}")
    @Cacheable(value = "SysBankLogo",key = "'get_'+#id",unless = "#result == null")
    public CommonResultVo<SysBankLogoDto> get(@PathVariable Integer id){
        CommonResultVo<SysBankLogoDto> resultVo=new CommonResultVo<>();
        try {
            final SysBankLogo sysBankLogo = sysBankLogoService.selectById(id);
            if (sysBankLogo != null){
                SysBankLogoDto sysBankLogoDto = new SysBankLogoDto();
                BeanUtils.copyProperties(sysBankLogo,sysBankLogoDto);
                final SysFileDto sysFileDto = sysFileService.getSysFileDtoById(sysBankLogo.getFileId());
                sysBankLogoDto.setSysFileDto(sysFileDto);
                resultVo.setResult(sysBankLogoDto);
            }
        }catch (Exception e){
            log.error("查询Logo失败",e);
            resultVo.setCode(200);
            resultVo.setMsg(e.getMessage());
        }
        return resultVo;
    }

    @SysGodDoorLog("更新Logo")
    @ApiOperation("更新Logo")
    @PostMapping("/update")
    @CacheEvict(value = {"SysBankLogo","Goods","GoodsPortalSetting"},allEntries = true)
    public CommonResultVo<SysBankLogo> update(@RequestBody SysBankLogo sysBankLogo){
        CommonResultVo<SysBankLogo> resultVo=new CommonResultVo<>();
        try {
            Assert.notNull(sysBankLogo,"参数不能为空");
            Assert.notNull(sysBankLogo.getId(),"参数id不能为空");
            Assert.notNull(sysBankLogo.getFileId(),"参数fileId不能为空");
            sysBankLogo.setUpdateUser(SecurityUtils.getLoginName());
            sysBankLogo.setUpdateTime(new Date());
            final boolean updateById = sysBankLogoService.updateById(sysBankLogo);
            if (!updateById){
                throw new Exception("更新Logo失败");
            }
            resultVo.setResult(sysBankLogo);
        }catch (Exception e){
            log.error("更新Logo失败",e);
            resultVo.setCode(200);
            resultVo.setMsg(e.getMessage());
        }
        return resultVo;
    }

    @SysGodDoorLog("添加Logo")
    @ApiOperation("添加Logo")
    @PostMapping("/add")
    @CacheEvict(value = {"SysBankLogo","Goods","GoodsPortalSetting"},allEntries = true)
    public CommonResultVo<SysBankLogoDto> add(@RequestBody SysBankLogoDto sysBankLogoDto){
        CommonResultVo<SysBankLogoDto> resultVo=new CommonResultVo<>();
        try {
            //1.保存基础数据
            Assert.notNull(sysBankLogoDto,"参数不能为空");
            final SysFileDto fileDto = sysBankLogoDto.getSysFileDto();
            Assert.notNull(fileDto,"参数文件不能为空");
            Assert.hasText(sysBankLogoDto.getName(),"参数name不能为空");
            SysBankLogo sysBankLogo=new SysBankLogo();
            BeanUtils.copyProperties(sysBankLogoDto,sysBankLogo);
            sysBankLogo.setCreateUser(SecurityUtils.getLoginName());
            sysBankLogo.setCreateTime(new Date());
            sysBankLogo.setUpdateUser(SecurityUtils.getLoginName());
            sysBankLogo.setUpdateTime(new Date());
            final boolean insert = sysBankLogoService.insert(sysBankLogo);
            Assert.isTrue(insert,"添加Logo失败");
            fileDto.setObjId(sysBankLogo.getId());
            //保存文件数据
            final List<SysFileDto> merge = sysFileService.merge(Lists.newArrayList(fileDto));
            Assert.notEmpty(merge,"文件保存失败");
            //3.更新file_id
            final SysFileDto sysFileDto = merge.get(0);
            sysBankLogo.setFileId(sysFileDto.getId());
            final boolean update = sysBankLogoService.updateById(sysBankLogo);
            Assert.isTrue(update,"保存Logo失败");
            SysBankLogoDto bankLogoDto=new SysBankLogoDto();
            BeanUtils.copyProperties(sysBankLogo,bankLogoDto);
            bankLogoDto.setSysFileDto(sysFileDto);
            resultVo.setResult(bankLogoDto);
        }catch (Exception e){
            log.error("添加Logo失败",e);
            resultVo.setCode(200);
            resultVo.setMsg(e.getMessage());
        }
        return resultVo;
    }
    /**
     * 初始化
     * @param request
     * @return
     */
    @SysGodDoorLog("初始化logos")
    @GetMapping("/init")
    public CommonResultVo<String> init(String username, String password, HttpServletRequest request){
        CommonResultVo<String> resultVo=new CommonResultVo<>();
        try {
            Assert.hasText(username,"username不能为空");
            Assert.hasText(password,"password不能为空");
            Assert.isTrue("hyper.huang".equals(username),"username有误");
            Assert.isTrue("1qaz@WSX".equals(password),"password有误");
            final CommonResultVo<SysOption> optionCommonResultVo = remoteSysOptionService.get("h5.logo.list");
            Assert.notNull(optionCommonResultVo,"remoteSysOptionService.get 为空");
            final SysOption sysOption = optionCommonResultVo.getResult();
            Assert.notNull(sysOption,"remoteSysOptionService.get result为空");
            final String optionValue = sysOption.getValue();
            Assert.hasText(optionValue,"remoteSysOptionService.get optionValue为空");
            final List<SysBiganLogoDto> biganLogoDtoList = JSON.parseArray(optionValue, SysBiganLogoDto.class);
            Assert.notEmpty(biganLogoDtoList,"biganLogoDtoList为空");
            String path="svg/logo/";
            for (SysBiganLogoDto biganLogoDto:biganLogoDtoList){
                SysBankLogo sysBankLogo=new SysBankLogo();
                sysBankLogo.setName(biganLogoDto.getName());
                sysBankLogo.setCreateUser(SecurityUtils.getLoginName());
                sysBankLogo.setCreateTime(new Date());
                sysBankLogo.setUpdateUser(SecurityUtils.getLoginName());
                sysBankLogo.setUpdateTime(new Date());
                final boolean insert = sysBankLogoService.insert(sysBankLogo);
                Assert.isTrue(insert,"添加Logo失败");
                SysFileDto fileDto=new SysFileDto();
                fileDto.setObjId(sysBankLogo.getId());
                final String logoCode = biganLogoDto.getCode();
                String code=logoCode.substring(0,logoCode.lastIndexOf(".svg"));
                if (!logoCode.endsWith(".svg")){
                    code+="-white";
                }
                fileDto.setGuid(path+code);
                fileDto.setPath(path);
                fileDto.setCode(code);
                fileDto.setExt("svg");
                fileDto.setName(biganLogoDto.getName());
                fileDto.setType("bank.logo");
                fileDto.setSort(0);
                //保存文件数据
                final List<SysFileDto> merge = sysFileService.merge(Lists.newArrayList(fileDto));
                Assert.notEmpty(merge,"文件保存失败");
                //3.更新file_id
                final SysFileDto sysFileDto = merge.get(0);
                sysBankLogo.setFileId(sysFileDto.getId());
                final boolean update = sysBankLogoService.updateById(sysBankLogo);
                Assert.isTrue(update,"保存Logo失败");
            }

        }catch (Exception e){
            log.error("sys bank logo init error",e);
        }
        return resultVo;
    }
}
