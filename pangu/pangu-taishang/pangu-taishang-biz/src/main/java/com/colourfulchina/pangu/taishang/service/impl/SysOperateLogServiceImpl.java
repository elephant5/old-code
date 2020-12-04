package com.colourfulchina.pangu.taishang.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.inf.base.entity.SysOperateLog;
import com.colourfulchina.inf.base.entity.SysOperateLogDetail;
import com.colourfulchina.inf.base.entity.SysOperateLogInfo;
import com.colourfulchina.inf.base.vo.PageVo;
import com.colourfulchina.pangu.taishang.api.vo.req.ShopLogPageListReq;
import com.colourfulchina.pangu.taishang.api.vo.res.HotelLogRes;
import com.colourfulchina.pangu.taishang.api.vo.res.ShopLogPageListRes;
import com.colourfulchina.pangu.taishang.mapper.SysOperateLogDetailMapper;
import com.colourfulchina.pangu.taishang.mapper.SysOperateLogInfoMapper;
import com.colourfulchina.pangu.taishang.mapper.SysOperateLogMapper;
import com.colourfulchina.pangu.taishang.service.SysOperateLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class SysOperateLogServiceImpl extends ServiceImpl<SysOperateLogMapper, SysOperateLog> implements SysOperateLogService {
    @Autowired
    SysOperateLogMapper sysOperateLogMapper;

    @Autowired
    SysOperateLogInfoMapper sysOperateLogInfoMapper;

    @Autowired
    SysOperateLogDetailMapper sysOperateLogDetailMapper;

    @Override
    public void insertSysOperateLogInfoAndDetail(SysOperateLogInfo operateLogInfo, List<SysOperateLogDetail> operateLogDetailList) {
        try {
            log.debug("insertSysOperateLogInfoAndDetail.operateLogInfo:{}",JSON.toJSONString(operateLogInfo));
            final Integer insert = sysOperateLogInfoMapper.insert(operateLogInfo);
            log.debug("sysOperateLogInfoMapper.insert:{}",insert);

            if (!CollectionUtils.isEmpty(operateLogDetailList)){
                for (SysOperateLogDetail operateLogDetail:operateLogDetailList){
                    operateLogDetail.setLogInfoId(operateLogInfo.getId());
                    sysOperateLogDetailMapper.insert(operateLogDetail);
                }
            }
        }catch (Exception e){
            log.error("insertSysOperateLogInfoAndDetail error:{}",e.getMessage(),e);
        }
    }

    @Override
    public Map<String, Object> dynamicSelectById(SysOperateLogInfo operateLogInfo) {
        return sysOperateLogMapper.dynamicSelectById(operateLogInfo);
    }

    @Override
    public PageVo<SysOperateLogDetail> querySysOperateLogDetailPage(PageVo<SysOperateLogDetail> detailPageVo) {
        final List<SysOperateLogDetail> detailList = sysOperateLogDetailMapper.selectByPage(detailPageVo,detailPageVo.getCondition());
        detailPageVo.setRecords(detailList);
        return detailPageVo;
    }

    /**
     * 查询酒店操作日志详情
     * @param detailPageVo
     * @return
     */
    @Override
    public PageVo<HotelLogRes> queryHotelNameLogPage(PageVo<SysOperateLogDetail> detailPageVo) {
        PageVo<HotelLogRes> pageRes = new PageVo<>();
        final List<HotelLogRes> detailList = sysOperateLogDetailMapper.selectHotelByPage(detailPageVo,detailPageVo.getCondition());
        BeanUtils.copyProperties(detailPageVo,pageRes);
        pageRes.setRecords(detailList);
        return pageRes;
    }

    /**
     * 查询商户操作日志
     * @param pageVoReq
     * @return
     */
    @Override
    public PageVo<ShopLogPageListRes> queryShopLogPage(PageVo<ShopLogPageListReq> pageVoReq) {
        PageVo<ShopLogPageListRes> pageRes = new PageVo<>();
        List<ShopLogPageListRes> list = sysOperateLogDetailMapper.selectShopLogByPage(pageVoReq,pageVoReq.getCondition());
        BeanUtils.copyProperties(pageVoReq,pageRes);
        pageRes.setRecords(list);
        return pageRes;
    }
}
