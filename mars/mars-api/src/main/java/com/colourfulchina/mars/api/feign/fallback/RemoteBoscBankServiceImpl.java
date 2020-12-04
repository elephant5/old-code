package com.colourfulchina.mars.api.feign.fallback;

import java.util.List;

import com.colourfulchina.mars.api.entity.BoscBank;
import com.colourfulchina.mars.api.vo.req.GetBoscBanksReqVo;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.mars.api.entity.BoscBankTxtEntity;
import com.colourfulchina.mars.api.feign.RemoteBoscBankService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Component
public class RemoteBoscBankServiceImpl implements RemoteBoscBankService {
	
	@Override
	public CommonResultVo<List<String>> getBoscBankTxtList(List<BoscBankTxtEntity> banklist) {
		log.error("getShBankTxtList error:{}", JSON.toJSONString(banklist));
		return null;
	}

	@Override
	public CommonResultVo<Integer> batchInsert(List<BoscBankTxtEntity> banklist) {
		log.error("batchInsert error:{}", JSON.toJSONString(banklist));
		return null;
	}

	@Override
	public CommonResultVo<List<BoscBank>> getBoscBankList() {
		return null;
	}

	@Override
	public CommonResultVo<List<BoscBank>> batchInsertBoscBank(List<BoscBank> list) {
		return null;
	}

	@Override
	public CommonResultVo<List<BoscBank>> getBoscBanksByCondition(GetBoscBanksReqVo reqVo) {
		return null;
	}

	@Override
	public CommonResultVo<Boolean> updateBatchBoscBanks(List<BoscBank> boscBankList) {
		return null;
	}

	@Override
	public CommonResultVo<Boolean> freezeBatchBoscBanks(List<BoscBank> boscBankLis,@RequestParam(value = "delFlag",required = true)Integer delFlag) {
		return null;
	}

}
