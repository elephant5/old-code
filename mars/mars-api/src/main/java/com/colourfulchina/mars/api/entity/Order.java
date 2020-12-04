package com.colourfulchina.mars.api.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@TableName("order")
@Data
public class Order extends Model<Order> {
    private static final long serialVersionUID = -497626370615916054L;

    /**
     * 销售单id
     */
    @ApiModelProperty("销售单id")
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    /**
     * 批次id
     */
    @ApiModelProperty("批次id")
    @TableField("order_batch_id")
    private String orderBatchId;
    /**
     * 销售单状态
     */
    @ApiModelProperty("销售单状态")
    @TableField("order_status")
    private String orderStatus;
    /**
     * 销售日期
     */
    @ApiModelProperty("销售日期")
    @TableField("order_sales_time")
    private Date orderSalesTime;
    /**
     * 销售渠道名称
     */
    @ApiModelProperty("销售渠道名称")
    @TableField("sales_channel_name")
    private String salesChannelName;
    /**
     * 销售渠道id
     */
    @ApiModelProperty("销售渠道id")
    @TableField("sales_channel_id")
    private Integer salesChannelId;
    /**
     * 项目权益id
     */
    @ApiModelProperty("项目权益id")
    @TableField("product_id")
    private Integer productId;
    /**
     * 项目权益名称
     */
    @ApiModelProperty("项目权益名称")
    @TableField("product_name")
    private String productName;
    /**
     * 商品编号
     */
    @ApiModelProperty("商品编号")
    @TableField("goods_id")
    private Integer goodsId;
    /**
     * 商品名称
     */
    @ApiModelProperty("商品名称")
    @TableField("goods_name")
    private String goodsName;
    /**
     * 商品型号
     */
    @ApiModelProperty("商品型号")
    @TableField("goods_model")
    private String goodsModel;
    /**
     * 第三方订单号
     */
    @ApiModelProperty("第三方订单号")
    @TableField("order_other_no")
    private String orderOtherNo;
    /**
     * 来源方式：1批量导入|2接口导入|3手工录入
     */
    @ApiModelProperty("来源方式：1批量导入|2接口导入|3手工录入")
    @TableField("mode_type")
    private String modeType;
    /**
     * 支付方式
     */
    @ApiModelProperty("支付方式")
    @TableField("pay_type")
    private String payType;
    /**
     * 支付流水
     */
    @ApiModelProperty("支付流水")
    @TableField("pay_order_no")
    private String payOrderNo;
    /**
     * 币种 cny
     */
    @ApiModelProperty("币种 cny")
    @TableField("currency_type")
    private String currencyType;
    /**
     * 销售单价
     */
    @ApiModelProperty("销售单价")
    @TableField("sales_unit_price")
    private BigDecimal salesUnitPrice;
    /**
     * 销售价
     */
    @ApiModelProperty("销售价")
    @TableField("sales_price")
    private BigDecimal salesPrice;
    /**
     * 佣金比例
     */
    @ApiModelProperty("佣金比例")
    @TableField("channel_rate")
    private String channelRate;
    /**
     * 积分抵现金金额
     */
    @ApiModelProperty("积分抵现金金额")
    @TableField("points_cash")
    private String pointsCash;
    /**
     * 优惠券抵现金金额
     */
    @ApiModelProperty("优惠券抵现金金额")
    @TableField("coupon_price")
    private String couponPrice;
    /**
     * 购买份数
     */
    @ApiModelProperty("购买份数")
    @TableField("buy_num")
    private Integer buyNum;
    /**
     * 购买人信息
     */
    @ApiModelProperty("购买人信息")
    @TableField("buy_people")
    private String buyPeople;
    /**
     * 购买人手机号
     */
    @ApiModelProperty("购买人手机号")
    @TableField("buy_phone")
    private String buyPhone;
    /**
     * 权益使用次数
     */
    @ApiModelProperty("权益使用次数")
    @TableField("product_usage_num")
    private Integer productUsageNum;
    /**
     * 激活码
     */
    @ApiModelProperty("激活码")
    @TableField("order_code")
    private String orderCode;
    /**
     * 客户类型
     */
    @ApiModelProperty("客户类型")
    @TableField("customer_type")
    private String customerType;
    /**
     * 退货状态
     */
    @ApiModelProperty("退货状态")
    @TableField("return_status")
    private String returnStatus;
    /**
     * 接单人
     */
    @ApiModelProperty("接单人")
    @TableField("order_creator")
    private String orderCreator;
    /**
     * 备注
     */
    @ApiModelProperty("备注")
    @TableField("remarks")
    private String remarks;
    /**
     * 操作人
     */
    @ApiModelProperty("操作人")
    @TableField("operator")
    private String operator;
    /**
     * 倒入json串
     */
    @ApiModelProperty("倒入json串")
    @TableField("import_json")
    private String importJson;
    /**
     * 是否发送短信：0未发送，1已发送,2发送失败
     */
    @ApiModelProperty("是否发送短信：0未发送，1已发送,2发送失败")
    @TableField("send_sms_flg")
    private String sendSmsFlg;
    /**
     * 是否开发票：0不开，1开，默认不开
     */
    @ApiModelProperty("是否开发票：0不开，1开，默认不开")
    @TableField("receipt_flg")
    private String receiptFlg;
    /**
     * 删除标识（0正常，1删除）
     */
    @TableField("del_flag")
    private Integer delFlag;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 创建人
     */
    @TableField("create_user")
    private String createUser;
    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;
    /**
     * 更新人
     */
    @TableField("update_user")
    private String updateUser;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
