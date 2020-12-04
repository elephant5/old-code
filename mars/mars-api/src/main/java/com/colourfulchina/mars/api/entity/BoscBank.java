package com.colourfulchina.mars.api.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@TableName("bosc_bank")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoscBank extends Model<BoscBank> {
	/**   
	 * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)   
	 */ 
	private static final long serialVersionUID = -7466706376334449514L;
	
	@TableId(value = "id",type = IdType.AUTO)
    private Integer id;
	@TableField(value = "batch")
	private String batch; //批次号 YYYYMMDD
	@TableField(value = "card_no")
	private String cardNo; // 卡号   加密
	@TableField(value = "card_progroup_no")
	private String cardProgroupNo; //卡产品组代码
	@TableField(value = "card_type")
	private String cardType; // 附卡标志  B-主卡，S-附卡
	@TableField(value = "last_four_no")
	private String lastFourNo;  //卡号末四位
	@TableField(value = "ecif")
	private String ecif; //ECIF客户号  加密
	@TableField(value = "name")
	private String name; //姓名
	@TableField(value = "mobile")
	private String mobile; //手机号
	@TableField(value = "main_card_no")
	private String mainCardNo; // 主卡卡号  加密
	@TableField(value = "remark")
	private String remark;//预留字段
	
	@TableField(value = "create_time")
	private Date createTime;
	@TableField(value = "update_time",update = "CURRENT_TIMESTAMP()")
	private Date updateTime;

	@TableField(value = "unified_product_param_code")
	private String unifiedProductParamCode; //统一产品参数代码

	@TableField(value = "card_set")
	private String cardSet; //套卡，0非套卡，1套卡

	@TableField(value = "del_flag")
	private Integer delFlag;

	@TableField(exist = false)
	private Integer status =0;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		BoscBank boscBank = (BoscBank) o;
		return Objects.equals(cardProgroupNo, boscBank.cardProgroupNo) &&
				Objects.equals(ecif, boscBank.ecif);
	}

	@Override
	public int hashCode() {

		return Objects.hash(cardProgroupNo, ecif);
	}

	@Override
    protected Serializable pkVal() {
        return this.id;
    }


}
