package com.colourfulchina.pangu.taishang.api.vo.res;

import com.colourfulchina.pangu.taishang.api.entity.GoodsClause;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ShopOrderDetailRes implements Serializable {

    @ApiModelProperty("产品组产品id")
    private Integer productGroupProductId;

    @ApiModelProperty(value = "使用细则")
    private String useInstruction;

    @ApiModelProperty(value = "商户介绍")
    private String content;

    @ApiModelProperty(value = "产品标题")
    private String goodsTitle;

    @ApiModelProperty(value = "酒店名称")
    private String hotelName;

    @ApiModelProperty(value = "酒店地址")
    private String address;

    @ApiModelProperty("商品条款")
    private List<GoodsClause> goodsClauseList;

    @ApiModelProperty(value = "适用条款")
    private String clause;

    @ApiModelProperty(value = "项目组名称")
    private String prjGroupTitle;

    @ApiModelProperty(value = "商户电话")
    private String shopPhone;

    @ApiModelProperty(value = "商户类型")
    private String type;

    @ApiModelProperty(value = "权益名称")
    private String gift;

    @ApiModelProperty(value = "酒店id")
    private Integer hotelId;

    @ApiModelProperty(value = "商户id")
    private Integer shopId;

    @ApiModelProperty(value = "参考价")
    private List<String> priceList;

    @ApiModelProperty(value = "商户名称")
    private String shopName;

    @ApiModelProperty(value = "定制套餐开餐时间")
    private List<String> mealTimeList;

    @ApiModelProperty(value = "营业时间")
    private String opentime;

    @ApiModelProperty(value = "入住日期")
    private String checkInTime;

    @ApiModelProperty(value = "离店日期")
    private String checkOutTime;

    @ApiModelProperty(value = "商户规格id")
    private Integer shopItemId;

    @ApiModelProperty(value = "资源类型")
    private String serviceType;

    @ApiModelProperty(value = "床型")
    private String needs;

    @ApiModelProperty(value = "餐型")
    private String addon;

    @ApiModelProperty(value = "商户经度")
    private String lng;

    @ApiModelProperty(value = "商户纬度")
    private String lat;

    @ApiModelProperty(value = "坐标")
    private String point;

}
