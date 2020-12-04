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
 * 客乐芙_定制套餐
 */
@Data
@TableName("clf_set_menu")
public class ClfSetMenu extends Model<ClfSetMenu>{
    private static final long serialVersionUID = 1L;
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;//primary key

    @TableField(value = "city")
    private String city;//城市
    @TableField(value = "city_id")
    private Integer cityId;//城市ID
    @TableField(value = "hotel_ch")
    private String hotelCh;////酒店中文名称
    @TableField(value = "hotel_en")
    private String hotelEn;//酒店英文名称
    @TableField(value = "restaurant_name_old")
    private String restaurantNameOld;//餐厅名
    @TableField(value = "restaurant_name_en")
    private String restaurantNameEn;//餐厅名
    @TableField(value = "restaurant_name_ch")
    private String restaurantNameCh;//餐厅名
    @TableField(value = "service_project")
    private String serviceProject;//权益项目
    @TableField(value = "project_type")
    private String projectType;//项目
    @TableField(value = "service_type")
    private String serviceType;//权益

    @TableField(value = "discount")
    private String discount;//券折扣

    @TableField(value = "user_name")
    private String userName;//
    @TableField(value = "pwd")
    private String pwd;//

    @TableField(value = "price")
    private String price;//

    @TableField(value = "meal_selection_old")
    private String mealSelectionOld;//餐段

    @TableField(value = "week_block")
    private String weekBlock;//餐段

    @TableField(value = "meal_selection")
    private String mealSelection;//餐段

    @TableField(value = "net_price")
    private String netPrice;//净价

    @TableField(value = "service_cost")
    private String serviceCost;//服务费

    @TableField(value = "rate")
    private String rate;//税费
    @TableField(value = "total_price")
    private String totalPrice;//
    @TableField(value = "cost")
    private String cost;//成本

//    @TableField(value = "end_price")
//    private String endPrice;//结算价
    @TableField(value = "prepayment")
    private String prepayment;//合同约定预付
    @TableField(value = "cash")
    private String cash;//押金

    @TableField(value = "sign_date")
    private Date signDate;//签约日期
    @TableField(value = "contract_end")
    private Date contractEnd;//合作截止

    @TableField(value = "lunch_time")
    private String lunchTime;//
    @TableField(value = "dinner_time")
    private String dinnerTime;//
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
    @TableField(value = "paper_and_electron")
    private String paperAndElectron;//电子券+纸质券
    @TableField(value = "electron")
    private String electron;//电子券

    @TableField(value = "contract_num")
    private Integer contractNum;//合同数

    @TableField(value = "sign_user")
    private String signUser;//签约人
    @TableField(value = "vindicate_user")
    private String vindicateUser;//维护人
    @TableField(value = "remarkInfo")
    private String remarkInfo;//

    @TableField(value = "menu_content")
    private String menuContent;//

    @TableField(value = "remark")
    private String remark;//
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
        return this.id;//
    }
}
