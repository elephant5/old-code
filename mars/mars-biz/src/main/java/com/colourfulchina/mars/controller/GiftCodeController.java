package com.colourfulchina.mars.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.colourfulchina.god.door.api.annotation.SysGodDoorLog;
import com.colourfulchina.god.door.api.util.SecurityUtils;
import com.colourfulchina.inf.base.utils.MaskUtils;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.inf.base.vo.PageVo;
import com.colourfulchina.mars.api.entity.GiftCode;
import com.colourfulchina.mars.api.enums.ActCodeStatusEnum;
import com.colourfulchina.mars.api.vo.req.*;
import com.colourfulchina.mars.api.vo.res.CheckCodesRes;
import com.colourfulchina.mars.api.vo.res.GenerateActCodeRes;
import com.colourfulchina.mars.api.vo.res.GiftCodePageRes;
import com.colourfulchina.mars.api.vo.res.SendGiftCodeResVo;
import com.colourfulchina.mars.config.FileDownloadProperties;
import com.colourfulchina.mars.constants.GiftCodeConstants;
import com.colourfulchina.mars.service.GiftCodeService;
import com.colourfulchina.mars.service.GiftService;
import com.colourfulchina.mars.service.PanguInterfaceService;
import com.colourfulchina.mars.utils.CheckSignUtils;
import com.colourfulchina.mars.utils.CodeUtils;
import com.colourfulchina.member.api.feign.RemoteLoginService;
import com.colourfulchina.member.api.res.MemLoginResDTO;
import com.colourfulchina.pangu.taishang.api.dto.GoodsPortalSettingDto;
import com.colourfulchina.pangu.taishang.api.entity.Goods;
import com.colourfulchina.pangu.taishang.api.feign.RemoteGoodsPortalSettingService;
import com.colourfulchina.pangu.taishang.api.feign.RemoteGoodsService;
import com.colourfulchina.pangu.taishang.api.vo.req.GoodsBaseVo;
import com.colourfulchina.pangu.taishang.api.vo.res.GoodsChannelRes;
import com.colourfulchina.tianyan.admin.api.entity.SysDict;
import com.colourfulchina.tianyan.admin.api.feign.RemoteDictService;
import com.colourfulchina.tianyan.common.core.util.R;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/giftCode")
public class GiftCodeController extends BaseController {

    private final static int SUCCESS_CODE = 100;
    
    private final static int ERROR_CODE = 200;
    @Autowired
    private GiftCodeService giftCodeService;
    @Autowired
    private PanguInterfaceService panguInterfaceService;
    @Autowired
    private FileDownloadProperties fileDownloadProperties;
    
	private final RemoteLoginService remoteLoginService;
	
	private final RemoteGoodsService remoteGoodsService;
	
	private final RemoteDictService remoteDictService;

	private final RemoteGoodsPortalSettingService remoteGoodsPortalSettingService;

	@Autowired
    private GiftService giftService;


    @SysGodDoorLog("激活码的生成")
	@ApiOperation("激活码的生成")
	@PostMapping("/generateActCode")
	public CommonResultVo<GenerateActCodeRes> generateActCode(@RequestBody ActCodeReq actCodeReq){
        CommonResultVo<GenerateActCodeRes> result = new CommonResultVo();
        GenerateActCodeRes generateActCodeRes = new GenerateActCodeRes();
        try {
            Assert.notNull(actCodeReq,"参数不能为空");
            Assert.notNull(actCodeReq.getGoodsId(),"商品id不能为空");
            Assert.notNull(actCodeReq.getActCodeNum(),"激活码生成数量不能为空");
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
                giftCode.setCreateUser(SecurityUtils.getLoginName());
                list.add(giftCode);
            }
            giftCodeService.insertBatch(list);
            //生成激活码
            for (GiftCode giftCode : list) {
                giftCode.setActCodeStatus(ActCodeStatusEnum.ActCodeStatus.CREATE.getIndex());
                giftCode.setActCode(CodeUtils.getCodeByRedis(GiftCodeConstants.GIFT_ACT_CODE));
            }
            giftCodeService.updateBatchById(list);
            generateActCodeRes.setCodeBatchNo(batchNo);
            generateActCodeRes.setGoodsId(actCodeReq.getGoodsId());
            result.setResult(generateActCodeRes);
        }catch (Exception e){
            log.error("激活码生成失败",e);
            result.setCode(ERROR_CODE);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    @SysGodDoorLog("激活码分页查询")
    @ApiOperation("激活码分页查询")
    @PostMapping("/selectPageList")
    public CommonResultVo<PageVo<GiftCodePageRes>> selectPageList(@RequestBody PageVo<GiftCodePageReq> pageReq){
	    CommonResultVo<PageVo<GiftCodePageRes>> result = new CommonResultVo();
	    try {
	        PageVo<GiftCodePageRes> pageRes = giftCodeService.selectPageList(pageReq);
            if(!CollectionUtils.isEmpty(pageRes.getRecords())){
                for(GiftCodePageRes vo : pageRes.getRecords()){
                    vo.setPeopleName(MaskUtils.nameMask(vo.getPeopleName()));
                    if(StringUtils.isNotBlank(vo.getPhone())){
                        vo.setPhone(MaskUtils.allMaskPhone(vo.getPhone()));
                    }

                }
            }
	        result.setResult(pageRes);
        }catch (Exception e){
	        log.error("激活码模糊分页查询失败",e);
	        result.setCode(ERROR_CODE);
	        result.setMsg(e.getMessage());
        }
        return result;
    }

    @SysGodDoorLog("激活码导出")
    @ApiOperation("激活码导出")
    @PostMapping("/exportList")
    public CommonResultVo<String> exportList(@RequestBody PageVo<GiftCodePageReq> pageReq){
        CommonResultVo<String> result = new CommonResultVo();
        try {
            String url = giftCodeService.selectExportList(pageReq.getCondition());
            result.setResult(url);
        }catch (Exception e){
            log.error("激活码列表导出失败",e);
            result.setCode(ERROR_CODE);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    @SysGodDoorLog("激活码出库导出")
    @ApiOperation("激活码出库导出")
    @PostMapping("/exportOutCodeExcel")
    public CommonResultVo<String> exportOutCodeExcel(@RequestBody List<String> codes){
        CommonResultVo<String> result = new CommonResultVo();
        try {
            Assert.notEmpty(codes,"激活码不能为空");
            Wrapper codeWrapper = new Wrapper() {
                @Override
                public String getSqlSegment() {
                    return "where act_code IN ('"+ StringUtils.join(codes,"','") +"')";
                }
            };
            List<GiftCode> list = giftCodeService.selectList(codeWrapper);
            if(CollectionUtils.isEmpty(list)){
                result.setCode(ERROR_CODE);
                result.setMsg("无数据导出！");
                return result;
            }
            List<GiftCodePageRes> resList = Lists.newLinkedList();

            Set<Integer> idList = list.stream().map(code -> code.getGoodsId()).collect(Collectors.toSet());
            List<Integer> ids = Lists.newLinkedList(idList);
            List<Goods> remoteList = panguInterfaceService.selectGoodsNameByIds(ids);
            if(CollectionUtils.isEmpty(remoteList)){
                result.setCode(ERROR_CODE);
                result.setMsg("无法查询当前项目！");
                return result;
            }
            Goods goods = remoteList.get(0);
            for (GiftCode giftCode : list) {
                GiftCodePageRes giftCodePageRes = new GiftCodePageRes();
                BeanUtils.copyProperties(giftCode,giftCodePageRes);
                giftCodePageRes.setGoodsName(giftCodePageRes.getGoodsId()+"#"+goods.getName());
                if (giftCodePageRes.getSalesChannelId() != null){
                    GoodsChannelRes goodsChannelRes = panguInterfaceService.findChannelById(giftCodePageRes.getSalesChannelId());
                    giftCodePageRes.setSalesChannelName(goodsChannelRes.getBankName()+"/"+goodsChannelRes.getSalesChannelName()+"/"+goodsChannelRes.getSalesWayName());
                }
                resList.add(giftCodePageRes);
            }
            List<String> titleList=Lists.newArrayList();
            titleList.add("激活码");
            titleList.add("码状态");
            titleList.add("批次号");
            titleList.add("关联商品");
            titleList.add("销售渠道");
            titleList.add("有效期");
            ExcelWriter excelWriter=new ExcelWriter(true);
            String fileName=goods.getId()+"-"+goods.getShortName()+"-"+DateUtil.format(new Date(),"yyyyMMdd")+"-"+list.size()+".xlsx";
            excelWriter.setDestFile(new File(fileDownloadProperties.getPath()+"/"+fileName));

            excelWriter.setOrCreateSheet("sheet1");
            excelWriter.writeHeadRow(titleList);

            if (!CollectionUtils.isEmpty(resList)){
                resList.forEach(record->{
                    List<Object> rowData=Lists.newArrayList();
                    rowData.add(record.getActCode());
                    rowData.add(record.getCodeStatusName());
                    rowData.add(record.getCodeBatchNo());
                    rowData.add(record.getGoodsName());
                    rowData.add(record.getSalesChannelName());
                    rowData.add(record.getActEndTime() == null? null: DateUtil.format(record.getActEndTime(),"yyyy-MM-dd"));
                    excelWriter.writeRow(rowData);
                });
            }
            excelWriter.flush();
            result.setResult(fileDownloadProperties.getUrl()+fileName);
        }catch (Exception e){
            log.error("激活码生成导出失败",e);
            result.setCode(ERROR_CODE);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    @SysGodDoorLog("激活码生成导出")
    @ApiOperation("激活码生成导出")
    @GetMapping("/exportExcel/{codeBatchNo}")
    public CommonResultVo<String> exportExcel(@PathVariable String codeBatchNo){
	    CommonResultVo<String> result = new CommonResultVo();
	    try {
	        Assert.notNull(codeBatchNo,"激活码批次号不能为空");
	        Wrapper wrapper = new Wrapper() {
                @Override
                public String getSqlSegment() {
                    return "where code_batch_no = '"+codeBatchNo+"'";
                }
            };
	        List<GiftCode> list = giftCodeService.selectList(wrapper);
            if(CollectionUtils.isEmpty(list)){
                result.setCode(ERROR_CODE);
                result.setMsg("无数据导出！");
                return result;
            }
            List<GiftCodePageRes> resList = Lists.newLinkedList();

            Set<Integer> idList = list.stream().map(code -> code.getGoodsId()).collect(Collectors.toSet());
            List<Integer> ids = Lists.newLinkedList(idList);
            List<Goods> remoteList = panguInterfaceService.selectGoodsNameByIds(ids);
            if(CollectionUtils.isEmpty(remoteList)){
                result.setCode(ERROR_CODE);
                result.setMsg("无法查询当前项目！");
                return result;
            }
            Goods goods = remoteList.get(0);
            for (GiftCode giftCode : list) {
                GiftCodePageRes giftCodePageRes = new GiftCodePageRes();
                BeanUtils.copyProperties(giftCode,giftCodePageRes);
                giftCodePageRes.setGoodsName(giftCodePageRes.getGoodsId()+"#"+goods.getName());
                if (giftCodePageRes.getSalesChannelId() != null){
                    GoodsChannelRes goodsChannelRes = panguInterfaceService.findChannelById(giftCodePageRes.getSalesChannelId());
                    giftCodePageRes.setSalesChannelName(goodsChannelRes.getBankName()+"/"+goodsChannelRes.getSalesChannelName()+"/"+goodsChannelRes.getSalesWayName());
                }
                resList.add(giftCodePageRes);
            }
            List<String> titleList=Lists.newArrayList();
            titleList.add("激活码");
            titleList.add("码状态");
            titleList.add("批次号");
            titleList.add("关联商品");
            titleList.add("销售渠道");
            titleList.add("有效期");
            ExcelWriter excelWriter=new ExcelWriter(true);
            String fileName=goods.getId()+"-"+goods.getShortName()+"-"+DateUtil.format(new Date(),"yyyyMMdd")+"-"+list.size()+".xlsx";
            excelWriter.setDestFile(new File(fileDownloadProperties.getPath()+"/"+fileName));

            excelWriter.setOrCreateSheet("sheet1");
            excelWriter.writeHeadRow(titleList);

            if (!CollectionUtils.isEmpty(resList)){
                resList.forEach(record->{
                    List<Object> rowData=Lists.newArrayList();
                    rowData.add(record.getActCode());
                    rowData.add(record.getCodeStatusName());
                    rowData.add(record.getCodeBatchNo());
                    rowData.add(record.getGoodsName());
                    rowData.add(record.getSalesChannelName());
                    rowData.add(record.getActEndTime() == null? null: DateUtil.format(record.getActEndTime(),"yyyy-MM-dd"));
                    excelWriter.writeRow(rowData);
                });
            }
            excelWriter.flush();
            result.setResult(fileDownloadProperties.getUrl()+fileName);
        }catch (Exception e){
	        log.error("激活码生成导出失败",e);
	        result.setCode(ERROR_CODE);
	        result.setMsg(e.getMessage());
        }
        return result;
    }

    @SysGodDoorLog("根据激活码列表出库")
    @ApiOperation("根据激活码列表出库")
    @PostMapping("/outActCodeByCodes")
    public CommonResultVo<List<GiftCode>> outActCodeByCodes(@RequestBody OutCodeReq outCodeReq, HttpServletRequest request){
	    CommonResultVo<List<GiftCode>> result = new CommonResultVo();
	    try {
	        Assert.notNull(outCodeReq,"参数不能为空");
	        Assert.notEmpty(outCodeReq.getCodes(),"激活码不能为空");
	        Assert.notNull(outCodeReq.getSalesChannelId(),"销售渠道不能为空");
	        List<GiftCode> list = giftCodeService.outActCodeByCodes(outCodeReq,request);
	        result.setResult(list);
        }catch (Exception e){
	        log.error("激活码出库失败",e);
	        result.setCode(ERROR_CODE);
	        result.setMsg(e.getMessage());
        }
        return result;
    }

    @SysGodDoorLog("根据批次号出库")
    @ApiOperation("根据批次号出库")
    @PostMapping("/outActCodeByBatch")
    public CommonResultVo<List<GiftCode>> outActCodeByBatch(@RequestBody OutCodeReq outCodeReq, HttpServletRequest request){
        CommonResultVo<List<GiftCode>> result = new CommonResultVo();
        try {
            Assert.notNull(outCodeReq,"参数不能为空");
            Assert.notNull(outCodeReq.getCodeBatchNo(),"批次号不能为空");
            Assert.notNull(outCodeReq.getSalesChannelId(),"销售渠道不能为空");
            List<GiftCode> list = giftCodeService.outActCodeByBatch(outCodeReq,request);
            result.setResult(list);
        }catch (Exception e){
            log.error("激活码出库失败",e);
            result.setCode(ERROR_CODE);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    @SysGodDoorLog("检测激活码")
    @ApiModelProperty("检测激活码")
    @PostMapping("/checkCodes")
    public CommonResultVo<CheckCodesRes> checkCodes(@RequestBody String codes){
	    CommonResultVo<CheckCodesRes> result = new CommonResultVo();
	    try {
	        Assert.notNull(codes,"激活码不能为空");
	        CheckCodesRes checkCodesRes = giftCodeService.checkCodes("code",codes);
	        result.setResult(checkCodesRes);
        }catch (Exception e){
	        log.error("检测激活码失败",e);
	        result.setCode(ERROR_CODE);
	        result.setMsg(e.getMessage());
        }
        return result;
    }

    @SysGodDoorLog("根据批次号检测激活码")
    @ApiModelProperty("根据批次号检测激活码")
    @PostMapping("/checkCodesByBatch")
    public CommonResultVo<CheckCodesRes> checkCodesByBatch(@RequestBody String batchNo){
	    CommonResultVo<CheckCodesRes> result = new CommonResultVo();
	    try {
	        Assert.notNull(batchNo,"批次号不能为空");
	        CheckCodesRes checkCodesRes = giftCodeService.checkCodes("batch",batchNo);
	        result.setResult(checkCodesRes);
        }catch (Exception e){
	        log.error("检测激活码失败",e);
	        result.setCode(ERROR_CODE);
	        result.setMsg(e.getMessage());
        }
        return result;
    }

    @SysGodDoorLog("发码")
    @ApiOperation("发码")
    @PostMapping("/sendCode")
    public CommonResultVo<List<GiftCode>> sendCode(@RequestBody SendCodeReq sendCodeReq,HttpServletRequest request){
	    CommonResultVo<List<GiftCode>> result = new CommonResultVo();
	    try {
	        Assert.notNull(sendCodeReq,"参数不能为空");
	        Assert.notNull(sendCodeReq.getGoodsId(),"商品id不能为空");
	        Assert.notNull(sendCodeReq.getActCodeNum(),"激活码数量不能为空");
            //获取销售渠道
            List<GoodsChannelRes> goodsChannelResList = panguInterfaceService.selectGoodsChannel(sendCodeReq.getGoodsId());
            Assert.notEmpty(goodsChannelResList,"商品的销售渠道不能为空");
            //第一步，生成激活码
            ActCodeReq actCodeReq = new ActCodeReq();
            BeanUtils.copyProperties(sendCodeReq,actCodeReq);
            List<GiftCode> codeList = giftCodeService.generateActCode(actCodeReq);
            //第二步，出库激活码
            OutCodeReq outCodeReq = new OutCodeReq();
            BeanUtils.copyProperties(sendCodeReq,outCodeReq);
            outCodeReq.setSalesChannelId(goodsChannelResList.get(0).getId());
            outCodeReq.setCodes(codeList.stream().map(giftCode -> giftCode.getActCode()).collect(Collectors.toList()));
            List<GiftCode> list = giftCodeService.outActCodeByCodes(outCodeReq,request);
            result.setResult(list);
        }catch (Exception e){
	        log.error("发码失败",e);
	        result.setCode(ERROR_CODE);
	        result.setMsg(e.getMessage());
        }
        return result;
    }

    @SysGodDoorLog("对外发码")
    @ApiOperation("对外发码")
    @PostMapping("/out/sendGiftCode")
    public CommonResultVo<SendGiftCodeResVo> sendGiftCode(@RequestBody SendGiftCodeReqVo reqVo, HttpServletRequest request){
        CommonResultVo<SendGiftCodeResVo> result = new CommonResultVo();
        try {
            //验签
            Boolean validate = CheckSignUtils.validateKey(reqVo.getSign());
            if(validate){
                SendGiftCodeResVo resVo = new SendGiftCodeResVo();
                Assert.notNull(reqVo,"参数不能为空");
                Assert.notNull(reqVo.getGoodsId(),"商品id不能为空");
                Assert.notNull(reqVo.getCodeNum(),"激活码数量不能为空");
                //获取销售渠道
                List<GoodsChannelRes> goodsChannelResList = panguInterfaceService.selectGoodsChannel(reqVo.getGoodsId());
                Assert.notEmpty(goodsChannelResList,"商品的销售渠道不能为空");
                //第一步，生成激活码
                ActCodeReq actCodeReq = new ActCodeReq();
                actCodeReq.setActCodeNum(reqVo.getCodeNum());
                actCodeReq.setGoodsId(reqVo.getGoodsId());
                List<GiftCode> codeList = giftCodeService.generateActCode(actCodeReq);
                //第二步，出库激活码
                OutCodeReq outCodeReq = new OutCodeReq();
                BeanUtils.copyProperties(reqVo,outCodeReq);
                outCodeReq.setSalesChannelId(goodsChannelResList.get(0).getId());
                outCodeReq.setCodes(codeList.stream().map(giftCode -> giftCode.getActCode()).collect(Collectors.toList()));
                List<GiftCode> list = giftCodeService.outActCodeByCodes(outCodeReq,request);
                if(!CollectionUtils.isEmpty(list)){
                    Date actExpireTime = list.get(0).getActExpireTime();
                    resVo.setActExpireTime(null != actExpireTime ? DateUtil.format(actExpireTime,"yyyy-MM-dd"):"");
                }
                //获取项目的行权短链
                CommonResultVo<GoodsPortalSettingDto> commonResultVo = remoteGoodsPortalSettingService.get(reqVo.getGoodsId());
                if(null != commonResultVo && null != commonResultVo.getResult()){
                    resVo.setGiftUrl(commonResultVo.getResult().getShortUrl());
                }
                resVo.setCodeList(list);
                result.setResult(resVo);
                result.setCode(SUCCESS_CODE);
            }else {
                throw new Exception("验签失败");
            }
        }catch (Exception e){
            log.error("发码失败",e);
            result.setCode(ERROR_CODE);
            result.setMsg(e.getMessage());
        }
        return result;
    }


    @SysGodDoorLog("激活激活码")
    @ApiOperation("激活激活码")
    @PostMapping("/activeActCode")
    public CommonResultVo<GiftCode> activeActCode(@RequestBody ActiveActCodeReq activeActCodeReq){
	    CommonResultVo<GiftCode> result = new CommonResultVo();
	    try {
	        Assert.notNull(activeActCodeReq,"参数不能为空");
	        Assert.notNull(activeActCodeReq.getActCode(),"激活码不能为空");
	        Assert.notNull(activeActCodeReq.getMemberId(),"激活人会员id不能为空");
	        //检测激活码是否存在
            Wrapper giftCodeWrapper = new Wrapper() {
                @Override
                public String getSqlSegment() {
                    return "where del_flag = 0 and act_code ='"+activeActCodeReq.getActCode()+"'";
                }
            };
            List<GiftCode> giftCodeList = giftCodeService.selectList(giftCodeWrapper);
            if (CollectionUtils.isEmpty(giftCodeList)){
                result.setCode(ERROR_CODE);
                result.setMsg("激活码不存在");
            }else {
                if (giftCodeList.get(0).getActCodeStatus().compareTo(ActCodeStatusEnum.ActCodeStatus.OUT.getIndex()) == 0){
                	// 获取商品信息
                	CommonResultVo<GoodsBaseVo> remoteResult = remoteGoodsService.selectById(giftCodeList.get(0).getGoodsId());
                    if (remoteResult.getResult() == null) {
                        throw new Exception("商品不存在");
                    }
                	// 获取当前登录用户
                    MemLoginResDTO member = this.getLoginUser();
                    // 检验渠道是否正确
                    SysDict sysDict = new SysDict();
                    sysDict.setType("bank_type");
                    sysDict.setLabel(member.getAcChannel().toUpperCase());
                    R<SysDict> rs = remoteDictService.selectByType(sysDict);
                    if(rs.getData()==null) {
                    	throw new Exception("渠道不存在");
                    }
                    // 对比两者的ID 是否一致
                    if(!rs.getData().getValue().equals(remoteResult.getResult().getBankId())) {
                    	log.info("银行bankid={}, 用户channelId={}", remoteResult.getResult().getBankId(), rs.getData().getValue());
                        throw new Exception("ERROR_001:激活码无效");
                    }
                    // 过期
                    Date endTime = giftCodeList.get(0).getActEndTime() != null ? DateUtil.parse(DateUtil.format(giftCodeList.get(0).getActEndTime(),"yyyy-MM-dd"),"yyyy-MM-dd") : null;
                    Date nowTime = DateUtil.parse(DateUtil.format(new Date(),"yyyy-MM-dd"),"yyyy-MM-dd");
                    if (giftCodeList.get(0).getActEndTime() == null || endTime.compareTo(nowTime) != -1){
                        GiftCode giftCode = giftCodeService.activeActCode(activeActCodeReq);
                        result.setResult(giftCode);
                        // 激活成功，更新登录信息
                        if(member.getMbName()==null) {
                            member.setMbName(activeActCodeReq.getActiveRemarks());
                            //4.执行 更新会员的有效期  会员接口
                            CommonResultVo<MemLoginResDTO> resVo = remoteLoginService.updateMemlogInInfo(member);
                            if(resVo.getCode()==100) {
                                if(resVo.getResult().getLoginToken()!=null) {
                                    //放入缓存
                                    redisTemplate.opsForValue().set("MEM_"+resVo.getResult().getLoginToken(), resVo.getResult());
                                }
                            } else {
                                //查询接口异常
                                log.error(resVo.getMsg());
                                throw new Exception(resVo.getMsg());
                            }
                        }
                    }else {
                    	log.info("已过激活截止日期");
                        result.setCode(ERROR_CODE);
                        result.setMsg("激活码无效");
                    }
                }else {
                	log.info("激活码状态不匹配");
                    result.setCode(ERROR_CODE);
                    result.setMsg("激活码状态不匹配");
                }
            }
        }catch (Exception e){
	        log.error("激活激活码失败",e);
	        result.setCode(ERROR_CODE);
	        result.setMsg(e.getMessage());
        }
        return result;
    }

    @SysGodDoorLog("激活码退货")
    @ApiOperation("激活码退货")
    @PostMapping("/returnActCodes")
    public CommonResultVo<List<GiftCode>> returnActCodes(@RequestBody ReturnCodeReq returnCodeReq){
	    CommonResultVo<List<GiftCode>> result = new CommonResultVo();
	    try {
	        Assert.notNull(returnCodeReq,"参数不能为空");
	        Assert.notEmpty(returnCodeReq.getCodes(),"激活码不能为空");
	        List<GiftCode> list = giftCodeService.returnActCodes(returnCodeReq);
	        result.setResult(list);
        }catch (Exception e){
	        log.error("激活码退货失败",e);
	        result.setCode(ERROR_CODE);
	        result.setMsg(e.getMessage());
        }
        return result;
    }

    @SysGodDoorLog("激活码作废")
    @ApiOperation("激活码作废")
    @PostMapping("/obsoleteActCodes")
    public CommonResultVo<List<GiftCode>> obsoleteActCodes(@RequestBody ObsoleteCodeReq obsoleteCodeReq){
	    CommonResultVo<List<GiftCode>> result = new CommonResultVo();
	    try {
	        Assert.notNull(obsoleteCodeReq,"参数不能为空");
	        Assert.notEmpty(obsoleteCodeReq.getCodes(),"激活码不能为空");
	        List<GiftCode> list = giftCodeService.obsoleteActCodes(obsoleteCodeReq);
	        result.setResult(list);
        }catch (Exception e){
	        log.error("激活码作废失败",e);
	        result.setCode(ERROR_CODE);
	        result.setMsg(e.getMessage());
        }
        return result;
    }

    @SysGodDoorLog("根据gift_code的id作废激活码")
    @ApiOperation("根据gift_code的id作废激活码")
    @PostMapping("/cancelCodeById")
    public CommonResultVo<Boolean> cancelCodeById(@RequestBody CancelCodeReq req){
        CommonResultVo<Boolean> result = new CommonResultVo();
        try {
            Assert.notNull(req,"参数不能为空");
            result.setResult(giftCodeService.cancleCode(req));
        }catch (Exception e){
            log.error("激活码作废失败",e);
            result.setCode(ERROR_CODE);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    @SysGodDoorLog("激活码用完判断")
    @ApiOperation("激活码用完判断")
    @PostMapping("/actCodesIsRunOut")
    public CommonResultVo actCodesIsRunOut(Set<Integer> ids){
	    CommonResultVo result = new CommonResultVo();
	    try {
	        Assert.notEmpty(ids,"激活码id不能为空");
	        giftCodeService.actCodesIsRunOut(ids);
        }catch (Exception e){
	        log.error("激活码用完判断失败",e);
	        result.setCode(ERROR_CODE);
	        result.setMsg(e.getMessage());
        }
        return result;
    }

    @SysGodDoorLog("后台激活激活码")
    @ApiOperation("后台激活激活码")
    @PostMapping("/backendActive")
    public CommonResultVo<GiftCode> backendActiveActCode(@RequestBody ActiveActCodeReq activeActCodeReq){
	    CommonResultVo<GiftCode> result = new CommonResultVo<GiftCode>();
	    try {
	        Assert.notNull(activeActCodeReq,"参数不能为空");
	        Assert.notNull(activeActCodeReq.getActCode(),"激活码不能为空");
	        Assert.notNull(activeActCodeReq.getMemberId(),"激活人会员id不能为空");
	        //检测激活码是否存在
            Wrapper giftCodeWrapper = new Wrapper() {
                @Override
                public String getSqlSegment() {
                    return "where del_flag = 0 and act_code ='"+activeActCodeReq.getActCode()+"'";
                }
            };
            List<GiftCode> giftCodeList = giftCodeService.selectList(giftCodeWrapper);
            if (CollectionUtils.isEmpty(giftCodeList)){
                result.setCode(ERROR_CODE);
                result.setMsg("激活码不存在");
            }else {
                if (giftCodeList.get(0).getActCodeStatus().compareTo(ActCodeStatusEnum.ActCodeStatus.OUT.getIndex()) == 0){
                    // 过期
                    if (giftCodeList.get(0).getActEndTime() != null){
                        Date endTime = DateUtil.parse(DateUtil.format(giftCodeList.get(0).getActEndTime(),"yyyy-MM-dd"),"yyyy-MM-dd");
                        Date nowTime = DateUtil.parse(DateUtil.format(new Date(),"yyyy-MM-dd"),"yyyy-MM-dd");
                        if (endTime.compareTo(nowTime) != -1){
                            GiftCode giftCode = giftCodeService.activeActCode(activeActCodeReq);
                            result.setResult(giftCode);
                        }else {
                            result.setCode(ERROR_CODE);
                            result.setMsg("激活码无效");
                        }
                    }else {
                        GiftCode giftCode = giftCodeService.activeActCode(activeActCodeReq);
                        result.setResult(giftCode);
                    }
                }else {
                    result.setCode(ERROR_CODE);
                    result.setMsg("激活码状态不匹配");
                }
            }
        }catch (Exception e){
	        log.error("激活激活码失败",e);
	        result.setCode(ERROR_CODE);
	        result.setMsg(e.getMessage());
        }
        return result;
    }

    @SysGodDoorLog("激活码列表")
    @ApiOperation("激活码列表")
    @PostMapping("/codelist")
    public CommonResultVo<List<HashMap<String, Object>>> getActCodeList(@RequestBody HashMap<String, Object> map) throws Exception {
    	CommonResultVo<List<HashMap<String, Object>>> common = new CommonResultVo<List<HashMap<String, Object>>>();
    	try {
    		Assert.notNull(map,"map不能为空");
	        Assert.notEmpty((List<HashMap<String, Object>>) map.get("sonlist") ,"sonlist不能为空");
    		common.setResult(giftCodeService.getActCodeList(map));
		} catch (Exception e) {
			log.error("激活激列表失败",e);
			common.setCode(ERROR_CODE);
			common.setMsg("系统异常");
		}
		return common;
    	
    }

    @SysGodDoorLog("获取激活码信息")
    @ApiOperation("获取激活码信息")
    @PostMapping("/selectGiftCodeInfo")
    public CommonResultVo<GiftCode> selectGiftCodeInfo(@RequestBody Integer giftCodeId){
        CommonResultVo<GiftCode> common = new CommonResultVo<>();
        try {
            log.info("获取激活码信息:{}",giftCodeId);
            GiftCode giftCode = giftCodeService.selectGiftCodeInfo(giftCodeId);
            common.setResult(giftCode);
        } catch (Exception e) {
            log.error("获取激活码信息异常",e);
            common.setCode(ERROR_CODE);
            common.setMsg("系统异常");
        }
        return common;
    }


    @SysGodDoorLog("根据act_code获取激活码信息")
    @ApiOperation("根据act_code获取激活码信息")
    @PostMapping("/selectGiftCodeByActCode")
    public CommonResultVo<GiftCode> selectGiftCodeByActCode(@RequestBody String actCode){
        CommonResultVo<GiftCode> common = new CommonResultVo<>();
        try {
            log.info("获取激活码信息:{}",actCode);
            GiftCode giftCode = giftCodeService.selectGiftCodeByActCode(actCode);
            common.setResult(giftCode);
        } catch (Exception e) {
            log.error("获取激活码信息异常",e);
            common.setCode(ERROR_CODE);
            common.setMsg("系统异常");
        }
        return common;
    }

    @SysGodDoorLog("激活码延期")
    @ApiOperation("激活码延期")
    @PostMapping("/prolongGiftCode")
    public CommonResultVo<Boolean> prolongGiftCode(@RequestBody GiftCodeProlongReq req){
        CommonResultVo<Boolean> common = new CommonResultVo<>();
        try {
            log.info("激活码延期:{}",req);
            giftCodeService.prolongGiftCode(req);
            common.setResult(Boolean.TRUE);
        } catch (Exception e) {
            log.error("激活码延期异常",e);
            common.setCode(ERROR_CODE);
            common.setMsg(e.getMessage());
            common.setResult(Boolean.FALSE);
        }
        return common;
    }

    @SysGodDoorLog("无限期循环权益定时任务")
    @ApiOperation("无限期循环权益定时任务")
    @PostMapping("/equityTimedTask")
    public boolean equityTimedTask(){
	    try{
	        giftCodeService.equityTimedTask();
	    } catch (Exception e){
	        log.error("无限期循环权益定时任务异常");
        }
	    return true;
    }
    @SysGodDoorLog("激活码过期")
    @ApiOperation("激活码过期")
    @PostMapping("/optExpireGiftCode")
    public CommonResultVo<Boolean> optExpireGiftCode(){
	    CommonResultVo<Boolean> result = new CommonResultVo<>();
	    try {
	        giftCodeService.optExpireGiftCode();
	        result.setResult(Boolean.TRUE);
        }catch (Exception e){
	        log.error("激活码过期操作失败",e);
	        result.setCode(ERROR_CODE);
	        result.setMsg(e.getMessage());
        }
        return result;
    }
    @SysGodDoorLog("判断用户在指定商品下是否有有效期内的权益")
    @ApiOperation("判断用户在指定商品下是否有有效期内的权益")
    @PostMapping("/getGiftCode")
    public CommonResultVo<String> getGiftCode(@RequestBody QueryGiftReqVO reqVO){
        CommonResultVo<String> resultVo = new CommonResultVo<>();
        try {
            String code = giftService.queryGiftByAcIdAndGoodsId(reqVO);
            resultVo.setCode(SUCCESS_CODE);
            resultVo.setResult(code);
        } catch (Exception e) {
           resultVo.setCode(ERROR_CODE);
           resultVo.setMsg(e.getMessage());
        }
        return resultVo;
    }

    @SysGodDoorLog("查询支付宝渠道延期的激活码")
    @ApiOperation("查询支付宝渠道延期的激活码")
    @PostMapping("/selectAlipayProLong")
    public CommonResultVo<List<GiftCode>> selectAlipayProLong(@RequestBody Map map){
	    CommonResultVo<List<GiftCode>> result = new CommonResultVo<>();
	    try {
            List<GiftCode> list = giftCodeService.selectAlipayProLong(map);
            result.setResult(list);
        }catch (Exception e){
	        log.error("查询支付宝渠道延期的激活码失败",e);
	        result.setCode(ERROR_CODE);
	        result.setMsg(e.getMessage());
        }
        return result;
    }

    @SysGodDoorLog("上海银行批量作废激活码")
    @ApiOperation("上海银行批量作废激活码")
    @GetMapping("/obsoleteBoscActCodes")
    public CommonResultVo<Boolean> obsoleteBoscActCodes(){
        CommonResultVo<Boolean> result = new CommonResultVo();
        try {
            Boolean f = giftCodeService.obsoleteBoscActCodes();
            result.setResult(f);
            result.setCode(100);
            result.setMsg("成功");
        }catch (Exception e){
            log.error("激活码作废失败",e);
            result.setCode(ERROR_CODE);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    /**
     * 冻结上海银行激活码
     * @return
     */
    @SysGodDoorLog("冻结上海银行激活码")
    @ApiOperation("冻结上海银行激活码")
    @PostMapping("/frozenBoscActCodes")
    public CommonResultVo<Boolean> frozenBoscActCodes(@RequestBody List<String> activeRemarks){
	    CommonResultVo<Boolean> result = new CommonResultVo<>();
	    try {
	        giftCodeService.frozenBoscActCodes(activeRemarks);
	        result.setResult(Boolean.TRUE);
        }catch (Exception e){
	        log.error("冻结上海银行激活码失败",e);
	        result.setCode(ERROR_CODE);
	        result.setMsg(e.getMessage());
	        result.setResult(Boolean.FALSE);
        }
        return result;
    }

    /**
     * 解冻上海银行激活码
     * @return
     */
    @SysGodDoorLog("解冻上海银行激活码")
    @ApiOperation("解冻上海银行激活码")
    @PostMapping("/thawBoscActCodes")
    public CommonResultVo<Boolean> thawBoscActCodes(@RequestBody List<String> activeRemarks){
        CommonResultVo<Boolean> result = new CommonResultVo<>();
        try {
            giftCodeService.thawBoscActCodes(activeRemarks);
            result.setResult(Boolean.TRUE);
        }catch (Exception e){
            log.error("解冻上海银行激活码失败",e);
            result.setCode(ERROR_CODE);
            result.setMsg(e.getMessage());
            result.setResult(Boolean.FALSE);
        }
        return result;
    }

    /**
     * 替换上海银行激活码
     * @return
     */
    @SysGodDoorLog("替换上海银行激活码")
    @ApiOperation("替换上海银行激活码")
    @PostMapping("/replaceBoscCodes")
    public CommonResultVo<Boolean> replaceBoscCodes(@RequestBody List<Map<String,String>> req){
        CommonResultVo<Boolean> result = new CommonResultVo<>();
        try {
            giftCodeService.replaceBoscCodes(req);
            result.setResult(Boolean.TRUE);
        }catch (Exception e){
            log.error("替换上海银行激活码失败",e);
            result.setCode(ERROR_CODE);
            result.setMsg(e.getMessage());
            result.setResult(Boolean.FALSE);
        }
        return result;
    }
}
