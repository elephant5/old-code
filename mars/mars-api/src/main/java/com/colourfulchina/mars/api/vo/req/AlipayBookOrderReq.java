package com.colourfulchina.mars.api.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.List;

/**
 * 支付宝我的预约单列表查询入参
 */
@Slf4j
@Data
public class AlipayBookOrderReq implements Serializable {
    private static final long serialVersionUID = -7529982806393143782L;

    @ApiModelProperty("用户acid")
    private Long memberId;
    @ApiModelProperty("排除的id列表")
    private List<Integer> idList;
}
