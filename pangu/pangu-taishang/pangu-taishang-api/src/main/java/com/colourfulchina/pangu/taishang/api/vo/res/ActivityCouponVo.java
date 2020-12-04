package com.colourfulchina.pangu.taishang.api.vo.res;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
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
public class ActivityCouponVo implements Serializable {

    private static final long serialVersionUID = -8393486864378330562L;
    @ApiModelProperty("礼券类型：1抵用券 2折扣券")
    private Integer couponType;
    @ApiModelProperty("礼券批次")
    private Long batchId;
    @ApiModelProperty("优惠券名称")
    private String batchName;
    @ApiModelProperty("发放规则：-1 无限制 ，其他为固定次数")
    private Integer grantLimit;
    @ApiModelProperty("使用限制：-1 无限制 其他为固定次数")
    private Integer useLimit;
    @ApiModelProperty("使用规则频率数量：几 年，几 季度，几 月，几 周，几 天")
    private Integer useLimitRateNum;
    @ApiModelProperty("使用规则频率：Y 年，S 季度，M 月，W 周，D 天")
    private String useLimitRate;

}
