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
@TableName("shop")
public class Shop extends Model<Shop> {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.INPUT)
    private Long id;
    @TableField("type")
    private String type;
    @TableField("hotel_id")
    private Long hotelId;
    @TableField("hotel")
    private String hotel;
    @TableField("name")
    private String name;
    @TableField("name_en")
    private String nameEn;
    @TableField("py")
    private String py;
    @TableField("title")
    private String title;
    @TableField("city_id")
    private Long cityId;
    @TableField("city")
    private String city;
    @TableField("oversea")
    private String oversea;
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
    @TableField("level")
    private String level;
    @TableField("protocol")
    private Integer protocol;
    @TableField("consignee")
    private String consignee;
    @TableField("currency")
    private String currency;
    @TableField("settle_method")
    private String settleMethod;
    @TableField("summary")
    private String summary;
    @TableField("create_time")
    private Date createTime;
    @TableField("channel_id")
    private Long channelId;
    @TableField("geo_id")
    private Long geoId;
    @TableField("contract_expiry")
    private Date contractExpiry;
    @TableField("portal")
    private String portal;
    @TableField("opentime")
    private String openTime;
    @TableField("__block_rule")
    private String blockRule;
    @TableField("more")
    private String more;
    @TableField("title_en")
    private String titleEn;
    @TableField("block")
    private String block;
    @Override
    public String toString() {
        return "Shop{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", hotelId=" + hotelId +
                ", hotel='" + hotel + '\'' +
                ", name='" + name + '\'' +
                ", nameEn='" + nameEn + '\'' +
                ", py='" + py + '\'' +
                ", title='" + title + '\'' +
                ", cityId=" + cityId +
                ", city='" + city + '\'' +
                ", oversea=" + oversea +
                ", address='" + address + '\'' +
                ", addressEn='" + addressEn + '\'' +
                ", phone='" + phone + '\'' +
                ", fax='" + fax + '\'' +
                ", notes='" + notes + '\'' +
                ", level='" + level + '\'' +
                ", protocol=" + protocol +
                ", consignee='" + consignee + '\'' +
                ", currency='" + currency + '\'' +
                ", settleMethod='" + settleMethod + '\'' +
                ", summary='" + summary + '\'' +
                ", createTime=" + createTime +
                ", channelId=" + channelId +
                ", geoId=" + geoId +
                ", contractExpiry=" + contractExpiry +
                ", portal='" + portal + '\'' +
                ", openTime='" + openTime + '\'' +
                ", blockRule='" + blockRule + '\'' +
                ", more='" + more + '\'' +
                ", titleEn='" + titleEn + '\'' +
                ", block='" + block + '\'' +
                '}';
    }
    @Override
    protected Serializable pkVal() {
        return this.id;
    }


}
