package com.colourfulchina.pangu.taishang.api.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Sections implements Serializable {
    private static final long serialVersionUID = -4565369267701295510L;

    /**
     * 老系统中portal字段实体
     */
    private List<PortalOldVo> sections;
}
