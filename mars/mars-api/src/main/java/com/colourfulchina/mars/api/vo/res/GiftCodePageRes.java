package com.colourfulchina.mars.api.vo.res;

import com.colourfulchina.mars.api.entity.GiftCode;
import com.colourfulchina.mars.api.enums.ActCodeStatusEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 激活码分页出参
 */
@Slf4j
@Data
public class GiftCodePageRes extends GiftCode {
    private static final long serialVersionUID = 6112354148097008683L;

    @ApiModelProperty("码状态名称")
    private String codeStatusName;
    @ApiModelProperty("商品名称")
    private String goodsName;
    @ApiModelProperty("商品内部简称")
    private String goodsShortName;
    @ApiModelProperty("销售渠道名称")
    private String salesChannelName;
    @ApiModelProperty("手机号")
    private String phone;
    @ApiModelProperty("客户姓名")
    private String peopleName;

    public String getCodeStatusName() {
        String name = ActCodeStatusEnum.ActCodeStatus.findNameByIndex(this.getActCodeStatus());
        return name;
    }
}
