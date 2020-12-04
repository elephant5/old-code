package com.colourfulchina.pangu.taishang.controller;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.colourfulchina.bigan.api.entity.ShopSection;
import com.colourfulchina.bigan.api.feign.RemoteShopSectionService;
import com.colourfulchina.god.door.api.annotation.SysGodDoorLog;
import com.colourfulchina.inf.base.entity.SysOperateLogDetail;
import com.colourfulchina.inf.base.enums.SysOperateLogEnums;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.inf.base.vo.PageVo;
import com.colourfulchina.pangu.taishang.api.entity.Shop;
import com.colourfulchina.pangu.taishang.api.entity.ShopProtocol;
import com.colourfulchina.pangu.taishang.api.vo.res.shopProtocol.ShopProtocolRes;
import com.colourfulchina.pangu.taishang.constant.ShopSectionConstant;
import com.colourfulchina.pangu.taishang.constant.SysLogConstant;
import com.colourfulchina.pangu.taishang.service.ShopProtocolService;
import com.colourfulchina.pangu.taishang.service.ShopService;
import com.colourfulchina.pangu.taishang.service.SysOperateLogService;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/shopProtocol")
@AllArgsConstructor
@Slf4j
@Api(tags = {"商户协议操作接口"})
public class ShopProtocolController {
    @Autowired
    private ShopProtocolService shopProtocolService;
    @Autowired
    private SysOperateLogService sysOperateLogService;
    @Autowired
    private ShopService shopService;
    private final RemoteShopSectionService remoteShopSectionService;

    /**
     * 商户合同有效期修改记录分页查询接口
     * @param shopProtocolId
     * @return
     */
    @SysGodDoorLog("商户合同有效期修改记录分页查询接口")
    @ApiOperation("商户合同有效期修改记录分页查询接口")
    @PostMapping("/selectContractEffectiveLogPageList")
    public CommonResultVo<PageVo<SysOperateLogDetail>> selectContractEffectiveLogPageList(Integer shopProtocolId){
        CommonResultVo<PageVo<SysOperateLogDetail>> result = new CommonResultVo<>();
        if (shopProtocolId == null){
            result.setCode(200);
            result.setMsg("商户合同有效期修改记录参数有误！");
            return result;
        }
        PageVo<SysOperateLogDetail> PageVo = new PageVo<>();
        PageVo.getCondition().put(SysLogConstant.LOG_TABLE_NAME,ShopProtocol.class.getAnnotation(TableName.class).value());
        PageVo.getCondition().put(SysLogConstant.LOG_OPT_TYPE, SysOperateLogEnums.Type.UPDATE.getCode());
        PageVo.getCondition().put(SysLogConstant.LOG_ROW_KEY,shopProtocolId);
        try {
            PageVo.getCondition().put(SysLogConstant.LOG_FIELD_NAME,ShopProtocol.class.getDeclaredField("contractEffective").getAnnotation(TableField.class).value());
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        PageVo = sysOperateLogService.querySysOperateLogDetailPage(PageVo);
        result.setResult(PageVo);
        return result;
    }

    /**
     * 商户block修改记录分页查询接口
     * @param shopProtocolId
     * @return
     */
    @SysGodDoorLog("商户block修改记录分页查询接口")
    @ApiOperation("商户block修改记录分页查询接口")
    @PostMapping("/selectShopBlockLogPageList")
    public CommonResultVo<PageVo<SysOperateLogDetail>> selectShopBlockLogPageList(Integer shopProtocolId){
        CommonResultVo<PageVo<SysOperateLogDetail>> result = new CommonResultVo<>();
        if (shopProtocolId == null){
            result.setCode(200);
            result.setMsg("商户block修改记录参数有误！");
            return result;
        }
        PageVo<SysOperateLogDetail> PageVo = new PageVo<>();
        PageVo.getCondition().put(SysLogConstant.LOG_TABLE_NAME,ShopProtocol.class.getAnnotation(TableName.class).value());
        PageVo.getCondition().put(SysLogConstant.LOG_OPT_TYPE,SysOperateLogEnums.Type.UPDATE.getCode());
        PageVo.getCondition().put(SysLogConstant.LOG_ROW_KEY,shopProtocolId);
        try {
            PageVo.getCondition().put(SysLogConstant.LOG_FIELD_NAME,ShopProtocol.class.getDeclaredField("blockRule").getAnnotation(TableField.class).value());
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        PageVo = sysOperateLogService.querySysOperateLogDetailPage(PageVo);
        result.setResult(PageVo);
        return result;
    }

    /**
     * 同步老系统中shopSection到新系统商户协议中
     * @return
     */
    @SysGodDoorLog("同步老系统中shopSection到新系统商户协议")
    @ApiOperation("同步老系统中shopSection到新系统商户协议中")
    @Transactional(rollbackFor=Exception.class)
    @PostMapping("/syncShopSectionList")
    public CommonResultVo<List<ShopProtocol>> syncShopSectionList(){
        log.info("同步开始");
        CommonResultVo<List<ShopProtocol>> result = new CommonResultVo<>();
        List<ShopProtocol> shopProtocolList = Lists.newLinkedList();
        CommonResultVo<List<ShopSection>> remoteResult = remoteShopSectionService.selectShopSectionList();
        for (ShopSection shopSection : remoteResult.getResult()) {
            if (shopSection.getShopId() != null){
                ShopProtocol shopProtocol = new ShopProtocol();
                //根据old_shop_id查询新系统shopId
                Shop shop = shopService.selectByOldId(shopSection.getShopId().intValue());
                shopProtocol.setId(shop.getId());
                if (shopSection.getTitle().equals(ShopSectionConstant.PAEKING)){
                    shopProtocol.setParking(shopSection.getContent());
                }
                if (shopSection.getTitle().equals(ShopSectionConstant.CHILDREN)){
                    shopProtocol.setChildren(shopSection.getContent());
                }
                if (shopSection.getTitle().equals(ShopSectionConstant.NOTICE)){
                    shopProtocol.setNotice(shopSection.getContent());
                }
                shopProtocolService.updateById(shopProtocol);
                shopProtocolList.add(shopProtocol);
            }
        }
        result.setResult(shopProtocolList);
        log.info("同步结束");
        return result;
    }

    @SysGodDoorLog("根据shopId获取商户扩展信息")
    @ApiOperation("根据shopId获取商户扩展信息")
    @PostMapping("/selectShopProtocol")
    CommonResultVo<List<ShopProtocolRes>> selectShopProtocol(@RequestBody Integer shopId){
        CommonResultVo<List<ShopProtocolRes>> resultVo = new CommonResultVo<>();
        try {
            List<ShopProtocolRes> shopProtocolResList = shopProtocolService.selectShopProtocol(shopId);
            resultVo.setResult(shopProtocolResList);
        }catch (Exception e){
            log.error("根据shopId获取商户扩展信息异常:",e);
            resultVo.setCode(200);
            resultVo.setMsg("获取商户扩展信息异常");
        }
        return resultVo;
    }

    @SysGodDoorLog("根据商户id查询商户协议")
    @ApiOperation("根据商户id查询商户协议")
    @PostMapping("/findOneShopProtocol")
    public CommonResultVo<ShopProtocol> findOneShopProtocol(@RequestBody Integer shopId){
        CommonResultVo<ShopProtocol> result = new CommonResultVo<>();
        try {
            ShopProtocol shopProtocol = shopProtocolService.selectById(shopId);
            result.setResult(shopProtocol);
        }catch (Exception e){
            log.error("根据商户id查询商户协议失败",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }
}