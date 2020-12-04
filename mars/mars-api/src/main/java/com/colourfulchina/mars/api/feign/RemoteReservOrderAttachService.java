package com.colourfulchina.mars.api.feign;

import com.colourfulchina.inf.base.constant.ServiceNameConstant;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.mars.api.feign.fallback.RemoteReservOrderAttachServiceImpl;
import com.colourfulchina.mars.api.vo.req.NewReservOrderPlaceReq;
import com.colourfulchina.mars.api.vo.res.ReservOrderResVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(value = ServiceNameConstant.MARS_SERVICE, fallback = RemoteReservOrderAttachServiceImpl.class)
public interface RemoteReservOrderAttachService {

    //对外在线下预约单
    @PostMapping("/reservOrderAttach/openPlaceOrder")
    CommonResultVo<ReservOrderResVO> openPlaceOrder(@RequestBody NewReservOrderPlaceReq newreservOrderPlaceReq);
}