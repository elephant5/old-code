package com.colourfulchina.bigan.api.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ItemNameVo implements Serializable {
    private String name;
    private List<String> nameDetailList;
}
