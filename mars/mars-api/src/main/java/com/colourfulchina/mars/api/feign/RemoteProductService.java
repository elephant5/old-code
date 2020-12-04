package com.colourfulchina.mars.api.feign;

import com.colourfulchina.inf.base.constant.ServiceNameConstant;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.mars.api.feign.fallback.RemoteProductServiceImpl;
import com.colourfulchina.pangu.taishang.api.vo.res.SelectBookProductRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = ServiceNameConstant.MARS_SERVICE,fallback = RemoteProductServiceImpl.class)
public interface RemoteProductService {

    @PostMapping("/product/goodsListNEW2")
    CommonResultVo<List<SelectBookProductRes>> getGoodsListNEW2(@RequestBody Integer goodsId);
}
