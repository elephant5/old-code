package com.colourfulchina.pangu.taishang.controller;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.colourfulchina.god.door.api.annotation.SysGodDoorLog;
import com.colourfulchina.god.door.api.util.SecurityUtils;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.pangu.taishang.api.entity.Goods;
import com.colourfulchina.pangu.taishang.api.entity.GoodsPrice;
import com.colourfulchina.pangu.taishang.api.vo.res.GoodsPriceListRes;
import com.colourfulchina.pangu.taishang.api.vo.res.GoodsPriceRes;
import com.colourfulchina.pangu.taishang.service.GoodsPriceService;
import com.colourfulchina.pangu.taishang.service.GoodsService;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/goodsPrice")
@Slf4j
@AllArgsConstructor
@Api(tags = {"商品销售价"})
public class GoodsPriceController {
    @Autowired
    private GoodsPriceService goodsPriceService;
    @Autowired
    private GoodsService goodsService;
    /**
     * 根据商品id查询销售价格列表
     * @param goodsPrice
     * @return
     */
    @SysGodDoorLog("根据商品id查询销售价格列表")
    @ApiOperation("根据商品id查询销售价格列表")
    @PostMapping("/list")
    public CommonResultVo<GoodsPriceListRes> list(@RequestBody GoodsPrice goodsPrice){
        CommonResultVo<GoodsPriceListRes> result = new CommonResultVo<>();
        try {
            List<GoodsPriceRes> goodsPriceResList=Lists.newArrayList();
            Assert.notNull(goodsPrice,"参数不能为空");
            Assert.notNull(goodsPrice.getGoodsId(),"商品id不能为空");

            Wrapper<GoodsPrice> listWrapper=new Wrapper<GoodsPrice>() {
                @Override
                public String getSqlSegment() {
                    return "where del_flag = 0 and goods_id = "+goodsPrice.getGoodsId();
                }
            };
            final Goods goods = goodsService.selectById(goodsPrice.getGoodsId());
            GoodsPriceListRes goodsPriceListRes=new GoodsPriceListRes();
            goodsPriceListRes.setGoodsId(goods.getId());
            goodsPriceListRes.setPrice(goods.getSalesPrice());
            final List<GoodsPrice> goodsPriceList = goodsPriceService.selectList(listWrapper);
            goodsPriceListRes.setGoodsPriceResList(updateGoodsPriceResList(goodsPriceList,goodsPrice.getGoodsId()));
            result.setResult(goodsPriceListRes);
        }catch (Exception e){
            log.error("查询商品销售价格出错",e);
        }
        return result;
    }

    private GoodsPriceRes copyGoodsPrice(Goods goods, GoodsPrice price) {
        GoodsPriceRes priceRes=new GoodsPriceRes();
        BeanUtils.copyProperties(price,priceRes);
        priceRes.setPrice(goods.getSalesPrice());
        return priceRes;
    }

    /**
     * 根据id查询销售价格
     * @param id
     * @return
     */
    @SysGodDoorLog("根据id查询销售价格")
    @ApiOperation("根据id查询销售价格")
    @PostMapping("/get/{id}")
    @Cacheable(value = "GoodsPrice",key = "'get_'+#id",unless = "#result == null")
    public CommonResultVo<GoodsPriceRes> get(@PathVariable Integer id){
        CommonResultVo<GoodsPriceRes> result = new CommonResultVo<>();
        try {
            Assert.notNull(id,"商品id不能为空");
            final GoodsPrice goodsPrice = goodsPriceService.selectById(id);
            result.setResult(updateGoodsPriceRes(goodsPrice));
        }catch (Exception e){
            log.error("查询商品销售价格出错",e);
        }
        return result;
    }

    @SysGodDoorLog("新增商品销售价格")
    @ApiOperation("新增商品销售价格")
    @PostMapping("/insert")
    @CacheEvict(value = {"GoodsPrice","Goods"},allEntries = true)
    public CommonResultVo<GoodsPriceRes> insert(@RequestBody GoodsPrice goodsPrice){
        CommonResultVo<GoodsPriceRes>  result = new CommonResultVo<>();
        try {
            Assert.notNull(goodsPrice,"参数不能为空");
            Assert.notNull(goodsPrice.getGoodsId(),"参数goodsId不能为空");
            goodsPrice.setCreateTime(new Date());
            goodsPrice.setCreateUser(SecurityUtils.getLoginName());
            goodsPrice.setUpdateTime(new Date());
            goodsPrice.setUpdateUser(SecurityUtils.getLoginName());
            final boolean insert = goodsPriceService.insert(goodsPrice);
            Assert.isTrue(insert,"新增商品销售价格失败");
            result.setResult(updateGoodsPriceRes(goodsPrice));
        }catch (Exception e){
            log.error("新增商品销售价格失败",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    @SysGodDoorLog("更新商品销售价格")
    @ApiOperation("更新商品销售价格")
    @PostMapping("/update")
    @CacheEvict(value = {"GoodsPrice","Goods"},allEntries = true)
    public CommonResultVo<GoodsPriceRes> update(@RequestBody GoodsPrice goodsPrice){
        CommonResultVo<GoodsPriceRes>  result = new CommonResultVo<>();
        try {
            Assert.notNull(goodsPrice,"参数不能为空");
            Assert.notNull(goodsPrice.getGoodsId(),"参数goodsId不能为空");
            updateGoodsPrice(goodsPrice);
            goodsPrice.setUpdateTime(new Date());
            goodsPrice.setUpdateUser(SecurityUtils.getLoginName());
            final boolean update = goodsPriceService.updateById(goodsPrice);
            Assert.isTrue(update,"更新商品销售价格失败");
            GoodsPriceRes goodsPriceRes=new GoodsPriceRes();
            result.setResult(updateGoodsPriceRes(goodsPrice));
        }catch (Exception e){
            log.error("更新商品销售价格失败",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    @SysGodDoorLog("保存商品销售价格")
    @ApiOperation("保存商品销售价格")
    @PostMapping("/save")
    @CacheEvict(value = {"GoodsPrice","Goods"},allEntries = true)
    public CommonResultVo<GoodsPriceRes> save(@RequestBody GoodsPrice goodsPrice){
        CommonResultVo<GoodsPriceRes>  result = new CommonResultVo<>();
        try {
            Assert.notNull(goodsPrice,"参数不能为空");
            Assert.notNull(goodsPrice.getGoodsId(),"参数goodsId不能为空");
                if (goodsPrice.getId() == null){
                    goodsPrice.setCreateTime(new Date());
                    goodsPrice.setCreateUser(SecurityUtils.getLoginName());
                }else {
                    updateGoodsPrice(goodsPrice);
                }
                goodsPrice.setUpdateTime(new Date());
                goodsPrice.setUpdateUser(SecurityUtils.getLoginName());
            final boolean update = goodsPriceService.insertOrUpdate(goodsPrice);
            Assert.isTrue(update,"保存商品销售价格失败");
            result.setResult(updateGoodsPriceRes(goodsPrice));
        }catch (Exception e){
            log.error("保存商品销售价格失败",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    @SysGodDoorLog("批量保存商品销售价格")
    @ApiOperation("批量保存商品销售价格")
    @PostMapping("/saveBatch")
    @CacheEvict(value = {"GoodsPrice","Goods"},allEntries = true)
    public CommonResultVo<List<GoodsPriceRes>> saveBatch(@RequestBody List<GoodsPrice> priceList){
        CommonResultVo<List<GoodsPriceRes>>  result = new CommonResultVo<>();
        try {
            Assert.notEmpty(priceList,"参数不能为空");
            for (GoodsPrice goodsPrice:priceList){
                Assert.notNull(goodsPrice.getGoodsId(),"参数goodsId不能为空");
                if (goodsPrice.getId() == null){
                    goodsPrice.setCreateTime(new Date());
                    goodsPrice.setCreateUser(SecurityUtils.getLoginName());
                }else {
                    final GoodsPrice price = goodsPriceService.selectById(goodsPrice.getId());
                    updateGoodsPrice(goodsPrice);
                }
                goodsPrice.setUpdateTime(new Date());
                goodsPrice.setUpdateUser(SecurityUtils.getLoginName());
            }
            final boolean updateBatch = goodsPriceService.insertOrUpdateBatch(priceList);
            Assert.isTrue(updateBatch,"批量保存商品销售价格失败");
            result.setResult(updateGoodsPriceResList(priceList, priceList.get(0).getGoodsId()));
        }catch (Exception e){
            log.error("批量保存商品销售价格失败",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    /**
     * 批量更新List<GoodsPriceRes>
     * @param priceList
     * @param goodsId
     * @return
     */
    private List<GoodsPriceRes> updateGoodsPriceResList(List<GoodsPrice> priceList, Integer goodsId) {
        final Goods goods = goodsService.selectById(goodsId);
        List<GoodsPriceRes> priceResList=Lists.newArrayList();
        priceList.forEach(price->{
            GoodsPriceRes priceRes=new GoodsPriceRes();
            BeanUtils.copyProperties(price,priceRes);
            priceRes.setPrice(goods.getSalesPrice());
            priceResList.add(priceRes);
        });
        return priceResList;
    }

    /**
     * 更新GoodsPriceRes
     * @param price
     * @return
     */
    private GoodsPriceRes updateGoodsPriceRes(GoodsPrice price) {
        final Goods goods = goodsService.selectById(price.getGoodsId());
        GoodsPriceRes priceRes=new GoodsPriceRes();
        BeanUtils.copyProperties(price,priceRes);
        priceRes.setPrice(goods.getSalesPrice());
        return priceRes;
    }

    /**
     * 填充不进行更新的字段
     * @param goodsPrice
     * @throws Exception
     */
    private void updateGoodsPrice(GoodsPrice goodsPrice) throws Exception {
        final GoodsPrice price = goodsPriceService.selectById(goodsPrice.getId());
        if (price == null) {
            throw new Exception("id:" + goodsPrice.getId() + "对应的销售价格不存在");
        } else {
            goodsPrice.setCreateTime(price.getCreateTime());
            goodsPrice.setCreateUser(price.getCreateUser());
        }
    }
}