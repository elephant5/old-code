package com.colourfulchina.bigan.api.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("hotel")
public class Hotel extends Model<Hotel> {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.INPUT)
    private Long id;
    @TableField(value = "name")
    private String name;
    @TableField("name_en")
    private String nameEn;
    @TableField("py")
    private String py;
    @TableField("city_id")
    private Integer cityId;
    @TableField("city")
    private String city;
    @TableField("oversea")
    private Integer oversea;
    @TableField("address")
    private String address;
    @TableField("address_en")
    private String addressEn;
    @TableField("phone")
    private String phone;
    @TableField("fax")
    private String fax;
    @TableField("notes")
    private String notes;
    @TableField("protocol")
    private Integer protocol;
    @TableField("consignee")
    private String consignee;
    @TableField("currency")
    private String currency;
    @TableField("settle_method")
    private String settleMethod;
    @TableField("book_before_days")
    private Integer bookBeforeDays;
    @TableField("cancel_before_days")
    private Integer cancelBeforeDays;
    @TableField("price_rule")
    private String priceRule;
    @TableField("summary")
    private String summary;
    @TableField("qunar_sn")
    private String qunarSn;
    @TableField("wifi")
    private String wifi;
    @TableField("geo_id")
    private Integer geoId;
    @TableField("portal")
    private String portal;
    @TableField("create_time")
    private Date createTime;
    @TableField("creator_id")
    private Integer creatorId;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Hotel{" +
                "id=" + id +
                ", name=" + name +
                ", nameEn='" + nameEn + '\'' +
                ", py='" + py + '\'' +
                ", cityId='" + cityId + '\'' +
                ", city='" + city + '\'' +
                ", oversea='" + oversea + '\'' +
                ", address='" + address + '\'' +
                ", addressEn='" + addressEn + '\'' +
                ", phone='" + phone + '\'' +
                ", fax='" + fax + '\'' +
                ", notes='" + notes + '\'' +
                ", protocol='" + protocol + '\'' +
                ", consignee='" + consignee + '\'' +
                ", currency='" + currency + '\'' +
                ", settleMethod='" + settleMethod + '\'' +
                ", bookBeforeDays='" + bookBeforeDays + '\'' +
                ", cancelBeforeDays='" + cancelBeforeDays + '\'' +
                ", priceRule='" + priceRule + '\'' +
                ", summary='" + summary + '\'' +
                ", qunarSn='" + qunarSn + '\'' +
                ", wifi='" + wifi + '\'' +
                ", geoId='" + geoId + '\'' +
                ", portal='" + portal + '\'' +
                ", createTime=" + createTime +
                ", creatorId=" + creatorId +
                '}';
    }
}
