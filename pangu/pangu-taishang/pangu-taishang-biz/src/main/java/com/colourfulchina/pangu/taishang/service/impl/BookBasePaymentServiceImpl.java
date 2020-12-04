package com.colourfulchina.pangu.taishang.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.colourfulchina.pangu.taishang.api.entity.BookBasePayment;
import com.colourfulchina.pangu.taishang.api.enums.DelFlagEnums;
import com.colourfulchina.pangu.taishang.api.vo.BookBasePaymentVo;
import com.colourfulchina.pangu.taishang.api.vo.req.SelectBookPayReq;
import com.colourfulchina.pangu.taishang.api.vo.res.bookBasePayment.BookBasePaymentRes;
import com.colourfulchina.pangu.taishang.mapper.BookBasePaymentMapper;
import com.colourfulchina.pangu.taishang.service.BookBasePaymentService;
import com.colourfulchina.pangu.taishang.utils.DateUtils;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BookBasePaymentServiceImpl extends ServiceImpl<BookBasePaymentMapper,BookBasePayment> implements BookBasePaymentService {
    @Autowired
    private BookBasePaymentMapper bookBasePaymentMapper;

    /**
     * 查询预约支付金额
     * @param selectBookPayReq
     * @return
     * @throws Exception
     */
    @Override
    public List<BookBasePaymentRes> selectBookPay(SelectBookPayReq selectBookPayReq) throws Exception {
        List<BookBasePaymentRes> list = Lists.newLinkedList();
        Wrapper wrapper = new Wrapper() {
            @Override
            public String getSqlSegment() {
                return "where del_flag = 0 and product_group_product_id ="+selectBookPayReq.getProductGroupProductId();
            }
        };
        List<BookBasePayment> paymentList = bookBasePaymentMapper.selectList(wrapper);
        if (!CollectionUtils.isEmpty(paymentList)){
            for (Date date : selectBookPayReq.getBookDates()) {
                BookBasePaymentRes bookBasePaymentRes = new BookBasePaymentRes();
                BookBasePayment bookBasePayment = foundBookPayByTime(date,paymentList);
                if (bookBasePayment != null){
                    BeanUtils.copyProperties(bookBasePayment,bookBasePaymentRes);
                    bookBasePaymentRes.setBookDate(date);
                    list.add(bookBasePaymentRes);
                }
            }
        }
        return list;
    }

    @Override
    public List<BookBasePaymentRes> selectBookPayList(List<Integer> productGroupProductIdList) {
        List<BookBasePaymentRes> list = Lists.newLinkedList();
        List<BookBasePayment> paymentList = bookBasePaymentMapper.selectBookPayList(productGroupProductIdList);
        if (!CollectionUtils.isEmpty(paymentList)){
            list = paymentList.stream().map(bookBasePayment -> {
                BookBasePaymentRes bookBasePaymentRes = new BookBasePaymentRes();
                BeanUtils.copyProperties(bookBasePayment,bookBasePaymentRes);
                return bookBasePaymentRes;
            }).collect(Collectors.toList());
        }
        return list;
    }


    /**
     * 处理编辑产品组产品时的预约支付金额
     * @param bookBasePaymentVoList
     * @throws Exception
     */
    @Override
    public void optBookPaymentList(List<BookBasePaymentVo> bookBasePaymentVoList) throws Exception {
        if (!CollectionUtils.isEmpty(bookBasePaymentVoList)){
            //软删除旧的预约支付金额
            Wrapper wrapper = new Wrapper() {
                @Override
                public String getSqlSegment() {
                    return "where product_group_product_id ="+bookBasePaymentVoList.get(0).getProductGroupProductId();
                }
            };
            List<BookBasePayment> basePayments = bookBasePaymentMapper.selectList(wrapper);
            if (!CollectionUtils.isEmpty(basePayments)){
                for (BookBasePayment basePayment : basePayments) {
                    basePayment.setDelFlag(DelFlagEnums.DELETE.getCode());
                    bookBasePaymentMapper.updateById(basePayment);
                }
            }
            for (BookBasePaymentVo bookBasePaymentVo : bookBasePaymentVoList) {
                //查询已经存在的预约支付金额
                BookBasePaymentVo finalBookPaymentVo = bookBasePaymentVo;
                Wrapper wrapper1 = new Wrapper() {
                    @Override
                    public String getSqlSegment() {
                        return "where del_flag = 0 and product_group_product_id ="+ finalBookPaymentVo.getProductGroupProductId();
                    }
                };
                List<BookBasePayment> bookBasePayments = bookBasePaymentMapper.selectList(wrapper1);
                bookBasePaymentVo = convertBookPayment(bookBasePaymentVo);
                optPrice(bookBasePaymentVo,bookBasePayments);
            }
        }
    }

    /**
     * 预约支付金额转换
     * @param bookBasePaymentVo
     * @return
     * @throws Exception
     */
    @Override
    public BookBasePaymentVo convertPayment(BookBasePaymentVo bookBasePaymentVo) throws Exception {
        if (bookBasePaymentVo.getBookPrice() == null){
            bookBasePaymentVo.setBookPrice(new BigDecimal(0));
        }
        if (!CollectionUtils.isEmpty(bookBasePaymentVo.getWeeks())){
            if (bookBasePaymentVo.getWeeks().contains(1)){
                bookBasePaymentVo.setMonday(1);
            }else {
                bookBasePaymentVo.setMonday(0);
            }
            if (bookBasePaymentVo.getWeeks().contains(2)){
                bookBasePaymentVo.setTuesday(1);
            }else {
                bookBasePaymentVo.setTuesday(0);
            }
            if (bookBasePaymentVo.getWeeks().contains(3)){
                bookBasePaymentVo.setWednesday(1);
            }else {
                bookBasePaymentVo.setWednesday(0);
            }
            if (bookBasePaymentVo.getWeeks().contains(4)){
                bookBasePaymentVo.setThursday(1);
            }else {
                bookBasePaymentVo.setThursday(0);
            }
            if (bookBasePaymentVo.getWeeks().contains(5)){
                bookBasePaymentVo.setFriday(1);
            }else {
                bookBasePaymentVo.setFriday(0);
            }
            if (bookBasePaymentVo.getWeeks().contains(6)){
                bookBasePaymentVo.setSaturday(1);
            }else {
                bookBasePaymentVo.setSaturday(0);
            }
            if (bookBasePaymentVo.getWeeks().contains(7)){
                bookBasePaymentVo.setSunday(1);
            }else {
                bookBasePaymentVo.setSunday(0);
            }
        }
        bookBasePaymentVo = translatePayment(bookBasePaymentVo);
        return bookBasePaymentVo;
    }

    /**
     * 翻译支付预约金额
     * @param bookBasePaymentVo
     * @return
     * @throws Exception
     */
    @Override
    public BookBasePaymentVo translatePayment(BookBasePaymentVo bookBasePaymentVo) throws Exception {
        List<String> weeks = Lists.newLinkedList();
        StringBuffer timeString = new StringBuffer();
        timeString.append(DateUtil.format(bookBasePaymentVo.getStartDate(),"yyyy-MM-dd"));
        timeString.append("~");
        timeString.append(DateUtil.format(bookBasePaymentVo.getEndDate(),"yyyy-MM-dd"));
        timeString.append("：");
        if (bookBasePaymentVo.getMonday()==1){
            weeks.add("周一");
        }
        if (bookBasePaymentVo.getTuesday()==1){
            weeks.add("周二");
        }
        if (bookBasePaymentVo.getWednesday()==1){
            weeks.add("周三");
        }
        if (bookBasePaymentVo.getThursday()==1){
            weeks.add("周四");
        }
        if (bookBasePaymentVo.getFriday()==1){
            weeks.add("周五");
        }
        if (bookBasePaymentVo.getSaturday()==1){
            weeks.add("周六");
        }
        if (bookBasePaymentVo.getSunday()==1){
            weeks.add("周日");
        }
        if (!CollectionUtils.isEmpty(weeks)){
            timeString.append(StringUtils.join(weeks,"、"));
            bookBasePaymentVo.setTimeStr(timeString.toString());
        }
        return bookBasePaymentVo;
    }

    /**
     * 预约支付金额修改、插入(按相同的时间段和星期覆盖替换)
     * @param pay
     * @param payments
     * @return
     */
    private List<BookBasePayment> optPrice(BookBasePayment pay,List<BookBasePayment> payments){
        List<BookBasePayment> result = Lists.newLinkedList();
        Date newStartDate = DateUtil.parse(DateUtil.format(pay.getStartDate(),"yyyy-MM-dd"),"yyyy-MM-dd");
        Date newEndDate = DateUtil.parse(DateUtil.format(pay.getEndDate(),"yyyy-MM-dd"),"yyyy-MM-dd");
        List<List<BookBasePayment>> remainList = Lists.newLinkedList();
        int i = 0;
        boolean flag = false;
        if (!CollectionUtils.isEmpty(payments)){
            for (BookBasePayment payment : payments) {
                i++;
                long newStart = newStartDate.getTime();
                long newEnd = newEndDate.getTime();
                Date oldStartDate = payment.getStartDate();
                Date oldEndDate = payment.getEndDate();
                long oldStart = oldStartDate.getTime();
                long oldEnd = oldEndDate.getTime();
                Calendar calendar = Calendar.getInstance();
                //新价格的起始时间在老价格起止时间之间,新价格结束时间在老价格起止时间之外
                if (newStart >= oldStart && newStart <= oldEnd && newEnd > oldEnd){
                    //没有相同的星期
                    if (isNoWeekCommon(payment,pay)){
                        //新价格插入
                        BookBasePayment bookPay = new BookBasePayment();
                        BeanUtils.copyProperties(pay,bookPay);
                        bookPay.setStartDate(newStartDate);
                        bookPay.setEndDate(oldEndDate);
                        bookPay.setDelFlag(DelFlagEnums.NORMAL.getCode());
                        bookPay = insertPrice(bookPay);
                        if (bookPay != null){
                            result.add(bookPay);
                        }
                        //剩下的区间塞进列表中
                        List<BookBasePayment> list = Lists.newLinkedList();
                        calendar.setTime(oldEndDate);
                        calendar.add(Calendar.DAY_OF_MONTH,1);
                        BookBasePayment remainPay = new BookBasePayment();
                        BeanUtils.copyProperties(pay,remainPay);
                        remainPay.setStartDate(calendar.getTime());
                        remainPay.setEndDate(newEndDate);
                        list.add(remainPay);
                        remainList.add(list);
                    }else {
                        //老价格所有的星期都在新价格中存在
                        if (isBothWeekCommon(payment,pay)){
                            if (oldStart == newStart){
                                payment.setDelFlag(DelFlagEnums.DELETE.getCode());
                            }else {
                                //老价格结束时间更新为新价格开始时间-1
                                calendar.setTime(newStartDate);
                                calendar.add(Calendar.DAY_OF_MONTH,-1);
                                payment.setEndDate(calendar.getTime());
                            }
                            bookBasePaymentMapper.updateById(payment);
                            //新价格插入
                            BookBasePayment bookPay = new BookBasePayment();
                            BeanUtils.copyProperties(pay,bookPay);
                            bookPay.setStartDate(newStartDate);
                            bookPay.setEndDate(oldEndDate);
                            bookPay.setDelFlag(DelFlagEnums.NORMAL.getCode());
                            bookPay = insertPrice(bookPay);
                            if (bookPay != null){
                                result.add(bookPay);
                            }
                            //剩下的区间塞进列表中
                            List<BookBasePayment> list = Lists.newLinkedList();
                            calendar.setTime(oldEndDate);
                            calendar.add(Calendar.DAY_OF_MONTH,1);
                            BookBasePayment remainPay = new BookBasePayment();
                            BeanUtils.copyProperties(pay,remainPay);
                            remainPay.setStartDate(calendar.getTime());
                            remainPay.setEndDate(newEndDate);
                            list.add(remainPay);
                            remainList.add(list);
                        }else {//老价格部分星期在新价格中存在
                            List<Integer> weeks = findCommonWeek(payment,pay);
                            if (oldStart == newStart){
                                payment.setDelFlag(DelFlagEnums.DELETE.getCode());
                            }else {
                                //老价格结束时间更新为新价格开始时间-1
                                calendar.setTime(newStartDate);
                                calendar.add(Calendar.DAY_OF_MONTH,-1);
                                payment.setEndDate(calendar.getTime());
                            }
                            bookBasePaymentMapper.updateById(payment);
                            //老价格存在而新价格不存在的星期价格插入
                            BookBasePayment oldPay = new BookBasePayment();
                            BeanUtils.copyProperties(payment,oldPay);
                            oldPay.setId(null);
                            oldPay.setStartDate(newStartDate);
                            oldPay.setEndDate(oldEndDate);
                            oldPay.setDelFlag(DelFlagEnums.NORMAL.getCode());
                            if (weeks.contains(1)){
                                oldPay.setMonday(1);
                            }else {
                                oldPay.setMonday(0);
                            }
                            if (weeks.contains(2)){
                                oldPay.setTuesday(1);
                            }else {
                                oldPay.setTuesday(0);
                            }
                            if (weeks.contains(3)){
                                oldPay.setWednesday(1);
                            }else {
                                oldPay.setWednesday(0);
                            }
                            if (weeks.contains(4)){
                                oldPay.setThursday(1);
                            }else {
                                oldPay.setThursday(0);
                            }
                            if (weeks.contains(5)){
                                oldPay.setFriday(1);
                            }else {
                                oldPay.setFriday(0);
                            }
                            if (weeks.contains(6)){
                                oldPay.setSaturday(1);
                            }else {
                                oldPay.setSaturday(0);
                            }
                            if (weeks.contains(7)){
                                oldPay.setSunday(1);
                            }else {
                                oldPay.setSunday(0);
                            }
                            oldPay = insertPrice(oldPay);
                            if (oldPay != null){
                                result.add(oldPay);
                            }
                            //新价格插入
                            BookBasePayment bookPay = new BookBasePayment();
                            BeanUtils.copyProperties(pay,bookPay);
                            bookPay.setStartDate(newStartDate);
                            bookPay.setEndDate(oldEndDate);
                            bookPay.setDelFlag(DelFlagEnums.NORMAL.getCode());
                            bookPay = insertPrice(bookPay);
                            if (bookPay != null){
                                result.add(bookPay);
                            }
                            //剩下的区间塞进列表中
                            List<BookBasePayment> list = Lists.newLinkedList();
                            calendar.setTime(oldEndDate);
                            calendar.add(Calendar.DAY_OF_MONTH,1);
                            BookBasePayment remainPay = new BookBasePayment();
                            BeanUtils.copyProperties(pay,remainPay);
                            remainPay.setStartDate(calendar.getTime());
                            remainPay.setEndDate(newEndDate);
                            list.add(remainPay);
                            remainList.add(list);
                        }
                    }
                }
                //新价格的结束时间在老价格的起止时间之间，新价格的起始时间在老价格的起止时间之外
                else if (newEnd >= oldStart && newEnd <= oldEnd && newStart < oldStart){
                    //没有相同的星期
                    if (isNoWeekCommon(payment,pay)){
                        //新价格插入
                        BookBasePayment bookPay = new BookBasePayment();
                        BeanUtils.copyProperties(pay,bookPay);
                        bookPay.setStartDate(oldStartDate);
                        bookPay.setEndDate(newEndDate);
                        bookPay.setDelFlag(DelFlagEnums.NORMAL.getCode());
                        bookPay = insertPrice(bookPay);
                        if (bookPay != null){
                            result.add(bookPay);
                        }
                        //剩下的区间塞进列表中
                        List<BookBasePayment> list = Lists.newLinkedList();
                        calendar.setTime(oldStartDate);
                        calendar.add(Calendar.DAY_OF_MONTH,-1);
                        BookBasePayment remainPay = new BookBasePayment();
                        BeanUtils.copyProperties(pay,remainPay);
                        remainPay.setStartDate(newStartDate);
                        remainPay.setEndDate(calendar.getTime());
                        list.add(remainPay);
                        remainList.add(list);
                    }else {
                        //老价格所有的星期都在新价格中存在
                        if (isBothWeekCommon(payment,pay)){
                            if (oldEnd == newEnd){
                                payment.setDelFlag(DelFlagEnums.DELETE.getCode());
                            }else {
                                //老价格开始时间更新为新价格结束时间+1
                                calendar.setTime(newEndDate);
                                calendar.add(Calendar.DAY_OF_MONTH,1);
                                payment.setStartDate(calendar.getTime());
                            }
                            bookBasePaymentMapper.updateById(payment);
                            //新价格插入
                            BookBasePayment bookPay = new BookBasePayment();
                            BeanUtils.copyProperties(pay,bookPay);
                            bookPay.setStartDate(oldStartDate);
                            bookPay.setEndDate(newEndDate);
                            bookPay.setDelFlag(DelFlagEnums.NORMAL.getCode());
                            bookPay = insertPrice(bookPay);
                            if (bookPay != null){
                                result.add(bookPay);
                            }
                            //剩下的区间塞进列表中
                            List<BookBasePayment> list = Lists.newLinkedList();
                            calendar.setTime(oldStartDate);
                            calendar.add(Calendar.DAY_OF_MONTH,-1);
                            BookBasePayment remainPay = new BookBasePayment();
                            BeanUtils.copyProperties(pay,remainPay);
                            remainPay.setStartDate(newStartDate);
                            remainPay.setEndDate(calendar.getTime());
                            list.add(remainPay);
                            remainList.add(list);
                        }else {//老价格部分星期在新价格中存在
                            List<Integer> weeks = findCommonWeek(payment,pay);
                            if (oldEnd == newEnd){
                                payment.setDelFlag(DelFlagEnums.DELETE.getCode());
                            }else {
                                //老价格开始时间更新为新价格结束时间+1
                                calendar.setTime(newEndDate);
                                calendar.add(Calendar.DAY_OF_MONTH,1);
                                payment.setStartDate(calendar.getTime());
                            }
                            bookBasePaymentMapper.updateById(payment);
                            //老价格存在而新价格不存在的星期价格插入
                            BookBasePayment oldPay = new BookBasePayment();
                            BeanUtils.copyProperties(payment,oldPay);
                            oldPay.setId(null);
                            oldPay.setStartDate(oldStartDate);
                            oldPay.setEndDate(newEndDate);
                            oldPay.setDelFlag(DelFlagEnums.NORMAL.getCode());
                            if (weeks.contains(1)){
                                oldPay.setMonday(1);
                            }else {
                                oldPay.setMonday(0);
                            }
                            if (weeks.contains(2)){
                                oldPay.setTuesday(1);
                            }else {
                                oldPay.setTuesday(0);
                            }
                            if (weeks.contains(3)){
                                oldPay.setWednesday(1);
                            }else {
                                oldPay.setWednesday(0);
                            }
                            if (weeks.contains(4)){
                                oldPay.setThursday(1);
                            }else {
                                oldPay.setThursday(0);
                            }
                            if (weeks.contains(5)){
                                oldPay.setFriday(1);
                            }else {
                                oldPay.setFriday(0);
                            }
                            if (weeks.contains(6)){
                                oldPay.setSaturday(1);
                            }else {
                                oldPay.setSaturday(0);
                            }
                            if (weeks.contains(7)){
                                oldPay.setSunday(1);
                            }else {
                                oldPay.setSunday(0);
                            }
                            oldPay = insertPrice(oldPay);
                            if (oldPay != null){
                                result.add(oldPay);
                            }
                            //新价格插入
                            BookBasePayment bookPay = new BookBasePayment();
                            BeanUtils.copyProperties(pay,bookPay);
                            bookPay.setStartDate(oldStartDate);
                            bookPay.setEndDate(newEndDate);
                            bookPay.setDelFlag(DelFlagEnums.NORMAL.getCode());
                            bookPay = insertPrice(bookPay);
                            if (bookPay != null){
                                result.add(bookPay);
                            }
                            //剩下的区间塞进列表中
                            List<BookBasePayment> list = Lists.newLinkedList();
                            calendar.setTime(oldStartDate);
                            calendar.add(Calendar.DAY_OF_MONTH,-1);
                            BookBasePayment remainPay = new BookBasePayment();
                            BeanUtils.copyProperties(pay,remainPay);
                            remainPay.setStartDate(newStartDate);
                            remainPay.setEndDate(calendar.getTime());
                            list.add(remainPay);
                            remainList.add(list);
                        }
                    }
                }
                //新价格起止时间都在老价格起止时间之间
                else if (newStart >= oldStart && newEnd <= oldEnd){
                    //没有相同的星期
                    if (isNoWeekCommon(payment,pay)){
                        //新价格插入
                        BookBasePayment bookPay = new BookBasePayment();
                        BeanUtils.copyProperties(pay,bookPay);
                        bookPay.setStartDate(newStartDate);
                        bookPay.setEndDate(newEndDate);
                        bookPay.setDelFlag(DelFlagEnums.NORMAL.getCode());
                        bookPay = insertPrice(bookPay);
                        if (bookPay != null){
                            result.add(bookPay);
                        }
                        flag = true;
                    }else {
                        //老价格所有的星期都在新价格中存在
                        if (isBothWeekCommon(payment,pay)){
                            if (newStart == oldStart && newEnd < oldEnd){
                                //老价格开始时间更新为新价格结束时间+1
                                calendar.setTime(newEndDate);
                                calendar.add(Calendar.DAY_OF_MONTH,1);
                                payment.setStartDate(calendar.getTime());
                                bookBasePaymentMapper.updateById(payment);
                            }else if (newStart > oldStart && newEnd == oldEnd){
                                //老价格结束时间更新为新价格开始时间-1
                                calendar.setTime(newStartDate);
                                calendar.add(Calendar.DAY_OF_MONTH,-1);
                                payment.setEndDate(calendar.getTime());
                                bookBasePaymentMapper.updateById(payment);
                            }else if (newStart == oldStart && newEnd == oldEnd){
                                payment.setDelFlag(DelFlagEnums.DELETE.getCode());
                                bookBasePaymentMapper.updateById(payment);
                            }else if (newStart > oldStart && newEnd < oldEnd){
                                BookBasePayment leftPay = new BookBasePayment();
                                BookBasePayment rightPay = new BookBasePayment();
                                BeanUtils.copyProperties(payment,leftPay);
                                BeanUtils.copyProperties(payment,rightPay);
                                //左边价格插入
                                calendar.setTime(newStartDate);
                                calendar.add(Calendar.DAY_OF_MONTH,-1);
                                leftPay.setId(null);
                                leftPay.setStartDate(oldStartDate);
                                leftPay.setEndDate(calendar.getTime());
                                //右边价格插入
                                calendar.setTime(newEndDate);
                                calendar.add(Calendar.DAY_OF_MONTH,1);
                                rightPay.setId(null);
                                rightPay.setStartDate(calendar.getTime());
                                rightPay.setEndDate(oldEndDate);
                                //老价格删除
                                payment.setDelFlag(DelFlagEnums.DELETE.getCode());
                                leftPay = insertPrice(leftPay);
                                rightPay = insertPrice(rightPay);
                                if (leftPay != null){
                                    result.add(leftPay);
                                }
                                if (rightPay != null){
                                    result.add(rightPay);
                                }
                                bookBasePaymentMapper.updateById(payment);
                            }
                            //新价格插入
                            BookBasePayment bookPay = new BookBasePayment();
                            BeanUtils.copyProperties(pay,bookPay);
                            bookPay.setStartDate(newStartDate);
                            bookPay.setEndDate(newEndDate);
                            bookPay.setDelFlag(DelFlagEnums.NORMAL.getCode());
                            bookPay = insertPrice(bookPay);
                            if (bookPay != null){
                                result.add(bookPay);
                            }
                            flag = true;
                        }else {//老价格部分星期在新价格中存在
                            List<Integer> weeks = findCommonWeek(payment,pay);
                            if (newStart == oldStart && newEnd < oldEnd){
                                //老价格开始时间更新为新价格结束时间+1
                                calendar.setTime(newEndDate);
                                calendar.add(Calendar.DAY_OF_MONTH,1);
                                payment.setStartDate(calendar.getTime());
                                bookBasePaymentMapper.updateById(payment);
                            }else if (newStart > oldStart && newEnd == oldEnd){
                                //老价格结束时间更新为新价格开始时间-1
                                calendar.setTime(newStartDate);
                                calendar.add(Calendar.DAY_OF_MONTH,-1);
                                payment.setEndDate(calendar.getTime());
                                bookBasePaymentMapper.updateById(payment);
                            }else if (newStart == oldStart && newEnd == oldEnd){
                                payment.setDelFlag(DelFlagEnums.DELETE.getCode());
                                bookBasePaymentMapper.updateById(payment);
                            }else if (newStart > oldStart && newEnd < oldEnd){
                                BookBasePayment leftPay = new BookBasePayment();
                                BookBasePayment rightPay = new BookBasePayment();
                                BeanUtils.copyProperties(payment,leftPay);
                                BeanUtils.copyProperties(payment,rightPay);
                                //左边价格插入
                                calendar.setTime(newStartDate);
                                calendar.add(Calendar.DAY_OF_MONTH,-1);
                                leftPay.setId(null);
                                leftPay.setStartDate(oldStartDate);
                                leftPay.setEndDate(calendar.getTime());
                                //右边价格插入
                                calendar.setTime(newEndDate);
                                calendar.add(Calendar.DAY_OF_MONTH,1);
                                rightPay.setId(null);
                                rightPay.setStartDate(calendar.getTime());
                                rightPay.setEndDate(oldEndDate);
                                //老价格删除
                                payment.setDelFlag(DelFlagEnums.DELETE.getCode());
                                leftPay = insertPrice(leftPay);
                                rightPay = insertPrice(rightPay);
                                if (leftPay != null){
                                    result.add(leftPay);
                                }
                                if (rightPay != null){
                                    result.add(rightPay);
                                }
                                bookBasePaymentMapper.updateById(payment);
                            }
                            //老价格存在而新价格不存在的星期价格插入
                            BookBasePayment oldPay = new BookBasePayment();
                            BeanUtils.copyProperties(payment,oldPay);
                            oldPay.setId(null);
                            oldPay.setStartDate(newStartDate);
                            oldPay.setEndDate(newEndDate);
                            oldPay.setDelFlag(DelFlagEnums.NORMAL.getCode());
                            if (weeks.contains(1)){
                                oldPay.setMonday(1);
                            }else {
                                oldPay.setMonday(0);
                            }
                            if (weeks.contains(2)){
                                oldPay.setTuesday(1);
                            }else {
                                oldPay.setTuesday(0);
                            }
                            if (weeks.contains(3)){
                                oldPay.setWednesday(1);
                            }else {
                                oldPay.setWednesday(0);
                            }
                            if (weeks.contains(4)){
                                oldPay.setThursday(1);
                            }else {
                                oldPay.setThursday(0);
                            }
                            if (weeks.contains(5)){
                                oldPay.setFriday(1);
                            }else {
                                oldPay.setFriday(0);
                            }
                            if (weeks.contains(6)){
                                oldPay.setSaturday(1);
                            }else {
                                oldPay.setSaturday(0);
                            }
                            if (weeks.contains(7)){
                                oldPay.setSunday(1);
                            }else {
                                oldPay.setSunday(0);
                            }
                            oldPay = insertPrice(oldPay);
                            if (oldPay != null){
                                result.add(oldPay);
                            }
                            //新价格插入
                            BookBasePayment bookPay = new BookBasePayment();
                            BeanUtils.copyProperties(pay,bookPay);
                            bookPay.setStartDate(newStartDate);
                            bookPay.setEndDate(newEndDate);
                            bookPay.setDelFlag(DelFlagEnums.NORMAL.getCode());
                            bookPay = insertPrice(bookPay);
                            if (bookPay != null){
                                result.add(bookPay);
                            }
                            flag = true;
                        }
                    }
                }
                //老价格开始时间大于新价格的起始时间，老价格的结束时间小于新价格的结束时间
                else if (newStart < oldStart && newEnd > oldEnd){
                    //没有相同的星期
                    if (isNoWeekCommon(payment,pay)){
                        //新价格插入
                        BookBasePayment bookPay = new BookBasePayment();
                        BeanUtils.copyProperties(pay,bookPay);
                        bookPay.setStartDate(oldStartDate);
                        bookPay.setEndDate(oldEndDate);
                        bookPay.setDelFlag(DelFlagEnums.NORMAL.getCode());
                        bookPay = insertPrice(bookPay);
                        if (bookPay != null){
                            result.add(bookPay);
                        }
                        //左边价格
                        List<BookBasePayment> list = Lists.newLinkedList();
                        BookBasePayment leftPay = new BookBasePayment();
                        BeanUtils.copyProperties(pay,leftPay);
                        calendar.setTime(oldStartDate);
                        calendar.add(Calendar.DAY_OF_MONTH,-1);
                        leftPay.setStartDate(newStartDate);
                        leftPay.setEndDate(calendar.getTime());
                        list.add(leftPay);
                        //右边价格
                        BookBasePayment rightPay = new BookBasePayment();
                        BeanUtils.copyProperties(pay,rightPay);
                        calendar.setTime(oldEndDate);
                        calendar.add(Calendar.DAY_OF_MONTH,1);
                        rightPay.setStartDate(calendar.getTime());
                        rightPay.setEndDate(newEndDate);
                        list.add(rightPay);
                        remainList.add(list);
                    }else {
                        //老价格所有的星期都在新价格中存在
                        if (isBothWeekCommon(payment,pay)){
                            //老价格删除
                            payment.setDelFlag(DelFlagEnums.DELETE.getCode());
                            bookBasePaymentMapper.updateById(payment);
                            //新价格插入
                            BookBasePayment bookPay = new BookBasePayment();
                            BeanUtils.copyProperties(pay,bookPay);
                            bookPay.setStartDate(oldStartDate);
                            bookPay.setEndDate(oldEndDate);
                            bookPay.setDelFlag(DelFlagEnums.NORMAL.getCode());
                            bookPay = insertPrice(bookPay);
                            if (bookPay != null){
                                result.add(bookPay);
                            }
                            //左边价格
                            List<BookBasePayment> list = Lists.newLinkedList();
                            BookBasePayment leftPay = new BookBasePayment();
                            BeanUtils.copyProperties(pay,leftPay);
                            calendar.setTime(oldStartDate);
                            calendar.add(Calendar.DAY_OF_MONTH,-1);
                            leftPay.setStartDate(newStartDate);
                            leftPay.setEndDate(calendar.getTime());
                            list.add(leftPay);
                            //右边价格
                            BookBasePayment rightPay = new BookBasePayment();
                            BeanUtils.copyProperties(pay,rightPay);
                            calendar.setTime(oldEndDate);
                            calendar.add(Calendar.DAY_OF_MONTH,1);
                            rightPay.setStartDate(calendar.getTime());
                            rightPay.setEndDate(newEndDate);
                            list.add(rightPay);
                            remainList.add(list);
                        }else {//老价格部分星期在新价格中存在
                            List<Integer> weeks = findCommonWeek(payment,pay);
                            //老价格更新
                            if (weeks.contains(1)){
                                payment.setMonday(1);
                            }else {
                                payment.setMonday(0);
                            }
                            if (weeks.contains(2)){
                                payment.setTuesday(1);
                            }else {
                                payment.setTuesday(0);
                            }
                            if (weeks.contains(3)){
                                payment.setWednesday(1);
                            }else {
                                payment.setWednesday(0);
                            }
                            if (weeks.contains(4)){
                                payment.setThursday(1);
                            }else {
                                payment.setThursday(0);
                            }
                            if (weeks.contains(5)){
                                payment.setFriday(1);
                            }else {
                                payment.setFriday(0);
                            }
                            if (weeks.contains(6)){
                                payment.setSaturday(1);
                            }else {
                                payment.setSaturday(0);
                            }
                            if (weeks.contains(7)){
                                payment.setSunday(1);
                            }else {
                                payment.setSunday(0);
                            }
                            bookBasePaymentMapper.updateById(payment);
                            //新价格插入
                            BookBasePayment bookPay = new BookBasePayment();
                            BeanUtils.copyProperties(pay,bookPay);
                            bookPay.setStartDate(oldStartDate);
                            bookPay.setEndDate(oldEndDate);
                            bookPay.setDelFlag(DelFlagEnums.NORMAL.getCode());
                            bookPay = insertPrice(bookPay);
                            if (bookPay != null){
                                result.add(bookPay);
                            }
                            //左边价格
                            List<BookBasePayment> list = Lists.newLinkedList();
                            BookBasePayment leftPay = new BookBasePayment();
                            BeanUtils.copyProperties(pay,leftPay);
                            calendar.setTime(oldStartDate);
                            calendar.add(Calendar.DAY_OF_MONTH,-1);
                            leftPay.setStartDate(newStartDate);
                            leftPay.setEndDate(calendar.getTime());
                            list.add(leftPay);
                            //右边价格
                            BookBasePayment rightPay = new BookBasePayment();
                            BeanUtils.copyProperties(pay,rightPay);
                            calendar.setTime(oldEndDate);
                            calendar.add(Calendar.DAY_OF_MONTH,1);
                            rightPay.setStartDate(calendar.getTime());
                            rightPay.setEndDate(newEndDate);
                            list.add(rightPay);
                            remainList.add(list);
                        }
                    }
                }else {
                    List<BookBasePayment> list = Lists.newLinkedList();
                    BookBasePayment remainPay = new BookBasePayment();
                    BeanUtils.copyProperties(pay,remainPay);
                    remainPay.setStartDate(newStartDate);
                    remainPay.setEndDate(newEndDate);
                    list.add(remainPay);
                    remainList.add(list);
                }
                if (i == payments.size()){
                    if (flag){
                        remainList = null;
                    }
                    if (!CollectionUtils.isEmpty(remainList)){
                        List<BookBasePayment> intersectList = Lists.newLinkedList();
                        intersectList = remainList.get(0);
                        int j = 0;
                        for (List<BookBasePayment> bookBasePayments : remainList) {
                            j++;
                            if (!CollectionUtils.isEmpty(intersectList)){
                                intersectList = unionPriceDate(intersectList,bookBasePayments);
                            }
                            if (j == remainList.size()){
                                if (!CollectionUtils.isEmpty(intersectList)){
                                    for (BookBasePayment bookBasePayment : intersectList) {
                                        BookBasePayment temp = new BookBasePayment();
                                        bookBasePayment.setDelFlag(DelFlagEnums.NORMAL.getCode());
                                        temp = insertPrice(bookBasePayment);
                                        if (temp != null){
                                            result.add(temp);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }else {
            //新价格插入
            BookBasePayment bookPay = new BookBasePayment();
            BeanUtils.copyProperties(pay,bookPay);
            bookPay.setStartDate(newStartDate);
            bookPay.setEndDate(newEndDate);
            bookPay.setDelFlag(DelFlagEnums.NORMAL.getCode());
            bookPay = insertPrice(bookPay);
            if (bookPay != null){
                result.add(bookPay);
            }
        }
        return result;
    }

    /**
     * 价格入库，重复入库判断
     * @param pay
     * @return
     */
    private BookBasePayment insertPrice(BookBasePayment pay){
        Wrapper wrapper = new Wrapper() {
            @Override
            public String getSqlSegment() {
                return "where del_flag = 0 and product_group_product_id ="+pay.getProductGroupProductId() +" and start_date = '"+pay.getStartDate()+
                        "' and end_date = '"+pay.getEndDate()+"' and monday ="+pay.getMonday()+" and tuesday ="+pay.getTuesday()+" and wednesday ="+pay.getWednesday()+
                        " and thursday ="+pay.getThursday()+" and friday ="+pay.getFriday()+" and saturday ="+pay.getSaturday()+" and sunday ="+pay.getSunday()+
                        " and book_price ="+pay.getBookPrice();
            }
        };
        List<BookBasePayment> bookBasePayments = bookBasePaymentMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(bookBasePayments)){
            bookBasePaymentMapper.insert(pay);
            return pay;
        }
        return null;
    }

    /**
     * 得到2个列表中价格的交集(按相同的年月日区分)
     * @param intersectList
     * @param remainList
     * @return
     */
    private List<BookBasePayment> unionPriceDate(List<BookBasePayment> intersectList,List<BookBasePayment> remainList){
        List<BookBasePayment> result = Lists.newLinkedList();
        for (BookBasePayment intersect : intersectList) {
            Date intersectStartDate = intersect.getStartDate();
            Date intersectEndDate = intersect.getEndDate();
            long intersectStart = intersectStartDate.getTime();
            long intersectEnd = intersectEndDate.getTime();
            for (BookBasePayment remain : remainList) {
                Date remainStartDate = remain.getStartDate();
                Date remainEndDate = remain.getEndDate();
                long remainStart = remainStartDate.getTime();
                long remainEnd = remainEndDate.getTime();
                if (remainStart >= intersectStart && remainStart <= intersectEnd && remainEnd > intersectEnd){
                    BookBasePayment pay = new BookBasePayment();
                    BeanUtils.copyProperties(intersect,pay);
                    pay.setStartDate(remainStartDate);
                    pay.setEndDate(intersectEndDate);
                    result.add(pay);
                }else if (remainEnd >= intersectStart && remainEnd <= intersectEnd && remainStart < intersectStart){
                    BookBasePayment pay = new BookBasePayment();
                    BeanUtils.copyProperties(intersect,pay);
                    pay.setStartDate(intersectStartDate);
                    pay.setEndDate(remainEndDate);
                    result.add(pay);
                }else if (remainStart >= intersectStart && remainEnd <= intersectEnd){
                    BookBasePayment pay = new BookBasePayment();
                    BeanUtils.copyProperties(intersect,pay);
                    pay.setStartDate(remainStartDate);
                    pay.setEndDate(remainEndDate);
                    result.add(pay);
                }else if (remainStart < intersectStart && remainEnd > intersectEnd){
                    BookBasePayment pay = new BookBasePayment();
                    BeanUtils.copyProperties(intersect,pay);
                    pay.setStartDate(intersectStartDate);
                    pay.setEndDate(intersectEndDate);
                    result.add(pay);
                }
            }
        }
        return result;
    }

    /**
     * 查找第一个价格存在而第二个价格不存在的星期列表
     * @param pay1
     * @param pay2
     * @return
     */
    private List<Integer> findCommonWeek(BookBasePayment pay1,BookBasePayment pay2){
        List<Integer> list = Lists.newLinkedList();
        if (pay1.getMonday() == 1 && pay1.getMonday() != pay2.getMonday()){
            list.add(1);
        }
        if (pay1.getTuesday() == 1 && pay1.getTuesday() != pay2.getTuesday()){
            list.add(2);
        }
        if (pay1.getWednesday() == 1 && pay1.getWednesday() != pay2.getWednesday()){
            list.add(3);
        }
        if (pay1.getThursday() == 1 && pay1.getThursday() != pay2.getThursday()){
            list.add(4);
        }
        if (pay1.getFriday() == 1 && pay1.getFriday() != pay2.getFriday()){
            list.add(5);
        }
        if (pay1.getSaturday() == 1 && pay1.getSaturday() != pay2.getSaturday()){
            list.add(6);
        }
        if (pay1.getSunday() == 1 && pay1.getSunday() != pay2.getSunday()){
            list.add(7);
        }
        return list;
    }

    /**
     * 判断第一个价格是否全部在第二个价格中存在相同的星期
     * @param pay1
     * @param pay2
     * @return
     */
    private boolean isBothWeekCommon(BookBasePayment pay1,BookBasePayment pay2){
        boolean flag = true;
        if (pay1.getMonday() == 1 && pay1.getMonday() != pay2.getMonday()){
            flag = false;
        }
        if (pay1.getTuesday() == 1 && pay1.getTuesday() != pay2.getTuesday()){
            flag = false;
        }
        if (pay1.getWednesday() == 1 && pay1.getWednesday() != pay2.getWednesday()){
            flag = false;
        }
        if (pay1.getThursday() == 1 && pay1.getThursday() != pay2.getThursday()){
            flag = false;
        }
        if (pay1.getFriday() == 1 && pay1.getFriday() != pay2.getFriday()){
            flag = false;
        }
        if (pay1.getSaturday() == 1 && pay1.getSaturday() != pay2.getSaturday()){
            flag = false;
        }
        if (pay1.getSunday() == 1 && pay1.getSunday() != pay2.getSunday()){
            flag = false;
        }
        return flag;
    }

    /**
     * 判断两个价格是否没有相同的星期
     * @param pay1
     * @param pay2
     * @return
     */
    private boolean isNoWeekCommon(BookBasePayment pay1,BookBasePayment pay2){
        boolean flag = true;
        if (pay1.getMonday() == 1 && pay1.getMonday() == pay2.getMonday()){
            flag = false;
        }
        if (pay1.getTuesday() == 1 && pay1.getTuesday() == pay2.getTuesday()){
            flag = false;
        }
        if (pay1.getWednesday() == 1 && pay1.getWednesday() == pay2.getWednesday()){
            flag = false;
        }
        if (pay1.getThursday() == 1 && pay1.getThursday() == pay2.getThursday()){
            flag = false;
        }
        if (pay1.getFriday() == 1 && pay1.getFriday() == pay2.getFriday()){
            flag = false;
        }
        if (pay1.getSaturday() == 1 && pay1.getSaturday() == pay2.getSaturday()){
            flag = false;
        }
        if (pay1.getSunday() == 1 && pay1.getSunday() == pay2.getSunday()){
            flag = false;
        }
        return flag;
    }

    /**
     * 预约支付金额转换
     * @param bookBasePaymentVo
     * @return
     */
    private BookBasePaymentVo convertBookPayment(BookBasePaymentVo bookBasePaymentVo){
        bookBasePaymentVo.setId(null);
        if (bookBasePaymentVo.getBookPrice() == null){
            bookBasePaymentVo.setBookPrice(new BigDecimal(0));
        }
        if (!CollectionUtils.isEmpty(bookBasePaymentVo.getWeeks())){
            if (bookBasePaymentVo.getWeeks().contains(1)){
                bookBasePaymentVo.setMonday(1);
            }else {
                bookBasePaymentVo.setMonday(0);
            }
            if (bookBasePaymentVo.getWeeks().contains(2)){
                bookBasePaymentVo.setTuesday(1);
            }else {
                bookBasePaymentVo.setTuesday(0);
            }
            if (bookBasePaymentVo.getWeeks().contains(3)){
                bookBasePaymentVo.setWednesday(1);
            }else {
                bookBasePaymentVo.setWednesday(0);
            }
            if (bookBasePaymentVo.getWeeks().contains(4)){
                bookBasePaymentVo.setThursday(1);
            }else {
                bookBasePaymentVo.setThursday(0);
            }
            if (bookBasePaymentVo.getWeeks().contains(5)){
                bookBasePaymentVo.setFriday(1);
            }else {
                bookBasePaymentVo.setFriday(0);
            }
            if (bookBasePaymentVo.getWeeks().contains(6)){
                bookBasePaymentVo.setSaturday(1);
            }else {
                bookBasePaymentVo.setSaturday(0);
            }
            if (bookBasePaymentVo.getWeeks().contains(7)){
                bookBasePaymentVo.setSunday(1);
            }else {
                bookBasePaymentVo.setSunday(0);
            }
        }
        return bookBasePaymentVo;
    }

    /**
     * 预约时间对应的预约支付金额
     * @param bookDate
     * @param paymentList
     * @return
     */
    public BookBasePayment foundBookPayByTime(Date bookDate, List<BookBasePayment> paymentList)throws Exception{
        BookBasePayment res = null;
        Integer week = DateUtils.dateForWeek(bookDate);
        if (!CollectionUtils.isEmpty(paymentList)){
            for (BookBasePayment bookBasePayment : paymentList) {
                boolean dateFlag = DateUtils.belongCalendar(bookDate,bookBasePayment.getStartDate(),bookBasePayment.getEndDate());
                if (dateFlag){
                    if (week.compareTo(2) == 0){
                        if (bookBasePayment.getMonday() == 1){
                            res = bookBasePayment;
                            break;
                        }
                    }
                    if (week.compareTo(3) == 0){
                        if (bookBasePayment.getTuesday() == 1){
                            res = bookBasePayment;
                            break;
                        }
                    }
                    if (week.compareTo(4) == 0){
                        if (bookBasePayment.getWednesday() == 1){
                            res = bookBasePayment;
                            break;
                        }
                    }
                    if (week.compareTo(5) == 0){
                        if (bookBasePayment.getThursday() == 1){
                            res = bookBasePayment;
                            break;
                        }
                    }
                    if (week.compareTo(6) == 0){
                        if (bookBasePayment.getFriday() == 1){
                            res = bookBasePayment;
                            break;
                        }
                    }
                    if (week.compareTo(7) == 0){
                        if (bookBasePayment.getSaturday() == 1){
                            res = bookBasePayment;
                            break;
                        }
                    }
                    if (week.compareTo(1) == 0){
                        if (bookBasePayment.getSunday() == 1){
                            res = bookBasePayment;
                            break;
                        }
                    }
                }
            }
        }
        return res;
    }
}