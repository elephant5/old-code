package com.colourfulchina.mars.api.enums;

import java.util.*;
import com.google.common.collect.Lists;

/**
 * 上海银行卡产品参数
 */
public enum  BoscCardEnum {
    VISA_INFINITE_CARD("WORLD","上海银行Visa极致无限卡","1912","极致无限卡", "2801","0300001826","1"),
    DIAMOND_CREDIT_CARD("WORLD","上海银行钻石信用卡","3901","上海银行钻石信用卡", "2802","0300001522","0"),
    DIAMOND_CREDIT_CARD_TEST("WORLD","上海银行钻石信用卡（测试卡）","3902","上海银行钻石信用卡", "2802","0300001523","0"),

    UNION_PAY_DIAMOND_CARD("DIAMOND","银联钻石卡","6910","标准钻石卡", "2803","0300001691","0"),
    UNION_PAY_DIAMOND_CREDIT_CARD("DIAMOND","银联钻石信用卡","6913","标准钻石卡", "2803","0300001851","1"),
    UNION_PAY_STANDARD_DIAMOND_CREDIT_CARD("DIAMOND","银联标准钻石信用卡","6914","标准钻石卡", "2803","0300001854","1"),

    MC_DUAL_CURRENCY_PLATINUM_CARD("PLATINUM","MC双币种白金卡","3001","标准白金卡", "2804","0300001348","0"),
    MC_EMP_PLATINUM_CARD("PLATINUM","MC员工白金卡","3002","标准白金卡", "2804","0300001386","0"),
    MC_GIFT_PLATINUM_CARD("PLATINUM","MC办卡赠礼白金卡","3003","标准白金卡", "2804","0300001409","0"),
    MC_HOUSE_LOAN_PLATINUM_CARD("PLATINUM","MC白金卡房贷","3007","标准白金卡", "2804","0300001336","0");

    //type:卡片层级,name:卡产品名称,cardProgroupNo:卡产品组代码,showName:H5页面显示卡片名称,unifiedProductParamCode:统一产品参数代码; cardSet :是否为套卡产品:1:是,0,否
    private String type;
    private String name;
    private String cardProgroupNo;
    private String showName;
    private String goodsId;
    private String unifiedProductParamCode;
    private String cardSet;

    BoscCardEnum(String type, String name, String cardProgroupNo, String showName, String goodsId,String unifiedProductParamCode,String cardSet) {
        this.type = type;
        this.name = name;
        this.cardProgroupNo = cardProgroupNo;
        this.showName = showName;
        this.goodsId = goodsId;
        this.unifiedProductParamCode = unifiedProductParamCode;
        this.cardSet = cardSet;
    }

    /**
     * 根据卡产品组代码获取看产品参数
     * @param cardProgroupNo
     * @return
     */
    public static Map<String,String> getCard(String cardProgroupNo){
        Map<String,String> map = new HashMap<>();
        for(BoscCardEnum boscCardEnum:BoscCardEnum.values()){
            if(cardProgroupNo.equals(boscCardEnum.getCardProgroupNo())){
                map.put("type",boscCardEnum.getType());
                map.put("name",boscCardEnum.getName());
                map.put("showName",boscCardEnum.getShowName());
                map.put("goodsId", String.valueOf(boscCardEnum.getGoodsId()));
                map.put("unifiedProductParamCode",boscCardEnum.getUnifiedProductParamCode());
                map.put("cardSet",boscCardEnum.getCardSet());
            }
        }
        return map;
    }

    /**
     * 根据卡片层级获取卡产品组代码集合
     * @return
     */
    public static List<String> getCardProgroupNoList(String type){
        List<String> list = Lists.newArrayList();
        for(BoscCardEnum boscCardEnum:BoscCardEnum.values()){
            if(type.equals(boscCardEnum.getType())){
                list.add(boscCardEnum.getCardProgroupNo());
            }
        }
        return list;
    }
    
    public static BoscCardEnum getBoscCard(String cardProgroupNo) {
        for (BoscCardEnum bosc:BoscCardEnum.values()){
            if (bosc.getCardProgroupNo().equalsIgnoreCase(cardProgroupNo.trim())){
                return bosc;
            }
        }
        return null;
    }

    /**根据卡产品组代码获取统一产品参数代码
     *
     * @param cardProgroupNo
     * @return
     */
    public static String getUnifiedProductParamCode(String cardProgroupNo){
        for(BoscCardEnum boscCardEnum:BoscCardEnum.values()){
            if(cardProgroupNo.equals(boscCardEnum.getCardProgroupNo())){
              return boscCardEnum.getUnifiedProductParamCode();
            }
        }
        return null;
    }

    /**根据统一产品参数代码获取卡产品组代码
     *
     * @param unifiedProductParamCode
     * @return
     */
    public static String getCardProgrouoNo(String unifiedProductParamCode){
        for(BoscCardEnum boscCardEnum:BoscCardEnum.values()){
            if(unifiedProductParamCode.equals(boscCardEnum.getUnifiedProductParamCode())){
                return boscCardEnum.getCardProgroupNo();
            }
        }
        return null;
    }

    /**根据卡统一产品参数代码获取获取套卡类型
     *
     * @param unifiedProductParamCode
     * @return
     */
    public static String getCardSet(String unifiedProductParamCode){
        for(BoscCardEnum boscCardEnum:BoscCardEnum.values()){
            if(unifiedProductParamCode.equals(boscCardEnum.getUnifiedProductParamCode())){
                return boscCardEnum.getCardSet();
            }
        }
        return null;
    }

//    /**根据卡产品组代码获取获取套卡类型
//     *
//     * @param cardProgroupNo
//     * @return
//     */
//    public static String getCardSet(String cardProgroupNo){
//        for(BoscCardEnum boscCardEnum:BoscCardEnum.values()){
//            if(cardProgroupNo.equals(boscCardEnum.getCardProgroupNo())){
//                return boscCardEnum.getCardSet();
//            }
//        }
//        return null;
//    }
    
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCardProgroupNo() {
        return cardProgroupNo;
    }

    public void setCardProgroupNo(String cardProgroupNo) {
        this.cardProgroupNo = cardProgroupNo;
    }

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

    public String getUnifiedProductParamCode() {
        return unifiedProductParamCode;
    }

    public void setUnifiedProductParamCode(String unifiedProductParamCode) {
        this.unifiedProductParamCode = unifiedProductParamCode;
    }

    public String getCardSet() {
        return cardSet;
    }

    public void setCardSet(String cardSet) {
        this.cardSet = cardSet;
    }
}
