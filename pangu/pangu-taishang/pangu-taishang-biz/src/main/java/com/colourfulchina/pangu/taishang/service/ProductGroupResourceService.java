package com.colourfulchina.pangu.taishang.service;

import com.baomidou.mybatisplus.service.IService;
import com.colourfulchina.pangu.taishang.api.entity.ProductGroupResource;
import io.swagger.models.auth.In;

import java.util.List;

public interface ProductGroupResourceService extends IService<ProductGroupResource> {

    /**
     * 产品组对应的资源类型存储入库
     * @param service
     * @param productGroupId
     * @return
     */
    List<ProductGroupResource> storage(String[] service, Integer productGroupId, Integer goodsId) throws Exception;
    /**
     * 查询产品组与资源类型数据
     * @param productGroupId
     * @return
     */
    List<ProductGroupResource> selectByProductGroupId(Integer productGroupId);
    /**
     * 查询商品与资源类型数据
     * @param goodsId
     * @return
     */
    List<ProductGroupResource> selectByGoodsId(Integer goodsId);
}
