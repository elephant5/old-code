package com.colourfulchina.pangu.taishang.api.vo.res;

import com.colourfulchina.pangu.taishang.api.dto.SysFileDto;
import com.colourfulchina.pangu.taishang.api.entity.ShopItemSettleExpress;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Api("规格结算规则出参")
@Data
public class ShopItemSettleRulesListRes implements Serializable {
    private static final long serialVersionUID = -5078684554558608370L;

    @ApiModelProperty("结算规则主键")
    private Integer id;
    @ApiModelProperty("商户id")
    private Integer shopId;
    @ApiModelProperty("规格id")
    private Integer shopItemId;
    @ApiModelProperty("权益类型")
    private String gift;
    @ApiModelProperty("开始时间")
    private Date startDate;
    @ApiModelProperty("结束时间")
    private Date endDate;
    @ApiModelProperty("星期一")
    private int monday;
    @ApiModelProperty("星期二")
    private int tuesday;
    @ApiModelProperty("星期三")
    private int wednesday;
    @ApiModelProperty("星期四")
    private int thursday;
    @ApiModelProperty("星期五")
    private int friday;
    @ApiModelProperty("星期六")
    private int saturday;
    @ApiModelProperty("星期日")
    private int sunday;
    @ApiModelProperty("结算公式id")
    private Integer settleExpressId;
    @ApiModelProperty("结算规则翻译时间部分")
    private String timeStr;
    @ApiModelProperty("结算规则翻译权益公式部分")
    private String settleRuleStr;
    @ApiModelProperty("结算公式，编辑回显用")
    private String express;
    @ApiModelProperty("自定义增值税公式,编辑回显用")
    private String customTaxExpress;
    @ApiModelProperty("结算公式")
    private ShopItemSettleExpress settleExpress;

    @ApiModelProperty("文件列表")
    List<SysFileDto> files;
}
