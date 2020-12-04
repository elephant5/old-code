package com.colourfulchina.bigan.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.colourfulchina.bigan.api.entity.Goods;
import com.colourfulchina.bigan.api.entity.PrjGroupGoods;
import com.colourfulchina.bigan.api.vo.GoodsDetailVo;
import com.colourfulchina.bigan.api.vo.ProjectGoodsResVo;
import com.colourfulchina.bigan.api.vo.ProjectGroupResVo;

import java.util.List;
import java.util.Map;

public interface GoodsMapper extends BaseMapper<Goods> {

    Long selectGoodsSeqNextValue();

    Map saveGoodsInfo( Map params );

	/**
	 * 更具PrjGroupGoods中的goodsid查询商品详情
	 * @param prjGroupGoodsList
	 * @return
	 */
	List<GoodsDetailVo> queryGoodsDetail(List<PrjGroupGoods> prjGroupGoodsList);

	/**
	 * @Title: getGoodsList
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param @param groupList
	 * @param @return
	 * @return List<ProjectGoodsResVo>
	 * @author Sunny
	 * @date 2018年9月18日
	 * @throws
	*/
	public List<ProjectGoodsResVo> getGoodsList(ProjectGroupResVo group);

	/**
	 * 根据goodsID查询商品详情
	 * @param goodsId
	 * @return
	 */
	GoodsDetailVo getGoodsDetail(String goodsId);
}