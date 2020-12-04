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

@TableName("order_act_person")
@Data
public class OrderActPerson extends Model<OrderActPerson> {
    private static final long serialVersionUID = -6007986980124711822L;
    /**
     * 权益所有人id
     */
    @ApiModelProperty("权益所有人id")
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    /**
     * 权益人名称
     */
    @ApiModelProperty("权益人名称")
    @TableField("intere_person")
    private String interePerson;
    /**
     * 权益人电话
     */
    @ApiModelProperty("权益人电话")
    @TableField("intere_phone")
    private String interePhone;
    /**
     * 权益人性别
     */
    @ApiModelProperty("权益人性别")
    @TableField("intere_gender")
    private String intereGender;
    /**
     * 操作人
     */
    @ApiModelProperty("操作人")
    @TableField("operator")
    private String operator;
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
