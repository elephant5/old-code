package com.colourfulchina.pangu.taishang.controller;

import com.colourfulchina.god.door.api.annotation.SysGodDoorLog;
import com.colourfulchina.inf.base.vo.CommonResultVo;
import com.colourfulchina.inf.base.vo.PageVo;
import com.colourfulchina.pangu.taishang.api.entity.Activity;
import com.colourfulchina.pangu.taishang.api.entity.ActivityCoupon;
import com.colourfulchina.pangu.taishang.api.vo.ActivityBackReqVo;
import com.colourfulchina.pangu.taishang.api.vo.ActivityPageVo;
import com.colourfulchina.pangu.taishang.api.vo.ActivityReqVo;
import com.colourfulchina.pangu.taishang.api.vo.res.ActivityResVo;
import com.colourfulchina.pangu.taishang.service.ActivityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/activity")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @SysGodDoorLog("活动列表")
    @PostMapping("/activityPage")
    public CommonResultVo<PageVo<ActivityPageVo>> activityPage(@RequestBody PageVo<ActivityBackReqVo> pageVoReq) {
        CommonResultVo<PageVo<ActivityPageVo>> common = new CommonResultVo<PageVo<ActivityPageVo>>();
        try {
            PageVo<ActivityPageVo> pageVoRes = activityService.getActivityPage(pageVoReq);
            common.setCode(100);
            common.setMsg("成功");
            common.setResult(pageVoRes);
        } catch (Exception e) {
            common.setCode(200);
            common.setMsg(e.getMessage());
        }
        return common;
    }

    @SysGodDoorLog("新建/编辑活动")
    @PostMapping("/saveActivity")
    public CommonResultVo<Activity> saveActivity(@RequestBody ActivityBackReqVo reqVo) throws Exception {
        CommonResultVo<Activity> common = new CommonResultVo<Activity>();
        try {
            Assert.notNull(reqVo.getActName(), "活动名称不能为空");
            Assert.notNull(reqVo.getSalesChannel(), "渠道不能为空");
            Assert.notNull(reqVo.getGrantMode(), "发放方式不能为空");
            // 如果不是针对全部商品 商品id不能为空
            if (reqVo.getGoodsTag() == 0) {
                Assert.notNull(reqVo.getGoodIds(), "针对商品不能为空");
            }
            // 如果不是永久有效
            if (reqVo.getForeverTag() == 0) {
                Assert.notNull(reqVo.getStartDate(), "活动开始时间不能为空");
                Assert.notNull(reqVo.getEndDate(), "活动结束时间不能为空");
            }
            Activity result = activityService.saveActivity(reqVo);
            common.setCode(100);
            common.setMsg("成功");
            common.setResult(result);
        } catch (Exception e) {
            common.setCode(200);
            common.setMsg(e.getMessage());
        }
        return common;
    }

    @SysGodDoorLog("查询活动配置明细")
    @PostMapping("/getActivityDetail")
    public CommonResultVo<Activity> getActivityDetail (@RequestBody Integer id) throws Exception {
        CommonResultVo<Activity> common = new CommonResultVo<Activity>();
        try {
            Assert.notNull(id, "活动id不能为空");
            Activity result = activityService.getActivityDetail(id);
            common.setCode(100);
            common.setMsg("成功");
            common.setResult(result);
        } catch (Exception e) {
            common.setCode(200);
            common.setMsg(e.getMessage());
        }
        return common;
    }

    @SysGodDoorLog("活动关联优惠券列表")
    @PostMapping("/activityCpnPage")
    public CommonResultVo<PageVo<ActivityCoupon>> activityCpnPage(@RequestBody PageVo<ActivityCoupon> pageVoReq) {
        CommonResultVo<PageVo<ActivityCoupon>> common = new CommonResultVo<PageVo<ActivityCoupon>>();
        try {
            PageVo<ActivityCoupon> pageVoRes = activityService.getActivityCpnPage(pageVoReq);
            common.setCode(100);
            common.setMsg("成功");
            common.setResult(pageVoRes);
        } catch (Exception e) {
            common.setCode(200);
            common.setMsg(e.getMessage());
        }
        return common;
    }

    @SysGodDoorLog("活动关联优惠券批次")
    @PostMapping("/relationCoupon")
    public CommonResultVo<ActivityCoupon> relationCoupon(@RequestBody ActivityCoupon reqVo) throws Exception {
        CommonResultVo<ActivityCoupon> common = new CommonResultVo<ActivityCoupon>();
        try {
            Assert.notNull(reqVo.getActId(), "活动ID不能为空");
            Assert.notNull(reqVo.getBatchId(), "批次ID不能为空");
            Assert.notNull(reqVo.getCouponType(), "优惠券类型不能为空");
            Assert.notNull(reqVo.getGrantLimit(), "发放总数不能为空");
            Assert.notNull(reqVo.getUseLimit(), "使用限制不能为空");
            Assert.notNull(reqVo.getUseLimitRateNum(), "使用限制RATE_NUM不能为空");
            Assert.notNull(reqVo.getUseLimitRate(), "使用限制RATE不能为空");
            ActivityCoupon result = activityService.relationCoupon(reqVo);
            common.setCode(100);
            common.setMsg("成功");
            common.setResult(result);
        } catch (Exception e) {
            common.setCode(200);
            common.setMsg(e.getMessage());
        }
        return common;
    }

    @SysGodDoorLog("删除优惠券批次")
    @PostMapping("/deleteCoupon")
    public CommonResultVo<Integer> deleteCoupon(@RequestBody Integer id) throws Exception {
        CommonResultVo<Integer> common = new CommonResultVo<Integer>();
        try {
            Assert.notNull(id, "ID不能为空");
            Integer result = activityService.deleteCoupon(id);
            common.setCode(100);
            common.setMsg("成功");
            common.setResult(result);
        } catch (Exception e) {
            common.setCode(200);
            common.setMsg(e.getMessage());
        }
        return common;
    }

    /**
     * 获取活动配置
     *
     * @return
     * @throws Exception
     */
    @SysGodDoorLog("获取活动配置")
    @PostMapping("/getActCouponConfig")
    public CommonResultVo<List<ActivityResVo>> getActCouponConfig(@RequestBody ActivityReqVo reqVo) throws Exception {
        CommonResultVo<List<ActivityResVo>> common = new CommonResultVo<List<ActivityResVo>>();
        try {
            Assert.notNull(reqVo.getGrantMode(), "发放方式不能为空");
            Assert.notNull(reqVo.getId(), "ID不能为空");
            Assert.notNull(reqVo.getIdType(), "ID类型不能为空");
            List<ActivityResVo> coupon = activityService.getActivityCoupon(reqVo);
            common.setCode(100);
            common.setMsg("成功");
            common.setResult(coupon);
        } catch (Exception e) {
            common.setCode(200);
            common.setMsg(e.getMessage());
        }
        return common;
    }

}
