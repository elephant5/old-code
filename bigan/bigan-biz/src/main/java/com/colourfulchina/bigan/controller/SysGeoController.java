package com.colourfulchina.bigan.controller;

import com.colourfulchina.bigan.api.entity.SysGeo;
import com.colourfulchina.bigan.service.SysGeoService;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/sysGeo")
public class SysGeoController {
    @Autowired
    private SysGeoService sysGeoService;

    /**
     * 查询定位信息列表
     * @return
     */
    @PostMapping("/selectGeoList")
    public CommonResultVo<List<SysGeo>> selectGeoList(){
        CommonResultVo<List<SysGeo>> result = new CommonResultVo<>();
        List<SysGeo> sysGeoList = sysGeoService.selectList(null);
        result.setResult(sysGeoList);
        return result;
    }

    /**
     * 远程同步新增定位信息
     * @param sysGeo
     * @return
     */
    @PostMapping("/remoteAddGeo")
    public CommonResultVo<SysGeo> remoteAddGeo(@RequestBody SysGeo sysGeo){
        CommonResultVo<SysGeo> result = new CommonResultVo<>();
        try {
            sysGeo.setPoint("POINT ("+sysGeo.getLng()+" "+sysGeo.getLat()+")");
            sysGeoService.insert(sysGeo);
            result.setResult(sysGeo);
        }catch (Exception e){
            log.error("远程同步新增定位信息失败");
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    /**
     * 远程同步修改定位信息
     * @param sysGeo
     * @return
     */
    @PostMapping("/remoteUpdGeo")
    public CommonResultVo<SysGeo> remoteUpdGeo(@RequestBody SysGeo sysGeo){
        CommonResultVo<SysGeo> result = new CommonResultVo<>();
        try {
            sysGeo.setPoint("POINT ("+sysGeo.getLng()+" "+sysGeo.getLat()+")");
            sysGeoService.updateById(sysGeo);
            result.setResult(sysGeo);
        }catch (Exception e){
            log.error("远程同步修改定位信息失败");
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }
}
