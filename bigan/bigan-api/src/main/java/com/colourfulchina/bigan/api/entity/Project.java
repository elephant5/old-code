package com.colourfulchina.bigan.api.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("project")
public class Project extends Model<Project> {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id")
    private Long id;
    @TableField(value = "name")
    private String name;
    @TableField(value = "short")
    private String shortName;
    @TableField(value = "auth_type")
    private String authType;
    @TableField(value = "limit_type")
    private String limitType;
    @TableField(value = "multi_pack")
    private Integer multiPack;
    @TableField(value = "multi_group")
    private Integer multiGroup;
    @TableField(value = "enable_bank_order")
    private Integer enableBankOrder;
    @TableField(value = "dynamic")
    private Integer dynamic;
    @TableField(value = "package_id")
    private Integer packageId;
    @TableField(value = "bank_id")
    private Integer bankId;
    @TableField(value = "register")
    private String register;
    @TableField(value = "code")
    private String code;
    @TableField(value = "theme")
    private String theme;
    @TableField(value = "title")
    private String title;
    @TableField(value = "notes")
    private String notes;
    @TableField(value = "expiry_date")
    private String expiryDate;
    @TableField(value = "create_time")
    private Date createTime;
    @TableField(value = "update_time")
    private Date updateTime;
    @TableField(value = "portal")
    private String portal;
    @TableField(value = "unit_expiry")
    private String unitExpiry;
    @TableField(value = "more")
    private String more;
    @TableField(value = "start_date")
    private Date startDate;
    @TableField(value = "end_date")
    private Date endDate;
    @TableField(value = "enable_portal")
    private Integer enablePortal;
    @TableField(value = "disable_wx_book")
    private Integer disableWxBook;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", shortName='" + shortName + '\'' +
                ", authType='" + authType + '\'' +
                ", limitType='" + limitType + '\'' +
                ", multiPack=" + multiPack +
                ", multiGroup=" + multiGroup +
                ", enableBankOrder=" + enableBankOrder +
                ", dynamic=" + dynamic +
                ", packageId=" + packageId +
                ", bankId=" + bankId +
                ", register='" + register + '\'' +
                ", code='" + code + '\'' +
                ", theme='" + theme + '\'' +
                ", title='" + title + '\'' +
                ", notes='" + notes + '\'' +
                ", expiryDate='" + expiryDate + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", portal='" + portal + '\'' +
                ", unitExpiry='" + unitExpiry + '\'' +
                ", more='" + more + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", enablePortal=" + enablePortal +
                '}';
    }
}
