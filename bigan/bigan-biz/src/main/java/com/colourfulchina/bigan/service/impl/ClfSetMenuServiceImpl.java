package com.colourfulchina.bigan.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.bigan.api.entity.ClfSetMenu;
import com.colourfulchina.inf.base.vo.PageVo;
import com.colourfulchina.bigan.mapper.ClfSetMenuMapper;
import com.colourfulchina.bigan.service.ClfSetMenuService;
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
public class ClfSetMenuServiceImpl extends ServiceImpl<ClfSetMenuMapper,ClfSetMenu> implements ClfSetMenuService
{

    @Autowired
    ClfSetMenuMapper clfSetMenuMapper;

    @Override
    public PageVo selectClfSetMenuPage(PageVo page) {

        List<ClfSetMenu> result = clfSetMenuMapper.selectClfSetMenuPage(page,page.getCondition());
        page.setRecords(result);
        return page;
    }

    @Override
    public int logicDelById(Integer id) {
        return clfSetMenuMapper.logicDelById(id);
    }
}
