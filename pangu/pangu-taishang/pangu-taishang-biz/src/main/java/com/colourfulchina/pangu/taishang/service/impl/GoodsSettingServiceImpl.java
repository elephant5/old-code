package com.colourfulchina.pangu.taishang.service.impl;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.pangu.taishang.api.entity.Goods;
import com.colourfulchina.pangu.taishang.api.entity.GoodsSetting;
import com.colourfulchina.pangu.taishang.mapper.GoodsMapper;
import com.colourfulchina.pangu.taishang.mapper.GoodsSettingMapper;
import com.colourfulchina.pangu.taishang.service.GoodsService;
import com.colourfulchina.pangu.taishang.service.GoodsSettingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class GoodsSettingServiceImpl extends ServiceImpl<GoodsSettingMapper,GoodsSetting> implements GoodsSettingService {
    @Autowired
    GoodsSettingMapper goodsSettingMapper;

    @Override
    public GoodsSetting selectByGoodsId(Integer id) {

        Wrapper<GoodsSetting> local = new Wrapper<GoodsSetting>() {
            @Override
            public String getSqlSegment() {
                return "where goods_id = " + id ;
            }
        };
        List<GoodsSetting> list  =  goodsSettingMapper.selectList(local);
        return list.size() == 0? null : list.get(0);
    }
}