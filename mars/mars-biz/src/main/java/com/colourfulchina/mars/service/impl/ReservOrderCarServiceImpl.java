package com.colourfulchina.mars.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.mars.api.entity.ReservCode;
import com.colourfulchina.mars.api.entity.ReservOrderCar;
import com.colourfulchina.mars.mapper.ReservOrderCarMapper;
import com.colourfulchina.mars.service.ReservCodeService;
import com.colourfulchina.mars.service.ReservOrderCarService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ReservOrderCarServiceImpl    extends ServiceImpl<ReservOrderCarMapper, ReservOrderCar> implements ReservOrderCarService {
}
