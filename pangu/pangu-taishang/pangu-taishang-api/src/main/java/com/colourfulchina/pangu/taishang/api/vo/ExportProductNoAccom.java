package com.colourfulchina.pangu.taishang.api.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ExportProductNoAccom extends BaseRowModel {

    @ExcelProperty(value = "城市", index = 0)
    private String city;

    @ExcelProperty(value = "酒店中文名", index = 1)
    private String hotelChName;

    @ExcelProperty(value = "酒店英文名", index = 2)
    private String hotelEnName;

    @ExcelProperty(value = "餐厅英文名", index = 3)
    private String shopEnName;

    @ExcelProperty(value = "餐厅中文名", index = 4)
    private String shopChName;

    @ExcelProperty(value = "权益项目", index = 5)
    private String serviceName;

    @ExcelProperty(value = "权益类型", index = 6)
    private String giftShortName;

    @ExcelProperty(value = "权益内容", index = 7)
    private String giftName;

    @ExcelProperty(value = "市场参考价", index = 8)
    private String marketPrice;

    @ExcelProperty(value = "适用日期", index = 9)
    private String applyDate;

    @ExcelProperty(value = "适用星期", index = 10)
    private String applyWeek;

    @ExcelProperty(value = "餐段", index = 11)
    private String shopItemName;

    @ExcelProperty(value = "净价", index = 12)
    private BigDecimal netPrice;

    @ExcelProperty(value = "服务费", index = 13)
    private String serviceRate;

    @ExcelProperty(value = "税费", index = 14)
    private String taxRate;

    @ExcelProperty(value = "单人总价", index = 15)
    private BigDecimal singlePrice;

    @ExcelProperty(value = "双人总价", index = 16)
    private BigDecimal doublePrice;

    @ExcelProperty(value = "成本", index = 17)
    private BigDecimal cost;

    @ExcelProperty(value = "折扣率", index = 18)
    private String itemRate;

    @ExcelProperty(value = "点评/微商城售价（元/人）", index = 19)
    private String otherPrice;

    @ExcelProperty(value = "开餐时间", index = 20)
    private String openTime;

    @ExcelProperty(value = "不可用日期", index = 21)
    private String block;

    @ExcelProperty(value = "地址", index = 22)
    private String address;

    @ExcelProperty(value = "电话", index = 23)
    private String phone;

    @ExcelProperty(value = "泊车信息", index = 24)
    private String parking;

    @ExcelProperty(value = "儿童政策", index = 25)
    private String kid;

    @ExcelProperty(value = "备注", index = 26)
    private String remark;

}
