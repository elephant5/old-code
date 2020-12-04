package com.colourfulchina.pangu.taishang.api.vo.req;

import com.colourfulchina.pangu.taishang.api.dto.BlockRule;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 新增/修改商户协议信息入参
 */
@Data
@Api("新增/修改商户协议信息入参")
public class AddShopProtocolReq implements Serializable {
    private static final long serialVersionUID = 4980170671611614245L;

    @ApiModelProperty("商户id")
    private Integer shopId;
    @ApiModelProperty("商户协议id")
    private Integer protocolId;
    @ApiModelProperty("商户账号id")
    private Integer shopAccountId;
    @ApiModelProperty("商户资源类型id")
    private Integer shopChannelId;
    @ApiModelProperty("商户结算币种")
    private String currency;
    @ApiModelProperty("商户结算方式")
    private String settleMethod;
    @ApiModelProperty("商户结算精度")
    private Integer decimal;
    @ApiModelProperty("商户进位规则")
    private String roundup;
    @ApiModelProperty("商户负责人")
    private String principal;
    @ApiModelProperty("商户预付金")
    private BigDecimal imprest;
    @ApiModelProperty("商户押金")
    private BigDecimal deposit;
    @ApiModelProperty("商户合同开始日期")
    private Date contractStart;
    @ApiModelProperty("商户合同结束日期")
    private Date contractExpiry;
    @ApiModelProperty("商户block规则")
    private List<BlockRule> blockRuleList;
    @ApiModelProperty("商户泊车信息")
    private String parking;
    @ApiModelProperty("商户儿童政策")
    private String children;
    @ApiModelProperty("商户重要通知")
    private String notice;
    @ApiModelProperty("商户账号")
    private String shopAccount;
    @ApiModelProperty("商户密码")
    private String shopPassword;
    @ApiModelProperty("开户名")
    private String accountName;
    @ApiModelProperty("开户行")
    private String openingBank;
    @ApiModelProperty("银行账号")
    private String bankAccount;
    @ApiModelProperty("结算价格的变动对已经生成的预约单的影响")
    private String changeRice;
}
