package com.colourfulchina.mars.api.feign.fallback;

import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.mars.api.feign.RemoteReservOrderAttachService;
import com.colourfulchina.mars.api.vo.req.NewReservOrderPlaceReq;
import com.colourfulchina.mars.api.vo.res.ReservOrderResVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RemoteReservOrderAttachServiceImpl implements RemoteReservOrderAttachService {
    @Override
    public CommonResultVo<ReservOrderResVO> openPlaceOrder(NewReservOrderPlaceReq newReservOrderPlaceReq) {
        return null;
    }
}
