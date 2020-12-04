package com.colourfulchina.mars.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.mars.api.entity.OrderReceipt;
import com.colourfulchina.mars.api.entity.ReservChannel;
import com.colourfulchina.mars.mapper.ReservChannelMapper;
import com.colourfulchina.mars.service.OrderReceiptService;
import com.colourfulchina.mars.service.ReservChannelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ReservChannelServiceImpl  extends ServiceImpl<ReservChannelMapper, ReservChannel> implements ReservChannelService {
}
