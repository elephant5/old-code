package com.colourfulchina.pangu.taishang.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.colourfulchina.inf.base.vo.PageVo;
import com.colourfulchina.pangu.taishang.api.entity.Goods;
import com.colourfulchina.pangu.taishang.api.vo.req.GoodsBaseVo;
import com.colourfulchina.pangu.taishang.api.vo.res.bigan.Project;
import com.colourfulchina.pangu.taishang.api.vo.res.bigan.ProjectCdnVo;

import java.util.List;
import java.util.Map;

public interface GoodsMapper extends BaseMapper<Goods> {

    List<GoodsBaseVo> findPageList(PageVo<GoodsBaseVo> pageVoReq, Map<String, Object> condition);

    List<Goods> selectNameByIds(List<Integer> ids);

    Integer updateIdByGoodsId(GoodsBaseVo goodsBaseVo);

    /**
     * 查询项目列表简略信息
     * @param strings
     * @return
     */
    List<ProjectCdnVo> selectProjectBriefList(String[] strings);

    Project selectProjectById(Integer id);

    void insertSyncGoodsCode(Map<String,Integer> map);
}