package com.colourfulchina.bigan.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.bigan.api.entity.SysGift;
import com.colourfulchina.bigan.mapper.SysGiftMapper;
import com.colourfulchina.bigan.service.SysGiftService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class SysGiftServiceImpl extends ServiceImpl<SysGiftMapper,SysGift> implements SysGiftService {
}
