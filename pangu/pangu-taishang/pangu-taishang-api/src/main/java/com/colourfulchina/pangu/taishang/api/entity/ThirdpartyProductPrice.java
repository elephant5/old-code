package com.colourfulchina.pangu.taishang.api.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 第三方资源价格表
 */
@Data
@TableName(value = "thirdparty_product_price")
public class ThirdpartyProductPrice extends Model<ThirdpartyProductPrice> {
    private static final long serialVersionUID = 8614993444024221928L;
    /**
     * 主键
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    /**
     * 渠道id
     */
    @TableField(value = "channel_id")
    private Integer channelId;
    @TableField(value = "hotel_group_id")
    private Integer hotelGroupId;
    @TableField(value = "hotel_id")
    private Integer hotelId;
    @TableField(value = "shop_id")
    private Integer shopId;
    @TableField(value = "shop_item_id")
    private Integer shopItemId;
    /**
     * 集团拼音
     */
    @TableField(value = "third_code")
    private String thirdCode;
    @TableField(value = "price")
    private BigDecimal price;
    @TableField(value = "price_date")
    private Date priceDate;
    //年月日时分
    @TableField(value = "batch_id")
    private Long batchId;
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
    /**
     * 删除标识（0-正常，1-删除）
     */
    @TableField(value = "del_flag")
    private Integer delFlag;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}