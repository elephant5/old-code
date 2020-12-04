package com.colourfulchina.pangu.taishang.api.vo.req;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 操作酒店探索章节入参
 */
@Data
@Api("操作酒店探索章节入参")
public class OptHotelPortalReq implements Serializable {
    private static final long serialVersionUID = 7323551827085503096L;

    @ApiModelProperty("酒店章节id")
    private Integer id;
    @ApiModelProperty("酒店id")
    private Integer hotelId;
    @ApiModelProperty("章节标题")
    private String title;
    @ApiModelProperty("酒店章节内容")
    private String content;
//    @ApiModelProperty("酒店章节图片列表")
//    private List<String> images;
}
