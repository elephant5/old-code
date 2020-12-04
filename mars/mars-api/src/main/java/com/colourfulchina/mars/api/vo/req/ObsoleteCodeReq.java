package com.colourfulchina.mars.api.vo.req;

import com.colourfulchina.mars.api.entity.GiftCode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 激活码作废请求参数
 */
@Slf4j
@Data
public class ObsoleteCodeReq extends GiftCode {
    private static final long serialVersionUID = 2730973048773760467L;

    @ApiModelProperty("激活码列表")
    private List<String> codes;
}
