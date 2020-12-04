package com.colourfulchina.bigan.api.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ListSysFileReq implements Serializable {
    private String type;
    private Integer objId;
}