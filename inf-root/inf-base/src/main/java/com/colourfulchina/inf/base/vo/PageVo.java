package com.colourfulchina.inf.base.vo;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import lombok.Data;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * User: Ryan
 * Date: 2018/8/7
 */
@Data
public class PageVo<T> extends Pagination {
    private static final long serialVersionUID = 1L;
    private List<T> records;
    private Map<String, Object> condition;

    public PageVo() {
        this.records = Collections.emptyList();
    }

    public PageVo(int current, int size) {
        super(current, size);
        this.records = Collections.emptyList();
    }

    public PageVo(int current, int size, String orderByField) {
        super(current, size);
        this.records = Collections.emptyList();
        this.setOrderByField(orderByField);
    }

    public PageVo(int current, int size, String orderByField, boolean isAsc) {
        this(current, size, orderByField);
        this.setAsc(isAsc);
    }

    public List<T> getRecords() {
        return this.records;
    }

    public PageVo<T> setRecords(List<T> records) {
        this.records = records;
        return this;
    }

    public Map<String, Object> getCondition() {
        return this.condition;
    }

    public void setCondition(Map<String, Object> condition) {
        this.condition = condition;
    }

    //    public String toString() {
//        StringBuilder pg = new StringBuilder();
//        pg.append(" PageVo:{ [").append(super.toString()).append("], ");
//        if (this.records != null) {
//            pg.append("records-size:").append(this.records.size());
//        } else {
//            pg.append("records is null");
//        }
//
//        return pg.append(" }").toString();
//    }
}
