package com.colourfulchina.pangu.taishang.api.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel
public class ShopListQueryReq implements Serializable {
    private static final long serialVersionUID = 4474588115662080661L;

    @ApiModelProperty(value = "产品组id")
    private Integer productGroupId;

    @ApiModelProperty(value = "资源类型")
    private String service;

    @ApiModelProperty(value = "城市")
    private String city;

    @ApiModelProperty(value = "渠道")
    private String acChannel;
}
