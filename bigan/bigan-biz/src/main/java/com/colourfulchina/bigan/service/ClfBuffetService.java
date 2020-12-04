package com.colourfulchina.bigan.service;

import com.baomidou.mybatisplus.service.IService;
import com.colourfulchina.bigan.api.entity.ClfBuffet;
import com.colourfulchina.inf.base.vo.PageVo;

/**
 * User: Ryan
 * Date: 2018/8/5
 */
public interface ClfBuffetService extends IService<ClfBuffet> {

    PageVo selectClfBuffetPage(PageVo page);
    int logicDelById(Integer id);
}
