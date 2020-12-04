package com.colourfulchina.mars.controller;

import java.util.ArrayList;
import java.util.List;

import com.colourfulchina.god.door.api.annotation.SysGodDoorLog;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.mars.api.vo.res.CityResVo;
import com.colourfulchina.pangu.taishang.api.entity.City;
import com.colourfulchina.pangu.taishang.api.feign.RemoteSysCityService;

import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/syscity")
public class CityController {
	
	private final RemoteSysCityService remoteSysCityService;

	@SysGodDoorLog("获取城市列表")
	@ApiOperation("获取城市列表")
	@PostMapping("/getCityList")
	public CommonResultVo<List<CityResVo>> getCityList() {
		// 定义返回结果
		CommonResultVo<List<CityResVo>> commonResultVo = new CommonResultVo<List<CityResVo>>();
		List<CityResVo> listResVos = new ArrayList<CityResVo>();
		// 调用盘古接口
		CommonResultVo<List<City>> commonPangu = remoteSysCityService.selectCityList();
		if(commonPangu.getCode()==100) {
			for (City city : commonPangu.getResult()) {
				CityResVo resVo = new CityResVo();
				resVo.setName(city.getNameCh());
				resVo.setPinyin(city.getNamePy());
				resVo.setZip(city.getId()+"");
				resVo.setLabel(city.getNameCh()+city.getNamePy()+city.getId());
				listResVos.add(resVo);
			}
			commonResultVo.setResult(listResVos);
		}
		
		return commonResultVo;
		
	}
}
