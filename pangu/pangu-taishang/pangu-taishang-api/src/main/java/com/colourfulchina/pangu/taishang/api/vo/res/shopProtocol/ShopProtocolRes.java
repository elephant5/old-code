package com.colourfulchina.pangu.taishang.api.vo.res.shopProtocol;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ShopProtocolRes {

    private Integer id;

    private Integer channelId;

    private String currency;

    private String settleMethod;

    private Integer decimal;

    private Boolean roundup;

    private String principal;

    private BigDecimal imprest;

    private BigDecimal deposit;

    private Date contractStart;

    private Date contractExpiry;

    private String contractEffective;

    private String blockRule;

    private Integer defaultImgId;

    private String parking;

    private String children;

    private String notice;

    private String accountName;

    private String openingBank;

    private String bankAccount;

    private Integer delFlag;

    private Date createTime;

    private String createUser;

    private Date updateTime;

    private String updateUser;

    private String changePrice;

}
