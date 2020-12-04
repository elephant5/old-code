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
 * 结算规则
 */
@Data
@TableName(value = "sys_settle_express_item")
public class SysSettleExpressItem extends Model<SysSettleExpressItem> {
    /**
     * 结算公式主键
     */
    @TableId(value = "id",type = IdType.AUTO)
    private String id;
    /**
     * 变量名
     */
    @TableField(value = "variant")
    private String variant;
    /**
     * 名称
     */
    private String name;
    /**
     * 类型(0:符号,1:增值税变量,2:结算公式变量)
     */
    private Integer type;
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

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}