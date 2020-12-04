package com.colourfulchina.mars.service;

public interface SynchroPushService {

   /**
    * @param reservOderId
    * @return
    * @throws Exception
    */
   public String synchroPush(Integer reservOderId) throws Exception;

   /**
    * 重复推送失败的订单至第三方
    * @return
    * @throws Exception
    */
   Boolean rePushFailThirdOrder()throws Exception;
}
