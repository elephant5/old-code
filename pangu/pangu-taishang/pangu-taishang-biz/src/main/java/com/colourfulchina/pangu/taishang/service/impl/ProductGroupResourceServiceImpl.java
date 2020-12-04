package com.colourfulchina.pangu.taishang.service.impl;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.god.door.api.util.SecurityUtils;
import com.colourfulchina.pangu.taishang.api.entity.ProductGroupGift;
import com.colourfulchina.pangu.taishang.api.entity.ProductGroupResource;
import com.colourfulchina.pangu.taishang.api.enums.DelFlagEnums;
import com.colourfulchina.pangu.taishang.mapper.ProductGroupResourceMapper;
import com.colourfulchina.pangu.taishang.service.ProductGroupResourceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductGroupResourceServiceImpl extends ServiceImpl<ProductGroupResourceMapper,ProductGroupResource> implements ProductGroupResourceService {
    @Autowired
    private ProductGroupResourceMapper productGroupResourceMapper;

    /**
     * 产品组对应的资源类型存储入库
     * @param service
     * @param productGroupId
     * @return
     */
    @Override
    @Transactional(rollbackFor=Exception.class)
    public List<ProductGroupResource> storage(String[] service, Integer productGroupId, Integer goodsId) throws Exception {
        //列出所有该产品组的资源类型（包括已删除的）
        Wrapper wrapper = new Wrapper() {
            @Override
            public String getSqlSegment() {
                return "where product_group_id ="+productGroupId;
            }
        };
        List<ProductGroupResource> list = productGroupResourceMapper.selectList(wrapper);
        //库里没有该产品组的资源类型
        if (CollectionUtils.isEmpty(list)){
            if (ArrayUtils.isNotEmpty(service)){
                for (String s : service) {
                    ProductGroupResource productGroupResource = new ProductGroupResource();
                    productGroupResource.setProductGroupId(productGroupId);
                    productGroupResource.setService(s);
                    productGroupResource.setGoodsId(goodsId);
                    productGroupResource.setDelFlag(DelFlagEnums.NORMAL.getCode());
                    productGroupResource.setCreateUser(SecurityUtils.getLoginName());
                    productGroupResourceMapper.insert(productGroupResource);
                }
            }
        }else {
            //修改后没有该产品组的资源类型
            if (ArrayUtils.isEmpty(service)){
                for (ProductGroupResource productGroupResource : list) {
                    productGroupResource.setDelFlag(DelFlagEnums.DELETE.getCode());
                    productGroupResourceMapper.updateById(productGroupResource);
                }
            }else {
                //两者都存在时
                List<String> serviceList  = Arrays.asList(service);
                //首先将库里多余的资源类型删除以及库里被软删除的恢复
                for (ProductGroupResource productGroupResource : list) {
                    if (!serviceList.contains(productGroupResource.getService()) && productGroupResource.getDelFlag().compareTo(DelFlagEnums.NORMAL.getCode()) == 0 ) {
                        productGroupResource.setDelFlag(DelFlagEnums.DELETE.getCode());
                        productGroupResourceMapper.updateById(productGroupResource);
                    }

                    if (serviceList.contains(productGroupResource.getService()) && productGroupResource.getDelFlag().compareTo(DelFlagEnums.DELETE.getCode()) == 0 ) {
                        productGroupResource.setDelFlag(DelFlagEnums.NORMAL.getCode());
                        productGroupResourceMapper.updateById(productGroupResource);
                    }
                }
                List<String> serviceListOld  =list.stream().map(obj -> obj.getService()).collect(Collectors.toList());
                for (String str : serviceList) {
                    if (!serviceListOld.contains(str)) {
                        ProductGroupResource productGroupResource = new ProductGroupResource();
                        productGroupResource.setProductGroupId(productGroupId);
                        productGroupResource.setService(str);
                        productGroupResource.setDelFlag(DelFlagEnums.NORMAL.getCode());
                        productGroupResource.setGoodsId(goodsId);
                        productGroupResource.setCreateUser(SecurityUtils.getLoginName());
                        productGroupResourceMapper.insert(productGroupResource);
                    }
                }
//                for (ProductGroupResource productGroupResource : list) {
//                    boolean flag = false;
//                    for (String g : service) {
//                        if (productGroupResource.getService().equals(g)){
//                            flag = true;
//                            break;
//                        }
//                    }
//                    if (flag == false && productGroupResource.getDelFlag().compareTo(DelFlagEnums.NORMAL.getCode()) == 0){
//                        productGroupResource.setDelFlag(DelFlagEnums.DELETE.getCode());
//                        productGroupResourceMapper.updateById(productGroupResource);
//                    }else {
////                        if (productGroupResource.getDelFlag().compareTo(DelFlagEnums.DELETE.getCode()) == 0){
////                            productGroupResource.setDelFlag(DelFlagEnums.NORMAL.getCode());
////                            productGroupResourceMapper.updateById(productGroupResource);
////                        }
//                    }
//                }
//                //再将库里少的资源类型插入
//                for (String g : service) {
//                    boolean flag = false;
//                    for (ProductGroupResource productGroupResource : list) {
//                        if (g.equals(productGroupResource.getService())){
//                            flag = true;
//                            break;
//                        }
//                    }
//                    if (flag == false){
//                        ProductGroupResource productGroupResource = new ProductGroupResource();
//                        productGroupResource.setProductGroupId(productGroupId);
//                        productGroupResource.setService(g);
//                        productGroupResource.setDelFlag(DelFlagEnums.NORMAL.getCode());
//                        productGroupResource.setCreateUser(SecurityUtils.getLoginName());
//                        productGroupResourceMapper.insert(productGroupResource);
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
        List<ProductGroupResource> result = productGroupResourceMapper.selectList(res);
        return result;
    }

    /**
     * 查询产品组与资源类型数据
     *
     * @param productGroupId
     * @return
     */
    @Override
    public List<ProductGroupResource> selectByProductGroupId(Integer productGroupId) {
        Wrapper<ProductGroupResource> localWrapper = new Wrapper<ProductGroupResource>() {
            @Override
            public String getSqlSegment() {
                return " where del_flag = 0 and product_group_id = " + productGroupId;
            }
        };
        return productGroupResourceMapper.selectList(localWrapper);
    }

    @Override
    public List<ProductGroupResource> selectByGoodsId(Integer goodsId) {
        Wrapper<ProductGroupResource> localWrapper = new Wrapper<ProductGroupResource>() {
            @Override
            public String getSqlSegment() {
                return " where del_flag = 0 and goods_id = " + goodsId;
            }
        };
        return productGroupResourceMapper.selectList(localWrapper);
    }

}
