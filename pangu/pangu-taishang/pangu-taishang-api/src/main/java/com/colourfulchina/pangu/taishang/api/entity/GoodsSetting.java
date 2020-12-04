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
 * 商品设置表
 */
@Data
@TableName(value = "goods_setting")
public class GoodsSetting extends Model<GoodsSetting> {

    private static final long serialVersionUID = 7684287433353504776L;
    @TableId(value = "goods_id",type = IdType.INPUT)
    private Integer goodsId ;// 商品ID
    @TableField(value = "superposition")
    private String superposition ;//  '同一时段权益不叠加',
    @TableField(value = "accom_after_leave")
    private String  accomAfterLeave ;// '住宿）到离店日期后才可以进行下次预约',
     @TableField(value = "single_thread")
    private String singleThread ;// '行权完毕次日才可以进行下一次预约',
    @TableField(value = "enable_max_night")
    private String  enableMaxNight;// '是否限制单次可预订最大间',
    @TableField(value = "enable_min_night")
    private String  enableMinNight;// '是否限制单次可预订最小间夜',
     @TableField(value = "max_night")
    private Integer maxNight ;//  '间夜',
    @TableField(value = "min_night")
    private Integer minNight ;//  '间夜',
     @TableField(value = "all_year")
    private String allYear ;// 'OK365（1：无限制）',
     @TableField(value = "disable_call")
    private String disableCall ;// '禁止来电预约',
     @TableField(value = "disable_wechat")
    private String disableWechat;// '禁止微信统一行权',
    @TableField(value = "only_self")
    private String onlySelf;// '仅限本人使用',
    @TableField(value = "del_flag")
    private Integer delFlag;//'删除标识（0-正常，1-删除）',
    @TableField(value = "create_time")
    private Date createTime ;//'创建时间',
    @TableField(value = "create_user")
    private String createUser;// '创建人',
    @TableField(value = "update_time",update = "CURRENT_TIMESTAMP()")
    private Date updateTime;// '更新时间',
    @TableField(value = "update_user")
    private String updateUser;// '更新人',



    /**
     * 主键值
     */
    @Override
    protected Serializable pkVal() {
        return this.goodsId;
    }
}
