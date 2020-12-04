package com.colourfulchina.mars.api.vo.req;

import com.colourfulchina.mars.api.entity.GiftCode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.List;

/**
 * 激活码分页入参
 */
@Slf4j
@Data
public class GiftCodePageReq extends GiftCode {
    private static final long serialVersionUID = 4827303527704263784L;

    @ApiModelProperty("多个激活码,逗号隔开")
    private String giftCodes;
    @ApiModelProperty("商品ids")
    private List<Integer> goodsIds;
    @ApiModelProperty("销售渠道")
    private List<Integer> salesChannel;
    @ApiModelProperty("激活码状态")
    private List<Integer> codeStatus;
    @ApiModelProperty("激活码到期时间")
    private List<Date> expireTimes;
    @ApiModelProperty("客户手机号")
    private String phone;
}
