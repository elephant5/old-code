package com.colourfulchina.mars.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.colourfulchina.mars.api.entity.GiftCode;
import com.colourfulchina.mars.api.entity.ReservOrder;
import com.colourfulchina.mars.api.entity.ThirdReservPushLog;
import com.colourfulchina.mars.api.entity.ThirdReservPushLogInfo;
import com.colourfulchina.mars.api.enums.PushStatusEnum;
import com.colourfulchina.mars.api.vo.ReservOrderProductVo;
import com.colourfulchina.mars.api.vo.SynchroPushOrderVo;
import com.colourfulchina.mars.api.vo.req.BrokerCouponsUsedReq;
import com.colourfulchina.mars.config.MqscProperties;
import com.colourfulchina.mars.config.SynchroPushConfig;
import com.colourfulchina.mars.service.*;
import com.colourfulchina.mars.utils.HttpClientUtils;
import com.colourfulchina.mars.utils.PushThirdOrderUtils;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class SynchroPushServiceImpl implements SynchroPushService {

    @Autowired
    private SynchroPushConfig synchroPushConfig;
    @Autowired
    private ReservOrderService reservOrderService;
    @Autowired
    private GiftOrderApiService giftOrderApiService;
    @Autowired
    private GiftCodeService giftCodeService;
    @Autowired
    private BrokerService brokerService;
    @Autowired
    private ThirdReservPushLogService thirdReservPushLogService;
    @Autowired
    private ThirdReservPushLogInfoService thirdReservPushLogInfoService;
    @Autowired
    private MqscProperties mqscProperties;
    @SuppressWarnings("rawtypes")
    @Autowired
    protected RedisTemplate redisTemplate;

    @Override
    public String synchroPush(Integer reservOderId) throws Exception {
        return pushReservOrder(reservOderId,null);
    }

    /**
     * 重复推送失败的订单至第三方
     * @return
     * @throws Exception
     */
    @Override
    public Boolean rePushFailThirdOrder() throws Exception {
        //查询出推送失败的并且推送总次数小于8次的预约单（推送时间小于等于当前时间）
        Wrapper wrapper = new Wrapper() {
            @Override
            public String getSqlSegment() {
                return "where push_status = '" + PushStatusEnum.PUSH_FAIL.getCode() + "' and push_count < " + PushThirdOrderUtils.PUSH_MAX_COUNT + " and next_push_time <= NOW()";
            }
        };
        List<ThirdReservPushLog> list = thirdReservPushLogService.selectList(wrapper);
        //循环需要推送的预约单调用推送接口
        if (!CollectionUtils.isEmpty(list)){
            for (ThirdReservPushLog pushLog : list) {
                pushReservOrder(pushLog.getOrderId(),pushLog.getId());
            }
        }
        return Boolean.TRUE;
    }

    private String pushReservOrder(Integer reservOderId,Integer logId) throws Exception {
        if (reservOderId == null) {
            throw new Exception("订单ID为空");
        }
        // 1.查询订单信息
        ReservOrder reservOrder = reservOrderService.getReservOrderById(reservOderId);
        if (reservOrder == null) {
            throw new Exception("预约单不存在");
        }

        //中行项目10280订单
        if(reservOrder.getGoodsId().compareTo(mqscProperties.getGoodsId()) == 0){
            BrokerCouponsUsedReq couponsUsedReq = new BrokerCouponsUsedReq();
            Integer giftCodeId = reservOrder.getGiftCodeId();
            GiftCode giftCode = giftCodeService.selectGiftCodeInfo(giftCodeId);
            //商品id,不是预约单中的商品id值，要改动
            couponsUsedReq.setWaresId(giftCode.getRemarks());
            //库存编码, 优惠券id + 00， gift_code
            couponsUsedReq.setWEid(giftCodeId + "00");
            // 优惠券表示你
            couponsUsedReq.setWSign("E");
            //优惠券串码
            couponsUsedReq.setWInfo(giftCode.getActCode());
            //使用日期时间，可以省去
            Date actCodeTime = giftCode.getActCodeTime();

            if(actCodeTime != null){
                String format = DateUtil.format(actCodeTime, "yyyyMMdd HH:mm:ss");
                String[] s = format.split(" ");
                couponsUsedReq.setUsedDate(s[0]);
                couponsUsedReq.setUsedTime(s[1]);
            }

            Boolean aBoolean = brokerService.couponUsed(couponsUsedReq);
            if(aBoolean){
                log.info("中行优惠券使用更新成功,{}",reservOrder.getId());
            }else{
                log.error("中行优惠券使用更新失败，{}",JSON.toJSONString(reservOrder));
            }
        }

        switch (reservOrder.getOrderSource()) {
            // 银联的订单
            case "UNIONPAY_APP":
            case "UNIONPAY_WECHAT":
                //插入推送日志
                ThirdReservPushLog pushLog = new ThirdReservPushLog();
                ThirdReservPushLogInfo logInfo = new ThirdReservPushLogInfo();
                insertLog(logId, reservOrder, logInfo, pushLog);
                // 根据预约单中的激活码 查询 对应的 订单信息
                String partnerOrderId = giftOrderApiService.getOrderInfoByGiftCode(reservOrder.getGiftCodeId());
                if (partnerOrderId == null) {
                    logInfo.setRemark("订单不存在");
                    thirdReservPushLogInfoService.updateById(logInfo);
                    throw new Exception("订单不存在");
                }
                // 查询预约单详情
                ReservOrderProductVo productVo = reservOrderService.getReservOrderProById(reservOderId);
                if(productVo ==null ){
                    logInfo.setRemark("预约单不存在");
                    thirdReservPushLogInfoService.updateById(logInfo);
                    throw new Exception("预约单不存在");
                }
                if (logId == null){
                    pushLog.setHxStatus(productVo.getUseStatus());
                    thirdReservPushLogService.updateById(pushLog);
                }
                return this.synchroPushToUionPay(partnerOrderId, productVo, pushLog, logInfo);
            default:
                log.info("非第三方订单");
        }
        return null;
    }

    private void insertLog(Integer logId, ReservOrder reservOrder, ThirdReservPushLogInfo logInfo, ThirdReservPushLog pushLog) throws Exception {
        if (logId == null){
            pushLog.setOrderId(reservOrder.getId());
            pushLog.setProseStatus(reservOrder.getProseStatus());
            pushLog.setPushCount(1);
            pushLog.setPushStatus(PushStatusEnum.PUSH_FAIL.getCode());
            pushLog.setNextPushTime(PushThirdOrderUtils.calNextPushTime(pushLog.getPushCount()));
            thirdReservPushLogService.insert(pushLog);
        }else {
            ThirdReservPushLog temp = thirdReservPushLogService.selectById(logId);
            temp.setPushCount(temp.getPushCount()+1);
            temp.setPushStatus(PushStatusEnum.PUSH_FAIL.getCode());
            temp.setNextPushTime(PushThirdOrderUtils.calNextPushTime(temp.getPushCount()));
            thirdReservPushLogService.updateById(temp);
            BeanUtils.copyProperties(temp,pushLog);
        }
        logInfo.setLogId(pushLog.getId());
        logInfo.setProseStatus(reservOrder.getProseStatus());
        logInfo.setThisCount(pushLog.getPushCount());
        thirdReservPushLogInfoService.insert(logInfo);
    }

    /**
     * @Title 银联订单推送
     * @param productVo
     * @throws Exception
     */
    private String synchroPushToUionPay(String partnerOrderId, ReservOrderProductVo productVo, ThirdReservPushLog pushLog, ThirdReservPushLogInfo logInfo) throws Exception {
        //2.1.1获取token
        String token = (String) redisTemplate.opsForValue().get("UNIONPAY_TOKEN");
        if (StringUtils.isEmpty(token)) {
            String tokenData = HttpClientUtils.httpGet(synchroPushConfig.getGetUrl() + "?app_id=" + synchroPushConfig.getAppId() + "&app_secret=" + synchroPushConfig.getAppSecret());
            log.info("银联获取Token接口结果为:"+ JSON.toJSONString(tokenData));
            if (!StringUtils.isEmpty(tokenData)) {
                // 创建json对象
                JSONObject jObject = new JSONObject(tokenData);
                // 解析第一层---对象
                if("成功".equals(jObject.getString("respMsg"))){
                    // 获取token
                    token = jObject.getString("token");
                    redisTemplate.opsForValue().set("UNIONPAY_TOKEN", token, 90, TimeUnit.MINUTES);
                } else {
                    throw new Exception(jObject.getString("respMsg"));
                }
            }
        }
        //接口有返回值
        if (!StringUtils.isEmpty(token)) {
            // 推送至第三方对象
            SynchroPushOrderVo orderVo = new SynchroPushOrderVo();
            orderVo.setBookOrderId(productVo.getId() + "");  // 订单编号
            orderVo.setOrderTime(productVo.getCreateTimeStr()); // 订单生成时间
            orderVo.setGiftName(getGiftName(productVo.getGiftType())); // 权益内容
            orderVo.setBookDate(productVo.getGiftDate()); // 预定时间 - 日期
            orderVo.setBookTime(productVo.getGiftTime()); // 定时间 - 时间
            orderVo.setHotelName(productVo.getHotelName()); // 预约酒店- 酒店名称
            orderVo.setShopName(productVo.getShopName()); //  预约酒店 - 餐厅名称
            orderVo.setGiftTypeName(productVo.getServiceName()+getGiftTypeName(productVo.getGiftType())); //用餐类型
            orderVo.setMealSection(productVo.getProductName()); //餐段
            orderVo.setAddress(productVo.getShopAddress()); // 酒店地址
            orderVo.setUsePeople(productVo.getGiftPeopleNum()+""); // 用餐人数
            orderVo.setUseName(productVo.getGiftName()); // 预约姓名
            orderVo.setUsePhone(productVo.getGiftPhone()); // 预约手机
            orderVo.setNotes(productVo.getReservRemark()); // 特殊要求
            orderVo.setCity(productVo.getShopCity()); //城市
            orderVo.setReservNumber(productVo.getReservNumber()); //核销码
            orderVo.setStatus(getOrderStatus(productVo)); // 预约单状态
            // NewMap
            Map<String, String> map = new HashMap<String, String>();
            //2.2.1组装 请求报文体参数
            map.put("orderId", partnerOrderId); //权益平台订单号  银联订单号
            map.put("merOrderId", orderVo.getBookOrderId()); //合作方订单号 客乐芙预约单号
            map.put("partner", synchroPushConfig.getPartnerId()); //接入的合作方ID
            map.put("state", orderVo.getStatus()); //订单状态
            map.put("reserve", new Gson().toJson(orderVo)); //预留字段 客乐芙报送内容主体json
            map.put("appId", synchroPushConfig.getAppId()); //登录银联技术开放平台 APP_ID
            //2.2.2 sign
            String body = new Gson().toJson(map);
            //2.2.3 时间戳
            String ts = String.valueOf(System.currentTimeMillis());
            // 签名信息
            String sign = HttpClientUtils.sign(body, ts, synchroPushConfig.getSignSecret());
            //2.2.5推送数据
            String resp = HttpClientUtils.httpPostJson(synchroPushConfig.getPostUrl() + "?token=" + token + "&sign=" + sign + "&ts=" + ts, body);
            // 创建json对象
            // JSONObject resultJson = new JSONObject(resp);
            // 解析第一层---对象 //获取token
            log.info("银联订单推送回调:{}",resp);

            com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(resp);
            String respCd = jsonObject.getString("respCd");
            if ("0000".equals(respCd)){
                pushLog.setPushStatus(PushStatusEnum.PUSH_SUCCESS.getCode());
                thirdReservPushLogService.updateById(pushLog);
            }
            logInfo.setRespCd(respCd);
            logInfo.setHxStatus(productVo.getUseStatus());
            logInfo.setRespMsg(jsonObject.getString("respMsg"));
            logInfo.setRemark(resp);
            thirdReservPushLogInfoService.updateById(logInfo);
            return resp;
        }
        return null;
    }

    /**
     * 获取权益内容
     * @param giftType
     * @return
     */
    private static String getGiftName(String giftType) {
        switch (giftType) {
            case "2F1":
                return "两人同行享五折优惠";
            case "3F1":
                return "三人同行享六折优惠";
            case "B1F1":
                return "买一赠一";
            case "D5":
                return "五折尊享";
            case "F1":
                return "单人礼遇";
            case "F2":
                return "双人礼遇";
            case "N1":
                return "两天一晚";
            case "N2":
                return "三天两晚";
            case "N3":
                return "四天三晚";
            case "N4":
                return "五天四晚";
            case "NX":
                return "开放住宿";
            default:
                return null;
        }
    }

    /**
     * 用餐类型
     * @param giftType
     * @return
     */
    private static String getGiftTypeName(String giftType) {
        switch (giftType) {
            case "2F1":
                return "二免一";
            case "3F1":
                return "三免一";
            case "B1F1":
                return "买一赠一";
            case "D5":
                return "五折";
            case "F1":
                return "单免";
            case "F2":
                return "双免";
            case "N1":
                return "两天一晚";
            case "N2":
                return "三天两晚";
            case "N3":
                return "四天三晚";
            case "N4":
                return "五天四晚";
            case "NX":
                return "开放住宿";
            default:
                return null;
        }
    }

    /**
     * 预约单状态
     * @param productVo
     * @return
     */
    private static String getOrderStatus(ReservOrderProductVo productVo) {
        // 1.预约单状态
        switch (productVo.getProseStatus()) {
            case "0": // 尚未预定
            case "4": // 处理中
                return "02"; // 对标 预约中
            case "1": // 预定成功
                //1.1核销码状态
                switch (productVo.getUseStatus()) {
                    case "0": // 未使用
                        return "16"; // 对标 预约成功
                    //已完成
                    case "1": // 已过期
                    case "2": // 已使用
                    case "3": // 已作废
                        return "15"; // 对标 已完成
                }
            case "2": // 预订取消
                return "18"; // 对标 预订取消
            case "3": // 预订失败
                return "17"; // 对标 预订失败
            default:
                return null;
        }
    }
}
