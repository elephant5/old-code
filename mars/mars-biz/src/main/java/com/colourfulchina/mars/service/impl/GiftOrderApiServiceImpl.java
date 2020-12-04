package com.colourfulchina.mars.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.colourfulchina.inf.base.encrypt.EncryptAES;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.mars.api.config.MarsApiConfig;
import com.colourfulchina.mars.api.entity.*;
import com.colourfulchina.mars.api.enums.ActCodeStatusEnum;
import com.colourfulchina.mars.api.vo.req.*;
import com.colourfulchina.mars.api.vo.res.GiftCodeResVo;
import com.colourfulchina.mars.api.vo.res.GiftOrderDetailVo;
import com.colourfulchina.mars.api.vo.res.GiftOrderInfoResVo;
import com.colourfulchina.mars.api.vo.res.GiftOrderResVo;
import com.colourfulchina.mars.mapper.*;
import com.colourfulchina.mars.service.GiftCodeService;
import com.colourfulchina.mars.service.GiftOrderApiService;
import com.colourfulchina.mars.service.PanguInterfaceService;
import com.colourfulchina.member.api.entity.MemMemberAccount;
import com.colourfulchina.member.api.feign.RemoteLoginService;
import com.colourfulchina.member.api.req.MemLoginReqDTO;
import com.colourfulchina.nuwa.api.feign.RemoteKltSmsService;
import com.colourfulchina.nuwa.api.sms.model.SmsSendResult;
import com.colourfulchina.nuwa.api.vo.KltSendSmsMsgReq;
import com.colourfulchina.pangu.taishang.api.dto.GoodsPortalSettingDto;
import com.colourfulchina.pangu.taishang.api.feign.RemoteGoodsPortalSettingService;
import com.colourfulchina.pangu.taishang.api.feign.RemoteGoodsService;
import com.colourfulchina.pangu.taishang.api.vo.req.GoodsBaseVo;
import com.colourfulchina.pangu.taishang.api.vo.res.GoodsChannelRes;
import com.icbc.api.internal.util.internal.util.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class GiftOrderApiServiceImpl implements GiftOrderApiService {

    @Autowired
    private GiftOrderCapitalMapper giftOrderCapitalMapper;

    @Autowired
    private GiftOrderItemMapper giftOrderItemMapper;

    @Autowired
    private GiftOrderItemDetailMapper giftOrderItemDetailMapper;

    @Autowired
    private GiftOrderConfigMapper giftOrderConfigMapper;

    @Autowired
    private GiftOrderConfigPortalMapper giftOrderConfigPortalMapper;

    @Autowired
    private GiftCodeService giftCodeService;

    @Autowired
    private MarsApiConfig marsApiConfig;
    @Autowired
    private PanguInterfaceService panguInterfaceService;

    private final RemoteGoodsService remoteGoodsService;

    private final RemoteLoginService remoteLoginService;

    private final RemoteGoodsPortalSettingService remoteGoodsPortalSettingService;

    private final RemoteKltSmsService remoteKltSmsService;

    @Override
    public GiftOrderResVo createOrder(GiftOrderReqVo reqVo) throws Exception {
        //GDA渠道的限制在2020.8.31后不可以下单
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date endDate = df.parse("2020-12-31");
        String new_time = DateUtil.format(new Date(), "yyyy-MM-dd");
        Date newDate = df.parse(new_time);
        if(reqVo.getAcChannel() == "GDA" && newDate.getTime() > endDate.getTime()){
            throw new Exception("项目合作截止时间到：2020-12-31，如有疑问请及时联系对应商务!");
        }
        // 1 校验参数
        this.validateParam(reqVo);
        // 定义返回结果
        GiftOrderResVo resultVo = new GiftOrderResVo();
        List<GiftCodeResVo> itemlist = new ArrayList<GiftCodeResVo>();
        // 2 生成主订单信息
        try {
            GiftOrderCapital entity = new GiftOrderCapital();
            entity.setCapitalOrderId(reqVo.getCapitalOrderId());
            GiftOrderCapital capital = giftOrderCapitalMapper.selectOne(entity);
            if (capital == null) {
                capital = new GiftOrderCapital();
                BeanUtils.copyProperties(reqVo, capital);
                capital.setSmsTag(reqVo.getSmsTag() ? 1 : 0);
                capital.setActivateTag(reqVo.getActivateTag() ? 1 : 0);
                capital.setPayTag(reqVo.getPayTag() ? 1 : 0);
                capital.setStatus(reqVo.getPayTag() ? 1 : 2); // 订单状态(1-待支付，2-已支付，3-退款，4-超时关闭订单)
                capital.setCreateTime(new Date());
                giftOrderCapitalMapper.insert(capital);
            }
            resultVo.setCapitalOrderId(capital.getCapitalOrderId());
            resultVo.setStatus(capital.getStatus());
            try {
                // 总下单数
                Integer totalNum = 0;
                Boolean sendFlag = true;
                // 3 生成子订单信息
                for (GiftOrderItemReqVo itemVo : reqVo.getItemlist()) {
                    GiftOrderItem entity1 = new GiftOrderItem();
                    entity1.setCapitalOrderId(reqVo.getCapitalOrderId());
                    entity1.setItemOrderId(itemVo.getItemOrderId());
                    GiftOrderItem gItem = giftOrderItemMapper.selectOne(entity1);
                    if (gItem == null) {
                        gItem = new GiftOrderItem();
                        gItem.setItemOrderId(itemVo.getItemOrderId());
                        gItem.setCapitalOrderId(reqVo.getCapitalOrderId());
                        gItem.setGoodsId(itemVo.getGoodsId());
                        gItem.setGoodsNum(itemVo.getGoodsNum());
                        // 3.1查询商品信息
                        CommonResultVo<GoodsBaseVo> remoteResult = remoteGoodsService.selectById(itemVo.getGoodsId());
                        if (remoteResult.getResult() == null) {
                            throw new Exception("商品不存在");
                        }
                        gItem.setGoodsPrice(remoteResult.getResult().getMarketPrice());
                        gItem.setGoodsSalesPrice(remoteResult.getResult().getSalesPrice());
                        gItem.setGoodsName(remoteResult.getResult().getName());
                        gItem.setCreateTime(new Date());
                        giftOrderItemMapper.insert(gItem);
                    }
                    totalNum += gItem.getGoodsNum();
                    // 查询发码记录
                    Wrapper<GiftOrderItemDetail> wrapper = new Wrapper<GiftOrderItemDetail>() {
                        @Override
                        public String getSqlSegment() {
                            StringBuffer sb = new StringBuffer();
                            sb.append("where item_order_id = '" + itemVo.getItemOrderId() + "'");
                            return sb.toString();
                        }
                    };
                    List<GiftOrderItemDetail> list = giftOrderItemDetailMapper.selectList(wrapper);
                    if (!list.isEmpty()) {
                        if (list.size() != itemVo.getGoodsNum()) {
                            throw new Exception("发货数目和实际发码数量不一致");
                        }
                        sendFlag = false;
                        for (GiftOrderItemDetail itemDetail : list) {
                            GiftCodeResVo resVo = new GiftCodeResVo();
                            resVo.setGiftCode(itemDetail.getGiftCode());
                            resVo.setGoodsId(itemDetail.getGoodsId());
                            resVo.setGiftId(itemDetail.getGiftId());
                            resVo.setItemOrderId(itemDetail.getItemOrderId());
                            resVo.setGiftUrl(itemDetail.getGiftUrl());
                            itemlist.add(resVo);
                        }
                    }
                }
                // 4. 如果状态是已支付 -->发码
                if (capital.getStatus() == 2) {
                    // 是否发码
                    if (sendFlag) {
                        // 查询 是否达到发码上限
                        GiftOrderConfig queryConfig = new GiftOrderConfig();
                        queryConfig.setAcChannel(reqVo.getAcChannel());
                        GiftOrderConfig config = giftOrderConfigMapper.selectOne(queryConfig);
                        // 如果配置不为空
                        if (config != null) {
                            // 如果单次购买数量 超过最大限制
                            if (totalNum > config.getMaxNum()) {
                                log.info("库存不足:购买总数totalNum={}, 该商户最大限制maxNum={}", totalNum, config.getMaxNum());
                                throw new Exception("最多可购买" + config.getMaxNum() + "个商品");
                            }
                            // 查询该渠道发码总数
                            Integer cnt = giftOrderItemDetailMapper.getCountByChannel(reqVo.getAcChannel());
                            if (cnt + totalNum > config.getMaxNum()) {
                                log.error("订单【{}】下单失败，原因【库存不足】", reqVo.getCapitalOrderId());
                                throw new Exception("库存不足");
                            }
                            // 最大库存 - (购买数+已用次数) = 剩余库存 < 预警值 就告警 发邮件
                            if (config.getMaxNum() - (cnt + totalNum) < config.getAlarmNum()) {
                                //TODO 告警 发邮件
                                log.info("渠道【{}】剩余库存已不足【{}】件，请知悉", reqVo.getAcChannel(), config.getMaxNum() - (cnt + totalNum));
                            }
                        }
                        for (GiftOrderItemReqVo itemVo : reqVo.getItemlist()) {
                            // 构造发码入参
                            GiftCodeSendVo send = new GiftCodeSendVo();
                            send.setItemOrderId(itemVo.getItemOrderId());
                            send.setMobile(reqVo.getBuyerMobile());
                            send.setAcChannel(reqVo.getAcChannel());
                            send.setActivateTag(reqVo.getActivateTag());
                            send.setSmsTag(reqVo.getSmsTag());
                            send.setGoodsId(itemVo.getGoodsId());
                            send.setGoodsNum(itemVo.getGoodsNum());
                            CommonResultVo<GoodsPortalSettingDto> commonResultVo = remoteGoodsPortalSettingService
                                    .get(itemVo.getGoodsId());
                            if (null != commonResultVo && null != commonResultVo.getResult()) {
                                send.setGiftUrl(commonResultVo.getResult().getShortUrl());
                            }
                            // 调用发码接口
                            itemlist.addAll(this.sendCode(send));
                        }
                    }
                }
                // 封装
                resultVo.setItemlist(itemlist);
            } catch (Exception e) {
                log.error("生成子订单失败", e);
                throw new Exception(e.getMessage());
            }
        } catch (Exception e) {
            log.error("生成主订单失败", e);
            throw new Exception(e.getMessage());
        }

        return resultVo;
    }

    private void validateParam(GiftOrderReqVo reqVo) throws Exception {
        log.info("入参reqVo={}", JSON.toJSONString(reqVo));
        if (reqVo == null) {
            throw new Exception("参数为空");
        }
        if (reqVo.getSign() == null) {
            throw new Exception("验签不能为空");
        }
        // 解密验签
        String encrypt = EncryptAES.decrypt(reqVo.getSign(), marsApiConfig.getEncryptKey());
        if (encrypt == null) {
            throw new Exception("验签失败");
        }
        if (reqVo.getCapitalOrderId() == null) {
            throw new Exception("主订单ID为空");
        }
        if (reqVo.getAcChannel() == null) {
            throw new Exception("渠道ID为空");
        }
        if (reqVo.getItemlist().isEmpty()) {
            throw new Exception("子订单信息为空");
        }
        for (GiftOrderItemReqVo item : reqVo.getItemlist()) {
            if (item.getGoodsId() == null) {
                throw new Exception("商品ID为空");
            }
            if (item.getItemOrderId() == null) {
                throw new Exception("子订单为空");
            }
            if (item.getGoodsNum() == null) {
                throw new Exception("商品购买数量为空");
            }
        }
        if (reqVo.getSmsTag() == null) {
            throw new Exception("短信标识为空");
        }
        if (reqVo.getActivateTag() == null) {
            throw new Exception("激活标识为空");
        }
        if (reqVo.getActivateTag()) {
            if (reqVo.getBuyerMobile() == null) {
                throw new Exception("购买人手机号为空");
            }
        }
        if (reqVo.getPayTag() == null) {
            throw new Exception("支付标识为空");
        }
        if (reqVo.getPayTag()) {
            if (reqVo.getOrderAmount() == null) {
                throw new Exception("订单支付金额为空");
            }
        }
    }

    @Override
    public GiftOrderResVo updateOrder(GiftOrderPayReqVo reqVo) throws Exception {
        // 1 校验参数
        if (reqVo == null) {
            throw new Exception("参数为空");
        }
        if (reqVo.getCapitalOrderId() == null) {
            throw new Exception("主订单ID为空");
        }
//        if (reqVo.getPayNumber() == null) {
//            throw new Exception("支付单号为空");
//        }
//        if (reqVo.getPayTime() == null) {
//            throw new Exception("支付完成时间为空");
//        }
        // 定义返回结果
        GiftOrderResVo resultVo = new GiftOrderResVo();
        List<GiftCodeResVo> itemlist = new ArrayList<GiftCodeResVo>();
        // 2.更新订单
        try {
            GiftOrderCapital query = new GiftOrderCapital();
            query.setCapitalOrderId(reqVo.getCapitalOrderId());
            GiftOrderCapital res = giftOrderCapitalMapper.selectOne(query);
            if (res == null) {
                throw new Exception("订单不存在");
            }
            // 订单状态(1-待支付，2-已支付，3-退款，4-超时关闭订单)
            if (res.getStatus() != 1) {
                log.info("订单状态status={}", res.getStatus());
                throw new Exception("订单状态异常");
            }
            BeanUtils.copyProperties(reqVo, res);
            res.setStatus(2);
            giftOrderCapitalMapper.updateById(res);
            // 3.根据子订单发码
            Wrapper<GiftOrderItem> wrapper = new Wrapper<GiftOrderItem>() {
                @Override
                public String getSqlSegment() {
                    StringBuffer sb = new StringBuffer();
                    sb.append("where capital_order_id = '" + reqVo.getCapitalOrderId() + "'");
                    return sb.toString();
                }
            };
            List<GiftOrderItem> list = giftOrderItemMapper.selectList(wrapper);
            if (list.isEmpty()) {
                throw new Exception("子订单【" + reqVo.getCapitalOrderId() + "】不存在");
            }
            for (GiftOrderItem itemVo : list) {
                GiftCodeSendVo send = new GiftCodeSendVo();
                send.setItemOrderId(itemVo.getItemOrderId());
                send.setMobile(res.getBuyerMobile());
                send.setAcChannel(res.getAcChannel());
                send.setActivateTag(res.getActivateTag() == 1 ? true : false);
                send.setSmsTag(res.getSmsTag() == 1 ? true : false);
                send.setGoodsId(itemVo.getGoodsId());
                send.setGoodsNum(itemVo.getGoodsNum());
                CommonResultVo<GoodsPortalSettingDto> commonResultVo = remoteGoodsPortalSettingService
                        .get(itemVo.getGoodsId());
                if (null != commonResultVo && null != commonResultVo.getResult()) {
                    send.setGiftUrl(commonResultVo.getResult().getShortUrl());
                }
                // 调用发码接口
                itemlist.addAll(this.sendCode(send));
            }
            resultVo.setItemlist(itemlist);
            resultVo.setCapitalOrderId(res.getCapitalOrderId());
            resultVo.setStatus(res.getStatus());
        } catch (Exception e) {
            log.error("订单更新异常", e);
            throw new Exception("订单更新异常:" + e.getMessage());
        }

        return resultVo;
    }

    /**
     * @throws
     * @Title: noticeOrder
     * @Description: 异步通知商户
     * @author: nickal.zhang
     * @date: 2019年8月21日
     * @param: capitalOrderId
     * @return: GiftOrderResVo
     */
    @Override
    public GiftOrderResVo noticeOrder(GiftOrderReqVo reqVo) throws Exception {
        // 解密验签
        if (reqVo == null) {
            throw new Exception("参数为空");
        }
        if (reqVo.getSign() == null) {
            throw new Exception("验签不能为空");
        }
        // 解密验签
        String encrypt = EncryptAES.decrypt(reqVo.getSign(), marsApiConfig.getEncryptKey());
        if (encrypt == null) {
            throw new Exception("验签失败");
        }
        //定义返回结果
        GiftOrderResVo vo = new GiftOrderResVo();
        List<GiftCodeResVo> itemlist = new ArrayList<GiftCodeResVo>();
        GiftOrderCapital result = giftOrderItemDetailMapper.getNoticeOrder(reqVo.getCapitalOrderId());
        if (result == null) {
            throw new Exception("查询主订单号失败");
        } else if (result.getCapitalOrderId().length() == 0) {
            throw new Exception("此订单号没有记录");
        }

        // 3.根据子订单发码
        Wrapper<GiftOrderItem> wrapper = new Wrapper<GiftOrderItem>() {
            @Override
            public String getSqlSegment() {
                StringBuffer sb = new StringBuffer();
                sb.append("where capital_order_id = '" + reqVo.getCapitalOrderId() + "'");
                return sb.toString();
            }
        };
        List<GiftOrderItem> list = giftOrderItemMapper.selectList(wrapper);
        if (list.isEmpty()) {
            throw new Exception("子订单【" + reqVo.getCapitalOrderId() + "】不存在");
        }
        for (GiftOrderItem itemVo : list) {
            // 查询发码记录
            Wrapper<GiftOrderItemDetail> detailWrapper = new Wrapper<GiftOrderItemDetail>() {
                @Override
                public String getSqlSegment() {
                    StringBuffer sb = new StringBuffer();
                    sb.append("where item_order_id = '" + itemVo.getItemOrderId() + "'");
                    return sb.toString();
                }
            };
            List<GiftOrderItemDetail> detailList = giftOrderItemDetailMapper.selectList(detailWrapper);
            if (!detailList.isEmpty()) {
                for (GiftOrderItemDetail itemDetail : detailList) {
                    GiftCodeResVo resVo = new GiftCodeResVo();
                    resVo.setGiftCode(itemDetail.getGiftCode());
                    resVo.setGoodsId(itemDetail.getGoodsId());
                    resVo.setGiftId(itemDetail.getGiftId());
                    resVo.setItemOrderId(itemDetail.getItemOrderId());
                    resVo.setGiftUrl(itemDetail.getGiftUrl());
                    itemlist.add(resVo);
                }
            }
            vo.setCapitalOrderId(result.getCapitalOrderId());
            vo.setStatus(result.getStatus());
            vo.setItemlist(itemlist);
        }

        return vo;
    }


    /**
     * @throws
     * @Title: sendCode
     * @Description: 发码
     * @author: sunny.wang
     * @date: 2019年7月29日 下午4:29:05
     * @param: sendVo
     * @return: List<GiftCodeResVo>
     */
    private List<GiftCodeResVo> sendCode(GiftCodeSendVo sendVo) throws Exception {
        // 定义返回结果
        List<GiftCodeResVo> resultList = new ArrayList<GiftCodeResVo>();
        // 查询 是否达到发码上限
        GiftOrderConfigPortal queryConfig = new GiftOrderConfigPortal();
        queryConfig.setAcChannel(sendVo.getAcChannel());
        queryConfig.setGoodsId(sendVo.getGoodsId());
        GiftOrderConfigPortal configPortal = giftOrderConfigPortalMapper.selectOne(queryConfig);
        // 如果配置不为空
        if (configPortal != null) {
            // 如果单次购买数量 超过最大限制
            if (sendVo.getGoodsNum() > configPortal.getMaxNum()) {
                log.info("商品【{}】,购买数量【{}】超出最大限制【{}】", sendVo.getGoodsId(), sendVo.getGoodsNum(), configPortal.getMaxNum());
                throw new Exception("商品" + sendVo.getGoodsId() + "发货失败:原因【库存不足】");
            }
            // 查询该渠道发码总数
            Integer cnt = giftOrderItemDetailMapper.getCountByGoodsId(sendVo.getGoodsId());
            if (cnt + sendVo.getGoodsNum() > configPortal.getMaxNum()) {
                log.info("商品【{}】发货失败:剩余库存【{}】件", sendVo.getGoodsId(), configPortal.getMaxNum() - cnt);
                throw new Exception("商品" + sendVo.getGoodsId() + "发货失败:原因【库存不足】");
            }
            // 最大库存 - (购买数+已用次数) = 剩余库存 < 预警值 就告警 发邮件
            if (configPortal.getMaxNum() - (cnt + sendVo.getGoodsNum()) < configPortal.getAlarmNum()) {
                //TODO 告警 发邮件
                log.info("商品【{}】剩余库存不足【{}】件，请知悉", sendVo.getGoodsId(), configPortal.getMaxNum() - (cnt + sendVo.getGoodsNum()));
            }
        }
        //获取销售渠道
        List<GoodsChannelRes> goodsChannelResList = panguInterfaceService.selectGoodsChannel(sendVo.getGoodsId());
        Assert.notEmpty(goodsChannelResList,"商品的销售渠道不能为空");
        // 第1步，生成激活码
        ActCodeReq actCodeReq = new ActCodeReq();
        actCodeReq.setGoodsId(sendVo.getGoodsId());
        actCodeReq.setActCodeNum(sendVo.getGoodsNum());
        List<GiftCode> codeList = giftCodeService.generateActCode(actCodeReq);
        // 第2步，出库激活码
        OutCodeReq outCodeReq = new OutCodeReq();
        BeanUtils.copyProperties(actCodeReq, outCodeReq);
        outCodeReq.setCodes(codeList.stream().map(giftCode -> giftCode.getActCode()).collect(Collectors.toList()));
        outCodeReq.setSalesChannelId(goodsChannelResList.get(0).getId());
        List<GiftCode> giftCodeList = giftCodeService.outActCodeByCodes(outCodeReq, null);
        if (!giftCodeList.isEmpty()) {
            CommonResultVo<MemMemberAccount> comMem =  null;
            if (sendVo.getMobile() != null && sendVo.getAcChannel() != null) {
                // 第3.1步 生成会员信息
                MemLoginReqDTO reqDto = new MemLoginReqDTO();
                reqDto.setMobile(sendVo.getMobile());
                reqDto.setAcChannel(sendVo.getAcChannel().toUpperCase());
                comMem = remoteLoginService.register(reqDto);
                if (comMem.getCode() == 200 || comMem.getResult() == null) {
                    throw new Exception("会员注册失败:" + comMem.getMsg());
                }
            }
            // 第3.2步 遍历激活码
            for (GiftCode actCode : giftCodeList) {
                // 第3.3步 生成激活码记录
                GiftOrderItemDetail itemDetail = new GiftOrderItemDetail();
                itemDetail.setItemOrderId(sendVo.getItemOrderId());
                itemDetail.setGoodsId(actCode.getGoodsId());
                itemDetail.setGiftId(actCode.getId());
                itemDetail.setGiftUrl(sendVo.getGiftUrl());
                itemDetail.setGiftCode(actCode.getActCode());
                itemDetail.setCreateTime(new Date());
                giftOrderItemDetailMapper.insert(itemDetail);
                // 第3.4步 封装返回结果
                GiftCodeResVo resVo = new GiftCodeResVo();
                resVo.setGiftCode(actCode.getActCode());
                resVo.setGoodsId(sendVo.getGoodsId());
                resVo.setGiftId(actCode.getId());
                resVo.setItemOrderId(sendVo.getItemOrderId());
                resVo.setGiftUrl(sendVo.getGiftUrl());
                resultList.add(resVo);
                // 第4步，直接激活
                if (sendVo.getActivateTag() && sendVo.getMobile() != null) {
                    if (actCode.getActCodeStatus().compareTo(ActCodeStatusEnum.ActCodeStatus.OUT.getIndex()) == 0) {
                        // 过期
                        Date endTime = actCode.getActEndTime() == null? null: DateUtil.parse(DateUtil.format(actCode.getActEndTime(), "yyyy-MM-dd"),
                                "yyyy-MM-dd");
                        Date nowTime = DateUtil.parse(DateUtil.format(new Date(), "yyyy-MM-dd"), "yyyy-MM-dd");
                        if (endTime == null || endTime.compareTo(nowTime) != -1) {
                            ActiveActCodeReq activeReq = new ActiveActCodeReq();
                            activeReq.setActCode(actCode.getActCode());
                            activeReq.setMemberId(comMem.getResult().getAcid());
                            // 第3.3步 激活
                            giftCodeService.activeActCode(activeReq);
                        } else {
                            throw new Exception("激活码无效");
                        }
                    } else {
                        throw new Exception("激活码状态不可用");
                    }
                }
                if(sendVo.smsTag){
                    this.sendSms(sendVo.getMobile(), sendVo.getGoodsId());
                }
            }
        }
        return resultList;
    }

    @Override
    public GiftOrderInfoResVo getOrder(String orderId) throws Exception {
        // 解密验签
        if (orderId == null) {
            throw new Exception("参数为空");
        }
        //定义返回结果
        GiftOrderInfoResVo vo = new GiftOrderInfoResVo();
        List<GiftCodeResVo> itemlist = new ArrayList<GiftCodeResVo>();
        GiftOrderCapital result = giftOrderItemDetailMapper.getNoticeOrder(orderId);
        if (result == null) {
            throw new Exception("查询主订单号失败");
        } else if (result.getCapitalOrderId().length() == 0) {
            throw new Exception("此订单号没有记录");
        }

        // 3.根据子订单发码
        Wrapper<GiftOrderItem> wrapper = new Wrapper<GiftOrderItem>() {
            @Override
            public String getSqlSegment() {
                StringBuffer sb = new StringBuffer();
                sb.append("where capital_order_id = '" + orderId + "'");
                return sb.toString();
            }
        };
        List<GiftOrderItem> list = giftOrderItemMapper.selectList(wrapper);
        if (list.isEmpty()) {
            throw new Exception("子订单【" + orderId + "】不存在");
        }
        for (GiftOrderItem itemVo : list) {
            // 查询发码记录
            Wrapper<GiftOrderItemDetail> detailWrapper = new Wrapper<GiftOrderItemDetail>() {
                @Override
                public String getSqlSegment() {
                    StringBuffer sb = new StringBuffer();
                    sb.append("where item_order_id = '" + itemVo.getItemOrderId() + "'");
                    return sb.toString();
                }
            };
            List<GiftOrderItemDetail> detailList = giftOrderItemDetailMapper.selectList(detailWrapper);
            if (!detailList.isEmpty()) {
                for (GiftOrderItemDetail itemDetail : detailList) {
                    GiftCodeResVo resVo = new GiftCodeResVo();
                    resVo.setGiftCode(itemDetail.getGiftCode());
                    resVo.setGoodsId(itemDetail.getGoodsId());
                    resVo.setGiftId(itemDetail.getGiftId());
                    resVo.setItemOrderId(itemDetail.getItemOrderId());
                    resVo.setGiftUrl(itemDetail.getGiftUrl());
                    itemlist.add(resVo);
                }
            }
            vo.setGiftOrderCapital(result);
            vo.setItemlist(itemlist);
        }

        return vo;
    }

    @Override
    public Integer refundOrder(String orderId) throws Exception {
        if (orderId == null) {
            throw new Exception("参数为空");
        }
        GiftOrderCapital query = new GiftOrderCapital();
        query.setCapitalOrderId(orderId);
        GiftOrderCapital res = giftOrderCapitalMapper.selectOne(query);
        if (res == null) {
            throw new Exception("订单不存在");
        }
        // 订单状态(1-待支付，2-已支付，3-退款，4-超时关闭订单)
        if (res.getStatus() != 2) {
            throw new Exception("订单状态异常");
        }
        res.setStatus(3);
        return giftOrderCapitalMapper.updateById(res);
    }

    @Override
    public GiftOrderInfoResVo getOrderDetail(GiftOrderQueryReqVo reqVo) throws Exception {
        // 解密验签
        if (reqVo == null) {
            throw new Exception("参数为空");
        }
        //定义返回结果
        GiftOrderInfoResVo vo = new GiftOrderInfoResVo();
        // 根据手机号 渠道  商品ID 查询
        GiftOrderDetailVo capital = giftOrderItemDetailMapper.getOrderDetail(reqVo);
        if (capital != null) {
            List<GiftCodeResVo> itemlist = new ArrayList<GiftCodeResVo>();
            // 查询发码记录
            Wrapper<GiftOrderItemDetail> detailWrapper = new Wrapper<GiftOrderItemDetail>() {
                @Override
                public String getSqlSegment() {
                    StringBuffer sb = new StringBuffer();
                    sb.append("where item_order_id = '" + capital.getItemOrderId() + "'");
                    return sb.toString();
                }
            };
            List<GiftOrderItemDetail> detailList = giftOrderItemDetailMapper.selectList(detailWrapper);
            if (!detailList.isEmpty()) {
                for (GiftOrderItemDetail itemDetail : detailList) {
                    GiftCodeResVo resVo = new GiftCodeResVo();
                    resVo.setGiftCode(itemDetail.getGiftCode());
                    resVo.setGoodsId(itemDetail.getGoodsId());
                    resVo.setGiftId(itemDetail.getGiftId());
                    resVo.setItemOrderId(itemDetail.getItemOrderId());
                    resVo.setGiftUrl(itemDetail.getGiftUrl());
                    itemlist.add(resVo);
                }
            }
            vo.setGiftOrderCapital(capital);
            vo.setItemlist(itemlist);
        }
        return vo;
    }

    @Override
    public void updateOrderId(GiftOrderIdReqVo reqVo) throws Exception {
        // 根据旧订单ID 查询订单信息
        GiftOrderCapital entity = new GiftOrderCapital();
        entity.setCapitalOrderId(reqVo.getOldCapitalOrderId());
        GiftOrderCapital capital = giftOrderCapitalMapper.selectOne(entity);
        capital.setCapitalOrderId(reqVo.getNewCapitalOrderId());
        capital.setUpdateTime(new Date());
        giftOrderCapitalMapper.updateById(capital);
        // 查询子订单信息
        Wrapper<GiftOrderItem> wrapper = new Wrapper<GiftOrderItem>() {
            @Override
            public String getSqlSegment() {
                StringBuffer sb = new StringBuffer();
                sb.append("where capital_order_id = '" + reqVo.getOldCapitalOrderId() + "'");
                return sb.toString();
            }
        };
        List<GiftOrderItem> list = giftOrderItemMapper.selectList(wrapper);
        for (GiftOrderItem giftOrderItem : list){
            giftOrderItem.setCapitalOrderId(reqVo.getNewCapitalOrderId());
            giftOrderItem.setUpdateTime(new Date());
            giftOrderItemMapper.updateById(giftOrderItem);
        }
    }

    @Override
    public String getOrderInfoByGiftCode(Integer actCodeId) throws Exception {
        if(actCodeId==null){
            throw new Exception("激活码为空");
        }
        // 查询子订单信息
        GiftOrderItemDetail reqDetail = new GiftOrderItemDetail();
        reqDetail.setGiftId(actCodeId);
        GiftOrderItemDetail resDetail = giftOrderItemDetailMapper.selectOne(reqDetail);
        if(resDetail==null){
            throw new Exception("激活码不存在");
        }
        // 根据子订单查询主订单信息
        GiftOrderItem reqItem = new GiftOrderItem();
        reqItem.setItemOrderId(resDetail.getItemOrderId());
        GiftOrderItem resItem = giftOrderItemMapper.selectOne(reqItem);
        if(resItem==null){
            throw new Exception("激活码不存在");
        }
        return resItem.getCapitalOrderId();
    }

    /**
     * 发短信接口
     * @param phone
     */
    private void sendSms(String phone, Integer goodsId) {
        String content = "";
        // 银联饭圈
        if (goodsId==10534) {
            content = "尊敬的银联持卡人****" + phone.substring(phone.length()-4) + "，恭喜您在银联“Fun圈联盟”活动中完成权益兑换，并成功领取“莫逆之交““高端酒店自助餐双人礼遇”产品。请于2020年11月30日前，在银联权益平台进行预约使用，短信勿回。客服热线4006368008";
            KltSendSmsMsgReq kltSendSmsMsgReq = new KltSendSmsMsgReq();
            kltSendSmsMsgReq.setPhone(phone);
            kltSendSmsMsgReq.setContent(content);
            final CommonResultVo<SmsSendResult> resultVo = remoteKltSmsService.sendMsg(kltSendSmsMsgReq);
            if (resultVo.getCode() != 100) {
                log.error("手机号：{}, 短信发送失败:{}", phone, resultVo.getMsg());
            }
        } else {
            // TODO
        }
    }

}
