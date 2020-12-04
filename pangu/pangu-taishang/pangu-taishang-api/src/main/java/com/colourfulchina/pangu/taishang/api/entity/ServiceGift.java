package com.colourfulchina.pangu.taishang.api.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 服务对应的权益类型关系表
 */
@Data
@TableName(value = "sys_service_gift")
public class ServiceGift extends Model<ServiceGift> {
    /**
     * 主键id
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    /**
     * 服务类型id
     */
    @TableField(value = "service_id")
    private String serviceId;
    /**
     * 权益类型id
     */
    @TableField(value = "gift_id")
    private String giftId;
    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;
    /**
     * 创建人
     */
    @TableField(value = "create_user")
    private String createUser;
    /**
     * 更新时间
     */
    @TableField(value = "update_time",update = "CURRENT_TIMESTAMP()")
    private Date updateTime;
    /**
     * 更新人
     */
    @TableField(value = "update_user")
    private String updateUser;
    /**
     * 删除标识（0-正常，1-删除）
     */
    @TableField(value = "del_flag")
    private Integer delFlag;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}