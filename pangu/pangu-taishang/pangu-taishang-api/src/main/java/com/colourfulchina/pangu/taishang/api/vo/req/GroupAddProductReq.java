package com.colourfulchina.pangu.taishang.api.vo.req;

import com.colourfulchina.pangu.taishang.api.vo.PackProductVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 产品组添加产品入参
 */
@Data
@Api("产品组添加产品入参")
public class GroupAddProductReq implements Serializable {
    private static final long serialVersionUID = 7503879390594135652L;

    @ApiModelProperty("添加的目标产品组id")
    private Integer productGroupId;
    @ApiModelProperty("打包选择的产品信息")
    private List<PackProductVo> packProducts;
//    @ApiModelProperty("添加的的源产品列表id")
//    private List<Integer> productIds;
}
