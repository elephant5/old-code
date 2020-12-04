package com.colourfulchina.mars.api.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@TableName("order_code")
@Data
public class OrderCode extends Model<OrderCode> {
    private static final long serialVersionUID = -8670649280327741795L;

    /**
     * 激活码表id
     */
    @ApiModelProperty("激活码表id")
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    /**
     * 激活码批次号
     */
    @ApiModelProperty("激活码批次号")
    @TableField("order_batch_no")
    private String orderBatchNo;
    /**
     * 码状态
     */
    @ApiModelProperty("码状态")
    @TableField("act_code_status")
    private String actCodeStatus;
    /**
     * 激活码
     */
    @ApiModelProperty("激活码")
    @TableField("act_code")
    private String actCode;
    /**
     * 激活码出库时间2019-01-01
     */
    @ApiModelProperty("激活码出库时间2019-01-01")
    @TableField("act_out_date")
    private Date actOutDate;
    /**
     * 激活码规则
     */
    @ApiModelProperty("激活码规则")
    @TableField("act_rule")
    private String actRule;
    /**
     * 激活码到期时间
     */
    @ApiModelProperty("激活码到期时间")
    @TableField("act_expire_time")
    private Date actExpireTime;
    /**
     * 激活时间
     */
    @ApiModelProperty("激活时间")
    @TableField("act_code_time")
    private Date actCodeTime;
    /**
     * 项目id
     */
    @ApiModelProperty("项目id")
    @TableField("act_project_id")
    private Integer actProjectId;
    /**
     * 关联项目名称
     */
    @ApiModelProperty("关联项目名称")
    @TableField("act_project_name")
    private String actProjectName;
    /**
     * 销售渠道id
     */
    @ApiModelProperty("销售渠道id")
    @TableField("sales_channel_id")
    private Integer salesChannelId;
    /**
     * 销售渠道名称
     */
    @ApiModelProperty("销售渠道名称")
    @TableField("sales_channel_name")
    private String salesChannelName;
    /**
     * 销售方式id
     */
    @ApiModelProperty("销售方式id")
    @TableField("sales_channel_type_id")
    private Integer salesChannelTypeId;
    /**
     * 销售方式：直营
     */
    @ApiModelProperty("销售方式：直营")
    @TableField("sales_channel_type")
    private String salesChannelType;
    /**
     * 银行信息id
     */
    @ApiModelProperty("银行信息id")
    @TableField("sales_channel_bank_id")
    private Integer salesChannelBankId;
    /**
     * 银行信息:建设银行、微信、支付、开联通
     */
    @ApiModelProperty("银行信息:建设银行、微信、支付、开联通")
    @TableField("sales_channel_bank")
    private String salesChannelBank;
    /**
     * 操作人
     */
    @ApiModelProperty("操作人")
    @TableField("operator")
    private String operator;
    /**
     * 操作时间
     */
    @ApiModelProperty("操作时间")
    @TableField("operator_time")
    private Date operatorTime;
    /**
     * 所属人id
     */
    @ApiModelProperty("所属人id")
    @TableField("act_persion_id")
    private String actPersionId;
    /**
     * 渠道结算方式
     */
    @ApiModelProperty("渠道结算方式")
    @TableField("settle_channel_type")
    private String settleChannelType;
    /**
     * 商户结算方式
     */
    @ApiModelProperty("商户结算方式")
    @TableField("settle_mcth_type")
    private String settleMcthType;
    /**
     * 标签id
     */
    @ApiModelProperty("标签id")
    @TableField("tag_id")
    private Integer tagId;
    /**
     * 佣金比例
     */
    @ApiModelProperty("佣金比例")
    @TableField("amount_rate")
    private String amountRate;
    /**
     * 激活状态 默认0 激活1
     */
    @ApiModelProperty("激活状态 默认0 激活1")
    @TableField("act_state")
    private Integer actState;
    /**
     * 备注说明
     */
    @ApiModelProperty("备注说明")
    @TableField("standby")
    private String standby;
    /**
     * 激活码绑定状态（1已绑定：部分销售单导前时没有激活码，导入时绑定激活码）
     */
    @ApiModelProperty("激活码绑定状态（1已绑定：部分销售单导前时没有激活码，导入时绑定激活码）")
    @TableField("bangding_flg")
    private String bangdingFlg;
    /**
     * 删除标识（0正常，1删除）
     */
    @TableField("del_flag")
    private Integer delFlag;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 创建人
     */
    @TableField("create_user")
    private String createUser;
    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;
    /**
     * 更新人
     */
    @TableField("update_user")
    private String updateUser;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
