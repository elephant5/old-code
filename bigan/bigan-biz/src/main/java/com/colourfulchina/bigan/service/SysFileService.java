package com.colourfulchina.bigan.service;

import com.baomidou.mybatisplus.service.IService;
import com.colourfulchina.bigan.api.dto.ListSysFileReq;
import com.colourfulchina.bigan.api.dto.SysFileDto;
import com.colourfulchina.bigan.api.entity.SysFile;

import java.util.List;

public interface SysFileService extends IService<SysFile> {

    List<SysFile> selectShopPic(Long shopId);
    /**
     * 根据文件类型 及 objId查询文件信息
     * @return
     */
    List<SysFileDto> listSysFileDto(ListSysFileReq sysFileReq);
}
