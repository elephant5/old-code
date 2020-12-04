package com.colourfulchina.mars.api.feign.fallback;

import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.mars.api.feign.RemotePayService;
import com.colourfulchina.mars.api.vo.PayInfoVo;
import com.colourfulchina.mars.api.vo.req.AggregatePayParamsReqVo;
import com.colourfulchina.mars.api.vo.res.PayParamsResVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class RemotePayServiceImpl implements RemotePayService {
    @Override
    public CommonResultVo<PayParamsResVo> getAggregatePayParams(AggregatePayParamsReqVo reqVo) {
        return null;
    }

    @Override
    public String success(PayInfoVo payInfo) {
        return null;
    }
}
