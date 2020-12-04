package com.colourfulchina.mars.service;

import com.colourfulchina.mars.api.entity.ReservOrder;
import com.colourfulchina.mars.api.vo.req.BookPayReq;
import com.colourfulchina.mars.api.vo.req.ReservOrderPlaceReq;
import com.colourfulchina.mars.api.vo.res.PriceRes;
import com.colourfulchina.mars.api.vo.res.ReservOrderResVO;
import com.colourfulchina.member.api.res.MemLoginResDTO;
import com.colourfulchina.mars.api.vo.req.GoodsSettingReq;
import com.colourfulchina.pangu.taishang.api.entity.BookBasePayment;
import com.colourfulchina.pangu.taishang.api.vo.req.SelectBookPayReq;
import com.colourfulchina.pangu.taishang.api.vo.res.QueryBookBlockRes;
import com.colourfulchina.pangu.taishang.api.vo.res.bookBasePayment.BookBasePaymentRes;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 预约单附属信息接口
 *
 * @Author: luck.luo
 * @Description:
 * @Date: 2019/5/27 17:46
 */
public interface ReservOrderAttachService {

    QueryBookBlockRes getBlockRule(Map<String, Integer> param);

    ReservOrderResVO placeOrder(ReservOrderPlaceReq reservOrderPlaceReq, MemLoginResDTO memInfo) throws Exception;

    Map<String,Object> getGoodsSetting(GoodsSettingReq req);

    BookBasePaymentRes getMinPrice(BookPayReq req);

    List<BookBasePaymentRes> selectBookPay(BookPayReq req);

    PriceRes getStorePrice(BookPayReq req);
}
