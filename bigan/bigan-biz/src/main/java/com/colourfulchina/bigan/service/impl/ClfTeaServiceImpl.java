package com.colourfulchina.bigan.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.bigan.api.entity.ClfTea;
import com.colourfulchina.inf.base.vo.PageVo;
import com.colourfulchina.bigan.mapper.ClfTeaMapper;
import com.colourfulchina.bigan.service.ClfTeaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * User: Ryan
 * Date: 2018/8/5
 */
@Slf4j
@Service
public class ClfTeaServiceImpl  extends ServiceImpl<ClfTeaMapper,ClfTea> implements ClfTeaService {

    @Autowired
    ClfTeaMapper clfTeaMapper;
    @Override
    public PageVo selectClfTeaPage(PageVo page) {
        page.getCondition().put("current",page.getCurrent());
        page.getCondition().put("size",page.getSize());
        List<ClfTea> result  = clfTeaMapper.selectClfTeaPage(page,page.getCondition());
        page.setRecords(result);
        return page;
    }

    @Override
    public int logicDelById(Integer id) {
        return clfTeaMapper.logicDelById(id);
    }
}
