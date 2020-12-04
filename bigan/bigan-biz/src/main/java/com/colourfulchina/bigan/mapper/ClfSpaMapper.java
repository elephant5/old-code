package com.colourfulchina.bigan.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.colourfulchina.bigan.api.entity.ClfSpa;
import com.colourfulchina.inf.base.vo.PageVo;

import java.util.List;
import java.util.Map;

/**
 * User: Ryan
 * Date: 2018/8/5
 */
public interface ClfSpaMapper   extends BaseMapper<ClfSpa> {
    List<ClfSpa> selectListPage(PageVo page, Map map);
    int logicDelById(Integer id);
}
