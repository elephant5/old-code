package com.colourfulchina.pangu.taishang.api.vo.req;

import lombok.Data;

import java.util.Date;

@Data
public class SelectPriceByTimeReqVo {

    private Integer productGroupProductId;

    private Date bookDate;

}
