package com.colourfulchina.pangu.taishang.api.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.sql.Date;

@Data
@TableName(value = "sku_goods_rel")
public class SkuGoodsRel {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    @TableField(value = "channel")
    private String channel;

    @TableField(value = "sku")
    private String sku;

    @TableField(value = "goods_id")
    private Long goodsId;

    @TableField(value = "goods_name")
    private String goodsName;

    @TableField(value = "create_time")
    private Date createTime;

    @TableField(value = "create_user")
    private String createUser;

    @TableField(value = "update_time")
    private Date updateTime;

    @TableField(value = "update_user")
    private String updateUser;
}
