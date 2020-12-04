package com.colourfulchina.inf.base.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("sys_operate_log_detail")
public class SysOperateLogDetail implements Serializable {
    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 操作日志ID
     */
    private Long logId;

    /**
     * 操作日志ID
     */
    private Long logInfoId;
    /**
     * 操作类型
     */
    private String type;
    /**
     * 操作表名
     */
    private String tableName;

    /**
     * 操作行ID
     */
    private Object rowKey;

    /**
     * 操作字段
     */
    private String fieldName;
    /**
     * 操作前的值
     */
    private String beforeValue;

    /**
     * 操作后的值
     */
    private String afterValue;
    /**
     * 创建人
     */
    private String createUser;
    /**
     * 创建时间
     */
    private Date createTime;
}
