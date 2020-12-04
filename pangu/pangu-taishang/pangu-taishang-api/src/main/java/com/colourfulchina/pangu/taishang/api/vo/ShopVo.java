package com.colourfulchina.pangu.taishang.api.vo;

import com.colourfulchina.pangu.taishang.api.entity.Shop;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 商户vo
 */
@Data
public class ShopVo extends Shop {
    private static final long serialVersionUID = 4281299648873091943L;

    @ApiModelProperty("国家id")
    private String countryId;
    @ApiModelProperty("国家名称")
    private String countryName;
    @ApiModelProperty("城市名称")
    private String cityName;
}
