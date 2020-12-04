package com.colourfulchina.pangu.taishang.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.colourfulchina.pangu.taishang.api.entity.ActivityRelation;
import com.colourfulchina.pangu.taishang.api.vo.ActivityReqVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ActivityRelationMapper extends BaseMapper<ActivityRelation> {

    public List<Integer> getActivityList(ActivityReqVo reqVo);
}
