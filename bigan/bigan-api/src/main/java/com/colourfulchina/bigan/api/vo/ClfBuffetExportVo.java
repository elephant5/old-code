package com.colourfulchina.bigan.api.vo;

import com.colourfulchina.bigan.api.entity.ClfBuffet;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

@Data
public class ClfBuffetExportVo extends ClfBuffet {
//    @Override
//    public Long getId() {
//        return null2Empty(super.getId());
//    }

    @Override
    public String getCity() {
        return null2Empty(super.getCity());
    }

    @Override
    public String getHotelCh() {
        return null2Empty(super.getHotelCh());
    }

    @Override
    public String getHotelEn() {
        return null2Empty(super.getHotelEn());
    }

    @Override
    public String getRestaurantName() {
        return null2Empty(super.getRestaurantName());
    }

    @Override
    public String getRestaurantEn() {
        return null2Empty(super.getRestaurantEn());
    }

    @Override
    public String getRestaurantCh() {
        return null2Empty(super.getRestaurantCh());
    }

    @Override
    public String getServiceOld() {
        return null2Empty(super.getServiceOld());
    }

    @Override
    public String getService() {
        return null2Empty(super.getService());
    }

    @Override
    public String getServiceType() {
        return null2Empty(super.getServiceType());
    }

    @Override
    public String getDiscount() {
        return null2Empty(super.getDiscount());
    }

    @Override
    public String getUserName() {
        return null2Empty(super.getUserName());
    }

    @Override
    public String getPwd() {
        return null2Empty(super.getPwd());
    }

    @Override
    public String getPrice() {
        return null2Empty(super.getPrice());
    }

    @Override
    public String getMealSelectionOld() {
        return null2Empty(super.getMealSelectionOld());
    }

    @Override
    public String getWeekBlock() {
        return null2Empty(super.getWeekBlock());
    }

    @Override
    public String getWeekUse() {
        return null2Empty(super.getWeekUse());
    }

    @Override
    public String getMealSelection() {
        return null2Empty(super.getMealSelection());
    }

    @Override
    public String getNetPrice() {
        return null2Empty(super.getNetPrice());
    }

    @Override
    public String getServiceCost() {
        return null2Empty(super.getServiceCost());
    }

    @Override
    public String getRate() {
        return null2Empty(super.getRate());
    }

    @Override
    public String getTotalPrice() {
        return null2Empty(super.getTotalPrice());
    }

    @Override
    public String getCost() {
        return null2Empty(super.getCost());
    }

    @Override
    public String getEndPriceRule() {
        return null2Empty(super.getEndPriceRule());
    }

    @Override
    public String getPrepayment() {
        return null2Empty(super.getPrepayment());
    }

    @Override
    public String getCash() {
        return null2Empty(super.getCash());
    }

//    @Override
//    public Date getSignDate() {
//        return null2Empty(super.getSignDate());
//    }
//
//    @Override
//    public Date getContractEnd() {
//        return null2Empty(super.getContractEnd());
//    }

    @Override
    public String getLuchTime() {
        return null2Empty(super.getLuchTime());
    }

    @Override
    public String getDinnerTime() {
        return null2Empty(super.getDinnerTime());
    }

    @Override
    public String getUseElement() {
        return null2Empty(super.getUseElement());
    }

    @Override
    public String getPaperAndElectron() {
        return null2Empty(super.getPaperAndElectron());
    }

    @Override
    public String getElectron() {
        return null2Empty(super.getElectron());
    }

    @Override
    public String getContractNum() {
        return null2Empty(super.getContractNum());
    }

    @Override
    public String getSignUser() {
        return null2Empty(super.getSignUser());
    }

    @Override
    public String getVindicateUser() {
        return null2Empty(super.getVindicateUser());
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
    public String getParking() {
        return null2Empty(super.getParking());
    }

    @Override
    public String getKids() {
        return null2Empty(super.getKids());
    }

    @Override
    public String getRemarkInfo() {
        return null2Empty(super.getRemarkInfo());
    }

    @Override
    public String getRemark() {
        return null2Empty(super.getRemark());
    }

    @Override
    public String getForgetRemark() {
        return null2Empty(super.getForgetRemark());
    }

    @Override
    public String getOperatePro() {
        return null2Empty(super.getOperatePro());
    }

//    @Override
//    public Integer getCityId() {
//        return null2Empty(super.getCityId());
//    }
//
//    @Override
//    public Integer getHotelId() {
//        return null2Empty(super.getHotelId());
//    }
//
//    @Override
//    public Integer getShopId() {
//        return null2Empty(super.getShopId());
//    }
//
//    @Override
//    public Date getCreateTime() {
//        return null2Empty(super.getCreateTime());
//    }
//
//    @Override
//    public Date getUpdateTime() {
//        return null2Empty(super.getUpdateTime());
//    }

    @Override
    public String getCreateUser() {
        return null2Empty(super.getCreateUser());
    }

    @Override
    public String getUpdateUser() {
        return null2Empty(super.getUpdateUser());
    }

    private static String null2Empty(String input){
        return StringUtils.isBlank(input)?"":input;
    }
}
