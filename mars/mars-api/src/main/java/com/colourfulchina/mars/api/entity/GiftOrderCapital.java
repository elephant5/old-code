package com.colourfulchina.mars.api.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@TableName("gift_order_capital")
@Data
public class GiftOrderCapital extends Model<GiftOrderCapital> {
	
	private static final long serialVersionUID = 7186959137072439091L;

	@ApiModelProperty("激活码订单主表ID")
	@TableId(value = "id",type = IdType.AUTO)
	private Integer id; 
	
	@ApiModelProperty("主订单ID")
	@TableField(value = "capital_order_id")
	private String capitalOrderId;    
	
	@ApiModelProperty("所属渠道")
	@TableField(value = "ac_channel")
	private String acChannel;      
	
	@ApiModelProperty("购买人手机号")
	@TableField(value = "buyer_mobile")
	private String buyerMobile;   
	
	@ApiModelProperty("激活人会员ID")
	@TableField(value = "member_id")
	private Long memberId;
	
	@ApiModelProperty("短信标识")
	@TableField(value = "sms_tag")
	private Integer smsTag;         
	
	@ApiModelProperty("激活标识")
	@TableField(value = "activate_tag")
	private Integer activateTag;   
	
	@ApiModelProperty("订单总金额")
	@TableField(value = "total_amount")
	private BigDecimal totalAmount;   
	
	@ApiModelProperty("订单实付金额")
	@TableField(value = "order_amount")
	private BigDecimal orderAmount;   
	
	@ApiModelProperty("订单优惠金额")
	@TableField(value = "offer_amount")
	private BigDecimal offerAmount;       
	
	@ApiModelProperty("优惠券ID")
	@TableField(value = "coupons_id")
	private String couponsId;         
	
	@ApiModelProperty("优惠券类型")
	@TableField(value = "coupons_type")
	private Integer couponsType;       
	
	@ApiModelProperty("消耗的积分")
	@TableField(value = "integral")
	private Integer integral;           
	
	@ApiModelProperty("是否需要支付")
	@TableField(value = "pay_tag")
	private Integer payTag;            
	
	@ApiModelProperty("第三方支付单号")
	@TableField(value = "pay_number")
	private String payNumber;         
	
	@ApiModelProperty("订单支付时间")
	@TableField(value = "pay_time")
	private Date payTime;           
	
	@ApiModelProperty("订单支付方式")
	@TableField(value = "pay_method")
	private String payMethod;         
	
	@ApiModelProperty("订单状态")
	@TableField(value = "status")
	private Integer status;            
	
	@ApiModelProperty("是否分期付款")
	@TableField(value = "hire_purchase_tag")
	private Integer hirePurchaseTag;  
	
	@ApiModelProperty("分期付款期数")
	@TableField(value = "hire_purchase_num")
	private Integer hirePurchaseNum;  
	
	@ApiModelProperty("分期付款每期金额")
	@TableField(value = "hire_purchase_amount")
	private BigDecimal hirePurchaseAmount;
	
	@ApiModelProperty("退款时间")
	@TableField(value = "refund_time")
	private Date refundTime;        
	
	@ApiModelProperty("退款金额")
	@TableField(value = "refund_amount")
	private BigDecimal refundAmount;      
	
	@ApiModelProperty("下单时间")
	@TableField(value = "create_time")
	private Date createTime;        
	
	@ApiModelProperty("更新时间")
	@TableField(value = "update_time",update = "CURRENT_TIMESTAMP()")
	private Date updateTime; 
	
	@Override
    protected Serializable pkVal() {
        return this.id;
    }

}
