package com.colourfulchina.pangu.taishang.api.dto;

import lombok.Data;

@Data
public class BookNum {
    /**
     * 最小提前预约时间
     */
    public int minBook;
    /**
     * 最大提前预约时间
     */
    public int maxBook;
}
