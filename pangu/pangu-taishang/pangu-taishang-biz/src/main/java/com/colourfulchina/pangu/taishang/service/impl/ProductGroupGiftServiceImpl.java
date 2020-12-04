package com.colourfulchina.pangu.taishang.service.impl;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.god.door.api.util.SecurityUtils;
import com.colourfulchina.pangu.taishang.api.entity.ProductGroupGift;
import com.colourfulchina.pangu.taishang.api.enums.DelFlagEnums;
import com.colourfulchina.pangu.taishang.mapper.ProductGroupGiftMapper;
import com.colourfulchina.pangu.taishang.service.ProductGroupGiftService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductGroupGiftServiceImpl extends ServiceImpl<ProductGroupGiftMapper,ProductGroupGift> implements ProductGroupGiftService {
    @Autowired
    private ProductGroupGiftMapper productGroupGiftMapper;

    /**
     * 产品组对应的权益类型存储入库
     * @param gift
     * @param productGroupId
     * @return
     */
    @Override
    @Transactional(rollbackFor=Exception.class)
    public List<ProductGroupGift> storage(String[] gift, Integer productGroupId) throws Exception {
        //列出所有该产品组的权益类型（包括已删除的）
        Wrapper wrapper = new Wrapper() {
            @Override
            public String getSqlSegment() {
                return "where product_group_id ="+productGroupId;
            }
        };
        List<ProductGroupGift> list = productGroupGiftMapper.selectList(wrapper);
        //库里没有该产品组的权益类型
        if (CollectionUtils.isEmpty(list)){
            if (ArrayUtils.isNotEmpty(gift)){
                for (String s : gift) {
                    ProductGroupGift productGroupGift = new ProductGroupGift();
                    productGroupGift.setProductGroupId(productGroupId);
                    productGroupGift.setGift(s);
                    productGroupGift.setDelFlag(DelFlagEnums.NORMAL.getCode());
                    productGroupGift.setCreateUser(SecurityUtils.getLoginName());
                    productGroupGiftMapper.insert(productGroupGift);
                }
            }
        }else {
            //修改后没有该产品组的权益类型
            if (ArrayUtils.isEmpty(gift)){
                for (ProductGroupGift productGroupGift : list) {
                    productGroupGift.setDelFlag(DelFlagEnums.DELETE.getCode());
                    productGroupGiftMapper.updateById(productGroupGift);
                }
            }else {
                //两者都存在时
                List<String> giftList  =Arrays.asList(gift);
                //首先将库里多余的权益类型删除以及库里被软删除的恢复
                for (ProductGroupGift productGroupGift : list) {
                    if(!giftList.contains(productGroupGift.getGift()) && productGroupGift.getDelFlag().compareTo(DelFlagEnums.NORMAL.getCode()) == 0 ){
                        productGroupGift.setDelFlag(DelFlagEnums.DELETE.getCode());
                        productGroupGiftMapper.updateById(productGroupGift);
                    }
                    if(giftList.contains(productGroupGift.getGift()) && productGroupGift.getDelFlag().compareTo(DelFlagEnums.DELETE.getCode()) == 0 ){
                        productGroupGift.setDelFlag(DelFlagEnums.NORMAL.getCode());
                        productGroupGiftMapper.updateById(productGroupGift);
                    }
//                    boolean flag = false;
//                    for (String g : gift) {
//                        if (productGroupGift.getGift().equals(g)){
//                            flag = true;
//                            break;
//                        }
//                    }
//                    if (flag == false && productGroupGift.getDelFlag().compareTo(DelFlagEnums.NORMAL.getCode()) == 0){
//                        productGroupGift.setDelFlag(DelFlagEnums.DELETE.getCode());
//                        productGroupGiftMapper.updateById(productGroupGift);
//                    }else {
////                        if (productGroupGift.getDelFlag().compareTo(DelFlagEnums.DELETE.getCode()) == 0){
////                            productGroupGift.setDelFlag(DelFlagEnums.NORMAL.getCode());
////                            productGroupGiftMapper.updateById(productGroupGift);
////                        }
//                    }

                }
                List<String> giftListOld  =list.stream().map(obj -> obj.getGift()).collect(Collectors.toList());
                for (String str : giftList) {
                    if (!giftListOld.contains(str)) {
                        ProductGroupGift productGroupGift = new ProductGroupGift();
                        productGroupGift.setProductGroupId(productGroupId);
                        productGroupGift.setGift(str);
                        productGroupGift.setDelFlag(DelFlagEnums.NORMAL.getCode());
                        productGroupGift.setCreateUser(SecurityUtils.getLoginName());
                        productGroupGiftMapper.insert(productGroupGift);
                    }
                }
                //再将库里少的权益插入
//                for (String g : gift) {
//                    boolean flag = false;
//                    for (ProductGroupGift productGroupGift : list) {
//                        if (g.equals(productGroupGift.getGift())){
//                            flag = true;
//                            break;
//                        }
//                    }
//                    if (flag == false){
//                        ProductGroupGift productGroupGift = new ProductGroupGift();
//                        productGroupGift.setProductGroupId(productGroupId);
//                        productGroupGift.setGift(g);
//                        productGroupGift.setDelFlag(DelFlagEnums.NORMAL.getCode());
//                        productGroupGift.setCreateUser(SecurityUtils.getLoginName());
//                        productGroupGiftMapper.insert(productGroupGift);
//                    }
//                }
            }
        }
        Wrapper res = new Wrapper() {
            @Override
            public String getSqlSegment() {
                return "where del_flag = 0 and product_group_id ="+productGroupId;
            }
        };
        List<ProductGroupGift> result = productGroupGiftMapper.selectList(res);
        return result;
    }

    /**
     * 根据产品组，查询产品在对应的权益类型
     *
     * @param oldproductGroup
     * @return
     */
    @Override
    public List<ProductGroupGift> selectByProductGroupId(Integer oldproductGroup) {
        Wrapper<ProductGroupGift> localWrapper = new Wrapper<ProductGroupGift>() {
            @Override
            public String getSqlSegment() {
                return "where del_flag = 0 and product_group_id = " + oldproductGroup;
            }
        };
        return productGroupGiftMapper.selectList(localWrapper);
    }
}
