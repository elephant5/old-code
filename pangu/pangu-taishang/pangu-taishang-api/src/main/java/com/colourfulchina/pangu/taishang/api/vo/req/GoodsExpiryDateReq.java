package com.colourfulchina.pangu.taishang.api.vo.req;

import com.colourfulchina.pangu.taishang.api.dto.BlockRule;
import com.colourfulchina.pangu.taishang.api.entity.Goods;
import com.colourfulchina.pangu.taishang.api.entity.GoodsClause;
import com.colourfulchina.pangu.taishang.api.entity.GoodsPrice;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Api("到期日期")
public class GoodsExpiryDateReq implements Serializable {


    private static final long serialVersionUID = 6285991198692091120L;
    @ApiModelProperty("商品ID")
    private Integer goodsId ;

    @ApiModelProperty("激活时间")
    private Date activeDay;

    @ApiModelProperty("出库时间")
    private Date outDate;


}
