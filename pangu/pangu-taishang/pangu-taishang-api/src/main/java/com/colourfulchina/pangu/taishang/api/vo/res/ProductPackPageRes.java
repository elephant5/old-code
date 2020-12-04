package com.colourfulchina.pangu.taishang.api.vo.res;

import io.swagger.annotations.Api;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 打包产品列表分页查询出参
 */
@Data
@Api("打包产品列表分页查询出参")
public class ProductPackPageRes extends ProductPageRes{
    private static final long serialVersionUID = 555077650376266656L;

    //商户电话
    private String phone;
    //泊车信息
    private String parking;
    //备注
    private String remark;
    //商户地址
    private String address;
    //开餐时间
    private String openTime;
    //关餐时间
    private String closeTime;
    //权益类型code
    private String giftCode;
    //产品子项id
    private Integer productItemId;
    //适用范围
    private String applyTime;
    //起止时间
    private Date startDate;
    //截止时间
    private Date endDate;
    //周一
    private Integer monday;
    //周二
    private Integer tuesday;
    //周三
    private Integer wednesday;
    //周四
    private Integer thursday;
    //周五
    private Integer friday;
    //周六
    private Integer saturday;
    //周日
    private Integer sunday;
    //成本
    private BigDecimal cost;
    //市场参考价
    private BigDecimal marketPrice;
    //适用日期
    private String applyDate;
    //适用星期
    private String applyWeek;
    //净价
    private BigDecimal netPrice;
    //服务费
    private String serviceRate;
    //税费
    private String taxRate;
    //单人总价
    private BigDecimal singlePrice;
    //双人总价
    private BigDecimal doublePrice;
    //折扣率
    private String itemRate;
}
