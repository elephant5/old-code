package com.colourfulchina.pangu.taishang.api.vo.req;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 删除酒店探索章节入参
 */
@Data
@Api("删除酒店探索章节入参")
public class DelHotelPortalReq implements Serializable {
    private static final long serialVersionUID = -7524535009658293973L;

    @ApiModelProperty("酒店章节id")
    private Integer id;
    @ApiModelProperty("酒店id")
    private Integer hotelId;
}
