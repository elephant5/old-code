package com.colourfulchina.mars.api.vo;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * @ClassName: OpenCodeCallBackVo  
 * @Description: TODO(这里用一句话描述这个类的作用)   
 * @author Sunny  
 * @date 2018年9月21日  
 * @company Colourful@copyright(c) 2018
 * @version V1.0
 */
@Data
public class DemoVo implements Serializable{  
	
	private static final long serialVersionUID = -686679929035876511L;
	
	private Integer id;
	
	//1."流水号"
	private String sn;  
	//2."剩余库存，限量激活码时返回"
	private Integer stock;
	//3.value = "处理时，如2018-08-08 18:18:18"
	private Date processTime;
	//4."行权地址(短链)"
	private String giftUrl;
	//2、激活码
	private String activateCode;
	//3、手机号
	//4、姓名
	//5、对接方订单号
	//6、渠道
	//7、发送状态
	private Integer sendCode;
	//8、发送状态描述
	private String sendDesc;
	
	private String signCode;      //  是      签名code，由客乐芙提供
	private String validateCode;  //  是      签名校验码，由客乐芙提供
	private String token;         //  是      签名code+签名校验码进行md5加密，由客乐芙提供
	private Integer projectId;    //  是      项目编号，由客乐芙提供
	private Integer packageId;    //  是      权益包编号，由客乐芙提供
	private Integer channelId;    //  是      渠道编号，由客乐芙提供
	private String buyerName;     //  是      购买人姓名
	private String buyerGender;	  //  否	    购买人性别(M/F)
	private String buyerPhone;	  //  是	    购买人手机号码
	private String buyerIdCode;	  //  否	    购买人证件号码
	private String buyerCardNo;	  //  否	    购买人卡号
	private String activateType;  //  否	    激活方式,默认是激活码激活
	private String orderId;	      //  是	    订单号码，同一渠道编号和订单编号唯一
	private Integer activate;	  //  是      (true,false)	是否直接激活
	private Integer limited;	  //  是      (true,false)	是否限量激活码，由客乐芙提供
	private Integer batchId;	  //  否	     发码批次号，限量激活码时必须传入
	private Integer sendSms;	  //  是	    是否发送短信
	
}
