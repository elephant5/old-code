package com.colourfulchina.pangu.taishang.service;

import com.baomidou.mybatisplus.service.IService;
import com.colourfulchina.pangu.taishang.api.entity.GroupProductBlockDate;

import java.util.List;

public interface GroupProductBlockDateService extends IService<GroupProductBlockDate> {

    void generateBothBlockDate()throws Exception;

    void updBlockDate(List<Integer> ids)throws Exception;

    void updBlockDateByGroupId(Integer productGroupId)throws Exception;

    void updBlockDateByGoodsId(Integer goodsId)throws Exception;

    void updBlockDateByShopItemId(Integer shopItemId)throws Exception;

    void updBlockDateByShopId(Integer shopId)throws Exception;
}