package com.colourfulchina.mars.api.vo;

import com.baomidou.mybatisplus.annotations.TableField;
import com.colourfulchina.pangu.taishang.api.vo.req.GoodsBaseVo;
import com.colourfulchina.pangu.taishang.api.vo.res.GoodsGroupListRes;
import lombok.Data;

import java.util.*;
import java.io.Serializable;

/**
 * 根据手机号或者激活码匹配的权益列表
 */
@Data
public class EquityListVo extends BaseModelVo {
    private static final long serialVersionUID = 2219978814652401220L;

    private String code;

    private String goodsName;

    private String goodsShortName;

    private String codeStatus;

    private String codeStatusName;

    private String memberName;

    private String memberPhone;

    private String buyMemberName;

    private String buyMemberPhone;

    private String buyMembersex;

    private String goodsExpiryValue;
    private String expiryDate;
    private String actOutDate;
    private String actRule;

    private String actCodeTime;

    private String creatDate;
    private Integer totalCount;
    private Integer useCount;
    private String channel;
    private String tags;
    private  GoodsBaseVo goodsBaseVo;

    private Boolean isChoose = false;//是否被选中，用于页面操作

    private List<GoodsGroupListRes> goodsGroupListRes;




}
