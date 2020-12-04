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
 * 地理位置表
 */
@Data
@TableName(value = "sys_geo")
public class Geo extends Model<Geo> {
    /**
     * 地理位置主键
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    /**
     * 旧系统id
     */
    @TableField(value = "old_id")
    private Integer oldId;
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
     * 坐标
     */
    @TableField(value = "point")
    private String point;
    /**
     * 纬度
     */
    @TableField(value = "lat")
    private String lat;
    /**
     * 经度
     */
    @TableField(value = "lng")
    private String lng;
    /**
     * 缩放
     */
    @TableField(value = "zoom")
    private Integer zoom;
    /**
     * 国家
     */
    @TableField(value = "country")
    private String country;
    /**
     * 省份
     */
    @TableField(value = "province")
    private String province;
    /**
     * 城市
     */
    @TableField(value = "city")
    private String city;
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