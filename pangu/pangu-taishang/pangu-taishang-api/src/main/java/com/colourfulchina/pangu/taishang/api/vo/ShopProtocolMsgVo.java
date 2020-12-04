package com.colourfulchina.pangu.taishang.api.vo;

import com.colourfulchina.pangu.taishang.api.dto.BlockRule;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 商户协议信息vo
 */
@Data
public class ShopProtocolMsgVo implements Serializable {
    private static final long serialVersionUID = -3291887224908764689L;

    @ApiModelProperty("商户id")
    private Integer shopId;
    @ApiModelProperty("商户协议id")
    private Integer protocolId;
    @ApiModelProperty("商户渠道id")
    private Integer shopChannelId;
    @ApiModelProperty("商户账号id")
    private Integer shopAccountId;
    @ApiModelProperty("商户类型")
    private String shopType;
    @ApiModelProperty("负责人")
    private String principal;
    @ApiModelProperty("预付款")
    private BigDecimal imprest;
    @ApiModelProperty("押金")
    private BigDecimal deposit;
    @ApiModelProperty("商户合同开始日期")
    private Date contractStart;
    @ApiModelProperty("商户合同结束日期")
    private Date contractExpiry;
    @ApiModelProperty("商户中心账号")
    private String shopAccount;
    @ApiModelProperty("商户中心密码")
    private String shopPassword;
    @ApiModelProperty("商户结算方式")
    private String settleMethod;
    @ApiModelProperty("商户结算币种")
    private String currency;
    @ApiModelProperty("商户结算精度")
    private Integer decimal;
    @ApiModelProperty("商户进位规则")
    private String roundup;
    @ApiModelProperty("开户名")
    private String accountName;
    @ApiModelProperty("开户行")
    private String openingBank;
    @ApiModelProperty("银行账号")
    private String bankAccount;
    @ApiModelProperty("商户block规则")
    private String blockRule;
    @ApiModelProperty("商户泊车信息")
        private String parking;
    @ApiModelProperty("商户儿童政策")
    private String children;
    @ApiModelProperty("商户重要通知")
    private String notice;
    @ApiModelProperty("渠道资源类型")
    private Integer internal;
    @ApiModelProperty("商户block列表")
    private List<BlockRule> blockRuleList;
}
