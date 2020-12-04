package com.colourfulchina.bigan.api.dto;

import com.colourfulchina.bigan.api.vo.GoodsImportVo;
import com.google.common.collect.Maps;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
public class GoodsInfo implements Serializable {
    //酒店中文名
    private String hotelCh;
    //餐厅中文名
    private String shopCh;
    //权益项目
    private String gift;

    //餐段
    private String mealSelection;
    //
    private String weekBlock;

    private String clauseBlock;
    //权益项目

    private Map<String,Object> rows = new HashMap<>(0);

    /**
     * 自助餐：餐型-定制套餐、下午茶：套餐名称，住宿：餐型）
     */
    private Set<String> meatName = new HashSet<>(0);

    private Map<String,Object> bedTypeMap = new HashMap<>(0);

    /**
     * 类型：自助餐，住宿等。。。。
     */
    private String serviceCode;

    /**
     * 类型：自助餐，住宿等。。。。
     */
    private String serviceName;

    /**
     *房型
     */
    private String roomType;

    private String giftCode;//折扣卷

    private GoodsImportVo goodsImportVo;
//存放餐段
    private Map<String,String> weekBlockMap = new HashMap<>(0);
    @Override
    public String toString() {
        StringBuffer key =new StringBuffer(hotelCh );
        if( this.serviceCode.equals("setmenu") ||  this.serviceCode.equals("tea")||  this.serviceCode.equals("spa")||this.serviceCode.equals("gym")){
            key.append("-"+shopCh+"-"+giftCode);
        }
        if( this.serviceCode.equals("buffet")){
            key.append("-"+shopCh+"-"+giftCode);
        }
        if( this.serviceCode.equals("accom")){
            key.append("-"+roomType);
        }
        if(this.serviceCode.equals("drink")){
            key.append("-"+shopCh+"-"+serviceCode);
        }

        return key.toString();
    }
}
