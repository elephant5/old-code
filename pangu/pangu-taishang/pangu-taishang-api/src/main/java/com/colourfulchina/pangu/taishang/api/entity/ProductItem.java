package com.colourfulchina.pangu.taishang.api.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 产品子项
 */
@Data
@TableName("product_item")
public class ProductItem extends Model<ProductItem> {
    private static final long serialVersionUID = -6038822338864931344L;

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    /**
     * 产品id
     */
    @TableField("product_id")
    private Integer productId;
    /**
     * 名称
     */
    @TableField("name")
    private String name;
    /**
     * 资源类型类型
     */
    @TableField("service")
    private String service;
    /**
     * 权益类型
     */
    @TableField("gift")
    private String gift;
    /**
     * 适用时间
     */
    @TableField("apply_time")
    private String applyTime;
    /**
     * 有效时间
     */
    @TableField(value = "start_date")
    private Date startDate;
    /**
     * 截止时间
     */
    @TableField(value = "end_date")
    private Date endDate;
    /**
     * 周一
     */
    @TableField(value = "monday")
    private int monday;
    /**
     * 周二
     */
    @TableField(value = "tuesday")
    private int tuesday;
    /**
     * 周三
     */
    @TableField(value = "wednesday")
    private int wednesday;
    /**
     * 周四
     */
    @TableField(value = "thursday")
    private int thursday;
    /**
     * 周五
     */
    @TableField(value = "friday")
    private int friday;
    /**
     * 周六
     */
    @TableField(value = "saturday")
    private int saturday;
    /**
     * 周日
     */
    @TableField(value = "sunday")
    private int sunday;
    /**
     * 成本
     */
    @TableField("cost")
    private BigDecimal cost;
    /**
     * block规则
     */
    @TableField("block_rule")
    private String blockRule;
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
