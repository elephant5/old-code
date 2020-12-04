package com.colourfulchina.pangu.taishang.service;

import com.baomidou.mybatisplus.service.IService;
import com.colourfulchina.pangu.taishang.api.entity.ProductGroupProduct;
import com.colourfulchina.pangu.taishang.api.entity.ShopItem;
import com.colourfulchina.pangu.taishang.api.vo.ShopItemPriceVo;
import com.colourfulchina.pangu.taishang.api.vo.ShopItemSettleExpressVo;
import com.colourfulchina.pangu.taishang.api.vo.res.ShopItemPriceRes;
import com.colourfulchina.pangu.taishang.api.vo.res.ShopItemRes;
import com.colourfulchina.pangu.taishang.api.vo.res.shop.ShopListQueryResVo;
import com.colourfulchina.pangu.taishang.api.vo.res.shopItem.GoodsShopItemIdRes;
import com.colourfulchina.pangu.taishang.api.vo.res.shopItem.ShopItemConciseRes;
import com.colourfulchina.pangu.taishang.api.vo.res.shopItem.ShopItemInfoRes;
import com.colourfulchina.pangu.taishang.api.vo.res.shopItem.ShopItemSetmenuInfo;

import java.util.Date;
import java.util.List;

public interface ShopItemService extends IService<ShopItem> {
    /**
     * 根据商户id查询规格列表
     * @param shopId
     * @return
     */
    List<ShopItem> selectListByShopId(Integer shopId);

    /**
     * 新增规格
     * @param shopItem
     * @return
     * @throws Exception
     */
    ShopItem add(ShopItem shopItem)throws Exception;

    /**
     * 远程更新老系统商户规格
     * @param shopItem
     * @return
     * @throws Exception
     */
    com.colourfulchina.bigan.api.entity.ShopItem remoteUpd(ShopItem shopItem)throws Exception;

    /**
     * 解析老系统shopItem中的price字段
     * @param price
     * @return
     */
    ShopItemPriceVo analysisPrice(String price);

    /**
     * 解析老系统shopItem中的price字段中的结算规则
     * @param settleExpress
     * @return
     */
    List<ShopItemSettleExpressVo> analysisSettleExpress(String settleExpress);

    /**
     * @title:selectPriceList
     * @Description: 查询商品项目价格
     * @Param: [shopIdlist]
     * @return: java.util.List<com.colourfulchina.pangu.taishang.api.vo.res.ShopItemPriceRes>
     * @Auther: 图南
     * @Date: 2019/6/19 11:58
     */
    List<ShopItemPriceRes> selectPriceList(List<Integer> shopIdlist);

    /**
     * @title:getShopItemInfo
     * @Description: 查询商品项目列表信息
     * @Param: [shopItemIdList]
     * @return: java.util.List<com.colourfulchina.pangu.taishang.api.vo.res.ShopItemRes>
     * @Auther: 图南
     * @Date: 2019/6/19 15:35
     */
    List<ShopItemRes> getShopItemInfo(List<Integer> shopItemIdList);

    /**
     * @title:getShopItemId
     * @Description: 根据goods获取shopItemId信息
     * @Param: [goodsId]
     * @return: com.colourfulchina.pangu.taishang.api.vo.res.shopItem.GoodsShopItemIdRes
     * @Auther: 图南
     * @Date: 2019/6/20 16:32
     */
    GoodsShopItemIdRes getShopItemId(Integer goodsId);

    /**
     * @title:getItems
     * @Description: 根据goodsId批量查询items
     * @Param: [goodsIdList]
     * @return: java.util.List<com.colourfulchina.pangu.taishang.api.vo.res.shop.ShopListQueryResVo>
     * @Auther: 图南
     * @Date: 2019/6/20 16:54
     */
    List<ShopListQueryResVo> getItems(List<Integer> goodsIdList);

    /**
     * @title:selectShopItemPrice
     * @Description: 获取市场参考价
     * @Param: [itemsIdList]
     * @return: java.util.List<com.colourfulchina.pangu.taishang.api.vo.res.shopItem.ShopItemResVo>
     * @Auther: 图南
     * @Date: 2019/6/20 16:57
     */
    List<ShopItemInfoRes> selectShopItemPrice(List<Integer> itemsIdList, Date date);

    /**
     * @title:getShopItemSetmenuInfo
     * @Description: 获取商品定制套餐信息
     * @Param: [shopItemId]
     * @return: com.colourfulchina.pangu.taishang.api.vo.res.shopItem.ShopItemSetmenuInfo
     * @Auther: 图南
     * @Date: 2019/6/27 16:33
     */
    ShopItemSetmenuInfo getShopItemSetmenuInfo(Integer shopItemId);

    /**
     * 根据shopItem ids查询
     * @param items
     * @return
     * @throws Exception
     */
    List<ShopItemConciseRes> selectByItems(List<Integer> items)throws Exception;

    String export()throws Exception;

    String offShopItem()throws Exception;

    /**
     * 检查规格是否被包进商品售卖
     * @param id
     * @return
     */
    List<ProductGroupProduct> checkItemIsSale(Integer id);
}