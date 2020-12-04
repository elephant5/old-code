package com.colourfulchina.bigan.api.vo;

import com.colourfulchina.bigan.api.entity.SysFile;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class HotelOldPortalVo implements Serializable {
    private static final long serialVersionUID = 6271501003838118856L;
    /**
     * 酒店章节标题
     */
    private String title;
    /**
     * 酒店章节内容
     */
    private String content;
    /**
     * 酒店章节图片列表
     */
    private List<SysFile> fileList;
}
