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
 * 第三方资源表
 */
@Data
@TableName(value = "thirdparty_order")
public class ThirdpartyOrder extends Model<ThirdpartyOrder> {
    private static final long serialVersionUID = -5298608327165359453L;
    /**
     * 主键
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    /**
     * 渠道id
     */
    @TableField(value = "channel_id")
    private Integer channelId;
    @TableField(value = "reserv_order_id")
    private Integer reservOrderId;
    @TableField(value = "thirdparty_order_id")
    private Integer thirdpartyOrderId;
    /**
     * 第三方返回结果
     */
    @TableField(value = "result")
    private String result;
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
    /**
     * 删除标识（0-正常，1-删除）
     */
    @TableField(value = "del_flag")
    private Integer delFlag;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}