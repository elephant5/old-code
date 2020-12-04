package com.colourfulchina.pangu.taishang.api.vo.req;

import lombok.Data;

import java.io.Serializable;

@Data
public class ListSysFileReq implements Serializable {
    private static final long serialVersionUID = -7358085702653063443L;

    private String type;
    private Integer objId;
    private String guid;
}
