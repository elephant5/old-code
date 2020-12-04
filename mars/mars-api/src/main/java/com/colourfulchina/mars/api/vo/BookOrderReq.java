package com.colourfulchina.mars.api.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: Nickal
 * @Description:
 * @Date: 2019/9/17 14:31
 */
@Data
  public class BookOrderReq implements Serializable {
    private static final long serialVersionUID = -2070760513087731108L;

    private String channel;//销售渠道
    private Date startDate;//订单时间段开始
    private Date endDate;//订单时间段结束
    private Integer orderId;//预约单id
    private Integer goodsId;//商品id
    private String sign;//验签
}
