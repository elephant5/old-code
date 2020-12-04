package com.colourfulchina.pangu.taishang.api.vo.res.bigan;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
public class ProjectChannel implements Serializable {
    private static final long serialVersionUID = 2958887527612149066L;
    private Integer ProjectId;
    private Integer oldId;
    private Integer newId;
    private String note;
}
