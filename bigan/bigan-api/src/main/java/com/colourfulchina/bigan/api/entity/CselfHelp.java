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
 * @author wujun
 * c端项目_自助
 */
@Data
@TableName("Clf_Cself_Help")
public class CselfHelp extends Model<CselfHelp> {
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

    @TableField(value = "service_type")
    private String serviceType;//权益项目
    @TableField(value = "marketing_price")
    private String marketingPrice;//市场价格
    @TableField(value = "meal_selection")
    private String mealSelection;//餐段
    @TableField(value = "lunch_time")
    private String lunchTime;
    @TableField(value = "dinner_time")
    private String dinnerTime;//晚餐开餐时间

    @TableField(value = "net_price")
    private String netPrice;//净价

    @TableField(value = "service_cost")
    private String serviceCost;//服务费
    @TableField(value = "rate")
    private String rate;//税费
    @TableField(value = "cost")
    private String cost;//成本

    @TableField(value = "sale_rule")
    private String saleRule;//售卖规则
    @TableField(value = "prepayment")
    private String prepayment;//合同约定预付
    @TableField(value = "cash")
    private String cash;//押金

    @TableField(value = "product_use_start")
    private String productUseStart;//产品使用日期
    @TableField(value = "product_use_end")
    private String productUseEnd;//产品结束日期
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