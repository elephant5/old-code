package com.colourfulchina.mars.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.colourfulchina.inf.base.entity.SysOperateLogDetail;
import com.colourfulchina.inf.base.vo.PageVo;

import java.util.List;
import java.util.Map;

public interface SysOperateLogDetailMapper extends BaseMapper<SysOperateLogDetail> {
    List<SysOperateLogDetail> selectByPage(PageVo<SysOperateLogDetail> pageVo, Map<String, Object> map);
}
