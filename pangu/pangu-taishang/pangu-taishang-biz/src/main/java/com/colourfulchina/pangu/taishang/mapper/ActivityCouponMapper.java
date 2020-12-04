package com.colourfulchina.pangu.taishang.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.colourfulchina.inf.base.vo.PageVo;
import com.colourfulchina.pangu.taishang.api.entity.ActivityCoupon;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ActivityCouponMapper extends BaseMapper<ActivityCoupon> {

    public List<ActivityCoupon> getActivityCouponList(@Param("actIdList") List<Integer> actIdList);

    public List<ActivityCoupon> findActivityCpnPageList(PageVo<ActivityCoupon> pageVoReq, Map<String, Object> params);

}
