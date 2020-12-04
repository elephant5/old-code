package com.colourfulchina.bigan.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.colourfulchina.bigan.api.entity.Caccom;

import java.util.List;
import java.util.Map;

/**
 * User: Ryan
 * Date: 2018/8/3
 */
public interface CaccomMapper extends BaseMapper<Caccom> {
    List<Caccom> selectListPage(Pagination page, Map map);
    int logicDelById(Integer id);
}
