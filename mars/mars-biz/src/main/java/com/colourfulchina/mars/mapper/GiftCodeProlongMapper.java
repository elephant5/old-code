package com.colourfulchina.mars.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.colourfulchina.mars.api.entity.GiftCode;
import com.colourfulchina.mars.api.entity.GiftCodeProlong;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: luck.luo
 * @Description:
 * @Date: 2019/7/26 10:54
 */
public interface GiftCodeProlongMapper extends BaseMapper<GiftCodeProlong> {
    int batchInsert(@Param("prolongs") List<GiftCodeProlong> prolongs);
}
