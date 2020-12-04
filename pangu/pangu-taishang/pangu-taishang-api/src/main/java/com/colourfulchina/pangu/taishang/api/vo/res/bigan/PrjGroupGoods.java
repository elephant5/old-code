package com.colourfulchina.pangu.taishang.api.vo.res.bigan;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class PrjGroupGoods implements Serializable{

    private Long groupId;
    private Long goodsId;
    private BigDecimal weight;
    private Long projectId;
    private Integer online;
    private Integer sort; //prj_group_goods表新增的排序值字段
    private Integer pgpId;//中间表ID
}
