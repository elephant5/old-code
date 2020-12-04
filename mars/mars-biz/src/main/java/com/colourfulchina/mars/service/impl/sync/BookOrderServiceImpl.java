package com.colourfulchina.mars.service.impl.sync;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.mars.api.entity.sync.BookOrderPo;
import com.colourfulchina.mars.api.entity.sync.BookOrderPricePo;
import com.colourfulchina.mars.api.entity.sync.BookOrderTagsPo;
import com.colourfulchina.mars.api.entity.sync.GiftAtvcodePo;
import com.colourfulchina.mars.mapper.sync.BookOrderMapper;
import com.colourfulchina.mars.service.sync.BookOrderPriceService;
import com.colourfulchina.mars.service.sync.BookOrderService;
import com.colourfulchina.mars.service.sync.BookOrderTagsService;
import com.colourfulchina.mars.service.sync.GiftAtvcodeService;
import com.colourfulchina.mars.utils.BeanCopierUtils;
import com.colourfulchina.yangjian.api.entity.BookOrderPrice;
import com.colourfulchina.yangjian.api.entity.GiftActivateCode;
import com.colourfulchina.yangjian.api.entity.PgSyncInfo;
import com.colourfulchina.yangjian.api.vo.BookOrderResultVo;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 
 * @since 2019-12-11
 */
@Slf4j
@Service
public class BookOrderServiceImpl extends ServiceImpl<BookOrderMapper, BookOrderPo> implements BookOrderService {

    @Autowired
    private BookOrderTagsService bookOrderTagsService;

    @Autowired
    private GiftAtvcodeService giftAtvcodeService;

    @Autowired
    private BookOrderPriceService bookOrderPriceService;

    /**
     * 功能描述: 获取从查理系统同步来的预约单数据<br>
     * @param bookOrder
     * @return:
     * @since: 1.0.0
     * @Author:zhaojun2
     * @Date: 2019/12/11 13:31
     */
    public CommonResultVo<List<BookOrderResultVo>> selectBookOrderByProject(PgSyncInfo bookOrder) {
        CommonResultVo<List<BookOrderResultVo>> resultVo = new CommonResultVo<>();

        Wrapper<BookOrderPo> wrapper = new EntityWrapper();
        wrapper.eq("project_id", bookOrder.getObjId());
        wrapper.eq(bookOrder.getSecId() != null, "id", bookOrder.getSecId());
        List<BookOrderPo> bookOrderPoList = this.selectList(wrapper);
        List<BookOrderResultVo> BookOrderResultVos = Lists.newArrayList();
        if (CollectionUtils.isEmpty(bookOrderPoList)){
            log.info("bookOrderService.selectBookOrderByProject size = 0");
        }else {
            log.info("bookOrderService.selectBookOrderByProject size = {}", bookOrderPoList.size());
            BookOrderResultVo vo = null;
            Wrapper<GiftAtvcodePo> giftAtvcodePoWrapper = null;
            Wrapper<BookOrderPricePo> bookOrderPricePoWrapper = null;
            for (BookOrderPo order: bookOrderPoList){
                vo = new BookOrderResultVo();
                BeanUtils.copyProperties(order, vo);
                BookOrderTagsPo bookOrderTagsPo = bookOrderTagsService.selectById(order.getId());
                vo.setTags(bookOrderTagsPo.getTags());

                giftAtvcodePoWrapper = new EntityWrapper();
                giftAtvcodePoWrapper.eq("order_id",order.getId());
                GiftAtvcodePo code = giftAtvcodeService.selectOne(giftAtvcodePoWrapper);
                if (code != null) {
                    vo.setCode(BeanCopierUtils.copyProperties(code, GiftActivateCode.class));
                }
                bookOrderPricePoWrapper = new EntityWrapper<>();
                bookOrderPricePoWrapper.eq("order_id",order.getId());
                List<BookOrderPricePo> prices = bookOrderPriceService.selectList(bookOrderPricePoWrapper);
                if (!prices.isEmpty()) {
                    vo.setPrices(BeanCopierUtils.copyPropertiesOfList(prices, BookOrderPrice.class));
                }
                BookOrderResultVos.add(vo);
            }
        }
        resultVo.setResult(BookOrderResultVos);

        return resultVo;
    }
}
