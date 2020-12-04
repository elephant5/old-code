package com.colourfulchina.pangu.taishang.api.dto;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.*;
import java.io.Serializable;

@Data
public class SalesChannelVo  implements Serializable  {
    private static final long serialVersionUID = -5450327235791098363L;

    private String value ;
    private String label;

    private Set<SalesChannelVo> children;
}
