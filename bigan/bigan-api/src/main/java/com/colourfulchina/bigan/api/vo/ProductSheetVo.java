package com.colourfulchina.bigan.api.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class ProductSheetVo implements Serializable {
    private String id;
    private String batchId;
    private int sheetIndex;
    private String serviceCode;
    private String sheetName;
    private List<String> titleList;
    private List<ProductRowVo> rowVoList;
    private Integer state;//0未处理,1处理中,2成功,3失败
    private Date createTime;
    private Date updateTime;
}
