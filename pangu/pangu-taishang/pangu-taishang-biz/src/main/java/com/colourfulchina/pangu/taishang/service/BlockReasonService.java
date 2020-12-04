package com.colourfulchina.pangu.taishang.service;

import com.baomidou.mybatisplus.service.IService;
import com.colourfulchina.pangu.taishang.api.dto.BlockRule;
import com.colourfulchina.pangu.taishang.api.entity.BlockReason;

import java.util.List;

public interface BlockReasonService   extends IService<BlockReason> {

    /**
     * block原因插入
     * @return
     * @throws Exception
     */
    List<BlockReason> addReason(String type, Integer value, List<BlockRule> list) throws Exception;

    /**
     * 获取商户关房关餐
     * @param id
     * @return
     */
    List<BlockReason> getBlockReason(Integer id);
}
