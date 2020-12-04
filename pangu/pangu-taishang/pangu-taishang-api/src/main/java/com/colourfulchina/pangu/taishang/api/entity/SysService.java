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
 * 服务类型
 */
@Data
@TableName(value = "sys_service")
public class SysService extends Model<SysService> {
    /**
     * 服务类型码主键
     */
    @TableId(value = "code",type = IdType.INPUT)
    private String code;
    /**
     * 服务类型名称
     */
    @TableField(value = "name")
    private String name;
    /**
     * 生成产品的方式（0-直接生成，1按权益类型生成）
     */
    @TableField(value = "product_type")
    private Integer productType;
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