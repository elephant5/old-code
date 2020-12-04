package com.colourfulchina.bigan.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.colourfulchina.bigan.api.entity.ClfSocialMeat;

import java.util.List;
import java.util.Map;

/**
 * User: Ryan
 * Date: 2018/8/5
 */
public interface ClfSocialMeatMapper   extends BaseMapper<ClfSocialMeat> {

    List<ClfSocialMeat> selectClfSocialMeatPage(Map map);
    int logicDelById(Integer id);
}
