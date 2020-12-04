package com.colourfulchina.mars.api.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * 激活码操作请求参数
 */
@Slf4j
@Data
public class ActCodeReq implements Serializable {
    private static final long serialVersionUID = -8839593856746893337L;

    @ApiModelProperty("商品id")
    private Integer goodsId;
    @ApiModelProperty("激活码数量,前段控制单次最多生成一万个")
    private Integer actCodeNum;
    @ApiModelProperty("生成备注")
    private String remarks;
}
