package com.colourfulchina.mars.api.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Nickal
 * @Description:
 * @Date: 2019/5/16 11:06
 */
@Data
public class ProjectGroupGiftVo implements Serializable {

    private static final long serialVersionUID = -4288897791459186464L;

    private Integer groupId;

    //权益标题
    private  String title;
    
    //权益简称
    private String shortName;

    //总次数
    private Integer totalTimes;

    //剩余次数
    private Integer surplusTimes;

    //使用次数
    private Integer useCount;

    //周期内总次数
    private Integer cycleCount;

    //权益类型
    private String giftType;

    //资源类型
    private String sysService;

    //权益类型code（目前仅限于住宿）
    private String giftCode;

    //最小提前预约时间
    private Integer minBookDays;

    //最大提前预约时间
    private Integer maxBookDays;

    //是否是周期重复类型
    private String type;

    //周期重复的开始时间
    private String startTime;

    //周期重复的结束时间
    private String endTime;

    //剩余免费次数
    private Integer surplusFreeTimes;
}
