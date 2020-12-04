package com.colourfulchina.bigan.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.bigan.api.entity.AccomList;
import com.colourfulchina.inf.base.vo.PageVo;
import com.colourfulchina.bigan.mapper.AccomListMapper;
import com.colourfulchina.bigan.service.AccomListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * User: Ryan
 * Date: 2018/8/3
 */
@Slf4j
@Service
public class AccomListServiceImpl extends ServiceImpl<AccomListMapper,AccomList> implements AccomListService {


    @Autowired
    private AccomListMapper accomListMapper;



    @Override
    public PageVo selectAccomListPage(PageVo page) {

        List<AccomList> list = accomListMapper.selectAccomListPage(page,page.getCondition());
        page.setRecords(list);
        return page;
    }

    @Override
    public int logicDelById(Integer id) {
        return accomListMapper.logicDelById(id);
    }

}
