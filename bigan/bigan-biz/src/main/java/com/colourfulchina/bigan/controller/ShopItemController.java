package com.colourfulchina.bigan.controller;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.colourfulchina.bigan.api.entity.ShopItem;
import com.colourfulchina.bigan.service.ShopitemsService;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/shopItem")
public class ShopItemController {
    @Autowired
    private ShopitemsService shopItemsService;

    /**
     * 查询老系统商户shopItem列表
     * @return
     */
    @PostMapping("/selectShopItemList")
    public CommonResultVo<List<ShopItem>> selectShopItemList(){
        CommonResultVo<List<ShopItem>> result = new CommonResultVo<>();
        List<ShopItem> shopItemList = shopItemsService.selectList(null);
        result.setResult(shopItemList);
        return result;
    }

    /**
     * 根据商户id查询商户规格
     * @param shopId
     * @return
     */
    @ApiOperation("根据商户id查询商户规格")
    @PostMapping("/selectByShopId")
    public CommonResultVo<List<ShopItem>> selectByShopId(@RequestBody Integer shopId){
        CommonResultVo<List<ShopItem>> result = new CommonResultVo();
        try {
            Wrapper wrapper = new Wrapper() {
                @Override
                public String getSqlSegment() {
                    return "where shop_id ="+shopId;
                }
            };
            List<ShopItem> list = shopItemsService.selectList(wrapper);
            result.setResult(list);
        }catch (Exception e){
            log.error("根据商户id查询商户规格失败",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    /**
     * 老系统商户规格新增
     * @return
     */
    @PostMapping("/remoteAddShopItem")
    public CommonResultVo<ShopItem> remoteAddShopItem(@RequestBody ShopItem shopItem){
        CommonResultVo<ShopItem> result = new CommonResultVo<>();
        try {
            shopItem.setId(shopItemsService.selectShopItemSeqNextValue());
            shopItemsService.insert(shopItem);
            result.setResult(shopItem);
        }catch (Exception e){
            log.error("老系统商户规格新增失败{}",e.getMessage());
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    /**
     * 老系统商户规格更新
     * @param shopItem
     * @return
     */
    @PostMapping("/remoteUpdShopItem")
    public CommonResultVo<ShopItem> remoteUpdShopItem(@RequestBody ShopItem shopItem){
        CommonResultVo<ShopItem> result = new CommonResultVo<>();
        try {
            shopItemsService.updateById(shopItem);
            result.setResult(shopItem);
        }catch (Exception e){
            log.error("老系统商户规格更新失败{}",e.getMessage());
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }
}
