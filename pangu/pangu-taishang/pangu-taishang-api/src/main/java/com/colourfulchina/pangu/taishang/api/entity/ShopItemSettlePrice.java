package com.colourfulchina.pangu.taishang.api.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品规格历史每日价格表(数据每天跑批进来)
 */
@Data
@TableName(value = "shop_item_settle_price")
public class ShopItemSettlePrice extends Model<ShopItemSettlePrice> {
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
     * 商品规格ID
     */
    @TableField(value = "shop_item_id")
    private Integer shopItemId;

    /**
     * 预约单id
     */
    private Integer bookOrderId;
    /**
     * 结算价
     */
    @TableField(value = "settle_price")
    private BigDecimal settlePrice;
    /**
     * 日期
     */
    @TableField(value = "book_date")
    private Date bookDate;
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