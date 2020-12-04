package com.colourfulchina.pangu.taishang.service;

import com.colourfulchina.inf.base.vo.PageVo;
import com.colourfulchina.pangu.taishang.api.entity.Activity;
import com.colourfulchina.pangu.taishang.api.entity.ActivityCoupon;
import com.colourfulchina.pangu.taishang.api.vo.ActivityBackReqVo;
import com.colourfulchina.pangu.taishang.api.vo.ActivityPageVo;
import com.colourfulchina.pangu.taishang.api.vo.ActivityReqVo;
import com.colourfulchina.pangu.taishang.api.vo.res.ActivityResVo;

import java.util.List;

public interface ActivityService {

    public PageVo<ActivityPageVo> getActivityPage(PageVo<ActivityBackReqVo> pageVoReq) throws Exception;

    public Activity saveActivity(ActivityBackReqVo reqVo) throws Exception;

    public Activity getActivityDetail(Integer id) throws Exception;

    public PageVo<ActivityCoupon> getActivityCpnPage(PageVo<ActivityCoupon> pageVoReq) throws Exception;

    public ActivityCoupon relationCoupon(ActivityCoupon reqVo) throws Exception;

    public Integer deleteCoupon(Integer id) throws Exception;

    public List<ActivityResVo> getActivityCoupon(ActivityReqVo reqVo) throws Exception;

}
