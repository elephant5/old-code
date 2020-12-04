package com.colourfulchina.pangu.taishang.service.impl;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.god.door.api.util.SecurityUtils;
import com.colourfulchina.pangu.taishang.api.dto.SysFileDto;
import com.colourfulchina.pangu.taishang.api.entity.SysFile;
import com.colourfulchina.pangu.taishang.api.entity.SysFileQuote;
import com.colourfulchina.pangu.taishang.api.vo.req.ListSysFileReq;
import com.colourfulchina.pangu.taishang.config.FtpProperties;
import com.colourfulchina.pangu.taishang.mapper.SysFileMapper;
import com.colourfulchina.pangu.taishang.mapper.SysFileQuoteMapper;
import com.colourfulchina.pangu.taishang.service.SysFileService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class SysFileServiceImpl extends ServiceImpl<SysFileMapper,SysFile> implements SysFileService {
    @Autowired
    SysFileMapper sysFileMapper;
    @Autowired
    SysFileQuoteMapper sysFileQuoteMapper;
    @Autowired
    FtpProperties ftpProperties;
    @Override
    public List<SysFileDto> listSysFileDto(ListSysFileReq sysFileReq) {
        List<SysFileDto> list = sysFileMapper.listSysFileDto(sysFileReq);
        if (!CollectionUtils.isEmpty(list)){
            list.forEach(file->{
                file.setPgCdnUrl(ftpProperties.getPgCdnUrl());
                file.setErpCdnUrl(ftpProperties.getErpCdnUrl());
            });
        }
        return list;
    }

    @Override
    public SysFileDto getSysFileDtoById(Integer id) {
        final SysFileDto sysFileDto = sysFileMapper.getSysFileDtoById(id);
        if (sysFileDto != null){
            sysFileDto.setPgCdnUrl(ftpProperties.getPgCdnUrl());
            sysFileDto.setErpCdnUrl(ftpProperties.getErpCdnUrl());
        }
        return sysFileDto;
    }

    @Override
    public void updateDelFlagById(SysFile sysFile) {
        sysFileMapper.updateDelFlagById(sysFile);
    }

    @Override
    public List<SysFileDto> merge(List<SysFileDto> fileDtoList) {
        //id存在 则更新  id 不存在 则新增
        if(!CollectionUtils.isEmpty(fileDtoList)){
            for (int i=0;i<fileDtoList.size();i++){
                final SysFileDto sysFileDto = fileDtoList.get(i);
                SysFile sysFile=new SysFile();
                BeanUtils.copyProperties(sysFileDto,sysFile);

                if (sysFile.getId() == null){
                    sysFile.setCreateTime(new Date());
                    sysFile.setUpdateTime(new Date());
                    sysFile.setCreateUser(SecurityUtils.getLoginName());
                    sysFile.setUpdateUser(SecurityUtils.getLoginName());
                    sysFileMapper.insert(sysFile);
                    sysFileDto.setId(sysFile.getId());
                    SysFileQuote sysFileQuote=new SysFileQuote();
                    sysFileQuote.setType(sysFileDto.getType());
                    sysFileQuote.setObjId(sysFileDto.getObjId());
                    sysFileQuote.setFileId(sysFile.getId());
                    sysFileQuote.setSort(i);
                    sysFileQuote.setCreateTime(new Date());
                    sysFileQuote.setUpdateTime(new Date());
                    sysFileQuote.setCreateUser(SecurityUtils.getLoginName());
                    sysFileQuote.setUpdateUser(SecurityUtils.getLoginName());
                    sysFileQuoteMapper.insert(sysFileQuote);
                }else {
                    sysFile.setUpdateTime(new Date());
                    sysFile.setUpdateUser(SecurityUtils.getLoginName());
                    sysFileMapper.updateById(sysFile);

                    SysFileQuote fileQuote=new SysFileQuote();
                    fileQuote.setDelFlag(0);
                    fileQuote.setType(sysFileDto.getType());
                    fileQuote.setObjId(sysFileDto.getObjId());
                    fileQuote.setFileId(sysFileDto.getId());
                    final SysFileQuote sysFileQuote = sysFileQuoteMapper.selectOne(fileQuote);
                    if (sysFileQuote != null){
                        sysFileQuote.setDelFlag(sysFileDto.getDelFlag());
                        sysFileQuote.setSort(i);
                        sysFileQuote.setUpdateTime(new Date());
                        sysFileQuote.setUpdateUser(SecurityUtils.getLoginName());
                        sysFileQuoteMapper.updateById(sysFileQuote);
                    }
                }
            }
        }
        return fileDtoList;
    }

    @Override
    public List<SysFileDto> listSysFileDtoByGuid(ListSysFileReq sysFileReq) {
        final List<SysFileDto> list = sysFileMapper.listSysFileDtoByGuid(sysFileReq);
        if (!CollectionUtils.isEmpty(list)){
            list.forEach(file->{
                file.setPgCdnUrl(ftpProperties.getPgCdnUrl());
                file.setErpCdnUrl(ftpProperties.getErpCdnUrl());
            });
        }
        return list;
    }
}