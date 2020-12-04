package com.colourfulchina.pangu.taishang.api.vo.res;

import io.swagger.annotations.Api;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 产品列表导出字段
 */
@Data
@Api("产品列表导出字段")
public class GroupProductExportVo implements Serializable {
    private static final long serialVersionUID = -3006792066459548511L;

    private String cityName;
    private String countryName;
    private String hotelChName;
    private String hotelEnName;
    private String shopChName;
    private String shopEnName;
    private String serviceName;
    private String serviceCode;
    private String giftCode;
    private String giftShortName;
    private String giftName;
    private String shopItemId;
    private String shopItemName;
    private String openTime;
    private String closeTime;
    private String addon;
    private String needs;
    private String groupProductBlock;
    private String productGroupBlock;
    private String goodsBlock;
    private String shopItemBlock;
    private String shopBlock;
    private String address;
    private String phone;
    private String parking;
    private String children;
    private String remark;
    private Integer productId;
    //起止时间
    private Date startDate;
    //截止时间
    private Date endDate;
    private BigDecimal cost;
    private String applyTime;
    //周一
    private Integer monday;
    //周二
    private Integer tuesday;
    //周三
    private Integer wednesday;
    //周四
    private Integer thursday;
    //周五
    private Integer friday;
    //周六
    private Integer saturday;
    //周日
    private Integer sunday;
}
