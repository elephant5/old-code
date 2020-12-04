package com.colourfulchina.mars.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.mars.api.entity.SysHospital;
import com.colourfulchina.mars.service.SysHospitalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequestMapping("/sysHospital")
@RestController
public class SysHospitalController {

    @Autowired
    private SysHospitalService sysHospitalService;

    //查询
    @PostMapping(path = "/getSysHospitalList")
    public CommonResultVo<List<SysHospital>> getSkuGoodsRelList(@RequestBody SysHospital sysHospital){
        CommonResultVo<List<SysHospital>> common = new CommonResultVo<List<SysHospital>>();
        try {
            Wrapper<SysHospital> wrapper = new EntityWrapper();
            wrapper.eq(!StringUtils.isEmpty(sysHospital.getProvince()),"province",sysHospital.getProvince());
            wrapper.eq(!StringUtils.isEmpty(sysHospital.getCity()),"city",sysHospital.getCity());
            wrapper.eq(!StringUtils.isEmpty(sysHospital.getName()),"name",sysHospital.getName());
            wrapper.eq(!StringUtils.isEmpty(sysHospital.getGrade()),"grade",sysHospital.getGrade());
            wrapper.eq(!StringUtils.isEmpty(sysHospital.getHospitalType()),"hospital_type",sysHospital.getHospitalType());
            wrapper.eq(!StringUtils.isEmpty(sysHospital.getDelFlag()),"del_flag",sysHospital.getDelFlag());
            List<SysHospital> list = sysHospitalService.selectList(wrapper);
            common.setCode(100);
            common.setMsg("成功");
            common.setResult(list);
        } catch (Exception e) {
            log.error("查询SysHospital列表失败{}",e);
            common.setCode(200);
            common.setMsg(e.getMessage());
        }
        return common;
    }

    //新增
    @PostMapping(path = "/addSysHospital")
    public CommonResultVo<Boolean> addSysHospital(@RequestBody SysHospital sysHospital){
        CommonResultVo<Boolean> common = new CommonResultVo<Boolean>();
        Assert.notNull(sysHospital,"入参不能为空");
        Assert.notNull(sysHospital.getProvince(),"省份不能为空");
        Assert.notNull(sysHospital.getCity(),"城市不能为空");
        Assert.notNull(sysHospital.getName(),"医院名称不能为空");
        Assert.notNull(sysHospital.getGrade(),"医院等级不能为空");
        Assert.notNull(sysHospital.getHospitalType(),"医院类型不能为空");
        try {
            SysHospital sysHospital1 = sysHospital.selectOne(new Wrapper<SysHospital>() {
                @Override
                public String getSqlSegment() {
                    return " where city = " + sysHospital.getCity() + " and name = " + sysHospital.getName() ;
                }
            });
            Assert.isTrue(null != sysHospital1,"该城市下已经配过此医院");
            common.setCode(100);
            common.setMsg("成功");
            common.setResult(sysHospitalService.insert(sysHospital));
        } catch (Exception e) {
            log.error("新增SysHospital失败{}",e);
            common.setCode(200);
            common.setMsg(e.getMessage());
        }
        return common;
    }

    //修改
    @PostMapping(path = "/updateSysHospital")
    public CommonResultVo<Boolean> updateSysHospital(@RequestBody SysHospital sysHospital){
        CommonResultVo<Boolean> common = new CommonResultVo<Boolean>();
        try {
            common.setCode(100);
            common.setMsg("成功");
            common.setResult(sysHospitalService.updateById(sysHospital));
        } catch (Exception e) {
            log.error("修改SysHospital失败{}",e);
            common.setCode(200);
            common.setMsg(e.getMessage());
        }
        return common;
    }

    //删除
    @GetMapping(path = "/deleteSysHospital/{id}")
    public CommonResultVo<Boolean> deleteSkuGoodsRel(@PathVariable Integer id){
        CommonResultVo<Boolean> common = new CommonResultVo<Boolean>();
        try {
            common.setCode(100);
            common.setMsg("成功");
            common.setResult(sysHospitalService.deleteById(id));
        } catch (Exception e) {
            log.error("删除SysHospital失败{}",e);
            common.setCode(200);
            common.setMsg(e.getMessage());
        }
        return common;
    }

}
