package com.colourfulchina.pangu.taishang.api.dto;

import lombok.Data;

@Data
public class TSysFunction implements java.io.Serializable {

	private static final long serialVersionUID = 586671622683524977L;

	private Long id; //id;
	
	private Long pid; //pid;
	
	private Long appid; //appid;
	
    private String functionurl; //功能地址;
	
    private String functioncode; //功能代码;
	
    private String functionname; //功能名称;
	
    private String functionttype; //功能类型;菜单;普通URL
	
    private String lstupdatedtm; //lstupdatedtm;
	
	private String createdtm; //createdtm;
	
    private Integer status; //状态;1 有效 0 无效
	
    private String createusr; //createuser;
	
    private String lastupdateusr; //lastupdateuser;
	
    private Integer functionlevel; //功能级别;
    
    private Integer functionorder; //排序;
    
    private String iconpath; //功能图标;
    

}
