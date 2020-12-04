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
 * 集团集团表
 */
@Data
@TableName(value = "hotel_group")
public class HotelGroup extends Model<HotelGroup> {
    private static final long serialVersionUID = -506668151334590310L;
    /**
     * 集团主键
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    /**
     * 集团中文名
     */
    @TableField(value = "name_ch")
    private String nameCh;
    /**
     * 集团英文名
     */
    @TableField(value = "name_en")
    private String nameEn;
    /**
     * 集团拼音
     */
    @TableField(value = "name_py")
    private String namePy;
    /**
     * 集团联系电话
     */
    @TableField(value = "phone")
    private String phone;
    /**
     * 备注
     */
    @TableField(value = "notes")
    private String notes;
    /**
     * 集团简介
     */
    @TableField(value = "summary")
    private String summary;
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