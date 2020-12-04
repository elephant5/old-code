package com.colourfulchina.bigan.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.bigan.api.entity.ClfSocialMeat;
import com.colourfulchina.inf.base.vo.PageVo;
import com.colourfulchina.bigan.mapper.ClfSocialMeatMapper;
import com.colourfulchina.bigan.service.ClfSocialMeatService;
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
public class ClfSocialMeatServiceImpl extends ServiceImpl<ClfSocialMeatMapper,ClfSocialMeat> implements ClfSocialMeatService
{

    @Autowired
    ClfSocialMeatMapper clfSocialMeatMapper;
    @Override
    public PageVo selectClfSocialMeatPage(PageVo page) {
        page.getCondition().put("current",page.getCurrent());
        page.getCondition().put("size",page.getSize());
        List<ClfSocialMeat> result  = clfSocialMeatMapper.selectClfSocialMeatPage(page.getCondition());
        page.setRecords(result);
        return page;
    }

    @Override
    public int logicDelById(Integer id) {
        return clfSocialMeatMapper.logicDelById(id);
    }
}
