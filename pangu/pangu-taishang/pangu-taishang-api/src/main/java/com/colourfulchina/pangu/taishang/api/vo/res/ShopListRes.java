package com.colourfulchina.pangu.taishang.api.vo.res;

import com.colourfulchina.pangu.taishang.api.entity.Shop;
import com.colourfulchina.pangu.taishang.api.enums.GiftTypeEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

@Api("商户列表查询出参")
@Data
public class ShopListRes extends Shop {
    private static final long serialVersionUID = -1054955270100634099L;
    @ApiModelProperty("酒店名称|商户名称")
    private String hotelNameAndShopName;
    @ApiModelProperty("酒店名称")
    private String hotelName;
    @ApiModelProperty("商户名称")
    private String shopName;
    @ApiModelProperty("商户类型")
    private String shopType;
    @ApiModelProperty("商户权益")
    private String gift;
    @ApiModelProperty("商户权益")
    private String giftName;
    public String getHotelNameAndShopName() {
        String res;
        if ("accom".equals(this.shopType)){
            res = this.hotelName;
        }else {
            if (StringUtils.isNotBlank(this.hotelName)){
                res = this.hotelName +  "|" + this.shopName;
            }else {
                res = this.shopName;
            }
        }
        return res;
    }


}
