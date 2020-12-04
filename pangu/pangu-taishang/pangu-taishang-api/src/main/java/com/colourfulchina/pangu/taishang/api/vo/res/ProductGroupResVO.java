package com.colourfulchina.pangu.taishang.api.vo.res;

import lombok.Data;

import java.io.Serializable;

@Data
public class ProductGroupResVO implements Serializable{

    private static final long serialVersionUID = 3571086744252316247L;
    private Integer productGroupProductId;

    private Integer productGroupId;

    private Integer productId;

    private Integer shopId;

    private Integer shopItemId;

    private Integer shopChannelId;

    private String source;  //第三方渠道名称

    private String thirdCpnNo; //第三方券产品编号

}
