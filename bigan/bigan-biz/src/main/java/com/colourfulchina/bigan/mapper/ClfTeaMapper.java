package com.colourfulchina.bigan.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.colourfulchina.bigan.api.entity.ClfTea;
import com.colourfulchina.inf.base.vo.PageVo;

import java.util.List;
import java.util.Map;

/**
 * User: Ryan
 * Date: 2018/8/5
 */
public interface ClfTeaMapper   extends BaseMapper<ClfTea> {

    List<ClfTea> selectClfTeaPage(PageVo page, Map condition);
    int logicDelById(Integer id);
}
