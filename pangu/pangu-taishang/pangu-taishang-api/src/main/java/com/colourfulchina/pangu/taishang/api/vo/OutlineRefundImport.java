package com.colourfulchina.pangu.taishang.api.vo;

import com.colourfulchina.pangu.taishang.api.annotation.ExcelColumn;
import lombok.Data;

import java.io.Serializable;

@Data
public class OutlineRefundImport implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 交易日
     */
    @ExcelColumn(value = "交易日", col = 1)
    private String saleDateStr;

    /**
     * 借款金额
     */
    @ExcelColumn(value = "借", col = 2)
    private Double investAmount;

    /**
     * 贷
     */
    @ExcelColumn(value = "贷", col = 3)
    private Double leadingAmount;

    /**
     * 摘要
     */
    @ExcelColumn(value = "摘要", col = 4)
    private String summer;

    /**
     * 收(付)方名称
     */
    @ExcelColumn(value = "收(付)方名称", col = 5)
    private String sName;

    /**
     * 收(付)方帐号
     */
    @ExcelColumn(value = "收(付)方帐号", col = 6)
    private String sAccount;

    /**
     * 交易类型
     */
    @ExcelColumn(value = "交易类型", col = 7)
    private String saleType;

    /**
     * 备注
     */
    @ExcelColumn(value = "备注", col = 8)
    private String comments;

}
