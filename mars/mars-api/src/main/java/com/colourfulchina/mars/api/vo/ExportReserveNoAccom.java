package com.colourfulchina.mars.api.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ExportReserveNoAccom extends BaseRowModel {

    @ExcelProperty(value = "序号", index = 0)
    private Integer rank;

    @ExcelProperty(value = "系统编号", index = 1)
    private Integer reserveId;

    @ExcelProperty(value = "预订月度", index = 2)
    private String createMonth;

    @ExcelProperty(value = "商品编号", index = 3)
    private Integer goodsId;

    @ExcelProperty(value = "商品简称", index = 4)
    private String goodsShortName;

    @ExcelProperty(value = "销售渠道", index = 5)
    private String salesChannel;

    @ExcelProperty(value = "销售年度", index = 6)
    private String salesYear;

    @ExcelProperty(value = "销售月度", index = 7)
    private String salesMonth;

    @ExcelProperty(value = "权益类型", index = 8)
    private String giftType;

    @ExcelProperty(value = "产品类型", index = 9)
    private String serviceType;

    @ExcelProperty(value = "订单日期", index = 10)
    private String createTime;

    @ExcelProperty(value = "接单侠", index = 11)
    private String orderCreator;

    @ExcelProperty(value = "预定状态", index = 12)
    private String reservOrderStatus;

    @ExcelProperty(value = "状态日期", index = 13)
    private String statusDate;

    @ExcelProperty(value = "激活码", index = 14)
    private String actCode;

    @ExcelProperty(value = "验证码", index = 15)
    private String varCode;

    @ExcelProperty(value = "会员有效期", index = 16)
    private String actExpireTime;

    @ExcelProperty(value = "客户姓名", index = 17)
    private String mbName;

    @ExcelProperty(value = "联系方式", index = 18)
    private String mobile;

    @ExcelProperty(value = "用餐月度", index = 19)
    private String giftDateMonth;

    @ExcelProperty(value = "用餐日期", index = 20)
    private String giftDate;

    @ExcelProperty(value = "餐型", index = 21)
    private String shopItemName;

    @ExcelProperty(value = "用餐人数", index = 22)
    private Integer giftPeopleNum;

    @ExcelProperty(value = "用餐人姓名", index = 23)
    private String giftName;

    @ExcelProperty(value = "酒店名", index = 24)
    private String hotelName;

    @ExcelProperty(value = "餐厅名称", index = 25)
    private String shopName;

    @ExcelProperty(value = "酒店名/餐厅名称", index = 26)
    private String hotelAndShopName;

    @ExcelProperty(value = "预订渠道", index = 27)
    private String shopChannel;

    @ExcelProperty(value = "所属地区", index = 28)
    private String cityName;

    @ExcelProperty(value = "使用状态", index = 29)
    private String varStatus;

    @ExcelProperty(value = "协议价格", index = 30)
    private BigDecimal protocolPrice;

    @ExcelProperty(value = "净价", index = 31)
    private BigDecimal netPrice;

    @ExcelProperty(value = "服务费", index = 32)
    private String serviceRate;

    @ExcelProperty(value = "增值税", index = 33)
    private String taxRate;

    @ExcelProperty(value = "结算规则", index = 34)
    private String settleRule;

    @ExcelProperty(value = "结算方式", index = 35)
    private String settleMethod;

    @ExcelProperty(value = "贴现金额", index = 36)
    private BigDecimal shopAmount;

    @ExcelProperty(value = "银行贴现金额", index = 37)
    private BigDecimal bankAmount;

    @ExcelProperty(value = "权益已使用次数", index = 38)
    private Integer useCount;

    @ExcelProperty(value = "权益剩余次数", index = 39)
    private String giftTimes;

}
