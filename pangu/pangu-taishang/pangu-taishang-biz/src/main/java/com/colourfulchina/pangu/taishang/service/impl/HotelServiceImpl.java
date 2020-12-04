package com.colourfulchina.pangu.taishang.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.bigan.api.entity.SysFile;
import com.colourfulchina.bigan.api.feign.RemoteHotelService;
import com.colourfulchina.bigan.api.vo.HotelOldDetailVo;
import com.colourfulchina.bigan.api.vo.HotelOldPortalVo;
import com.colourfulchina.god.door.api.util.SecurityUtils;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.inf.base.vo.PageVo;
import com.colourfulchina.pangu.taishang.api.dto.SysFileDto;
import com.colourfulchina.pangu.taishang.api.entity.*;
import com.colourfulchina.pangu.taishang.api.enums.DelFlagEnums;
import com.colourfulchina.pangu.taishang.api.vo.HotelDetailVo;
import com.colourfulchina.pangu.taishang.api.vo.HotelPortalVo;
import com.colourfulchina.pangu.taishang.api.vo.PortalOldVo;
import com.colourfulchina.pangu.taishang.api.vo.Sections;
import com.colourfulchina.pangu.taishang.api.vo.req.HotelPageListReq;
import com.colourfulchina.pangu.taishang.api.vo.req.ListSysFileReq;
import com.colourfulchina.pangu.taishang.api.vo.res.HotelPageListRes;
import com.colourfulchina.pangu.taishang.api.vo.res.hotel.HotelInfoQueryRes;
import com.colourfulchina.pangu.taishang.mapper.HotelMapper;
import com.colourfulchina.pangu.taishang.service.*;
import com.colourfulchina.pangu.taishang.utils.PinYinUtils;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class HotelServiceImpl extends ServiceImpl<HotelMapper,Hotel> implements HotelService {
    @Autowired
    private HotelMapper hotelMapper;
    private final RemoteHotelService remoteHotelService;
    @Autowired
    private CityService cityService;
    @Autowired
    private SysFileService sysFileService;
    @Autowired
    private HotelPortalService hotelPortalService;
    @Autowired
    private HotelPortalImgService hotelPortalImgService;
    /**
     * 酒店列表查询
     * @param pageVoReq
     * @return
     */
    @Override
    public PageVo<HotelPageListRes> findPageList(PageVo<HotelPageListReq> pageVoReq) {
        PageVo<HotelPageListRes> pageVoRes = new PageVo<>();
        List<HotelPageListRes> resList = hotelMapper.findPageList(pageVoReq,pageVoReq.getCondition());
        BeanUtils.copyProperties(pageVoReq,pageVoRes);
        pageVoRes.setRecords(resList);
        return pageVoRes;
    }

    /**
     * 根据酒店中文名查询酒店信息
     * @param hotelName
     * @return
     */
    @Override
    public Hotel selectByHotelName(String hotelName) {
        return hotelMapper.selectByHotelName(hotelName);
    }

    /**
     * 新增酒店
     * @param hotel
     * @return
     */
    @Override
    public Hotel addHotel(Hotel hotel) {
        Hotel hotelFlag = checkHotelIsExist(hotel.getNameCh());
        if (hotelFlag == null){
            if (StringUtils.isNotEmpty(hotel.getNameCh())){
                hotel.setNamePy(PinYinUtils.getPinYinHeadChar(hotel.getNameCh()));
            }
            City city = cityService.selectById(hotel.getCityId());
            if (city.getCountryId() != null && city.getCountryId().equals("CN")){
                hotel.setOversea(0);
            }else {
                hotel.setOversea(1);
            }
            hotel.setDelFlag(DelFlagEnums.NORMAL.getCode());
            hotel.setCreateTime(new Date());

            hotel.setCreateUser(SecurityUtils.getLoginName());
            //同步新增sqlserver酒店
            com.colourfulchina.bigan.api.entity.Hotel oldHotel = addOldHotel(hotel);
            hotel.setOldHotelId(Integer.valueOf(oldHotel.getId()+""));
            hotelMapper.insert(hotel);
            return hotel;
        }
        return null;
    }

    /**
     * 酒店是否存在检查
     * @param hotelChName
     * @return
     */
    @Override
    public Hotel checkHotelIsExist(String hotelChName) {
        Hotel hotel = hotelMapper.checkHotelIsExist(hotelChName);
        return hotel;
    }

    /**
     * 旧系统酒店修改同步更新
     * @param hotelDetailVo
     * @return
     */
    @Override
    public CommonResultVo<com.colourfulchina.bigan.api.entity.Hotel> syncOldHotel(HotelDetailVo hotelDetailVo) {
        //json字符串老系统酒店portal字段
        String portal = null;
        //调用酒店老系统更新接口的入参
        HotelOldDetailVo hotelOldDetailVo = new HotelOldDetailVo();
        //老系统更新接口入参中的酒店信息
        com.colourfulchina.bigan.api.entity.Hotel oldHotel = new com.colourfulchina.bigan.api.entity.Hotel();
        //老系统更新接口入参中的酒店章节列表
        List<HotelOldPortalVo> hotelOldPortalVoList = Lists.newLinkedList();
        if (!CollectionUtils.isEmpty(hotelDetailVo.getHotelPortalVoList())){
            //组装老系统中酒店portal的实体对象
            Sections sections = new Sections();
            //sections中的酒店章节列表
            List<PortalOldVo> portalOldVoList = Lists.newLinkedList();
            for (HotelPortalVo hotelPortalVo : hotelDetailVo.getHotelPortalVoList()) {
                //sections中的酒店章节
                PortalOldVo portalOldVo = new PortalOldVo();
                portalOldVo.setTitle(hotelPortalVo.getHotelPortal().getTitle());
                portalOldVo.setContent(hotelPortalVo.getHotelPortal().getContent());
                //老系统更新接口入参中的酒店章节
                HotelOldPortalVo hotelOldPortalVo = new HotelOldPortalVo();
                hotelOldPortalVo.setTitle(hotelPortalVo.getHotelPortal().getTitle());
                hotelOldPortalVo.setContent(hotelPortalVo.getHotelPortal().getContent());
                //老系统更新接口入参中酒店章节中的图片列表
                List<SysFile> fileList = Lists.newLinkedList();
                //sections中酒店章节中的图片列表
                List<String> pics = Lists.newLinkedList();
                if (!CollectionUtils.isEmpty(hotelPortalVo.getSysFileDtoList())){
                    for (SysFileDto sysFileDto : hotelPortalVo.getSysFileDtoList()) {
                        String img = sysFileDto.getGuid();
                        String ext = sysFileDto.getExt();
                        Wrapper wrapper = new Wrapper() {
                            @Override
                            public String getSqlSegment() {
                                return "where guid ='"+img + "' and ext ='"+ext +"'";
                            }
                        };
                        com.colourfulchina.pangu.taishang.api.entity.SysFile file = sysFileService.selectOne(wrapper);
                        SysFile sysFile = new SysFile();
                        //图片信息暂时不处理
//                        sysFile.setName(file.getName());
//                        sysFile.setSize(file.getSize()+"");
                        fileList.add(sysFile);
                        pics.add(sysFileDto.getGuid()+"."+sysFileDto.getExt());
                    }
                }
                portalOldVo.setPics(pics);
                portalOldVoList.add(portalOldVo);
                hotelOldPortalVo.setFileList(fileList);
                hotelOldPortalVoList.add(hotelOldPortalVo);
            }
            //sections转json字符串
            sections.setSections(portalOldVoList);
            portal = JSONObject.toJSONString(sections);
        }
        oldHotel.setId(Long.valueOf(hotelDetailVo.getHotel().getOldHotelId()));
        oldHotel.setName(hotelDetailVo.getHotel().getNameCh());
        oldHotel.setPy(PinYinUtils.getPinYinHeadChar(hotelDetailVo.getHotel().getNameCh()));
        oldHotel.setNameEn(hotelDetailVo.getHotel().getNameEn());
        oldHotel.setCity(cityService.selectById(hotelDetailVo.getHotel().getCityId()).getNameCh());
        oldHotel.setCityId(cityService.selectById(hotelDetailVo.getHotel().getCityId()).getOldCityId());
        oldHotel.setAddress(hotelDetailVo.getHotel().getAddressCh());
        oldHotel.setAddressEn(hotelDetailVo.getHotel().getAddressEn());
        oldHotel.setPortal(portal);
        hotelOldDetailVo.setHotel(oldHotel);
        hotelOldDetailVo.setHotelOldPortalVoList(hotelOldPortalVoList);
        return remoteHotelService.updHotel(hotelOldDetailVo);
    }

    /**
     * 根据老系统酒店id查询酒店信息
     * @param oldId
     * @return
     */
    @Override
    public Hotel selectByOldId(Integer oldId) {
        Wrapper<Hotel> wrapper = new Wrapper<Hotel>() {
            @Override
            public String getSqlSegment() {
                return "where old_hotel_id = "+oldId;
            }
        };
        return hotelMapper.selectList(wrapper).get(0);
    }

    /**
     * 酒店详情查询
     * @param hotelId
     * @return
     */
    @Override
    public HotelDetailVo queryHotelDetail(Integer hotelId) {
        HotelDetailVo hotelDetailVo = new HotelDetailVo();
        Hotel hotel = hotelMapper.selectById(hotelId);
        if (hotel != null){

            hotelDetailVo.setHotel(hotel);
            List<HotelPortal> hotelPortalList = hotelPortalService.selectListByHotelId(hotelId);
            List<HotelPortalVo> hotelPortalVoList = Lists.newLinkedList();
            for (HotelPortal hotelPortal : hotelPortalList) {
                HotelPortalVo hotelPortalVo = new HotelPortalVo();
                ListSysFileReq sysFileReq = new ListSysFileReq();
                sysFileReq.setType("hotel.file");
                sysFileReq.setObjId(hotelPortal.getId());
                List<SysFileDto> sysFileDtoList = sysFileService.listSysFileDto(sysFileReq);
                hotelPortalVo.setHotelPortal(hotelPortal);
                hotelPortalVo.setSysFileDtoList(sysFileDtoList);
                hotelPortalVoList.add(hotelPortalVo);
            }
            hotelDetailVo.setHotelPortalVoList(hotelPortalVoList);
        }else {
            hotelDetailVo = null;
        }
        return hotelDetailVo;
    }

    /**
     * 检车是否存在相同的中文名酒店
     * @param hotel
     * @return
     */
    @Override
    public Hotel checkHotelByNameCh(Hotel hotel) {
        return hotelMapper.checkHotelByNameCh(hotel);
    }

    /**
     * 检车是否存在相同的英文名酒店
     * @param hotel
     * @return
     */
    @Override
    public Hotel checkHotelByNameEn(Hotel hotel) {
        return hotelMapper.checkHotelByNameEn(hotel);
    }

    /**
     * 酒店的模糊查询
     *
     * @param hotelName
     * @return
     */
    @Override
    public List<Hotel> selectByHotelNameList(String hotelName) {
        Wrapper<Hotel> hotelWrapper = new Wrapper<Hotel>() {
            @Override
            public String getSqlSegment() {
                if(StringUtils.isNotBlank(hotelName)){
                    return "where name_ch like '%"+hotelName+"%' or name_py like '%"+hotelName+"%' or name_en like '%"+hotelName+"%'";
                }else{
                    return "where 1=1";
                }

            }
        };
        return hotelMapper.selectList(hotelWrapper);
    }

    @Override
    public HotelInfoQueryRes selectHotelByShopId(Integer shopId) {
        HotelInfoQueryRes hotelInfoQueryRes = hotelMapper.selectHotelByShopId(shopId);
        return hotelInfoQueryRes;
    }

    /**
     * 同步新增sqlserver酒店
     * @param hotel
     * @return
     */
    public com.colourfulchina.bigan.api.entity.Hotel addOldHotel(Hotel hotel){
        City city = cityService.selectById(hotel.getCityId());
        com.colourfulchina.bigan.api.entity.Hotel oldHotel = new com.colourfulchina.bigan.api.entity.Hotel();
        oldHotel.setName(hotel.getNameCh());
        oldHotel.setPy(hotel.getNamePy());
        oldHotel.setCityId(city.getOldCityId());
        oldHotel.setCity(city.getNameCh());
        oldHotel.setOversea(hotel.getOversea());
        oldHotel.setAddress(hotel.getAddressCh());
        oldHotel.setPhone(hotel.getPhone());
        oldHotel.setNotes(hotel.getNotes());
        oldHotel = remoteHotelService.addHotel(oldHotel).getResult();
        return oldHotel;
    }
}