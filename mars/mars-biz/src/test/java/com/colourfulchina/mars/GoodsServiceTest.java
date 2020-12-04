package com.colourfulchina.mars;

import cn.hutool.core.date.DateUtil;
import com.colourfulchina.mars.api.entity.ReservOrder;
import com.colourfulchina.mars.api.vo.BoscCheckCardVo;
import com.colourfulchina.mars.api.vo.res.GiftTimesVo;
import com.colourfulchina.mars.service.EquityCodeDetailService;
import com.colourfulchina.mars.service.ReservOrderService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;


@Slf4j
public class GoodsServiceTest extends BaseApplicationTest{

    @Autowired
    private ReservOrderService reservOrderService;

    @Autowired
    private EquityCodeDetailService equityCodeDetailService;

    @Test
    public void testReservOrder(){
        log.info(DateUtil.format(new Date(),"mmddhhmmss"));
        log.info((DateUtil.format(new Date(),"mmdd")));
        log.info((DateUtil.format(new Date(),"hhmmss")));
        log.info(((int)((Math.random()*9+1)*100000)+""));
    }

    @Test
    public void testEquityCode(){
        List<GiftTimesVo> ls = equityCodeDetailService.selectGiftTimesVoList(324,3936205);
        System.out.println("======" + ls.size());
    }



}
