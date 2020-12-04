package com.colourfulchina.bigan.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.bigan.api.entity.PrjGroup;
import com.colourfulchina.bigan.api.vo.PrjGroupVo;
import com.colourfulchina.bigan.mapper.PrjGroupMapper;
import com.colourfulchina.bigan.service.PrjGroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
public class PrjGroupServiceImpl extends ServiceImpl<PrjGroupMapper,PrjGroup> implements PrjGroupService {
    @Autowired
    private PrjGroupMapper prjGroupMapper;

    @Override
    public List<PrjGroupVo> selectPrjGroupList(PrjGroupVo prjGroupVo) {
        return prjGroupMapper.selectPrjGroupList(prjGroupVo);
    }
}
