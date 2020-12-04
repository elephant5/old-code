package com.colourfulchina.pangu.taishang.service.impl;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.pangu.taishang.api.entity.ShopTypeService;
import com.colourfulchina.pangu.taishang.mapper.ShopTypeServiceMapper;
import com.colourfulchina.pangu.taishang.service.ShopTypeServiceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ShopTypeServiceServiceImpl extends ServiceImpl<ShopTypeServiceMapper, ShopTypeService> implements ShopTypeServiceService {
    @Autowired
    private ShopTypeServiceMapper shopTypeServiceMapper;

    /**
     * 根据服务类型查询商户类型与服务关联
     * @param typeService
     * @return
     */
    @Override
    public ShopTypeService selectByService(String typeService) {
        Wrapper<ShopTypeService> wrapper = new Wrapper<ShopTypeService>() {
            @Override
            public String getSqlSegment() {
                return "where code = '"+typeService+"'";
            }
        };
        return shopTypeServiceMapper.selectList(wrapper).get(0);
    }
}
