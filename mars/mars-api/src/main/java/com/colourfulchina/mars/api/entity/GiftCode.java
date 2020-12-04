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

@TableName("gift_code")
@Data
public class GiftCode extends Model<GiftCode> {
	private static final long serialVersionUID = 247865592442736933L;

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
	@TableField(value = "code_batch_no")
	private String codeBatchNo;
	/**
	 * 码状态
	 */
	@ApiModelProperty("码状态")
	@TableField(value = "act_code_status")
	private Integer actCodeStatus;
	/**
	 * 激活码
	 */
	@ApiModelProperty("激活码")
	@TableField(value = "act_code")
	private String actCode;
	/**
	 * 激活码出库时间
	 */
	@ApiModelProperty("激活码出库时间")
	@TableField(value = "act_out_date" )
	private Date actOutDate;
	/**
	 * 激活码规则
	 */
	@ApiModelProperty("激活码规则")
	@TableField(value = "act_rule")
	private String actRule;
	/**
	 * 激活码到期时间（权益失效的时间）
	 */
	@ApiModelProperty("激活码到期时间（权益失效的时间）")
	@TableField(value = "act_expire_time")
	private Date actExpireTime;
	/**
	 * 激活时间
	 */
	@ApiModelProperty("激活码激活到期时间(激活失效时间）")
	@TableField(value = "act_end_time")
	private Date actEndTime;
	/**
	 * 激活时间
	 */
	@ApiModelProperty("激活时间")
	@TableField(value = "act_code_time")
	private Date actCodeTime;
	/**
	 * 项目id
	 */
	@ApiModelProperty("项目id")
	@TableField(value = "goods_id")
	private Integer goodsId;
	/**
	 * 销售渠道id
	 */
	@ApiModelProperty("销售渠道id")
	@TableField(value = "sales_channel_id")
	private Integer salesChannelId;
	/**
	 * 购买人会员信息
	 */
	@ApiModelProperty("购买人会员信息")
	@TableField(value = "buy_member_id")
	private Long buyMemberId;
	/**
	 * 激活人id
	 */
	@ApiModelProperty("激活人id")
	@TableField(value = "member_id")
	private Long memberId;
	/**
	 * 标签
	 */
	@ApiModelProperty("标签")
	@TableField(value = "tags")
	private String tags;
	/**
	 * 生成备注说明
	 */
	@ApiModelProperty("生成备注说明")
	@TableField(value = "remarks")
	private String remarks;
	/**
	 * 出库备注说明
	 */
	@ApiModelProperty("出库备注说明")
	@TableField(value = "out_remarks")
	private String outRemarks;
	/**
	 * 激活备注说明
	 */
	@ApiModelProperty("激活备注说明")
	@TableField(value = "active_remarks")
	private String activeRemarks;
	/**
	 * 退货备注说明
	 */
	@ApiModelProperty("退货备注说明")
	@TableField(value = "return_remarks")
	private String returnRemarks;
	@ApiModelProperty("老系统unitId")
	@TableField(value = "unit_id")
	private Integer unitId;

	/**
	 * 作废备注说明
	 */
	@ApiModelProperty("作废备注说明")
	@TableField(value = "obsolete_remarks")
	private String obsoleteRemarks;
	@TableField(value = "del_flag")
	private Integer delFlag;
	@TableField(value = "create_time")
	private Date createTime;
	@TableField(value = "create_user")
	private String createUser;
	@TableField(value = "update_time",update = "CURRENT_TIMESTAMP()")
	private Date updateTime;
	@TableField(value = "update_user")
	private String updateUser;
	/**
	 * 激活码退货时间
	 */
	@TableField("act_return_date")
	private Date actReturnDate;
	/**
	 * 激活码作废时间
	 */
	@TableField("act_obsolete_date")
	private Date actObsoleteDate;



	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
