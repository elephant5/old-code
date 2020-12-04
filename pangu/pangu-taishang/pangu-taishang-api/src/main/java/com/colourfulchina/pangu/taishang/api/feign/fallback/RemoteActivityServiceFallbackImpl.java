package com.colourfulchina.pangu.taishang.api.feign.fallback;

import com.alibaba.fastjson.JSON;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.pangu.taishang.api.feign.RemoteActivityService;
import com.colourfulchina.pangu.taishang.api.vo.ActivityReqVo;
import com.colourfulchina.pangu.taishang.api.vo.res.ActivityResVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class RemoteActivityServiceFallbackImpl implements RemoteActivityService {

    @Override
    public CommonResultVo<List<ActivityResVo>> getActCouponConfig(ActivityReqVo reqVo) throws Exception {
        log.error("获取活动配置异常", JSON.toJSONString(reqVo));
        return null;
    }
}
