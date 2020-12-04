package com.colourfulchina.mars.api.enums;

/**
 * @ClassName: SvipOrderEnum     
 * @Description:订单状态
 * @author: sunny.wang     
 * @date:   2019年4月19日 上午9:34:08   
 * @version V1.0 
 * @Copyright: www.colourfulchina.com@2019.All rights reserved.
 */
public enum OrderStatusEnum {

	TO_BE_PAID(0, "待支付", ""), 
	IN_PAYING(1, "支付中", "交易进行中，请勿重复支付"), 
	SUCC_PAID(2, "支付成功", "支付成功，请勿重复支付"), 
	FAIL_PAID(-2, "支付失败", "支付失败，请联系客服"), 
	CANCELLED(3, "已取消", "交易失败，交易已取消"), 
	IN_REFUND(4, "退款中", "交易失败，退款处理中"), 
	SUCC_REFUND(5, "退款成功", "交易失败，已退款成功"), 
	FAIL_REFUND(-5, "退款失败", "退款失败，请联系客服"), 
	PAY_TIMEOUT(6, "支付超时", "支付超时，请重新下单"),
	TRANS_COMPLETED(7, "交易完成", "交易完成");

	private int code;
	private String value;
	private String desc;

	OrderStatusEnum(int code, String value, String desc) {
		this.code = code;
		this.value = value;
		this.desc = desc;
	}

	public int getCode() {
		return code;
	}

	public String getValue() {
		return value;
	}
	
	public String getDesc() {
		return desc;
	}
	
	public static String getStatusDesc(Integer code){
        for (OrderStatusEnum status:OrderStatusEnum.values()){
            if(status.getCode()==code) {
            	return status.desc;
            }
        }
        return OrderStatusEnum.TO_BE_PAID.desc;
    }
	
}
