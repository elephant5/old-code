package com.colourfulchina.bigan.api.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
@Data
@TableName("goods")
public class Goods  extends Model<Goods> {
    private static final long serialVersionUID = -5665867365146195698L;
    @TableId(value = "id")
    private Long id;
    @TableField("type")
    private String type;
    @TableField("gift")
    private String gift;
    @TableField("shop_id")
    private Long shopId;
    @TableField("unique_data")
    private String uniqueData;
    @TableField("alias")
    private String alias;
    @TableField("title")
    private String title;
    @TableField("clause")
    private String clause;
    @TableField("days")
    private Long days;
    @TableField("tag")
    private String tag;
    @TableField("create_time")
    private Date createTime;
    @TableField("creator_id")
    private Long creatorId;
    @TableField("block_code")
    private String blockCode;
    @TableField("channel_id")
    private Integer channelId;
    @TableField("price")
    private BigDecimal price;
    @TableField("more")
    private String more;
    @TableField("items")
    private String items;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}