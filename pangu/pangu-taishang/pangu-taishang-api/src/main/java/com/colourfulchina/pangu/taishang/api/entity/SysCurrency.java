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
 * 货币类型
 */
@Data
@TableName(value = "sys_currency")
public class SysCurrency extends Model<SysCurrency> {
    /**
     * 货币类型码主键
     */
    @TableId(value = "code",type = IdType.INPUT)
    private String code;
    /**
     * 货币类型名称
     */
    @TableField(value = "name")
    private String name;
    /**
     * 货币符号
     */
    @TableField(value = "symbol")
    private String symbol;
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
        return this.code;
    }
}