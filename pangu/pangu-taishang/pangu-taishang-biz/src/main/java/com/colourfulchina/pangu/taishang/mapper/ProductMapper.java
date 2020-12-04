package com.colourfulchina.pangu.taishang.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.colourfulchina.inf.base.vo.PageVo;
import com.colourfulchina.pangu.taishang.api.entity.Product;
import com.colourfulchina.pangu.taishang.api.entity.ShopItem;
import com.colourfulchina.pangu.taishang.api.vo.GroupProductVo;
import com.colourfulchina.pangu.taishang.api.vo.req.ProductPageReq;
import com.colourfulchina.pangu.taishang.api.vo.res.GroupProductExportVo;
import com.colourfulchina.pangu.taishang.api.vo.res.ProductPackPageRes;
import com.colourfulchina.pangu.taishang.api.vo.res.ProductPageRes;
import com.colourfulchina.pangu.taishang.api.vo.res.bigan.BookOrderReqVo;
import com.colourfulchina.pangu.taishang.api.vo.res.bigan.GoodsDetailVo;
import com.colourfulchina.pangu.taishang.api.vo.res.bigan.PrjGroupGoods;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ProductMapper extends BaseMapper<Product> {

    /**
     * 根据产品组id查询产品列表
     * @param groupId
     * @return
     */
    List<GroupProductVo> selectByGroupId(Integer groupId);

    /**
     * 产品分页查询列表
     * @param pageReq
     * @param map
     * @return
     */
    List<ProductPageRes> selectPageList(PageVo<ProductPageReq> pageReq, Map map);

    /**
     * 产品查询列表
     * @param map
     * @return
     */
    List<ProductPageRes> selectPageList(Map map);

    /**
     * 住宿只有固定贴现结算规则时产品子项 及成本显示问题
     * @return
     */
    List<Integer> discountProduct();

    /**
     * 停售规格删除的权益类型对应的产品
     *
     */
    void updateProductStatus(Product params );

    /**
     * 产品导出
     * @param params
     * @return
     */
    List<GroupProductExportVo> selectExportList(Map params);

    List<ProductPackPageRes> selectItemPageList(Map map);

    List<ProductPackPageRes> selectItemPageList(PageVo<ProductPageReq> pageReq, Map map);

    /**
     * 查询产品详情
     * @return
     */
    List<GoodsDetailVo> queryGoodsDetail(List<PrjGroupGoods> prjGroupGoodsList);

    /**
     * 查询单个产品详情
     * @return
     */
    GoodsDetailVo getGoodsDetail(@Param("pgpId") Long pgpId);

    //查询礼品block
    List<String> queryGiftBlockList(BookOrderReqVo vo);

    //
    ShopItem queryShopItemBlock(Long pgpId);

    Integer selectNewProductId(Integer productId);
}
