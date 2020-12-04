package com.colourfulchina.pangu.taishang.api.vo.res;

import com.colourfulchina.pangu.taishang.api.enums.ShopChannelEnums;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Api("酒店关联商户列表查询出参")
@Data
public class HotelShopListRes implements Serializable {
    private static final long serialVersionUID = 7173652544649268910L;
    @ApiModelProperty("商户id")
    private Integer id;
    @ApiModelProperty("商户中文名称")
    private String shopNameCh;
    @ApiModelProperty("商户资源的类型")
    private String serviceName;
    @ApiModelProperty("商户状态（0上架，1下架）")
    private Integer delFlag;
    @ApiModelProperty("商户渠道名称")
    private String shopChannelName;
    @ApiModelProperty("商户渠道类型名称")
    private String shopChannelType;
    @ApiModelProperty("商户渠道类型名称id")
    private String shopChannelTypeCode;

    public String getShopChannelType() {
        String str = ShopChannelEnums.getNameByCode(Integer.valueOf(this.shopChannelTypeCode));
        return str;
    }
}
