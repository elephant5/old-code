package com.colourfulchina.pangu.taishang.service.impl;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.pangu.taishang.api.entity.ServiceGift;
import com.colourfulchina.pangu.taishang.api.entity.ServiceGift;
import com.colourfulchina.pangu.taishang.mapper.ServiceGiftMapper;
import com.colourfulchina.pangu.taishang.service.ServiceGiftService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ServiceGiftServiceImpl extends ServiceImpl<ServiceGiftMapper,ServiceGift> implements ServiceGiftService {

    @Autowired
    ServiceGiftMapper serviceGiftMapper;

    @Override
    public List<ServiceGift> selectByCode(String code) {
        Wrapper<ServiceGift> local = new Wrapper<ServiceGift>() {
            @Override
            public String getSqlSegment() {
                return  " where del_flag  = 0 and  service_id ='"+ code +"'" ;
            }
        };
        return serviceGiftMapper.selectList(local);
    }
}