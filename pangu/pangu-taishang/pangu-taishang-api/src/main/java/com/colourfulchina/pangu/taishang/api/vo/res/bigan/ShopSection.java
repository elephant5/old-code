package com.colourfulchina.pangu.taishang.api.vo.res.bigan;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class ShopSection implements Serializable {
    private Long shopId;
    private String title;
    private String content;
}
