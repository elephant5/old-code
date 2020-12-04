package com.colourfulchina.bigan.api.vo;

import java.io.Serializable;

import lombok.Data;

@Data
public class ProjectReqVo implements Serializable{
	
	private static final long serialVersionUID = 1199747593953341339L;
	
	private String signCode;      //  是      签名code，由客乐芙提供
	private String validateCode;  //  是      签名校验码，由客乐芙提供
	private String token;         //  是      签名code+签名校验码进行md5加密，由客乐芙提供
	
	private Integer projectId;  // 是    项目ID 
	private Integer packageId;   // 是   权益包ID
	private Integer prjGroupId; // 否  产品组ID
	
}
