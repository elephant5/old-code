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
 * 住宿列表
 */
@Data
@TableName("clf_accom")
public  class AccomList extends Model<AccomList> {
    private static final long serialVersionUID = 1L;
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;//primary key
    @TableField(value = "country")
    private String country;//国家
    @TableField(value = "country_code")
    private String countryCode;//国家
    @TableField(value = "city")
    private String city;//城市
    @TableField(value = "city_id")
    private Integer cityId;//城市
    @TableField(value = "hot_hotel")
    private String hotHotel;//热推酒店
    @TableField(value = "hot")
    private Integer hot;//热推酒店
    @TableField(value = "hotel_id")
    private Integer hotelId;//酒店ID
    @TableField(value = "hotel_ch")
    private String hotelCh;//酒店中文名称
    @TableField(value = "hotel_en")
    private String hotelEn;//酒店英文名称

    @TableField(value = "keep_room")
    private String keepRoom;//保留房
    @TableField(value = "room_type")
    private String roomType;//房型
    @TableField(value = "room_type_old")
    private String roomTypeOld;//房型
    @TableField(value = "bed_type")
    private String bedType;//床型
    @TableField(value = "restaurant_type")
    private String restaurantType;//餐型
    @TableField(value = "sale_start")
    private Date saleStart;//售卖期限
    @TableField(value = "sale_end")
    private Date saleEnd;//售卖期限
    @TableField(value = "coin_unit")
    private String coinUnit;//货币单位
    @TableField(value = "change_price")
    private String changePrice;//换算价
    @TableField(value = "contract_start")
    private Date contractStart;//合同开始
    @TableField(value = "contract_end")
    private Date contractEnd;//合同截止
    @TableField(value = "between_price")
    private String betweenPrice;//价格区间
    @TableField(value = "week_price")
    private String weekPrice;//周中均价（rmb)
    @TableField(value = "weekend_price")
    private String weekendPrice;//周末均价（rmb)
    @TableField(value = "low_price")
    private String lowPrice;//最低价
    @TableField(value = "avg_price")
    private String avgPrice;
    @TableField(value = "best_price")
    private String bestPrice;
    @TableField(value = "address")
    private String address;
    @TableField(value = "phone")
    private String phone;
    @TableField(value = "use_element")
    private String useElement;
    @TableField(value = "remark")
    private String remark;
    @TableField(value = "stars")
    private String stars;//挂星
    @TableField(value = "channel")
    private String channel;//资源渠道
    @TableField(value = "channel_id")
    private Integer channelId;//资源渠道ID
    @TableField(value = "shop_name")
    private String shopName;//
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