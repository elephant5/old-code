package com.colourfulchina.pangu.taishang.controller;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.colourfulchina.bigan.api.feign.RemoteShopItemService;
import com.colourfulchina.bigan.api.feign.RemoteSysFileService;
import com.colourfulchina.god.door.api.annotation.SysGodDoorLog;
import com.colourfulchina.god.door.api.util.SecurityUtils;
import com.colourfulchina.inf.base.encrypt.EncryptMD5;
import com.colourfulchina.inf.base.entity.SysOperateLogInfo;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.pangu.taishang.api.dto.SysFileDto;
import com.colourfulchina.pangu.taishang.api.entity.*;
import com.colourfulchina.pangu.taishang.api.enums.DelFlagEnums;
import com.colourfulchina.pangu.taishang.api.vo.req.ListSysFileReq;
import com.colourfulchina.pangu.taishang.config.FtpProperties;
import com.colourfulchina.pangu.taishang.service.*;
import com.colourfulchina.pangu.taishang.utils.FtpClientUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.InvalidParameterException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sysFile")
@Slf4j
@AllArgsConstructor
@Api(tags = {"文件操作接口"})
public class SysFileController {
    @Autowired
    FtpProperties ftpProperties;
    @Autowired
    SysFileTypeService sysFileTypeService;
    @Autowired
    SysFileService sysFileService;
    @Autowired
    SysFileQuoteService sysFileQuoteService;
    @Autowired
    SysOperateLogService sysOperateLogService;
    @Autowired
    private HotelPortalImgService hotelPortalImgService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private RemoteShopItemService remoteShopItemService;
    @Autowired
    private ShopItemService shopItemService;
    private final RemoteSysFileService remoteSysFileService;
    private static final List<String> FIXED_PATH_FILE_TYPE=Lists.newArrayList();
    static {
        FIXED_PATH_FILE_TYPE.add("bank.logo");
    }
    /**
     * 上传文件并返回文件路径
     * @param fileType
     * @param file
     * @return
     */
    @ApiOperation("上传文件接口")
    @PostMapping("/upload")
    public CommonResultVo<SysFileDto> upload(String fileType, MultipartFile file){
        CommonResultVo<SysFileDto> resultVo=new CommonResultVo<>();
        log.info("单个文件上传{}-{}",fileType,file.getOriginalFilename());
        try {
            Assert.hasText(fileType,"文件类型为空");
            Assert.notNull(file,"文件列表为空");
            if (StringUtils.isBlank(fileType)){
                throw new InvalidParameterException("文件类型为空");
            }
            if (file == null){
                throw new InvalidParameterException("文件列表为空");
            }
            final SysFileType sysFileType = sysFileTypeService.selectById(fileType);
            if (sysFileType == null){
                throw new Exception("文件类型错误");
            }
            String path=sysFileType.getPath();
            if (!FIXED_PATH_FILE_TYPE.contains(fileType)){
                final String midPath=DateUtil.format(new Date(),"yyyy/MM/dd");
                path=midPath+sysFileType.getPath();
            }

            //为了保证文件的顺序 所以使用LinkedHashMap
            Map<String, MultipartFile> fileInfo= Maps.newLinkedHashMap();

            final String filename = file.getOriginalFilename();
            String code=EncryptMD5.MD5_16(filename+System.currentTimeMillis());
            fileInfo.put(code, file);
            SysFileDto sysFile=new SysFileDto();
            sysFile.setGuid(path+""+code);
            sysFile.setPath(path);
            sysFile.setCode(code);
            sysFile.setExt(filename.substring(filename.lastIndexOf(".")+1));
            sysFile.setName(filename.substring(0,filename.lastIndexOf(".")));
            sysFile.setSize(file.getSize());
            sysFile.setCreateTime(new Date());
            sysFile.setUpdateTime(new Date());
            sysFile.setCreateUser(SecurityUtils.getLoginName());
            sysFile.setUpdateUser(SecurityUtils.getLoginName());
            sysFile.setErpCdnUrl(ftpProperties.getErpCdnUrl());
            sysFile.setPgCdnUrl(ftpProperties.getPgCdnUrl());
            sysFile.setType(fileType);
            //上传新系统
            FtpClientUtils.batchUpload(ftpProperties.getPgHost(),ftpProperties.getPgPort(),ftpProperties.getPgUsername(),ftpProperties.getPgPassword(),sysFileType.getPgPrefix()+path,fileInfo);
            //上传旧系统
            FtpClientUtils.batchUpload(ftpProperties.getErpHost(),ftpProperties.getErpPort(),ftpProperties.getErpUsername(),ftpProperties.getErpPassword(),sysFileType.getErpPrefix()+path,fileInfo);

            resultVo.setResult(sysFile);
        } catch (Exception e) {
            log.error("文件上传失败",e);
            resultVo.setCode(200);
            resultVo.setMsg(e.getMessage());
        }
        return resultVo;
    }

    /**
     * 批量上传文件并返回文件路径
     * @param fileType
     * @param objId
     * @param tag
     * @param fileList
     * @return
     */
    @ApiOperation("批量上传文件接口")
    @PostMapping("/uploadBatch")
    public CommonResultVo<List<SysFileDto>> uploadBatch(String fileType, Integer objId, String tag, List<MultipartFile> fileList){
        CommonResultVo<List<SysFileDto>> resultVo=new CommonResultVo<>();
        try {
            Assert.hasText(fileType,"文件类型为空");
            Assert.notNull(objId,"对象id为空");
            Assert.notEmpty(fileList,"文件列表为空");
            if (StringUtils.isBlank(fileType)){
                throw new InvalidParameterException("文件类型为空");
            }
            if (objId == null){
                throw new InvalidParameterException("对象id为空");
            }
            if (CollectionUtils.isEmpty(fileList)){
                throw new InvalidParameterException("文件列表为空");
            }
            final SysFileType sysFileType = sysFileTypeService.selectById(fileType);
            if (sysFileType == null){
                throw new Exception("文件类型错误");
            }
            final String midPath=DateUtil.format(new Date(),"yyyy/MM/dd");
            final String path=midPath+sysFileType.getPath()+objId;
            //为了保证文件的顺序 所以使用LinkedHashMap
            Map<String, MultipartFile> fileInfo= Maps.newLinkedHashMap();

            List<SysFileDto> sysFileList= Lists.newArrayList();
            for (int i=0;i<fileList.size();i++){
                final MultipartFile multipartFile = fileList.get(i);
                final String filename = multipartFile.getOriginalFilename();
                String code=EncryptMD5.MD5_16(filename+i+System.currentTimeMillis());
                fileInfo.put(code, multipartFile);
                SysFileDto sysFile=new SysFileDto();
                sysFile.setGuid(path+""+code);
                sysFile.setPath(path);
                sysFile.setCode(code);
                sysFile.setExt(filename.substring(filename.lastIndexOf(".")+1));
                sysFile.setName(filename.substring(0,filename.lastIndexOf(".")));
                sysFile.setSize(multipartFile.getSize());
                sysFile.setTag(tag);
                sysFile.setCreateTime(new Date());
                sysFile.setUpdateTime(new Date());
                sysFile.setCreateUser(SecurityUtils.getLoginName());
                sysFile.setUpdateUser(SecurityUtils.getLoginName());
                sysFile.setErpCdnUrl(ftpProperties.getErpCdnUrl());
                sysFile.setPgCdnUrl(ftpProperties.getPgCdnUrl());
                sysFile.setType(fileType);
                sysFileList.add(sysFile);
            }
            log.info("ftpProperties:{}",JSON.toJSONString(ftpProperties));
            //上传新系统
            FtpClientUtils.batchUpload(ftpProperties.getPgHost(),ftpProperties.getPgPort(),ftpProperties.getPgUsername(),ftpProperties.getPgPassword(),sysFileType.getPgPrefix()+path,fileInfo);
            //上传旧系统
            FtpClientUtils.batchUpload(ftpProperties.getErpHost(),ftpProperties.getErpPort(),ftpProperties.getErpUsername(),ftpProperties.getErpPassword(),sysFileType.getErpPrefix()+path,fileInfo);

            resultVo.setResult(sysFileList);
        } catch (Exception e) {
            log.error("文件上传失败",e);
            resultVo.setCode(200);
            resultVo.setMsg(e.getMessage());
        }
        return resultVo;
    }

    /**
     * 更新文件信息
     * @param fileDtoList
     * @return
     */
    @SysGodDoorLog("更新文件接口")
    @ApiOperation("更新文件接口")
    @PostMapping("/merge")
    @CacheEvict(value = {"Hotel","Goods","Shop","ShopItem","ProductGroup","GoodsDetail","SysBankLogo","GoodsPortalSetting"},allEntries = true)
    public CommonResultVo<List<SysFileDto>> merge(@RequestBody List<SysFileDto> fileDtoList){
        CommonResultVo<List<SysFileDto>> resultVo=new CommonResultVo<>();
        try {
            Assert.notEmpty(fileDtoList,"参数为空");
            Assert.notNull(fileDtoList.get(0).getObjId(),"参数objId不能为空");
            Assert.hasText(fileDtoList.get(0).getType(),"参数type不能为空");
            final SysFileType sysFileType = sysFileTypeService.selectById(fileDtoList.get(0).getType());
            Assert.notNull(sysFileType,"参数type有误");
            Assert.isTrue(sysFileType.getDelFlag().compareTo(0)==0,"参数type已禁用");
            sysFileService.merge(fileDtoList);

            //1.把objId转换成旧的objId
            SysOperateLogInfo logInfo=new SysOperateLogInfo();
            logInfo.setTableName(sysFileType.getTable());
            logInfo.setTableId(sysFileType.getTableId());
            logInfo.setRowKey(fileDtoList.get(0).getObjId());
            final Map<String, Object> dynamicSelectById = sysOperateLogService.dynamicSelectById(logInfo);

            if (CollectionUtils.isEmpty(dynamicSelectById)){
                log.error("sysOperateLogService.dynamicSelectById结果为空,参数:{}", JSON.toJSONString(logInfo));
            }
            else {
                final Object oldId = dynamicSelectById.get(sysFileType.getOldTableId());
                log.info("sysOperateLogService.dynamicSelectById结果:{}", JSON.toJSONString(dynamicSelectById));
                if (oldId == null){
                    log.error("sysOperateLogService.dynamicSelectById key:{}为空{}", sysFileType.getOldTableId());
                }else {
                    Integer oldObjId= NumberUtils.toInt(oldId+"");
                    //2.调用远程服务
                    List<com.colourfulchina.bigan.api.dto.SysFileDto> fileList=Lists.newArrayList();
                    fileDtoList.forEach(fileDto->{
                        com.colourfulchina.bigan.api.dto.SysFileDto sysFileDto=new com.colourfulchina.bigan.api.dto.SysFileDto();
                        BeanUtils.copyProperties(fileDto,sysFileDto,"id","objId");
                        sysFileDto.setObjId(oldObjId);
                        fileList.add(sysFileDto);
                    });
                    final CommonResultVo<Boolean> merge = remoteSysFileService.merge(fileList);
                    Assert.notNull(merge,"远程服务remoteSysFileService.merge调用失败");
                    if (merge.getCode() == 100 && merge.getResult().equals(Boolean.TRUE)){
                        log.info("远程服务remoteSysFileService.merge处理成功");
                    }else {
                        log.error("远程服务remoteSysFileService.merge处理失败,{}",merge.getMsg());
                    }
                }
            }
            resultVo.setResult(fileDtoList);
        }catch (Exception e){
            log.error("文件更新失败",e);
            resultVo.setCode(200);
            resultVo.setMsg(e.getMessage());
        }
        return resultVo;
    }

    @SysGodDoorLog("根据fileId删除文件")
    @ApiOperation("根据fileId删除文件")
    @GetMapping("/deleteFileByFileId/{fileId}")
    public CommonResultVo<Boolean> deleteFileByFileId(@PathVariable Integer fileId){
        CommonResultVo<Boolean> resultVo=new CommonResultVo<>();
        try {
            final SysFile file = sysFileService.selectById(fileId);
            if (file != null){
                SysFileQuote sysFileQuote = new SysFileQuote();
                sysFileQuote.setFileId(fileId);
                sysFileQuote.setDelFlag(1);
                sysFileQuote.setUpdateUser(SecurityUtils.getLoginName());
                sysFileQuote.setUpdateTime(new Date());
                SysFile sysFile=new SysFile();
                sysFile.setId(fileId);
                sysFile.setDelFlag(1);
                sysFile.setUpdateUser(SecurityUtils.getLoginName());
                sysFile.setUpdateTime(new Date());
                sysFileQuoteService.updateDelFlagByFileId(sysFileQuote);
                sysFileService.updateDelFlagById(sysFile);
                remoteSysFileService.deleteFileByGuid(file.getGuid());
            }
            resultVo.setResult(Boolean.TRUE);
        }catch (Exception e){
            resultVo.setCode(200);
            resultVo.setMsg(e.getMessage());
            log.error("根据fileId删除文件失败",e);
        }
        return resultVo;
    }

    @SysGodDoorLog("根据type objId查询文件列表")
    @ApiOperation("根据type objId查询文件列表")
    @PostMapping("/list")
    public CommonResultVo<List<SysFileDto>> list(@RequestBody ListSysFileReq sysFileReq){
        CommonResultVo<List<SysFileDto>> resultVo=new CommonResultVo<>();
        try {
            Assert.notNull(sysFileReq,"参数不能为空");
            Assert.hasText(sysFileReq.getType(),"参数type不能为空");
            Assert.notNull(sysFileReq.getObjId(),"参数objId不能为空");
            final List<SysFileDto> fileDtoList = sysFileService.listSysFileDto(sysFileReq);
            resultVo.setResult(fileDtoList);
        }catch (Exception e){
            log.error("文件列表查询失败",e);
            resultVo.setCode(200);
            resultVo.setMsg(e.getMessage());
        }
        return resultVo;
    }
    @SysGodDoorLog("根据id查询文件")
    @ApiOperation("根据id查询文件")
    @GetMapping("/getById/{id}")
    public CommonResultVo<SysFileDto> getByGUID(@PathVariable Integer id){
        CommonResultVo<SysFileDto> resultVo=new CommonResultVo<>();
        try {
            final SysFileDto sysFileDto = sysFileService.getSysFileDtoById(id);
            resultVo.setResult(sysFileDto);
        }catch (Exception e){
            log.error("文件列表查询失败",e);
            resultVo.setCode(200);
            resultVo.setMsg(e.getMessage());
        }
        return resultVo;
    }

    /**
     *
     * @param username
     * @param password
     * @param type
     * @param objId 新系统的objId
     * @return
     */
    @SysGodDoorLog("初始化文件数据")
    @ApiOperation("初始化文件数据")
    @GetMapping("/init")
    public CommonResultVo<Boolean> init(String username,String password,String type,Integer objId){
        CommonResultVo<Boolean> resultVo=new CommonResultVo<>();
        try {
            Assert.hasText(username,"username不能为空");
            Assert.hasText(password,"password不能为空");
            Assert.isTrue("hyper.huang".equals(username),"username有误");
            Assert.isTrue("1qaz@WSX".equals(password),"password有误");
            com.colourfulchina.bigan.api.dto.ListSysFileReq sysFileReq=new com.colourfulchina.bigan.api.dto.ListSysFileReq();
            sysFileReq.setType(type);
            sysFileReq.setObjId(objId);
            final CommonResultVo<List<com.colourfulchina.bigan.api.dto.SysFileDto>> commonResultVo = remoteSysFileService.list(sysFileReq);
            Assert.notNull(commonResultVo,"远程服务remoteSysFileService.list调用失败");
            if (commonResultVo.getCode() != 100){
                log.error("远程服务remoteSysFileService.list处理失败:{}",commonResultVo.getMsg());
            }else {
                final List<com.colourfulchina.bigan.api.dto.SysFileDto> result = commonResultVo.getResult();
                log.info("远程服务remoteSysFileService.list处理成功");
                if (CollectionUtils.isEmpty(result)){
                    log.info("远程服务remoteSysFileService.list结果为空");
                }else {
                    //key为type-objId，value为newObjId
                    Map<String,Integer> typeObjMap=Maps.newHashMap();
                    Map<String,SysFileType> typeMap=Maps.newHashMap();
                    for (com.colourfulchina.bigan.api.dto.SysFileDto fileDto : result){
                        SysFileType sysFileType=typeMap.get(fileDto.getType());
                        if (sysFileType== null){
                            sysFileType = sysFileTypeService.selectById(fileDto.getType());
                            typeMap.put(fileDto.getType(),sysFileType);
                        }
                        if (sysFileType.getDelFlag().compareTo(1) == 0){
                            log.info("objId:{} 对应的文件类型 {}:{} 已停用",fileDto.getObjId(),sysFileType.getType(),sysFileType.getName());
                            continue;
                        }
                        SysFileDto sysFileDto=new SysFileDto();
                        BeanUtils.copyProperties(fileDto,sysFileDto,"id","objId","fileId");
                        Integer newObjId=typeObjMap.get(fileDto.getType()+"-"+fileDto.getObjId());
                        //如果为空 则进行数据库查询 并放入 typeObjMap
                        if (newObjId == null){
                            SysOperateLogInfo logInfo=new SysOperateLogInfo();
                            logInfo.setTableName(sysFileType.getTable());
                            logInfo.setTableId(sysFileType.getOldTableId());
                            logInfo.setRowKey(fileDto.getObjId());
                            final Map<String, Object> dynamicSelectById = sysOperateLogService.dynamicSelectById(logInfo);
                            if (CollectionUtils.isEmpty(dynamicSelectById)){
                                log.info("sysOperateLogService.dynamicSelectById结果为空,参数:{}", JSON.toJSONString(logInfo));
                                continue;
                            }
                            log.info("sysOperateLogService.dynamicSelectById结果:{}", JSON.toJSONString(dynamicSelectById));
                            newObjId = NumberUtils.toInt(dynamicSelectById.get(sysFileType.getTableId())+"");
                            typeObjMap.put(fileDto.getType()+"-"+fileDto.getObjId(),newObjId);
                            sysFileDto.setObjId(newObjId);
                            SysFile sysFile=new SysFile();
                            BeanUtils.copyProperties(sysFileDto,sysFile,"id");
                            sysFile.setCreateTime(new Date());
                            sysFile.setUpdateTime(new Date());
                            sysFile.setCreateUser(SecurityUtils.getLoginName());
                            sysFile.setUpdateUser(SecurityUtils.getLoginName());
                            sysFileService.insert(sysFile);
                            SysFileQuote fileQuote=new SysFileQuote();
                            BeanUtils.copyProperties(sysFileDto,fileQuote,"fileId");
                            fileQuote.setFileId(sysFile.getId());
                            fileQuote.setCreateTime(new Date());
                            fileQuote.setUpdateTime(new Date());
                            fileQuote.setCreateUser(SecurityUtils.getLoginName());
                            fileQuote.setUpdateUser(SecurityUtils.getLoginName());
                            sysFileQuoteService.insert(fileQuote);
                        }
                    }
                }
            }
        }catch (Exception e){
            log.error("图片初始化失败",e);
        }
        return resultVo;
    }

    @SysGodDoorLog("文件同步")
    @ApiOperation("文件同步")
    @PostMapping("/syncFile")
    public CommonResultVo<Boolean> syncFile(String username,String password,String type,Integer objId){
        log.info("文件同步开始");
        CommonResultVo<Boolean> resultVo=new CommonResultVo<>();
        try {
            Assert.hasText(username,"username不能为空");
            Assert.hasText(password,"password不能为空");
            Assert.isTrue("hyper.huang".equals(username),"username有误");
            Assert.isTrue("1qaz@WSX".equals(password),"password有误");
            com.colourfulchina.bigan.api.dto.ListSysFileReq sysFileReq=new com.colourfulchina.bigan.api.dto.ListSysFileReq();
            sysFileReq.setType(type);
            sysFileReq.setObjId(objId);
            final CommonResultVo<List<com.colourfulchina.bigan.api.dto.SysFileDto>> commonResultVo = remoteSysFileService.list(sysFileReq);
            Assert.notNull(commonResultVo,"远程服务remoteSysFileService.list调用失败");
            if (commonResultVo.getCode() != 100){
                log.error("远程服务remoteSysFileService.list处理失败:{}",commonResultVo.getMsg());
            }else {
                final List<com.colourfulchina.bigan.api.dto.SysFileDto> result = commonResultVo.getResult();
                log.info("远程服务remoteSysFileService.list处理成功");
                if (CollectionUtils.isEmpty(result)){
                    log.info("远程服务remoteSysFileService.list结果为空");
                }else {
                    //用于酒店章节图片重复添加
                    HashSet hotelSet = Sets.newHashSet();
                    for (com.colourfulchina.bigan.api.dto.SysFileDto fileDto : result){
                        //酒店章节图片同步
                        if (fileDto.getType().equals("hotel.file")){
                            SysFile sysFile = new SysFile();
                            sysFile.setOldId(fileDto.getId());
                            sysFile.setGuid(fileDto.getGuid());
                            sysFile.setPath(fileDto.getPath());
                            sysFile.setCode(fileDto.getCode());
                            sysFile.setExt(fileDto.getExt());
                            sysFile.setName(fileDto.getName());
                            sysFile.setSize(fileDto.getSize());
                            sysFile.setDelFlag(DelFlagEnums.NORMAL.getCode());
                            sysFile.setCreateTime(new Date());
                            sysFile.setCreateUser(SecurityUtils.getLoginName());
                            sysFileService.insert(sysFile);
                            if (!hotelSet.contains(fileDto.getGuid()+"."+fileDto.getExt())){
                                hotelSet.add(fileDto.getGuid()+"."+fileDto.getExt());
                                Wrapper hotelImgWrapper = new Wrapper() {
                                    @Override
                                    public String getSqlSegment() {
                                        return "where image ='"+fileDto.getGuid()+"."+fileDto.getExt()+"'";
                                    }
                                };
                                List<HotelPortalImg> hotelImgs = hotelPortalImgService.selectList(hotelImgWrapper);
                                if (!CollectionUtils.isEmpty(hotelImgs)){
                                    for (HotelPortalImg hotelImg : hotelImgs) {
                                        SysFileQuote sysFileQuote = new SysFileQuote();
                                        sysFileQuote.setType("hotel.file");
                                        sysFileQuote.setFileId(sysFile.getId());
                                        sysFileQuote.setObjId(hotelImg.getHotelPortalId());
                                        sysFileQuote.setDelFlag(DelFlagEnums.NORMAL.getCode());
                                        sysFileQuote.setCreateTime(new Date());
                                        sysFileQuote.setCreateUser(SecurityUtils.getLoginName());
                                        sysFileQuoteService.insert(sysFileQuote);
                                    }
                                }
                            }
                        }
                        //商户图片同步
                        if (fileDto.getType().equals("shop.pic")){
                            SysFile sysFile = new SysFile();
                            sysFile.setOldId(fileDto.getId());
                            sysFile.setGuid(fileDto.getGuid());
                            sysFile.setPath(fileDto.getPath());
                            sysFile.setCode(fileDto.getCode());
                            sysFile.setExt(fileDto.getExt());
                            sysFile.setName(fileDto.getName());
                            sysFile.setSize(fileDto.getSize());
                            sysFile.setDelFlag(DelFlagEnums.NORMAL.getCode());
                            sysFile.setCreateTime(new Date());
                            sysFile.setCreateUser(SecurityUtils.getLoginName());
                            sysFileService.insert(sysFile);
                            //根据老系统id查询新系统商户id
                            Wrapper<Shop> shopWrapper = new Wrapper<Shop>() {
                                @Override
                                public String getSqlSegment() {
                                    return "where old_shop_id ="+fileDto.getObjId();
                                }
                            };
                            List<Shop> shopList = shopService.selectList(shopWrapper);
                            if (!CollectionUtils.isEmpty(shopList)){
                                Shop shop = shopList.get(0);
                                SysFileQuote sysFileQuote = new SysFileQuote();
                                sysFileQuote.setType("shop.pic");
                                sysFileQuote.setFileId(sysFile.getId());
                                sysFileQuote.setObjId(shop.getId());
                                sysFileQuote.setDelFlag(DelFlagEnums.NORMAL.getCode());
                                sysFileQuote.setCreateTime(new Date());
                                sysFileQuote.setCreateUser(SecurityUtils.getLoginName());
                                sysFileQuoteService.insert(sysFileQuote);
                            }
                        }
                        //商户规格图片同步
                        if (fileDto.getType().equals("shop.menu")){
                            SysFile sysFile = new SysFile();
                            sysFile.setOldId(fileDto.getId());
                            sysFile.setGuid(fileDto.getGuid());
                            sysFile.setPath(fileDto.getPath());
                            sysFile.setCode(fileDto.getCode());
                            sysFile.setExt(fileDto.getExt());
                            sysFile.setName(fileDto.getName());
                            sysFile.setSize(fileDto.getSize());
                            sysFile.setDelFlag(DelFlagEnums.NORMAL.getCode());
                            sysFile.setCreateTime(new Date());
                            sysFile.setCreateUser(SecurityUtils.getLoginName());
                            sysFileService.insert(sysFile);
                            //查询老商户规格列表
                            CommonResultVo<List<com.colourfulchina.bigan.api.entity.ShopItem>> remoteResult = remoteShopItemService.selectByShopId(fileDto.getObjId());
                            if (remoteResult.getResult() != null){
                                if (!CollectionUtils.isEmpty(remoteResult.getResult())){
                                    for (com.colourfulchina.bigan.api.entity.ShopItem remoteItem : remoteResult.getResult()) {
                                        if (StringUtils.isNotBlank(remoteItem.getMore())){
                                            String tempStr = fileDto.getGuid()+"."+fileDto.getExt();
                                            if (remoteItem.getMore().contains(tempStr)){
                                                //根据老商户规格id查询商户规格
                                                Wrapper<ShopItem> wrapper = new Wrapper() {
                                                    @Override
                                                    public String getSqlSegment() {
                                                        return "where old_item_id ="+remoteItem.getId();
                                                    }
                                                };
                                                List<ShopItem> shopItemList = shopItemService.selectList(wrapper);
                                                if (!CollectionUtils.isEmpty(shopItemList)){
                                                    ShopItem newItem = shopItemList.get(0);
                                                    SysFileQuote sysFileQuote = new SysFileQuote();
                                                    sysFileQuote.setType("shop.item.pic");
                                                    sysFileQuote.setFileId(sysFile.getId());
                                                    sysFileQuote.setObjId(newItem.getId());
                                                    sysFileQuote.setDelFlag(DelFlagEnums.NORMAL.getCode());
                                                    sysFileQuote.setCreateTime(new Date());
                                                    sysFileQuote.setCreateUser(SecurityUtils.getLoginName());
                                                    sysFileQuoteService.insert(sysFileQuote);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            log.info("同步结束");
            resultVo.setResult(true);
        }catch (Exception e){
            log.error("图片初始化失败",e);
            resultVo.setCode(200);
            resultVo.setMsg(e.getMessage());
        }
        return resultVo;
    }
}
