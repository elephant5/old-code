package com.colourfulchina.pangu.taishang.utils;

import com.baomidou.mybatisplus.plugins.Page;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;

public class PageUtil<E> extends ArrayList<E> implements Closeable {

    private long current;
    private long pageSize;
    private long total;
    private long pages;

    public long getCurrent() {
        return current;
    }

    public void setCurrent(long current) {
        this.current = current;
    }

    public long getPageSize() {
        return pageSize;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getPages() {
        return pages;
    }

    public void setPages(long pages) {
        this.pages = pages;
    }

    public static <M> Page<M> of(Page<?> page, Class<M> clzz){
        Page<M> pageVo = new Page<>();
        pageVo.setCurrent(page.getCurrent());
        pageVo.setSize(page.getSize());
        pageVo.setRecords(BeanCopierUtils.copyPropertiesOfList(page.getRecords(), clzz));
        pageVo.setTotal(page.getTotal());
        return pageVo;
    }

    public String toString() {
        return "Page{pageNum=" + this.current + ", pageSize=" + this.pageSize + ", total=" + this.total + ", pages=" + this.pages + '}';
    }

    @Override
    public void close() throws IOException {

    }
}