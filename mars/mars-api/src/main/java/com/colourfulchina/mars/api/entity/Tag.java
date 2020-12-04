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
 * @Author: Nickal
 * @Description:
 * @Date: 2019/5/15 10:35
 */
@Data
@TableName(value = "tag")
public class Tag extends Model<Tag> {
    @TableId(value = "id",type = IdType.INPUT)
    private Integer id;
    @TableField(value = "tag_name")
    private String tagName;
    @TableField(value = "tag_satues")
    private String tagSatues;
    @TableField(value = "tag_origin")
    private String tagOrigin;
    @TableField(value = "tag_remark")
    private String tagRemark;
    @TableField(value = "tag_time")
    private Date tagTime;
    @ApiModelProperty("删除标识（0-正常，1-删除）")
    @TableField(value = "del_flag")
    private Integer delFlag;
    @ApiModelProperty("创建时间")
    @TableField(value = "create_time")
    private java.util.Date createTime;
    @ApiModelProperty("创建人")
    @TableField(value = "create_user")
    private String createUser;
    @ApiModelProperty("更新时间")
    @TableField(value = "update_time",update = "CURRENT_TIMESTAMP()")
    private java.util.Date updateTime;
    @ApiModelProperty("更新人")
    @TableField(value = "update_user")
    private String updateUser;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
