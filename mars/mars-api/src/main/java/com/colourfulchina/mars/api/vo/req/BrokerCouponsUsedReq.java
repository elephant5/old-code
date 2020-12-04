package com.colourfulchina.mars.api.vo.req;

import lombok.Data;

import java.io.Serializable;

@Data
public class BrokerCouponsUsedReq implements Serializable {

    private String waresId;  //商品id
    private String wEid;     //库存编码
    private String wSign;    //优惠券标识 E
    private String wInfo;    // 优惠券串码
    private String usedDate; //使用日期 非必须 yyyyMMDD
    private String usedTime; //使用时间 非必须 HH:mm:ss

}
