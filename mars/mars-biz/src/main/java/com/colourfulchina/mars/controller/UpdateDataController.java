package com.colourfulchina.mars.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.colourfulchina.colourfulCoupon.api.entity.CpnThirdCode;
import com.colourfulchina.god.door.api.annotation.SysGodDoorLog;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.mars.api.entity.EquityCodeDetail;
import com.colourfulchina.mars.api.entity.ReservOrder;
import com.colourfulchina.mars.api.vo.ReservOrderVo;
import com.colourfulchina.mars.api.vo.req.CouponThirdCodeReqVO;
import com.colourfulchina.mars.service.EquityCodeDetailService;
import com.colourfulchina.mars.service.GiftCodeService;
import com.colourfulchina.mars.service.ReservOrderService;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/updateData")
public class UpdateDataController {

    @Autowired
    ReservOrderService reservOrderService;
    @Autowired
    GiftCodeService giftCodeService;
    @Autowired
    EquityCodeDetailService equityCodeDetailService;

    @SysGodDoorLog("修复useCount不对的数据")
    @ApiOperation("修复useCount不对的数据")
    @PostMapping(value = "/update")
    public CommonResultVo<String> getBoscCardList(@RequestBody ReservOrderVo reqVO){
        CommonResultVo<String> common = new CommonResultVo<String>();
        try {


            EntityWrapper<EquityCodeDetail> local = new EntityWrapper<>();
            local.eq("goods_id",reqVO.getGoodsId());
            local.eq("del_flag",0);
            List<EquityCodeDetail> equityCodeDetails = equityCodeDetailService.selectList(local);

            for(EquityCodeDetail detail : equityCodeDetails){
                EntityWrapper<ReservOrder> params = new EntityWrapper<>();
                params.eq("goods_id",reqVO.getGoodsId());
                params.eq("gift_code_id",detail.getGiftCodeId());
                params.eq("product_group_id",detail.getProductGroupId());
                params.in("prose_status",Lists.newArrayList(0,1,4));
                params.eq("del_flag",0);
                int size  = reservOrderService.selectCount(params);
                if(size != 0){
                    detail.setUseCount(size);
                    equityCodeDetailService.updateById(detail);
                }
            }
            common.setCode(100);
            common.setMsg("成功");
            common.setResult("成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            common.setCode(200);
            common.setMsg(e.getMessage());
        }
        return common;
    }
}
