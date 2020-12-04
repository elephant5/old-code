package com.colourfulchina.mars.api.vo.req;

import com.colourfulchina.mars.api.entity.GiftCode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 激活码退货请求参数
 */
@Slf4j
@Data
public class ReturnCodeReq extends GiftCode {
    private static final long serialVersionUID = 5110953348860285264L;

    @ApiModelProperty("激活码列表")
    private List<String> codes;
}
