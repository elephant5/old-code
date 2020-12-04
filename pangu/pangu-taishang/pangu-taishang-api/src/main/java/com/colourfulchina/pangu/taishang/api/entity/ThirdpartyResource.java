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
 * 第三方资源表
 */
@Data
@TableName(value = "thirdparty_resource")
public class ThirdpartyResource extends Model<ThirdpartyResource> {
    private static final long serialVersionUID = 197095161708690927L;
    /**
     * 主键
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    /**
     * 类型：hotel_group,hotel,shop,shop_item
     */
    @TableField(value = "type")
    private String type;
    /**
     * 渠道id
     */
    @TableField(value = "channel_id")
    private Integer channelId;
    @TableField(value = "obj_id")
    private Integer objId;
    /**
     * 集团拼音
     */
    @TableField(value = "code")
    private String code;
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
    /**
     * 删除标识（0-正常，1-删除）
     */
    @TableField(value = "del_flag")
    private Integer delFlag;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}