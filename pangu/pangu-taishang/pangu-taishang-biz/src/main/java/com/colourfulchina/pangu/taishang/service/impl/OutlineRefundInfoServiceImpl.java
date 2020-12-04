package com.colourfulchina.pangu.taishang.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.alipay.api.entity.SalesOrder;
import com.colourfulchina.alipay.api.entity.SalesOrderCode;
import com.colourfulchina.alipay.api.feign.RemoteBookOrderPaymentService;
import com.colourfulchina.alipay.api.feign.RemoteSalesOrderService;
import com.colourfulchina.alipay.api.vo.BookOrderPayComInfoVo;
import com.colourfulchina.alipay.api.vo.SalesOrderComInfoVo;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.mars.api.feign.RemoteReservOrderService;
import com.colourfulchina.mars.api.vo.ReservOrderComInfoVo;
import com.colourfulchina.pangu.taishang.api.entity.*;
import com.colourfulchina.pangu.taishang.api.vo.OutlineRefundInfoExport;
import com.colourfulchina.pangu.taishang.api.vo.req.OutlineRefundInfoReq;
import com.colourfulchina.pangu.taishang.api.vo.res.GoodsChannelRes;
import com.colourfulchina.pangu.taishang.api.vo.res.OutlineRefundInfoRes;
import com.colourfulchina.pangu.taishang.config.RestApiProperties;
import com.colourfulchina.pangu.taishang.mapper.OutlineRefundInfoMapper;
import com.colourfulchina.pangu.taishang.service.*;
import com.colourfulchina.pangu.taishang.utils.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class OutlineRefundInfoServiceImpl extends ServiceImpl<OutlineRefundInfoMapper, OutlineRefundInfo> implements OutlineRefundInfoService {

//    @Autowired
//    private BankSalesOrderService bankSalesOrderService; 使用rest调用接口

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private ProductItemService productItemService;

    @Autowired
    private ShopService shopService;

    @Autowired
    private HotelService hotelService;

    @Autowired
    private SalesChannelService salesChannelService;

    @Autowired
    private RestApiProperties restApiProperties;

    private RemoteReservOrderService remoteReservOrderService;

    private RemoteSalesOrderService remoteSalesOrderService;

    private RemoteBookOrderPaymentService remoteBookOrderPaymentService;

    @Override
    public Page<OutlineRefundInfoRes> getOutlineRefundInfoByImport(List<OutlineRefundInfoReq> list) {
        //获取所有订单号
        List<String> orderNos = new ArrayList<>();
        list.forEach(info -> orderNos.add(info.getOrderNo()));

        //----商区销售单----
        List<String> collect = orderNos.stream().filter(a -> a.contains("-")).collect(Collectors.toList());
        orderNos.removeAll(collect);

        //----支付宝销售单----
        List<String> so = orderNos.stream().filter(a -> a.startsWith("SO")).collect(Collectors.toList());
        //add 支付宝预约单, ziya.book_order_payment
        List<String> bo = orderNos.stream().filter(a -> a.startsWith("BO")).collect(Collectors.toList());
        orderNos.removeAll(bo);
        orderNos.removeAll(so);

        //----商区销售单----
        collect.addAll(orderNos.stream().filter(a -> a.length() > 11).collect(Collectors.toList()));

        //----在线预约单----
        orderNos.removeAll(collect);


        //START=====================================================
        //1、获取所有渠道的需要退单的表数据
        //1.1、商区销售订单表 colourful.bank_sales_order, 使用rest接口调用
        List<Map<String, Object>> bankSalesOrderVos = null;
        if(collect.size() > 0){
            try{
                String params = JSONObject.toJSONString(collect);
                JSONObject body = restPost(restApiProperties.getColourfulServicePath() + "/admin/v1/bankSalesOrder/getOrderInfoByOrderIds", params);
                bankSalesOrderVos = body.getObject("result", List.class);
            }catch (Exception e){
                log.error("商区销售单相关信息查询异常");
            }
        }

        //1.2、在线预约单 mars.reserv_order/reserv_order_detail, 通过feign接口调用
        List<ReservOrderComInfoVo> reservComInfos = null;
        if(orderNos.size() > 0) {
            try {
                CommonResultVo<List<ReservOrderComInfoVo>> comInfoByOrderIds = remoteReservOrderService.getComInfoByOrderIds(orderIdsFromStrToInt(orderNos));
                if (comInfoByOrderIds == null || comInfoByOrderIds.getCode() == 200) {
                    log.error("在线预约单相关信息查询异常");
                } else {
                    reservComInfos = comInfoByOrderIds.getResult();
                }
            } catch (Exception e) {
                log.error("在线预约单相关信息查询异常");
            }
        }

        //1.3、支付宝销售单 ziya.sales_order/sales_order_code/sales_order_item 通过feign接口调用
        List<SalesOrderComInfoVo> saleComInfos = null;
        if(so.size() > 0) {
            try {
                CommonResultVo<List<SalesOrderComInfoVo>> bySalesOrderByIds = remoteSalesOrderService.getBySalesOrderByIds(so);
                if (bySalesOrderByIds == null || bySalesOrderByIds.getCode() == 200) {
                    log.error("支付宝销售单相关信息查询异常");
                } else {
                    saleComInfos = bySalesOrderByIds.getResult();
                }
            } catch (Exception e) {
                log.error("支付宝销售单相关信息查询异常");
            }
        }

        //1.4、支付宝预约单 ziya.book_order_payment
        List<BookOrderPayComInfoVo> zfbReservInfos = null;
        if(bo.size() > 0) {
            try {
                CommonResultVo<List<BookOrderPayComInfoVo>> byBookOrderByIds = remoteBookOrderPaymentService.getByBookOrderPaymentByIds(bo);
                if (byBookOrderByIds == null || byBookOrderByIds.getCode() == 200) {
                    log.error("支付宝预约单相关信息查询异常,{}", byBookOrderByIds);
                } else {
                    zfbReservInfos = byBookOrderByIds.getResult();
                }
            } catch (Exception e) {
                log.error("支付宝预约单相关信息查询异常");
            }
        }
        //END=====================================================


        //START=====================================================
        //2、获取需要的字段输出展示
        List<OutlineRefundInfo> outlineRefundInfoPos = new ArrayList<>();
        //2.1、商区销售订单表
        if(bankSalesOrderVos != null) {

            bankSalesOrderVos.forEach(order -> {
                OutlineRefundInfoReq info = getOutlineRefundRelatedInfo(list, StringUtil.valueOf(order.get("salesOrderId")));

                OutlineRefundInfo outlineRefundInfoPo = new OutlineRefundInfo();
                outlineRefundInfoPo.setOrderNo(StringUtil.valueOf(order.get("salesOrderId")));
                outlineRefundInfoPo.setProductName(null);

                outlineRefundInfoPo.setGoodsName(StringUtil.valueOf(order.get("projectName")));
                outlineRefundInfoPo.setProductGroupId(null);
                outlineRefundInfoPo.setPhoneReciever(info.getPhoneReciever());
                outlineRefundInfoPo.setGoodsId(StringUtil.valueIntOf(order.get("projectId")));

                outlineRefundInfoPo.setMerchantName(null); //商区销售单无商户|酒店信息
                outlineRefundInfoPo.setRefundReciever(info.getRefundReciever());
                outlineRefundInfoPo.setActCode(StringUtil.valueOf(order.get("code")));
                String payTime = StringUtil.valueOf(order.get("payTime"));
                if(payTime == null){
                    outlineRefundInfoPo.setOrderTime(null);
                }else{
                    String s = payTime.replace("T", " ").replaceAll("Z", "");
                    outlineRefundInfoPo.setOrderTime(DateUtil.toDate(s));
                }

                outlineRefundInfoPo.setOrderSource("商区销售单");
                outlineRefundInfoPo.setRefundPrice(info.getRefundPrice());
                outlineRefundInfoPo.setRefundTime(info.getRefundTime()!=null?info.getRefundTime():new Date());
                outlineRefundInfoPo.setRefundStatus(false);
                outlineRefundInfoPo.setRefundChannel(StringUtil.valueOf(order.get("acChannel")));
                outlineRefundInfoPos.add(outlineRefundInfoPo);
            });
        }

        //2.2、在线预约单
        if(reservComInfos != null) {
            reservComInfos.forEach(reserv -> {
                OutlineRefundInfoReq info = getOutlineRefundRelatedInfo(list, String.valueOf(reserv.getOrderId()));

                OutlineRefundInfo outlineRefundInfoPo = new OutlineRefundInfo();
                outlineRefundInfoPo.setOrderNo(StringUtil.valueOf(reserv.getOrderId()));
                outlineRefundInfoPo.setProductGroupId(StringUtil.valueIntOf(reserv.getProductGroupId()));
                outlineRefundInfoPo.setPhoneReciever(info.getPhoneReciever());
                outlineRefundInfoPo.setRefundReciever(info.getRefundReciever());
                outlineRefundInfoPo.setActCode(StringUtil.valueOf(reserv.getActCode()));
                outlineRefundInfoPo.setOrderTime(reserv.getOrderTime());
                outlineRefundInfoPo.setOrderSource("在线预约单");
                outlineRefundInfoPo.setRefundPrice(info.getRefundPrice());
                outlineRefundInfoPo.setRefundTime(info.getRefundTime()!=null?info.getRefundTime():new Date());
                outlineRefundInfoPo.setRefundStatus(false);

                if(reserv.getSaleChannelId() != null) {

                    GoodsChannelRes gcr = null;
                    try {
                        gcr = salesChannelService.findById(reserv.getSaleChannelId());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (gcr != null) {
                        outlineRefundInfoPo.setRefundChannel(gcr.getBankName() + "/" + gcr.getSalesChannelName() + "/" + gcr.getSalesWayName());
                    }
                }

                //商品信息
                Goods goods = goodsService.selectById(reserv.getGoodsId());
                if(goods == null){
                    goods = new Goods();
                }
                outlineRefundInfoPo.setGoodsName(goods.getName());
                outlineRefundInfoPo.setGoodsId(reserv.getGoodsId());

                //产品信息
                ProductItem productItem = productItemService.selectById(reserv.getProductId());
                if(productItem == null){
                    productItem = new ProductItem();
                }
                outlineRefundInfoPo.setProductName(productItem.getName());

                //商户信息
                Shop shop = shopService.selectById(reserv.getShopId());
                if(shop == null){
                    shop = new Shop();
                }
                outlineRefundInfoPo.setMerchantName(shop.getName());

                //酒店信息
                if(shop.getHotelId() != null){
                    Hotel hotel = hotelService.selectById(shop.getId());
                    if(hotel == null){
                        hotel = new Hotel();
                    }
                    outlineRefundInfoPo.setHotelName(hotel.getNameCh());
                }

                outlineRefundInfoPos.add(outlineRefundInfoPo);
            });
        }

        //2.3、支付宝销售单
        if(saleComInfos != null) {

            saleComInfos.forEach(sale -> {
                SalesOrder salesOrder = sale.getSalesOrder();
                SalesOrderCode salesOrderCode = sale.getSalesOrderCode();

                OutlineRefundInfoReq info = getOutlineRefundRelatedInfo(list, salesOrder.getId());

                OutlineRefundInfo outlineRefundInfoPo = new OutlineRefundInfo();
                outlineRefundInfoPo.setOrderNo(salesOrder.getId());
                outlineRefundInfoPo.setProductName(null);
                outlineRefundInfoPo.setProductGroupId(null);
                outlineRefundInfoPo.setPhoneReciever(info.getPhoneReciever());
                outlineRefundInfoPo.setMerchantName(null);
                outlineRefundInfoPo.setRefundReciever(info.getRefundReciever());
                outlineRefundInfoPo.setActCode(salesOrderCode.getActivateCode());
                outlineRefundInfoPo.setOrderTime(salesOrder.getPayTime());
                outlineRefundInfoPo.setOrderSource("支付宝销售单");
                outlineRefundInfoPo.setRefundPrice(info.getRefundPrice());
                outlineRefundInfoPo.setRefundTime(info.getRefundTime()!=null?info.getRefundTime():new Date());
                outlineRefundInfoPo.setRefundStatus(false);

                outlineRefundInfoPo.setRefundChannel("支付宝");
                //商品信息
                Goods goods = goodsService.selectById(salesOrderCode.getProjectId());
                outlineRefundInfoPo.setGoodsName(goods.getName());
                outlineRefundInfoPo.setGoodsId(goods.getId());

                outlineRefundInfoPos.add(outlineRefundInfoPo);
            });
        }

        //2.4、支付宝预约单
        if(zfbReservInfos != null) {

            zfbReservInfos.forEach(sale -> {

                OutlineRefundInfoReq info = getOutlineRefundRelatedInfo(list, sale.getId());

                OutlineRefundInfo outlineRefundInfoPo = new OutlineRefundInfo();
                outlineRefundInfoPo.setOrderNo(sale.getId());
                outlineRefundInfoPo.setProductName(null);
                outlineRefundInfoPo.setProductGroupId(null);
                outlineRefundInfoPo.setPhoneReciever(info.getPhoneReciever());
                outlineRefundInfoPo.setMerchantName(null);
                outlineRefundInfoPo.setRefundReciever(info.getRefundReciever());
//                outlineRefundInfoPo.setActCode(salesOrderCode.getActivateCode());
                outlineRefundInfoPo.setOrderTime(sale.getOrderTime());
                outlineRefundInfoPo.setOrderSource("支付宝销售单");
                outlineRefundInfoPo.setRefundPrice(info.getRefundPrice());
                outlineRefundInfoPo.setRefundTime(info.getRefundTime()!=null?info.getRefundTime():new Date());
                outlineRefundInfoPo.setRefundStatus(false);

                outlineRefundInfoPo.setRefundChannel("支付宝");
                //商品信息
//                Goods goods = goodsService.selectById(sale.getProjectId());
                outlineRefundInfoPo.setGoodsName(null);
                outlineRefundInfoPo.setGoodsId(null);

                outlineRefundInfoPos.add(outlineRefundInfoPo);
            });
        }

        //END=====================================================

        //3、将线下退款数据存库
        outlineRefundInfoPos.forEach(info->{
            if(this.selectById(info.getOrderNo()) == null){
                this.insert(info);
            }
        });

        //4、从线下退款表中分页查询
        OutlineRefundInfoReq outlineRefundInfo = new OutlineRefundInfoReq();
        outlineRefundInfo.setPageNo(1);
        outlineRefundInfo.setPageSize(10);
        return this.queryRefundInfoByConditioin(outlineRefundInfo);
    }

    @Override
    public Boolean exportOutlineRefundInfo(List<OutlineRefundInfoReq> outlineRefundInfos, HttpServletResponse response) {
        try {
            List<String> ids = new ArrayList<>();
            if(outlineRefundInfos != null){
                outlineRefundInfos.forEach(info-> ids.add(info.getOrderNo()));
            }
            EntityWrapper<OutlineRefundInfo> queryWrapper = new EntityWrapper<>();
            queryWrapper
                    .in(!ids.isEmpty(), "order_no", ids)
                    .setSqlSelect("order_no",
                            "concat_ws(' | ',goods_name, product_name) as product_name",
                            "merchant_name",
                            "concat_ws(' | ',refund_reciever, phone_reciever) as refund_reciever",
                            "act_code",
                            "order_time",
                            "order_source",
                            "refund_price",
                            "refund_status",
                            "refund_time",
                            "refund_channel");

            List<OutlineRefundInfo> list = this.selectList(queryWrapper);
            List<OutlineRefundInfoRes> outlineRefundInfoVos = BeanCopierUtils.copyPropertiesOfList(list, OutlineRefundInfoRes.class);
            List<OutlineRefundInfoExport> exps = new ArrayList<>();
            outlineRefundInfoVos.forEach( vo -> {
                OutlineRefundInfoExport ex = new OutlineRefundInfoExport();
                ex.setActCode(vo.getActCode());
                ex.setMerchantName(vo.getMerchantName());
                ex.setOrderNo(vo.getOrderNo());
                ex.setOrderSource(vo.getOrderSource());
                ex.setOrderTime(DateUtil.toString(vo.getOrderTime()));
                ex.setProductName(vo.getProductName());
                ex.setRefundChannel(vo.getRefundChannel());
                ex.setRefundPrice(vo.getRefundPrice());
                ex.setRefundReciever(vo.getRefundReciever());
                ex.setRefundStatus(vo.getRefundStatus()?"已退款":"待退款");
                ex.setRefundTime(DateUtil.toString(vo.getRefundTime(), "yyyyMMdd"));
                exps.add(ex);
            });
            ExcelUtils.writeExcel("export", response, exps, OutlineRefundInfoExport.class);

        } catch (Exception e) {
            return false;
        }

        return true;
    }

    @Override
    public Boolean updateRefundInfo(List<OutlineRefundInfoReq> outlineRefundInfoVos) {

        List<OutlineRefundInfo> outlineRefundInfoPos = BeanCopierUtils.copyPropertiesOfList(outlineRefundInfoVos, OutlineRefundInfo.class);
        outlineRefundInfoPos.forEach(info ->info.setRefundStatus(true));

        //更新状态和时间
        //获取所有订单号
        List<String> orderNos = new ArrayList<>();
        outlineRefundInfoVos.forEach(info -> orderNos.add(info.getOrderNo()));

        //----商区销售单----
        List<String> collect = orderNos.stream().filter(a -> a.contains("-")).collect(Collectors.toList());
        orderNos.removeAll(collect);

        //----支付宝销售单----
        List<String> so = orderNos.stream().filter(a -> a.startsWith("SO")).collect(Collectors.toList());

        //add 支付宝预约单, ziya.book_order_payment
        List<String> bo = orderNos.stream().filter(a -> a.startsWith("BO")).collect(Collectors.toList());
        orderNos.removeAll(bo);
        orderNos.removeAll(so);

        //----商区销售单----
        collect.addAll(orderNos.stream().filter(a -> a.length() > 11).collect(Collectors.toList()));
        //----在线预约单----
        orderNos.removeAll(collect);

        //1.1、colourful.bank_sales_order,               商区销售单, rest修改
//        boolean b_bank_sales = bankSalesOrderService.updateStatusByOrderNos(collect);
        boolean b_bank_sales = false;
        if(collect.size() > 0){
            Map<String, Date> bankSales = getOutlineRefundRelatedInfos(outlineRefundInfoVos, collect);
            String params = JSONObject.toJSONString(bankSales, SerializerFeature.WriteMapNullValue);
            JSONObject body = restPost(restApiProperties.getColourfulServicePath() + "/admin/v1/bankSalesOrder/updateOrderInfoByMap", params);
            b_bank_sales = body.getObject("result", Boolean.class);
        }


        //1.2、mars.reserv_order/reserv_order_detail,    在线预约单
//        String params = JSONObject.toJSONString(orderNos);
//        JSONObject body = restPost(marsServicePath + "/reservOrder/updateReservOrderComInfoByOrderIds", params);
//        boolean b_reserv_order = body.getObject("result", Boolean.class);
        boolean b_reserv_order = false;
        if(orderNos.size() > 0){
            Map<String, Date> reservOrders = getOutlineRefundRelatedInfos(outlineRefundInfoVos, orderNos);
            CommonResultVo<Boolean> booleanCommonResultVo = remoteReservOrderService.updateComInfoByMap(reservOrders);
            if (booleanCommonResultVo == null || booleanCommonResultVo.getCode() == 200) {
                log.error("在线预约单相关信息查询异常");
                b_reserv_order = false;
            }else{
                b_reserv_order = booleanCommonResultVo.getResult();
            }
        }

        //1.4、ziya.book_order_payment,支付宝预约单
//        String params2 = JSONObject.toJSONString(so);
//        JSONObject body2 = restPost(ziyaServicePath + "/salesOrder/updateBySalesOrderByIds", params2);
//        boolean b_sales_order = body2.getObject("result", Boolean.class);
        boolean b_sales_order = false;
        if(so.size() > 0){
            Map<String, Date> salesOrders = getOutlineRefundRelatedInfos(outlineRefundInfoVos, so);
            CommonResultVo<Boolean> booleanCommonResultVo1 = remoteSalesOrderService.updateBySalesOrderByMap(salesOrders);
            if (booleanCommonResultVo1 == null || booleanCommonResultVo1.getCode() == 200) {
                log.error("支付宝销售单相关信息查询异常");
                b_sales_order = false;
            }else{
                b_sales_order = booleanCommonResultVo1.getResult();
            }
        }

        boolean b_book_order_payment = false;
        if(bo.size() > 0){
            Map<String, Date> bookOrderPayments = getOutlineRefundRelatedInfos(outlineRefundInfoVos, bo);
            CommonResultVo<Boolean> booleanCommonResultVo1 = remoteBookOrderPaymentService.updateByBookOrderPaymentByMap(bookOrderPayments);
            if (booleanCommonResultVo1 == null || booleanCommonResultVo1.getCode() == 200) {
                log.error("支付宝预约单相关信息查询异常");
                b_book_order_payment = false;
            }else{
                b_book_order_payment = booleanCommonResultVo1.getResult();
            }
        }

        //1.5、更新本表
        boolean b_local = this.updateBatchById(outlineRefundInfoPos);
        return b_bank_sales && b_reserv_order && b_sales_order && b_book_order_payment && b_local;
    }

    @Override
    public Page<OutlineRefundInfoRes> queryRefundInfoByConditioin(OutlineRefundInfoReq outlineRefundInfo) {

        EntityWrapper<OutlineRefundInfo> queryWrapper = new EntityWrapper<>();
        queryWrapper
                .like(StringUtils.isNotEmpty(outlineRefundInfo.getRefundReciever()),
                        "concat_ws('|', refund_reciever, phone_reciever)",
                        outlineRefundInfo.getRefundReciever())

                .like(StringUtils.isNotEmpty(outlineRefundInfo.getProductName()),
                        "concat_ws(' | ',goods_name, product_name, goods_id, product_group_id)",
                        outlineRefundInfo.getProductName())

                .eq(StringUtils.isNotEmpty(outlineRefundInfo.getOrderSource()),
                        "order_source",
                        outlineRefundInfo.getOrderSource())

                .between(StringUtils.isNotEmpty(outlineRefundInfo.getOrderStartTime()) && StringUtils.isNotEmpty(outlineRefundInfo.getOrderEndTime()),
                        "order_time", outlineRefundInfo.getOrderStartTime(),
                        outlineRefundInfo.getOrderEndTime())

                .between(StringUtils.isNotEmpty(outlineRefundInfo.getRefundStartTime()) && StringUtils.isNotEmpty(outlineRefundInfo.getRefundEndTime()),
                        "refund_time", outlineRefundInfo.getRefundStartTime(),
                        outlineRefundInfo.getRefundEndTime())

                .eq(outlineRefundInfo.getRefundStatus()!=null,
                        "refund_status",
                        outlineRefundInfo.getRefundStatus())
                .setSqlSelect("order_no",
                        "concat_ws(' | ',goods_name, product_name) as product_name",
                        "concat_ws(' | ',merchant_name, hotel_name) as merchant_name",
                        "concat_ws(' | ',refund_reciever, phone_reciever) as refund_reciever",
                        "act_code",
                        "order_time",
                        "order_source",
                        "refund_price",
                        "refund_status",
                        "refund_time",
                        "refund_channel");

        Integer pageNo = outlineRefundInfo.getPageNo();
        Integer pageSize = outlineRefundInfo.getPageSize();

        if(pageNo == null || pageSize==null) {
            pageNo = 1;
            pageSize = 10;
        }
        Page<OutlineRefundInfo> p = new Page<>(pageNo,pageSize);
        Page<OutlineRefundInfo> page = this.selectPage(p, queryWrapper);

        return PageUtil.of(page, OutlineRefundInfoRes.class);
    }

    private OutlineRefundInfoReq getOutlineRefundRelatedInfo(List<OutlineRefundInfoReq> list, String orderId) {
        List<OutlineRefundInfoReq> collect = list.stream().filter(info -> orderId.equals(info.getOrderNo())).collect(Collectors.toList());
        return collect.get(0);
    }

    private Map<String, Date> getOutlineRefundRelatedInfos(List<OutlineRefundInfoReq> list, List<String> orderIds){
        Map<String, Date> rs = new HashMap<>();

        list.forEach( info -> {
            if(orderIds.contains(info.getOrderNo())){
                if(info.getRefundTime() == null){
                    info.setRefundTime(new Date());
                }
                rs.put(info.getOrderNo(), info.getRefundTime());
            }
        });
        return rs;
    }

    private List<Integer> orderIdsFromStrToInt(List<String> orderIds){
        List<Integer> re = new ArrayList<>();
        orderIds.forEach( orderId -> re.add(Integer.valueOf(orderId)));

        return re;
    }

    private JSONObject restPost(String url, String params){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.add("Xe-Auth", restApiProperties.getAuth());
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        HttpEntity<String> entity = new HttpEntity<>(params, headers);
        ResponseEntity<JSONObject> responseEntity = restTemplate.postForEntity(url, entity, JSONObject.class);
        JSONObject body = responseEntity.getBody();
        return responseEntity.getBody();
    }
}
