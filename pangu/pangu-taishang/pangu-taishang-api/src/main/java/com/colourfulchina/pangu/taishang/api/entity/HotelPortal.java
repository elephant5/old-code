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
 * 酒店探索章节
 */
@Data
@TableName(value = "hotel_portal")
public class HotelPortal extends Model<HotelPortal> {
    /**
     * 酒店探索章节主键
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    /**
     * 酒店id
     */
    @TableField(value = "hotel_id")
    private Integer hotelId;
    /**
     * 章节标题
     */
    @TableField(value = "title")
    private String title;
    /**
     * 文字描述
     */
    @TableField(value = "content")
    private String content;
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