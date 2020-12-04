package com.colourfulchina.mars.api.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class PayInfoVo implements Serializable {
    private static final long serialVersionUID = -2391101094534776414L;

    private String salesOrderId;
    private String sign;
    private String amount;
    private String payChannel;
    private String payOrderId;



}
