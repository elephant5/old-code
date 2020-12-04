package com.colourfulchina.pangu.taishang.api.feign;

import com.colourfulchina.inf.base.constant.ServiceNameConstant;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.pangu.taishang.api.entity.Goods;
import com.colourfulchina.pangu.taishang.api.entity.GoodsClause;
import com.colourfulchina.pangu.taishang.api.entity.GoodsSetting;
import com.colourfulchina.pangu.taishang.api.entity.SalesChannel;
import com.colourfulchina.pangu.taishang.api.feign.fallback.RemoteGoodsServiceFallbackImpl;
import com.colourfulchina.pangu.taishang.api.vo.req.GoodsBaseVo;
import com.colourfulchina.pangu.taishang.api.vo.req.GoodsExpiryDateReq;
import com.colourfulchina.pangu.taishang.api.vo.res.GoodsChannelRes;
import com.colourfulchina.pangu.taishang.api.vo.res.GoodsClauseRes;
import com.colourfulchina.pangu.taishang.api.vo.res.bigan.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = ServiceNameConstant.PANGU_TAISHANG_SERVICE, fallback = RemoteGoodsServiceFallbackImpl.class)
public interface RemoteGoodsService {

	/**
	 * 根据Id查找商品
	 * @param id
	 * @return
	 */
	@GetMapping("/goods/selectById/{id}")
	CommonResultVo<GoodsBaseVo> selectById(@PathVariable(value = "id") Integer id);

	/**
	 * 查询所有商品列表
	 * @return
	 */
	@PostMapping("/goods/selectGoodsList")
	CommonResultVo<List<Goods>> selectGoodsList();

	/**
	 * 根据Ids查找商品
	 * @param ids
	 * @return
	 */
	@PostMapping("/goods/selectGoodsByIds")
	CommonResultVo<List<GoodsBaseVo>> selectGoodsByIds(@RequestBody List<Integer> ids);


	/**
	 * 根据id查找实体类的商品
	 * @param id
	 * @return
	 */
	@PostMapping("/goods/findGoodsById")
	CommonResultVo<Goods> findGoodsById(@RequestBody Integer id);

	/**
	 * 根据商品ids查询商品名称
	 * @param ids
	 * @return
	 */
	@PostMapping("/goods/selectNameByIds")
	CommonResultVo<List<Goods>> selectNameByIds(@RequestBody List<Integer> ids);

	/**
	 * 查询权益的到期日期
	 * @param queryOrderExpireTimeReq
	 * @return
	 */
	@PostMapping("/goods/queryOrderExpireTime")
	CommonResultVo<GoodsBaseVo> queryOrderExpireTime(@RequestBody GoodsExpiryDateReq queryOrderExpireTimeReq);


	@GetMapping("/goods/findByOldKey/{oldKey}")
	CommonResultVo<Goods> findByOldKey(@PathVariable(value ="oldKey") String oldKey);

	/**
	 * 根据商品id查询使用规则列表
	 * @param goodsClause
	 * @return
	 */
	@PostMapping("/goodsClause/list")
	CommonResultVo<List<GoodsClauseRes>> selectClauseList(@RequestBody GoodsClause goodsClause);

	/**
	 * 查询商品的销售渠道
	 * @param goodsId
	 * @return
	 */
	@PostMapping("/goodsChannel/selectGoodsChannel")
	CommonResultVo<List<GoodsChannelRes>> selectGoodsChannel(@RequestBody Integer goodsId);

	/**
	 * 根据大客户、销售渠道销售方式查询salesChannel
	 * @param salesChannel
	 * @return
	 */
	@PostMapping("/salesChannel/selectByBCW")
	CommonResultVo<List<SalesChannel>> selectByBCW(@RequestBody SalesChannel salesChannel);

	/**
	 * 根据大客户查询salesChannel
	 * @param salesChannel
	 * @return
	 */
	@PostMapping("/salesChannel/selectByBankIds")
	CommonResultVo<List<SalesChannel>> selectByBankIds(@RequestBody SalesChannel salesChannel);
	/**
	 * @title:selectSalesChannel
	 * @Description: 根据渠道id获取渠道信息
	 * @Param: [channelId]
	 * @return: com.colourfulchina.inf.base.vo.CommonResultVo<com.colourfulchina.pangu.taishang.api.vo.res.SalesChannelRes>
	 * @Auther: 图南
	 * @Date: 2019/6/19 9:49
	 */
	@PostMapping("/salesChannel/selectSalesChannel")
	CommonResultVo<List<Integer>> selectSalesChannel(@RequestBody String bankId);

	/**
	 * 根据销售渠道id查询销售渠道详情
	 * @param id
	 * @return
	 */
	@PostMapping("/salesChannel/findById")
	CommonResultVo<GoodsChannelRes> findById(@RequestBody Integer id);

	/**
	 * 根据商品id查询商品预约限制
	 * @param goodsId
	 * @return
	 */
	@PostMapping("/goodsSetting/selectGoodsSettingById")
	CommonResultVo<GoodsSetting> selectGoodsSettingById(@RequestBody Integer goodsId);

	/**
	 * @title:selectGoodsClauseById
	 * @Description: 根据goodsId获取商品扩展信息
	 * @Param: [goodsId]
	 * @return: com.colourfulchina.inf.base.vo.CommonResultVo<com.colourfulchina.pangu.taishang.api.vo.req.GoodsBaseVo>
	 * @Auther: 图南
	 * @Date: 2019/6/26 16:01
	 */
	@PostMapping("/goodsClause/selectGoodsClauseById")
	CommonResultVo<GoodsBaseVo> selectGoodsClauseById(@RequestBody Integer goodsId);

	/**
	 * 查询所有销售渠道列表详情
	 * @returngiftTimesVo
	 */
	@PostMapping("/salesChannel/selectSalesChannelList")
	CommonResultVo<List<GoodsChannelRes>> selectSalesChannelList();

	@GetMapping("/goods/getGoodsBaseById/{id}")
	CommonResultVo<GoodsBaseVo> getGoodsBaseById(@PathVariable(value = "id") Integer id);

	@GetMapping({"/bigan-replace/project/prjList/{projectIds}"})
	CommonResultVo<List<ProjectCdnVo>> prjBriefList(@PathVariable(value = "projectIds",required = false) String projectIds);

	@GetMapping({"/bigan-replace/project/cdnList/{projectIds}"})
	CommonResultVo<List<ProjectCdnVo>> prjList(@PathVariable(value = "projectIds",required = false) String projectIds);

	@GetMapping("/bigan-replace/project/getGoodsDetail/{pgpId}")
	public CommonResultVo<GoodsDetailVo> getGoodsDetail(@PathVariable("pgpId") Long pgpId);

	@GetMapping({"/bigan-replace/project/getProjectById/{id}"})
	CommonResultVo<Project> getProjectById(@PathVariable(value = "id") Integer id);

	@PostMapping("/bigan-replace/bookOrder/queryGiftBlockList")
	CommonResultVo<List<GoodsBlockVo>> queryGiftBlockList(@RequestBody BookOrderReqVo bookOrderReqVo);

	//支付宝产品预约时间范围及价格组装
	@PostMapping("/bigan-replace/bookOrder/prepareAlipayBookPrice")
	CommonResultVo<AlipayBookPriceVo> prepareAlipayBookPrice(@RequestBody Integer productGroupProductId);

	@GetMapping("/bigan-replace/bookOrder/getBlockRule/{pgpId}")
	public CommonResultVo<String> getBlockRule(@PathVariable(value = "pgpId") Integer pgpId);
}
