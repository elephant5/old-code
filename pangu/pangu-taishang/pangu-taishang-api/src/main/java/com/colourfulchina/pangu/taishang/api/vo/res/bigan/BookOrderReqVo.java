package com.colourfulchina.pangu.taishang.api.vo.res.bigan;

import lombok.Data;

import java.io.Serializable;

@Data
public class BookOrderReqVo implements Serializable {

    private Integer projectId;

    private Integer goodsId;

    private Integer shopId;

    private Integer groupId;

    private Integer shopItemId;

    private Integer packageId;

    private Integer unitId;

    private Long memberId;
}
