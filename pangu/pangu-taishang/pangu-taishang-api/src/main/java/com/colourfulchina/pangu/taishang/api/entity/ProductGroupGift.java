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
 * 产品组和权益类型对应关系
 */
@Data
@TableName("product_group_gift")
public class ProductGroupGift extends Model<ProductGroupGift> {
    private static final long serialVersionUID = -6874153770414367537L;

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    /**
     * 产品组id
     */
    @TableField("product_group_id")
    private Integer productGroupId;
    /**
     * 权益类型
     */
    @TableField("gift")
    private String gift;
    /**
     * 删除标识（0正常，1删除）
     */
    @TableField("del_flag")
    private Integer delFlag;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 创建人
     */
    @TableField("create_user")
    private String createUser;
    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;
    /**
     * 更新人
     */
    @TableField("update_user")
    private String updateUser;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
