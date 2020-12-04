package com.colourfulchina.mars.api.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: SynchroPushOrderVo
 * @Description: 银联推送预约单信息
 * @author Sunny
 * @date 2019年9月24日09:19:43
 * @company Colourful@copyright(c) 2018
 * @version V1.0
 */
@Data
public class SynchroPushOrderVo implements Serializable {

	private static final long serialVersionUID = 1902378046879754158L;

	private String bookOrderId; // 订单编号
	private String orderTime; // 订单生成时间  NEW
//	private String custPhone; // 客户电话 去掉了
	private String giftName; // 权益内容
	private String bookDate; // 预定时间 - 日期
	private String bookTime; // 预定时间 - 时间
	private String hotelName; // 预约酒店- 酒店名称
	private String shopName; //  预约酒店 - 餐厅名称 NEW
	private String giftTypeName; // 用餐类型 NEW
	private String mealSection; // 餐段 NEW
	private String address; // 酒店地址
	private String usePeople; // 用餐人数
	private String useName;  // 预约姓名
	private String usePhone; // 预约手机
	private String notes; // 特殊要求 备注
	private String city; // 酒店城市
	private String reservNumber; // 核销码  NEW
	private String status; // 订单状态

}