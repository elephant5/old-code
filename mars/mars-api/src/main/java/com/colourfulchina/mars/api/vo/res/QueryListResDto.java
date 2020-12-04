package com.colourfulchina.mars.api.vo.res;

import com.colourfulchina.pangu.taishang.api.entity.Hotel;
import com.colourfulchina.pangu.taishang.api.entity.ProductGroup;
import lombok.Data;

@Data
public class QueryListResDto {

//    private BigDecimal discountRate;
//
//    private BigDecimal shopPriceRule;

    private Hotel hotel;

    private ShopResDto shopResDto;

    private ProductGroup productGroup;

    private String shopPic;
    
    private Boolean orderFlag;

    private String cityName;

}