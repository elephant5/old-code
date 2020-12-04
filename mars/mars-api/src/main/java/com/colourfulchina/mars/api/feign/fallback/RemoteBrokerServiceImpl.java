package com.colourfulchina.mars.api.feign.fallback;

import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.mars.api.feign.RemoteBrokerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RemoteBrokerServiceImpl implements RemoteBrokerService {

    @Override
    public CommonResultVo<String> batchUpload() {
        log.error("batchUpload fallback error");
        return null;
    }
}
