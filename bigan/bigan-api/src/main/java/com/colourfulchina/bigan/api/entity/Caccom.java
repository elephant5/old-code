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
 * C端项目住宿含套餐
 */
@Data
@TableName("CLF_Caccom")
public class Caccom extends Model<Caccom> {
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
     * 产品名称
     */
    @TableField(value = "product_name")
    private String productName;
    /**
     * 房型
     */
    @TableField(value = "house_type")
    private String houseType;
    /**
     * 售卖期限
     */
    @TableField(value = "sale_period")
    private String salePeriod;
    /**
     * 货币单位
     */
    @TableField(value = "unit")
    private String unit;
    /**
     * 换算价
     */
    @TableField(value = "price")
    private String price;
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
     * 傲乐协议价
     */
    @TableField(value = "pro_price")
    private String proPrice;
    /**
     * 产品规格
     */
    @TableField(value = "standard")
    private String standard;
    /**
     * 产品内容
     */
    @TableField(value = "content")
    private String content;
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
     * 适用条款
     */
    @TableField(value = "tiaokuan")
    private String tiaokuan;
    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;
    /**
     * 挂星
     */
    @TableField(value = "guaxing")
    private String guaxing;
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
