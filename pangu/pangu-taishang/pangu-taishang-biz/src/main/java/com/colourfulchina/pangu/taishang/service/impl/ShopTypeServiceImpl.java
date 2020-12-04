package com.colourfulchina.pangu.taishang.service.impl;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.pangu.taishang.api.entity.ShopType;
import com.colourfulchina.pangu.taishang.mapper.ShopTypeMapper;
import com.colourfulchina.pangu.taishang.service.ShopTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ShopTypeServiceImpl extends ServiceImpl<ShopTypeMapper, ShopType> implements ShopTypeService {
    @Autowired
    private ShopTypeMapper shopTypeMapper;

    /**
     * 查询商户类型列表
     * @return
     */
    @Override
    public List<ShopType> selectShopTypeList() {
        Wrapper wrapper = new Wrapper() {
            @Override
            public String getSqlSegment() {
                return "where del_flag = '0'";
            }
        };
        return shopTypeMapper.selectList(wrapper);
    }
}
