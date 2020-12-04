package com.colourfulchina.bigan.api.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * User: Ryan
 * Date: 2018/8/3
 * 客乐芙酒店早餐
 */
@Data
@TableName("Clf_Breakfast")
public class ClfBreakfast extends Model<ClfBreakfast> {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.AUTO)
    private Long id;//primary key
    /**
     * 城市
     */
    @TableField(value = "city")
    private String city;
    /**
     * 酒店中文名
     */
    @TableField(value = "hotel_ch")
    private String hotelCh;
    /**
     * 酒店英文名
     */
    @TableField(value = "hotel_en")
    private String hotelEn;

    /**
     * 餐厅名
     */
    @TableField(value = "restaurant_name")
    private String restaurantName;
    /**
     * 权益项目
     */
    @TableField(value = "service_type")
    private String serviceType;

    /**
     * 券折扣
     */
    @TableField(value = "discount")
    private String discount;

    /**
     * 签约人
     */
    @TableField(value = "sign_contract_user")
    private String signContractUser;
    /**
     * 维护人
     */
    @TableField(value = "weihu")
    private String weihu;

    /**
     * 用户名
     */
    @TableField(value = "user_name")
    private String userName;
    /**
     * 密码
     */
    @TableField(value = "pwd")
    private String pwd;

    /**
     * 早餐类型
     */
    @TableField(value = "breakfast_type")
    private String breakfastType;
    /**
     * 货币单位
     */
    @TableField(value = "unit")
    private String unit;

    /**
     * 成本价
     */
    @TableField(value = "cost")
    private String cost;
    /**
     * 成本价（RMB）
     */
    @TableField(value = "cost_rmb")
    private String costRmb;

    /**
     * 签约日期
     */
    @TableField(value = "contract_date")
    private String contractDate;
    /**
     * 合作截止
     */
    @TableField(value = "contract_end")
    private String contractEnd;
    /**
     * openTime
     */
    @TableField(value = "open_time")
    private String openTime;

    /**
     * 适用条款
     */
    @TableField(value = "tiaokuan")
    private String tiaokuan;
    /**
     * 地址
     */
    @TableField(value = "address")
    private String address;
    /**
     * 电话
     */
    @TableField(value = "phone")
    private String phone;
    /**
     * 泊车信息
     */
    @TableField(value = "parking")
    private String parking;
    /**
     * 儿童政策
     */
    @TableField(value = "kids")
    private String kids;

    /**
     * 姓名
     */
    @TableField(value = "name")
    private String name;
    /**
     * 电话
     */
    @TableField(value = "telephone")
    private String telephone;
    /**
     * 电子邮箱
     */
    @TableField(value = "email")
    private String email;
    /**
     * 备注信息
     */
    @TableField(value = "remark")
    private String remark;
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
    @TableField(value = "update_time")
    private Date updateTime;
    /**
     * 更新人
     */
    @TableField(value = "update_user")
    private String updateUser;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
