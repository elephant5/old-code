package com.colourfulchina.mars.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.IService;
import com.colourfulchina.mars.api.annotation.ReservOrderOperLog;
import com.colourfulchina.mars.api.entity.ReservOrderLog;
import org.aspectj.lang.ProceedingJoinPoint;

import java.util.List;

/**
 * @Author: luck.luo
 * @Description:
 * @Date: 2019/7/31 18:32
 */
public interface ReservOrderLogService extends IService<ReservOrderLog> {
    void insertLog(ProceedingJoinPoint point, ReservOrderOperLog reservOrderOperLog,JSONObject obj);


    ReservOrderLog insertManual(ReservOrderLog reservOrderLog);

    List<ReservOrderLog> selectList(ReservOrderLog reservOrderLog);
}
