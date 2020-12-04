package com.colourfulchina.pangu.taishang.service;

import com.colourfulchina.pangu.taishang.api.dto.BlockRule;
import com.colourfulchina.pangu.taishang.api.vo.GenerateBlockRuleVo;
import com.colourfulchina.pangu.taishang.api.vo.req.QueryBookBlockReq;
import com.colourfulchina.pangu.taishang.api.vo.req.QueryCallBookReq;
import com.colourfulchina.pangu.taishang.api.vo.res.QueryBookBlockRes;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

public interface BlockRuleService {
    /**
     * 把blockrule字符串转换成blockrule列表
     * blockrule字符串规则
     * 1.多个blockrule用逗号(,)分隔，如：W1-W3,W2,10/12
     * 2.短横线(-)代表范围，如：W1-W3,10/01-10/10
     * 3.斜线(/)代表年月日周分割
     *      1.如果斜线两边都是数字，则代表是年月日分割，如果有2个斜线，则代表年/月/日；如果1个斜线，代表月/日，如：2019/1/1,10/01-10/10
     *      2.如果斜线右边是字母W，则代表是某段时间段中的周，如：2019/1/1-2019/1/31/W1-W4,10/01-10/10/W4-W5
     *
     * @param blockRule
     * @return
     */
    List<BlockRule> blockRuleStr2list(String blockRule);

    /**
     * 把blockrule集合转换成blockrule字符串
     * @param blockRuleList
     * @return
     */
    String blockRuleList2str(List<BlockRule> blockRuleList);

    /**
     * 检查俩个时间是否是整整一个月
     * @param startTime
     * @param endTime
     * @return
     */
    Boolean checkAllMonth(String startTime,String endTime);

    /**
     * 编辑block规则，解析成前端展示对象
     * @param blockRule
     * @return
     */
    GenerateBlockRuleVo editBlockRule(BlockRule blockRule);

    /**
     * 前段对象 生成block规则
     * @param generateBlockRuleVo
     * @return
     */
    List<BlockRule> generateBlockRule(GenerateBlockRuleVo generateBlockRuleVo);

    /**
     * 查询预约的可预约时间范围
     * @param startDate
     * @param endDate
     * @param blockRule
     * @return
     * @throws Exception
     */
    List<Date> generateBookDate(Date startDate,Date endDate,String blockRule) throws Exception;

    /**
     * 根据时间段和block规则获取具体的block日期
     * @param startDate
     * @param endDate
     * @param blockRule
     * @return
     * @throws Exception
     */
    HashSet<Date> generateBlockDate(Date startDate, Date endDate, String blockRule) throws Exception;

    /**
     * 根据block规则获取有效的block（当前时间之前的block排除）
     * @return
     * @throws Exception
     */
    List<BlockRule> getEffectBlockRule(List<BlockRule> blockRuleList)throws Exception;

    QueryBookBlockRes queryAllBlock(Integer productGroupProductId)throws Exception;

    List<Date> queryBookBlockNew(QueryBookBlockReq queryBookBlockReq);

    //查询block规则
    public String getBlockRule(Integer productGroupProductId);

    /**
     * 来电录单初步查询指定时间能否预约（商户）(已激活的码)
     * @param req
     * @return
     */
    List<Integer> queryActCallBook(QueryCallBookReq req)throws Exception;

    /**
     * 来电录单初步查询指定时间能否预约（商户）(已出库的码)
     * @param req
     * @return
     */
    List<Integer> queryOutCallBook(QueryCallBookReq req)throws Exception;
}
