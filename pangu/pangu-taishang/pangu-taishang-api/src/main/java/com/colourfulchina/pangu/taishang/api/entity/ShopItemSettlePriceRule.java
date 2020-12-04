package com.colourfulchina.pangu.taishang.api.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import org.springframework.data.annotation.Transient;

import java.io.Serializable;
import java.util.Date;

/**
 * 商户规格结算规则
 */
@Data
@TableName(value = "shop_item_settle_price_rule")
public class ShopItemSettlePriceRule extends Model<ShopItemSettlePriceRule> {
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

    @TableField(value = "shop_item_price_rule_id")
    private Integer shopItemPriceRuleId;
    private String gift;
    private Date startDate;
    private Date endDate;
    private int monday;
    private int tuesday;
    private int wednesday;
    private int thursday;
    private int friday;
    private int saturday;
    private int sunday;
    @TableField(value = "settle_express_id")
    private Integer settleExpressId;
    /**
     * 删除标识（0-正常，1-删除）
     */
    @TableField(value = "del_flag")
    private Integer delFlag;
    /**
     * 0启动1禁用，删除恢复时候使用
     */
    @TableField(value = "status")
    private Integer status;
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

    @Transient
    public boolean isEnable(int weekDay){
        switch (weekDay){
            case 0:
                return this.getSunday()==1;
            case 1:
                return this.getMonday()==1;
            case 2:
                return this.getTuesday()==1;
            case 3:
                return this.getWednesday()==1;
            case 4:
                return this.getTuesday()==1;
            case 5:
                return this.getFriday()==1;
            case 6:
                return this.getSaturday()==1;
            default:
                return false;
        }
    }
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}