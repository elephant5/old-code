package com.colourfulchina.bigan.api.vo;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class ProjectGroupResVo implements Serializable {

	private static final long serialVersionUID = 8010556508804379720L;

	private Integer id; // 产品组id
	private Integer projectId; // 所属项目id
	private String title; // 产品组标题
	private Integer times; // 使用次数限制

	private List<ProjectGoodsResVo> goodsDetailVoList; // 产品组包含的商品和商户信息
}
