package com.colourfulchina.mars.api.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.List;

/**
 *  查询上海银行酒店列表入参
 */
@Slf4j
@Data
public class BoscHotelListReq implements Serializable {
    private static final long serialVersionUID = 3415465657022750837L;

    @ApiModelProperty("资源类型")
    private String service;
    @ApiModelProperty("产品组ids")
    private List<Integer> productGroupIds;
}
