package com.colourfulchina.pangu.taishang.service;

import com.baomidou.mybatisplus.service.IService;
import com.colourfulchina.pangu.taishang.api.entity.SysFileQuote;

public interface SysFileQuoteService extends IService<SysFileQuote> {

    void updateDelFlagByFileId(SysFileQuote sysFileQuote);
}