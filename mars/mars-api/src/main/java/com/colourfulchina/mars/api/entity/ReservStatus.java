package com.colourfulchina.mars.api.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * reserv_status 实体
 *
 * @Author: luck.luo
 * @Description:
 * @Date: 2019/5/15 9:43
 */
@Data
@TableName(value = "reserv_status")
public class ReservStatus  extends Model<ReservStatus> {
    @ApiModelProperty("交易ID")
    @TableField(value = "order_id")
    private Integer orderId;
    @ApiModelProperty("状态id")
    @TableField(value = "status_id")
    private Integer statusId;
    @ApiModelProperty("审核ID")
    @TableField(value = "value_id")
    private Integer valueId;
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
        return orderId+""+statusId+""+valueId;
    }
}
