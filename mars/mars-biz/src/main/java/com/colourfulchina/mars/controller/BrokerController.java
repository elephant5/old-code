package com.colourfulchina.mars.controller;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.colourfulchina.god.door.api.annotation.SysGodDoorLog;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.mars.api.entity.GiftCode;
import com.colourfulchina.mars.api.entity.ReservOrder;
import com.colourfulchina.mars.api.vo.req.BrokerCouponsUsedReq;
import com.colourfulchina.mars.config.MqscProperties;
import com.colourfulchina.mars.service.BrokerService;
import com.colourfulchina.mars.service.GiftCodeService;
import com.colourfulchina.mars.service.ReservOrderService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Slf4j
@RestController
@RequestMapping("/broker")
public class BrokerController {

    @Autowired
    private BrokerService brokerService;

    @Autowired
    private MqscProperties mqscProperties;

    @Autowired
    GiftCodeService giftCodeService;
    @Autowired
    ReservOrderService reservOrderService;

    @SysGodDoorLog("码券商城文件生成")
    @ApiOperation("码券商城文件生成")
    @PostMapping(value = "/batchUpload")
    public CommonResultVo<String> batchUpload(){
        log.info("-----------start---------------");
        String merchantId = "MKLFG00001";
        brokerService.batchUploadData(merchantId);

        CommonResultVo<String> resultVo = new CommonResultVo<>();
        resultVo.setCode(200);
        resultVo.setMsg("Success");
        log.info("-----------end---------------");
        return resultVo;
    }
    @SysGodDoorLog("码券商城文件生成")
    @ApiOperation("码券商城文件生成")
        @GetMapping(value = "/synchroPush/{reservOderId}")
    public CommonResultVo synchroPush(@PathVariable("reservOderId") Integer reservOderId){
        CommonResultVo<String> resultVo = new CommonResultVo<>();
        try {
            if (reservOderId == null) {
                throw new Exception("订单ID为空");
            }
            // 1.查询订单信息
            ReservOrder reservOrder = reservOrderService.getReservOrderById(reservOderId);
            if (reservOrder == null) {
                throw new Exception("预约单不存在");
            }
            //中行项目10280订单
            if(reservOrder.getGoodsId().compareTo(mqscProperties.getGoodsId()) == 0){
                BrokerCouponsUsedReq couponsUsedReq = new BrokerCouponsUsedReq();
                Integer giftCodeId = reservOrder.getGiftCodeId();
                GiftCode giftCode = giftCodeService.selectGiftCodeInfo(giftCodeId);
                //商品id,不是预约单中的商品id值，要改动
                couponsUsedReq.setWaresId(giftCode.getRemarks());
                //库存编码, 优惠券id + 00， gift_code
                couponsUsedReq.setWEid(giftCodeId + "00");
                // 优惠券表示你
                couponsUsedReq.setWSign("E");
                //优惠券串码
                couponsUsedReq.setWInfo(giftCode.getActCode());
                //使用日期时间，可以省去
                Date actCodeTime = giftCode.getActCodeTime();

                if(actCodeTime != null){
                    String format = DateUtil.format(actCodeTime, "yyyyMMdd HH:mm:ss");
                    String[] s = format.split(" ");
                    couponsUsedReq.setUsedDate(s[0]);
                    couponsUsedReq.setUsedTime(s[1]);
                }

                Boolean aBoolean = brokerService.couponUsed(couponsUsedReq);
                if(aBoolean){
                    resultVo.setMsg("中行优惠券使用更新成功");
                    log.info("中行优惠券使用更新成功,{}",reservOrder.getId());
                }else{
                    resultVo.setMsg("中行优惠券使用更新失败");
                    log.error("中行优惠券使用更新失败，{}", JSON.toJSONString(reservOrder));
                }
            }else{
                resultVo.setMsg("商品ID不正确，正确的为："+mqscProperties.getGoodsId());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            resultVo.setCode(200);
            resultVo.setMsg(e.getMessage());
        }
        return resultVo;

    }

}
