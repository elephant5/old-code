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
 * 酒店探索章节图片
 */
@Data
@TableName(value = "hotel_portal_img")
public class HotelPortalImg extends Model<HotelPortalImg> {
    /**
     * 酒店探索章节图片主键
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    /**
     * 酒店探索章节id
     */
    @TableField(value = "hotel_portal_id")
    private Integer hotelPortalId;
    /**
     * 图片
     */
    @TableField(value = "image")
    private String image;
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