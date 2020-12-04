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
 * 商户资源表
 */
@Data
@TableName(value = "shop_channel")
public class ShopChannel extends Model<ShopChannel> {

    /**
     * 商户资源主键
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    /**
     * 旧系统id
     */
    @TableField("old_id")
    private Integer oldId;
    /**
     * 商户资源名称
     */
    @TableField(value = "name")
    private String name;
    /**
     * (1公司资源，0第三方资源)
     */
    @TableField(value = "internal")
    private Integer internal;
    /**
     * 结算方式
     */
    @TableField(value = "settle_method")
    private String settleMethod;
    /**
     * 结算币种
     */
    @TableField(value = "currency")
    private String currency;
    /**
     * (0正常，1删除)
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

    /**
     * 类型能选择的渠道
     */
    @TableField(value = "service")
    private String service;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}