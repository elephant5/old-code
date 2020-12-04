package com.colourfulchina.mars.api.vo.res;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.colourfulchina.mars.api.entity.ReservOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ReservOrderResVO extends ReservOrder {

    @ApiModelProperty(value = "第三方提货请求号")
    private String thirdCodeNum;

    @ApiModelProperty(value = "商品券号；如果商品类型是短接，该字段存放短链接；商品类型是卡密，该字段存放卡券号 -> 券码密码")
    private String thirdCodePassword;

    @ApiModelProperty(value = "商品类型；卡密；短链" )
    private String couponsType;

}
