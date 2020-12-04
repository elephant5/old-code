package com.colourfulchina.mars.api.vo;

import lombok.Data;

/**
 * @Author: Nickal
 * @Description:
 * @Date: 2019/10/28 9:42
 */
@Data
public class ZwBookOrder {
    private String projectChannelId;// 渠道编号
    private String salesBankName;// 隶属大客户
    private String channel;// 销售渠道
    private String mode;// 销售方式
    private String projectId;// 项目编号
    private String projectShortName;// 系统内部简称
    private String num;// 序号
    private String salesOrderId;// 预约单号
    private String createOrderDate;// 下单日期
    private String useDate;// 使用日期
    private String useStatus;//使用状态
    private String customerName;// 客户姓名
    private String mobile;// 手机号
    private String code;// 激活码(第三方卷码)
    private String code2;//卡卷兑换码
    private String goodsNum;// 商品数量
    private String payType;// 支付方式
    private String originalPrice;// 原价
    private String subsidyMoney;// 优惠金额
    private String discountRatio;// 折扣比例
    private String realPrice;// 客户实际支付金额
    private String payRecordid;//众网小贷支付id
}
