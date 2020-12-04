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
 * 遨乐网与客乐芙_总住宿(旧)
 */
@Data
@TableName("Clf_Old_Accom")
public class ClfOldBuffet extends Model<ClfOldBuffet> {
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
     * 酒店中文名称
     */
    @TableField(value = "hotel_ch")
    private String hotelCh;
    /**
     * 酒店英文名称
     */
    @TableField(value = "hotel_en")
    private String hotelEn;
    /**
     * 酒店联系电话
     */
    @TableField(value = "hotel_phone")
    private String hotelPhone;

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
     * 房型
     */
    @TableField(value = "room_type")
    private String roomType;
    /**
     * 保留房
     */
    @TableField(value = "keep_room")
    private String keepRoom;
    /**
     * 月份
     */
    @TableField(value = "month")
    private String month;
    /**
     * 货币单位
     */
    @TableField(value = "unit")
    private String unit;
    /**
     * 协议价
     */
    @TableField(value = "pro_price")
    private String proPrice;
    /**
     * 协议价（RMB）
     */
    @TableField(value = "pro_price_rmb")
    private String proPriceRmb;
    /**
     * 是否含早
     */
    @TableField(value = "had_breakfast")
    private String hadBreakfast;
    /**
     * 携程最低价
     */
    @TableField(value = "xiecheng_down_price")
    private String xiechengDownPrice;
    /**
     * 携程平均价
     */
    @TableField(value = "xiecheng_avg_price")
    private String xiechengAvgPrice;
    /**
     * 携程最高价
     */
    @TableField(value = "xiecheng_best_price")
    private String xiechengBestPrice;
    /**
     * 合作银行
     */
    @TableField(value = "bank")
    private String bank;
    /**
     * 合同签约期
     */
    @TableField(value = "constract_day")
    private String constractDay;
    /**
     * 合同开始
     */
    @TableField(value = "contract_start")
    private String contractStart;
    /**
     * 合同截止
     */
    @TableField(value = "contract_end")
    private String contractEnd;
    /**
     * 预定提前天数
     */
    @TableField(value = "prep_days")
    private String prepDays;
    /**
     * 取消更改提前天数
     */
    @TableField(value = "cancel_days")
    private String cancelDays;
    /**
     * 预定方式
     */
    @TableField(value = "prep_method")
    private String prepMethod;
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
    @TableField(value = "pay_time")
    private String payTime;
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
