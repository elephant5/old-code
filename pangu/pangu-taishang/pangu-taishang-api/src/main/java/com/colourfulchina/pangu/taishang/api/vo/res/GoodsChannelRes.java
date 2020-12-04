package com.colourfulchina.pangu.taishang.api.vo.res;

import com.colourfulchina.pangu.taishang.api.entity.SalesChannel;
import io.swagger.annotations.Api;
import lombok.Data;

@Api("商品渠道出参")
@Data
public class GoodsChannelRes extends SalesChannel {
    private static final long serialVersionUID = 4048878586324994273L;

    private String bankName;
    private String salesChannelName;
    private String salesWayName;
    private String bankCode;
}
