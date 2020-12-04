package com.colourfulchina.pangu.taishang.api.vo.req;

import lombok.Data;

@Data
public class ShopListPageReq extends ShopListReq{

    private Integer offset;
    private Integer limit;

}
