package com.colourfulchina.pangu.taishang.service;

import com.baomidou.mybatisplus.service.IService;
import com.colourfulchina.god.door.api.vo.KltSysUser;
import com.colourfulchina.inf.base.vo.PageVo;
import com.colourfulchina.pangu.taishang.api.entity.ProductGroupProduct;
import com.colourfulchina.pangu.taishang.api.vo.BlockBookDaysVo;
import com.colourfulchina.pangu.taishang.api.vo.BookProductVo;
import com.colourfulchina.pangu.taishang.api.vo.GroupProductVo;
import com.colourfulchina.pangu.taishang.api.vo.req.QueryGroupProductReq;
import com.colourfulchina.pangu.taishang.api.vo.req.SelectBookShopItemReq;
import com.colourfulchina.pangu.taishang.api.vo.req.ShopListPageQueryReq;
import com.colourfulchina.pangu.taishang.api.vo.res.GroupProductExportVo;
import com.colourfulchina.pangu.taishang.api.vo.res.ShopListQueryRes;

import java.util.List;
import java.util.Map;

public interface ProductGroupProductService extends IService<ProductGroupProduct> {
    /**
     * 产品组合产品之间的关系数据
     * @param oldproductGroup
     * @return
     */
    List<ProductGroupProduct> selectByProductGroupId(Integer oldproductGroup);

    /**
     * 产品组的所属产品条件查询
     * @param queryGroupProductReq
     * @return
     * @throws Exception
     */
    List<GroupProductVo> queryGroupProduct(QueryGroupProductReq queryGroupProductReq) throws Exception;

    /**
     * 查询product_group_product最大sort
     * @return
     * @throws Exception
     */
    Integer queryMaxSort() throws Exception;

    /**
     * 更新产品组下面的产品的成本区间(根据产品组下面的产品id)
     * @param productGroupProductIds
     * @return
     * @throws Exception
     */
    List<ProductGroupProduct> updCost(List<Integer> productGroupProductIds)throws Exception;

    /**
     * 更新产品组下面的产品的成本区间(根据产品组id)
     * @param productGroupId
     * @return
     * @throws Exception
     */
    List<ProductGroupProduct> updCostByGroupId(Integer productGroupId)throws Exception;


    /**
     * 查询
     * @param productGroupId
     * @return
     * @throws Exception
     */
    public List<ProductGroupProduct> getCostByGroupId(Integer productGroupId )throws Exception;

    /**
     * 更新产品组下面的产品的成本区间(根据商品id)
     * @param goodsId
     * @return
     * @throws Exception
     */
    List<ProductGroupProduct> updCostByGoodsId(Integer goodsId)throws Exception;

    /**
     * 更新产品组下面的产品的成本区间(根据商户规格id)
     * @param shopItemId
     * @return
     * @throws Exception
     */
    List<ProductGroupProduct> updCostByShopItemId(Integer shopItemId)throws Exception;

    /**
     * 更新产品组下面的产品的成本区间(根据商户id)
     * @param shopId
     * @return
     * @throws Exception
     */
    List<ProductGroupProduct> updCostByShopId(Integer shopId)throws Exception;

    void saveSort(List<GroupProductVo> groupProductVoList);

    /**
     * 查询产品组中的产品
     * @param ids
     * @return
     */
    List<ProductGroupProduct> selectByProductIds(List<Integer> ids);

    void updateByProductIds(Map params);

    /**
     * 查询产品组下面的产品信息（商区）
     * @param pageVo
     * @return
     * @throws Exception
     */
    PageVo<ShopListQueryRes> selectGoodsListByGroupId(PageVo<ShopListPageQueryReq> pageVo)throws Exception;

    /**
     * 产品组产品导出
     * @param queryGroupProductReq
     * @return
     * @throws Exception
     */
    String exportGroupProduct(QueryGroupProductReq queryGroupProductReq, KltSysUser sysUser)throws Exception;

    /**
     * 在线预约产品列表
     * @param selectBookShopItemReq
     * @return
     * @throws Exception
     */
    List<BookProductVo> selectBookProduct(SelectBookShopItemReq selectBookShopItemReq)throws Exception;

    /**
     * 产品组产品最小最大预约天数
     * @param productGroupProductId
     * @return
     */
    BlockBookDaysVo queryBookDays(Integer productGroupProductId);

    String exportItem(boolean noAccom, List<GroupProductExportVo> list, String fileName)throws Exception;
}
