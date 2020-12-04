package com.colourfulchina.mars.controller;

import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.mars.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 预约核销初始化
 */
@Slf4j
@RestController
@RequestMapping("/BookInit")
public class BookInitController {

    @Autowired
    private AsyncInitService asyncInitService;

    /**
     * 商品预约单初始化
     * @param request
     * @return
     */
    @GetMapping("/init")
    public CommonResultVo<String> init(String username, String password, Integer projectId, Integer goodsId,Integer orderId, HttpServletRequest request){
        CommonResultVo<String> resultVo=new CommonResultVo<>();
        try {
            asyncInitService.bookInit(username, password, projectId, goodsId, orderId);
            resultVo.setCode(100);
            resultVo.setMsg("预约单初始化成功" + new Date());
        }catch (Exception e){
            resultVo.setCode(200);
            resultVo.setMsg("预约单初始化"+e.getMessage());
            log.error("预约单初始化"+e.getMessage(),e);
        }
        return resultVo;
    }
}
