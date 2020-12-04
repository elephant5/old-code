package com.colourfulchina.pangu.taishang.api.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "outline_refund_info")
public class OutlineRefundInfo extends Model<OutlineRefundInfo> {

    private static final long serialVersionUID = -8604514819657376254L;

    @ApiModelProperty("订单号")
    @TableId(value = "order_no", type = IdType.INPUT)
    private String orderNo;

    @ApiModelProperty("商品名")
    @TableField(value = "goods_name")
    private String goodsName;

    @ApiModelProperty("产品名")
    @TableField(value = "product_name")
    private String productName;

    @ApiModelProperty("商品id")
    @TableField(value = "goods_id")
    private Integer goodsId;

    @ApiModelProperty("产品组id")
    @TableField(value = "product_group_id")
    private Integer productGroupId;

    @ApiModelProperty("商户名称")
    @TableField(value = "merchant_name")
    private String merchantName;

    @ApiModelProperty("酒店名称")
    @TableField(value = "hotel_name")
    private String hotelName;

    @ApiModelProperty("手机号")
    @TableField(value = "phone_reciever")
    private String phoneReciever;

    @ApiModelProperty("收款方")
    @TableField(value = "refund_reciever")
    private String refundReciever;

    @ApiModelProperty("激活码")
    @TableField(value = "act_code")
    private String actCode;

    @ApiModelProperty("订单时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "order_time")
    private Date orderTime;

    @ApiModelProperty("订单来源")
    @TableField(value = "order_source")
    private String orderSource;

    @ApiModelProperty("退款金额")
    @TableField(value = "refund_price")
    private Double refundPrice;

    @ApiModelProperty("退款状态, 0:未退款,1:已退款")
    @TableField(value = "refund_status")
    private Boolean refundStatus;

    @ApiModelProperty("退款时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "refund_time")
    private Date refundTime;

    @ApiModelProperty("退款渠道")
    @TableField(value = "refund_channel")
    private String refundChannel;

    @Override
    protected Serializable pkVal() {
        return this.orderNo;
    }
}
