package com.colourfulchina.pangu.taishang.api.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ShopOrderDetailReq {

    @ApiModelProperty(value = "产品ID")
    private Integer goodsId;

    @ApiModelProperty(value = "产品组ID")
    private Integer groupId;

    @ApiModelProperty(value = "项目ID")
    private Integer projectId;

    @ApiModelProperty(value = "订单ID")
    private String  salesOrderId;
}
