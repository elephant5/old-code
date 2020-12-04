package com.colourfulchina.pangu.taishang.api.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 老系统商户表中more字段内容实体
 */
@Data
public class OldShopMoreVo implements Serializable {
    private static final long serialVersionUID = -7012475786145878071L;
    /**
     * 负责人
     */
    private String principal;
    /**
     * 录单提示
     */
    private String tips;
    /**
     * 结算精度（0整数，1一位小数，2-2位小数）
     */
    private Integer decimal;
    /**
     * roundUp
     */
    private String roundup;
    /**
     * 入住时间
     */
    private String checkInTime;
    /**
     * 退房时间
     */
    private String checkOutTime;
    /**
     * 最少提前天数
     */
    private Integer bookMinDays;
    /**
     * 最多提前天数
     */
    private Integer bookMaxDays;
}
