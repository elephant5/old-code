package com.colourfulchina.mars.api.vo.req;

import lombok.Data;

/**
 * 查询商品使用限制
 * @Author: luck.luo
 * @Description:
 * @Date: 2019/6/21 14:19
 */
@Data
public class GoodsSettingReq {
    private Integer goodsId;
    private Integer productGroupId;
    private Integer giftCodeId;
}
