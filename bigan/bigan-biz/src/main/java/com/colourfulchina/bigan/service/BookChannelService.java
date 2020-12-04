package com.colourfulchina.bigan.service;

import com.baomidou.mybatisplus.service.IService;
import com.colourfulchina.bigan.api.entity.BookChannel;

import java.util.List;

public interface BookChannelService extends IService<BookChannel> {
    /**
     * 查询老系统所有商户资源
     * @return
     */
    List<BookChannel> selectBookChannelList();
}
