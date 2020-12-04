package com.colourfulchina.bigan.api.vo;

import com.colourfulchina.bigan.api.entity.AccomList;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.text.SimpleDateFormat;

@Data
public class AccomListExportVo extends AccomList {
//    @Override
//    public Long getId() {
//        return null2Empty(super.getId());
//    }

    private static final SimpleDateFormat SDF_YYYY_MM_DD=new SimpleDateFormat("YYYY-MM-DD");

    @Override
    public String getCountry() {
        return null2Empty(super.getCountry());
    }

    @Override
    public String getCountryCode() {
        return null2Empty(super.getCountryCode());
    }

    @Override
    public String getCity() {
        return null2Empty(super.getCity());
    }

//    @Override
//    public Integer getCityId() {
//        return null2Empty(super.getCityId());
//    }

    @Override
    public String getHotHotel() {
        return null2Empty(super.getHotHotel());
    }

//    @Override
//    public Integer getHot() {
//        return null2Empty(super.getHot());
//    }
//
//    @Override
//    public Integer getHotelId() {
//        return null2Empty(super.getHotelId());
//    }

    @Override
    public String getHotelCh() {
        return null2Empty(super.getHotelCh());
    }

    @Override
    public String getHotelEn() {
        return null2Empty(super.getHotelEn());
    }

    @Override
    public String getKeepRoom() {
        return null2Empty(super.getKeepRoom());
    }

    @Override
    public String getRoomType() {
        return null2Empty(super.getRoomType());
    }

    @Override
    public String getRoomTypeOld() {
        return null2Empty(super.getRoomTypeOld());
    }

    @Override
    public String getBedType() {
        return null2Empty(super.getBedType());
    }

    @Override
    public String getRestaurantType() {
        return null2Empty(super.getRestaurantType());
    }

    public String getSaleStartStr() {
        return super.getSaleEnd() == null ? "":SDF_YYYY_MM_DD.format(super.getSaleStart());
    }

    public String getSaleEndStr() {
        return super.getSaleEnd() == null ? "":SDF_YYYY_MM_DD.format(super.getSaleEnd());
    }

    @Override
    public String getCoinUnit() {
        return null2Empty(super.getCoinUnit());
    }

    @Override
    public String getChangePrice() {
        return null2Empty(super.getChangePrice());
    }

//    @Override
//    public Date getContractStart() {
//        return null2Empty(super.getContractStart());
//    }
//
//    @Override
//    public Date getContractEnd() {
//        return null2Empty(super.getContractEnd());
//    }

    @Override
    public String getBetweenPrice() {
        return null2Empty(super.getBetweenPrice());
    }

    @Override
    public String getWeekPrice() {
        return null2Empty(super.getWeekPrice());
    }

    @Override
    public String getWeekendPrice() {
        return null2Empty(super.getWeekendPrice());
    }

    @Override
    public String getLowPrice() {
        return null2Empty(super.getLowPrice());
    }

    @Override
    public String getAvgPrice() {
        return null2Empty(super.getAvgPrice());
    }

    @Override
    public String getBestPrice() {
        return null2Empty(super.getBestPrice());
    }

    @Override
    public String getAddress() {
        return null2Empty(super.getAddress());
    }

    @Override
    public String getPhone() {
        return null2Empty(super.getPhone());
    }

    @Override
    public String getUseElement() {
        return null2Empty(super.getUseElement());
    }

    @Override
    public String getRemark() {
        return null2Empty(super.getRemark());
    }

    @Override
    public String getStars() {
        return null2Empty(super.getStars());
    }

    @Override
    public String getChannel() {
        return null2Empty(super.getChannel());
    }

//    @Override
//    public Integer getChannelId() {
//        return null2Empty(super.getChannelId());
//    }

    @Override
    public String getShopName() {
        return null2Empty(super.getShopName());
    }

//    @Override
//    public Integer getShopId() {
//        return null2Empty(super.getShopId());
//    }

//    @Override
//    public Date getCreateTime() {
//        return null2Empty(super.getCreateTime());
//    }

    @Override
    public String getCreateUser() {
        return null2Empty(super.getCreateUser());
    }

//    @Override
//    public Date getUpdateTime() {
//        return null2Empty(super.getUpdateTime());
//    }

    @Override
    public String getUpdateUser() {
        return null2Empty(super.getUpdateUser());
    }

    private static String null2Empty(String input){
        return StringUtils.isBlank(input)?"":input;
    }
}
