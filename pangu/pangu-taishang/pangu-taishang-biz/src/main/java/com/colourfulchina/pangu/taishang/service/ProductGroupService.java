package com.colourfulchina.pangu.taishang.service;

import com.baomidou.mybatisplus.service.IService;
import com.colourfulchina.inf.base.vo.PageVo;
import com.colourfulchina.pangu.taishang.api.dto.BlockRule;
import com.colourfulchina.pangu.taishang.api.entity.ProductGroup;
import com.colourfulchina.pangu.taishang.api.entity.ShopItemNetPriceRule;
import com.colourfulchina.pangu.taishang.api.entity.ShopItemSettlePriceRule;
import com.colourfulchina.pangu.taishang.api.entity.SysService;
import com.colourfulchina.pangu.taishang.api.vo.GroupProductVo;
import com.colourfulchina.pangu.taishang.api.vo.ProductGroupBlockRuleVo;
import com.colourfulchina.pangu.taishang.api.vo.req.*;
import com.colourfulchina.pangu.taishang.api.vo.res.*;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface ProductGroupService extends IService<ProductGroup> {

    /**
     * 新增产品组
     * @param groupSaveReq
     * @return
     */
    ProductGroup save(GroupSaveReq groupSaveReq) throws Exception;

    /**
     * 根据id查询产品组
     * @param productGroupId
     * @return
     * @throws Exception
     */
    GroupQueryOneRes findOneById(Integer productGroupId)throws Exception;

    /**
     * 编辑产品组
     * @param groupUpdReq
     * @return
     */
    ProductGroup updGroup(GroupUpdReq groupUpdReq) throws Exception;

    /**
     * 删除产品组
     * @param id
     * @return
     */
    Boolean delGroup(Integer id) throws Exception;

    /**
     * 产品组分页查询
     * @param pageReq
     * @return
     * @throws Exception
     */
    PageVo<GroupPageRes> selectPageList(PageVo<GroupPageReq> pageReq) throws Exception;

    /**
     * 产品组详情查询
     * @param id
     * @return
     * @throws Exception
     */
    GroupDetailRes groupDetail(Integer id) throws Exception;

    /**
     * 复制产品组
     * @param groupCopyReq
     * @return
     * @throws Exception
     */
    void copyGroup(GroupCopyReq groupCopyReq) throws Exception;

    /**
     * 产品组添加产品
     * @param groupAddProductReq
     * @throws Exception
     */
    void groupAddProduct(GroupAddProductReq groupAddProductReq)throws Exception;
    /**
     * 产品组添加产品
     * @param groupAddProductReq
     * @throws Exception
     */
    void groupAddProductAndBlock(GroupAddProductAndBlockReq groupAddProductReq)throws Exception;

    /**
     * 产品组移除产品
     * @param groupDelProductReq
     * @throws Exception
     */
    void groupDelProduct(GroupDelProductReq groupDelProductReq)throws Exception;

    /**
     * 产品组产品编辑
     * @param groupEditProductReq
     * @return
     * @throws Exception
     */
    GroupEditProductReq groupEditProduct(GroupEditProductReq groupEditProductReq)throws Exception;

    /**
     * 添加产品组block
     * @param productGroupBlockRuleVo
     * @throws Exception
     */
    List<BlockRule> addGroupBlock(ProductGroupBlockRuleVo productGroupBlockRuleVo)throws Exception;

    /**
     * 编辑产品组block
     * @param productGroupBlockEditReq
     * @return
     * @throws Exception
     */
    List<BlockRule> editGroupBlock(ProductGroupBlockEditReq productGroupBlockEditReq)throws Exception;

    /**
     * 查询商品下面的产品组信息
     * @param goodsId
     * @return
     * @throws Exception
     */
    List<GoodsGroupListRes> selectGoodsGroup(Integer goodsId) throws Exception;

    /**
     * 根据产品组ids查询产品组信息
     * @param groupIds
     * @return
     * @throws Exception
     */
    List<GoodsGroupListRes> selectGoodsGroup(List<String> groupIds) throws Exception;

    /**
     * 根据产品组id查询产品组下的资源类型
     * @param productGroupId
     * @return
     * @throws Exception
     */
    List<SysService> selectGroupService(Integer productGroupId) throws Exception;

    /**
     * 根据产品组和资源类型id查询商户列表
     * @param selectShopByGroupServiceReq
     * @return
     * @throws Exception
     */
    List<SelectShopByGroupServiceRes> selectShopByGroupService(SelectShopByGroupServiceReq selectShopByGroupServiceReq) throws Exception;

    /**
     * 根据产品组id合资源类型查询产品组产品详情
     * @param selectShopByGroupServiceReq
     * @return
     * @throws Exception
     */
    List<SelectProductByGroupServiceRes> selectProductByGroupService(SelectShopByGroupServiceReq selectShopByGroupServiceReq)throws Exception;

    /**
     * 预约时选择资源的列表查询
     * @param selectBookShopItemReq
     * @return
     * @throws Exception
     */
    List<SelectBookShopItemRes> selectBookShopItem(SelectBookShopItemReq selectBookShopItemReq)throws Exception;

    /**
     * 预约获取商户结算信息
     * @param shopSettleMsgReq
     * @return
     * @throws Exception
     */
    ShopSettleMsgRes shopSettleMsg(ShopSettleMsgReq shopSettleMsgReq)throws Exception;

    /**
     * 预约时间对应的结算规则
     * @param bookDate
     * @param ruleList
     * @return
     * @throws Exception
     */
    ShopItemSettlePriceRule foundRuleByTime(Date bookDate, List<ShopItemSettlePriceRule> ruleList)throws Exception;

    /**
     * 查询商品下的产品组关联信息
     * @param goodsId
     * @return
     */
    List<ProductGroup> selectByGoodsId(Integer goodsId);

   void saveSort(List<GroupProductVo> groupProductVoList);

    List<GoodsGroupListRes> selectGoodsGroupById(Integer groupById) throws Exception;

    List<GoodsGroupListRes> selectGoodsGroupByGoodsId(Integer goodsId);

    /**
     * 根据产品组id查询产品组下商户和规格
     * @param productGroupId
     * @return
     */
    List<ProductGroupResVO> selectProductGroupById(QueryProductGroupInfoReqVo reqVo);

    /**
     * 预约时选择资源的列表查询 分页
     * @param selectBookShopItemReq
     * @return
     * @throws Exception
     */
    PageVo<SelectBookShopItemRes> selectBookShopItemPaging(PageVo<SelectBookShopItemReq>  pageVo)throws Exception;

    /**
     * 根据产品组id查询产品组信息
     * @param productGroupId
     * @return
     * @throws Exception
     */
    GoodsGroupListRes findByGroupId(Integer productGroupId)throws Exception;

    /**
     * 根据产品组id列表查询折扣比例
     * @param groups
     * @return
     * @throws Exception
     */
    List<ProductGroup> selectDiscountByIds(List<Integer> groups)throws Exception;
}
