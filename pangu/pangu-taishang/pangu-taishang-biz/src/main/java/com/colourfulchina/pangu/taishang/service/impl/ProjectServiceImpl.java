package com.colourfulchina.pangu.taishang.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.colourfulchina.pangu.taishang.api.dto.BookNum;
import com.colourfulchina.pangu.taishang.api.entity.*;
import com.colourfulchina.pangu.taishang.api.utils.GoodsUtils;
import com.colourfulchina.pangu.taishang.api.vo.BlockBookDaysVo;
import com.colourfulchina.pangu.taishang.api.vo.res.bigan.*;
import com.colourfulchina.pangu.taishang.api.vo.res.bigan.ShopItem;
import com.colourfulchina.pangu.taishang.enums.ProductEnums;
import com.colourfulchina.pangu.taishang.mapper.*;
import com.colourfulchina.pangu.taishang.service.GoodsSettingService;
import com.colourfulchina.pangu.taishang.service.ProjectService;
import com.colourfulchina.pangu.taishang.service.ShopItemNetPriceRuleService;
import com.colourfulchina.pangu.taishang.utils.BookMinMaxDayUtils;
import com.colourfulchina.pangu.taishang.utils.DateUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductGroupMapper productGroupMapper;

    @Autowired
    private GoodsChannelsMapper goodsChannelsMapper;

    @Autowired
    private ShopItemMapper shopItemMapper;

    @Autowired
    private SysFileMapper sysFileMapper;

    @Autowired
    private GoodsClauseMapper goodsClauseMapper;

    @Autowired
    private GoodsDetailMapper goodsDetailMapper;

    @Autowired
    private GoodsSettingService goodsSettingService;

    @Autowired
    private ShopItemNetPriceRuleMapper shopItemNetPriceRuleMapper;

    @Autowired
    private ProductGroupProductMapper productGroupProductMapper;

    @Autowired
    private ShopItemNetPriceRuleService shopItemNetPriceRuleService;

    @Override
    public List<ProjectCdnVo> prjBriefList(String[] projectIds) {
        List<ProjectCdnVo> projectCdnVoList = goodsMapper.selectProjectBriefList(projectIds);
        for (ProjectCdnVo projectCdnVo : projectCdnVoList) {
            //产品组
            PrjGroupVo pgvo = new PrjGroupVo();
            pgvo.setProjectId(projectCdnVo.getId());
            List<PrjGroupVo> prjGroupVoList = productGroupMapper.selectPrjGroupList(pgvo);
            projectCdnVo.setPrjGroupVoList(prjGroupVoList);
            //设置有效期
            try {
                projectCdnVo.setUnitExpiry(GoodsUtils.translateExpiryValue(projectCdnVo.getUnitExpiry()));
            } catch (Exception e) {
                log.error("======有效期转换异常", e);
            }
        }
        return projectCdnVoList;
    }

    @Override
    public List<ProjectCdnVo> prjList(String[] projectIds) {
        List<ProjectCdnVo> projectCdnVoList = goodsMapper.selectProjectBriefList(projectIds);
        for (ProjectCdnVo projectCdnVo : projectCdnVoList) {
            //销售渠道
            List<ProjectChannel> projectChannelList = goodsChannelsMapper.selectGoodsChannelByGoodsId(projectCdnVo.getId().intValue());
            projectCdnVo.setProjectChannelList(projectChannelList);

            //portal字段
            String portal = this.getPortal(projectCdnVo.getId().intValue());
            projectCdnVo.setPortal(portal);


            //设置有效期
            try {
                projectCdnVo.setUnitExpiry(GoodsUtils.translateExpiryValue(projectCdnVo.getUnitExpiry()));
                projectCdnVo.setProjectUnitExpiry(projectCdnVo.getUnitExpiry());
            } catch (Exception e) {
                log.error("======有效期转换异常", e);
            }
            //设置maxNight,bookAfterLeave
            GoodsSetting goodsSetting = goodsSettingService.selectByGoodsId(Integer.valueOf(projectCdnVo.getId().toString()));
            projectCdnVo.setMaxNight(goodsSetting.getMaxNight());
            projectCdnVo.setBookAfterLeave(goodsSetting.getAccomAfterLeave());

            //设置产品组
            PrjGroupVo pgvo = new PrjGroupVo();
            pgvo.setProjectId(projectCdnVo.getId());
            List<PrjGroupVo> prjGroupVoList = productGroupMapper.selectPrjGroupList(pgvo);
            for (PrjGroupVo prjGroupVo : prjGroupVoList) {
                //商品组查询
                PrjGroupGoods prjGroupGoods = new PrjGroupGoods();
                prjGroupGoods.setGroupId(prjGroupVo.getId());
                prjGroupGoods.setProjectId(projectCdnVo.getId());
                log.info("prjGroupGoods参数{}",prjGroupGoods);
                List<PrjGroupGoods> prjGroupGoodsList = productGroupMapper.findListByProjectIdAndGroupId(prjGroupGoods);
                log.info("prjGroupGoodsList参数{}",prjGroupGoodsList);
                //商品详情信息查询
                List<GoodsDetailVo> goodsDetailVoList = productMapper.queryGoodsDetail(prjGroupGoodsList);

                //商品规格查询
                List<ShopItem> shopItems = shopItemMapper.selectShopItemByIds(goodsDetailVoList.stream().map(g -> g.getShopItemId()).collect(Collectors.toList()));
                for (GoodsDetailVo gdv : goodsDetailVoList) {
                    gdv.setGroupId(prjGroupVo.getId());
                    List<ShopItem> shopItemList = shopItems.stream().filter(shopItem -> shopItem.getId().equals(gdv.getShopItemId())).collect(Collectors.toList());

                    if(shopItemList!=null && shopItemList.size()>0){
                        gdv.setItemList(shopItemList);
                        gdv.setType(shopItemList.get(0).getServiceName());
                        gdv.setTypeCode(shopItemList.get(0).getServiceType());
                        gdv.setMinPrice(this.getMinPrice(shopItemList.get(0)));
                    }

                    List<SysFile> fileList = sysFileMapper.selectShopPic(gdv.getShopId());
                    List<String> stringList = new ArrayList<String>();
                    if (fileList == null || fileList.size() == 0) {
                        stringList.add("https://cdn.colourfulchina.com/alipay/shop/imgs/shop-small.jpg");
                        stringList.add("https://cdn.colourfulchina.com/alipay/shop/imgs/shop-large.jpg");
                    } else {
                        for (SysFile sysFile : fileList) {
                            stringList.add("https://cdn2.colourfulchina.com/upload/" + sysFile.getGuid() + "." + sysFile.getExt());
                        }
                    }
                    gdv.setImgList(stringList);
                    //住宿的根据权益code转换权益展现形式
                    if ("accom".equals(gdv.getTypeCode()) && StringUtils.isNotEmpty(gdv.getGiftCode())) {
                        gdv.setGiftName(ProductEnums.GiftTypeEnum.getGiftDesc(gdv.getGiftCode()));
                    }
                    if ("accom".equals(gdv.getTypeCode())){
                        gdv.setShopTitle(gdv.getHotel());
                    }else {
                        gdv.setShopTitle(gdv.getHotel()+"|"+gdv.getShopName());
                    }
                }
                prjGroupVo.setGoodsDetailVoList(goodsDetailVoList);
            }
            projectCdnVo.setPrjGroupVoList(prjGroupVoList);
        }
        return projectCdnVoList;
    }

    private BigDecimal getMinPrice(ShopItem shopItem){
        BigDecimal minNet = shopItemNetPriceRuleMapper.selectMinPrice(shopItem.getId());
        //下午茶双人套餐特殊
        if ("tea".equals(shopItem.getType()) && shopItem.getName().contains("双人")){
            minNet = minNet.divide(new BigDecimal(2));
        }
        System.out.println("======minprice======" + minNet);
        return minNet;
    }

    @Override
    public GoodsDetailVo getGoodsDetail(Long pgpId)throws Exception {
        GoodsDetailVo gdv = productMapper.getGoodsDetail(pgpId);
        //商品规格查询
        List<Long> shopItemIds = new ArrayList<Long>();
        shopItemIds.add(gdv.getShopItemId());
        List<ShopItem> shopItemList = shopItemMapper.selectShopItemByIds(shopItemIds);
        gdv.setItemList(shopItemList);
        gdv.setType(shopItemList.get(0).getServiceName());
        gdv.setTypeCode(shopItemList.get(0).getServiceType());

        List<SysFile> fileList = sysFileMapper.selectShopPic(gdv.getShopId());
        List<String> stringList = new ArrayList<String>();
        if (fileList == null || fileList.size() == 0) {
            stringList.add("https://cdn.colourfulchina.com/alipay/shop/imgs/shop-small.jpg");
            stringList.add("https://cdn.colourfulchina.com/alipay/shop/imgs/shop-large.jpg");
        } else {
            for (SysFile sysFile : fileList) {
                stringList.add("https://cdn2.colourfulchina.com/upload/" + sysFile.getGuid() + "." + sysFile.getExt());
            }
        }
        gdv.setImgList(stringList);
        //住宿的根据权益code转换权益展现形式
        if ("accom".equals(gdv.getTypeCode()) && StringUtils.isNotEmpty(gdv.getGiftCode())) {
            gdv.setGiftName(ProductEnums.GiftTypeEnum.getGiftDesc(gdv.getGiftCode()));
        }
        return gdv;
    }

    @Override
    public Project getProjectById(Integer id) {
        Project project = goodsMapper.selectProjectById(id);
        //portal字段
        String portal = this.getPortal(project.getId().intValue());
        project.setPortal(portal);
        return project;
    }

    @Override
    public List<GoodsBlockVo> queryGiftBlockList(BookOrderReqVo bookOrderReqVo) {
        List<GoodsBlockVo> result = new ArrayList<GoodsBlockVo>();
        com.colourfulchina.pangu.taishang.api.entity.ShopItem shopItem = productMapper.queryShopItemBlock(Long.valueOf(bookOrderReqVo.getGoodsId()));
        List<String> blockList = productMapper.queryGiftBlockList(bookOrderReqVo);
        if(shopItem!=null){
            blockList.add(shopItem.getBlockRule());
        }

        StringBuilder str = new StringBuilder();
        blockList.stream().filter(s -> StringUtils.isNotEmpty(s)).forEach(b -> {
                b = b.replaceAll("\\s","");
                String[] blocks = b.split(",");
                for(String block : blocks){
                    GoodsBlockVo vo = new GoodsBlockVo();
                    vo.setItem_id(shopItem.getId());
                    vo.setBlock(block);
                    result.add(vo);
                }
            });
        return result;
    }

    /**
     * 支付宝产品预约时间范围及价格组装
     * @param productGroupProductId
     * @return
     */
    @Override
    public AlipayBookPriceVo prepareAlipayBookPrice(Integer productGroupProductId) throws Exception {
        AlipayBookPriceVo alipayBookPriceVo = new AlipayBookPriceVo();
        int hour = DateUtil.hour(new Date(),true);
        BlockBookDaysVo vo = productGroupProductMapper.queryBookDays(productGroupProductId);
        //查询最小最大提前预约时间
        Date startDate;//有效范围开始时间
        Date endDate;//有效范围结束时间
        if ("drink".equals(vo.getServiceType()) || vo.getServiceType().indexOf("_cpn") > -1){
            startDate = new Date();
            endDate = new Date();
        }else {
            //商户中最小提前预约天数
            Integer shopMinBookDay = vo.getShopMinBookDays();
            if (hour>=17){
                if (shopMinBookDay != null){
                    shopMinBookDay = shopMinBookDay + 1;
                }
            }
            //商户中最大提起预约天数
            Integer shopMaxBookDay = null;
            if (shopMinBookDay != null && vo.getShopMaxBookDays() != null){
                shopMaxBookDay = vo.getShopMaxBookDays()+shopMinBookDay;
            }
            //商品中最小提前预约天数
            Integer goodsMinBookDay = vo.getGoodsMinBookDays();
            if (hour>=17){
                if (goodsMinBookDay != null){
                    goodsMinBookDay = goodsMinBookDay + 1;
                }
            }
            //商品中最大提前预约天数
            Integer goodsMaxBookDay = null;
            if (goodsMinBookDay != null && vo.getGoodsMaxBookDays() != null){
                goodsMaxBookDay = vo.getGoodsMaxBookDays()+goodsMinBookDay;
            }
            //产品组中最小提前预约天数
            Integer productGroupMinBookDay = vo.getGroupMinBookDays();
            if (hour>=17){
                if (productGroupMinBookDay != null){
                    productGroupMinBookDay = productGroupMinBookDay + 1;
                }
            }
            //产品组中最大提前预约天数
            Integer productGroupMaxBookDay = null;
            if (productGroupMinBookDay != null && vo.getGroupMaxBookDays() != null){
                productGroupMaxBookDay = vo.getGroupMaxBookDays()+productGroupMinBookDay;
            }
            //综合最小提前预约天数
            Integer minBookDay = maxInteger(shopMinBookDay,maxInteger(goodsMinBookDay,productGroupMinBookDay));
            //综合最大提前预约天数
            Integer maxBookDay = minInteger(shopMaxBookDay,minInteger(goodsMaxBookDay,productGroupMaxBookDay));
            //获取资源默认的最小最大提前预约时间
            BookNum bookNum = BookMinMaxDayUtils.getBookByService(vo.getServiceType(),vo.getCountryId());
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
        //组装区间内的净价服务费税费信息
        List<GoodsPriceVo> goodsPriceVos = Lists.newLinkedList();
        //获取价格信息
        ProductGroupProduct productGroupProduct = productGroupProductMapper.selectById(productGroupProductId);
        Product product = productMapper.selectById(productGroupProduct.getProductId());
        Wrapper priceWrapper = new Wrapper() {
            @Override
            public String getSqlSegment() {
                return "where del_flag = 0 and shop_item_id ="+product.getShopItemId();
            }
        };
        List<ShopItemNetPriceRule> priceList = shopItemNetPriceRuleService.selectList(priceWrapper);
        //获取可预约日期列表
        List<Date> bookDates = DateUtils.containDateList(startDate,endDate,null);
        if (!CollectionUtils.isEmpty(bookDates)){
            for (Date date : bookDates) {
                GoodsPriceVo goodsPriceVo = new GoodsPriceVo();
                ShopItemNetPriceRule price = shopItemNetPriceRuleService.foundPriceByTime(date,priceList);
                goodsPriceVo.setDate(date);
                Integer dateForWeek = DateUtils.dateForWeek(date);
                if (dateForWeek.compareTo(1) == 0){
                    goodsPriceVo.setWeekDay(7);
                }else {
                    goodsPriceVo.setWeekDay(dateForWeek-1);
                }
                if (price != null){
                    goodsPriceVo.setNet(price.getNetPrice());
                    goodsPriceVo.setServiceFeeRate(price.getServiceRate());
                    goodsPriceVo.setServiceFeeRateStr(price.getServiceRate().multiply(new BigDecimal(100)).doubleValue()+"%");
                    goodsPriceVo.setIncrTaxRate(price.getTaxRate());
                    goodsPriceVo.setIncrTaxRateStr(price.getTaxRate().multiply(new BigDecimal(100)).doubleValue()+"%");
                }
                goodsPriceVos.add(goodsPriceVo);
            }
        }
        Map<Integer,List<GoodsPriceVo>> priceMap = Maps.newHashMap();
        priceMap.put(product.getShopItemId(),goodsPriceVos);
        alipayBookPriceVo.setBookStartTime(DateUtil.format(startDate,"yyyy-MM-dd") + " 00:00");
        alipayBookPriceVo.setBookEndTime(DateUtil.format(endDate,"yyyy-MM-dd") + " 23:00");
        alipayBookPriceVo.setNowDate(DateUtil.format(new Date(),"yyyy-MM-dd"));
        alipayBookPriceVo.setPriceMap(priceMap);
        return alipayBookPriceVo;
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

    private String getPortal(Integer productId) {
        Map<String, Object> content = new HashMap<String, Object>();
        List<GoodsClause> goodsClauseList = goodsClauseMapper.selectGoodsClauseById(productId);
        Map<String, String> clauseMap = new HashMap<String, String>();
        for (GoodsClause clause : goodsClauseList) {
            if("total".equals(clause.getClauseType())){
                clauseMap.put("main", clause.getClause());
            }else{
                clauseMap.put(clause.getClauseType(), clause.getClause());
            }
        }
        content.put("clause",clauseMap);
        GoodsDetail goodsDetail = goodsDetailMapper.selectById(productId);
        content.put("summary",Optional.ofNullable(goodsDetail.getDetail()).orElse(""));
        String jsonStr = JSON.toJSONString(content);
        String str = jsonStr.replaceAll("<p>","").replaceAll("</p>","\\\\n");
        return str;
    }
}
