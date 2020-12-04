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
 * 客乐芙_SPA
 */
@Data
@TableName("Clf_Spa")
public class ClfSpa extends Model<ClfSpa> {
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
     * 水疗品牌中文名
     */
    @TableField(value = "product_ch")
    private String productCh;
    /**
     * 水疗品牌英文名
     */
    @TableField(value = "product_en")
    private String productEn;
    /**
     * 权益项目
     */
    @TableField(value = "service_type")
    private String serviceType;

    /**
     * 卡折扣
     */
    @TableField(value = "cards_count")
    private String cardsCount;

    /**
     * 券折扣
     */
    @TableField(value = "discount")
    private String discount;
    /**
     * 项目名称
     */
    @TableField(value = "project_name")
    private String projectName;
    /**
     * 项目时长
     */
    @TableField(value = "project_time")
    private String projectTime;
    /**
     * spa营业时间
     */
    @TableField(value = "spa_open_time")
    private String spaOpenTime;

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
     * 原价价格
     */
    @TableField(value = "old_price")
    private String oldPrice;

    /**
     * 结算价格
     */
    @TableField(value = "end_price")
    private String endPrice;

    /**
     * 合同约定预付
     */
    @TableField(value = "contract_price")
    private String contractPrice;

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
     * 备注
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


