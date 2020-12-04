package com.colourfulchina.mars.api.feign.fallback;

import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.mars.api.feign.RemoteProductService;
import com.colourfulchina.pangu.taishang.api.vo.res.SelectBookProductRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * @Author: Nickal
 * @Description:
 * @Date: 2019/12/19 19:19
 */
@Slf4j
@Component
public class RemoteProductServiceImpl implements RemoteProductService {
    @PostMapping("/product/goodsListNEW2")
    @Override
    public CommonResultVo<List<SelectBookProductRes>> getGoodsListNEW2(Integer goodsId) {
        return null;
    }
}




