package com.colourfulchina.pangu.taishang.api.vo.res.bigan;

import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

@Data
public class ShopItem implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String type;
    private Long shopId;
    private String name;
    private String needs;
    private String addon;
    private String opentime;
    private String closeTime;
    private String block;
    private String price;
    private String more;
    private String bookTime; //预约时段:适用单杯茶饮 下午茶 自助餐 定制套餐
    private String serviceType;//服务类型
    private String serviceName;//服务名称
}