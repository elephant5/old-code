package com.colourfulchina.pangu.taishang.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.colourfulchina.inf.base.vo.PageVo;
import com.colourfulchina.pangu.taishang.api.entity.ProductGroup;
import com.colourfulchina.pangu.taishang.api.entity.Shop;
import com.colourfulchina.pangu.taishang.api.entity.SysService;
import com.colourfulchina.pangu.taishang.api.vo.req.GroupPageReq;
import com.colourfulchina.pangu.taishang.api.vo.req.QueryProductGroupInfoReqVo;
import com.colourfulchina.pangu.taishang.api.vo.req.SelectBookShopItemReq;
import com.colourfulchina.pangu.taishang.api.vo.req.SelectShopByGroupServiceReq;
import com.colourfulchina.pangu.taishang.api.vo.res.*;
import com.colourfulchina.pangu.taishang.api.vo.res.bigan.PrjGroupGoods;
import com.colourfulchina.pangu.taishang.api.vo.res.bigan.PrjGroupVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ProductGroupMapper extends BaseMapper<ProductGroup> {

    /**
     * 产品组分页查询
     * @param pageReq
     * @param map
     * @return
     */
    List<GroupPageRes> selectPageList(PageVo<GroupPageReq> pageReq, Map map);

    /**
     * 产品组详情
     * @param id
     * @return
     */
    GroupDetailRes queryGroupDetail(Integer id);

    /**
     * 查询商品下面的产品组信息
     * @param goodsId
     * @return
     */
    List<GoodsGroupListRes> selectGoodsGroup(Integer goodsId);

    /**
     * 根据产品组ids查询产品组信息
     * @param groupIds
     * @return
     */
    List<GoodsGroupListRes> selectGoodsGroupByIds(List<String> groupIds);


    /**
     * 查询产品组信息
     * @param goodsId
     * @return
     */
    List<GoodsGroupListRes> selectGoodsGroupById(Integer goodsId);

    /**
     * 根据产品组id查询产品组下的资源类型
     * @param productGroupId
     * @return
     */
    List<SysService> selectGroupService(Integer productGroupId);

    /**
     * 根据产品组和资源类型id查询商户列表
     * @param selectShopByGroupServiceReq
     * @return
     */
    List<Shop> selectShopByGroupService(SelectShopByGroupServiceReq selectShopByGroupServiceReq);

    /**
     * 根据产品组id合资源类型查询产品组产品详情
     * @param selectShopByGroupServiceReq
     * @return
     */
    List<SelectProductByGroupServiceRes> selectProductByGroupService(SelectShopByGroupServiceReq selectShopByGroupServiceReq);

    /**
     * 根据产品组id、资源类型、商户id查询规格列表
     * @param selectBookShopItemReq
     * @return
     */
    List<SelectBookShopItemRes> selectShopItemByGroupServiceShop(SelectBookShopItemReq selectBookShopItemReq);


    /**
     * 根据产品组id、资源类型、商户id查询规格列表
     * @param selectBookShopItemReq
     * @return
     */
    List<SelectBookShopItemRes> selectShopItemByGroupServiceShopPaging(PageVo<SelectBookShopItemReq> pageVoReq, Map<String,Object> condition);
    /**
     * @title:selectGoodsGroupByGoodsId
     * @Description: 根据goodsId 获取产品信息
     * @Param: [goodsId]
     * @return: java.util.List<com.colourfulchina.pangu.taishang.api.vo.res.GoodsGroupListRes>
     * @Auther: 图南
     * @Date: 2019/6/28 9:49
     */
    List<GoodsGroupListRes> selectGoodsGroupByGoodsId(Integer goodsId);


    List<ProductGroupResVO> queryProductGroupById(QueryProductGroupInfoReqVo reqVo);


    List<PrjGroupVo> selectPrjGroupList(PrjGroupVo prjGroupVo);

    //查询项目
    List<PrjGroupGoods> findListByProjectIdAndGroupId(PrjGroupGoods prjGroupGoods);

    List<ProductGroup> selectDiscountByIds(List<Integer> groups);
}
