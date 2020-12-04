package com.colourfulchina.pangu.taishang.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
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

public interface ProductGroupProductMapper extends BaseMapper<ProductGroupProduct> {

    /**
     * 产品组的所属产品条件查询
     * @param queryGroupProductReq
     * @return
     */
    List<GroupProductVo> queryGroupProduct(QueryGroupProductReq queryGroupProductReq);

    /**
     * 查询product_group_product最大sort
     * @return
     */
    Integer queryMaxSort();

    void updateByProductIds(Map params);

    /**
     * 查询产品组下面的产品信息（商区）
     * @param pageVo
     * @param map
     * @return
     */
    List<ShopListQueryRes> selectGoodsListByGroupId(PageVo<ShopListPageQueryReq> pageVo, Map map);

    List<GroupProductExportVo> groupProductExport(QueryGroupProductReq queryGroupProductReq);

    List<BookProductVo> selectBookProduct(SelectBookShopItemReq selectBookShopItemReq);

    BlockBookDaysVo queryBookDays(Integer productGroupProductId);

    /**
     * 更具产品组id查询商户id列表
     * @param productGroupId
     * @return
     */
    List<Integer> selectShopByGroup(Integer productGroupId);
}
