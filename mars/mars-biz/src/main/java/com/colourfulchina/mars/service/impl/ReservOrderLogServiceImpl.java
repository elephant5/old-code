package com.colourfulchina.mars.service.impl;

import cn.hutool.core.lang.Assert;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.god.door.api.util.SecurityUtils;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.mars.api.annotation.ReservOrderOperLog;
import com.colourfulchina.mars.api.constant.OrderConstant;
import com.colourfulchina.mars.api.entity.ReservOrderLog;
import com.colourfulchina.mars.api.enums.ExpressStatusEnum;
import com.colourfulchina.mars.api.enums.ReservOrderStatusEnums;
import com.colourfulchina.mars.api.vo.ReservOrderVo;
import com.colourfulchina.mars.mapper.ReservOrderLogMapper;
import com.colourfulchina.mars.mapper.ReservOrderMapper;
import com.colourfulchina.mars.service.NuwaInterfaceService;
import com.colourfulchina.mars.service.ReservOrderLogService;
import com.colourfulchina.nuwa.api.entity.SysSmsQueue;
import com.colourfulchina.nuwa.api.sms.enums.SmsStateEnums;
import com.colourfulchina.pangu.taishang.api.entity.Hotel;
import com.colourfulchina.pangu.taishang.api.entity.ShopChannel;
import com.colourfulchina.pangu.taishang.api.feign.RemoteShopService;
import com.colourfulchina.tianyan.admin.api.entity.SysDict;
import com.colourfulchina.tianyan.admin.api.feign.RemoteDictService;
import com.colourfulchina.tianyan.common.core.util.R;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

/**
 * @Author: luck.luo
 * @Description:
 * @Date: 2019/7/31 18:33
 */
@Slf4j
@Service
@AllArgsConstructor
public class ReservOrderLogServiceImpl extends ServiceImpl<ReservOrderLogMapper, ReservOrderLog> implements ReservOrderLogService {
    @Autowired
    private ReservOrderLogMapper reservOrderLogMapper;
    @Autowired
    private ReservOrderMapper reservOrderMapper;
    @Autowired
    private NuwaInterfaceService nuwaInterfaceService;
    private RemoteShopService remoteShopService;
    private RemoteDictService remoteDictService;


    @Override
    public void insertLog(ProceedingJoinPoint point, ReservOrderOperLog reservOrderOperLog,JSONObject result) {
        Object[] args = point.getArgs();
        log.info("请求参数:{}",JSONObject.toJSONString(args));
        ReservOrderLog reservOrderLog = new ReservOrderLog();
        log.info("get user ....");
        String user = SecurityUtils.getLoginName();
//        String user = "testuser";
        log.info("user:",user);
        reservOrderLog.setCreateUser(user);
        reservOrderLog.setOperType(OrderConstant.SYS_AUTO_LOG);
        String type = reservOrderOperLog.value();
        String content="";
        ReservOrderVo reservOrderVo=null;
        switch (type) {
            case OrderConstant.RESERV_IN_PROGRESS:
                log.info("处理中。。。");
                content = OrderConstant.RESERV_IN_PROGRESS_LOG_TEMPL;
                content = content.replaceAll("【user】", user==null?"":user);
                if (args != null) {
                    Integer orderId = (Integer) args[0];
                    reservOrderLog.setOrderId(orderId);
                    reservOrderVo = reservOrderMapper.selectReservOrderById(orderId);
                    reservOrderLog.setOldProseStatus(reservOrderVo.getProseStatus());
                    reservOrderLog.setNowProseStatus(ReservOrderStatusEnums.ReservOrderStatus.process.getcode());
                }
                break;
            case OrderConstant.RESERV_SUCCESS:
                log.info("处理成功。。。");
                if (args != null) {
                    ReservOrderVo param = (ReservOrderVo) args[0];
                    Integer orderId = param.getId();
                    reservOrderLog.setOrderId(orderId);
                     reservOrderVo = reservOrderMapper.selectReservOrderById(orderId);
                    reservOrderLog.setOldProseStatus(reservOrderVo.getProseStatus());
                    reservOrderLog.setNowProseStatus(result.getString("proseStatus"));
                    String serviceType = reservOrderVo.getServiceType();
                    if (serviceType.equalsIgnoreCase("accom")) {
                        content = OrderConstant.RESERV_SUCCESS_ACCOM_LOG_TEMPL;
                        content = content.replaceAll("【reservNumber】", param.getReservNumber())
                                .replaceAll("【giftName】", reservOrderVo.getGiftName())
                                .replaceAll("【giftDate】", reservOrderVo.getGiftDate());
                    }else if(serviceType.equalsIgnoreCase("buffet")||serviceType.equalsIgnoreCase("tea")||serviceType.equalsIgnoreCase("drink")||serviceType.equalsIgnoreCase("gym")||serviceType.equalsIgnoreCase("spa")||serviceType.equalsIgnoreCase("setmenu")){
                        content = OrderConstant.RESERV_SUCCESS_BUFFET_LOG_TEMPL;
                        content = content.replaceAll("【varCode】", result.get("varCode").toString());
                    }else if(serviceType.equalsIgnoreCase("lounge")||serviceType.equalsIgnoreCase("trip_cpn")){
                        content = OrderConstant.RESERV_SUCCESS_VIP_LOG_TEMPL;
                        content = content.replaceAll("【giftName】", reservOrderVo.getGiftName())
                                //预定号
                                .replaceAll("【reservNumber】",  param.getReservNumber());
                    }
                    CommonResultVo<ShopChannel> resultVo =  remoteShopService.shopChannelDetail(param.getShopChannelId());
                    ShopChannel shopChannel =null;
                    Assert.isTrue(resultVo!=null&&resultVo.getCode()==100&&(shopChannel = resultVo.getResult())!=null,"查询预定渠道有误");
                    content= content.replaceAll("【shopChannel】", shopChannel.getName())
                            .replaceAll("【orderSettleAmount】", result.getString("orderSettleAmount") );
                }
                break;
            case OrderConstant.RESERV_CANCEL:
                log.info("取消。。。");
                if (args != null) {
                    ReservOrderVo param = (ReservOrderVo) args[0];
                    Integer orderId = param.getId();
                    reservOrderLog.setOrderId(orderId);
                     reservOrderVo = reservOrderMapper.selectReservOrderById(orderId);
                    reservOrderLog.setOldProseStatus(reservOrderVo.getProseStatus());
                    reservOrderLog.setNowProseStatus(result.getString("proseStatus"));
                    content = OrderConstant.RESERV_CANCEL_LOG_TEMPL;
                    content = content.replaceAll("【cancelReason】", param.getCancelReason())
                            .replaceAll("【refundInter】", param.getRefundInter()==1?"是":"否")
                            .replaceAll("【messageFlag】",param.getMessageFlag()==1?"是":"否");
                }
                break;
            case OrderConstant.RESERV_FAIL:
                log.info("失败。。。");
                if (args != null) {
                    ReservOrderVo param = (ReservOrderVo) args[0];
                    Integer orderId = param.getId();
                    reservOrderLog.setOrderId(orderId);
                     reservOrderVo = reservOrderMapper.selectReservOrderById(orderId);
                    reservOrderLog.setOldProseStatus(reservOrderVo.getProseStatus());
                    reservOrderLog.setNowProseStatus(result.getString("proseStatus"));
                    content = OrderConstant.RESERV_FAIL_LOG_TEMPL;
                    content = content.replaceAll("【failReason】", param.getFailReason())
                            .replaceAll("【failInter】", "1".equals(param.getFailInter())?"是":"否")
                            .replaceAll("【messageFlag】", param.getMessageFlag()==1?"是":"否");
                }
                break;
            case OrderConstant.ADJUS_HOTEL:
                log.info("调剂酒店。。。");
                if (args != null) {
                    ReservOrderVo param = (ReservOrderVo) args[0];
                    Integer orderId = param.getId();
                    reservOrderLog.setOrderId(orderId);
                     reservOrderVo = reservOrderMapper.selectReservOrderById(orderId);
                    reservOrderLog.setOldProseStatus(reservOrderVo.getProseStatus());
                    reservOrderLog.setNowProseStatus(result.getString("proseStatus"));
                    content = OrderConstant.ADJUS_HOTEL_LOG_TEMPL;
                    String oldInfo ="#"+param.getOldProductId()+" 商户:"+ param.getOldHotelName()+"|"+param.getOldShopName()+" 产品:"+param.getOldShopItemName()+" 预定日期:"+param.getOldGiftDate()+(param.getOldGiftTime()==null?"":" 预定时间:"+param.getOldGiftTime())+("accom".equals( param.getServiceType())?" 离店日期:"+param.getOldDeparDate()+" 入住间夜:"+param.getOldCheckNight()+(param.getOldAccoAddon()==null?"":" 房型:"+param.getOldAccoAddon()):"")+" 预订姓名:"+param.getOldGiftName()+" 预订电话:"+param.getOldGiftPhone();
                    content=content.replaceAll("【oldHotel】",oldInfo);

                    JSONObject shopDetailRes = result.getJSONObject("shopDetailRes");
                    if(shopDetailRes!=null){
                        JSONObject shop = shopDetailRes.getJSONObject("shop");
                        if(shop!=null){
                            String newInfo ="#"+result.get("productId")+" 商户:"+ shop.get("hotelName")+"|"+shop.get("name")+" 产品:"+result.getString("shopItemName")+" 预定日期:"+result.getString("giftDate")+(result.getString("giftTime")==null?"":" 预定时间:"+result.getString("giftTime"))+"【ACCOM】"+" 预订姓名:"+result.getString("giftName")+" 预订电话:"+result.getString("giftPhone");
                            if("accom".equals( param.getServiceType())){
                                JSONObject detail = result.getJSONObject("detail");
                                if(detail!=null){
                                    newInfo=  newInfo.replaceAll("【ACCOM】"," 离店日期:"+detail.getString("deparDate")+" 入住间夜:"+detail.getString("checkNight")+(detail.getString("accoAddon")==null?"":" 房型:"+detail.getString("accoAddon")));
                                }else{
                                    newInfo=  newInfo.replaceAll("【ACCOM】","");
                                }
                            }else{
                                newInfo=  newInfo.replaceAll("【ACCOM】","");
                            }
                            content=content.replaceAll("【newHotel】",newInfo);
                        }
                    }
                    content = content.replaceAll("【remarks】",param.getReservRemark()==null?"":param.getReservRemark());
                }
                break;
            case OrderConstant.REPEAT_SEND_SMS:
                log.info("重发短信...,参数:{}",args);
                if (args != null) {
                    ReservOrderVo param = (ReservOrderVo) args[0];
                    Integer orderId = param.getId();
                    reservOrderLog.setOrderId(orderId);
                    reservOrderVo = reservOrderMapper.selectReservOrderById(orderId);
                    reservOrderLog.setOldProseStatus(reservOrderVo.getProseStatus());
                    reservOrderLog.setNowProseStatus(reservOrderVo.getProseStatus());
                    content = OrderConstant.REPEAT_SEND_SMS_LOG_TEMPL;
                }
                break;
            case OrderConstant.RESERV_FIX:
                log.info("修正订单...,参数:{}",args);
                if (args != null) {
                    ReservOrderVo param = (ReservOrderVo) args[0];
                    Integer orderId = param.getId();
                    reservOrderLog.setOrderId(orderId);
                    reservOrderVo = reservOrderMapper.selectReservOrderById(orderId);
                    reservOrderLog.setOldProseStatus(reservOrderVo.getProseStatus());
                    reservOrderLog.setNowProseStatus(reservOrderVo.getProseStatus());
                    content = OrderConstant.RESERV_FIX_LOG_TEMPL;
                }
                break;
            case OrderConstant.SAVE_OBJ_EDIT:
                log.info("保存快递发货...参数：{}",args);
                if (args != null){
                    ReservOrderVo param = (ReservOrderVo) args[0];
                    Integer orderId = param.getId();
                    reservOrderLog.setOrderId(orderId);
                    reservOrderVo = reservOrderMapper.selectReservOrderById(orderId);
                    reservOrderLog.setOldProseStatus(reservOrderVo.getProseStatus());
                    reservOrderLog.setNowProseStatus(reservOrderVo.getProseStatus());
                    content = OrderConstant.SAVE_OBJ_EDIT_LOG_TEMPL;
                    content = content.replaceAll("【orderId】",orderId+"");
                    content = content.replaceAll("【expressStatus】",ExpressStatusEnum.getNameByCode(reservOrderVo.getLogisticsInfo().getStatus()));
                    content = content.replaceAll("【expressNumber】",reservOrderVo.getLogisticsInfo().getExpressNumber());
                    if (StringUtils.isNotBlank(reservOrderVo.getLogisticsInfo().getExpressNameId())){
                        SysDict sysDict = new SysDict();
                        sysDict.setType("express_type");
                        sysDict.setValue(reservOrderVo.getLogisticsInfo().getExpressNameId());
                        R<SysDict> dictR = remoteDictService.selectByType(sysDict);
                        if (dictR != null && dictR.getData() != null){
                            content = content.replaceAll("【expressCompany】",dictR.getData().getLabel());
                        }
                    }
                    content = content.replaceAll("【expressCompany】","无");
                }
                break;
            case OrderConstant.SEND_OBJ_EDIT:
                log.info("发货快递物流...参数:{}",args);
                if (args != null){
                    ReservOrderVo param = (ReservOrderVo) args[0];
                    Integer orderId = param.getId();
                    reservOrderLog.setOrderId(orderId);
                    reservOrderVo = reservOrderMapper.selectReservOrderById(orderId);
                    reservOrderLog.setOldProseStatus(reservOrderVo.getProseStatus());
                    reservOrderLog.setNowProseStatus(reservOrderVo.getProseStatus());
                    content = OrderConstant.SEND_OBJ_EDIT_LOG_TEMPL;
                    content = content.replaceAll("【orderId】",orderId+"");
                    content = content.replaceAll("【expressStatus】",ExpressStatusEnum.getNameByCode(reservOrderVo.getLogisticsInfo().getStatus()));
                    content = content.replaceAll("【expressNumber】",reservOrderVo.getLogisticsInfo().getExpressNumber());
                    if (StringUtils.isNotBlank(reservOrderVo.getLogisticsInfo().getExpressNameId())){
                        SysDict sysDict = new SysDict();
                        sysDict.setType("express_type");
                        sysDict.setValue(reservOrderVo.getLogisticsInfo().getExpressNameId());
                        R<SysDict> dictR = remoteDictService.selectByType(sysDict);
                        if (dictR != null && dictR.getData() != null){
                            content = content.replaceAll("【expressCompany】",dictR.getData().getLabel());
                        }
                    }
                    content = content.replaceAll("【expressCompany】","无");
                }
                break;
            default:
                ;
        }
//        if (null != reservOrderVo.getMessageFlag() && reservOrderVo.getMessageFlag().compareTo(1)==0) {
            List<SysSmsQueue> smsQueueList =nuwaInterfaceService.querySms(reservOrderVo);
            if(smsQueueList!=null&&!smsQueueList.isEmpty()){
                log.info("短信列表:{}",smsQueueList);
                SysSmsQueue sysSmsQueue = smsQueueList.get(0);
                String smsStatus =SmsStateEnums.get(sysSmsQueue.getState()).getValue();
                content = content.replaceAll("【smsStatus】",smsStatus);
            }else{
                content = content.replaceAll("【smsStatus】",SmsStateEnums.INIT.getValue());
            }
//        }else{
//            content = content.replaceAll("【smsStatus】",SmsStateEnums.INIT.getValue());
//        }
        reservOrderLog.setContent(content);
        log.info("【插入日志内容】:{}",reservOrderLog);
        reservOrderLogMapper.insert(reservOrderLog);
    }
    private String join(Object... obj){
        return StringUtils.join(obj,"|");
    }



    @Override
    public ReservOrderLog insertManual(ReservOrderLog reservOrderLog) {
        String user = SecurityUtils.getLoginName();
        reservOrderLog.setCreateUser(user);
        reservOrderLog.setOperType(OrderConstant.MANUAL_REMARK_LOG);
        int num = reservOrderLogMapper.insert(reservOrderLog);
        ReservOrderLog reservOrderLogResult = reservOrderLogMapper.selectById(reservOrderLog.getId());
        return reservOrderLogResult;
    }

    @Override
    public List<ReservOrderLog> selectList(ReservOrderLog reservOrderLog) {
        List<ReservOrderLog> list = reservOrderLogMapper.selectList(reservOrderLog);
        return list;
    }
}
