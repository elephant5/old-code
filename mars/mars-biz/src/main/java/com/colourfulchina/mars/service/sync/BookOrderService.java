package com.colourfulchina.mars.service.sync;

import com.baomidou.mybatisplus.service.IService;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.mars.api.entity.sync.BookOrderPo;
import com.colourfulchina.yangjian.api.entity.PgSyncInfo;
import com.colourfulchina.yangjian.api.vo.BookOrderResultVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 
 * @since 2019-12-11
 */
public interface BookOrderService extends IService<BookOrderPo> {
    CommonResultVo<List<BookOrderResultVo>> selectBookOrderByProject(PgSyncInfo bookOrder);
}
