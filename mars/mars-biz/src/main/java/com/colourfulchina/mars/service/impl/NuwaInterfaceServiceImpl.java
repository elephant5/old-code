package com.colourfulchina.mars.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.mars.api.entity.ReservCode;
import com.colourfulchina.mars.api.entity.ReservOrder;
import com.colourfulchina.mars.api.entity.ReservOrderDetail;
import com.colourfulchina.mars.api.entity.SmsConfig;
import com.colourfulchina.mars.api.vo.ReservOrderVo;
import com.colourfulchina.mars.service.*;
import com.colourfulchina.nuwa.api.entity.SysSmsQueue;
import com.colourfulchina.nuwa.api.feign.RemoteKltSmsService;
import com.colourfulchina.nuwa.api.sms.model.SmsSendResult;
import com.colourfulchina.nuwa.api.vo.KltSendSmsRequestVo;
import com.colourfulchina.pangu.taishang.api.entity.Goods;
import com.colourfulchina.pangu.taishang.api.enums.GiftTypeEnum;
import com.colourfulchina.pangu.taishang.api.enums.ResourceTypeEnums;
import com.colourfulchina.pangu.taishang.api.vo.res.ShopDetailRes;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;


@Service
@Slf4j
@AllArgsConstructor
public class NuwaInterfaceServiceImpl implements NuwaInterfaceService {

    RemoteKltSmsService remoteKltSmsService;

    @Autowired
    SmsConfigService smsConfigService;

    @Autowired
    PanguInterfaceService panguInterfaceService;

    @Autowired
    ReservOrderDetailService reservOrderDetailService;

    @Autowired
    ReservCodeService reservCodeService;
    /**
     * 短信接口
     *
     * @param smsSendRequestVo
     * @return
     */
    @Override
    public CommonResultVo<SmsSendResult> send(KltSendSmsRequestVo smsSendRequestVo) {
        log.info("发送短信:{}",JSON.toJSONString(smsSendRequestVo));
        final CommonResultVo<SmsSendResult> resultVo = remoteKltSmsService.send(smsSendRequestVo);
//        Assert.notNull(resultVo,"短信发送失败");
//        Assert.isTrue(resultVo.getCode()==100,"短信发送失败");
//        Assert.notNull(resultVo.getResult(),"短信发送失败");
        return resultVo;
    }

    /**
     * 预约单统一发送短信
     *
     * @param reservOrder
     */
    @Override
    public void sendMsg(ReservOrder reservOrder) {

        EntityWrapper<SmsConfig> local = new EntityWrapper<>();
        local.eq("type",reservOrder.getServiceType() );
        local.eq("goods_id",reservOrder.getGoodsId());
        local.eq("name","reserv_order");
        local.eq("status",reservOrder.getProseStatus());
        local.eq("del_flag",0);
        List<SmsConfig> list  =  smsConfigService.selectList(local);
        if(CollectionUtils.isEmpty(list)){
            EntityWrapper<SmsConfig> local2 = new EntityWrapper<>();
            local2.eq("type",reservOrder.getServiceType());
            local2.eq("name","reserv_order");
            local2.eq("status",reservOrder.getProseStatus());
            local2.eq("del_flag",0);
            list  = smsConfigService.selectList(local2);
        }
        if(!CollectionUtils.isEmpty(list)){
            SmsConfig smsConfig = list.get(0);
            List<String> phones = Lists.newArrayList();
            phones.add(reservOrder.getGiftPhone());
            KltSendSmsRequestVo smsSendRequestVo = new KltSendSmsRequestVo();
            smsSendRequestVo.setTemplateId(smsConfig.getSmsId()+"");
            smsSendRequestVo.setObjId(reservOrder.getId());
            smsSendRequestVo.setType(smsConfig.getName());
            Map<String,Object> templateParams = Maps.newHashMap();
            Goods goods = panguInterfaceService.selectGoodsById(reservOrder.getGoodsId());
            ShopDetailRes shopDetailRes = panguInterfaceService.getShopDetail(reservOrder.getShopId());
            ReservCode code = reservCodeService.selectOneReservCode(reservOrder.getId());
            ReservOrderDetail detail = reservOrderDetailService.selectOneReservOrderDetail(reservOrder.getId());
            templateParams.put("userName",reservOrder.getGiftName());
            templateParams.put("merchantName", StringUtils.isBlank(shopDetailRes.getShop().getHotelName()) ? shopDetailRes.getShop().getName() : shopDetailRes.getShop().getHotelName()+"|"+shopDetailRes.getShop().getName());
            templateParams.put("checkInDate",reservOrder.getGiftDate());
            templateParams.put("checkInTime",shopDetailRes.getShop().getCheckInTime());
            templateParams.put("checkOutDate",detail.getDeparDate());
            templateParams.put("checkOutTime",shopDetailRes.getShop().getCheckOutTime());
            templateParams.put("checkInName",reservOrder.getGiftName());
            templateParams.put("merchantAddress",shopDetailRes.getShop().getAddress());
            templateParams.put("merchantTel",shopDetailRes.getShop().getPhone());
            templateParams.put("tel",goods.getHotline());
            templateParams.put("equityType",reservOrder.getGiftType() == null? null : GiftTypeEnum.findByCode(reservOrder.getGiftType()).getName());
            templateParams.put("appointmentDate",reservOrder.getGiftDate());
            templateParams.put("resourcesName",shopDetailRes.getShop().getName());
            if(code!= null ){
                templateParams.put("code",code.getVarCode());
            }else {
                if (reservOrder.getServiceType().equals(ResourceTypeEnums.ACCOM.getCode())){
                    templateParams.put("code",reservOrder.getReservNumber());
                }
            }

            templateParams.put("numberOfPeople",reservOrder.getGiftPeopleNum() +"");
            smsSendRequestVo.setPhones(phones);
            smsSendRequestVo.setTemplateParams(templateParams);
            this.send(smsSendRequestVo);
        }

    }

    @Override
    public SmsSendResult  sendMsgReservOrderVo(ReservOrderVo reservOrder) {
        SmsSendResult smsSendResult = null;
        EntityWrapper<SmsConfig> local = new EntityWrapper<>();
        local.eq("type",reservOrder.getServiceTypeCode() );
        local.eq("goods_id",reservOrder.getGoodsId());
        local.eq("name","reserv_order");
        local.eq("status",reservOrder.getProseStatus());
        local.eq("del_flag",0);
        List<SmsConfig> list  =  smsConfigService.selectList(local);
        if(CollectionUtils.isEmpty(list)){
            EntityWrapper<SmsConfig> local2 = new EntityWrapper<>();
            local2.eq("type",reservOrder.getServiceTypeCode());
            local2.eq("name","reserv_order");
            local2.eq("status",reservOrder.getProseStatus());
            local2.eq("del_flag",0);
            list  = smsConfigService.selectList(local2);
        }
        if(CollectionUtils.isEmpty(list)){
            log.info("未查询到短信模板:{}",JSON.toJSONString(reservOrder));
        }
        SmsConfig smsConfig = list.get(0);
        List<String> phones = Lists.newArrayList();
        phones.add(reservOrder.getGiftPhone());
        KltSendSmsRequestVo smsSendRequestVo = new KltSendSmsRequestVo();
        smsSendRequestVo.setTemplateId(smsConfig.getSmsId()+"");
        smsSendRequestVo.setObjId(reservOrder.getId());
        smsSendRequestVo.setType(smsConfig.getName());
        Map<String,Object> templateParams = Maps.newHashMap();
        Goods goods = panguInterfaceService.selectGoodsById(reservOrder.getGoodsId());
        ShopDetailRes shopDetailRes = panguInterfaceService.getShopDetail(reservOrder.getShopId());
        ReservCode code = reservCodeService.selectOneReservCode(reservOrder.getId());
        ReservOrderDetail detail = reservOrderDetailService.selectOneReservOrderDetail(reservOrder.getId());
        templateParams.put("userName",reservOrder.getGiftName());
        templateParams.put("merchantName",StringUtils.isBlank(shopDetailRes.getShop().getHotelName()) ? shopDetailRes.getShop().getName() : shopDetailRes.getShop().getHotelName()+"|"+shopDetailRes.getShop().getName());
        templateParams.put("checkInDate",reservOrder.getGiftDate());
        templateParams.put("checkInTime",shopDetailRes.getShop().getCheckInTime());
        templateParams.put("checkOutDate",detail.getDeparDate());
        templateParams.put("checkOutTime",shopDetailRes.getShop().getCheckOutTime());
        templateParams.put("checkInName",reservOrder.getGiftName());
        templateParams.put("merchantAddress",shopDetailRes.getShop().getAddress());
        templateParams.put("merchantTel",shopDetailRes.getShop().getPhone());
        templateParams.put("tel",goods.getHotline());
        templateParams.put("equityType",reservOrder.getGiftType() == null? null : GiftTypeEnum.findByCode(reservOrder.getGiftType()).getName());
        templateParams.put("appointmentDate",reservOrder.getGiftDate());
        templateParams.put("resourcesName",shopDetailRes.getShop().getName());
        if(code!= null ){
            templateParams.put("code",code.getVarCode());
        }else {
            if (reservOrder.getServiceTypeCode().equals(ResourceTypeEnums.ACCOM.getCode())){
                templateParams.put("code",reservOrder.getReservNumber());
            }
        }

        templateParams.put("numberOfPeople",reservOrder.getGiftPeopleNum() +"");
        smsSendRequestVo.setPhones(phones);
        smsSendRequestVo.setTemplateParams(templateParams);
        this.send(smsSendRequestVo);
        return smsSendResult;
    }


    @Override
    public List<SysSmsQueue> querySms(ReservOrderVo reservOrder){
        log.info("查询参数:id:{},serviceType:{},proseStatus:{},giftPhone:{}",reservOrder.getId(),reservOrder.getServiceType(),reservOrder.getProseStatus(),reservOrder.getGiftPhone());
        EntityWrapper<SmsConfig> local = new EntityWrapper<>();
        local.eq("type",reservOrder.getServiceType() );
        local.eq("goods_id",reservOrder.getGoodsId());
        local.eq("name","reserv_order");
        local.eq("status",reservOrder.getProseStatus());
        local.eq("del_flag",0);
        List<SmsConfig> list  =  smsConfigService.selectList(local);
        List<SysSmsQueue> sysSmsQueues = null;
        if(CollectionUtils.isEmpty(list)){
            EntityWrapper<SmsConfig> local2 = new EntityWrapper<>();
            local2.eq("type",reservOrder.getServiceType());
            local2.eq("name","reserv_order");
            local2.eq("status",reservOrder.getProseStatus());
            local2.eq("del_flag",0);
            list  = smsConfigService.selectList(local2);
        }
        if(!CollectionUtils.isEmpty(list)){
            SmsConfig smsConfig = list.get(0);
            log.info("smsConfig:{}",smsConfig);
            SysSmsQueue sysSmsQueue = new SysSmsQueue();
            sysSmsQueue.setObjId(reservOrder.getId());
            sysSmsQueue.setTemplateId(String.valueOf(smsConfig.getSmsId()));
            sysSmsQueue.setPhone(reservOrder.getGiftPhone());
            CommonResultVo<List<SysSmsQueue>> resultVo = remoteKltSmsService.list(sysSmsQueue);
            if (resultVo == null){
                log.error("短信发送失败:{}", JSON.toJSONString(sysSmsQueue));
                return sysSmsQueues;
            }
            if (resultVo.getCode()!=100 || resultVo.getResult()==null){
                log.error("短信发送失败:{}", JSON.toJSONString(sysSmsQueue));
                return sysSmsQueues;
            }
            sysSmsQueues=resultVo.getResult();
            log.info("resultVo.getResult():{}",resultVo.getResult());
            log.info("sysSmsQueues:{}",sysSmsQueues);
        }
        return sysSmsQueues;
    }
}
