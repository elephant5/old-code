package com.colourfulchina.pangu.taishang.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.colourfulchina.inf.base.entity.SysOperateLog;
import com.colourfulchina.inf.base.entity.SysOperateLogInfo;

import java.util.Map;

public interface SysOperateLogMapper extends BaseMapper<SysOperateLog> {
    Map<String,Object> dynamicSelectById(SysOperateLogInfo operateLogInfo);
}
