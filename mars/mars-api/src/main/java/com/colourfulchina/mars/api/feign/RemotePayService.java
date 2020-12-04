package com.colourfulchina.mars.api.feign;

import com.colourfulchina.inf.base.constant.ServiceNameConstant;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.mars.api.feign.fallback.RemoteBoscBankServiceImpl;
import com.colourfulchina.mars.api.feign.fallback.RemotePayServiceImpl;
import com.colourfulchina.mars.api.vo.PayInfoVo;
import com.colourfulchina.mars.api.vo.req.AggregatePayParamsReqVo;
import com.colourfulchina.mars.api.vo.res.PayParamsResVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = ServiceNameConstant.MARS_SERVICE,fallback = RemotePayServiceImpl.class)
public interface RemotePayService {

    @PostMapping("/pay/getAggregatePayParams")
    public CommonResultVo<PayParamsResVo> getAggregatePayParams(@RequestBody AggregatePayParamsReqVo reqVo);

    @PostMapping("/pay/payStatus/success")
    public String success(@RequestBody PayInfoVo payInfo) ;

}
