package com.colourfulchina.pangu.taishang.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.colourfulchina.pangu.taishang.api.dto.BlockRule;
import com.colourfulchina.pangu.taishang.api.dto.BookNum;
import com.colourfulchina.pangu.taishang.api.dto.Lunar;
import com.colourfulchina.pangu.taishang.api.dto.Solar;
import com.colourfulchina.pangu.taishang.api.entity.*;
import com.colourfulchina.pangu.taishang.api.enums.BlockRuleEnums;
import com.colourfulchina.pangu.taishang.api.enums.WeekEnums;
import com.colourfulchina.pangu.taishang.api.utils.GoodsUtils;
import com.colourfulchina.pangu.taishang.api.vo.BlockBookDaysVo;
import com.colourfulchina.pangu.taishang.api.vo.GenerateBlockRuleVo;
import com.colourfulchina.pangu.taishang.api.vo.req.GoodsBaseVo;
import com.colourfulchina.pangu.taishang.api.vo.req.QueryBookBlockReq;
import com.colourfulchina.pangu.taishang.api.vo.req.QueryCallBookReq;
import com.colourfulchina.pangu.taishang.api.vo.req.QueryOrderExpireTimeReq;
import com.colourfulchina.pangu.taishang.api.vo.res.QueryBookBlockRes;
import com.colourfulchina.pangu.taishang.mapper.*;
import com.colourfulchina.pangu.taishang.service.BlockRuleService;
import com.colourfulchina.pangu.taishang.service.HotelService;
import com.colourfulchina.pangu.taishang.service.SysHolidayConfigService;
import com.colourfulchina.pangu.taishang.utils.BookMinMaxDayUtils;
import com.colourfulchina.pangu.taishang.utils.DateUtils;
import com.colourfulchina.pangu.taishang.utils.LunarSolarConverterUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class BlockRuleServiceImpl implements BlockRuleService {
    private static final ThreadLocal<List<SysHolidayConfig>> HOLIDAY_THREAD_LOCAL=new ThreadLocal<>();
    private static final Map<String,String> WEEK_MAP= Maps.newHashMap();
    private static final Map<String,String> MONTH_MAP= Maps.newLinkedHashMap();
    private static final Map<String,String> REPLACE_MAP= Maps.newLinkedHashMap();
    private static final List<String> WEEK_LIST=Lists.newArrayList();
    private static final List<String> WEEK_CODE_LIST=Lists.newArrayList();
    private static final List<String> PATTERN_LIST=Lists.newArrayList();
    private static final Map<String,List<String>> PATTERN_MAP=Maps.newHashMap();
    private static final String PATTERN_WEEK="PATTERN_WEEK";
    private static final String PATTERN_HOLIDAY="PATTERN_HOLIDAY";
    private static final String PATTERN_LUNAR="PATTERN_LUNAR";
    private static final String PATTERN_SOLAR="PATTERN_SOLAR";
    static {
        WEEK_MAP.put("W1","星期一");
        WEEK_MAP.put("W2","星期二");
        WEEK_MAP.put("W3","星期三");
        WEEK_MAP.put("W4","星期四");
        WEEK_MAP.put("W5","星期五");
        WEEK_MAP.put("W6","星期六");
        WEEK_MAP.put("W7","星期日");

        MONTH_MAP.put("M10","10月");
        MONTH_MAP.put("M11","11月");
        MONTH_MAP.put("M12","12月");
        MONTH_MAP.put("M1","1月");
        MONTH_MAP.put("M2","2月");
        MONTH_MAP.put("M3","3月");
        MONTH_MAP.put("M4","4月");
        MONTH_MAP.put("M5","5月");
        MONTH_MAP.put("M6","6月");
        MONTH_MAP.put("M7","7月");
        MONTH_MAP.put("M8","8月");
        MONTH_MAP.put("M9","9月");
        MONTH_MAP.put("M01","1月");
        MONTH_MAP.put("M02","2月");
        MONTH_MAP.put("M03","3月");
        MONTH_MAP.put("M04","4月");
        MONTH_MAP.put("M05","5月");
        MONTH_MAP.put("M06","6月");
        MONTH_MAP.put("M07","7月");
        MONTH_MAP.put("M08","8月");
        MONTH_MAP.put("M09","9月");

        REPLACE_MAP.putAll(WEEK_MAP);
        REPLACE_MAP.putAll(MONTH_MAP);
        REPLACE_MAP.put("C","阴历");

        WEEK_LIST.add("星期一");
        WEEK_LIST.add("星期二");
        WEEK_LIST.add("星期三");
        WEEK_LIST.add("星期四");
        WEEK_LIST.add("星期五");
        WEEK_LIST.add("星期六");
        WEEK_LIST.add("星期日");

        WEEK_CODE_LIST.add("W1");
        WEEK_CODE_LIST.add("W2");
        WEEK_CODE_LIST.add("W3");
        WEEK_CODE_LIST.add("W4");
        WEEK_CODE_LIST.add("W5");
        WEEK_CODE_LIST.add("W6");
        WEEK_CODE_LIST.add("W7");


        PATTERN_LIST.add("^W[1-7]$");//周x
        PATTERN_LIST.add("^W[1-7]-W[1-7]$");//周x-周y
        PATTERN_LIST.add("^[A-Z]{2,10}$");//节假日

        PATTERN_LIST.add("^M[0-9]{1,2}$");//阳历x月
        PATTERN_LIST.add("^M[0-9]{1,2}/W[1-7]$");//阳历x月/周x
        PATTERN_LIST.add("^M[0-9]{1,2}/W[1-7]-W[1-7]$");//阳历x月/周x-周y
        PATTERN_LIST.add("^M[0-9]{1,2}-M[0-9]{1,2}$");//阳历x月-y月
        PATTERN_LIST.add("^M[0-9]{1,2}-M[0-9]{1,2}/W[1-7]$");//阳历x月-y月/周x
        PATTERN_LIST.add("^M[0-9]{1,2}-M[0-9]{1,2}/W[1-7]-W[1-7]$");//阳历x月-y月/周x-周y
        PATTERN_LIST.add("^[0-9]{1,2}/[0-9]{1,2}$");//阳历x/y
        PATTERN_LIST.add("^[0-9]{1,2}/[0-9]{1,2}-[0-9]{1,2}/[0-9]{1,2}$");//阳历x/y-x/y
        PATTERN_LIST.add("^20[0-9]{2}/[0-9]{1,2}/[0-9]{1,2}$");//阳历20xx/x/y
        PATTERN_LIST.add("^20[0-9]{2}/[0-9]{1,2}/[0-9]{1,2}-20[0-9]{2}/[0-9]{1,2}/[0-9]{1,2}$");//阳历20xx/x/y-阳历20xx/x/y
        PATTERN_LIST.add("^20[0-9]{2}/[0-9]{1,2}/[0-9]{1,2}-20[0-9]{2}/[0-9]{1,2}/[0-9]{1,2}/W[1-7]$");//阳历20xx/x/y-阳历20xx/x/y/周x
        PATTERN_LIST.add("^20[0-9]{2}/[0-9]{1,2}/[0-9]{1,2}-20[0-9]{2}/[0-9]{1,2}/[0-9]{1,2}/W[1-7]-W[1-7]$");//阳历20xx/x/y-阳历20xx/x/y/周x-周y
        PATTERN_LIST.add("^(\\d{4})\\/(\\d{1,2})\\/(\\d{1,2})-(\\d{4})\\/(\\d{1,2})\\/(\\d{1,2})\\/(W[1234567\\/\\-W]+)$");//阳历20xx/x/y-阳历20xx/x/y/周x-周y/周x/周x

        PATTERN_LIST.add("^C[0-9]{1,2}/[0-9]{1,2}$");//农历x/y
        PATTERN_LIST.add("^C[0-9]{1,2}/[0-9]{1,2}-C[0-9]{1,2}/[0-9]{1,2}$");//农历x/y
        PATTERN_MAP.put(PATTERN_WEEK,PATTERN_LIST.subList(0,2));
        PATTERN_MAP.put(PATTERN_HOLIDAY,PATTERN_LIST.subList(2,3));
        PATTERN_MAP.put(PATTERN_SOLAR,PATTERN_LIST.subList(3,16));
        PATTERN_MAP.put(PATTERN_LUNAR,PATTERN_LIST.subList(16,18));
    }

    @Autowired
    private SysHolidayConfigService sysHolidayConfigService;
    @Autowired
    private ShopProtocolMapper shopProtocolMapper;
    @Autowired
    private ShopItemMapper shopItemMapper;
    @Autowired
    private ShopMapper shopMapper;
    @Autowired
    private HotelService hotelService;
    @Autowired
    private CityMapper cityMapper;
    @Autowired
    private ProductGroupProductMapper productGroupProductMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ProductGroupMapper productGroupMapper;

    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private GroupProductBlockDateMapper groupProductBlockDateMapper;
    @Autowired
    private ProductGroupResourceMapper productGroupResourceMapper;



    @Override
    public List<BlockRule> blockRuleStr2list(String blockRule) {
        //先把字符串拆分成多个blockRule
        String blocks = blockRule.replaceAll("\\s","");
        final String[] blockRuleList = blocks.split(",");
        Set<String> blockSet = new HashSet<>(Arrays.asList(blockRuleList));
        List<SysHolidayConfig> holidayConfigList = Lists.newLinkedList();
        if (CollectionUtils.isEmpty(HOLIDAY_THREAD_LOCAL.get())){
            holidayConfigList = sysHolidayConfigService.getAllSysHolidayConfigList();
            HOLIDAY_THREAD_LOCAL.set(holidayConfigList);
        }else {
            holidayConfigList = HOLIDAY_THREAD_LOCAL.get();
        }
        Map<String,SysHolidayConfig> holidayMap=Maps.newHashMap();
        if (!CollectionUtils.isEmpty(holidayConfigList)){
            holidayConfigList.forEach(holiday->{
                holidayMap.put(holiday.getCode(),holiday);
            });
        }
        List<BlockRule> blockRules= Lists.newArrayList();
        for (String block : blockSet){
            BlockRule rule =new BlockRule();
            rule.setClose(Boolean.TRUE);
            rule.setRule(block);

            //先判断block是否为周
            if (WEEK_MAP.containsKey(block)){
                rule.setNatural(WEEK_MAP.get(block));
                blockRules.add(rule);
                continue;
            }
            //先判断block是否为节假日
            if (holidayMap.containsKey(block)){
                //把节假日解析成block
                rule.setNatural(holidayMap.get(block).getName());
                rule.setStart(holidayMap.get(block).getStart());
                rule.setEnd(holidayMap.get(block).getEnd());
                //节假日格式不定。type在此设置
                rule.setType(2);
                blockRules.add(rule);
                continue;
            }

            //复杂判断
            //特殊的时间段加上星期 yyyy/MM/dd-yyyy/MM/dd/Wx/Wx...
            String moreTimeWeek = "^(\\d{4})\\/(\\d{1,2})\\/(\\d{1,2})-(\\d{4})\\/(\\d{1,2})\\/(\\d{1,2})\\/(W[1234567\\/\\-W]+)$";
            if (Pattern.matches(moreTimeWeek,block)){
                String time = block.substring(0,block.indexOf("/W"));
                String week = block.substring(block.indexOf("/W")+1,block.length());
                week = week.replaceAll("/","、");
                block = time +"："+ week;
            }
            String natural=block;
            for (Map.Entry<String,String> entry:REPLACE_MAP.entrySet()){
                natural=natural.replaceAll(entry.getKey(),entry.getValue());
            }
            rule.setNatural(natural);
            blockRules.add(rule);
            /*
            //先按短横线(-)切割
            final String[] horLines = block.split("-");
            //这种情况应该不存在 简易的block在前面就会过滤掉
            if (horLines.length==1){

            }
            //分3种情况  1.日期x-日期y 2.周m-周n 3.日期x-日期y/周m
            else if (horLines.length==2){
                final boolean week = block.contains("W");
                //周m-周n
                if (week && block.length() == 5){
//                    rule.setNatural(WEEK_MAP.get(horLines[0])+"至"+WEEK_MAP.get(horLines[1]));
                    int num1= NumberUtils.toInt(horLines[0].substring(1));
                    int num2= NumberUtils.toInt(horLines[1].substring(1));
                    if (num1>num2){
                        rule.setNatural(String.join(",",WEEK_LIST.subList(num1-1,num2)));
                    }else {
                        StringBuffer ruleBuffer=new StringBuffer();
                        final List<String> subList = WEEK_LIST.subList(num2 - 1, 7);
                        subList.addAll(WEEK_LIST.subList(0,num1));
                        rule.setNatural(String.join(",",subList));
                    }
                    blockRules.add(rule);
                }

                else if (week && block.length() != 5){
                    final boolean week2 = horLines[1].contains("W");
                    if (week2){
//                        horLines[1]
                    }else {

                    }
                }
                else {
                    rule.setNatural(horLines[0]+"至"+horLines[1]);
                    blockRules.add(rule);
                }
            }
            else if (horLines.length==3){

            }
            //理论上不存在else
            else {

            }*/
        }
        typeAndSort(blockRules);

        return blockRules;
    }

    /**
     * 对列表进行排序
     * 1.按type升序排
     * 2.按字母升序排
     * @param blockRules
     */
    private void typeAndSort(List<BlockRule> blockRules) {
        setTypeInfo(blockRules);
        sortRuleList(blockRules);
    }

    private void sortRuleList(List<BlockRule> blockRules) {
        blockRules.sort((rule1, rule2) -> {
            Assert.notNull(rule1,"参数1 不能为null");
            Assert.notNull(rule2,"参数2 不能为null");
            Assert.notNull(rule1.getType(),"参数1 type不能为null"+rule1.getRule());
            Assert.notNull(rule2.getType(),"参数2 type不能为null");
            Assert.notNull(rule1.getRule(),"参数1 rule不能为null");
            Assert.notNull(rule2.getRule(),"参数2 rule不能为null");
            return rule1.getType().compareTo(rule2.getType())==0?rule1.getRule().compareTo(rule2.getRule()):rule1.getType().compareTo(rule2.getType());
        });
    }

    /**
     * 设置type信息
     * @param blockRules
     */
    private void setTypeInfo(List<BlockRule> blockRules) {
        for (BlockRule blockRule:blockRules){
            for (Map.Entry<String,List<String>> entry : PATTERN_MAP.entrySet()){
                final List<String> patternList = entry.getValue();
                for (String pattern:patternList){
                    if (blockRule.getType() == null){
                        if (Pattern.matches(pattern,blockRule.getRule())){
                            if (entry.getKey().equalsIgnoreCase(PATTERN_WEEK)){
                                blockRule.setType(1);
                            }
                            else if (entry.getKey().equalsIgnoreCase(PATTERN_HOLIDAY)){
                                blockRule.setType(2);
                            }
                            else if (entry.getKey().equalsIgnoreCase(PATTERN_LUNAR)){
                                blockRule.setType(3);
                            }
                            else if (entry.getKey().equalsIgnoreCase(PATTERN_SOLAR)){
                                blockRule.setType(4);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public String blockRuleList2str(List<BlockRule> blockRuleList) {
        //把传入的blockrule集合进行合并 然后转成字符串格式
        if(CollectionUtils.isEmpty(blockRuleList)){
            return null;
        }
        //先设置type信息
        setTypeInfo(blockRuleList);


        Set<String> weekSet= Sets.newHashSet();
        Set<String> holidaySet= Sets.newHashSet();
        Set<String> lunarSet= Sets.newHashSet();
        Set<String> solarSet= Sets.newHashSet();
        //按顺序进行合并
        for (BlockRule blockRule:blockRuleList){
            if (blockRule.getType().compareTo(1)==0){
                resolveWeek(weekSet, blockRule.getRule(),blockRule.getClose().booleanValue());
                continue;
            }
            if (blockRule.getType().compareTo(2)==0){
                resolveHoliday(holidaySet,blockRule.getRule(),blockRule.getClose().booleanValue());
                continue;
            }
            if (blockRule.getType().compareTo(3)==0){
                resolveLunar(lunarSet,blockRule.getRule(),blockRule.getClose().booleanValue());
                continue;
            }
            if (blockRule.getType().compareTo(4)==0){
                resolveSolar(solarSet,blockRule.getRule(),blockRule.getClose().booleanValue());
                continue;
            }
        }
        //合并完成后，进行排序
        List<String> weekList=Lists.newArrayList(weekSet);
        List<String> holidayList=Lists.newArrayList(holidaySet);
        List<String> lunarList=Lists.newArrayList(lunarSet);
        List<String> solarList=Lists.newArrayList(solarSet);
        Collections.sort(weekList);
        Collections.sort(holidayList);
        Collections.sort(lunarList);
        Collections.sort(lunarList);
        List<String> result=Lists.newArrayList();
        result.addAll(weekList);
        result.addAll(holidayList);
        result.addAll(lunarList);
        result.addAll(solarList);
        return String.join(", ",result);
    }

    /**
     * 检查俩个时间是否是整整一个月
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public Boolean checkAllMonth(String startTime, String endTime) {
        String startYear = startTime.substring(0,4);
        String startMonth = startTime.substring(5,7);
        String startDay = startTime.substring(8,10);
        String endYear = endTime.substring(0,4);
        String endMonth = endTime.substring(5,7);
        String endDay = endTime.substring(8,10);
        if (startYear.equals(endYear)){
            if (startMonth.equals(endMonth)){
                if (startDay.equals("01")){
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(DateUtil.parse(endTime,"yyyy/MM/dd"));
                    calendar.add(Calendar.DAY_OF_MONTH,1);
                    endTime = DateUtil.format(calendar.getTime(),"yyyy/MM/dd");
                    if (endTime.substring(8,10).equals("01")){
                        return true;
                    }
                }

            }
        }
        return false;
    }

    /**
     * 编辑block规则，解析成前端展示对象
     * @param blockRule
     * @return
     */
    @Override
    public GenerateBlockRuleVo editBlockRule(BlockRule blockRule) {
        GenerateBlockRuleVo generateBlockRuleVo = new GenerateBlockRuleVo();
        //单独的周 正则
        String singleWeek = "^W[1-7]$";
        //连续的周 正则
        String manyWeek = "^W[1-7]-W[1-7]$";
        //单独的日期 正则  yyyy/MM/dd
        String singleTime = "^(\\d{4})\\/(\\d{1,2})\\/(\\d{1,2})$";
        //连续的日期 正则 yyyy/MM/dd-yyyy/MM/dd
        String manyTime = "^(\\d{4})\\/(\\d{1,2})\\/(\\d{1,2})-(\\d{4})\\/(\\d{1,2})\\/(\\d{1,2})$";
        //单独的月份 正则 Mx
        String singleMonth = "^M[0-9]{1,2}$";
        //连续的日期加上星期 正则 yyyy/MM/dd-yyyy/MM/dd/Wx/Wx...
        String manyTimeWeek = "^(\\d{4})\\/(\\d{1,2})\\/(\\d{1,2})-(\\d{4})\\/(\\d{1,2})\\/(\\d{1,2})\\/(W[1234567\\/\\-W]+)$";
        //单独的月份加上星期 Mx/Wx
        String singleMonthWeek = "^M[0-9]{1,2}/W[1-7]$";
        //单独的日期 MM/dd
        String singleMonthTime = "^(\\d{1,2})\\/(\\d{1,2})$";
        //连续的日期 MM/dd-MM/dd
        String manyMonthTime = "^(\\d{1,2})\\/(\\d{1,2})-(\\d{1,2})\\/(\\d{1,2})$";
        //农历日期 CM/d
        String singleLunar = "^C[0-9]{1,2}/[0-9]{1,2}$";
        //农历日期范围 CM/d-CM/d
        String manyLunar = "^C[0-9]{1,2}/[0-9]{1,2}-C[0-9]{1,2}/[0-9]{1,2}$";

        //第一种情况，单独的星期Wx
        if (Pattern.matches(singleWeek,blockRule.getRule())){
            String week = blockRule.getRule().substring(1,2);
            String[] arrayWeek = {week};
            generateBlockRuleVo.setCalendar(BlockRuleEnums.Calendar.SOLAR_CALENDAR.getCode());
            generateBlockRuleVo.setRepeat(BlockRuleEnums.RepeatEnum.NO_REPETITION.getCode());
            generateBlockRuleVo.setContainWeek(arrayWeek);
        }

        //第二种情况，连续的周区间Wx-Wx
        if (Pattern.matches(manyWeek,blockRule.getRule())){
            List<String> stringList = Lists.newLinkedList();
            int num1 = NumberUtils.toInt(blockRule.getRule().substring(1,2));
            int num2 = NumberUtils.toInt(blockRule.getRule().substring(4,5));
            if (num1>num2){
                for (int i = num1;i <= 7;i++){
                    stringList.add(i+"");
                }
                for (int i = 1;i<= num2;i++){
                    stringList.add(i+"");
                }
            }else {
                for (int i = num1;i<=num2;i++){
                    stringList.add(i+"");
                }
            }
            String[] arrayWeek = new String[stringList.size()];
            generateBlockRuleVo.setCalendar(BlockRuleEnums.Calendar.SOLAR_CALENDAR.getCode());
            generateBlockRuleVo.setRepeat(BlockRuleEnums.RepeatEnum.NO_REPETITION.getCode());
            generateBlockRuleVo.setContainWeek(stringList.toArray(arrayWeek));
        }

        //第三种情况，单独的日期 yyyy/MM/dd
        if (Pattern.matches(singleTime,blockRule.getRule())){
            Date date = DateUtil.parse(blockRule.getRule(),"yyyy/MM/dd");
            Date[] dates = {date,date};
            generateBlockRuleVo.setCalendar(BlockRuleEnums.Calendar.SOLAR_CALENDAR.getCode());
            generateBlockRuleVo.setRepeat(BlockRuleEnums.RepeatEnum.NO_REPETITION.getCode());
            generateBlockRuleVo.setBlockTime(dates);
        }

        //第四种情况，连续的日期 yyyy/MM/dd-yyyy/MM/dd
        if (Pattern.matches(manyTime,blockRule.getRule())){
            String[] dateString = blockRule.getRule().split("-");
            Date startDate = DateUtil.parse(dateString[0],"yyyy/MM/dd");
            Date endDate = DateUtil.parse(dateString[1],"yyyy/MM/dd");
            Date[] dates = {startDate,endDate};
            generateBlockRuleVo.setCalendar(BlockRuleEnums.Calendar.SOLAR_CALENDAR.getCode());
            generateBlockRuleVo.setRepeat(BlockRuleEnums.RepeatEnum.NO_REPETITION.getCode());
            generateBlockRuleVo.setBlockTime(dates);
        }

        //第五种情况，单独的月份 Mx
        if (Pattern.matches(singleMonth,blockRule.getRule())){
            String months = blockRule.getRule().replaceAll("M","");
            Calendar calendar = Calendar.getInstance();

            int month = Integer.valueOf(months);
            int year = calendar.get(Calendar.YEAR);

            String startTime = year +"/"+ month + "/1";
            String nextMinTime = month == 12 ? (year+1) +"/1/1" : year +"/"+(month+1)+"/1";

            calendar.setTime(DateUtil.parse(nextMinTime,"yyyy/MM/dd"));
            calendar.add(Calendar.DAY_OF_MONTH,-1);
            String endTime = DateUtil.format(calendar.getTime(),"yyyy/MM/dd");

            Date start = DateUtil.parse(startTime,"yyyy/MM/dd");
            Date end = DateUtil.parse(endTime,"yyyy/MM/dd");
            Date[] dates = {start,end};

            generateBlockRuleVo.setCalendar(BlockRuleEnums.Calendar.SOLAR_CALENDAR.getCode());
            generateBlockRuleVo.setRepeat(BlockRuleEnums.RepeatEnum.NO_REPETITION.getCode());
            generateBlockRuleVo.setBlockTime(dates);
        }

        //第六种情况，连续的日期加上星期 yyyy/MM/dd-yyyy/MM/dd/Wx/Wx-Wx/Wx...
        if (Pattern.matches(manyTimeWeek,blockRule.getRule())){
            String time = blockRule.getRule().substring(0,blockRule.getRule().indexOf("/W"));
            String week = blockRule.getRule().substring(blockRule.getRule().indexOf("/W"),blockRule.getRule().length());
            String[] strings = time.split("-");
            Date startTime = DateUtil.parse(strings[0],"yyyy/MM/dd");
            Date endTime = DateUtil.parse(strings[1],"yyyy/MM/dd");
            Date[] dates = {startTime,endTime};
            List<String> weekList = Lists.newLinkedList();
            String[] weekStr = week.substring(1,week.length()).split("/");
            if (ArrayUtils.isNotEmpty(weekStr)){
                for (String s : weekStr) {
                    if (s.indexOf("-") >= 0){
                        int num1 = NumberUtils.toInt(s.substring(1,2));
                        int num2 = NumberUtils.toInt(s.substring(4,5));
                        if (num1>num2){
                            for (int i = num1;i <= 7;i++){
                                weekList.add(i+"");
                            }
                            for (int i = 1;i<= num2;i++){
                                weekList.add(i+"");
                            }
                        }else {
                            for (int i = num1;i<=num2;i++){
                                weekList.add(i+"");
                            }
                        }
                    }else {
                        weekList.add(s.substring(1));
                    }
                }
            }
            String[] weeks = new String[weekList.size()];
            weekList.toArray(weeks);
            generateBlockRuleVo.setCalendar(BlockRuleEnums.Calendar.SOLAR_CALENDAR.getCode());
            generateBlockRuleVo.setRepeat(BlockRuleEnums.RepeatEnum.NO_REPETITION.getCode());
            generateBlockRuleVo.setBlockTime(dates);
            generateBlockRuleVo.setContainWeek(weeks);
        }

        //第七种情况，单独的月份加上星期 Mx/Wx
        if (Pattern.matches(singleMonthWeek,blockRule.getRule())){
            String[] mw = blockRule.getRule().split("/");
            String mStr = mw[0];
            String wStr = mw[1];
            String months = mStr.replaceAll("M","");
            Calendar calendar = Calendar.getInstance();

            int month = Integer.valueOf(months);
            int year = calendar.get(Calendar.YEAR);

            String startTime = year +"/"+ month + "/1";
            String nextMinTime = month == 12 ? (year+1) +"/1/1" : year +"/"+(month+1)+"/1";

            calendar.setTime(DateUtil.parse(nextMinTime,"yyyy/MM/dd"));
            calendar.add(Calendar.DAY_OF_MONTH,-1);
            String endTime = DateUtil.format(calendar.getTime(),"yyyy/MM/dd");

            Date start = DateUtil.parse(startTime,"yyyy/MM/dd");
            Date end = DateUtil.parse(endTime,"yyyy/MM/dd");
            Date[] dates = {start,end};
            String[] weeks = {wStr.substring(1,2)};

            generateBlockRuleVo.setCalendar(BlockRuleEnums.Calendar.SOLAR_CALENDAR.getCode());
            generateBlockRuleVo.setRepeat(BlockRuleEnums.RepeatEnum.NO_REPETITION.getCode());
            generateBlockRuleVo.setBlockTime(dates);
            generateBlockRuleVo.setContainWeek(weeks);
        }

        //第八种情况，单独的日期  MM/dd
        if (Pattern.matches(singleMonthTime,blockRule.getRule())){
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            Date date = DateUtil.parse(year+"/"+blockRule.getRule(),"yyyy/MM/dd");
            Date[] dates = {date,date};

            generateBlockRuleVo.setCalendar(BlockRuleEnums.Calendar.SOLAR_CALENDAR.getCode());
            generateBlockRuleVo.setRepeat(BlockRuleEnums.RepeatEnum.REPEAT.getCode());
            generateBlockRuleVo.setBlockTime(dates);
        }

        //第九种情况，连续的日期段  MM/dd-MM/dd
        if (Pattern.matches(manyMonthTime,blockRule.getRule())){
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            String[] times = blockRule.getRule().split("-");
            Date startTime = DateUtil.parse(year+"/"+times[0],"yyyy/MM/dd");
            Date endTime = DateUtil.parse(year+"/"+times[1],"yyyy/MM/dd");
            Date[] dates = {startTime,endTime};

            generateBlockRuleVo.setCalendar(BlockRuleEnums.Calendar.SOLAR_CALENDAR.getCode());
            generateBlockRuleVo.setRepeat(BlockRuleEnums.RepeatEnum.REPEAT.getCode());
            generateBlockRuleVo.setBlockTime(dates);
        }

        //第十种情况，单独的农历  CM/d
        if (Pattern.matches(singleLunar,blockRule.getRule())){
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            String time = blockRule.getRule().substring(1);
            Date dateTime = DateUtil.parse(year+"/"+time,"yyyy/MM/dd");
            Date[] dates = {dateTime,dateTime};

            generateBlockRuleVo.setCalendar(BlockRuleEnums.Calendar.LUNAR_CALENDAR.getCode());
            generateBlockRuleVo.setBlockTime(dates);
        }

        //第十一种情况，连续的农历 CM/d-CM/d
        if (Pattern.matches(manyLunar,blockRule.getRule())){
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            String[] times = blockRule.getRule().split("-");
            String start = times[0].substring(1);
            String end = times[1].substring(1);
            Date startTime = DateUtil.parse(year+"/"+start,"yyyy/MM/dd");
            Date endTime = DateUtil.parse(year+"/"+end,"yyyy/MM/dd");
            Date[] dates = {startTime,endTime};

            generateBlockRuleVo.setCalendar(BlockRuleEnums.Calendar.LUNAR_CALENDAR.getCode());
            generateBlockRuleVo.setBlockTime(dates);
        }
        return generateBlockRuleVo;
    }

    /**
     * 前段对象 生成block规则
     * @param generateBlockRuleVo
     * @return
     */
    @Override
    public List<BlockRule> generateBlockRule(GenerateBlockRuleVo generateBlockRuleVo) {
        List<BlockRule> blockRuleList = Lists.newLinkedList();
        //起止时间string
        String startTime = null,endTime = null;
        //起止时间状态
        boolean timeFlag = ArrayUtils.isNotEmpty(generateBlockRuleVo.getBlockTime());
        boolean weekFlag = ArrayUtils.isNotEmpty(generateBlockRuleVo.getContainWeek());

        //起止时间是否是整整一个月
        Boolean allMonthFlag = null;
        if (timeFlag && null != generateBlockRuleVo.getBlockTime()[0] ){
            startTime = DateUtil.format(generateBlockRuleVo.getBlockTime()[0],"yyyy/MM/dd");
            endTime = DateUtil.format(generateBlockRuleVo.getBlockTime()[1],"yyyy/MM/dd");
            if (!startTime.equals(endTime)){
                allMonthFlag = checkAllMonth(startTime,endTime);
            }
        }

        //阳历
        if (BlockRuleEnums.Calendar.SOLAR_CALENDAR.getCode().compareTo(generateBlockRuleVo.getCalendar()) == 0){
            //第一种情况，只选星期
            if (!timeFlag && weekFlag){
                for (String s : generateBlockRuleVo.getContainWeek()) {
                    BlockRule blockRule = new BlockRule();
                    blockRule.setRule("W"+s);
                    blockRule.setNatural(WeekEnums.getNameByCode(s));
                    blockRule.setClose(true);
                    blockRule.setReason(generateBlockRuleVo.getReason());
                    blockRuleList.add(blockRule);
                }
            }

            //只选起止日期
            if (timeFlag && !weekFlag && generateBlockRuleVo.getRepeat().compareTo(BlockRuleEnums.RepeatEnum.NO_REPETITION.getCode()) == 0){
                //第二种情况，只选起止日期 并且起止时间相同
                if (startTime.equals(endTime)){
                    BlockRule blockRule = new BlockRule();
                    blockRule.setRule(startTime);
                    blockRule.setNatural(blockRule.getRule());
                    blockRule.setClose(true);
                    blockRule.setReason(generateBlockRuleVo.getReason());
                    blockRuleList.add(blockRule);
                }

                //第三种情况，只选起止日期 同时起止时间不相同 跨度任意
                if (!startTime.equals(endTime)){
                    BlockRule blockRule = new BlockRule();
                    blockRule.setRule(startTime+"-"+endTime);
                    blockRule.setNatural(blockRule.getRule());
                    blockRule.setClose(true);
                    blockRule.setReason(generateBlockRuleVo.getReason());
                    blockRuleList.add(blockRule);
                }
            }

            //选择起止日期和星期
            if (timeFlag && weekFlag && generateBlockRuleVo.getRepeat().compareTo(BlockRuleEnums.RepeatEnum.NO_REPETITION.getCode()) == 0){
                //第四种情况，起止日期相同
                if (startTime.equals(endTime)){
                    BlockRule blockRule = new BlockRule();
                    blockRule.setRule(startTime);
                    blockRule.setNatural(blockRule.getRule());
                    blockRule.setClose(true);
                    blockRule.setReason(generateBlockRuleVo.getReason());
                    blockRuleList.add(blockRule);
                }

                //第五种情况，起止日期不同 跨度任意
                if (!startTime.equals(endTime)){
                    String block = startTime+"-"+endTime;
                    for (String s : generateBlockRuleVo.getContainWeek()) {
                        block = block + "/W"+s;
                    }
                    BlockRule blockRule = new BlockRule();
                    //星期全选则为全部block
                    if (generateBlockRuleVo.getContainWeek().length == 7){
                        blockRule.setRule(startTime+"-"+endTime);
                        blockRule.setNatural(blockRule.getRule());
                        blockRule.setClose(true);
                        blockRule.setReason(generateBlockRuleVo.getReason());
                    }else {
                        blockRule.setRule(block);
                        blockRule.setNatural(blockRuleStr2list(blockRule.getRule()).get(0).getNatural());
                        blockRule.setClose(true);
                        blockRule.setReason(generateBlockRuleVo.getReason());
                    }
                    blockRuleList.add(blockRule);
                }
            }

            //选择起止日期和每年重复
            if (timeFlag && !weekFlag && generateBlockRuleVo.getRepeat().compareTo(BlockRuleEnums.RepeatEnum.REPEAT.getCode()) == 0){
                //第六种情况，起止日期相同
                if (startTime.equals(endTime)){
                    BlockRule blockRule = new BlockRule();
                    blockRule.setRule(startTime.substring(5,10));
                    blockRule.setNatural(blockRule.getRule());
                    blockRule.setClose(true);
                    blockRule.setReason(generateBlockRuleVo.getReason());
                    blockRuleList.add(blockRule);
                }

                //第七种情况，起止日期不同 并且跨度不是整月
                if (!startTime.equals(endTime) && !allMonthFlag){
                    BlockRule blockRule = new BlockRule();
                    blockRule.setRule(startTime.substring(5,10)+"-"+endTime.substring(5,10));
                    blockRule.setNatural(blockRule.getRule());
                    blockRule.setClose(true);
                    blockRule.setReason(generateBlockRuleVo.getReason());
                    blockRuleList.add(blockRule);
                }

                //第八种情况，起止日期不同 并且跨度为整月
                if (!startTime.equals(endTime) && allMonthFlag){
                    BlockRule blockRule = new BlockRule();
                    blockRule.setRule("M"+startTime.substring(5,7));
                    blockRule.setNatural(blockRuleStr2list(blockRule.getRule()).get(0).getNatural());
                    blockRule.setClose(true);
                    blockRule.setReason(generateBlockRuleVo.getReason());
                    blockRuleList.add(blockRule);
                }
            }

            //选择起止日期和星期和每年重复
            if (timeFlag && weekFlag && generateBlockRuleVo.getRepeat().compareTo(BlockRuleEnums.RepeatEnum.REPEAT.getCode()) == 0){
                //第九种情况，起止日期相同
                if (startTime.equals(endTime)){
                    BlockRule blockRule = new BlockRule();
                    blockRule.setRule(startTime.substring(5,10));
                    blockRule.setNatural(blockRule.getRule());
                    blockRule.setClose(true);
                    blockRule.setReason(generateBlockRuleVo.getReason());
                    blockRuleList.add(blockRule);
                }

                //第十种情况，起止日期不同 并且跨度不为整月
                if (!startTime.equals(endTime) && !allMonthFlag){
                    BlockRule blockRule = new BlockRule();
                    blockRule.setRule(startTime.substring(5,10)+"-"+endTime.substring(5,10));
                    blockRule.setNatural(blockRule.getRule());
                    blockRule.setClose(true);
                    blockRule.setReason(generateBlockRuleVo.getReason());
                    blockRuleList.add(blockRule);
                }

                //第十一种情况，起止日期不同 并且跨度为整月
                if (!startTime.equals(endTime) && allMonthFlag){
                    for (String s : generateBlockRuleVo.getContainWeek()) {
                        BlockRule blockRule = new BlockRule();
                        blockRule.setRule("M"+startTime.substring(5,7)+"/W"+s);
                        blockRule.setNatural(blockRuleStr2list(blockRule.getRule()).get(0).getNatural());
                        blockRule.setClose(true);
                        blockRule.setReason(generateBlockRuleVo.getReason());
                        blockRuleList.add(blockRule);
                    }
                }
            }
        }

        //阴历
        if (BlockRuleEnums.Calendar.LUNAR_CALENDAR.getCode().compareTo(generateBlockRuleVo.getCalendar()) == 0){
            //第十二种情况，起止日期相同
            if (startTime.equals(endTime)){
                String rule = startTime.substring(startTime.indexOf("/")+1);
                BlockRule blockRule = new BlockRule();
                blockRule.setRule("C"+rule);
                blockRule.setNatural(blockRuleStr2list(blockRule.getRule()).get(0).getNatural());
                blockRule.setClose(true);
                blockRule.setReason(generateBlockRuleVo.getReason());
                blockRuleList.add(blockRule);
            }

            //第十三种情况，起止日期不同
            if (!startTime.equals(endTime)){
                String rule1 = startTime.substring(startTime.indexOf("/")+1);
                String rule2 = endTime.substring(endTime.indexOf("/")+1);
                BlockRule blockRule = new BlockRule();
                blockRule.setRule("C"+rule1+"-C"+rule2);
                blockRule.setNatural(blockRuleStr2list(blockRule.getRule()).get(0).getNatural());
                blockRule.setClose(true);
                blockRule.setReason(generateBlockRuleVo.getReason());
                blockRuleList.add(blockRule);
            }
        }
        return blockRuleList;
    }

    /**
     * 根据时间段和block规则获取具体的可预约日期
     * @param startDate
     * @param endDate
     * @param blockRule
     * @return
     */
    @Override
    public List<Date> generateBookDate(Date startDate,Date endDate,String blockRule){
        List<Date> dates;
        if (StringUtils.isBlank(blockRule)){
            dates = DateUtils.containDateList(startDate,endDate,null);
        }else {
            //计算区间范围内的block日期列表
            HashSet<Date> blockDate = generateBlockDate(startDate,endDate,blockRule);
            dates = DateUtils.containDateList(startDate,endDate,blockDate);
        }
        return dates;
    }

    /**
     * 根据时间段和block规则获取具体的block日期
     * @param startDate
     * @param endDate
     * @param blockRule
     * @return
     */
    @Override
    public HashSet<Date> generateBlockDate(Date startDate,Date endDate,String blockRule){
        HashSet<Date> result = new HashSet();
        //单独的周 正则
        String singleWeek = "^W[1-7]$";
        //连续的周 正则
        String manyWeek = "^W[1-7]-W[1-7]$";
        //单独的日期 正则  yyyy/MM/dd
        String singleTime = "^(\\d{4})\\/(\\d{1,2})\\/(\\d{1,2})$";
        //连续的日期 正则 yyyy/MM/dd-yyyy/MM/dd
        String manyTime = "^(\\d{4})\\/(\\d{1,2})\\/(\\d{1,2})-(\\d{4})\\/(\\d{1,2})\\/(\\d{1,2})$";
        //单独的月份 正则 Mx
        String singleMonth = "^M[0-9]{1,2}$";
        //连续的日期加上星期 正则 yyyy/MM/dd-yyyy/MM/dd/Wx/Wx...
        String manyTimeWeek = "^(\\d{4})\\/(\\d{1,2})\\/(\\d{1,2})-(\\d{4})\\/(\\d{1,2})\\/(\\d{1,2})\\/(W[1234567\\/\\-W]+)$";
        //单独的月份加上星期 Mx/Wx
        String singleMonthWeek = "^M[0-9]{1,2}/W[1-7]$";
        //单独的日期 MM/dd
        String singleMonthTime = "^(\\d{1,2})\\/(\\d{1,2})$";
        //连续的日期 MM/dd-MM/dd
        String manyMonthTime = "^(\\d{1,2})\\/(\\d{1,2})-(\\d{1,2})\\/(\\d{1,2})$";
        //农历日期 CM/d
        String singleLunar = "^C[0-9]{1,2}/[0-9]{1,2}$";
        //农历日期范围 CM/d-CM/d
        String manyLunar = "^C[0-9]{1,2}/[0-9]{1,2}-C[0-9]{1,2}/[0-9]{1,2}$";

        //先把字符串拆分成多个blockRule
        blockRule = blockRule.replaceAll("\\s","");
        final String[] blockRuleList = blockRule.split(",");
        for (String block : blockRuleList) {
            //第一种情况，单独的星期Wx
            if (Pattern.matches(singleWeek,block)){
                Integer week = Integer.valueOf(block.substring(1,2));
                if (week.compareTo(7) == 0){
                    week = 1;
                }else {
                    week = week + 1;
                }
                List<Date> dates = DateUtils.containDateByWeeks(startDate,endDate,week.toString());
                result.addAll(dates);
            }

            //第二种情况，连续的周区间Wx-Wx
            if (Pattern.matches(manyWeek,block)){
                List<Integer> intWeek = Lists.newLinkedList();
                int num1 = NumberUtils.toInt(block.substring(1,2));
                int num2 = NumberUtils.toInt(block.substring(4,5));
                if (num1>num2){
                    for (int i = num1;i <= 7;i++){
                        intWeek.add(i);
                    }
                    for (int i = 1;i<= num2;i++){
                        intWeek.add(i);
                    }
                }else {
                    for (int i = num1;i<=num2;i++){
                        intWeek.add(i);
                    }
                }
                for (int i = 0; i<intWeek.size(); i++){
                    if (intWeek.get(i).compareTo(7) == 0){
                        intWeek.set(i,1);
                    }else {
                        intWeek.set(i,intWeek.get(i)+1);
                    }
                }
                List<Date> dates = DateUtils.containDateByWeeks(startDate,endDate,StringUtils.join(intWeek,""));
                result.addAll(dates);
            }

            //第三种情况，单独的日期 yyyy/MM/dd
            if (Pattern.matches(singleTime,block)){
                Date date = DateUtil.parse(block,"yyyy/MM/dd");
                List<Date> blockDates = Lists.newLinkedList();
                blockDates.add(date);
                //获取可预约范围内的block列表
                List<Date> bookDates = DateUtils.containDateList(startDate,endDate,null);
                List<Date> tempDates = Lists.newLinkedList(bookDates);
                tempDates.removeAll(blockDates);
                bookDates.removeAll(tempDates);
                result.addAll(bookDates);
            }

            //第四种情况，连续的日期 yyyy/MM/dd-yyyy/MM/dd
            if (Pattern.matches(manyTime,block)){
                String[] dateString = block.split("-");
                Date from = DateUtil.parse(dateString[0],"yyyy/MM/dd");
                Date end = DateUtil.parse(dateString[1],"yyyy/MM/dd");
                //获取可预约范围内的block列表
                List<Date> bookDates = DateUtils.containDateList(startDate,endDate,null);
                List<Date> blockDates = DateUtils.containDateList(from,end,null);
                List<Date> tempDates = Lists.newLinkedList(bookDates);
                tempDates.removeAll(blockDates);
                bookDates.removeAll(tempDates);
                result.addAll(bookDates);
            }

            //第五种情况，单独的月份 Mx
            if (Pattern.matches(singleMonth,block)){
                //获取月份
                String months = block.replaceAll("M","");
                int month = Integer.valueOf(months);

                //获取年份
                List<Integer> years = Lists.newLinkedList();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(startDate);
                int year1 = calendar.get(Calendar.YEAR);
                calendar.setTime(endDate);
                int year2 = calendar.get(Calendar.YEAR);
                for ( ; year1 <= year2; year1++ ){
                    years.add(year1);
                }

                //获取特定年份的block日期
                List<Date> blockDates = Lists.newLinkedList();
                for (Integer year : years) {
                    String startTime = year +"/"+ month + "/1";
                    Date fromDate = DateUtil.parse(startTime,"yyyy/MM/dd");
                    String nextMinTime = month == 12 ? (year+1) +"/1/1" : year +"/"+(month+1)+"/1";
                    calendar.setTime(DateUtil.parse(nextMinTime,"yyyy/MM/dd"));
                    calendar.add(Calendar.DAY_OF_MONTH,-1);
                    Date endTime = calendar.getTime();
                    blockDates.addAll(DateUtils.containDateList(fromDate,endTime,null));
                }
                //获取可预约范围内的block列表
                List<Date> bookDates = DateUtils.containDateList(startDate,endDate,null);
                List<Date> tempDates = Lists.newLinkedList(bookDates);
                tempDates.removeAll(blockDates);
                bookDates.removeAll(tempDates);
                result.addAll(bookDates);
            }

            //第六种情况，连续的日期加上星期 yyyy/MM/dd-yyyy/MM/dd/Wx/Wx-Wx/Wx...
            if (Pattern.matches(manyTimeWeek,block)){
                String time = block.substring(0,block.indexOf("/W"));
                String week = block.substring(block.indexOf("/W"),block.length());
                String[] strings = time.split("-");
                Date startTime = DateUtil.parse(strings[0],"yyyy/MM/dd");
                Date endTime = DateUtil.parse(strings[1],"yyyy/MM/dd");

                List<String> weekList = Lists.newLinkedList();
                String[] weekStr = week.substring(1,week.length()).split("/");
                if (ArrayUtils.isNotEmpty(weekStr)){
                    for (String s : weekStr) {
                        if (s.indexOf("-") >= 0){
                            int num1 = NumberUtils.toInt(s.substring(1,2));
                            int num2 = NumberUtils.toInt(s.substring(4,5));
                            if (num1>num2){
                                for (int i = num1;i <= 7;i++){
                                    weekList.add(i+"");
                                }
                                for (int i = 1;i<= num2;i++){
                                    weekList.add(i+"");
                                }
                            }else {
                                for (int i = num1;i<=num2;i++){
                                    weekList.add(i+"");
                                }
                            }
                        }else {
                            weekList.add(s.substring(1));
                        }
                    }
                }
                String[] weeks = new String[weekList.size()];
                weekList.toArray(weeks);

                List<Integer> intWeek = Lists.newLinkedList();
                for (String s : weeks) {
                    intWeek.add(Integer.valueOf(s));
                }
                for (int i = 0; i<intWeek.size(); i++){
                    if (intWeek.get(i).compareTo(7) == 0){
                        intWeek.set(i,1);
                    }else {
                        intWeek.set(i,intWeek.get(i)+1);
                    }
                }
                List<Date> blockDates = DateUtils.containDateByWeeks(startTime,endTime,StringUtils.join(intWeek,""));
                //获取可预约范围内的block列表
                List<Date> bookDates = DateUtils.containDateList(startDate,endDate,null);
                List<Date> tempDates = Lists.newLinkedList(bookDates);
                tempDates.removeAll(blockDates);
                bookDates.removeAll(tempDates);
                result.addAll(bookDates);
            }

            //第七种情况，单独的月份加上星期 Mx/Wx
            if (Pattern.matches(singleMonthWeek,block)){
                String[] mw = block.split("/");
                String mStr = mw[0];
                String wStr = mw[1];
                //获取月份
                String months = mStr.replaceAll("M","");
                int month = Integer.valueOf(months);
                //获取年份
                List<Integer> years = Lists.newLinkedList();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(startDate);
                int year1 = calendar.get(Calendar.YEAR);
                calendar.setTime(endDate);
                int year2 = calendar.get(Calendar.YEAR);
                for ( ; year1 <= year2; year1++ ){
                    years.add(year1);
                }
                //获取星期系数
                Integer week = Integer.valueOf(wStr.substring(1,2));
                if (week.compareTo(7) == 0){
                    week = 1;
                }else {
                    week = week + 1;
                }

                //获取特定年份特定星期的block日期
                List<Date> blockDates = Lists.newLinkedList();
                for (Integer year : years) {
                    String startTime = year +"/"+ month + "/1";
                    Date fromDate = DateUtil.parse(startTime,"yyyy/MM/dd");
                    String nextMinTime = month == 12 ? (year+1) +"/1/1" : year +"/"+(month+1)+"/1";
                    calendar.setTime(DateUtil.parse(nextMinTime,"yyyy/MM/dd"));
                    calendar.add(Calendar.DAY_OF_MONTH,-1);
                    Date endTime = calendar.getTime();
                    blockDates.addAll(DateUtils.containDateByWeeks(fromDate,endTime,week.toString()));
                }
                //获取可预约范围内的block列表
                List<Date> bookDates = DateUtils.containDateList(startDate,endDate,null);
                List<Date> tempDates = Lists.newLinkedList(bookDates);
                tempDates.removeAll(blockDates);
                bookDates.removeAll(tempDates);
                result.addAll(bookDates);
            }

            //第八种情况，单独的日期  MM/dd
            if (Pattern.matches(singleMonthTime,block)){
                //获取年份
                List<Integer> years = Lists.newLinkedList();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(startDate);
                int year1 = calendar.get(Calendar.YEAR);
                calendar.setTime(endDate);
                int year2 = calendar.get(Calendar.YEAR);
                for ( ; year1 <= year2; year1++ ){
                    years.add(year1);
                }
                //获取年区间内block日期
                List<Date> blockDates = Lists.newLinkedList();
                for (Integer year : years) {
                    String dateStr = year + "/" +block;
                    Date date = DateUtil.parse(dateStr,"yyyy/MM/dd");
                    blockDates.add(date);
                }
                //获取可预约范围内的block列表
                List<Date> bookDates = DateUtils.containDateList(startDate,endDate,null);
                List<Date> tempDates = Lists.newLinkedList(bookDates);
                tempDates.removeAll(blockDates);
                bookDates.removeAll(tempDates);
                result.addAll(bookDates);
            }

            //第九种情况，连续的日期段  MM/dd-MM/dd
            if (Pattern.matches(manyMonthTime,block)){
                //获取年份
                List<Integer> years = Lists.newLinkedList();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(startDate);
                int year1 = calendar.get(Calendar.YEAR);
                calendar.setTime(endDate);
                int year2 = calendar.get(Calendar.YEAR);
                for ( ; year1 <= year2; year1++ ){
                    years.add(year1);
                }
                //获取月份
                String[] times = block.split("-");
                int monthStart = Integer.valueOf(times[0].substring(0,times[0].indexOf("/")));
                int monthEnd = Integer.valueOf(times[1].substring(0,times[1].indexOf("/")));
                //获取block日期列表
                List<Date> blockDates = Lists.newLinkedList();
                if (monthEnd >= monthStart){
                    for (Integer year : years) {
                        String from = year + "/" + times[0];
                        String end = year + "/" + times[1];
                        Date fromTime = DateUtil.parse(from,"yyyy/MM/dd");
                        Date endTIme = DateUtil.parse(end,"yyyy/MM/dd");
                        blockDates.addAll(DateUtils.containDateList(fromTime,endTIme,null));
                    }
                }else {
                    for (Integer year : years) {
                        int y1 = year - 1;
                        int y2 = year + 1;
                        String from1 = y1 + "/" + times[0];
                        String end1 = year + "/" + times[1];
                        Date fromTime1 = DateUtil.parse(from1,"yyyy/MM/dd");
                        Date endTime1 = DateUtil.parse(end1,"yyyy/MM/dd");
                        blockDates.addAll(DateUtils.containDateList(fromTime1,endTime1,null));

                        String from2 = year + "/" + times[0];
                        String end2 = y2 + "/" + times[1];
                        Date fromTime2 = DateUtil.parse(from2,"yyyy/MM/dd");
                        Date endTime2 = DateUtil.parse(end2,"yyyy/MM/dd");
                        blockDates.addAll(DateUtils.containDateList(fromTime2,endTime2,null));
                    }
                }
                //获取可预约范围内的block列表
                List<Date> bookDates = DateUtils.containDateList(startDate,endDate,null);
                List<Date> tempDates = Lists.newLinkedList(bookDates);
                tempDates.removeAll(blockDates);
                bookDates.removeAll(tempDates);
                result.addAll(bookDates);
            }

            //第十种情况，单独的农历  CM/d
            if (Pattern.matches(singleLunar,block)){
                //获取年份
                List<Integer> years = Lists.newLinkedList();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(startDate);
                int year1 = calendar.get(Calendar.YEAR);
                calendar.setTime(endDate);
                int year2 = calendar.get(Calendar.YEAR);
                for ( ; year1 <= year2; year1++ ){
                    years.add(year1);
                }
                //获取农历月份
                int monthLunar = Integer.valueOf(block.substring(1,block.indexOf("/")));
                //获取农历日
                int dayLunar = Integer.valueOf(block.substring(block.indexOf("/")+1));
                //生成农历年月日,为防止临界值情况，特在年范围内+ - 1 年,获取对应阳历的block列表
                List<Date> blockDates = Lists.newLinkedList();
                for (Integer year : years) {
                    for (int i = year-1;i<=year+1;i++){
                        Lunar lunar = new Lunar();
                        lunar.setLunarYear(i);
                        lunar.setLunarMonth(monthLunar);
                        lunar.setLunarDay(dayLunar);
                        Solar solar = LunarSolarConverterUtils.LunarToSolar(lunar);
                        String dateStr = solar.getSolarYear() + "/" + solar.getSolarMonth() + "/" + solar.getSolarDay();
                        blockDates.add(DateUtil.parse(dateStr,"yyyy/MM/dd"));
                    }
                }
                //获取可预约范围内的block列表
                List<Date> bookDates = DateUtils.containDateList(startDate,endDate,null);
                List<Date> tempDates = Lists.newLinkedList(bookDates);
                tempDates.removeAll(blockDates);
                bookDates.removeAll(tempDates);
                result.addAll(bookDates);
            }

            //第十一种情况，连续的农历 CM/d-CM/d
            if (Pattern.matches(manyLunar,block)){
                //获取年份
                List<Integer> years = Lists.newLinkedList();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(startDate);
                int year1 = calendar.get(Calendar.YEAR);
                calendar.setTime(endDate);
                int year2 = calendar.get(Calendar.YEAR);
                for ( ; year1 <= year2; year1++ ){
                    years.add(year1);
                }
                String[] times = block.split("-");
                //获取时间段开始的农历月份
                int startMonthLunar = Integer.valueOf(times[0].substring(1,times[0].indexOf("/")));
                //获取时间段开始的农历日
                int startDayLunar = Integer.valueOf(times[0].substring(times[0].indexOf("/")+1));
                //获取时间段结束的农历月份
                int endMonthLunar = Integer.valueOf(times[1].substring(1,times[1].indexOf("/")));
                //获取时间段结束的农历日
                int endDayLunar = Integer.valueOf(times[1].substring(times[1].indexOf("/")+1));
                //获取block日期列表
                List<Date> blockDates = Lists.newLinkedList();
                if (endMonthLunar >= startMonthLunar){
                    for (Integer year : years) {
                        for (int i=year-1;i<=year+1;i++){
                            Lunar lunar = new Lunar();
                            lunar.setLunarYear(i);
                            lunar.setLunarMonth(startMonthLunar);
                            lunar.setLunarDay(startDayLunar);
                            Solar solarStart = LunarSolarConverterUtils.LunarToSolar(lunar);
                            String dateStrStart = solarStart.getSolarYear() + "/" + solarStart.getSolarMonth() + "/" + solarStart.getSolarDay();
                            //阳历起始日期
                            Date dateStart = DateUtil.parse(dateStrStart,"yyyy/MM/dd");
                            lunar.setLunarYear(i);
                            lunar.setLunarMonth(endMonthLunar);
                            lunar.setLunarDay(endDayLunar);
                            Solar solarEnd = LunarSolarConverterUtils.LunarToSolar(lunar);
                            String dateStrEnd = solarEnd.getSolarYear() + "/" + solarEnd.getSolarMonth() + "/" + solarEnd.getSolarDay();
                            //阳历结束日期
                            Date dateEnd = DateUtil.parse(dateStrEnd,"yyyy/MM/dd");
                            blockDates.addAll(DateUtils.containDateList(dateStart,dateEnd,null));
                        }
                    }
                }else {
                    for (Integer year : years) {
                        int y1 = year - 1;
                        int y2 = year + 1;
                        //年份为 year-1到year
                        Lunar lunar = new Lunar();
                        lunar.setLunarYear(y1);
                        lunar.setLunarMonth(startMonthLunar);
                        lunar.setLunarDay(startDayLunar);
                        Solar solarStart1 = LunarSolarConverterUtils.LunarToSolar(lunar);
                        String dateStrStart1 = solarStart1.getSolarYear() + "/" + solarStart1.getSolarMonth() + "/" + solarStart1.getSolarDay();
                        //阳历起始日期
                        Date dateStart1 = DateUtil.parse(dateStrStart1,"yyyy/MM/dd");
                        lunar.setLunarYear(year);
                        lunar.setLunarMonth(endMonthLunar);
                        lunar.setLunarDay(endDayLunar);
                        Solar solarEnd1 = LunarSolarConverterUtils.LunarToSolar(lunar);
                        String dateStrEnd1 = solarEnd1.getSolarYear() + "/" + solarEnd1.getSolarMonth() + "/" + solarEnd1.getSolarDay();
                        //阳历结束日期
                        Date dateEnd1 = DateUtil.parse(dateStrEnd1,"yyyy/MM/dd");
                        blockDates.addAll(DateUtils.containDateList(dateStart1,dateEnd1,null));

                        //年份为year到year+1
                        lunar.setLunarYear(year);
                        lunar.setLunarMonth(startMonthLunar);
                        lunar.setLunarDay(startDayLunar);
                        Solar solarStart2 = LunarSolarConverterUtils.LunarToSolar(lunar);
                        String dateStrStart2 = solarStart2.getSolarYear() + "/" + solarStart2.getSolarMonth() + "/" + solarStart2.getSolarDay();
                        //阳历起始日期
                        Date dateStart2 = DateUtil.parse(dateStrStart2,"yyyy/MM/dd");
                        lunar.setLunarYear(y2);
                        lunar.setLunarMonth(endMonthLunar);
                        lunar.setLunarDay(endDayLunar);
                        Solar solarEnd2 = LunarSolarConverterUtils.LunarToSolar(lunar);
                        String dateStrEnd2 = solarEnd2.getSolarYear() + "/" + solarEnd2.getSolarMonth() + "/" + solarEnd2.getSolarDay();
                        //阳历结束日期
                        Date dateEnd2 = DateUtil.parse(dateStrEnd2,"yyyy/MM/dd");
                        blockDates.addAll(DateUtils.containDateList(dateStart2,dateEnd2,null));
                    }
                }
                //获取可预约范围内的block列表
                List<Date> bookDates = DateUtils.containDateList(startDate,endDate,null);
                List<Date> tempDates = Lists.newLinkedList(bookDates);
                tempDates.removeAll(blockDates);
                bookDates.removeAll(tempDates);
                result.addAll(bookDates);
            }

            //第十二种情况，除夕
            if ("EVE".equals(block)){
                //获取年份
                List<Integer> years = Lists.newLinkedList();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(startDate);
                int year1 = calendar.get(Calendar.YEAR);
                calendar.setTime(endDate);
                int year2 = calendar.get(Calendar.YEAR);
                for ( ; year1 <= year2; year1++ ){
                    years.add(year1);
                }
                //获取block日期列表
                List<Date> blockDates = Lists.newLinkedList();
                for (Integer year : years) {
                    for (int i = year-1;i<=year+1;i++){
                        Lunar lunar = new Lunar();
                        lunar.setLunarYear(i);
                        lunar.setLunarMonth(1);
                        lunar.setLunarDay(1);
                        Solar solar = LunarSolarConverterUtils.LunarToSolar(lunar);
                        String dateStr = solar.getSolarYear()+"/"+solar.getSolarMonth()+"/"+solar.getSolarDay();
                        Date date = DateUtil.parse(dateStr,"yyyy/MM/dd");
                        calendar.setTime(date);
                        calendar.add(Calendar.DAY_OF_MONTH,-1);
                        blockDates.add(calendar.getTime());
                    }
                }
                //获取可预约范围内的block列表
                List<Date> bookDates = DateUtils.containDateList(startDate,endDate,null);
                List<Date> tempDates = Lists.newLinkedList(bookDates);
                tempDates.removeAll(blockDates);
                bookDates.removeAll(tempDates);
                result.addAll(bookDates);
            }
            
            //第十三种情况，父亲节
            if ("FD".equals(block)){
                //获取年份
                List<Integer> years = Lists.newLinkedList();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(startDate);
                int year1 = calendar.get(Calendar.YEAR);
                calendar.setTime(endDate);
                int year2 = calendar.get(Calendar.YEAR);
                for ( ; year1 <= year2; year1++ ){
                    years.add(year1);
                }
                //获取block日期列表
                List<Date> blockDates = Lists.newLinkedList();
                for (Integer year : years) {
                    Date date = DateUtils.getYMNumWeekDate(year,6,3,0);
                    blockDates.add(date);
                }
                //获取可预约范围内的block列表
                List<Date> bookDates = DateUtils.containDateList(startDate,endDate,null);
                List<Date> tempDates = Lists.newLinkedList(bookDates);
                tempDates.removeAll(blockDates);
                bookDates.removeAll(tempDates);
                result.addAll(bookDates);
            }

            //第十四种情况，母亲节
            if ("MD".equals(block)){
                //获取年份
                List<Integer> years = Lists.newLinkedList();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(startDate);
                int year1 = calendar.get(Calendar.YEAR);
                calendar.setTime(endDate);
                int year2 = calendar.get(Calendar.YEAR);
                for ( ; year1 <= year2; year1++ ){
                    years.add(year1);
                }
                //获取block日期列表
                List<Date> blockDates = Lists.newLinkedList();
                for (Integer year : years) {
                    Date date = DateUtils.getYMNumWeekDate(year,5,2,0);
                    blockDates.add(date);
                }
                //获取可预约范围内的block列表
                List<Date> bookDates = DateUtils.containDateList(startDate,endDate,null);
                List<Date> tempDates = Lists.newLinkedList(bookDates);
                tempDates.removeAll(blockDates);
                bookDates.removeAll(tempDates);
                result.addAll(bookDates);
            }

        }
        return result;
    }


    /**
     * 根据block规则获取有效的block（当前时间之前的block排除）
     * @param blockRuleList
     * @return
     * @throws Exception
     */
    @Override
    public List<BlockRule> getEffectBlockRule(List<BlockRule> blockRuleList) throws Exception {
        List<BlockRule> result = Lists.newLinkedList();
        if (!CollectionUtils.isEmpty(blockRuleList)){
            //获取当前时间
            Calendar calendar = Calendar.getInstance();
            Date startDate = DateUtil.parse(DateUtil.format(new Date(),"yyyy-MM-dd"),"yyyy-MM-dd");
            calendar.setTime(startDate);
            calendar.add(Calendar.YEAR,1);
            Date endDate = DateUtil.parse(DateUtil.format(calendar.getTime(),"yyyy-MM-dd"),"yyyy-MM-dd");
            for (BlockRule blockRule : blockRuleList) {
                HashSet<Date> blockDate = generateBlockDate(startDate,endDate,blockRule.getRule());
                if (!CollectionUtils.isEmpty(blockDate)){
                    result.add(blockRule);
                }
            }
        }
        return result;
    }

    @Override
    public QueryBookBlockRes queryAllBlock(Integer productGroupProductId)throws Exception {
        List<String> allBlockList = Lists.newLinkedList();
        String allBlock = null;
        QueryBookBlockRes queryBookBlockRes = new QueryBookBlockRes();
        //查询产品组的产品
        ProductGroupProduct productGroupProduct = productGroupProductMapper.selectById(productGroupProductId);
        //查询产品
        Product product = productMapper.selectById(productGroupProduct.getProductId());
        //查询商户协议
        ShopProtocol shopProtocol = shopProtocolMapper.selectById(product.getShopId());
        //查询规格
        ShopItem shopItem = shopItemMapper.selectById(product.getShopItemId());
        //查询产品组
        ProductGroup productGroup = productGroupMapper.selectById(productGroupProduct.getProductGroupId());
        //查询商品
        Goods goods = goodsMapper.selectById(productGroup.getGoodsId());
        if (StringUtils.isNotBlank(shopProtocol.getBlockRule())){
            allBlockList.add(shopProtocol.getBlockRule());
        }
        if (StringUtils.isNotBlank(productGroupProduct.getBlockRule())){
            allBlockList.add(productGroupProduct.getBlockRule());
        }
        if (StringUtils.isNotBlank(shopItem.getBlockRule())){
            allBlockList.add(shopItem.getBlockRule());
        }
        if (StringUtils.isNotBlank(goods.getBlock())){
            allBlockList.add(goods.getBlock());
        }
        if (StringUtils.isNotBlank(productGroup.getBlockRule())){
            allBlockList.add(productGroup.getBlockRule());
        }
        if (!CollectionUtils.isEmpty(allBlockList)){
            allBlock = StringUtils.join(allBlockList,", ");
        }
        if (StringUtils.isNotBlank(allBlock)){
            List<BlockRule> block = this.blockRuleStr2list(allBlock);
            if (!CollectionUtils.isEmpty(block)){
                List<BlockRule> tempBlock = getEffectBlockRule(block);
                if (!CollectionUtils.isEmpty(tempBlock)){
                    queryBookBlockRes.setBlockRule(tempBlock.stream().map(rule -> rule.getNatural()).collect(Collectors.joining("; ")));
                }
            }
        }
        return queryBookBlockRes;
    }

    @Override
    public String getBlockRule(Integer productGroupProductId){
        String blockRule = null;
        List<String> allBlockList = Lists.newLinkedList();
        String allBlock = null;
        QueryBookBlockRes queryBookBlockRes = new QueryBookBlockRes();
        //查询产品组的产品
        ProductGroupProduct productGroupProduct = productGroupProductMapper.selectById(productGroupProductId);
        //查询产品
        Product product = productMapper.selectById(productGroupProduct.getProductId());
        //查询商户协议
        ShopProtocol shopProtocol = shopProtocolMapper.selectById(product.getShopId());
        //查询规格
        ShopItem shopItem = shopItemMapper.selectById(product.getShopItemId());
        //查询产品组
        ProductGroup productGroup = productGroupMapper.selectById(productGroupProduct.getProductGroupId());
        //查询商品
        Goods goods = goodsMapper.selectById(productGroup.getGoodsId());
        if (StringUtils.isNotBlank(shopProtocol.getBlockRule())){
            allBlockList.add(shopProtocol.getBlockRule());
        }
        if (StringUtils.isNotBlank(productGroupProduct.getBlockRule())){
            allBlockList.add(productGroupProduct.getBlockRule());
        }
        if (StringUtils.isNotBlank(shopItem.getBlockRule())){
            allBlockList.add(shopItem.getBlockRule());
        }
        if (StringUtils.isNotBlank(goods.getBlock())){
            allBlockList.add(goods.getBlock());
        }
        if (StringUtils.isNotBlank(productGroup.getBlockRule())){
            allBlockList.add(productGroup.getBlockRule());
        }
        if (!CollectionUtils.isEmpty(allBlockList)){
            allBlock = StringUtils.join(allBlockList,", ");
        }
        if (StringUtils.isNotBlank(allBlock)){
            List<BlockRule> block = this.blockRuleStr2list(allBlock);
            if (!CollectionUtils.isEmpty(block)){
                List<BlockRule> tempBlock = null;
                try {
                    tempBlock = this.getEffectBlockRule(block);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (!CollectionUtils.isEmpty(tempBlock)){
                   blockRule = tempBlock.stream().map(rule -> rule.getNatural()).collect(Collectors.joining("; "));
                }
            }
        }
        return blockRule;
    }

    /**
     * 来电录单初步查询指定时间能否预约（商户）(已激活的码)
     * @param req
     * @return
     * @throws Exception
     */
    @Override
    public List<Integer> queryActCallBook(QueryCallBookReq req) throws Exception {
        //可预约商户id列表
        List<Integer> res = Lists.newLinkedList();
        int hour = DateUtil.hour(new Date(),true);
        //查询指定产品组的所有商户
        List<Integer> shopIds = productGroupProductMapper.selectShopByGroup(req.getProductGroupId());
        //查询商品
        Goods goods = goodsMapper.selectById(req.getGoodsId());
        //查询产品组
        ProductGroup productGroup = productGroupMapper.selectById(req.getProductGroupId());
        Wrapper wrapper = new Wrapper() {
            @Override
            public String getSqlSegment() {
                return "where del_flag = 0 and product_group_id ="+req.getProductGroupId();
            }
        };
        List<ProductGroupResource> resourceList = productGroupResourceMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(resourceList)){
            throw new Exception("产品组资源类型不能为空");
        }
        if (!CollectionUtils.isEmpty(shopIds)){
            for (Integer shopId : shopIds) {
                List<String> allBlockList = Lists.newLinkedList();
                String allBlock = null;
                //查询商户信息
                Shop shop = shopMapper.selectById(shopId);
                //查询酒店信息
                Hotel hotel = null;
                if (shop.getHotelId()!=null){
                    hotel = hotelService.selectById(shop.getHotelId());
                }
                //查询城市信息
                Integer cityId;
                if (shop.getCityId()!=null){
                    cityId = shop.getCityId();
                }else if (hotel != null){
                    cityId = hotel.getCityId();
                }else {
                    cityId = null;
                }
                City city = new City();
                if (cityId != null){
                    city = cityMapper.selectById(cityId);
                }
                //查询商户协议
                ShopProtocol shopProtocol = shopProtocolMapper.selectById(shopId);
                if (StringUtils.isNotBlank(shopProtocol.getBlockRule())){
                    allBlockList.add(shopProtocol.getBlockRule());
                }
                if (StringUtils.isNotBlank(goods.getBlock())){
                    allBlockList.add(goods.getBlock());
                }
                if (StringUtils.isNotBlank(productGroup.getBlockRule())){
                    allBlockList.add(productGroup.getBlockRule());
                }
                if (!CollectionUtils.isEmpty(allBlockList)){
                    allBlock = StringUtils.join(allBlockList,", ");
                }
                //查询权益有效期
                Date startDate;//有效范围开始时间
                Date endDate;//有效范围结束时间
                if ("drink".equals(resourceList.get(0).getService()) || resourceList.get(0).getService().indexOf("_cpn") > -1){
                    startDate = new Date();
                    endDate = new Date();
                }else {
                    //商户中最小提前预约天数
                    Integer shopMinBookDay = shop.getMinBookDays();
                    if (hour>=17){
                        if (shopMinBookDay != null){
                            shopMinBookDay = shopMinBookDay + 1;
                        }
                    }
                    //商户中最大提起预约天数
                    Integer shopMaxBookDay = null;
                    if (shopMinBookDay != null && shop.getMaxBookDays() != null){
                        shopMaxBookDay = shop.getMaxBookDays()+shopMinBookDay;
                    }
                    //商品中最小提前预约天数
                    Integer goodsMinBookDay = goods.getMinBookDays();
                    if (hour>=17){
                        if (goodsMinBookDay != null){
                            goodsMinBookDay = goodsMinBookDay + 1;
                        }
                    }
                    //商品中最大提前预约天数
                    Integer goodsMaxBookDay = null;
                    if (goodsMinBookDay != null && goods.getMaxBookDays() != null){
                        goodsMaxBookDay = goods.getMaxBookDays()+goodsMinBookDay;
                    }
                    //产品组中最小提前预约天数
                    Integer productGroupMinBookDay = productGroup.getMinBookDays();
                    if (hour>=17){
                        if (productGroupMinBookDay != null){
                            productGroupMinBookDay = productGroupMinBookDay + 1;
                        }
                    }
                    //产品组中最大提前预约天数
                    Integer productGroupMaxBookDay = null;
                    if (productGroupMinBookDay != null && productGroup.getMaxBookDays() != null){
                        productGroupMaxBookDay = productGroup.getMaxBookDays()+productGroupMinBookDay;
                    }
                    //综合最小提前预约天数
                    Integer minBookDay = maxInteger(shopMinBookDay,maxInteger(goodsMinBookDay,productGroupMinBookDay));
                    //综合最大提前预约天数
                    Integer maxBookDay = minInteger(shopMaxBookDay,minInteger(goodsMaxBookDay,productGroupMaxBookDay));
                    //获取资源默认的最小最大提前预约时间
                    BookNum bookNum = BookMinMaxDayUtils.getBookByService(resourceList.get(0).getService(),city.getCountryId());
                    if (minBookDay == null){
                        minBookDay = bookNum.getMinBook();
                    }
                    if (maxBookDay == null){
                        maxBookDay = bookNum.getMaxBook();
                    }
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(new Date());
                    calendar.add(Calendar.DAY_OF_MONTH,minBookDay);
                    startDate = calendar.getTime();
                    calendar.setTime(new Date());
                    calendar.add(Calendar.DAY_OF_MONTH,maxBookDay);
                    endDate = calendar.getTime();
                }
                //获取权益到期时间
                Date expiryDate = req.getActExpireTime() != null ? DateUtil.parse(DateUtil.format(req.getActExpireTime(),"yyyy-MM-dd"),"yyyy-MM-dd") : null;
                startDate = DateUtil.parse(DateUtil.format(startDate,"yyyy-MM-dd"),"yyyy-MM-dd");
                endDate = DateUtil.parse(DateUtil.format(endDate,"yyyy-MM-dd"),"yyyy-MM-dd");
                if (expiryDate != null){
                    if (expiryDate.compareTo(startDate) < 0){
                        continue;
                    }else if (expiryDate.compareTo(startDate) >= 0 && expiryDate.compareTo(endDate) <=0){
                        endDate = expiryDate;
                    }
                    //券类型资源预定范围到权益结束
                    if (resourceList.get(0).getService().indexOf("_cpn") > -1){
                        endDate = expiryDate;
                    }
                }
                List<Date> bookDates = generateBookDate(startDate,endDate,allBlock);
                //券类型不考虑设置的block
                if (resourceList.get(0).getService().indexOf("_cpn") > -1){
                    bookDates = DateUtils.containDateList(startDate,endDate,null);
                }
                if (!CollectionUtils.isEmpty(bookDates)){
                    Date bookDate = DateUtil.parse(DateUtil.format(req.getBookDate(),"yyyy-MM-dd"),"yyyy-MM-dd");
                    if (bookDates.contains(bookDate)){
                        res.add(shopId);
                    }
                }
            }
        }
        return res;
    }

    /**
     * 来电录单初步查询指定时间能否预约（商户）(已出库的码)
     * @param req
     * @return
     */
    @Override
    public List<Integer> queryOutCallBook(QueryCallBookReq req) throws Exception{
        //可预约商户id列表
        List<Integer> res = Lists.newLinkedList();
        int hour = DateUtil.hour(new Date(),true);
        //查询指定产品组的所有商户
        List<Integer> shopIds = productGroupProductMapper.selectShopByGroup(req.getProductGroupId());
        //查询商品
        Goods goods = goodsMapper.selectById(req.getGoodsId());
        //查询产品组
        ProductGroup productGroup = productGroupMapper.selectById(req.getProductGroupId());
        Wrapper wrapper = new Wrapper() {
            @Override
            public String getSqlSegment() {
                return "where del_flag = 0 and product_group_id ="+req.getProductGroupId();
            }
        };
        List<ProductGroupResource> resourceList = productGroupResourceMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(resourceList)){
            throw new Exception("产品组资源类型不能为空");
        }
        if (!CollectionUtils.isEmpty(shopIds)){
            for (Integer shopId : shopIds) {
                List<String> allBlockList = Lists.newLinkedList();
                String allBlock = null;
                //查询商户信息
                Shop shop = shopMapper.selectById(shopId);
                //查询酒店信息
                Hotel hotel = null;
                if (shop.getHotelId()!=null){
                    hotel = hotelService.selectById(shop.getHotelId());
                }
                //查询城市信息
                Integer cityId;
                if (shop.getCityId()!=null){
                    cityId = shop.getCityId();
                }else if (hotel != null){
                    cityId = hotel.getCityId();
                }else {
                    cityId = null;
                }
                City city = new City();
                if (cityId != null){
                    city = cityMapper.selectById(cityId);
                }
                //查询商户协议
                ShopProtocol shopProtocol = shopProtocolMapper.selectById(shopId);
                if (StringUtils.isNotBlank(shopProtocol.getBlockRule())){
                    allBlockList.add(shopProtocol.getBlockRule());
                }
                if (StringUtils.isNotBlank(goods.getBlock())){
                    allBlockList.add(goods.getBlock());
                }
                if (StringUtils.isNotBlank(productGroup.getBlockRule())){
                    allBlockList.add(productGroup.getBlockRule());
                }
                if (!CollectionUtils.isEmpty(allBlockList)){
                    allBlock = StringUtils.join(allBlockList,", ");
                }
                //查询权益有效期
                Date startDate;//有效范围开始时间
                Date endDate;//有效范围结束时间
                GoodsBaseVo goodsBaseVo = new GoodsBaseVo();
                BeanUtils.copyProperties(goods,goodsBaseVo);
                goodsBaseVo = GoodsUtils.analyCodeDate(goodsBaseVo,req.getActivationDate(),req.getOutDate());
                if ("drink".equals(resourceList.get(0).getService()) || resourceList.get(0).getService().indexOf("_cpn") > -1){
                    startDate = new Date();
                    endDate = new Date();
                }else {
                    //商户中最小提前预约天数
                    Integer shopMinBookDay = shop.getMinBookDays();
                    if (hour>=17){
                        if (shopMinBookDay != null){
                            shopMinBookDay = shopMinBookDay + 1;
                        }
                    }
                    //商户中最大提起预约天数
                    Integer shopMaxBookDay = null;
                    if (shopMinBookDay != null && shop.getMaxBookDays() != null){
                        shopMaxBookDay = shop.getMaxBookDays()+shopMinBookDay;
                    }
                    //商品中最小提前预约天数
                    Integer goodsMinBookDay = goods.getMinBookDays();
                    if (hour>=17){
                        if (goodsMinBookDay != null){
                            goodsMinBookDay = goodsMinBookDay + 1;
                        }
                    }
                    //商品中最大提前预约天数
                    Integer goodsMaxBookDay = null;
                    if (goodsMinBookDay != null && goods.getMaxBookDays() != null){
                        goodsMaxBookDay = goods.getMaxBookDays()+goodsMinBookDay;
                    }
                    //产品组中最小提前预约天数
                    Integer productGroupMinBookDay = productGroup.getMinBookDays();
                    if (hour>=17){
                        if (productGroupMinBookDay != null){
                            productGroupMinBookDay = productGroupMinBookDay + 1;
                        }
                    }
                    //产品组中最大提前预约天数
                    Integer productGroupMaxBookDay = null;
                    if (productGroupMinBookDay != null && productGroup.getMaxBookDays() != null){
                        productGroupMaxBookDay = productGroup.getMaxBookDays()+productGroupMinBookDay;
                    }
                    //综合最小提前预约天数
                    Integer minBookDay = maxInteger(shopMinBookDay,maxInteger(goodsMinBookDay,productGroupMinBookDay));
                    //综合最大提前预约天数
                    Integer maxBookDay = minInteger(shopMaxBookDay,minInteger(goodsMaxBookDay,productGroupMaxBookDay));
                    //获取资源默认的最小最大提前预约时间
                    BookNum bookNum = BookMinMaxDayUtils.getBookByService(resourceList.get(0).getService(),city.getCountryId());
                    if (minBookDay == null){
                        minBookDay = bookNum.getMinBook();
                    }
                    if (maxBookDay == null){
                        maxBookDay = bookNum.getMaxBook();
                    }
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(new Date());
                    calendar.add(Calendar.DAY_OF_MONTH,minBookDay);
                    startDate = calendar.getTime();
                    calendar.setTime(new Date());
                    calendar.add(Calendar.DAY_OF_MONTH,maxBookDay);
                    endDate = calendar.getTime();
                }
                //获取权益到期时间
                String expiryStr = goodsBaseVo.getExpiryDate();
                Date expiryDate;
                if (expiryStr.equals("NULL")){
                    expiryDate = null;
                }else {
                    expiryDate = DateUtil.parse(expiryStr,"yyyy-MM-dd");
                }
                startDate = DateUtil.parse(DateUtil.format(startDate,"yyyy-MM-dd"),"yyyy-MM-dd");
                endDate = DateUtil.parse(DateUtil.format(endDate,"yyyy-MM-dd"),"yyyy-MM-dd");
                if (expiryDate != null){
                    if (expiryDate.compareTo(startDate) < 0){
                        continue;
                    }else if (expiryDate.compareTo(startDate) >= 0 && expiryDate.compareTo(endDate) <=0){
                        endDate = expiryDate;
                    }
                    //券类型资源预定范围到权益结束
                    if (resourceList.get(0).getService().indexOf("_cpn") > -1){
                        endDate = expiryDate;
                    }
                }
                List<Date> bookDates = generateBookDate(startDate,endDate,allBlock);
                //券类型不考虑设置的block
                if (resourceList.get(0).getService().indexOf("_cpn") > -1){
                    bookDates = DateUtils.containDateList(startDate,endDate,null);
                }
                if (!CollectionUtils.isEmpty(bookDates)){
                    Date bookDate = DateUtil.parse(DateUtil.format(req.getBookDate(),"yyyy-MM-dd"),"yyyy-MM-dd");
                    if (bookDates.contains(bookDate)){
                        res.add(shopId);
                    }
                }
            }
        }
        return res;
    }

    @Override
    @Cacheable(value = "BlockRule",key = "'queryBookBlockNew_'+#queryBookBlockReq.productGroupProductId+'_'+((#queryBookBlockReq.actExpireTime == null) ? null : #queryBookBlockReq.actExpireTime.time)",unless = "#result == null")
    public List<Date> queryBookBlockNew(QueryBookBlockReq queryBookBlockReq) {
        int hour = DateUtil.hour(new Date(),true);
        BlockBookDaysVo vo = productGroupProductMapper.queryBookDays(queryBookBlockReq.getProductGroupProductId());
        //查询权益有效期
        Date startDate;//有效范围开始时间
        Date endDate;//有效范围结束时间
        if ("drink".equals(vo.getServiceType()) || vo.getServiceType().indexOf("_cpn") > -1){
            startDate = new Date();
            endDate = new Date();
        }else {
            //商户中最小提前预约天数
            Integer shopMinBookDay = vo.getShopMinBookDays();
            if (hour>=17){
                if (shopMinBookDay != null){
                    shopMinBookDay = shopMinBookDay + 1;
                }
            }
            //商户中最大提起预约天数
            Integer shopMaxBookDay = null;
            if (shopMinBookDay != null && vo.getShopMaxBookDays() != null){
                shopMaxBookDay = vo.getShopMaxBookDays()+shopMinBookDay;
            }
            //商品中最小提前预约天数
            Integer goodsMinBookDay = vo.getGoodsMinBookDays();
            if (hour>=17){
                if (goodsMinBookDay != null){
                    goodsMinBookDay = goodsMinBookDay + 1;
                }
            }
            //商品中最大提前预约天数
            Integer goodsMaxBookDay = null;
            if (goodsMinBookDay != null && vo.getGoodsMaxBookDays() != null){
                goodsMaxBookDay = vo.getGoodsMaxBookDays()+goodsMinBookDay;
            }
            //产品组中最小提前预约天数
            Integer productGroupMinBookDay = vo.getGroupMinBookDays();
            if (hour>=17){
                if (productGroupMinBookDay != null){
                    productGroupMinBookDay = productGroupMinBookDay + 1;
                }
            }
            //产品组中最大提前预约天数
            Integer productGroupMaxBookDay = null;
            if (productGroupMinBookDay != null && vo.getGroupMaxBookDays() != null){
                productGroupMaxBookDay = vo.getGroupMaxBookDays()+productGroupMinBookDay;
            }
            //综合最小提前预约天数
            Integer minBookDay = maxInteger(shopMinBookDay,maxInteger(goodsMinBookDay,productGroupMinBookDay));
            //综合最大提前预约天数
            Integer maxBookDay = minInteger(shopMaxBookDay,minInteger(goodsMaxBookDay,productGroupMaxBookDay));
            //获取资源默认的最小最大提前预约时间
            BookNum bookNum = BookMinMaxDayUtils.getBookByService(vo.getServiceType(),vo.getCountryId());
            if (minBookDay == null){
                minBookDay = bookNum.getMinBook();
            }
            if (maxBookDay == null){
                maxBookDay = bookNum.getMaxBook();
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.DAY_OF_MONTH,minBookDay);
            startDate = calendar.getTime();
            calendar.setTime(new Date());
            calendar.add(Calendar.DAY_OF_MONTH,maxBookDay);
            endDate = calendar.getTime();
        }
        //获取权益到期时间
        Date expiryDate = queryBookBlockReq.getActExpireTime() != null ? DateUtil.parse(DateUtil.format(queryBookBlockReq.getActExpireTime(),"yyyy-MM-dd"),"yyyy-MM-dd") : null;
        startDate = DateUtil.parse(DateUtil.format(startDate,"yyyy-MM-dd"),"yyyy-MM-dd");
        endDate = DateUtil.parse(DateUtil.format(endDate,"yyyy-MM-dd"),"yyyy-MM-dd");
        if (expiryDate != null){
            if (expiryDate.compareTo(startDate) < 0){
                return null;
            }else if (expiryDate.compareTo(startDate) >= 0 && expiryDate.compareTo(endDate) <=0){
                endDate = expiryDate;
            }
            //券类型资源预定范围到权益结束
            if (vo.getServiceType().indexOf("_cpn") > -1){
                endDate = expiryDate;
            }
        }
        Date finalStartDate = startDate;
        Date finalEndDate = endDate;
        Wrapper wrapper = new Wrapper() {
            @Override
            public String getSqlSegment() {
                return "where del_flag = 0 and product_group_product_id ="+queryBookBlockReq.getProductGroupProductId()+" and block_date BETWEEN '"+DateUtil.format(finalStartDate,"yyyy-MM-dd")+"' AND '"+DateUtil.format(finalEndDate,"yyyy-MM-dd")+"'";
            }
        };
        List<GroupProductBlockDate> blockDateList = groupProductBlockDateMapper.selectList(wrapper);
        List<Date> bookDates = Lists.newLinkedList();
        if (CollectionUtils.isEmpty(blockDateList)){
            bookDates = DateUtils.containDateList(startDate,endDate,null);
        }else {
            Set<Date> blockDate = blockDateList.stream().map(groupProductBlockDate -> groupProductBlockDate.getBlockDate()).collect(Collectors.toSet());
            bookDates = DateUtils.containDateList(startDate,endDate, (HashSet<Date>) blockDate);
        }
        //券类型不考虑设置的block
        if (vo.getServiceType().indexOf("_cpn") > -1){
            bookDates = DateUtils.containDateList(startDate,endDate,null);
        }
        return bookDates;
    }

    /**
     * 获取俩个integer数据中小的数据
     * @param one
     * @param two
     * @return
     */
    public Integer minInteger(Integer one,Integer two){
        if (one == null && two != null){
            return two;
        }
        if (one != null && two == null){
            return one;
        }
        if (one != null && two != null){
            if (one.compareTo(two) > 0){
                return two;
            }else {
                return one;
            }
        }
        return null;
    }

    /**
     * 获取俩个integer数据中大的数据
     * @param one
     * @param two
     * @return
     */
    public Integer maxInteger(Integer one,Integer two){
        if (one == null && two != null){
            return two;
        }
        if (one != null && two == null){
            return one;
        }
        if (one != null && two != null){
            if (one.compareTo(two) > 0){
                return one;
            }else {
                return two;
            }
        }
        return null;
    }

    /**
     * 合并解析周block规则
     * @param weekSet
     * @param ruleStr
     * @param close 是否为关闭
     */
    private void resolveWeek(Set<String> weekSet, String ruleStr,boolean close) {
        if (CollectionUtils.isEmpty(weekSet)&&!close){
            return;
        }
        if (ruleStr.indexOf("-")==-1){
            if (close){
                weekSet.add(ruleStr);
            }
            else {
                weekSet.remove(ruleStr);
            }
        }else {
            int num1 = NumberUtils.toInt(ruleStr.substring(1,2));
            int num2 = NumberUtils.toInt(ruleStr.substring(4));
            if (num1>num2){
                if (close){
                    weekSet.addAll(WEEK_CODE_LIST.subList(num1-1,WEEK_CODE_LIST.size()));
                    weekSet.addAll(WEEK_CODE_LIST.subList(0,num2));
                }else {
                    weekSet.removeAll(WEEK_CODE_LIST.subList(num1-1,WEEK_CODE_LIST.size()));
                    weekSet.removeAll(WEEK_CODE_LIST.subList(0,num2));
                }

            }else {
                if (close){
                    weekSet.addAll(WEEK_CODE_LIST.subList(num1-1,num2));
                }else {
                    weekSet.removeAll(WEEK_CODE_LIST.subList(num1-1,num2));
                }
            }
        }
    }

    /**
     * 合并解析节假日block规则
     * @param holidaySet
     * @param ruleStr
     * @param close
     */
    private void resolveHoliday(Set<String> holidaySet, String ruleStr,boolean close){
        if (CollectionUtils.isEmpty(holidaySet)&&!close){
            return;
        }
        if (close){
            holidaySet.add(ruleStr);
        }else {
            holidaySet.remove(ruleStr);
        }
    }

    /**
     * 合并解析阴历(要吐血了)
     * @param lunarSet
     * @param ruleStr
     * @param close
     */
    private void resolveLunar(Set<String> lunarSet, String ruleStr,boolean close){

        //阴历只存在2种格式：Cx/y,Cx/y-Cm/n
        if (CollectionUtils.isEmpty(lunarSet)){
            if (!close){
                return;
            }
            lunarSet.add(ruleStr);
        }else {
            //遍历并重组集合
            final Iterator<String> iterator = lunarSet.iterator();
            boolean flag=false;//是否存在交叉
            while (iterator.hasNext()){
                final String next = iterator.next();
                //1.如果与循环中的block范围有交叉 且为 close 则取并集   如果不为close 则取差集
                //2.如果循环完成范围无交叉 且为close 则新增block
                if (next.equalsIgnoreCase(ruleStr)){
                    if (!close){
                        iterator.remove();
                    }
                    flag=true;
                    break;
                }
                if (next.indexOf("-")==-1 && ruleStr.indexOf("-")==-1){
                    continue;
                }
                if (next.indexOf("-")>-1 && ruleStr.indexOf("-")==-1){
                    final String[] nextArr = next.split("-");
                    final String[] startArr1 = nextArr[0].split("/");
                    final String[] endArr1 = nextArr[1].split("/");
                    final String[] ruleArr = ruleStr.split("/");
                    if (startArr1.length==endArr1.length &&endArr1.length==ruleArr.length){
                        //月/日
                        if (startArr1.length==2){
                            int startMon=NumberUtils.toInt(startArr1[0]);
                            int startDay=NumberUtils.toInt(startArr1[1]);
                            int endMon=NumberUtils.toInt(endArr1[0]);
                            int endDay=NumberUtils.toInt(endArr1[1]);
                            int ruleMon=NumberUtils.toInt(ruleArr[0]);
                            int ruleDay=NumberUtils.toInt(ruleArr[1]);
                            Calendar now=Calendar.getInstance();
                            now.set(2020,startMon-1,startDay);
                            Date start=now.getTime();
                            now.set(2020,endMon-1,endDay);
                            Date end=now.getTime();
                            now.set(2020,ruleMon-1,ruleDay);
                            Date rule=now.getTime();
                            //说明rule在两个日期之间
                            if (!start.after(rule) && !rule.after(end)){
                                if (!close){
                                    iterator.remove();
                                    if (start.compareTo(rule)==0){
                                        lunarSet.add("C"+ruleMon+"/"+(ruleDay+1)+"-"+nextArr[1]);
                                    }
                                    else if(end.compareTo(rule)==0){
                                        lunarSet.add(nextArr[0]+"-C"+ruleMon+"/"+(ruleDay-1));
                                    }else {
                                        lunarSet.add(nextArr[0]+"-C"+ruleMon+"/"+(ruleDay-1));
                                        lunarSet.add("C"+ruleMon+"/"+(ruleDay+1)+"-"+nextArr[1]);
                                    }
                                }
                                flag=true;
                                break;
                            }
                        }
                        //年/月/日
                        if (startArr1.length==3){
                            int startYear=NumberUtils.toInt(startArr1[0].substring(1));
                            int startMon=NumberUtils.toInt(startArr1[1]);
                            int startDay=NumberUtils.toInt(startArr1[2]);
                            int endYear=NumberUtils.toInt(endArr1[0].substring(1));
                            int endMon=NumberUtils.toInt(endArr1[1]);
                            int endDay=NumberUtils.toInt(endArr1[2]);
                            int ruleYear=NumberUtils.toInt(ruleArr[0].substring(1));
                            int ruleMon=NumberUtils.toInt(ruleArr[1]);
                            int ruleDay=NumberUtils.toInt(ruleArr[2]);
                            Calendar now=Calendar.getInstance();
                            now.set(startYear,startMon-1,startDay);
                            Date start=now.getTime();
                            now.set(endYear,endMon-1,endDay);
                            Date end=now.getTime();
                            now.set(ruleYear,ruleMon-1,ruleDay);
                            Date rule=now.getTime();
                            //说明rule在两个日期之间
                            if (!start.after(rule) && !rule.after(end)){
                                if (!close){
                                    iterator.remove();
                                    if (start.compareTo(rule)==0){
                                        lunarSet.add("C"+ruleYear+"/"+ruleMon+"/"+(ruleDay+1)+"-"+nextArr[1]);
                                    }
                                    else if(end.compareTo(rule)==0){
                                        lunarSet.add(nextArr[0]+"-C"+ruleYear+"/"+ruleMon+"/"+(ruleDay-1));
                                    }else {
                                        lunarSet.add(nextArr[0]+"-C"+ruleYear+"/"+ruleMon+"/"+(ruleDay-1));
                                        lunarSet.add("C"+ruleYear+"/"+ruleMon+"/"+(ruleDay+1)+"-"+nextArr[1]);
                                    }
                                }
                                flag=true;
                                break;
                            }
                        }
                    }
                    continue;
                }
                if (next.indexOf("-")==-1 && ruleStr.indexOf("-")>-1){
                    final String[] ruleArr = ruleStr.split("-");
                    final String[] startArr1 =ruleArr[0].split("/");
                    final String[] endArr1 = ruleArr[1].split("/");
                    final String[] nextArr = next.split("/");
                    if (startArr1.length==endArr1.length && endArr1.length==nextArr.length){
                        //月/日
                        if (startArr1.length==2){
                            int startMon=NumberUtils.toInt(startArr1[0].substring(1));
                            int startDay=NumberUtils.toInt(startArr1[1]);
                            int endMon=NumberUtils.toInt(endArr1[0].substring(1));
                            int endDay=NumberUtils.toInt(endArr1[1]);
                            int nextMon=NumberUtils.toInt(nextArr[0].substring(1));
                            int nextDay=NumberUtils.toInt(nextArr[1]);
                            Calendar now=Calendar.getInstance();
                            now.set(2020,startMon-1,startDay);
                            Date start=now.getTime();
                            now.set(2020,endMon-1,endDay);
                            Date end=now.getTime();
                            now.set(2020,nextMon-1,nextDay);
                            Date nextDate=now.getTime();
                            //说明nextDate在两个日期之间
                            if (!start.after(nextDate) && !nextDate.after(end)){
                                if (!close){
                                    iterator.remove();
                                }
                                flag=true;
                                break;
                            }
                        }
                        //年/月/日
                        if (startArr1.length==3){
                            int startYear=NumberUtils.toInt(startArr1[0].substring(1));
                            int startMon=NumberUtils.toInt(startArr1[1]);
                            int startDay=NumberUtils.toInt(startArr1[2]);
                            int endYear=NumberUtils.toInt(endArr1[0].substring(1));
                            int endMon=NumberUtils.toInt(endArr1[1]);
                            int endDay=NumberUtils.toInt(endArr1[2]);
                            int nextYear=NumberUtils.toInt(nextArr[0].substring(1));
                            int nextMon=NumberUtils.toInt(nextArr[1]);
                            int nextDay=NumberUtils.toInt(nextArr[2]);
                            Calendar now=Calendar.getInstance();
                            now.set(startYear,startMon-1,startDay);
                            Date start=now.getTime();
                            now.set(endYear,endMon-1,endDay);
                            Date end=now.getTime();
                            now.set(nextYear,nextMon-1,nextDay);
                            Date nextDate=now.getTime();
                            //说明nextDate在两个日期之间
                            if (!start.after(nextDate) && !nextDate.after(end)){
                                if (!close){
                                    iterator.remove();
                                }
                                flag=true;
                                break;
                            }
                        }
                    }
                    continue;
                }
                if (next.indexOf("-")>-1 && ruleStr.indexOf("-")>-1){
                    final String[] ruleArr = ruleStr.split("-");
                    final String[] startArr1 =ruleArr[0].split("/");
                    final String[] endArr1 = ruleArr[1].split("/");
                    final String[] nextArr = next.split("-");
                    final String[] nextArr1 = nextArr[0].split("/");
                    final String[] nextArr2 = nextArr[1].split("/");
                    if (startArr1.length==endArr1.length && endArr1.length==nextArr1.length  && nextArr1.length==nextArr2.length){
                        //月/日
                        if (startArr1.length==2){
                            int startMon=NumberUtils.toInt(startArr1[0].substring(1));
                            int startDay=NumberUtils.toInt(startArr1[1]);
                            int endMon=NumberUtils.toInt(endArr1[0].substring(1));
                            int endDay=NumberUtils.toInt(endArr1[1]);
                            int nextMon1=NumberUtils.toInt(nextArr1[0].substring(1));
                            int nextDay1=NumberUtils.toInt(nextArr1[1]);
                            int nextMon2=NumberUtils.toInt(nextArr2[0].substring(1));
                            int nextDay2=NumberUtils.toInt(nextArr2[1]);
                            Calendar now=Calendar.getInstance();
                            now.set(2020,startMon-1,startDay);
                            Date start=now.getTime();
                            now.set(2020,endMon-1,endDay);
                            Date end=now.getTime();
                            now.set(2020,nextMon1-1,nextDay1);
                            Date nextDate1=now.getTime();
                            now.set(2020,nextMon2-1,nextDay2);
                            Date nextDate2=now.getTime();
                            //说明nextDate在两个日期之间
                            if ((!start.after(nextDate1) && !nextDate1.after(end))||(!nextDate1.after(start) && !start.after(nextDate2))){
                                iterator.remove();
                                if (close){
                                    String date1=start.before(nextDate1)?ruleArr[0]:nextArr[0];
                                    String date2=end.after(nextDate2)?ruleArr[1]:nextArr[1];
                                    lunarSet.add(date1+"-"+date2);
                                }else {
                                    if (start.compareTo(nextDate1)<=0){
                                        if (end.compareTo(nextDate2)<0){
                                            lunarSet.add(endMon+"/"+(endDay+1)+"-"+nextArr[1]);
                                        }
                                    }
                                    else {
                                        if (end.compareTo(nextDate2)<0){
                                            lunarSet.add(nextArr[0]+"-C"+endMon+"/"+(endDay-1));
                                            lunarSet.add("C"+endMon+"/"+(endDay+1)+"-"+nextArr[1]);
                                        }
                                        if (end.compareTo(nextDate2)>=0){
                                            lunarSet.add(nextArr[0]+"-C"+endMon+"/"+(endDay-1));
                                        }
                                    }
                                }
                                flag=true;
                                break;
                            }
                        }
                        //年/月/日
                        if (startArr1.length==3){
                            int startYear=NumberUtils.toInt(startArr1[0].substring(1));
                            int startMon=NumberUtils.toInt(startArr1[1]);
                            int startDay=NumberUtils.toInt(startArr1[2]);
                            int endYear=NumberUtils.toInt(endArr1[0].substring(1));
                            int endMon=NumberUtils.toInt(endArr1[1]);
                            int endDay=NumberUtils.toInt(endArr1[2]);
                            int nextYear1=NumberUtils.toInt(nextArr1[0].substring(1));
                            int nextMon1=NumberUtils.toInt(nextArr1[1]);
                            int nextDay1=NumberUtils.toInt(nextArr1[2]);
                            int nextYear2=NumberUtils.toInt(nextArr2[0].substring(1));
                            int nextMon2=NumberUtils.toInt(nextArr2[1]);
                            int nextDay2=NumberUtils.toInt(nextArr2[2]);
                            Calendar now=Calendar.getInstance();
                            now.set(startYear,startMon-1,startDay);
                            Date start=now.getTime();
                            now.set(endYear,endMon-1,endDay);
                            Date end=now.getTime();
                            now.set(nextYear1,nextMon1-1,nextDay1);
                            Date nextDate1=now.getTime();
                            now.set(nextYear2,nextMon2-1,nextDay2);
                            Date nextDate2=now.getTime();
                            //说明nextDate在两个日期之间
                            //说明nextDate在两个日期之间
                            if ((!start.after(nextDate1) && !nextDate1.after(end))||(!nextDate1.after(start) && !start.after(nextDate2))){
                                iterator.remove();
                                if (close){
                                    String date1=start.before(nextDate1)?ruleArr[0]:nextArr[0];
                                    String date2=end.after(nextDate2)?ruleArr[1]:nextArr[1];
                                    lunarSet.add(date1+"-"+date2);
                                }else {
                                    if (start.compareTo(nextDate1)<=0){
                                        if (end.compareTo(nextDate2)<0){
                                            lunarSet.add(endArr1[0]+"/"+endMon+"/"+(endDay+1)+"-"+nextArr[1]);
                                        }
                                    }
                                    else {
                                        if (end.compareTo(nextDate2)<0){
                                            lunarSet.add(nextArr[0]+"-"+endArr1[0]+endMon+"/"+(endDay-1));
                                            lunarSet.add(endArr1[0]+endMon+"/"+(endDay+1)+"-"+nextArr[1]);
                                        }
                                        if (end.compareTo(nextDate2)>=0){
                                            lunarSet.add(nextArr[0]+"-"+endArr1[0]+endMon+"/"+(endDay-1));
                                        }
                                    }
                                }
                                flag=true;
                                break;
                            }
                        }
                    }
                    continue;
                }
            }
            if (!flag&&close){
                lunarSet.add(ruleStr);
            }
        }
    }

    /**
     * 合并解析阳历(最后一口老血)
     * @param solarSet
     * @param ruleStr
     * @param close
     */
    private void resolveSolar(Set<String> solarSet, String ruleStr,boolean close){
        if (CollectionUtils.isEmpty(solarSet)){
            if (!close){
                return;
            }
            solarSet.add(ruleStr);
        }else {
            //遍历并重组集合
            final Iterator<String> iterator = solarSet.iterator();
            boolean flag=false;//是否存在交叉
            while (iterator.hasNext()){
                final String next = iterator.next();
                //1.如果与循环中的block范围有交叉 且为 close 则取并集   如果不为close 则取差集
                //2.如果循环完成范围无交叉 且为close 则新增block
                if (next.equalsIgnoreCase(ruleStr)){
                    if (!close){
                        iterator.remove();
                    }
                    flag=true;
                    break;
                }
                if (next.indexOf("-")==-1 && ruleStr.indexOf("-")==-1){
                    continue;
                }
                if (next.indexOf("-")>-1 && ruleStr.indexOf("-")==-1){
                    final String[] nextArr = next.split("-");
                    final String[] startArr1 = nextArr[0].split("/");
                    final String[] endArr1 = nextArr[1].split("/");
                    final String[] ruleArr = ruleStr.split("/");
                    if (startArr1.length==endArr1.length &&endArr1.length==ruleArr.length){
                        //月/日
                        if (startArr1.length==2){
                            int startMon=NumberUtils.toInt(startArr1[0]);
                            int startDay=NumberUtils.toInt(startArr1[1]);
                            int endMon=NumberUtils.toInt(endArr1[0]);
                            int endDay=NumberUtils.toInt(endArr1[1]);
                            int ruleMon=NumberUtils.toInt(ruleArr[0]);
                            int ruleDay=NumberUtils.toInt(ruleArr[1]);
                            Calendar now=Calendar.getInstance();
                            now.set(2020,startMon-1,startDay);
                            Date start=now.getTime();
                            now.set(2020,endMon-1,endDay);
                            Date end=now.getTime();
                            now.set(2020,ruleMon-1,ruleDay);
                            Date rule=now.getTime();
                            //说明rule在两个日期之间
                            if (!start.after(rule) && !rule.after(end)){
                                if (!close){
                                    iterator.remove();
                                    if (start.compareTo(rule)==0){
                                        solarSet.add(ruleMon+"/"+(ruleDay+1)+"-"+nextArr[1]);
                                    }
                                    else if(end.compareTo(rule)==0){
                                        solarSet.add(nextArr[0]+""+ruleMon+"/"+(ruleDay-1));
                                    }else {
                                        solarSet.add(nextArr[0]+""+ruleMon+"/"+(ruleDay-1));
                                        solarSet.add(ruleMon+"/"+(ruleDay+1)+"-"+nextArr[1]);
                                    }
                                }
                                flag=true;
                                break;
                            }
                        }
                        //年/月/日
                        if (startArr1.length==3){
                            int startYear=NumberUtils.toInt(startArr1[0].substring(1));
                            int startMon=NumberUtils.toInt(startArr1[1]);
                            int startDay=NumberUtils.toInt(startArr1[2]);
                            int endYear=NumberUtils.toInt(endArr1[0].substring(1));
                            int endMon=NumberUtils.toInt(endArr1[1]);
                            int endDay=NumberUtils.toInt(endArr1[2]);
                            int ruleYear=NumberUtils.toInt(ruleArr[0].substring(1));
                            int ruleMon=NumberUtils.toInt(ruleArr[1]);
                            int ruleDay=NumberUtils.toInt(ruleArr[2]);
                            Calendar now=Calendar.getInstance();
                            now.set(startYear,startMon-1,startDay);
                            Date start=now.getTime();
                            now.set(endYear,endMon-1,endDay);
                            Date end=now.getTime();
                            now.set(ruleYear,ruleMon-1,ruleDay);
                            Date rule=now.getTime();
                            //说明rule在两个日期之间
                            if (!start.after(rule) && !rule.after(end)){
                                if (!close){
                                    iterator.remove();
                                    if (start.compareTo(rule)==0){
                                        solarSet.add(ruleYear+"/"+ruleMon+"/"+(ruleDay+1)+"-"+nextArr[1]);
                                    }
                                    else if(end.compareTo(rule)==0){
                                        solarSet.add(nextArr[0]+""+ruleYear+"/"+ruleMon+"/"+(ruleDay-1));
                                    }else {
                                        solarSet.add(nextArr[0]+""+ruleYear+"/"+ruleMon+"/"+(ruleDay-1));
                                        solarSet.add(ruleYear+"/"+ruleMon+"/"+(ruleDay+1)+"-"+nextArr[1]);
                                    }
                                }
                                flag=true;
                                break;
                            }
                        }
                    }
                    continue;
                }
                if (next.indexOf("-")==-1 && ruleStr.indexOf("-")>-1){
                    final String[] ruleArr = ruleStr.split("-");
                    final String[] startArr1 =ruleArr[0].split("/");
                    final String[] endArr1 = ruleArr[1].split("/");
                    final String[] nextArr = next.split("/");
                    if (startArr1.length==endArr1.length && endArr1.length==nextArr.length){
                        //月/日
                        if (startArr1.length==2){
                            int startMon=NumberUtils.toInt(startArr1[0].substring(1));
                            int startDay=NumberUtils.toInt(startArr1[1]);
                            int endMon=NumberUtils.toInt(endArr1[0].substring(1));
                            int endDay=NumberUtils.toInt(endArr1[1]);
                            int nextMon=NumberUtils.toInt(nextArr[0].substring(1));
                            int nextDay=NumberUtils.toInt(nextArr[1]);
                            Calendar now=Calendar.getInstance();
                            now.set(2020,startMon-1,startDay);
                            Date start=now.getTime();
                            now.set(2020,endMon-1,endDay);
                            Date end=now.getTime();
                            now.set(2020,nextMon-1,nextDay);
                            Date nextDate=now.getTime();
                            //说明nextDate在两个日期之间
                            if (!start.after(nextDate) && !nextDate.after(end)){
                                if (!close){
                                    iterator.remove();
                                }
                                flag=true;
                                break;
                            }
                        }
                        //年/月/日
                        if (startArr1.length==3){
                            int startYear=NumberUtils.toInt(startArr1[0].substring(1));
                            int startMon=NumberUtils.toInt(startArr1[1]);
                            int startDay=NumberUtils.toInt(startArr1[2]);
                            int endYear=NumberUtils.toInt(endArr1[0].substring(1));
                            int endMon=NumberUtils.toInt(endArr1[1]);
                            int endDay=NumberUtils.toInt(endArr1[2]);
                            int nextYear=NumberUtils.toInt(nextArr[0].substring(1));
                            int nextMon=NumberUtils.toInt(nextArr[1]);
                            int nextDay=NumberUtils.toInt(nextArr[2]);
                            Calendar now=Calendar.getInstance();
                            now.set(startYear,startMon-1,startDay);
                            Date start=now.getTime();
                            now.set(endYear,endMon-1,endDay);
                            Date end=now.getTime();
                            now.set(nextYear,nextMon-1,nextDay);
                            Date nextDate=now.getTime();
                            //说明nextDate在两个日期之间
                            if (!start.after(nextDate) && !nextDate.after(end)){
                                if (!close){
                                    iterator.remove();
                                }
                                flag=true;
                                break;
                            }
                        }
                    }
                    continue;
                }
                if (next.indexOf("-")>-1 && ruleStr.indexOf("-")>-1){
                    final String[] ruleArr = ruleStr.split("-");
                    final String[] startArr1 =ruleArr[0].split("/");
                    final String[] endArr1 = ruleArr[1].split("/");
                    final String[] nextArr = next.split("-");
                    final String[] nextArr1 = nextArr[0].split("/");
                    final String[] nextArr2 = nextArr[1].split("/");
                    if (startArr1.length==endArr1.length && endArr1.length==nextArr1.length  && nextArr1.length==nextArr2.length){
                        //月/日
                        if (startArr1.length==2){
                            int startMon=NumberUtils.toInt(startArr1[0].substring(1));
                            int startDay=NumberUtils.toInt(startArr1[1]);
                            int endMon=NumberUtils.toInt(endArr1[0].substring(1));
                            int endDay=NumberUtils.toInt(endArr1[1]);
                            int nextMon1=NumberUtils.toInt(nextArr1[0].substring(1));
                            int nextDay1=NumberUtils.toInt(nextArr1[1]);
                            int nextMon2=NumberUtils.toInt(nextArr2[0].substring(1));
                            int nextDay2=NumberUtils.toInt(nextArr2[1]);
                            Calendar now=Calendar.getInstance();
                            now.set(2020,startMon-1,startDay);
                            Date start=now.getTime();
                            now.set(2020,endMon-1,endDay);
                            Date end=now.getTime();
                            now.set(2020,nextMon1-1,nextDay1);
                            Date nextDate1=now.getTime();
                            now.set(2020,nextMon2-1,nextDay2);
                            Date nextDate2=now.getTime();
                            //说明nextDate在两个日期之间
                            if ((!start.after(nextDate1) && !nextDate1.after(end))||(!nextDate1.after(start) && !start.after(nextDate2))){
                                iterator.remove();
                                if (close){
                                    String date1=start.before(nextDate1)?ruleArr[0]:nextArr[0];
                                    String date2=end.after(nextDate2)?ruleArr[1]:nextArr[1];
                                    solarSet.add(date1+"-"+date2);
                                }else {
                                    if (start.compareTo(nextDate1)<=0){
                                        if (end.compareTo(nextDate2)<0){
                                            solarSet.add(endMon+"/"+(endDay+1)+"-"+nextArr[1]);
                                        }
                                    }
                                    else {
                                        if (end.compareTo(nextDate2)<0){
                                            solarSet.add(nextArr[0]+""+endMon+"/"+(endDay-1));
                                            solarSet.add(endMon+"/"+(endDay+1)+"-"+nextArr[1]);
                                        }
                                        if (end.compareTo(nextDate2)>=0){
                                            solarSet.add(nextArr[0]+""+endMon+"/"+(endDay-1));
                                        }
                                    }
                                }
                                flag=true;
                                break;
                            }
                        }
                        //年/月/日
                        if (startArr1.length==3){
                            int startYear=NumberUtils.toInt(startArr1[0].substring(1));
                            int startMon=NumberUtils.toInt(startArr1[1]);
                            int startDay=NumberUtils.toInt(startArr1[2]);
                            int endYear=NumberUtils.toInt(endArr1[0].substring(1));
                            int endMon=NumberUtils.toInt(endArr1[1]);
                            int endDay=NumberUtils.toInt(endArr1[2]);
                            int nextYear1=NumberUtils.toInt(nextArr1[0].substring(1));
                            int nextMon1=NumberUtils.toInt(nextArr1[1]);
                            int nextDay1=NumberUtils.toInt(nextArr1[2]);
                            int nextYear2=NumberUtils.toInt(nextArr2[0].substring(1));
                            int nextMon2=NumberUtils.toInt(nextArr2[1]);
                            int nextDay2=NumberUtils.toInt(nextArr2[2]);
                            Calendar now=Calendar.getInstance();
                            now.set(startYear,startMon-1,startDay);
                            Date start=now.getTime();
                            now.set(endYear,endMon-1,endDay);
                            Date end=now.getTime();
                            now.set(nextYear1,nextMon1-1,nextDay1);
                            Date nextDate1=now.getTime();
                            now.set(nextYear2,nextMon2-1,nextDay2);
                            Date nextDate2=now.getTime();
                            //说明nextDate在两个日期之间
                            //说明nextDate在两个日期之间
                            if ((!start.after(nextDate1) && !nextDate1.after(end))||(!nextDate1.after(start) && !start.after(nextDate2))){
                                iterator.remove();
                                if (close){
                                    String date1=start.before(nextDate1)?ruleArr[0]:nextArr[0];
                                    String date2=end.after(nextDate2)?ruleArr[1]:nextArr[1];
                                    solarSet.add(date1+"-"+date2);
                                }else {
                                    if (start.compareTo(nextDate1)<=0){
                                        if (end.compareTo(nextDate2)<0){
                                            solarSet.add(endArr1[0]+"/"+endMon+"/"+(endDay+1)+"-"+nextArr[1]);
                                        }
                                    }
                                    else {
                                        if (end.compareTo(nextDate2)<0){
                                            solarSet.add(nextArr[0]+"-"+endArr1[0]+endMon+"/"+(endDay-1));
                                            solarSet.add(endArr1[0]+endMon+"/"+(endDay+1)+"-"+nextArr[1]);
                                        }
                                        if (end.compareTo(nextDate2)>=0){
                                            solarSet.add(nextArr[0]+"-"+endArr1[0]+endMon+"/"+(endDay-1));
                                        }
                                    }
                                }
                                flag=true;
                                break;
                            }
                        }
                    }
                    continue;
                }
            }
            if (!flag&&close){
                solarSet.add(ruleStr);
            }
        }

    }
}
