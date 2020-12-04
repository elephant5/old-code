package com.colourfulchina.pangu.taishang.service.impl;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.inf.base.enums.SysDictTypeEnums;
import com.colourfulchina.pangu.taishang.api.entity.GoodsChannels;
import com.colourfulchina.pangu.taishang.api.vo.req.ShopListQueryReq;
import com.colourfulchina.pangu.taishang.api.vo.res.GoodsChannelRes;
import com.colourfulchina.pangu.taishang.api.vo.res.SalesChannelRes;
import com.colourfulchina.pangu.taishang.mapper.GoodsChannelsMapper;
import com.colourfulchina.pangu.taishang.service.GoodsChannelsService;
import com.colourfulchina.tianyan.admin.api.entity.SysDict;
import com.colourfulchina.tianyan.admin.api.feign.RemoteDictService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class GoodsChannelServiceImpl extends ServiceImpl<GoodsChannelsMapper, GoodsChannels> implements GoodsChannelsService {


    @Autowired
    GoodsChannelsMapper goodsChannelsMapper;
    private final RemoteDictService remoteDictService;

    /**
     * 根据goodsId查询渠道信息
     *
     * @param goodsId
     * @return
     */
    @Override
    public List<GoodsChannels> selectGoodsChannelsByGoodsId(Integer goodsId) {
        Wrapper<GoodsChannels> localWrapper = new Wrapper<GoodsChannels>() {
            @Override
            public String getSqlSegment() {
                return " where goods_id = " + goodsId;
            }
        };
        List<GoodsChannels> goodsChannelsList =goodsChannelsMapper.selectList(localWrapper);
        return goodsChannelsList;
    }

    /**
     * 根据商品ID和渠道ID查询
     *
     * @param goodsId
     * @param channelId
     * @return
     */
    @Override
    public GoodsChannels selectByGoodsIdAndChannelId(Integer goodsId, Integer channelId) {
        GoodsChannels goodsChannels = new GoodsChannels();
        goodsChannels.setGoodsId(goodsId);
        goodsChannels.setChannelId(channelId);
        GoodsChannels goodsChannelsParams =goodsChannelsMapper.selectOne(goodsChannels);
        return goodsChannelsParams;
    }

    /**
     * 根据商品id查询渠道详情
     * @param goodsId
     * @return
     * @throws Exception
     */
    @Override
    public List<GoodsChannelRes> selectGoodsChannel(Integer goodsId) throws Exception {
        List<SysDict> bankList = remoteDictService.findDictByType(SysDictTypeEnums.BANK_TYPE.getType()).getData();
        List<SysDict> salesChannelList = remoteDictService.findDictByType(SysDictTypeEnums.SALES_CHANNEL_TYPE.getType()).getData();
        List<SysDict> salesWayList = remoteDictService.findDictByType(SysDictTypeEnums.SALES_WAY_TYPE.getType()).getData();
        Map<String, SysDict> bankMap = bankList.stream().collect(Collectors.toMap(SysDict::getValue, bank -> bank));
        Map<String, SysDict> salesChannelMap = salesChannelList.stream().collect(Collectors.toMap(SysDict::getValue, bank -> bank));
        Map<String, SysDict> salesWayMap = salesWayList.stream().collect(Collectors.toMap(SysDict::getValue, bank -> bank));
        List<GoodsChannelRes> list = goodsChannelsMapper.selectGoodsChannel(goodsId);
        for (GoodsChannelRes goodsChannelRes : list) {
            SysDict bank = bankMap.get(goodsChannelRes.getBankId());
            if(null  == bank ){
                goodsChannelRes.setBankName("-");
            }else{
                goodsChannelRes.setBankCode(bank.getRemarks());
                goodsChannelRes.setBankName(bank.getLabel());
            }
            SysDict channel = salesChannelMap.get(goodsChannelRes.getSalesChannelId());
            if(null  == channel ){
                goodsChannelRes.setSalesChannelName("-");
            }else{
                goodsChannelRes.setSalesChannelName(channel.getLabel());
            }
            SysDict way = salesWayMap.get(goodsChannelRes.getSalesWayId());
            if(null  == way ){
                goodsChannelRes.setSalesWayName("-");
            }else{
                goodsChannelRes.setSalesWayName(way.getLabel());
            }
        }
        return list;
    }

    @Override
    public SalesChannelRes selectGoodsChannel(String channelId) {
        SalesChannelRes salesChannelRes = goodsChannelsMapper.selectGoodsChannelByChannelId(channelId);
        return salesChannelRes;
    }

}