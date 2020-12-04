package com.colourfulchina.pangu.taishang.service;

import com.baomidou.mybatisplus.service.IService;
import com.colourfulchina.inf.base.entity.SysOperateLog;
import com.colourfulchina.inf.base.entity.SysOperateLogDetail;
import com.colourfulchina.inf.base.entity.SysOperateLogInfo;
import com.colourfulchina.inf.base.vo.PageVo;
import com.colourfulchina.pangu.taishang.api.vo.req.ShopLogPageListReq;
import com.colourfulchina.pangu.taishang.api.vo.res.HotelLogRes;
import com.colourfulchina.pangu.taishang.api.vo.res.ShopLogPageListRes;

import java.util.List;
import java.util.Map;

public interface SysOperateLogService extends IService<SysOperateLog> {
    /**
     * 新增SysOperateLogInfo和SysOperateLogDetail
     * @param operateLogInfo
     * @param operateLogDetailList
     */
    void insertSysOperateLogInfoAndDetail(SysOperateLogInfo operateLogInfo, List<SysOperateLogDetail> operateLogDetailList);

    /**
     * 根据表名、主键字段名、主键值动态查询数据
     * @param operateLogInfo
     * @return
     */
    Map<String,Object> dynamicSelectById(SysOperateLogInfo operateLogInfo);

    /**
     * 查询操作日志详情
     * @param detailPageVo
     * @return
     */
    PageVo<SysOperateLogDetail> querySysOperateLogDetailPage(PageVo<SysOperateLogDetail> detailPageVo);

    /**
     * 查询酒店操作日志详情
     * @param detailPageVo
     * @return
     */
    PageVo<HotelLogRes> queryHotelNameLogPage(PageVo<SysOperateLogDetail> detailPageVo);

    /**
     * 查询商户操作日志
     * @param pageVoReq
     * @return
     */
    PageVo<ShopLogPageListRes> queryShopLogPage(PageVo<ShopLogPageListReq> pageVoReq);
}
