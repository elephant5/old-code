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
@TableName("sys_city")
public class City extends Model<City> {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.INPUT)
    private Long id;
    @TableField(value = "name")
    private String name;
    @TableField("hot")
    private Integer hot;
    @TableField("oversea")
    private Integer oversea;
    @TableField("py")
    private String py;
    @TableField("qunar")
    private String qunar;
    @TableField("sort")
    private Integer sort;
    @TableField("country")
    private String country;
    @TableField("creator_id")
    private Integer creatorId;
    @TableField("create_time")
    private Date createTime;
    @TableField("en")
    private String en;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
