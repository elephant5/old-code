package com.colourfulchina.bigan.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.bigan.api.entity.Caccom;
import com.colourfulchina.inf.base.vo.PageVo;
import com.colourfulchina.bigan.mapper.CaccomMapper;
import com.colourfulchina.bigan.service.CaccomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * User: Ryan
 * Date: 2018/8/3
 */
@Slf4j
@Service
public class CaccomServiceImpl  extends ServiceImpl<CaccomMapper,Caccom> implements CaccomService {
    @Autowired
    private CaccomMapper caccomMapper;

    @Override
    public PageVo selectListPage(PageVo page) {
        return page.setRecords(caccomMapper.selectListPage(page,page.getCondition()));
    }

    @Override
    public int logicDelById(Integer id) {
        return caccomMapper.logicDelById(id);
    }
}
