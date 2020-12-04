package com.colourfulchina.mars.service.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.mars.api.entity.GiftCode;
import com.colourfulchina.mars.api.vo.req.ActCodeReq;
import com.colourfulchina.mars.api.vo.req.BrokerCouponsUsedReq;
import com.colourfulchina.mars.api.vo.req.OutCodeReq;
import com.colourfulchina.mars.config.MqscProperties;
import com.colourfulchina.mars.service.BrokerService;
import com.colourfulchina.mars.service.GiftCodeService;
import com.colourfulchina.mars.service.PanguInterfaceService;
import com.colourfulchina.mars.utils.*;
import com.colourfulchina.pangu.taishang.api.vo.res.GoodsChannelRes;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.*;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Slf4j
public class BrokerServiceImpl implements BrokerService {

    @Value("${mqsc.uid}")
    private int uid;
    @Autowired
    private MqscProperties mqscProperties;
    @Autowired
    private PanguInterfaceService panguInterfaceService;
    @Autowired
    private GiftCodeService giftCodeService;

    @SuppressWarnings("rawtypes")
    @Autowired
    protected RedisTemplate redisTemplate;

    private String generatorBroker(String fileFlag, List<List<String>> lines){
        String[] split = fileFlag.split(",");
        String type = split[0];
        String systemName = split[1];
        String flag = split[2];
        String td = split[3];

        String fileName = String.format("%s.%s.%s.00.%s", type, systemName, td, flag);


        String sourcePath = mqscProperties.getLocalSavePath() + "source/" + fileName;
        File file = new File(mqscProperties.getLocalSavePath() + "source/");
        if(!file.exists()){
            file.mkdirs();
        }

        List<String> wds = new ArrayList<>();

        for(List<String> line: lines){
            String join = String.join(" |#| ", line);
            join = join + " |#| ";
            wds.add(join);
        }

        // 最后一行填写总行数
        wds.add("TLRL" + String.format("%1$015d",lines.size()));
        //生成源文件
        try {
            FileUtils.writeLines(new File(sourcePath), "GBK", wds);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //zip压缩源文件
        String zipName = String.format("%s.ZIP", fileName);
        String zipPath = mqscProperties.getLocalSavePath() + "zip/" + zipName;
        File file1 = new File(mqscProperties.getLocalSavePath() + "zip/");
        if(!file1.exists()){
            file1.mkdirs();
        }

        ZipUtils.fileToZip(sourcePath, zipPath);

        //pgp加密
        String pgpName = String.format("%s.DAT",zipName);
        String pgpPath = mqscProperties.getLocalSavePath() + "pgp/" + pgpName;
        File file2 = new File(mqscProperties.getLocalSavePath() + "pgp/");
        if(!file2.exists()){
            file2.mkdirs();
        }
        try {
            PgpEncryUtil.Encry(zipPath, pgpPath, mqscProperties.getLocalSavePath() + "dsfpublic.asc");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pgpPath + "," + pgpName;
    }

    /**
     * 获取激活码
     * @return
     */
    private CommonResultVo<List<GiftCode>> getCode(String waresId,String tod){

        int goodsId = mqscProperties.getGoodsId();
        int actCodeNum = mqscProperties.getActCodeNum();

        CommonResultVo<List<GiftCode>> result = new CommonResultVo();
        try {
            //获取销售渠道
            List<GoodsChannelRes> goodsChannelResList = panguInterfaceService.selectGoodsChannel(goodsId);
            Assert.notEmpty(goodsChannelResList,"商品的销售渠道不能为空");
            //第一步，生成激活码
//            ActCodeReq actCodeReq = new ActCodeReq();
//            actCodeReq.setActCodeNum(actCodeNum);
//            actCodeReq.setGoodsId(goodsId);
//            actCodeReq.setRemarks(waresId);
//
//            List<GiftCode> codeList = giftCodeService.generateActCode(actCodeReq);
//

            Wrapper<GiftCode> local = new Wrapper<GiftCode>() {
                @Override
                public String getSqlSegment() {
                    return " WHERE goods_id =10280   and member_id is null  and remarks ='"+tod+"'  ";
                }
            };
            List<GiftCode> list = giftCodeService.selectList(local);
//            //第二步，出库激活码
//
//            OutCodeReq outCodeReq = new OutCodeReq();
//            outCodeReq.setGoodsId(goodsId);
//            outCodeReq.setRemarks(waresId);
//
//            outCodeReq.setSalesChannelId(goodsChannelResList.get(0).getId());
//            outCodeReq.setCodes(codeList.stream().map(giftCode -> giftCode.getActCode()).collect(Collectors.toList()));
//            List<GiftCode> list = giftCodeService.outActCodeByCodes(outCodeReq,null);
            result.setResult(list);
        }catch (Exception e){
            log.error("发码失败",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    private String get2byteStr(String str, int formantLen){
        String format = "%1$-"+formantLen+"s";

        byte[] utf8 = str.getBytes(Charset.forName("GBK"));
        String bbiso = new String(utf8, StandardCharsets.ISO_8859_1);
        String cc = String.format(format, bbiso);

        byte[] ddiso = cc.getBytes(StandardCharsets.ISO_8859_1);
        return new String(ddiso, Charset.forName("GBK"));
    }

    private String getGoodsInfoFile(String goodsUid, String merchantId, Date cur,String tod) {

        List<String> line = new ArrayList<>();
        line.add(goodsUid); //商品唯一编码
//        line.add(get2byteStr("高端酒店住宿一间夜权益", 40)); //商品中文名称
//        line.add(get2byteStr("高端酒店住宿一间夜(VISA持卡人专享)", 40)); //商品中文名称
        line.add(get2byteStr("高端酒店住宿一间夜(万事达持卡人专享)", 40));
        line.add("00"); // 商品所属大类
        line.add("0000"); // 商品所属子类
        line.add(merchantId); //可使用商户ID
        line.add(String.format("%1$11s","0.00")); // 价格
        line.add(String.format("%1$11s","0")); // 积分
        line.add("2020-05-14"); // 有效期开始时间
        line.add("2020-11-30"); // 有效期结束时间
//        line.add(DateUtil.format(cur, "yyyy-MM-dd")); // 有效期开始时间
//        line.add(DateUtil.format(DateUtil.offsetMonth(cur, 12), "yyyy-MM-dd")); // 有效期结束时间
        line.add("0"); // 秒杀标识
        line.add(get2byteStr("【活动来源】中行持卡人跨境消费达标送礼活动，获赠 “高端酒店住宿一间夜”电子券；",200));
        line.add(get2byteStr("【有效期】有效期至2020-11-30，每张电子券只能使用一次，请在有效期内使用，逾期作废，不可兑换现金，除房价外在酒店产生的额外费用需要您到酒店前台支付；",200));
        line.add(get2byteStr("【兑换方式】登录缤纷生活APP-我的-我的礼券-我的优惠券-高端酒店住宿一间夜，点击兑换券描述中的链接：https://icolourful.com/ec16781f，进入兑换页面，输入券码，并根据页面提示进行对换操作；",200));
        line.add(get2byteStr("【温馨提示】使用权益需提前在手机端、网站，或致电客乐芙客服热线400 921 6918（住宿）进行预约，住宿需至少提前7天、最多提前2个月，预订成功将会收到确认短信，限本人预订使用，免费入住权益不可同时或连续使用；",200));
        line.add(get2byteStr("【除外责任】如客户通过非中国银行自有渠道获得该电子券，中国银行对其相关服务概不负责；",200));
        line.add(get2byteStr("客乐芙（上海静元信息技术有限公司）保留随时更换相关商户的权力；",200));
//        line.add(get2byteStr("活动来源：中行跨境消费达标送礼活动",200)); // 商品描述1 客乐芙商品ID
//        line.add(get2byteStr("商品名称：高端酒店住宿一间夜权益",200)); // 商品描述2 客乐芙商品名称
//        line.add(get2byteStr("激活方式：激活码",200)); // 商品描述3 客乐芙商品激活方式
//        line.add(get2byteStr("行权地址：https://icolourful.com/ec16781f", 200)); // 商品描述4 行权短链
//        line.add(get2byteStr("使用权益需提前在手机端、网站，或致电客乐芙客服热线4006368008（住宿）进行预约，住宿需至少提前7天、最多提前2个月，预订成功将会收到确认短信，限本人预订使用。免费入住权益不可同时或连续使用。",200)); // 商品描述5
//        line.add(get2byteStr("订房需视酒店指定房型之入住情况而定，如酒店客房订满，则安排同级别酒店入住。所有酒店指定房型一次可住2人，已包括房费、税费及相关服务费。不可兑换现金，除房价外在酒店产生的额外费用需要您到酒店前台支付",200)); // 商品描述6
//        line.add(get2byteStr("客乐芙为您完成住宿权益预定后，恕不接受取消/更改，若无法预期使用，该次权益将视作已使用, 权益一旦激活绑定，将不得转让会员资格和优惠，也不可随意取消购买或退货，如有特殊情况可致电客服中心协商处理。",200)); // 商品描述7
//        line.add(get2byteStr(" 客乐芙（上海静元信息技术有限公司）保留随时更换相关商户的权力。该会籍由客乐芙（上海静元信息技术有限公司）提供服务支持，客乐芙（上海静元信息技术有限公司）对其所提供的礼遇保留解释权。",200)); // 商品描述8
        line.add(get2byteStr("详细细则请以用户登录页使用细则为准！", 200)); // 商品描述7
        line.add(String.format("%1$-200s","")); // 商品描述8
        line.add(String.format("%1$-200s","")); // 商品描述9
        line.add(String.format("%1$-200s","")); // 商品描述10
        line.add("F"); // 预留域1
        line.add(""); // 是否支持实时退货
        line.add(""); // 预留域3

        List<List<String>> lines = new ArrayList<>();
        lines.add(line);
        log.info("getGoodsInfoFile:{}",JSONObject.toJSON(lines));
        String fileFlag = "WARES,KLFG,P," + tod;
        return generatorBroker(fileFlag, lines);
    }

    private String getMerchantInfoFile(Boolean isFirst,String merchantId, String tod) {
        List<List<String>> lines = new ArrayList<>();

        if(isFirst) {

            List<String> line = new ArrayList<>();
            line.add(merchantId); //商户id
            line.add("000000"); //所在省ID
            line.add("000000"); //所在城市ID
            line.add("000000"); //所在城区ID
            line.add(get2byteStr("客乐芙中国", 200)); //商户名称
            line.add(get2byteStr("全国通用", 200)); //门店名称
            line.add(get2byteStr("客乐芙专注提供奢华超值的礼遇、权益和优惠，为客户的生活、商务和旅行带来绚丽体验和美好未来。住宿权益覆盖全国62座城市，近160家高端酒店。酒店品牌包括：希尔顿、喜来登、香格里拉、洲际等国际高端品牌。"
                    , 200)); //商户简介
            line.add(get2byteStr("上海浦东杨高南路428号由由世纪广场9楼", 200)); //门店地址
            line.add(String.format("%1$-20s", "4006368008")); //门店电话
            line.add(String.format("%1$-100s", "00:00:00-23:59:59")); //门店营业时间
            line.add(String.format("%1$-100s", "http://www.colourfulchina.com")); //商户网址
            line.add(String.format("%1$-50s", "121.54")); //经度
            line.add(String.format("%1$-50s", "31.22")); //纬度
            line.add("PKLFG00001"); //图片ID
            line.add("Y"); //门店开关
            line.add(merchantId); // 预留域1 商户唯一ID, 门店ID唯一
            line.add(""); // 预留域2
            line.add(""); // 预留域3

            lines.add(line);
        }
        log.info("getMerchantInfoFile:{}",JSONObject.toJSON(lines));
        String fileFlag = "MER,KLFG,P," + tod;
        return generatorBroker(fileFlag, lines);

    }

    private String getCouponInfoFile(List<GiftCode> codes, String goodsUid, String tod) {
        List<List<String>> lines = new ArrayList<>();
        codes = codes == null ? Lists.newArrayList() :codes;
        for(GiftCode code: codes){
            List<String> line = new ArrayList<>();

            line.add(goodsUid); //商品唯一编码
            //生成库存编码
            line.add(String.format("%s00", code.getId())); //库存编码 该商品的唯一编码、商品ID与库存编码组成唯一值
            line.add("E"); //优惠券标识
            line.add(String.format("%1$-36s",code.getActCode())); //优惠券码串
            line.add(""); // 预留域1
            line.add(""); // 预留域2
            line.add(""); // 预留域3

            lines.add(line);
        }
        log.info("getCouponInfoFile Size:{}",lines.size());
        String fileFlag = "COUPON,KLFG,P," + tod;
        return generatorBroker(fileFlag, lines);
    }

    private String getCouponUseInfoFile(List<GiftCode> codes, String goodsUid, String tod,String merchantId) {
        List<List<String>> lines = new ArrayList<>();
        codes = codes == null ? Lists.newArrayList() :codes;
        for(GiftCode code: codes){
            List<String> line = new ArrayList<>();
            line.add(goodsUid); //商品唯一编码

            line.add(String.format("%s00",code.getId())); //库存编码 该商品的唯一编码、商品ID与库存编码组成唯一值
            line.add("E"); //优惠券标识
            line.add(String.format("%1$-36s",code.getActCode())); //优惠券码串
            if(code.getActCodeTime() == null){
                continue;
            }
            line.add(DateUtil.format(code.getActCodeTime(), "yyyyMMdd")); //使用时间
            line.add(merchantId); //使用商户ID
            line.add(""); // 预留域1
            line.add(""); // 预留域2
            line.add(""); // 预留域3

            lines.add(line);
        }
        log.info("getCouponUseInfoFile:{}",JSONObject.toJSON(lines));
        String fileFlag = "CUSED,KLFG,U," + tod;
        return generatorBroker(fileFlag, lines);
    }

    private String picInfo(String goodsUid, String curDate){

        //zip压缩源文件
        String zipName = String.format("PIC.KLFG.%s.00.P.ZIP", curDate);
        String zipPath = mqscProperties.getLocalSavePath() + "zip/" + zipName;
        String sourcePath = mqscProperties.getLocalSavePath() + "images/";

        ZipUtils.fixFileName(new File(sourcePath), "WKLFG", goodsUid+".png");
        ZipUtils.fileToZip(sourcePath, zipPath);

        //pgp加密
        String pgpName = String.format("%s.DAT",zipName);
        String pgpPath = mqscProperties.getLocalSavePath() + "pgp/" + pgpName;
        try {
            PgpEncryUtil.Encry(zipPath, pgpPath, mqscProperties.getLocalSavePath() + "dsfpublic.asc");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pgpPath + "," + pgpName;
    }

    @Override
    public void batchUploadData(String merchantId){

        Integer uuid = (Integer)redisTemplate.opsForValue().get("WKLFG:ZONGHANG");
        if(mqscProperties.getIsReset() || uuid == null){ //将商品id序列存入redis中，避免项目重启，又得配置序列; isReset：是否重置序列
            uuid = uid;
        }
        String goodsUid ="WKLFG" + String.format("%1$06d",uuid);

        Date date = new Date();
        String tod = mqscProperties.getDateStr();
        log.info("goodsUid:{}----tod(DateStr):{}",JSONObject.toJSON(goodsUid),tod);
        if(StringUtils.isEmpty(mqscProperties.getDateStr()) || tod.equalsIgnoreCase("null") ){
            tod =DateUtil.format(date, "yyyyMMdd");
        }
        CommonResultVo<List<GiftCode>> commonResultVo = getCode(goodsUid,tod);
        List<GiftCode> records = commonResultVo.getRecords();
        List<String> paths = new ArrayList<>();
        log.info("-----------getCouponInfoFile---------------优惠券数据：{}",records.size());
        //生成优惠券文件
        paths.add(getCouponInfoFile(records,goodsUid, tod));
        log.info("-----------getCouponUseInfoFile---------------");
        //生成优惠券更新文件
        paths.add(getCouponUseInfoFile(records,goodsUid, tod,merchantId));
        log.info("-----------picInfo---------------");
//        //图片包
        paths.add(picInfo(goodsUid,tod));
        log.info("-----------getGoodsInfoFile---------------");
//        //生成商品文件
        paths.add(getGoodsInfoFile(goodsUid, merchantId, date,tod));
        log.info("-----------getMerchantInfoFile---------------");
//        //生成商户文件
        paths.add(getMerchantInfoFile(mqscProperties.getIsFirst(), merchantId, tod));
        log.info("-----------paths{}---------------", JSONObject.toJSON(paths));
        if(mqscProperties.getIsUpload()){
            log.info("-----------SftpUtils---------------");
            //文件上传
            SftpUtils sftpCustom = new SftpUtils(mqscProperties.getRemoteHost(), mqscProperties.getRemotePort(), mqscProperties.getRemoteUser(), mqscProperties.getRemotePriKeyPath(), mqscProperties.getRemotePath());
//            String sourcePath = mqscProperties.getLocalSavePath() + "pgp/";
//            List<String> pathsTMP = getFiles(sourcePath);
            if(!CollectionUtils.isEmpty(paths)){
                log.info("-----------pathsTMP.size{}---------------",paths.size());
                for(String path : paths){
                    String[] split = path.split(",");
                    log.info("-----------path---------------{}", JSONObject.toJSON(split));
                    if(!sftpCustom.upload(split[0], split[1])){
                        log.info(path + "上传失败");
                    }
                }
            }

            sftpCustom.closeChannel();
        }
//        String sourcePath = mqscProperties.getLocalSavePath() + "pgp/";
//        List<String> paths = getFiles(sourcePath);
//        if(mqscProperties.getIsUpload()){
//            log.info("-----------SftpUtils---------------");
//            //文件上传
//            SftpUtils sftpCustom = new SftpUtils(mqscProperties.getRemoteHost(), mqscProperties.getRemotePort(), mqscProperties.getRemoteUser(), mqscProperties.getRemotePriKeyPath(), mqscProperties.getRemotePath());
//            for(String path : paths){
//                String[] split = path.split(",");
//                log.info("-----------path---------------{}", JSONObject.toJSON(split));
//                if(!sftpCustom.upload(split[0], split[1])){
//                    log.error(split[0] + "上传失败");
//                }
//            }
//            sftpCustom.closeChannel();
//        }
        log.info("-----------WKLFG---------------{}",new Date());
        uuid += 1;
        redisTemplate.opsForValue().set("WKLFG:ZONGHANG", uuid);
    }

    /**
     * @Author：
     * @Description：获取某个目录下所有直接下级文件，不包括目录下的子目录的下的文件，所以不用递归获取
     * @Date：
     */
    public static List<String> getFiles(String path) {
        List<String> files = new ArrayList<String>();
        File file = new File(path);
        File[] tempList = file.listFiles();

        for (int i = 0; i < tempList.length; i++) {
            if (tempList[i].isFile()) {
                String fileName = tempList[i].getName();
                files.add( tempList[i].toString()+ "," + fileName);
                //文件名，不包含路径
//
//                files.add(fileName);
            }
        }
        return files;
    }



    @Override
    public Boolean couponUsed(BrokerCouponsUsedReq couponsUsedReq){
        String s = JSONObject.toJSONString(couponsUsedReq);
        try {
            log.info("mqscProperties.getPubKey: {},{}", s,mqscProperties.getPubKey());
            String encryptStr = RSAUtil.encodeForString(s, mqscProperties.getPubKey());
            log.info("encryptStr: {}", encryptStr);
//            String res = HttpClientUtils.httpsPostForm(url, params);
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            String url =mqscProperties.getCouponUpdateUrl();
            MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
            map.add("requestType", "S");
            map.add("data", encryptStr);
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
            ResponseEntity<String> response = restTemplate.postForEntity( url, request , String.class );
//            String res = HttpClientUtils.httpsPostForm("https://mlife.jf365.boc.cn/CouponsMall/couponUsed.do", params);
            log.info("response: {}", response);
            if(response !=null &&  response.getBody()!= null){
                JSONObject jsonObject = JSONObject.parseObject(response.getBody());
                if(jsonObject != null && jsonObject.containsKey("stat") && "00".equals(jsonObject.getString("stat"))){
                    return true;
                }
            }

        } catch (Exception e) {
            log.error("couponUsed",e);
            e.printStackTrace();
        }
        return false;
    }

//    public static void main(String args[]){
//        try {
//            String key  ="3081CF300D06092A864886F70D01010105000381BD003081B90281B100B4B3701C4419013380CDF6F852A7F91B2A14CAFACA8A08C19A16E4628997C44D431935AB11F55A6E5E106AA41DA42672C59256C53251A58820589A2DE830F8884589E94EB587800FD7748F17824D797092A88D7B8AB16398E85E7F296F151AC828E75179D70F8CC1D42D3429BFA44835E1C5280FAA424781199480626442E9945AC953F4EFB5BEF940C893C0795C8ED240F1D7CD279DF184EE7EAA7EEB82FBA6018D7A1B6EA2DC883777F747376D3E830203010001";
//            String s ="{\"usedDate\":\"20200531\",\"usedTime\":\"15:49:57\",\"wEid\":\"1063238000\",\"wInfo\":\"584619364444\",\"wSign\":\"E\",\"waresId\":\"WKLFG000019\"}";
//            String encryptStr = RSAUtil.encodeForString(s, key);
//            Map<String, String> params = new HashMap<>();
//            params.put("requestType", "S");
//            params.put("data", encryptStr);
//            log.info("encryptStr: {}", encryptStr);
//
//
//            RestTemplate restTemplate = new RestTemplate();
//            HttpHeaders headers = new HttpHeaders();
//            String url ="https://mlife.jf365.boc.cn/CouponsMall/couponUsed.do";
//            MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
//            map.add("requestType", "S");
//            map.add("data", encryptStr);
//
//            org.springframework.http.HttpEntity<MultiValueMap<String, String>> request = new org.springframework.http.HttpEntity<MultiValueMap<String, String>>(map, headers);
//
//            ResponseEntity<String> response = restTemplate.postForEntity( url, request , String.class );
////            String res = HttpClientUtils.httpsPostForm("https://mlife.jf365.boc.cn/CouponsMall/couponUsed.do", params);
//            log.info("couponUsed: {}", response);
//            JSONObject jsonObject = JSONObject.parseObject(response.getBody());
//            if(jsonObject.containsKey("stat") && "00".equals(jsonObject.getString("stat"))){
//                System.out.println("接口访问成功，执行成功");
//            }else{
//                System.out.println("接口访问失败，执行失败");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
