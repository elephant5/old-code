package com.colourfulchina.pangu.taishang.service.impl;

import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.pangu.taishang.api.entity.City;
import com.colourfulchina.pangu.taishang.api.entity.CountryArea;
import com.colourfulchina.pangu.taishang.mapper.CityMapper;
import com.colourfulchina.pangu.taishang.mapper.CountryAreaMapper;
import com.colourfulchina.pangu.taishang.service.CityService;
import com.colourfulchina.pangu.taishang.service.CountryAreaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CountryAreaServiceImpl extends ServiceImpl<CountryAreaMapper,CountryArea> implements CountryAreaService {

}