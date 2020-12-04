package com.colourfulchina.pangu.taishang.api.vo.res.bigan;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PrjGroupVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long projectId;
    private Long dynamic;
    private String filter;
    private String limitType;
    private String title;
    private Long times;
    //内部备注
    private String notes;
    private String define;
    private String more;
    private List<GoodsDetailVo> goodsDetailVoList;
}
