package com.colourfulchina.mars.api.vo.res;

import com.baomidou.mybatisplus.annotations.TableField;
import com.colourfulchina.pangu.taishang.api.entity.ProductGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 上海银行用户卡片信息
 */
@Data
public class BoscCardInfoVo {

    @ApiModelProperty(value = "卡片名称")
    private String cardName;

    @ApiModelProperty(value = "卡片展示名称")
    private String cardShowName;

    @ApiModelProperty(value = "卡号")
    private String cardNo;

    @ApiModelProperty(value = "卡产品组代码")
    private String cardProgroupNo;

    @ApiModelProperty(value = "卡片层级")
    private String cardLevel;

    @ApiModelProperty(value = "卡片类型")
    private String cardType;

    @ApiModelProperty(value = "卡片对应的项目信息")
    private List<GoodsResVo> goodsResVoList;

    @ApiModelProperty(value = "卡的可用状态")
    private Integer status =0;
}
