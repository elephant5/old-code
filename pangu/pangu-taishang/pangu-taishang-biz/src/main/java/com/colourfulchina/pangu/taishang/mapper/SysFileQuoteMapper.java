package com.colourfulchina.pangu.taishang.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.colourfulchina.pangu.taishang.api.entity.SysFileQuote;

public interface SysFileQuoteMapper extends BaseMapper<SysFileQuote> {

    void updateDelFlagByFileId(SysFileQuote sysFileQuote);
}