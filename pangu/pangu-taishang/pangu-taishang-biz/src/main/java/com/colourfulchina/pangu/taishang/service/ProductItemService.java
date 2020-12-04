package com.colourfulchina.pangu.taishang.service;

import com.baomidou.mybatisplus.service.IService;
import com.colourfulchina.pangu.taishang.api.entity.ProductItem;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ProductItemService extends IService<ProductItem> {

    /**
     * 生成产品子项
     * @param productId
     * @return
     * @throws Exception
     */
    List<ProductItem> generateProductItem(Integer productId,HttpServletRequest request) throws Exception;

    /**
     * 合并产品子项，生成对应的block规则
     * @param productItemList
     * @return
     */
    List<ProductItem> margeItem(List<ProductItem> productItemList)throws Exception;

    String item2Block(List<ProductItem> blockItem)throws Exception;
}
