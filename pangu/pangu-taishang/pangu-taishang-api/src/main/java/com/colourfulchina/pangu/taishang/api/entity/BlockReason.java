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
 * block原因
 */
@Data
@TableName(value = "block_reason")
public class BlockReason  extends Model<BlockReason> {
    private static final long serialVersionUID = -7266097050158146160L;

    /**
     * ID
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    /**
     * 数据的所属类型，一般为表名
     */
    @TableField(value = "type")
    private String type;
    /**
     * 数据的ID
     */
    @TableField(value = "value")
    private Integer value;
    /**
     * block
     */
    @TableField(value = "block")
    private String block;
    /**
     * reason
     */
    @TableField(value = "reason")
    private String reason;


    /**
     * 删除标识（0-正常，1-删除）
     */
    @TableField(value = "del_flag")
    private Integer delFlag;
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
     * 主键值
     */
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
