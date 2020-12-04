package com.colourfulchina.pangu.taishang.api.vo;

import com.colourfulchina.pangu.taishang.api.enums.ShopTypeEnums;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

/**
 * 商户基础信息vo
 */
@Data
public class ShopBaseMsgVo implements Serializable {
    private static final long serialVersionUID = 5314839689762533685L;

    @ApiModelProperty("商户id")
    private Integer id;
    @ApiModelProperty("商户类型")
    private String shopType;
    @ApiModelProperty("商户所属酒店名称")
    private String hotelName;
    @ApiModelProperty("商户所属酒店id")
    private String hotelId;
    @ApiModelProperty("商户中文名")
    private String name;
    @ApiModelProperty("商户英文名")
    private String nameEn;
    @ApiModelProperty("商户级别")
    private String level;
    @ApiModelProperty("商户电话")
    private String phone;
    @ApiModelProperty("商户所属国家id")
    private String countryId;
    @ApiModelProperty("商户所属城市id")
    private String cityId;
    @ApiModelProperty("商户地址")
    private String address;
    @ApiModelProperty("商户地址英文")
    private String addressEn;
    @ApiModelProperty("开始营业时间")
    private String openTime;
    @ApiModelProperty("结束营业时间")
    private String closeTime;
    @ApiModelProperty("入住时间")
    private String checkInTime;
    @ApiModelProperty("退房时间")
    private String checkOutTime;
    @ApiModelProperty("最小提前预约时间")
    private Integer minBookDays;
    @ApiModelProperty("最大提前预约时间")
    private Integer maxBookDays;
    @ApiModelProperty("商户介绍")
    private String summary;
    @ApiModelProperty("录单提示")
    private String tips;
    @ApiModelProperty("备注")
    private String notes;
    @ApiModelProperty("商户类型名称")
    private String shopServiceName;
    @ApiModelProperty("商户性质(0附属商户，1独立商户)")
    private Integer shopNature;
    @ApiModelProperty("商户详情")
    private String detail;
    @ApiModelProperty("商户城市")
    private String city;

    public String getShopServiceName() {
        String res = null ;
        if(StringUtils.isNotBlank(this.shopType)){
            res = ShopTypeEnums.findByCode(this.shopType).getName();
        }

        return res;
    }

}
