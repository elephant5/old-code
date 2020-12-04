package com.colourfulchina.mars.api.feign.fallback;

import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.mars.api.entity.GiftCode;
import com.colourfulchina.mars.api.feign.RemoteGiftTableService;
import com.colourfulchina.mars.api.vo.req.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: RemoteGiftTableServiceImpl  
 * @Description: TODO(这里用一句话描述这个类的作用)   
 * @author Sunny  
 * @date 2018年10月8日  
 * @company Colourful@copyright(c) 2018
 * @version V1.0
 */
@Slf4j
@Component
public class RemoteGiftTableServiceImpl implements RemoteGiftTableService {
	
	@Override
	public CommonResultVo<List<GiftCode>> sendCode(SendCodeReq sendCodeReq) {
		log.error("fegin sendCode 接口异常");
		return null;
	}

	@Override
	public CommonResultVo<GiftCode> backendActive(ActiveActCodeReq activeActCodeReq) {
		log.error("fegin backendActive 接口异常");
		return null;
	}

	@Override
	public CommonResultVo<List<HashMap<String, Object>>> getActCodeList(HashMap<String, Object> map) {
		log.error("fegin getActCodeList 接口异常");
		return null;
	}

	@Override
	public CommonResultVo<Boolean> cancelCodeById(CancelCodeReq req) {
		log.error("fegin 作废激活码 接口异常");
		return null;
	}

	@Override
	public CommonResultVo<GiftCode> selectGiftCodeInfo(Integer giftCodeId) {
		log.error("fegin 获取激活码信息 接口异常");
		return null;
	}

	@Override
	public CommonResultVo<GiftCode> selectGiftCodeByActCode(String actCode) {
		return null;
	}

	@Override
	public CommonResultVo<List<GiftCode>> obsoleteActCodes(ObsoleteCodeReq obsoleteCodeReq) {
		return null;
	}

	@Override
	public CommonResultVo<List<GiftCode>> returnActCodes(ReturnCodeReq returnCodeReq) {
		return null;
	}

    @Override
    public boolean equityTimedTask() {
		log.error("fegin 获取无限期循环权益定时任务 接口异常");
        return false;
    }
	@Override
	public CommonResultVo<Boolean> optExpireGiftCode() {
		log.error("fegin定时批量过期激活码失败");
		return null;
	}

	@Override
	public CommonResultVo<List<GiftCode>> selectAlipayProLong(Map map) {
		log.error("fegin查询支付宝延期激活码失败");
		return null;
	}

	@Override
	public CommonResultVo<Boolean> obsoleteBoscActCodes() {
		log.error("fegin调用上海银行批量作废激活码失败");
		return null;
	}

	@Override
	public CommonResultVo<Boolean> frozenBoscActCodes(List<String> activeRemarks) {
		log.error("fegin调用冻结激活码失败");
		return null;
	}

	@Override
	public CommonResultVo<Boolean> thawBoscActCodes(List<String> activeRemarks) {
		log.error("fegin调用解冻激活码失败");
		return null;
	}

	@Override
	public CommonResultVo<Boolean> replaceBoscCodes(List<Map<String, String>> req) {
		log.error("fegin调用替换激活码失败");
		return null;
	}

}
