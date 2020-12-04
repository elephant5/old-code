package com.colourfulchina.bigan.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.colourfulchina.bigan.api.entity.ClfSetMenu;
import com.colourfulchina.inf.base.vo.PageVo;

import java.util.List;
import java.util.Map;

/**
 * User: Ryan
 * Date: 2018/8/5
 */
public interface ClfSetMenuMapper  extends BaseMapper<ClfSetMenu> {

    List<ClfSetMenu> selectClfSetMenuPage(PageVo page, Map map);

    int logicDelById(Integer id);

}
