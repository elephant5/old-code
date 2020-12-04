package com.colourfulchina.pangu.taishang.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.colourfulchina.inf.base.entity.SysOperateLogDetail;
import com.colourfulchina.inf.base.vo.PageVo;
import com.colourfulchina.pangu.taishang.api.vo.req.ShopLogPageListReq;
import com.colourfulchina.pangu.taishang.api.vo.res.HotelLogRes;
import com.colourfulchina.pangu.taishang.api.vo.res.ShopLogPageListRes;

import java.util.List;
import java.util.Map;

public interface SysOperateLogDetailMapper extends BaseMapper<SysOperateLogDetail> {
    List<SysOperateLogDetail> selectByPage(PageVo<SysOperateLogDetail> pageVo, Map<String,Object> map);

    /**
     * 查询酒店操作日志详情
     * @param pageVo
     * @param map
     * @return
     */
    List<HotelLogRes> selectHotelByPage(PageVo<SysOperateLogDetail> pageVo, Map<String,Object> map);
    /**
     * 查询商户操作日志详情
     * @param pageVo
     * @param map
     * @return
     */
    List<ShopLogPageListRes> selectShopLogByPage(PageVo<ShopLogPageListReq> pageVo, Map<String,Object> map);
}
