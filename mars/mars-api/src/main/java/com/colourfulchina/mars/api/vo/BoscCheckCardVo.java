package com.colourfulchina.mars.api.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Data
public class BoscCheckCardVo implements Serializable {

    private static final long serialVersionUID = 3546335774998732727L;

    //验卡接口参数：
    @JsonProperty
    private String MsgType="0360";
    @JsonProperty( "AcctNo")
    private String AcctNo;//------  卡号传加密的卡号
    @JsonProperty( "ProCode")
    private String ProCode = "340000";// ------   处理代码   固定值340000
    @JsonProperty( "TranTime")
    private String TranTime;// ------   交易日期与时间 mmddhhmmss
    @JsonProperty( "SysNo")
    private String SysNo;//------- 系统跟踪审计号码随机6位数
    @JsonProperty( "LocalTime")
    private String LocalTime;//  ------   本地交易时间hhmmss
    @JsonProperty( "LocalDate")
    private String LocalDate;//------   本地交易日期mmdd

//    private String entryMode="012";//------   登陆模式  固定值012
    @JsonProperty( "EcifNo")
    private String EcifNo;   //------  加密ECIF客户号10或11位
    @JsonProperty( "CardNo")
    private String CardNo; //   ------  卡后四位不加密
    @JsonProperty( "SerNo")
    private String SerNo;//唯一流水号
}
