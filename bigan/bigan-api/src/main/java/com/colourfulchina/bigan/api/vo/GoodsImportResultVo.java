package com.colourfulchina.bigan.api.vo;

import com.colourfulchina.inf.base.vo.CommonResultVo;
import lombok.Data;

import java.util.Map;

@Data
public class GoodsImportResultVo extends CommonResultVo<Map> {

    private static final long serialVersionUID = 6443999154728803215L;
    private Map<String,String> result;

}
