package com.colourfulchina.bigan.api.feign;

import com.colourfulchina.bigan.api.entity.SysGeo;
import com.colourfulchina.bigan.api.feign.fallback.RemoteGeoServiceFallbackImpl;
import com.colourfulchina.inf.base.constant.ServiceNameConstant;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = ServiceNameConstant.BIGAN_SERVICE, fallback = RemoteGeoServiceFallbackImpl.class)
public interface RemoteGeoService {
    /**
     * fegin查询sqlserver酒店定位列表
     * @return
     */
	@PostMapping("/sysGeo/selectGeoList")
    CommonResultVo<List<SysGeo>> selectGeoList();

    /**
     * fegin新增酒店定位信息
     * @param sysGeo
     * @return
     */
	@PostMapping("/sysGeo/remoteAddGeo")
	CommonResultVo<SysGeo> remoteAddGeo(@RequestBody SysGeo sysGeo);
    /**
     * fegin修改酒店定位信息
     * @param sysGeo
     * @return
     */
	@PostMapping("/sysGeo/remoteUpdGeo")
	CommonResultVo<SysGeo> remoteUpdGeo(@RequestBody SysGeo sysGeo);

}
