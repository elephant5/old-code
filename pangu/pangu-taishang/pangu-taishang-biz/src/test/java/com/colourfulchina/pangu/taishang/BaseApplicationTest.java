package com.colourfulchina.pangu.taishang;

import com.colourfulchina.pangu.taishang.service.GoodsService;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class BaseApplicationTest {

    @Before
    public void init() {
        log.info("-----------------开始测试-----------------");
    }

    @After
    public void after() {
        log.info("-----------------结束测试-----------------");
    }

}
