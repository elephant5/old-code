package com.colourfulchina.bigan.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.bigan.api.entity.ClfOldBuffet;
import com.colourfulchina.inf.base.vo.PageVo;
import com.colourfulchina.bigan.mapper.ClfOldBuffetMapper;
import com.colourfulchina.bigan.service.ClfOldBuffetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * User: Ryan
 * Date: 2018/8/5
 */
@Slf4j
@Service
public class ClfOldBuffetServiceImpl extends ServiceImpl<ClfOldBuffetMapper,ClfOldBuffet> implements ClfOldBuffetService {
    @Autowired
    private ClfOldBuffetMapper clfOldBuffetMapper;
    @Override
    public PageVo selectListPage(PageVo page) {
        return page.setRecords(clfOldBuffetMapper.selectListPage(page,page.getCondition()));
    }

    @Override
    public int logicDelById(Integer id) {
        return clfOldBuffetMapper.logicDelById(id);
    }
}
