package com.colourfulchina.mars.api.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("gift_unit")
public class GiftUnit extends Model<GiftUnit> {
    private static final long serialVersionUID = 1L;

    @TableId(value = "unit_id")
    private Integer unitId;
    @TableField(value = "project_id")
    private Integer projectId;
    @TableField(value = "package_id")
    private Integer packageId;
    //0 未出库
    //1 已出库
    //2 未使用
    //3 已使用
    //4 已过期
    //5 已退货
    //6 已作废
    @TableField(value = "state")
    private Integer state;
    @TableField(value = "vtype")
    private String vtype;
    @TableField(value = "code")
    private String code;
    @TableField(value = "phone")
    private String phone;
    @TableField(value = "member_id")
    private Integer memberId;
    @TableField(value = "bin")
    private String bin;
    @TableField(value = "suffix")
    private String suffix;
    @TableField(value = "tag")
    private String tag;
    @TableField(value = "log_id")
    private Integer logId;
    @TableField(value = "channel_id")
    private Integer channelId;
    @TableField(value = "new_channel_id")
    private Integer newChannelId;
    @TableField(value = "create_time")
    private Date createTime;
    @TableField(value = "expiry_date")
    private Date expiryDate;
    @TableField(value = "activation_date")
    private Date activationDate;
    @TableField(value = "enable_date")
    private Date enableDate;
    @TableField(value = "unit_expiry")
    private String unitExpiry;
//    @TableField(value = "update_time")
//    private byte[] updateTime;

    /**
     * 激活码退货时间
     */
    @TableField(exist =  false)
    private Date actReturnDate;
    /**
     * 激活码作废时间
     */
    @TableField(exist =  false)
    private Date actObsoleteDate;

    @Override
    protected Serializable pkVal() {
        return null;
    }

}
