package com.colourfulchina.mars.controller;

import com.colourfulchina.god.door.api.annotation.SysGodDoorLog;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.mars.api.entity.ReservCode;
import com.colourfulchina.mars.api.enums.HxCodeStatusEnum;
import com.colourfulchina.mars.api.vo.ReservOrderVo;
import com.colourfulchina.mars.service.HxCodeService;
import com.colourfulchina.mars.service.ReservCodeService;
import com.colourfulchina.mars.service.ReservOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.Date;

@Slf4j
@RestController
@RequestMapping("/reservCode")
@Api(value = "", tags = {""})
public class ReservCodeController {

    @Autowired
    ReservCodeService reservCodeService;

    @Autowired
    ReservOrderService reservOrderService;

    @Autowired
    private HxCodeService hxCodeService;

    @Value("${encode.url}")
    private String encodeUrl;

    @SysGodDoorLog("重新激活过期的验证码")
    @ApiOperation("重新激活过期的验证码")
    @GetMapping("/reuse/{id}")
    public CommonResultVo<ReservOrderVo> get(@PathVariable Integer id) {
        CommonResultVo<ReservOrderVo> resultVo = new CommonResultVo<>();
        try {
            ReservCode reservCode = reservCodeService.selectById(id);
            if (!reservCode.getVarStatus().equals(HxCodeStatusEnum.HxCodeStatus.OVERTIME.getIndex() + "")) {
                resultVo.setCode(200);
                resultVo.setMsg("只能激活已过期的验证码");
            } else {
                reservCode.setVarStatus(HxCodeStatusEnum.HxCodeStatus.NEWONE.getIndex() + "");
                Calendar c = Calendar.getInstance();
                c.setTime(new Date());
                c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH),
                        23, 59, 59);
                reservCode.setVarExpireTime(c.getTime());
                c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH),
                        0, 0, 0);
                reservCode.setVarCrtTime(c.getTime());
                reservCodeService.updateById(reservCode);
            }
            ReservOrderVo reservOrder = reservOrderService.selectReservOrderById(reservCode.getOrderId());

            resultVo.setResult(reservOrder);
        } catch (Exception e) {
            log.error("重新激活过期的验证码！{}", e);
            resultVo.setCode(200);
            resultVo.setMsg(e.getMessage());
        }
        return resultVo;
    }

//    public static void main(String args[]) {
//        Calendar c = Calendar.getInstance();
//        c.setTime(new Date());
//        c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH),
//                23, 59, 59);
//        System.out.println(DateUtil.format(c.getTime(),"yyyy-MM-dd HH:mm:ss"));
//    }

    @SysGodDoorLog("查询核销码")
    @ApiOperation("查询核销码")
    @PostMapping("/getReservCode")
    public CommonResultVo<ReservCode> getReservCode (@RequestBody Integer reservCodeId) {
        CommonResultVo<ReservCode> commonResultVo = new CommonResultVo<ReservCode>();
        try {
            ReservCode code = reservCodeService.selectOneReservCode(reservCodeId);
            commonResultVo.setCode(100);
            commonResultVo.setMsg("成功");
            commonResultVo.setResult(code);
        } catch (Exception e) {
            log.error("查询核销码！{}", e);
            commonResultVo.setCode(200);
            commonResultVo.setMsg(e.getMessage());
        }
        return commonResultVo;
    }

    @SysGodDoorLog("获取生成二维码url")
    @ApiOperation("获取生成二维码url")
    @GetMapping("/getEncodeUrl/{varCode}")
    public CommonResultVo<String> getEncodeUrl(@PathVariable String varCode) {
        CommonResultVo<String> resultVo = new CommonResultVo<>();
        if (StringUtils.isEmpty(encodeUrl)) {
            resultVo.setCode(200);
        } else {
            String url = encodeUrl+varCode;
            log.info("生成二维码的url:{}",url);
            resultVo.setResult(url);
        }
        return resultVo;
    }

    @SysGodDoorLog("核销码过期")
    @ApiOperation("核销码过期")
    @PostMapping("/optExpireVarCode")
    public CommonResultVo<Boolean> optExpireVarCode(){
        CommonResultVo<Boolean> result = new CommonResultVo<>();
        try {
            hxCodeService.optExpireVarCode();
            result.setResult(Boolean.TRUE);
        }catch (Exception e){
            log.error("核销码过期操作失败",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }
}
