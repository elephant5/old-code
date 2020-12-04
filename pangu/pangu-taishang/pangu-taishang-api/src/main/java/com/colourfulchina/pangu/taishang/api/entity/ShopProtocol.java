package com.colourfulchina.pangu.taishang.api.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldStrategy;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 商户扩展信息
 */
@Data
@TableName(value = "shop_protocol")
public class ShopProtocol extends Model<ShopProtocol> {
    /**
     * 与shop表ID一一对应
     */
    @TableId(value = "id",type = IdType.INPUT)
    private Integer id;
    /**
     * 渠道编号
     */
    @TableField("channel_id")
    private Integer channelId;
    /**
     * 币种
     */
    @TableField("currency")
    private String currency;
    /**
     * 结算方式
     */
    @TableField("settle_method")
    private String settleMethod;
    /**
     * 结算精度
     */
    @TableField("decimal")
    private Integer decimal;
    /**
     * 是否向上取值
     */
    @TableField("roundup")
    private String roundup;
    /**
     * 负责人
     */
    @TableField("principal")
    private String principal;
    /**
     * 预付金
     */
    @TableField("imprest")
    private BigDecimal imprest;
    /**
     * 押金
     */
    @TableField("deposit")
    private BigDecimal deposit;
    /**
     * 合同开始
     */
    @TableField("contract_start")
    private Date contractStart;
    /**
     * 合同截止
     */
    @TableField("contract_expiry")
    private Date contractExpiry;
    /**
     * 合同有效时间段，开始日期到结束日期
     */
    @TableField("contract_effective")
    private String contractEffective;
    /**
     * block规则
     */
    @TableField(value = "block_rule",strategy = FieldStrategy.IGNORED)
    private String blockRule;
    /**
     * 默认图片
     */
    @TableField("default_img_id")
    private Integer defaultImgId;
    /**
     * 停车政策
     */
    @TableField("parking")
    private String parking;
    /**
     * 儿童政策
     */
    @TableField("children")
    private String children;
    /**
     * 重要通知
     */
    @TableField("notice")
    private String notice;
    /**
     * 开户名
     */
    @TableField(value = "account_name")
    private String accountName;
    /**
     * 开户行
     */
    @TableField(value = "opening_bank")
    private String openingBank;
    /**
     * 银行账号
     */
    @TableField("bank_account")
    private String bankAccount;
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
    /**
     * "结算价格的变动对已经生成的预约单的影响
     */
    @TableField(value = "change_rice")
    private String changePrice;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}