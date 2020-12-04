package com.colourfulchina.mars.service;

import com.colourfulchina.mars.api.entity.GiftCode;

import java.util.List;

public interface GiftListService {

    List<GiftCode> selectmemberId(Long memberId);

}
