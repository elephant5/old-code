package com.colourfulchina.mars.api.vo.res;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class ThirdCouponsProductInfoResVO {

    @ApiModelProperty(value = "商品版面的url地址" )
    private String imgUrl;

    @ApiModelProperty(value = "商品简介")
    private String productDesc;

    @ApiModelProperty(value = "商品出售价格；单位：分" )
    private Integer salePrice;

    @ApiModelProperty(value = "使用须知" )
    private String useCondition;

    @ApiModelProperty(value = "剩余可售量")
    private Integer remainAllowSaleAmount;

    @ApiModelProperty(value = "商品名称" )
    private String productName;

    @ApiModelProperty(value = "商品编号" )
    private String productNo;

    @ApiModelProperty(value = "商品类型；卡密；短链" )
    private String productType;

    @ApiModelProperty(value = " 面额；单位：分" )
    private Integer denomination;



}
