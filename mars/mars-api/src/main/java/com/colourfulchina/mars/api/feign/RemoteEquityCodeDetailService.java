package com.colourfulchina.mars.api.feign;

import com.colourfulchina.inf.base.constant.ServiceNameConstant;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.mars.api.entity.EquityCodeDetail;
import com.colourfulchina.mars.api.feign.fallback.RemoteEquityCodeDetailServiceImpl;
import com.colourfulchina.mars.api.feign.fallback.RemoteGiftTableServiceImpl;
import com.colourfulchina.mars.api.vo.req.CheckGiftCodeReq;
import com.colourfulchina.mars.api.vo.req.EquityCodeDetailReq;
import com.colourfulchina.mars.api.vo.res.GiftTimesVo;
import com.colourfulchina.pangu.taishang.api.vo.res.bigan.BookOrderReqVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = ServiceNameConstant.MARS_SERVICE,fallback = RemoteEquityCodeDetailServiceImpl.class)
public interface RemoteEquityCodeDetailService {


    @PostMapping("/equityCodeDetail/selectByEquityCode")
    public CommonResultVo<EquityCodeDetail> selectByEquityCode(@RequestBody EquityCodeDetailReq equityCodeDetailReq);

    @PostMapping("/equityCodeDetail/checkGiftCode")
    public CommonResultVo<Boolean> checkGiftCode(@RequestBody CheckGiftCodeReq checkGiftCodeReq);

    @PostMapping("/equityCodeDetail/selectEquityCodeDetailList")
    public CommonResultVo<List<EquityCodeDetail>> selectEquityCodeDetailListByGiftCodeId(@RequestBody CheckGiftCodeReq checkGiftCodeReq);

    @PostMapping("/equityCodeDetail/queryUnitGroups")
    public CommonResultVo<List<GiftTimesVo>> queryGiftTimesVo(@RequestBody BookOrderReqVo bookOrderReqVo);
}
