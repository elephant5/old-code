package com.colourfulchina.mars.controller;

import com.colourfulchina.colourfulCoupon.api.entity.CpnThirdCode;
import com.colourfulchina.colourfulCoupon.api.enums.ThirdSourceEnum;
import com.colourfulchina.god.door.api.annotation.SysGodDoorLog;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.mars.api.vo.req.CouponThirdCodeReqVO;
import com.colourfulchina.mars.api.vo.req.ThirdCouponsProductInfoReqVO;
import com.colourfulchina.mars.api.vo.res.ThirdCouponsProductInfoResVO;
import com.colourfulchina.mars.service.CouponsService;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/coupons")
@Slf4j
public class CouponsController {

	@Autowired
	private CouponsService couponsService;

	@SysGodDoorLog("用户领取第三方券")
	@ApiOperation("用户领取第三方券")
	@PostMapping(value = "/receiveCoupons")
	public CommonResultVo<CpnThirdCode> getBoscCardList(@RequestBody CouponThirdCodeReqVO reqVO){
		CommonResultVo<CpnThirdCode> common = new CommonResultVo<CpnThirdCode>();
		try {
			CpnThirdCode couponThirdCode = couponsService.putThirdCoupons(reqVO);
			common.setCode(100);
			common.setMsg("成功");
			common.setResult(couponThirdCode);
		} catch (Exception e) {
			log.error(e.getMessage());
			common.setCode(200);
			common.setMsg(e.getMessage());
		}
		return common;
	}

	@SysGodDoorLog("查询第三方上架的可售卖的商品信息")
	@ApiOperation("查询第三方上架的可售卖的商品信息")
	@PostMapping(value = "/getThirdProductInfo")
	public CommonResultVo<List<ThirdCouponsProductInfoResVO>> getBoscCardList(@RequestBody ThirdCouponsProductInfoReqVO reqVO) throws Exception {
		if(null == reqVO || StringUtils.isEmpty(reqVO.getSource())){
			throw new Exception("渠道不能为空");
		}
		CommonResultVo<List<ThirdCouponsProductInfoResVO>> common = new CommonResultVo<List<ThirdCouponsProductInfoResVO>>();
		List<ThirdCouponsProductInfoResVO> list = Lists.newArrayList();
		try {
			if(ThirdSourceEnum.YSKQ.getCode().equalsIgnoreCase(reqVO.getSource())){
				//银商渠道
				list = couponsService.getThirdProductInfo();
			}
			common.setCode(100);
			common.setMsg("成功");
			common.setResult(list);
		} catch (Exception e) {
			log.error(e.getMessage());
			common.setCode(200);
			common.setMsg(e.getMessage());
		}
		return common;
	}


}
