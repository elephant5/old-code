package com.colourfulchina.pangu.taishang.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.colourfulchina.inf.base.vo.PageVo;
import com.colourfulchina.pangu.taishang.api.entity.Activity;
import com.colourfulchina.pangu.taishang.api.vo.ActivityBackReqVo;
import com.colourfulchina.pangu.taishang.api.vo.ActivityPageVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ActivityMapper extends BaseMapper<Activity> {

    public List<Activity> getActivityActList(@Param("grantMode") Integer grantMode, @Param("actIdList") List<Integer> actIdList, @Param("channel") String channel);

    public List<ActivityPageVo> findActivityPageList(PageVo<ActivityBackReqVo> pageVoReq, Map<String, Object> params);
}
