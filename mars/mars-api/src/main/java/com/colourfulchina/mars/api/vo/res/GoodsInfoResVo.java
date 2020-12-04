package com.colourfulchina.mars.api.vo.res;

import com.colourfulchina.pangu.taishang.api.vo.res.GoodsGroupListRes;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class GoodsInfoResVo implements Serializable{
    private static final long serialVersionUID = -3256411699448514414L;

    @ApiModelProperty(value = "商品id")
    private Integer id ;

    @ApiModelProperty(value = "商品名称")
    private String  name;

    @ApiModelProperty(value = "商品内部简称")
    private String shortName;

    @ApiModelProperty(value = "权益码id")
    private Integer unitId;

    private List<GoodsGroupListRes> groupListResList;
}
