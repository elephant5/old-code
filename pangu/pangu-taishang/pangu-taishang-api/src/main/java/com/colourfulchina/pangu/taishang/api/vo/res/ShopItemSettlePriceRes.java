package com.colourfulchina.pangu.taishang.api.vo.res;

import com.colourfulchina.pangu.taishang.api.dto.GiftPriceDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class ShopItemSettlePriceRes implements Serializable {
    private static final long serialVersionUID = -7179428499207380760L;

    @ApiModelProperty("日历时间")
    private Date calendarDate;
    @ApiModelProperty("是否被block，true被block，false没有被block")
    private Boolean isBlock;
    @ApiModelProperty("结算价列表")
    private List<GiftPriceDto> prices;
}
