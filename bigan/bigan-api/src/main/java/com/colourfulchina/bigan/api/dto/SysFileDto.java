package com.colourfulchina.bigan.api.dto;

import com.colourfulchina.bigan.api.entity.SysFile;
import lombok.Data;

@Data
public class SysFileDto extends SysFile {
    private String type;
    private Integer objId;
    private Integer sort;
    private Integer delFlag;
}
