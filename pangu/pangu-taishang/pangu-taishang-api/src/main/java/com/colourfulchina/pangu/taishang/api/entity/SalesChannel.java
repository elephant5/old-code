package com.colourfulchina.pangu.taishang.api.entity;

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

/**
 * 渠道
 *
 * @Author: luck.luo
 * @Description:
 * @Date: 2019/2/25 15:35
 */
@Data
@TableName(value = "sales_channel")
public class SalesChannel extends Model<SalesChannel> {
    @ApiModelProperty("渠道主键")
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    @ApiModelProperty("银行ID")
    @TableField(value = "bank_id")
    private String bankId;
    @ApiModelProperty("销售渠道ID")
    @TableField(value = "sales_channel_id")
    private String salesChannelId;
    @ApiModelProperty("销售方式ID")
    @TableField(value = "sales_way_id")
    private String salesWayId;
    @ApiModelProperty("结算方式ID")
    @TableField(value = "settle_method_id")
    private String settleMethodId;
    @ApiModelProperty("开票节点ID")
    @TableField(value = "invoice_node_id")
    private String invoiceNodeId;
    @ApiModelProperty("开票对象ID")
    @TableField(value = "invoice_obj_id")
    private String invoiceObjId;
    @ApiModelProperty("佣金")
    private BigDecimal commision;
    @ApiModelProperty("状态 (0-启用 1-停用)")
    private Integer status;
    @ApiModelProperty("排序")
    @TableField(value = "orders")
    private Integer orders;
    @ApiModelProperty("删除标识（0-正常，1-删除）")
    @TableField(value = "del_flag")
    private Integer delFlag;
    @ApiModelProperty("创建时间")
    @TableField(value = "create_time")
    private Date createTime;
    @ApiModelProperty("创建人")
    @TableField(value = "create_user")
    private String createUser;
    @ApiModelProperty("更新时间")
    @TableField(value = "update_time",update = "CURRENT_TIMESTAMP()")
    private Date updateTime;
    @ApiModelProperty("更新人")
    @TableField(value = "update_user")
    private String updateUser;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }


}
