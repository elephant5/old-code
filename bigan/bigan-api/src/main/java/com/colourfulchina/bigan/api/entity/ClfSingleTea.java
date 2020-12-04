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
 * 单杯茶饮
 */
@Data
@TableName("Clf_Single_Tea")
public class ClfSingleTea extends Model<ClfSingleTea> {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;//primary key

    @TableField(value = "city")
    private String city;//城市
    @TableField(value = "hotel_ch")
    private String hotelCh;////酒店中文名称
    @TableField(value = "hotel_en")
    private String hotelEn;//酒店英文名称
    @TableField(value = "restaurant_name")
    private String restaurantName;//餐厅名
    @TableField(value = "restaurant_en")
    private String restaurantEn;//餐厅英文名
    @TableField(value = "restaurant_ch")
    private String restaurantCh;//餐厅中文名

    @TableField(value = "service_old")
    private String serviceOld;//权益项目原始
    @TableField(value = "service")
    private String service;//权益项目
    @TableField(value = "service_type")
    private String serviceType;//权益类型
    @TableField(value = "week_block")
    private String weekBlock;//周block

    @TableField(value = "discount")
    private String discount;//券折扣


    @TableField(value = "paper_and_electron")
    private String paperAndElectron;//电子券+纸质券
    @TableField(value = "papers")
    private String papers;//纸质券
    @TableField(value = "electron")
    private String electron;//电子券
    @TableField(value = "other")
    private String other;//其他券

    @TableField(value = "contract_num")
    private String contractNum;//合同数
    @TableField(value = "sign_user")
    private String signUser;//签约人
    @TableField(value = "vindicate_user")
    private String vindicateUser;//维护人

    @TableField(value = "user_name")
    private String userName;//
    @TableField(value = "pwd")
    private String pwd;//

    @TableField(value = "drink_type")
    private String drinkType;//

    @TableField(value = "cost")
    private String cost;//成本
    @TableField(value = "end_price")
    private String endPrice;//结算价
    @TableField(value = "contr_price")
    private String contrPrice;//合同约定预付
    @TableField(value = "cash")
    private String cash;//押金
    @TableField(value = "sign_date")
    private Date signDate;//签约日期
    @TableField(value = "contract_end")
    private Date contractEnd;//合作截止
    @TableField(value = "use_date")
    private String useDate;//使用时间
    @TableField(value = "use_element")
    private String useElement;//适用条款
    @TableField(value = "address")
    private String address;//
    @TableField(value = "phone")
    private String phone;//
    @TableField(value = "parking")
    private String parking;//
    @TableField(value = "kids")
    private String kids;//
    @TableField(value = "remarkInfo")
    private String remarkInfo;//
    @TableField(value = "remark")
    private String remark;//
    @TableField(value = "city_id")
    private Integer cityId;//
    @TableField(value = "hotel_id")
    private Integer hotelId;//
    @TableField(value = "shop_id")
    private Integer shopId;//
    @TableField(value = "create_time")
    private Date createTime;//
    @TableField(value = "create_user")
    private String createUser;//
    @TableField(value = "update_time")
    private Date updateTime;//
    @TableField(value = "update_user")
    private String updateUser;//


    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
