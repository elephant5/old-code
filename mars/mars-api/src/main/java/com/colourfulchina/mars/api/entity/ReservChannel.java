package com.colourfulchina.mars.api.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "reserv_channel")
public class ReservChannel extends Model<ReservChannel> {

    private static final long serialVersionUID = 5297344807077380586L;
    @TableId(value = "id" ,type = IdType.INPUT)
     private Integer id	;//渠道ID
    @TableField(value = "channel_name")
     private String channelName	;//渠道名称
    @TableField(value = "internal")
     private String internal	;//内部编码
    @TableField(value = "settle_method")
     private String settleMethod	;//结算方式
    @TableField(value = "settle_currency")
     private String settleCurrency;//	结算币种
    @TableField(value = "channel_phone")
     private String channelPphone	;//联系方式
    @TableField(value = "consignee")
     private String consignee;//	收货人
    @TableField(value = "product_type")
     private String productType	;//类型
    @TableField(value = "notes")
     private String notes	;//其他说明
    @TableField(value = "del_flag")
    private Integer delFlag	;//删除标识（0-正常，1-删除）
    @TableField(value = "create_time")
    private Date createTime;//	创建时间
    @TableField(value = "create_user")
    private String createUser	;//创建人
    @TableField(value = "update_time",update = "CURRENT_TIMESTAMP()")
    private Date updateTime	;//更新时间
    @TableField(value = "update_user")
    private String updateuser	;//更新人

    /**
     * 主键值
     */
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
