package com.colourfulchina.bigan.api.vo;

import java.io.Serializable;

import lombok.Data;

@Data
public class ProjectGoodsResVo implements Serializable {
	    
	private static final long serialVersionUID = -5465809292171574923L;
	
	private String goodsId;// 商品id
	private String type;// 商品类型
	private String gift;// 权益类型
	private String shopid;// 商户id
	private String title;// 商品标题
	private String clause;// 条款
	private String city;// 城市
	private String hotel;// 酒店
	private String shopName;// 商户名称
	private String address;// 地址
	private String summary;// 商户介绍
	private String imgList;// 商户图片

	private String marketPrice; //市场价格
	private String opentime; //开餐时间
	private String checkinTime; //入住时间
	private String checkoutTime; //退房时间
	private String parkingInfo; //泊车信息
	private String childPolicy; //儿童政策
	
	private String phone;// 电话
	
}