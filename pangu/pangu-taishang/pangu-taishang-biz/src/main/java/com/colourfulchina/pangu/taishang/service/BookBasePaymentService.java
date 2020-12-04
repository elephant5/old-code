package com.colourfulchina.pangu.taishang.service;

import com.baomidou.mybatisplus.service.IService;
import com.colourfulchina.pangu.taishang.api.entity.BookBasePayment;
import com.colourfulchina.pangu.taishang.api.vo.BookBasePaymentVo;
import com.colourfulchina.pangu.taishang.api.vo.req.SelectBookPayReq;
import com.colourfulchina.pangu.taishang.api.vo.res.bookBasePayment.BookBasePaymentRes;

import java.util.List;

public interface BookBasePaymentService extends IService<BookBasePayment> {

    /**
     * 查询产品预约支付金额
     * @param selectBookPayReq
     * @return
     * @throws Exception
     */
    List<BookBasePaymentRes> selectBookPay(SelectBookPayReq selectBookPayReq)throws Exception;

    /**
     * 处理编辑产品组产品时的预约支付金额
     * @param bookBasePaymentVoList
     * @throws Exception
     */
    void optBookPaymentList(List<BookBasePaymentVo> bookBasePaymentVoList)throws Exception;

    /**
     * 预约支付金额转换
     * @param bookBasePaymentVo
     * @return
     * @throws Exception
     */
    BookBasePaymentVo convertPayment(BookBasePaymentVo bookBasePaymentVo)throws Exception;

    /**
     * 翻译支付预约金额
     * @param bookBasePaymentVo
     * @return
     * @throws Exception
     */
    BookBasePaymentVo translatePayment(BookBasePaymentVo bookBasePaymentVo)throws Exception;

    /**
     * @title:selectBookPayList
     * @Description: 批量获取预约支付金额
     * @Param: [productGroupProductIdList]
     * @return: java.util.List<com.colourfulchina.pangu.taishang.api.vo.res.bookBasePayment.BookBasePaymentRes>
     * @Auther: 图南
     * @Date: 2019/9/26 16:13
     */
    List<BookBasePaymentRes> selectBookPayList(List<Integer> productGroupProductIdList);
}