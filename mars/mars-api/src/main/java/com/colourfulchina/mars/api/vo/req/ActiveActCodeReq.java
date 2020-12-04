package com.colourfulchina.mars.api.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * 激活激活码操作请求参数
 */
@Slf4j
@Data
public class ActiveActCodeReq implements Serializable {
    private static final long serialVersionUID = 1306835106538977607L;

    @ApiModelProperty("激活码")
    private String actCode;
    @ApiModelProperty("激活人会员id")
    private Long memberId;
    @ApiModelProperty("激活备注")
    private String activeRemarks;
}
