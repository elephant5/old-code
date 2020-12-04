package com.colourfulchina.mars.service.impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.colourfulCoupon.api.feign.RemoteCouponCommonService;
import com.colourfulchina.colourfulCoupon.api.vo.req.NewSendCouponReqVo;
import com.colourfulchina.god.door.api.util.SecurityUtils;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.inf.base.vo.PageVo;
import com.colourfulchina.mars.api.constant.GiftCodeRuleConstant;
import com.colourfulchina.mars.api.entity.BoscBank;
import com.colourfulchina.mars.api.entity.EquityCodeDetail;
import com.colourfulchina.mars.api.entity.GiftCode;
import com.colourfulchina.mars.api.entity.GiftCodeProlong;
import com.colourfulchina.mars.api.enums.ActCodeStatusEnum;
import com.colourfulchina.mars.api.enums.GiftCodeDetailTypeEnum;
import com.colourfulchina.mars.api.vo.CheckCodesVo;
import com.colourfulchina.mars.api.vo.req.*;
import com.colourfulchina.mars.api.vo.res.CheckCodesRes;
import com.colourfulchina.mars.api.vo.res.GiftCodePageRes;
import com.colourfulchina.mars.config.FileDownloadProperties;
import com.colourfulchina.mars.constants.GiftCodeConstants;
import com.colourfulchina.mars.constants.RedisKeys;
import com.colourfulchina.mars.mapper.GiftCodeMapper;
import com.colourfulchina.mars.mapper.GiftCodeProlongMapper;
import com.colourfulchina.mars.service.*;
import com.colourfulchina.mars.utils.CodeUtils;
import com.colourfulchina.mars.utils.HelpUtils;
import com.colourfulchina.member.api.entity.MemMemberInfo;
import com.colourfulchina.member.api.feign.RemoteMemberAccountService;
import com.colourfulchina.member.api.res.MemLoginResDTO;
import com.colourfulchina.member.api.res.MemberAccountInfoVo;
import com.colourfulchina.pangu.taishang.api.entity.Goods;
import com.colourfulchina.pangu.taishang.api.entity.ProductGroup;
import com.colourfulchina.pangu.taishang.api.entity.SalesChannel;
import com.colourfulchina.pangu.taishang.api.enums.CycleTypeEnums;
import com.colourfulchina.pangu.taishang.api.enums.UseLimitTypeEnums;
import com.colourfulchina.pangu.taishang.api.feign.RemoteActivityService;
import com.colourfulchina.pangu.taishang.api.utils.GoodsUtils;
import com.colourfulchina.pangu.taishang.api.vo.ActivityReqVo;
import com.colourfulchina.pangu.taishang.api.vo.req.GoodsBaseVo;
import com.colourfulchina.pangu.taishang.api.vo.res.ActivityCouponVo;
import com.colourfulchina.pangu.taishang.api.vo.res.ActivityResVo;
import com.colourfulchina.pangu.taishang.api.vo.res.GoodsChannelRes;
import com.colourfulchina.pangu.taishang.api.vo.res.GoodsGroupListRes;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Slf4j
@Service
public class GiftCodeServiceImp extends ServiceImpl<GiftCodeMapper,GiftCode> implements GiftCodeService {

	@Autowired
	private GiftCodeMapper giftCodeMapper;
	@Autowired
	private PanguInterfaceService panguInterfaceService;
	@Autowired
	private EquityCodeDetailService equityCodeDetailService;
	@Autowired
	private MemberInterfaceService memberInterfaceService;

	@Autowired
	private GiftCodeProlongMapper giftCodeProlongMapper;
	@Autowired
	private FileDownloadProperties fileDownloadProperties;
	@Autowired
	RedisTemplate redisTemplate;

	private final RemoteCouponCommonService remoteCouponCommonService;

	private final RemoteActivityService remoteActivityService;

	private RemoteMemberAccountService remoteMemberAccountService;

	@Autowired
	private BoscBankService boscBankService;

	/**
	 * 生成激活码
	 * @param actCodeReq
	 * @throws Exception
	 */
	@Override
	public List<GiftCode> generateActCode(ActCodeReq actCodeReq) throws Exception {
		//获取商品信息
		Goods goodsBaseVo = panguInterfaceService.findGoodsById(actCodeReq.getGoodsId());
		List<GiftCode> list = Lists.newLinkedList();
		String batchNo = CodeUtils.generateBatchNo(CodeUtils.BatchTypeNo.ACTIVATION_CODE);
		//入库
		for (int i = 0;i<actCodeReq.getActCodeNum();i++){
			GiftCode giftCode = new GiftCode();
			giftCode.setGoodsId(actCodeReq.getGoodsId());
			giftCode.setRemarks(actCodeReq.getRemarks());
			giftCode.setCodeBatchNo(batchNo);
			giftCode.setActRule(goodsBaseVo.getExpiryValue());
			giftCode.setActCodeStatus(ActCodeStatusEnum.ActCodeStatus.CREATE.getIndex());
			giftCode.setCreateUser(SecurityUtils.getLoginName());
			giftCodeMapper.insert(giftCode);
			list.add(giftCode);
		}
		//生成激活码
		for (GiftCode giftCode : list) {
			giftCode.setActCode(CodeUtils.getCodeByRedis(GiftCodeConstants.GIFT_ACT_CODE));
			giftCodeMapper.updateById(giftCode);
		}
		return list;
	}

	/**
	 * 激活码分页
	 * @param pageReq
	 * @return
	 * @throws Exception
	 */
	@Override
	public PageVo<GiftCodePageRes> selectPageList(PageVo<GiftCodePageReq> pageReq) throws Exception {
		PageVo<GiftCodePageRes> pageRes = new PageVo<>();
		Map map = pageReq.getCondition();
        List<Long> tempMem = Lists.newLinkedList();
        tempMem.add(0L);
        if (map.containsKey("giftCodes") && !map.get("giftCodes").equals("")){
            String[] giftCodeArr = map.get("giftCodes").toString().split(",");
            map.put("giftCodeArr",giftCodeArr);
        }
        if (map.containsKey("salesChannel") && !CollectionUtils.isEmpty((List)map.get("salesChannel"))){
            List<Integer> salesChannelIds = (List) map.get("salesChannel");
            SalesChannel salesChannel = new SalesChannel();
            salesChannel.setBankId(salesChannelIds.get(0)+"");
            salesChannel.setSalesChannelId(salesChannelIds.get(1)+"");
            salesChannel.setSalesWayId(salesChannelIds.get(2)+"");
            List<SalesChannel> salesChannelList = panguInterfaceService.selectByBCW(salesChannel);
            if (!CollectionUtils.isEmpty(salesChannelList)){
                map.put("salesChannelId",salesChannelList.get(0).getId());
            }else {
                map.put("salesChannelId",0);
            }
        }
		if (map.containsKey("phone") && StringUtils.isNotBlank((String) map.get("phone"))){
			MemMemberInfo memMemberInfo = new MemMemberInfo();
			memMemberInfo.setMobile((String) map.get("phone"));
			MemberAccountInfoVo memberAccountInfoVo = memberInterfaceService.getMember(memMemberInfo);
			if (memberAccountInfoVo != null && !CollectionUtils.isEmpty(memberAccountInfoVo.getAccList())){
				List<Long> members = memberAccountInfoVo.getAccList().stream().map(obj -> obj.getAcid()).collect(Collectors.toList());
				if (!CollectionUtils.isEmpty(members)){
					map.put("memberIds",members);
				}else {
                    map.put("memberIds",tempMem);
                }
			}else {
                map.put("memberIds",tempMem);
            }
		}
		List<GiftCodePageRes> list = giftCodeMapper.selectPageList(pageReq,pageReq.getCondition());
		for (GiftCodePageRes giftCodePageRes : list) {
			List<Integer> ids = Lists.newLinkedList();
			ids.add(giftCodePageRes.getGoodsId());
			List<Goods> remoteList = panguInterfaceService.selectGoodsNameByIds(ids);
			giftCodePageRes.setGoodsName(remoteList.get(0).getName());
			giftCodePageRes.setGoodsShortName(remoteList.get(0).getShortName());
			if (giftCodePageRes.getSalesChannelId() != null){
				GoodsChannelRes goodsChannelRes = panguInterfaceService.findChannelById(giftCodePageRes.getSalesChannelId());
				giftCodePageRes.setSalesChannelName(goodsChannelRes.getBankName()+"/"+goodsChannelRes.getSalesChannelName()+"/"+goodsChannelRes.getSalesWayName());
			}
			if (giftCodePageRes.getMemberId()!=null){
				MemLoginResDTO memAccount = memberInterfaceService.getMemberFullInfo(giftCodePageRes.getMemberId());
				if (memAccount!=null){
					giftCodePageRes.setPhone(memAccount.getMobile());
					giftCodePageRes.setPeopleName(memAccount.getMbName());
				}
			}
		}
		BeanUtils.copyProperties(pageReq,pageRes);
		pageRes.setRecords(list);
		return pageRes;
	}

	/**
	 * 激活码列表导出查询
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@Override
	public String selectExportList(Map map) throws Exception {
		String result = null;
		List<Long> tempMem = Lists.newLinkedList();
		tempMem.add(0L);
		if (map.containsKey("giftCodes") && !map.get("giftCodes").equals("")){
			String[] giftCodeArr = map.get("giftCodes").toString().split(",");
			map.put("giftCodeArr",giftCodeArr);
		}
		if (map.containsKey("salesChannel") && !CollectionUtils.isEmpty((List)map.get("salesChannel"))){
			List<Integer> salesChannelIds = (List) map.get("salesChannel");
			SalesChannel salesChannel = new SalesChannel();
			salesChannel.setBankId(salesChannelIds.get(0)+"");
			salesChannel.setSalesChannelId(salesChannelIds.get(1)+"");
			salesChannel.setSalesWayId(salesChannelIds.get(2)+"");
			List<SalesChannel> salesChannelList = panguInterfaceService.selectByBCW(salesChannel);
			if (!CollectionUtils.isEmpty(salesChannelList)){
				map.put("salesChannelId",salesChannelList.get(0).getId());
			}else {
				map.put("salesChannelId",0);
			}
		}
		if (map.containsKey("phone") && StringUtils.isNotBlank((String) map.get("phone"))){
			MemMemberInfo memMemberInfo = new MemMemberInfo();
			memMemberInfo.setMobile((String) map.get("phone"));
			MemberAccountInfoVo memberAccountInfoVo = memberInterfaceService.getMember(memMemberInfo);
			if (memberAccountInfoVo != null && !CollectionUtils.isEmpty(memberAccountInfoVo.getAccList())){
				List<Long> members = memberAccountInfoVo.getAccList().stream().map(obj -> obj.getAcid()).collect(Collectors.toList());
				if (!CollectionUtils.isEmpty(members)){
					map.put("memberIds",members);
				}else {
					map.put("memberIds",tempMem);
				}
			}else {
				map.put("memberIds",tempMem);
			}
		}
		List<GiftCodePageRes> list = giftCodeMapper.selectExportList(map);
		if (!CollectionUtils.isEmpty(list)){
			Set<Integer> idList = list.stream().map(code -> code.getGoodsId()).collect(Collectors.toSet());
			List<Integer> ids = Lists.newLinkedList(idList);
			List<Goods> remoteList = panguInterfaceService.selectGoodsNameByIds(ids);
			Map<Integer,Goods> goodsMap = remoteList.stream().collect(Collectors.toMap(Goods::getId,goods -> goods));

			List<String> titleList=Lists.newArrayList();
			titleList.add("激活码");
			titleList.add("码状态");
			titleList.add("批次号");
			titleList.add("关联商品");
			titleList.add("销售渠道");
			titleList.add("有效期");
			ExcelWriter excelWriter=new ExcelWriter(true);
			String fileName="激活码-"+DateUtil.format(new Date(),"yyyyMMddHHmmss")+"-"+list.size()+".xlsx";
			excelWriter.setDestFile(new File(fileDownloadProperties.getPath()+"/"+fileName));
			excelWriter.setOrCreateSheet("sheet1");
			excelWriter.writeHeadRow(titleList);

			for (GiftCodePageRes giftCodePageRes : list) {
				giftCodePageRes.setGoodsName(giftCodePageRes.getGoodsId()+"#"+goodsMap.get(giftCodePageRes.getGoodsId()).getName());
				if (giftCodePageRes.getSalesChannelId() != null){
					GoodsChannelRes goodsChannelRes = panguInterfaceService.findChannelById(giftCodePageRes.getSalesChannelId());
					giftCodePageRes.setSalesChannelName(goodsChannelRes.getBankName()+"/"+goodsChannelRes.getSalesChannelName()+"/"+goodsChannelRes.getSalesWayName());
				}
				List<Object> rowData=Lists.newArrayList();
				rowData.add(giftCodePageRes.getActCode());
				rowData.add(giftCodePageRes.getCodeStatusName());
				rowData.add(giftCodePageRes.getCodeBatchNo());
				rowData.add(giftCodePageRes.getGoodsName());
				rowData.add(giftCodePageRes.getSalesChannelName());
				rowData.add(giftCodePageRes.getActEndTime() == null? null: DateUtil.format(giftCodePageRes.getActEndTime(),"yyyy-MM-dd"));
				excelWriter.writeRow(rowData);
			}
			excelWriter.flush();
			result=fileDownloadProperties.getUrl()+fileName;
		}
		return result;
	}

	/**
	 * 出库激活码，根据激活码列表
	 * @param outCodeReq
	 * @throws Exception
	 */
	@Override
	public List<GiftCode> outActCodeByCodes(OutCodeReq outCodeReq,HttpServletRequest request) throws Exception {
		if (outCodeReq.getActExpireTime() != null){
			outCodeReq.setActEndTime(outCodeReq.getActExpireTime());
		}else {
			//获取激活码激活规则
			Wrapper codeWrapper = new Wrapper() {
				@Override
				public String getSqlSegment() {
					return "where act_code IN ('"+ StringUtils.join(outCodeReq.getCodes(),"','") +"')";
				}
			};
			List<GiftCode> giftCodeList = giftCodeMapper.selectList(codeWrapper);
			//调用项目过期时间工具
			GoodsBaseVo goodsBaseVo = new GoodsBaseVo();
			goodsBaseVo.setExpiryValue(giftCodeList.get(0).getActRule());
			goodsBaseVo = GoodsUtils.analyCodeDate(goodsBaseVo,null,new Date());
			outCodeReq.setActEndTime("NULL".equalsIgnoreCase(goodsBaseVo.getActiveDate()) || StringUtils.isBlank(goodsBaseVo.getActiveDate()) ? null : DateUtil.parse(goodsBaseVo.getActiveDate(),"yyyy-MM-dd"));
			if (StringUtils.isNotBlank(goodsBaseVo.getExpiryDate())){
				outCodeReq.setActExpireTime("NULL".equals(goodsBaseVo.getExpiryDate()) || StringUtils.isBlank(goodsBaseVo.getExpiryDate()) ? null : DateUtil.parse(goodsBaseVo.getExpiryDate(),"yyyy-MM-dd"));
			}
		}
		outCodeReq.setActCodeStatus(ActCodeStatusEnum.ActCodeStatus.OUT.getIndex());
		outCodeReq.setActOutDate(new Date());
		if (!CollectionUtils.isEmpty(outCodeReq.getTagList())){
			outCodeReq.setTags(StringUtils.join(outCodeReq.getTagList(),","));
		}
		giftCodeMapper.outActCodeByCodes(outCodeReq);
		Wrapper wrapper = new Wrapper() {
			@Override
			public String getSqlSegment() {
				return "where act_code IN ('"+ StringUtils.join(outCodeReq.getCodes(),"','") +"')";
			}
		};
		List<GiftCode> list = giftCodeMapper.selectList(wrapper);
		//权益明细表信息插入
		List<GoodsGroupListRes> goodsGroup = panguInterfaceService.selectGoodsGroup(list.get(0).getGoodsId());
		List<EquityCodeDetail> codeDetails = addCodeDetail(list,goodsGroup,request);
		return list;
	}

	/**
	 * 出库激活码，根据批次号
	 * @param outCodeReq
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<GiftCode> outActCodeByBatch(OutCodeReq outCodeReq, HttpServletRequest request) throws Exception {
		if (outCodeReq.getActExpireTime() != null){
			outCodeReq.setActEndTime(outCodeReq.getActExpireTime());
		}else {
			//获取激活码激活规则
			Wrapper codeWrapper = new Wrapper() {
				@Override
				public String getSqlSegment() {
					return "where code_batch_no ='"+outCodeReq.getCodeBatchNo()+"'";
				}
			};
			List<GiftCode> giftCodeList = giftCodeMapper.selectList(codeWrapper);
			//调用项目过期时间工具
			GoodsBaseVo goodsBaseVo = new GoodsBaseVo();
			goodsBaseVo.setExpiryValue(giftCodeList.get(0).getActRule());
			goodsBaseVo = GoodsUtils.analyCodeDate(goodsBaseVo,null,new Date());
			outCodeReq.setActEndTime("NULL".equals(goodsBaseVo.getActiveDate()) ? null : DateUtil.parse(goodsBaseVo.getActiveDate(),"yyyy-MM-dd"));
			if (StringUtils.isNotBlank(goodsBaseVo.getExpiryDate())){
				outCodeReq.setActExpireTime("NULL".equals(goodsBaseVo.getExpiryDate()) ? null : DateUtil.parse(goodsBaseVo.getExpiryDate(),"yyyy-MM-dd"));
			}
		}
		outCodeReq.setActCodeStatus(ActCodeStatusEnum.ActCodeStatus.OUT.getIndex());
		outCodeReq.setActOutDate(new Date());
		if (!CollectionUtils.isEmpty(outCodeReq.getTagList())){
			outCodeReq.setTags(StringUtils.join(outCodeReq.getTagList(),","));
		}
		giftCodeMapper.outActCodeByBatch(outCodeReq);
		Wrapper wrapper = new Wrapper() {
			@Override
			public String getSqlSegment() {
				return "where code_batch_no ='"+outCodeReq.getCodeBatchNo()+"'";
			}
		};
		List<GiftCode> list = giftCodeMapper.selectList(wrapper);
		//权益明细表信息插入
		List<GoodsGroupListRes> goodsGroup = panguInterfaceService.selectGoodsGroup(list.get(0).getGoodsId());
		List<EquityCodeDetail> codeDetails = addCodeDetail(list,goodsGroup,request);
		return list;
	}

	/**
	 * 权益明细表信息插入
	 * @param giftCodeList
	 * @param goodsGroup
	 * @param request
	 * @return
	 */
	@Async("taskExecutorPool")
	public List<EquityCodeDetail> addCodeDetail(List<GiftCode> giftCodeList, List<GoodsGroupListRes> goodsGroup, HttpServletRequest request){
		List<EquityCodeDetail> codeDetails = Lists.newLinkedList();
		for (GiftCode giftCode : giftCodeList) {
			Wrapper wrapper = new Wrapper() {
				@Override
				public String getSqlSegment() {
					return "where gift_code_id ="+giftCode.getId();
				}
			};
			List<EquityCodeDetail> details = equityCodeDetailService.selectList(wrapper);
			if (CollectionUtils.isEmpty(details)){
				for (GoodsGroupListRes goodsGroupListRes : goodsGroup) {
					ProductGroup productGroup = new ProductGroup();
					BeanUtils.copyProperties(goodsGroupListRes,productGroup);
					//循环周期的产品组权益
					if (goodsGroupListRes.getUseLimitId().equals(UseLimitTypeEnums.CYCLE_REPEAT.getCode())){
						//权益开始时间
						Date startDate = giftCode.getActOutDate();
						//权益结束时间
						Date endDate = null;
						//周期重复时间
						Integer cycleTime = goodsGroupListRes.getCycleTime();
						int cycleType = 0;
						if (goodsGroupListRes.getCycleType().compareTo(CycleTypeEnums.CYCLE_DAY.getCode()) == 0){
							cycleType = Calendar.DAY_OF_MONTH;
						}else if (goodsGroupListRes.getCycleType().compareTo(CycleTypeEnums.CYCLE_WEEK.getCode()) == 0){
							cycleType = Calendar.WEEK_OF_MONTH;
						}else if (goodsGroupListRes.getCycleType().compareTo(CycleTypeEnums.CYCLE_MONTH.getCode()) == 0){
							cycleType = Calendar.MONTH;
						}else if (goodsGroupListRes.getCycleType().compareTo(CycleTypeEnums.CYCLE_YEAR.getCode()) == 0){
							cycleType = Calendar.YEAR;
						}
						//无限制
						if (giftCode.getActEndTime() == null){
							Calendar calendar = Calendar.getInstance();
							calendar.setTime(startDate);
							calendar.add(Calendar.YEAR,1);
							endDate = calendar.getTime();
						}
						//有权益截止时间
						else if (giftCode.getActExpireTime() != null){
							endDate = giftCode.getActExpireTime();
						}
						//有激活截止时间，没有权益截止时间
						else if (giftCode.getActEndTime() != null){
							Calendar calendar = Calendar.getInstance();
							calendar.setTime(giftCode.getActEndTime());
							calendar.add(Calendar.MONTH,3);
							endDate = calendar.getTime();
						}
						startDate = DateUtil.parse(DateUtil.format(startDate,"yyyy-MM-dd"),"yyyy-MM-dd");
						endDate= DateUtil.parse(DateUtil.format(endDate,"yyyy-MM-dd"),"yyyy-MM-dd");
						Date nowDate = startDate;
						//根据起始截止时间生成激活码权益明细
						while (nowDate.before(endDate)){
							Date tempDate = nowDate;
							Calendar calendar = Calendar.getInstance();
							calendar.setTime(nowDate);
							calendar.add(cycleType,cycleTime);
							if (calendar.getTime().compareTo(endDate) >= 0){
								nowDate = endDate;
							}else {
								nowDate = calendar.getTime();
							}
							EquityCodeDetail equityCodeDetail = new EquityCodeDetail();
							equityCodeDetail.setGiftCodeId(giftCode.getId());
							equityCodeDetail.setGoodsId(giftCode.getGoodsId());
							equityCodeDetail.setProductGroupId(goodsGroupListRes.getId());
							equityCodeDetail.setUseCount(0);
							equityCodeDetail.setTotalFreeCount(goodsGroupListRes.getFreeCount());
							equityCodeDetail.setType(GiftCodeDetailTypeEnum.CYCLE_TYPE.getcode());
							equityCodeDetail.setGroupDetail(JSONObject.toJSONString(productGroup));
							if (goodsGroupListRes.getUseNum() != null){
								equityCodeDetail.setTotalCount(goodsGroupListRes.getUseNum().intValue());
							}
							equityCodeDetail.setStartTime(tempDate);
							equityCodeDetail.setEndTime(nowDate);
							equityCodeDetail.setCycleCount(goodsGroupListRes.getCycleNum());
							equityCodeDetail.setCreateUser(SecurityUtils.getLoginName(request));
							codeDetails.add(equityCodeDetail);
						}
					}else {
						EquityCodeDetail equityCodeDetail = new EquityCodeDetail();
						equityCodeDetail.setGiftCodeId(giftCode.getId());
						equityCodeDetail.setGoodsId(giftCode.getGoodsId());
						equityCodeDetail.setProductGroupId(goodsGroupListRes.getId());
						equityCodeDetail.setUseCount(0);
						equityCodeDetail.setTotalFreeCount(goodsGroupListRes.getFreeCount());
						equityCodeDetail.setType(GiftCodeDetailTypeEnum.NOMAL_TYPE.getcode());
						equityCodeDetail.setGroupDetail(JSONObject.toJSONString(productGroup));
						if (goodsGroupListRes.getUseNum() != null){
							equityCodeDetail.setTotalCount(goodsGroupListRes.getUseNum().intValue());
						}
						equityCodeDetail.setCreateUser(SecurityUtils.getLoginName(request));
						codeDetails.add(equityCodeDetail);
					}
				}
			}
		}
		if (!CollectionUtils.isEmpty(codeDetails)){
			equityCodeDetailService.insertBatch(codeDetails);
		}
		return codeDetails;
	}

	/**
	 * 检测激活码
	 * @param type (code 根据激活码检测；batch 根据批次号检测)
	 * @param codes
	 * @throws Exception
	 */
	@Override
	public CheckCodesRes checkCodes(String type,String codes) throws Exception {
		CheckCodesRes checkCodesRes = new CheckCodesRes();
		boolean isCommonGoods = true;
		//所有码
		List<String> allCodes = Lists.newLinkedList();
		//不存在的码
		List<CheckCodesVo> notExistCodes = Lists.newLinkedList();
		//生成状态的码
		List<CheckCodesVo> generateCodes = Lists.newLinkedList();
		//出库状态的码
		List<CheckCodesVo> outCodes = Lists.newLinkedList();
		//激活状态的码
		List<CheckCodesVo> activeCodes = Lists.newLinkedList();
		//用完状态的码
		List<CheckCodesVo> runOutCodes = Lists.newLinkedList();
		//过期状态的码
		List<CheckCodesVo> pastCodes = Lists.newLinkedList();
		//退货状态的码
		List<CheckCodesVo> returnCodes = Lists.newLinkedList();
		//作废状态的码
		List<CheckCodesVo> obsoleteCodes = Lists.newLinkedList();

		List<GiftCode> giftCodeList = Lists.newLinkedList();
		if ("code".equals(type)){
			codes = codes.replaceAll("，",",");
			List<String> baseCodes = Arrays.asList(codes.split(","));
			allCodes = baseCodes;
			Wrapper wrapper = new Wrapper() {
				@Override
				public String getSqlSegment() {
					return "where act_code IN ('"+ StringUtils.join(baseCodes,"','") +"')";
				}
			};
			giftCodeList = giftCodeMapper.selectList(wrapper);
		}else if ("batch".equals(type)){
			codes = codes.replaceAll("\\s","");
			String finalCodes = codes;
			Wrapper wrapper = new Wrapper() {
				@Override
				public String getSqlSegment() {
					return "where code_batch_no ='"+ finalCodes +"'";
				}
			};
			giftCodeList = giftCodeMapper.selectList(wrapper);
			allCodes = giftCodeList.stream().map(giftCode -> giftCode.getActCode()).collect(Collectors.toList());
		}

		//获取商品列表
		List<Goods> goodsList = panguInterfaceService.selectGoodsList();
		Map<Integer,Goods> goodsMap = goodsList.stream().collect(Collectors.toMap(Goods::getId, goods -> goods));

		for (GiftCode giftCode : giftCodeList) {
			if (giftCode.getActCodeStatus().compareTo(ActCodeStatusEnum.ActCodeStatus.CREATE.getIndex()) == 0){
				CheckCodesVo check = new CheckCodesVo();
				check.setActCode(giftCode.getActCode());
				check.setActCodeStatus(giftCode.getActCodeStatus());
				check.setActCodeName(ActCodeStatusEnum.ActCodeStatus.findNameByIndex(giftCode.getActCodeStatus()));
				check.setGoodsId(giftCode.getGoodsId());
				check.setGoodsShortName(goodsMap.get(giftCode.getGoodsId()).getShortName());
				generateCodes.add(check);
			}
			if (giftCode.getActCodeStatus().compareTo(ActCodeStatusEnum.ActCodeStatus.OUT.getIndex()) == 0){
				CheckCodesVo check = new CheckCodesVo();
				check.setActCode(giftCode.getActCode());
				check.setActCodeStatus(giftCode.getActCodeStatus());
				check.setActCodeName(ActCodeStatusEnum.ActCodeStatus.findNameByIndex(giftCode.getActCodeStatus()));
				check.setGoodsId(giftCode.getGoodsId());
				check.setGoodsShortName(goodsMap.get(giftCode.getGoodsId()).getShortName());
				outCodes.add(check);
			}
			if (giftCode.getActCodeStatus().compareTo(ActCodeStatusEnum.ActCodeStatus.ACTIVATE.getIndex()) == 0){
				CheckCodesVo check = new CheckCodesVo();
				check.setActCode(giftCode.getActCode());
				check.setActCodeStatus(giftCode.getActCodeStatus());
				check.setActCodeName(ActCodeStatusEnum.ActCodeStatus.findNameByIndex(giftCode.getActCodeStatus()));
				check.setGoodsId(giftCode.getGoodsId());
				check.setGoodsShortName(goodsMap.get(giftCode.getGoodsId()).getShortName());
				activeCodes.add(check);
			}
			if (giftCode.getActCodeStatus().compareTo(ActCodeStatusEnum.ActCodeStatus.RUN_OUT.getIndex()) == 0){
				CheckCodesVo check = new CheckCodesVo();
				check.setActCode(giftCode.getActCode());
				check.setActCodeStatus(giftCode.getActCodeStatus());
				check.setActCodeName(ActCodeStatusEnum.ActCodeStatus.findNameByIndex(giftCode.getActCodeStatus()));
				check.setGoodsId(giftCode.getGoodsId());
				check.setGoodsShortName(goodsMap.get(giftCode.getGoodsId()).getShortName());
				runOutCodes.add(check);
			}
			if (giftCode.getActCodeStatus().compareTo(ActCodeStatusEnum.ActCodeStatus.PAST.getIndex()) == 0){
				CheckCodesVo check = new CheckCodesVo();
				check.setActCode(giftCode.getActCode());
				check.setActCodeStatus(giftCode.getActCodeStatus());
				check.setActCodeName(ActCodeStatusEnum.ActCodeStatus.findNameByIndex(giftCode.getActCodeStatus()));
				check.setGoodsId(giftCode.getGoodsId());
				check.setGoodsShortName(goodsMap.get(giftCode.getGoodsId()).getShortName());
				pastCodes.add(check);
			}
			if (giftCode.getActCodeStatus().compareTo(ActCodeStatusEnum.ActCodeStatus.RETURN.getIndex()) == 0){
				CheckCodesVo check = new CheckCodesVo();
				check.setActCode(giftCode.getActCode());
				check.setActCodeStatus(giftCode.getActCodeStatus());
				check.setActCodeName(ActCodeStatusEnum.ActCodeStatus.findNameByIndex(giftCode.getActCodeStatus()));
				check.setGoodsId(giftCode.getGoodsId());
				check.setGoodsShortName(goodsMap.get(giftCode.getGoodsId()).getShortName());
				returnCodes.add(check);
			}
			if (giftCode.getActCodeStatus().compareTo(ActCodeStatusEnum.ActCodeStatus.OBSOLETE.getIndex()) == 0){
				CheckCodesVo check = new CheckCodesVo();
				check.setActCode(giftCode.getActCode());
				check.setActCodeStatus(giftCode.getActCodeStatus());
				check.setActCodeName(ActCodeStatusEnum.ActCodeStatus.findNameByIndex(giftCode.getActCodeStatus()));
				check.setGoodsId(giftCode.getGoodsId());
				check.setGoodsShortName(goodsMap.get(giftCode.getGoodsId()).getShortName());
				obsoleteCodes.add(check);
			}
		}
		Set<Integer> goodsSet = giftCodeList.stream().map(giftCode -> giftCode.getGoodsId()).collect(Collectors.toSet());
		if (goodsSet.size()>1){
			isCommonGoods = false;
		}
		if (goodsSet.size() == 1){
			for (Integer integer : goodsSet) {
				checkCodesRes.setGoodsId(integer);
			}
		}
		List<String> codeList = giftCodeList.stream().map(giftCode -> giftCode.getActCode()).collect(Collectors.toList());
		for (String baseCode : allCodes) {
			if (!codeList.contains(baseCode)){
				CheckCodesVo check = new CheckCodesVo();
				check.setActCode(baseCode);
				notExistCodes.add(check);
			}
		}
		checkCodesRes.setCommonGoods(isCommonGoods);
		checkCodesRes.setAllCodes(allCodes);
		checkCodesRes.setNotExistCodes(notExistCodes);
		checkCodesRes.setGenerateCodes(generateCodes);
		checkCodesRes.setOutCodes(outCodes);
		checkCodesRes.setActiveCodes(activeCodes);
		checkCodesRes.setRunOutCodes(runOutCodes);
		checkCodesRes.setPastCodes(pastCodes);
		checkCodesRes.setReturnCodes(returnCodes);
		checkCodesRes.setObsoleteCodes(obsoleteCodes);
		return checkCodesRes;
	}

	/**
	 * 激活激活码
	 * @param activeActCodeReq
	 * @return
	 * @throws Exception
	 */
	@Override
	public GiftCode activeActCode(ActiveActCodeReq activeActCodeReq) throws Exception {
		Wrapper wrapper = new Wrapper() {
			@Override
			public String getSqlSegment() {
				return "where del_flag = 0 and act_code_status = 1 and act_code ='"+activeActCodeReq.getActCode()+"'";
			}
		};
		List<GiftCode> giftCodeList = giftCodeMapper.selectList(wrapper);
		if (!CollectionUtils.isEmpty(giftCodeList)){
			GiftCode giftCode = giftCodeList.get(0);
			giftCode.setActCodeStatus(ActCodeStatusEnum.ActCodeStatus.ACTIVATE.getIndex());
			//激活码到期时间不存在则获取商品设置的到期时间
			if (giftCode.getActExpireTime() == null){
				//调用项目过期时间工具
				GoodsBaseVo goodsBaseVo = new GoodsBaseVo();
				goodsBaseVo.setExpiryValue(giftCode.getActRule());
				goodsBaseVo = GoodsUtils.analyCodeDate(goodsBaseVo,new Date(),giftCode.getActOutDate());
				giftCode.setActExpireTime("NULL".equals(goodsBaseVo.getExpiryDate()) ? null : DateUtil.parse(goodsBaseVo.getExpiryDate(),"yyyy-MM-dd"));
			}
			giftCode.setActCodeTime(new Date());
			giftCode.setMemberId(activeActCodeReq.getMemberId());
			giftCode.setActiveRemarks(activeActCodeReq.getActiveRemarks());
			giftCodeMapper.updateById(giftCode);
			//权益明细表memberId插入及循环权益变动
			Wrapper detailWrapper = new Wrapper() {
				@Override
				public String getSqlSegment() {
					return "where del_flag = 0 and gift_code_id ="+giftCode.getId();
				}
			};
			List<EquityCodeDetail> detailList = equityCodeDetailService.selectList(detailWrapper);
			if (!CollectionUtils.isEmpty(detailList)){
				//按产品组分类入map
				Map<Integer,List<EquityCodeDetail>> map = Maps.newHashMap();
				for (EquityCodeDetail equityCodeDetail : detailList) {
					Integer key = equityCodeDetail.getProductGroupId();
					List<EquityCodeDetail> list = Lists.newLinkedList();
					if (map.containsKey(key)){
						list = map.get(key);
					}
					list.add(equityCodeDetail);
					map.put(key,list);
				}
				//针对不同类型的权益修改或新增权益明细
				if (!CollectionUtils.isEmpty(map)){
					for (Map.Entry<Integer, List<EquityCodeDetail>> entry : map.entrySet()) {
						List<EquityCodeDetail> list = entry.getValue();
						if (!CollectionUtils.isEmpty(list)){
							//添加用户id
							for (EquityCodeDetail detail : list) {
								detail.setMemberId(activeActCodeReq.getMemberId());
								equityCodeDetailService.updateById(detail);
							}
							//循环周期权益（无限制产品组权益延长一年，到期时间大于权益最大截止时间则修改最大权益明细到期时间，并插入后续时间的权益）
							if (list.get(0).getType().compareTo(GiftCodeDetailTypeEnum.CYCLE_TYPE.getcode()) == 0){
								//找出list里最大的权益截止日期数据
								list.sort(new Comparator<EquityCodeDetail>() {
									@Override
									public int compare(EquityCodeDetail o1, EquityCodeDetail o2) {
										return o2.getEndTime().compareTo(o1.getEndTime());
									}
								});
								EquityCodeDetail equityCodeDetail = list.get(0);
								Date oldEndTime = equityCodeDetail.getEndTime();
								Date expiryDate = giftCode.getActExpireTime();
								//无限制权益，最后时间设置一年后
								if (expiryDate == null){
									Calendar calendar = Calendar.getInstance();
									calendar.add(Calendar.YEAR,1);
									expiryDate = calendar.getTime();
								}
								oldEndTime = DateUtil.parse(DateUtil.format(oldEndTime,"yyyy-MM-dd"),"yyyy-MM-dd");
								expiryDate = DateUtil.parse(DateUtil.format(expiryDate,"yyyy-MM-dd"),"yyyy-MM-dd");
								//到期时间大于权益最大截止时间
								if (oldEndTime.before(expiryDate)){
									JSONObject jsonObject = JSONObject.parseObject(equityCodeDetail.getGroupDetail());
									Integer cycleTime = Integer.valueOf(jsonObject.getString("cycleTime"));
									Integer cycleTypeInt = Integer.valueOf(jsonObject.getString("cycleType"));
									Integer productGroupId = Integer.valueOf(jsonObject.getString("id"));
									Integer cycleNum = Integer.valueOf(jsonObject.getString("cycleNum"));
									int cycleType = 0;
									if (cycleTypeInt.compareTo(CycleTypeEnums.CYCLE_DAY.getCode()) == 0){
										cycleType = Calendar.DAY_OF_MONTH;
									}else if (cycleTypeInt.compareTo(CycleTypeEnums.CYCLE_WEEK.getCode()) == 0){
										cycleType = Calendar.WEEK_OF_MONTH;
									}else if (cycleTypeInt.compareTo(CycleTypeEnums.CYCLE_MONTH.getCode()) == 0){
										cycleType = Calendar.MONTH;
									}else if (cycleTypeInt.compareTo(CycleTypeEnums.CYCLE_YEAR.getCode()) == 0){
										cycleType = Calendar.YEAR;
									}
									//更新过去权益明细的最大截止时间
									Date oldStartTime = DateUtil.parse(DateUtil.format(equityCodeDetail.getStartTime(),"yyyy-MM-dd"),"yyyy-MM-dd");
									Date nowDate = oldStartTime;
									Calendar calendar = Calendar.getInstance();
									calendar.setTime(nowDate);
									calendar.add(cycleType,cycleTime);
									if (calendar.getTime().compareTo(expiryDate) >= 0){
										nowDate = expiryDate;
									}else {
										nowDate = calendar.getTime();
									}
									equityCodeDetail.setEndTime(nowDate);
									equityCodeDetailService.updateById(equityCodeDetail);
									//新增权益明细信息
									List<EquityCodeDetail> codeDetails = Lists.newLinkedList();
									while (nowDate.before(expiryDate)){
										Date tempDate = nowDate;
										calendar.setTime(nowDate);
										calendar.add(cycleType,cycleTime);
										if (calendar.getTime().compareTo(expiryDate) >= 0){
											nowDate = expiryDate;
										}else {
											nowDate = calendar.getTime();
										}
										EquityCodeDetail equity = new EquityCodeDetail();
										equity.setGiftCodeId(giftCode.getId());
										equity.setGoodsId(giftCode.getGoodsId());
										equity.setProductGroupId(productGroupId);
										equity.setUseCount(0);
										equity.setType(GiftCodeDetailTypeEnum.CYCLE_TYPE.getcode());
										equity.setGroupDetail(equityCodeDetail.getGroupDetail());
										equity.setTotalCount(equityCodeDetail.getTotalCount());
										equity.setStartTime(tempDate);
										equity.setEndTime(nowDate);
										equity.setCycleCount(cycleNum);
										equity.setMemberId(activeActCodeReq.getMemberId());
										equity.setCreateUser(SecurityUtils.getLoginName());
										codeDetails.add(equity);
									}
									if (!CollectionUtils.isEmpty(codeDetails)){
										equityCodeDetailService.insertBatch(codeDetails);
									}
								}
							}
						}
					}
				}
			}
			//判断商品是不是有活动 有则发优惠券
			if(!this.sendCoupon(giftCode.getGoodsId(), 1,1, activeActCodeReq.getMemberId())) {
				log.error("发券异常", JSON.toJSON(activeActCodeReq));
			}

			return giftCode;
		}
		return null;
	}

	/**
	 * 激活码退货
	 * @param returnCodeReq
	 * @throws Exception
	 */
	@Override
	public List<GiftCode> returnActCodes(ReturnCodeReq returnCodeReq) throws Exception {
		returnCodeReq.setActCodeStatus(ActCodeStatusEnum.ActCodeStatus.RETURN.getIndex());
		returnCodeReq.setActReturnDate(new Date());
		giftCodeMapper.returnActCodeByCodes(returnCodeReq);
		Wrapper wrapper = new Wrapper() {
			@Override
			public String getSqlSegment() {
				return "where act_code IN ('"+ StringUtils.join(returnCodeReq.getCodes(),"','") +"')";
			}
		};
		List<GiftCode> list = giftCodeMapper.selectList(wrapper);
		return list;
	}

	/**
	 * 激活码作废
	 * @param obsoleteCodeReq
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<GiftCode> obsoleteActCodes(ObsoleteCodeReq obsoleteCodeReq) throws Exception {
		obsoleteCodeReq.setActCodeStatus(ActCodeStatusEnum.ActCodeStatus.OBSOLETE.getIndex());
		obsoleteCodeReq.setActObsoleteDate(new Date());
		giftCodeMapper.obsoleteActCodeByCodes(obsoleteCodeReq);
		Wrapper wrapper = new Wrapper() {
			@Override
			public String getSqlSegment() {
				return "where act_code IN ('"+ StringUtils.join(obsoleteCodeReq.getCodes(),"','") +"')";
			}
		};
		List<GiftCode> list = giftCodeMapper.selectList(wrapper);
		return list;
	}

	/**
	 * 激活码是否用完逻辑流程
	 * @param ids
	 * @throws Exception
	 */
	@Override
	public void  actCodesIsRunOut(Set<Integer> ids) throws Exception {
		if (!CollectionUtils.isEmpty(ids)){
			for (Integer id : ids) {
				Wrapper wrapper = new Wrapper() {
					@Override
					public String getSqlSegment() {
						return "where del_flag = 0 and gift_code_id ="+id;
					}
				};
				List<EquityCodeDetail> details = equityCodeDetailService.selectList(wrapper);
				boolean temp = true;//true用完，false没用完
				if (!CollectionUtils.isEmpty(details)){
                    Map<Integer,Integer> map = new HashMap<Integer, Integer>();
					for (EquityCodeDetail detail : details) {
						if (detail.getTotalCount() == null){
							temp = false;
						}else {
						    //判断是不是周期重复  不是(NOMAL_TYPE)直接走原来方法
						    if(detail.getType() == GiftCodeDetailTypeEnum.NOMAL_TYPE.getcode()) {
                                if (detail.getTotalCount().compareTo(detail.getUseCount()) == 1) {
                                    temp = false;
                                }
                            }
                            // 是周期重复(CYCLE_TYPE) 按照产品组id分别统计useCount的和
                            else if(detail.getType() == GiftCodeDetailTypeEnum.CYCLE_TYPE.getcode()){
                                if (map.containsKey(detail.getProductGroupId())){
                                    Integer a = map.get(detail.getProductGroupId());
                                    a += detail.getUseCount().intValue();
                                    map.put(detail.getProductGroupId(), a);
                                } else {
                                    map.put(detail.getProductGroupId(), detail.getUseCount().intValue());
                            }
                            }
						}
					}
					//拿刚刚统计的值作对比 看激活码用没用完
                    for(EquityCodeDetail newDetail : details){
					    if(newDetail.getType() == GiftCodeDetailTypeEnum.CYCLE_TYPE.getcode()){
					        if(newDetail.getTotalCount().compareTo(map.get(newDetail.getProductGroupId())) == 1){
					            temp = false;
                            }
                        }
                    }
				}
				// 查询giftcode
				GiftCode giftCode = giftCodeMapper.selectById(id);
				if (temp == true){
					if (giftCode.getActCodeStatus().compareTo(ActCodeStatusEnum.ActCodeStatus.ACTIVATE.getIndex()) == 0){
						giftCode.setActCodeStatus(ActCodeStatusEnum.ActCodeStatus.RUN_OUT.getIndex());
						giftCodeMapper.updateById(giftCode);
					}
				}else if (temp == false){
					if (giftCode.getActCodeStatus().compareTo(ActCodeStatusEnum.ActCodeStatus.RUN_OUT.getIndex()) == 0){
						giftCode.setActCodeStatus(ActCodeStatusEnum.ActCodeStatus.ACTIVATE.getIndex());
						giftCodeMapper.updateById(giftCode);
					}
				}
			}
		}
	}

	@Override
	public List<HashMap<String, Object>> getActCodeList(HashMap<String, Object> map) throws Exception {
		
		return giftCodeMapper.getActCodeList(map);
	}

	/**
	 * 根据gift_code的id作废激活码
	 * @param req
	 * @return
	 */
	@Override
	public Boolean cancleCode(CancelCodeReq req) throws Exception {
		if (null == req || null == req.getGiftCodeId()) {
			throw new Exception("参数不能为空");
		}
		//根据gift_code的id查询激活码act_code
		GiftCode giftCode = giftCodeMapper.selectById(req.getGiftCodeId());
		if (null == giftCode || StringUtils.isEmpty(giftCode.getActCode())) {
			throw new Exception("激活码不存在");
		}
		//作废激活码
		ObsoleteCodeReq obsoleteCodeReq = new ObsoleteCodeReq();
		List<String> codes = new ArrayList<String>() {{
			add(giftCode.getActCode());
		}};
		obsoleteCodeReq.setCodes(codes);
		List<GiftCode> giftCodes = this.obsoleteActCodes(obsoleteCodeReq);
		if (!CollectionUtils.isEmpty(giftCodes)) {
			log.info("作废激活码成功{}", giftCode.getActCode());
			return true;
		}
		return false;
	}
	/**
	 * 更新权益ID为erp系统的ID
	 *
	 * @param giftCode
	 * @return
	 */
	@Override
	public boolean updateGiftCode(GiftCode giftCode) {
		return giftCodeMapper.updateGiftCode(giftCode);
	}
	public boolean updateIdByUnitId(GiftCode giftCode) {
		final int row=giftCodeMapper.updateIdByUnitId(giftCode);
		return row==1;
	}

	@Override
	public GiftCode selectGiftCodeInfo(Integer giftCodeId) {
		GiftCode giftCode = giftCodeMapper.selectById(giftCodeId);
		return giftCode;
	}

	@Override
	public void deleteRedis(Integer giftCodeId) {
		redisTemplate.delete(RedisKeys.MARS_INSERTRESERVORDER_GIFT_CODE + giftCodeId);
	}

	/*
	 * 激活码延期
	 * */
	@Transactional(rollbackFor=Exception.class)
	@Override
	public void prolongGiftCode(GiftCodeProlongReq req) {
		Assert.notNull(req, "传入参数不能为空！");
		List<Integer> giftCodeIds;
		Assert.isTrue((giftCodeIds = req.getGiftCodeIds()) != null && !giftCodeIds.isEmpty(), "需要延期的激活码id不能为空！");
		String prolongType = req.getProlongType();
		Assert.hasText(prolongType,"延长模式不能为空");
		String prolongDate=req.getProlongDate();
		Assert.hasText(prolongDate,"延长时间不能为空");
		//根据激活码ids 查找激活码信息列表
		List<GiftCode> giftCodeList = giftCodeMapper.selectList(new Wrapper() {
			@Override
			public String getSqlSegment() {
				return "where id IN ('" + StringUtils.join(giftCodeIds, "','") + "')";
			}
		});
		Assert.notNull(giftCodeList, "没有查到相关激活码信息！");
		Integer oldActCodeStatus;
		//保存的延期信息
		List<GiftCodeProlong> giftCodeProlongList = new ArrayList<>();
		List<GiftCode> updateGiftCodeList = new ArrayList<>();
		GiftCodeProlong prolong;
		for (GiftCode giftCode : giftCodeList) {
			//无法延长未出库的有效期
			oldActCodeStatus = giftCode.getActCodeStatus();
			Assert.isTrue(oldActCodeStatus!=ActCodeStatusEnum.ActCodeStatus.CREATE.getIndex(),"该激活码未出库，无法延期！");
			Assert.isTrue(oldActCodeStatus!=ActCodeStatusEnum.ActCodeStatus.RUN_OUT.getIndex(),"该激活码已用完，无法延期！");
			Assert.isTrue(oldActCodeStatus!=ActCodeStatusEnum.ActCodeStatus.RETURN.getIndex(),"该激活码已退货，无法延期！");
			Assert.isTrue(oldActCodeStatus!=ActCodeStatusEnum.ActCodeStatus.OBSOLETE.getIndex(),"该激活码已作废，无法延期！");
			//插入激活码延期表
			prolong = getInsertGiftCodeProlong(prolongType,prolongDate,giftCode.getId(),giftCode.getActExpireTime(), giftCode.getActEndTime(),req.getRemarks());
			giftCodeProlongList.add(prolong);
			//更新激活码表
			updateGiftCodeList.add(getUpdateGiftCode(giftCode,prolong.getProlongExpireTime(),prolong.getProlongEndTime()));

            //对EquityCodeDetail表进行更新操作
            Wrapper detailWrapper = new Wrapper() {
                @Override
                public String getSqlSegment() {
                    return "where del_flag = 0 and gift_code_id ="+giftCode.getId();
                }
            };
            List<EquityCodeDetail> detailList = equityCodeDetailService.selectList(detailWrapper);
            if (!CollectionUtils.isEmpty(detailList)){
                //按产品组分类入map
                Map<Integer,List<EquityCodeDetail>> map = Maps.newHashMap();
                for (EquityCodeDetail equityCodeDetail : detailList) {
                    Integer key = equityCodeDetail.getProductGroupId();
                    List<EquityCodeDetail> list = Lists.newLinkedList();
                    if (map.containsKey(key)){
                        list = map.get(key);
                    }
                    list.add(equityCodeDetail);
                    map.put(key,list);
                }
                //针对不同类型的权益修改或新增权益明细
                if (!CollectionUtils.isEmpty(map)){
                    for (Map.Entry<Integer, List<EquityCodeDetail>> entry : map.entrySet()) {
                        List<EquityCodeDetail> list = entry.getValue();
                        if (!CollectionUtils.isEmpty(list)){
                            //循环周期权益（到期时间大于权益最大截止时间则修改最大权益明细到期时间，并插入后续时间的权益）
                            if (list.get(0).getType().compareTo(GiftCodeDetailTypeEnum.CYCLE_TYPE.getcode()) == 0){
                                //找出list里最大的权益截止日期数据
                                list.sort(new Comparator<EquityCodeDetail>() {
                                    @Override
                                    public int compare(EquityCodeDetail o1, EquityCodeDetail o2) {
                                        return o2.getEndTime().compareTo(o1.getEndTime());
                                    }
                                });
                                EquityCodeDetail equityCodeDetail = list.get(0);
                                Date oldEndTime = equityCodeDetail.getEndTime();
                                Date expiryDate = giftCode.getActExpireTime();
                                oldEndTime = DateUtil.parse(DateUtil.format(oldEndTime,"yyyy-MM-dd"),"yyyy-MM-dd");
                                expiryDate = DateUtil.parse(DateUtil.format(expiryDate,"yyyy-MM-dd"),"yyyy-MM-dd");
                                //拿到需要延期到的时间 将expiryDate替换成它
                                   expiryDate = prolong.getProlongExpireTime();
                                //到期时间大于权益最大截止时间
                                if (oldEndTime.before(expiryDate)){
                                    JSONObject jsonObject = JSONObject.parseObject(equityCodeDetail.getGroupDetail());
                                    Integer cycleTime = Integer.valueOf(jsonObject.getString("cycleTime"));
                                    Integer cycleTypeInt = Integer.valueOf(jsonObject.getString("cycleType"));
                                    Integer productGroupId = Integer.valueOf(jsonObject.getString("id"));
                                    Integer cycleNum = Integer.valueOf(jsonObject.getString("cycleNum"));
                                    int cycleType = 0;
                                    if (cycleTypeInt.compareTo(CycleTypeEnums.CYCLE_DAY.getCode()) == 0){
                                        cycleType = Calendar.DAY_OF_MONTH;
                                    }else if (cycleTypeInt.compareTo(CycleTypeEnums.CYCLE_WEEK.getCode()) == 0){
                                        cycleType = Calendar.WEEK_OF_MONTH;
                                    }else if (cycleTypeInt.compareTo(CycleTypeEnums.CYCLE_MONTH.getCode()) == 0){
                                        cycleType = Calendar.MONTH;
                                    }else if (cycleTypeInt.compareTo(CycleTypeEnums.CYCLE_YEAR.getCode()) == 0){
                                        cycleType = Calendar.YEAR;
                                    }
                                    //更新过去权益明细的最大截止时间
                                    Date oldStartTime = DateUtil.parse(DateUtil.format(equityCodeDetail.getStartTime(),"yyyy-MM-dd"),"yyyy-MM-dd");
                                    Date nowDate = oldStartTime;
                                    Calendar calendar = Calendar.getInstance();
                                    calendar.setTime(nowDate);
                                    calendar.add(cycleType,cycleTime);
                                    if (calendar.getTime().compareTo(expiryDate) >= 0){
                                        nowDate = expiryDate;
                                    }else {
                                        nowDate = calendar.getTime();
                                    }
                                    equityCodeDetail.setEndTime(nowDate);
                                    equityCodeDetailService.updateById(equityCodeDetail);

                                    //新增权益明细信息
                                    List<EquityCodeDetail> codeDetails = Lists.newLinkedList();
                                    while (nowDate.before(expiryDate)){
                                        Date tempDate = nowDate;
                                        calendar.setTime(nowDate);
                                        calendar.add(cycleType,cycleTime);
                                        if (calendar.getTime().compareTo(expiryDate) >= 0){
                                            nowDate = expiryDate;
                                        }else {
                                            nowDate = calendar.getTime();
                                        }
                                        EquityCodeDetail equity = new EquityCodeDetail();
                                        equity.setGiftCodeId(giftCode.getId());
                                        equity.setGoodsId(giftCode.getGoodsId());
                                        equity.setProductGroupId(productGroupId);
                                        equity.setUseCount(0);
                                        equity.setType(GiftCodeDetailTypeEnum.CYCLE_TYPE.getcode());
                                        equity.setGroupDetail(equityCodeDetail.getGroupDetail());
                                        equity.setTotalCount(equityCodeDetail.getTotalCount());
                                        equity.setStartTime(tempDate);
                                        equity.setEndTime(nowDate);
                                        equity.setCycleCount(cycleNum);
                                        equity.setMemberId(giftCode.getMemberId());
                                        equity.setCreateUser(SecurityUtils.getLoginName());
                                        codeDetails.add(equity);
                                    }
                                    if (!CollectionUtils.isEmpty(codeDetails)){
                                        equityCodeDetailService.insertBatch(codeDetails);
                                    }
                                }
                            }
                        }
                    }
                }
            }
		}
		int insertFlag = giftCodeProlongMapper.batchInsert(giftCodeProlongList);
		Assert.isTrue(insertFlag>0,"插入延期表失败");
		int updateFlag = giftCodeMapper.batchUpdateById(updateGiftCodeList);
		Assert.isTrue(updateFlag>0,"更新激活码表失败");

	}

	/**
	 * 根据act_code获取激活码信息
	 * @param actCode
	 * @return
	 */
	@Override
	public GiftCode selectGiftCodeByActCode(String actCode) {
		GiftCode giftCode = giftCodeMapper.selectGiftCodeByActCode(actCode);
		return giftCode;
	}

	/*
	 * 组合需要插入的激活码延期表
	 * */
	private GiftCodeProlong getInsertGiftCodeProlong(String prolongType, String prolongDate, Integer giftCodeId, Date actExpireTime, Date actEndTime, String remarks){
		GiftCodeProlong prolong = new GiftCodeProlong();
		prolong.setGiftCodeId(giftCodeId);
		prolong.setActExpireTime(actExpireTime);
		prolong.setRemarks(remarks);
		Date prolongExpireTime=null;
		Date prolongEndTime=null;
		switch (prolongType) {
			case GiftCodeRuleConstant.FIXED_DATE:
				Assert.isTrue(HelpUtils.validStrFormat(prolongDate,HelpUtils.dateReg),"延长日期格式有误,必须为日期格式");
				if(actExpireTime!=null){
					prolongExpireTime = DateUtil.parseDate(prolongDate);
				}
				if(actEndTime!=null){
					prolongEndTime = DateUtil.parseDate(prolongDate);
				}
				break;
			case GiftCodeRuleConstant.XM:
				Assert.isTrue(HelpUtils.validStrFormat(prolongDate,HelpUtils.numReg),"延长日期格式有误,必须为数字");
				if(actExpireTime!=null){
					prolongExpireTime =  DateUtil.offset(actExpireTime, DateField.MONTH, Integer.parseInt(prolongDate));
				}
				if(actEndTime!=null){
					prolongEndTime =  DateUtil.offset(actEndTime, DateField.MONTH, Integer.parseInt(prolongDate));
				}
				break;
			default:
				throw new IllegalArgumentException("延长日期类型有误");
		}
		prolong.setProlongExpireTime(prolongExpireTime);
        prolong.setProlongEndTime(prolongEndTime);
        log.info("prolongExpireTime:{},prolongEndTime:{}",prolongExpireTime,prolongEndTime);
		prolong.setProlongRule(prolongType + ":" +prolongDate);
		return prolong;
	}

	/*
	 * 组合需要更新的giftcode对象
	 * */
	private GiftCode getUpdateGiftCode(GiftCode giftCode,Date prolongExpireTime,Date prolongActEndTime){
		GiftCode updateGiftCode = new GiftCode();
		updateGiftCode.setId(giftCode.getId());
		updateGiftCode.setActExpireTime(prolongExpireTime);
		updateGiftCode.setActEndTime(prolongActEndTime);
		String tags = giftCode.getTags();
		if(StringUtils.isEmpty(tags)){
			tags="延期";
		}else if(!tags.contains("延期")){
			tags+=",延期";
		}
		updateGiftCode.setTags(tags);
		updateGiftCode.setActCodeStatus(getActCodeStatusAfterProlong(giftCode,prolongExpireTime));
		return updateGiftCode;
	}
	/*
	 * 获取延期后激活码状态
	 * */
	private Integer getActCodeStatusAfterProlong(GiftCode giftCode,Date prolongExpireTime) {
		Integer oldActCodeStatus = giftCode.getActCodeStatus();
		Integer actCodeStatus = oldActCodeStatus;
		//只有已过期的情况需要改变
		if (ActCodeStatusEnum.ActCodeStatus.PAST.getIndex() == oldActCodeStatus) {
			if(prolongExpireTime.compareTo(new Date())<0){
				//延期比当前日期小 还是已过期
				actCodeStatus = ActCodeStatusEnum.ActCodeStatus.PAST.getIndex();
			}else if (giftCode.getActOutDate() == null) {
				actCodeStatus = ActCodeStatusEnum.ActCodeStatus.CREATE.getIndex();
			} else if (giftCode.getActCodeTime() == null) {
				actCodeStatus = ActCodeStatusEnum.ActCodeStatus.OUT.getIndex();
			} else {
				actCodeStatus = ActCodeStatusEnum.ActCodeStatus.ACTIVATE.getIndex();
			}
		}
		return actCodeStatus;
	}

	/**
	 * @throws Exception
	 * @Title: sendCoupon
	 * @Description: 活动发劵
	 * @author: nickal.zhang
	 * @date: 2019年9月6日 下午4:29:05
	 */
	@Override
	public Boolean sendCoupon(Integer id, Integer idType, Integer grantMode, long acId) throws Exception {
		// 根据acId查询会员信息
		CommonResultVo<MemLoginResDTO> commoMem = remoteMemberAccountService.getMemberFullInfo(acId);
		if(commoMem!=null && commoMem.getCode() == 100){
			ActivityReqVo reqVo = new ActivityReqVo();
			reqVo.setChannel(commoMem.getResult().getAcChannel());
			reqVo.setGrantMode(grantMode);
			reqVo.setId(id);
			reqVo.setIdType(idType);
			//查询正在进行的活动的卷码类型和批次等信息
			CommonResultVo<List<ActivityResVo>> commonResultVo = remoteActivityService.getActCouponConfig(reqVo);
			if (commonResultVo == null || commonResultVo.getCode() == 200) {
				log.error("活动查询异常", commonResultVo.getMsg());
			} else {
				//活动配置
				if (!commonResultVo.getResult().isEmpty()) {
					// 遍历活动 逐个活动发券
					for (ActivityResVo resVo : commonResultVo.getResult()) {
						// 获取 活动配置的 优惠券信息
						for (ActivityCouponVo couponVo : resVo.getCouponVoList()) {
							// 总张数
							int total = couponVo.getGrantLimit();
							// 每次发的张数
							int perCnt = couponVo.getUseLimit();
							// 发的次数
							int times = total % perCnt > 0 ? total / perCnt + 1 : total / perCnt;
							for (int i = 0; i < times; i++) {
								// 起始日期
								Calendar start = Calendar.getInstance();
								start.setTime(new Date());
								// 截止日期
								Calendar end = Calendar.getInstance();
								end.setTime(new Date());
								switch (couponVo.getUseLimitRate()) {
									//年份
									case "Y":
										start.add(Calendar.YEAR, couponVo.getUseLimitRateNum() * i);
										end.add(Calendar.YEAR, couponVo.getUseLimitRateNum() * (i + 1));
										break;
										//季
									case "S":
										start.add(Calendar.MONTH, couponVo.getUseLimitRateNum() * 3 * i);
										end.add(Calendar.MONTH, couponVo.getUseLimitRateNum() * 3 * (i + 1));
										break;
										//月份
									case "M":
										start.add(Calendar.MONTH, couponVo.getUseLimitRateNum() * i);
										end.add(Calendar.MONTH, couponVo.getUseLimitRateNum() * (i + 1));
										break;
										//周
									case "W":
										start.add(Calendar.DATE, couponVo.getUseLimitRateNum() * 7 * i);
										end.add(Calendar.DATE, couponVo.getUseLimitRateNum() * 7 * (i + 1));
										break;
										//天
									case "D":
										start.add(Calendar.DATE, couponVo.getUseLimitRateNum() * i);
										end.add(Calendar.DATE, couponVo.getUseLimitRateNum() * (i + 1));
										break;
									default:
										break;
								}
								//  券起始时间
								Date startTime = start.getTime();
								//  券截止时间
								Date endTime = end.getTime();
								// 组装参数
								NewSendCouponReqVo couponReqVo = new NewSendCouponReqVo();
								couponReqVo.setCpnType(couponVo.getCouponType());
								couponReqVo.setBatchId(couponVo.getBatchId());
								couponReqVo.setAcId(acId);
								couponReqVo.setValidStartTime(startTime);
								couponReqVo.setExperTime(endTime);
								//循环调用礼券中心发劵接口
								for (int j = 0; j < perCnt; j++) {
									CommonResultVo<Boolean> vo = remoteCouponCommonService.newSendCpn(couponReqVo);
									if (vo == null) {
										throw new Exception("礼券中心发劵接口异常");
									}
								}
							}
						}
					}
				}
				return true;
			}
		}
		return false;
	}

    @Override
    public void equityTimedTask() {
        //需要查询是循环周期的权益 并且是即将到期的权益向后延期一年   拿到giftCode中act_end_time  来判断是不是无限制的权益类型  然后拿到EquityCodeDetail表 循环补上权益Wrapper wrapper = new Wrapper()
        Wrapper giftCodeWrapper = new Wrapper() {
            @Override
            public String getSqlSegment() { return "where act_code_status in (1,2) and del_flag = 0 and act_end_time is null";
            }
        };
        List<GiftCode> giftCodeList = giftCodeMapper.selectList(giftCodeWrapper);
        //拿到giftCode对应的equityCodeDetail数据
        for(GiftCode giftCode : giftCodeList) {
            Wrapper detailWrapper = new Wrapper() {
                @Override
                public String getSqlSegment() {
                    return "where del_flag = 0 and gift_code_id ="+giftCode.getId();
                }
            };
            List<EquityCodeDetail> equityCodeDetailList = equityCodeDetailService.selectList(detailWrapper);
                //按产品组分类入map
                Map<Integer,List<EquityCodeDetail>> map = Maps.newHashMap();
                for (EquityCodeDetail equityCodeDetail : equityCodeDetailList) {
                    Integer key = equityCodeDetail.getProductGroupId();
                    List<EquityCodeDetail> list = Lists.newLinkedList();
                    if (map.containsKey(key)){
                        list = map.get(key);
                    }
                    list.add(equityCodeDetail);
                    map.put(key,list);
                }
            for (Map.Entry<Integer, List<EquityCodeDetail>> entry : map.entrySet()) {
                List<EquityCodeDetail> list = entry.getValue();
                if (!CollectionUtils.isEmpty(list)){
                    //循环周期权益（到期时间距离现在海油个月就重新生成一年的权益时间）
                    if (list.get(0).getType().compareTo(GiftCodeDetailTypeEnum.CYCLE_TYPE.getcode()) == 0) {
                        //找出equityCodeDetail里最大的权益截止日期数据
                        list.sort(new Comparator<EquityCodeDetail>() {
                            @Override
                            public int compare(EquityCodeDetail o1, EquityCodeDetail o2) {
                                return o2.getEndTime().compareTo(o1.getEndTime());
                            }
                        });
                        EquityCodeDetail equityCodeDetail = list.get(0);
                        Date oldEndTime = equityCodeDetail.getEndTime();
                        //拿到当前时间
                        Date nowdate = new Date();
                        oldEndTime = DateUtil.parse(DateUtil.format(oldEndTime, "yyyy-MM-dd"), "yyyy-MM-dd");
                        nowdate = DateUtil.parse(DateUtil.format(nowdate,"yyyy-MM-dd"),"yyyy-MM-dd");
                        //和最大到期时间作比较 还有不到两个月的权益进行操作
                        if((nowdate.getTime() - oldEndTime.getTime() + 1000000)/(60*60*24*1000) <= 60){
                            JSONObject jsonObject = JSONObject.parseObject(equityCodeDetail.getGroupDetail());
                            Integer cycleTime = Integer.valueOf(jsonObject.getString("cycleTime"));
                            Integer cycleTypeInt = Integer.valueOf(jsonObject.getString("cycleType"));
                            Integer productGroupId = Integer.valueOf(jsonObject.getString("id"));
                            Integer cycleNum = Integer.valueOf(jsonObject.getString("cycleNum"));
                            int cycleType = 0;
                            if (cycleTypeInt.compareTo(CycleTypeEnums.CYCLE_DAY.getCode()) == 0) {
                                cycleType = Calendar.DAY_OF_MONTH;
                            } else if (cycleTypeInt.compareTo(CycleTypeEnums.CYCLE_WEEK.getCode()) == 0) {
                                cycleType = Calendar.WEEK_OF_MONTH;
                            } else if (cycleTypeInt.compareTo(CycleTypeEnums.CYCLE_MONTH.getCode()) == 0) {
                                cycleType = Calendar.MONTH;
                            } else if (cycleTypeInt.compareTo(CycleTypeEnums.CYCLE_YEAR.getCode()) == 0) {
                                cycleType = Calendar.YEAR;
                            }
                            //更新过去权益明细的最大截止时间
                            Date oldStartTime = DateUtil.parse(DateUtil.format(equityCodeDetail.getStartTime(), "yyyy-MM-dd"), "yyyy-MM-dd");
                            Date nowDate = oldStartTime;
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(nowDate);
                            calendar.add(cycleType, cycleTime);
                            Date expiryDate = null;
                            //将此权益最后时间设置一年后
                            if (expiryDate == null) {
                                calendar.add(Calendar.YEAR, 1);
                                expiryDate = calendar.getTime();
                            }
                            if (calendar.getTime().compareTo(expiryDate) >= 0) {
                                nowDate = expiryDate;
                            } else {
                                nowDate = calendar.getTime();
                            }
                            equityCodeDetail.setEndTime(nowDate);
                            equityCodeDetailService.updateById(equityCodeDetail);
                            //新增权益明细信息
                            List<EquityCodeDetail> codeDetails = Lists.newLinkedList();
                            while (nowDate.before(expiryDate)) {
                                Date tempDate = nowDate;
                                calendar.setTime(nowDate);
                                calendar.add(cycleType, cycleTime);
                                if (calendar.getTime().compareTo(expiryDate) >= 0) {
                                    nowDate = expiryDate;
                                } else {
                                    nowDate = calendar.getTime();
                                }
                                EquityCodeDetail equity = new EquityCodeDetail();
                                equity.setGiftCodeId(giftCode.getId());
                                equity.setGoodsId(giftCode.getGoodsId());
                                equity.setProductGroupId(productGroupId);
                                equity.setUseCount(0);
                                equity.setType(GiftCodeDetailTypeEnum.CYCLE_TYPE.getcode());
                                equity.setGroupDetail(equityCodeDetail.getGroupDetail());
                                equity.setTotalCount(equityCodeDetail.getTotalCount());
                                equity.setStartTime(tempDate);
                                equity.setEndTime(nowDate);
                                equity.setCycleCount(cycleNum);
                                equity.setMemberId(giftCode.getMemberId());
                                equity.setCreateUser(SecurityUtils.getLoginName());
                                codeDetails.add(equity);
                            }
                            if (!CollectionUtils.isEmpty(codeDetails)) {
                                equityCodeDetailService.insertBatch(codeDetails);
                            }
                        }
                    }
                }
            }
        }
    }
	/**
	 * 激活码过期操作
	 * @throws Exception
	 */
	@Override
	public void optExpireGiftCode() throws Exception {
		//获取当前时间前一天的时间
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH,-1);
		Date yesterday = calendar.getTime();
		String yesterdayStr = DateUtil.format(yesterday,"yyyy-MM-dd");
		Wrapper wrapper = new Wrapper() {
			@Override
			public String getSqlSegment() {
				return "where act_code_status IN (0,1,2) AND DATE(act_expire_time) <= STR_TO_DATE('"+yesterdayStr+"','%Y-%m-%d')";
			}
		};
		List<GiftCode> giftCodeList = giftCodeMapper.selectList(wrapper);
		Integer totalNum = 0;
		if (!CollectionUtils.isEmpty(giftCodeList)){
			totalNum = giftCodeList.size();
		}
		List<String> codes = giftCodeList.stream().map(giftCode -> giftCode.getActCode()).collect(Collectors.toList());
		log.info("本次操作{}条数据:{}",totalNum, JSONObject.toJSONString(codes));
		giftCodeMapper.optExpireGiftCode(yesterdayStr);
	}

	/**
	 * 查询支付宝渠道延期的激活码
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<GiftCode> selectAlipayProLong(Map map) throws Exception {
		List<GiftCode> list = giftCodeMapper.selectAlipayProLong(map);
		return list;
	}

	@Override
	public Boolean obsoleteBoscActCodes() throws Exception {
		//1.查询bosc_bank中客户数据
		List<BoscBank> boscBankList = boscBankService.getBoscBankList();
		if(!CollectionUtils.isEmpty(boscBankList)){
			List<String> activeRemarks = Lists.newArrayList();
			boscBankList.stream().forEach(item->{
				activeRemarks.add(item.getEcif()+"_"+item.getCardProgroupNo());
			});
			//2.查询上海银行项目里所有的激活码
			List<String> codes = giftCodeMapper.selectBoscCodes(activeRemarks);
			ObsoleteCodeReq codeReq = new ObsoleteCodeReq();
			codeReq.setCodes(codes);
			//3.批量作废
			List<GiftCode> giftCodeList = this.obsoleteActCodes(codeReq);
			if(!CollectionUtils.isEmpty(giftCodeList) && giftCodeList.size() == codes.size()){
				return true;
			}
		}
		return false;
	}

	@Override
	public void frozenBoscActCodes(List<String> activeRemarks)throws Exception {
		if (!CollectionUtils.isEmpty(activeRemarks)){
			//查询激活码
			List<String> codes = giftCodeMapper.selectBoscCodes(activeRemarks);
			if(!CollectionUtils.isEmpty(codes)){
				//批量冻结
				ObsoleteCodeReq codeReq = new ObsoleteCodeReq();
				codeReq.setCodes(codes);
				codeReq.setObsoleteRemarks("上海银行冻结");
				codeReq.setActCodeStatus(ActCodeStatusEnum.ActCodeStatus.OBSOLETE.getIndex());
				codeReq.setActObsoleteDate(new Date());
				giftCodeMapper.frozenBoscActCodes(codeReq);
			}
		}
	}

	@Override
	public void thawBoscActCodes(List<String> activeRemarks) throws Exception {
		if (!CollectionUtils.isEmpty(activeRemarks)){
			//查询激活码
			List<String> codes = giftCodeMapper.selectBoscCodes(activeRemarks);
			if(!CollectionUtils.isEmpty(codes)){
				//批量解冻
				ObsoleteCodeReq codeReq = new ObsoleteCodeReq();
				codeReq.setCodes(codes);
				codeReq.setObsoleteRemarks("上海银行解冻");
				codeReq.setActCodeStatus(ActCodeStatusEnum.ActCodeStatus.ACTIVATE.getIndex());
				codeReq.setActObsoleteDate(null);
				giftCodeMapper.thawBoscActCodes(codeReq);
			}
		}
	}

	@Override
	public void replaceBoscCodes(List<Map<String, String>> req) throws Exception {
		if (!CollectionUtils.isEmpty(req)){
			for (Map<String, String> map : req) {
				for (Map.Entry<String, String> remarks : map.entrySet()) {
					String oldRemark = remarks.getKey();
					String newRemark = remarks.getValue();
					giftCodeMapper.replaceBoscCodes(oldRemark,newRemark);
				}
			}
		}
	}
}
