package com.colourfulchina.mars.controller;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.colourfulchina.colourfulCoupon.api.enums.CpnVouchersStatusEunm;
import com.colourfulchina.colourfulCoupon.api.vo.req.UpdateCpnReqVo;
import com.colourfulchina.god.door.api.annotation.SysGodDoorLog;
import com.colourfulchina.god.door.api.util.SecurityUtils;
import com.colourfulchina.god.door.api.vo.KltSysUser;
import com.colourfulchina.inf.base.utils.MaskUtils;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.inf.base.vo.PageVo;
import com.colourfulchina.mars.api.annotation.ReservOrderOperLog;
import com.colourfulchina.mars.api.constant.OrderConstant;
import com.colourfulchina.mars.api.entity.*;
import com.colourfulchina.mars.api.enums.ExpressStatusEnum;
import com.colourfulchina.mars.api.enums.PayOrderStatusEnum;
import com.colourfulchina.mars.api.enums.ReservOrderStatusEnums;
import com.colourfulchina.mars.api.vo.*;
import com.colourfulchina.mars.api.vo.req.AlipayBookOrderReq;
import com.colourfulchina.mars.api.vo.req.LogisticsReqVo;
import com.colourfulchina.mars.api.vo.req.QueryReserveOrderDateReqVO;
import com.colourfulchina.mars.api.vo.req.ResrrvOrderReqVo;
import com.colourfulchina.mars.service.*;
import com.colourfulchina.mars.utils.HelpUtils;
import com.colourfulchina.member.api.entity.MemMemberInfo;
import com.colourfulchina.member.api.res.MemLoginResDTO;
import com.colourfulchina.member.api.res.MemberAccountInfoVo;
import com.colourfulchina.nuwa.api.sms.model.SmsSendResult;
import com.colourfulchina.nuwa.api.vo.SysEmailSendReqVo;
import com.colourfulchina.pangu.taishang.api.enums.ResourceTypeEnums;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/reservOrder")

@Api(value = "预约订单相关Controller", tags = {"预约订单相关操作接口"})
public class ReservOrderController extends BaseController {

    @Autowired
    ReservOrderService reservOrderService;

    @Autowired
    ReservOrderDetailService reservOrderDetailService;

    @Autowired
    PanguInterfaceService panguInterfaceService;

    @Autowired
    ProductListService productListService;

    @Autowired
    NuwaInterfaceService nuwaInterfaceService;

    @Autowired
    MemberInterfaceService memberInterfaceService;

    @Autowired
    private CouponsService couponsService;

    @Autowired
    private SynchroPushService synchroPushService;
    @Autowired
    private LogisticsInfoService logisticsInfoService;

    @Autowired
    private ReservOrderHospitalService reservOrderHospitalService;

    /**
     * 商户列表模糊分页查询
     */
    @SysGodDoorLog("预约单列表")
    @ApiOperation("预约单列表")
    @PostMapping("/selectReservOrderPageList")
    public CommonResultVo<PageVo<ReservOrderVo>> selectGoodsPageList(
            @RequestBody PageVo<ReservOrderVo> pageVoReq) {
        CommonResultVo<PageVo<ReservOrderVo>> result = new CommonResultVo<>();
        try {
            log.info("ReservOrderController.selectGoodsPageList:{}",JSON.toJSONString(pageVoReq));
            PageVo<ReservOrderVo> pageVoRes = reservOrderService.selectReservOrderPageList(pageVoReq);
            if(!CollectionUtils.isEmpty(pageVoRes.getRecords())){
                for(ReservOrderVo vo : pageVoRes.getRecords()){
                    vo.setGiftName(MaskUtils.nameMask(vo.getGiftName()));
                }
            }
            result.setResult(pageVoRes);
        } catch (Exception e) {
            log.error("预约单列表查询失败", e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    @PostMapping("/selectPayTimeOutReservOrderList")
    public CommonResultVo<List<ReservOrder>> selectPayTimeOutReservOrderList(
            @RequestBody ReservOrderVo reservOrderVo) {
        CommonResultVo<List<ReservOrder>> result = new CommonResultVo<>();
        try {
            Assert.notNull(reservOrderVo, "参数不能为空");
            final Integer payTimeOut = reservOrderVo.getPayTimeOut();
            Assert.notNull(payTimeOut, "超时时间不能为空");
            reservOrderVo.setPayStatus(PayOrderStatusEnum.UNPAID.getCode());
            reservOrderVo.setProseStatus(ReservOrderStatusEnums.ReservOrderStatus.none.getcode());
            EntityWrapper<ReservOrder> orderWrapper = new EntityWrapper<>();
            orderWrapper.setEntity(reservOrderVo);
            Calendar now = Calendar.getInstance();
            now.add(Calendar.MINUTE, -payTimeOut);
            orderWrapper.le("create_time", now.getTime());
            final List<ReservOrder> reservOrderList = reservOrderService.selectList(orderWrapper);
            if (CollectionUtils.isEmpty(reservOrderList)) {
                log.info("ReservOrderController.selectPayTimeOutReservOrderList result is empty");
            } else {
                log.info("ReservOrderController.selectPayTimeOutReservOrderList result size : {}",
                        reservOrderList.size());
            }
            result.setResult(reservOrderList);
        } catch (Exception e) {
            log.error("查询支付超时预约单失败", e);
            result.setCode(200);
            result.setMsg("查询支付超时预约单失败");
        }
        return result;
    }

    /**
     * 匹配权益列表
     */
    @SysGodDoorLog("匹配权益列表")
    @ApiOperation("匹配权益列表")
    @PostMapping("/selectEquityList")
    public CommonResultVo<List<EquityListVo>> selectEquityList(
            @RequestBody EquityListVo equityListVo) {
        CommonResultVo<List<EquityListVo>> result = new CommonResultVo();
        try {
            Assert.notNull(equityListVo.getCode(), "激活码或者手机号不能为空！");

            Boolean isMobile = MaskUtils.mobileCheck(equityListVo.getCode());//判断是否是手机号

            List<EquityListVo> pageVoRes = reservOrderService.selectEquityList(equityListVo, isMobile);
            result.setResult(pageVoRes);
        } catch (Exception e) {
            log.error("匹配权益列表查询失败", e);
            result.setCode(200);
            result.setMsg("匹配权益列表查询失败");
        }
        return result;
    }

    /**
     * @Author: Nickal
     * @Description: 对外接口
     * @Date: 2019/9/17 11:08
     */
    @SysGodDoorLog("对外接口预约单详情")
    @ApiOperation("对外接口预约单详情")
    @PostMapping("/bookOrderDetail")
    public CommonResultVo<List<BookOrderDetail>> getBookOrderDetail(@RequestBody BookOrderReq req)
            throws Exception {
        if (req == null) {
            throw new Exception("参数为空");
        }
        CommonResultVo<List<BookOrderDetail>> common = new CommonResultVo<List<BookOrderDetail>>();
        try {
            List<BookOrderDetail> bookOrderDetailList = reservOrderService.getBookOrderDetail(req);
            common.setCode(100);
            common.setMsg("成功");
            common.setResult(bookOrderDetailList);
        } catch (Exception e) {
            common.setMsg(e.getMessage());
            common.setCode(200);
        }
        return common;
    }

    /**
     * @Author: Nickal
     * @Description: 对外接口
     * @Date: 2019/9/17 11:08
     */
    @SysGodDoorLog("对外接口权益次数查询")
    @ApiOperation("对外接口权益次数查询")
    @PostMapping("/giftConut")
    public CommonResultVo<List<Map<String, Object>>> getgiftConut(
            @RequestBody Map<String, Object> req) throws Exception {
        CommonResultVo<List<Map<String, Object>>> common = new CommonResultVo<List<Map<String, Object>>>();
        try {
            List<Map<String, Object>> bookOrderDetailList = reservOrderService.getgiftConut(req);
            common.setCode(100);
            common.setMsg("成功");
            common.setResult(bookOrderDetailList);
        } catch (Exception e) {
            common.setMsg(e.getMessage());
            common.setCode(200);
        }
        return common;
    }

    /**
     * @Author: Nickal
     * @Description: 对外接口
     * @Date: 2019/9/17 11:08
     */
    @SysGodDoorLog("对外接口预约单取消")
    @ApiOperation("对外接口预约单取消")
    @PostMapping("/bookOrderCancel")
    public CommonResultVo<ReservOrderVo> getbookOrderCancel(@RequestBody Map<String, Object> req)
            throws Exception {
        if (req == null) {
            throw new Exception("参数为空");
        }
        CommonResultVo<ReservOrderVo> common = new CommonResultVo<ReservOrderVo>();
        String str = req.get("orderId").toString();
        Integer id = null;
        if (str != null) {
            id = Integer.valueOf(str);
        }
        try {
            ReservOrderVo reservOrder = reservOrderService.selectReservOrderById(id);
            if (reservOrder == null) {
                throw new Exception("预约单不存在");
            }
//            if(reservOrder.getProseStatus() == "2" || reservOrder.getProseStatus() == "3"){
//                throw  new Exception("订单状态不可取消");
//            }

            //当前时间加24小时
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, 1);
            //将当前时间转化成string
            String nowTime = HelpUtils.dateToStr(calendar.getTime(), "yyyy-MM-dd HH:mm");
            //转化成时间戳
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            int startDay = 0;
            int endDay = 0;
            Date dateStart = new Date();
            if (reservOrder.getGiftType() != "accom") {
                String cc = reservOrder.getGiftDate() + " " + reservOrder.getGiftTime();
                dateStart = format.parse(cc);
            } else {
                dateStart = format.parse(reservOrder.getGiftDate());
            }
            Date dateEnd = format.parse(nowTime);
            startDay = (int) (dateStart.getTime() / 1000);
            endDay = (int) (dateEnd.getTime() / 1000);
            if (endDay - startDay > 0) {
                throw new Exception("距离预约使用日期不足24小时，不能直接取消订单");
            }
            // 返还权益
            reservOrder.setRefundInter(1);
            CommonResultVo<ReservOrderVo> bookOrderDetail = this.reservCancel(reservOrder);
            if (bookOrderDetail.getCode() == 100) {
                common.setCode(100);
                common.setMsg("成功");
                common.setResult(null);
            } else {
                common.setCode(200);
                common.setMsg(bookOrderDetail.getMsg());
            }
//            // === 第三方订单推送埋点 开始 ===
//            try {
//                synchroPushService.synchroPush(reservOrder.getId());
//            } catch (Exception e) {
//                log.error("第三方订单推送异常", e);
//            }
//            // === 第三方订单推送埋点 结束 ===
        } catch (Exception e) {
            common.setMsg(e.getMessage());
            common.setCode(200);
        }
        return common;
    }


    @SysGodDoorLog("根据id获取预约单")
    @ApiOperation("根据id获取预约单")
    @GetMapping("/selectReservOrderById/{id}")
    public CommonResultVo<ReservOrderVo> selectReservOrderById(@PathVariable Integer id) {
        CommonResultVo<ReservOrderVo> resultVo = new CommonResultVo<>();
        try {
            ReservOrderVo reservOrder = reservOrderService.selectReservOrderById(id);
//            reservOrder.setGiftName(MaskUtils.nameMask(reservOrder.getGiftName()));
//            if(StringUtils.isNotBlank(reservOrder.getGiftPhone())){
//                reservOrder.setGiftPhone(MaskUtils.maskPhone(reservOrder.getGiftPhone()));
//            }
//
//            reservOrder.setActiveName(MaskUtils.nameMask(reservOrder.getActiveName()));
//            if(StringUtils.isNotBlank(reservOrder.getActivePhone())){
//                reservOrder.setActivePhone(MaskUtils.maskPhone(reservOrder.getActivePhone()));
//            }

            resultVo.setResult(reservOrder);
        } catch (Exception e) {
            log.error("根据id获取预约单结果失败！{}", e);
            resultVo.setCode(200);
            resultVo.setMsg("根据id获取预约单结果失败！");
        }
        return resultVo;
    }

    @SysGodDoorLog("预约单开始处理")
    @ApiOperation("预约单开始处理")
    @ReservOrderOperLog(OrderConstant.RESERV_IN_PROGRESS)
    @GetMapping("/startHandle/{id}")
    public CommonResultVo<ReservOrder> startHandle(@PathVariable Integer id) {
        CommonResultVo<ReservOrder> resultVo = new CommonResultVo<>();
        try {
            ReservOrder reservOrder = reservOrderService.selectById(id);
            Assert
                    .isTrue(ReservOrderStatusEnums.ReservOrderStatus.canProcess(reservOrder.getProseStatus()),
                            "当前订单状态为:" + ReservOrderStatusEnums.ReservOrderStatus
                                    .getNameByCode(reservOrder.getProseStatus()));
            reservOrderService.getRedisReservOrder(id);
            reservOrder.setProseStatus(ReservOrderStatusEnums.ReservOrderStatus.process.getcode() + "");
            reservOrder.setOperator(SecurityUtils.getLoginName());
//            reservOrder.setOrderCreator(SecurityUtils.getLoginName());
            reservOrderService.updateById(reservOrder);
            ReservOrderVo reservOrderVo = reservOrderService.selectReservOrderById(reservOrder.getId());
            resultVo.setResult(reservOrder);
            reservOrderService.deleteRedisReservOrder(id);
            // === 第三方订单推送埋点 开始 ===
            try {
                synchroPushService.synchroPush(reservOrder.getId());
            } catch (Exception e) {
                log.error("第三方订单推送异常", e);
            }
//            if (ResourceTypeEnums.MEDICAL.getCode().equalsIgnoreCase(reservOrderVo.getServiceTypeCode()) ) {
                //绿通医疗发送邮件测试
//                reservOrderService.sendMailforMedicalOrderData(reservOrderVo);
//            }
            // === 第三方订单推送埋点 结束 ===
        } catch (Exception e) {
            log.error("根据id获取预约单结果失败！{}", e);
            resultVo.setCode(200);
            resultVo.setMsg(e.getMessage());
        }
        return resultVo;
    }

    @SysGodDoorLog("根据id获取预约单结果失败")
    @ApiOperation("根据id获取预约单结果失败")
    @GetMapping("/basic/{id}")
    public CommonResultVo<ReservOrder> basic(@PathVariable Integer id) {
        CommonResultVo<ReservOrder> resultVo = new CommonResultVo<>();
        try {
            Assert.notNull(id, "商品id不能为空！");
            ReservOrder reservOrder = reservOrderService.selectById(id);
            resultVo.setResult(reservOrder);
        } catch (Exception e) {
            log.error("根据id获取预约单结果失败！{}", e);
            resultVo.setCode(200);
            resultVo.setMsg(e.getMessage());
        }
        return resultVo;
    }

    /**
     * 来电预约新增
     */
    @SysGodDoorLog("来电预约新增")
    @ApiOperation("来电预约新增")
    @PostMapping("/insertReservOrder")
    public CommonResultVo<ReservOrderVo> insertReservOrder(@RequestBody ReservOrderVo reservOrderVo) {
        CommonResultVo<ReservOrderVo> result = new CommonResultVo();
        try {
            Assert.notNull(reservOrderVo.getGoodsId(), "商品id不能为空！");
            Assert.notNull(reservOrderVo.getProductId(), "产品id不能为空！");
            Assert.notNull(reservOrderVo.getProductGroupId(), "产品组id不能为空！");
            Assert.notNull(reservOrderVo.getProductGroupProductId(), "产品组产品id 不能为空！");
            reservOrderVo.setOrderCreator(SecurityUtils.getLoginName());
            if (reservOrderVo.getSuperposition().equals("1")) {
                ReservOrder reservOrder = reservOrderService.selectReservOrderVoIsExsist(reservOrderVo);
                if (null != reservOrder) {
                    result.setCode(200);
                    result.setResult(reservOrderVo);
                    result.setMsg("添加失败！当前项目同一时段权益不可叠加使用！");
                } else {
                    reservOrderVo = reservOrderService.insertReservOrder(reservOrderVo);
                    result.setResult(reservOrderVo);
                }
            } else if (reservOrderVo.getSingleThread().equals("1")) {
                ReservOrder reservOrder = reservOrderService.selectReservOrderVoIsSusess(reservOrderVo);
                if (null != reservOrder) {
                    result.setCode(200);
                    result.setResult(reservOrderVo);
                    result.setMsg("添加失败！当前项目行权完毕次日才可以进行下一次预约！");
                } else {
                    reservOrderVo = reservOrderService.insertReservOrder(reservOrderVo);
                    result.setResult(reservOrderVo);
                }
            } else {
                reservOrderVo = reservOrderService.insertReservOrder(reservOrderVo);
                result.setResult(reservOrderVo);
            }

        } catch (Exception e) {
            log.error("来电预约新增失败:{}", JSON.toJSONString(reservOrderVo), e);
            result.setCode(200);
            result.setMsg("系统异常：" + e.getMessage());
        }
        return result;
    }

    /**
     *
     */
    @SysGodDoorLog("变更订单信息")
    @ApiOperation("变更订单信息")
    @ReservOrderOperLog(OrderConstant.ADJUS_HOTEL)
    @PostMapping("/updateReservOrder")
    public CommonResultVo<ReservOrderVo> updateReservOrder(@RequestBody ReservOrderVo reservOrderVo) {
        CommonResultVo<ReservOrderVo> result = new CommonResultVo();
        try {

            Assert.notNull(reservOrderVo.getId(), "订单id不能为空！");
            reservOrderService.getRedisReservOrder(reservOrderVo.getId());
            reservOrderVo = reservOrderService.updateReservOrder(reservOrderVo);
            ReservOrderVo reservOrder = reservOrderService.selectReservOrderById(reservOrderVo.getId());
            result.setResult(reservOrder);
            reservOrderService.deleteRedisReservOrder(reservOrderVo.getId());
            // === 第三方订单推送埋点 开始 ===
            try {
                synchroPushService.synchroPush(reservOrder.getId());
            } catch (Exception e) {
                log.error("第三方订单推送异常", e);
            }
            // === 第三方订单推送埋点 结束 ===
        } catch (Exception e) {
            log.error("变更订单信息:{}", JSON.toJSONString(reservOrderVo), e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    @SysGodDoorLog("兑换券变更订单信息")
    @ApiOperation("兑换券变更订单信息")
    @ReservOrderOperLog(OrderConstant.ADJUS_HOTEL)
    @PostMapping("/updateCouponsReservOrder")
    public CommonResultVo<ReservOrderVo> updateCouponsReservOrder(
            @RequestBody ReservOrderVo reservOrderVo) {
        CommonResultVo<ReservOrderVo> result = new CommonResultVo();
        try {

            Assert.notNull(reservOrderVo.getId(), "订单id不能为空！");
            reservOrderVo = reservOrderService.updateCouponsReservOrder(reservOrderVo);
            result.setResult(reservOrderVo);
        } catch (Exception e) {
            log.error("变更订单信息:{}", JSON.toJSONString(reservOrderVo), e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    /**
     * 预订成功信息
     */
    @SysGodDoorLog("预订成功信息")
    @ApiOperation("预订成功信息")
    @ReservOrderOperLog(OrderConstant.RESERV_SUCCESS)
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/reservSuccess")
    public CommonResultVo<ReservOrderVo> reservSuccess(@RequestBody ReservOrderVo reservOrderVo) {
        CommonResultVo<ReservOrderVo> result = new CommonResultVo();
        try {
            Assert.notNull(reservOrderVo.getId(), "订单id不能为空！");
            reservOrderService.getRedisReservOrder(reservOrderVo.getId());
            log.info("reservSuccess reservOrderVo 1:{}", JSON.toJSONString(reservOrderVo));
            reservOrderVo = reservOrderService.reservSuccess(reservOrderVo);
            log.info("reservSuccess reservOrderVo 2:{}", JSON.toJSONString(reservOrderVo));
            ReservOrderVo reservOrder = reservOrderService.selectReservOrderById(reservOrderVo.getId());
            result.setResult(reservOrder);
            reservOrderService.deleteRedisReservOrder(reservOrderVo.getId());
            // === 第三方订单推送埋点 开始 ===
            try {
                    synchroPushService.synchroPush(reservOrder.getId());
            } catch (Exception e) {
                log.error("第三方订单推送异常", e);
            }
            // === 第三方订单推送埋点 结束 ===
        } catch (Exception e) {
            log.error("预订成功信息:{}", JSON.toJSONString(reservOrderVo), e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    /**
     * 预订失败信息
     */
    @SysGodDoorLog("预订失败信息")
    @ApiOperation("预订失败信息")
    @ReservOrderOperLog(OrderConstant.RESERV_FAIL)
    @PostMapping("/reservFail")
    public CommonResultVo<ReservOrderVo> reservFail(@RequestBody ReservOrderVo reservOrderVo) {
        CommonResultVo<ReservOrderVo> result = new CommonResultVo();
        try {
            Assert.notNull(reservOrderVo.getId(), "订单id不能为空！");
            reservOrderService.getRedisReservOrder(reservOrderVo.getId());
            log.info("reservFail reservOrderVo 1:{}", JSON.toJSONString(reservOrderVo));
            reservOrderVo = reservOrderService.reservFail(reservOrderVo);
            log.info("reservFail reservOrderVo 2:{}", JSON.toJSONString(reservOrderVo));
            ReservOrderVo reservOrder = reservOrderService.selectReservOrderById(reservOrderVo.getId());
            result.setResult(reservOrder);
            reservOrderService.deleteRedisReservOrder(reservOrderVo.getId());
            // === 第三方订单推送埋点 开始 ===
            try {
                synchroPushService.synchroPush(reservOrder.getId());
            } catch (Exception e) {
                log.error("第三方订单推送异常", e);
            }
            // === 第三方订单推送埋点 结束 ===
        } catch (Exception e) {
            log.error("预订失败信息:{}", JSON.toJSONString(reservOrderVo), e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    /**
     * 预订取消信息
     */
    @SysGodDoorLog("预订取消信息")
    @ApiOperation("预订取消信息")
    @ReservOrderOperLog(OrderConstant.RESERV_CANCEL)
    @PostMapping("/reservCancel")
    public CommonResultVo<ReservOrderVo> reservCancel(@RequestBody ReservOrderVo reservOrderVo) {
        CommonResultVo<ReservOrderVo> result = new CommonResultVo();
        //TODO 预约单 取消时间不能大于预定时间 包括预约时段的check
        try {
            Assert.notNull(reservOrderVo.getId(), "订单id不能为空！");
            reservOrderService.getRedisReservOrder(reservOrderVo.getId());
            log.info("reservCancel reservOrderVo 1:{}", JSON.toJSONString(reservOrderVo));
            if(StringUtils.isBlank(reservOrderVo.getCancelOperator())){
                if( StringUtils.isBlank(reservOrderVo.getTags()) || reservOrderVo.getTags().indexOf("用户") <= 0){
                    reservOrderVo.setTags("用户自行取消,");
                }
            }
            reservOrderVo = reservOrderService.reservCancel(reservOrderVo);
            log.info("reservCancel reservOrderVo 2:{}", JSON.toJSONString(reservOrderVo));
            ReservOrderVo reservOrder = reservOrderService.selectReservOrderById(reservOrderVo.getId());
            result.setResult(reservOrder);
            reservOrderService.deleteRedisReservOrder(reservOrderVo.getId());

            //如果使用了优惠券,取消预约单时，更改状态为未使用
            if (null != reservOrder.getCpnType() && null != reservOrder.getCpnId()) {
                UpdateCpnReqVo updateCpnReqVo = new UpdateCpnReqVo();
                updateCpnReqVo.setCouponsType(reservOrder.getCpnType());
                updateCpnReqVo.setCpnId(reservOrder.getCpnId());
                updateCpnReqVo.setStatus(CpnVouchersStatusEunm.NOT_USE.getValue()); //未使用
                final Boolean f = couponsService.updateCoupon(updateCpnReqVo);
                if (!f) {
                    throw new Exception("更新券状态失败");
                }
            }
            // === 第三方订单推送埋点 开始 ===
            try {
                synchroPushService.synchroPush(reservOrder.getId());
            } catch (Exception e) {
                log.error("第三方订单推送异常", e);
            }
            // === 第三方订单推送埋点 结束 ===
        } catch (Exception e) {
            log.error("预订取消信息:{}", JSON.toJSONString(reservOrderVo), e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    @SysGodDoorLog("修改订单")
    @ApiOperation("修改订单")
    @PostMapping("/reservUpdate")
    public CommonResultVo<ReservOrderVo> reservUpdate(@RequestBody ReservOrderVo reservOrderVo) {
        ReservOrder reservOrder = reservOrderService.selectById(reservOrderVo.getId());
        BeanUtils.copyProperties(reservOrderVo, reservOrder);
        reservOrderService.updateById(reservOrder);
        return new CommonResultVo() {
            {
                setResult(reservOrderVo);
            }
        };
    }

    /**
     * 预订失败信息
     */
    @SysGodDoorLog("修正信息")
    @ReservOrderOperLog(OrderConstant.RESERV_FIX)
    @ApiOperation("修正信息")
    @PostMapping("/reservUpdateInfo")
    public CommonResultVo<ReservOrderVo> reservUpdateInfo(@RequestBody ReservOrderVo reservOrderVo) {
        CommonResultVo<ReservOrderVo> result = new CommonResultVo();
        try {
            Assert.notNull(reservOrderVo.getId(), "订单id不能为空！");
            reservOrderService.getRedisReservOrder(reservOrderVo.getId());
            reservOrderVo = reservOrderService.reservUpdateInfo(reservOrderVo);
            ReservOrderVo reservOrder = reservOrderService.selectReservOrderById(reservOrderVo.getId());
            result.setResult(reservOrder);
            reservOrderService.deleteRedisReservOrder(reservOrderVo.getId());

            //如果使用了优惠券,预订失败时，更改状态为未使用
            if (null != reservOrder.getCpnType() && null != reservOrder.getCpnId()) {
                UpdateCpnReqVo updateCpnReqVo = new UpdateCpnReqVo();
                updateCpnReqVo.setCouponsType(reservOrder.getCpnType());
                updateCpnReqVo.setCpnId(reservOrder.getCpnId());
                updateCpnReqVo.setStatus(CpnVouchersStatusEunm.NOT_USE.getValue()); //未使用
                final Boolean f = couponsService.updateCoupon(updateCpnReqVo);
                if (!f) {
                    throw new Exception("更新券状态失败");
                }
            }
            // === 第三方订单推送埋点 开始 ===
            try {
                synchroPushService.synchroPush(reservOrder.getId());
            } catch (Exception e) {
                log.error("第三方订单推送异常", e);
            }
            // === 第三方订单推送埋点 结束 ===
        } catch (Exception e) {
            log.error("预订取消信息:{}", JSON.toJSONString(reservOrderVo), e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }


    /**
     * 获取下预约单所需的产品信息
     */
    @SysGodDoorLog("获取下预约单所需的产品信息")
    @ApiOperation("获取下预约单所需的产品信息")
    @GetMapping("/getReservOrderVo/{productGroupProductId}")
    public CommonResultVo<ReservOrderProductVo> getReservOrderVo(
            @PathVariable("productGroupProductId") Integer productGroupProductId) {
        CommonResultVo<ReservOrderProductVo> result = new CommonResultVo();
        try {
            Assert.notNull(productGroupProductId, "产品产品组关联id不能为空！");
            ReservOrderProductVo vo = productListService.selectReservOrderVo(productGroupProductId);
            result.setResult(vo);
        } catch (Exception e) {
            log.error("下预约单所需的产品信息查询失败", e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    @SysGodDoorLog("根据id获取预约单")
    @ApiOperation("根据id获取预约单")
    @GetMapping("/get/{id}")
    public CommonResultVo<ReservOrderProductVo> get(@PathVariable Integer id) {
        CommonResultVo<ReservOrderProductVo> resultVo = new CommonResultVo<>();
        try {
            MemLoginResDTO memInfo = getLoginUser();
            ReservOrderProductVo reservOrder = reservOrderService.getReservOrder(id, memInfo);
            resultVo.setResult(reservOrder);
        } catch (Exception e) {
            log.error("根据id获取预约单结果失败！{}", e);
            resultVo.setCode(200);
            resultVo.setMsg(e.getMessage());
        }
        return resultVo;
    }

    @SysGodDoorLog("根据id获取预约单")
    @ApiOperation("根据id获取预约单/暂时用于前端")
    @GetMapping("/getOrder/{id}")
    public CommonResultVo<ReservOrderVo> getReservOrder(@PathVariable("id") Integer id) {
        CommonResultVo<ReservOrderVo> resultVo = new CommonResultVo<>();
        try {
            ReservOrder reservOrder = reservOrderService.selectById(id);
            ReservOrderVo reservOrderVo = new ReservOrderVo();
            BeanUtils.copyProperties(reservOrder, reservOrderVo);
            ReservOrderDetail reservOrderDetail = reservOrderDetailService.selectOneReservOrderDetail(reservOrder.getId());
            reservOrderVo.setDeparDate(reservOrderDetail.getDeparDate());
            resultVo.setResult(reservOrderVo);
        } catch (Exception e) {
            log.error("根据id获取预约单结果失败！{}", e);
            resultVo.setCode(200);
            resultVo.setMsg(e.getMessage());
        }
        return resultVo;
    }


    @SysGodDoorLog("根据id获取预约单")
    @ApiOperation("根据id获取预约单/暂时用于前端")
    @PostMapping("/getOrders")
    public CommonResultVo<List<ReservOrder>> getReservOrders(@RequestBody AlipayBookOrderReq req) {
        CommonResultVo<List<ReservOrder>> resultVo = new CommonResultVo<>();
        try {
            EntityWrapper<ReservOrder> wrapper = new EntityWrapper<>();
            wrapper.eq("member_id", req.getMemberId());
            if (!CollectionUtils.isEmpty(req.getIdList())) {
                wrapper.notIn("id", req.getIdList());
            }
            wrapper.ne("pay_status", 1);
            List<ReservOrder> reservOrder = reservOrderService.selectList(wrapper);
            resultVo.setResult(reservOrder);
        } catch (Exception e) {
            log.error("根据id获取预约单结果失败！{}", e);
            resultVo.setCode(200);
            resultVo.setMsg(e.getMessage());
        }
        return resultVo;
    }

    @SysGodDoorLog("改变预约单支付状态")
    @ApiOperation("改变预约单支付状态")
    @PostMapping("/changeOrderPayStatus")
    public CommonResultVo<Boolean> changeOrderPayStatus(@RequestBody ReservOrder reservOrder) {
        CommonResultVo<Boolean> result = new CommonResultVo();
        try {
            Assert.notNull(reservOrder, "参数不能为空");
            Assert.notNull(reservOrder.getId(), "预约单id不能为空");
            Assert.notNull(reservOrder.getPayStatus(), "支付状态不能为空");
            EntityWrapper<ReservOrder> wrapper = new EntityWrapper<>();
            wrapper.eq("id", reservOrder.getId());
            boolean flag = reservOrderService.updateForSet("pay_status=" + reservOrder.getPayStatus(), wrapper);
            result.setResult(flag);
        } catch (Exception e) {
            log.error("改变预约单支付状态失败", e);
            result.setCode(200);
            result.setMsg("改变预约单支付状态失败");
        }
        return result;
    }


    @SysGodDoorLog("获取预约单列表 分页")
    @ApiOperation("获取预约单列表 分页")
    @PostMapping("/page")
    public CommonResultVo<PageVo<ReservOrderProductVo>> page(
            @RequestBody PageVo<ReservOrder> pageVo) {
        CommonResultVo<PageVo<ReservOrderProductVo>> resultVo = new CommonResultVo<>();
        try {
            Map<String, Object> condition = pageVo.getCondition();
            //上线后放开
            MemLoginResDTO memInfo = getLoginUser();
            condition.put("memberId", memInfo.getAcid());
            PageVo<ReservOrderProductVo> list = reservOrderService.selectReservOrderList(pageVo);
            resultVo.setResult(list);
        } catch (Exception e) {
            log.error("获取预约单列表！{}", e);
            resultVo.setCode(200);
            resultVo.setMsg(e.getMessage());
        }
        return resultVo;
    }

    @SysGodDoorLog("发送短信")
    @ApiOperation("发送短信")
    @ReservOrderOperLog(OrderConstant.REPEAT_SEND_SMS)
    @PostMapping("/message")
    public CommonResultVo<SmsSendResult> message(@RequestBody ReservOrderVo reservOrderVo) {
        CommonResultVo<SmsSendResult> resultVo = new CommonResultVo<>();
        try {
            ReservOrderVo reservOrder = reservOrderService.selectReservOrderById(reservOrderVo.getId());
            SmsSendResult smsSendResult = nuwaInterfaceService.sendMsgReservOrderVo(reservOrder);
            log.info("send Message info :{}", JSON.toJSONString(smsSendResult));
            resultVo.setResult(smsSendResult);
        } catch (Exception e) {
            log.error("发送短信失败！{},{}", reservOrderVo.getId(), e);
            resultVo.setCode(200);
            resultVo.setMsg("发送短信失败！");
        }
        return resultVo;
    }

    @SysGodDoorLog("手机号码验证项目权益")
    @ApiOperation("手机号码验证项目权益")
    @PostMapping("/phoneCheckEquity")
    public CommonResultVo<List<EquityListVo>> phoneCheck(@RequestBody ReservOrderVo reservOrderVo) {
        CommonResultVo<List<EquityListVo>> resultVo = new CommonResultVo<>();
        List<EquityListVo> selectEquityList = Lists.newArrayList();
        try {
            //先查出会员ID
            List<Long> members = Lists.newArrayList();
            MemMemberInfo memMemberInfo = new MemMemberInfo();
            memMemberInfo.setMobile(reservOrderVo.getActivePhone());
            MemberAccountInfoVo memberAccountInfoVo = memberInterfaceService.getMember(memMemberInfo);
            members = memberAccountInfoVo.getAccList().stream().map(obj -> obj.getAcid())
                    .collect(Collectors.toList());
            selectEquityList = reservOrderService.selectEquityListByMembers(members);
            resultVo.setResult(selectEquityList);
        } catch (Exception e) {
            log.error("手机号码验证项目失败！{}", e);
            resultVo.setCode(200);
            resultVo.setMsg("手机号码验证项目失败！");
        }
        return resultVo;
    }

    @SysGodDoorLog("手机号码验证项目")
    @ApiOperation("手机号码验证项目")
    @PostMapping("/phoneCheckReservOrder")
    public CommonResultVo<List<ReservOrder>> selectReservOrderVoByPhoneIsExsist(
            @RequestBody ReservOrderVo reservOrderVo) {
        CommonResultVo<List<ReservOrder>> resultVo = new CommonResultVo<>();
        List<ReservOrder> reservOrderList = Lists.newArrayList();
        try {
            reservOrderList = reservOrderService.selectReservOrderVoByPhoneIsExsist(reservOrderVo);
            resultVo.setResult(reservOrderList);
        } catch (Exception e) {
            log.error("手机号码验证项目失败！{}", e);
            resultVo.setCode(200);
            resultVo.setMsg("手机号码验证项目失败！");
        }
        return resultVo;
    }


    @SysGodDoorLog("导出所选条件的记录为Excel表")
    @ApiOperation("导出所选条件的记录为Excel表")
    @PostMapping("/export-old")
    public CommonResultVo<String> exportOld(@RequestBody PageVo<ReservOrderVo> pageVoReq) {
        CommonResultVo<String> result = new CommonResultVo<>();
        try {
            List<String> services = (List<String>) pageVoReq.getCondition().get("serviceTypes");
            Assert.notEmpty(services, "资源类型不能为空");
            Assert.isTrue(!(services.size() > 1 && services.contains("accom")), "资源类型住宿和非住宿不能同时都出");
            PageVo<ReservOrderVo> newPageVo = new PageVo<>();
            BeanUtils.copyProperties(pageVoReq, newPageVo);
            newPageVo.setSize(Integer.MAX_VALUE);
            String url = reservOrderService.exportReservOrder(newPageVo);
            result.setResult(url);
        } catch (Exception e) {
            log.error("预约单导出失败", e);
            result.setCode(200);
            result.setMsg("预约单导出失败");
        }
        return result;
    }

    @SysGodDoorLog("导出所选条件的记录为Excel表")
    @ApiOperation("导出所选条件的记录为Excel表")
    @PostMapping("/export")
    public CommonResultVo<String> export(@RequestBody PageVo<ReservOrderVo> pageVoReq) {
        CommonResultVo<String> result = new CommonResultVo<>();
        try {
            log.info("================newExport============");
            List<String> services = (List<String>) pageVoReq.getCondition().get("serviceTypes");
            Assert.notEmpty(services, "资源类型不能为空");
            Assert.isTrue(!(services.size() > 1 && services.contains("accom")), "资源类型住宿和非住宿不能同时都出");
            pageVoReq.setSize(Integer.MAX_VALUE);
            KltSysUser sysUser = SecurityUtils.getAttributeUser(request);
            log.info("=====sysUser:{}", JSON.toJSONString(sysUser));
            result.setResult(reservOrderService.exportReservOrderNew(pageVoReq, sysUser));
        } catch (Exception e) {
            log.error("预约单导出失败:{}", JSON.toJSONString(pageVoReq), e);
            result.setCode(200);
            result.setMsg("预约单导出失败");
        }
        return result;
    }

    private void exportExcel(HttpServletResponse response,
                             List<ReservOrderVo> historyOrderResBeanList) {
        response.setContentType("text/html;charset=utf-8");
        try {
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + "历史成功订单表" + new SimpleDateFormat("yyyyMMddHHmmss")
                            .format(new Date().getTime()) + ".xls");
            ServletOutputStream out = response.getOutputStream();
            reservOrderService.exportExcel(out, historyOrderResBeanList);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @SysGodDoorLog("查询该权益预约成功后并使用的最早时间")
    @ApiOperation("查询该权益预约成功后并使用的最早时间")
    @PostMapping("/getReserveOrderDate")
    public CommonResultVo<String> getReserveOrderDate(@RequestBody QueryReserveOrderDateReqVO reqVO) {
        CommonResultVo<String> result = new CommonResultVo<>();
        try {
            String orderSuccessDate = reservOrderService.getReserveOrderSucessDate(reqVO);
            result.setCode(100);
            result.setResult(orderSuccessDate);
        } catch (Exception e) {
            log.error("预约单导出失败", e);
            result.setCode(200);
            result.setMsg("预约单导出失败");
        }
        return result;
    }

    @SysGodDoorLog("获取预约单列表")
    @ApiOperation("获取预约单列表")
    @PostMapping("/getOrderList")
    public CommonResultVo<List<ReservOrder>> getOrderList(@RequestBody Integer giftCodeId) {
        CommonResultVo<List<ReservOrder>> result = new CommonResultVo<List<ReservOrder>>();
        try {
            List<ReservOrder> list = reservOrderService.getOrderList(giftCodeId);
            result.setCode(100);
            result.setResult(list);
        } catch (Exception e) {
            log.error("获取预约单列表失败:{}", giftCodeId, e);
            result.setCode(200);
            result.setMsg("获取预约单列表失败");
        }
        return result;
    }

    @SysGodDoorLog("根据各种条件查询预约单列表")
    @ApiOperation("根据各种条件查询预约单列表")
    @PostMapping("/getOrderListByContion")
    public CommonResultVo<List<ReservOrder>> getOrderListByContion(@RequestBody ReservOrder order) {
        CommonResultVo<List<ReservOrder>> result = new CommonResultVo<List<ReservOrder>>();
        try {
            List<ReservOrder> list = reservOrderService.getOrderListByCondition(order);
            result.setCode(100);
            result.setResult(list);
        } catch (Exception e) {
            log.error("获取预约单列表失败:{},memberId==={}", order.getGiftCodeId(), order.getMemberId(), e);
            result.setCode(200);
            result.setMsg("获取预约单列表失败");
        }
        return result;
    }


    @PostMapping("/getComInfoByOrderIds")
    public CommonResultVo<List<ReservOrderComInfoVo>> getComInfoByOrderIds(@RequestBody List<Integer> ids) {
        CommonResultVo<List<ReservOrderComInfoVo>> result = new CommonResultVo<>();
        try {
            List<ReservOrderComInfoVo> reservOrderComInfoByOrderIds = reservOrderService.getReservOrderComInfoByOrderIds(ids);
            result.setCode(100);
            result.setResult(reservOrderComInfoByOrderIds);
        } catch (Exception e) {
            log.error("获取预约单相关信息失败", e);
            result.setCode(200);
            result.setMsg("获取预约单相关信息失败");
        }
        return result;
    }

    @PostMapping("/updateComInfoByMap")
    public CommonResultVo<Boolean> updateComInfoByMap(@RequestBody Map<String, Date> map) {
        CommonResultVo<Boolean> result = new CommonResultVo<>();
        try {
            Boolean b = reservOrderService.updateReservOrderStatusByMap(map);
            Boolean b2 = reservOrderDetailService.updateStatusByMap(map);
            result.setCode(100);
            result.setResult(b && b2);
        } catch (Exception e) {
            log.error("获取预约单相关信息失败", e);
            result.setCode(200);
            result.setMsg("获取预约单相关信息失败");
        }

        return result;
    }

    @PostMapping("/getReservOrderBySalesOrderId")
    public CommonResultVo<ReservOrder> getReservOrderBySalesOrderId(@RequestBody String salesOrderId){
        CommonResultVo<ReservOrder> result = new CommonResultVo<>();
        try {
            ReservOrder order = reservOrderService.getReservOrderBySalesOrderId(salesOrderId);
            result.setCode(100);
            result.setResult(order);
        } catch (Exception e) {
            log.error("获取预约单相关信息失败", e);
            result.setCode(200);
            result.setMsg("获取预约单相关信息失败");
        }
        return result;
    }

    @PostMapping("/checkResrrvOrder")
    public CommonResultVo<Boolean> checkResrrvOrder(@RequestBody ResrrvOrderReqVo reqVo) {
        Assert.notNull(reqVo, "参数不能为空");
        CommonResultVo<Boolean> result = new CommonResultVo<>();
        try {
            Boolean f = reservOrderService.checkResrrvOrderNum(reqVo);
            result.setCode(100);
            result.setResult(f);
        } catch (Exception e) {
            log.error("检测是否可以下预约单失败", e.getMessage());
            result.setCode(200);
            result.setMsg("检测是否可以下预约单失败");
        }
        return result;
    }


    @SysGodDoorLog("查询支付宝钻铂免费项目取消/失败的已支付的预约单")
    @PostMapping("/selectAlipayFreeList")
    public CommonResultVo<List<ReservOrder>> selectAlipayFreeList(){
        CommonResultVo<List<ReservOrder>> result = new CommonResultVo<>();
        try {
            List<String> statusList = Lists.newLinkedList();
            statusList.add(ReservOrderStatusEnums.ReservOrderStatus.cancel.getcode());
            statusList.add(ReservOrderStatusEnums.ReservOrderStatus.failed.getcode());
            //设置当前时间-7天的日期
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.DAY_OF_MONTH,-7);
            String date = DateUtil.format(calendar.getTime(),"yyyy-MM-dd");
            Wrapper wrapper = new Wrapper() {
                @Override
                public String getSqlSegment() {
                    return "where goods_id = 428 and pay_status = "+PayOrderStatusEnum.PREPAID.getCode()+" and prose_status in ('"+StringUtils.join(statusList,"','")+"')" +
                            " and (date(cancel_date) >= str_to_date('"+date+"','%Y-%m-%d') or date(fail_date) >= str_to_date('"+date+"','%Y-%m-%d'))";
                }
            };
            List<ReservOrder> list = reservOrderService.selectList(wrapper);
            result.setResult(list);
        }catch (Exception e){
            log.error("查询支付宝钻铂免费项目取消/失败的已支付的预约单失败",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    @SysGodDoorLog("客服操作保存编辑实物的物流信息")
    @ReservOrderOperLog(OrderConstant.SAVE_OBJ_EDIT)
    @PostMapping("/saveObjEdit")
    public CommonResultVo<ReservOrderVo> saveObjEdit(@RequestBody LogisticsInfo logisticsInfo){
        CommonResultVo<ReservOrderVo> result = new CommonResultVo();
        try {
            Assert.notNull(logisticsInfo,"参数不能为空");
            Assert.notNull(logisticsInfo.getReservOrderId(),"订单号不能为空");
            LogisticsInfo temp = logisticsInfoService.selectById(logisticsInfo.getReservOrderId());
            temp.setConsignee(logisticsInfo.getConsignee());
            temp.setPhone(logisticsInfo.getPhone());
            temp.setAddress(logisticsInfo.getAddress());
            temp.setExpressNameId(logisticsInfo.getExpressNameId());
            temp.setExpressNumber(logisticsInfo.getExpressNumber());
            temp.setUpdateUser(SecurityUtils.getLoginName());
            logisticsInfoService.updateById(temp);
            ReservOrderVo reservOrder = reservOrderService.selectReservOrderById(logisticsInfo.getReservOrderId());
            result.setResult(reservOrder);
        }catch (Exception e){
            log.error("客服操作保存编辑实物的物流信息失败",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    @SysGodDoorLog("客服操作保存编辑实物的物流信息并发货")
    @PostMapping("/saveObjEditAndSend")
    @ReservOrderOperLog(OrderConstant.SEND_OBJ_EDIT)
    public CommonResultVo<ReservOrderVo> saveObjEditAndSend(@RequestBody LogisticsReqVo req){
        CommonResultVo<ReservOrderVo> result = new CommonResultVo();
        try {
            Assert.notNull(req,"参数不能为空");
            Assert.notNull(req.getReservOrderId(),"订单号不能为空");
            LogisticsInfo temp = logisticsInfoService.selectById(req.getReservOrderId());
            temp.setConsignee(req.getConsignee());
            temp.setPhone(req.getPhone());
            temp.setAddress(req.getAddress());
            temp.setExpressNameId(req.getExpressNameId());
            temp.setExpressNumber(req.getExpressNumber());
            temp.setExpressDate(new Date());
            temp.setStatus(ExpressStatusEnum.CAN_SEND.getCode());
            temp.setUpdateUser(SecurityUtils.getLoginName());
            logisticsInfoService.updateById(temp);
            ReservOrderVo reservOrder = reservOrderService.selectReservOrderById(req.getReservOrderId());
            result.setResult(reservOrder);
        }catch (Exception e){
            log.error("客服操作保存编辑实物的物流信息并发货失败",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    @PostMapping("/selectSuceessReservOrderList")
    public CommonResultVo<List<ReservOrderVo>> selectSuceessReservOrderList(
            @RequestBody ReservOrderVo reservOrderVo) {
        CommonResultVo<List<ReservOrderVo>> result = new CommonResultVo<>();
        try {
            Assert.notNull(reservOrderVo, "参数不能为空");

            Assert.notNull(reservOrderVo.getSuccessDate(), "预订成功时间不能为空");
            reservOrderVo.setPayStatus(PayOrderStatusEnum.UNPAID.getCode());
            reservOrderVo.setProseStatus(ReservOrderStatusEnums.ReservOrderStatus.none.getcode());
            EntityWrapper<ReservOrder> orderWrapper = new EntityWrapper<>();
            orderWrapper.eq("del_flag","0");
            Calendar now = Calendar.getInstance();
            now.add(Calendar.DAY_OF_MONTH, -1);
            now.set(Calendar.HOUR_OF_DAY, 0);
            now.set(Calendar.MINUTE, 0);
            now.set(Calendar.SECOND, 0);
            orderWrapper.le("DATE_FORMAT(success_date ,'%Y-%m-%d')", DateUtil.format(now.getTime(),"yyyy-MM-dd HH:mm:ss"));
            orderWrapper.eq("proseStatus",ReservOrderStatusEnums.ReservOrderStatus.done);
            orderWrapper.eq("serviceType", ResourceTypeEnums.MEDICAL.getCode());

            final List<ReservOrder> reservOrderList = reservOrderService.selectList(orderWrapper);
            List<ReservOrderVo> reservOrderVos = Lists.newArrayList();
            for(ReservOrder reservOrder : reservOrderList){
                ReservOrderVo vo = new ReservOrderVo();
                BeanUtils.copyProperties(reservOrder,vo);
                Wrapper<ReservOrderHospital> local3 = new Wrapper<ReservOrderHospital>() {
                    @Override
                    public String getSqlSegment() {
                        return "where del_flag = 0 and order_id = " + reservOrder.getId();
                    }
                };
                ReservOrderHospital hospital = reservOrderHospitalService.selectOne(local3);
                vo.setHospital(hospital);
                reservOrderVos.add(vo);
            }
            result.setResult(reservOrderVos);
        } catch (Exception e) {
            log.error("查询支付超时预约单失败", e);
            result.setCode(200);
            result.setMsg("查询支付超时预约单失败");
        }
        return result;
    }




}