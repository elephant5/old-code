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
 * C端——自助 餐
 */
@Data
@TableName("Clf_Buffet")
public class ClfBuffet extends Model<ClfBuffet> {
    private static final long serialVersionUID = 1L;
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;//primary key
    @TableField(value = "city")
    private String city;//城市
    @TableField(value = "hotel_ch")
    private String hotelCh;////酒店中文名称
    @TableField(value = "hotel_en")
    private String hotelEn;//酒店英文名称
    @TableField(value = "restaurant_name")
    private String restaurantName;//餐厅名
    @TableField(value = "restaurant_en")
    private String restaurantEn;//餐厅名英文名
    @TableField(value = "restaurant_ch")
    private String restaurantCh;//餐厅中文名

    @TableField(value = "service_old")
    private String serviceOld;//权益项目原始
    @TableField(value = "service")
    private String service;//权益项目
    @TableField(value = "service_type")
    private String serviceType;//权益类型
    @TableField(value = "discount")
    private String discount;//券折扣

    @TableField(value = "user_name")
    private String userName;//
    @TableField(value = "pwd")
    private String pwd;//
    @TableField(value = "price")
    private String price;//

    @TableField(value = "meal_selection_old")
    private String mealSelectionOld;//餐段原始
    @TableField(value = "week_block")
    private String weekBlock;//周block
    @TableField(value = "week_use")
    private String weekUse;//适用星期
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
    @TableField(value = "end_price_rule")
    private String endPriceRule;//结算规则

    @TableField(value = "prepayment")
    private String prepayment;//合同约定预付
    @TableField(value = "cash")
    private String cash;//押金

    @TableField(value = "sign_date")
    private Date signDate;//签约日期
    @TableField(value = "contract_end")
    private Date contractEnd;//合作截止


    @TableField(value = "luch_time")
    private String luchTime;
    @TableField(value = "dinner_time")
    private String dinnerTime;//晚餐开餐时间
    @TableField(value = "use_element")
    private String useElement;//适用条款

    @TableField(value = "paper_and_electron")
    private String paperAndElectron;//电子券+纸质券
    @TableField(value = "electron")
    private String electron;//电子券
    @TableField(value = "contract_num")
    private String contractNum;//合同数

    @TableField(value = "sign_user")
    private String signUser;//签约人
    @TableField(value = "vindicate_user")
    private String vindicateUser;//维护人
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



    @TableField(value = "forget_remark")
    private String forgetRemark;//采购协议备忘录
    @TableField(value = "operate_protocl")
    private String operatePro;//招行代运营协议

    @TableField(value = "city_id")
    private Integer cityId;//

    @TableField(value = "hotel_id")
    private Integer hotelId;//

    @TableField(value = "shop_id")
    private Integer shopId;//

    @TableField(value = "create_time")
    private Date createTime;//

    @TableField(value = "update_time")
    private Date updateTime;//

    @TableField(value = "create_user")
    private String createUser;//

    @TableField(value = "update_user")
    private String updateUser;//

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
