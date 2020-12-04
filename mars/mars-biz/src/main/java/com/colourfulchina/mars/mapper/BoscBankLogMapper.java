package com.colourfulchina.mars.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.colourfulchina.mars.api.entity.BoscBankLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: 罗幸
 * @Description:
 * @Date: 2020/3/17 10:55
 */
public interface BoscBankLogMapper  extends BaseMapper<BoscBankLog> {
    Integer batchInsert(@Param("logs") List<BoscBankLog> logs);
}
