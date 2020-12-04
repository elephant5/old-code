package com.colourfulchina.pangu.taishang.api.vo.res.bigan;

import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

@Data
public class GoodsBlockVo implements Serializable {
    private Integer item_id;
    private String code;
    private String block;

    public String getCode() {
        if (StringUtils.isNotBlank(block)){
            return block;
        }
        return code;
    }
}
