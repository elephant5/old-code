package com.colourfulchina.pangu.taishang.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 国家城市二级联动
 */
@Data
public class CountryCityVo implements Serializable {
    private static final long serialVersionUID = -4371365991570294039L;

    @ApiModelProperty("国家id")
    private String countryId;
    @ApiModelProperty("国建名称")
    private String countryName;
    @ApiModelProperty("国家对应城市信息")
    private List<CityVo> cityDetail;
}
