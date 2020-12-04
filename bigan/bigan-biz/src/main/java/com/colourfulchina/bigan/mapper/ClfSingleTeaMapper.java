package com.colourfulchina.bigan.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.colourfulchina.bigan.api.entity.ClfSingleTea;
import com.colourfulchina.inf.base.vo.PageVo;

import java.util.List;
import java.util.Map;

/**
 * User: Ryan
 * Date: 2018/8/6
 */
public interface ClfSingleTeaMapper   extends BaseMapper<ClfSingleTea> {

    List<ClfSingleTea> selectClfSingleTeaPage(PageVo page, Map condition);
    int logicDelById(Integer id);
}
