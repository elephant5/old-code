package com.colourfulchina.bigan.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.bigan.api.entity.ClfRoomUp;
import com.colourfulchina.inf.base.vo.PageVo;
import com.colourfulchina.bigan.mapper.ClfRoomUpMapper;
import com.colourfulchina.bigan.service.ClfRoomUpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * User: Ryan
 * Date: 2018/8/5
 */
@Slf4j
@Service
public class ClfRoomUpServiceImpl extends ServiceImpl<ClfRoomUpMapper,ClfRoomUp> implements ClfRoomUpService {
        @Autowired
        private ClfRoomUpMapper clfRoomUpMapper;

        @Override
        public PageVo selectListPage(PageVo page) {
                return page.setRecords(clfRoomUpMapper.selectListPage(page,page.getCondition()));
        }

        @Override
        public int logicDelById(Integer id) {
                return clfRoomUpMapper.logicDelById(id);
        }
}
