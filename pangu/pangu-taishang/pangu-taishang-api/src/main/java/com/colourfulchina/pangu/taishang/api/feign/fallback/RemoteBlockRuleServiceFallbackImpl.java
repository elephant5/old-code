package com.colourfulchina.pangu.taishang.api.feign.fallback;

import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.pangu.taishang.api.feign.RemoteBlockRuleService;
import com.colourfulchina.pangu.taishang.api.vo.req.QueryBookBlockReq;
import com.colourfulchina.pangu.taishang.api.vo.res.QueryBookBlockRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RemoteBlockRuleServiceFallbackImpl implements RemoteBlockRuleService {
	@Override
	public CommonResultVo<QueryBookBlockRes> queryBookBlock(QueryBookBlockReq queryBookBlockReq) {
		log.error("fegin查询预约的block规则和可预约时间范围失败");
		return null;
	}

	@Override
	public CommonResultVo<QueryBookBlockRes> queryBookBlockNew(QueryBookBlockReq queryBookBlockReq) {
		log.error("fegin查询预约的block规则和可预约时间范围优化失败");
		return null;
	}

	@Override
	public CommonResultVo<QueryBookBlockRes> queryAllBlock(QueryBookBlockReq queryBookBlockReq) {
		return null;
	}
}
