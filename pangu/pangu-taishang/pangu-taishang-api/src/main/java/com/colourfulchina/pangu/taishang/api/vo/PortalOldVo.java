package com.colourfulchina.pangu.taishang.api.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PortalOldVo implements Serializable {
    private static final long serialVersionUID = 1160592569073264528L;

    /**
     * 酒店章节内容
     */
    private String content;
    /**
     * 酒店章节图片
     */
    private List<String> pics;
    /**
     * 酒店章节标题
     */
    private String title;
}
