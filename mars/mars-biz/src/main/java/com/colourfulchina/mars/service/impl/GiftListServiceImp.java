package com.colourfulchina.mars.service.impl;

import com.colourfulchina.mars.api.entity.GiftCode;
import com.colourfulchina.mars.mapper.GiftListMapper;
import com.colourfulchina.mars.service.GiftListService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: Nickal
 * @Description:
 * @Date: 2019/5/27 9:45
 */
@AllArgsConstructor
@Slf4j
@Service
public class GiftListServiceImp implements GiftListService {

    @Autowired
    private GiftListMapper giftListMapper;


    @Override
    public  List<GiftCode> selectmemberId(Long memberId) {
        List<GiftCode> giftcode = giftListMapper.selectmemberId(memberId);
        return giftcode;
    }
}
