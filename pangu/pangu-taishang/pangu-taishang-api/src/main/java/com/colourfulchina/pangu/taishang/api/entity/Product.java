package com.colourfulchina.pangu.taishang.api.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldStrategy;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 产品
 */
@Data
@TableName("product")
public class Product extends Model<Product> {
    private static final long serialVersionUID = 5689513289721390246L;

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    /**
     * 商户id
     */
    @TableField("shop_id")
    private Integer shopId;
    /**
     * 商户规格id
     */
    @TableField("shop_item_id")
    private Integer shopItemId;
    /**
     * 权益类型
     */
    @TableField("gift")
    private String gift;
    /**
     * 最小成本
     */
    @TableField(value = "min_cost",strategy = FieldStrategy.IGNORED)
    private BigDecimal minCost;
    /**
     * 最大成本
     */
    @TableField(value = "max_cost",strategy = FieldStrategy.IGNORED)
    private BigDecimal maxCost;
    /**
     * 星期适用时间(废弃)
     */
    @TableField("apply_time")
    private String applyTime;
    /**
     * 结算规则id(废弃)
     */
    @TableField("settle_price_id")
    private Integer settlePriceId;
    /**
     * 成本（废弃）
     */
    @TableField("cost")
    private BigDecimal cost;
    /**
     * block规则（废弃）
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
    /**
     * 状态（0在售，1停售）
     */
    @TableField("status")
    private Integer status;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
