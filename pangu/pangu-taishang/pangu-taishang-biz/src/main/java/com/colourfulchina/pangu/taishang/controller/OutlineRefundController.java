package com.colourfulchina.pangu.taishang.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.pangu.taishang.api.vo.OutlineRefundImport;
import com.colourfulchina.pangu.taishang.api.vo.req.OutlineRefundInfoReq;
import com.colourfulchina.pangu.taishang.api.vo.res.OutlineRefundInfoRes;
import com.colourfulchina.pangu.taishang.service.OutlineRefundInfoService;
import com.colourfulchina.pangu.taishang.utils.DateUtil;
import com.colourfulchina.pangu.taishang.utils.ExcelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/outlineRefund")
public class OutlineRefundController {

    @Autowired
    OutlineRefundInfoService outlineRefundInfoService;

//    @RequestMapping(value = "/test", method = RequestMethod.POST)
//    public CommonResultVo<Boolean> getTest(@RequestParam String id){
//        CommonResultVo<Boolean> restResponse = new CommonResultVo<>();
//        List<OutlineRefundInfo> list = new ArrayList<>();
//        OutlineRefundInfo info = new OutlineRefundInfo();
//        info.setOrderNo(id);
//        list.add(info);
//        Page<OutlineRefundInfoRes> outlineRefundInfoVos = outlineRefundInfoService.getOutlineRefundInfoByImport(list);
//        restResponse.setData(true);
//
//        return restResponse;
//    }

    /**
     * 根据导入信息获取线下退款单详情
     * @param file
     * @return
     */
    @RequestMapping(value = "/import", method = RequestMethod.POST)
    public CommonResultVo<Page<OutlineRefundInfoRes>> getOutlineRefundInfoByImport(@RequestParam(value="file", required = false) MultipartFile file){
        CommonResultVo<Page<OutlineRefundInfoRes>> restResponse = new CommonResultVo<>();
        //获取导入的数据
        List<OutlineRefundImport> imports = ExcelUtils.readExcel(OutlineRefundImport.class, file);

        //将导入数据整理为输入数据
        List<OutlineRefundInfoReq> list = new ArrayList<>();
        imports.forEach( imp -> {
            OutlineRefundInfoReq info = new OutlineRefundInfoReq();
            info.setOrderNo(imp.getComments());
            info.setRefundPrice(imp.getInvestAmount());
            info.setRefundReciever(imp.getSName());
            info.setPhoneReciever(imp.getSAccount());
            if(imp.getSaleDateStr() == null){
                info.setRefundTime(new Date());
            }else{
                info.setRefundTime(DateUtil.toDate(imp.getSaleDateStr(), "yyyyMMdd"));
            }
            list.add(info);
        });

        Page<OutlineRefundInfoRes> outlineRefundInfoVos = outlineRefundInfoService.getOutlineRefundInfoByImport(list);
        restResponse.setResult(outlineRefundInfoVos);

        return restResponse;
    }

    /**
     * 更新退款
     * @param outlineRefundInfoVos
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public CommonResultVo<Boolean> updateRefundInfo(@RequestBody  List<OutlineRefundInfoReq> outlineRefundInfoVos){
        CommonResultVo<Boolean> restResponse = new CommonResultVo<>();
        Boolean aBoolean = outlineRefundInfoService.updateRefundInfo(outlineRefundInfoVos);
        restResponse.setResult(aBoolean);
        return restResponse;
    }

    /**
     * 导出线下退款单完整信息
     * @param outlineRefundInfos
     * @param response
     * @return
     */
    @RequestMapping(value = "/export", method = RequestMethod.POST)
    public void exportOutlineRefundInfo(@RequestBody List<OutlineRefundInfoReq> outlineRefundInfos, HttpServletResponse response) {
//        RestResponse<Boolean> restResponse = new RestResponse<>();
//        try{
        outlineRefundInfoService.exportOutlineRefundInfo(outlineRefundInfos, response);
//            restResponse.setData(aBoolean);

//        }catch (Exception e){
//            restResponse.setCode(ErrorCode.RESULT_IS_FULL.errorCode);
//            restResponse.setMsg(ErrorCode.RESULT_IS_FULL.errorMessage);
//        }
//        return restResponse;
    }

    /**
     * 根据条件查询
     * @param outlineRefundQueryInfo
     * @return
     */
    @RequestMapping(value = "query", method = RequestMethod.POST)
    public CommonResultVo<Page<OutlineRefundInfoRes>> queryRefundInfoByCondition(@RequestBody OutlineRefundInfoReq outlineRefundQueryInfo){
        CommonResultVo<Page<OutlineRefundInfoRes>> restResponse = new CommonResultVo<>();
        Page<OutlineRefundInfoRes> outlineRefundInfoVos = outlineRefundInfoService.queryRefundInfoByConditioin(outlineRefundQueryInfo);

        restResponse.setResult(outlineRefundInfoVos);
        return restResponse;
    }
}
