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
 * 国家表
 */
@Data
@TableName(value = "sys_country")
public class Country extends Model<Country> {
    /**
     * 国家id
     */
    @TableId(value = "id",type = IdType.INPUT)
    private String id;
    /**
     * 国家中文名
     */
    @TableField(value = "name_ch")
    private String nameCh;
    /**
     * 国家英文名
     */
    @TableField(value = "name_en")
    private String nameEn;
    /**
     * 国家拼音
     */
    @TableField(value = "name_py")
    private String namePy;
    /**
     * 国旗
     */
    @TableField(value = "flag")
    private String flag;
    /**
     * 别名
     */
    @TableField(value = "alias")
    private String alias;
    /**
     * 地区id
     */
    @TableField(value = "area_id")
    private String areaId;
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