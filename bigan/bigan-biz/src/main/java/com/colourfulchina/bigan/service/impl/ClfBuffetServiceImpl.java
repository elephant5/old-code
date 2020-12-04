package com.colourfulchina.bigan.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.bigan.api.entity.ClfBuffet;
import com.colourfulchina.inf.base.vo.PageVo;
import com.colourfulchina.bigan.mapper.ClfBuffetMapper;
import com.colourfulchina.bigan.service.ClfBuffetService;
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
public class ClfBuffetServiceImpl extends ServiceImpl<ClfBuffetMapper,ClfBuffet> implements ClfBuffetService {

    @Autowired
    ClfBuffetMapper clfBuffetMapper;

    @Override
    public PageVo selectClfBuffetPage(PageVo page) {
        page.getCondition().put("current", page.getCurrent());
        page.getCondition().put("size", page.getSize());
        List<ClfBuffet> result = clfBuffetMapper.selectClfBuffetPage(page,page.getCondition());
        page.setRecords(result);
        return page;
    }

    @Override
    public int logicDelById(Integer id) {
        return clfBuffetMapper.logicDelById(id);
    }
}
