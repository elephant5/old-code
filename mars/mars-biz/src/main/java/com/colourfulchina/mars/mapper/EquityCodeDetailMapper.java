package com.colourfulchina.mars.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.colourfulchina.mars.api.entity.EquityCodeDetail;
import com.colourfulchina.mars.api.vo.GiftSurplusTimesVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EquityCodeDetailMapper   extends BaseMapper<EquityCodeDetail>  {

    /**
     * 根据giftCodeId查询权益使用次数
     * @param giftCodeId
     * @return
     */
    List<EquityCodeDetail> selectEquityCodeDetail(Integer giftCodeId);


    List<EquityCodeDetail> selectGiftTimesVoList(@Param("groupId") Integer groupId, @Param("giftCodeId") Integer giftCodeId);


    List<GiftSurplusTimesVo> getSurplusTimes(EquityCodeDetail equityCodeDetail);
}
