package com.colourfulchina.mars.api.feign.fallback;

import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.mars.api.entity.EquityCodeDetail;
import com.colourfulchina.mars.api.feign.RemoteEquityCodeDetailService;
import com.colourfulchina.mars.api.vo.req.CheckGiftCodeReq;
import com.colourfulchina.mars.api.vo.req.EquityCodeDetailReq;
import com.colourfulchina.mars.api.vo.res.GiftTimesVo;
import com.colourfulchina.pangu.taishang.api.vo.res.bigan.BookOrderReqVo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class RemoteEquityCodeDetailServiceImpl implements RemoteEquityCodeDetailService {

    @Override
    public CommonResultVo<EquityCodeDetail> selectByEquityCode(EquityCodeDetailReq equityCodeDetailReq) {
        return null;
    }

    @Override
    public CommonResultVo<Boolean> checkGiftCode(CheckGiftCodeReq checkGiftCodeReq) {
        log.error("查询权益是否使用接口异常");
        return null;
    }

    @Override
    public CommonResultVo<List<EquityCodeDetail>> selectEquityCodeDetailListByGiftCodeId(CheckGiftCodeReq checkGiftCodeReq) {
        return null;
    }

    @Override
    public CommonResultVo<List<GiftTimesVo>> queryGiftTimesVo(BookOrderReqVo bookOrderReqVo) {
        log.error("查询权益次数接口异常");
        return null;
    }
}
