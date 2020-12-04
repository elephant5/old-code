package com.colourfulchina.mars.api.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ExportReserveAccom extends BaseRowModel {

    @ExcelProperty(value = "产品类型", index = 0)
    private String serviceType;

    @ExcelProperty(value = "系统编号", index = 1)
    private Integer reserveId;

    @ExcelProperty(value = "接单侠", index = 2)
    private String orderCreator;

    @ExcelProperty(value = "处理人", index = 3)
    private String operator;

    @ExcelProperty(value = "预定状态", index = 4)
    private String reservOrderStatus;

    @ExcelProperty(value = "状态日期", index = 5)
    private String statusDate;

    @ExcelProperty(value = "预定成功日期", index = 6)
    private String successDate;

    @ExcelProperty(value = "预定取消日期", index = 7)
    private String cancelDate;

    @ExcelProperty(value = "取消原因", index = 8)
    private String cancelReason;

    @ExcelProperty(value = "预定失败日期", index = 9)
    private String failDate;

    @ExcelProperty(value = "失败原因", index = 10)
    private String failReason;

    @ExcelProperty(value = "是否调剂", index = 11)
    private String dispensing;

    @ExcelProperty(value = "预定年度", index = 12)
    private String createYear;

    @ExcelProperty(value = "预定月度", index = 13)
    private String createMonth;

    @ExcelProperty(value = "大客户", index = 14)
    private String bankName;

    @ExcelProperty(value = "销售渠道", index = 15)
    private String salesChannelName;

    @ExcelProperty(value = "销售方式", index = 16)
    private String salesWayName;

    @ExcelProperty(value = "销售年份", index = 17)
    private String salesYear;

    @ExcelProperty(value = "销售年月", index = 18)
    private String salesMonth;

    @ExcelProperty(value = "项目编号", index = 19)
    private Integer goodsId;

    @ExcelProperty(value = "项目简称", index = 20)
    private String goodsShortName;

    @ExcelProperty(value = "项目名称", index = 21)
    private String goodsName;

    @ExcelProperty(value = "系统名称", index = 22)
    private String sysName;

    @ExcelProperty(value = "系统订单日期", index = 23)
    private String createTime;

    @ExcelProperty(value = "产品编号/券码", index = 24)
    private String actCode;

    @ExcelProperty(value = "原有效期", index = 25)
    private String oldExpireTime;

    @ExcelProperty(value = "是否延期", index = 26)
    private String proLong;

    @ExcelProperty(value = "新有效期", index = 27)
    private String newExpireTime;

    @ExcelProperty(value = "银行单号", index = 28)
    private String bankOrderNo;

    @ExcelProperty(value = "客户姓名", index = 29)
    private String mbName;

    @ExcelProperty(value = "手机", index = 30)
    private String mobile;

    @ExcelProperty(value = "证件类型", index = 31)
    private String cardType;

    @ExcelProperty(value = "证件号", index = 32)
    private String idNumber;

    @ExcelProperty(value = "第三方客户号", index = 33)
    private String thirdId;

    @ExcelProperty(value = "入住人姓名", index = 34)
    private String giftName;

    @ExcelProperty(value = "酒店", index = 35)
    private String hotelName;

    @ExcelProperty(value = "城市", index = 36)
    private String cityName;

    @ExcelProperty(value = "订房渠道", index = 37)
    private String shopChannel;

    @ExcelProperty(value = "结算方式", index = 38)
    private String settleMethod;

    @ExcelProperty(value = "渠道单号", index = 39)
    private String channelNumber;

    @ExcelProperty(value = "入住年份", index = 40)
    private String giftDateYear;

    @ExcelProperty(value = "入住年月", index = 41)
    private String giftDateMonth;

    @ExcelProperty(value = "入住日期", index = 42)
    private String checkDate;

    @ExcelProperty(value = "离店日期", index = 43)
    private String deparDate;

    @ExcelProperty(value = "间数", index = 44)
    private Integer checkNight;

    @ExcelProperty(value = "天数", index = 45)
    private Integer nightNumbers;

    @ExcelProperty(value = "间夜数", index = 46)
    private Integer jyTimes;

    @ExcelProperty(value = "酒店单价(RMB)", index = 47)
    private BigDecimal singleAmount;

    @ExcelProperty(value = "酒店总价(RMB)", index = 48)
    private BigDecimal shopAmount;

    @ExcelProperty(value = "外币种类", index = 49)
    private String cornType;

    @ExcelProperty(value = "外币金额", index = 50)
    private String cornAmount;

    @ExcelProperty(value = "参考汇率", index = 51)
    private String otherRate;

    @ExcelProperty(value = "酒店预订类型", index = 52)
    private String hotelReserveType;

    @ExcelProperty(value = "客户支付", index = 53)
    private BigDecimal paymentAmount;

    @ExcelProperty(value = "银行补贴", index = 54)
    private String bankMoney;

    @ExcelProperty(value = "权益结算总价", index = 55)
    private BigDecimal salesPrice;

    @ExcelProperty(value = "房型", index = 56)
    private String shopItemName;

    @ExcelProperty(value = "床型", index = 57)
    private String needs;

    @ExcelProperty(value = "早餐", index = 58)
    private String addon;

    @ExcelProperty(value = "酒店确认号", index = 59)
    private String reservNumber;

    @ExcelProperty(value = "备注", index = 60)
    private String reservRemark;

    @ExcelProperty(value = "权益已使用次数", index = 61)
    private Integer useCount;

    @ExcelProperty(value = "权益剩余次数", index = 62)
    private String giftTimes;

    @ExcelProperty(value = "点数", index = 63)
    private String point;

    @ExcelProperty(value = "产品ID", index = 64)
    private String thirdCpnNum;

    @ExcelProperty(value = "套餐名称", index = 65)
    private String thirdCpnName;
}
