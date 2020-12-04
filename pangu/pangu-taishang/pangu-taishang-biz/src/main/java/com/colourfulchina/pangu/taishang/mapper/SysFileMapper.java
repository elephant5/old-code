package com.colourfulchina.pangu.taishang.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.colourfulchina.pangu.taishang.api.dto.SysFileDto;
import com.colourfulchina.pangu.taishang.api.entity.SysFile;
import com.colourfulchina.pangu.taishang.api.vo.req.ListSysFileReq;

import java.util.List;

public interface SysFileMapper extends BaseMapper<SysFile> {
    List<SysFileDto> listSysFileDto(ListSysFileReq sysFileReq);
    SysFileDto getSysFileDtoById(Integer id);
    void updateDelFlagById(SysFile sysFile);
    List<SysFileDto> listSysFileDtoByGuid(ListSysFileReq sysFileReq);
    List<SysFile> selectShopPic(Long shopId);
}