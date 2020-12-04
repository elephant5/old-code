package com.colourfulchina.pangu.taishang.service;

import com.baomidou.mybatisplus.service.IService;
import com.colourfulchina.inf.base.vo.PageVo;
import com.colourfulchina.pangu.taishang.api.dto.SalesChannelVo;
import com.colourfulchina.pangu.taishang.api.entity.SalesChannel;
import com.colourfulchina.pangu.taishang.api.vo.req.SalesChannelReqVo;
import com.colourfulchina.pangu.taishang.api.vo.res.GoodsChannelRes;
import com.colourfulchina.tianyan.admin.api.entity.SysDict;

import java.util.List;

/**
 *
 * @Author: luck.luo
 * @Description:
 * @Date: 2019/2/25 17:39
 */
public interface SalesChannelService extends IService<SalesChannel> {


    PageVo<SalesChannel> findPageList(PageVo<SalesChannelReqVo> pageVoReq);

    /**
     * 根据三个属性确定一条销售渠道
     * @param bankId
     * @param salesChannelId
     * @param salesWayId
     * @return
     */
    SalesChannel selectOneDate(String bankId, String salesChannelId, String salesWayId);



    Integer count(String bankId, String salesChannelId, String salesWayId);

    List<SalesChannelVo> selectAll() throws Exception;

    /**
     * 根据类型查询字典数据
     * @param type
     * @return
     */
    List<SysDict> selectSysDict(String type);

    List<String> getGoodsServiceType(String type);


    Boolean insertSalesChannel(SalesChannelReqVo t) throws Exception;

    boolean updateChannelById(SalesChannel t);

    List<Integer> selectSalesChannel(String bankId);
    /**
     * 根据id查询销售渠道详情
     * @param id
     * @return
     * @throws Exception
     */
    GoodsChannelRes findById(Integer id)throws Exception;

    /**
     * 查询销售渠道列表详情
     * @return
     * @throws Exception
     */
    List<GoodsChannelRes> selectSalesChannelList()throws Exception;

    List<SalesChannel> selectByBCW(SalesChannel salesChannel)throws Exception;
}
