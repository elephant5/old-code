package com.colourfulchina.pangu.taishang.api.vo.req;

import com.colourfulchina.pangu.taishang.api.entity.ShopItemSettlePrice;
import lombok.Data;

import java.util.Date;

@Data
public class ShopItemSettlePriceHisReq extends ShopItemSettlePrice {
    /**
     * 开始日期(年月日)
     */
    private Date startDate;
    /**
     * 结束日期(年月日 不包含)
     */
    private Date endDate;
}
