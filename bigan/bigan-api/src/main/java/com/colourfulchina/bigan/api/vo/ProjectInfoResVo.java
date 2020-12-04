package com.colourfulchina.bigan.api.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ProjectInfoResVo implements Serializable {

	private static final long serialVersionUID = 635935267906427389L;
	
	private Integer projectId; // 项目ID
	private String projectName; // 项目名称
	private String shortName; // 项目简称

	private String authType; // 验证类型
	private Integer multiGroup; // 是否多产品组
	private Integer packageId; // 所属权益包
	private String bankName;//隶属大客户
	private Integer bankId;//隶属大客户ID
	private List<ProjectGroupResVo> prjGroupVoList; // 项目产品组
}
