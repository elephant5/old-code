package com.colourfulchina.bigan.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.colourfulchina.bigan.api.dto.ListSysFileReq;
import com.colourfulchina.bigan.api.dto.SysFileDto;
import com.colourfulchina.bigan.api.entity.SysFile;

import java.util.List;

public interface SysFileMapper extends BaseMapper<SysFile> {
    List<SysFile> selectShopPic(Long shopId);

    List<SysFileDto> listSysFileDto(ListSysFileReq sysFileReq);
}