package com.colourfulchina.bigan.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.bigan.api.entity.ClfThirdList;
import com.colourfulchina.inf.base.vo.PageVo;
import com.colourfulchina.bigan.mapper.ClfThirdListMapper;
import com.colourfulchina.bigan.service.ClfThirdListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * User: Ryan
 * Date: 2018/8/5
 */
@Slf4j
@Service
public class ClfThirdListServiceImpl extends ServiceImpl<ClfThirdListMapper,ClfThirdList> implements ClfThirdListService {
    @Autowired
    private ClfThirdListMapper clfThirdListMapper;

    @Override
    public PageVo selectListPage(PageVo page) {
        return page.setRecords(clfThirdListMapper.selectListPage(page,page.getCondition()));
    }

    @Override
    public int logicDelById(Integer id) {
        return clfThirdListMapper.logicDelById(id);
    }
}
