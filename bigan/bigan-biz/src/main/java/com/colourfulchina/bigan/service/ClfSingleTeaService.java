package com.colourfulchina.bigan.service;

import com.baomidou.mybatisplus.service.IService;
import com.colourfulchina.bigan.api.entity.ClfSingleTea;
import com.colourfulchina.inf.base.vo.PageVo;

/**
 * User: Ryan
 * Date: 2018/8/6
 */
public interface ClfSingleTeaService  extends IService<ClfSingleTea> {

    PageVo selectClfSingleTeaPage(PageVo page);
    int logicDelById(Integer id);
}
