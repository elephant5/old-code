package com.colourfulchina.mars.api.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@TableName("bosc_bank_log")
@Data
public class BoscBankLog extends Model<BoscBankLog> {
	/**   
	 * @Fields serialVersionUID :
	 */ 
	private static final long serialVersionUID = -7466706376334449514L;
	
	@TableId(value = "id",type = IdType.AUTO)
    private Integer id;

	@TableField(value = "bosc_bank_id")
	private Integer boscBankId; //客户数据主键

	@TableField(value = "basic_update_before")
	private String basicUpdateBefore; //基础数据变更前

	@TableField(value = "basic_update_after")
	private String basicUpdateAfter; //基础数据变更后


	@TableField(value = "operation")
	private String operation; //操作

	@TableField(value = "handle_result")
	private String handleResult; //处理结果

	@TableField(value = "remark")
	private String remark;//预留字段

	@TableField(value = "create_time")
	private Date createTime;

	@TableField(value = "create_user")
	private String createUser;

	
	@Override
    protected Serializable pkVal() {
        return this.id;
    }
	
}
