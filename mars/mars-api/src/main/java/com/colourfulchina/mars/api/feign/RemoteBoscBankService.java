package com.colourfulchina.mars.api.feign;

import java.util.List;

import com.colourfulchina.mars.api.entity.BoscBank;
import com.colourfulchina.mars.api.vo.req.GetBoscBanksReqVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.colourfulchina.inf.base.constant.ServiceNameConstant;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.mars.api.entity.BoscBankTxtEntity;
import com.colourfulchina.mars.api.feign.fallback.RemoteBoscBankServiceImpl;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = ServiceNameConstant.MARS_SERVICE,fallback = RemoteBoscBankServiceImpl.class)
public interface RemoteBoscBankService {
	
	@PostMapping("/boscbank/list")
	CommonResultVo<List<String>> getBoscBankTxtList(@RequestBody List<BoscBankTxtEntity> banklist);

	@PostMapping("/boscbank/insert")
	CommonResultVo<Integer> batchInsert(@RequestBody List<BoscBankTxtEntity> banklist);

	@GetMapping("/boscbank/getBoscBankList")
	CommonResultVo<List<BoscBank>> getBoscBankList();

	@PostMapping("/boscbank/batchInsertBoscBank")
	CommonResultVo<List<BoscBank>> batchInsertBoscBank(@RequestBody List<BoscBank> list);

	@PostMapping("/boscbank/getBoscBanks")
	CommonResultVo<List<BoscBank>> getBoscBanksByCondition(@RequestBody GetBoscBanksReqVo reqVo);

	@PostMapping("/boscbank/updateBatchBoscBanks")
	CommonResultVo<Boolean> updateBatchBoscBanks(@RequestBody List<BoscBank> boscBankList);

	@PostMapping("/boscbank/freezeBatchBoscBanks")
	CommonResultVo<Boolean> freezeBatchBoscBanks(@RequestBody List<BoscBank> boscBankList,@RequestParam(value = "delFlag",required = true)Integer delFlag);
}
