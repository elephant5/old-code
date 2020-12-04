package com.colourfulchina.pangu.taishang.api.vo.res;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Copyright Colourful
 * @Author SunnyWang
 * @Date 2019年9月6日10:19:10
 */
@Data
public class ActivityResVo implements Serializable {

    private static final long serialVersionUID = -924682066265425056L;
    @ApiModelProperty("活动Id")
    private Integer actId;
    @ApiModelProperty("活动名称")
    private String actName;
    @ApiModelProperty("销售渠道")
    private String salesChannel;
    @ApiModelProperty("全部商品 0否；1是；默认0")
    private Integer goodsTag;
    @ApiModelProperty("发放方式 1 购买成功后 2 注册成功后 3其他")
    private Integer grantMode;
    @ApiModelProperty("活动开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date startDate;
    @ApiModelProperty("活动结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date endDate;
    @ApiModelProperty("永久有效：0否 1是 默认0")
    private Integer foreverTag;

    private List<ActivityCouponVo> couponVoList;
}
