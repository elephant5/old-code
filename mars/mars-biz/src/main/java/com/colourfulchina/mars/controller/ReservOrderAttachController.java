package com.colourfulchina.mars.controller;

import com.alibaba.fastjson.JSON;
import com.colourfulchina.god.door.api.annotation.SysGodDoorLog;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.mars.api.enums.PayOrderStatusEnum;
import com.colourfulchina.mars.api.enums.ReservOrderStatusEnums;
import com.colourfulchina.mars.api.vo.ReservOrderVo;
import com.colourfulchina.mars.api.vo.req.GoodsSettingReq;
import com.colourfulchina.mars.api.vo.req.MemberCertificateReq;
import com.colourfulchina.mars.api.vo.req.NewReservOrderPlaceReq;
import com.colourfulchina.mars.api.vo.req.ReservOrderPlaceReq;
import com.colourfulchina.mars.api.vo.res.ReservOrderResVO;
import com.colourfulchina.mars.constants.RedisKeys;
import com.colourfulchina.mars.service.GiftCodeService;
import com.colourfulchina.mars.service.ReservOrderAttachService;
import com.colourfulchina.mars.service.ReservOrderService;
import com.colourfulchina.mars.service.SynchroPushService;
import com.colourfulchina.member.api.entity.MemMemberInfo;
import com.colourfulchina.member.api.entity.MemberCertificate;
import com.colourfulchina.member.api.feign.RemoteMemberInfoServcie;
import com.colourfulchina.member.api.res.MemLoginResDTO;
import com.colourfulchina.pangu.taishang.api.entity.ShopProtocol;
import com.colourfulchina.pangu.taishang.api.enums.ResourceTypeEnums;
import com.colourfulchina.pangu.taishang.api.feign.RemoteShopService;
import com.colourfulchina.pangu.taishang.api.vo.res.QueryBookBlockRes;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: luck.luo
 * @Description:
 * @Date: 2019/5/28 9:41
 */
@Slf4j
@RestController
@RequestMapping("/reservOrderAttach")
@AllArgsConstructor
@Api(value = "预约订单附加操作相关Controller", tags = {"预约订单附加操作相关Controller"})
public class ReservOrderAttachController extends BaseController{
    @Autowired
    private ReservOrderAttachService reservOrderAttachService;

    @Autowired
    private RemoteMemberInfoServcie remoteMemberInfoServcie;

    @Autowired
    ReservOrderService reservOrderService;

    private final RemoteShopService remoteShopService;

    @Autowired
    private SynchroPushService synchroPushService;
    @Autowired
    private GiftCodeService giftCodeService;

    @SysGodDoorLog("预约订单获取block")
    @PostMapping("getBlockRule")
    public CommonResultVo<QueryBookBlockRes> getBlockRule(@RequestBody Map<String,Integer> param){
        CommonResultVo<QueryBookBlockRes> result = new CommonResultVo<>();
        try {
            QueryBookBlockRes res = reservOrderAttachService.getBlockRule(param);
            result.setResult(res);
        }catch (Exception e){
            log.error("block查询失败",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    //在线下预约单
    @SysGodDoorLog("在线下预约单")
    @PostMapping("placeOrder")
    @Transactional(rollbackFor=Exception.class)
    public CommonResultVo<ReservOrderResVO> placeOrder(@RequestBody ReservOrderPlaceReq reservOrderPlaceReq){
        CommonResultVo<ReservOrderResVO> result = new CommonResultVo<>();
        final Integer giftCodeId = reservOrderPlaceReq.getGiftCodeId();
        try {
            log.info("placeOrder reservOrderPlaceReq:{}",JSON.toJSONString(reservOrderPlaceReq));
            MemLoginResDTO memInfo = getLoginUser();
            final Boolean hasKey = redisTemplate.hasKey(RedisKeys.MARS_INSERTRESERVORDER_GIFT_CODE + giftCodeId);
            Assert.isTrue(!hasKey, giftCodeId+"当前激活码正在处理中，请勿重复提交！");
            ReservOrderResVO order =  reservOrderAttachService.placeOrder(reservOrderPlaceReq,memInfo);

            log.info("placeOrder order:{}",JSON.toJSONString(order));

            if(order.getProseStatus().equals(ReservOrderStatusEnums.ReservOrderStatus.none.getcode()) && order.getPayStatus().compareTo(PayOrderStatusEnum.PREPAID.getCode()) == 0){
                if(ResourceTypeEnums.DRINK.getCode().equalsIgnoreCase(order.getServiceType()) || (order.getServiceType().indexOf("_cpn") != -1 && (null == reservOrderPlaceReq.getPayAmoney() || reservOrderPlaceReq.getPayAmoney().compareTo(new BigDecimal(0)) == 0))){
                    ReservOrderVo reservOrderVo = new ReservOrderVo();
                    reservOrderVo.setId(order.getId());
                    reservOrderVo.setShopChannelId(reservOrderPlaceReq.getShopChannelId());
                    reservOrderVo.setPayAmoney(reservOrderPlaceReq.getPayAmoney());
                    reservOrderService.reservSuccess(reservOrderVo);
                }
            }
            result.setResult(order);
            // === 第三方订单推送埋点 开始 ===
            try {
                synchroPushService.synchroPush(order.getId());
            } catch (Exception e) {
                log.error("第三方订单推送异常", e);
            }
            // === 第三方订单推送埋点 结束 ===
        }catch (Exception e){
            log.error("placeOrder插入失败:{}", JSON.toJSONString(reservOrderPlaceReq),e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }finally {
            //删除缓存
            giftCodeService.deleteRedis(giftCodeId);
        }
        return result;
    }


    //在线下预约单
    @SysGodDoorLog("对外在线下预约单")
    @PostMapping("openPlaceOrder")
    @Transactional(rollbackFor=Exception.class)
    public CommonResultVo<ReservOrderResVO> openPlaceOrder(@RequestBody NewReservOrderPlaceReq newreservOrderPlaceReq){
        CommonResultVo<ReservOrderResVO> result = new CommonResultVo<>();
        ReservOrderPlaceReq reservOrderPlaceReq = new ReservOrderPlaceReq();
        try {
            log.info("openPlaceOrder reservOrderPlaceReq:{}",JSON.toJSONString(newreservOrderPlaceReq));
            MemLoginResDTO memInfo = new MemLoginResDTO();
            memInfo.setAcid(newreservOrderPlaceReq.getAcId());
            memInfo.setMbName(newreservOrderPlaceReq.getBookName());
            memInfo.setMobile(newreservOrderPlaceReq.getBookPhone());

            BeanUtils.copyProperties(newreservOrderPlaceReq,reservOrderPlaceReq);
            reservOrderPlaceReq.setEquitySubNum(1);
            reservOrderPlaceReq.setExchangeNum(1);
//            reservOrderPlaceReq.setBookName(newreservOrderPlaceReq.getBookName());
//            reservOrderPlaceReq.setBookPhone(newreservOrderPlaceReq.getBookPhone());
//            reservOrderPlaceReq.setGoodsId(newreservOrderPlaceReq.getGoodsId());
//            reservOrderPlaceReq.setProductId(newreservOrderPlaceReq.getProductId());
//            reservOrderPlaceReq.setProductGroupProductId(newreservOrderPlaceReq.getProductGroupProductId());
//            reservOrderPlaceReq.setServiceType(newreservOrderPlaceReq.getServiceType());
//            reservOrderPlaceReq.setGiftCodeId(newreservOrderPlaceReq.getGiftCodeId());
//            reservOrderPlaceReq.setProductGroupId(newreservOrderPlaceReq.getProductGroupId());
//            reservOrderPlaceReq.setServiceType(newreservOrderPlaceReq.getServiceType());
//            reservOrderPlaceReq.setShopId(newreservOrderPlaceReq.getShopId());
//            reservOrderPlaceReq.setShopItemId(newreservOrderPlaceReq.getShopItemId());
//            reservOrderPlaceReq.setGiftType(newreservOrderPlaceReq.getGiftType());
//            reservOrderPlaceReq.setShopChannelId(newreservOrderPlaceReq.getShopChannelId());
            reservOrderPlaceReq.setCreateTime(new Date());
            CommonResultVo<ShopProtocol> remoteShopProtocol = remoteShopService.findOneShopProtocol(reservOrderPlaceReq.getShopId());
            if (remoteShopProtocol != null && remoteShopProtocol.getCode() == 100 && remoteShopProtocol.getResult() != null){
                reservOrderPlaceReq.setShopChannelId(remoteShopProtocol.getRecords().getChannelId());
            }
            ReservOrderResVO order =  reservOrderAttachService.placeOrder(reservOrderPlaceReq,memInfo);

            log.info("placeOrder order:{}",JSON.toJSONString(order));

            if(order.getProseStatus().equals(ReservOrderStatusEnums.ReservOrderStatus.none.getcode()) && order.getPayStatus().compareTo(PayOrderStatusEnum.PREPAID.getCode()) == 0){
                if(ResourceTypeEnums.DRINK.getCode().equalsIgnoreCase(order.getServiceType()) || (order.getServiceType().indexOf("_cpn") != -1 && (null == reservOrderPlaceReq.getPayAmoney() || reservOrderPlaceReq.getPayAmoney().compareTo(new BigDecimal(0)) == 0))){
                    ReservOrderVo reservOrderVo = new ReservOrderVo();
                    reservOrderVo.setId(order.getId());
                    reservOrderVo.setShopChannelId(reservOrderPlaceReq.getShopChannelId());
                    reservOrderVo.setPayAmoney(reservOrderPlaceReq.getPayAmoney());
                    reservOrderService.reservSuccess(reservOrderVo);
                }
            }
            result.setResult(order);
        }catch (Exception e){
            log.error("placeOrder插入失败:{}", JSON.toJSONString(reservOrderPlaceReq),e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    //根据商品信息 获取商品使用限制和激活码信息
    @SysGodDoorLog("根据商品信息 获取商品使用限制和激活码信息")
    @PostMapping("/getGoodsSetting")
    public CommonResultVo<Map> getGoodsSetting(@RequestBody GoodsSettingReq req){
        CommonResultVo<Map> result = new CommonResultVo<>();
        try {
            Map<String,Object> setting =  reservOrderAttachService.getGoodsSetting(req);
            result.setResult(setting);
        }catch (Exception e){
            log.error("根据商品id获取商品使用限制失败",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    //根据mbid获取用户证件信息
    @SysGodDoorLog("根据mbid获取用户证件信息")
    @PostMapping("/getMemberCertificateInfo")
    public CommonResultVo<Map> getMemberCertificateInfo(@RequestBody MemberCertificateReq req){
        CommonResultVo<Map> result = new CommonResultVo<>();
        try {
            MemMemberInfo memMemberInfo = new MemMemberInfo();
            memMemberInfo.setMbid(req.getMbid());
            CommonResultVo<List<MemberCertificate>> resultVo = remoteMemberInfoServcie.getMemberCertificateInfo(memMemberInfo);
            Map<String,String> map = new HashMap<>();
            if(null != resultVo && null != resultVo.getResult()){
                map = resultVo.getResult().stream().collect(Collectors.toMap(MemberCertificate::getCertificateType,MemberCertificate::getCertificateNumber));
            }
            result.setResult(map);
        }catch (Exception e){
            log.error("根据mbid获取用户证件信息",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }
}
