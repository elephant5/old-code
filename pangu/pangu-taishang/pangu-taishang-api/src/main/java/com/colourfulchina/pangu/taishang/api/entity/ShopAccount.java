package com.colourfulchina.pangu.taishang.api.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.colourfulchina.pangu.taishang.api.enums.ShopAccountTypeEnums;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 商户账户表
 */
@Data
@TableName(value = "shop_account")
public class ShopAccount extends Model<ShopAccount> {
    private static final long serialVersionUID = 551702799828320228L;
    /**
     * 主键
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    /**
     * 商户ID
     */
    @TableField(value = "shop_id")
    private Integer shopId;
    /**
     * 酒店ID
     */
    @TableField(value = "hotel_id")
    private Integer hotelId;
    /**
     * 账户类型(0商户1酒店2集团） 默认为商户
     */
    @TableField(value = "account_type")
    private Integer accountType = ShopAccountTypeEnums.SHOP.getCode();
    /**
     * 用户名
     */
    @TableField(value = "username")
    private String username;
    /**
     * 密码
     */
    @TableField(value = "password")
    private String password;
    /**
     * 账户状态，0正常，1离职
     */
    @TableField(value = "status")
    private Integer status;


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
        return this.id;
    }
}