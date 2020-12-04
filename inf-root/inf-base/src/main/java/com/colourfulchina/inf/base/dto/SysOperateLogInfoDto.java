package com.colourfulchina.inf.base.dto;

import com.colourfulchina.inf.base.entity.SysOperateLogInfo;
import lombok.Data;

import java.util.Map;

@Data
public class SysOperateLogInfoDto extends SysOperateLogInfo {
    private Map<String,Object> before;
    private Map<String,Object> after;
}
