package com.colourfulchina.bigan.api.vo;

import com.colourfulchina.bigan.api.entity.PrjPackage;
import com.colourfulchina.bigan.api.entity.Project;
import com.colourfulchina.bigan.api.entity.ProjectChannel;
import com.colourfulchina.bigan.api.entity.ProjectGoods;
import com.colourfulchina.bigan.api.entity.ProjectMeta;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class BigProjectVo implements Serializable {
    private Project project;
    private List<PrjPackage> packageList;
    private List<ProjectChannel> channelList;
    private List<ProjectMeta> metaList;
    private List<PrjGroupVo> groupVoList;
}
