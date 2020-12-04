package com.colourfulchina.pangu.taishang.api.vo.req;

import com.colourfulchina.pangu.taishang.api.dto.BlockRule;
import com.colourfulchina.pangu.taishang.api.vo.BookBasePaymentVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 产品组编辑产品入参
 */
@Data
@Api("产品组编辑产品入参")
public class GroupEditProductReq implements Serializable {
    private static final long serialVersionUID = 1395385029762176973L;

    @ApiModelProperty("产品组和产品关系表id")
    private Integer productGroupProductId;
    @ApiModelProperty("点数")
    private BigDecimal point;
    @ApiModelProperty("点数或者次数")
    private BigDecimal pointOrTimes;
    @ApiModelProperty("排序")
    private Integer sort;
    @ApiModelProperty("block列表")
    private List<BlockRule> blockRuleList;
    @ApiModelProperty("预约支付金额列表")
    private List<BookBasePaymentVo> bookPaymentVoList;
}
