package com.colourfulchina.bigan.controller;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.colourfulchina.bigan.api.dto.ListSysFileReq;
import com.colourfulchina.bigan.api.dto.SysFileDto;
import com.colourfulchina.bigan.api.entity.SysFile;
import com.colourfulchina.bigan.api.entity.SysFileQuote;
import com.colourfulchina.bigan.service.SysFileQuoteService;
import com.colourfulchina.bigan.service.SysFileService;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.tianyan.common.security.util.SecurityUtils;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/sysFile")
@Slf4j
public class SysFileController {
    @Autowired
    private SysFileService sysFileService;

    @Autowired
    private SysFileQuoteService sysFileQuoteService;

    /**
     * 更新文件
     * @return
     */
    @PostMapping("/merge")
    public CommonResultVo<Boolean> merge(@RequestBody List<SysFileDto> fileDtoList){
        CommonResultVo<Boolean>  result = new CommonResultVo<>();
        try {
            Assert.notEmpty(fileDtoList,"参数为空");
            //1.查询原type obj_id对应的file_id,
            //2.删除file_id对应的sys_file
            //3.删除type obj_id对应的sys_file_quote
            //4.如果del_flag为0 则入库sys_file sys_file_quote
            Wrapper<SysFileQuote> wrapper=new Wrapper<SysFileQuote>() {
                @Override
                public String getSqlSegment() {
                    return "where type='"+fileDtoList.get(0).getType()+"' and obj_id="+fileDtoList.get(0).getObjId();
                }
            };
            final List<SysFileQuote> fileQuoteList = sysFileQuoteService.selectList(wrapper);
            log.info("merge fileQuoteList:{}",fileQuoteList);
            if (!CollectionUtils.isEmpty(fileQuoteList)){
                List<Integer> fileIds= Lists.newArrayList();
                fileQuoteList.forEach(fileQuote->{
                    fileIds.add(fileQuote.getFileId());
                });
                sysFileService.deleteBatchIds(fileIds);
                sysFileQuoteService.delete(wrapper);
            }
            for (int i=0;i<fileDtoList.size();i++){
                final SysFileDto sysFileDto = fileDtoList.get(i);
                if (sysFileDto.getDelFlag().compareTo(0)==0){
                    SysFile sysFile=new SysFile();
                    BeanUtils.copyProperties(sysFileDto,sysFile,"id","fileId");
                    sysFile.setCreatorId(232);
                    sysFileService.insert(sysFile);
                    SysFileQuote sysFileQuote=new SysFileQuote();
                    sysFileQuote.setSort(i);
                    sysFileQuote.setFileId(sysFile.getId());
                    sysFileQuoteService.insert(sysFileQuote);
                }
            }
            result.setResult(Boolean.TRUE);
        }catch (Exception e){
            log.error("更新文件失败",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
            result.setResult(Boolean.FALSE);
        }
        return result;
    }
    @PostMapping("/list")
    public CommonResultVo<List<SysFileDto>> list(@RequestBody ListSysFileReq sysFileReq){
        CommonResultVo<List<SysFileDto>> resultVo=new CommonResultVo<>();
        try {
            final List<SysFileDto> fileDtoList = sysFileService.listSysFileDto(sysFileReq);
            resultVo.setResult(fileDtoList);
        }catch (Exception e){
            log.error("文件列表查询失败",e);
            resultVo.setCode(200);
            resultVo.setMsg(e.getMessage());
        }
        return resultVo;
    }

    @GetMapping("/deleteFileByGuid/{fileId}")
    public CommonResultVo<Boolean> deleteFileByGuid(@PathVariable String guid){
        CommonResultVo<Boolean> resultVo=new CommonResultVo<>();
        try {
            Wrapper<SysFile> oneWrapper=new Wrapper<SysFile>() {
                @Override
                public String getSqlSegment() {
                    return "where guid='"+guid+"'";
                }
            };
            final SysFile sysFile = sysFileService.selectOne(oneWrapper);
            if (sysFile != null){
                Wrapper<SysFileQuote> delWrapper=new Wrapper<SysFileQuote>() {
                    @Override
                    public String getSqlSegment() {
                        return "where file_id="+sysFile.getId();
                    }
                };
                final boolean delete = sysFileQuoteService.delete(delWrapper);
                sysFileService.deleteById(sysFile.getId());
            }

        }catch (Exception e){
            resultVo.setCode(200);
            resultVo.setMsg(e.getMessage());
            log.error("删除文件失败",e);
        }
        return resultVo;
    }
}
