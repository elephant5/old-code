package com.colourfulchina.mars.api.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName(value = "reserv_order_hospital")
public class ReservOrderHospital extends Model<ReservOrderHospital> {

    private static final long serialVersionUID = 737302427523622717L;
    @TableField(value = "id")
    private Integer id;//预约单附属表id
    @TableField(value = "order_id")
    private Integer orderId;//预约单主键

    @TableField(value = "mem_family_id")
    private Long memFamilyId;//预约人家属ID
    @TableField(value = "hospital_id")
    private Integer hospitalId;//医院
    @TableField(value = "visit")
    private String visit;//就诊类型
    @TableField(value = "special")
    private String special;// 特殊需求
    @TableField(value = "name")
    private String name ;// 医院名字
    @TableField(value = "grade")
    private String grade ;//医院等级
    @TableField(value = "province")
    private String province;//省
    @TableField(value = "city")
    private String city;//城市
    @TableField(value = "department")
    private String department;//科室
    @TableField(value = "hospital_type")
    private String  hospitalType;//医院类型
    @TableField(value = "del_flag")
    private Integer delFlag;//删除标识（0-正常，1-删除）
    @TableField(value = "create_time")
    private Date createTime;//	创建时间
    @TableField(value = "create_user")
    private String createUser;//创建人check_room
    @TableField(value = "update_time",update = "CURRENT_TIMESTAMP()")
    private Date updateTime;//更新时间
    @TableField(value = "update_user")
    private String updateuser;//更新人


    /**
     * 主键值
     */
    @Override
    protected Serializable pkVal() {
        return null;
    }
}
