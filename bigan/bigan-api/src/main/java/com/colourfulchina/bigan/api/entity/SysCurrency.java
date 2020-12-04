package com.colourfulchina.bigan.api.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;

/**
 * 结算币种
 */
@Data
@TableName("sys_currency")
public class SysCurrency extends Model<SysCurrency> {
    private static final long serialVersionUID = 7404639943468736736L;

    /**
     * 货币code
     */
    @TableId(value = "code",type = IdType.INPUT)
    private String code;
    /**
     * 币种名称
     */
    @TableField("name")
    private String name;
    /**
     * 币种符号
     */
    @TableField("symbol")
    private String symbol;

    @Override
    protected Serializable pkVal() {
        return this.code;
    }
}
