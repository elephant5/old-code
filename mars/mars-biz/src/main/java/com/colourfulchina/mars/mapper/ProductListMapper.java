package com.colourfulchina.mars.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.colourfulchina.mars.api.entity.GiftCode;
import feign.Param;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductListMapper extends BaseMapper<GiftCode> {

    GiftCode getProductList(@Param("actCode") Integer actCode);
}
