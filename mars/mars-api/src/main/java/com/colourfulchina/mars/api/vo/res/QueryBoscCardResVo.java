package com.colourfulchina.mars.api.vo.res;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class QueryBoscCardResVo {

    @ApiModelProperty(value = "卡片信息")
    private List<BoscCardInfoVo> boscCardInfoVoList;

    @ApiModelProperty(value = "是否有权益:true:有权益;false:无权益")
    private Boolean ifGift;
}
