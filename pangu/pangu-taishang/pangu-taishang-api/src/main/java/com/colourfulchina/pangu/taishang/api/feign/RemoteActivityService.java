package com.colourfulchina.pangu.taishang.api.feign;

import com.colourfulchina.inf.base.constant.ServiceNameConstant;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.pangu.taishang.api.feign.fallback.RemoteActivityServiceFallbackImpl;
import com.colourfulchina.pangu.taishang.api.vo.ActivityReqVo;
import com.colourfulchina.pangu.taishang.api.vo.res.ActivityResVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = ServiceNameConstant.PANGU_TAISHANG_SERVICE, fallback = RemoteActivityServiceFallbackImpl.class)
public interface RemoteActivityService {

    @PostMapping("/activity/getActCouponConfig")
    public CommonResultVo<List<ActivityResVo>> getActCouponConfig(@RequestBody ActivityReqVo reqVo) throws Exception;

}
