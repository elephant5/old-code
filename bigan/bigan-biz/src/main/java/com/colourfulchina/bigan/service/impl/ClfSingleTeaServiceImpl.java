package com.colourfulchina.bigan.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.bigan.api.entity.ClfSingleTea;
import com.colourfulchina.inf.base.vo.PageVo;
import com.colourfulchina.bigan.mapper.ClfSingleTeaMapper;
import com.colourfulchina.bigan.service.ClfSingleTeaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * User: Ryan
 * Date: 2018/8/6
 */
@Slf4j
@Service
public class ClfSingleTeaServiceImpl extends ServiceImpl<ClfSingleTeaMapper,ClfSingleTea> implements ClfSingleTeaService {

    @Autowired
    ClfSingleTeaMapper clfSingleTeaMapper;

    @Override
    public PageVo selectClfSingleTeaPage(PageVo page) {
        page.getCondition().put("current", page.getCurrent());
        page.getCondition().put("size", page.getSize());
        List<ClfSingleTea> result = clfSingleTeaMapper.selectClfSingleTeaPage(page,page.getCondition());
        page.setRecords(result);
        return page;
    }

    @Override
    public int logicDelById(Integer id) {
        return clfSingleTeaMapper.logicDelById(id);
    }
}
