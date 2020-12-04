package com.colourfulchina.pangu.taishang.event;

import com.alibaba.fastjson.JSON;
import com.colourfulchina.god.door.api.util.SecurityUtils;
import com.colourfulchina.inf.base.dto.SysOperateLogInfoDto;
import com.colourfulchina.inf.base.entity.SysOperateLogDetail;
import com.colourfulchina.inf.base.entity.SysOperateLogInfo;
import com.colourfulchina.inf.base.enums.SysOperateLogEnums;
import com.colourfulchina.pangu.taishang.service.SysOperateLogService;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Slf4j
//@Component
public class SysOperateLogEventListener {
  private static final Set<String> EXCLUDE_FIELDS=Sets.newHashSet();
  private static final Set<String> DEL_FLAG_FIELDS=Sets.newHashSet();
  static {
    EXCLUDE_FIELDS.add("create_time");
    EXCLUDE_FIELDS.add("create_user");
    EXCLUDE_FIELDS.add("update_time");
    EXCLUDE_FIELDS.add("update_user");
    DEL_FLAG_FIELDS.add("del_flag");
  }
  @Autowired
  SysOperateLogService operateLogService;

//  @Async
  @Order(Ordered.HIGHEST_PRECEDENCE)
  @EventListener(SysOperateLogEvent.class)
  public void saveOperateLog(SysOperateLogEvent operateLogEvent){
    try {
      log.debug("SysOperateLogEventListener saveOperateLog");
      final SysOperateLogInfoDto operateLogInfoDto = (SysOperateLogInfoDto)operateLogEvent.getSource();
      log.debug("SysOperateLogEventListener saveOperateLog operateLogInfoDto:{}",JSON.toJSONString(operateLogInfoDto));
      //TODO 保持操作日志
      if (operateLogInfoDto != null){
        final Map<String, Object> after = operateLogInfoDto.getAfter();
        if (CollectionUtils.isEmpty(after)){
          log.debug("SysOperateLogEventListener saveOperateLog after is empty");
          return;
        }
        if (!SysOperateLogEnums.Type.INSERT.getCode().equalsIgnoreCase(operateLogInfoDto.getType()) && !SysOperateLogEnums.Type.UPDATE.getCode().equalsIgnoreCase(operateLogInfoDto.getType())){
          log.debug("SysOperateLogEventListener saveOperateLog type error");
          return;
        }
        final Map<String, Object> before = operateLogInfoDto.getBefore();
        if (SysOperateLogEnums.Type.UPDATE.getCode().equalsIgnoreCase(operateLogInfoDto.getType()) && CollectionUtils.isEmpty(before)){
          log.debug("SysOperateLogEventListener saveOperateLog UPDATE before is empty");
          return;
        }
        SysOperateLogInfo operateLogInfo=new SysOperateLogInfo();
        BeanUtils.copyProperties(operateLogInfoDto,operateLogInfo);
        operateLogInfo.setCreateUser(SecurityUtils.getLoginName());
        operateLogInfo.setCreateTime(new Date());
        List<SysOperateLogDetail> operateLogDetailList=Lists.newArrayList();
        Set<String> allKeys=after.keySet();
        if (!CollectionUtils.isEmpty(before)){
          before.keySet().forEach(k->{
            if (!allKeys.contains(k)){
              allKeys.add(k);
            }
          });
        }
        for (String key:allKeys){
          if (!EXCLUDE_FIELDS.contains(key)){
            if(SysOperateLogEnums.Type.INSERT.getCode().equalsIgnoreCase(operateLogInfo.getType())){
              if (after.get(key)!=null){
                SysOperateLogDetail operateLogDetail=new SysOperateLogDetail();
                BeanUtils.copyProperties(operateLogInfo,operateLogDetail);
                operateLogDetail.setFieldName(key);
                operateLogDetail.setAfterValue(object2string(after.get(key)));
                operateLogDetail.setType(SysOperateLogEnums.Type.INSERT.getCode());
                operateLogDetailList.add(operateLogDetail);
              }
            }else{
              if (DEL_FLAG_FIELDS.contains(key) && !after.get(key).equals(before.get(key))){
                operateLogInfo.setType(SysOperateLogEnums.Type.DELETE.getCode());
              }
              //如果before为空 after不为空 则为值更新
              if (!Objects.equal(before.get(key),after.get(key))){
                SysOperateLogDetail operateLogDetail=new SysOperateLogDetail();
                BeanUtils.copyProperties(operateLogInfo,operateLogDetail);
                operateLogDetail.setFieldName(key);
                operateLogDetail.setBeforeValue(object2string(before.get(key)));
                operateLogDetail.setAfterValue(object2string(after.get(key)));
                operateLogDetail.setType(SysOperateLogEnums.Type.UPDATE.getCode());
                operateLogDetailList.add(operateLogDetail);
              }
            }
          }
        }
        operateLogService.insertSysOperateLogInfoAndDetail(operateLogInfo,operateLogDetailList);
      }
    }catch (Exception e){
      log.error("SysOperateLogEventListener saveOperateLog error:",e);
    }
  }


  /**
   *
   * @param object
   * @return
   */
  private static String object2string(Object object){
    if (object == null){
      return null;
    }
    else if (object instanceof CharSequence){
      return object.toString();
    }
    else if (object instanceof Integer || object instanceof Long|| object instanceof Double || object instanceof Float || object instanceof Short){
      return object+"";
    }
    else if (object instanceof Number){
      Number number= (Number) object;
      return number.toString();
    }
    else if (object instanceof Date){
      return DateFormatUtils.format((Date)object,"yyyy-MM-dd HH:mm:ss");
    }
    else if (object instanceof Calendar){
      return DateFormatUtils.format((Calendar)object,"yyyy-MM-dd HH:mm:ss");
    }
    else if (object instanceof Boolean){
      return Boolean.toString((Boolean)object);
    }
    else if (object instanceof Collection){
      return JSON.toJSONString(object);
    }
    else {
      log.error("object2string unsupport object");
    }
    return null;
  }
}