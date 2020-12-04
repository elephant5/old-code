package com.colourfulchina.mars.api.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;

@TableName("activate_code_send_record")
@Data
public class DemoEntity extends Model<DemoEntity> {
      
	private static final long serialVersionUID = 8990898144978514042L;
	
	@TableId(value = "id")
    private Integer id;
    //1."流水号"
    @TableField(value = "sn")
  	private String sn;  
  	//2."剩余库存，限量激活码时返回"
    @TableField(value = "stock")
  	private Integer stock;
  	//3.value = "处理时，如2018-08-08 18:18:18"
    @TableField(value = "process_time")
  	private Date processTime;
  	//4."行权地址(短链)"
    @TableField(value = "gift_url")
  	private String giftUrl;
  	//2、激活码
    @TableField(value = "activate_code")
  	private String activateCode;
  	//7、发送状态
    @TableField(value = "send_code")
  	private Integer sendCode;
  	//8、发送状态描述
    @TableField(value = "send_desc")
  	private String sendDesc;
    @TableField(value = "sign_code")
  	private String signCode;      //  是      签名code，由客乐芙提供
    @TableField(value = "validate_code")
    private String validateCode;  //  是      签名校验码，由客乐芙提供
    @TableField(value = "token")
    private String token;         //  是      签名code+签名校验码进行md5加密，由客乐芙提供
    @TableField(value = "project_id")
    private Integer projectId;    //  是      项目编号，由客乐芙提供
    @TableField(value = "package_id")
    private Integer packageId;    //  是      权益包编号，由客乐芙提供
    @TableField(value = "channel_id")
    private Integer channelId;    //  是      渠道编号，由客乐芙提供
    @TableField(value = "buyer_name")
  	private String buyerName;     //  是      购买人姓名
    @TableField(value = "buyer_gender")
  	private String buyerGender;	  //  否	    购买人性别(M/F)
    @TableField(value = "buyer_phone")
  	private String buyerPhone;	  //  是	    购买人手机号码
    @TableField(value = "buyer_id_code")
  	private String buyerIdCode;	  //  否	    购买人证件号码
    @TableField(value = "buyer_card_no")
  	private String buyerCardNo;	  //  否	    购买人卡号
    @TableField(value = "activate_type")
  	private String activateType;  //  否	    激活方式,默认是激活码激活
    @TableField(value = "order_id")
  	private String orderId;	      //  是	    订单号码，同一渠道编号和订单编号唯一
    @TableField(value = "activate")
  	private Integer activate;	  //  是      (true,false)	是否直接激活
    @TableField(value = "limited")
  	private Integer limited;	  //  是      (true,false)	是否限量激活码，由客乐芙提供
    @TableField(value = "batch_id")
  	private Integer batchId;	  //  否	     发码批次号，限量激活码时必须传入
    @TableField(value = "send_sms")
  	private Integer sendSms;	  //  是	    是否发送短信
  	
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
