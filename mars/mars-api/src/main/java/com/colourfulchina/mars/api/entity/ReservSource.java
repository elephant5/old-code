package com.colourfulchina.mars.api.entity;

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
 * reserv_source 主键
 * @Author: luck.luo
 * @Description:
 * @Date: 2019/5/15 9:54
 */
@TableName("reserv_source")
@Data
public class ReservSource extends Model<ReservSource> {
    @ApiModelProperty("主键")
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    @ApiModelProperty("名称")
    @TableField(value = "name")
    private String name;
    @ApiModelProperty("类型")
    @TableField(value = "type")
    private String type;
    @TableField(value = "项目ID")
    private Integer projectId;
    @TableField(value = "del_flag")
    private Integer delFlag;
    @ApiModelProperty("创建时间")
    @TableField(value = "create_time")
    private Date createTime;
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
