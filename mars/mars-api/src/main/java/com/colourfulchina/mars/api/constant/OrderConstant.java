package com.colourfulchina.mars.api.constant;

/**
 * @Author: luck.luo
 * @Description:
 * @Date: 2019/7/31 17:44
 */
public class OrderConstant {
    public final static String SYS_AUTO_LOG = "sys_auto";//系统自动生成日志
    public final static String MANUAL_REMARK_LOG = "manual_remark";//手工备注

    /**
     * 预定中
     */
    public final static String RESERV_IN_PROGRESS = "1";//预定中
    public final static String RESERV_SUCCESS = "2";//预定成功
    public final static String RESERV_CANCEL = "3";//预定取消
    public final static String RESERV_FAIL = "4";//预定失败
    public final static String ADJUS_HOTEL = "5";//调剂酒店
    public final static String REPEAT_SEND_SMS = "6";//重发短信
    public final static String RESERV_FIX = "7";//修正预订信息
    public final static String SAVE_OBJ_EDIT = "8";//保存编辑快递发货信息
    public final static String SEND_OBJ_EDIT = "9";//快递发货信息


  //日志模板

    public final static String RESERV_IN_PROGRESS_LOG_TEMPL = "预约中 【user】开始处理订单";//预定中
    public final static String RESERV_SUCCESS_ACCOM_LOG_TEMPL = "订单状态：预定成功      " +
            "酒店确认号:【reservNumber】      " +
            "姓名：【giftName】      " +
            "预定渠道：【shopChannel】      " +
            "日期：【giftDate】      " +
            "金额：【orderSettleAmount】      " +
            "短信状态：【smsStatus】      ";//预定中
    public final static String RESERV_SUCCESS_BUFFET_LOG_TEMPL = "订单状态：预订成功      " +
            "核销码：【varCode】      " +
            "渠道：【shopChannel】      " +
            "金额：【orderSettleAmount】      " +
            "短信状态：【smsStatus】";//预定中
    public final static String RESERV_SUCCESS_VIP_LOG_TEMPL = "订单状态：预订成功      " +
            "预定号:【reservNumber】      " +
            "姓名：【giftName】      " +
            "预定渠道：【shopChannel】      " +
            "金额：【orderSettleAmount】      " +
            "短信状态：【smsStatus】";//预定中
    public final static String RESERV_CANCEL_LOG_TEMPL = "订单状态：预订取消      " +
            "取消原因：【cancelReason】      " +
            "权益是否退回：【refundInter】      " +
            "是否短信：【messageFlag】      " +
            "短信状态：【smsStatus】";//预定取消
    public final static String RESERV_FAIL_LOG_TEMPL = "订单状态：预订失败      " +
            "失败原因：【failReason】      " +
            "权益是否退回：【failInter】      " +
            "是否短信：【messageFlag】      " +
            "短信状态：【smsStatus】";//预定失败
    public final static String ADJUS_HOTEL_LOG_TEMPL = "产品：【oldHotel】 -> 【newHotel】      " +
            "备注：【remarks】";//调剂酒店
    public final static String RESERV_FIX_LOG_TEMPL = "修正：【oldChannel】 -> 【newChannel】      " +
            "备注：【remarks】";//修正订单
    public final static String REPEAT_SEND_SMS_LOG_TEMPL ="重发短信 "+
            "状态：【smsStatus】";//重发短信
    public final static String SAVE_OBJ_EDIT_LOG_TEMPL = "【保存编辑物流信息】订单号：【orderId】,【expressStatus】;快递公司：【expressCompany】; 快递单号：【expressNumber】";
    public final static String SEND_OBJ_EDIT_LOG_TEMPL = "【保存并发货物流信息】订单号：【orderId】,【expressStatus】;快递公司：【expressCompany】; 快递单号：【expressNumber】";

}
