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
 * 文件表
 */
@Data
@TableName(value = "sys_file")
public class SysFile extends Model<SysFile> {
    /**
     * 文件id
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    /**
     * 老系统id
     */
    @TableField("old_id")
    private Integer oldId;
    /**
     * path/code
     */
    @TableField(value = "guid")
    private String guid;
    private String tag;
    /**
     * 路径
     */
    @TableField(value = "path")
    private String path;
    /**
     * code码
     */
    @TableField(value = "code")
    private String code;
    /**
     * 文件类型，后缀
     */
    @TableField(value = "ext")
    private String ext;
    /**
     * 文件名称
     */
    @TableField(value = "name")
    private String name;
    /**
     * 文件大小
     */
    @TableField(value = "size")
    private Long size;
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