package com.colourfulchina.mars.controller;

import java.util.List;

import cn.hutool.core.date.DateUnit;
import com.alibaba.fastjson.JSONObject;
import com.colourfulchina.mars.api.entity.BoscBank;
import com.colourfulchina.mars.api.vo.BoscCardSet;
import com.colourfulchina.mars.api.vo.req.BookPayReq;
import com.colourfulchina.mars.api.vo.req.GetBoscBanksReqVo;
import com.colourfulchina.mars.api.vo.res.PriceRes;
import com.colourfulchina.mars.service.ReservOrderAttachService;
import com.colourfulchina.pangu.taishang.api.vo.res.bookBasePayment.BookBasePaymentRes;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import com.colourfulchina.god.door.api.annotation.SysGodDoorLog;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.mars.api.entity.BoscBankTxtEntity;
import com.colourfulchina.mars.api.vo.req.BoscHotelListReq;
import com.colourfulchina.mars.api.vo.req.QueryBoscCardReqVo;
import com.colourfulchina.mars.api.vo.res.BoscBankCustInfoRes;
import com.colourfulchina.mars.api.vo.res.QueryBoscCardResVo;
import com.colourfulchina.mars.service.BoscBankService;
import com.colourfulchina.mars.service.PanguInterfaceService;
import com.colourfulchina.mars.utils.BoscDecryptUtils;
import com.colourfulchina.pangu.taishang.api.entity.BookBasePayment;
import com.colourfulchina.pangu.taishang.api.vo.req.SelectBookPayReq;
import com.colourfulchina.pangu.taishang.api.vo.req.SelectShopByGroupServiceReq;
import com.colourfulchina.pangu.taishang.api.vo.res.SelectProductByGroupServiceRes;
import com.google.common.collect.Lists;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/boscbank")
@Slf4j
public class BoscBankController {
	
	@Autowired
	private BoscBankService boscBankService;

	@Autowired
	private ReservOrderAttachService reservOrderAttachService;
	@Autowired
	private PanguInterfaceService panguInterfaceService;

	@SysGodDoorLog("上海银行获取银行文件列表")
	@PostMapping("/list")
	public CommonResultVo<List<String>> getBoscBankTxtList(@RequestBody List<BoscBankTxtEntity> list) 
			throws Exception {
		CommonResultVo<List<String>> common = new CommonResultVo<List<String>>();
		try {
			common.setCode(100);
			common.setMsg("成功");
			common.setResult(boscBankService.getBoscBankTxtList(list));
		} catch (Exception e) {
			log.error(e.getMessage());
			common.setCode(200);
			common.setMsg(e.getMessage());
		}
		return common;
	}

	@SysGodDoorLog("上海银行批量插入")
	@PostMapping("/insert")
	public CommonResultVo<Integer> batchInsert(@RequestBody List<BoscBankTxtEntity> list) 
			throws Exception {
		CommonResultVo<Integer> common = new CommonResultVo<Integer>();
		try {
			common.setCode(100);
			common.setMsg("成功");
			common.setResult(boscBankService.batchInsert(list));
		} catch (Exception e) {
			log.error(e.getMessage());
			common.setCode(200);
			common.setMsg(e.getMessage());
		}
		return common;
	}

	@SysGodDoorLog("根据客编号获取用户所有卡信息")
	@ApiOperation("根据客编号获取用户所有卡信息")
	@PostMapping(value = "/getBoscCardList")
	public CommonResultVo<QueryBoscCardResVo> getBoscCardList(@RequestBody QueryBoscCardReqVo reqVo){
		CommonResultVo<QueryBoscCardResVo> common = new CommonResultVo<QueryBoscCardResVo>();
		try {
			QueryBoscCardResVo result = boscBankService.getCardList(reqVo);
			common.setCode(100);
			common.setMsg("成功");
			common.setResult(result);
		} catch (Exception e) {
			log.error(e.getMessage());
			common.setCode(200);
			common.setMsg(e.getMessage());
		}
		return common;
	}

	@SysGodDoorLog("一次解密银行app端登录后返回的客编号")
	@ApiOperation("一次解密银行app端登录后返回的客编号")
	@PostMapping(value = "/decryptEcifNo")
	public CommonResultVo<String> decryptEcifNo(@RequestBody QueryBoscCardReqVo reqVo) throws Exception {
	    if(null == reqVo || StringUtils.isEmpty(reqVo.getEcifNo())){
	        throw new Exception("客编号不能为空");
        }
		CommonResultVo<String> resultVo = new CommonResultVo<String>();
		try {
            String ecifNo = BoscDecryptUtils.decryEcifNo(reqVo.getEcifNo());
            resultVo.setCode(100);
            resultVo.setMsg("成功");
            resultVo.setResult(ecifNo);
		} catch (Exception e) {
			log.error(e.getMessage());
            resultVo.setCode(200);
            resultVo.setMsg(e.getMessage());
		}
		return resultVo;
	}

	@SysGodDoorLog("上海银行酒店列表")
	@ApiOperation("上海银行酒店列表")
	@PostMapping("/boscHotelList")
	public CommonResultVo<List<SelectProductByGroupServiceRes>> boscHotelList(@RequestBody BoscHotelListReq boscHotelListReq){
		CommonResultVo<List<SelectProductByGroupServiceRes>> result = new CommonResultVo<>();
		try {
			Assert.notNull(boscHotelListReq,"参数不能为空");
			Assert.notNull(boscHotelListReq.getService(),"资源类型不能为空");
			Assert.notEmpty(boscHotelListReq.getProductGroupIds(),"产品组id不能为空不能为空");
			List<SelectProductByGroupServiceRes> resList = Lists.newLinkedList();
			for (Integer integer : boscHotelListReq.getProductGroupIds()) {
				SelectShopByGroupServiceReq selectShopByGroupServiceReq = new SelectShopByGroupServiceReq();
				selectShopByGroupServiceReq.setService(boscHotelListReq.getService());
				selectShopByGroupServiceReq.setProductGroupId(integer);
				List<SelectProductByGroupServiceRes> list = panguInterfaceService.selectProductByGroupService(selectShopByGroupServiceReq);
				resList.addAll(list);
			}
			result.setResult(resList);
		}catch (Exception e){
			log.error("根据产品组id合资源类型查询产品组产品详情失败",e);
			result.setCode(200);
			result.setMsg(e.getMessage());
		}
		return result;
	}

	@SysGodDoorLog("查询预约支付金额")
	@ApiOperation("查询预约支付金额")
	@PostMapping("/selectBookPay")
	public CommonResultVo<List<BookBasePaymentRes>> selectBookPay(@RequestBody BookPayReq req){
		CommonResultVo<List<BookBasePaymentRes>> result = new CommonResultVo<>();
		try {
			Assert.notNull(req,"参数不能为空");
			Assert.notNull(req.getProductGroupProductId(),"产品组产品id不能为空");
			Assert.hasText(req.getStartDate(),"预约开始时间不能为空");
			Assert.hasText(req.getEndDate(),"预约结束时间不能为空");
			List<BookBasePaymentRes> bookBasePayments = reservOrderAttachService.selectBookPay(req);
			result.setResult(bookBasePayments);
		}catch (Exception e){
			log.error("查询预约支付金额失败",e);
			result.setCode(200);
			result.setMsg(e.getMessage());
		}
		return result;
	}

	@SysGodDoorLog("查询到店支付金额")
	@ApiOperation("查询到店支付金额")
	@PostMapping("/getStorePrice")
	public CommonResultVo<PriceRes> getStorePrice(@RequestBody BookPayReq req){
		CommonResultVo<PriceRes> result = new CommonResultVo<>();
		try {
			Assert.notNull(req,"参数不能为空");
			Assert.notNull(req.getProductGroupProductId(),"产品组产品id不能为空");
			Assert.hasText(req.getStartDate(),"预约时间不能为空");
			PriceRes priceRes = reservOrderAttachService.getStorePrice(req);
			result.setResult(priceRes);
		}catch (Exception e){
			log.error("查询预约支付金额失败",e);
			result.setCode(200);
			result.setMsg(e.getMessage());
		}
		return result;
	}

	@SysGodDoorLog("查询最低预约支付金额")
	@ApiOperation("查询最低预约支付金额")
	@PostMapping("/getMinPrice")
	public CommonResultVo<BookBasePaymentRes> getMinPrice(@RequestBody BookPayReq req){
		CommonResultVo<BookBasePaymentRes> result = new CommonResultVo<>();
		try {
			Assert.notNull(req,"参数不能为空");
			Assert.notNull(req.getProductGroupProductId(),"产品组产品id不能为空");
			Assert.hasText(req.getStartDate(),"预约开始时间不能为空");
			Assert.hasText(req.getEndDate(),"预约结束时间不能为空");
			BookBasePaymentRes bookBasePayments = reservOrderAttachService.getMinPrice(req);
			result.setResult(bookBasePayments);
		}catch (Exception e){
			log.error("查询预约支付金额失败",e);
			result.setCode(200);
			result.setMsg(e.getMessage());
		}
		return result;
	}
	
	@SysGodDoorLog("查询主副卡联系人")
	@ApiOperation("查询主副卡联系人")
	@PostMapping("/getBoscLinklist")
	public CommonResultVo<List<BoscBankCustInfoRes>> getBoscLinklist(@RequestBody QueryBoscCardReqVo reqVo) {
		CommonResultVo<List<BoscBankCustInfoRes>> resultVo = new CommonResultVo<List<BoscBankCustInfoRes>>();
		try {
			Assert.notNull(reqVo.getEcifNo(),"客户编号不能为空");
			List<BoscBankCustInfoRes> list = boscBankService.getBoscLinklist(reqVo.getEcifNo());
			resultVo.setCode(100);
			resultVo.setResult(list);
		} catch (Exception e) {
			log.error("查询主副卡联系人失败",e);
			resultVo.setCode(200);
			resultVo.setMsg(e.getMessage());
		}
		return resultVo;
	}

	@SysGodDoorLog("获取已经入库的上海银行数据")
	@GetMapping("/getBoscBankList")
	public CommonResultVo<List<BoscBank>> getBoscBankList() {
		CommonResultVo<List<BoscBank>> common = new CommonResultVo<List<BoscBank>>();
		try {
			common.setCode(100);
			common.setMsg("成功");
			common.setResult(boscBankService.getBoscBankList());
		} catch (Exception e) {
			log.error(e.getMessage());
			common.setCode(200);
			common.setMsg(e.getMessage());
		}
		return common;
	}

	@SysGodDoorLog("上海银行批量插入bosc_bank")
	@PostMapping("/batchInsertBoscBank")
	public CommonResultVo<List<BoscBank>> batchInsertBoscBank(@RequestBody List<BoscBank> list) {
		CommonResultVo<List<BoscBank>> common = new CommonResultVo<List<BoscBank>>();
		try {
			common.setCode(100);
			common.setMsg("成功");
			common.setResult(boscBankService.batchInsertBoscBank(list));
		} catch (Exception e) {
			log.error("e {}",e);
			common.setCode(200);
			common.setMsg(e.getMessage());
		}
		return common;
	}


	@SysGodDoorLog("根据条件筛选获取BoscBank数据")
	@PostMapping("/getBoscBanks")
	public CommonResultVo<List<BoscBank>> getBoscBanksByCondition(@RequestBody GetBoscBanksReqVo reqVo){
		CommonResultVo<List<BoscBank>> common = new CommonResultVo<List<BoscBank>>();
		try {
			common.setCode(100);
			common.setMsg("成功");
			common.setResult(boscBankService.getBoscBanksByCondition(reqVo));
		} catch (Exception e) {
			log.error("e{} ",e);
			common.setCode(200);
			common.setMsg(e.getMessage());
		}
		return common;
	}

	@SysGodDoorLog("批量更新BoscBank数据")
	@PostMapping("/updateBatchBoscBanks")
	public CommonResultVo<Boolean> updateBatchBoscBanks(@RequestBody List<BoscBank> boscBankList){
		CommonResultVo<Boolean> common = new CommonResultVo<Boolean>();
		try {
			common.setCode(100);
			common.setMsg("成功");
//			common.setResult(boscBankService.updateBatchBoscBanks(boscBankList));
			common.setResult(boscBankService.updateBatchById(boscBankList));
		} catch (Exception e) {
			log.error("e {} ",e);
			common.setCode(200);
			common.setMsg(e.getMessage());
		}
		return common;
	}

	@SysGodDoorLog("批量冻结或解封BoscBank数据")
	@PostMapping("/freezeBatchBoscBanks")
	public CommonResultVo<Boolean> freezeBatchBoscBanks(@RequestBody List<BoscBank> boscBankList,@RequestParam(value = "delFlag",required = true) Integer delFlag){
		CommonResultVo<Boolean> common = new CommonResultVo<Boolean>();
		try {
			common.setCode(100);
			common.setMsg("成功");
			common.setResult(boscBankService.freezeBatchBoscBanks(boscBankList,delFlag));
		} catch (Exception e) {
			log.error("e {} ",e);
			common.setCode(200);
			common.setMsg(e.getMessage());
		}
		return common;
	}

    @SysGodDoorLog("根据卡号查询套卡信息")
    @GetMapping("/getCardSetInfo/{cardNo}")
    public CommonResultVo<BoscCardSet> getCardSetInfo(@PathVariable("cardNo") String cardNo){
	    log.info("BoscBankController getCardSetInfo 请求参数:{}",cardNo);
        CommonResultVo<BoscCardSet> common = new CommonResultVo<>();
        try {
            common.setCode(100);
            common.setMsg("成功");
            common.setResult(boscBankService.getCardSetInfo(cardNo));
        } catch (Exception e) {
            log.error("e {} ",e);
            common.setCode(200);
            common.setMsg(e.getMessage());
        }
        log.info("BoscBankController getCardSetInfo 返回结果:{}",JSONObject.toJSONString(common));
        return common;
    }
}
