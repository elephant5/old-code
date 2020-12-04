package com.colourfulchina.bigan.service;

import com.baomidou.mybatisplus.service.IService;
import com.colourfulchina.bigan.api.entity.CselfHelp;
import com.colourfulchina.inf.base.vo.PageVo;

/**
 * User: Ryan
 * Date: 2018/8/3
 */
public interface CselfHelpService extends IService<CselfHelp> {

    PageVo selectCselfHelpPage(PageVo page);
    int logicDelById(Integer id);
}
