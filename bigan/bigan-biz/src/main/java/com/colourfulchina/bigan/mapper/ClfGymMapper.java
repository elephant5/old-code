package com.colourfulchina.bigan.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.colourfulchina.bigan.api.entity.ClfGym;
import com.colourfulchina.inf.base.vo.PageVo;

import java.util.List;
import java.util.Map;

/**
 * User: Ryan
 * Date: 2018/8/5
 */
public interface ClfGymMapper  extends BaseMapper<ClfGym> {
    List<ClfGym> selectListPage(PageVo page, Map map);
    int logicDelById(Integer id);
}
