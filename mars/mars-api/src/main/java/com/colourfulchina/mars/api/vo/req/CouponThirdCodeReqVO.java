package com.colourfulchina.mars.api.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CouponThirdCodeReqVO {

    @ApiModelProperty(value = "第三方券来源")
    private String source;

    @ApiModelProperty(value = "批次号")
    private Long batchId;

    @ApiModelProperty(value = "数量")
    private Integer num;

    @ApiModelProperty(value = "第三方商品编号")
    private String productNo;

    @ApiModelProperty(value = "账户acid")
    private Long acId;

    @ApiModelProperty(value = "用户名称")
    private String acName;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "预约单id")
    private Integer reserveOrderId;
}
