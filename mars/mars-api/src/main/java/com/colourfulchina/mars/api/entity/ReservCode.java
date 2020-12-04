package com.colourfulchina.mars.api.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "reserv_code")
public class ReservCode extends Model<ReservCode> {

    private static final long serialVersionUID = 6702826586407459158L;
    @TableId(value = "id" ,type = IdType.AUTO)
    private Integer id;//	验证码id
    @TableField(value = "order_id")
    private Integer orderId;//	预约id
    @TableField(value = "old_order_id")
    private Integer oldOrderId;//	老系统预约id
    @TableField(value = "product_group_id")
    private Integer productGroupId	;//类型
    @TableField(value = "var_code")
    private String varCode;//验证码
    @TableField(value = "var_status")
    private String varStatus;//	使用状态(1未使用，2已过期，3已使用4已作废）
    @TableField(value = "var_crt_time")
    private Date varCrtTime	;//验证码开始时间
    @TableField(value = "var_use_time")
    private Date varUseTime	;//使用时间
    @TableField(value = "var_expire_time")
    private Date varExpireTime	;//验证码过期时间
    @TableField(value = "var_days")
    private Date varDays	;//有效期天数
    @TableField(value = "del_flag")
    private Integer delFlag	;//删除标识（0-正常，1-删除）
    @TableField(value = "create_time")
    private Date createTime;//	创建时间
    @TableField(value = "create_user")
    private String createUser	;//创建人
    @TableField(value = "update_time",update = "CURRENT_TIMESTAMP()")
    private Date updateTime	;//更新时间
    @TableField(value = "update_user")
    private String updateuser	;//更新人
    /**
     * 主键值
     */
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
