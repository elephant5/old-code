package com.colourfulchina.mars.service;

import com.colourfulchina.inf.base.vo.PageVo;
import com.colourfulchina.mars.api.vo.GoodsListReqVo;
import com.colourfulchina.mars.api.vo.ReservOrderProductVo;
import com.colourfulchina.mars.api.vo.res.ProductDetailResVo;
import com.colourfulchina.mars.api.vo.res.QueryListResDto;
import com.colourfulchina.pangu.taishang.api.vo.res.SelectBookProductRes;

import java.util.List;

public interface ProductListService  {

    public List<QueryListResDto> getProductList(Integer actCode , String service) throws Exception;

    public ProductDetailResVo selectProductDetail(Integer productGroupProductId, Integer giftCodeId) throws Exception ;

    ReservOrderProductVo selectReservOrderVo(Integer productGroupProductId) throws Exception;

    PageVo<QueryListResDto> getGoddsListPaging(GoodsListReqVo goodsListReqVo) throws Exception;

    public List<QueryListResDto> getGoddsList(GoodsListReqVo goodsListReqVo) throws Exception;

    List<QueryListResDto> getGoddsListNew(GoodsListReqVo goodsListReqVo)throws Exception;

    List<SelectBookProductRes> getGoddsListNEW2(Integer goodId) throws Exception;


}
