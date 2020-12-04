package com.colourfulchina.mars.api.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.List;

/**
 * 发码操作请求参数
 */
@Slf4j
@Data
public class SendCodeReq extends ActCodeReq {
    private static final long serialVersionUID = -8175311678861770523L;

    @ApiModelProperty("标签列表")
    private List<String> tagList;
    @ApiModelProperty("激活码有效期")
    private Date actExpireTime;
    @ApiModelProperty("激活码出库备注")
    private String outRemarks;
}
