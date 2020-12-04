package com.colourfulchina.pangu.taishang.api.vo.res;

import com.colourfulchina.pangu.taishang.api.entity.Shop;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Api("商户新增第一步出参")
@Data
public class AddShopOneRes implements Serializable {
    private static final long serialVersionUID = -2047159619893972778L;

    @ApiModelProperty("商户基本信息")
    private Shop shop;
}
