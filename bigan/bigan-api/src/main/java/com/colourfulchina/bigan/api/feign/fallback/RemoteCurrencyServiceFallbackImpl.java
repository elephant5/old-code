package com.colourfulchina.bigan.api.feign.fallback;

import com.colourfulchina.bigan.api.entity.SysCurrency;
import com.colourfulchina.bigan.api.feign.RemoteCurrencyService;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class RemoteCurrencyServiceFallbackImpl implements RemoteCurrencyService {
	@Override
	public CommonResultVo<List<SysCurrency>> selectCurrencyList() {
		log.info("fegin查询货币列表失败");
		return null;
	}
}
