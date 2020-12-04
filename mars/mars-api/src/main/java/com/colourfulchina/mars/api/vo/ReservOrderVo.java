package com.colourfulchina.mars.api.vo;


import com.baomidou.mybatisplus.annotations.TableField;
import com.colourfulchina.colourfulCoupon.api.entity.CpnThirdCode;
import com.colourfulchina.mars.api.entity.*;
import com.colourfulchina.pangu.taishang.api.vo.req.GoodsBaseVo;
import com.colourfulchina.pangu.taishang.api.vo.res.ShopDetailRes;
import com.colourfulchina.pangu.taishang.api.vo.res.ShopSettleMsgRes;
import com.google.common.collect.Lists;
import lombok.Data;

import java.math.BigDecimal;
import java.util.*;

@Data
public class ReservOrderVo  extends ReservOrder  {
    private static final long serialVersionUID = 6110718714640462422L;
    private ReservOrderDetail reservOrderDetail;
    private ReservOrderHospital hospital ;
    private String giftTypeName;
    private String serviceTypeCode;
    private String shopItemName;
    private List<ReservOrderPrice> reservPrices;
    private List<ShopSettleMsgRes> shopSettleMsgRes = Lists.newArrayList();
    private ReservOrderDetail detail;
    private ReservCode reservCode;
    private String superposition ;
    private String  singleThread;
    //-----------------激活---------------------
    private String activeName;//激活人名字
    private String code;
    private String codeStatus;
    //-----------------激活码---------------------
    private String goodsName;
    private String hotelName;
    private String shopName;
    private String activePhone;//激活人手机号
    private Integer activeSex;//激活人性别
    private String varTags;
    private ShopDetailRes  shopDetailRes;
    private GoodsBaseVo goodsBaseVo;
    //-----------------核销码---------------------
    private Integer reservCodeId;
    private String varCode;
    private String varStatus;
    private String varCodeTime;
    //--------------------------------------
    private String deparDate	;//离开时间
    private Integer checkNight;//	入住多少间
    private Integer nightNumbers;//	入住多少夜
    private String accoAddon;//	房型 例如 豪华/无早餐
    private String accoNedds	;//床型 大/双
    //------------


    private List<String> cancelDates;//取消日期
    private List<Integer> channel;//销售渠道
    private List<Integer> goods;//项目ID
    private List<String> orderDate;//订单日期
    private String params;//关联人识别
    private List<Integer> reservChannel;//预定渠道
    private List<String> reservDates;//预约日期
    private List<String> reservStatus;//状态
    private List<String> resourceTypes;//权益类型
    private List<String> serviceTypes;//资源类型
    private List<Integer> shopIds;//商户ID
    private List<String> successDates;//预约成功日期
    private List<String> userStatus;//使用状态
    private List<String> tagsList;//标签筛选

    private Integer rank; //序号
    private Date actOutDate;//激活码出库时间
    private String actCode;//激活码
    private Date actExpireTime;//激活码到期时间
    private Long memberId; //权益人acid
    private BigDecimal protocolPrice;//协议价
    private BigDecimal netPrice;//净价
    private String serviceRate;//服务费
    private String taxRate;//增值税
    private String settleRule;//结算规则
    private String settleMethod;//结算方式
    private Integer useCount;//权益使用次数
    private Integer totalCount;//权益总次数
    private Date oldExpireTime;//延期前时间
    private Date newExpireTime;//延期后时间
    private Integer proLongId;//延期id
    private String checkDate;//入住时间
    private BigDecimal payAmoney;//客户支付
    private BigDecimal paymentAmount;//实付金额
    

    private Integer payTimeOut;//分钟

//    ------update之前信息--------
    private Integer oldProductId;//调剂酒店的oldProductId
    private String oldShopName;
    private String oldHotelName;//调剂酒店的oldHotelName
    private String oldShopItemName;
    private String oldGiftDate;
    private String oldDeparDate;
    private int oldCheckNight;//入住间
    private String oldAccoAddon;//房型
    private String oldGiftName;//预约人
    private String oldGiftPhone;//预约人电话
    private String oldGiftTime;
    private Integer bankTypeId;//大客户

    //第三方券信息
    private CpnThirdCode cpnThirdCode;

    private Long thirdCpnNo;//第三方券表id

    //自付金额
    private BigDecimal realMoney;


    private String oldKey;
    //自付标签
    private String payTags;

    //-----------实物物流信息---------------
    private List<String> expressNameIds;
    private List<String> expressNumbers;
    private String expressStatus;
    private List<String> expressDates;

    //实物物流实体
    private LogisticsInfo logisticsInfo;
    //退货物流（1退货，0不退货）
    private Integer expressFlag;
    //------------

    //就诊人信息
    private Long memFamilyId;//预约人家属ID
    private Long hospitalId;
    private String hospitalMobile;
    private String hospitalName;
    private String hospitalSex;
    private String hospitalCertificateType;
    private String hospitalCertificateNumber;
    private String department;//科室
    private String visit;//就诊类型
    private String special;// 特殊需求
    private Integer medicalInsuranceType;//医保类型 1-医保 2-自费
}
