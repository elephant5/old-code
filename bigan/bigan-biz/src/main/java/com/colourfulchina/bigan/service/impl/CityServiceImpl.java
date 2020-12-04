package com.colourfulchina.bigan.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.bigan.api.entity.City;
import com.colourfulchina.bigan.mapper.CityMapper;
import com.colourfulchina.bigan.service.CityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class CityServiceImpl extends ServiceImpl<CityMapper,City> implements CityService {


}
