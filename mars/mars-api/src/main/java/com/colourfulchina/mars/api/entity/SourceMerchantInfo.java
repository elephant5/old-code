package com.colourfulchina.mars.api.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 渠道终端与商户关系
 */
@TableName("source_merchant_info")
@Data
public class SourceMerchantInfo extends Model<SourceMerchantInfo> {

    private static final long serialVersionUID = 5137270788670164547L;
    @TableId(value = "source",type = IdType.INPUT)
    private String source;
    @TableField(value = "merchant_id")
    private Long merchantId;
    @TableField(value = "merchant_key")
    private String merchantKey;
    @TableField("remark")
    private String remark;
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
        return this.source;
    }
}
