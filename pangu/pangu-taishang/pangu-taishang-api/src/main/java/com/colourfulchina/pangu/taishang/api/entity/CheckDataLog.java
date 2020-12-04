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

@Data
@TableName(value = "check_data_log")
public class CheckDataLog  extends Model<CheckDataLog> {
    private static final long serialVersionUID = -3189241882399820805L;

    @ApiModelProperty("主键，自动增长")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @TableField(value = "type")
    private String type;
    @TableField(value = "old_value")
    private String oldValue;
    @TableField(value = "new_value")
    private String newValue;
    @TableField(value = "keys")
    private String keys;
    @TableField(value = "size_value")
    private String sizeValue;
    @TableField(value = "log")
    private String log;
    @TableField(value = "remark")
    private String remark;
    @ApiModelProperty("删除标识（0-正常，1-删除）")
    @TableField(value = "del_flag")
    private Integer delFlag;
    @ApiModelProperty("创建时间")
    @TableField(value = "create_time")
    private Date createTime ;
    @ApiModelProperty("创建人")
    @TableField(value = "create_user")
    private String createUser;
    @ApiModelProperty("更新时间")
    @TableField(value = "update_time",update = "CURRENT_TIMESTAMP()")
    private Date updateTime;
    @ApiModelProperty("更新人")
    @TableField(value = "update_user")
    private String updateUser;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
