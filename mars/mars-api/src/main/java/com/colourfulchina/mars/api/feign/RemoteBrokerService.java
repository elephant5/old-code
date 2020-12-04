package com.colourfulchina.mars.api.feign;

import com.colourfulchina.inf.base.constant.ServiceNameConstant;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.mars.api.feign.fallback.RemoteBrokerServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = ServiceNameConstant.MARS_SERVICE, fallback = RemoteBrokerServiceImpl.class)
public interface RemoteBrokerService {

    @PostMapping("/broker/batchUpload")
    CommonResultVo<String> batchUpload();
}
