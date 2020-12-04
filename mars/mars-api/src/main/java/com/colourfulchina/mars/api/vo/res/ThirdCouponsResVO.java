package com.colourfulchina.mars.api.vo.res;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class ThirdCouponsResVO {


    @ApiModelProperty(value = "提货请求号" )
    private String reqNo;

    @ApiModelProperty(value = "商品编号" )
    private String productNo;

    @ApiModelProperty(value = "商品类型；卡密；短链" )
    private String productType;

    @ApiModelProperty(value = "商品券号；如果商品类型是短接，该字段存放短链接；商品类型是卡密，该字段存放卡券号" )
    private String productAuthCode;

    @ApiModelProperty(value = "卡券密码；卡密类型的商品对应的密码；商品类型为卡密类型时，此字段有值" )
    private String authCodePwd;

    @ApiModelProperty(value = " 面额；单位：分" )
    private Integer denomination;

    @ApiModelProperty(value = "是否激活；true-已激活，false-未激活" )
    private Boolean activate;

    @ApiModelProperty(value = " 有效期开始时间；格式：YYYYMMDDhhmmss\n" )
    private String validStartTime;

    @ApiModelProperty(value = " 有效期结束时间；格式：YYYYMMDDhhmmss\n" )
    private String validEndTime;

}
