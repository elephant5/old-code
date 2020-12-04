package com.colourfulchina.pangu.taishang.controller;

import cn.hutool.core.lang.Assert;
import com.colourfulchina.god.door.api.annotation.SysGodDoorLog;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.pangu.taishang.api.entity.Goods;
import com.colourfulchina.pangu.taishang.api.entity.GoodsChannels;
import com.colourfulchina.pangu.taishang.api.entity.ProductGroup;
import com.colourfulchina.pangu.taishang.api.entity.SalesChannel;
import com.colourfulchina.pangu.taishang.api.vo.req.GoodsBaseVo;
import com.colourfulchina.pangu.taishang.api.vo.res.GoodsChannelRes;
import com.colourfulchina.pangu.taishang.api.vo.res.SalesChannelRes;
import com.colourfulchina.pangu.taishang.service.GoodsChannelsService;
import com.colourfulchina.pangu.taishang.service.GoodsService;
import com.colourfulchina.pangu.taishang.service.ProductGroupService;
import com.colourfulchina.pangu.taishang.service.SalesChannelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/goodsChannel")
@Slf4j
@Api(tags={"商品和渠道的操作"})
public class GoodsChannelController {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private GoodsChannelsService goodsChannelsService;
    @Autowired
    SalesChannelService salesChannelService;

    @Autowired
    ProductGroupService productGroupService;


    @SysGodDoorLog("修改商品的渠道")
    @ApiOperation("修改商品的渠道")
    @GetMapping("/update")
    public CommonResultVo<Goods> update(@RequestBody GoodsBaseVo goodsVo){
        CommonResultVo<Goods>  result = new CommonResultVo<>();
        try {
            Assert.isNull(goodsVo.getId(),"商品ID不能为空！");
//            Goods goods = goodsService.selectById(goodsVo.getId());
            SalesChannel salesChannel = salesChannelService.selectOneDate(goodsVo.getBankId(),goodsVo.getSalesChannelId(),goodsVo.getSalesWayId());
            List<ProductGroup> productGroupsList =productGroupService.selectByGoodsId(goodsVo.getId());
            if(productGroupsList.size() > 2){
                //需要删掉除了测试的其他渠道数据
            }
            GoodsChannels goodsChannels = goodsChannelsService.selectByGoodsIdAndChannelId(goodsVo.getId(),salesChannel.getId());
            if(null == goodsChannels ){
                goodsChannels = new GoodsChannels();
                goodsChannels.setGoodsId(goodsVo.getId());
                goodsChannels.setChannelId(salesChannel.getId());
                goodsChannelsService.insert(goodsChannels);
                //TODO 测试渠道的添加

            }else{
                goodsChannels.setChannelId(salesChannel.getId());
                goodsChannelsService.updateById(goodsChannels);
            }
            result.setResult(goodsVo);
        }catch (Exception e){
            log.error("修改商品的渠道",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    @SysGodDoorLog("查询商品的销售渠道")
    @ApiOperation("查询商品的销售渠道")
    @PostMapping("/selectGoodsChannel")
    public CommonResultVo<List<GoodsChannelRes>> selectGoodsChannel(@RequestBody Integer goodsId){
        CommonResultVo<List<GoodsChannelRes>> result = new CommonResultVo();
        try {
            List<GoodsChannelRes> list = goodsChannelsService.selectGoodsChannel(goodsId);
            result.setResult(list);
        }catch (Exception e){
            log.error("商品的销售渠道查询失败",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }



}
