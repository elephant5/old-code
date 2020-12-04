package com.colourfulchina.pangu.taishang.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.colourfulchina.inf.base.vo.PageVo;
import com.colourfulchina.pangu.taishang.api.entity.SalesChannel;
import com.colourfulchina.pangu.taishang.api.vo.req.SalesChannelReqVo;

import java.util.List;
import java.util.Map;

/**
 * 渠道Mapper
 *
 * @Author: luck.luo
 * @Description:
 * @Date: 2019/2/25 17:38
 */
public interface SalesChannelMapper extends BaseMapper<SalesChannel> {
    List<SalesChannel> findPageList(PageVo<SalesChannelReqVo> pageVoReq, Map<String,Object> condition);

    Integer selectSalesChannelCount(SalesChannel salesChannel);

    List<Integer> selectSalesChannel(String bankId);

    SalesChannel selectSalesChannelOne(SalesChannel paramsSalesChannel);
}
