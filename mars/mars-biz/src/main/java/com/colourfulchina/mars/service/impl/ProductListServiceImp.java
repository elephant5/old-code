package com.colourfulchina.mars.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.inf.base.vo.PageVo;
import com.colourfulchina.mars.api.entity.GiftCode;
import com.colourfulchina.mars.api.enums.ReservOrderStatusEnums;
import com.colourfulchina.mars.api.vo.GoodsListReqVo;
import com.colourfulchina.mars.api.vo.ReservOrderProductVo;
import com.colourfulchina.mars.api.vo.req.BookPayReq;
import com.colourfulchina.mars.api.vo.res.*;
import com.colourfulchina.mars.config.ThreadPoolTaskExecutorConfig;
import com.colourfulchina.mars.mapper.ProductListMapper;
import com.colourfulchina.mars.service.GiftCodeService;
import com.colourfulchina.mars.service.PanguInterfaceService;
import com.colourfulchina.mars.service.ProductListService;
import com.colourfulchina.mars.service.ReservOrderAttachService;
import com.colourfulchina.mars.utils.HelpUtils;
import com.colourfulchina.pangu.taishang.api.dto.SysFileDto;
import com.colourfulchina.pangu.taishang.api.entity.ProductGroup;
import com.colourfulchina.pangu.taishang.api.entity.ShopItem;
import com.colourfulchina.pangu.taishang.api.entity.ShopItemNetPriceRule;
import com.colourfulchina.pangu.taishang.api.enums.ResourceTypeEnums;
import com.colourfulchina.pangu.taishang.api.feign.RemoteBlockRuleService;
import com.colourfulchina.pangu.taishang.api.feign.RemoteProductGroupProductService;
import com.colourfulchina.pangu.taishang.api.feign.RemoteProductGroupService;
import com.colourfulchina.pangu.taishang.api.feign.RemoteShopService;
import com.colourfulchina.pangu.taishang.api.vo.req.QueryBookBlockReq;
import com.colourfulchina.pangu.taishang.api.vo.req.SelectBookPayReq;
import com.colourfulchina.pangu.taishang.api.vo.req.SelectBookProductReq;
import com.colourfulchina.pangu.taishang.api.vo.req.SelectBookShopItemReq;
import com.colourfulchina.pangu.taishang.api.vo.res.*;
import com.colourfulchina.pangu.taishang.api.vo.res.bookBasePayment.BookBasePaymentRes;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.security.PublicKey;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * @Author: Nickal
 * @Description:
 * @Date: 2019/5/17 16:13
 */
@AllArgsConstructor
@Slf4j
@Service
public class ProductListServiceImp implements ProductListService {
    @Autowired
    private ProductListMapper productListMapper;

    @Autowired
    private GiftCodeService giftCodeService;

    private final RemoteProductGroupProductService remoteProductGroupProductService;

    private final RemoteBlockRuleService remoteBlockRuleService;

    private final RemoteProductGroupService remoteProductGroupService;

    private final RemoteShopService remoteShopService;

    @Autowired
    private PanguInterfaceService panguInterfaceService;

    @SuppressWarnings("rawtypes")
    @Autowired
    protected RedisTemplate redisTemplate;

//    private static ExecutorService executorService = Executors.newFixedThreadPool(100);

    @Override
    public List<QueryListResDto> getProductList(Integer groupId, String service) throws Exception {
        log.info("参数groupId={}", groupId);
        if (groupId == null) {
            throw new Exception("参数不能为空");
        }
        SelectBookProductReq selectBookProductReq = new SelectBookProductReq();
        selectBookProductReq.setProductGroupId(groupId);
        selectBookProductReq.setServiceType(service);

        CommonResultVo<List<SelectBookProductRes>> selectproductlist = remoteProductGroupProductService.selectBookProduct(selectBookProductReq);
        if (selectproductlist == null)
            throw new Exception("调用产品列表接口返回值为空");
        List<SelectBookProductRes> resultList = selectproductlist.getResult();
        List<Map> list = new ArrayList<>();
        Map map;
        List<QueryListResDto> queryListResDtos = Lists.newArrayList();
        //价格信息查询 调用luoxing接口
        for (SelectBookProductRes Res : resultList) {

            List<ShopItemNetPriceRule> priceRules = Res.getPriceRuleList();
            BigDecimal shopPriceRule = this.getPrice(priceRules);
            map = new HashMap();
            map.put("res", Res);
            map.put("shopPriceRule", shopPriceRule);
            //折扣价  原价 - 产品组中折扣比例 discountRate
            BigDecimal discountRate = getPrice(shopPriceRule, Res.getProductGroup().getDiscountRate());
            map.put("discountRate", discountRate);
            list.add(map);

            QueryListResDto resDto = new QueryListResDto();
            resDto.setHotel(Res.getHotel());
            resDto.setProductGroup(Res.getProductGroup());
            if (!Res.getShopPics().isEmpty()) {
                SysFileDto sysFile = Res.getShopPics().get(0);
                resDto.setShopPic(sysFile.getPgCdnHttpUrl() + '/' + sysFile.getGuid() + '.' + sysFile.getExt());
            }
            ShopResDto shopResDto = new ShopResDto();
            List<ShopItemResDto> shopItemResDtoList = Lists.newArrayList();
            shopResDto.setShop(Res.getShop());
            ShopItemResDto shopItemResDto = new ShopItemResDto();
            shopItemResDto.setShopItem(Res.getShopItem());
            shopItemResDto.setPriceRules(Res.getPriceRuleList());
            shopItemResDto.setProductGroupProduct(Res.getProductGroupProduct());
            shopItemResDto.setShopItemPics(Res.getShopItemPics());
            shopItemResDto.setDiscountRate(discountRate);
            shopItemResDto.setShopPriceRule(shopPriceRule);
            shopItemResDtoList.add(shopItemResDto);

            shopResDto.setShopItemResDtoList(shopItemResDtoList);
            resDto.setShopResDto(shopResDto);
            queryListResDtos.add(resDto);

        }
        List<QueryListResDto> result = Lists.newArrayList();
        Map<String, QueryListResDto> resDtoMap = new HashMap<String, QueryListResDto>();
        if (!CollectionUtils.isEmpty(queryListResDtos)) {
            for (QueryListResDto s : queryListResDtos) {
                if (resDtoMap.containsKey(s.getShopResDto().getShop().getId().toString())) {
                    if (!CollectionUtils.isEmpty(result)) {
                        for (QueryListResDto resDto : result) {
                            QueryListResDto listResDto = new QueryListResDto();
                            if (s.getShopResDto().getShop().getId().intValue() == resDto.getShopResDto().getShop().getId().intValue()) {
                                listResDto.setHotel(resDto.getHotel());
                                listResDto.setProductGroup(resDto.getProductGroup());
                                ShopResDto shopResDto = new ShopResDto();
                                shopResDto.setShop(resDto.getShopResDto().getShop());
                                List<ShopItemResDto> shopItemResDtoList = resDto.getShopResDto().getShopItemResDtoList();
                                shopItemResDtoList.add(s.getShopResDto().getShopItemResDtoList().get(0));
                                shopResDto.setShopItemResDtoList(shopItemResDtoList);
                                listResDto.setShopResDto(shopResDto);
                                result.remove(resDto);
                                result.add(listResDto);
                                break;
                            }
                        }
                    }

                } else {
                    resDtoMap.put(s.getShopResDto().getShop().getId().toString(), s);
                    result.add(s);
                }
            }
        }
        return result;
    }


    @Override
    public ProductDetailResVo selectProductDetail(Integer productGroupProductId, Integer giftCodeId) throws Exception {
        log.info("参数productGroupProductId={}", productGroupProductId);
        if (productGroupProductId == null) {
            throw new Exception("参数不能为空");
        }
        CommonResultVo<GroupProductDetailRes> productdetail = remoteProductGroupProductService.selectProductDetail(productGroupProductId);
        if (productdetail == null) {
            throw new Exception("调用产品详情接口返回值为空");
        }
        ProductDetailResVo resultVo = new ProductDetailResVo();
        resultVo.setProductDetail(productdetail.getResult());
        // 根据giftcodeId 查询激活码信息
        Wrapper giftCodeWrapper = new Wrapper() {
            @Override
            public String getSqlSegment() {
                return "where del_flag = 0 and id ='" + giftCodeId + "'";
            }
        };
        GiftCode giftCode = giftCodeService.selectOne(giftCodeWrapper);
        if (giftCode != null) {
            QueryBookBlockRes blockRes = (QueryBookBlockRes) redisTemplate.opsForValue().get("BLOCKS_SHOP:" + productdetail.getResult().getShop().getId() + "_GROUP:" + productdetail.getResult().getProductGroup().getId() + "_GIFT:" + giftCode.getId() + "_productGroupProductId" + productGroupProductId);
            if (blockRes == null) {
                QueryBookBlockReq queryBookBlockReq = new QueryBookBlockReq();
                queryBookBlockReq.setShopId(productdetail.getResult().getShop().getId());
                queryBookBlockReq.setGoodsId(productdetail.getResult().getProductGroup().getGoodsId());
                queryBookBlockReq.setProductGroupId(productdetail.getResult().getProductGroup().getId());
                queryBookBlockReq.setProductGroupProductId(productGroupProductId);
                queryBookBlockReq.setOutDate(giftCode.getActOutDate());
                queryBookBlockReq.setActivationDate(giftCode.getActCodeTime());
                queryBookBlockReq.setActExpireTime(giftCode.getActExpireTime());
                CommonResultVo<QueryBookBlockRes> common = remoteBlockRuleService.queryBookBlock(queryBookBlockReq);
                if (common.getResult() != null) {
                    blockRes = common.getResult();
                    //设置缓存10分钟刷新一次
                    redisTemplate.opsForValue().set("BLOCKS_SHOP:" + productdetail.getResult().getShop().getId() + "_GROUP:" + productdetail.getResult().getProductGroup().getId() + "_GIFT:" + giftCode.getId() + "_productGroupProductId" + productGroupProductId, blockRes, 60 * 10, TimeUnit.SECONDS);
                }
            }
            resultVo.setBlockRules(blockRes);
        }
        //调用pangu商户点击次数增加接口
        if (null != productdetail.getResult() && null != productdetail.getResult().getShop().getId()){
            remoteShopService.shopPointUp(productdetail.getResult().getShop().getId());
        }


        return resultVo;
    }


    /*
     * 根据产品组产品id关联id  获取预约单信息
     * */
    @Override
    public ReservOrderProductVo selectReservOrderVo(Integer productGroupProductId) throws Exception {
        /*
         * 住宿需要 ：
         * 1.商户规格取消政策
         * 2.商品使用限制  产品组可用次数
         * 3.历史预约单之类的信息来判断是否可以预定？
         * */
        log.info("参数productGroupProductId={}", productGroupProductId);
        Assert.notNull(productGroupProductId, "参数不能为空");
        CommonResultVo<GroupProductDetailRes> resultVo = remoteProductGroupProductService.selectProductDetail(productGroupProductId);
        GroupProductDetailRes groupProductDetailRes = null;
        Assert.isTrue(resultVo != null && resultVo.getCode() == 100 && (groupProductDetailRes = resultVo.getResult()) != null, "调用产品详情接口失败");
        ReservOrderProductVo reservOrderVo = new ReservOrderProductVo();
        ShopItem shopItem = groupProductDetailRes.getShopItem();
        ProductGroup productGroup = groupProductDetailRes.getProductGroup();
        if (shopItem != null) {
            reservOrderVo.setServiceType(shopItem.getServiceType());
            reservOrderVo.setServiceName(shopItem.getServiceName());
            reservOrderVo.setProductId(shopItem.getId());
            reservOrderVo.setShopId(shopItem.getShopId());
            //产品名称
            reservOrderVo.setProductName(shopItem.getName());
            //取消政策提示文案
            reservOrderVo.setCancelPolicy(ReservOrderStatusEnums.CancelPolicyStatus.getTextByCode(shopItem.getCancelPolicy()));
            reservOrderVo.setNeeds(shopItem.getNeeds());
            reservOrderVo.setAddon(shopItem.getAddon());
        }
        if (productGroup != null) {
            //TODO  渠道号 应该是从第一级点进来的
            reservOrderVo.setProductGroupId(productGroup.getId());
            reservOrderVo.setProductGroupProductId(productGroupProductId);
            //项目id
            reservOrderVo.setGoodsId(productGroup.getGoodsId());
        }
        //价格   原价按年月日 周几算  +税费 服务费
        BigDecimal price = getPrice(groupProductDetailRes.getPriceRuleList());
        //折扣价  原价 - 产品组中折扣比例 discountRate
        BigDecimal discountPrice = getPrice(price, productGroup.getDiscountRate());
//        reservOrderVo.setTotalAmount(price);
//        reservOrderVo.setDiscountAmount(discountPrice);
        reservOrderVo.setExchangeNum(1);
        return reservOrderVo;
    }

    private BigDecimal getPrice(BigDecimal price, BigDecimal rate) {
        BigDecimal discountRate = rate == null ? BigDecimal.valueOf(0) : rate.divide(BigDecimal.valueOf(100));
        BigDecimal discountPrice = price.multiply(discountRate);
        return discountPrice;
    }

    /*
     * 获取原价价格
     * */
    private BigDecimal getPrice(List<ShopItemNetPriceRule> priceRules) {
        BigDecimal price;
        if (priceRules == null || priceRules.isEmpty()) {
            return BigDecimal.valueOf(0);
        }
        //获取当前时间
        Date date = new Date();
        //获取当前星期几  0 -星期天
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        for (ShopItemNetPriceRule priceRule : priceRules) {
            //判断是否在日期范围
            if (date.compareTo(priceRule.getStartDate()) >= 0 && date.compareTo(priceRule.getEndDate()) <= 0) {
                //判断当前周几是否可用
                int isUse = 0;
                switch (w) {
                    case 0:
                        isUse = priceRule.getSunday();
                        break;
                    case 1:
                        isUse = priceRule.getMonday();
                        break;
                    case 2:
                        isUse = priceRule.getTuesday();
                        break;
                    case 3:
                        isUse = priceRule.getWednesday();
                        break;
                    case 4:
                        isUse = priceRule.getThursday();
                        break;
                    case 5:
                        isUse = priceRule.getFriday();
                        break;
                    case 6:
                        isUse = priceRule.getSaturday();
                        break;
                }
                if (isUse == 1) {
                    //价格= 价格+（价格*服务费率）+（价格*税率）
                    price = priceRule.getNetPrice().add(
                            priceRule.getNetPrice().multiply(priceRule.getServiceRate()).add(
                                    priceRule.getNetPrice().multiply(priceRule.getTaxRate())));
                    return price;
                }
            }
        }

        return BigDecimal.valueOf(0);
    }

    @Override
    public PageVo<QueryListResDto> getGoddsListPaging(GoodsListReqVo goodsListReqVo) throws Exception {
        log.info("参数goodsListReqVo={}", JSON.toJSON(goodsListReqVo));
        if (goodsListReqVo.getGroupId() == null) {
            throw new Exception("产品组ID不能为空");
        }
        if (goodsListReqVo.getService() == null) {
            throw new Exception("权益类型不能为空");
        }
        PageVo<QueryListResDto> resDtoPageVo = new PageVo<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        List<SelectBookProductRes> resultList = (List<SelectBookProductRes>) redisTemplate.opsForValue().get("GOODS_"+goodsListReqVo.getGroupId()+"_"+goodsListReqVo.getService());
//        if(resultList==null) {
        Map<String, Object> condition = new HashMap<>();
        condition.put("productGroupId", goodsListReqVo.getGroupId());
        condition.put("service", goodsListReqVo.getService());
        condition.put("hotelAddress", goodsListReqVo.getHotelAddress());
        condition.put("hotelName", goodsListReqVo.getHotelName());
        PageVo<SelectBookShopItemReq> pageVo = new PageVo<>(goodsListReqVo.getCurrent(), goodsListReqVo.getSize());
        pageVo.setCondition(condition);
        CommonResultVo<PageVo<SelectBookProductRes>> selectproductlist = remoteProductGroupProductService.selectBookProductPaging(pageVo);
        PageVo<SelectBookProductRes> pageResVo;
        if (selectproductlist == null || (pageResVo = selectproductlist.getResult()) == null) {
            throw new Exception("调用产品列表接口返回值为空");
        }
        BeanUtils.copyProperties(pageResVo, resDtoPageVo);
        List<SelectBookProductRes> resultList = pageResVo.getRecords();
        //设置缓存10分钟刷新一次
//        redisTemplate.opsForValue().set("GOODS_" + goodsListReqVo.getGroupId() + "_" + goodsListReqVo.getService(), resultList, 60 * 10, TimeUnit.SECONDS);
//        }
        List<Map> list = new ArrayList<>();
        List<QueryListResDto> queryListResDtos = Lists.newArrayList();
        //价格信息查询 调用luoxing接口
        for (SelectBookProductRes Res : resultList) {
            QueryListResDto resDto = new QueryListResDto();
            // 默认都不可预约
            resDto.setOrderFlag(false);
            // Block筛选
            // 根据giftcodeId 查询激活码信息
            Wrapper giftCodeWrapper = new Wrapper() {
                @Override
                public String getSqlSegment() {
                    return "where del_flag = 0 and id ='" + goodsListReqVo.getGiftcodeId() + "'";
                }
            };
            GiftCode giftCode = giftCodeService.selectOne(giftCodeWrapper);
            if (giftCode != null) {
                QueryBookBlockRes blockRes = (QueryBookBlockRes) redisTemplate.opsForValue().get("BLOCKS_SHOP:" + Res.getShop().getId() + "_GROUP:" + Res.getProductGroup().getId() + "_GIFT:" + giftCode.getId());
                if (blockRes == null) {
                    QueryBookBlockReq queryBookBlockReq = new QueryBookBlockReq();
                    queryBookBlockReq.setShopId(Res.getShop().getId());
                    queryBookBlockReq.setGoodsId(Res.getProductGroup().getGoodsId());
                    queryBookBlockReq.setProductGroupId(Res.getProductGroup().getId());
                    queryBookBlockReq.setProductGroupProductId(Res.getProductGroupProduct().getId());
                    queryBookBlockReq.setOutDate(giftCode.getActOutDate());
                    queryBookBlockReq.setActivationDate(giftCode.getActCodeTime());
                    queryBookBlockReq.setActExpireTime(giftCode.getActExpireTime());
                    CommonResultVo<QueryBookBlockRes> common = remoteBlockRuleService.queryBookBlock(queryBookBlockReq);
                    if (common.getResult() != null) {
                        blockRes = common.getResult();
                        //设置缓存10分钟刷新一次
                        redisTemplate.opsForValue().set("BLOCKS_SHOP:" + Res.getShop().getId() + "_GROUP:" + Res.getProductGroup().getId() + "_GIFT:" + giftCode.getId(), blockRes, 60 * 10, TimeUnit.SECONDS);
                    }
                }
                if (blockRes != null) {
                    List<Date> bookDates = blockRes.getBookDates();
                    // 起始日期筛选
                    if (goodsListReqVo.getBeginDate() != null) {
                        if (bookDates != null && bookDates.contains(sdf.parse(goodsListReqVo.getBeginDate()))) {
//            				log.info("酒店ID="+Res.getShop().getId()+",日期="+goodsListReqVo.getBeginDate()+"可预订");
                            resDto.setOrderFlag(true);
//            				continue;
                        }
                    }
                    // 截止日期筛选
                    if (goodsListReqVo.getEndDate() != null) {
                        List<Date> dateList = this.findDates(sdf.parse(goodsListReqVo.getBeginDate()), sdf.parse(goodsListReqVo.getEndDate()));
                        if (bookDates != null && bookDates.containsAll(dateList)) {
//            				log.info("酒店ID="+Res.getShop().getId()+",日期="+goodsListReqVo.getEndDate()+"可预订");
                            resDto.setOrderFlag(true);
//            				continue;
                        }
                    }
                } else {
                    // 兑换券 blockRes 为空， 不限制block
                    resDto.setOrderFlag(true);
                }

            } else {
                log.info("激活码不存在,ID={}", goodsListReqVo.getGiftcodeId());
            }

            List<ShopItemNetPriceRule> priceRules = Res.getPriceRuleList();
            BigDecimal shopPriceRule = this.getPrice(priceRules);
            Map map = new HashMap();
            map.put("res", Res);
            map.put("shopPriceRule", shopPriceRule);
            //折扣价  原价 - 产品组中折扣比例 discountRate
            BigDecimal discountRate = getPrice(shopPriceRule, Res.getProductGroup().getDiscountRate());
            map.put("discountRate", discountRate);
            list.add(map);

            resDto.setHotel(Res.getHotel());
            resDto.setProductGroup(Res.getProductGroup());
            if (!Res.getShopPics().isEmpty()) {
                SysFileDto sysFile = Res.getShopPics().get(0);
                resDto.setShopPic(sysFile.getPgCdnHttpUrl() + '/' + sysFile.getGuid() + '.' + sysFile.getExt());
            }
            ShopResDto shopResDto = new ShopResDto();
            List<ShopItemResDto> shopItemResDtoList = Lists.newArrayList();
            shopResDto.setShop(Res.getShop());
            ShopItemResDto shopItemResDto = new ShopItemResDto();
            shopItemResDto.setShopItem(Res.getShopItem());
            shopItemResDto.setPriceRules(Res.getPriceRuleList());
            shopItemResDto.setProductGroupProduct(Res.getProductGroupProduct());
            shopItemResDto.setShopItemPics(Res.getShopItemPics());
            shopItemResDto.setDiscountRate(discountRate);
            shopItemResDto.setShopPriceRule(shopPriceRule);
            shopItemResDtoList.add(shopItemResDto);

            shopResDto.setShopItemResDtoList(shopItemResDtoList);
            resDto.setShopResDto(shopResDto);
            queryListResDtos.add(resDto);

        }
        List<QueryListResDto> result = Lists.newArrayList();
        Map<String, QueryListResDto> resDtoMap = new HashMap<String, QueryListResDto>();
        if (!CollectionUtils.isEmpty(queryListResDtos)) {
            for (QueryListResDto s : queryListResDtos) {
                if (resDtoMap.containsKey(s.getShopResDto().getShop().getId().toString())) {
                    if (!CollectionUtils.isEmpty(result)) {
                        for (QueryListResDto resDto : result) {
                            QueryListResDto listResDto = new QueryListResDto();
                            if (s.getShopResDto().getShop().getId().intValue() == resDto.getShopResDto().getShop().getId().intValue()) {
                                listResDto.setHotel(resDto.getHotel());
                                listResDto.setProductGroup(resDto.getProductGroup());
                                ShopResDto shopResDto = new ShopResDto();
                                shopResDto.setShop(resDto.getShopResDto().getShop());
                                List<ShopItemResDto> shopItemResDtoList = resDto.getShopResDto().getShopItemResDtoList();
                                shopItemResDtoList.add(s.getShopResDto().getShopItemResDtoList().get(0));
                                shopResDto.setShopItemResDtoList(shopItemResDtoList);
                                listResDto.setShopResDto(shopResDto);
                                listResDto.setOrderFlag(resDto.getOrderFlag());
                                listResDto.setShopPic(resDto.getShopPic());
                                result.remove(resDto);
                                result.add(listResDto);
                                break;
                            }
                        }
                    }

                } else {
                    resDtoMap.put(s.getShopResDto().getShop().getId().toString(), s);
                    result.add(s);
                }
                // 如果是 推荐酒店
                if (goodsListReqVo.getRecommend() != null && goodsListReqVo.getRecommend().equals(1)) {
                    // 非推荐酒店
                    Boolean recoMond = false;
                    for (ShopItemResDto shopItem : s.getShopResDto().getShopItemResDtoList()) {
                        if (shopItem.getProductGroupProduct().getRecommend().equals(1)) {
                            recoMond = true;
                        }
                    }
                    if (!recoMond) {
                        result.remove(s);
                    }
                }
            }
        }
        log.info("result.size()=========" + result.size());

        return resDtoPageVo.setRecords(result);

    }

    @Override
    public List<QueryListResDto> getGoddsList(GoodsListReqVo goodsListReqVo) throws Exception {
        log.info("参数goodsListReqVo={}", JSON.toJSON(goodsListReqVo));
        if (goodsListReqVo.getGroupId() == null) {
            throw new Exception("产品组ID不能为空");
        }
        if (goodsListReqVo.getService() == null) {
            throw new Exception("权益类型不能为空");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<SelectBookProductRes> resultList = (List<SelectBookProductRes>) redisTemplate.opsForValue().get("GOODS_" + goodsListReqVo.getGroupId() + "_" + goodsListReqVo.getService());
        if (resultList == null) {
            SelectBookProductReq selectBookProductReq = new SelectBookProductReq();
            selectBookProductReq.setProductGroupId(goodsListReqVo.getGroupId());
            selectBookProductReq.setServiceType(goodsListReqVo.getService());
            CommonResultVo<List<SelectBookProductRes>> selectproductlist = remoteProductGroupProductService.selectBookProduct(selectBookProductReq);
            if (selectproductlist == null) {
                throw new Exception("调用产品列表接口返回值为空");
            }
            resultList = selectproductlist.getResult();
            //设置缓存10分钟刷新一次
            redisTemplate.opsForValue().set("GOODS_" + goodsListReqVo.getGroupId() + "_" + goodsListReqVo.getService(), resultList, 60 * 10, TimeUnit.SECONDS);
        }
        List<Map> list = new ArrayList<>();
        List<QueryListResDto> queryListResDtos = Lists.newArrayList();
        //价格信息查询 调用luoxing接口
        for (SelectBookProductRes Res : resultList) {
            QueryListResDto resDto = new QueryListResDto();
            // 默认都不可预约
            resDto.setOrderFlag(false);
            // Block筛选
            // 根据giftcodeId 查询激活码信息
            Wrapper giftCodeWrapper = new Wrapper() {
                @Override
                public String getSqlSegment() {
                    return "where del_flag = 0 and id ='" + goodsListReqVo.getGiftcodeId() + "'";
                }
            };
            GiftCode giftCode = giftCodeService.selectOne(giftCodeWrapper);
            if (giftCode != null) {
                QueryBookBlockRes blockRes = (QueryBookBlockRes) redisTemplate.opsForValue().get("BLOCKS_SHOP:" + Res.getShop().getId() + "_GROUP:" + Res.getProductGroup().getId() + "_GIFT:" + giftCode.getId());
                if (blockRes == null) {
                    QueryBookBlockReq queryBookBlockReq = new QueryBookBlockReq();
                    queryBookBlockReq.setShopId(Res.getShop().getId());
                    queryBookBlockReq.setGoodsId(Res.getProductGroup().getGoodsId());
                    queryBookBlockReq.setProductGroupId(Res.getProductGroup().getId());
                    queryBookBlockReq.setProductGroupProductId(Res.getProductGroupProduct().getId());
                    queryBookBlockReq.setOutDate(giftCode.getActOutDate());
                    queryBookBlockReq.setActivationDate(giftCode.getActCodeTime());
                    queryBookBlockReq.setActExpireTime(giftCode.getActExpireTime());
                    CommonResultVo<QueryBookBlockRes> common = remoteBlockRuleService.queryBookBlock(queryBookBlockReq);
                    if (common.getResult() != null) {
                        blockRes = common.getResult();
                        //设置缓存10分钟刷新一次
                        redisTemplate.opsForValue().set("BLOCKS_SHOP:" + Res.getShop().getId() + "_GROUP:" + Res.getProductGroup().getId() + "_GIFT:" + giftCode.getId(), blockRes, 60 * 10, TimeUnit.SECONDS);
                    }
                }
                if (blockRes != null) {
                    List<Date> bookDates = blockRes.getBookDates();
                    // 起始日期筛选
                    if (goodsListReqVo.getBeginDate() != null) {
                        if (bookDates != null && bookDates.contains(sdf.parse(goodsListReqVo.getBeginDate()))) {
//            				log.info("酒店ID="+Res.getShop().getId()+",日期="+goodsListReqVo.getBeginDate()+"可预订");
                            resDto.setOrderFlag(true);
//            				continue;
                        }
                    }
                    // 截止日期筛选
                    if (goodsListReqVo.getEndDate() != null) {
                        List<Date> dateList = this.findDates(sdf.parse(goodsListReqVo.getBeginDate()), sdf.parse(goodsListReqVo.getEndDate()));
                        if (bookDates != null && bookDates.containsAll(dateList)) {
//            				log.info("酒店ID="+Res.getShop().getId()+",日期="+goodsListReqVo.getEndDate()+"可预订");
                            resDto.setOrderFlag(true);
//            				continue;
                        }
                    }
                } else {
                    // 兑换券 blockRes 为空， 不限制block
                    resDto.setOrderFlag(true);
                }

            } else {
                log.info("激活码不存在,ID={}", goodsListReqVo.getGiftcodeId());
            }

            List<ShopItemNetPriceRule> priceRules = Res.getPriceRuleList();
            BigDecimal shopPriceRule = this.getPrice(priceRules);
            Map map = new HashMap();
            map.put("res", Res);
            map.put("shopPriceRule", shopPriceRule);
            //折扣价  原价 - 产品组中折扣比例 discountRate
            BigDecimal discountRate = getPrice(shopPriceRule, Res.getProductGroup().getDiscountRate());
            map.put("discountRate", discountRate);
            list.add(map);

            resDto.setHotel(Res.getHotel());
            resDto.setProductGroup(Res.getProductGroup());
            if (!Res.getShopPics().isEmpty()) {
                SysFileDto sysFile = Res.getShopPics().get(0);
                resDto.setShopPic(sysFile.getPgCdnHttpUrl() + '/' + sysFile.getGuid() + '.' + sysFile.getExt());
            }
            ShopResDto shopResDto = new ShopResDto();
            List<ShopItemResDto> shopItemResDtoList = Lists.newArrayList();
            shopResDto.setShop(Res.getShop());
            ShopItemResDto shopItemResDto = new ShopItemResDto();
            shopItemResDto.setShopItem(Res.getShopItem());
            shopItemResDto.setPriceRules(Res.getPriceRuleList());
            shopItemResDto.setProductGroupProduct(Res.getProductGroupProduct());
            shopItemResDto.setShopItemPics(Res.getShopItemPics());
            shopItemResDto.setDiscountRate(discountRate);
            shopItemResDto.setShopPriceRule(shopPriceRule);

            SelectBookPayReq selectBookPayReq = new SelectBookPayReq();
            selectBookPayReq.setProductGroupProductId(Res.getProductGroupProduct().getId());
            List<Date> dates = HelpUtils.getDatesBetweenTwoDate(DateUtil.parseDate(goodsListReqVo.getBeginDate()), DateUtil.parseDate(goodsListReqVo.getBeginDate()), false);
            selectBookPayReq.setBookDates(dates);
            List<BookBasePaymentRes> payments = panguInterfaceService.selectBookPay(selectBookPayReq);
            if(payments.size()>0) {
                BookBasePaymentRes minPriceRes = null;
                for (BookBasePaymentRes res : payments) {
                    if (minPriceRes == null) {
                        minPriceRes = res;
                    } else if (minPriceRes.getBookPrice().compareTo(res.getBookPrice()) > 0) {
                        minPriceRes = res;
                    }
                }
                shopItemResDto.setProductPrice(minPriceRes.getBookPrice());
            }
            shopItemResDtoList.add(shopItemResDto);

            shopResDto.setShopItemResDtoList(shopItemResDtoList);
            resDto.setShopResDto(shopResDto);
            queryListResDtos.add(resDto);

        }
        List<QueryListResDto> result = queryListResDtos;
        //如果是卷类型的就不合并
        if (goodsListReqVo.getService().indexOf("_cpn") == -1) {
            result = new ArrayList<QueryListResDto>();
            Map<String, QueryListResDto> resDtoMap = new HashMap<String, QueryListResDto>();
            if (!CollectionUtils.isEmpty(queryListResDtos)) {
                for (QueryListResDto s : queryListResDtos) {
                    if (resDtoMap.containsKey(s.getShopResDto().getShop().getId().toString())) {
                        if (!CollectionUtils.isEmpty(result)) {
                            for (QueryListResDto resDto : result) {
                                QueryListResDto listResDto = new QueryListResDto();
                                if (s.getShopResDto().getShop().getId().intValue() == resDto.getShopResDto().getShop().getId().intValue()) {
                                    listResDto.setHotel(resDto.getHotel());
                                    listResDto.setProductGroup(resDto.getProductGroup());
                                    ShopResDto shopResDto = new ShopResDto();
                                    shopResDto.setShop(resDto.getShopResDto().getShop());
                                    List<ShopItemResDto> shopItemResDtoList = resDto.getShopResDto().getShopItemResDtoList();
                                    shopItemResDtoList.add(s.getShopResDto().getShopItemResDtoList().get(0));
                                    shopResDto.setShopItemResDtoList(shopItemResDtoList);
                                    listResDto.setShopResDto(shopResDto);
                                    listResDto.setOrderFlag(resDto.getOrderFlag());
                                    listResDto.setShopPic(resDto.getShopPic());
                                    result.remove(resDto);
                                    result.add(listResDto);
                                    break;
                                }
                            }
                        }

                    } else {
                        resDtoMap.put(s.getShopResDto().getShop().getId().toString(), s);
                        result.add(s);
                    }
                    // 如果是 推荐酒店
                    if (goodsListReqVo.getRecommend() != null && goodsListReqVo.getRecommend().equals(1)) {
                        // 非推荐酒店
                        Boolean recoMond = false;
                        for (ShopItemResDto shopItem : s.getShopResDto().getShopItemResDtoList()) {
                            if (shopItem.getProductGroupProduct().getRecommend().equals(1)) {
                                recoMond = true;
                            }
                        }
                        if (!recoMond) {
                            result.remove(s);
                        }
                    }
                }
            }
        }

        log.info("result.size()=========" + result.size());
        return result;

    }

    @Override
    public List<QueryListResDto> getGoddsListNew(GoodsListReqVo goodsListReqVo)throws Exception {
        long start1 = System.currentTimeMillis();
        log.info("参数goodsListReqVo={}", JSON.toJSON(goodsListReqVo));
        if (goodsListReqVo.getGroupId() == null) {
            throw new Exception("产品组ID不能为空");
        }
        if (goodsListReqVo.getService() == null) {
            throw new Exception("权益类型不能为空");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // 根据giftcodeId 查询激活码信息
        Wrapper giftCodeWrapper = new Wrapper() {
            @Override
            public String getSqlSegment() {
                return "where del_flag = 0 and id ='"+goodsListReqVo.getGiftcodeId()+"'";
            }
        };
        GiftCode giftCode = giftCodeService.selectOne(giftCodeWrapper);
        List<SelectBookProductRes> resultList = (List<SelectBookProductRes>) redisTemplate.opsForValue().get("GOODSNEW_"+goodsListReqVo.getGroupId()+"_"+goodsListReqVo.getService()+"_"+goodsListReqVo.getBeginDate()+"_"+goodsListReqVo.getEndDate()+"_"+DateUtil.format(giftCode.getActExpireTime(),"yyyyMMddHHmmss"));
        if(resultList==null) {
            SelectBookProductReq selectBookProductReq = new SelectBookProductReq();
            selectBookProductReq.setProductGroupId(goodsListReqVo.getGroupId());
            selectBookProductReq.setServiceType(goodsListReqVo.getService());
            selectBookProductReq.setActExpireTime(giftCode.getActExpireTime());
            selectBookProductReq.setBeginDate(goodsListReqVo.getBeginDate());
            selectBookProductReq.setEndDate(goodsListReqVo.getEndDate());
            CommonResultVo<List<SelectBookProductRes>> selectproductlist = remoteProductGroupProductService.selectBookProductNew(selectBookProductReq);
            if (selectproductlist == null) {
                throw new Exception("调用产品列表接口返回值为空");
            }
            resultList = selectproductlist.getResult();
            //设置缓存10分钟刷新一次
            redisTemplate.opsForValue().set("GOODSNEW_"+goodsListReqVo.getGroupId()+"_"+goodsListReqVo.getService()+"_"+goodsListReqVo.getBeginDate()+"_"+goodsListReqVo.getEndDate()+"_"+DateUtil.format(giftCode.getActExpireTime(),"yyyyMMddHHmmss"), resultList, 60*10,TimeUnit.SECONDS);
        }
        List<Map> list = new ArrayList<>();
        List<QueryListResDto> queryListResDtos = Lists.newArrayList();
        //获取所有产品组的折扣比例
        Map<Integer,ProductGroup> groupMap = Maps.newHashMap();
        List<Integer> discountRates =  resultList.stream().map(
                selectBookProductRes -> selectBookProductRes.getProductGroup().getId()).collect(Collectors.toList());
        List<ProductGroup> productGroups = remoteProductGroupService.selectDiscountByIds(discountRates).getResult();
        if (!CollectionUtils.isEmpty(productGroups)){
            groupMap = productGroups.stream().collect(Collectors.toMap(ProductGroup::getId,productGroup -> productGroup));
        }
        //价格信息查询 调用luoxing接口
        for (SelectBookProductRes Res : resultList) {
            //使用线程查询预计到店付价格(疑似对服务器内存有影响，暂不开放)
//            BookPayReq bookPayReq = new BookPayReq();
//            bookPayReq.setProductGroupProductId(Res.getProductGroupProduct().getId());
//            bookPayReq.setStartDate(DateUtil.format(new Date(),"yyyy-MM-dd"));
//            Future<PriceRes> shopPriceRule = executorService.submit(() -> this.getStorePrice(bookPayReq));
            QueryListResDto queryListResDto = new QueryListResDto();
            queryListResDto.setOrderFlag(Res.getOrderFlag());
            queryListResDto.setCityName(Res.getCityName());
            BigDecimal discountRate = null;
//            CommonResultVo<GroupDetailRes> groupDetail =  remoteProductGroupProductService.groupDetail(Res.getProductGroup().getId());
            if(!CollectionUtils.isEmpty(groupMap) && groupMap.get(Res.getProductGroup().getId()) != null){
                discountRate = groupMap.get(Res.getProductGroup().getId()).getDiscountRate();
            }
            queryListResDto.setHotel(Res.getHotel());
            queryListResDto.setProductGroup(Res.getProductGroup());
            if(!Res.getShopPics().isEmpty()) {
                SysFileDto sysFile = Res.getShopPics().get(0);
                queryListResDto.setShopPic(sysFile.getPgCdnHttpUrl() + '/' + sysFile.getGuid());
            }
            List<ShopItemResDto> shopItemResList = Lists.newArrayList();
            ShopResDto shopDto = new ShopResDto();
            shopDto.setShop(Res.getShop());
            ShopItemResDto shopItemResDto = new ShopItemResDto();
            shopItemResDto.setShopItem(Res.getShopItem());
            shopItemResDto.setPriceRules(Res.getPriceRuleList());
            shopItemResDto.setProductGroupProduct(Res.getProductGroupProduct());
            shopItemResDto.setShopItemPics(Res.getShopItemPics());
            shopItemResDto.setDiscountRate(discountRate);
//            shopItemResDto.setShopPriceRule(shopPriceRule.get().getPayPrice());
            shopItemResList.add(shopItemResDto);
            shopDto.setShopItemResDtoList(shopItemResList);
            queryListResDto.setShopResDto(shopDto);
            queryListResDtos.add(queryListResDto);
        }
        //获取预约支付金额
        List<Integer> productGroupProductIdList =  resultList.stream().map(
                selectBookProductRes -> selectBookProductRes.getProductGroupProduct().getId()).collect(Collectors.toList());
        List<BookBasePaymentRes> payments = panguInterfaceService.selectBookPayList(productGroupProductIdList);
        if(!CollectionUtils.isEmpty(payments)) {
            //遍历封装结果,判断ProductGroupProductId 是否一致,进行预约支付金额封装
            for(QueryListResDto queryListResDto : queryListResDtos ){
                ShopItemResDto shopItemResDto = queryListResDto.getShopResDto().getShopItemResDtoList().get(0);
                List<BigDecimal> newList = new ArrayList<>();
                for (BookBasePaymentRes res : payments) {
                    if (shopItemResDto.getProductGroupProduct().getId().intValue() == res.getProductGroupProductId().intValue()){
                        //拿到传入日期段之间的价格list然后排序取最小
                        if (goodsListReqVo.getService() == "accom") {
                            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                            try {
                                Date beginDate = df.parse(goodsListReqVo.getBeginDate());
                                Date endDate = df.parse(goodsListReqVo.getEndDate());
                                Date startTime = df.parse(DateUtil.format(res.getStartDate(), "yyyy-MM-dd"));
                                Date endTime = df.parse(DateUtil.format(res.getEndDate(), "yyyy-MM-dd"));
                                //res中的数据 a1 : 10.1-10.10  200元  a2 :  10.11-10.20  100元 a3 : 10.21-10.31 500元
                                //入参 10.9-10.29
                                //实际满足条件的有 a1、 a2 、a3
                                List<Date> date = findDates(beginDate,endDate);
                                int i = date.size();
                                List<Date> resDate = findDates(startTime,endTime);
                                date.removeAll(resDate);
                                if (i != date.size()) {
                                    newList.add(res.getBookPrice());
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }else if(goodsListReqVo.getService() != "accom"){
                            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                            try {
                                Date beginDate = df.parse(goodsListReqVo.getBeginDate());
                                Date endDate = df.parse(goodsListReqVo.getBeginDate());
                                Date startTime = df.parse(DateUtil.format(res.getStartDate(), "yyyy-MM-dd"));
                                Date endTime = df.parse(DateUtil.format(res.getEndDate(), "yyyy-MM-dd"));
                                List<Date> date = findDates(beginDate,endDate);
                                int i = date.size();
                                List<Date> resDate = findDates(startTime,endTime);
                                date.removeAll(resDate);
                                if (i != date.size()) {
                                    newList.add(res.getBookPrice());
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                if(!CollectionUtils.isEmpty(newList)) {
                    //拿到newList中排序最小的值
                    Collections.sort(newList);
                    shopItemResDto.setProductPrice(newList.get(0));
                }
            }
        }
//        List<QueryListResDto> result = queryListResDtos;
        Map<Integer, QueryListResDto> resDtoMap = Maps.newHashMap();
        List<QueryListResDto> repeatList = Lists.newArrayList();
        //如果是卷类型的就不合并
        if (goodsListReqVo.getService().indexOf("_cpn") == -1 && !CollectionUtils.isEmpty(queryListResDtos)) {
            for (QueryListResDto dto : queryListResDtos) {
                Integer shopId  =  dto.getShopResDto().getShop().getId();
                if(resDtoMap.containsKey(shopId)){
                    QueryListResDto queryListResDto = resDtoMap.get(shopId);
                    List<ShopItemResDto> shopItemResDtoList = dto.getShopResDto().getShopItemResDtoList();
                    queryListResDto.getShopResDto().getShopItemResDtoList().addAll(shopItemResDtoList);
                    if(dto.getOrderFlag() || queryListResDto.getOrderFlag()){
                        queryListResDto.setOrderFlag(true);
                    }
                    resDtoMap.put(shopId,queryListResDto);
                }else{

                    resDtoMap.put(shopId,dto);
                }
                if (goodsListReqVo.getRecommend() != null && goodsListReqVo.getRecommend().equals(1)) {
                    for (ShopItemResDto shopItem : dto.getShopResDto().getShopItemResDtoList()) {
                        if (!shopItem.getProductGroupProduct().getRecommend().equals(1)) {
                            resDtoMap.remove(shopId);
                            break;
                        }
                    }
                }
            }
            if(resDtoMap.size() > 0)    repeatList =  new ArrayList(resDtoMap.values());;

        }else{
            repeatList =queryListResDtos;
        }
        log.info("result.size()========="+repeatList.size());
        return repeatList;
    }

    @Override
    public List<SelectBookProductRes> getGoddsListNEW2(Integer goodId) throws Exception {
        if (goodId == null) {
            throw new Exception("商品ID不能为空");
        }
        CommonResultVo<List<GoodsGroupListRes>> remoteGroup = remoteProductGroupService.selectGoodsGroup(goodId);
        if (remoteGroup.getResult() == null) {
            throw new Exception("产品组不存在");
        }
        List<SelectBookProductRes> res = new ArrayList<SelectBookProductRes>();
        for(GoodsGroupListRes resnew : remoteGroup.getResult()) {
            GoodsListReqVo goodsListReqVo = new GoodsListReqVo();
            goodsListReqVo.setGroupId(resnew.getId());
            ResourceTypeEnums resourceTypeEnums = ResourceTypeEnums.findByName(resnew.getService());
            goodsListReqVo.setService(resourceTypeEnums.getCode());
            SelectBookProductReq selectBookProductReq = new SelectBookProductReq();
            selectBookProductReq.setProductGroupId(goodsListReqVo.getGroupId());
            selectBookProductReq.setServiceType(goodsListReqVo.getService());
//            selectBookProductReq.setBeginDate(goodsListReqVo.getBeginDate());
//            selectBookProductReq.setEndDate(goodsListReqVo.getEndDate());
            CommonResultVo<List<SelectBookProductRes>> selectproductlist = remoteProductGroupProductService.selectBookProduct(selectBookProductReq);
            if (selectproductlist == null) {
                throw new Exception("调用产品列表接口返回值为空");
            }
            for(SelectBookProductRes selectBookProductRes : selectproductlist.getResult()){
                res.add(selectBookProductRes);
            }

        }
        return res;
    }

    public PriceRes getStorePrice(BookPayReq req){
        List<ShopItemNetPriceRule> priceRules = panguInterfaceService.selectProductPrices(req.getProductGroupProductId());
        return getPrice(priceRules,req.getStartDate()) ;
    }
    /*
     * 获取单人总价格
     * */
    private PriceRes getPrice(List<ShopItemNetPriceRule> priceRules,String bookDate) {
        PriceRes priceRes = new PriceRes();
        if (priceRules == null || priceRules.isEmpty()) {
            return priceRes;
        }
        //获取当前时间
        Date date = DateUtil.parseDate(bookDate);
        //获取当前星期几  0 -星期天
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        for (ShopItemNetPriceRule priceRule : priceRules) {
            //判断是否在日期范围
            if (date.compareTo(priceRule.getStartDate()) >= 0 && date.compareTo(priceRule.getEndDate()) <= 0) {
                //判断当前周几是否可用
                int isUse = 0;
                switch (w) {
                    case 0:
                        isUse = priceRule.getSunday();
                        break;
                    case 1:
                        isUse = priceRule.getMonday();
                        break;
                    case 2:
                        isUse = priceRule.getTuesday();
                        break;
                    case 3:
                        isUse = priceRule.getWednesday();
                        break;
                    case 4:
                        isUse = priceRule.getThursday();
                        break;
                    case 5:
                        isUse = priceRule.getFriday();
                        break;
                    case 6:
                        isUse = priceRule.getSaturday();
                        break;
                }
                if (isUse == 1) {
                    //价格= (价格+（价格*服务费率）+（(价格+（价格*服务费率）)*税率）)
                    BigDecimal price = priceRule.getNetPrice().add(
                            priceRule.getNetPrice().multiply(priceRule.getServiceRate()));
                    price =price.add(price.multiply(priceRule.getTaxRate()));
                    priceRes.setNetPrice(priceRule.getNetPrice());
                    priceRes.setPayPrice(price);
                    priceRes.setServiceRate(priceRule.getServiceRate());
                    priceRes.setTaxRate(priceRule.getTaxRate());
                    return priceRes;
                }
            }
        }

        return priceRes;
    }

    /**
     * @throws
     * @Title: findDates
     * @Description: JAVA获取某段时间内的所有日期
     * @author: sunny.wang
     * @date: 2019年6月18日 下午1:39:43
     * @param: @param dStart
     * @param: @param dEnd
     * @param: @return
     * @return: List<Date>
     */
    private List<Date> findDates(Date dStart, Date dEnd) {
        Calendar cStart = Calendar.getInstance();
        cStart.setTime(dStart);
        List<Date> dateList = new ArrayList<Date>();
        // 别忘了，把起始日期加上
        dateList.add(dStart);
        // 此日期是否在指定日期之后
        while (dEnd.after(cStart.getTime())) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            cStart.add(Calendar.DAY_OF_MONTH, 1);
            dateList.add(cStart.getTime());
        }
        return dateList;
    }


}
