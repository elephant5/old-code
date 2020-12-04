package com.colourfulchina.mars.api.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName(value = "reserv_order_detail")
public class ReservOrderDetail extends Model<ReservOrderDetail> {

    private static final long serialVersionUID = -3078925860023415577L;
    @TableField(value = "id")
    private Integer id;//预约单附属表id
    @TableField(value = "order_id")
    private Integer orderId;//预约单主键
    @TableField(value = "product_type")
    private String productType;//	类型
    @TableField(value = "goods_id")
    private Integer goodsId;//	项目ID
    @TableField(value = "check_date")
    private String checkDate;//入住日期
    @TableField(value = "depar_date")
    private String deparDate;//离开时间
    @TableField(value = "check_night")
    private Integer checkNight;//	入住多少间
    @TableField(value = "night_numbers")
    private Integer nightNumbers;//	入住多少夜
    @TableField(value = "acco_addon")
    private String accoAddon;//	房型 例如 豪华/无早餐
    @TableField(value = "acco_nedds")
    private String accoNedds;//床型 大/双
    @TableField(value = "ser_num")
    private Integer serNum;//	乘车人数
    @TableField(value = "ser_city")
    private String serCity;//使用城市
    @TableField(value = "flight_num")
    private String flightNum;//	航班号
    @TableField(value = "sail_route")
    private String sailroute;//	航段-航向线路起始
    @TableField(value = "board_address")
    private String boardAddress;//	乘车地址
    @TableField(value = "dest_address")
    private String destAddress;//目的地址
    @TableField(value = "del_flag")
    private Integer delFlag;//删除标识（0-正常，1-删除）
    @TableField(value = "create_time")
    private Date createTime;//	创建时间
    @TableField(value = "create_user")
    private String createUser;//创建人check_room
    @TableField(value = "update_time",update = "CURRENT_TIMESTAMP()")
    private Date updateTime;//更新时间
    @TableField(value = "update_user")
    private String updateuser;//更新人

    @TableField(value = "book_name")
    private String bookName;//预订人名称
    @TableField(value = "book_name_en")
    private String bookNameEn;//预订人名称拼音
    @TableField(value = "book_phone")
    private String bookPhone;//预订人手机号
    @TableField(value = "book_id_type")
    private String bookIdType;//预订人证件类型
    @TableField(value = "book_id_num")
    private String bookIdNum;//预订人证件号

    @TableField(value = "total_amoney")
    private BigDecimal totalAmoney	 = BigDecimal.ZERO;;//商品总额
    @TableField(value = "discount_amoney")
    private BigDecimal discountAmoney	 = BigDecimal.ZERO;;//折扣金额
    @TableField(value = "pay_amoney")
    private BigDecimal payAmoney  = BigDecimal.ZERO;//支付金额
    @TableField(value = "back_amount")
    private BigDecimal backAmount = BigDecimal.ZERO;//退款金额
    @TableField(value = "back_amount_date")
    private Date backAmountDate ;//退款时间
    @TableField(value = "back_amount_status")
    private Integer backAmountStatus;//退款状态
    @TableField(value = "back_fail_resean")
    private String backFailResean;//退款失败原因
    @TableField(value = "sales_ratio")
    private BigDecimal salesRatio;//折扣比例
    @TableField(value = "pay_channel")
    private String payChannel;//支付渠道名称
    @TableField(value = "payment_amount")
    private BigDecimal paymentAmount = BigDecimal.ZERO;//实付金额

    @TableField(value = "pay_order_id")
    private Integer payOrderId;//付款订单ID
    @TableField(value = "back_order_id")
    private Integer backOrderId;//退款订单ID
    @TableField("pay_success_time")
    private Date paySuccessTime;//支付成功时间
    /**
     * 重要通知
     */
    @TableField("notice")
    private String notice;
    /**
     * 主键值
     */
    @Override
    protected Serializable pkVal() {
        return null;
    }
}
