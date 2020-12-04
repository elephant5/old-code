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

@TableName("sys_hospital")
@Data
public class SysHospital extends Model<SysHospital>{
    private static final long serialVersionUID = -7999605109402187630L;

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    @TableField("province")
    private String province;

    @TableField("city")
    private String city;

    @TableField("name")
    private String name;

    @TableField("grade")
    private String grade;

    @TableField("hospital_type")
    private String hospitalType;

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
        return null;
    }
}
