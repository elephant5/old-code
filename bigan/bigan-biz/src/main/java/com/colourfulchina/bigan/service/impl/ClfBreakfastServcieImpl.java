package com.colourfulchina.bigan.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.bigan.api.entity.ClfBreakfast;
import com.colourfulchina.inf.base.vo.PageVo;
import com.colourfulchina.bigan.mapper.ClfBreakfastMapper;
import com.colourfulchina.bigan.service.ClfBreakfastService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * User: Ryan
 * Date: 2018/8/5
 */
@Slf4j
@Service
public class ClfBreakfastServcieImpl extends ServiceImpl<ClfBreakfastMapper,ClfBreakfast> implements ClfBreakfastService {
    @Autowired
    private ClfBreakfastMapper clfBreakfastMapper;
    @Override
    public PageVo selectListPage(PageVo page) {
        return page.setRecords(clfBreakfastMapper.selectListPage(page,page.getCondition()));
    }

    @Override
    public int logicDelById(Integer id) {
        return clfBreakfastMapper.logicDelById(id);
    }
}
