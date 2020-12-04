package com.colourfulchina.pangu.taishang.api.vo.req;

import lombok.Data;

@Data
public class ShopListPageQueryReq extends ShopListQueryReq {
    private static final long serialVersionUID = 1937798808768759221L;
    
    private Integer offset;

    private Integer limit;
}
