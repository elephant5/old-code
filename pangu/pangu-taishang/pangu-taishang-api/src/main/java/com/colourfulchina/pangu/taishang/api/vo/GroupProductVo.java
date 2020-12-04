package com.colourfulchina.pangu.taishang.api.vo;

import com.colourfulchina.pangu.taishang.api.dto.BlockRule;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 产品列表展示字段
 */
@Data
@Api("产品列表展示字段")
public class GroupProductVo implements Serializable {
    private static final long serialVersionUID = 7149416745545456585L;

    @ApiModelProperty("产品组和产品关系id")
    private Integer id;
    @ApiModelProperty("商户ID")
    private Integer shopId;
    @ApiModelProperty("商户规格id")
    private Integer shopItemId;
    @ApiModelProperty("产品id")
    private Integer productId;
    @ApiModelProperty("城市")
    private String cityName;
    @ApiModelProperty("酒店名称")
    private String hotelName;
    @ApiModelProperty("商户名称")
    private String shopName;
    @ApiModelProperty("资源类型")
    private String service;
    @ApiModelProperty("权益类型")
    private String gift;
    @ApiModelProperty("产品名称")
    private String productName;
    @ApiModelProperty("床型")
    private String needs;
    @ApiModelProperty("餐型")
    private String addon;
    @ApiModelProperty("block规则")
    private String blockRule;
    @ApiModelProperty("block规则翻译")
    private String blockNatural;
    @ApiModelProperty("block规则对象列表")
    private List<BlockRule> blockRuleList;
    @ApiModelProperty("最低成本")
    private BigDecimal minCost;
    @ApiModelProperty("最高成本")
    private BigDecimal maxCost;
    @ApiModelProperty("点数或者次数")
    private BigDecimal pointOrTimes;
    @ApiModelProperty("状态")
    private String status;
    @ApiModelProperty("排序")
    private Integer sort;
    @ApiModelProperty("推荐")
    private Integer recommend;

    @ApiModelProperty(value = "产品净价")
    private BigDecimal productNetPrice;

    @ApiModelProperty(value = "产品预约金额")
    private BigDecimal productReservePrice;

    @ApiModelProperty(value = "产品图片")
    private String productImgUrl;

    private String serviceCode;
}
