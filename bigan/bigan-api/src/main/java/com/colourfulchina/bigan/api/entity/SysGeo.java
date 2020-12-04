package com.colourfulchina.bigan.api.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("sys_geo")
public class SysGeo extends Model<SysGeo> {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    @TableField(value = "address")
    private String address;
    @TableField(value = "address_en")
    private String addressEn;
    @TableField(value = "point")
    private String point;
    @TableField(value = "lat")
    private String lat;
    @TableField(value = "lng")
    private String lng;
    @TableField(value = "zoom")
    private Integer zoom;
    @TableField(value = "country")
    private String country;
    @TableField(value = "province")
    private String province;
    @TableField(value = "city")
    private String city;
    @TableField(value = "date")
    private Date date;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
