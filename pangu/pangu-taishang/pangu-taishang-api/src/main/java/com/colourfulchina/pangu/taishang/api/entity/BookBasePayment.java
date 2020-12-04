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
 * 预约支付金额表
 */
@Data
@TableName(value = "book_base_payment")
public class BookBasePayment extends Model<BookBasePayment> {
    private static final long serialVersionUID = -6201138731687298469L;

    /**
     * 主键
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    /**
     * product_group_product表id
     */
    @TableField(value = "product_group_product_id")
    private Integer productGroupProductId;
    /**
     * 开始时间
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
     * 预约支付金额(元)
     */
    @TableField(value = "book_price")
    private BigDecimal bookPrice;
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