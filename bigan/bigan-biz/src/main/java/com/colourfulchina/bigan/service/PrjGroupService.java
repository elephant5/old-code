package com.colourfulchina.bigan.service;

import com.baomidou.mybatisplus.service.IService;
import com.colourfulchina.bigan.api.entity.PrjGroup;
import com.colourfulchina.bigan.api.vo.PrjGroupVo;

import java.util.List;

public interface PrjGroupService extends IService<PrjGroup> {
    List<PrjGroupVo> selectPrjGroupList(PrjGroupVo prjGroupVo);
}
