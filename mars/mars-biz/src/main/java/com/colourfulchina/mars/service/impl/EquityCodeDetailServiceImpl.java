package com.colourfulchina.mars.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.mars.api.entity.EquityCodeDetail;
import com.colourfulchina.mars.api.enums.GiftCodeDetailTypeEnum;
import com.colourfulchina.mars.api.enums.ReservOrderStatusEnums;
import com.colourfulchina.mars.api.vo.res.GiftTimesVo;
import com.colourfulchina.mars.mapper.EquityCodeDetailMapper;
import com.colourfulchina.mars.service.EquityCodeDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service
public class EquityCodeDetailServiceImpl extends ServiceImpl<EquityCodeDetailMapper, EquityCodeDetail> implements EquityCodeDetailService {
    @Autowired
    EquityCodeDetailMapper equityCodeDetailMapper;

    /**
     * 查询当前权益剩余的使用次数
     *
     * @param memberId
     * @param goodsId
     * @param id
     * @param giftCodeId
     * @return
     */
    @Override
    public EquityCodeDetail selectByEquityCode(Long memberId, Integer goodsId, Integer id, Integer giftCodeId, Date bookDate) {
        EntityWrapper local = new EntityWrapper();
        if (memberId != null) {
            local.eq("member_id", memberId);
        }
        local.eq("gift_code_id", giftCodeId);
        if (goodsId != null) {
            local.eq("goods_id", goodsId);
        }
        if (id != null) {
            local.eq("product_group_id", id);
        }
        List<EquityCodeDetail> equityCodeDetails = equityCodeDetailMapper.selectList(local);
        if(equityCodeDetails != null) {
            //判断start_time和end_time 为不为null 取不为null时预约日期在里面的一条数据
            for (EquityCodeDetail detail : equityCodeDetails) {
                if(detail.getType().compareTo(GiftCodeDetailTypeEnum.CYCLE_TYPE.getcode()) == 0) {
                    if (detail.getStartTime() != null && detail.getEndTime() != null) {
                        String start_time = DateUtil.format(detail.getStartTime(), "yyyy-MM-dd");
                        String end_time = DateUtil.format(detail.getEndTime(), "yyyy-MM-dd");
                        String book_date = DateUtil.format(bookDate, "yyyy-MM-dd");
                        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                        try {
                            Date dt1 = df.parse(start_time);
                            Date dt2 = df.parse(end_time);
                            Date dt3 = df.parse(book_date);
                            if (dt3.getTime() >= dt1.getTime() && dt3.getTime() <= dt2.getTime()) {
                                return detail;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return CollectionUtils.isEmpty(equityCodeDetails) ? null : equityCodeDetails.get(0);
    }
    /*
     * 改变权益次数
     * */
    @Override
    public EquityCodeDetail changeGiftTimes(String type, Long memberId, Integer goodsId, Integer id, Integer giftCodeId, int num, Date bookDate) throws Exception{
        return changeGiftTimes(type, memberId, goodsId, id, giftCodeId, num, bookDate,null);
    }

    /*
    * 改变权益次数
    * */
    @Override
    public EquityCodeDetail changeGiftTimes(String type, Long memberId, Integer goodsId, Integer productGroupId, Integer giftCodeId, int num,Date bookDate,Integer optUseFreeCount) throws Exception {
        log.info("changeGiftTimes type:{} memberId:{} goodsId:{} productGroupId:{} giftCodeId:{} num:{} useFreeCount:{}",type,memberId,goodsId,productGroupId,giftCodeId,num,bookDate,optUseFreeCount);
        EquityCodeDetail detail = selectByEquityCode(null, goodsId, productGroupId, giftCodeId,bookDate);
        Assert.notNull(detail,"没有权益！！！");
        Integer totalCount =  detail.getTotalCount();
        Integer useCount = detail.getUseCount();
        Integer totalFreeCount = detail.getTotalFreeCount();
        Integer useFreeCount = detail.getUseFreeCount();
        Integer tmpCount ;
        Integer tmpUseFreeCount=0;
        if (ReservOrderStatusEnums.typeEnum.ADD.getCode().equalsIgnoreCase(type)) {
            tmpCount =useCount-num>0?useCount-num:0;
            if(optUseFreeCount!=null){
                //扣减免费次数
                tmpUseFreeCount =useFreeCount-optUseFreeCount>0?useFreeCount-optUseFreeCount:0;
            }
        } else {
            //无限制  更新use_count
            //use_count+num>total_count  异常  回滚 测试 是否能回滚
            //use_count+num <total_count    update
            tmpCount =useCount+num;
            if(totalCount!=null &&tmpCount>totalCount){
                throw new Exception("权益可使用次数小于"+num);
            }
            if(optUseFreeCount!=null){
                tmpUseFreeCount =useFreeCount+optUseFreeCount;
                if(totalFreeCount!=null &&tmpUseFreeCount>totalFreeCount){
                    throw new Exception("免费总剩余次数次数小于"+tmpUseFreeCount);
                }
            }
        }
        detail.setUseCount(tmpCount);
        if(optUseFreeCount!=null) {
            detail.setUseFreeCount(tmpUseFreeCount);
        }
        Assert.isTrue(detail.getUseCount()>=0,"权益使用次数有误");
        if(null != detail.getTotalCount()){
            Assert.isTrue(detail.getTotalCount().compareTo(detail.getUseCount())>=0,"权益使用次数有误");
        }
        final Integer updateById = equityCodeDetailMapper.updateById(detail);
        if (updateById <= 0) {
            throw new Exception("权益次数更改失败");
        }
        Assert.isTrue(updateById==1,giftCodeId+"保存权益使用次数失败");

        return detail;
    }

    /**
     * 查询激活码详细信息
     *
     * @param goodsId
     * @param giftCodeId
     * @return
     */
    @Override
    public List<EquityCodeDetail> selectByEquityByGoodsId(Integer goodsId, Integer giftCodeId) {
        EntityWrapper local = new EntityWrapper();
        local.eq("gift_code_id", giftCodeId);
        if (goodsId != null) {
            local.eq("goods_id", goodsId);
        }

        List<EquityCodeDetail> equityCodeDetails = equityCodeDetailMapper.selectList(local);
        //定义List返回值 重新拼接返回的数据
        List<EquityCodeDetail> newDetail = new ArrayList<EquityCodeDetail>();
        if(equityCodeDetails != null) {
            //修改原因： 商区系统需要的返回值为原来equitycodedetail数据格式。现在按照条件返回原始格式
            //将产品组id相同的多条数据使用次数use_count 合并成一条数据放进map中
            Map<Integer, Integer> map = new HashMap<Integer, Integer>();
            for (EquityCodeDetail detail : equityCodeDetails) {
                if (detail.getStartTime() != null && detail.getEndTime() != null) {
                    if (map.containsKey(detail.getProductGroupId())) {
                        Integer a = map.get(detail.getProductGroupId());
                        a += detail.getUseCount().intValue();
                        map.put(detail.getProductGroupId(), a);
                    } else {
                        map.put(detail.getProductGroupId(), detail.getUseCount().intValue());
                    }
                } else {
                    newDetail.add(detail);
                }
            }
            //拿到刚刚键值对中的符合条件的第一条数据并将累加后的value值赋值到usecount 然后移除map
            for (EquityCodeDetail details : equityCodeDetails) {
                if (map.containsKey(details.getProductGroupId())) {
                    details.setUseCount(map.get(details.getProductGroupId()));
                    newDetail.add(details);
                    map.remove(details.getProductGroupId());
                }
            }
        }

        return newDetail;

    }

    /**
     * 根据gift_code的id判断权益是否使用过:true:未使用;false:有权益已经使用
     * @param giftCodeId
     * @return
     */
    @Override
    public Boolean checkGiftCodeUse(Integer giftCodeId) throws Exception {
        if(null == giftCodeId){
            throw new Exception("参数不能为空");
        }
        List<EquityCodeDetail> equityCodeDetails = equityCodeDetailMapper.selectEquityCodeDetail(giftCodeId);
        if(!CollectionUtils.isEmpty(equityCodeDetails)){
            for(EquityCodeDetail equityCodeDetail : equityCodeDetails){
                if(equityCodeDetail.getUseCount().intValue() > 0){
                    //使用次数 > 0 : 有权益已经使用
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 根据gift_code的id查询权益信息
     * @param giftCodeId
     * @return
     */
    @Override
    public List<EquityCodeDetail> selectEquityCodeDetailListByGiftCodeId(Integer giftCodeId) throws Exception {
        if(null == giftCodeId){
            throw new Exception("参数不能为空");
        }
        //定义List返回值 重新拼接返回的数据
        List<EquityCodeDetail> newDetail = new ArrayList<EquityCodeDetail>();
        List<EquityCodeDetail> equityCodeDetails = equityCodeDetailMapper.selectEquityCodeDetail(giftCodeId);
        if(equityCodeDetails != null) {
            //修改原因： 商区系统需要的返回值为原来equitycodedetail数据格式。现在按照条件返回原始格式
            //将产品组id相同的多条数据使用次数use_count 合并成一条数据放进map中
            Map<Integer, Integer> map = new HashMap<Integer, Integer>();
            for (EquityCodeDetail detail : equityCodeDetails) {
                if (detail.getStartTime() != null && detail.getEndTime() != null) {
                    if (map.containsKey(detail.getProductGroupId())) {
                        Integer a = map.get(detail.getProductGroupId());
                        a += detail.getUseCount().intValue();
                        map.put(detail.getProductGroupId(), a);
                    } else {
                        map.put(detail.getProductGroupId(), detail.getUseCount().intValue());
                    }
                } else {
                    newDetail.add(detail);
                }
            }
            //拿到刚刚键值对中的符合条件的第一条数据并将累加后的value值赋值到usecount 然后移除map
            for (EquityCodeDetail details : equityCodeDetails) {
                if (map.containsKey(details.getProductGroupId())) {
                    details.setUseCount(map.get(details.getProductGroupId()));
                    newDetail.add(details);
                    map.remove(details.getProductGroupId());
                }
            }
        }

        return newDetail;
    }

    @Override
    public List<GiftTimesVo> selectGiftTimesVoList(Integer groupId, Integer giftCodeId) {
        List<GiftTimesVo> ls = new ArrayList<GiftTimesVo>();
        List<EquityCodeDetail> result = equityCodeDetailMapper.selectGiftTimesVoList(groupId, giftCodeId);
        for(EquityCodeDetail e : result){
            GiftTimesVo vo = new GiftTimesVo();
            vo.setId(e.getProductGroupId());
            vo.setTimes(e.getTotalCount());
            vo.setPrv_cost(e.getUseCount());
            ls.add(vo);
        }
        return ls;
    }
}
