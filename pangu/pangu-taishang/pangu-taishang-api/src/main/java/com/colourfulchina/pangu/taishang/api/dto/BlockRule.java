package com.colourfulchina.pangu.taishang.api.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class BlockRule implements Serializable {
    //时间段始
    //阳历yyyy/mm/dd或mm/dd
    //阴历Cyyyy/mm/dd或Cmm/dd
    private String start;
    //时间段止
    //阳历yyyy/mm/dd或mm/dd
    //阴历Cyyyy/mm/dd或Cmm/dd
    private String end;
    //blockrule
    private String rule;
    //自然语言表示
    private String natural;
    //是否为关
    private Boolean close;
    //类型：1.周 2.节假日 3.阴历日期 4.阳历日期
    private Integer type;
    /**
     * block原因
     */
     private String reason;
}
