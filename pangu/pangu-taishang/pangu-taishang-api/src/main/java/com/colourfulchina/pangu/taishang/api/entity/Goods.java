package com.colourfulchina.pangu.taishang.api.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldStrategy;
import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品表
 */
@Data
@TableName(value = "goods")
public class Goods extends Model<Goods> {
    private static final long serialVersionUID = 1951482888936767092L;
    @ApiModelProperty("商品主键")
   @TableId(value = "id" ,type = IdType.AUTO)
    private Integer id ;
    @ApiModelProperty("商品名称")
   @TableField(value = "name")
    private String   name;
    @ApiModelProperty("商品内部简称")
   @TableField(value = "short_name")
    private String shortName;
    @ApiModelProperty("佣金")
   @TableField(value = "commission")
    private BigDecimal commission;
    @ApiModelProperty("佣金类型（0 比例 1 金额）")
    @TableField(value = "commission_type")
    private String commissionType;

    @ApiModelProperty("零售价")
   @TableField(value = "sales_price")
    private BigDecimal salesPrice;
    @ApiModelProperty("市场价")
   @TableField(value = "market_price")
    private BigDecimal marketPrice ;
    @ApiModelProperty("验证方式")
   @TableField(value = "auth_type")
    private String authType ;
    @ApiModelProperty("上架开始时间")
   @TableField(value = "up_start_time")
    private Date upstartTime;
    @ApiModelProperty("下架时间")
   @TableField(value = "down_end_time")
    private Date downEndTime ;
    @ApiModelProperty("最多提前N预定 默认一天")
   @TableField(value = "max_book_days",strategy = FieldStrategy.IGNORED)
    private Integer  maxBookDays;
    @ApiModelProperty("最少提前N预定 默认一天")
   @TableField(value = "min_book_days",strategy = FieldStrategy.IGNORED)
    private Integer  minBookDays ;
    @ApiModelProperty("权益有效期")
   @TableField(value = "expiry_date")
    private String  expiryDate;
    @ApiModelProperty("权益有效期公式")
    @TableField(value = "expiry_value")
    private String  expiryValue;
    @ApiModelProperty("上架状态")
   @TableField(value = "status")
    private String  status;
    @ApiModelProperty("商品block")
   @TableField(value = "block",strategy = FieldStrategy.IGNORED)
    private String  block ;
    @ApiModelProperty("热线号码")
   @TableField(value = "hotline")
    private String hotline ;
    @ApiModelProperty("是否短信通知")
   @TableField(value = "message_notice")
    private String messageNotice;
    @ApiModelProperty("商品备注")
   @TableField(value = "remark")
    private String  remark ;
    @ApiModelProperty("来电提醒")
   @TableField(value = "call_reminder")
    private String callReminder;
    @ApiModelProperty("客户来源(银行硬性提示要求）")
   @TableField(value = "custom_source")
    private String customSource;
    @ApiModelProperty("核销提醒")
   @TableField(value = "verify_reminder")
    private String verifyReminder ;
    @TableField(value = "old_key")
    private String oldKey ;
    @ApiModelProperty("删除标识（0-正常，1-删除）")
   @TableField(value = "del_flag")
    private Integer delFlag;
    @ApiModelProperty("创建时间")
   @TableField(value = "create_time")
    private Date  createTime ;
    @ApiModelProperty("创建人")
   @TableField(value = "create_user")
    private String createUser;
    @ApiModelProperty("更新时间")
   @TableField(value = "update_time",update = "CURRENT_TIMESTAMP()")
    private Date updateTime;
    @ApiModelProperty("更新人")
   @TableField(value = "update_user")
    private String updateUser;
    @ApiModelProperty("上架方式")
    @TableField(value = "up_Type")
    private String upType ="0";


    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}