package com.colourfulchina.pangu.taishang.api.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldStrategy;
import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 产品组和产品对应关系
 */
@Data
@TableName("product_group_product")
public class ProductGroupProduct extends Model<ProductGroupProduct> {
    private static final long serialVersionUID = -5274453942686996627L;

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    /**
     * 产品组id
     */
    @TableField("product_group_id")
    private Integer productGroupId;
    /**
     * 产品id
     */
    @TableField("product_id")
    private Integer productId;
    /**
     * 权益类型
     */
    @TableField("gift")
    private String gift;
    /**
     * 排序
     */
    @TableField("sort")
    private Integer sort;
    /**
     * block规则
     */
    @TableField(value = "block_rule", strategy = FieldStrategy.IGNORED)
    private String blockRule;
    /**
     * 最小成本
     */
    @TableField("min_cost")
    private BigDecimal minCost;
    /**
     * 最高成本
     */
    @TableField("max_cost")
    private BigDecimal maxCost;
    /**
     * 点数
     */
    @TableField("point")
    private BigDecimal point;
    /**
     * 状态（0在售，1停售，2申请停售）
     */
    @TableField("status")
    private Integer status;
    /**
     * 删除标识（0正常，1删除）
     */
    @TableField("del_flag")
    private Integer delFlag;

    @ApiModelProperty("推荐")
    @TableField("recommend")
    private Integer recommend;
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
