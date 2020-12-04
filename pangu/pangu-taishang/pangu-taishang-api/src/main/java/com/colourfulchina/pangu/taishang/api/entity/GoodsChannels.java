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
 * 商品和渠道表
 */
@Data
@TableName(value = "goods_channels")
public class GoodsChannels extends Model<GoodsChannels> {

    private static final long serialVersionUID = 8637825288121007644L;
    @TableId(value = "goods_id",type = IdType.INPUT)
    private Integer goodsId ;// 商品ID
    @TableField(value = "channel_id")
    private Integer channelId;//
    @TableField(value = "del_flag")
    private Integer delFlag = 0;//'删除标识（0-正常，1-删除）',
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
