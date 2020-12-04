package com.colourfulchina.mars.service;

import com.baomidou.mybatisplus.service.IService;
import com.colourfulchina.inf.base.vo.PageVo;
import com.colourfulchina.mars.api.entity.GiftCode;
import com.colourfulchina.mars.api.vo.req.*;
import com.colourfulchina.mars.api.vo.res.CheckCodesRes;
import com.colourfulchina.mars.api.vo.res.GiftCodePageRes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface GiftCodeService extends IService<GiftCode> {

    /**
     * 生成激活码
     * @param actCodeReq
     * @throws Exception
     */
	List<GiftCode> generateActCode(ActCodeReq actCodeReq)throws Exception;

    /**
     * 激活码分页
     * @param pageReq
     * @return
     * @throws Exception
     */
	PageVo<GiftCodePageRes> selectPageList(PageVo<GiftCodePageReq> pageReq)throws Exception;

    /**
     * 激活码列表导出查询
     * @param map
     * @return
     * @throws Exception
     */
	String selectExportList(Map map)throws Exception;

    /**
     * 出库激活码，根据激活码列表
     * @param outCodeReq
     * @throws Exception
     */
	List<GiftCode> outActCodeByCodes(OutCodeReq outCodeReq, HttpServletRequest request)throws Exception;

    /**
     * 出库激活码，根据批次号
     * @param outCodeReq
     * @param request
     * @return
     * @throws Exception
     */
	List<GiftCode> outActCodeByBatch(OutCodeReq outCodeReq, HttpServletRequest request)throws Exception;

    /**
     * 检测激活码
     * @param type (code 根据激活码检测；batch 根据批次号检测)
     * @param codes
     * @return
     * @throws Exception
     */
	CheckCodesRes checkCodes(String type,String codes)throws Exception;

    /**
     * 激活激活码
     * @param activeActCodeReq
     * @return
     * @throws Exception
     */
	GiftCode activeActCode(ActiveActCodeReq activeActCodeReq)throws Exception;

    /**
     * 激活码退货
     * @param returnCodeReq
     * @throws Exception
     */
	List<GiftCode> returnActCodes(ReturnCodeReq returnCodeReq)throws Exception;

    /**
     * 激活码作废
     * @param obsoleteCodeReq
     * @return
     * @throws Exception
     */
	List<GiftCode> obsoleteActCodes(ObsoleteCodeReq obsoleteCodeReq) throws Exception;

    /**
     * 激活码是否用完逻辑流程
     * @param ids
     * @throws Exception
     */
    void actCodesIsRunOut(Set<Integer> ids) throws Exception;

	List<HashMap<String, Object>> getActCodeList(HashMap<String, Object> map) throws Exception;

    /**
     * 根据gift_code的id作废激活码
     * @param req
     * @return
     */
    Boolean cancleCode(CancelCodeReq req) throws Exception;

    /**
     * 更新权益ID为erp系统的ID
     * @param giftCode
     * @return
     */
    boolean updateGiftCode(GiftCode giftCode);
	boolean updateIdByUnitId(GiftCode giftCode);

	/**
	 * @title:selectGiftCodeInfo
	 * @Description: 获取激活码信息
	 * @Param: [giftCodeId]
	 * @return: com.colourfulchina.mars.api.entity.GiftCode
	 * @Auther: 图南
	 * @Date: 2019/7/23 17:11
	 */
    GiftCode selectGiftCodeInfo(Integer giftCodeId);

    void deleteRedis(Integer giftCodeId);

    /*
     * 激活码延期
     * */
    void prolongGiftCode(GiftCodeProlongReq req);

    /**
     * 根据act_code获取激活码信息
     * @param actCode
     * @return
     */
    GiftCode selectGiftCodeByActCode(String actCode);

    public Boolean sendCoupon(Integer id, Integer idType, Integer grantMode, long acId) throws Exception;

    void equityTimedTask();
    /**
     * 激活码过期操作
     * @throws Exception
     */
    void optExpireGiftCode()throws Exception;

    /**
     * 查询支付宝渠道延期的激活码
     * @param map
     * @return
     * @throws Exception
     */
    List<GiftCode> selectAlipayProLong(Map map) throws Exception;

    /**
     * 上海银行批量作废激活码
     * @return
     */
    Boolean obsoleteBoscActCodes() throws Exception;

    /**
     * 冻结上海银行激活码
     * @param activeRemarks
     * @return
     */
    void frozenBoscActCodes(List<String> activeRemarks)throws Exception;

    /**
     * 解冻上海银行激活码
     * @param activeRemarks
     * @throws Exception
     */
    void thawBoscActCodes(List<String> activeRemarks)throws Exception;

    /**
     * 替换上海银行激活码
     * @param req
     */
    void replaceBoscCodes(List<Map<String,String>> req)throws Exception;
}
