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
 * 文件引用表
 */
@Data
@TableName(value = "sys_file_quote")
public class SysFileQuote extends Model<SysFileQuote> {
    /**
     * 主键id
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    /**
     * 文件对应表格类型
     */
    @TableField(value = "type")
    private String type;
    /**
     * 文件对应表主键id
     */
    @TableField(value = "obj_id")
    private Integer objId;
    /**
     * 文件id
     */
    @TableField(value = "file_id")
    private Integer fileId;
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