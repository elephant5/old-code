package com.colourfulchina.mars.service.impl;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.aggregatePay.utils.goodLife.HttpClient;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.mars.api.constant.BoscCardConstant;
import com.colourfulchina.mars.api.entity.*;
import com.colourfulchina.mars.api.enums.BoscCardEnum;
import com.colourfulchina.mars.api.vo.BoscCardSet;
import com.colourfulchina.mars.api.vo.BoscCheckCardVo;
import com.colourfulchina.mars.api.vo.GiftSurplusTimesVo;
import com.colourfulchina.mars.api.vo.req.GetBoscBanksReqVo;
import com.colourfulchina.mars.api.vo.req.QueryBoscCardReqVo;
import com.colourfulchina.mars.api.vo.res.BoscBankCustInfoRes;
import com.colourfulchina.mars.api.vo.res.BoscCardInfoVo;
import com.colourfulchina.mars.api.vo.res.GoodsResVo;
import com.colourfulchina.mars.api.vo.res.ProductGroupResVo;
import com.colourfulchina.mars.api.vo.res.QueryBoscCardResVo;
import com.colourfulchina.mars.mapper.BoscBankLogMapper;
import com.colourfulchina.mars.mapper.EquityCodeDetailMapper;
import com.colourfulchina.mars.mapper.GoodsDetailMapper;
import com.colourfulchina.mars.service.BoscBankLogService;
import com.colourfulchina.mars.utils.BookMinMaxDayUtils;
import com.colourfulchina.mars.utils.BoscDecryptUtils;
import com.colourfulchina.member.api.entity.MemMemberAccount;
import com.colourfulchina.member.api.feign.RemoteMemberAccountService;
import com.colourfulchina.pangu.taishang.api.dto.BookNum;
import com.colourfulchina.pangu.taishang.api.dto.GoodsPortalSettingDto;
import com.colourfulchina.pangu.taishang.api.entity.Goods;
import com.colourfulchina.pangu.taishang.api.feign.RemoteGoodsPortalSettingService;
import com.colourfulchina.pangu.taishang.api.feign.RemoteProductGroupService;
import com.colourfulchina.pangu.taishang.api.vo.res.GoodsGroupListRes;
import com.colourfulchina.pangu.taishang.api.vo.res.GroupQueryOneRes;
import com.colourfulchina.yangjian.api.vo.InnerReq;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.colourfulchina.mars.mapper.BoscBankMapper;
import com.colourfulchina.mars.service.BoscBankService;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Service
@Slf4j
@AllArgsConstructor
public class BoscBankServiceImpl extends ServiceImpl<BoscBankMapper, BoscBank> implements BoscBankService {

    @Autowired
    private BoscBankMapper boscBankMapper;

    @Autowired
    private RemoteProductGroupService remoteProductGroupService;


    @Autowired
    private PanguInterfaceServiceImpl panguInterfaceService;

    @Autowired
    private GoodsDetailMapper goodsDetailMapper;

    @Autowired
    private RemoteGoodsPortalSettingService remoteGoodsPortalSettingService;

    @Autowired
    private EquityCodeDetailMapper equityCodeDetailMapper;


    @Override
    public List<String> getBoscBankTxtList(List<BoscBankTxtEntity> list) throws Exception {
        return boscBankMapper.getBoscBankTxtList(list);
    }

    @Override
    public Integer batchInsert(List<BoscBankTxtEntity> bankList) throws Exception {
        return boscBankMapper.batchInsert(bankList);
    }

    @Autowired
    private RemoteMemberAccountService remoteMemberAccountService;

    @Autowired
    private BoscBankLogService boscBankLogService;

    @Autowired
    private  BoscDecryptUtils boscDecryptUtils;


    /**
     * 根据客编号获取用户所有卡信息
     *
     * @param reqVo ecifNo 客编号
     * @return
     */
    @Override
    public QueryBoscCardResVo getCardList(QueryBoscCardReqVo reqVo) throws Exception {
        if (null == reqVo || StringUtils.isEmpty(reqVo.getEcifNo())) {
            throw new Exception("客编号不能为空");
        }
        QueryBoscCardResVo resVo = new QueryBoscCardResVo();
        //查询此客户下所有的卡片信息
        List<BoscBank> cardList = boscBankMapper.selectCardList(reqVo.getEcifNo());
        if (!CollectionUtils.isEmpty(cardList)) {

            //TODO 检查卡状态
            if(boscDecryptUtils.isValidcard()){
                for(BoscBank boscBank : cardList){
                    BoscCheckCardVo boscCheckCardVo = new BoscCheckCardVo();
                    boscCheckCardVo.setAcctNo(boscBank.getCardNo());
                    boscCheckCardVo.setTranTime(DateUtil.format(new Date(),"MMddHHmmss"));
                    boscCheckCardVo.setLocalDate(DateUtil.format(new Date(),"MMdd"));
                    boscCheckCardVo.setLocalTime(DateUtil.format(new Date(),"HHmmss"));
                    boscCheckCardVo.setSysNo(((int)(Math.random()*9+1)*100000)+"");
                    boscCheckCardVo.setSerNo(DateUtil.format(new Date(),"yyyyMMddHHmmss")+boscCheckCardVo.getSysNo());
                    boscCheckCardVo.setEcifNo(boscBank.getEcif());
                    boscCheckCardVo.setCardNo(boscBank.getLastFourNo());
                    StringBuffer bf  = new StringBuffer("{");
                    bf.append("\"MsgType\"").append(":\"").append(boscCheckCardVo.getMsgType()).append("\",");
                    bf.append("\"AcctNo\"").append(":\"").append(boscCheckCardVo.getAcctNo()).append("\",");
                    bf.append("\"ProCode\"").append(":\"").append(boscCheckCardVo.getProCode()).append("\",");
                    bf.append("\"TranTime\"").append(":\"").append(boscCheckCardVo.getTranTime()).append("\",");
                    bf.append("\"SysNo\"").append(":\"").append(boscCheckCardVo.getSysNo()).append("\",");
                    bf.append("\"LocalTime\"").append(":\"").append(boscCheckCardVo.getLocalTime()).append("\",");
                    bf.append("\"LocalDate\"").append(":\"").append(boscCheckCardVo.getLocalDate()).append("\",");
                    bf.append("\"EcifNo\"").append(":\"").append(boscCheckCardVo.getEcifNo()).append("\",");
                    bf.append("\"CardNo\"").append(":\"").append(boscCheckCardVo.getCardNo()).append("\",");
                    bf.append("\"SerNo\"").append(":\"").append(boscCheckCardVo.getSerNo()).append("\"}");

                    log.info("检查卡状态创建的请求：{}----{}",boscDecryptUtils.getCheckCardUrl(), JSONObject.toJSONString(boscCheckCardVo));
                    String result = HttpClient.httpPost(boscDecryptUtils.getCheckCardUrl(),bf.toString());
                    log.info("检查卡状态返回结果：{}", result);
                    if(result!=null ){
                        JSONObject resultJson = JSONObject.parseObject(result);
                        if(resultJson.containsKey("RetCode") && !resultJson.getString("RetCode").equalsIgnoreCase("00")){
                            boscBank.setStatus(1);
                        }
                    }
                }

            }
            List<BoscCardInfoVo> boscCardInfoVoList = Lists.newArrayList();
            for (BoscBank boscBank : cardList) {
                BoscCardInfoVo boscCardInfoVo = new BoscCardInfoVo();
                List<GoodsResVo> goodsResVoList = Lists.newArrayList();
                List<ProductGroupResVo> productGroupResVoList = Lists.newArrayList();
                BeanUtils.copyProperties(boscBank, boscCardInfoVo);
                boscCardInfoVo.setCardName(BoscCardEnum.getCard(boscBank.getCardProgroupNo()).get("name"));
                boscCardInfoVo.setCardLevel(BoscCardEnum.getCard(boscBank.getCardProgroupNo()).get("type"));
                boscCardInfoVo.setCardShowName(BoscCardEnum.getCard(boscBank.getCardProgroupNo()).get("showName"));
                boscCardInfoVo.setStatus(boscBank.getStatus());
                String goodsIdStr = null;
                if(!StringUtils.isEmpty(reqVo.getFlag()) && 0 == reqVo.getFlag()){
                    goodsIdStr = "391210";
                }else {
                    goodsIdStr = BoscCardEnum.getCard(boscBank.getCardProgroupNo()).get("goodsId");
                }
                if (!StringUtils.isEmpty(goodsIdStr) && !"null".equals(goodsIdStr)) {
                    String[] split = goodsIdStr.split(",");
                    for (String projectId : split) {
                        GoodsResVo goodsResVo = new GoodsResVo();
                        goodsResVo.setGoodsId(Integer.valueOf(projectId));
                        //根据用户ID+项目查询激活码 先获取激活码记录
                        if (!StringUtils.isEmpty(reqVo.getMemberId())) {
                            GiftCode gift = new GiftCode();
                            gift.setMemberId(Long.valueOf(reqVo.getMemberId()));
                            gift.setGoodsId(Integer.valueOf(projectId));
                            List<GiftCode> goodsDetailList = goodsDetailMapper.selectGiftCodeList(gift);
                            if (!CollectionUtils.isEmpty(goodsDetailList)) {
                                goodsResVo.setGiftCodeList(goodsDetailList);
                            }
                        }
                        //根据项目Id获取行权地址短链和行权地址后缀
                        CommonResultVo<GoodsPortalSettingDto> commonResultVo = remoteGoodsPortalSettingService.get(Integer.valueOf(projectId));
                        if (null != commonResultVo && null != commonResultVo.getResult()) {
                            goodsResVo.setShortUrl(commonResultVo.getResult().getShortUrl());
                            goodsResVo.setPrjCode(commonResultVo.getResult().getCode());
                        }
                        //根据项目id(新系统中是goodsId)查询产品组信息
                        CommonResultVo<List<GoodsGroupListRes>> resultVo = remoteProductGroupService.selectGoodsGroupByGoodsId
                                (Integer.valueOf(projectId));
                        if (null != resultVo && !CollectionUtils.isEmpty(resultVo.getResult())) {
                            //查询权益剩余次数  产品组维度
                            EquityCodeDetail equityCodeDetail = new EquityCodeDetail();
                            equityCodeDetail.setMemberId(Long.valueOf(reqVo.getMemberId()));
                            List<GiftSurplusTimesVo> surplusTimesList = equityCodeDetailMapper.getSurplusTimes(equityCodeDetail);
                            Map<Integer, Integer> surplusTimesMap = surplusTimesList.stream().collect(Collectors.toMap(GiftSurplusTimesVo::getProductGroupId, GiftSurplusTimesVo::getSurplusTimes));
                            log.info("surplusTimesMap :{}",JSONObject.toJSONString(surplusTimesMap));
                            productGroupResVoList = resultVo.getResult().stream().map(goodsGroupListRes -> {
                                log.info("goodsGroupListRes:{}",JSONObject.toJSONString(goodsGroupListRes));
                                ProductGroupResVo productGroupResVo = new ProductGroupResVo();
                                BeanUtils.copyProperties(goodsGroupListRes, productGroupResVo);
                                productGroupResVo.setProductGroupId(goodsGroupListRes.getId());
                                //综合最小提前预约天数 和 综合最大提前预约天数
                                try {
                                    Map<String, Integer> days = getDays(Integer.valueOf(projectId), goodsGroupListRes.getId(), goodsGroupListRes.getService());
                                    log.info("days:{}",JSONObject.toJSONString(days));
                                    if (null != days && !days.isEmpty()) {
                                        productGroupResVo.setMinBookDay(days.get("minBookDay"));
                                        productGroupResVo.setMaxBookDay(days.get("maxBookDay"));
                                    }
                                    //设置权益剩余次数
                                    int leftNum = surplusTimesMap.get(goodsGroupListRes.getId());
                                    productGroupResVo.setLeftNum(BigDecimal.valueOf(leftNum));
                                    log.info("productGroupResVo:{}",JSONObject.toJSONString(productGroupResVo));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                return productGroupResVo;
                            }).collect(Collectors.toList());
                            goodsResVo.setProductGroupResVoList(productGroupResVoList);

                        }
                        goodsResVoList.add(goodsResVo);
                    }
                    boscCardInfoVo.setGoodsResVoList(goodsResVoList);
                }
                boscCardInfoVoList.add(boscCardInfoVo);
            }
            //判断用户是否有权益
            for (BoscBank boscBank : cardList) {
                if (BoscCardEnum.getCardProgroupNoList("WORLD").contains(boscBank.getCardProgroupNo())) {
                    resVo.setIfGift(true);
                    break;
                } else if (BoscCardEnum.getCardProgroupNoList("DIAMOND").contains(boscBank.getCardProgroupNo()) && "B".equals(boscBank.getCardType())) {
                    resVo.setIfGift(true);
                    break;
                } else {
                    resVo.setIfGift(false);
                }
            }
            //对卡列表根据等级做排序
            List<String> list = new ArrayList<String>() {
                {
                    add(BoscCardEnum.VISA_INFINITE_CARD.getCardProgroupNo());
                    add(BoscCardEnum.DIAMOND_CREDIT_CARD.getCardProgroupNo());
                    add(BoscCardEnum.UNION_PAY_DIAMOND_CARD.getCardProgroupNo());
                    add(BoscCardEnum.UNION_PAY_DIAMOND_CREDIT_CARD.getCardProgroupNo());
                    add(BoscCardEnum.UNION_PAY_STANDARD_DIAMOND_CREDIT_CARD.getCardProgroupNo());
                }
            };
            boscCardInfoVoList.sort(new Comparator<BoscCardInfoVo>() {
                @Override
                public int compare(BoscCardInfoVo o1, BoscCardInfoVo o2) {
                    return list.indexOf(o1.getCardProgroupNo()) - list.indexOf(o2.getCardProgroupNo());
                }
            });
            resVo.setBoscCardInfoVoList(boscCardInfoVoList);

        }
        return resVo;
    }

    /**
     * 商品中最小提前预约天数 和最大天数
     *
     * @return
     */
    public Map<String, Integer> getDays(Integer goodsId, Integer ProductGroupId, String productGroupService) throws Exception {
        Map<String, Integer> map = new HashMap<>();
        int hour = DateUtil.hour(new Date(), true);
        Goods goods = panguInterfaceService.findGoodsById(goodsId);
        if (null == goods) {
            throw new Exception("商品不存在");
        }
        Integer goodsMinBookDay = goods.getMinBookDays();
        if (hour >= 17) {
            if (goodsMinBookDay != null) {
                goodsMinBookDay = goodsMinBookDay + 1;
            }
        }
        //商品中最大提前预约天数
        Integer goodsMaxBookDay = null;
        if (goodsMinBookDay != null && goods.getMaxBookDays() != null) {
            goodsMaxBookDay = goods.getMaxBookDays() + goodsMinBookDay;
        }
        CommonResultVo<GroupQueryOneRes> resultVo = remoteProductGroupService.findOneById(ProductGroupId);
        GroupQueryOneRes groupQueryOneRes = new GroupQueryOneRes();
        if (null != resultVo) {
            groupQueryOneRes = resultVo.getResult();
        }

        //产品组中最小提前预约天数
        Integer productGroupMinBookDay = groupQueryOneRes.getMinBookDays();
        if (hour >= 17) {
            if (productGroupMinBookDay != null) {
                productGroupMinBookDay = productGroupMinBookDay + 1;
            }
        }
        //产品组中最大提前预约天数
        Integer productGroupMaxBookDay = null;
        if (productGroupMinBookDay != null && groupQueryOneRes.getMaxBookDays() != null) {
            productGroupMaxBookDay = groupQueryOneRes.getMaxBookDays() + productGroupMinBookDay;
        }
        //综合最小提前预约天数
        Integer minBookDay = BookMinMaxDayUtils.maxInteger(goodsMinBookDay, productGroupMinBookDay);
        //综合最大提前预约天数
        Integer maxBookDay = BookMinMaxDayUtils.minInteger(goodsMaxBookDay, productGroupMaxBookDay);
        //获取资源默认的最小最大提前预约时间
        BookNum bookNum = BookMinMaxDayUtils.getBookByService(productGroupService, "CN");
        if (minBookDay == null) {
            minBookDay = bookNum.getMinBook();
        }
        if (maxBookDay == null) {
            maxBookDay = bookNum.getMaxBook();
        }
        map.put("minBookDay", minBookDay);
        map.put("maxBookDay", maxBookDay);
        return map;
    }

    @Override
    public List<BoscBankCustInfoRes> getBoscLinklist(String ecifNo) {
        return boscBankMapper.getBoscLinklist(ecifNo);
    }

    @Override
    public List<BoscBank> getBoscBankList() {
        return boscBankMapper.selectBoscBankList();
    }

    @Override
    public List<BoscBank> batchInsertBoscBank(List<BoscBank> list) {
        boscBankMapper.batchInsertBoscBank(list);
        return list;
    }

    @Override
    public List<BoscBank> getBoscBanksByCondition(GetBoscBanksReqVo reqVo) {
        Assert.notNull(reqVo, "参数不能为空");
        List<BoscBank> resultList = Lists.newArrayList();
        if (1 == reqVo.getType()) {
            //获取数据库中不存在的数据
            //1.查询数据库中已存在的数据
            List<String> ecifList = boscBankMapper.selectBoscBankListByCondition(reqVo.getList());
            //2.去除已存在的数据,得到需要新增的数据
            if (CollectionUtils.isEmpty(ecifList)) {
                resultList = reqVo.getList().stream().map(item -> {
                    BoscBank boscBank = new BoscBank();
                    BeanUtils.copyProperties(item, boscBank);
                    return boscBank;
                }).collect(Collectors.toList());
                return resultList;
            } else {
                Iterator<BoscBankTxtEntity> iterator = reqVo.getList().iterator();
                while (iterator.hasNext()) {
                    BoscBankTxtEntity next = iterator.next();
                    for (String s : ecifList) {
                        if (next.getEcif().equals(s)) {
                            iterator.remove();
                        }
                    }
                }
                resultList = reqVo.getList().stream().map(item -> {
                    BoscBank boscBank = new BoscBank();
                    BeanUtils.copyProperties(item, boscBank);
                    return boscBank;
                }).collect(Collectors.toList());
                return resultList;
            }
        } else if (2 == reqVo.getType()) {
            //获取数据库中存在(未冻结),但是上海银行当天有效数据中不存在的数据
            List<BoscBank> list = boscBankMapper.selectOnceExistBoscBankList(reqVo.getList());
            return list;
        } else if (3 == reqVo.getType()) {
            //以前被冻结账户再次出现在上海银行当天有效数据中的数据
            List<BoscBank> list = boscBankMapper.selectBeforeFreezeBoscBankList(reqVo.getList());
            return list;
        } else if (4 == reqVo.getType()) {
            //以前被冻结账户再次出现在上海银行当天有效数据中,且卡产品组代码一致的数据
            List<BoscBank> list = boscBankMapper.selectBeforeFreezeAndSameCardProgroupNoBoscBankList(reqVo.getList());
            return list;
        } else if (5 == reqVo.getType()) {
            //匹配到相同的ECIF客户号的数据
            return boscBankMapper.selectSameEcifBoscBankList(reqVo.getList());
        } else if (6 == reqVo.getType()) {
            //匹配到相同的ECIF客户号，且卡产品组代码也相同的数据(排除冻结的)
            return boscBankMapper.selectSameEcifAndCardProgroupNoBoscBankList(reqVo.getList());
        } else if (7 == reqVo.getType()) {
            //匹配到相同的ECIF客户号,但是统数据中的卡产品组代码在当天有效数据中不存在
            return boscBankMapper.selectSameEcifNoCardProgroupNoBoscBankList(reqVo.getList());
        } else if (8 == reqVo.getType()) {
            //匹配到相同的ECIF客户号,系统数据中无此产品组代码的数据
            List<String> ecifList = boscBankMapper.selectBoscBankListByCondition(reqVo.getList()); //数据库中存在相同客编号的数据
            //筛选出导入数据中相同客编号的数据
            List<BoscBank> boscBanks = Lists.newArrayList();
            for (BoscBankTxtEntity bankTxtEntity : reqVo.getList()) {
                for (String ecif : ecifList) {
                    if (ecif.equals(bankTxtEntity.getEcif())) {
                        BoscBank boscBank = new BoscBank();
                        BeanUtils.copyProperties(bankTxtEntity, boscBank);
                        boscBanks.add(boscBank);
                    }
                }
            }
            //筛选出导入数据中相同客编号但是无此产品组代码的数据
            List<BoscBank> sameEcifBoscBankList = boscBankMapper.selectSameEcifBoscBankList(reqVo.getList());
            boscBanks.removeAll(sameEcifBoscBankList);
            return boscBanks;
        } else if (9 == reqVo.getType()) {
            //获取原套卡产品下的第一个产品代码的数据
            List<BoscBank> boscBankList = boscBankMapper.selectFirstCardSetBoscBankList(reqVo.getList());
            return boscBankList;
        } else if(10 == reqVo.getType()){
            //查询客编号和卡产品组编号都相同,但是姓名或手机号修改过的数据
            List<BoscBank> updateList = Lists.newArrayList();
            for(BoscBankTxtEntity boscBankTxtEntity : reqVo.getList()){
                BoscBank boscBank = boscBankMapper.selectDiffBoscBank(boscBankTxtEntity);
                if(!StringUtils.isEmpty(boscBank)){
                    boscBank.setName(boscBankTxtEntity.getName());
                    boscBank.setMobile(boscBankTxtEntity.getMobile());
                    updateList.add(boscBank);
                }
            }
            if(!CollectionUtils.isEmpty(updateList)){
                //批量更新账户信息
                this.updateBatchById(updateList);
                List<BoscBankLog> bankLogs = Lists.newArrayList();
                //更新会员信息
                for(BoscBank boscBank : updateList){
                    //更新账户信息
                    MemMemberAccount memMemberAccount = new MemMemberAccount();
                    memMemberAccount.setAcChannel("BOSC");
                    memMemberAccount.setThirdId(boscBank.getEcif());
                    memMemberAccount.setAcName(boscBank.getName());
                    memMemberAccount.setMobile(boscBank.getMobile());
                    remoteMemberAccountService.updateMemAccount(memMemberAccount);

                    BoscBankLog boscBankLog = new BoscBankLog();
                    boscBankLog.setBoscBankId(boscBank.getId());
                    boscBankLog.setOperation("更新账户信息");
                    boscBankLog.setHandleResult("SUCCESS");
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("name",boscBank.getName());
                    jsonObject.put("mobile",boscBank.getMobile());
                    boscBankLog.setBasicUpdateAfter(jsonObject.toJSONString());
                    bankLogs.add(boscBankLog);
                }
                boscBankLogService.batchInsertLog(bankLogs);
            }
            return updateList;
        }
        return null;
    }

    @Override
    public Boolean updateBatchBoscBanks(List<BoscBank> boscBankList) {
        Assert.notNull(boscBankList, "参数不能为空");
        Integer update = boscBankMapper.updateBatchBoscBanks(boscBankList);
        return update > 0 ? true : false;
    }

    @Override
    public Boolean freezeBatchBoscBanks(List<BoscBank> boscBankList, Integer delFlag) {
        Assert.notNull(boscBankList, "参数不能为空");
        //批量更新账户信息
        this.updateBatchById(boscBankList);
        //更新会员信息
        for(BoscBank boscBank : boscBankList){
            //更新账户信息
            MemMemberAccount memMemberAccount = new MemMemberAccount();
            memMemberAccount.setAcChannel("BOSC");
            memMemberAccount.setThirdId(boscBank.getEcif());
            memMemberAccount.setAcName(boscBank.getName());
            memMemberAccount.setMobile(boscBank.getMobile());
            remoteMemberAccountService.updateMemAccount(memMemberAccount);
        }
        Integer update = boscBankMapper.freezeBatchBoscBanks(boscBankList, delFlag);
        return update > 0 ? true : false;
    }

    @Override
    public BoscCardSet getCardSetInfo(String cardNo) {
        BoscBank boscBank = new BoscBank();
        boscBank.setCardNo(cardNo);
        boscBank.setDelFlag(BoscCardConstant.NORMAL_STATUS);//查询未删除的
        BoscBank cardInfo = boscBankMapper.selectOne(boscBank);
        log.info("根据卡号查询出的信息:{}",JSONObject.toJSONString(cardInfo));
        Assert.notNull(cardInfo, "查询的信息不存在或者已删除！！！");
        BoscCardSet boscCardSet = new BoscCardSet();
        if (BoscCardConstant.SUB_CARD_TYPE.equals(cardInfo.getCardType())) {
            String mainCardNo = cardInfo.getMainCardNo();
            boscBank = new BoscBank();
            boscBank.setDelFlag(BoscCardConstant.NORMAL_STATUS);//查询未删除的
            boscBank.setCardNo(mainCardNo);//查询未删除的
            cardInfo = boscBankMapper.selectOne(boscBank);
            Assert.notNull(cardInfo, "查询的主卡信息不存在或者已删除！！！");
        }
        boscCardSet.setMasterCard(cardInfo);
        Map<String, Object> params = new HashMap<>();
        params.put("del_flag", BoscCardConstant.NORMAL_STATUS);
        params.put("main_card_no", cardInfo.getCardNo());
        List<BoscBank> boscBanks = boscBankMapper.selectByMap(params);
        boscCardSet.setSubCards(boscBanks);
        log.info("getListByCardNo返回结果:{}",JSONObject.toJSONString(boscCardSet));
        return boscCardSet;
    }


}
