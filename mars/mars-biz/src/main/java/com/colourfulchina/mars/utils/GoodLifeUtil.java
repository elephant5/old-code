package com.colourfulchina.mars.utils;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.colourfulchina.aggregatePay.utils.goodLife.*;
import com.colourfulchina.aggregatePay.utils.goodLife.enums.EnumCertificateType;
import com.colourfulchina.aggregatePay.utils.goodLife.enums.EnumCipherAlgorithm;
import com.colourfulchina.aggregatePay.utils.goodLife.enums.EnumKeyAlgorithm;
import com.colourfulchina.aggregatePay.utils.goodLife.enums.EnumKeyStoreType;
import com.colourfulchina.aggregatePay.vo.req.GoodLifeCommReq;
import com.colourfulchina.aggregatePay.vo.req.GoodLifeReq;
import com.colourfulchina.mars.api.config.GoodLifeConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import javax.crypto.SecretKey;
import java.util.*;

/**
 * @Author: 罗幸
 * @Description:
 * @Date: 2020/7/14 15:29
 */
@Slf4j
public class GoodLifeUtil {
    public static GoodLifeReq createReqHead(String reqBodyPlain, GoodLifeConfig config) throws Exception {
        GoodLifeCommReq head = new GoodLifeCommReq();
        String reqBody, signature, encodeKey;
        /*
         * Step1
         */
        SecretKey AESKey = KeyUtil.generateKey(EnumKeyAlgorithm.AES, 128);
        /*
         * Step 2 AES对reqBody加密;
         */
        byte[] encrypted = CipherUtil.encrpty(EnumCipherAlgorithm.AES_ECB_PKCS5Padding, AESKey, reqBodyPlain.getBytes("utf-8"));
        //加密后的请求体数据
        reqBody = Base64Util.encode2Str(encrypted, "UTF-8");

        /*
         * Step 3 对AESKey进行加密,并将加密结果转为16进制字符串
         */
        CertificateUtil certificateUtil = new CertificateUtil(EnumCertificateType.X509, config.getPublicKeyPath());
        KeyStoreUtil keyStoreUtil = new KeyStoreUtil(EnumKeyStoreType.PKCS12, config.getMerchKeyPath(), config.getMerchKeyPassword());

        //Step 4 使用商户证书对加密的reqBody签名
        String transDateTime = DateUtil.format(new Date(), DatePattern.NORM_DATETIME_PATTERN);

        String signStr = "{\"transDateTime\":\"" + transDateTime + "\",\"reqBody\":\"" + reqBody + "\"}";

        byte[] signRes = keyStoreUtil.sign(null, config.getMerchKeyPassword(), signStr.getBytes(), "SHA256withRSA");

        //签名信息
        signature = Base64Util.encode2Str(signRes, "UTF-8");

        //用银行证书公钥对密钥进行加密
        encodeKey = Base64Util.encode2Str(certificateUtil.encrptyByPublicKey(AESKey.getEncoded()), "UTF-8");


        //封装对象
        BeanUtils.copyProperties(config, head);
        head.setEncodeKey(encodeKey);
        head.setSignature(signature);
        head.setTransDateTime(DateUtil.format(new Date(), DatePattern.NORM_DATETIME_PATTERN));
        head.setJnlNo("CLF-" + getRandomStr(15));
        GoodLifeReq req = new GoodLifeReq();
        req.setReqHead(head);
        req.setReqBody(reqBody);
        return req;
    }

    /*
     * 发送请求
     * */
    public static String sendRequest(String url, String reqBodyPlain, GoodLifeConfig config) throws Exception {
        log.info("util sendRequest : url :{},reqBodyPlan:{},config:{}", url, reqBodyPlain, JSONObject.toJSONString(config));
        GoodLifeReq req = createReqHead(reqBodyPlain, config);
        log.info("创建的请求：{}", JSONObject.toJSONString(req));
        String result = HttpClient.httpPost(url, JSONObject.toJSONString(req));
        log.info("返回：{}", result);
        JSONObject resultJson = JSONObject.parseObject(result);
        String dealResult = dealResponse(resultJson, config);
        return dealResult;
    }

    /*
     * 解密
     * */
    private static String dealResponse(Map<String, Object> resMap, GoodLifeConfig config) throws Exception {
        Map<String, Object> resHead = (Map<String, Object>) resMap.get("resHead");
        //AES key加密串
        String encodeKey = (String) resHead.get("encodeKey");
        //AES加密后的请求体
        String resBody = (String) resMap.get("resBody");
        //请求体签名信息
        String signature = (String) resHead.get("signature");
        String transDateTime = (String) resHead.get("transDateTime");

        CertificateUtil certificateUtil = new CertificateUtil(EnumCertificateType.X509, config.getPublicKeyPath()); //银行证书公钥
        KeyStoreUtil keyStoreUtil = new KeyStoreUtil(EnumKeyStoreType.PKCS12, config.getMerchKeyPath(), config.getMerchKeyPassword());//商户证书

        byte[] decodedSignature = Base64Util.decode2Bytes(signature, "UTF-8");

        //1.3 decode reqBody
        byte[] decodedReqBody = Base64Util.decode2Bytes(resBody, "UTF-8");

        String signStr = "{\"transDateTime\":\"" + transDateTime + "\",\"resBody\":\"" + resBody + "\"}";
        //使用银行公钥证书验签
        System.out.println("verify result:" + certificateUtil.verify(signStr.getBytes(), decodedSignature, "SHA256withRSA"));

        //2,解密密钥  用商户证书私钥解密
        byte[] decodedKey = keyStoreUtil.decryptByPrivateKey(null, config.getMerchKeyPassword(), Base64Util.decode2Bytes(encodeKey, "UTF-8"));
        //解密后的AES key
        log.info("decoded key:{}", NumberUtil.bytesToStrHex(decodedKey));
        //3,解密报文
        String decryptedReqBody = new String(CipherUtil.decrypt(EnumCipherAlgorithm.AES_ECB_PKCS5Padding, decodedKey, decodedReqBody), "UTF-8");
        log.info("解密后的报文：{}", decryptedReqBody);
        return decryptedReqBody;
    }

//    public static void main(String[] args) throws Exception {
//        String configStr = "{\"accessTokenUrl\":\"http://203.156.238.218:10005/oauth/oauth2/access_token\",\"appID\":\"SCUR4BgJB6\",\"baseTokenUrl\":\"http://203.156.238.218:10005/oauth/oauth2/base_token\",\"channelFlag\":\"000\",\"clientId\":\"SCUR4BgJB6\",\"format\":\"json\",\"grantType\":\"authorization_code\",\"host\":\"http://47.96.101.167/\",\"instID\":\"315310018001004\",\"loginRedirectUri\":\"\",\"merchKeyPassword\":\"000000\",\"merchKeyPath\":\"D:\\\\work\\\\文档\\\\上海银行 新\\\\导出\\\\test.pfx\",\"publicKeyPath\":\"D:\\\\work\\\\文档\\\\上海银行 新\\\\导出\\\\bankofshanghai_test.cer\",\"signMethod\":\"sha256\",\"version\":\"1.0\"}";
//        GoodLifeConfig config = JSONObject.parseObject(configStr, GoodLifeConfig.class);
////        String s = JSONObject.toJSONString(createReqHead(tk(), config));
////        System.out.println(s);
//        String s = "{\"resBody\":\"F7qYj4FNqh0A9PhkCnRgDV1XBKl6tIL0fa2niq0NwpTNzJ+uD6l6LEY8A1QRVFb8hgMEQzNbO1oGvFFEEfTGdcKIR1GgSXst7UnTiWpV9XfghBT45KGOKzYk7UPA0TcAGkHKBs2Kl7S3DN63ocZSSA==\",\"resHead\":{\"encodeKey\":\"IeGK0KDmQwRyqmSii6LosFXfp0IA9Md2Qmauu5KoEaFd8wGJXAhGSD563YWKrwfNCWjbJbNlKZSWg1Hw2b6t1xBO7IB8Wy+gmta9g17X864XZlJDHYHpA7XRdXWj3RwVowkJ7d+zuDPwsKN38LTl2Y2V3V1805V+c7ptiQl6bdo=\",\"hostDateTime\":\"20200813 10:33:05\",\"hostJnlNo\":\"1293737304695418886\",\"responseCode\":\"000000\",\"responseMsg\":\"\",\"signature\":\"ha/z7+noX2esERqT4IY87etnpga5b/B0lZ+Xn0VqDj3yNDftD/lmUbR8n0kg6TrKs4aXznj4mJ13m1IpSpT5FgNP/EFc3LgcCJ2oChdMKoxA013qaagTWu7tfizB2UPdaT9rAidwQILZJRNA5j7m+CvVi8ob7hyNTlg2yFTaSJ/nxCmb9BUAVvR8Bq3z7+M5Eozku2ug6U5NMcF9mQ7tSwMP7w8pMKWBdBDYLQlxoCT6X90lkpJCi7Lb0P3p84MAaPSF5McgMZJx/NM67mfIpoYEg8vpHVFB5HIpdXQ8FuIwE5gtNjd6FQIU3bQamOM9OxvyKDW7TcTTcepsug/HOg==\",\"transDateTime\":\"2020-08-13 10:33:05\"}}";
//        System.out.println(dealResponse(JSONObject.parseObject(s), config));
//    }

    private static String query() {
        Map map = new HashMap();
        map.put("merchantTransJnl", "query-10001");
        map.put("merchantDate", DateUtil.format(new Date(), DatePattern.PURE_DATE_PATTERN));
        map.put("orgMerchantTransJnl", "clf-20200813095452866");
        map.put("orgMerchantDate", DateUtil.format(new Date(), DatePattern.PURE_DATE_PATTERN));
        map.put("transType", "A");
        String reqBodyPlain = JSONObject.toJSONString(map);
        System.out.println(reqBodyPlain);
        return reqBodyPlain;
    }

    private static String tk() {
        Map map = new HashMap();
        map.put("orderNo", "clf-20200813095452866order");
        map.put("merchantTransJnl", "clftk-0007");
        map.put("merchantDate", DateUtil.format(new Date(), DatePattern.PURE_DATE_PATTERN));
        map.put("merchantTime", DateUtil.format(new Date(), DatePattern.PURE_TIME_PATTERN));
        map.put("transJnl", "20200813100509000000000001");
        map.put("transDate", "20200813");
        map.put("refundAmt", "100");
        List<Map> list = new ArrayList<>();
        Map subMap = new HashMap();
        subMap.put("commodityCode", "001");
        subMap.put("commodityNum", "1");
        subMap.put("commodityPerAmt", "100");
        subMap.put("commodityPerPoint", "0");
        subMap.put("supplierCode", "1");
        subMap.put("supplierName", "客乐芙");
        subMap.put("orderNo", "clf-20200813095452866order");
        subMap.put("orderDate", "20200813");
        subMap.put("orderTime", DateUtil.format(new Date(), DatePattern.PURE_TIME_PATTERN));
        subMap.put("orderAmt", "100");
        list.add(subMap);
        map.put("subOrders", list);
        String reqBodyPlain = JSONObject.toJSONString(map);
        System.out.println(reqBodyPlain);
        return reqBodyPlain;
    }

    public static String getRandomStr(int length) {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        return uuid.substring(0, length);
    }
}
