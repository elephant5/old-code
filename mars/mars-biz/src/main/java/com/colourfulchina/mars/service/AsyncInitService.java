package com.colourfulchina.mars.service;

public interface AsyncInitService {

    public void giftInit (String username, String password, Integer projectId, Integer packageId, Integer channelId, Integer goodsId, Integer unitId,Integer state,Integer start,Integer stop) throws Exception;

    public void bookInit(String username, String password, Integer projectId, Integer goodsId,Integer orderId) throws Exception;

    void giftMemberSet()throws Exception;

    void giftDetailSet()throws Exception;

    void reservOrderGroupSet()throws Exception;

}
