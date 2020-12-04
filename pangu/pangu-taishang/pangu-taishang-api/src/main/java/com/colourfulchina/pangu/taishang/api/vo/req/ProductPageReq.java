package com.colourfulchina.pangu.taishang.api.vo.req;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 产品列表分页查询入参
 */
@Data
@Api("产品列表分页查询入参")
public class ProductPageReq implements Serializable {
    private static final long serialVersionUID = -6227507693748878000L;

    @ApiModelProperty("酒店名或者商户名")
    private String hotelOrShopName;
    @ApiModelProperty("资源类型")
    private List<String> service;
    @ApiModelProperty("权益类型")
    private List<String> gift;
    @ApiModelProperty("城市")
    private List<String> city;
    @ApiModelProperty("渠道类型（0渠道资源，1公司资源）")
    private Integer channelType;
    @ApiModelProperty("成本开始区间")
    private BigDecimal startCost;
    @ApiModelProperty("成本结束区间")
    private BigDecimal endCost;
    @ApiModelProperty("排除的产品id集合")
    private String ids;
}
