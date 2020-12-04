package com.colourfulchina.mars.api.vo.res;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class GiftTimesVo implements Serializable {
    private Integer id;
    private Integer times;
    private String limit_name;
    private String limit_code;
    private Integer prv_cost;
    private String name;
    private Integer pub_cost;
    private List<String> giftList;
    private String type;
}
