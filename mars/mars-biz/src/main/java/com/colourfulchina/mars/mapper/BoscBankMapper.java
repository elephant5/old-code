package com.colourfulchina.mars.mapper;

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.colourfulchina.mars.api.entity.BoscBank;
import com.colourfulchina.mars.api.entity.ReservOrder;
import org.apache.ibatis.annotations.Param;

import com.colourfulchina.mars.api.entity.BoscBankTxtEntity;
import com.colourfulchina.mars.api.vo.res.BoscBankCustInfoRes;

public interface BoscBankMapper extends BaseMapper<BoscBank> {

	public List<String> getBoscBankTxtList(@Param("banklist") List<BoscBankTxtEntity> list);

	public Integer batchInsert(@Param("banklist") List<BoscBankTxtEntity> bankList);

	/**
	 * 根据客编号获取用户所有卡信息
	 * @param ecifNo
	 * @return
	 */
	public List<BoscBank> selectCardList(@Param("ecifNo") String ecifNo);

	public List<BoscBankCustInfoRes> getBoscLinklist(@Param("ecifNo") String ecifNo);

	List<BoscBank> selectBoscBankList();

    Integer batchInsertBoscBank( List<BoscBank> list);

    List<String> selectBoscBankListByCondition(@Param("banklist")List<BoscBankTxtEntity> list);

	/**
	 * 获取数据库中存在,但是上海银行当天有效数据中不存在的数据
	 * @param list
	 * @return
	 */
	List<BoscBank> selectOnceExistBoscBankList(@Param("banklist") List<BoscBankTxtEntity> list);

	Integer updateBatchBoscBanks(@Param("banklist") List<BoscBank> boscBankList);

	Integer freezeBatchBoscBanks(@Param("banklist") List<BoscBank> boscBankList,@Param("delFlag") Integer delFlag);

	/**
	 * 以前被冻结账户再次出现在上海银行当天有效数据中的数据
	 * @param list
	 * @return
	 */
	List<BoscBank> selectBeforeFreezeBoscBankList(@Param("banklist")List<BoscBankTxtEntity> list);

	/**
	 * 以前被冻结账户再次出现在上海银行当天有效数据中,且卡产品组代码一致的数据
	 * @param list
	 * @return
	 */
	List<BoscBank> selectBeforeFreezeAndSameCardProgroupNoBoscBankList(@Param("banklist")List<BoscBankTxtEntity> list);

	/**
	 * 匹配到相同的ECIF客户号的数据
	 * @param list
	 * @return
	 */
	List<BoscBank> selectSameEcifBoscBankList(@Param("banklist") List<BoscBankTxtEntity> list);

	/**
	 * 匹配到相同的ECIF客户号，且卡产品组代码也相同的数据(排除冻结的)
	 * @param list
	 * @return
	 */
	List<BoscBank> selectSameEcifAndCardProgroupNoBoscBankList(@Param("banklist") List<BoscBankTxtEntity> list);

	/**
	 * 匹配到相同的ECIF客户号,但是统数据中的卡产品组代码在当天有效数据中不存在
	 * @param list
	 * @return
	 */
	List<BoscBank> selectSameEcifNoCardProgroupNoBoscBankList(@Param("banklist") List<BoscBankTxtEntity> list);



	/**
	 * 获取原套卡产品下的第一个产品代码的数据
	 * @param list
	 * @return
	 */
	List<BoscBank> selectFirstCardSetBoscBankList(@Param("banklist") List<BoscBankTxtEntity> list);


	BoscBank selectByCardNo(@Param("cardNo") String cardNo);

	/**
	 * 查询客编号和卡产品组编号都相同,但是姓名或手机号修改过的数据
	 * @param list
	 * @return
	 */
    List<BoscBank> selectDiffBoscBankList(@Param("bankList") List<BoscBankTxtEntity> list);

	BoscBank selectDiffBoscBank(BoscBankTxtEntity boscBankTxtEntity);
}
