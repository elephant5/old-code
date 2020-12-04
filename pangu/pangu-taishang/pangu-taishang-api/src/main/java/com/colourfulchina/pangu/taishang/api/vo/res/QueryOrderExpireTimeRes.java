package com.colourfulchina.pangu.taishang.api.vo.res;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 查询购买的权益到期时间出参
 */
@Data
@Api("查询购买的权益到期时间出参")
public class QueryOrderExpireTimeRes implements Serializable {
    private static final long serialVersionUID = -2492829929582900170L;

    @ApiModelProperty("有效期规则")
    private String validRule;
    @ApiModelProperty("到期日期")
    private Date expireDate;
}
