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
 * 商品结算规则表
 */
@Data
@TableName(value = "shop_item_settle_express")
public class ShopItemSettleExpress extends Model<ShopItemSettleExpress> {
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
     * 商品规格ID
     */
    @TableField(value = "shop_item_id")
    private Integer shopItemId;
    /**
     * 净价百分比
     */
    @TableField(value = "net_price_per")
    private BigDecimal netPricePer;
    /**
     * 自定义税费净价百分比
     */
    @TableField(value = "tax_net_price_per")
    private BigDecimal taxNetPricePer;
    /**
     * 服务费百分比
     */
    @TableField(value = "service_fee_per")
    private BigDecimal serviceFeePer;
    /**
     * 自定义税费服务费百分比
     */
    @TableField(value = "tax_service_fee_per")
    private BigDecimal taxServiceFeePer;
    /**
     * 税费百分比
     */
    @TableField(value = "tax_fee_per")
    private BigDecimal taxFeePer;
    /**
     * 自定义增值税百分比
     */
    @TableField(value = "custom_tax_fee_per")
    private BigDecimal customTaxFeePer;
    /**
     * 固定贴现
     */
    @TableField(value = "discount")
    private BigDecimal discount;
    /**
     * 调整金额
     */
    @TableField(value = "adjust")
    private BigDecimal adjust;
    /**
     * 结算公式
     */
    @TableField(value = "settle_express")
    private String settleExpress;
    /**
     * 自定义增值税公式
     */
    @TableField(value = "custom_tax_express")
    private String customTaxExpress;

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