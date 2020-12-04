package com.colourfulchina.bigan.api.vo;

import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class ProductRowVo {
    private String id;
    private String batchId;
    private int sheetIndex;
    private int rowIndex;
    private Map<String,String> rowMap;
    private Integer state;//0未处理,1处理中,2成功,3失败
    private String result;
    private Date createTime;
    private Date updateTime;
}
