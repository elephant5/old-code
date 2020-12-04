package com.colourfulchina.bigan.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.colourfulchina.bigan.api.entity.CselfHelp;
import com.colourfulchina.inf.base.vo.PageVo;

import java.util.List;
import java.util.Map;

/**
 * User: Ryan
 * Date: 2018/8/3
 */
public interface CselfHelpMapper extends BaseMapper<CselfHelp> {

    List<CselfHelp> selectCselfHelpPage(PageVo page, Map condition);
    int logicDelById(Integer id);
}
