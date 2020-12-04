package com.colourfulchina.bigan.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.colourfulchina.bigan.api.entity.ClfBuffet;
import com.colourfulchina.inf.base.vo.PageVo;

import java.util.List;
import java.util.Map;

/**
 * User: Ryan
 * Date: 2018/8/5
 */
public interface ClfBuffetMapper extends BaseMapper<ClfBuffet> {

    List<ClfBuffet> selectClfBuffetPage(PageVo page, Map condition);

    int logicDelById(Integer id);
}
