package com.colourfulchina.mars.api.enums;

public enum DateSourceEnum {

    BOSC_APP("BOSC_APP","上海银行-APP渠道"),
    BOSC_WECHAT("BOSC_WECHAT","上海银行-微信渠道"),
    CCB_WECHAT("CCB_WECHAT","上海建设银行-微信渠道"),
    CCB_APP("CCB_APP","上海建设银行-APP渠道"),
            ;
    private String code;
    private String codeName;

    DateSourceEnum(String code, String codeName) {
        this.code = code;
        this.codeName = codeName;
    }

    public String getcode() {
        return code;
    }

    public String getcodeName() {
        return codeName;
    }
    public static String getNameByCode(String code){
        for (DateSourceEnum bookState : DateSourceEnum.values()){
            if (bookState.code.equals(code) ){
                return bookState.codeName;
            }
        }
        return null;
    }
}
