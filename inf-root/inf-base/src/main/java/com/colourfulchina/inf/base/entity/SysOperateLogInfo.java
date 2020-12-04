package com.colourfulchina.inf.base.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("sys_operate_log_info")
public class SysOperateLogInfo implements Serializable {
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
     * 操作类型
     */
    private String type;

    /**
     * 操作表名
     */
    private String tableName;

    /**
     * 表ID字段名
     */
    private String tableId;
    /**
     * 操作行ID
     */
    private Object rowKey;
    /**
     * 创建人
     */
    private String createUser;
    /**
     * 创建时间
     */
    private Date createTime;
}
