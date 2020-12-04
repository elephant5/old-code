package com.colourfulchina.bigan.controller;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.colourfulchina.bigan.api.entity.SalesBank;
import com.colourfulchina.bigan.api.vo.SalesBankVo;
import com.colourfulchina.bigan.service.SalesBankService;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sales/bank")
public class SalesBankController {

    @Autowired
    private SalesBankService salesBankService;

    @GetMapping("/list")
    public List<SalesBankVo> list(){
        Wrapper<SalesBank> wrapper=new Wrapper<SalesBank>() {
            @Override
            public String getSqlSegment() {
                return null;
            }
        };
        List<SalesBank> salesBankList=salesBankService.selectList(wrapper);
        List<SalesBankVo> salesBankVoList=Lists.newArrayList();
        for (SalesBank salesBank : salesBankList){
            SalesBankVo salesBankVo=new SalesBankVo();
            BeanUtils.copyProperties(salesBank,salesBankVo);
            salesBankVoList.add(salesBankVo);
        }
        return salesBankVoList;
    }
}
