package com.colourfulchina.pangu.taishang.api.vo;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
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
public class ActivityBackReqVo implements Serializable {
    private static final long serialVersionUID = -6802031050510957536L;
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
    @ApiModelProperty("活动开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date startDate;
    @ApiModelProperty("活动结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date endDate;
    @ApiModelProperty("永久有效：0否 1是 默认0")
    private Integer foreverTag;
    @ApiModelProperty("活动状态 0草稿 1待审核 2审核通过 3待上线 4上线 5审核未通过 6待下线 7下线")
    private Integer actStatus;

    @ApiModelProperty("关联的项目ID")
    private List<Integer> goodIds;

}
