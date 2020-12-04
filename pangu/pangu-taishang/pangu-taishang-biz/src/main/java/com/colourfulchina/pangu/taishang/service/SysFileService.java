package com.colourfulchina.pangu.taishang.service;

import com.baomidou.mybatisplus.service.IService;
import com.colourfulchina.pangu.taishang.api.dto.SysFileDto;
import com.colourfulchina.pangu.taishang.api.entity.SysFile;
import com.colourfulchina.pangu.taishang.api.vo.req.ListSysFileReq;

import java.util.List;

public interface SysFileService extends IService<SysFile> {
    /**
     * 根据文件类型 及 objId查询文件信息
     * @param sysFileReq
     * @return
     */
    List<SysFileDto> listSysFileDto(ListSysFileReq sysFileReq);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    SysFileDto getSysFileDtoById(Integer id);

    void updateDelFlagById(SysFile sysFile);

    List<SysFileDto> merge(List<SysFileDto> fileDtoList);
    List<SysFileDto> listSysFileDtoByGuid(ListSysFileReq sysFileReq);
}