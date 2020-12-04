package com.colourfulchina.mars.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.colourfulchina.aggregatePay.enums.PayEnum;
import com.colourfulchina.aggregatePay.enums.PayOrderStatusEnum;
import com.colourfulchina.colourfulCoupon.api.entity.CpnThirdCode;
import com.colourfulchina.colourfulCoupon.api.enums.CpnVouchersStatusEunm;
import com.colourfulchina.colourfulCoupon.api.enums.ThirdSourceEnum;
import com.colourfulchina.colourfulCoupon.api.feign.RemoteCouponCommonService;
import com.colourfulchina.colourfulCoupon.api.enums.ThirdSourceEnum;
import com.colourfulchina.colourfulCoupon.api.feign.RemoteThirdCouponsService;
import com.colourfulchina.colourfulCoupon.api.vo.req.ReceiveCpnThirdCodeReqVO;
import com.colourfulchina.colourfulCoupon.api.vo.req.UpdateCpnReqVo;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.mars.api.entity.ReservOrder;
import com.colourfulchina.mars.api.vo.req.CouponThirdCodeReqVO;
import com.colourfulchina.mars.api.vo.res.ThirdCouponsProductInfoResVO;
import com.colourfulchina.mars.api.vo.res.ThirdCouponsResVO;
import com.colourfulchina.mars.config.CpnProperties;
import com.colourfulchina.mars.mapper.ReservOrderMapper;
import com.colourfulchina.mars.service.CouponsService;
import com.google.common.collect.Lists;
import io.netty.util.internal.StringUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class CouponsServiceImpl implements CouponsService {

    @Autowired
    private RemoteThirdCouponsService remoteThirdCouponsService;


    @Autowired
    private CpnProperties cpnProperties;

    RemoteCouponCommonService remoteCouponCommonService;

    @Autowired
    private ReservOrderMapper reservOrderMapper;

    /**
     * 给用户发放第三方券
     * @param reqVO
     * @return
     */
    @Override
    public CpnThirdCode putThirdCoupons(CouponThirdCodeReqVO reqVO) throws Exception {
        if(null == reqVO || null == reqVO.getProductNo()){
            throw new Exception("参数不能为空");
        }
        if(null != reqVO.getReserveOrderId()){
            ReservOrder reservOrder = reservOrderMapper.selectById(reqVO.getReserveOrderId());
            if(null != reservOrder){
                if(null != reservOrder.getPayAmount() && reservOrder.getPayAmount().compareTo(new BigDecimal(0)) != 0){
                    Assert.isTrue(PayOrderStatusEnum.PREPAID.getCode() == reservOrder.getPayStatus(),"预约单未支付,不可以领券");
                }
                reqVO.setAcId(reservOrder.getMemberId());
                reqVO.setMobile(reservOrder.getGiftPhone());
                reqVO.setAcName(reservOrder.getGiftName());
            }
        }
        //第三方券分两种形式:1.先批量把第三方券导入再发给用户的;2.通过第三方直接给用户发券,发券成功后再把券信息入库的
        if(ThirdSourceEnum.GSCGDSFQM.getCode().equalsIgnoreCase(reqVO.getSource())){
            ReceiveCpnThirdCodeReqVO receiveCpnThirdCodeReqVO = new ReceiveCpnThirdCodeReqVO();
            BeanUtils.copyProperties(reqVO,receiveCpnThirdCodeReqVO);
            receiveCpnThirdCodeReqVO.setSource(reqVO.getSource());
            receiveCpnThirdCodeReqVO.setBatchId(Long.valueOf(reqVO.getProductNo()));
            CommonResultVo<CpnThirdCode> resultVo = remoteThirdCouponsService.receiveCpnThirdCode(receiveCpnThirdCodeReqVO);
            if(null != resultVo && 200 == resultVo.getCode()){
                throw new Exception(resultVo.getMsg());
            }
            if(null != resultVo && null != resultVo.getResult()){
                //领券成功把第三方券表id入预约单库
                if(null != reqVO.getReserveOrderId()){
                    ReservOrder reservOrder = new ReservOrder();
                    reservOrder.setId(reqVO.getReserveOrderId());
                    reservOrder.setThirdCpnNo(resultVo.getResult().getId());
                    reservOrderMapper.updateById(reservOrder);
                }
                return resultVo.getResult();
            }
        }else if(ThirdSourceEnum.YSKQ.getCode().equalsIgnoreCase(reqVO.getSource())){
            //银商发券
            String service = "productDelivery";
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String timestamp = sdf.format(date);
            String reqNo = reqVO.getSource()+"_"+System.currentTimeMillis();
            StringBuffer s= new StringBuffer();
            s.append("{\"orderItems\":[{\"productNo\":\"");
            s.append(reqVO.getProductNo());
            s.append("\",\"productNum\":");
            s.append(reqVO.getNum());
            s.append("}],\"reqNo\":\"");
            s.append(reqNo);
            s.append("\"}");
            String bizContent = s.toString();
            String sign = this.getSign(cpnProperties.getPrivateKey(), cpnProperties.getApiKey(), service, timestamp, bizContent);
            StringBuffer params = new StringBuffer();
            params.append("timestamp=");
            params.append(timestamp);
            params.append("&service=");
            params.append(service);
            params.append("&apiKey=");
            params.append(cpnProperties.getApiKey());
            params.append("&sign=");
            params.append(sign);
            params.append("&bizContent=");
            params.append(bizContent);
            HttpPost httpPost = new HttpPost(cpnProperties.getYsApiUrl());
            CloseableHttpClient client = HttpClients.createDefault();
            String respContent = null;
            StringEntity entity = new StringEntity(params.toString());
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/x-www-form-urlencoded");
            httpPost.setEntity(entity);
            try {
                HttpResponse response = client.execute(httpPost);
                if(response.getStatusLine().getStatusCode()== 200) {
                    HttpEntity httpEntity = response.getEntity();
                    respContent = EntityUtils.toString(httpEntity,"UTF-8");
                    if(null != respContent){
                        JSONObject object = JSONObject.parseObject(respContent);
                        if("00".equals(object.getString("code")) && "SUCCESS".equals(object.getString("status"))){
                            String productDetails = object.getString("productDetail");
                            List<ThirdCouponsResVO> goodsBaseVoList = JSONObject.parseArray(productDetails, ThirdCouponsResVO.class);
                            List<CpnThirdCode> cpnThirdCodeList = Lists.newArrayList();
                            if(!CollectionUtils.isEmpty(goodsBaseVoList)){
                                cpnThirdCodeList = goodsBaseVoList.stream().map(thirdCouponsResVO -> {
                                    CpnThirdCode thirdCode = new CpnThirdCode();
                                    thirdCode.setThirdCodeNum(thirdCouponsResVO.getReqNo());
                                    //商品券号；如果商品类型是短接，该字段存放短链接；商品类型是卡密，该字段存放卡券号 -> 券码密码
                                    if("SHORTURL".equals(thirdCouponsResVO.getProductType())){
                                        thirdCode.setShortUrl(thirdCouponsResVO.getProductAuthCode());
                                    }else {
                                        thirdCode.setThirdCodePassword(thirdCouponsResVO.getProductAuthCode());
                                    }
                                    thirdCode.setBatchId(reqVO.getBatchId());
                                    //第三方券默认领取即已使用
                                    thirdCode.setStatus(CpnVouchersStatusEunm.ALREADY_USE.getValue());
                                    thirdCode.setAcId(reqVO.getAcId());
                                    thirdCode.setAcName(reqVO.getAcName());
                                    thirdCode.setMobile(reqVO.getMobile());
                                    thirdCode.setReceiveTime(new Date());
                                    thirdCode.setThirdProductNo(thirdCouponsResVO.getProductNo());
                                    thirdCode.setThirdCodeNum(reqNo);
                                    thirdCode.setValidStartTime(DateUtil.parse(thirdCouponsResVO.getValidStartTime(), "YYYYMMDDhhmmss"));
                                    //denomination 面额；单位：分
                                    thirdCode.setExperTime(DateUtil.parse(thirdCouponsResVO.getValidEndTime(),"YYYYMMDDhhmmss"));
                                    thirdCode.setCreateTime(new Date());
                                    thirdCode.setWorth(new BigDecimal(thirdCouponsResVO.getDenomination()).divide(new BigDecimal(100)));
                                    thirdCode.setCouponsType(thirdCouponsResVO.getProductType());
                                    return thirdCode;
                                }).collect(Collectors.toList());

                                //默认每次只可以领一张券
                                ReceiveCpnThirdCodeReqVO receiveCpnThirdCodeReqVO = new ReceiveCpnThirdCodeReqVO();
                                BeanUtils.copyProperties(cpnThirdCodeList.get(0),receiveCpnThirdCodeReqVO);
                                receiveCpnThirdCodeReqVO.setSource(reqVO.getSource());
                                receiveCpnThirdCodeReqVO.setThirdCodeNum(cpnThirdCodeList.get(0).getThirdCodeNum());
                                receiveCpnThirdCodeReqVO.setThirdProductNo(reqVO.getProductNo());
                                CommonResultVo<CpnThirdCode> resultVo = remoteThirdCouponsService.receiveCpnThirdCode(receiveCpnThirdCodeReqVO);
                                if(null != resultVo && null != resultVo.getResult()){
                                    log.info("银商渠道提取商品信息成功{}");
                                    //领券成功把第三方券表id入预约单库
                                    if(null != reqVO.getReserveOrderId()){
                                        ReservOrder reservOrder = new ReservOrder();
                                        reservOrder.setThirdCpnNo(resultVo.getResult().getId());
                                        reservOrder.setId(reqVO.getReserveOrderId());
                                        reservOrderMapper.updateById(reservOrder);
                                    }
                                    return resultVo.getResult();
                                }
                            }
                        }else {
                            log.error(object.getString("msg"));
                            throw new Exception("银商渠道提取商品信息失败");
                        }
                    }
                }
            }catch (Exception e){
                throw new Exception("银商渠道提取商品信息失败");
            }
        }

        return null;
    }

    /**
     * 银商渠道
     * 查询第三方上架的可售卖的商品信息
     * @return
     */
    @Override
    public List<ThirdCouponsProductInfoResVO> getThirdProductInfo() {
        String service = "queryProductInfo";
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String timestamp = sdf.format(date);
        String isPage = "N";  //Y-分页，N-不分页
        StringBuffer s= new StringBuffer();
        s.append("{\"isPage\":\"");
        s.append(isPage);
        s.append("\"}");
        String bizContent = s.toString();
        try {
            String sign = this.getSign(cpnProperties.getPrivateKey(), cpnProperties.getApiKey(), service, timestamp, bizContent);
            StringBuffer params = new StringBuffer();
            params.append("timestamp=");
            params.append(timestamp);
            params.append("&service=");
            params.append(service);
            params.append("&apiKey=");
            params.append(cpnProperties.getApiKey());
            params.append("&sign=");
            params.append(sign);
            params.append("&bizContent=");
            params.append(bizContent);
            HttpPost httpPost = new HttpPost(cpnProperties.getYsApiUrl());
            CloseableHttpClient client = HttpClients.createDefault();
            String respContent = null;
            StringEntity entity = new StringEntity(params.toString());
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/x-www-form-urlencoded");
            httpPost.setEntity(entity);
            try {
                HttpResponse response = client.execute(httpPost);
                if(response.getStatusLine().getStatusCode()== 200) {
                    HttpEntity httpEntity = response.getEntity();
                    respContent = EntityUtils.toString(httpEntity,"UTF-8");
                    if(null != respContent){
                        JSONObject object = JSONObject.parseObject(respContent);
                        if("00".equals(object.getString("code")) && "success".equals(object.getString("msg"))){
                            String productInfos = object.getString("productInfos");
                            if(!StringUtils.isEmpty(productInfos)){
                                List<ThirdCouponsProductInfoResVO> thirdCouponsProductInfoResVOList = JSONObject.parseArray(productInfos, ThirdCouponsProductInfoResVO.class);
                                return  thirdCouponsProductInfoResVOList;
                            }
                        }else {
                            log.error("银商渠道查询商品信息失败");
                            throw new Exception("银商渠道查询商品信息失败");
                        }
                    }
                }
            }catch (Exception e){
                throw new Exception("银商渠道查询商品信息失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 修改券状态
     * @param reqVo
     * @return
     */
    @Override
    public Boolean updateCoupon(UpdateCpnReqVo reqVo) {
        try {
            CommonResultVo<Boolean> commonResultVo = remoteCouponCommonService.updateCpn(reqVo);
            if(null != commonResultVo){
                return commonResultVo.getResult();
            }else {
                return false;
            }
        } catch (Exception e) {
            log.error("更新券信息失败{}",e.getMessage());
            return false;
        }
    }

    /**
     * 对请求url、POST或GET提交的所有参数（sign除外），使用加密秘钥进行加密。
     * 由于计算签名和参数顺序有关，所以计算sign前需要先对请求参数值（加密秘钥参与排序）进行字符串从小到大的排序。
     */
    private String getSign(String privateKey,String apiKey, String service, String timestamp, String bizContent)
            throws Exception {
        String[] values = new String[] { privateKey, apiKey, service, timestamp, bizContent };
        Collections.sort(Arrays.asList(values));
        return DigestUtils.md5Hex(StringUtils.join(values)).toUpperCase();
    }
}
