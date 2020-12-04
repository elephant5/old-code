package com.colourfulchina.bigan.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.bigan.api.entity.CselfHelp;
import com.colourfulchina.inf.base.vo.PageVo;
import com.colourfulchina.bigan.mapper.CselfHelpMapper;
import com.colourfulchina.bigan.service.CselfHelpService;
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
public class CselfHelpServiceImpl extends ServiceImpl<CselfHelpMapper,CselfHelp> implements CselfHelpService {


    @Autowired
    private CselfHelpMapper cselfHelpMapper;



    @Override
    public PageVo selectCselfHelpPage(PageVo page) {
        page.getCondition().put("current",page.getCurrent());
        page.getCondition().put("size",page.getSize());
        List<CselfHelp> result  = cselfHelpMapper.selectCselfHelpPage(page,page.getCondition());
        page.setRecords(result);
        return page;
    }

    @Override
    public int logicDelById(Integer id) {
        return cselfHelpMapper.logicDelById(id);
    }
}
