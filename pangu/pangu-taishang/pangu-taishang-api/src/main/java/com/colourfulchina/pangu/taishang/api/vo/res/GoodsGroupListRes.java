package com.colourfulchina.pangu.taishang.api.vo.res;

import com.colourfulchina.pangu.taishang.api.dto.BlockRule;
import com.colourfulchina.pangu.taishang.api.vo.GroupProductVo;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 商品下的产品组列表查询出参
 */
@Data
@Api("商品下的产品组列表查询出参")
public class GoodsGroupListRes implements Serializable {
    private static final long serialVersionUID = -8056524946816024930L;

    @ApiModelProperty("产品组id")
    private Integer id;
    @ApiModelProperty("产品组名称")
    private String name;
    @ApiModelProperty("产品组内部名称")
    private String shortName;
    @ApiModelProperty("使用限制id")
    private String useLimitId;
    @ApiModelProperty("使用数量")
    private BigDecimal useNum;
    @ApiModelProperty("折扣比例")
    private BigDecimal discountRate;
    @ApiModelProperty("block规则")
    private String blockRule;
    @ApiModelProperty("资源类型")
    private String service;
    @ApiModelProperty("权益类型")
    private String gift;
    @ApiModelProperty("权益类型code（目前仅限于住宿）")
    private String giftCode;
    @ApiModelProperty("资源类型列表")
    private List<String> serviceList;
    @ApiModelProperty("权益类型列表")
    private List<String> giftList;
    @ApiModelProperty("block规则对象列表")
    private List<BlockRule> blockRuleList;
    @ApiModelProperty("产品组的产品列表")
    private List<GroupProductVo> groupProductList;
    @ApiModelProperty("产品组的产品Ids")
    private String groupProductListIds;
    @ApiModelProperty("未使用次数")
    private Integer totalCount;
    @ApiModelProperty("已使用次数")
    private Integer useCount;
    @ApiModelProperty("免费次数")
    private Integer freeCount;
    @ApiModelProperty("最小提前预约时间")
    private Integer minBookDays;
    @ApiModelProperty("最大提前预约时间")
    private Integer maxBookDays;
    @ApiModelProperty("原产品组id")
    private Integer oldId;
    @ApiModelProperty("产品使用率")
    private BigDecimal useRate;
    @ApiModelProperty("商户类型")
    private String shopType;
    @ApiModelProperty("周期重复时间")
    private Integer cycleTime;
    @ApiModelProperty("周期类型（0-天 1-周 2-月 3-年）")
    private Integer cycleType;
    @ApiModelProperty("周期重复数量")
    private Integer cycleNum;
    @ApiModelProperty("使用类型（0-次数  1-点数）")
    private Integer useType;
    @ApiModelProperty("商品id")
    private Integer goodsId;
    @ApiModelProperty("周期循环开始时间")
    private String startTime;
    @ApiModelProperty("周期循环结束时间")
    private String endTime;


    public String getGroupProductListIds() {

       if(this.groupProductList != null  && this.groupProductList.size()>0){
           List<Integer> idList = this.groupProductList.stream().map(pro-> pro.getProductId()).collect(Collectors.toList());
          return  Joiner.on(",").join(idList);
       }else{
           return null;
       }
    }

    public List<String> getServiceList() {
        List<String> serList = Lists.newLinkedList();
        if (StringUtils.isNotBlank(this.service)){
            serList= Arrays.asList(this.service.split(" "));
//            for (String string : strings) {
////                serList.add(string);
////            }
        }
        return serList;
    }

    public List<String> getGiftList() {
        List<String> gList = Lists.newLinkedList();
        if (StringUtils.isNotBlank(this.gift)){
            gList= Arrays.asList( this.gift.split(" "));
        }
        return gList;
    }
}
