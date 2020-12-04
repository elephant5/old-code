package com.colourfulchina.mars.api.vo.req;

import lombok.Data;

import java.io.Serializable;

@Data
public class SendGiftCodeReqVo  implements Serializable{

    private static final long serialVersionUID = 6910939506408988795L;

    private Integer goodsId;

    private Integer codeNum;

    private String sign;
}
