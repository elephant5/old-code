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
 * 产品组产品block日期表
 */
@Data
@TableName("group_product_block_date")
public class GroupProductBlockDate extends Model<GroupProductBlockDate> {
    private static final long serialVersionUID = -8312922041053341503L;

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    /**
     * 产品组产品id
     */
    @TableField("product_group_product_id")
    private Integer productGroupProductId;
    /**
     * block时间
     */
    @TableField("block_date")
    private Date blockDate;
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
