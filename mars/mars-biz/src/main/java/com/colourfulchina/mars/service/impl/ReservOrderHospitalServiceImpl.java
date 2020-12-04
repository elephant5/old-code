package com.colourfulchina.mars.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.mars.api.entity.ReservOrderDetail;
import com.colourfulchina.mars.api.entity.ReservOrderHospital;
import com.colourfulchina.mars.mapper.ReservOrderDetailMapper;
import com.colourfulchina.mars.mapper.ReservOrderHospitalMapper;
import com.colourfulchina.mars.service.ReservOrderDetailService;
import com.colourfulchina.mars.service.ReservOrderHospitalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ReservOrderHospitalServiceImpl   extends ServiceImpl<ReservOrderHospitalMapper, ReservOrderHospital> implements ReservOrderHospitalService {
}
