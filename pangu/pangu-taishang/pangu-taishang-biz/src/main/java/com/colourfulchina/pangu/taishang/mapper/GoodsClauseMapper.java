package com.colourfulchina.pangu.taishang.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.colourfulchina.pangu.taishang.api.entity.GoodsClause;
import com.colourfulchina.pangu.taishang.api.vo.req.GoodsBaseVo;

import java.util.List;

public interface GoodsClauseMapper extends BaseMapper<GoodsClause> {

    List<GoodsClause> selectGoodsClauseById(Integer goodsId);
}
