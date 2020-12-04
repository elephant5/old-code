package com.colourfulchina.mars.api.vo;

import com.colourfulchina.mars.api.entity.BoscBank;
import lombok.Data;

import java.util.List;

/**
 * @Author: 罗幸
 * @Description: 套卡信息
 * @Date: 2020/3/31 10:41
 */
@Data
public class BoscCardSet {
    /**
     * 主卡信息
     */
    private BoscBank masterCard;

    /**
     * 副卡信息
     */
    private List<BoscBank> subCards;
}
