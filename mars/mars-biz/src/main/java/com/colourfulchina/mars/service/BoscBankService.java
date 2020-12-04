package com.colourfulchina.mars.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.colourfulchina.mars.api.entity.BoscBank;
import com.colourfulchina.mars.api.entity.BoscBankTxtEntity;
import com.colourfulchina.mars.api.entity.ReservOrder;
import com.colourfulchina.mars.api.vo.BoscCardSet;
import com.colourfulchina.mars.api.vo.req.GetBoscBanksReqVo;
import com.colourfulchina.mars.api.vo.req.QueryBoscCardReqVo;
import com.colourfulchina.mars.api.vo.res.BoscBankCustInfoRes;
import com.colourfulchina.mars.api.vo.res.QueryBoscCardResVo;

public interface BoscBankService  extends IService<BoscBank> {
	
	public List<String> getBoscBankTxtList(List<BoscBankTxtEntity> list) throws Exception;
	
	public Integer batchInsert(List<BoscBankTxtEntity> list) throws Exception;

	/**
	 * 根据客编号获取用户所有卡信息
	 * @param reqVo ecifNo 客编号
	 * @return
	 */
	QueryBoscCardResVo getCardList(QueryBoscCardReqVo reqVo) throws Exception;

	public List<BoscBankCustInfoRes> getBoscLinklist(String ecifNo);

	List<BoscBank> getBoscBankList();

	List<BoscBank> batchInsertBoscBank(List<BoscBank> list);

	/**
	 * 根据条件筛选获取BoscBank数据
	 * @param reqVo
	 * @return
	 */
	List<BoscBank> getBoscBanksByCondition(GetBoscBanksReqVo reqVo);

	/**
	 * 批量更新BoscBank数据
	 * @param boscBankList
	 * @return
	 */
    Boolean updateBatchBoscBanks(List<BoscBank> boscBankList);

	/**
	 * 批量冻结BoscBank数据
	 * @param boscBankList
	 * @return
	 */
	Boolean freezeBatchBoscBanks(List<BoscBank> boscBankList,Integer delFlag);


	BoscCardSet getCardSetInfo(String cardNo);
}
