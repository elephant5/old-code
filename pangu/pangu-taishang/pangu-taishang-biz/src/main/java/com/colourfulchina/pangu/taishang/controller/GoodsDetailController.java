package com.colourfulchina.pangu.taishang.controller;

import com.colourfulchina.god.door.api.annotation.SysGodDoorLog;
import com.colourfulchina.god.door.api.util.SecurityUtils;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.pangu.taishang.api.entity.GoodsDetail;
import com.colourfulchina.pangu.taishang.service.GoodsDetailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
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

@RestController
@RequestMapping("/goodsDetail")
@Slf4j
@Api(tags = {"商品详情"})
public class GoodsDetailController {
    @Autowired
    private GoodsDetailService goodsDetailService;

    /**
     * 根据goodsId查询商品详情
     * @param goodsId
     * @return
     */
    @SysGodDoorLog("根据id查询使用规则")
    @ApiOperation("根据id查询使用规则")
    @PostMapping("/get/{goodsId}")
    @Cacheable(value = "GoodsDetail",key = "'get_'+#goodsId",unless = "#result == null")
    public CommonResultVo<GoodsDetail> get(@PathVariable Integer goodsId){
        CommonResultVo<GoodsDetail> result = new CommonResultVo<>();
        try {
            Assert.notNull(goodsId,"商品id不能为空");
            final GoodsDetail goodsDetail = goodsDetailService.selectById(goodsId);
            result.setResult(goodsDetail);
        }catch (Exception e){
            log.error("查询商品使用规则出错",e);
        }
        return result;
    }

    @SysGodDoorLog("新增商品详情")
    @ApiOperation("新增商品详情")
    @PostMapping("/insert")
    @CacheEvict(value = {"GoodsDetail","Goods"},allEntries = true)
    public CommonResultVo<GoodsDetail> insert(@RequestBody GoodsDetail goodsDetail){
        CommonResultVo<GoodsDetail>  result = new CommonResultVo<>();
        try {
            Assert.notNull(goodsDetail,"参数不能为空");
            Assert.notNull(goodsDetail.getGoodsId(),"参数goodsId不能为空");
            goodsDetail.setCreateTime(new Date());
            goodsDetail.setCreateUser(SecurityUtils.getLoginName());
            goodsDetail.setUpdateTime(new Date());
            goodsDetail.setUpdateUser(SecurityUtils.getLoginName());
            goodsDetailService.insert(goodsDetail);
            result.setResult(goodsDetail);
        }catch (Exception e){
            log.error("新增商品详情失败",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    @SysGodDoorLog("更新商品详情")
    @ApiOperation("更新商品详情")
    @PostMapping("/update")
    @CacheEvict(value = {"GoodsDetail","Goods"},allEntries = true)
    public CommonResultVo<GoodsDetail> update(@RequestBody GoodsDetail goodsDetail){
        CommonResultVo<GoodsDetail>  result = new CommonResultVo<>();
        try {
            Assert.notNull(goodsDetail,"参数不能为空");
            Assert.notNull(goodsDetail.getGoodsId(),"参数goodsId不能为空");
            updateGoodsDetail(goodsDetail);
            goodsDetail.setUpdateTime(new Date());
            goodsDetail.setUpdateUser(SecurityUtils.getLoginName());
            goodsDetailService.updateById(goodsDetail);
            result.setResult(goodsDetail);
        }catch (Exception e){
            log.error("更新商品详情失败",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }
    /**
     * 填充不进行更新的字段
     * @param goodsDetail
     * @throws Exception
     */
    private void updateGoodsDetail(GoodsDetail goodsDetail) throws Exception {
        final GoodsDetail detail = goodsDetailService.selectById(goodsDetail.getGoodsId());
        if (detail == null) {
            throw new Exception("goodsId:" + goodsDetail.getGoodsId() + "不存在");
        } else {
            goodsDetail.setCreateTime(detail.getCreateTime());
            goodsDetail.setCreateUser(detail.getCreateUser());
        }
    }
    @SysGodDoorLog("保存商品详情")
    @ApiOperation("保存商品详情")
    @PostMapping("/save")
    @CacheEvict(value = {"GoodsDetail","Goods"},allEntries = true)
    public CommonResultVo<GoodsDetail> save(@RequestBody GoodsDetail goodsDetail){
        CommonResultVo<GoodsDetail>  result = new CommonResultVo<>();
        try {
            Assert.notNull(goodsDetail,"参数不能为空");
            Assert.notNull(goodsDetail.getGoodsId(),"参数goodsId不能为空");
            final GoodsDetail detail = goodsDetailService.selectById(goodsDetail.getGoodsId());
            if (detail == null) {
                goodsDetail.setCreateTime(new Date());
                goodsDetail.setCreateUser(SecurityUtils.getLoginName());
            } else {
                goodsDetail.setCreateTime(detail.getCreateTime());
                goodsDetail.setCreateUser(detail.getCreateUser());
            }
            goodsDetail.setUpdateTime(new Date());
            goodsDetail.setUpdateUser(SecurityUtils.getLoginName());
            final boolean update = goodsDetailService.insertOrUpdate(goodsDetail);
            Assert.isTrue(update,"保存商品详情失败");
            result.setResult(goodsDetail);
        }catch (Exception e){
            log.error("保存商品详情失败",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

}