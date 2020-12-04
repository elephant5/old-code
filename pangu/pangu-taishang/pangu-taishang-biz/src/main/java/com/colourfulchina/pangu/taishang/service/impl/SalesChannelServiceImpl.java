package com.colourfulchina.pangu.taishang.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.inf.base.enums.SysDictTypeEnums;
import com.colourfulchina.inf.base.vo.PageVo;
import com.colourfulchina.pangu.taishang.api.dto.SalesChannelVo;
import com.colourfulchina.pangu.taishang.api.entity.SalesChannel;
import com.colourfulchina.pangu.taishang.api.vo.req.SalesChannelReqVo;
import com.colourfulchina.pangu.taishang.api.vo.res.GoodsChannelRes;
import com.colourfulchina.pangu.taishang.mapper.SalesChannelMapper;
import com.colourfulchina.pangu.taishang.service.SalesChannelService;
import com.colourfulchina.pangu.taishang.utils.PinYinUtils;
import com.colourfulchina.tianyan.admin.api.entity.SysDict;
import com.colourfulchina.tianyan.admin.api.feign.RemoteDictService;
import com.colourfulchina.tianyan.common.core.constant.enums.EnumsReqCode;
import com.colourfulchina.tianyan.common.core.util.R;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author: luck.luo
 * @Description:
 * @Date: 2019/2/25 17:42
 */
@Slf4j
@Service
@AllArgsConstructor
public class SalesChannelServiceImpl extends ServiceImpl<SalesChannelMapper, SalesChannel> implements SalesChannelService {
    @Autowired
    private SalesChannelMapper salesChannelMapper;

    private RemoteDictService remoteDictService;

    @Override
    public PageVo<SalesChannel> findPageList(PageVo<SalesChannelReqVo> pageVoReq) {
        PageVo<SalesChannel> pageVoRes = new PageVo<>();
        List<SalesChannel> list = salesChannelMapper.findPageList(pageVoReq, pageVoReq.getCondition());
        BeanUtils.copyProperties(pageVoReq, pageVoRes);
        return pageVoRes.setRecords(list);
    }

    /**
     * 根据三个属性确定一条销售渠道
     *
     * @param bankId
     * @param salesChannelId
     * @param salesWayId
     * @return
     */
    @Override
    public SalesChannel selectOneDate(String bankId, String salesChannelId, String salesWayId) {
        SalesChannel paramsSalesChannel = new SalesChannel();
        paramsSalesChannel.setBankId(bankId);
        if (StringUtils.isEmpty(salesChannelId)) {
            paramsSalesChannel.setSalesChannelId(null);
        } else {
            paramsSalesChannel.setSalesChannelId(salesChannelId);
        }
        paramsSalesChannel.setSalesWayId(salesWayId);
        return salesChannelMapper.selectSalesChannelOne(paramsSalesChannel);
    }

    @Override
    public Integer count(String bankId, String salesChannelId, String salesWayId) {
        SalesChannel paramsSalesChannel = new SalesChannel();
        paramsSalesChannel.setBankId(bankId);
        paramsSalesChannel.setSalesChannelId(salesChannelId);
        paramsSalesChannel.setSalesWayId(salesWayId);
        return salesChannelMapper.selectSalesChannelCount(paramsSalesChannel);
    }


    @Override
    public List<SalesChannelVo> selectAll() throws Exception {

        Wrapper<SalesChannel> channelWrapper = new Wrapper<SalesChannel>() {
            @Override
            public String getSqlSegment() {
                return "where del_flag = 0  and status = 0 ";
            }
        };
        channelWrapper.orderBy("orders",true);
        List<SalesChannel> salesChannels = salesChannelMapper.selectList(channelWrapper);
        List<SysDict> bankList = selectSysDict(SysDictTypeEnums.BANK_TYPE.getType());
        List<SysDict> salesChannelList = selectSysDict(SysDictTypeEnums.SALES_CHANNEL_TYPE.getType());
        List<SysDict> salesWayList = selectSysDict(SysDictTypeEnums.SALES_WAY_TYPE.getType());
        Map<String, Set<String>> salesChannelMaps = Maps.newHashMap();//银行下的销售渠道
        Map<String, Set<String>> bankMaps = Maps.newHashMap();//销售渠道的销售方式
        for (SalesChannel sale : salesChannels) {
            Set<String> temp = null;
            if (salesChannelMaps.containsKey(sale.getBankId())) {
                temp = salesChannelMaps.get(sale.getBankId());
            } else {
                temp = Sets.newHashSet();
            }
            if (StringUtils.isBlank(sale.getSalesChannelId())) {
                temp.add("-1");
            } else {
                temp.add(sale.getSalesChannelId());
            }
            salesChannelMaps.put(sale.getBankId(), temp);
//-------------------------------------------------------------------------
            Set<String> temp2 = null;
            String channelId = sale.getSalesChannelId();
            if (StringUtils.isBlank(sale.getSalesChannelId())) {
                channelId = "-1";
            }
            if (bankMaps.containsKey(sale.getBankId()+"-"+channelId)) {
                temp2 = bankMaps.get(sale.getBankId()+"-"+channelId);
            } else {
                temp2 = Sets.newHashSet();
            }
            temp2.add(sale.getSalesWayId());
            bankMaps.put(sale.getBankId()+"-"+channelId, temp2);
        }
//        for (SalesChannel sale : salesChannels) {
//            Set<String> temp2 = null;
//            String channelId = sale.getSalesChannelId();
//            if (StringUtils.isBlank(sale.getSalesChannelId())) {
//                channelId = "-1";
//            }
//            if (bankMaps.containsKey(channelId)) {
//                temp2 = bankMaps.get(channelId);
//            } else {
//                temp2 = Sets.newHashSet();
//            }
//
//            temp2.add(sale.getSalesWayId());
//            bankMaps.put("sales"+channelId, temp2);
//        }
        List<SalesChannelVo> salesChannelVos = Lists.newArrayList();
        Map<String, SysDict> bankMap = bankList.stream().collect(Collectors.toMap(SysDict::getValue, bank -> bank));
        Map<String, SysDict> salesChannelMap = salesChannelList.stream().collect(Collectors.toMap(SysDict::getValue, bank -> bank));
        Map<String, SysDict> salesWayMap = salesWayList.stream().collect(Collectors.toMap(SysDict::getValue, bank -> bank));
        for (String bankId : salesChannelMaps.keySet()) {
            Set<String> salesChannelIds = salesChannelMaps.get(bankId);
            SalesChannelVo salesChannelVo = new SalesChannelVo();
            SysDict sysDict = bankMap.get(bankId);
            salesChannelVo.setValue(bankId + "");
            salesChannelVo.setLabel(sysDict.getLabel());

            Set<SalesChannelVo> salesChannelChildren = Sets.newHashSet();
            for (String channel : salesChannelIds) {
                SalesChannelVo son = new SalesChannelVo();
                SysDict sysDict2 = salesChannelMap.get(channel);
                if (channel.equals("-1")) {
                    son.setValue("");
                    son.setLabel("-");
                } else {
                    son.setValue(channel);
                    son.setLabel(sysDict2.getLabel());
                }
                salesChannelChildren.add(son);
                Set<String> wayIds = bankMaps.get(bankId+"-"+channel);

                Set<SalesChannelVo> littleSon = Sets.newHashSet();
                if(null != wayIds && !wayIds.isEmpty() ){
                    for (String way : wayIds) {
                        SalesChannelVo son2 = new SalesChannelVo();
                        SysDict sysDict3 = salesWayMap.get(way);
                        son2.setValue(way + "");
                        son2.setLabel(sysDict3.getLabel());
                        littleSon.add(son2);
                    }
                    son.setChildren(littleSon);
                }

            }
            salesChannelVo.setChildren(salesChannelChildren);
            salesChannelVos.add(salesChannelVo);
        }
        return salesChannelVos;
    }

    @Override
    public List<SysDict> selectSysDict(String type) {
        R<List<SysDict>> resultVO = remoteDictService.findDictByType(type);
        return resultVO.getData();
    }

    @Override
    public List<String> getGoodsServiceType(String type) {
        return null;
    }

    @Override
    public Boolean insertSalesChannel(SalesChannelReqVo t) throws Exception {
        t.setBankId(insertDict(SysDictTypeEnums.BANK_TYPE.getType(), t.getBankName()));
        String salesChannelName = t.getSalesChannelName();
        if(StringUtils.isNotEmpty(salesChannelName)){
            t.setSalesChannelId(insertDict(SysDictTypeEnums.SALES_CHANNEL_TYPE.getType(),salesChannelName ));
        }
        t.setSalesWayId(insertDict(SysDictTypeEnums.SALES_WAY_TYPE.getType(), t.getSalesWayName()));
        //校验银行、销售渠道、销售方式是否重复
        Integer count = salesChannelMapper.selectSalesChannelCount(t);
        Assert.isTrue(count == null || count == 0, "银行、销售渠道、销售方式对应的渠道已存在！");
        Integer insertBy = salesChannelMapper.insert(t);
        return insertBy > 0;
    }

    @Override
    public boolean updateChannelById(SalesChannel t){
        Integer count = salesChannelMapper.selectSalesChannelCount(t);
        Assert.isTrue(count == null || count == 0, "银行、销售渠道、销售方式对应的渠道已存在！");
        Integer updateBy = salesChannelMapper.updateById(t);
        return updateBy > 0;
    }

    @Override
    public List<Integer> selectSalesChannel(String bankId) {
        List<Integer> salesChannelRes = salesChannelMapper.selectSalesChannel(bankId);
        return salesChannelRes;
	}

    /**
     * 根据id查询销售渠道详情
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public GoodsChannelRes findById(Integer id) throws Exception {
        GoodsChannelRes goodsChannelRes = new GoodsChannelRes();
        List<SysDict> bankList = remoteDictService.findDictByType(SysDictTypeEnums.BANK_TYPE.getType()).getData();
        List<SysDict> salesChannelList = remoteDictService.findDictByType(SysDictTypeEnums.SALES_CHANNEL_TYPE.getType()).getData();
        List<SysDict> salesWayList = remoteDictService.findDictByType(SysDictTypeEnums.SALES_WAY_TYPE.getType()).getData();
        Map<String, SysDict> bankMap = bankList.stream().collect(Collectors.toMap(SysDict::getValue, bank -> bank));
        Map<String, SysDict> salesChannelMap = salesChannelList.stream().collect(Collectors.toMap(SysDict::getValue, bank -> bank));
        Map<String, SysDict> salesWayMap = salesWayList.stream().collect(Collectors.toMap(SysDict::getValue, bank -> bank));
        SalesChannel salesChannel = salesChannelMapper.selectById(id);
        BeanUtils.copyProperties(salesChannel,goodsChannelRes);
        SysDict bank = bankMap.get(goodsChannelRes.getBankId());
        if(null  == bank ){
            goodsChannelRes.setBankName("-");
        }else{
            goodsChannelRes.setBankName(bank.getLabel());
            goodsChannelRes.setBankCode(bank.getRemarks());
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
        return goodsChannelRes;
    }

    /**
     * 查询销售渠道列表详情
     * @return
     * @throws Exception
     */
    @Override
    public List<GoodsChannelRes> selectSalesChannelList() throws Exception {
        List<GoodsChannelRes> result = Lists.newLinkedList();
        List<SysDict> bankList = remoteDictService.findDictByType(SysDictTypeEnums.BANK_TYPE.getType()).getData();
        List<SysDict> salesChannelList = remoteDictService.findDictByType(SysDictTypeEnums.SALES_CHANNEL_TYPE.getType()).getData();
        List<SysDict> salesWayList = remoteDictService.findDictByType(SysDictTypeEnums.SALES_WAY_TYPE.getType()).getData();
        Map<String, SysDict> bankMap = bankList.stream().collect(Collectors.toMap(SysDict::getValue, bank -> bank));
        Map<String, SysDict> salesChannelMap = salesChannelList.stream().collect(Collectors.toMap(SysDict::getValue, bank -> bank));
        Map<String, SysDict> salesWayMap = salesWayList.stream().collect(Collectors.toMap(SysDict::getValue, bank -> bank));
        Wrapper wrapper = new Wrapper() {
            @Override
            public String getSqlSegment() {
                return "where del_flag = 0";
            }
        };
        List<SalesChannel> list = salesChannelMapper.selectList(wrapper);
        if (!CollectionUtils.isEmpty(list)){
            for (SalesChannel salesChannel : list) {
                GoodsChannelRes goodsChannelRes = new GoodsChannelRes();
                BeanUtils.copyProperties(salesChannel,goodsChannelRes);
                SysDict bank = bankMap.get(goodsChannelRes.getBankId());
                if(null  == bank ){
                    goodsChannelRes.setBankName("-");
                }else{
                    goodsChannelRes.setBankName(bank.getLabel());
                    goodsChannelRes.setBankCode(bank.getRemarks());
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
                result.add(goodsChannelRes);
            }
        }
        return result;
    }

    @Override
    public List<SalesChannel> selectByBCW(SalesChannel salesChannel) throws Exception{
        Wrapper wrapper = new Wrapper() {
            @Override
            public String getSqlSegment() {
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("where del_flag = 0");
                if (StringUtils.isNotBlank(salesChannel.getBankId())){
                    stringBuffer.append(" and bank_id ='"+salesChannel.getBankId()+"'");
                }else {
                    stringBuffer.append(" and (bank_id IS NULL or bank_id = '')");
                }
                if (StringUtils.isNotBlank(salesChannel.getSalesChannelId())){
                    stringBuffer.append(" and sales_channel_id ='"+salesChannel.getSalesChannelId()+"'");
                }else {
                    stringBuffer.append(" and (sales_channel_id IS NULL or sales_channel_id = '')");
                }
                if (StringUtils.isNotBlank(salesChannel.getSalesWayId())){
                    stringBuffer.append(" and sales_way_id ='"+salesChannel.getSalesWayId()+"'");
                }else {
                    stringBuffer.append(" and (sales_way_id IS NULL or sales_way_id = '')");
                }
                return stringBuffer.toString();
            }
        };
        wrapper.orderBy("orders");
        List<SalesChannel> list = salesChannelMapper.selectList(wrapper);
        return list;
    }

    private synchronized String insertDict(String type, String label) throws Exception {
        // label type =》查到value   直接用查到的value   没有查到 生成   拼音+4位随机数   循环看字典值生成一个唯一值 插入字典
        String value;
        SysDict param = new SysDict();
        param.setType(type);
        param.setLabel(StringUtils.trim(label));
        R<SysDict> resultVO = remoteDictService.selectByType(param);
        Assert.isTrue(resultVO != null && resultVO.getCode() == EnumsReqCode.SUCCESS.getCode(), "请求字典接口失败");
        SysDict sysDict = resultVO.getData();
        if (sysDict != null) {
            value = sysDict.getValue();
        } else {
            //根据类型查找该类型下全部字典项
            R<List<SysDict>> result = remoteDictService.findDictByType(type);
            Assert.isTrue(result != null && result.getCode() == EnumsReqCode.SUCCESS.getCode(), "根据类型查找字典项失败");
            List<SysDict> list = result.getData();
            //防止value重复
            while (true) {
                value = getAutoValue(label);
                boolean flag = false;
                for (SysDict s : list) {
                    if (value.equals(s.getValue())) {
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    break;
                }
            }
            //暂时取label值
            param.setDescription(label);
            param.setSort(new BigDecimal(1));
            //新增字典
            param.setValue(value);
            R<Boolean> r = remoteDictService.dict(param);
            //请求成功
            Assert.isTrue(r != null && r.getCode() == EnumsReqCode.SUCCESS.getCode() && r.getData(), "插入字典项失败");
        }
        return value;
    }

    //生成label拼音首字母+4位数字
    private String getAutoValue(String label) {
        String str = PinYinUtils.getPinYinHeadChar(label);
        String numStr = String.valueOf((int) (Math.random() * 9999));
        return str +"_"+ "0000".substring(numStr.length()) + numStr;
    }

}
