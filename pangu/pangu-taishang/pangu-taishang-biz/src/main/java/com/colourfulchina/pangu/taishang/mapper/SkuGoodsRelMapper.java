package com.colourfulchina.pangu.taishang.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.colourfulchina.pangu.taishang.api.entity.SkuGoodsRel;
import com.colourfulchina.pangu.taishang.api.vo.req.SkuGoodsReqVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SkuGoodsRelMapper extends BaseMapper<SkuGoodsRel> {

    SkuGoodsRel selectGoodsInfoBySku(SkuGoodsReqVo reqVo);
}
