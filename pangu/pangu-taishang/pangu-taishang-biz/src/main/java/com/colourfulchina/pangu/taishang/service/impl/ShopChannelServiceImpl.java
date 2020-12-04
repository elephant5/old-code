package com.colourfulchina.pangu.taishang.service.impl;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.inf.base.vo.PageVo;
import com.colourfulchina.pangu.taishang.api.entity.ShopChannel;
import com.colourfulchina.pangu.taishang.api.vo.req.ShopChannelPageListReq;
import com.colourfulchina.pangu.taishang.mapper.ShopChannelMapper;
import com.colourfulchina.pangu.taishang.service.ShopChannelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ShopChannelServiceImpl extends ServiceImpl<ShopChannelMapper,ShopChannel> implements ShopChannelService {
    @Autowired
    private ShopChannelMapper shopChannelMapper;

    /**
     * 查询酒店资源列表
     * @return
     */
    @Override
    public PageVo<ShopChannel> selectShopChannelPageList(PageVo<ShopChannelPageListReq> pageReq) {
        PageVo<ShopChannel> pageRes = new PageVo<>();
        BeanUtils.copyProperties(pageReq,pageRes);
        return pageRes.setRecords(shopChannelMapper.selectShopChannelPageList(pageReq,pageReq.getCondition()));
    }

    /**
     * 根据老系统商户渠道id查询新系统商户渠道信息
     * @param oldChannelId
     * @return
     */
    @Override
    public ShopChannel selectByOldChannel(Integer oldChannelId) {
        Wrapper<ShopChannel> wrapper = new Wrapper<ShopChannel>() {
            @Override
            public String getSqlSegment() {
                return "where old_id = "+oldChannelId;
            }
        };
        return shopChannelMapper.selectList(wrapper).get(0);
    }

    /**
     * 檢查商戶資源渠道是否已經存在（根據渠道名稱）
     * @param shopChannel
     * @return
     */
    @Override
    public List<ShopChannel> checkChannelIsExist(ShopChannel shopChannel) {
        return shopChannelMapper.checkChannelIsExist(shopChannel);
    }
}