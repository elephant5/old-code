package com.colourfulchina.bigan.controller;

import com.colourfulchina.bigan.api.entity.ShopSection;
import com.colourfulchina.bigan.service.ShopSectionService;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/shopSection")
public class ShopSectionController {
    @Autowired
    private ShopSectionService shopSectionService;

    /**
     * 查询老系统商户section列表
     * @return
     */
    @PostMapping("/selectShopSectionList")
    public CommonResultVo<List<ShopSection>> selectShopSectionList(){
        CommonResultVo<List<ShopSection>> result = new CommonResultVo<>();
        List<ShopSection> shopSectionList = shopSectionService.selectList(null);
        result.setResult(shopSectionList);
        return result;
    }
}
