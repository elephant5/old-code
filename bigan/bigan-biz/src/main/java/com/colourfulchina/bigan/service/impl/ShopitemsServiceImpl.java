package com.colourfulchina.bigan.service.impl;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.bigan.api.dto.GoodsInfo;
import com.colourfulchina.bigan.api.entity.Shop;
import com.colourfulchina.bigan.api.entity.ShopItem;
import com.colourfulchina.bigan.enums.GoodsEnums;
import com.colourfulchina.bigan.mapper.ShopItemMapper;
import com.colourfulchina.bigan.service.ShopitemsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * User: Ryan
 * Date: 2018/8/1
 */
@Service
@Slf4j
public class ShopitemsServiceImpl extends ServiceImpl<ShopItemMapper,ShopItem> implements ShopitemsService {

    @Autowired
    private ShopItemMapper shopItemMapper;
    @Override
    public List<ShopItem> getShopitemsByShopId(Shop shop,String serviceCode){
        Wrapper<ShopItem> shopitemWrapper=new Wrapper<ShopItem>() {
            @Override
            public String getSqlSegment() {
               String str  =  "where shop_id=" +shop.getId()+" and type ='"+serviceCode+"'" ;

                return str;

            }
        };
        List<ShopItem> items = shopItemMapper.selectList(shopitemWrapper);
        return items;
    }

    @Override
    public List<ShopItem> getShopitems(Shop shop,String serviceCode,GoodsInfo goodsVo){
        Wrapper<ShopItem> shopitemWrapper=new Wrapper<ShopItem>() {
            @Override
            public String getSqlSegment() {
                String str  =  "where shop_id=" +shop.getId()+" and type ='"+serviceCode+"'" ;
                if(serviceCode.equals(GoodsEnums.service.ACCOM.getCode())){
                    str = str  + " and name ='"+goodsVo.getRoomType()+"'" ;
//                    if(StringUtils.isNotEmpty()){
//                        str = str  + " and name ='"+goodsVo.getRoomType()+"'" ;
//                    }else{
//                        str = str  + " and name ='"+goodsVo.getRoomType()+"'" ;
//                    }

//                    if(StringUtils.isNotEmpty()){
//                        str = str  + " and name ='"+goodsVo.getRoomType()+"'" ;
//                    }else{
//                        str = str  + " and name ='"+goodsVo.getRoomType()+"'" ;
//                    }
                }
                return str ;
            }
        };
        List<ShopItem> items = shopItemMapper.selectList(shopitemWrapper);
        return items;
    }

    @Override
    public List<ShopItem> selectByItemIdList(List items) {
        return shopItemMapper.selectByItemIdList(items);
    }

    /**
     * 获取数据库商户规格主键生成
     * @return
     * @throws Exception
     */
    @Override
    public Long selectShopItemSeqNextValue() throws Exception {
        return shopItemMapper.selectShopItemSeqNextValue();
    }
}
