package com.colourfulchina.bigan.api.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class GoodsImportVo implements Serializable {
    private static final long serialVersionUID = -2965257459746188140L;

    private Integer bankId;
    private Integer projectId;
//    @JsonIgnore
//    MultipartFile multipartFile;
}
