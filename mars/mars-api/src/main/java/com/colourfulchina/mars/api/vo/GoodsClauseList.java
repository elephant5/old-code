package com.colourfulchina.mars.api.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Nickal
 * @Description:
 * @Date: 2019/5/17 9:56
 */
@Data
public class GoodsClauseList implements Serializable {

    private static final long serialVersionUID = -4288897791459186464L;

    private Integer goodsId;

    //限制详情
    private  String clause;

    //限制类型
    private String clauseType;

    //限制名称
    private String clauseTypeName;

}