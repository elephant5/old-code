package com.colourfulchina.pangu.taishang.api.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import org.springframework.data.annotation.Transient;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 商户规格净价规则
 */
@Data
@TableName(value = "shop_item_net_price_rule")
public class ShopItemNetPriceRule extends Model<ShopItemNetPriceRule> {

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
     * 净价(元)
     */
    @TableField(value = "net_price")
    private BigDecimal netPrice;
    /**
     * 服务费费率
     */
    @TableField(value = "service_rate")
    private BigDecimal serviceRate;
    /**
     * 税费费率
     */
    @TableField(value = "tax_rate")
    private BigDecimal taxRate;
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