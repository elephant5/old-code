package com.colourfulchina.mars.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.colourfulchina.inf.base.vo.PageVo;
import com.colourfulchina.mars.api.entity.GiftCode;
import com.colourfulchina.mars.api.vo.req.*;
import com.colourfulchina.mars.api.vo.res.GiftCodePageRes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper
public interface GiftCodeMapper extends BaseMapper<GiftCode> {

    /**
     * 激活码分页查询
     * @param pageReq
     * @param map
     * @return
     */
    List<GiftCodePageRes> selectPageList(PageVo<GiftCodePageReq> pageReq, Map map);

    /**
     * 激活码列表导出查询
     * @param map
     * @return
     */
    List<GiftCodePageRes> selectExportList(Map map);

    /**
     * 激活码出库，根据激活码列表
     * @param outCodeReq
     */
    void outActCodeByCodes(OutCodeReq outCodeReq);

    /**
     * 激活码出库，根据批次号
     * @param outCodeReq
     */
    void outActCodeByBatch(OutCodeReq outCodeReq);

    /**
     * 激活码退货
     * @param returnCodeReq
     */
    void returnActCodeByCodes(ReturnCodeReq returnCodeReq);

    /**
     * 激活码作废
     * @param obsoleteCodeReq
     */
    void obsoleteActCodeByCodes(ObsoleteCodeReq obsoleteCodeReq);

	List<HashMap<String, Object>> getActCodeList(HashMap<String, Object> map);

    boolean updateGiftCode(GiftCode giftCode);
    int updateIdByUnitId(GiftCode giftCode);

    int batchUpdateById(@Param("giftCodes") List<GiftCode> list);

    /**
     * 根据act_code获取激活码信息
     * @param actCode
     * @return
     */
    GiftCode selectGiftCodeByActCode(String actCode);

    /**
     * 激活码过期操作
     * @param yesterdayStr
     */
    void optExpireGiftCode(String yesterdayStr);

	/**
     * 根据acId个goods_id查询权益
     * @param reqVO
     * @return
     */
    List<GiftCode> selectGiftByAcIdAndGoodsId(QueryGiftReqVO reqVO);

    List<GiftCode> selectAlipayProLong(Map map);

    /**
     * 查询上海银行项目里所有的激活码
     * @param activeRemarks
     * @return
     */
    List<String> selectBoscCodes(@Param("activeRemarks") List<String> activeRemarks);

    /**
     * 上海银行冻结激活码
     * @param codeReq
     */
    void frozenBoscActCodes(ObsoleteCodeReq codeReq);

    /**
     * 上海银行解冻
     * @param codeReq
     */
    void thawBoscActCodes(ObsoleteCodeReq codeReq);

    /**
     * 替换上海银行激活码
     * @param oldRemark
     * @param newRemark
     */
    void replaceBoscCodes(String oldRemark, String newRemark);
}
