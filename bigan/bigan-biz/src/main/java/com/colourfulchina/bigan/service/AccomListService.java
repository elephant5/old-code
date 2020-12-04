package com.colourfulchina.bigan.service;

import com.baomidou.mybatisplus.service.IService;
import com.colourfulchina.bigan.api.entity.AccomList;
import com.colourfulchina.inf.base.vo.PageVo;

/**
 * User: Ryan
 * Date: 2018/8/3
 */
public interface AccomListService extends IService<AccomList> {
    PageVo selectAccomListPage(PageVo page);
    int logicDelById(Integer id);
}
