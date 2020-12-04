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
 * 节假日配置表
 */
@Data
@TableName(value = "sys_holiday_config")
public class SysHolidayConfig extends Model<SysHolidayConfig> {
    /**
     * 编码
     */
    @TableId(value = "code",type = IdType.INPUT)
    private String code;
    /**
     * 名称
     */
    @TableField(value = "name")
    private String name;
    //阳历yyyy/mm/dd或mm/dd
    //阴历Cyyyy/mm/dd或Cmm/dd
    private String start;
    private String end;
    /**
     * 启用禁用
     */
    private Integer enable;

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
        return this.code;
    }
}