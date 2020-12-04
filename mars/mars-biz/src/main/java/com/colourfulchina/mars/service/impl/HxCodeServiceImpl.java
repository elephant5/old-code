package com.colourfulchina.mars.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.colourfulchina.mars.api.entity.ReservCode;
import com.colourfulchina.mars.mapper.ReservCodeMapper;
import com.colourfulchina.mars.service.HxCodeService;
import com.colourfulchina.mars.service.SynchroPushService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class HxCodeServiceImpl implements HxCodeService {

    @Autowired
    ReservCodeMapper reservCodeMapper;

    @Autowired
    private SynchroPushService synchroPushService;

    /**
     * 核销码过期操作
     * @throws Exception
     */
    @Override
    public void optExpireVarCode() throws Exception {
        //获取当前时间前一天的时间
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH,-1);
        Date yesterday = calendar.getTime();
        String yesterdayStr = DateUtil.format(yesterday,"yyyy-MM-dd");
        Wrapper wrapper = new Wrapper() {
            @Override
            public String getSqlSegment() {
                return "where var_status = 0 AND del_flag = 0 AND var_use_time IS NULL AND var_expire_time IS NOT NULL AND DATE(var_expire_time) <= STR_TO_DATE('"+yesterdayStr+"','%Y-%m-%d')";
            }
        };
        List<ReservCode> reservCodeList = reservCodeMapper.selectList(wrapper);
        Integer totalNum = 0;
        if (!CollectionUtils.isEmpty(reservCodeList)){
            totalNum = reservCodeList.size();
            for (ReservCode reservCode : reservCodeList) {
                // === 第三方订单推送埋点 开始 ===
                try {
                    synchroPushService.synchroPush(reservCode.getOrderId());
                } catch (Exception e) {
                    log.error("第三方订单推送异常", e);
                }
                // === 第三方订单推送埋点 结束 ===
            }
        }
        List<String> codes = reservCodeList.stream().map(reservCode -> reservCode.getVarCode()).collect(Collectors.toList());
        log.info("本次操作{}条数据:{}",totalNum, JSONObject.toJSONString(codes));
        reservCodeMapper.optExpireVarCode(yesterdayStr);
    }
}
