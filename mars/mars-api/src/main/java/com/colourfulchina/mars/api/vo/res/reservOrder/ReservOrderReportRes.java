package com.colourfulchina.mars.api.vo.res.reservOrder;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 约单报表出参
 */
@Data
public class ReservOrderReportRes implements Serializable {
    private static final long serialVersionUID = 4784786506812752241L;

    @ApiModelProperty("渠道编号")
    private Integer salesChannelId;
    @ApiModelProperty("大客户")
    private String salesBankName;
    @ApiModelProperty("销售渠道")
    private String salesChannelName;
    @ApiModelProperty("销售方式")
    private String salesWayName;
    @ApiModelProperty("项目编号")
    private Integer goodsId;
    @ApiModelProperty("系统内部简称")
    private String goodsShortName;
    @ApiModelProperty("序号")
    private Integer rank;
    @ApiModelProperty("预约单号")
    private Integer reservOrderId;
    @ApiModelProperty("订单日期")
    private Date createTime;
    @ApiModelProperty("订单日期，字符串类型")
    private String createDate;
    @ApiModelProperty("使用日期")
    private String giftDate;
    @ApiModelProperty("使用状态")
    private String varStatus;
    @ApiModelProperty("客户姓名")
    private String giftName;
    @ApiModelProperty("手机号")
    private String giftPhone;
    @ApiModelProperty("激活码")
    private String actCode;
    @ApiModelProperty("商品数量")
    private Integer num;
    @ApiModelProperty("金额")
    private String totalAmount;
    @ApiModelProperty("支付金额")
    private String payAmoney;
    @ApiModelProperty("自付金额")
    private String paymentAmount;
    @ApiModelProperty("补贴金额")
    private String discountAmount;
    @ApiModelProperty("第三方支付id")
    private String thirdPayId;
    @ApiModelProperty("支付成功时间")
    private Date paySuccessTime;
    @ApiModelProperty("支付成功时间,字符串类型")
    private String paySuccessTimeStr;
    @ApiModelProperty("退款时间")
    private Date backAmountDate;
    @ApiModelProperty("退款时间字符串")
    private String backAmountDateStr;
    @ApiModelProperty("订单付款状态")
    private String payStatus;
}
