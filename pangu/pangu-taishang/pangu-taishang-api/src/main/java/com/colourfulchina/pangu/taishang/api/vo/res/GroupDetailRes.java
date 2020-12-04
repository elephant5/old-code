package com.colourfulchina.pangu.taishang.api.vo.res;

import com.colourfulchina.pangu.taishang.api.dto.BlockRule;
import com.colourfulchina.pangu.taishang.api.vo.GroupProductVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 产品组详情出参
 */
@Data
@Api("产品组详情出参")
public class GroupDetailRes implements Serializable {
    private static final long serialVersionUID = 8364572216296245819L;

    @ApiModelProperty("产品组id")
    private Integer id;
    @ApiModelProperty("产品组内部名称")
    private String shortName;
    @ApiModelProperty("使用限制id")
    private String useLimitId;
    @ApiModelProperty("使用限制")
    private String useLimit;
    @ApiModelProperty("使用数量")
    private BigDecimal useNum;
    @ApiModelProperty("折扣比例")
    private BigDecimal discountRate;
    @ApiModelProperty("block规则")
    private String blockRule;
    @ApiModelProperty("资源类型")
    private String service;
    @ApiModelProperty("权益类型")
    private String gift;
    @ApiModelProperty("成本最高价")
    private BigDecimal maxCost;
    @ApiModelProperty("成本最低价")
    private BigDecimal minCost;
    @ApiModelProperty("成本平均价")
    private BigDecimal avgCost;
    @ApiModelProperty("block规则对象列表")
    private List<BlockRule> blockRules;
    @ApiModelProperty("产品组下的产品列表")
    private List<GroupProductVo> productVoList;
    @ApiModelProperty("产品使用率")
    private BigDecimal useRate;
}
