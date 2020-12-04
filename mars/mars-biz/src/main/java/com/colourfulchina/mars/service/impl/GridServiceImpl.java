package com.colourfulchina.mars.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.mars.api.entity.Grid;
import com.colourfulchina.mars.mapper.GridMapper;
import com.colourfulchina.mars.service.GridService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GridServiceImpl extends ServiceImpl<GridMapper,Grid> implements GridService {
    @Autowired
    private GridMapper gridMapper;
}
