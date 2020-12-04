package com.colourfulchina.pangu.taishang.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.colourfulchina.bigan.api.feign.RemoteShopItemService;
import com.colourfulchina.god.door.api.annotation.SysGodDoorLog;
import com.colourfulchina.god.door.api.util.SecurityUtils;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.pangu.taishang.api.dto.BlockRule;
import com.colourfulchina.pangu.taishang.api.dto.SysFileDto;
import com.colourfulchina.pangu.taishang.api.entity.*;
import com.colourfulchina.pangu.taishang.api.enums.*;
import com.colourfulchina.pangu.taishang.api.vo.ShopItemBlockRuleVo;
import com.colourfulchina.pangu.taishang.api.vo.ShopItemPriceVo;
import com.colourfulchina.pangu.taishang.api.vo.ShopItemSettleExpressVo;
import com.colourfulchina.pangu.taishang.api.vo.req.ListSysFileReq;
import com.colourfulchina.pangu.taishang.api.vo.req.ShopItemBlockDelReq;
import com.colourfulchina.pangu.taishang.api.vo.req.ShopItemReq;
import com.colourfulchina.pangu.taishang.api.vo.res.ShopItemPriceRes;
import com.colourfulchina.pangu.taishang.api.vo.res.ShopItemRes;
import com.colourfulchina.pangu.taishang.api.vo.res.shop.ShopListQueryResVo;
import com.colourfulchina.pangu.taishang.api.vo.res.shopItem.*;
import com.colourfulchina.pangu.taishang.service.*;
import com.colourfulchina.pangu.taishang.utils.DateUtils;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/shopItem")
@Slf4j
@AllArgsConstructor
@Api(tags = {"规格操作接口"})
public class ShopItemController {
    @Autowired
    private ShopItemService shopItemService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private SysServiceService sysServiceService;
    @Autowired
    private ShopItemNetPriceRuleService shopItemNetPriceRuleService;
    @Autowired
    private ShopItemSettlePriceRuleService shopItemSettlePriceRuleService;
    @Autowired
    private ShopItemSettleExpressService shopItemSettleExpressService;
    @Autowired
    private BlockRuleService blockRuleService;
    @Autowired
    private BlockReasonService blockReasonService;
    private final RemoteShopItemService remoteShopItemService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductGroupProductService productGroupProductService;
    @Autowired
    private GroupProductBlockDateService groupProductBlockDateService;

    @Autowired
    private SysFileService sysFileService;

    /**
     *
     * @param shopItem
     * @return
     */
    @SysGodDoorLog("查询规格列表")
    @ApiOperation("查询规格列表")
    @PostMapping("/list")
    public CommonResultVo<List<ShopItemRes>> list(@RequestBody ShopItem shopItem){
        CommonResultVo<List<ShopItemRes>> resultVo=new CommonResultVo<>();
        try {
            Wrapper<ShopItem> configWrapper=new Wrapper<ShopItem>() {
                @Override
                public String getSqlSegment() {
                    if (shopItem == null){
                        return null;
                    }
                    StringBuffer sql=new StringBuffer("where 1=1");
                    if (shopItem.getShopId() != null){
                        sql.append(" and ").append("shop_id = ").append(shopItem.getShopId());
                    }
                    return sql.toString();
                }
            };
            final List<ShopItem> shopItems = shopItemService.selectList(configWrapper);
            if (!CollectionUtils.isEmpty(shopItems)){
                List<ShopItemRes> shopItemResList=Lists.newArrayList();
                shopItems.forEach(item -> {
                    shopItemResList.add(shopItem2shopItemRes(item));
                });
                resultVo.setResult(shopItemResList);
            }
        }catch (Exception e){
            log.error("查询商户规格列表失败",e);
            resultVo.setCode(200);
            resultVo.setMsg(e.getMessage());
        }

        return resultVo;
    }

    @SysGodDoorLog("根据id查询规格")
    @ApiOperation("根据id查询规格")
    @GetMapping("/get/{id}")
    public CommonResultVo<ShopItemRes> get(@PathVariable Integer id){
        CommonResultVo<ShopItemRes> resultVo=new CommonResultVo<>();
        try {
            final ShopItem shopItem = shopItemService.selectById(id);
            resultVo.setResult(shopItem2shopItemRes(shopItem));
        }catch (Exception e){
            log.error("查询商户规格失败",e);
            resultVo.setCode(200);
            resultVo.setMsg(e.getMessage());
        }
        return resultVo;
    }

    @SysGodDoorLog("更新规格")
    @ApiOperation("更新规格")
    @PostMapping("/update")
    @CacheEvict(value = {"Shop","ShopItemSettlePriceRule","ProductGroup","ProductGroupProduct","Goods"},allEntries = true)
    public CommonResultVo<ShopItem> update(@RequestBody ShopItemReq shopItemReq,HttpServletRequest request){
        CommonResultVo<ShopItem> resultVo=new CommonResultVo<>();
        try {
            if (shopItemReq == null){
                throw new Exception("参数不能为空");
            }
            if (shopItemReq.getId() == null){
                throw new Exception("参数code不能为空");
            }
            ShopItem shopItem = shopItemService.selectById(shopItemReq.getId());

            shopItem.setDelFlag(DelFlagEnums.NORMAL.getCode());
            shopItem.setName(shopItemReq.getName());


            shopItem.setNeeds(shopItemReq.getNeeds());
            shopItem.setAddon(shopItemReq.getAddon());
            shopItem.setCancelPolicy(shopItemReq.getCancelPolicy());
            shopItem.setRetainRoom(shopItemReq.getRetainRoom());
            shopItem.setXcMinPrice(shopItemReq.getXcMinPrice());
            shopItem.setXcMaxPrice(shopItemReq.getXcMaxPrice());
            shopItem.setXcAvgPrice(shopItemReq.getXcAvgPrice());
            shopItem.setOpenTime(shopItemReq.getOpenTime());
            shopItem.setCloseTime(shopItemReq.getCloseTime());
            shopItem.setMenuText(shopItemReq.getMenuText());
            shopItem.setThirdCpnNum(shopItemReq.getThirdCpnNum());
            shopItem.setThirdCpnName(shopItemReq.getThirdCpnName());
            shopItem.setStopReason(shopItemReq.getStopReason());
            shopItem.setExpressMode(shopItemReq.getExpressMode());
            if(shopItemReq.getCityList()!= null && shopItemReq.getCityList().size() > 0){
                shopItem.setCitys(StringUtils.join(shopItemReq.getCityList(), ","));
            }

            if(shopItem.getEnable() != shopItemReq.getEnable()){
                if (!CollectionUtils.isEmpty(shopItemReq.getGiftList())){
                    productService.updateProductStatus(shopItemReq.getGiftList() ,shopItem,shopItemReq.getEnable());
                }else if (null == shopItemReq.getGiftList()){
                    productService.updateProductStatus(null ,shopItem,shopItemReq.getEnable());
                }
                shopItem.setEnable(shopItemReq.getEnable());
            }
//            if(shopItem.getEnable() == 1){//资源状态的停售和在售更新对应的产品状态
//////                productService.updateProductStatus(null ,shopItem,shopItem.getEnable());
//////            }else{
//////                productService.updateProductStatus(null ,shopItem,shopItem.getEnable());
//////            }
            String giftStr  = shopItem.getGift();
            if(!CollectionUtils.isEmpty(shopItemReq.getGiftList())){
                List<String> giftListNew  = shopItemReq.getGiftList();
                if(StringUtils.isNotBlank(giftStr)){
                    List<String> giftListOld  = Arrays.asList(giftStr.split(","));
                    List<String> listTemp = new ArrayList();
                    for(String str  : giftListOld){
                        if( !giftListNew.contains(str)){
                            listTemp.add(str);
                        }
                    }
                    if(listTemp.size() > 0){
                            productService.updateProductStatus(listTemp,shopItem,1);//删除
                    }

                }
                if(shopItem.getEnable() == 0 && giftListNew.size() > 0 &&StringUtils.isNotBlank(giftStr) ){
                    List<String> giftListOld  = Arrays.asList(giftStr.split(","));
                    List<String> listTemp = new ArrayList();
                    for(String str: giftListNew){
                        if( !giftListOld.contains(str)){
                            listTemp.add(str);
                        }
                    }
                    if(listTemp.size() > 0){
                        productService.updateProductStatus(listTemp ,shopItem,0);//恢复
                    }

                }

                shopItem.setGift(StringUtils.join(giftListNew, ","));
            }else {
                shopItem.setGift(null);
                if (StringUtils.isNotBlank(giftStr)){
                    List<String> giftListOld  = Arrays.asList(giftStr.split(","));
                    productService.updateProductStatus(giftListOld,shopItem,1);//删除
                }
            }

//            final ShopItem shopItem = shopItemRes2shopItem(shopItemReq);
            final boolean updateById = shopItemService.updateById(shopItem);
            if (!updateById){
                throw new Exception("更新商户规格失败");
            }else {
                //同步更新老系统商户规格
                shopItemService.remoteUpd(shopItem);
                //生成产品
                productService.generateProduct(shopItem.getId(),request);
            }
            resultVo.setResult(shopItem);
        }catch (Exception e){
            log.error("更新商户规格失败",e);
            resultVo.setCode(200);
            resultVo.setMsg(e.getMessage());
        }
        return resultVo;
    }

    @SysGodDoorLog("删除规格")
    @ApiOperation("删除规格")
    @PostMapping("/delete")
    @CacheEvict(value = {"Shop","ShopItemSettlePriceRule","ProductGroup","ProductGroupProduct","Goods"},allEntries = true)
    public CommonResultVo<ShopItem> delete(@RequestBody Integer id){
        CommonResultVo<ShopItem> resultVo=new CommonResultVo<>();
        try {
            if (id == null){
                throw new Exception("参数不能为空");
            }
            ShopItem shopItem = shopItemService.selectById(id);
            //判断该规格是否被包入商品售卖
            List<ProductGroupProduct> groupProducts = shopItemService.checkItemIsSale(shopItem.getId());
            if (CollectionUtils.isEmpty(groupProducts)){
                shopItem.setDelFlag(DelFlagEnums.DELETE.getCode());
                final boolean flag = shopItemService.updateById(shopItem);
                if (!flag){
                    throw new Exception("删除商户规格失败");
                }else {
                    //产品删除
                    Wrapper productWrapper = new Wrapper() {
                        @Override
                        public String getSqlSegment() {
                            return "where shop_item_id ="+shopItem.getId();
                        }
                    };
                    List<Product> products = productService.selectList(productWrapper);
                    if (!CollectionUtils.isEmpty(products)){
                        for (Product product : products) {
                            product.setDelFlag(DelFlagEnums.DELETE.getCode());
                            productService.updateById(product);
                        }
                    }
                }
            }else {
                resultVo.setCode(200);
                resultVo.setMsg("该资源已经包入商品，无法删除");
            }
            resultVo.setResult(shopItem);
        }catch (Exception e){
            log.error("删除商户规格失败",e);
            resultVo.setCode(200);
            resultVo.setMsg(e.getMessage());
        }
        return resultVo;
    }

    @SysGodDoorLog("添加规格")
    @ApiOperation("添加规格")
    @PostMapping("/add")
    @CacheEvict(value = {"Shop","ShopItemSettlePriceRule","ProductGroup","ProductGroupProduct","Goods"},allEntries = true)
    public CommonResultVo<ShopItem> add(@RequestBody ShopItem shopItem, HttpServletRequest request){
        CommonResultVo<ShopItem> resultVo=new CommonResultVo<>();
        try {
            if(null != shopItem.getCityList() &&shopItem.getCityList().size() > 0){
                shopItem.setCitys(StringUtils.join(shopItem.getCityList(), ","));
            }
            if(null != shopItem.getGiftList() && shopItem.getGiftList().size() > 0){
                shopItem.setGift(StringUtils.join(shopItem.getGiftList(), ","));
            }
            shopItem = shopItemService.add(shopItem);
            //生成产品
            productService.generateProduct(shopItem.getId(),request);
            resultVo.setResult(shopItem);
        }catch (Exception e){
            log.error("添加商户规格失败",e);
            resultVo.setCode(200);
            resultVo.setMsg(e.getMessage());
        }
        return resultVo;
    }

    /**
     * 把shopItem转成shopItemRes
     * @param shopItem
     * @return
     */
    private ShopItemRes shopItem2shopItemRes(ShopItem shopItem){
        if (shopItem == null){
            return null;
        }
        ShopItemRes shopItemRes=new ShopItemRes();
        BeanUtils.copyProperties(shopItem,shopItemRes);
        //TODO 需要翻译blockRule
        return shopItemRes;
    }

    /**
     * 把shopItemReq转成shopItem
     * @param shopItemReq
     * @return
     */
    private ShopItem shopItemRes2shopItem(ShopItemReq shopItemReq){
        if (shopItemReq == null){
            return null;
        }
        ShopItem shopItem=new ShopItem();
        BeanUtils.copyProperties(shopItemReq,shopItem);
        //TODO 需要翻译blockRule
        return shopItem;
    }

    /**
     * 查询规格block列表
     * @param shopItemId
     * @return
     */
    @SysGodDoorLog("规格block列表")
    @ApiOperation("规格block列表")
    @PostMapping("/shopItemBlockList")
    public CommonResultVo<List<BlockRule>> shopItemBlockList(@RequestBody Integer shopItemId){
        CommonResultVo<List<BlockRule>> result = new CommonResultVo<>();
        try {
            ShopItem shopItem = shopItemService.selectById(shopItemId);
            if (StringUtils.isNotBlank(shopItem.getBlockRule())){
                List<BlockRule> list = blockRuleService.blockRuleStr2list(shopItem.getBlockRule());
                result.setResult(list);
            }
        }catch (Exception e){
            log.error("规格block列表查询失败",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    /**
     * 规格block添加
     * @param shopItemBlockRuleVo
     * @return
     */
    @SysGodDoorLog("规格block添加")
    @ApiOperation("规格block添加")
    @PostMapping("/shopItemBlockAdd")
    @CacheEvict(value = {"ProductGroup","Goods","Shop","ShopItem","ProductGroupProduct"},allEntries = true)
    public CommonResultVo<Integer> shopItemBlockAdd(@RequestBody ShopItemBlockRuleVo shopItemBlockRuleVo){
        CommonResultVo<Integer> result = new CommonResultVo<>();
        try {
            Assert.notNull(shopItemBlockRuleVo,"参数不能为空");
            Assert.notNull(shopItemBlockRuleVo.getShopItemId(),"规格id不能为空");
            List<BlockRule> blockRuleList = Lists.newLinkedList();
            ShopItem shopItem = shopItemService.selectById(shopItemBlockRuleVo.getShopItemId());
            if (StringUtils.isNotBlank(shopItem.getBlockRule())){
                List<BlockRule> list1 = blockRuleService.blockRuleStr2list(shopItem.getBlockRule());
                blockRuleList.addAll(list1);
            }
            if (shopItemBlockRuleVo.getCalendar()!=null && shopItemBlockRuleVo.getRepeat()!=null&&(!ArrayUtils.isEmpty(shopItemBlockRuleVo.getContainWeek()) || !ArrayUtils.isEmpty(shopItemBlockRuleVo.getBlockTime()))){
                List<BlockRule> list2 = blockRuleService.generateBlockRule(shopItemBlockRuleVo);
                if (!CollectionUtils.isEmpty(list2)){
                    blockRuleList.addAll(list2);
                }
            }
            if (!CollectionUtils.isEmpty(shopItemBlockRuleVo.getSpecialBlocks())){
                blockRuleList.addAll(shopItemBlockRuleVo.getSpecialBlocks());
            }
            if (!CollectionUtils.isEmpty(blockRuleList)){
                //block原因表存入数据
                blockReasonService.addReason(BlockReasonEnums.ReasonType.TYPE_SHOP_ITEM.getCode(),shopItemBlockRuleVo.getShopItemId(),blockRuleList);
                String blockRule = blockRuleService.blockRuleList2str(blockRuleList);
                shopItem.setBlockRule(blockRule);
                shopItemService.updateById(shopItem);
                //同步更新老系统商户规格
                shopItemService.remoteUpd(shopItem);
            }
            //产品组下面的产品成本价格区间更新
            productGroupProductService.updCostByShopItemId(shopItem.getId());
            //产品组产品block日期添加
            groupProductBlockDateService.updBlockDateByShopItemId(shopItem.getId());
            result.setResult(shopItem.getId());
        }catch (Exception e){
            log.error("规格block添加失败",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    /**
     *规格block删除接口
     * @param shopItemBlockDelReq
     * @return
     */
    @SysGodDoorLog("规格block删除接口")
    @ApiOperation("规格block删除接口")
    @PostMapping("/shopItemBlockDel")
    @CacheEvict(value = {"ProductGroup","Goods","Shop","ShopItem","ProductGroupProduct"},allEntries = true)
    public CommonResultVo<List<BlockRule>> shopItemBlockDel(@RequestBody ShopItemBlockDelReq shopItemBlockDelReq){
        CommonResultVo result = new CommonResultVo();
        try {
            Assert.notNull(shopItemBlockDelReq.getShopItemId(),"规格id不能为空");
            ShopItem shopItem = shopItemService.selectById(shopItemBlockDelReq.getShopItemId());
            if (CollectionUtils.isEmpty(shopItemBlockDelReq.getBlockRuleList())){
                shopItem.setBlockRule(null);
            }else {
                String blockRule = blockRuleService.blockRuleList2str(shopItemBlockDelReq.getBlockRuleList());
                shopItem.setBlockRule(blockRule);
            }
            //删除block的原因同步删除
            Wrapper delWrapper = new Wrapper() {
                @Override
                public String getSqlSegment() {
                    StringBuffer stringBuffer = new StringBuffer();
                    stringBuffer.append("where type = 'shop_item' and value ="+shopItem.getId());
                    if (!CollectionUtils.isEmpty(shopItemBlockDelReq.getBlockRuleList())){
                        List<String> blocks = shopItemBlockDelReq.getBlockRuleList().stream().map(blockRule -> blockRule.getRule()).collect(Collectors.toList());
                        stringBuffer.append(" and block not in ('" + StringUtils.join(blocks,"','") + "')");
                    }
                    return stringBuffer.toString();
                }
            };
            blockReasonService.delete(delWrapper);

            shopItemService.updateById(shopItem);
            //同步更新老系统商户规格
            shopItemService.remoteUpd(shopItem);
            //产品组下面的产品成本价格区间更新
            productGroupProductService.updCostByShopItemId(shopItem.getId());
            //产品组产品block日期添加
            groupProductBlockDateService.updBlockDateByShopItemId(shopItem.getId());
        }catch (Exception e){
            log.error("规格block删除失败",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    /**
     * 同步老系统shopItem数据到新系统shopItem中
     * @return
     */
    @SysGodDoorLog("同步老系统shopItem数据到新系统shopItem")
    @ApiOperation("同步老系统shopItem数据到新系统shopItem中")
    @PostMapping("/syncShopItemList")
    @Transactional(rollbackFor=Exception.class)
    public CommonResultVo<List<ShopItem>> syncShopItemList(){
        log.info("同步开始");
        CommonResultVo<List<ShopItem>> result = new CommonResultVo<>();
        List<ShopItem> shopItemList = Lists.newLinkedList();
        try {
            CommonResultVo<List<com.colourfulchina.bigan.api.entity.ShopItem>> remoteResult = remoteShopItemService.selectShopItemList();
            if (!CollectionUtils.isEmpty(remoteResult.getResult())){
                for (com.colourfulchina.bigan.api.entity.ShopItem remoteShopItem : remoteResult.getResult()) {
                    log.info("开始同步老系统规格id为{}",remoteShopItem.getId());
                    //根据老系统shopId查询新系统商户信息
                    Shop shop = shopService.selectByOldId(remoteShopItem.getShopId().intValue());
                    //根据服务类型查询服务信息
                    SysService sysService = sysServiceService.selectById(remoteShopItem.getType());
                    //得到菜单信息
                    String menuText = null;
                    if (StringUtils.isNotBlank(remoteShopItem.getMore())){
                        JSONObject jsonObject = JSON.parseObject(remoteShopItem.getMore());
                        menuText = jsonObject.getString("menuText");
                    }
                    ShopItem shopItem = new ShopItem();
                    shopItem.setOldItemId(remoteShopItem.getId().intValue());
                    shopItem.setShopId(shop.getId());
                    shopItem.setServiceType(remoteShopItem.getType());
                    shopItem.setServiceName(sysService.getName());
                    shopItem.setName(remoteShopItem.getName());
                    shopItem.setNeeds(remoteShopItem.getNeeds());
                    shopItem.setAddon(remoteShopItem.getAddon());
                    shopItem.setOpenTime(StringUtils.isBlank(remoteShopItem.getOpentime()) ? null : remoteShopItem.getOpentime().split("~")[0]);
                    shopItem.setCloseTime(StringUtils.isBlank(remoteShopItem.getOpentime()) ? null : remoteShopItem.getOpentime().split("~")[1]);
                    shopItem.setBlockRule(remoteShopItem.getBlock());
                    shopItem.setMenuText(menuText);
                    shopItem.setEnable(0);
                    shopItem.setDelFlag(DelFlagEnums.NORMAL.getCode());
                    shopItem.setCreateTime(new Date());
                    shopItem.setCreateUser(SecurityUtils.getLoginName());
                    shopItem.setUpdateTime(new Date());
                    shopItem.setUpdateUser(SecurityUtils.getLoginName());
                    ShopItemPriceVo shopItemPriceVo = null;
                    if (StringUtils.isNotBlank(remoteShopItem.getPrice())){
                        shopItemPriceVo = shopItemService.analysisPrice(remoteShopItem.getPrice());
                        log.info("price字段解析：{}",shopItemPriceVo.toString());
                        List<String> gifts = Lists.newLinkedList();
                        if (StringUtils.isNotBlank(shopItemPriceVo.getGiftF2())){
                            gifts.add("F2");
                        }
                        if (StringUtils.isNotBlank(shopItemPriceVo.getGiftD5())){
                            gifts.add("D5");
                        }
                        if (StringUtils.isNotBlank(shopItemPriceVo.getGift2F1())){
                            gifts.add("2F1");
                        }
                        if (StringUtils.isNotBlank(shopItemPriceVo.getGift3F1())){
                            gifts.add("3F1");
                        }
                        if (StringUtils.isNotBlank(shopItemPriceVo.getGiftF1())){
                            gifts.add("F1");
                        }
                        if (!CollectionUtils.isEmpty(gifts)){
                            shopItem.setGift(StringUtils.join(gifts,","));
                        }
                    }
                    //spa同步过来的权益类型为五折
                    if (ShopTypeEnums.SPA.getCode().equals(remoteShopItem.getType())){
                        shopItem.setGift("D5");
                    }
                    //单杯同步过来的权益类型为单免
                    if (ShopTypeEnums.DRINK.getCode().equals(remoteShopItem.getType())){
                        shopItem.setGift("F1");
                    }
                    shopItemService.insert(shopItem);
                    if (shopItemPriceVo != null){
                        //产品价格
                        if (!CollectionUtils.isEmpty(shopItemPriceVo.getNetList())){
                            for (Map<String, String> map : shopItemPriceVo.getNetList()) {
                                //产品价格入库
                                ShopItemNetPriceRule shopItemNetPriceRule = new ShopItemNetPriceRule();
                                shopItemNetPriceRule.setShopId(shop.getId());
                                shopItemNetPriceRule.setShopItemId(shopItem.getId());
                                //TODO:这个起止时间 老系统没有
                                shopItemNetPriceRule.setStartDate(DateUtil.parse("2019-01-01","yyyy-MM-dd"));
                                shopItemNetPriceRule.setEndDate(DateUtil.parse("2021-12-31","yyyy-MM-dd"));
                                if (map.containsKey("monday")){
                                    shopItemNetPriceRule.setMonday(1);
                                }
                                if (map.containsKey("tuesday")){
                                    shopItemNetPriceRule.setTuesday(1);
                                }
                                if (map.containsKey("wednesday")){
                                    shopItemNetPriceRule.setWednesday(1);
                                }
                                if (map.containsKey("thursday")){
                                    shopItemNetPriceRule.setThursday(1);
                                }
                                if (map.containsKey("friday")){
                                    shopItemNetPriceRule.setFriday(1);
                                }
                                if (map.containsKey("saturday")){
                                    shopItemNetPriceRule.setSaturday(1);
                                }
                                if (map.containsKey("sunday")){
                                    shopItemNetPriceRule.setSunday(1);
                                }
                                shopItemNetPriceRule.setNetPrice(new BigDecimal(map.get("net_price")));
                                shopItemNetPriceRule.setServiceRate(shopItemPriceVo.getServiceFee());
                                shopItemNetPriceRule.setTaxRate(shopItemPriceVo.getTaxation());
                                shopItemNetPriceRuleService.insert(shopItemNetPriceRule);

                                //当资源类型为drink时，且老系统录入了价格的情况下，默认添加单杯的结算规则为F1固定贴现
                                if (ShopTypeEnums.DRINK.getCode().equals(remoteShopItem.getType())){
                                    shopItemPriceVo.setGiftF1(map.get("net_price")+"(固定贴现)");
                                }
                            }
                        }
                        //二免一  结算规则
                        if (StringUtils.isNotBlank(shopItemPriceVo.getGift2F1())){
                            List<ShopItemSettleExpressVo> gift2F1 = shopItemService.analysisSettleExpress(shopItemPriceVo.getGift2F1());
                            for (ShopItemSettleExpressVo shopItemSettleExpressVo : gift2F1) {
                                log.info("结算规则解析：{}",shopItemSettleExpressVo.toString());
                                //结算规则中的公式表
                                ShopItemSettleExpress shopItemSettleExpress = new ShopItemSettleExpress();
                                if (StringUtils.isNotBlank(shopItemSettleExpressVo.getSettleExpress()) || StringUtils.isNotBlank(shopItemSettleExpressVo.getCustomTaxExpress())){
                                    shopItemSettleExpress.setShopId(shop.getId());
                                    shopItemSettleExpress.setShopItemId(shopItem.getId());
                                    shopItemSettleExpress.setNetPricePer(shopItemSettleExpressVo.getNetPricePer());
                                    shopItemSettleExpress.setTaxNetPricePer(shopItemSettleExpressVo.getTaxNetPricePer());
                                    shopItemSettleExpress.setServiceFeePer(shopItemSettleExpressVo.getServiceFeePer());
                                    shopItemSettleExpress.setTaxServiceFeePer(shopItemSettleExpressVo.getTaxServiceFeePer());
                                    shopItemSettleExpress.setTaxFeePer(shopItemSettleExpressVo.getTaxFeePer());
                                    shopItemSettleExpress.setCustomTaxFeePer(shopItemSettleExpressVo.getCustomTaxFeePer());
                                    shopItemSettleExpress.setDiscount(shopItemSettleExpressVo.getDiscount());
                                    shopItemSettleExpress.setAdjust(shopItemSettleExpressVo.getAdjust());
                                    shopItemSettleExpress.setSettleExpress(shopItemSettleExpressVo.getSettleExpress());
                                    shopItemSettleExpress.setCustomTaxExpress(shopItemSettleExpressVo.getCustomTaxExpress());
                                    shopItemSettleExpress.setDelFlag(DelFlagEnums.NORMAL.getCode());
                                    shopItemSettleExpress.setCreateTime(new Date());
                                    shopItemSettleExpress.setCreateUser("init");//初始化
                                    shopItemSettleExpress.setUpdateTime(new Date());
                                    shopItemSettleExpress.setUpdateUser("init");//初始化
                                    shopItemSettleExpressService.insert(shopItemSettleExpress);
                                }
                                //结算规则表
                                ShopItemSettlePriceRule shopItemSettlePriceRule = new ShopItemSettlePriceRule();
                                shopItemSettlePriceRule.setShopId(shop.getId());
                                shopItemSettlePriceRule.setShopItemId(shopItem.getId());
                                shopItemSettlePriceRule.setGift("2F1");
                                //TODO:这个起止时间 老系统没有
                                shopItemSettlePriceRule.setStartDate(DateUtil.parse("2019-01-01","yyyy-MM-dd"));
                                shopItemSettlePriceRule.setEndDate(DateUtil.parse("2021-12-31","yyyy-MM-dd"));
                                shopItemSettlePriceRule.setMonday(shopItemSettleExpressVo.getMonday());
                                shopItemSettlePriceRule.setTuesday(shopItemSettleExpressVo.getTuesday());
                                shopItemSettlePriceRule.setWednesday(shopItemSettleExpressVo.getWednesday());
                                shopItemSettlePriceRule.setThursday(shopItemSettleExpressVo.getThursday());
                                shopItemSettlePriceRule.setFriday(shopItemSettleExpressVo.getFriday());
                                shopItemSettlePriceRule.setSaturday(shopItemSettleExpressVo.getSaturday());
                                shopItemSettlePriceRule.setSunday(shopItemSettleExpressVo.getSunday());
                                shopItemSettlePriceRule.setSettleExpressId(shopItemSettleExpress.getId());
                                shopItemSettlePriceRule.setDelFlag(DelFlagEnums.NORMAL.getCode());
                                shopItemSettlePriceRule.setCreateTime(new Date());
                                shopItemSettlePriceRule.setCreateUser("init");//初始化
                                shopItemSettlePriceRule.setUpdateTime(new Date());
                                shopItemSettlePriceRule.setUpdateUser("init");//初始化
                                shopItemSettlePriceRuleService.insert(shopItemSettlePriceRule);
                            }
                        }
                        //三免一  结算规则
                        if (StringUtils.isNotBlank(shopItemPriceVo.getGift3F1())){
                            List<ShopItemSettleExpressVo> gift3F1 = shopItemService.analysisSettleExpress(shopItemPriceVo.getGift3F1());
                            for (ShopItemSettleExpressVo shopItemSettleExpressVo : gift3F1) {
                                log.info("结算规则解析：{}",shopItemSettleExpressVo.toString());
                                //结算规则中的公式表
                                ShopItemSettleExpress shopItemSettleExpress = new ShopItemSettleExpress();
                                if (StringUtils.isNotBlank(shopItemSettleExpressVo.getSettleExpress()) || StringUtils.isNotBlank(shopItemSettleExpressVo.getCustomTaxExpress())){
                                    shopItemSettleExpress.setShopId(shop.getId());
                                    shopItemSettleExpress.setShopItemId(shopItem.getId());
                                    shopItemSettleExpress.setNetPricePer(shopItemSettleExpressVo.getNetPricePer());
                                    shopItemSettleExpress.setTaxNetPricePer(shopItemSettleExpressVo.getTaxNetPricePer());
                                    shopItemSettleExpress.setServiceFeePer(shopItemSettleExpressVo.getServiceFeePer());
                                    shopItemSettleExpress.setTaxServiceFeePer(shopItemSettleExpressVo.getTaxServiceFeePer());
                                    shopItemSettleExpress.setTaxFeePer(shopItemSettleExpressVo.getTaxFeePer());
                                    shopItemSettleExpress.setCustomTaxFeePer(shopItemSettleExpressVo.getCustomTaxFeePer());
                                    shopItemSettleExpress.setDiscount(shopItemSettleExpressVo.getDiscount());
                                    shopItemSettleExpress.setAdjust(shopItemSettleExpressVo.getAdjust());
                                    shopItemSettleExpress.setSettleExpress(shopItemSettleExpressVo.getSettleExpress());
                                    shopItemSettleExpress.setCustomTaxExpress(shopItemSettleExpressVo.getCustomTaxExpress());
                                    shopItemSettleExpress.setDelFlag(DelFlagEnums.NORMAL.getCode());
                                    shopItemSettleExpress.setCreateTime(new Date());
                                    shopItemSettleExpress.setCreateUser("init");//初始化
                                    shopItemSettleExpress.setUpdateTime(new Date());
                                    shopItemSettleExpress.setUpdateUser("init");//初始化
                                    shopItemSettleExpressService.insert(shopItemSettleExpress);
                                }
                                //结算规则表
                                ShopItemSettlePriceRule shopItemSettlePriceRule = new ShopItemSettlePriceRule();
                                shopItemSettlePriceRule.setShopId(shop.getId());
                                shopItemSettlePriceRule.setShopItemId(shopItem.getId());
                                shopItemSettlePriceRule.setGift("3F1");
                                //TODO:这个起止时间 老系统没有
                                shopItemSettlePriceRule.setStartDate(DateUtil.parse("2019-01-01","yyyy-MM-dd"));
                                shopItemSettlePriceRule.setEndDate(DateUtil.parse("2021-12-31","yyyy-MM-dd"));
                                shopItemSettlePriceRule.setMonday(shopItemSettleExpressVo.getMonday());
                                shopItemSettlePriceRule.setTuesday(shopItemSettleExpressVo.getTuesday());
                                shopItemSettlePriceRule.setWednesday(shopItemSettleExpressVo.getWednesday());
                                shopItemSettlePriceRule.setThursday(shopItemSettleExpressVo.getThursday());
                                shopItemSettlePriceRule.setFriday(shopItemSettleExpressVo.getFriday());
                                shopItemSettlePriceRule.setSaturday(shopItemSettleExpressVo.getSaturday());
                                shopItemSettlePriceRule.setSunday(shopItemSettleExpressVo.getSunday());
                                shopItemSettlePriceRule.setSettleExpressId(shopItemSettleExpress.getId());
                                shopItemSettlePriceRule.setDelFlag(DelFlagEnums.NORMAL.getCode());
                                shopItemSettlePriceRule.setCreateTime(new Date());
                                shopItemSettlePriceRule.setCreateUser("init");//初始化
                                shopItemSettlePriceRule.setUpdateTime(new Date());
                                shopItemSettlePriceRule.setUpdateUser("init");//初始化
                                shopItemSettlePriceRuleService.insert(shopItemSettlePriceRule);
                            }
                        }
                        //五折  结算规则
                        if (StringUtils.isNotBlank(shopItemPriceVo.getGiftD5())){
                            List<ShopItemSettleExpressVo> giftD5 = shopItemService.analysisSettleExpress(shopItemPriceVo.getGiftD5());
                            for (ShopItemSettleExpressVo shopItemSettleExpressVo : giftD5) {
                                log.info("结算规则解析：{}",shopItemSettleExpressVo.toString());
                                //结算规则中的公式表
                                ShopItemSettleExpress shopItemSettleExpress = new ShopItemSettleExpress();
                                if (StringUtils.isNotBlank(shopItemSettleExpressVo.getSettleExpress()) || StringUtils.isNotBlank(shopItemSettleExpressVo.getCustomTaxExpress())){
                                    shopItemSettleExpress.setShopId(shop.getId());
                                    shopItemSettleExpress.setShopItemId(shopItem.getId());
                                    shopItemSettleExpress.setNetPricePer(shopItemSettleExpressVo.getNetPricePer());
                                    shopItemSettleExpress.setTaxNetPricePer(shopItemSettleExpressVo.getTaxNetPricePer());
                                    shopItemSettleExpress.setServiceFeePer(shopItemSettleExpressVo.getServiceFeePer());
                                    shopItemSettleExpress.setTaxServiceFeePer(shopItemSettleExpressVo.getTaxServiceFeePer());
                                    shopItemSettleExpress.setTaxFeePer(shopItemSettleExpressVo.getTaxFeePer());
                                    shopItemSettleExpress.setCustomTaxFeePer(shopItemSettleExpressVo.getCustomTaxFeePer());
                                    shopItemSettleExpress.setDiscount(shopItemSettleExpressVo.getDiscount());
                                    shopItemSettleExpress.setAdjust(shopItemSettleExpressVo.getAdjust());
                                    shopItemSettleExpress.setSettleExpress(shopItemSettleExpressVo.getSettleExpress());
                                    shopItemSettleExpress.setCustomTaxExpress(shopItemSettleExpressVo.getCustomTaxExpress());
                                    shopItemSettleExpress.setDelFlag(DelFlagEnums.NORMAL.getCode());
                                    shopItemSettleExpress.setCreateTime(new Date());
                                    shopItemSettleExpress.setCreateUser("init");//初始化
                                    shopItemSettleExpress.setUpdateTime(new Date());
                                    shopItemSettleExpress.setUpdateUser("init");//初始化
                                    shopItemSettleExpressService.insert(shopItemSettleExpress);
                                }
                                //结算规则表
                                ShopItemSettlePriceRule shopItemSettlePriceRule = new ShopItemSettlePriceRule();
                                shopItemSettlePriceRule.setShopId(shop.getId());
                                shopItemSettlePriceRule.setShopItemId(shopItem.getId());
                                shopItemSettlePriceRule.setGift("D5");
                                //TODO:这个起止时间 老系统没有
                                shopItemSettlePriceRule.setStartDate(DateUtil.parse("2019-01-01","yyyy-MM-dd"));
                                shopItemSettlePriceRule.setEndDate(DateUtil.parse("2021-12-31","yyyy-MM-dd"));
                                shopItemSettlePriceRule.setMonday(shopItemSettleExpressVo.getMonday());
                                shopItemSettlePriceRule.setTuesday(shopItemSettleExpressVo.getTuesday());
                                shopItemSettlePriceRule.setWednesday(shopItemSettleExpressVo.getWednesday());
                                shopItemSettlePriceRule.setThursday(shopItemSettleExpressVo.getThursday());
                                shopItemSettlePriceRule.setFriday(shopItemSettleExpressVo.getFriday());
                                shopItemSettlePriceRule.setSaturday(shopItemSettleExpressVo.getSaturday());
                                shopItemSettlePriceRule.setSunday(shopItemSettleExpressVo.getSunday());
                                shopItemSettlePriceRule.setSettleExpressId(shopItemSettleExpress.getId());
                                shopItemSettlePriceRule.setDelFlag(DelFlagEnums.NORMAL.getCode());
                                shopItemSettlePriceRule.setCreateTime(new Date());
                                shopItemSettlePriceRule.setCreateUser("init");//初始化
                                shopItemSettlePriceRule.setUpdateTime(new Date());
                                shopItemSettlePriceRule.setUpdateUser("init");//初始化
                                shopItemSettlePriceRuleService.insert(shopItemSettlePriceRule);
                            }
                        }
                        //单免  结算规则
                        if (StringUtils.isNotBlank(shopItemPriceVo.getGiftF1())){
                            List<ShopItemSettleExpressVo> giftF1 = shopItemService.analysisSettleExpress(shopItemPriceVo.getGiftF1());
                            for (ShopItemSettleExpressVo shopItemSettleExpressVo : giftF1) {
                                log.info("结算规则解析：{}",shopItemSettleExpressVo.toString());
                                //结算规则中的公式表
                                ShopItemSettleExpress shopItemSettleExpress = new ShopItemSettleExpress();
                                if (StringUtils.isNotBlank(shopItemSettleExpressVo.getSettleExpress()) || StringUtils.isNotBlank(shopItemSettleExpressVo.getCustomTaxExpress())){
                                    shopItemSettleExpress.setShopId(shop.getId());
                                    shopItemSettleExpress.setShopItemId(shopItem.getId());
                                    shopItemSettleExpress.setNetPricePer(shopItemSettleExpressVo.getNetPricePer());
                                    shopItemSettleExpress.setTaxNetPricePer(shopItemSettleExpressVo.getTaxNetPricePer());
                                    shopItemSettleExpress.setServiceFeePer(shopItemSettleExpressVo.getServiceFeePer());
                                    shopItemSettleExpress.setTaxServiceFeePer(shopItemSettleExpressVo.getTaxServiceFeePer());
                                    shopItemSettleExpress.setTaxFeePer(shopItemSettleExpressVo.getTaxFeePer());
                                    shopItemSettleExpress.setCustomTaxFeePer(shopItemSettleExpressVo.getCustomTaxFeePer());
                                    shopItemSettleExpress.setDiscount(shopItemSettleExpressVo.getDiscount());
                                    shopItemSettleExpress.setAdjust(shopItemSettleExpressVo.getAdjust());
                                    shopItemSettleExpress.setSettleExpress(shopItemSettleExpressVo.getSettleExpress());
                                    shopItemSettleExpress.setCustomTaxExpress(shopItemSettleExpressVo.getCustomTaxExpress());
                                    shopItemSettleExpress.setDelFlag(DelFlagEnums.NORMAL.getCode());
                                    shopItemSettleExpress.setCreateTime(new Date());
                                    shopItemSettleExpress.setCreateUser("init");//初始化
                                    shopItemSettleExpress.setUpdateTime(new Date());
                                    shopItemSettleExpress.setUpdateUser("init");//初始化
                                    shopItemSettleExpressService.insert(shopItemSettleExpress);
                                }
                                //结算规则表
                                ShopItemSettlePriceRule shopItemSettlePriceRule = new ShopItemSettlePriceRule();
                                shopItemSettlePriceRule.setShopId(shop.getId());
                                shopItemSettlePriceRule.setShopItemId(shopItem.getId());
                                shopItemSettlePriceRule.setGift("F1");
                                //TODO:这个起止时间 老系统没有
                                shopItemSettlePriceRule.setStartDate(DateUtil.parse("2019-01-01","yyyy-MM-dd"));
                                shopItemSettlePriceRule.setEndDate(DateUtil.parse("2021-12-31","yyyy-MM-dd"));
                                shopItemSettlePriceRule.setMonday(shopItemSettleExpressVo.getMonday());
                                shopItemSettlePriceRule.setTuesday(shopItemSettleExpressVo.getTuesday());
                                shopItemSettlePriceRule.setWednesday(shopItemSettleExpressVo.getWednesday());
                                shopItemSettlePriceRule.setThursday(shopItemSettleExpressVo.getThursday());
                                shopItemSettlePriceRule.setFriday(shopItemSettleExpressVo.getFriday());
                                shopItemSettlePriceRule.setSaturday(shopItemSettleExpressVo.getSaturday());
                                shopItemSettlePriceRule.setSunday(shopItemSettleExpressVo.getSunday());
                                shopItemSettlePriceRule.setSettleExpressId(shopItemSettleExpress.getId());
                                shopItemSettlePriceRule.setDelFlag(DelFlagEnums.NORMAL.getCode());
                                shopItemSettlePriceRule.setCreateTime(new Date());
                                shopItemSettlePriceRule.setCreateUser("init");//初始化
                                shopItemSettlePriceRule.setUpdateTime(new Date());
                                shopItemSettlePriceRule.setUpdateUser("init");//初始化
                                shopItemSettlePriceRuleService.insert(shopItemSettlePriceRule);
                            }
                        }
                        //双免  结算规则
                        if (StringUtils.isNotBlank(shopItemPriceVo.getGiftF2())){
                            List<ShopItemSettleExpressVo> giftF2 = shopItemService.analysisSettleExpress(shopItemPriceVo.getGiftF2());
                            for (ShopItemSettleExpressVo shopItemSettleExpressVo : giftF2) {
                                log.info("结算规则解析：{}",shopItemSettleExpressVo.toString());
                                //结算规则中的公式表
                                ShopItemSettleExpress shopItemSettleExpress = new ShopItemSettleExpress();
                                if (StringUtils.isNotBlank(shopItemSettleExpressVo.getSettleExpress()) || StringUtils.isNotBlank(shopItemSettleExpressVo.getCustomTaxExpress())){
                                    shopItemSettleExpress.setShopId(shop.getId());
                                    shopItemSettleExpress.setShopItemId(shopItem.getId());
                                    shopItemSettleExpress.setNetPricePer(shopItemSettleExpressVo.getNetPricePer());
                                    shopItemSettleExpress.setTaxNetPricePer(shopItemSettleExpressVo.getTaxNetPricePer());
                                    shopItemSettleExpress.setServiceFeePer(shopItemSettleExpressVo.getServiceFeePer());
                                    shopItemSettleExpress.setTaxServiceFeePer(shopItemSettleExpressVo.getTaxServiceFeePer());
                                    shopItemSettleExpress.setTaxFeePer(shopItemSettleExpressVo.getTaxFeePer());
                                    shopItemSettleExpress.setCustomTaxFeePer(shopItemSettleExpressVo.getCustomTaxFeePer());
                                    shopItemSettleExpress.setDiscount(shopItemSettleExpressVo.getDiscount());
                                    shopItemSettleExpress.setAdjust(shopItemSettleExpressVo.getAdjust());
                                    shopItemSettleExpress.setSettleExpress(shopItemSettleExpressVo.getSettleExpress());
                                    shopItemSettleExpress.setCustomTaxExpress(shopItemSettleExpressVo.getCustomTaxExpress());
                                    shopItemSettleExpress.setDelFlag(DelFlagEnums.NORMAL.getCode());
                                    shopItemSettleExpress.setCreateTime(new Date());
                                    shopItemSettleExpress.setCreateUser("init");//初始化
                                    shopItemSettleExpress.setUpdateTime(new Date());
                                    shopItemSettleExpress.setUpdateUser("init");//初始化
                                    shopItemSettleExpressService.insert(shopItemSettleExpress);
                                }
                                //结算规则表
                                ShopItemSettlePriceRule shopItemSettlePriceRule = new ShopItemSettlePriceRule();
                                shopItemSettlePriceRule.setShopId(shop.getId());
                                shopItemSettlePriceRule.setShopItemId(shopItem.getId());
                                shopItemSettlePriceRule.setGift("F2");
                                //TODO:这个起止时间 老系统没有
                                shopItemSettlePriceRule.setStartDate(DateUtil.parse("2019-01-01","yyyy-MM-dd"));
                                shopItemSettlePriceRule.setEndDate(DateUtil.parse("2021-12-31","yyyy-MM-dd"));
                                shopItemSettlePriceRule.setMonday(shopItemSettleExpressVo.getMonday());
                                shopItemSettlePriceRule.setTuesday(shopItemSettleExpressVo.getTuesday());
                                shopItemSettlePriceRule.setWednesday(shopItemSettleExpressVo.getWednesday());
                                shopItemSettlePriceRule.setThursday(shopItemSettleExpressVo.getThursday());
                                shopItemSettlePriceRule.setFriday(shopItemSettleExpressVo.getFriday());
                                shopItemSettlePriceRule.setSaturday(shopItemSettleExpressVo.getSaturday());
                                shopItemSettlePriceRule.setSunday(shopItemSettleExpressVo.getSunday());
                                shopItemSettlePriceRule.setSettleExpressId(shopItemSettleExpress.getId());
                                shopItemSettlePriceRule.setDelFlag(DelFlagEnums.NORMAL.getCode());
                                shopItemSettlePriceRule.setCreateTime(new Date());
                                shopItemSettlePriceRule.setCreateUser("init");//初始化
                                shopItemSettlePriceRule.setUpdateTime(new Date());
                                shopItemSettlePriceRule.setUpdateUser("init");//初始化
                                shopItemSettlePriceRuleService.insert(shopItemSettlePriceRule);
                            }
                        }
                    }
                    shopItemList.add(shopItem);
                }
            }
            result.setResult(shopItemList);
            log.info("同步结束");
        }catch (Exception e){
            log.error("规格同步失败",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    @SysGodDoorLog("查询产品价格")
    @ApiOperation("查询产品价格")
    @PostMapping("/selectPriceList")
    public CommonResultVo<List<ShopItemPriceRes>> selectPriceList(@RequestBody List<Integer> shopIdlist){
        CommonResultVo<List<ShopItemPriceRes>> resultVo = new CommonResultVo<>();
        try {
            List<ShopItemPriceRes> shopItemPriceResList = shopItemService.selectPriceList(shopIdlist);
            resultVo.setResult(shopItemPriceResList);
        }catch (Exception e){
            log.error("查询产品价格异常:",e);
            resultVo.setCode(200);
            resultVo.setMsg("查询产品价格异常");
        }
        return resultVo;
    }

    @ApiOperation("查询商品项目信息")
    @PostMapping("/getShopItemInfo")
    public CommonResultVo<List<ShopItemRes>> getShopItemInfo(@RequestBody  List<Integer> shopItemIdList){
        CommonResultVo<List<ShopItemRes>> resultVo = new CommonResultVo<>();
        try {
            List<ShopItemRes> shopItemResList = shopItemService.getShopItemInfo(shopItemIdList);
            resultVo.setResult(shopItemResList);
        }catch (Exception e){
            resultVo.setCode(200);
            resultVo.setMsg("查询商品项目信息列表异常");
        }
        return resultVo;
    }


    @SysGodDoorLog("根据goods获取商品项目信息")
    @ApiOperation("根据goods获取商品项目信息")
    @PostMapping("/getShopItemId")
    CommonResultVo<GoodsShopItemIdRes> getShopItemId(@RequestBody Integer goodsId){
        CommonResultVo<GoodsShopItemIdRes> resultVo = new CommonResultVo<>();
        try {
            GoodsShopItemIdRes goodsShopItemIdRes = shopItemService.getShopItemId(goodsId);
            resultVo.setResult(goodsShopItemIdRes);
        }catch (Exception e){
            resultVo.setCode(200);
            resultVo.setMsg("查询商品项目信息列表异常");
        }
        return resultVo;
    }

    @SysGodDoorLog("查询商品项目信息")
    @ApiOperation("查询商品项目信息")
    @PostMapping("/getItems")
    CommonResultVo<List<ShopListQueryResVo>> getItems(@RequestBody List<Integer> goodsIdList){
        CommonResultVo<List<ShopListQueryResVo>> resultVo = new CommonResultVo<>();
        try {
            List<ShopListQueryResVo> shopListQueryResVoList = shopItemService.getItems(goodsIdList);
            resultVo.setResult(shopListQueryResVoList);
        }catch (Exception e){
            resultVo.setCode(200);
            resultVo.setMsg("查询商品项目信息列表异常");
        }
        return resultVo;
    }

    @SysGodDoorLog("获取市场参考价")
    @ApiOperation("获取市场参考价")
    @PostMapping("/selectShopItemPrice")
    CommonResultVo<List<ShopItemResVo>> selectShopItemPrice(@RequestParam("itemsIdList") List<Integer> itemsIdList ,@RequestParam("date") Date date){
        CommonResultVo<List<ShopItemResVo>> resultVo = new CommonResultVo<>();
        try {
            List<ShopItemInfoRes> shopItemNetPriceRuleList = shopItemService.selectShopItemPrice(itemsIdList,date);
            Integer week = DateUtils.dateForWeek(date);
            String weekDesc = WeekEnums.getDescByCode(week.toString());
            List<ShopItemResVo> shopItemResVoList = new ArrayList<>();
            for (ShopItemInfoRes shopItemInfoRes : shopItemNetPriceRuleList) {
                Class cla = shopItemInfoRes.getClass();
                //获得该类下面所有的字段集合
                Field[] fields = cla.getDeclaredFields();
                for(Field field : fields) {
                    String filedName = field.getName();
                    String firstLetter = filedName.substring(0,1).toUpperCase(); //获得字段第一个字母大写
                    String getMethodName = "get"+firstLetter+filedName.substring(1); //转换成字段的get方法
                    Method getMethod = null;
                    Object value = null;
                    try {
                        getMethod = cla.getMethod(getMethodName, new Class[] {});
                        value = getMethod.invoke(shopItemInfoRes, new Object[] {}); //这个对象字段get方法的值
                    } catch (NoSuchMethodException e) {
                        log.error("对象反射解析异常",e);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    //判断当时时间是否有价格
                    if (filedName.equals(weekDesc) && value != null && value.toString().equals("1")){
                        ShopItemResVo shopItemResVo = new ShopItemResVo();
                        BeanUtils.copyProperties(shopItemInfoRes,shopItemResVo);
                        //净价
                        shopItemResVo.setNet(shopItemInfoRes.getNetPrice().toString());
                        //增值税
                        shopItemResVo.setVat(shopItemInfoRes.getTaxRate().toString());
                        //服务费
                        shopItemResVo.setFee(shopItemInfoRes.getServiceRate().toString());
                        shopItemResVoList.add(shopItemResVo);
                    }
                }
            }
            resultVo.setResult(shopItemResVoList);
        }catch (Exception e){
            resultVo.setCode(200);
            resultVo.setMsg("查询商品项目信息列表异常");
        }
        return resultVo;
    }

    @SysGodDoorLog("获取商品定制套餐信息")
    @ApiOperation("获取商品定制套餐信息")
    @PostMapping("/getShopItemSetmenuInfo")
    public CommonResultVo<ShopItemSetmenuInfo> getShopItemSetmenuInfo(@RequestBody Integer shopItemId){
        CommonResultVo<ShopItemSetmenuInfo> resultVo = new CommonResultVo<>();
        try {
            ShopItemSetmenuInfo shopItemSetmenuInfo = shopItemService.getShopItemSetmenuInfo(shopItemId);
            //获取定制套餐图片
            ListSysFileReq sysFileReq = new ListSysFileReq();
            sysFileReq.setObjId(shopItemId);
            sysFileReq.setType(FileTypeEnums.SHOP_ITEM_PIC.getCode());
            List<SysFileDto> fileDtoList = sysFileService.listSysFileDto(sysFileReq);
            //图片数据封装
            if (CollectionUtil.isNotEmpty(fileDtoList)){
                List<String> menuImgList = new ArrayList<>();
                menuImgList = fileDtoList.stream().map(sysFileDto -> {
                    String imgUrl = "Https://" + sysFileDto.getPgCdnNoHttpFullUrl();
                    return imgUrl;
                }).collect(Collectors.toList());
                shopItemSetmenuInfo.setMenuImgList(menuImgList);
            }
            resultVo.setResult(shopItemSetmenuInfo);
        }catch (Exception e){
            log.error("获取商品定制套餐信息异常:",e);
            resultVo.setCode(200);
            resultVo.setMsg("获取商品定制套餐信息异常");
        }
        return resultVo;
    }
    @SysGodDoorLog("获取所有商户规格信息")
    @ApiOperation("获取所有商户规格信息")
    @PostMapping("/getShopItemList")
    public CommonResultVo<List<ShopItem>> getShopItemList(){

        CommonResultVo<List<ShopItem>> result = new CommonResultVo<>();
        try {
            Wrapper<ShopItem> shopItemWrapper=new Wrapper<ShopItem>() {
                @Override
                public String getSqlSegment() {
                    return "where old_item_id <> 0";
                }
            };
            final List<ShopItem> shopItemList = shopItemService.selectList(shopItemWrapper);
            result.setResult(shopItemList);
        }catch (Exception e){
            log.error("获取所有商户规格信息失败",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    @SysGodDoorLog("根据shopitemids查询规格信息")
    @ApiOperation("根据shopitemids查询规格信息")
    @PostMapping("/selectByItems")
    public CommonResultVo<List<ShopItemConciseRes>> selectByItems(@RequestBody List<Integer> items){
        CommonResultVo<List<ShopItemConciseRes>> result = new CommonResultVo<>();
        try {
            Assert.notEmpty(items,"商户规格ids不能为空");
            List<ShopItemConciseRes> list = shopItemService.selectByItems(items);
            result.setResult(list);
        }catch (Exception e){
            log.error("根据shopitemids查询规格信息失败",e);
            result.setCode(200);
            result.setMsg("根据shopitemids查询规格信息失败");
        }
        return result;
    }

    @SysGodDoorLog("大批量被block数据拉取")
    @ApiOperation("大批量被block数据拉取")
    @PostMapping("/export")
    public CommonResultVo<String> export(){
        CommonResultVo<String> result = new CommonResultVo();
        try {
            String file = shopItemService.export();
            result.setResult(file);
        }catch (Exception e){
            log.error("数据拉取失败",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    @SysGodDoorLog("大批量商户停售跑批")
    @ApiOperation("大批量商户停售跑批")
    @PostMapping("/offShopItem")
    public CommonResultVo<String> offShopItem(){
        CommonResultVo<String> result = new CommonResultVo();
        try {
            String file = shopItemService.offShopItem();
            result.setResult(file);
        }catch (Exception e){
            log.error("大批量商户停售跑批失败",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }
}