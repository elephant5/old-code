package com.colourfulchina.bigan.controller;

import com.colourfulchina.bigan.api.entity.BookChannel;
import com.colourfulchina.bigan.service.BookChannelService;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/bookChannel")
public class BookChannelController {
    @Autowired
    private BookChannelService bookChannelService;

    /**
     * 老系统商户资源列表
     * @return
     */
    @PostMapping("/selectBookChannelList")
    public CommonResultVo<List<BookChannel>> selectBookChannelList(){
        CommonResultVo<List<BookChannel>> result = new CommonResultVo<>();
        List<BookChannel> bookChannelList = bookChannelService.selectBookChannelList();
        result.setResult(bookChannelList);
        return result;
    }
}
