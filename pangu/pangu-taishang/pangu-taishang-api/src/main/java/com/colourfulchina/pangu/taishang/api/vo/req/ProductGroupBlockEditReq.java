package com.colourfulchina.pangu.taishang.api.vo.req;

import com.colourfulchina.pangu.taishang.api.dto.BlockRule;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ProductGroupBlockEditReq implements Serializable {
    private static final long serialVersionUID = -1029332259620095540L;

    @ApiModelProperty("产品组id")
    private Integer productGroupId;
    @ApiModelProperty("block对象")
    private List<BlockRule> blockRuleList;
}
