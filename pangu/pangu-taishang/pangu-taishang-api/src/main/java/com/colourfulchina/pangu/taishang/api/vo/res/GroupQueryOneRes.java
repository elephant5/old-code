package com.colourfulchina.pangu.taishang.api.vo.res;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 产品组查询出参
 */
@Data
@Api("产品组查询出参")
public class GroupQueryOneRes implements Serializable {
    private static final long serialVersionUID = -2111751503915339314L;

    @ApiModelProperty("产品组id")
    private Integer id;
    @ApiModelProperty("商品id")
    private Integer goodsId;
    @ApiModelProperty("产品组名称")
    private String name;
    @ApiModelProperty("产品组内部名称")
    private String shortName;
    @ApiModelProperty("资源类型")
    private List<String> service;
    @ApiModelProperty("权益类型")
    private List<String> gift;
    @ApiModelProperty("使用限制")
    private String useLimitId;
    @ApiModelProperty("使用数量")
    private BigDecimal useNum;
    @ApiModelProperty("折扣比例")
    private BigDecimal discountRate;
    @ApiModelProperty("最多提前N预定")
    private Integer maxBookDays;
    @ApiModelProperty("最少提前N预定")
    private Integer minBookDays;
    @ApiModelProperty("服务资源")
    private List<String> serviceList;
    @ApiModelProperty("权益类型")
    private List<String> giftList;
    @ApiModelProperty("产品使用率")
    private BigDecimal useRate;
    @ApiModelProperty("商户类型")
    private String shopType;
    @ApiModelProperty("周期重复时间")
    private Integer cycleTime;
    @ApiModelProperty("周期类型（0-天 1-周 2-月 3-年）")
    private Integer cycleType;
    @ApiModelProperty("周期重复数量")
    private Integer cycleNum;
    @ApiModelProperty("使用类型（0-次数  1-点数）")
    private Integer useType;
    @ApiModelProperty("免费次数")
    private Integer freeCount;
}
