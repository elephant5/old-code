package com.colourfulchina.pangu.taishang.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Copyright Colourful
 * @Author SunnyWang
 * @Date 2019年9月6日10:19:10
 */
@Data
public class ActivityPageVo implements Serializable {

    private static final long serialVersionUID = 7081891286861339295L;

    @ApiModelProperty("主键")
    private Integer id;
    @ApiModelProperty("活动名称")
    private String actName;
    @ApiModelProperty("销售渠道")
    private String salesChannel;
    @ApiModelProperty("全部商品 0否；1是；默认0")
    private Integer goodsTag;
    @ApiModelProperty("发放方式 1 购买成功后 2 注册成功后 3其他")
    private Integer grantMode;
    @ApiModelProperty("活动有效期")
    private String actExpire;
    @ApiModelProperty("活动状态 0草稿 1待审核 2审核通过 3待上线 4上线 5审核未通过 6待下线 7下线")
    private Integer actStatus;
    @ApiModelProperty("创建人")
    private String createUser;
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;
    @ApiModelProperty("更新人")
    private String updateUser;
    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updateTime;

}
