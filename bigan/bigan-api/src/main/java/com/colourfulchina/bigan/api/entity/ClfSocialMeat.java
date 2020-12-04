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
 * 社会餐
 */
@Data
@TableName("Clf_Social_Meat")
public class ClfSocialMeat extends Model<ClfSocialMeat> {
    private static final long serialVersionUID = 1L;
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;//primary key

    @TableField(value = "city")
    private String city;//城市
    @TableField(value = "hotel_ch")
    private String hotelCh;////酒店中文名称
    @TableField(value = "hotel_en")
    private String hotelEn;//酒店英文名称


    @TableField(value = "service_type")
    private String serviceType;//权益项目
    @TableField(value = "service_content")
    private String serviceContent;//权益内容详情

    @TableField(value = "user_name")
    private String userName;
    @TableField(value = "pwd")
    private String pwd;

    @TableField(value = "lunch_time")
    private String lunchTime;
    @TableField(value = "dinner_time")
    private String dinnerTime;//晚餐开餐时间
    @TableField(value = "address")
    private String address;//地址


    @TableField(value = "phone")
    private String phone;//
    @TableField(value = "parking")
    private String parking;//
    @TableField(value = "kids")
    private String kids;//
    @TableField(value = "use_element")
    private String useElement;//适用条款

//    @TableField(value = "discount")
//    private String discount;//券折扣
    @TableField(value = "paper_and_electron")
    private String paperAndElectron;//电子券+纸质券
    @TableField(value = "papers")
    private String papers;//纸质券
    @TableField(value = "electron")
    private String electron;//电子券
    @TableField(value = "other")
    private String other;//其他券

    @TableField(value = "cost")
    private String cost;//成本

    @TableField(value = "end_price")
    private String endPrice;//结算价



    @TableField(value = "prepayment")
    private String prepayment;//合同约定预付

    @TableField(value = "sign_date")
    private String signDate;//签约日期
    @TableField(value = "contract_end")
    private String contractEnd;//合作截止

    @TableField(value = "cash")
    private String cash;//押金

    @TableField(value = "sign_user")
    private String signUser;//签约人
    @TableField(value = "vindicate_user")
    private String vindicateUser;//维护人

    @TableField(value = "remark")
    private String remark;//
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
