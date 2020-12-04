package com.colourfulchina.mars.service.impl;

import com.alibaba.fastjson.JSON;
import com.colourfulchina.mars.api.config.MarsApiConfig;
import com.colourfulchina.mars.api.entity.GiftCode;
import com.colourfulchina.mars.api.vo.req.GiftOrderItemReqVo;
import com.colourfulchina.mars.api.vo.req.GiftOrderReqVo;
import com.colourfulchina.mars.api.vo.req.QueryGiftReqVO;
import com.colourfulchina.mars.api.vo.res.GiftOrderResVo;
import com.colourfulchina.mars.mapper.GiftCodeMapper;
import com.colourfulchina.mars.service.GiftOrderApiService;
import com.colourfulchina.mars.service.GiftService;
import com.colourfulchina.mars.utils.CheckSignUtils;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;


@Slf4j
@Service
@AllArgsConstructor
public class GiftServiceImpl implements GiftService {

    @Autowired
    private GiftCodeMapper giftCodeMapper;

    @Autowired
    private GiftOrderApiService giftOrderApiService;

    @Autowired
    private MarsApiConfig marsApiConfig;


    /**
     * 判断用户在指定商品下是否有有效期内的权益:有返回code,没有发送权益并返回code
     * @param reqVO
     * @return
     */
    @Override
    public String queryGiftByAcIdAndGoodsId(QueryGiftReqVO reqVO) throws Exception {
        List<GiftCode> giftCodeList = giftCodeMapper.selectGiftByAcIdAndGoodsId(reqVO);
        if(CollectionUtils.isEmpty(giftCodeList) || (null != giftCodeList.get(0).getActExpireTime()) && giftCodeList.get(0).getActExpireTime().before(new Date()) ){
            //用户在此商品下没有权益或者权益已经过期 : 给用户发权益
            GiftOrderReqVo orderReqVo = new GiftOrderReqVo();
            orderReqVo.setCapitalOrderId(reqVO.getAcChannel()+System.currentTimeMillis()); //主订单号
            orderReqVo.setAcChannel(reqVO.getAcChannel());
            orderReqVo.setActivateTag(true); //是否直接激活
            orderReqVo.setBuyerMobile(reqVO.getMobile());
            orderReqVo.setSmsTag(false); //是否发送短信
            orderReqVo.setPayTag(false); //是否需要支付 1需要支付，0无需支付
            List<GiftOrderItemReqVo> itemlist = Lists.newArrayList();
            GiftOrderItemReqVo orderItemReqVo = new GiftOrderItemReqVo();
            orderItemReqVo.setItemOrderId(orderReqVo.getCapitalOrderId()+"S001");//子订单ID
            orderItemReqVo.setGoodsId(reqVO.getGoodsId());
            orderItemReqVo.setGoodsNum(1); //购买数量
            itemlist.add(orderItemReqVo);
            orderReqVo.setItemlist(itemlist);
            String params = CheckSignUtils.encrypt(JSON.toJSONString(orderReqVo), marsApiConfig.getEncryptKey());
            orderReqVo.setSign(params);
            try {
                GiftOrderResVo order = giftOrderApiService.createOrder(orderReqVo);
                if(null != order){
                    return order.getItemlist().get(0).getGiftCode();
                }
            } catch (Exception e) {
                log.error("激活订单下单失败");
                throw new Exception(e.getMessage());
            }
        }
        return giftCodeList.get(0).getActCode();
    }
}
