package com.colourfulchina.pangu.taishang.api.vo.res;

import com.colourfulchina.pangu.taishang.api.enums.ShopChannelEnums;
import com.colourfulchina.pangu.taishang.api.enums.ShopTypeEnums;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.Date;

@Api("商户列表模糊分页查询出参")
@Data
public class ShopPageListRes implements Serializable {
    private static final long serialVersionUID = 5098065021775288222L;

    @ApiModelProperty("商户id")
    private Integer id;
    @ApiModelProperty("酒店名称|商户名称")
    private String hotelNameAndShopName;
    @ApiModelProperty("酒店名称")
    private String hotelName;
    @ApiModelProperty("商户名称")
    private String shopName;
    @ApiModelProperty("商户类型")
    private String shopType;
    @ApiModelProperty("城市名称")
    private String cityName;
    @ApiModelProperty("资源类型（服务类型）")
    private String serviceName;
    @ApiModelProperty("商户状态")
    private String shopStatus;
    @ApiModelProperty("下架时间")
    private Date downSaleDate;
    @ApiModelProperty("商户资源渠道")
    private String shopChannelName;
    @ApiModelProperty("商户资源渠道id")
    private String shopChannelCode;
    @ApiModelProperty("商户标签(0有图，1无图)")
    private Integer shopTagId;
    @ApiModelProperty("商户类型")
    private String shopServiceName;
    @ApiModelProperty("商户资源")
    private String channelName;
    public String getShopChannelName() {
        String str = ShopChannelEnums.getNameByCode(Integer.valueOf(this.shopChannelCode));
        return str;
    }

    public String getShopServiceName() {
        String res = null ;
        if(StringUtils.isNotBlank(shopType)){
            res = ShopTypeEnums.findByCode(this.shopType).getName();
        }
        return res;
    }

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
