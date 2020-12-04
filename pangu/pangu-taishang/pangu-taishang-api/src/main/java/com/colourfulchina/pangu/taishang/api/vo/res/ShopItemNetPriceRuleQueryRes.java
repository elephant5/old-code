package com.colourfulchina.pangu.taishang.api.vo.res;

import com.colourfulchina.pangu.taishang.api.dto.SysFileDto;
import com.colourfulchina.pangu.taishang.api.entity.ShopItemNetPriceRule;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 规格价格查询出参
 */
@Data
@Api("规格价格查询出参")
public class ShopItemNetPriceRuleQueryRes extends ShopItemNetPriceRule {
    private static final long serialVersionUID = 7341959650090468751L;

    @ApiModelProperty("时间白话文")
    private String timeString;
    @ApiModelProperty("价格白话文")
    private String priceString;
    @ApiModelProperty("文件列表")
    List<SysFileDto> files;


}
