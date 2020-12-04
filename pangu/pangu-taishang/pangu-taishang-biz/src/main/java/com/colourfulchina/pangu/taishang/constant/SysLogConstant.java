package com.colourfulchina.pangu.taishang.constant;

import java.io.Serializable;

/**
 * 修改日志常量
 */
public class SysLogConstant implements Serializable {
    private static final long serialVersionUID = 966978697809471958L;
    /**
     * 数据库操作的表名
     */
    public static final String LOG_TABLE_NAME = "tableName";
    /**
     * 数据库操作的类型
     */
    public static final String LOG_OPT_TYPE = "type";
    /**
     * 数据库操作的主键
     */
    public static final String LOG_ROW_KEY = "rowKey";
    /**
     * 数据库操作的列名
     */
    public static final String LOG_FIELD_NAME = "fieldName";

    /**
     * 酒店日志查询酒店中文名
     */
    public static final String LOG_HOTEL_NAME_CH = "hotelNameCh";
    /**
     * 酒店日志查询酒店英文名
     */
    public static final String LOG_HOTEL_NAME_EN = "hotelNameEn";
}
