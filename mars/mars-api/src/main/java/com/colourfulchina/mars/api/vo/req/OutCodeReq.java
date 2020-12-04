package com.colourfulchina.mars.api.vo.req;

import com.colourfulchina.mars.api.entity.GiftCode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 激活码出库请求参数
 */
@Slf4j
@Data
public class OutCodeReq extends GiftCode {
    private static final long serialVersionUID = 2097639800018959392L;

    @ApiModelProperty("激活码列表")
    private List<String> codes;
    @ApiModelProperty("标签列表")
    private List<String> tagList;
}
