package com.colourfulchina.pangu.taishang.api.vo.res;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

/**
 * 根据产品组和资源类型id查询商户列表出参
 */
@Data
@Api("根据产品组和资源类型id查询商户列表出参")
public class SelectShopByGroupServiceRes implements Serializable {
    private static final long serialVersionUID = -1846311834174688133L;

    @ApiModelProperty("商户id")
    private Integer id;
    @ApiModelProperty("商户类型")
    private String shopType;
    @ApiModelProperty("城市id")
    private Integer cityId;
    @ApiModelProperty("酒店id")
    private Integer hotelId;
    @ApiModelProperty("酒店名称")
    private String hotelName;
    @ApiModelProperty("商户名称")
    private String name;
    @ApiModelProperty("酒店名称|商户名称")
    private String hotelNameAndShopName;
    @ApiModelProperty("商户英文名")
    private String nameEn;
    @ApiModelProperty("商户拼音")
    private String py;
    @ApiModelProperty("商户地址")
    private String address;
    @ApiModelProperty("商户英文地址")
    private String addressEn;
    @ApiModelProperty("商户渠道id")
    private Integer shopChannelId;
    @ApiModelProperty("商户渠道")
    private String shopChannel;

    public String getHotelNameAndShopName() {
        String res;
        if ("accom".equals(this.shopType)){
            res = this.hotelName;
        }else {
            if (StringUtils.isNotBlank(this.hotelName)){
                res = this.hotelName +  "|" + this.name;
            }else {
                res = this.name;
            }
        }
        return res;
    }
}
