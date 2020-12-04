package com.colourfulchina.mars.api.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;


@Data
public class MyGiftCodeVo implements Serializable{

	private static final long serialVersionUID = -4386616377317171318L;
	
	private Integer bankId; //隶属大客户ID
	
	private String bankName; //隶属大客户名称
	
	private String bankLogo; //隶属大客户Logo
	
	private Integer projectId; // 项目ID
	
	private Integer packageId; //权益包ID
	
	private Integer unitId; // 权益码id

	private String projectName; //标题
	
	@JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
	private Date expiryDate; // 过期时间
	
	private String imgUrl; //图片地址
	
	private Integer status; //该权益的状态
	
	private String backColour; //yanse

	private String salesChannelId;

	private Integer goodsId;

	private List<GoodsClauseList> goodsclauseList;//使用限制
	
	private List<ProjectGroupGiftVo> giftList; // 权益内容

	private String actCode;

}
