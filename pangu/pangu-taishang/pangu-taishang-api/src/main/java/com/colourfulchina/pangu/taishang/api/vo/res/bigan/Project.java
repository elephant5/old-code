package com.colourfulchina.pangu.taishang.api.vo.res.bigan;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
public class Project implements Serializable {
    private Long id;
    private String name;
    private String shortName;
    private String authType;
    private String limitType;
    private Integer multiPack;
    private Integer multiGroup;
    private Integer enableBankOrder;
    private Integer dynamic;
    private Integer packageId;
    private Integer bankId;
    private String register;
    private String code;
    private String theme;
    private String title;
    private String notes;
    private String expiryDate;
    private Date createTime;
    private Date updateTime;
    private String portal;
    private String unitExpiry;
    private String more;
    private Date startDate;
    private Date endDate;
    private Integer enablePortal;
    private Integer disableWxBook;
}
