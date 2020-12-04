package com.colourfulchina.mars.api.entity;

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
 * @Author: 罗幸
 * @Description:
 * @Date: 2020/3/13 17:47
 */
@Data
@TableName("logistics_info")
public class LogisticsInfo extends Model<LogisticsInfo> {
    private static final long serialVersionUID = 1484359011471402919L;

    //订单ID
    @TableId(value = "reserv_order_id",type = IdType.INPUT)
    private Integer reservOrderId;
    //快递公司(字典表中value)
    @TableField(value = "express_name_id")
    private String expressNameId;
    //快递单号
    @TableField(value = "express_number")
    private String expressNumber;
    //发货时间
    @TableField(value = "express_date")
    private Date expressDate;
    //收件人
    @TableField(value = "consignee")
    private String consignee;
    //手机号
    @TableField(value = "phone")
    private String phone;
    //地址
    @TableField(value = "address")
    private String address;
    //邮编
    @TableField(value = "code")
    private String code;
    //省份
    @TableField(value = "province")
    private String province;
    //市
    @TableField(value = "city")
    private String city;
    //发货人
    @TableField(value = "shipper")
    private String shipper;
    //发货人手机号
    @TableField(value = "shipper_phone")
    private String shipperPhone;
    //发货人地址
    @TableField(value = "shipper_address")
    private String shipperAddress;
    //发货人邮编
    @TableField(value = "shipper_code")
    private String shipperCode;
    //物流费用
    @TableField(value = "cost")
    private BigDecimal cost;
    //重量
    @TableField(value = "weight")
    private BigDecimal weight;
    //商品类型
    @TableField(value = "shop_type")
    private String shopType;
    //注意事项
    @TableField(value = "attention")
    private String attention;
    //物流状态，0未发货1已发货2已收货3已退货
    @TableField(value = "status")
    private Integer status;
    //配送方式（0-无需配送，1-快递发货，2-及时送达，3-到店自取）
    @TableField(value = "express_mode")
    private Integer expressMode;
    //删除标识（0-正常，1-删除）
    @TableField(value = "del_flag")
    private int delFlag;
    //创建时间
    @TableField(value = "create_time")
    private Date createTime;
    //创建人
    @TableField(value = "create_user")
    private String createUser;
    //更新时间
    @TableField(value = "update_time")
    private Date updateTime;
    //更新人
    @TableField(value = "update_user")
    private String updateUser ;

    @Override
    protected Serializable pkVal() {
        return this.reservOrderId;
    }
}
