package com.colourfulchina.pangu.taishang.api.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldStrategy;
import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 商户基础信息
 */
@Data
@TableName(value = "shop")
public class Shop extends Model<Shop> {
    /**
     * 商户ID
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    /**
     * Charlie商户ID
     */
    @TableField("old_shop_id")
    private Integer oldShopId;
    /**
     * 商户类型
     */
    @TableField("shop_type")
    private String shopType;
    /**
     * 商户所属城市(仅限于独立商户，非独立商户跟随所属酒店)
     */
    @TableField("city_id")
    private Integer cityId;
    /**
     * 酒店ID
     */
    @TableField(value = "hotel_id")
    private Integer hotelId;
    /**
     * 门店名
     */
    @TableField(value = "name")
    private String name;
    /**
     * 门店英文名
     */
    @TableField(value = "name_en")
    private String nameEn;
    /**
     * 商户拼音
     */
    @TableField(value = "py")
    private String py;
    /**
     * 中文地址
     */
    @TableField("address")
    private String address;
    /**
     * 英文地址
     */
    @TableField("address_en")
    private String addressEn;
    /**
     * 商户电话
     */
    @TableField(value = "phone")
    private String phone;
    /**
     * 开始营业时间
     */
    @TableField("open_time")
    private String openTime;
    /**
     * 结束营业时间
     */
    @TableField("close_time")
    private String closeTime;
    /**
     * 入住时间(住宿类型时)
     */
    @TableField("check_in_time")
    private String checkInTime;
    /**
     * 退房时间(住宿类型时)
     */
    @TableField("check_out_time")
    private String checkOutTime;
    /**
     * 商户级别
     */
    @TableField(value = "level")
    private String level;
    /**
     * 录单提示
     */
    @TableField("tips")
    private String tips;
    /**
     * 备注
     */
    @TableField("notes")
    private String notes;
    /**
     * 位置id
     */
    @TableField("geo_id")
    private Integer geoId;
    /**
     * 商户性质(0附属商户，1独立商户)
     */
    @TableField("shop_nature")
    private Integer shopNature;
    /**
     * 商铺介绍
     */
    @TableField("summary")
    private String summary;
    /**
     * 商户状态（0上架，1下架）
     */
    @TableField("shop_status")
    private Integer shopStatus;
    /**
     * 上下架时间
     */
    @TableField("status_time")
    private Date statusTime;
    /**
     * 删除标识（0-正常，1-删除）
     */
    @TableField(value = "del_flag")
    private Integer delFlag;
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
    @TableField(value = "update_time",update = "CURRENT_TIMESTAMP()")
    private Date updateTime;
    /**
     * 更新人
     */
    @TableField(value = "update_user")
    private String updateUser;

    @ApiModelProperty("最多提前N预定")
    @TableField(value = "max_book_days",strategy = FieldStrategy.IGNORED)
    private Integer  maxBookDays;
    @ApiModelProperty("最少提前N预定")
    @TableField(value = "min_book_days",strategy = FieldStrategy.IGNORED)
    private Integer  minBookDays ;
    @ApiModelProperty("商户详情")
    @TableField(value = "detail")
    private String detail;
    /**
     * 销售量
     */
    @TableField("sales_count")
    private Long salesCount;
    /**
     * 点击量
     */
    @TableField("point_count")
    private Long pointCount;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}