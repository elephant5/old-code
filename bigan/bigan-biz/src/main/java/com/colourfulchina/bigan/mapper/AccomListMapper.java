package com.colourfulchina.bigan.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.colourfulchina.bigan.api.entity.AccomList;
import com.colourfulchina.inf.base.vo.PageVo;

import java.util.List;
import java.util.Map;

/**
 * User: Ryan
 * Date: 2018/8/3
 */
public interface AccomListMapper extends BaseMapper<AccomList> {

    List<AccomList> selectAccomListPage(PageVo page, Map map);
    int logicDelById(Integer id);
}
