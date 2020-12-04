package com.colourfulchina.pangu.taishang.controller;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.colourfulchina.god.door.api.annotation.SysGodDoorLog;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.inf.base.vo.PageVo;
import com.colourfulchina.pangu.taishang.api.dto.BlockRule;
import com.colourfulchina.pangu.taishang.api.entity.ProductGroup;
import com.colourfulchina.pangu.taishang.api.entity.ProductGroupResource;
import com.colourfulchina.pangu.taishang.api.entity.SysService;
import com.colourfulchina.pangu.taishang.api.vo.GroupProductVo;
import com.colourfulchina.pangu.taishang.api.vo.ProductGroupBlockRuleVo;
import com.colourfulchina.pangu.taishang.api.vo.req.*;
import com.colourfulchina.pangu.taishang.api.vo.res.*;
import com.colourfulchina.pangu.taishang.service.GroupProductBlockDateService;
import com.colourfulchina.pangu.taishang.service.ProductGroupProductService;
import com.colourfulchina.pangu.taishang.service.ProductGroupResourceService;
import com.colourfulchina.pangu.taishang.service.ProductGroupService;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/productGroup")
@Slf4j
@Api(tags = {"产品组操作接口"})
public class ProductGroupController {
    @Autowired
    private ProductGroupService productGroupService;
    @Autowired
    private ProductGroupProductService productGroupProductService;

    @Autowired
    private ProductGroupResourceService productGroupResourceService;
    @Autowired
    private GroupProductBlockDateService groupProductBlockDateService;

    /**
     * 新增产品组
     * @param groupSaveReq
     * @returnproductGroup
     */
    @SysGodDoorLog("新增产品组接口")
    @ApiOperation("新增产品组接口")
    @PostMapping("/save")
    @CacheEvict(value = {"ProductGroup","Goods"},allEntries = true)
    public CommonResultVo<GroupQueryOneRes> save(@RequestBody GroupSaveReq groupSaveReq){
        CommonResultVo<GroupQueryOneRes> result = new CommonResultVo<>();
        try {
            Assert.notNull(groupSaveReq.getGoodsId(),"商品id不能为空");
            Assert.hasText(groupSaveReq.getName(),"产品组名称不能为空");
            ProductGroup productGroup = productGroupService.save(groupSaveReq);
            GroupQueryOneRes res = productGroupService.findOneById(productGroup.getId());
            result.setResult(res);
        }catch (Exception e){
            log.error("产品组新增失败",e);
            result.setCode(200);
            result.setMsg("产品组新增失败");
        }
        return result;
    }

    /**
     * 根据id查询产品组
     * @param productGroupId
     * @return
     */
    @SysGodDoorLog("根据id查询产品组")
    @ApiOperation("根据id查询产品组")
    @GetMapping("/findOneById/{id}")
    public CommonResultVo<GroupQueryOneRes> findOneById(@PathVariable("id") Integer productGroupId){
        CommonResultVo<GroupQueryOneRes> result = new CommonResultVo<>();
        try {
            Assert.notNull(productGroupId,"产品组id不能为空");
            GroupQueryOneRes res = productGroupService.findOneById(productGroupId);
            result.setResult(res);
        }catch (Exception e){
            log.error("产品组查询失败",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    /**
     * 编辑产品组
     * @param groupUpdReq
     * @return productGroupUpdate
     */
    @SysGodDoorLog("编辑产品组接口")
    @ApiOperation("编辑产品组接口")
    @PostMapping("/update")
    @CacheEvict(value = {"ProductGroup","Goods"},allEntries = true)
    public CommonResultVo<GroupQueryOneRes> update(@RequestBody GroupUpdReq groupUpdReq){
        CommonResultVo<GroupQueryOneRes> result = new CommonResultVo<>();
        try {
            Assert.notNull(groupUpdReq.getId(),"产品组id不能为空");
            Assert.hasText(groupUpdReq.getName(),"产品组名称不能为空");
            ProductGroup productGroup = productGroupService.updGroup(groupUpdReq);
            GroupQueryOneRes res = productGroupService.findOneById(groupUpdReq.getId());
            result.setResult(res);
        }catch (Exception e){
            log.error("产品组编辑失败",e);
            result.setCode(200);
            result.setMsg("产品组编辑失败");
        }
        return result;
    }

    /**
     * 删除产品组
     * @param id
     * @returngroupAddProduct
     */
    @SysGodDoorLog("删除产品组接口")
    @ApiOperation("删除产品组接口")
    @PostMapping("/delete")
    @CacheEvict(value = {"ProductGroup","Goods"},allEntries = true)
    public CommonResultVo<Integer> delete(@RequestBody Integer id){
        CommonResultVo<Integer> result = new CommonResultVo<>();
        try {
            Assert.notNull(id,"产品组id不能为空");
            Boolean flag = productGroupService.delGroup(id);
//            result.setResult(flag);
            result.setResult(id);
        }catch (Exception e){
            log.error("产品组删除失败",e);
            result.setCode(200);
            result.setMsg("产品组删除失败");
        }
        return result;
    }

    /**
     * 产品组分页查询接口
     * @param pageReq
     * @return
     */
    @SysGodDoorLog("产品组分页查询接口")
    @ApiOperation("产品组分页查询接口")
    @PostMapping("/selectPage")
    public CommonResultVo<PageVo<GroupPageRes>> selectPage(@RequestBody PageVo<GroupPageReq> pageReq){
        CommonResultVo<PageVo<GroupPageRes>> result = new CommonResultVo<>();
        try {
            PageVo<GroupPageRes> pageRes = productGroupService.selectPageList(pageReq);
            result.setResult(pageRes);
        }catch (Exception e){
            log.error("产品组分页查询失败",e);
            result.setCode(200);
            result.setMsg("产品组分页查询失败");
        }
        return result;
    }

    /**
     * 产品组详情查询接口
     * @param id
     * @return
     */
    @ApiOperation("产品组详情查询接口")
    @PostMapping("/groupDetail")
    public CommonResultVo<GroupDetailRes> groupDetail(@RequestBody Integer id){
        CommonResultVo<GroupDetailRes> result = new CommonResultVo<>();
        try {
            Assert.notNull(id,"产品组id不能为空");
            GroupDetailRes res = productGroupService.groupDetail(id);
            result.setResult(res);
        }catch (Exception e){
            log.error("产品组详情查询失败",e);
            result.setCode(200);
            result.setMsg("产品组详情查询失败");
        }
        return result;
    }

    /**
     * 复制产品组接口
     * @param groupCopyReq
     * @return
     */
    @SysGodDoorLog("复制产品组接口")
    @ApiOperation("复制产品组接口")
    @PostMapping("/copyGroup")
    @CacheEvict(value = {"ProductGroup","Goods","ProductGroupProduct"},allEntries = true)
    public CommonResultVo copyGroup(@RequestBody GroupCopyReq groupCopyReq){
        CommonResultVo result = new CommonResultVo();
        try {
            Assert.notNull(groupCopyReq.getGoodsId(),"目标商品id不能为空");
            Assert.notEmpty(groupCopyReq.getGroupIds(),"源产品组不能为空");
            productGroupService.copyGroup(groupCopyReq);
        }catch (Exception e){
            log.error("产品组复制失败",e);
            result.setCode(200);
            result.setMsg("产品组复制失败");
        }
        return result;
    }

    @SysGodDoorLog("产品组添加产品接口")
    @ApiOperation("产品组添加产品接口")
    @PostMapping("/groupAddProduct")
    @CacheEvict(value = {"ProductGroup","Goods","ProductGroupProduct"},allEntries = true)
    public CommonResultVo groupAddProduct(@RequestBody GroupAddProductReq groupAddProductReq){
        CommonResultVo result = new CommonResultVo();
        try {
            Assert.notNull(groupAddProductReq.getProductGroupId(),"产品组id不能为空");
            Assert.notEmpty(groupAddProductReq.getPackProducts(),"产品列表不能为空");
            productGroupService.groupAddProduct(groupAddProductReq);
        }catch (Exception e){
            log.error("产品组添加产品失败",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    @SysGodDoorLog("产品组移除产品接口")
    @ApiOperation("产品组移除产品接口")
    @PostMapping("/groupDelProduct")
    @CacheEvict(value = {"ProductGroup","Goods"},allEntries = true)
    public CommonResultVo groupDelProduct(@RequestBody GroupDelProductReq groupDelProductReq){
        CommonResultVo result = new CommonResultVo();
        try {
            Assert.notNull(groupDelProductReq.getProductGroupId(),"产品组id不能为空");
            Assert.notEmpty(groupDelProductReq.getProductGroupProductIds(),"产品列表不能为空");
            productGroupService.groupDelProduct(groupDelProductReq);
        }catch (Exception e){
            log.error("产品组移除产品失败",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    @SysGodDoorLog("产品组产品编辑接口")
    @ApiOperation("产品组产品编辑接口")
    @PostMapping("/groupEditProduct")
    @CacheEvict(value = {"ProductGroup","Goods","ProductGroupProduct"},allEntries = true)
    public CommonResultVo<GroupEditProductReq> groupEditProduct(@RequestBody GroupEditProductReq groupEditProductReq){
        CommonResultVo<GroupEditProductReq> result = new CommonResultVo();
        List<Integer> ids = Lists.newLinkedList();
        ids.add(groupEditProductReq.getProductGroupProductId());
        try {
            Assert.notNull(groupEditProductReq.getProductGroupProductId(),"产品组和产品关系id不能为空");
            groupEditProductReq = productGroupService.groupEditProduct(groupEditProductReq);
            //产品组的产品成本价格区间更新
            productGroupProductService.updCost(ids);
            //产品组产品block日期生成
            groupProductBlockDateService.updBlockDate(ids);
            result.setResult(groupEditProductReq);
        }catch (Exception e){
            log.error("产品组产品编辑失败",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    /**
     * 添加产品组block
     * @param productGroupBlockRuleVo
     * @return
     */
    @SysGodDoorLog("添加产品组block")
    @ApiOperation("添加产品组block")
    @PostMapping("/addGroupBlock")
    @CacheEvict(value = {"ProductGroup","Goods","ProductGroupProduct"},allEntries = true)
    public CommonResultVo<List<BlockRule>> addGroupBlock(@RequestBody ProductGroupBlockRuleVo productGroupBlockRuleVo){
        CommonResultVo<List<BlockRule>> result = new CommonResultVo();
        try {
            Assert.notNull(productGroupBlockRuleVo,"参数不能为空");
            Assert.notNull(productGroupBlockRuleVo.getProductGroupId(),"产品组id不能为空");
            List<BlockRule> list = productGroupService.addGroupBlock(productGroupBlockRuleVo);
            //产品组下面的产品成本价格区间更新
            productGroupProductService.updCostByGroupId(productGroupBlockRuleVo.getProductGroupId());
            //产品组产品block日期添加
            groupProductBlockDateService.updBlockDateByGroupId(productGroupBlockRuleVo.getProductGroupId());
            result.setResult(list);
        }catch (Exception e){
            log.error("添加产品组block失败",e);
            result.setCode(200);
            result.setMsg("添加产品组block失败");
        }
        return result;
    }

    /**
     * 修改/删除产品组block
     * @param productGroupBlockEditReq
     * @return EDITGROUPBLOCK
     */
    @SysGodDoorLog("修改/删除产品组block")
    @ApiOperation("修改/删除产品组block")
    @PostMapping("/editGroupBlock")
    @CacheEvict(value = {"ProductGroup","Goods","ProductGroupProduct"},allEntries = true)
    public CommonResultVo<List<BlockRule>> editGroupBlock(@RequestBody ProductGroupBlockEditReq productGroupBlockEditReq){
        CommonResultVo<List<BlockRule>> result = new CommonResultVo<>();
        try {
            Assert.notNull(productGroupBlockEditReq.getProductGroupId(),"产品组id不能为空");
            List<BlockRule> list = productGroupService.editGroupBlock(productGroupBlockEditReq);
            //产品组下面的产品成本价格区间更新
            productGroupProductService.updCostByGroupId(productGroupBlockEditReq.getProductGroupId());
            //产品组产品block日期添加
            groupProductBlockDateService.updBlockDateByGroupId(productGroupBlockEditReq.getProductGroupId());
            result.setResult(list);
        }catch (Exception e){
            log.error("编辑产品组block失败",e);
            result.setCode(200);
            result.setMsg("编辑产品组block失败");
        }
        return result;
    }

    /**
     * 查询商品下面的产品组信息
     * @param goodsId
     * @return
     */
    @SysGodDoorLog("查询商品下面的产品组信息")
    @ApiOperation("查询商品下面的产品组信息")
    @PostMapping("/selectGoodsGroup")
    @Cacheable(value = "ProductGroup",key = "'selectGoodsGroup_'+#goodsId",unless = "#result == null")
    public CommonResultVo<List<GoodsGroupListRes>> selectGoodsGroup(@RequestBody Integer goodsId){
        CommonResultVo<List<GoodsGroupListRes>> result = new CommonResultVo();
        try {
            Assert.notNull(goodsId,"商品id不能为空");
            List<GoodsGroupListRes> list = productGroupService.selectGoodsGroup(goodsId);
            result.setResult(list);
        }catch (Exception e){
            log.error("查询商品的产品组失败",e);
            result.setCode(200);
            result.setMsg("查询商品的产品组失败");
        }
        return result;
    }

    /**
     * 根据产品组ids查询产品组信息
     * @param groupIds
     * @return
     */
    @SysGodDoorLog("根据产品组ids查询产品组信息")
    @ApiOperation("根据产品组ids查询产品组信息")
    @PostMapping("/selectGoodsGroupByIds")
    @Cacheable(value = "ProductGroup",key = "'selectGoodsGroupByIds_'+#groupIds",unless = "#result == null")
    public CommonResultVo<List<GoodsGroupListRes>> selectGoodsGroupByIds(@RequestBody String groupIds){
        CommonResultVo<List<GoodsGroupListRes>> result = new CommonResultVo();
        try {
            Assert.notNull(groupIds,"产品组ids不能为空");
            List<String> ids = Arrays.asList(groupIds.split(","));
            List<GoodsGroupListRes> list = productGroupService.selectGoodsGroup(ids);
            result.setResult(list);
        }catch (Exception e){
            log.error("查询商品的产品组失败",e);
            result.setCode(200);
            result.setMsg("查询商品的产品组失败");
        }
        return result;
    }

    /**
     * 根据产品组id查询产品组信息
     * @param productGroupId
     * @return
     */
    @SysGodDoorLog("根据产品组id查询产品组信息")
    @ApiOperation("根据产品组id查询产品组信息")
    @PostMapping("/findByGroupId")
    @Cacheable(value = "ProductGroup",key = "'findByGroupId_'+#productGroupId",unless = "#result == null")
    public CommonResultVo<GoodsGroupListRes> findByGroupId(@RequestBody Integer productGroupId){
        CommonResultVo<GoodsGroupListRes> result = new CommonResultVo();
        try {
            Assert.notNull(productGroupId,"产品组id不能为空");
            GoodsGroupListRes res = productGroupService.findByGroupId(productGroupId);
            result.setResult(res);
        }catch (Exception e){
            log.error("根据产品组id查询产品组信息失败",e);
            result.setCode(200);
            result.setMsg("根据产品组id查询产品组信息失败");
        }
        return result;
    }

    /**
     * 查询商品下面的产品组信息
     * @param groupById
     * @return
     */
    @SysGodDoorLog("查询商品下面的产品组信息")
    @ApiOperation("查询商品下面的产品组信息")
    @PostMapping("/selectGoodsGroupById")
    @Cacheable(value = "ProductGroup",key = "'selectGoodsGroupById_'+#groupById",unless = "#result == null")
    public CommonResultVo<List<GoodsGroupListRes>> selectGoodsGroupById(@RequestBody Integer groupById){
        CommonResultVo<List<GoodsGroupListRes>> result = new CommonResultVo();
        try {
            Assert.notNull(groupById,"商品id不能为空");
            List<GoodsGroupListRes> list = productGroupService.selectGoodsGroupById(groupById);
            result.setResult(list);
        }catch (Exception e){
            log.error("查询商品的产品组失败",e);
            result.setCode(200);
            result.setMsg("查询商品的产品组失败");
        }
        return result;
    }

    /**
     * 查询商品下面的产品组信息
     * @param reqVo
     * @return
     */
    @SysGodDoorLog("根据产品组id查询产品组下商户和规格")
    @ApiOperation("根据产品组id查询产品组下商户和规格")
    @PostMapping("/selectProductGroupById")
    @Cacheable(value = "ProductGroup",key = "'selectProductGroupById_'+#reqVo.productGroupId+'_'+#reqVo.productId",unless = "#result == null")
    public CommonResultVo<List<ProductGroupResVO>> selectProductGroupById(@RequestBody QueryProductGroupInfoReqVo reqVo){
        CommonResultVo<List<ProductGroupResVO>> result = new CommonResultVo();
        try {
            Assert.notNull(reqVo.getProductGroupId(),"产品组id不能为空");
            List<ProductGroupResVO> list = productGroupService.selectProductGroupById(reqVo);
            result.setResult(list);
        }catch (Exception e){
            log.error("查询商品的产品组失败",e);
            result.setCode(200);
            result.setMsg("查询商品的产品组失败");
        }
        return result;
    }

    /**
     * 查询产品组的资源类型
     * @param productGroupId
     * @return
     */
    @SysGodDoorLog("查询产品组的资源类型")
    @ApiOperation("查询产品组的资源类型")
    @PostMapping("/selectGroupService")
    @Cacheable(value = "ProductGroup",key = "'selectGroupService_'+#productGroupId",unless = "#result == null")
    public CommonResultVo<List<SysService>> selectGroupService(@RequestBody Integer productGroupId){
        CommonResultVo<List<SysService>> result = new CommonResultVo<>();
        try {
            Assert.notNull(productGroupId,"产品组id不能为空");
            List<SysService> list = productGroupService.selectGroupService(productGroupId);
            result.setResult(list);
        }catch (Exception e){
            log.error("查询产品组的资源类型失败",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    /**
     * 查询产品组资源类型的商户
     * @param selectShopByGroupServiceReq
     * @return
     */
    @SysGodDoorLog("查询产品组资源类型的商户")
    @ApiOperation("查询产品组资源类型的商户")
    @PostMapping("/selectShopByGroupService")
    @Cacheable(value = "ProductGroup",key = "'selectShopByGroupService_'+#selectShopByGroupServiceReq.productGroupId+'_'+#selectShopByGroupServiceReq.service",unless = "#result == null")
    public CommonResultVo<List<SelectShopByGroupServiceRes>> selectShopByGroupService(@RequestBody SelectShopByGroupServiceReq selectShopByGroupServiceReq){
        CommonResultVo<List<SelectShopByGroupServiceRes>> result = new CommonResultVo<>();
        try {
            Assert.notNull(selectShopByGroupServiceReq.getProductGroupId(),"产品组id不能为空");
            Assert.notNull(selectShopByGroupServiceReq.getService(),"资源类型不能为空");
            List<SelectShopByGroupServiceRes> list = productGroupService.selectShopByGroupService(selectShopByGroupServiceReq);
            result.setResult(list);
        }catch (Exception e){
            log.error("查询产品组资源类型的商户失败",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    @SysGodDoorLog("根据产品组id合资源类型查询产品组产品详情")
    @ApiOperation("根据产品组id合资源类型查询产品组产品详情")
    @PostMapping("/selectProductByGroupService")
    @Cacheable(value = "ProductGroup",key = "'selectProductByGroupService_'+#selectShopByGroupServiceReq.productGroupId+'_'+#selectShopByGroupServiceReq.service",unless = "#result == null")
    public CommonResultVo<List<SelectProductByGroupServiceRes>> selectProductByGroupService(@RequestBody SelectShopByGroupServiceReq selectShopByGroupServiceReq){
        CommonResultVo<List<SelectProductByGroupServiceRes>> result = new CommonResultVo();
        try {
            Assert.notNull(selectShopByGroupServiceReq.getProductGroupId(),"产品组id不能为空");
            Assert.notNull(selectShopByGroupServiceReq.getService(),"资源类型不能为空");
            List<SelectProductByGroupServiceRes> list = productGroupService.selectProductByGroupService(selectShopByGroupServiceReq);
            result.setResult(list);
        }catch (Exception e){
            log.error("根据产品组id合资源类型查询产品组产品详情失败",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    /**
     * 预约时选择资源的列表查询
     * @param selectBookShopItemReq
     * @return
     */
    @SysGodDoorLog("预约时选择资源的列表查询")
    @ApiOperation("预约时选择资源的列表查询")
    @PostMapping("/selectBookShopItem")
    @Cacheable(value = "ProductGroup",key = "'selectBookShopItem_'+#selectBookShopItemReq.shopId+'_'+#selectBookShopItemReq.productGroupId+'_'+#selectBookShopItemReq.service",unless = "#result == null")
    public CommonResultVo<List<SelectBookShopItemRes>> selectBookShopItem(@RequestBody SelectBookShopItemReq selectBookShopItemReq){
        CommonResultVo<List<SelectBookShopItemRes>> result = new CommonResultVo<>();
        try {
            Assert.notNull(selectBookShopItemReq.getShopId(),"商户id不能为空");
            Assert.notNull(selectBookShopItemReq.getProductGroupId(),"产品组id不能为空");
            Assert.notNull(selectBookShopItemReq.getService(),"资源类型不能为空");
            List<SelectBookShopItemRes> list = productGroupService.selectBookShopItem(selectBookShopItemReq);
            result.setResult(list);
        }catch (Exception e){
            log.error("查询预约时选择资源的列表失败",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    /**
     * 预约获取商户结算信息
     * @param shopSettleMsgReq
     * @return
     */
    @SysGodDoorLog("预约获取商户结算信息")
    @ApiOperation("预约获取商户结算信息")
    @PostMapping("/shopSettleMsg")
    public CommonResultVo<ShopSettleMsgRes> shopSettleMsg(@RequestBody ShopSettleMsgReq shopSettleMsgReq){
        CommonResultVo<ShopSettleMsgRes> result = new CommonResultVo<>();
        try {
            Assert.notNull(shopSettleMsgReq,"参数不能为空");
            ShopSettleMsgRes shopSettleMsgRes = productGroupService.shopSettleMsg(shopSettleMsgReq);
            result.setResult(shopSettleMsgRes);
        }catch (Exception e){
            log.error("预约获取商户结算信息失败",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }
    /**
     * 排序保存
     * @param goodsGroupListRes
     * @return
     */
    @SysGodDoorLog("排序保存")
    @ApiOperation("排序保存")
    @PostMapping("/saveSort")
    @CacheEvict(value = {"ProductGroup","Goods"},allEntries = true)
    public CommonResultVo<List<GroupProductVo>> saveSort(@RequestBody GoodsGroupListRes goodsGroupListRes){
        CommonResultVo<List<GroupProductVo>> result = new CommonResultVo<>();
        try {
            productGroupService.saveSort(goodsGroupListRes.getGroupProductList());
            result.setResult(goodsGroupListRes.getGroupProductList());
        }catch (Exception e){
            log.error("预约获取商户结算信息失败",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    @SysGodDoorLog("根据id查询商品产品组信息")
    @ApiOperation("根据id查询商品产品组信息")
    @PostMapping("/selectGoodsGroupByGoodsId")
    @Cacheable(value = "ProductGroup",key = "'selectGoodsGroupByGoodsId_'+#goodsId",unless = "#result == null")
    public CommonResultVo<List<GoodsGroupListRes>> selectGoodsGroupByGoodsId(@RequestBody Integer goodsId){
        CommonResultVo<List<GoodsGroupListRes>> result = new CommonResultVo<>();
        try {
            List<GoodsGroupListRes> goodsGroupListResList = productGroupService.selectGoodsGroupByGoodsId(goodsId);
            for (GoodsGroupListRes goodsGroupListRes : goodsGroupListResList) {
                List<ProductGroupResource> productGroupResourceList = productGroupResourceService.selectByProductGroupId(goodsGroupListRes.getId());
                //获取资源类型,如果有多个,默认取第一个
                if (productGroupResourceList != null ){
                    goodsGroupListRes.setService(productGroupResourceList.get(0).getService());
                }

            }
            result.setResult(goodsGroupListResList);
        }catch (Exception e){
            log.error("根据id查询商品产品组信息异常:",e);
            result.setCode(200);
            result.setMsg("根据id查询商品产品组信息");
        }
        return result;
    }

    @SysGodDoorLog("根据产品组id列表查询折扣比例")
    @ApiOperation("根据产品组id列表查询折扣比例")
    @PostMapping("/selectDiscountByIds")
    public CommonResultVo<List<ProductGroup>> selectDiscountByIds(@RequestBody List<Integer> groups){
        CommonResultVo<List<ProductGroup>> result = new CommonResultVo<>();
        try {
            List<ProductGroup> productGroups = productGroupService.selectDiscountByIds(groups);
            result.setResult(productGroups);
        }catch (Exception e){
            log.error("根据产品组id列表查询折扣比例失败",e);
            result.setMsg(e.getMessage());
            result.setCode(200);
        }
        return result;
    }

    @SysGodDoorLog("根据Charlie产品组id查询产品组信息")
    @ApiOperation("根据Charlie产品组id查询产品组信息")
    @PostMapping("/selectGroupByOldId")
    public CommonResultVo<List<ProductGroup>> selectGroupByOldId(@RequestBody Integer oldId){
        CommonResultVo<List<ProductGroup>> result = new CommonResultVo<>();
        try {
            Assert.notNull(oldId,"Charlie系统产品组id不能为空");
            Wrapper wrapper = new Wrapper() {
                @Override
                public String getSqlSegment() {
                    return "where del_flag = 0 and old_id = "+oldId;
                }
            };
            List<ProductGroup> list = productGroupService.selectList(wrapper);
            result.setResult(list);
        }catch (Exception e){
            log.error("根据Charlie产品组id查询产品组信息失败",e);
            result.setMsg(e.getMessage());
            result.setCode(200);
        }
        return result;
    }
}
