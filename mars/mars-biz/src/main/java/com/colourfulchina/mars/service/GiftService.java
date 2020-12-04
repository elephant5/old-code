package com.colourfulchina.mars.service;

import com.colourfulchina.mars.api.vo.req.QueryGiftReqVO;

public interface GiftService {

    /**
     * 判断用户在指定商品下是否有有效期内的权益
     * @param reqVO
     * @return
     */
    String queryGiftByAcIdAndGoodsId(QueryGiftReqVO reqVO) throws Exception;

}
