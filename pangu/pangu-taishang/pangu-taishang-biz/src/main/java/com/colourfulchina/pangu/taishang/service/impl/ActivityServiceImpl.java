package com.colourfulchina.pangu.taishang.service.impl;

import com.colourfulchina.inf.base.vo.PageVo;
import com.colourfulchina.pangu.taishang.api.entity.Activity;
import com.colourfulchina.pangu.taishang.api.entity.ActivityCoupon;
import com.colourfulchina.pangu.taishang.api.entity.ActivityRelation;
import com.colourfulchina.pangu.taishang.api.vo.ActivityBackReqVo;
import com.colourfulchina.pangu.taishang.api.vo.ActivityPageVo;
import com.colourfulchina.pangu.taishang.api.vo.ActivityReqVo;
import com.colourfulchina.pangu.taishang.api.vo.GroupProductVo;
import com.colourfulchina.pangu.taishang.api.vo.res.ActivityCouponVo;
import com.colourfulchina.pangu.taishang.api.vo.res.ActivityResVo;
import com.colourfulchina.pangu.taishang.api.vo.res.GoodsGroupListRes;
import com.colourfulchina.pangu.taishang.api.vo.res.GroupDetailRes;
import com.colourfulchina.pangu.taishang.mapper.ActivityCouponMapper;
import com.colourfulchina.pangu.taishang.mapper.ActivityMapper;
import com.colourfulchina.pangu.taishang.mapper.ActivityRelationMapper;
import com.colourfulchina.pangu.taishang.service.ActivityService;
import com.colourfulchina.pangu.taishang.service.ProductGroupService;
import com.google.common.collect.Maps;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private ActivityRelationMapper activityRelationMapper;

    @Autowired
    private ActivityCouponMapper activityCouponMapper;

    @Autowired
    private ProductGroupService productGroupService;

    @Override
    public PageVo<ActivityPageVo> getActivityPage(PageVo<ActivityBackReqVo> pageVoReq) throws Exception {
        PageVo<ActivityPageVo> pageVoRes = new PageVo<ActivityPageVo>();
        Map<String, Object> params = pageVoReq.getCondition() == null ? Maps.newHashMap() : pageVoReq.getCondition();
        List<ActivityPageVo> listRes = activityMapper.findActivityPageList(pageVoReq, params);
        BeanUtils.copyProperties(pageVoReq, pageVoRes);
        return pageVoRes.setRecords(listRes);
    }

    /**
     * 新增/编辑保存活动
     *
     * @param reqVo
     * @return
     * @throws Exception
     */
    @Override
    public Activity saveActivity(ActivityBackReqVo reqVo) throws Exception {
        //新增
        Activity activity = new Activity();
        if (reqVo.getId() == null) {
            BeanUtils.copyProperties(reqVo, activity);
            // 新增活动
//            activity.setCreateUser();
            activity.setCreateTime(new Date());
            activityMapper.insert(activity);
            // 新增商品关联
            if (reqVo.getGoodsTag() == 0 && !reqVo.getGoodIds().isEmpty()) {
                for (Integer goodId : reqVo.getGoodIds()) {
                    ActivityRelation activityRelation = new ActivityRelation();
                    activityRelation.setActId(activity.getId());
                    activityRelation.setRelateId(goodId);
                    activityRelation.setRelateIdType(1);
//                    activityRelation.setCreateUser();
                    activityRelation.setCreateTime(new Date());
                    activityRelationMapper.insert(activityRelation);
                }
            }
        } else {
            //编辑
            activity = activityMapper.selectById(reqVo.getId());
            BeanUtils.copyProperties(reqVo, activity);
//            activity.setUpdateUser();
            activityMapper.updateById(activity);
        }
        return activity;
    }

    @Override
    public Activity getActivityDetail(Integer id) throws Exception {
        return activityMapper.selectById(id);
    }

    @Override
    public PageVo<ActivityCoupon> getActivityCpnPage(PageVo<ActivityCoupon> pageVoReq) throws Exception {
        PageVo<ActivityCoupon> pageVoRes = new PageVo<ActivityCoupon>();
        Map<String, Object> params = pageVoReq.getCondition() == null ? Maps.newHashMap() : pageVoReq.getCondition();
        List<ActivityCoupon> listRes = activityCouponMapper.findActivityCpnPageList(pageVoReq, params);
        BeanUtils.copyProperties(pageVoReq, pageVoRes);
        return pageVoRes.setRecords(listRes);
    }

    @Override
    public ActivityCoupon relationCoupon(ActivityCoupon reqVo) throws Exception {
//        reqVo.setCreateUser();
        reqVo.setCreateTime(new Date());
        activityCouponMapper.insert(reqVo);
        return reqVo;
    }

    @Override
    public Integer deleteCoupon(Integer id) throws Exception {
        return activityCouponMapper.deleteById(id);
    }

    /**
     * 活动活动礼券配置
     *
     * @param reqVo
     * @return
     * @throws Exception
     */
    @Override
    public List<ActivityResVo> getActivityCoupon(ActivityReqVo reqVo) throws Exception {
        //定义返回值
        List<ActivityResVo> resultList = new ArrayList<ActivityResVo>();
        //构造入参
        List<Integer> actIdList = new ArrayList<Integer>();
        actIdList.addAll(activityRelationMapper.getActivityList(reqVo));
        //1.根据 ID 和 idType 查询 参加的活动
        switch (reqVo.getIdType()) {
            // 类型为商品ID 需要查询商品包含的产品组信息
            case 1:
                // 查询产品组信息
                List<GoodsGroupListRes> groupListRes = productGroupService.selectGoodsGroup(reqVo.getId());
                if (!groupListRes.isEmpty()) {
                    // 获取产品祖ID
                    for (GoodsGroupListRes group : groupListRes) {
                        // 根据产品组ID查询
                        reqVo.setId(group.getId());
                        reqVo.setIdType(2);
                        actIdList.addAll(activityRelationMapper.getActivityList(reqVo));
                        // 根据产品查询
                        for (GroupProductVo productVo : group.getGroupProductList()) {
                            reqVo.setId(productVo.getId());
                            reqVo.setIdType(3);
                            actIdList.addAll(activityRelationMapper.getActivityList(reqVo));
                        }
                    }
                }
                break;
            //传入的是产品组ID 查询
            case 2:
                // 根据ID 查询产品组信息
                GroupDetailRes groupDetailRes = productGroupService.groupDetail(reqVo.getId());
                // 根据产品查询
                for (GroupProductVo productVo : groupDetailRes.getProductVoList()) {
                    reqVo.setId(productVo.getId());
                    reqVo.setIdType(3);
                    actIdList.addAll(activityRelationMapper.getActivityList(reqVo));
                }
                break;
            // 传入的是产品ID
            case 3:
                // do nothing
                break;
            // 单品ID
            case 4:
                // do nothing
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + reqVo.getIdType());
        }
        //2.根据活动ID 查询正在进行中的活动
        List<Activity> activityList = activityMapper.getActivityActList(reqVo.getGrantMode(), actIdList, reqVo.getChannel());
        if (!activityList.isEmpty()) {
            List<Integer> proActList = new ArrayList<Integer>();
            for (Activity activity : activityList) {
                // 只需要上线的产品 并且 处于活动期间
                //  永久有效：0否 1是 默认0  需要判断活动时间
                if (activity.getForeverTag() != 1) {
                    // 判断活动是否进行中
                    if (this.onProcessing(activity.getStartDate(), activity.getEndDate())) {
                        // 添加未过期的活动
                        proActList.add(activity.getId());
                    }
                } else {
                    proActList.add(activity.getId());
                }
            }
            // 3.有进行中的活动，查询活动配置的礼券
            if (!proActList.isEmpty()) {
                // 查询礼券配置
                List<ActivityCoupon> couponList = activityCouponMapper.getActivityCouponList(proActList);
                // 根据活动ID分组
                for (Integer actId : proActList) {
                    ActivityResVo activityResVo = new ActivityResVo();
                    activityResVo.setActId(actId);
                    // 获取 礼券配置 追加
                    List<ActivityCouponVo> latsVo = new ArrayList<ActivityCouponVo>();
                    // 活动ID 筛选 活动 唯一
                    BeanUtils.copyProperties(activityList.stream().filter((Activity activity) -> activity.getId().equals(actId))
                            .collect(Collectors.toList()).get(0), activityResVo);
                    if (!couponList.isEmpty()) {
                        // 活动ID 筛选 活动配置 不唯一
                        List<ActivityCoupon> newCoupon = couponList.stream().filter((ActivityCoupon coupon) -> coupon.getActId().equals(actId))
                                .collect(Collectors.toList());
                        // 对象转换
                        for (ActivityCoupon cc : newCoupon) {
                            ActivityCouponVo couponVo = new ActivityCouponVo();
                            BeanUtils.copyProperties(cc, couponVo);
                            latsVo.add(couponVo);
                        }
                        // 礼券配置
                        activityResVo.setCouponVoList(latsVo);
                    }
                    resultList.add(activityResVo);
                }
            }
        }
        return resultList;
    }

    /**
     * 判断活动是否进行中
     *
     * @param startDate
     * @param endDate
     * @return
     */
    private static boolean onProcessing(Date startDate, Date endDate) {
        boolean process = false;
        if (startDate != null) {
            // 开始时间 小于等于 当前时间 活动开始
            if (startDate.getTime() <= new Date().getTime()) {
                process = true;
            } else {
                return false;
            }
        }
        if (endDate != null) {
            // 结束时间 大于等于 当前时间 活动进行中
            if (endDate.getTime() >= new Date().getTime()) {
                process = true;
            } else {
                return false;
            }
        }
        return process;
    }
}
