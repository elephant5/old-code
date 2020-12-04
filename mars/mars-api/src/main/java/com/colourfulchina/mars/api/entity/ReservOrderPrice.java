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

@TableName("reserv_order_price")
@Data
public class ReservOrderPrice extends Model<ReservOrderPrice> {

    private static final long serialVersionUID = -6182564139749708248L;
    @TableId(value = "id" ,type = IdType.AUTO)
    private Integer id	;//预约单号
    @TableField(value = "reserv_order_id" )
    private Integer reservOrderId	;//预约单号
    @TableField(value = "order_date")
    private String orderDate;
    @TableField(value = "price")
    private BigDecimal price;
    @TableField(value = "number")
    private Integer number;//数量
    @TableField(value = "contact")
    private String contact;
    @ApiModelProperty("净价")
    @TableField(value = "net_price")
    private String netPrice;
    @ApiModelProperty("服务费")
    @TableField(value = "service_rate")
    private String serviceRate;
    @ApiModelProperty("税费")
    @TableField(value = "tax_rate")
    private String taxRate;
    @ApiModelProperty("结算规则")
    @TableField(value = "settle_rule")
    private String settleRule;
    @ApiModelProperty("协议价")
    @TableField(value = "protocol_price")
    private BigDecimal protocolPrice;
    @ApiModelProperty("实报价")
    @TableField(value = "real_rice")
    private BigDecimal realPrice;
    @ApiModelProperty("结算方式")
    @TableField(value = "settle_method")
    private String settleMethod;
    @ApiModelProperty("结算币种")
    @TableField(value = "currency")
    private String currency;

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
    /**
     * 主键值
     */
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
