package com.colourfulchina.pangu.taishang.api.vo.res;

import com.colourfulchina.pangu.taishang.api.entity.ShopItem;
import com.colourfulchina.pangu.taishang.api.vo.ShopBaseMsgVo;
import com.colourfulchina.pangu.taishang.api.vo.ShopProtocolMsgVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Api("商户详情出参")
@Data
public class ShopDetailRes implements Serializable {
    private static final long serialVersionUID = 1747989958044788806L;

    @ApiModelProperty("商户信息")
    private ShopBaseMsgVo shop;
    @ApiModelProperty("商户协议")
    private ShopProtocolMsgVo shopProtocol;
    @ApiModelProperty("商户资源列表")
    private List<ShopItem> shopItemList;
}
