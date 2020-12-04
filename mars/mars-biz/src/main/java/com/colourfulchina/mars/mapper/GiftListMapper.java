package com.colourfulchina.mars.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.colourfulchina.mars.api.entity.GiftCode;

import java.util.List;

public interface GiftListMapper extends BaseMapper<GiftCode> {

    List<GiftCode> selectmemberId(long memberId);
}
