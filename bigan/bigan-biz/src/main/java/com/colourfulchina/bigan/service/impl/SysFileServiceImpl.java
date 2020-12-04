package com.colourfulchina.bigan.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.bigan.api.dto.ListSysFileReq;
import com.colourfulchina.bigan.api.dto.SysFileDto;
import com.colourfulchina.bigan.api.entity.SysFile;
import com.colourfulchina.bigan.mapper.SysFileMapper;
import com.colourfulchina.bigan.service.SysFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class SysFileServiceImpl extends ServiceImpl<SysFileMapper,SysFile> implements SysFileService {
    @Autowired
    private SysFileMapper sysFileMapper;

    @Override
    public List<SysFile> selectShopPic(Long shopId) {
        return sysFileMapper.selectShopPic(shopId);
    }

    @Override
    public List<SysFileDto> listSysFileDto(ListSysFileReq sysFileReq) {
        return sysFileMapper.listSysFileDto(sysFileReq);
    }
}
