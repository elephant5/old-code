package com.colourfulchina.pangu.taishang.service;

import com.baomidou.mybatisplus.service.IService;
import com.colourfulchina.god.door.api.vo.KltSysUser;
import com.colourfulchina.inf.base.vo.PageVo;
import com.colourfulchina.pangu.taishang.api.entity.Product;
import com.colourfulchina.pangu.taishang.api.entity.ShopItem;
import com.colourfulchina.pangu.taishang.api.vo.GroupProductVo;
import com.colourfulchina.pangu.taishang.api.vo.req.ProductPageReq;
import com.colourfulchina.pangu.taishang.api.vo.res.ProductPackPageRes;
import com.colourfulchina.pangu.taishang.api.vo.res.ProductPageRes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ProductService extends IService<Product> {

    /**
     * 根据产品组id查询产品列表
     * @return
     * @throws Exception
     */
    List<GroupProductVo> selectByGroupId(Integer groupId) throws Exception;

    /**
     *产品分页查询列表
     * @param pageReq
     * @return
     * @throws Exception
     */
    PageVo<ProductPageRes> selectPageList(PageVo<ProductPageReq> pageReq) throws Exception;

    /**
     * 打包产品分页查询列表
     * @param pageReq
     * @return
     * @throws Exception
     */
    PageVo<ProductPackPageRes> selectPackPageList(PageVo<ProductPageReq> pageReq)throws Exception;

    /**
     * 规格生成产品
     * @param shopItemId
     * @return
     * @throws Exception
     */
    List<Product> generateProduct(Integer shopItemId, HttpServletRequest request)throws Exception;

    /**
     * 住宿只有固定贴现结算规则时产品子项 及成本显示问题
     * @return
     * @throws Exception
     */
    List<Integer> discountProduct() throws Exception;

    /**
     * 停售规格删除的权益类型对应的产品
     * @param listTemp
     * @param shopItem
     */
    void updateProductStatus(List<String> listTemp, ShopItem shopItem,Integer status);

    /**
     * 产品导出
     * @param pageReq
     * @return
     */
    String exportProduct(PageVo<ProductPageReq> pageReq, KltSysUser sysUser)throws Exception;

    Integer selectNewProductId(Integer productId) throws Exception;
}
