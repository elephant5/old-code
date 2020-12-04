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
 * 城市表
 */
@Data
@TableName(value = "sys_city")
public class City extends Model<City> {
    /**
     * 城市id
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    /**
     * 城市中文名
     */
    @TableField(value = "name_ch")
    private String nameCh;
    /**
     * 是否热门城市（0-不是，1-是）
     */
    @TableField(value = "hot")
    private int hot;
    /**
     * 是否内陆（0-内陆，1-非内陆）
     */
    @TableField(value = "oversea")
    private int oversea;
    /**
     * 城市拼音
     */
    @TableField(value = "name_py")
    private String namePy;
    /**
     * 国家编码
     */
    @TableField(value = "country_id")
    private String countryId;
    /**
     * 城市英文名
     */
    @TableField(value = "name_en")
    private String nameEn;
    /**
     * 老系统城市id
     */
    @TableField("old_city_id")
    private Integer oldCityId;
    /**
     * 排序
     */
    @TableField("sort")
    private Integer sort;
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