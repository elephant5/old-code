package com.colourfulchina.pangu.taishang.service;

import com.colourfulchina.pangu.taishang.api.vo.res.bigan.*;

import java.util.List;


public interface ProjectService {

    //查询项目列表简略信息
    List<ProjectCdnVo> prjBriefList(String[] projectIds);

    //查询项目列表详情
    List<ProjectCdnVo> prjList(String[] projectIds);

    //查询商品详情
    GoodsDetailVo getGoodsDetail(Long pgpId)throws Exception;

    Project getProjectById(Integer id);

    //查询商品block信息
    List<GoodsBlockVo> queryGiftBlockList(BookOrderReqVo bookOrderReqVo);

    //支付宝产品预约时间范围及价格组装
    AlipayBookPriceVo prepareAlipayBookPrice(Integer productGroupProductId)throws Exception;
}
