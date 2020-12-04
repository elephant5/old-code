package com.colourfulchina.pangu.taishang.api.vo;

import com.colourfulchina.pangu.taishang.api.annotation.ExcelColumn;
import lombok.Data;

import java.io.Serializable;

@Data
public class OutlineRefundInfoExport  implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单号
     */
    @ExcelColumn(value = "订单号", col = 1)
    private String orderNo;

    /**
     * 关联商品或产品名称
     */
    @ExcelColumn(value = "关联商品|产品", col = 2)
    private String productName;

    /**
     * 酒店或商户名称
     */
    @ExcelColumn(value = "酒店|商户", col = 3)
    private String merchantName;

    /**
     * 收款方
     */
    @ExcelColumn(value = "收款方", col = 4)
    private String refundReciever;

    /**
     * 激活码
     */
    @ExcelColumn(value = "激活码", col = 5)
    private String actCode;

    /**
     * 订单时间
     */
    @ExcelColumn(value = "订单时间", col = 6)
    private String orderTime;

    /**
     * 订单来源
     */
    @ExcelColumn(value = "订单来源", col = 7)
    private String orderSource;

    /**
     * 退款总价
     */
    @ExcelColumn(value = "退款总价", col = 8)
    private Double refundPrice;

    /**
     * 退款状态
     */
    @ExcelColumn(value = "退款状态", col = 9)
    private String refundStatus;

    /**
     * 退款时间
     */
    @ExcelColumn(value = "退款时间", col = 10)
    private String refundTime;

    /**
     * 退款渠道
     */
    @ExcelColumn(value = "退款渠道", col = 11)
    private String refundChannel;

}
