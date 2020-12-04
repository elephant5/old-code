package com.colourfulchina.pangu.taishang.controller;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.colourfulchina.god.door.api.annotation.SysGodDoorLog;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.pangu.taishang.api.dto.BlockRule;
import com.colourfulchina.pangu.taishang.api.dto.BookNum;
import com.colourfulchina.pangu.taishang.api.entity.*;
import com.colourfulchina.pangu.taishang.api.vo.BlockBookDaysVo;
import com.colourfulchina.pangu.taishang.api.vo.GenerateBlockRuleVo;
import com.colourfulchina.pangu.taishang.api.vo.req.GoodsBaseVo;
import com.colourfulchina.pangu.taishang.api.vo.req.QueryBookBlockReq;
import com.colourfulchina.pangu.taishang.api.vo.req.QueryCallBookReq;
import com.colourfulchina.pangu.taishang.api.vo.req.QueryOrderExpireTimeReq;
import com.colourfulchina.pangu.taishang.api.vo.res.QueryBookBlockRes;
import com.colourfulchina.pangu.taishang.service.*;
import com.colourfulchina.pangu.taishang.utils.BookMinMaxDayUtils;
import com.colourfulchina.pangu.taishang.utils.DateUtils;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/blockRule")
@Slf4j
@Api(value = "block规则controller",tags = {"block规则操作接口"})
public class BlockRuleController {
    @Autowired
    private BlockRuleService blockRuleService;
    @Autowired
    private ShopProtocolService shopProtocolService;
    @Autowired
    private ProductGroupProductService productGroupProductService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ShopItemService shopItemService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private ProductGroupService productGroupService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private HotelService hotelService;
    @Autowired
    private CityService cityService;
    @Autowired
    private GroupProductBlockDateService groupProductBlockDateService;

    @SysGodDoorLog("生成block规则")
    @ApiOperation("生成block规则")
    @PostMapping("/generateBlockRule")
    public CommonResultVo<List<BlockRule>> generateBlockRule(@RequestBody GenerateBlockRuleVo generateBlockRuleVo){
        CommonResultVo<List<BlockRule>>  result = new CommonResultVo<>();
        try {
            List<BlockRule> blockRuleList = blockRuleService.generateBlockRule(generateBlockRuleVo);
            result.setResult(blockRuleList);
        }catch (Exception e){
            log.error("block规则生成失败",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    /**
     * 编辑block，前端回显
     * @param blockRule
     * @return
     */
    @SysGodDoorLog("编辑block")
    @ApiOperation("编辑block，前端回显")
    @PostMapping("/editBlockRule")
    public CommonResultVo<GenerateBlockRuleVo> editBlockRule(@RequestBody BlockRule blockRule){
        CommonResultVo<GenerateBlockRuleVo> result = new CommonResultVo<>();
        try {
            GenerateBlockRuleVo generateBlockRuleVo = blockRuleService.editBlockRule(blockRule);
            result.setResult(generateBlockRuleVo);
        }catch (Exception e){
            log.error("编辑block错误",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    /**
     * 查询不可预约时间字符串
     * @param queryBookBlockReq
     * @return
     */
    @SysGodDoorLog("查询不可预约时间字符串")
    @ApiOperation("查询不可预约时间字符串")
    @PostMapping("/queryAllBlock")
    public CommonResultVo<QueryBookBlockRes> queryAllBlock(@RequestBody QueryBookBlockReq queryBookBlockReq){
        CommonResultVo<QueryBookBlockRes> result = new CommonResultVo<>();
        QueryBookBlockRes queryBookBlockRes = null;
        try {
            Assert.notNull(queryBookBlockReq,"参数不能为空");
            Assert.notNull(queryBookBlockReq.getProductGroupProductId(),"产品组和产品关系id不能为空");
            queryBookBlockRes = blockRuleService.queryAllBlock(queryBookBlockReq.getProductGroupProductId());
            result.setResult(queryBookBlockRes);
        }catch (Exception e){
            log.error("查询不可预约时间字符串失败",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    /**
     * 查询预约的block规则和可预约时间范围(已激活的码)
     * @param queryBookBlockReq
     * @return
     */
    @SysGodDoorLog("查询预约的block规则和可预约时间范围(已激活的码)")
    @ApiOperation("查询预约的block规则和可预约时间范围(已激活的码)")
    @PostMapping("/queryBookBlock")
    public CommonResultVo<QueryBookBlockRes> queryBookBlock(@RequestBody QueryBookBlockReq queryBookBlockReq){
        CommonResultVo<QueryBookBlockRes> result = new CommonResultVo<>();
        QueryBookBlockRes queryBookBlockRes = new QueryBookBlockRes();
        int hour = DateUtil.hour(new Date(),true);
        try {
            Assert.notNull(queryBookBlockReq,"参数不能为空");
            Assert.notNull(queryBookBlockReq.getGoodsId(),"商品id不能为空");
            Assert.notNull(queryBookBlockReq.getProductGroupId(),"产品组id不能为空");
            Assert.notNull(queryBookBlockReq.getShopId(),"商户id不能为空");
            Assert.notNull(queryBookBlockReq.getProductGroupProductId(),"产品组和产品关系id不能为空");
            List<String> allBlockList = Lists.newLinkedList();
            String allBlock = null;
            //查询商户信息
            Shop shop = shopService.selectById(queryBookBlockReq.getShopId());
            //查询酒店信息
            Hotel hotel = null;
            if (shop.getHotelId()!=null){
                hotel = hotelService.selectById(shop.getHotelId());
            }
            //查询城市信息
            Integer cityId;
            if (shop.getCityId()!=null){
                cityId = shop.getCityId();
            }else if (hotel != null){
                cityId = hotel.getCityId();
            }else {
                cityId = null;
            }
            City city = new City();
            if (cityId != null){
                city = cityService.selectById(cityId);
            }
            //查询商户协议
            ShopProtocol shopProtocol = shopProtocolService.selectById(queryBookBlockReq.getShopId());
            //查询产品组的产品
            ProductGroupProduct productGroupProduct = productGroupProductService.selectById(queryBookBlockReq.getProductGroupProductId());
            //查询产品
            Product product = productService.selectById(productGroupProduct.getProductId());
            //查询规格
            ShopItem shopItem = shopItemService.selectById(product.getShopItemId());
            //查询商品
            Goods goods = goodsService.selectById(queryBookBlockReq.getGoodsId());
            //查询产品组
            ProductGroup productGroup = productGroupService.selectById(queryBookBlockReq.getProductGroupId());
            if (StringUtils.isNotBlank(shopProtocol.getBlockRule())){
                allBlockList.add(shopProtocol.getBlockRule());
            }
            if (StringUtils.isNotBlank(productGroupProduct.getBlockRule())){
                allBlockList.add(productGroupProduct.getBlockRule());
            }
            if (StringUtils.isNotBlank(shopItem.getBlockRule())){
                allBlockList.add(shopItem.getBlockRule());
            }
            if (StringUtils.isNotBlank(goods.getBlock())){
                allBlockList.add(goods.getBlock());
            }
            if (StringUtils.isNotBlank(productGroup.getBlockRule())){
                allBlockList.add(productGroup.getBlockRule());
            }
            if (!CollectionUtils.isEmpty(allBlockList)){
                allBlock = StringUtils.join(allBlockList,", ");
            }
            if (StringUtils.isNotBlank(allBlock)){
                List<BlockRule> block = blockRuleService.blockRuleStr2list(allBlock);
                if (!CollectionUtils.isEmpty(block)){
                    List<BlockRule> tempBlock = blockRuleService.getEffectBlockRule(block);
                    if (!CollectionUtils.isEmpty(tempBlock)){
                        queryBookBlockRes.setBlockRule(tempBlock.stream().map(rule -> rule.getNatural()).collect(Collectors.joining("; ")));
                    }
                }
            }
            //查询权益有效期
            Date startDate;//有效范围开始时间
            Date endDate;//有效范围结束时间
            if ("drink".equals(shopItem.getServiceType()) || shopItem.getServiceType().indexOf("_cpn") > -1){
                startDate = new Date();
                endDate = new Date();
            }else {
                //商户中最小提前预约天数
                Integer shopMinBookDay = shop.getMinBookDays();
                if (hour>=17){
                    if (shopMinBookDay != null){
                        shopMinBookDay = shopMinBookDay + 1;
                    }
                }
                //商户中最大提起预约天数
                Integer shopMaxBookDay = null;
                if (shopMinBookDay != null && shop.getMaxBookDays() != null){
                    shopMaxBookDay = shop.getMaxBookDays()+shopMinBookDay;
                }
                //商品中最小提前预约天数
                Integer goodsMinBookDay = goods.getMinBookDays();
                if (hour>=17){
                    if (goodsMinBookDay != null){
                        goodsMinBookDay = goodsMinBookDay + 1;
                    }
                }
                //商品中最大提前预约天数
                Integer goodsMaxBookDay = null;
                if (goodsMinBookDay != null && goods.getMaxBookDays() != null){
                    goodsMaxBookDay = goods.getMaxBookDays()+goodsMinBookDay;
                }
                //产品组中最小提前预约天数
                Integer productGroupMinBookDay = productGroup.getMinBookDays();
                if (hour>=17){
                    if (productGroupMinBookDay != null){
                        productGroupMinBookDay = productGroupMinBookDay + 1;
                    }
                }
                //产品组中最大提前预约天数
                Integer productGroupMaxBookDay = null;
                if (productGroupMinBookDay != null && productGroup.getMaxBookDays() != null){
                    productGroupMaxBookDay = productGroup.getMaxBookDays()+productGroupMinBookDay;
                }
                //综合最小提前预约天数
                Integer minBookDay = maxInteger(shopMinBookDay,maxInteger(goodsMinBookDay,productGroupMinBookDay));
                //综合最大提前预约天数
                Integer maxBookDay = minInteger(shopMaxBookDay,minInteger(goodsMaxBookDay,productGroupMaxBookDay));
                //获取资源默认的最小最大提前预约时间
                BookNum bookNum = BookMinMaxDayUtils.getBookByService(shopItem.getServiceType(),city.getCountryId());
                if (minBookDay == null){
                    minBookDay = bookNum.getMinBook();
                }
                if (maxBookDay == null){
                    maxBookDay = bookNum.getMaxBook();
                }
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                calendar.add(Calendar.DAY_OF_MONTH,minBookDay);
                startDate = calendar.getTime();
                calendar.setTime(new Date());
                calendar.add(Calendar.DAY_OF_MONTH,maxBookDay);
                endDate = calendar.getTime();
            }
            //获取权益到期时间
            Date expiryDate = queryBookBlockReq.getActExpireTime() != null ? DateUtil.parse(DateUtil.format(queryBookBlockReq.getActExpireTime(),"yyyy-MM-dd"),"yyyy-MM-dd") : null;
            startDate = DateUtil.parse(DateUtil.format(startDate,"yyyy-MM-dd"),"yyyy-MM-dd");
            endDate = DateUtil.parse(DateUtil.format(endDate,"yyyy-MM-dd"),"yyyy-MM-dd");
            if (expiryDate != null){
                if (expiryDate.compareTo(startDate) < 0){
                    result.setResult(queryBookBlockRes);
                    return result;
                }else if (expiryDate.compareTo(startDate) >= 0 && expiryDate.compareTo(endDate) <=0){
                    endDate = expiryDate;
                }
                //券类型资源预定范围到权益结束
                if (shopItem.getServiceType().indexOf("_cpn") > -1){
                    endDate = expiryDate;
                }
            }
            List<Date> bookDates = blockRuleService.generateBookDate(startDate,endDate,allBlock);
            //券类型不考虑设置的block
            if (shopItem.getServiceType().indexOf("_cpn") > -1){
                bookDates = DateUtils.containDateList(startDate,endDate,null);
            }
            queryBookBlockRes.setBookDates(bookDates);
            result.setResult(queryBookBlockRes);
        }catch (Exception e){
            log.error("查询预约的block规则和可预约时间范围失败(已激活的码):{}",JSON.toJSONString(queryBookBlockReq),e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    /**
     * 查询预约的block规则和可预约时间范围(已激活的码优化)
     * @param queryBookBlockReq
     * @return
     */
    @SysGodDoorLog("查询预约的block规则和可预约时间范围(已激活的码优化)")
    @ApiOperation("查询预约的block规则和可预约时间范围(已激活的码优化)")
    @PostMapping("/queryBookBlockNew")
    public CommonResultVo<QueryBookBlockRes> queryBookBlockNew(@RequestBody QueryBookBlockReq queryBookBlockReq){
        CommonResultVo<QueryBookBlockRes> result = new CommonResultVo<>();
        QueryBookBlockRes queryBookBlockRes = new QueryBookBlockRes();
        try {
            Assert.notNull(queryBookBlockReq,"参数不能为空");
            Assert.notNull(queryBookBlockReq.getProductGroupProductId(),"产品组和产品关系id不能为空");
            List<Date> bookDates = blockRuleService.queryBookBlockNew(queryBookBlockReq);
            queryBookBlockRes.setBookDates(bookDates);
            result.setResult(queryBookBlockRes);
        }catch (Exception e){
            log.error("查询预约的block规则和可预约时间范围失败(已激活的码):{}",JSON.toJSONString(queryBookBlockReq),e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    /**
     * 查询预约的block规则和可预约时间范围(已出库的码)
     * @param queryBookBlockReq
     * @return
     */
    @SysGodDoorLog("查询预约的block规则和可预约时间范围(已出库的码)")
    @ApiOperation("查询预约的block规则和可预约时间范围(已出库的码)")
    @PostMapping("/queryOutCodeBookBlock")
    public CommonResultVo<QueryBookBlockRes> queryOutCodeBookBlock(@RequestBody QueryBookBlockReq queryBookBlockReq){
        CommonResultVo<QueryBookBlockRes> result = new CommonResultVo<>();
        QueryBookBlockRes queryBookBlockRes = new QueryBookBlockRes();
        int hour = DateUtil.hour(new Date(),true);
        try {
            Assert.notNull(queryBookBlockReq,"参数不能为空");
            Assert.notNull(queryBookBlockReq.getGoodsId(),"商品id不能为空");
            Assert.notNull(queryBookBlockReq.getProductGroupId(),"产品组id不能为空");
            Assert.notNull(queryBookBlockReq.getShopId(),"商户id不能为空");
            Assert.notNull(queryBookBlockReq.getProductGroupProductId(),"产品组和产品关系id不能为空");
            Assert.notNull(queryBookBlockReq.getOutDate(),"出库时间不能为空");
            Assert.notNull(queryBookBlockReq.getActivationDate(),"激活时间不能为空");
            List<String> allBlockList = Lists.newLinkedList();
            String allBlock = null;
            //查询商户信息
            Shop shop = shopService.selectById(queryBookBlockReq.getShopId());
            //查询酒店信息
            Hotel hotel = null;
            if (shop.getHotelId()!=null){
                hotel = hotelService.selectById(shop.getHotelId());
            }
            //查询城市信息
            Integer cityId;
            if (shop.getCityId()!=null){
                cityId = shop.getCityId();
            }else if (hotel != null){
                cityId = hotel.getCityId();
            }else {
                cityId = null;
            }
            City city = new City();
            if (cityId != null){
                city = cityService.selectById(cityId);
            }
            //查询商户协议
            ShopProtocol shopProtocol = shopProtocolService.selectById(queryBookBlockReq.getShopId());
            //查询产品组的产品
            ProductGroupProduct productGroupProduct = productGroupProductService.selectById(queryBookBlockReq.getProductGroupProductId());
            //查询产品
            Product product = productService.selectById(productGroupProduct.getProductId());
            //查询规格
            ShopItem shopItem = shopItemService.selectById(product.getShopItemId());
            //查询商品
            Goods goods = goodsService.selectById(queryBookBlockReq.getGoodsId());
            //查询产品组
            ProductGroup productGroup = productGroupService.selectById(queryBookBlockReq.getProductGroupId());
            if (StringUtils.isNotBlank(shopProtocol.getBlockRule())){
                allBlockList.add(shopProtocol.getBlockRule());
            }
            if (StringUtils.isNotBlank(productGroupProduct.getBlockRule())){
                allBlockList.add(productGroupProduct.getBlockRule());
            }
            if (StringUtils.isNotBlank(shopItem.getBlockRule())){
                allBlockList.add(shopItem.getBlockRule());
            }
            if (StringUtils.isNotBlank(goods.getBlock())){
                allBlockList.add(goods.getBlock());
            }
            if (StringUtils.isNotBlank(productGroup.getBlockRule())){
                allBlockList.add(productGroup.getBlockRule());
            }
            if (!CollectionUtils.isEmpty(allBlockList)){
                allBlock = StringUtils.join(allBlockList,", ");
            }
            if (StringUtils.isNotBlank(allBlock)){
                List<BlockRule> block = blockRuleService.blockRuleStr2list(allBlock);
                if (!CollectionUtils.isEmpty(block)){
                    List<BlockRule> tempBlock = blockRuleService.getEffectBlockRule(block);
                    if (!CollectionUtils.isEmpty(tempBlock)){
                        queryBookBlockRes.setBlockRule(tempBlock.stream().map(rule -> rule.getNatural()).collect(Collectors.joining("; ")));
                    }
                }
            }
            //查询权益有效期
            Date startDate;//有效范围开始时间
            Date endDate;//有效范围结束时间
            QueryOrderExpireTimeReq queryOrderExpireTimeReq = new QueryOrderExpireTimeReq();
            BeanUtils.copyProperties(queryBookBlockReq,queryOrderExpireTimeReq);
            GoodsBaseVo goodsBaseVo = goodsService.queryOrderExpireTime(queryOrderExpireTimeReq);
            if ("drink".equals(shopItem.getServiceType()) || shopItem.getServiceType().indexOf("_cpn") > -1){
                startDate = new Date();
                endDate = new Date();
            }else {
                //商户中最小提前预约天数
                Integer shopMinBookDay = shop.getMinBookDays();
                if (hour>=17){
                    if (shopMinBookDay != null){
                        shopMinBookDay = shopMinBookDay + 1;
                    }
                }
                //商户中最大提起预约天数
                Integer shopMaxBookDay = null;
                if (shopMinBookDay != null && shop.getMaxBookDays() != null){
                    shopMaxBookDay = shop.getMaxBookDays()+shopMinBookDay;
                }
                //商品中最小提前预约天数
                Integer goodsMinBookDay = goods.getMinBookDays();
                if (hour>=17){
                    if (goodsMinBookDay != null){
                        goodsMinBookDay = goodsMinBookDay + 1;
                    }
                }
                //商品中最大提前预约天数
                Integer goodsMaxBookDay = null;
                if (goodsMinBookDay != null && goods.getMaxBookDays() != null){
                    goodsMaxBookDay = goods.getMaxBookDays()+goodsMinBookDay;
                }
                //产品组中最小提前预约天数
                Integer productGroupMinBookDay = productGroup.getMinBookDays();
                if (hour>=17){
                    if (productGroupMinBookDay != null){
                        productGroupMinBookDay = productGroupMinBookDay + 1;
                    }
                }
                //产品组中最大提前预约天数
                Integer productGroupMaxBookDay = null;
                if (productGroupMinBookDay != null && productGroup.getMaxBookDays() != null){
                    productGroupMaxBookDay = productGroup.getMaxBookDays()+productGroupMinBookDay;
                }
                //综合最小提前预约天数
                Integer minBookDay = maxInteger(shopMinBookDay,maxInteger(goodsMinBookDay,productGroupMinBookDay));
                //综合最大提前预约天数
                Integer maxBookDay = minInteger(shopMaxBookDay,minInteger(goodsMaxBookDay,productGroupMaxBookDay));
                //获取资源默认的最小最大提前预约时间
                BookNum bookNum = BookMinMaxDayUtils.getBookByService(shopItem.getServiceType(),city.getCountryId());
                if (minBookDay == null){
                    minBookDay = bookNum.getMinBook();
                }
                if (maxBookDay == null){
                    maxBookDay = bookNum.getMaxBook();
                }
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                calendar.add(Calendar.DAY_OF_MONTH,minBookDay);
                startDate = calendar.getTime();
                calendar.setTime(new Date());
                calendar.add(Calendar.DAY_OF_MONTH,maxBookDay);
                endDate = calendar.getTime();
            }
            //获取权益到期时间
            String expiryStr = goodsBaseVo.getExpiryDate();
            Date expiryDate;
            if (expiryStr.equals("NULL")){
                expiryDate = null;
            }else {
                expiryDate = DateUtil.parse(expiryStr,"yyyy-MM-dd");
            }
            startDate = DateUtil.parse(DateUtil.format(startDate,"yyyy-MM-dd"),"yyyy-MM-dd");
            endDate = DateUtil.parse(DateUtil.format(endDate,"yyyy-MM-dd"),"yyyy-MM-dd");
            if (expiryDate != null){
                if (expiryDate.compareTo(startDate) < 0){
                    result.setResult(queryBookBlockRes);
                    return result;
                }else if (expiryDate.compareTo(startDate) >= 0 && expiryDate.compareTo(endDate) <=0){
                    endDate = expiryDate;
                }
                //券类型资源预定范围到权益结束
                if (shopItem.getServiceType().indexOf("_cpn") > -1){
                    endDate = expiryDate;
                }
            }
            List<Date> bookDates = blockRuleService.generateBookDate(startDate,endDate,allBlock);
            //券类型不考虑设置的block
            if (shopItem.getServiceType().indexOf("_cpn") > -1){
                bookDates = DateUtils.containDateList(startDate,endDate,null);
            }
            queryBookBlockRes.setBookDates(bookDates);
            result.setResult(queryBookBlockRes);
        }catch (Exception e){
            log.error("查询预约的block规则和可预约时间范围失败(已出库的码)",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    /**
     * 来电录单初步查询指定时间能否预约（商户）(已激活的码)
     * @param req
     * @return
     */
    @SysGodDoorLog("来电录单初步查询指定时间能否预约（商户）(已激活的码)")
    @ApiOperation("来电录单初步查询指定时间能否预约（商户）(已激活的码)")
    @PostMapping("/queryActCallBook")
    public CommonResultVo<List<Integer>> queryActCallBook(@RequestBody QueryCallBookReq req){
        CommonResultVo<List<Integer>> result = new CommonResultVo<>();
        try {
            Assert.notNull(req,"参数不能为空");
            Assert.notNull(req.getGoodsId(),"商品id不能为空");
            Assert.notNull(req.getProductGroupId(),"产品组id不能为空");
            Assert.notNull(req.getBookDate(),"预约时间不能为空");
            List<Integer> shopIds = blockRuleService.queryActCallBook(req);
            result.setResult(shopIds);
        }catch (Exception e){
            log.error("来电录单初步查询指定时间能否预约（商户）失败(已激活的码):{}",JSON.toJSONString(req),e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    /**
     * 来电录单初步查询指定时间能否预约（商户）(已出库的码)
     * @param req
     * @return
     */
    @SysGodDoorLog("来电录单初步查询指定时间能否预约（商户）(已出库的码)")
    @ApiOperation("来电录单初步查询指定时间能否预约（商户）(已出库的码)")
    @PostMapping("/queryOutCallBook")
    public CommonResultVo<List<Integer>> queryOutCallBook(@RequestBody QueryCallBookReq req){
        CommonResultVo<List<Integer>> result = new CommonResultVo<>();
        try {
            Assert.notNull(req,"参数不能为空");
            Assert.notNull(req.getGoodsId(),"商品id不能为空");
            Assert.notNull(req.getProductGroupId(),"产品组id不能为空");
            Assert.notNull(req.getOutDate(),"出库时间不能为空");
            Assert.notNull(req.getActivationDate(),"激活时间不能为空");
            Assert.notNull(req.getBookDate(),"预约时间不能为空");
            List<Integer> shopIds = blockRuleService.queryOutCallBook(req);
            result.setResult(shopIds);
        }catch (Exception e){
            log.error("来电录单初步查询指定时间能否预约（商户）失败(已出库的码)",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    /**
     * 获取俩个integer数据中小的数据
     * @param one
     * @param two
     * @return
     */
    public Integer minInteger(Integer one,Integer two){
        if (one == null && two != null){
            return two;
        }
        if (one != null && two == null){
            return one;
        }
        if (one != null && two != null){
            if (one.compareTo(two) > 0){
                return two;
            }else {
                return one;
            }
        }
        return null;
    }

    /**
     * 获取俩个integer数据中大的数据
     * @param one
     * @param two
     * @return
     */
    public Integer maxInteger(Integer one,Integer two){
        if (one == null && two != null){
            return two;
        }
        if (one != null && two == null){
            return one;
        }
        if (one != null && two != null){
            if (one.compareTo(two) > 0){
                return one;
            }else {
                return two;
            }
        }
        return null;
    }
}
