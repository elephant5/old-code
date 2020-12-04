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
 * 客乐芙_健身
 */
@Data
@TableName("Clf_Gym")
public class ClfGym extends Model<ClfGym> {
    private static final long serialVersionUID = 1L;
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;//primary key

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
     * 健身房中文名
     */
    @TableField(value = "gym_ch_name")
    private String gymChName;
    /**
     * 健生房英文名
     */
    @TableField(value = "gym_en_name")
    private String gymEnName;

    /**
     * 权益项目
     */
    @TableField(value = "service_type")
    private String serviceType;

    /**
     * 电子券+纸质券
     */
    @TableField(value = "dianzi_and_zhizhi")
    private String dianziAndzhizhi;
    /**
     * 电子券
     */
    @TableField(value = "dianzi")
    private String dianzi;
    /**
     * 纸质券
     */
    @TableField(value = "zhizhi")
    private String zhizhi;

    /**
     * 其他券
     */
    @TableField(value = "other")
    private String other;

    /**
     * 合同数
     */
    @TableField(value = "contract_num")
    private String contractNum;
    /**
     * 签约人
     */
    @TableField(value = "sign_contract_user")
    private String signContractUser;
    /**
     * 维护人
     */
    @TableField(value = "weihu")
    private String weihu;
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
     * 价格
     */
    @TableField(value = "price")
    private String price;

    /**
     * 净价
     */
    @TableField(value = "net_price")
    private String netPrice;
    /**
     * 总价
     */
    @TableField(value = "all_price")
    private String allPrice;
    /**
     * 成本
     */
    @TableField(value = "cost")
    private String cost;
    /**
     * 结算价
     */
    @TableField(value = "end_price")
    private String endPrice;

    /**
     * 合同约定预付
     */
    @TableField(value = "contract_price")
    private String contractPrice;

    /**
     * 押金
     */
    @TableField(value = "cash")
    private String cash;
    /**
     * 签约日期
     */
    @TableField(value = "contract_date")
    private Date contractDate;

    /**
     * 合同截止
     */
    @TableField(value = "contract_end")
    private Date contractEnd;
    /**
     * 健身房营业时间
     */
    @TableField(value = "open_time")
    private String openTime;
    /**
     * 适用条款
     */
    @TableField(value = "tiaokuan")
    private String tiaokuan;
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
     * 有无游泳池
     */
    @TableField(value = "swimming_pool")
    private String swimmingPool;
    /**
     * 泊车信息
     */
    @TableField(value = "parking")
    private String parking;
    /**
     * 儿童政策
     */
    @TableField(value = "kids")
    private String kids;
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
