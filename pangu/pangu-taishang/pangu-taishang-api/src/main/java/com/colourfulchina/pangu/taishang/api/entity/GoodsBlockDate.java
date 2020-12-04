package com.colourfulchina.pangu.taishang.api.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 商品特定日期block
 */
@Data
@TableName(value = "goods_block_date")
public class GoodsBlockDate extends Model<GoodsBlockDate> {
    /**
     * 主键
     */
    @TableId(value = "id")
    private Integer id;
    /**
     * 商户ID
     */
    @TableField(value = "shop_id")
    private Integer shopId;
    /**
     * 商户规格ID
     */
    @TableField(value = "shop_item_id")
    private Integer shopItemId;
    /**
     * 商品ID
     */
    @TableField(value = "goods_id")
    private Integer goodsId;
    /**
     * block日期
     */
    @TableField(value = "block_date")
    private Date blockDate;
    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

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