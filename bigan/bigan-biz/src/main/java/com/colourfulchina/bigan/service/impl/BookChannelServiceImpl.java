package com.colourfulchina.bigan.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.bigan.api.entity.BookChannel;
import com.colourfulchina.bigan.mapper.BookChannelMapper;
import com.colourfulchina.bigan.service.BookChannelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
public class BookChannelServiceImpl extends ServiceImpl<BookChannelMapper,BookChannel> implements BookChannelService {

    @Autowired
    private BookChannelMapper bookChannelMapper;

    /**
     * 老系统酒店资源列表
     * @return
     */
    @Override
    public List<BookChannel> selectBookChannelList() {
        return bookChannelMapper.selectList(null);
    }
}
