package com.colourfulchina.mars.api.vo.req;

import lombok.Data;

/**
 * @Author: luck.luo
 * @Description:
 * @Date: 2019/7/22 13:49
 */
@Data
public class BookPayReq {
    private String startDate;
    private String endDate;
    private Integer productGroupProductId;
}
