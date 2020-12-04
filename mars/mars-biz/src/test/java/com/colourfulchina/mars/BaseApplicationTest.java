package com.colourfulchina.mars;

import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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
