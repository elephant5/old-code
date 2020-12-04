package com.colourfulchina.bigan.api.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ProductWorkbookVo implements Serializable {
    private static final long serialVersionUID = 290041619061675943L;
    private String batchId;//主键
    private String fileName;
    private Integer bankId;
    private Integer projectId;
    private Integer totalSheet;
    private Integer totalRow;
    private Integer successRow;
    private Integer failRow;
    private Integer state;//0未处理,1处理中,2成功,3失败
    private Date createTime;
    private Date updateTime;
}
