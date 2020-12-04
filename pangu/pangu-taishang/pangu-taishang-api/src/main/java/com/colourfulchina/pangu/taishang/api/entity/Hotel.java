package com.colourfulchina.pangu.taishang.api.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 酒店表
 */
@Data
@TableName(value = "hotel")
public class Hotel extends Model<Hotel> {
    private static final long serialVersionUID = 6119796417902408445L;
    /**
     * 酒店主键
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    /**
     * Charlie系统中对应酒店id
     */
    @TableField(value = "old_hotel_id")
    private Integer oldHotelId;
    @TableField(value = "group_id")
    private Integer groupId;
    /**
     * 酒店中文名
     */
    @TableField(value = "name_ch")
    private String nameCh;
    /**
     * 酒店英文名
     */
    @TableField(value = "name_en")
    private String nameEn;
    /**
     * 酒店拼音
     */
    @TableField(value = "name_py")
    private String namePy;
    /**
     * 城市id
     */
    @TableField(value = "city_id")
    private Integer cityId;
    /**
     * 是否是内陆（0-内陆，1-非内陆）
     */
    @TableField(value = "oversea")
    private int oversea;
    /**
     * 地址中文名
     */
    @TableField(value = "address_ch")
    private String addressCh;
    /**
     * 地址英文名
     */
    @TableField(value = "address_en")
    private String addressEn;
    /**
     * 酒店联系电话
     */
    @TableField(value = "phone")
    private String phone;
    /**
     * 备注
     */
    @TableField(value = "notes")
    private String notes;
    /**
     * 酒店简介
     */
    @TableField(value = "summary")
    private String summary;
    /**
     * 挂星
     */
    @TableField(value = "star")
    private Integer star;
    /**
     * 酒店地理位置id
     */
    @TableField(value = "geo_id")
    private Integer geoId;
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

    @ApiModelProperty("商户坐标经纬度")
    @TableField(exist =false )
    private String coordinate;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}