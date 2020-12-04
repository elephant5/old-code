package com.colourfulchina.bigan.service;

import com.baomidou.mybatisplus.service.IService;
import com.colourfulchina.bigan.api.entity.ClfSpa;
import com.colourfulchina.inf.base.vo.PageVo;

/**
 * User: Ryan
 * Date: 2018/8/5
 */
public interface ClfSpaService  extends IService<ClfSpa> {
    PageVo selectListPage(PageVo page);
    int logicDelById(Integer id);
}
