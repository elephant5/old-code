package com.colourfulchina.pangu.taishang.controller.bigan;

import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.pangu.taishang.api.vo.res.bigan.ShopDetailVo;
import com.colourfulchina.pangu.taishang.service.ShopService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/bigan-replace/shop")
public class ShopReplaceController {

    @Autowired
    private ShopService shopService;
    /**
     * 查询商户及相关详情
     * @param id
     * @return
     */
    @GetMapping("/getShopDetail/{id}")
    public CommonResultVo<ShopDetailVo> getShopDetail(@PathVariable(value = "id") Long id) {
        CommonResultVo<ShopDetailVo> resultVo = new CommonResultVo<>();
        resultVo.setResult(shopService.getShopDetail(id));
        return resultVo;
    }

}
