package com.colourfulchina.mars.api.vo.res;

import com.colourfulchina.mars.api.vo.CheckCodesVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.List;

/**
 * 检测激活码出参
 */
@Slf4j
@Data
public class CheckCodesRes implements Serializable {
    private static final long serialVersionUID = -148605844969190868L;

    @ApiModelProperty("传入检测的激活码")
    private List<String> allCodes;
    @ApiModelProperty("是否是相同的项目")
    private boolean CommonGoods;
    @ApiModelProperty("商品id")
    private Integer goodsId;
    @ApiModelProperty("不存在的激活码")
    private List<CheckCodesVo> notExistCodes;
    @ApiModelProperty("生成状态的激活码")
    private List<CheckCodesVo> generateCodes;
    @ApiModelProperty("出库状态的激活码")
    private List<CheckCodesVo> outCodes;
    @ApiModelProperty("激活状态的激活码")
    private List<CheckCodesVo> activeCodes;
    @ApiModelProperty("用完状态的激活码")
    private List<CheckCodesVo> runOutCodes;
    @ApiModelProperty("过期状态的激活码")
    private List<CheckCodesVo> pastCodes;
    @ApiModelProperty("退货状态的激活码")
    private List<CheckCodesVo> returnCodes;
    @ApiModelProperty("作废状态的激活码")
    private List<CheckCodesVo> obsoleteCodes;
}
