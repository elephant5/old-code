package com.colourfulchina.pangu.taishang.api.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Copyright Colourful
 * @Author SunnyWang
 * @Date 2019年9月6日10:19:10
 */
@Data
@TableName(value = "activity_relation")
public class ActivityRelation extends Model<ActivityRelation> {
    private static final long serialVersionUID = -716174826114877426L;
    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @ApiModelProperty("活动ID")
    @TableField(value = "act_id")
    private Integer actId;
    @ApiModelProperty("关联ID")
    @TableField(value = "relate_id")
    private Integer relateId;
//    @ApiModelProperty("关联的名称")
//    @TableField(value = "relate_name")
//    private Integer relateName;
    @ApiModelProperty("关联ID类型：1商品ID, 2产品组ID，3产品ID, 4单品ID")
    @TableField(value = "relate_id_type")
    private Integer relateIdType;
    @ApiModelProperty("创建人")
    @TableField(value = "create_user")
    private String createUser;
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @TableField(value = "create_time")
    private Date createTime;
    @ApiModelProperty("更新人")
    @TableField(value = "update_user")
    private String updateUser;
    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @TableField(value = "update_time")
    private Date updateTime;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
