package com.colourfulchina.mars.service.impl;


import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.mars.api.entity.EquityCodeDetail;
import com.colourfulchina.mars.api.entity.GiftCode;
import com.colourfulchina.mars.api.vo.GoodsClauseList;
import com.colourfulchina.mars.api.vo.MyGiftCodeVo;
import com.colourfulchina.mars.api.vo.ProjectGroupGiftVo;
import com.colourfulchina.mars.api.vo.req.QueryGoodsDetailReqVo;
import com.colourfulchina.mars.api.vo.res.GoodsInfoResVo;
import com.colourfulchina.mars.mapper.GoodsDetailMapper;
import com.colourfulchina.mars.service.EquityCodeDetailService;
import com.colourfulchina.mars.service.GoodsDetailService;
import com.colourfulchina.pangu.taishang.api.dto.GoodsPortalSettingDto;
import com.colourfulchina.pangu.taishang.api.dto.SysFileDto;
import com.colourfulchina.pangu.taishang.api.entity.GoodsClause;
import com.colourfulchina.pangu.taishang.api.entity.ShopItemNetPriceRule;
import com.colourfulchina.pangu.taishang.api.entity.SysService;
import com.colourfulchina.pangu.taishang.api.enums.FileTypeEnums;
import com.colourfulchina.pangu.taishang.api.feign.*;
import com.colourfulchina.pangu.taishang.api.vo.GroupProductVo;
import com.colourfulchina.pangu.taishang.api.vo.req.GoodsBaseVo;
import com.colourfulchina.pangu.taishang.api.vo.req.ListSysFileReq;
import com.colourfulchina.pangu.taishang.api.vo.req.SelectBookPayReq;
import com.colourfulchina.pangu.taishang.api.vo.req.SelectPriceByTimeReqVo;
import com.colourfulchina.pangu.taishang.api.vo.res.GoodsClauseRes;
import com.colourfulchina.pangu.taishang.api.vo.res.GoodsGroupListRes;
import com.colourfulchina.pangu.taishang.api.vo.res.bookBasePayment.BookBasePaymentRes;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Slf4j
@Service
public class GoodsDetailServiceImp implements GoodsDetailService {

    @Autowired
    private GoodsDetailMapper goodsDetailMapper;

    private final RemoteGoodsService remoteGoodsService;

    private final RemoteProductGroupService remoteProductGroupService;

    private final RemoteGoodsPortalSettingService remoteGoodsPortalSettingService;

    @Autowired
    private RemoteSysFileService remoteSysFileService;

    @Autowired
    private RemoteProductGroupProductService remoteProductGroupProductService;

    @Autowired
    private RemoteShopItemNetPriceService remoteShopItemNetPriceService;
    @Autowired
    private EquityCodeDetailService equityCodeDetailService;

    /**
     * 根据电话号查询是否存在权益
     *
     * @param gift
     * @return flag(true : 存在, false : 不存在)
     */
    public boolean checkGiftByid(GiftCode gift) {
        boolean flag = true;
        //1.根据用户ID+项目查询激活码 先获取激活码记录
        Integer finalGoodsId = gift.getGoodsId();
        long memberId = gift.getMemberId();
        Wrapper<GiftCode> wrapper = new Wrapper<GiftCode>() {
            @Override
            public String getSqlSegment() {
                StringBuffer sb = new StringBuffer();
                sb.append("where 1=1 ");
                if(memberId > 0){
                    sb.append("and member_id = '"+memberId+"'");
                }
                if(finalGoodsId !=null){
                    sb.append("and goods_id = '"+ finalGoodsId +"'");
                }
                return sb.toString();
            }
        };
        List<GiftCode> goodsDetailList = goodsDetailMapper.selectList(wrapper);
        //List<GiftCode> giftTableList = goodsDetailMapper.checkGiftByid(gift);
        if (goodsDetailList.size() == 0 || gift == null) {
            flag = false;
        }
        return flag;
    }

    @Override
    public List<GiftCode> selectGiftCodeList(GiftCode gift) {
        List<GiftCode> giftTableList = goodsDetailMapper.selectGiftCodeList(gift);
        return giftTableList;
    }

    /**
     * @throws
     * @Title: getGoodsDetail
     * @Description: 商品详情
     * @author: nickal.zhang
     * @date: 2019年5月20日 下午6:54:41
     * @param: @param actCode
     * @param: @return
     * @param: @throws Exception
     */
    @Override
    public List<MyGiftCodeVo> getGoodsDetail(String actCode) throws Exception {
        log.info("参数actCode={}", actCode);
        long start = System.currentTimeMillis();
        if (actCode == null) {
            throw new Exception("参数为空");
        }
        // 1.先获取激活码记录
        List<GiftCode> goodsDetailList = goodsDetailMapper.getGoodsDetail(actCode);
        if (CollectionUtils.isEmpty(goodsDetailList)) {
            throw new Exception("权益不存在");
        }
        // 2.根据权益的项目id
        // TODO 调用资源的接口查询商品信息
        List<MyGiftCodeVo> result = Lists.newArrayList();
        for (GiftCode giftCode : goodsDetailList) {
            CommonResultVo<GoodsBaseVo> remoteResult = remoteGoodsService.selectById(giftCode.getGoodsId());
            if (remoteResult.getResult() == null) {
                throw new Exception("商品不存在");
            }
            MyGiftCodeVo vo = new MyGiftCodeVo();
            // 赋值
            vo.setProjectName(remoteResult.getResult().getName());
            // 商品图片
            if(!remoteResult.getResult().getFileDtoList().isEmpty()) {
                SysFileDto sysFile = remoteResult.getResult().getFileDtoList().get(0);
                vo.setImgUrl(sysFile.getPgCdnHttpUrl() + '/' + sysFile.getGuid() + '.' + sysFile.getExt());
            }
            // 3.使用限制
            GoodsClause goodsClause = new GoodsClause();
            goodsClause.setGoodsId(giftCode.getGoodsId());
            goodsClause.setDelFlag(0);
            CommonResultVo<List<GoodsClauseRes>> goodsClauseList = remoteGoodsService.selectClauseList(goodsClause);
            if (goodsClauseList.getResult() == null) {
                throw new Exception("使用限制不存在");
            }
            if (!CollectionUtils.isEmpty(goodsClauseList.getResult())) {
                List<GoodsClauseList> resultList = new ArrayList<>();
                for (GoodsClauseRes goodsclause : goodsClauseList.getResult()) {
                    GoodsClauseList gcl = new GoodsClauseList();
                    gcl.setClause(goodsclause.getClause());
                    gcl.setClauseType(goodsclause.getClauseType());
                    gcl.setClauseTypeName(goodsclause.getClauseTypeName());
                    resultList.add(gcl);
                }
                vo.setGoodsclauseList(resultList);
            }

            // 4.产品组信息
            CommonResultVo<List<GoodsGroupListRes>> remoteGroup = remoteProductGroupService.selectGoodsGroup(giftCode.getGoodsId());
            if (remoteGroup.getResult() == null) {
                throw new Exception("产品组不存在");
            }
            if (!CollectionUtils.isEmpty(remoteGroup.getResult())) {
                List<ProjectGroupGiftVo> list = new ArrayList<>();
                ProjectGroupGiftVo projectGroupGiftVo = null;
                for (GoodsGroupListRes goodsGroupListRes : remoteGroup.getResult()) {
                    //赋值
                    projectGroupGiftVo = new ProjectGroupGiftVo();
                    projectGroupGiftVo.setTitle(goodsGroupListRes.getName());
                    //根据每一个产品组id查询使用次数和总次数
                    projectGroupGiftVo.setGroupId(goodsGroupListRes.getId());
                    projectGroupGiftVo.setMinBookDays(goodsGroupListRes.getMinBookDays());
                    projectGroupGiftVo.setMaxBookDays(goodsGroupListRes.getMaxBookDays());

                    //查询产品组的资源类型
                    CommonResultVo<List<SysService>> sysService = remoteProductGroupService.selectGroupService(goodsGroupListRes.getId());
                    if (sysService.getResult() == null) {
                        throw new Exception("资源类型不存在");
                    }
                    if (!CollectionUtils.isEmpty(sysService.getResult())) {
                        for (SysService sysservice : sysService.getResult()) {
                            projectGroupGiftVo.setSysService(sysservice.getCode());
                        }
                    }
                    EquityCodeDetail order = new EquityCodeDetail();
                    order.setGiftCodeId(giftCode.getId());
                    order.setProductGroupId(goodsGroupListRes.getId());
                    // 查询权益总次数
                    List<EquityCodeDetail> equityCodeDetails = goodsDetailMapper.getGoodsTimes(order);
                    if(equityCodeDetails != null){
                        // 查询权益已使用次数
                        //int useTimes = this.getGiftUseTimes(order);
                        //(新改造  需要通过当前日期获取可以预定日期的次数)
                        for(EquityCodeDetail detail : equityCodeDetails) {
                            if(detail.getStartTime() != null && detail.getEndTime() != null){
                                String start_time = DateUtil.format(detail.getStartTime(), "yyyy-MM-dd");
                                String end_time = DateUtil.format(detail.getEndTime(), "yyyy-MM-dd");
                                String book_date = DateUtil.format(new Date(),"yyyy-MM-dd");
                                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                                try {
                                    Date dt1 = df.parse(start_time);
                                    Date dt2 = df.parse(end_time);
                                    Date dt3 = df.parse(book_date);
                                    if (dt3.getTime() >= dt1.getTime() && dt3.getTime() <= dt2.getTime()) {
                                        //新增加当期使用次数 useCount
                                        //一共使用次数newUseCount surplusTimes为剩余次数
                                        Integer newUseCount = 0;
                                        newUseCount =  goodsDetailMapper.getNewUseCount(giftCode.getId(),goodsGroupListRes.getId());
                                        projectGroupGiftVo.setUseCount(detail.getUseCount());//使用次数
                                        projectGroupGiftVo.setSurplusTimes(detail.getTotalCount() - newUseCount);//剩余次数
                                        projectGroupGiftVo.setSurplusFreeTimes((detail.getTotalFreeCount()==null?0:detail.getTotalFreeCount())-(detail.getUseFreeCount()==null?0:detail.getUseFreeCount()));//剩余免费次数
                                        projectGroupGiftVo.setCycleCount(detail.getCycleCount());//周期内总次数
                                        projectGroupGiftVo.setTotalTimes(detail.getTotalCount());//总次数
                                        projectGroupGiftVo.setStartTime(DateUtil.format(detail.getStartTime(),"yyyy-MM-dd"));
                                        projectGroupGiftVo.setEndTime(DateUtil.format(detail.getEndTime(),"yyyy-MM-dd"));
                                        projectGroupGiftVo.setType("cycle_type");
                                    }
                                }catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                            }else {
                                //正常不是周期重复的数据 直接返回
                                //  不是无限制项目返回
                                if(detail.getTotalCount() != null) {
                                    projectGroupGiftVo.setSurplusTimes(detail.getTotalCount() - detail.getUseCount());
                                    projectGroupGiftVo.setSurplusFreeTimes((detail.getTotalFreeCount()==null?0:detail.getTotalFreeCount())-(detail.getUseFreeCount()==null?0:detail.getUseFreeCount()));//剩余免费次数
                                    projectGroupGiftVo.setTotalTimes(detail.getTotalCount());
                                }
                            }
                        }
                    }
                }
                vo.setGiftList(list);
            }
            //权益到期时间
            vo.setExpiryDate(giftCode.getActExpireTime());
            result.add(vo);
        }
        System.out.println(System.currentTimeMillis()-start);
        return result;
    }

    /**
     * 根据产品组id查询产品组资源信息
     * @param groupId
     * @return
     */
    @Override
    public List<SysService> selectGroupInfo(Integer groupId) throws Exception {
        if(null == groupId){
            throw new Exception("参数不能为空");
        }
        CommonResultVo<List<SysService>> vo = remoteProductGroupService.selectGroupService(groupId);
        List<SysService> result = Lists.newArrayList();
        if(null != vo){
            result = vo.getResult();
        }
        return result;
    }

	@Override
	public List<MyGiftCodeVo> getGoodsList(Long memberId, String prjCode, String actCode) throws Exception {
		// 定义返回集
        List<MyGiftCodeVo> listGift = new ArrayList<MyGiftCodeVo>();
        // 根据prjcode查询
        Integer goodsId = null;
        String bankLogo = null;
        //String bankDefaltLogo = null;
        if(prjCode!=null && !"".equals(prjCode)) {
        	CommonResultVo<GoodsPortalSettingDto> common = remoteGoodsPortalSettingService.getByCode(prjCode);
            if(common.getResult()!=null) {
            	if(common.getResult().getGoodsId()==null) {
            		throw new Exception("项目不存在");
            	}
            	goodsId = common.getResult().getGoodsId();
            	if(common.getResult().getSysBankLogo()!=null && common.getResult().getSysBankLogo().getSysFileDto()!=null) {
            		bankLogo = common.getResult().getSysBankLogo().getSysFileDto().getPgCdnNoHttpFullUrl();
            	}
            }	
        }
        //1.根据用户ID+项目查询激活码 先获取激活码记录
        Integer finalGoodsId = goodsId;
        Wrapper<GiftCode> wrapper = new Wrapper<GiftCode>() {
            @Override
            public String getSqlSegment() {
                StringBuffer sb = new StringBuffer();
                sb.append("where 1=1 and goods_id != '428'");
                if(actCode!=null){
                    sb.append("and act_code = '"+actCode+"'");
                }
                if(memberId!=null){
                    sb.append("and member_id = '"+memberId+"'");
                }
                if(finalGoodsId !=null){
                    sb.append("and goods_id = '"+ finalGoodsId +"'");
                }
                return sb.toString();
            }
        };
        List<GiftCode> goodsDetailList = goodsDetailMapper.selectList(wrapper);
        // 2.1查询商品
        CommonResultVo<GoodsBaseVo> remoteResult = new CommonResultVo<GoodsBaseVo>();
        // 2.2使用限制
        CommonResultVo<List<GoodsClauseRes>> goodsClauseList = new CommonResultVo<List<GoodsClauseRes>>();
        List<GoodsClauseList> resultList = new ArrayList<>();
        // 2.3产品组信息
        CommonResultVo<List<GoodsGroupListRes>> remoteGroup = new CommonResultVo<List<GoodsGroupListRes>>();
        // 遍历
        for (GiftCode giftCode : goodsDetailList) {
        	// 返回对象
        	MyGiftCodeVo vo = new MyGiftCodeVo();
        	vo.setUnitId(giftCode.getId());
        	vo.setStatus(giftCode.getActCodeStatus());
        	vo.setActCode(giftCode.getActCode());
        	// 如果goodsId不为空，商品接口和产品组信息 只需调用一次
        		// 2.1查询商品
            remoteResult = remoteGoodsService.selectById(giftCode.getGoodsId());
            if (remoteResult.getResult() == null) {
                throw new Exception("商品不存在");
            }
            // 2.2.使用限制
            GoodsClause goodsClause = new GoodsClause();
            goodsClause.setGoodsId(giftCode.getGoodsId());
            goodsClause.setDelFlag(0);
            goodsClauseList = remoteGoodsService.selectClauseList(goodsClause);
            if (!CollectionUtils.isEmpty(goodsClauseList.getResult())) {
                for (GoodsClauseRes goodsclause : goodsClauseList.getResult()) {
                    GoodsClauseList gcl = new GoodsClauseList();
                    gcl.setClause(goodsclause.getClause());
                    gcl.setClauseType(goodsclause.getClauseType());
                    gcl.setClauseTypeName(goodsclause.getClauseTypeName());
                    resultList.add(gcl);
                }
            }
                // 2.3.产品组信息
            List<EquityCodeDetail> equityCodeDetailsList = equityCodeDetailService.selectByEquityByGoodsId(giftCode.getGoodsId(), giftCode.getId());
            Set<Integer> productGroupIds = equityCodeDetailsList.stream().map(equityCodeDetail -> equityCodeDetail.getProductGroupId()).collect(Collectors.toSet());
            if (!CollectionUtils.isEmpty(productGroupIds)){
                String ids = org.apache.commons.lang.StringUtils.join(productGroupIds,",");
                remoteGroup = remoteProductGroupService.selectGoodsGroupByIds(ids);
            }
            if (remoteGroup.getResult() == null) {
                throw new Exception("产品组不存在");
            }
            // 3. 赋值
            vo.setProjectName(remoteResult.getResult().getName());
            vo.setSalesChannelId(remoteResult.getResult().getSalesChannelId());
            vo.setGoodsId(remoteResult.getResult().getGoodsId());
            if(prjCode==null) {
            	GoodsPortalSettingDto goodsPortalSettingDto = remoteGoodsPortalSettingService.get(remoteResult.getResult().getGoodsId()).getResult();
                if(goodsPortalSettingDto!=null 
                		&& goodsPortalSettingDto.getSysBankLogo()!=null 
                		&& goodsPortalSettingDto.getSysBankLogo().getSysFileDto()!=null) {
                	bankLogo = goodsPortalSettingDto.getSysBankLogo().getSysFileDto().getPgCdnNoHttpFullUrl();
                }
            }
            vo.setBankLogo(bankLogo);
            // 商品图片
            if(!remoteResult.getResult().getFileDtoList().isEmpty()) {
                SysFileDto sysFile = remoteResult.getResult().getFileDtoList().get(0);
                vo.setImgUrl(sysFile.getPgCdnHttpUrl() + '/' + sysFile.getGuid() + '.' + sysFile.getExt());
            }
            vo.setGoodsclauseList(resultList);
            if (!CollectionUtils.isEmpty(remoteGroup.getResult())) {
                List<ProjectGroupGiftVo> list = new ArrayList<>();
                ProjectGroupGiftVo projectGroupGiftVo = null;
                for (GoodsGroupListRes goodsGroupListRes : remoteGroup.getResult()) {
                    //赋值
                    projectGroupGiftVo = new ProjectGroupGiftVo();
                    projectGroupGiftVo.setTitle(goodsGroupListRes.getName());
                    //根据每一个产品组id查询使用次数和总次数
                    projectGroupGiftVo.setGroupId(goodsGroupListRes.getId());
                    projectGroupGiftVo.setMinBookDays(goodsGroupListRes.getMinBookDays());
                    projectGroupGiftVo.setMaxBookDays(goodsGroupListRes.getMaxBookDays());
                    // 新增内部简称
                    projectGroupGiftVo.setShortName(goodsGroupListRes.getShortName());
                    //查询产品组的资源类型
                    CommonResultVo<List<SysService>> sysService = remoteProductGroupService.selectGroupService(goodsGroupListRes.getId());
                    if (sysService.getResult() == null) {
                        throw new Exception("资源类型不存在");
                    }
                    if (!CollectionUtils.isEmpty(sysService.getResult())) {
//                        for (SysService sysservice : sysService.getResult()) {
//                            projectGroupGiftVo.setSysService(sysservice.getCode());
//                        }
                    	projectGroupGiftVo.setSysService(sysService.getResult().get(0).getCode());
                    }
                    EquityCodeDetail order = new EquityCodeDetail();
                    order.setGiftCodeId(giftCode.getId());
                    order.setProductGroupId(goodsGroupListRes.getId());

                    // 查询权益总次数
                    List<EquityCodeDetail> equityCodeDetails = goodsDetailMapper.getGoodsTimes(order);
                    if(equityCodeDetails != null){
                        // 查询权益已使用次数
                        //int useTimes = this.getGiftUseTimes(order);
                        //(新改造  需要通过当前日期获取可以预定日期的次数)
                        for(EquityCodeDetail detail : equityCodeDetails) {
                            if(detail.getStartTime() != null && detail.getEndTime() != null){
                                String start_time = DateUtil.format(detail.getStartTime(), "yyyy-MM-dd");
                                String end_time = DateUtil.format(detail.getEndTime(), "yyyy-MM-dd");
                                String book_date = DateUtil.format(new Date(),"yyyy-MM-dd");
                                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                                try {
                                    Date dt1 = df.parse(start_time);
                                    Date dt2 = df.parse(end_time);
                                    Date dt3 = df.parse(book_date);
                                    if (dt3.getTime() >= dt1.getTime() && dt3.getTime() <= dt2.getTime()) {
                                        //新增加当期使用次数 useCount
                                        //一共使用次数newUseCount surplusTimes为剩余次数
                                        Integer newUseCount = 0;
                                        newUseCount =  goodsDetailMapper.getNewUseCount(giftCode.getId(),goodsGroupListRes.getId());
                                        projectGroupGiftVo.setUseCount(detail.getUseCount());//使用次数
                                        projectGroupGiftVo.setSurplusTimes(detail.getTotalCount() - newUseCount);//剩余次数
                                        projectGroupGiftVo.setCycleCount(detail.getCycleCount());//周期内总次数
                                        projectGroupGiftVo.setTotalTimes(detail.getTotalCount());//总次数
                                        projectGroupGiftVo.setStartTime(DateUtil.format(detail.getStartTime(),"yyyy-MM-dd"));
                                        projectGroupGiftVo.setEndTime(DateUtil.format(detail.getEndTime(),"yyyy-MM-dd"));
                                        projectGroupGiftVo.setType("cycle_type");
                                        projectGroupGiftVo.setSurplusFreeTimes((detail.getTotalFreeCount()==null?0:detail.getTotalFreeCount())-(detail.getUseFreeCount()==null?0:detail.getUseFreeCount()));//剩余免费次数
                                    }
                                }catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                            }else {
                                //正常不是周期重复的数据 直接返回
                                //  不是无限制项目返回
                                if(detail.getTotalCount() != null) {
                                    projectGroupGiftVo.setSurplusTimes(detail.getTotalCount() - detail.getUseCount());
                                    projectGroupGiftVo.setTotalTimes(detail.getTotalCount());
                                    projectGroupGiftVo.setSurplusFreeTimes((detail.getTotalFreeCount()==null?0:detail.getTotalFreeCount())-(detail.getUseFreeCount()==null?0:detail.getUseFreeCount()));//剩余免费次数
                                }
                            }
                        }
                    }
                    list.add(projectGroupGiftVo);
                }
                vo.setGiftList(list);
            }
            //权益到期时间
            vo.setExpiryDate(giftCode.getActExpireTime());
            listGift.add(vo);
        }
        return listGift;
    }
	
	@Override
	public MyGiftCodeVo getGiftDetail(Integer giftCodeId, Integer prjGroupId) throws Exception {
        //1.查询激活码信息
        GiftCode giftCode = goodsDetailMapper.selectById(giftCodeId);
        // 2.1查询商品
    	CommonResultVo<GoodsBaseVo> remoteResult = remoteGoodsService.selectById(giftCode.getGoodsId());
		if (remoteResult.getResult() == null) {
            throw new Exception("商品不存在");
        }
        // // 2.2使用限制
        List<GoodsClauseList> resultList = new ArrayList<>();
        // 2.3产品组信息
    	// 返回对象
    	MyGiftCodeVo vo = new MyGiftCodeVo();
    	vo.setUnitId(giftCode.getId());
    	vo.setStatus(giftCode.getActCodeStatus());
    	// 如果goodsId不为空，商品接口和产品组信息 只需调用一次
		// // 2.2.使用限制
        // 2.3.产品组信息
    	CommonResultVo<List<GoodsGroupListRes>> remoteGroup = remoteProductGroupService.selectGoodsGroup(giftCode.getGoodsId());
        if (remoteGroup.getResult() == null) {
            throw new Exception("产品组不存在");
        }
        // 3. 赋值
        vo.setProjectName(remoteResult.getResult().getName());
        vo.setSalesChannelId(remoteResult.getResult().getSalesChannelId());
        vo.setGoodsId(remoteResult.getResult().getGoodsId());
        // 商品图片
        if(!remoteResult.getResult().getFileDtoList().isEmpty()) {
            SysFileDto sysFile = remoteResult.getResult().getFileDtoList().get(0);
            vo.setImgUrl(sysFile.getPgCdnHttpUrl() + '/' + sysFile.getGuid() + '.' + sysFile.getExt());
        }
        vo.setGoodsclauseList(resultList);
        if (!CollectionUtils.isEmpty(remoteGroup.getResult())) {
            List<ProjectGroupGiftVo> list = new ArrayList<ProjectGroupGiftVo>();
            ProjectGroupGiftVo projectGroupGiftVo = null;
            for (GoodsGroupListRes goodsGroupListRes : remoteGroup.getResult()) {
            	if(goodsGroupListRes.getId().equals(prjGroupId)) {
            		//赋值
                    projectGroupGiftVo = new ProjectGroupGiftVo();
                    projectGroupGiftVo.setTitle(goodsGroupListRes.getName());
                    //根据每一个产品组id查询使用次数和总次数
                    projectGroupGiftVo.setGroupId(goodsGroupListRes.getId());
                    projectGroupGiftVo.setMinBookDays(goodsGroupListRes.getMinBookDays());
                    projectGroupGiftVo.setMaxBookDays(goodsGroupListRes.getMaxBookDays());// 新增内部简称
                    projectGroupGiftVo.setShortName(goodsGroupListRes.getShortName());
                    //查询产品组的资源类型
                    CommonResultVo<List<SysService>> sysService = remoteProductGroupService.selectGroupService(goodsGroupListRes.getId());
                    if (sysService.getResult() == null) {
                        throw new Exception("资源类型不存在");
                    }
                    if (!CollectionUtils.isEmpty(sysService.getResult())) {
                    	projectGroupGiftVo.setSysService(sysService.getResult().get(0).getCode());
                    }
                    EquityCodeDetail order = new EquityCodeDetail();
                    order.setGiftCodeId(giftCode.getId());
                    order.setProductGroupId(goodsGroupListRes.getId());
                    // 查询权益总次数
                    List<EquityCodeDetail> equityCodeDetails = goodsDetailMapper.getGoodsTimes(order);
                    if(equityCodeDetails != null){
                        // 查询权益已使用次数
                        //int useTimes = this.getGiftUseTimes(order);
                        //(新改造  需要通过当前日期获取可以预定日期的次数)
                        for(EquityCodeDetail detail : equityCodeDetails) {
                            if(detail.getStartTime() != null && detail.getEndTime() != null){
                                String start_time = DateUtil.format(detail.getStartTime(), "yyyy-MM-dd");
                                String end_time = DateUtil.format(detail.getEndTime(), "yyyy-MM-dd");
                                String book_date = DateUtil.format(new Date(),"yyyy-MM-dd");
                                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                                try {
                                    Date dt1 = df.parse(start_time);
                                    Date dt2 = df.parse(end_time);
                                    Date dt3 = df.parse(book_date);
                                    if (dt3.getTime() >= dt1.getTime() && dt3.getTime() <= dt2.getTime()) {
                                        //新增加当期使用次数 useCount
                                        //一共使用次数newUseCount surplusTimes为剩余次数
                                        Integer newUseCount = 0;
                                        newUseCount =  goodsDetailMapper.getNewUseCount(giftCode.getId(),goodsGroupListRes.getId());
                                        projectGroupGiftVo.setUseCount(detail.getUseCount());//使用次数
                                        projectGroupGiftVo.setSurplusTimes(detail.getTotalCount() - newUseCount);//剩余次数
                                        projectGroupGiftVo.setSurplusFreeTimes((detail.getTotalFreeCount()==null?0:detail.getTotalFreeCount())-( detail.getUseFreeCount()==null?0:detail.getUseFreeCount()));//剩余次数
                                        projectGroupGiftVo.setCycleCount(detail.getCycleCount());//周期内总次数
                                        projectGroupGiftVo.setTotalTimes(detail.getTotalCount());//总次数
                                        projectGroupGiftVo.setStartTime(DateUtil.format(detail.getStartTime(),"yyyy-MM-dd"));
                                        projectGroupGiftVo.setEndTime(DateUtil.format(detail.getEndTime(),"yyyy-MM-dd"));
                                        projectGroupGiftVo.setType("cycle_type");
                                    }
                                }catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                            }else {
                                //正常不是周期重复的数据 直接返回
                                //  不是无限制项目返回
                                if(detail.getTotalCount() != null) {
                                    projectGroupGiftVo.setSurplusTimes(detail.getTotalCount() - detail.getUseCount());
                                    projectGroupGiftVo.setSurplusFreeTimes((detail.getTotalFreeCount()==null?0:detail.getTotalFreeCount())-(detail.getUseFreeCount()==null?0:detail.getUseFreeCount()));//剩余免费次数
                                    projectGroupGiftVo.setTotalTimes(detail.getTotalCount());
                                }
                            }
                        }
                    }
            	}
            	list.add(projectGroupGiftVo);
            }
            vo.setGiftList(list);
        }
        //权益到期时间
        vo.setExpiryDate(giftCode.getActExpireTime());
        return vo;
    }

    /**
     * 根据激活码查询商品详情
     * @param reqVo
     * @return
     */
    @Override
    public GoodsInfoResVo getGoodsDetailNew(QueryGoodsDetailReqVo reqVo) throws Exception {
        Assert.notNull(reqVo,"参数不能为空");
        GoodsInfoResVo resVo = new GoodsInfoResVo();
        //根据激活码查询项目id
        List<GiftCode> goodsDetailList = goodsDetailMapper.getGiftCode(reqVo.getActCode());
        if (CollectionUtils.isEmpty(goodsDetailList)) {
            throw new Exception("权益不存在");
        }
        resVo.setUnitId(goodsDetailList.get(0).getId());
        CommonResultVo<GoodsBaseVo> remoteResult = remoteGoodsService.selectById(goodsDetailList.get(0).getGoodsId());
        Assert.notNull(remoteResult,"商品不存在");
        resVo.setId(remoteResult.getResult().getId());
        resVo.setName(remoteResult.getResult().getName());
        resVo.setShortName(remoteResult.getResult().getShortName());
        //产品组信息
        CommonResultVo<List<GoodsGroupListRes>> remoteGroup = remoteProductGroupService.selectGoodsGroup(goodsDetailList.get(0).getGoodsId());
        if(StringUtils.isEmpty(remoteGroup) || CollectionUtils.isEmpty(remoteGroup.getResult())){
            throw new Exception("该商品下没有产品组");
        }
        List<GoodsGroupListRes> goodsGroupListResList = remoteGroup.getResult();
        for(GoodsGroupListRes goodsGroupListRes : goodsGroupListResList){
//            EquityCodeDetail equityCodeDetail = new EquityCodeDetail();
//            equityCodeDetail.setGiftCodeId(goodsDetailList.get(0).getId());
//            equityCodeDetail.setProductGroupId(goodsGroupListRes.getId());
//            // 查询权益总次数和使用次数
//            Map<String, Integer> numMap = goodsDetailMapper.getGoodsTimes(equityCodeDetail);
//            if(null != numMap) {
//                Integer totalCount = numMap.get("tct"); //总次数
//                Integer useCount = numMap.get("cnt") == null ? 0 : numMap.get("cnt"); //已使用次数
//                goodsGroupListRes.setTotalCount(totalCount);
//                goodsGroupListRes.setUseCount(useCount);
//            }

            List<GroupProductVo> groupProductList = goodsGroupListRes.getGroupProductList();
            if(!CollectionUtils.isEmpty(groupProductList)){
                for(GroupProductVo groupProductVo : groupProductList){
                    //获取产品图片
                    ListSysFileReq sysFileReq = new ListSysFileReq();
                    sysFileReq.setType(FileTypeEnums.SHOP_ITEM_PIC.getCode());
                    sysFileReq.setObjId(groupProductVo.getShopItemId());
                    CommonResultVo<List<SysFileDto>> vo = remoteSysFileService.list(sysFileReq);
                    //商户规格图片
                    groupProductVo.setProductImgUrl(null != vo && !CollectionUtils.isEmpty(vo.getResult())? vo.getResult().get(0).getPgCdnHttpUrl()+"/"+vo.getResult().get(0).getGuid()+"."+vo.getResult().get(0).getExt():null);

                    //获取产品预约金额
                    SelectBookPayReq selectBookPayReq = new SelectBookPayReq();
                    selectBookPayReq.setProductGroupProductId(groupProductVo.getId());
                    selectBookPayReq.setBookDates(new ArrayList<Date>(){{
                        add(new Date());
                    }});
                    CommonResultVo<List<BookBasePaymentRes>> payResultVo = remoteProductGroupProductService.selectBookPay(selectBookPayReq);
                    groupProductVo.setProductReservePrice(null != payResultVo && !CollectionUtils.isEmpty(payResultVo.getResult())?payResultVo.getResult().get(0).getBookPrice():null);

                    //获取产品净价
                    SelectPriceByTimeReqVo selectPriceByTimeReqVo = new SelectPriceByTimeReqVo();
                    selectPriceByTimeReqVo.setBookDate(new Date());
                    selectPriceByTimeReqVo.setProductGroupProductId(groupProductVo.getId());
                    CommonResultVo<ShopItemNetPriceRule> netPriceVo = remoteShopItemNetPriceService.getPriceByTime(selectPriceByTimeReqVo);
                    groupProductVo.setProductNetPrice(null != netPriceVo && null != netPriceVo.getResult()?netPriceVo.getResult().getNetPrice():null);
                }
            }
        }
        resVo.setGroupListResList(goodsGroupListResList);
        return resVo;
    }

}
