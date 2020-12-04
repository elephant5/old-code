package com.colourfulchina.pangu.taishang.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.colourfulchina.god.door.api.annotation.SysGodDoorLog;
import com.colourfulchina.god.door.api.util.SecurityUtils;
import com.colourfulchina.inf.base.utils.ShortUrlUtil;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.pangu.taishang.api.dto.GoodsPortalSettingDto;
import com.colourfulchina.pangu.taishang.api.dto.SysFileDto;
import com.colourfulchina.pangu.taishang.api.entity.GoodsPortalSetting;
import com.colourfulchina.pangu.taishang.api.entity.SysBankLogo;
import com.colourfulchina.pangu.taishang.api.vo.SysBankLogoDto;
import com.colourfulchina.pangu.taishang.api.vo.res.GoodsChannelRes;
import com.colourfulchina.pangu.taishang.config.GoodsProperties;
import com.colourfulchina.pangu.taishang.service.GoodsChannelsService;
import com.colourfulchina.pangu.taishang.service.GoodsPortalSettingService;
import com.colourfulchina.pangu.taishang.service.SysBankLogoService;
import com.colourfulchina.pangu.taishang.service.SysFileService;
import com.colourfulchina.tianyan.admin.api.feign.RemoteDictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/GoodsPortalSetting")
@Api(tags = {"商品前端设置接口"})
public class GoodsPortalSettingController {
    @Resource
    private GoodsPortalSettingService goodsPortalSettingService;

    @Autowired
    private GoodsProperties goodsProperties;
    @Resource
    private SysBankLogoService sysBankLogoService;
    @Autowired
    private SysFileService sysFileService;
    @Autowired
    private GoodsChannelsService goodsChannelsService;
    /**
     *
     * @param goodsPortalSetting
     * @return
     */
    @SysGodDoorLog("查询商品前端设置列表")
    @ApiOperation("查询商品前端设置列表")
    @PostMapping("/list")
    public CommonResultVo<List<GoodsPortalSetting>> list(@RequestBody GoodsPortalSetting goodsPortalSetting){
        CommonResultVo<List<GoodsPortalSetting>> resultVo=new CommonResultVo<>();
        try {
            Wrapper<GoodsPortalSetting> configWrapper=new Wrapper<GoodsPortalSetting>() {
                @Override
                public String getSqlSegment() {
                    if (goodsPortalSetting == null){
                        return null;
                    }
                    StringBuffer sql=new StringBuffer("where 1=1");
                    if (StringUtils.isNotBlank(goodsPortalSetting.getCode())){
                        sql.append(" and ").append("code = '").append(goodsPortalSetting.getCode()).append("'");
                    }
                    if (goodsPortalSetting.getGoodsId() != null){
                        sql.append(" and ").append("goods_id = ").append(goodsPortalSetting.getGoodsId());
                    }
                    if (goodsPortalSetting.getDelFlag() != null){
                        sql.append(" and ").append("del_flag = ").append(goodsPortalSetting.getDelFlag());
                    }
                    return sql.toString();
                }
            };
            final List<GoodsPortalSetting> sysHolidayConfigs = goodsPortalSettingService.selectList(configWrapper);
            resultVo.setResult(sysHolidayConfigs);
        }catch (Exception e){
            log.error("查询商品前端设置列表失败",e);
            resultVo.setCode(200);
            resultVo.setMsg(e.getMessage());
        }

        return resultVo;
    }

    @SysGodDoorLog("根据goodsId查询商品前端设置")
    @ApiOperation("根据goodsId查询商品前端设置")
    @GetMapping("/get/{goodsId}")
    @Cacheable(value = "GoodsPortalSetting",key = "'get_'+#goodsId",unless = "#result == null")
    public CommonResultVo<GoodsPortalSettingDto> get(@PathVariable Integer goodsId){
        CommonResultVo<GoodsPortalSettingDto> resultVo=new CommonResultVo<>();
        try {
            GoodsPortalSettingDto portalSettingDto=new GoodsPortalSettingDto();
            portalSettingDto.setGoodsId(goodsId);
            portalSettingDto.setGiftUrl(goodsProperties.getGiftUrl());
            //查询销售渠道
            setBankCodeAndGiftUrl(portalSettingDto);
            Wrapper<GoodsPortalSetting> wrapper=new Wrapper<GoodsPortalSetting>() {
                @Override
                public String getSqlSegment() {
                    return "where goods_id = "+goodsId;
                }
            };
            //不使用selectOne 防止后面一个商品id对应多个网站设置
            final List<GoodsPortalSetting> portalSettingList = goodsPortalSettingService.selectList(wrapper);
            if (!CollectionUtils.isEmpty(portalSettingList)){
                BeanUtils.copyProperties(portalSettingList.get(0),portalSettingDto);
                if (portalSettingDto.getBankLogoId() != null){
                    //设置商品logo
                    final SysBankLogo sysBankLogo = sysBankLogoService.selectById(portalSettingDto.getBankLogoId());
                    if (sysBankLogo != null){
                        SysBankLogoDto sysBankLogoDto = new SysBankLogoDto();
                        BeanUtils.copyProperties(sysBankLogo,sysBankLogoDto);
                        final SysFileDto sysFileDto = sysFileService.getSysFileDtoById(sysBankLogo.getFileId());
                        sysBankLogoDto.setSysFileDto(sysFileDto);
                        portalSettingDto.setSysBankLogo(sysBankLogoDto);
                    }
                }
            }
            resultVo.setResult(portalSettingDto);
        }catch (Exception e){
            log.error("查询商品前端设置失败",e);
            resultVo.setCode(200);
            resultVo.setMsg(e.getMessage());
        }
        return resultVo;
    }

    /**
     * 获取银行code
     * @param portalSettingDto
     * @throws Exception
     */
    private void setBankCodeAndGiftUrl(GoodsPortalSettingDto portalSettingDto) throws Exception {
        Assert.notNull(portalSettingDto,"getBankCode参数不能为空");
        Assert.notNull(portalSettingDto.getGoodsId(),"getBankCode goodsId参数不能为空");
        final List<GoodsChannelRes> goodsChannelResList = goodsChannelsService.selectGoodsChannel(portalSettingDto.getGoodsId());
        Assert.notEmpty(goodsChannelResList,"商品未设置销售渠道");
        Assert.isTrue(goodsChannelResList.size() == 1,"商品不能有多个销售渠道");
        GoodsChannelRes goodsChannelRes=goodsChannelResList.get(0);
        portalSettingDto.setBankCode(goodsChannelRes.getBankCode());
        portalSettingDto.setGiftUrl(goodsProperties.getGiftUrl()+goodsChannelRes.getBankCode()+"&prjCode=");
    }

    @SysGodDoorLog("根据code查询商品前端设置")
    @ApiOperation("根据code查询商品前端设置")
    @GetMapping("/getByCode/{code}")
    @Cacheable(value = "GoodsPortalSetting",key = "'getByCode_'+#code",unless = "#result == null")
    public CommonResultVo<GoodsPortalSettingDto> getByCode(@PathVariable String code){
        CommonResultVo<GoodsPortalSettingDto> resultVo=new CommonResultVo<>();
        try {
            GoodsPortalSettingDto portalSettingDto=new GoodsPortalSettingDto();
            portalSettingDto.setGiftUrl(goodsProperties.getGiftUrl());
            Wrapper<GoodsPortalSetting> wrapper=new Wrapper<GoodsPortalSetting>() {
                @Override
                public String getSqlSegment() {
                    return "where code = '"+code+"'";
                }
            };
            //不使用selectOne 防止后面一个商品id对应多个网站设置
            final List<GoodsPortalSetting> portalSettingList = goodsPortalSettingService.selectList(wrapper);
            if (!CollectionUtils.isEmpty(portalSettingList)){
                BeanUtils.copyProperties(portalSettingList.get(0),portalSettingDto);
                setBankCodeAndGiftUrl(portalSettingDto);
                if (portalSettingDto.getBankLogoId() != null){
                    //设置商品logo
                    final SysBankLogo sysBankLogo = sysBankLogoService.selectById(portalSettingDto.getBankLogoId());
                    if (sysBankLogo != null){
                        SysBankLogoDto sysBankLogoDto = new SysBankLogoDto();
                        BeanUtils.copyProperties(sysBankLogo,sysBankLogoDto);
                        final SysFileDto sysFileDto = sysFileService.getSysFileDtoById(sysBankLogo.getFileId());
                        sysBankLogoDto.setSysFileDto(sysFileDto);
                        portalSettingDto.setSysBankLogo(sysBankLogoDto);
                    }
                }
            }
            resultVo.setResult(portalSettingDto);
        }catch (Exception e){
            log.error("查询商品前端设置失败",e);
            resultVo.setCode(200);
            resultVo.setMsg(e.getMessage());
        }
        return resultVo;
    }

    private void setGiftShortUrl(GoodsPortalSetting goodsPortalSetting) throws Exception{
        Assert.notNull(goodsPortalSetting,"参数不能为空");
        Assert.notNull(goodsPortalSetting.getGoodsId(),"参数goodsId不能为空");
        Assert.hasText(goodsPortalSetting.getCode(),"参数code不能为空");
        GoodsPortalSettingDto goodsPortalSettingDto=new GoodsPortalSettingDto();
        BeanUtils.copyProperties(goodsPortalSetting,goodsPortalSettingDto);
        setBankCodeAndGiftUrl(goodsPortalSettingDto);
        final String shortUrl2 = ShortUrlUtil.getKlfShortUrl(goodsPortalSettingDto.getGiftFullUrl());
        goodsPortalSetting.setShortUrl(shortUrl2);
    }

    @SysGodDoorLog("更新商品前端设置")
    @ApiOperation("更新商品前端设置")
    @PostMapping("/update")
    @CacheEvict(value = {"GoodsPortalSetting","Goods"},allEntries = true)
    public CommonResultVo<GoodsPortalSettingDto> update(@RequestBody GoodsPortalSetting goodsPortalSetting){
        CommonResultVo<GoodsPortalSettingDto> resultVo=new CommonResultVo<>();
        try {
            Assert.notNull(goodsPortalSetting,"参数不能为空");
            Assert.notNull(goodsPortalSetting.getId(),"参数id不能为空");
            Assert.notNull(goodsPortalSetting.getGoodsId(),"参数goodsId不能为空");
            Assert.hasText(goodsPortalSetting.getCode(),"参数code不能为空");
            log.debug("goodsPortalSetting:{}", JSON.toJSONString(goodsPortalSetting));
            setGiftShortUrl(goodsPortalSetting);
            goodsPortalSetting.setUpdateUser(SecurityUtils.getLoginName());
            goodsPortalSetting.setUpdateTime(new Date());
            final boolean updateById = goodsPortalSettingService.updateById(goodsPortalSetting);
            log.debug("updateById:{}",updateById);
            Assert.isTrue(updateById,"更新商品前端设置失败");
            GoodsPortalSettingDto portalSettingDto=new GoodsPortalSettingDto();
            BeanUtils.copyProperties(goodsPortalSetting,portalSettingDto);
            setBankCodeAndGiftUrl(portalSettingDto);
            resultVo.setResult(portalSettingDto);
        }catch (Exception e){
            log.error("更新商品前端设置失败",e);
            resultVo.setCode(200);
            resultVo.setMsg(e.getMessage());
        }
        return resultVo;
    }

    @SysGodDoorLog("添加商品前端设置")
    @ApiOperation("添加商品前端设置")
    @PostMapping("/add")
    @CacheEvict(value = {"GoodsPortalSetting","Goods"},allEntries = true)
    public CommonResultVo<GoodsPortalSettingDto> add(@RequestBody GoodsPortalSetting goodsPortalSetting){
        CommonResultVo<GoodsPortalSettingDto> resultVo=new CommonResultVo<>();
        try {
            Assert.notNull(goodsPortalSetting,"参数不能为空");
            Assert.hasText(goodsPortalSetting.getCode(),"参数code不能为空");
            Assert.notNull(goodsPortalSetting.getGoodsId(),"参数goodsId不能为空");
            setGiftShortUrl(goodsPortalSetting);
            goodsPortalSetting.setCreateUser(SecurityUtils.getLoginName());
            goodsPortalSetting.setCreateTime(new Date());
            goodsPortalSetting.setUpdateUser(SecurityUtils.getLoginName());
            goodsPortalSetting.setUpdateTime(new Date());
            final boolean insert = goodsPortalSettingService.insert(goodsPortalSetting);
            Assert.isTrue(insert,"添加商品前端设置失败");
            GoodsPortalSettingDto portalSettingDto=new GoodsPortalSettingDto();
            BeanUtils.copyProperties(goodsPortalSetting,portalSettingDto);
            setBankCodeAndGiftUrl(portalSettingDto);
            resultVo.setResult(portalSettingDto);
        }catch (Exception e){
            log.error("添加商品前端设置失败",e);
            resultVo.setCode(200);
            resultVo.setMsg(e.getMessage());
        }
        return resultVo;
    }

}
