package com.colourfulchina.mars.service;

import com.colourfulchina.mars.api.entity.GiftCode;
import com.colourfulchina.mars.api.vo.MyGiftCodeVo;
import com.colourfulchina.mars.api.vo.req.QueryGoodsDetailReqVo;
import com.colourfulchina.mars.api.vo.res.GoodsInfoResVo;
import com.colourfulchina.pangu.taishang.api.entity.SysService;

import java.util.List;

public interface GoodsDetailService {
    /**
     * 商品详情
     * @param gift
     * @return
     */
    boolean checkGiftByid(GiftCode gift);

    List<GiftCode> selectGiftCodeList(GiftCode gift);

    public List<MyGiftCodeVo> getGoodsDetail(String actCode) throws Exception;

    /**
     * 根据产品组id查询产品组资源信息
     * @param groupId
     * @return
     */
    public List<SysService> selectGroupInfo(Integer groupId) throws Exception;

    public List<MyGiftCodeVo> getGoodsList(Long memberId, String prjCode, String actCode) throws Exception;
    
    public MyGiftCodeVo getGiftDetail(Integer giftCodeId, Integer prjGroupId) throws Exception;

    /**
     * 根据激活码查询商品详情
     * @param reqVo
     * @return
     */
    GoodsInfoResVo getGoodsDetailNew(QueryGoodsDetailReqVo reqVo) throws Exception;
}
