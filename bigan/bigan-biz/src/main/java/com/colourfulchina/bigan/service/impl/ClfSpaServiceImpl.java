package com.colourfulchina.bigan.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.bigan.api.entity.ClfSpa;
import com.colourfulchina.inf.base.vo.PageVo;
import com.colourfulchina.bigan.mapper.ClfSpaMapper;
import com.colourfulchina.bigan.service.ClfSpaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * User: Ryan
 * Date: 2018/8/5
 */
@Slf4j
@Service
public class ClfSpaServiceImpl  extends ServiceImpl<ClfSpaMapper,ClfSpa> implements ClfSpaService {
    @Autowired
    private ClfSpaMapper clfSpaMapper;
    @Override
    public PageVo selectListPage(PageVo page) {
        return page.setRecords(clfSpaMapper.selectListPage(page,page.getCondition()));
    }

    @Override
    public int logicDelById(Integer id) {
        return clfSpaMapper.logicDelById(id);
    }
}
