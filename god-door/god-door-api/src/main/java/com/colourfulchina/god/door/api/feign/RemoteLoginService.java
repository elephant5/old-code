package com.colourfulchina.god.door.api.feign;

import com.colourfulchina.god.door.api.feign.fallback.RemoteLoginServiceFallbackImpl;
import com.colourfulchina.inf.base.constant.ServiceNameConstant;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value =  ServiceNameConstant.GOD_DOOR_SERVICE, fallback = RemoteLoginServiceFallbackImpl.class)
public interface RemoteLoginService {
}
