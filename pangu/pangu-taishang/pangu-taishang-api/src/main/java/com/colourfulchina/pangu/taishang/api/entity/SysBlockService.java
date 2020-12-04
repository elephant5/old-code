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
@TableName("sys_block_service")
public class SysBlockService extends Model<SysBlockService> {
    private static final long serialVersionUID = -5141052692015104630L;
    /**
     * 系统全局block和服务类型关系主键
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    /**
     * 系统全局block主键
     */
    @TableField("block_id")
    private Integer blockId;
    /**
     * 服务类型主键
     */
    @TableField("service_id")
    private String serviceId;
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
