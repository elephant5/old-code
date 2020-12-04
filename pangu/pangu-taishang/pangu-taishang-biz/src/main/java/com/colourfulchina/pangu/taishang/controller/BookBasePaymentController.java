package com.colourfulchina.pangu.taishang.controller;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.colourfulchina.god.door.api.annotation.SysGodDoorLog;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.pangu.taishang.api.entity.BookBasePayment;
import com.colourfulchina.pangu.taishang.api.vo.BookBasePaymentVo;
import com.colourfulchina.pangu.taishang.api.vo.req.SelectBookPayReq;
import com.colourfulchina.pangu.taishang.api.vo.res.bookBasePayment.BookBasePaymentRes;
import com.colourfulchina.pangu.taishang.service.BookBasePaymentService;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/bookBasePayment")
@Slf4j
@Api(tags = {"预约支付金额操作"})
public class BookBasePaymentController {
    @Autowired
    private BookBasePaymentService bookBasePaymentService;

    @SysGodDoorLog("查询产品预约支付金额")
    @ApiOperation("查询产品预约支付金额")
    @PostMapping("/selectBookPay")
    public CommonResultVo<List<BookBasePaymentRes>> selectBookPay(@RequestBody SelectBookPayReq selectBookPayReq){
        CommonResultVo<List<BookBasePaymentRes>> result = new CommonResultVo();
        try {
            Assert.notNull(selectBookPayReq,"参数不能为空");
            Assert.notNull(selectBookPayReq.getProductGroupProductId(),"产品组产品id不能为空");
            Assert.notEmpty(selectBookPayReq.getBookDates(),"预约时间不能为空");
            List<BookBasePaymentRes> bookBasePayments = bookBasePaymentService.selectBookPay(selectBookPayReq);
            result.setResult(bookBasePayments);
        }catch (Exception e){
            log.error("查询产品预约支付金额失败",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    @SysGodDoorLog("批量查询产品预约支付金额")
    @ApiOperation("批量查询产品预约支付金额")
    @PostMapping("/selectBookPayList")
    public CommonResultVo<List<BookBasePaymentRes>> selectBookPayList(@RequestBody List<Integer> productGroupProductIdList){
        CommonResultVo<List<BookBasePaymentRes>> result = new CommonResultVo();
        try {
            List<BookBasePaymentRes> bookBasePayments = bookBasePaymentService.selectBookPayList(productGroupProductIdList);
            result.setResult(bookBasePayments);
        }catch (Exception e){
            log.error("查询产品预约支付金额失败",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    @SysGodDoorLog("预约支付金额转换")
    @ApiOperation("预约支付金额转换")
    @PostMapping("/convertPayment")
    public CommonResultVo<BookBasePaymentVo> convertPayment(@RequestBody BookBasePaymentVo bookBasePaymentVo){
        CommonResultVo<BookBasePaymentVo> result = new CommonResultVo<>();
        try {
            Assert.notNull(bookBasePaymentVo,"参数不能为空");
            bookBasePaymentVo = bookBasePaymentService.convertPayment(bookBasePaymentVo);
            result.setResult(bookBasePaymentVo);
        }catch (Exception e){
            log.error("预约支付金额转换失败",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    @SysGodDoorLog("查询预约支付金额列表")
    @ApiOperation("查询预约支付金额列表")
    @PostMapping("/selectList")
    public CommonResultVo<List<BookBasePaymentVo>> selectList(@RequestBody Integer productGroupProductId){
        CommonResultVo<List<BookBasePaymentVo>> result = new CommonResultVo<>();
        List<BookBasePaymentVo> resList = Lists.newLinkedList();
        try {
            Assert.notNull(productGroupProductId,"产品组产品id不能为空");
            Wrapper wrapper = new Wrapper() {
                @Override
                public String getSqlSegment() {
                    return "where del_flag = 0 and product_group_product_id ="+productGroupProductId;
                }
            };
            List<BookBasePayment> list = bookBasePaymentService.selectList(wrapper);
            if (!CollectionUtils.isEmpty(list)){
                for (BookBasePayment bookBasePayment : list) {
                    BookBasePaymentVo bookBasePaymentVo = new BookBasePaymentVo();
                    BeanUtils.copyProperties(bookBasePayment,bookBasePaymentVo);
                    bookBasePaymentVo = bookBasePaymentService.translatePayment(bookBasePaymentVo);
                    resList.add(bookBasePaymentVo);
                }
            }
            result.setResult(resList);
        }catch (Exception e){
            log.error("查询预约支付金额列表失败",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    @SysGodDoorLog("检测该产品组是否存在预约支付的产品(true存在，false不存在)")
    @ApiOperation("检测该产品组是否存在预约支付的产品(true存在，false不存在)")
    @PostMapping("/checkGroupNeedPay")
    public CommonResultVo<Boolean> checkGroupNeedPay(@RequestBody Integer productGroupId){
        CommonResultVo<Boolean> result = new CommonResultVo<>();
        try {
            Assert.notNull(productGroupId,"参数有误");
            Wrapper wrapper = new Wrapper() {
                @Override
                public String getSqlSegment() {
                    return "WHERE del_flag = 0 AND product_group_product_id IN" +
                            " (SELECT id FROM product_group_product WHERE del_flag = 0 AND status = 0 AND product_group_id = "+productGroupId+" )";
                }
            };
            List<BookBasePayment> list = bookBasePaymentService.selectList(wrapper);
            if (CollectionUtils.isEmpty(list)){
                result.setResult(false);
            }else {
                result.setResult(true);
            }
        }catch (Exception e){
            log.error("检测产品组是否存在预约支付产品失败",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }
}