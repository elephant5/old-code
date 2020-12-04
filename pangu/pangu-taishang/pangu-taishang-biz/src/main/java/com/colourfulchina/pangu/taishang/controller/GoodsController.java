package com.colourfulchina.pangu.taishang.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.colourfulchina.god.door.api.annotation.SysGodDoorLog;
import com.colourfulchina.inf.base.enums.SysDictTypeEnums;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.inf.base.vo.PageVo;
import com.colourfulchina.pangu.taishang.api.dto.BlockRule;
import com.colourfulchina.pangu.taishang.api.dto.SysFileDto;
import com.colourfulchina.pangu.taishang.api.entity.*;
import com.colourfulchina.pangu.taishang.api.utils.GoodsUtils;
import com.colourfulchina.pangu.taishang.api.vo.req.GoodsBaseVo;
import com.colourfulchina.pangu.taishang.api.vo.req.GoodsExpiryDateReq;
import com.colourfulchina.pangu.taishang.api.vo.req.ListSysFileReq;
import com.colourfulchina.pangu.taishang.api.vo.res.GoodsGroupListRes;
import com.colourfulchina.pangu.taishang.service.*;
import com.colourfulchina.tianyan.admin.api.entity.SysDict;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/goods")
@Slf4j
@Api(tags = {"商品管理"})
public class GoodsController {
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private GoodsSettingService goodsSettingService;
    @Autowired
    private GoodsChannelsService goodsChannelsService;
    @Autowired
    SalesChannelService salesChannelService;
    @Autowired
    BlockRuleService blockRuleService;
    @Autowired
    private ProductGroupProductService productGroupProductService;
    @Autowired
    SysFileService sysFileService;
    @Autowired
    private GroupProductBlockDateService groupProductBlockDateService;

    @Autowired
    ProductGroupService productGroupService;

    @Autowired
    GoodsPortalSettingService goodsPortalSettingService;

    /**
     * 商户列表模糊分页查询
     * @param pageVoReq
     * @return
     */
    @SysGodDoorLog("商品列表")
    @ApiOperation("商品列表")
    @PostMapping("/selectGoodsPageList")
    public CommonResultVo<PageVo<GoodsBaseVo>> selectGoodsPageList(@RequestBody PageVo<GoodsBaseVo> pageVoReq){
        CommonResultVo<PageVo<GoodsBaseVo>> result = new CommonResultVo<>();
        PageVo<GoodsBaseVo> pageVoRes = goodsService.findPageList(pageVoReq);
        result.setResult(pageVoRes);
        return result;
    }

    /**
     * 查询所有商品列表
     * @return
     */
    @SysGodDoorLog("查询所有商品列表")
    @ApiOperation("查询所有商品列表")
    @PostMapping("/selectGoodsList")
    @Cacheable(value = "Goods",key = "'selectGoodsList'",unless = "#result == null")
    public CommonResultVo<List<Goods>> selectGoodsList(){
        CommonResultVo<List<Goods>> result = new CommonResultVo<>();
        try {
            List<Goods> list = goodsService.selectList(null);
            result.setResult(list);
        }catch (Exception e){
            log.error("查询所有商品列表失败",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }


    /**
     * 查询所有商品列表
     * @return
     */
    @SysGodDoorLog("查询所有商品列表")
    @ApiOperation("查询所有商品列表")
    @PostMapping("/seachGoodsListByName")
    @Cacheable(value = "Goods",key = "'seachGoodsListByName_'+#goods.name",unless = "#result == null")
    public CommonResultVo<List<Goods>> seachGoodsListByName(@RequestBody  Goods goods){
        CommonResultVo<List<Goods>> result = new CommonResultVo<>();
        try {
            EntityWrapper<Goods> local = new EntityWrapper<>();
            local.like("name","%"+ goods.getName()+"%").or().like("short_name","%"+ goods.getName()+"%").or().eq("id",goods.getName());
            local.last(" limit 10");
            List<Goods> list = goodsService.selectList(local);
            result.setResult(list);
        }catch (Exception e){
            log.error("查询所有商品列表失败",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    @SysGodDoorLog("新增商品")
    @ApiOperation("新增商品")
    @PostMapping("/insert")
    @CacheEvict(value = "Goods",allEntries = true)
    public CommonResultVo<Goods> insert(@RequestBody GoodsBaseVo goods){
        CommonResultVo<Goods>  result = new CommonResultVo<>();
        try {
            Assert.notNull(goods.getName(),"商品名称不能为空！");
            Assert.notNull(goods.getShortName(),"商品内部简称不能为空！");
            Assert.notNull(goods.getSalesChannelIds(),"请选择销售渠道！");
            goodsService.insertGoodsAndSetting(goods);

            goodsService.insertOrUpdateAnotherData(goods);

            result.setResult(goods);
        }catch (Exception e){
            log.error("新增商品失败",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    @SysGodDoorLog("更新商品")
    @ApiOperation("更新商品")
    @PostMapping("/update")
    @CacheEvict(value = {"ProductGroupProduct","Goods"},allEntries = true)
    public CommonResultVo<GoodsBaseVo> update(@RequestBody GoodsBaseVo goods){

        CommonResultVo<GoodsBaseVo>  result = new CommonResultVo<>();
        try {
            Assert.notNull(goods.getName(),"商品名称不能为空！");
            Assert.notNull(goods.getShortName(),"商品内部简称不能为空！");
            Assert.notNull(goods.getSalesChannelIds(),"请选择销售渠道！");
            goods.setUpdateTime(new Date());
            if(goods.getBlockRuleList().size() > 0){
                List<String> blockList =goods.getBlockRuleList().stream().map(block -> block.getRule()).collect(Collectors.toList());
                String block  = Joiner.on(",").join(blockList);
                goods.setBlock(block);
            }
            goodsService.updateById(goods);
            GoodsSetting goodsSetting = new GoodsSetting();
            BeanUtils.copyProperties(goods,goodsSetting);
            goodsSetting.setGoodsId(goods.getId());
            goodsSetting.setUpdateTime(new Date());
            goodsSetting.setAllYear(goodsSetting.getAllYear() == "true"?"1":"0");
            goodsSetting.setSuperposition(goodsSetting.getSuperposition() == "true"?"1":"0");
            goodsSetting.setSingleThread(goodsSetting.getSingleThread() == "true"?"1":"0");
            goodsSetting.setEnableMaxNight(goodsSetting.getEnableMaxNight() == "true"?"1":"0");
            goodsSetting.setDisableCall(goodsSetting.getDisableCall() == "true"?"1":"0");
            goodsSetting.setDisableWechat(goodsSetting.getDisableWechat() == "true"?"1":"0");
            goodsSetting.setOnlySelf(goodsSetting.getOnlySelf()== "true"?"1":"0");
            goodsSetting.setEnableMinNight(goodsSetting.getEnableMinNight()== "true"?"1":"0");
            goodsSettingService.updateById(goodsSetting);
            //TODO 更新其他的表
            goodsService.insertOrUpdateAnotherData(goods);
            //产品组下面的产品成本价格区间更新
            productGroupProductService.updCostByGoodsId(goods.getId());
            //产品组产品block日期添加
            groupProductBlockDateService.updBlockDateByGoodsId(goods.getId());

            result  = this.selectById(goods.getId());
        }catch (Exception e){
            log.error("更新商品失败",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    @SysGodDoorLog("根据old_key查询商品")
    @ApiOperation("根据old_key查询商品")
    @GetMapping("/findByOldKey/{oldKey}")
    @Cacheable(value = "Goods",key = "'findByOldKey_'+#oldKey",unless = "#result == null")
    public CommonResultVo<Goods> findByOldKey(@PathVariable String oldKey){
        CommonResultVo<Goods>  result = new CommonResultVo<>();
        try {
            Assert.hasText(oldKey,"商品oldKey不能为空！");
            EntityWrapper<Goods> goodsWrapper=new EntityWrapper<>();
            Goods entity=new Goods();
            entity.setOldKey(oldKey);
            goodsWrapper.setEntity(entity);
            Goods goods = goodsService.selectOne(goodsWrapper);
            result.setResult(goods);
        }catch (Exception e){
            log.error("根据old_key查询商品",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    @SysGodDoorLog("删除商品")
    @ApiOperation("删除商品")
    @GetMapping("/del/{id}")
    @CacheEvict(value = "Goods",allEntries = true)
    public CommonResultVo<Goods> del(@PathVariable Integer id){
        CommonResultVo<Goods>  result = new CommonResultVo<>();
        try {
            Assert.notNull(id,"商品ID不能为空！");
            Goods goods = goodsService.selectById(id);
            goods.setDelFlag(1);
            goodsService.updateById(goods);
            result.setResult(goods);
        }catch (Exception e){
            log.error("删除商品失败",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    @SysGodDoorLog("复制商品")
    @ApiOperation("复制商品")
    @GetMapping("/copy/{id}")
    @CacheEvict(value = "Goods",allEntries = true)
    public CommonResultVo<Goods> copy(@PathVariable Integer id){
        CommonResultVo<Goods>  result = new CommonResultVo<>();
        try {
            Assert.notNull(id,"商品ID不能为空！");
            Goods goods = goodsService.selectById(id);
            GoodsSetting goodsSetting = goodsSettingService.selectByGoodsId(id);

            Goods goodsNew = new Goods();
            GoodsSetting GoodsSettingNew = new GoodsSetting();
            BeanUtils.copyProperties(goods,goodsNew);
            goodsNew.setUpdateTime(new Date());
            goodsNew.setCreateTime(new Date());
            goodsNew.setId(null);
            goodsService.insert(goodsNew);
            goodsService.copyGoodsDate(goodsNew,id);
            if(null != goodsSetting){
                BeanUtils.copyProperties(goodsSetting,GoodsSettingNew);
                GoodsSettingNew.setGoodsId(goodsNew.getId());
                GoodsSettingNew.setUpdateTime(new Date());
                GoodsSettingNew.setCreateTime(new Date());
                goodsSettingService.insert(GoodsSettingNew);
            }
            GoodsPortalSetting goodsPortalSetting = goodsPortalSettingService.selectByGoodsId(id);
            if(null != goodsPortalSetting){
                GoodsPortalSetting newGoodsPortalSetting = new GoodsPortalSetting();
                BeanUtils.copyProperties(goodsPortalSetting,newGoodsPortalSetting);
                newGoodsPortalSetting.setGoodsId(goodsNew.getId());
                newGoodsPortalSetting.setCode(null);
                newGoodsPortalSetting.setShortUrl(null);
                newGoodsPortalSetting.setUpdateTime(new Date());
                newGoodsPortalSetting.setCreateTime(new Date());
                goodsPortalSettingService.insert(newGoodsPortalSetting);
            }
            goodsService.copyGoodsAndProductDate(goodsNew,id);
        }catch (Exception e){
            log.error("复制商品失败",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    @SysGodDoorLog("根据商品id列表查询商品名称")
    @ApiOperation("根据商品id列表查询商品名称")
    @PostMapping("/selectNameByIds")
    @Cacheable(value = "Goods",key = "'selectNameByIds_'+#ids",unless = "#result == null")
    public CommonResultVo<List<Goods>> selectNameByIds(@RequestBody List<Integer> ids){
        CommonResultVo<List<Goods>> result = new CommonResultVo<>();
        try {
            List<Goods> list = goodsService.selectNameByIds(ids);
            result.setResult(list);
        }catch (Exception e){
            log.error("根据商品id列表查询商品名称失败",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    @SysGodDoorLog("根据id查找实体类的商品")
    @ApiOperation("根据id查找实体类的商品")
    @PostMapping("/findGoodsById")
    @Cacheable(value = "Goods",key = "'findGoodsById_'+#id",unless = "#result == null")
    public CommonResultVo<Goods> findGoodsById(@RequestBody Integer id){
        CommonResultVo<Goods> result = new CommonResultVo<>();
        try {
            Assert.notNull(id,"id不能为空");
            Goods goods = goodsService.selectById(id);
            result.setResult(goods);
        }catch (Exception e){
            log.error("商品查询失败",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    @SysGodDoorLog("根据Id查找商品")
    @ApiOperation("根据Id查找商品")
    @GetMapping("/selectById/{id}")
    @Cacheable(value = "Goods",key = "'selectById_'+#id",unless = "#result == null")
    public CommonResultVo<GoodsBaseVo> selectById(@PathVariable Integer id){
        CommonResultVo<GoodsBaseVo>  result = new CommonResultVo<>();
        GoodsBaseVo goodsBaseVo = new GoodsBaseVo();
        List<SysDict> bankList = salesChannelService.selectSysDict(SysDictTypeEnums.BANK_TYPE.getType());
        Map<String, SysDict> bankMap = bankList.stream().collect(Collectors.toMap(SysDict::getValue, bank -> bank));
        try {
            Assert.notNull(id,"商品ID不能为空！");
            Goods goods = goodsService.selectById(id);
            Assert.notNull(goods,"商品id"+id+"不存在");
            GoodsSetting goodsSetting = goodsSettingService.selectByGoodsId(id);
            BeanUtils.copyProperties(goods,goodsBaseVo);
            if(null != goodsSetting){
                BeanUtils.copyProperties(goodsSetting,goodsBaseVo);
            }
            GoodsUtils.goodsExpiryValue(goods.getExpiryValue(),goodsBaseVo);
            List<GoodsChannels> goodsChannelsList  = goodsChannelsService.selectGoodsChannelsByGoodsId(goods.getId());
            if(goodsChannelsList.size() > 0 ){
                GoodsChannels goodsChannels = goodsChannelsList.get(0);
                SalesChannel salesChannel = salesChannelService.selectById(goodsChannels.getChannelId());
                SysDict bank = bankMap.get(salesChannel.getBankId());
                if(null  != bank ){
                    goodsBaseVo.setBankName(bank.getLabel());
                    goodsBaseVo.setBankCode(bank.getRemarks());
                }
                goodsBaseVo.setBankId(salesChannel.getBankId());
                goodsBaseVo.setSalesChannelId(salesChannel.getSalesChannelId());
                goodsBaseVo.setSalesWayId(salesChannel.getSalesWayId());
                List<String> ids = Lists.newArrayList();
                ids.add(salesChannel.getBankId());
                ids.add(salesChannel.getSalesChannelId() == null? "" : salesChannel.getSalesChannelId());
                ids.add(salesChannel.getSalesWayId());
                goodsBaseVo.setSalesChannelIds(ids);
                goodsBaseVo.setSalesChannelId(salesChannel.getId()+"");
            }
            if(StringUtils.isNotBlank(goodsBaseVo.getBlock())){
                List<BlockRule> blockRules = blockRuleService.blockRuleStr2list(goodsBaseVo.getBlock());
                goodsBaseVo.setBlockRuleList(blockRules);
            }
            ListSysFileReq sysFileReq=new ListSysFileReq();
            sysFileReq.setType("goods.pic");
            sysFileReq.setObjId(id);
            final List<SysFileDto> fileDtoList = sysFileService.listSysFileDto(sysFileReq);
            goodsBaseVo.setFileDtoList(fileDtoList);
            result.setResult(goodsBaseVo);
        }catch (Exception e){
            log.error("根据Id查找商品",e);
            result.setCode(200);
            result.setMsg("根据Id查找商品失败："+e.getMessage());
        }
        return result;
    }

    @SysGodDoorLog("根据商品id列表查询商品")
    @PostMapping("/selectGoodsByIds")
    @Cacheable(value = "Goods",key = "'selectGoodsByIds_'+#goodsIds",unless = "#result == null")
    public CommonResultVo<List<GoodsBaseVo>> selectGoodsByIds(@RequestBody List<Integer> goodsIds){
        CommonResultVo<List<GoodsBaseVo>>  result = new CommonResultVo<>();
        List<GoodsBaseVo> goodsBaseVos = Lists.newArrayList();
        try {
            for(Integer id : goodsIds){
                GoodsBaseVo goodsBaseVo = new GoodsBaseVo();
                Goods goods = goodsService.selectById(id);
                GoodsSetting goodsSetting = goodsSettingService.selectByGoodsId(id);
                BeanUtils.copyProperties(goods,goodsBaseVo);
                if(null != goodsSetting){
                    BeanUtils.copyProperties(goodsSetting,goodsBaseVo);
                }
                GoodsUtils.goodsExpiryValue(goods.getExpiryValue(),goodsBaseVo);
                List<GoodsChannels> goodsChannelsList  = goodsChannelsService.selectGoodsChannelsByGoodsId(goods.getId());
                if(goodsChannelsList.size() > 0 ){
                    GoodsChannels goodsChannels = goodsChannelsList.get(0);
                    SalesChannel salesChannel = salesChannelService.selectById(goodsChannels.getChannelId());
                    goodsBaseVo.setBankId(salesChannel.getBankId());
                    goodsBaseVo.setSalesChannelId(salesChannel.getSalesChannelId());
                    goodsBaseVo.setSalesWayId(salesChannel.getSalesWayId());
                    List<String> ids = Lists.newArrayList();
                    ids.add(salesChannel.getBankId());
                    ids.add(salesChannel.getSalesChannelId() == null? "" : salesChannel.getSalesChannelId());
                    ids.add(salesChannel.getSalesWayId());
                    goodsBaseVo.setSalesChannelIds(ids);
                }

                List<GoodsGroupListRes> list = productGroupService.selectGoodsGroup(id);
                goodsBaseVo.setGoodsGroupListRes(list);
                goodsBaseVos.add(goodsBaseVo);
            }

            result.setResult(goodsBaseVos);
        }catch (Exception e){
            log.error("根据Id查找商品",e);
            result.setCode(200);
            result.setMsg("根据Id查找商品失败："+e.getMessage());
        }
        return result;
    }

    @SysGodDoorLog("上架、下架、暂售商品")
    @ApiOperation("上架、下架、暂售商品")
    @PostMapping("/status")
    @CacheEvict(value = "Goods",allEntries = true)
    public CommonResultVo<Goods> upper(@RequestBody GoodsBaseVo goodsVo){
        CommonResultVo<Goods>  result = new CommonResultVo<>();
        try {
            Assert.notNull(goodsVo.getId(),"商品ID不能为空！");
            Assert.notNull(goodsVo.getStatus(),"商品状态不能为空！");
            Goods goods = goodsService.selectById(goodsVo.getId());
            goods.setStatus(goodsVo.getStatus());
            goods.setUpdateTime(new Date());
            goodsService.updateById(goods);
            result.setResult(goods);
        }catch (Exception e){
            log.error("上架、下架、暂售商品",e);
            result.setCode(200);
            result.setMsg("上架、下架、暂售商品 ："+e.getMessage());
        }
        return result;
    }
    @SysGodDoorLog("上架、下架时间修改")
    @ApiOperation("上架、下架时间修改")
    @PostMapping("/updateGoodsStatus")
    @CacheEvict(value = "Goods",allEntries = true)
    public CommonResultVo<Goods> updateGoodsStatus(@RequestBody GoodsBaseVo goodsVo){
        CommonResultVo<Goods>  result = new CommonResultVo<>();
        try {
            Assert.notNull(goodsVo.getId(),"商品ID不能为空！");
//            Assert.notNull(goodsVo.getStatus(),"商品状态不能为空！");
            Goods goods = goodsService.selectById(goodsVo.getId());
            goods.setUpType(goodsVo.getUpType());
            goods.setUpstartTime(goodsVo.getUpstartTime());
            goods.setDownEndTime(goodsVo.getDownEndTime());
            if(goodsVo.getUpType().equals("1") || goodsVo.getUpType().equals("2")){
                goods.setStatus(goodsVo.getStatus());
            }
            goods.setUpdateTime(new Date());
            goodsService.updateById(goods);
            result.setResult(goods);
        }catch (Exception e){
            log.error("上架、下架、暂售商品",e);
            result.setCode(200);
            result.setMsg("上架、下架、暂售商品 ："+e.getMessage());
        }
        return result;
    }


    /**
     * 查找验证方式渠道
     * @return
     */
    @SysGodDoorLog("查找验证方式渠道")
    @ApiOperation("查找验证方式渠道")
    @GetMapping("/authType")
    public CommonResultVo<List<SysDict>> authType(){
        CommonResultVo<List<SysDict>> result = new CommonResultVo<>();
        try {
            List<SysDict> sysDictList = salesChannelService.selectSysDict(SysDictTypeEnums.AUTH_TYPE.getType());
            result.setResult(sysDictList);
        }catch (Exception e){
            log.error("查找验证方式渠道失败",e);
            result.setMsg(e.getMessage());
            result.setCode(200);
        }
        return result;
    }


    /**
     * 查找权益类型
     * @return
     */
    @SysGodDoorLog("查找权益类型")
    @ApiOperation("查找权益类型")
    @GetMapping("/giftType")
    public CommonResultVo<List<SysDict>> getAllGiftType(){
        CommonResultVo<List<SysDict>> result = new CommonResultVo<>();
        try {
            List<SysDict> sysDictList = salesChannelService.selectSysDict(SysDictTypeEnums.GIFT_TYPE.getType());
            result.setResult(sysDictList);
        }catch (Exception e){
            log.error("查找权益类型失败",e);
            result.setMsg(e.getMessage());
            result.setCode(200);
        }
        return result;
    }

    /**
     * 查找所有资源类型
     * @return
     */
    @SysGodDoorLog("查找所有资源类型")
    @ApiOperation("查找所有资源类型")
    @GetMapping("/serviceType")
    public CommonResultVo<List<SysDict>> getAllServiceType(){
        CommonResultVo<List<SysDict>> result = new CommonResultVo<>();
        try {
            List<SysDict> sysDictList = salesChannelService.selectSysDict(SysDictTypeEnums.SERVICE_TYPE.getType());
            result.setResult(sysDictList);
        }catch (Exception e){
            log.error("查找资源类型失败",e);
            result.setMsg(e.getMessage());
            result.setCode(200);
        }
        return result;
    }

    /**
     * 查找所有资源类型
     * @return
     */
    @SysGodDoorLog("查找所有字典表")
    @ApiOperation("查找所有字典表")
    @PostMapping("/getAllSysDict")
    public CommonResultVo<List<SysDict>> getAllSysDict(@RequestBody SysDict type){
        CommonResultVo<List<SysDict>> result = new CommonResultVo<>();
        try {
            List<SysDict> sysDictList = salesChannelService.selectSysDict(type.getType());
            result.setResult(sysDictList);
        }catch (Exception e){
            log.error("查找所有字典表",e);
            result.setMsg(e.getMessage());
            result.setCode(200);
        }
        return result;
    }

    /**
     * 查找权益类型
     * @return
     */
    @SysGodDoorLog("查找商品所有权益类型")
    @ApiOperation("查找商品所有权益类型")
    @GetMapping("/goodsGiftType/{id}")
    @Cacheable(value = "Goods",key = "'getGoodsGiftType_'+#id",unless = "#result == null")
    public CommonResultVo<List<String>> getGoodsGiftType(@PathVariable Integer id){
        CommonResultVo<List<String>> result = new CommonResultVo<>();
        try {
            List<String> goodsGiftTypeList = goodsService.getGoodsGiftType(SysDictTypeEnums.GIFT_TYPE.getType());
            result.setResult(goodsGiftTypeList);
        }catch (Exception e){
            log.error("查找商品所有权益类型失败",e);
            result.setMsg(e.getMessage());
            result.setCode(200);
        }
        return result;
    }

    /**
     * 查找商品所有资源类型
     * @return
     */
    @SysGodDoorLog("查找商品所有资源类型")
    @ApiOperation("查找商品所有资源类型")
    @GetMapping("/goodsServiceType/{id}")
    public CommonResultVo<List<String>> getGoodsServiceType(@PathVariable Integer id){
        CommonResultVo<List<String>> result = new CommonResultVo<>();
        try {
            List<String> goodsServiceType = salesChannelService.getGoodsServiceType(SysDictTypeEnums.SERVICE_TYPE.getType());
            result.setResult(goodsServiceType);
        }catch (Exception e){
            log.error("查找商品所有资源类型失败",e);
            result.setMsg(e.getMessage());
            result.setCode(200);
        }
        return result;
    }

    /**
     * 查询权益的到期日期
     * @param queryOrderExpireTimeReq
     * @return
     */
    @SysGodDoorLog("查询权益的到期日期")
    @ApiOperation("查询权益的到期日期")
    @PostMapping("/queryOrderExpireTime")
    public CommonResultVo<GoodsBaseVo> queryOrderExpireTime(@RequestBody GoodsExpiryDateReq queryOrderExpireTimeReq){
        CommonResultVo<GoodsBaseVo> result = new CommonResultVo<>();
        try {
            GoodsBaseVo goodsBaseVo = new GoodsBaseVo();
            Assert.notNull(queryOrderExpireTimeReq.getGoodsId(),"商品id不能为空");
            Assert.notNull(queryOrderExpireTimeReq.getOutDate(),"出库日期不能为空");
            Assert.notNull(queryOrderExpireTimeReq.getActiveDay(),"激活日期不能为空");
            Goods goods = goodsService.selectById(queryOrderExpireTimeReq.getGoodsId());
            BeanUtils.copyProperties(goods,goodsBaseVo);
            GoodsUtils.expiryGoodsDate(goodsBaseVo,queryOrderExpireTimeReq.getActiveDay(),queryOrderExpireTimeReq.getOutDate());
            result.setResult(goodsBaseVo);
        }catch (Exception e){
            log.error("查询权益的到期日期失败",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    @SysGodDoorLog("查询渠道佣金")
    @ApiOperation("查询渠道佣金")
    @PostMapping("/getSalesChannel")
    public CommonResultVo<SalesChannel> getSalesChannel(@RequestBody GoodsBaseVo goods){
        CommonResultVo<SalesChannel> result = new CommonResultVo<>();
        try {
            SalesChannel salesChannel = salesChannelService.selectOneDate(goods.getSalesChannelIds().get(0),goods.getSalesChannelIds().get(1) ,goods.getSalesChannelIds().get(2));
            result.setResult(salesChannel);
        }catch (Exception e){
            log.error("查询权益的到期日期失败",e);
            result.setCode(200);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    @SysGodDoorLog("查询商品图片")
    @ApiOperation("查询商品图片")
    @GetMapping("/pic/list/{goodsId}")
    @Cacheable(value = "Goods",key = "'picList_'+#goodsId",unless = "#result == null")
    public CommonResultVo<List<SysFileDto>> picList(@PathVariable Integer goodsId){
        CommonResultVo<List<SysFileDto>> resultVo=new CommonResultVo<>();
        try {
            ListSysFileReq sysFileReq=new ListSysFileReq();
            sysFileReq.setType("goods.pic");
            sysFileReq.setObjId(goodsId);
            final List<SysFileDto> fileDtoList = sysFileService.listSysFileDto(sysFileReq);
            resultVo.setResult(fileDtoList);
        }catch (Exception e){
            log.error("文件列表查询失败",e);
            resultVo.setCode(200);
            resultVo.setMsg(e.getMessage());
        }
        return resultVo;
    }
    
    @SysGodDoorLog("根据Id查找商品")
    @GetMapping("/getGoodsBaseById/{id}")
    @Cacheable(value = "Goods",key = "'getGoodsBaseById_'+#id",unless = "#result == null")
    public CommonResultVo<GoodsBaseVo> getGoodsBaseById(@PathVariable Integer id){
        CommonResultVo<GoodsBaseVo>  result = new CommonResultVo<>();
        try {
            GoodsBaseVo goodsBaseVo = new GoodsBaseVo();
            Goods goods = goodsService.selectById(id);
            GoodsSetting goodsSetting = goodsSettingService.selectByGoodsId(id);
            BeanUtils.copyProperties(goods,goodsBaseVo);
            if(null != goodsSetting){
                BeanUtils.copyProperties(goodsSetting,goodsBaseVo);
            }
            GoodsUtils.goodsExpiryValue(goods.getExpiryValue(),goodsBaseVo);
            List<GoodsChannels> goodsChannelsList  = goodsChannelsService.selectGoodsChannelsByGoodsId(goods.getId());
            if(goodsChannelsList.size() > 0 ){
                GoodsChannels goodsChannels = goodsChannelsList.get(0);
                SalesChannel salesChannel = salesChannelService.selectById(goodsChannels.getChannelId());
                goodsBaseVo.setBankId(salesChannel.getBankId());
                goodsBaseVo.setSalesChannelId(salesChannel.getSalesChannelId());
                goodsBaseVo.setSalesWayId(salesChannel.getSalesWayId());
                List<String> ids = Lists.newArrayList();
                ids.add(salesChannel.getBankId());
                ids.add(salesChannel.getSalesChannelId() == null? "" : salesChannel.getSalesChannelId());
                ids.add(salesChannel.getSalesWayId());
                goodsBaseVo.setSalesChannelIds(ids);
            }
//            List<GoodsGroupListRes> list = productGroupService.selectGoodsGroup(id);
//            goodsBaseVo.setGoodsGroupListRes(list);
            result.setResult(goodsBaseVo);
        }catch (Exception e){
            log.error("根据Id查找商品",e);
            result.setCode(200);
            result.setMsg("根据Id查找商品失败："+e.getMessage());
        }
        return result;
    }
}