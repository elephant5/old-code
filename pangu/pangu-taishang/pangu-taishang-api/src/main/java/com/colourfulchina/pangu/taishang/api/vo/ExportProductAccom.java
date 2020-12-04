package com.colourfulchina.pangu.taishang.api.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ExportProductAccom extends BaseRowModel {

    @ExcelProperty(value = "国家", index = 0)
    private String countryName;

    @ExcelProperty(value = "城市", index = 1)
    private String cityName;

    @ExcelProperty(value = "酒店中文名称", index = 2)
    private String hotelChName;

    @ExcelProperty(value = "酒店英文名称", index = 3)
    private String hotelEnName;

    @ExcelProperty(value = "房型", index = 4)
    private String shopItemName;

    @ExcelProperty(value = "床型", index = 5)
    private String needs;

    @ExcelProperty(value = "餐型", index = 6)
    private String addon;

    @ExcelProperty(value = "适用日期", index = 7)
    private String applyDate;

    @ExcelProperty(value = "适用星期", index = 8)
    private String applyWeek;

    @ExcelProperty(value = "结算价（RMB）", index = 9)
    private BigDecimal cost;

    @ExcelProperty(value = "OTA参考价", index = 10)
    private String otherPrice;

    @ExcelProperty(value = "地址", index = 11)
    private String address;

    @ExcelProperty(value = "电话", index = 12)
    private String phone;

    @ExcelProperty(value = "不可用日期", index = 13)
    private String block;

    @ExcelProperty(value = "备注", index = 14)
    private String remark;
}
