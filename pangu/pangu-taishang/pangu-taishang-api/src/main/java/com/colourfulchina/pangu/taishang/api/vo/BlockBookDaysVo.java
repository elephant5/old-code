package com.colourfulchina.pangu.taishang.api.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 可预约时间最大最小天数
 */
@Data
public class BlockBookDaysVo implements Serializable {
    private static final long serialVersionUID = 1520093690815054487L;

    private String serviceType;
    private Integer shopMinBookDays;
    private Integer shopMaxBookDays;
    private Integer goodsMinBookDays;
    private Integer goodsMaxBookDays;
    private Integer groupMinBookDays;
    private Integer groupMaxBookDays;
    private String countryId;
}
