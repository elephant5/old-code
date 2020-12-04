package com.colourfulchina.inf.base.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用response
 * @param <T>
 */
@Data
public class CommonResultVo<T> implements Serializable {

    private static final long serialVersionUID = -5672939670502706137L;
    private int code = 100;
    private String msg = "ok";
    private T result;
    private boolean isSuccess = true;
    public T getRecords(){
        return this.result;
    }

    public void setCode(int code){
         this.isSuccess  =  code  == 200 ? false : true;
         this.code = code;
    }
}
