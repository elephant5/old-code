package com.colourfulchina.pangu.taishang.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.colourfulchina.pangu.taishang.api.entity.BookBasePayment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BookBasePaymentMapper extends BaseMapper<BookBasePayment> {
    /**
     * @title:selectBookPayList
     * @Description: 批量查询预约支付金额
     * @Param: [productGroupProductIdList]
     * @return: java.util.List<com.colourfulchina.pangu.taishang.api.entity.BookBasePayment>
     * @Auther: 图南
     * @Date: 2019/9/26 16:19
     */
    List<BookBasePayment> selectBookPayList(@Param("productGroupProductIdList") List<Integer> productGroupProductIdList);
}