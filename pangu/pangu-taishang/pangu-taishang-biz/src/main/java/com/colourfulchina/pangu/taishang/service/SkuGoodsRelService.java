package com.colourfulchina.pangu.taishang.service;


import com.colourfulchina.pangu.taishang.api.entity.SkuGoodsRel;
import com.colourfulchina.pangu.taishang.api.vo.req.SkuGoodsReqVo;

public interface SkuGoodsRelService {

    SkuGoodsRel getGoodsInfoBySku(SkuGoodsReqVo reqVo);
}
