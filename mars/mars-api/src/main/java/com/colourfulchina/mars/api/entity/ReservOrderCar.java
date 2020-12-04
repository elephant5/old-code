package com.colourfulchina.mars.api.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "reserv_order_car")
public class ReservOrderCar extends Model<ReservOrderCar> {

    private static final long serialVersionUID = -3136085441049504365L;
    @TableField(value = "id")
    private Integer id;//	机场服务ID
    @TableField(value = "order_id")
    private Integer orderId;//	预约单编号
    @TableField(value = "product_type")
    private String productType	;//类型
    @TableField(value = "product_id")
    private Integer productId;//	项目ID
    @TableField(value = "ser_content")
    private String serContent;//	服务内容
    @TableField(value = "ser_num")
    private Integer serNum;//	乘车人数
    @TableField(value = "ser_city")
    private String serCity	;//使用城市
    @TableField(value = "flight_num")
    private String flightNum;//	航班号
    @TableField(value = "sail_route")
    private String sailroute;//	航段-航向线路起始
    @TableField(value = "board_address")
    private String boardAddress;//	乘车地址
    @TableField(value = "dest_address")
    private String destAddress	;//目的地址
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
