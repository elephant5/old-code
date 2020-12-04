package com.colourfulchina.bigan.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.bigan.api.entity.Country;
import com.colourfulchina.bigan.mapper.CountryMapper;
import com.colourfulchina.bigan.service.CountryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class CountryServiceImpl extends ServiceImpl<CountryMapper,Country> implements CountryService {

}
