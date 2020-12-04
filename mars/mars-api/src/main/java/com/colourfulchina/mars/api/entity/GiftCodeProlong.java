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

/**
 * 激活码延期表
 * @Author: luck.luo
 * @Description:
 * @Date: 2019/7/26 9:57
 */
@TableName("gift_code_prolong")
@Data
public class GiftCodeProlong extends Model<GiftCodeProlong> {
    /**
     * 主键id
     */
    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 激活码id
     */
    @ApiModelProperty("激活码id")
    @TableField(value = "gift_code_id")
    private Integer giftCodeId;

    /**
     * 延期规则
     */
    @ApiModelProperty("延期规则")
    @TableField(value = "prolong_rule")
    private String prolongRule;

    /**
     * 激活码原到期时间
     */
    @ApiModelProperty("激活码原到期时间")
    @TableField(value = "act_expire_time")
    private Date actExpireTime;

    /**
     * 激活码延期后到期时间
     */
    @ApiModelProperty("激活码延期后到期时间")
    @TableField(value = "prolong_expire_time")
    private Date prolongExpireTime;

    /**
     * 延期备注
     */
    @ApiModelProperty("延期备注")
    @TableField(value = "remarks")
    private String remarks;

    private Date prolongEndTime;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
