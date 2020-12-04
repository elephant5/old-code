package com.colourfulchina.pangu.taishang.api.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("sys_block")
public class SysBlock extends Model<SysBlock> {
    private static final long serialVersionUID = 4662760831382101277L;

    /**
     * 系统全局block主键
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    /**
     * block规则
     */
    @TableField("block")
    private String block;
    /**
     * block备注
     */
    @TableField("notes")
    private String notes;
    /**
     * 排除在外的项目
     */
    @TableField("except_project")
    private String exceptProject;
    /**
     * 排除在外的产品组
     */
    @TableField("except_group")
    private String exceptGroup;
    /**
     * 删除标识（0正常，1删除）
     */
    @TableField("del_flag")
    private Integer delFlag;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 创建人
     */
    @TableField("create_user")
    private String createUser;
    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;
    /**
     * 更新人
     */
    @TableField("update_user")
    private String updateUser;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
