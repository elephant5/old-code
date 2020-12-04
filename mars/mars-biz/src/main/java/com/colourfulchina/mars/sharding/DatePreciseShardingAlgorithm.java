package com.colourfulchina.mars.sharding;

import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.time.DateUtils;
//import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
//import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.text.ParseException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

/**
 * @author ryan wu
 * @date 2020年3月16日
 * @descriptionimplements PreciseShardingAlgorithm<Date>
 */
@Slf4j
public class DatePreciseShardingAlgorithm {
//		implements PreciseShardingAlgorithm<Date> {
//	private static DateTimeFormatter sdf = DateTimeFormatter.ofPattern("yyyyMMDD", Locale.CHINA);
//	private static final String SEPERATOR = "_";//表名分隔符
//	private static Date  lowwerDate = null;
//
//	static {
//		try {
//			lowwerDate = DateUtils.parseDate("20200316", "yyyyMMDD");
//		} catch (ParseException e) {
//			log.error("解析其实日期异常",e);
//		}
//	}
//
//	@Override
//	public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Date> shardingValue) {
//		String loginTableName = shardingValue.getLogicTableName();
//		Date createTime = shardingValue.getValue();
//		if(createTime == null || createTime.before(lowwerDate) ){
//			log.info("创建时间为空，或者当前时间:{} 小于 2020-03 ，进入默认表",createTime);
//			return loginTableName;
//		}
//		String yyyyMM = "";
//		try{
//			yyyyMM =SEPERATOR+ createTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().format(sdf);
//			log.info("进入表：{}",loginTableName+yyyyMM);
//			return loginTableName+yyyyMM;
//		}catch(Exception e){
//			log.error("解析创建时间异常，分表失败，进入默认表",e);
//		}
//		return loginTableName;
//	}


}
