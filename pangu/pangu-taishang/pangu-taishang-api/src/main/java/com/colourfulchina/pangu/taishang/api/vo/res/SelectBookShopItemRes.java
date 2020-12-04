package com.colourfulchina.pangu.taishang.api.vo.res;

import com.colourfulchina.pangu.taishang.api.entity.ProductGroupProduct;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 查询预约时选择资源列表出参
 */
@Data
@Api("查询预约时选择资源列表出参")
public class SelectBookShopItemRes extends ProductGroupProduct {
    private static final long serialVersionUID = -2338505757901856958L;

    @ApiModelProperty("商户id")
    private Integer shopId;
    @ApiModelProperty("商户规格id")
    private Integer shopItemId;
    @ApiModelProperty("资源类型")
    private String serviceType;
    @ApiModelProperty("权益类型")
    private String gift;
    @ApiModelProperty("适用时间")
    private String applyTime;
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("住宿床型")
    private String needs;
    @ApiModelProperty("住宿餐型")
    private String addon;
    @ApiModelProperty("开放开始时间")
    private String openTime;
    @ApiModelProperty("开放结束时间")
    private String closeTime;
}
