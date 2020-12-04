package com.colourfulchina.mars.api.vo;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.io.Serializable;

/**
 * 公共返回的属性
 */
@Slf4j
@Data
public class BaseModelVo implements Serializable {
    private static final long serialVersionUID = -244366543422857950L;

    private Integer  goodsId; //商品ID（项目ID）

    private Integer shopId; //商户ID

    private Integer shopItemId;//

    private Integer productId;//产品ID

    private Long memberId;

    private Long buyMemberId;//购买人会员ID

    private Integer giftCodeId; //激活码ID



}
