package com.colourfulchina.mars.api.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MemberCertificateReq {

    @ApiModelProperty(value = "用户id")
    private Long mbid;
}
