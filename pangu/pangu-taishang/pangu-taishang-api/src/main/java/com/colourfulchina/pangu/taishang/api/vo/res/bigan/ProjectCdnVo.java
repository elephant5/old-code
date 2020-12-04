package com.colourfulchina.pangu.taishang.api.vo.res.bigan;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@ToString
public class ProjectCdnVo<T> implements Serializable{
    private static final long serialVersionUID = 1L;

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
    private String unitExpiry;
    private Date startDate;
    private Date endDate;
    private Integer enablePortal;
    private String portal;
    private String packageName;
    private String projectUnitExpiry;//项目的权益有效期
    private List<ProjectChannel> projectChannelList;

    //限制单次可预订间最大夜数
    private Integer maxNight;

    //(住宿)离店后才可以进行下次预约:true:限制;false:不限制
    private String bookAfterLeave;
    /**
     * 精选套餐项目方图
     */
    private List<String> comboImgList;
    /**
     * 我的订单项目方图
     */
    private List<String> orderImgList;
    /**
     * 商品详情项目长图
     */
    private List<String> detailImgList;
    private T userProjectDetail;
    private List<PrjGroupVo> prjGroupVoList;
}
