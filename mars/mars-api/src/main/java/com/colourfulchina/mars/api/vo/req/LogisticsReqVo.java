package com.colourfulchina.mars.api.vo.req;

import com.colourfulchina.mars.api.entity.LogisticsInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 激活码操作请求参数
 */
@Slf4j
@Data
public class LogisticsReqVo extends LogisticsInfo {
    private static final long serialVersionUID = 6759040166697394638L;

    @ApiModelProperty("是否发送短信标识(1发送，0不发送)")
    private Integer messageFlag;
}
