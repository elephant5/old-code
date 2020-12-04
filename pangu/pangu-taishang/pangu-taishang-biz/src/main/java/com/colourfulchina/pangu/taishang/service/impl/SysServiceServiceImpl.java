package com.colourfulchina.pangu.taishang.service.impl;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.pangu.taishang.api.entity.SysService;
import com.colourfulchina.pangu.taishang.mapper.SysServiceMapper;
import com.colourfulchina.pangu.taishang.service.SysServiceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class SysServiceServiceImpl extends ServiceImpl<SysServiceMapper,SysService> implements SysServiceService {
    @Autowired
    private SysServiceMapper sysServiceMapper;
    /**
     * 服务类型列表查询
     * @return
     */
    @Override
    public List<SysService> selectSysServiceList() {
        Wrapper wrapper = new Wrapper() {
            @Override
            public String getSqlSegment() {
                return "where del_flag = '0'";
            }
        };
        return sysServiceMapper.selectList(wrapper);
    }

    @Override
    public List<SysService> selectListByShopType(String shopType) {
        List<SysService> list = sysServiceMapper.selectListByShopType(shopType);
        return list;
    }
}