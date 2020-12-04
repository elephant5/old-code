package com.colourfulchina.bigan.api.vo;

import com.colourfulchina.bigan.api.entity.ShopItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@ApiModel
@Data
public class GoodsDetailVo implements Serializable {
    private static final long serialVersionUID = -1;

    private Long goodsId;
    private String type;
    private String typeCode;
    private String gift;
    private String giftCode;
    private String giftName;
    private Long shopId;
    private String title;
    private String clause;
    private String city;
    private String py; //城市拼音
    private String hotel; //酒店名称
    private String shopName;//商户name
    private String shopTitle;//shop表的title:商户名称
    private String address; //商户地址
    private String summary;
    private String items;
    private String blockCode;
    private BigDecimal weight;
    private List<String> imgList;
    private List<String> defaultShopImgList;
    private List<ShopItem> itemList;
    private BigDecimal minPrice;
    private List<ItemNameVo> itemString;
    private Long groupId;
    @ApiModelProperty(value = "住宿类:房型 | 床型 | 是否包含早餐")
    private String houseType;

    //住宿几晚
    private Integer nights;

    //是否为境外酒店:0:否;1:是
    private String oversea;

    //排序值
    private Integer sort;

    //查询条件
//    @ApiModelProperty(hidden = true)
//    private List<PrjGroupGoods> prjGroupGoodsList;

}
