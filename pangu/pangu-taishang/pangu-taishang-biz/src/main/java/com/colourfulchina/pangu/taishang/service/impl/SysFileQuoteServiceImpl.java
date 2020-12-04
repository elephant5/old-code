package com.colourfulchina.pangu.taishang.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.pangu.taishang.api.entity.SysFileQuote;
import com.colourfulchina.pangu.taishang.mapper.SysFileQuoteMapper;
import com.colourfulchina.pangu.taishang.service.SysFileQuoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SysFileQuoteServiceImpl extends ServiceImpl<SysFileQuoteMapper, SysFileQuote> implements SysFileQuoteService {
    @Autowired
    SysFileQuoteMapper sysFileQuoteMapper;
    @Override
    public void updateDelFlagByFileId(SysFileQuote sysFileQuote) {
        sysFileQuoteMapper.updateDelFlagByFileId(sysFileQuote);
    }
}