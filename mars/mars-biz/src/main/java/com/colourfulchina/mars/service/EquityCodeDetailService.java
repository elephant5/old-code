package com.colourfulchina.mars.service;

import com.baomidou.mybatisplus.service.IService;
import com.colourfulchina.mars.api.entity.EquityCodeDetail;
import com.colourfulchina.mars.api.vo.res.GiftTimesVo;

import java.util.Date;
import java.util.List;

public interface EquityCodeDetailService   extends IService<EquityCodeDetail> {
    /**
     * 查询当前权益剩余的使用次数
     * @param memberId
     * @param goodsId
     * @param id
     * @param giftCodeId
     * @return
     */
    EquityCodeDetail selectByEquityCode(Long memberId, Integer goodsId, Integer id, Integer giftCodeId,Date bookDate);

    /*
     * 改变权益次数
     * */
    EquityCodeDetail changeGiftTimes(String type, Long memberId, Integer goodsId, Integer id, Integer giftCodeId, int num, Date bookDate) throws Exception;
    /*
     * 改变权益次数
     * */
    EquityCodeDetail changeGiftTimes(String type, Long memberId, Integer goodsId, Integer id, Integer giftCodeId, int num, Date bookDate,Integer useFreeCount) throws Exception;
    /**
     * 查询激活码详细信息
     * @param id
     * @param giftCodeId
     * @return
     */
    List<EquityCodeDetail> selectByEquityByGoodsId(Integer id, Integer giftCodeId);

    /**
     * 根据gift_code的id判断权益是否使用过
     * @param giftCodeId
     * @return
     */
    Boolean checkGiftCodeUse(Integer giftCodeId) throws Exception;

    /**
     * 根据gift_code的id查询权益信息
     * @param giftCodeId
     * @return
     */
    List<EquityCodeDetail> selectEquityCodeDetailListByGiftCodeId(Integer giftCodeId) throws Exception;

    /**
     * 查询权益次数
     * @param giftCodeId
     * @return
     */
    List<GiftTimesVo> selectGiftTimesVoList(Integer groupId, Integer giftCodeId);
}
