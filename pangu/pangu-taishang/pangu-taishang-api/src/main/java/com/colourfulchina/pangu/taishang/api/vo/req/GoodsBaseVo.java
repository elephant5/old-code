package com.colourfulchina.pangu.taishang.api.vo.req;

import com.baomidou.mybatisplus.annotations.TableId;
import com.colourfulchina.pangu.taishang.api.dto.BlockRule;
import com.colourfulchina.pangu.taishang.api.dto.SysFileDto;
import com.colourfulchina.pangu.taishang.api.entity.Goods;
import com.colourfulchina.pangu.taishang.api.entity.GoodsClause;
import com.colourfulchina.pangu.taishang.api.entity.GoodsPrice;
import com.colourfulchina.pangu.taishang.api.vo.res.GoodsGroupListRes;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.*;

@Data
@Api("商品增加实体")
public class
GoodsBaseVo extends Goods {


    private static final long serialVersionUID = 4752978799289967715L;

    @ApiModelProperty("商品ID")
    private Integer goodsId ;
    @ApiModelProperty("同一时段权益不叠加")
    private String superposition ;
    @ApiModelProperty("住宿）到离店日期后才可以进行下次预约")
    private String  accomAfterLeave ;
    @ApiModelProperty("行权完毕次日才可以进行下一次预约")
    private String singleThread ;
    @ApiModelProperty("是否限制单次可预订最大间")
    private String  enableMaxNight;
    @ApiModelProperty("间夜")
    private Integer maxNight ;
    @ApiModelProperty("是否限制单次可预订最小间夜")
    private String  enableMinNight;
    @ApiModelProperty("间夜")
    private Integer minNight ;
    @ApiModelProperty("OK365（1：无限制）")
    private String allYear ;
    @ApiModelProperty("禁止来电预约")
    private String disableCall ;
    @ApiModelProperty("禁止微信统一行权")
    private String disableWechat;
    @ApiModelProperty("仅限本人使用")
    private String onlySelf;
    @ApiModelProperty("删除标识（0-正常，1-删除）")
    private Integer delFlag = 0 ;
    @ApiModelProperty("渠道ID")
    private String salesChannelId;

    @ApiModelProperty("渠道ID列表")
    private List<String> salesChannelIds;
    @ApiModelProperty("渠道Name")
    private String salesChannelName;
    @ApiModelProperty("银行ID")
    private String bankId;
    @ApiModelProperty("银行Name")
    private String bankName;
    @ApiModelProperty("银行Code")
    private String bankCode;
    @ApiModelProperty("销售方式")
    private String  salesWayId;
    @ApiModelProperty("销售方式Name")
    private String salesWayName;
    @ApiModelProperty("商品详情")
    private String detail;

    @ApiModelProperty("商品条款")
    private List<GoodsClause> goodsClauseList;
    @ApiModelProperty("商品价格")
    private List<GoodsPrice> goodsPriceList;

    @ApiModelProperty("售出限制")
    private String salesDate;
    @ApiModelProperty("激活时间范围")
    private String activeDate;
    @ApiModelProperty("激活限制")
    private String activeDay;
    @ApiModelProperty("使用限制")
    private String usedDay;
    @ApiModelProperty("使用固定日期")
    private String date;
    @ApiModelProperty("使用固定天数")
    private String days;
    @ApiModelProperty("激活时间")
    private String activeDateStr;
    @ApiModelProperty("使用固定天数")
    private String daysUse;


    private List<BlockRule> blockRuleList;
    private List<SysFileDto> fileDtoList;
    List<GoodsGroupListRes> goodsGroupListRes;//在根据mars系统新定义的

}
