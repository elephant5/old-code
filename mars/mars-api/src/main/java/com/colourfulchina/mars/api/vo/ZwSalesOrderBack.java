package com.colourfulchina.mars.api.vo;

import lombok.Data;

/**
 * @Author: Nickal
 * @Description:
 * @Date: 2019/10/16 17:43
 */
@Data
public class ZwSalesOrderBack {
    private String bankRecordid;// 银行支付ID
    private String billDate;// 开票日期
    private String channel;// 销售渠道
    private String createOrderDate;// 下单日期
    private String customerName;// 客户姓名
    private String code;// 激活码
    private String goodsNum;// 商品数量
    private String mobile;// 手机号
    private String mode;// 销售方式
    private String num;// 序号
    private String payTime;// 支付日期
    private String payType;// 支付方式
    private String projectChannelId;// 渠道编号
    private String projectId;// 项目编号
    private String projectName;// 项目名称
    private String projectShortName;// 系统内部简称
    private String realPrice;// 客户支付金额
    private String receiveOrderDate;// 收到订单日期
    private String salesBankName;// 隶属大客户
    private String salesMoney;// 销售金额
    private String salesOrderId;// 订单号
    private String salesOrderStatus;// 订单状态
    private String subsidyMoney;// 补贴金额
    private String payRecordid;//第三发支付id
    private String note;//销售单备注信息
}
