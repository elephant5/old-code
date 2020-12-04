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
 * 权益类型表
 */
@Data
@TableName(value = "sys_gift")
public class Gift extends Model<Gift> {
    /**
     * 权益类型码主键
     */
    @TableId(value = "code",type = IdType.INPUT)
    private String code;
    /**
     * 权益类型全名称
     */
    @TableField(value = "name")
    private String name;
    /**
     * 权益类型简称
     */
    @TableField(value = "short_name")
    private String shortName;
    /**
     * 删除标识（0-正常，1-删除）
     */
    @TableField(value = "del_flag")
    private Integer delFlag;
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

    @Override
    protected Serializable pkVal() {
        return this.code;
    }
}