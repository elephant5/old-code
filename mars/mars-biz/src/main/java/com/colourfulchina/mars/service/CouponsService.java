package com.colourfulchina.mars.service;

import com.colourfulchina.colourfulCoupon.api.entity.CpnThirdCode;
import com.colourfulchina.colourfulCoupon.api.vo.req.UpdateCpnReqVo;
import com.colourfulchina.mars.api.vo.req.CouponThirdCodeReqVO;
import com.colourfulchina.mars.api.vo.res.ThirdCouponsProductInfoResVO;

import java.util.List;

public interface CouponsService {

    /**
     * 给用户发放第三方券
     * @param reqVO
     * @return
     */
    CpnThirdCode putThirdCoupons(CouponThirdCodeReqVO reqVO) throws Exception;

    /**
     * 查询第三方上架的可售卖的商品信息
     * @return
     */
    List<ThirdCouponsProductInfoResVO> getThirdProductInfo();

    /**
     * 修改券状态
     * @param reqVo
     * @return
     */
    Boolean updateCoupon(UpdateCpnReqVo reqVo);
}
