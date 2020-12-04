package com.colourfulchina.mars.api.vo.res;

import com.colourfulchina.mars.api.entity.GiftCode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class GoodsResVo {

    @ApiModelProperty(value = "项目id")
    private Integer goodsId;

    @ApiModelProperty(value = "行权地址后缀")
    private String prjCode;

    @ApiModelProperty(value = "行权地址短链")
    private String shortUrl;

    @ApiModelProperty(value = "权益码id")
    private List<GiftCode> giftCodeList;

    @ApiModelProperty(value = "产品组信息")
    private List<ProductGroupResVo> productGroupResVoList;
}
