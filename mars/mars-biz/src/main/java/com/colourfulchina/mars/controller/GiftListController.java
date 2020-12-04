package com.colourfulchina.mars.controller;

import com.colourfulchina.god.door.api.annotation.SysGodDoorLog;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.mars.api.entity.GiftCode;
import com.colourfulchina.mars.service.GiftListService;
import com.colourfulchina.pangu.taishang.api.feign.RemoteGoodsService;
import com.colourfulchina.pangu.taishang.api.vo.req.GoodsBaseVo;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Nickal
 * @Description:
 * @Date: 2019/5/27 9:43
 */
@Slf4j
@RestController
@RequestMapping("/giftList")
@AllArgsConstructor
public class GiftListController {
    private final static int SUCCESS_CODE = 100;

    private final static int ERROR_CODE = 200;
    @Autowired
    private GiftListService giftListService;

    private RemoteGoodsService remoteGoodsService;

    @SysGodDoorLog("权益列表查询")
    @ApiOperation("权益列表查询")
    @PostMapping("/selectGiftList")
    public CommonResultVo<List<GoodsBaseVo>> selectGiftList(Long memberId) {
        CommonResultVo<List<GoodsBaseVo>> result = new CommonResultVo<>();
        try {
            //先去查询member_id对应的goods_id
            List<GiftCode> giftcode = giftListService.selectmemberId(memberId);
            if(giftcode != null)
              {
                  //通过goodsId循环查询商品
                  List<GoodsBaseVo> resultList = new ArrayList<>();
                  for(GiftCode gc : giftcode){
                      CommonResultVo<GoodsBaseVo> remoteResult = remoteGoodsService.selectById(gc.getGoodsId());
                      if(remoteResult == null|| remoteResult.getResult() == null)
                      {
                          throw new Exception("商品不存在");
                      }
                      resultList.add(remoteResult.getResult());
                      result.setResult(resultList);
                  }
              }

        } catch (Exception e) {
            result.setCode(ERROR_CODE);
            result.setMsg(e.getMessage());
            log.error("日志插入异常：" + e.getMessage());
        }
        return result;
    }
}
