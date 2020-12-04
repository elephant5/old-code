package com.colourfulchina.mars.api.vo.req;

import com.baomidou.mybatisplus.annotations.TableField;
import com.colourfulchina.mars.api.entity.ReservOrder;
import com.colourfulchina.mars.api.entity.ReservOrderDetail;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;


/**
 * 下预约单
 *
 * @Author: luck.luo
 * @Description:
 * @Date: 2019/6/12 11:27
 */
@Data
public class ReservOrderPlaceReq extends ReservOrder {
    private String bookName;//预订人名称
    private String bookNameEn;//预订人名称拼音
    private String bookPhone;//预订人手机号
    private String bookIdType;//预订人证件类型
    private String bookIdNum;//预订人证件号
    private List<ReservOrderDetail> books;//其他预约人
    private String checkDate;//入住日期
    private String deparDate;//离开时间
    private Integer checkNight;//	入住间
    private Integer nightNumbers;//入住夜
    private String accoAddon;//	房型 例如 豪华/无早餐
    private String accoNedds;//床型 大/双

    private Boolean self;//是否本人
    private Integer equitySubNum;//扣减间夜数

    private String superposition ;//  '同一时段权益不叠加',
    private String singleThread ;// '行权完毕次日才可以进行下一次预约',

    private String varCode;

    private BigDecimal totalAmoney	;//商品总额
    private BigDecimal discountAmoney	;//折扣金额
    private BigDecimal payAmoney;//支付金额
    private BigDecimal salesRatio;//折扣比例
    private String source;

    private Long batchId;;

    private String productNo; //第三方券编号

    @ApiModelProperty(value = "券类型")
    private Integer cpnType;

    @ApiModelProperty(value = "券编号")
    private String cpnId;

    //实物配送方式
    private Integer expressMode;
    //实物配送地址
    private String expressAddress;
    //实物收件人
    private String consignee;
    //实物收件人电话
    private String expressPhone;

    @ApiModelProperty(value = "医院ID")
    private Integer hospitalId;
    private String department;//科室
    private String visit;//就诊类型
    private String special;// 特殊需求
    private Long memFamilyId;//预约人家属ID
}
