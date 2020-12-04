package com.colourfulchina.pangu.taishang.api.feign.fallback;

import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.pangu.taishang.api.entity.Goods;
import com.colourfulchina.pangu.taishang.api.entity.GoodsClause;
import com.colourfulchina.pangu.taishang.api.entity.GoodsSetting;
import com.colourfulchina.pangu.taishang.api.entity.SalesChannel;
import com.colourfulchina.pangu.taishang.api.feign.RemoteGoodsService;
import com.colourfulchina.pangu.taishang.api.vo.req.GoodsBaseVo;
import com.colourfulchina.pangu.taishang.api.vo.req.GoodsExpiryDateReq;
import com.colourfulchina.pangu.taishang.api.vo.res.GoodsChannelRes;
import com.colourfulchina.pangu.taishang.api.vo.res.GoodsClauseRes;
import com.colourfulchina.pangu.taishang.api.vo.res.bigan.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Slf4j
@Component
public class RemoteGoodsServiceFallbackImpl implements RemoteGoodsService {
    @Override
    public CommonResultVo<GoodsBaseVo> selectById(Integer id) {
        log.error("fegin查询商品详情失败");
        return null;
    }

    @Override
    public CommonResultVo<List<Goods>> selectGoodsList() {
        log.error("fegin查询商品列表失败");
        return null;
    }

    /**
     * 根据Ids查找商品
     *
     * @param ids
     * @return
     */
    @PostMapping("/goods/selectGoodsByIds")
    @Override
    public CommonResultVo<List<GoodsBaseVo>> selectGoodsByIds(List<Integer> ids) {
        log.error("fegin查询商品信息失败");
        return null;
    }

    @Override
    public CommonResultVo<Goods> findGoodsById(Integer id) {
        log.error("fegin查询商品信息失败");
        return null;
    }

    @Override
    public CommonResultVo<List<Goods>> selectNameByIds(List<Integer> ids) {
        log.error("fegin查询商品名称失败");
        return null;
    }

    @Override
    public CommonResultVo<GoodsBaseVo> queryOrderExpireTime(GoodsExpiryDateReq queryOrderExpireTimeReq) {
        log.error("fegin查询权益到期时间失败");
        return null;
    }

    @Override
    public CommonResultVo<Goods> findByOldKey(String oldKey) {
        log.error("fegin查询失败");
        return null;
    }

    @Override
    public CommonResultVo<List<GoodsClauseRes>> selectClauseList(GoodsClause goodsClause) {
        log.error("fegin查询商品适用规则失败");
        return null;
    }

    @Override
    public CommonResultVo<List<GoodsChannelRes>> selectGoodsChannel(Integer goodsId) {
        log.error("fegin查询商品的销售渠道失败");
        return null;
    }

    @Override
    public CommonResultVo<List<SalesChannel>> selectByBCW(SalesChannel salesChannel) {
        log.error("fegin根据大客户、销售渠道销售方式查询salesChannel失败");
        return null;
    }

    @Override
    public CommonResultVo<List<SalesChannel>> selectByBankIds(SalesChannel salesChannel) {
        log.error("fegin根据大客户查询salesChannel失败");
        return null;
    }


    @Override
    public CommonResultVo<List<Integer>> selectSalesChannel(String bankId) {
        return null;
    }

    @Override
    public CommonResultVo<GoodsChannelRes> findById(Integer id) {
        log.error("fegin根据id查询销售渠道详情失败");
        return null;
    }

    @Override
    public CommonResultVo<GoodsSetting> selectGoodsSettingById(Integer goodsId) {
        log.error("fegin根据商品id查询商品预约限制失败");
        return null;
    }

    @Override
    public CommonResultVo<GoodsBaseVo> selectGoodsClauseById(Integer goodsId) {
        return null;
    }

    @Override
    public CommonResultVo<List<GoodsChannelRes>> selectSalesChannelList() {
        log.error("fegin查询所有销售列表详情失败");
        return null;
    }

    @Override
    public CommonResultVo<GoodsBaseVo> getGoodsBaseById(Integer id) {
        log.error("fegin查询商品信息失败");
        return null;
    }

    @Override
    public CommonResultVo<List<ProjectCdnVo>> prjBriefList(String projectIds) {
        return null;
    }

    @Override
    public CommonResultVo<List<ProjectCdnVo>> prjList(String projectIds) {
        return null;
    }

    @Override
    public CommonResultVo<GoodsDetailVo> getGoodsDetail(Long pgpId) {
        return null;
    }

    @Override
    public CommonResultVo<Project> getProjectById(Integer id) {
        return null;
    }

    @Override
    public CommonResultVo<List<GoodsBlockVo>> queryGiftBlockList(BookOrderReqVo bookOrderReqVo) {
        return null;
    }

    @Override
    public CommonResultVo<AlipayBookPriceVo> prepareAlipayBookPrice(Integer productGroupProductId) {
        log.error("fegin支付宝产品预约时间范围及价格组装失败");
        return null;
    }

    @Override
    public CommonResultVo<String> getBlockRule(Integer pgpId) {
        log.error("获取block规则异常");
        return null;
    }
}
