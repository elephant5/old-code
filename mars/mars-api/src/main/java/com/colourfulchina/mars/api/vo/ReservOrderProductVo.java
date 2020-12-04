package com.colourfulchina.mars.api.vo;

import com.colourfulchina.mars.api.entity.ReservCode;
import com.colourfulchina.mars.api.entity.ReservOrderDetail;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Author: luck.luo
 * @Description:
 * @Date: 2019/5/22 10:39
 */
@Data
public class ReservOrderProductVo {
    private static final long serialVersionUID = -3813215562729412187L;
    private  Integer id;//
    private  String productName;//产品名称
    private String serviceType;//	产品类型：自助餐、机场服务等
    private String serviceName;//
    private Integer productId;//	产品id
    private Integer goodsId	;//项目id
    private String goodsName	;//项目名称
    private String goodsImg	;//项目图片
    private Integer productGroupId;//	产品组id
    private Integer productGroupProductId;//	产品组产品id
    private Long memberId	;//用户id
    private Integer salesChannleId;//	销售渠道
    private Integer shopId	;//商户id
    private String shopName;//商户名称
    private String shopAddress; //商户地址
    private String shopCity; //商户所在城市
    private String hotelName; //酒店名称
    private String reservRemark	;//备注
    private String giftName;//	预约人姓名
    private String giftPhone	;//预约电话
    private String proseStatus;//预约状态
    private String proseStatusStr;//预约状态

    private Integer exchangeNum	;//兑换数量

    private Integer messageFlag	;//是否发送短信
    private Integer resourceType	;//资源名称id


    private String giftDate	;//预约日期 2019-01-12
    private String giftTime	;//预约时间  12：00
    private String giftText; //权益名称
    private String shopItemName;
    private Date createTime;
    private String createTimeStr;

    private String cancelPolicy;//取消政策


    private String countryid;//国家信息  CN国内

    private List<ReservOrderDetail> details;//预约单详情list

    private String giftType;//	预约单类型

    private Integer giftPeopleNum;//	预约人数

    private Integer reservCodeId;//核销码id

    private String useStatus;//使用状态

    private String varCode;//核销码
    private ReservCode reservCode;//核销码

    private String reservNumber;//确认号


    private BigDecimal payAmoney;//	实付金额
    private Integer payStatus;

    private Long thirdCpnNo;//第三方券表id

    private String validStartTime;//第三方券有效期开始时间

    private String experTime; //第三方券过期时间

    private String addon;//餐型
    private String needs;//床型
    private String couponsType; //第三方券类型:卡密；短链

    private String thirdCodePassword; //券码密码

    private String shortUrl; //或短链

    private String thirdCpnSource; //第三方券渠道

    private String thirdCpnNum; //第三方券产品编号

    private Integer expressMode; //配送方式数字

    private String expressModeStr; //配送方式中文

    private String expressAddress; //配送地址

    private Integer expressStatus;//快递状态数字

    private String expressStatusStr;//快递状态中文

    private String expressNumber; //快递单号

    private String consignee;//收件人

    private String expressPhone;//收件人手机

    private String companyName;//快递公司名称
    //航班号
    private String flightNumber;
    //机场
    private String  airport;
    //目的地
    private String endPoint;
    //出发地
    private String startPoint;
    //出行类型 1-接机 2-送机
    private Integer travelType;
    //备注
    private String  remark;
    //当前预约单使用掉的免费次数
    private Integer  useFreeCount;


    private List<ReservOrderHospitalVO> hospitalInfos;//医疗信息
}
