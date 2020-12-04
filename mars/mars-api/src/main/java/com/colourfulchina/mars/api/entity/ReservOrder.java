package com.colourfulchina.mars.api.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldStrategy;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName(value = "reserv_order")
public class ReservOrder extends Model<ReservOrder> {

    private static final long serialVersionUID = -3813215562729412187L;
    @TableId(value = "id" ,type = IdType.AUTO)
    private Integer id	;//预约单号
    @TableField(value = "old_order_id")
    private Integer oldOrderId	;//Charlie系统预约单号
    @TableField(value = "service_type")
    private String serviceType;//	产品类型：自助餐、机场服务等
    @TableField(value = "gift_type")
    private String giftType;//	权益类型：二免一，双免等
    @TableField(value = "product_id",strategy = FieldStrategy.IGNORED)
    private Integer productId;//产品id
    @TableField(value = "goods_id")
    private Integer goodsId	;//	商品id
    @TableField(value = "product_group_id")
    private Integer productGroupId;//	产品组id
    @TableField(value = "product_group_product_id")
    private Integer productGroupProductId;//	产品组产品id
    @TableField(value = "intere_usage_count")
    private Integer intereUsageCount	;//权益产品使用次数
    @TableField(value = "member_id")
    private Long memberId	;//用户id

    @TableField(value = "sales_channle_id")
    private Integer salesChannleId;//商户渠道
    @TableField(value = "reserv_number")
    private String reservNumber;//	确认号
    @TableField(value = "channel_number")
    private String channelNumber;//	渠道号如果是第三方

    @TableField(value = "act_state_time")
    private String actStateTime;//激活开始时间
    @TableField(value = "shop_channel_id")
    private Integer shopChannelId;//销售渠道渠道

    @TableField(value = "order_creator")
    private String orderCreator;//	接单人
    @TableField(value = "operator")
    private String operator	;//处理人
    @TableField(value = "tags")
    private String tags	;//标签id
    @TableField(value = "prose_status")
    private String proseStatus	;//预约状态
    @TableField(value = "gift_code_id")
    private Integer giftCodeId; //激活码ID

    @TableField(value = "shop_id")
    private Integer shopId	;//商户id


    @TableField(value = "shop_item_id")
    private Integer shopItemId	;//商户id

    @TableField(value = "order_settle_amount")
    private BigDecimal orderSettleAmount;//	结算总额
    @TableField(value = "shop_amount")
    private BigDecimal shopAmount	;//应付金额
    @TableField(value = "order_damage_amount")
    private BigDecimal orderDamageAmount	;//商户赔付金额

    @TableField(value = "reserv_remark")
    private String reservRemark	;//备注
    @TableField(value = "gift_name")
    private String giftName;//	预约人姓名
    @TableField(value = "gift_phone")
    private String giftPhone	;//预约电话

    @TableField(value = "gift_date")
    private String giftDate	;//预约日期 2019-01-12
    @TableField(value = "gift_time")
    private String giftTime	;//预约时间  12：00
    @TableField(value = "exchange_num")
    private Integer exchangeNum	;//兑换数量callReminder
    @TableField(value = "gift_solt")
    private String giftSolt	;//预约时段
    @TableField(value = "gift_people_num")
    private Integer giftPeopleNum;//	预约人数
    @TableField(value = "cancel_reason")
    private String cancelReason;//	取消原因
    @TableField(value = "cancel_time")
    private String cancelTime;//	取消时间
    @TableField(value = "cancel_date")
    private Date cancelDate;//	取消日期
    @TableField(value = "fail_date")
    private Date failDate;//	失败日期
    @TableField(value = "reserv_date")
    private Date reservDate;//	预约日期
    @TableField(value = "success_date")
    private Date successDate;//	预约日期

    @TableField(value = "dispensing")
    private Integer dispensing;//	是否调剂
    @TableField(value = "refund_inter")
    private Integer refundInter;//	是否退回权益
    @TableField(value = "cancel_operator")
    private String cancelOperator;//	取消操作人
    @TableField(value = "cancel_damage_amount")
    private BigDecimal cancelDamageAmount	;//商户赔付金额
    @TableField(value = "cancel_is_pay")
    private BigDecimal cancelIsPay	;//是否商户赔付金额


    @TableField(value = "fail_reason")
    private String failReason	;//失败原因
    @TableField(value = "fail_time")
    private Date failTime	;//失败申请时间
    @TableField(value = "fail_inter")
    private String failInter	;//失败是否退回权益
    @TableField(value = "fail_operator")
    private String failOperator;//	失败操作人
    @TableField(value = "message_flag")
    private Integer messageFlag	;//是否发送短信
    @TableField(value = "sales_order_id")
    private String salesOrderId	;//销售单id
    @TableField(value = "resource_type")
    private Integer resourceType	;//资源名称id


    @TableField(value = "del_flag")
    private Integer delFlag	;//删除标识（0-正常，1-删除）
    @TableField(value = "create_time")
    private Date createTime;//	创建时间
    @TableField(value = "create_user")
    private String createUser	;//创建人
    @TableField(value = "update_time",update = "CURRENT_TIMESTAMP()")
    private Date updateTime	;//更新时间
    @TableField(value = "update_user")
    private String updateUser	;//更新人

    @TableField(value = "reserv_code_id")
    private Integer reservCodeId;//核销码id

    @TableField(value = "pay_status")
    private Integer payStatus;//支付状态
    @TableField(value = "order_source")
    private String  orderSource;//订单来源

    @TableField(value = "third_cpn_no")
    private Long thirdCpnNo;  //第三方券id
    @TableField("third_cpn_num")
    private String thirdCpnNum; //第三方产品编号
    @TableField("third_cpn_name")
    private String thirdCpnName; //第三方产品名称

    @TableField("cpn_type")
    private Integer cpnType; //券类型

    @TableField("cpn_id")
    private String cpnId; //券编号

    @TableField("total_amount")
    private BigDecimal totalAmount;

    @TableField("discount_amount")
    private BigDecimal discountAmount;

    @TableField("pay_amount")
    private BigDecimal payAmount;
    @TableField("code_detail_id")
    private Integer codeDetailId;
    //迁移状态（0正常，1迁移中）
    @TableField("move_type")
    private Integer moveType;


    //航班号
    @TableField(value = "flight_number")
    private String flightNumber;
    //机场
    @TableField(value = "airport")
    private String  airport;
    //目的地
    @TableField(value = "end_point")
    private String endPoint;
    //出发地
    @TableField(value = "start_point")
    private String startPoint;
    //出行类型 1-接机 2-送机
    @TableField(value = "travel_type")
    private Integer travelType;
    //备注
    @TableField(value = "remark")
    private String  remark;
    //当前预约单使用掉的免费次数
    @TableField(value = "use_free_count")
    private Integer  useFreeCount;
    /**
     * 主键值
     */
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
