package com.colourfulchina.mars.api.feign;

import com.colourfulchina.inf.base.constant.ServiceNameConstant;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.mars.api.entity.GiftCode;
import com.colourfulchina.mars.api.feign.fallback.RemoteGiftTableServiceImpl;
import com.colourfulchina.mars.api.vo.req.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: RemoteActivateCodeService  
 * @Description: TODO(这里用一句话描述这个类的作用)   
 * @author Sunny  
 * @date 2018年10月8日  
 * @company Colourful@copyright(c) 2018
 * @version V1.0
 */
@FeignClient(value = ServiceNameConstant.MARS_SERVICE,fallback = RemoteGiftTableServiceImpl.class)
public interface RemoteGiftTableService {

	@PostMapping("/giftCode/sendCode")
    CommonResultVo<List<GiftCode>> sendCode(@RequestBody SendCodeReq sendCodeReq);
	
	@PostMapping("/giftCode/backendActive")
	CommonResultVo<GiftCode> backendActive(@RequestBody ActiveActCodeReq activeActCodeReq);

	@PostMapping("/giftCode/codelist")
	CommonResultVo<List<HashMap<String, Object>>> getActCodeList(@RequestBody HashMap<String, Object> map);

	@PostMapping("/giftCode/cancelCodeById")
	public CommonResultVo<Boolean> cancelCodeById(@RequestBody CancelCodeReq req);

	@PostMapping("/giftCode/selectGiftCodeInfo")
	public CommonResultVo<GiftCode> selectGiftCodeInfo(@RequestBody Integer giftCodeId);

	@PostMapping("/giftCode/selectGiftCodeByActCode")
	public CommonResultVo<GiftCode> selectGiftCodeByActCode(@RequestBody String actCode);

	@PostMapping("/giftCode/obsoleteActCodes")
	public CommonResultVo<List<GiftCode>> obsoleteActCodes(@RequestBody ObsoleteCodeReq obsoleteCodeReq);
	
	@PostMapping("/giftCode/returnActCodes")
	CommonResultVo<List<GiftCode>> returnActCodes(@RequestBody ReturnCodeReq returnCodeReq);

	@PostMapping("/giftCode/equityTimedTask")
	public boolean equityTimedTask();
	/**
	 * 激活码过期操作
	 * @return
	 */
	@PostMapping("/giftCode/optExpireGiftCode")
	CommonResultVo<Boolean> optExpireGiftCode();

	/**
	 * 查询支付宝渠道延期的激活码
	 * @param map
	 * @return
	 */
	@PostMapping("/giftCode/selectAlipayProLong")
	CommonResultVo<List<GiftCode>> selectAlipayProLong(@RequestBody Map map);

	/**
	 * 上海银行批量作废激活码
	 * @return
	 */
	@GetMapping("/giftCode/obsoleteBoscActCodes")
	CommonResultVo<Boolean> obsoleteBoscActCodes();

	@PostMapping("/giftCode/frozenBoscActCodes")
	CommonResultVo<Boolean> frozenBoscActCodes(@RequestBody List<String> activeRemarks);

	@PostMapping("/giftCode/thawBoscActCodes")
	CommonResultVo<Boolean> thawBoscActCodes(@RequestBody List<String> activeRemarks);

	//替换上海银行激活码
	@PostMapping("/giftCode/replaceBoscCodes")
	CommonResultVo<Boolean> replaceBoscCodes(@RequestBody List<Map<String,String>> req);
}
