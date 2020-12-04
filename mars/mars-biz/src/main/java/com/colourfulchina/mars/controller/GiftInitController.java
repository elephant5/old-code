package com.colourfulchina.mars.controller;

import com.colourfulchina.god.door.api.annotation.SysGodDoorLog;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.mars.service.AsyncInitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 权益初始化
 */
@Slf4j
@RestController
@RequestMapping("/GiftInit")
public class GiftInitController {

    @Autowired
    private AsyncInitService asyncInitService;

    /**
     * 商品激活码初始化
     * @param request
     * @return
     */
    @SysGodDoorLog("商品激活码初始化")
    @GetMapping("/init")
    public CommonResultVo<String> init(String username, String password, Integer projectId, Integer packageId, Integer channelId, Integer goodsId, Integer unitId, Integer state, Integer start, Integer stop, HttpServletRequest request){
        CommonResultVo<String> resultVo=new CommonResultVo<>();
        try {
            asyncInitService.giftInit(username, password, projectId, packageId, channelId, goodsId,unitId,state,start,stop);
            resultVo.setCode(100);
            resultVo.setMsg("权益初始化结束：" + new Date());
            log.info("权益初始化结束：" + new Date());
        }catch (Exception e){
            resultVo.setCode(200);
            resultVo.setMsg("权益初始化失败");
            log.error("权益初始化失败",e);
        }
        return resultVo;
    }

    /**
     * 权益缺失会员id恢复
     * @return
     */
    @PostMapping("/giftMemberSet")
    public CommonResultVo<Boolean> giftMemberSet(){
        CommonResultVo<Boolean> result = new CommonResultVo<>();
        try {
            asyncInitService.giftMemberSet();
            result.setResult(Boolean.TRUE);
        }catch (Exception e){
            log.error("权益缺失会员id恢复失败",e);
            result.setResult(Boolean.FALSE);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    /**
     * 权益缺失详情明细恢复
     * @return
     */
    @PostMapping("/giftDetailSet")
    public CommonResultVo<Boolean> giftDetailSet(){
        CommonResultVo<Boolean> result = new CommonResultVo<>();
        try {
            asyncInitService.giftDetailSet();
            result.setResult(Boolean.TRUE);
        }catch (Exception e){
            log.error("权益缺失详情明细恢复失败",e);
            result.setResult(Boolean.FALSE);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }


    /**
     * 预约单产品组恢复
     * @return
     */
    @PostMapping("/reservOrderGroupSet")
    public CommonResultVo<Boolean> reservOrderGroupSet(){
        CommonResultVo<Boolean> result = new CommonResultVo<>();
        try {
            asyncInitService.reservOrderGroupSet();
            result.setResult(Boolean.TRUE);
        }catch (Exception e){
            log.error("预约单产品组恢复失败",e);
            result.setResult(Boolean.FALSE);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

}
