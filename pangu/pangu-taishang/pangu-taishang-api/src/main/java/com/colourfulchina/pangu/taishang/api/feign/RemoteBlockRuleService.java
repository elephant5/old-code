package com.colourfulchina.pangu.taishang.api.feign;

import com.colourfulchina.inf.base.constant.ServiceNameConstant;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.pangu.taishang.api.feign.fallback.RemoteBlockRuleServiceFallbackImpl;
import com.colourfulchina.pangu.taishang.api.vo.req.QueryBookBlockReq;
import com.colourfulchina.pangu.taishang.api.vo.res.QueryBookBlockRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = ServiceNameConstant.PANGU_TAISHANG_SERVICE, fallback = RemoteBlockRuleServiceFallbackImpl.class)
public interface RemoteBlockRuleService {

	/**
	 * 查询预约的block规则和可预约时间范围
	 * @param queryBookBlockReq
	 * @return
	 */
	@PostMapping("/blockRule/queryBookBlock")
	CommonResultVo<QueryBookBlockRes> queryBookBlock(@RequestBody QueryBookBlockReq queryBookBlockReq);

	/**
	 * 查询预约的block规则和可预约时间范围(已激活的码优化)
	 * @param queryBookBlockReq
	 * @return
	 */
	@PostMapping("/blockRule/queryBookBlockNew")
	CommonResultVo<QueryBookBlockRes> queryBookBlockNew(@RequestBody QueryBookBlockReq queryBookBlockReq);

	/**
	 * 查询不可预约时间字符串
	 * @param queryBookBlockReq
	 * @return
	 */
	@PostMapping("/blockRule/queryAllBlock")
	CommonResultVo<QueryBookBlockRes> queryAllBlock(@RequestBody QueryBookBlockReq queryBookBlockReq);

}
