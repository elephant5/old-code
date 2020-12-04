package com.colourfulchina.pangu.taishang.controller;


import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.inf.base.vo.PageVo;
import com.colourfulchina.pangu.taishang.api.entity.SkuGoodsRel;
import com.colourfulchina.pangu.taishang.api.vo.ActivityPageVo;
import com.colourfulchina.pangu.taishang.api.vo.req.SkuGoodsReqVo;
import com.colourfulchina.pangu.taishang.service.SkuGoodsRelService;
import com.colourfulchina.pangu.taishang.utils.CheckSignUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/sku")
public class SkuGoodsRelController {

    @Autowired
    private SkuGoodsRelService skuGoodsRelService;

    @PostMapping(path = "/getGoodsInfo")
    public CommonResultVo<SkuGoodsRel> getGoodsInfoBySku(@RequestBody SkuGoodsReqVo reqVo){
        CommonResultVo<SkuGoodsRel> common = new CommonResultVo<SkuGoodsRel>();
        try {
            Boolean validate = CheckSignUtils.validateKey(reqVo.getSign());
            if(validate){
                SkuGoodsRel skuGoodsRel = skuGoodsRelService.getGoodsInfoBySku(reqVo);
                common.setCode(100);
                common.setMsg("成功");
                common.setResult(skuGoodsRel);
            }else {
                throw new Exception("验签失败");
            }

        } catch (Exception e) {
            common.setCode(200);
            common.setMsg(e.getMessage());
        }
        return common;
    }
}
