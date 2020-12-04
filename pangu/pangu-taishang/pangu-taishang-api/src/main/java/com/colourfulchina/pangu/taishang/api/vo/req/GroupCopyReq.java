package com.colourfulchina.pangu.taishang.api.vo.req;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 产品组复制入参
 */
@Data
@Api("产品组复制入参")
public class GroupCopyReq implements Serializable {
    private static final long serialVersionUID = 4246920339835336760L;

    @ApiModelProperty("复制的目标商品id")
    private Integer goodsId;
    @ApiModelProperty("复制的源产品组")
    private List<Integer> groupIds;
}
