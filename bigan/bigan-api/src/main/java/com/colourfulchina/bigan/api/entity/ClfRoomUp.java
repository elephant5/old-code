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
 * 客乐芙酒店客房升等
 */
@Data
@TableName("Clf_Room_Up")
public class ClfRoomUp extends Model<ClfRoomUp> {
    private static final long serialVersionUID = 1L;
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;//primary key

    /**
     * 国家
     */
    @TableField(value = "country")
    private String country;
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
     * 签约人
     */
    @TableField(value = "sign_contract_user")
    private String signContractUser;
    /**
     * 商户负责人
     */
    @TableField(value = "shop_user")
    private String shopUser;

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
     * 可升级房型
     */
    @TableField(value = "up_room_before")
    private String upRoomBefore;
    /**
     * 升级后房型
     */
    @TableField(value = "up_room_down")
    private String upRoomDown;
    /**
     * 货币单位
     */
    @TableField(value = "unit")
    private String unit;

    /**
     * 成本
     */
    @TableField(value = "cost")
    private String cost;
    /**
     * 成本（RMB）
     */
    @TableField(value = "cost_rmb")
    private String costRmb;

    /**
     * 签约日期
     */
    @TableField(value = "contract_date")
    private String contractDate;
    /**
     * 合同截止
     */
    @TableField(value = "contract_end")
    private String contractEnd;
    /**
     * 酒店地址
     */
    @TableField(value = "hotel_address")
    private String hotelAddress;
    /**
     * 结算方式
     */
    @TableField(value = "pay_method")
    private String payMethod;
    /**
     * 结算周期
     */
    @TableField(value = "pay_cycle")
    private String payCycle;
    /**
     * 结算联系人
     */
    @TableField(value = "link_man")
    private String linkMan;
    /**
     * 结算联系人电话
     */
    @TableField(value = "phone")
    private String phone;
    /**
     * 结算联系人邮箱
     */
    @TableField(value = "email")
    private String email;
    /**
     * 销售联系人
     */
    @TableField(value = "sales_user")
    private String salesUser;
    /**
     * 销售联系人邮箱
     */
    @TableField(value = "sale_email")
    private String saleEmail;
    /**
     * 销售联系人电话
     */
    @TableField(value = "sales_telephone")
    private String salesTelephone;
    /**
     * 销售联系人手机
     */
    @TableField(value = "sales_phone")
    private String salesPhone;
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
