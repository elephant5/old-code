package com.colourfulchina.pangu.taishang.api.vo.req;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 规格价格添加/修改入参
 */
@Data
@Api("规格价格添加/修改入参")
public class ShopItemNetPriceRuleOptReq implements Serializable {
    private static final long serialVersionUID = 2150916132573581247L;

    @ApiModelProperty("规格价格id")
    private Integer id;
    @ApiModelProperty("商户id")
    private Integer shopId;
    @ApiModelProperty("商户规格id")
    private Integer shopItemId;
    @ApiModelProperty("起止开始时间")
    private Date startDate;
    @ApiModelProperty("起止结束时间")
    private Date endDate;
    @ApiModelProperty("星期列表")
    private List<Integer> weeks;
    @ApiModelProperty("净价")
    private BigDecimal netPrice;
    @ApiModelProperty("服务费")
    private BigDecimal serviceRate;
    @ApiModelProperty("税费")
    private BigDecimal taxRate;
}
