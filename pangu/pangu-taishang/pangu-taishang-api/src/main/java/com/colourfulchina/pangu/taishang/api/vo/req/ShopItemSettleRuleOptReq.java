package com.colourfulchina.pangu.taishang.api.vo.req;

import com.colourfulchina.pangu.taishang.api.vo.SettleExpressVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 规格结算规则添加/修改入参
 */
@Data
@Api("规格结算规则添加/修改入参")
public class ShopItemSettleRuleOptReq implements Serializable {
    private static final long serialVersionUID = 325185268568234921L;

    @ApiModelProperty("规格结算规则id")
    private Integer id;
    @ApiModelProperty("商户id")
    private Integer shopId;
    @ApiModelProperty("商户规格id")
    private Integer shopItemId;
    @ApiModelProperty("起止开始时间")
    private Date startDate;
    @ApiModelProperty("起止结束时间")
    private Date endDate;
    @ApiModelProperty("星期列表")
    private List<Integer> weeks;
    @ApiModelProperty("结算公式列表")
    List<SettleExpressVo> settleExpressVoList;

}
