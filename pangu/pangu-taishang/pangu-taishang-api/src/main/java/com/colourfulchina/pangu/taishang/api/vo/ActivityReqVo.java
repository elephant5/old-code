package com.colourfulchina.pangu.taishang.api.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class ActivityReqVo implements Serializable {

    private static final long serialVersionUID = 8251778683924241410L;

    private Integer id;

    private Integer idType;

    private Integer grantMode;

    private String channel;

}
