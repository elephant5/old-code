package com.colourfulchina.mars.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.colourfulchina.mars.api.entity.EquityCodeDetail;
import com.colourfulchina.mars.api.entity.GiftCode;
import feign.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GoodsDetailMapper extends BaseMapper<GiftCode> {
    /**
     * 根据电话号查询是否存在权益
     *
     * @param gift
     * @return
     */
    List<GiftCode> checkGiftByid(GiftCode gift);

    List<GiftCode> getGoodsDetail(@Param("actCode") String actCode);

    //public int getGoodsTotalTimes(EquityCodeDetail order);

    List<EquityCodeDetail> getGoodsTimes(EquityCodeDetail order);

    //public int getGoodsUseTimes(EquityCodeDetail order);

    List<GiftCode> selectGiftCodeList(GiftCode gift);

    Integer getNewUseCount(Integer id, Integer productGroupId);

    List<GiftCode> getGiftCode(String actCode);
}
