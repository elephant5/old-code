package com.colourfulchina.mars.service;

import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.mars.api.entity.ReservOrder;
import com.colourfulchina.mars.api.vo.ReservOrderVo;
import com.colourfulchina.nuwa.api.entity.SysSmsQueue;
import com.colourfulchina.nuwa.api.sms.model.SmsSendResult;
import com.colourfulchina.nuwa.api.vo.KltSendSmsRequestVo;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface NuwaInterfaceService  {

    /**
     * 短信接口
     * @param smsSendRequestVo
     * @return
     */
    CommonResultVo<SmsSendResult> send(KltSendSmsRequestVo smsSendRequestVo);

    /**
     * 预约单统一发送短信
     * @param reservOrder
     */
    void sendMsg(ReservOrder reservOrder);


    SmsSendResult sendMsgReservOrderVo(ReservOrderVo reservOrder);


    List<SysSmsQueue> querySms(ReservOrderVo reservOrder);
}
