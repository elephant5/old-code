package com.colourfulchina.pangu.taishang.api.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("shop_type_service")
public class ShopTypeService extends Model<ShopTypeService> {

    /**
     * 编码
     */
    @TableId(value = "code",type=IdType.INPUT)
    private String code;

    /**
     * 商户类型
     */
    private String shopType;
    /**
     * 名称
     */
    private String name;
    /**
     * 备注描述
     */
    private String remark;
    /**
     * 是否删除
     */
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
    @Override
    protected Serializable pkVal() {
        return this.code;
    }
}
