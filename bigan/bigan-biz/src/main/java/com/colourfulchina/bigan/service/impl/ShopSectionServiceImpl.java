package com.colourfulchina.bigan.service.impl;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.bigan.api.entity.ShopSection;
import com.colourfulchina.bigan.api.entity.SysCountryArea;
import com.colourfulchina.bigan.mapper.ShopSectionMapper;
import com.colourfulchina.bigan.mapper.SysCountryAreaMapper;
import com.colourfulchina.bigan.service.ShopSectionService;
import com.colourfulchina.bigan.service.ShopService;
import com.colourfulchina.bigan.service.SysCountryAreaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ShopSectionServiceImpl extends ServiceImpl<ShopSectionMapper,ShopSection> implements ShopSectionService {
    @Autowired
    private ShopSectionMapper shopSectionMapper;

    /**
     * 根据商户id删除shopSection
     * @param shopId
     */
    @Override
    public void delSection(Integer shopId) {
        Wrapper<ShopSection> wrapper = new Wrapper<ShopSection>() {
            @Override
            public String getSqlSegment() {
                return "where shop_id = "+shopId;
            }
        };
        shopSectionMapper.delete(wrapper);
    }
}
