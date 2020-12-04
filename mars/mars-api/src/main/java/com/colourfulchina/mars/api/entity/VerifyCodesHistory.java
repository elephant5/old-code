package com.colourfulchina.mars.api.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * mars数据库
 */
@TableName("verify_codes_history")
@Data
public class VerifyCodesHistory extends Model<VerifyCodesHistory> {

    private static final long serialVersionUID = -6230186912705171890L;
    @TableId(value = "id",type = IdType.AUTO)
    private Integer  id ;//int(11) NOT NULL AUTO_INCREMENT,
    @TableField(value = "type")
    private String type;// varchar(10) DEFAULT NULL,
    @TableField(value = "code_num")
    private String codenum ;//varchar(100) DEFAULT NULL,
    @TableField(value = "amount")
    private String amount;// varchar(100) DEFAULT NULL,
    @TableField(value = "consumeamt")
    private String consumeamt;// varchar(100) DEFAULT NULL,
    @TableField(value = "order_no")
    private Integer  orderno ;//int(11) DEFAULT NULL,
    @TableField(value = "start_date")
    private String startdate;// varchar(100) DEFAULT NULL,
    @TableField(value = "end_date")
    private String enddate ;//varchar(100) DEFAULT NULL,
    @TableField(value = "remarks")
    private String remarks ;//varchar(1000) DEFAULT NULL,
    @TableField(value = "user_no")
    private Integer  userno ;//int(11) DEFAULT NULL,
    @TableField(value = "shop_id")
    private String shopid ;//varchar(100) DEFAULT NULL,
    @TableField(value = "supplier_no")
    private String supplierno ;//varchar(100) DEFAULT NULL,
    @TableField(value = "verdate")
    private Date verdate ;//timestamp NULL DEFAULT NULL,
    @TableField(value = "book_date")
    private String bookDate;// varchar(100) DEFAULT NULL,
    @TableField(value = "book_time")
    private String bookTime;// varchar(100) DEFAULT NULL,
    @TableField(value = "name")
    private String name ;//varchar(100) DEFAULT NULL,
    @TableField(value = "phone")
    private String phone ;//varchar(100) DEFAULT NULL,
    @TableField(value = "people")
    private String people;// varchar(5) DEFAULT NULL,
    @TableField(value = "unit")
    private String unit ;//varchar(20) DEFAULT NULL,verifyDateRange
    @TableField(value = "goods_id")
    private String  goodsId;// varchar(100) DEFAULT NULL,
    @TableField(value = "goods_name")
    private String goodsName ;//varchar(100) DEFAULT NULL,
    @TableField(value = "bank")
    private String bank;// varchar(100) DEFAULT NULL,
    @TableField(value = "state")
    private String state ;//varchar(2) DEFAULT '1' COMMENT '状态, 0:未核销, 1:已核销, 3:已过期',
    @TableField(value = "versupplierno")
    private String versupplierno;// varchar(100) DEFAULT NULL,
    @TableField(value = "item")
    private String item;// varchar(200) DEFAULT NULL COMMENT '票别',
    @TableField(value = "editlog")
    private String  editlog ;//mediumtext COMMENT

    @TableField(value = "old_id")
    private Integer  oldId ;//mediumtext COMMENT
    /**
     * 删除标识（0正常，1删除）
     */
    @TableField("del_flag")
    private Integer delFlag;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 创建人
     */
    @TableField("create_user")
    private String createUser;
    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;
    /**
     * 更新人
     */
    @TableField("update_user")
    private String updateUser;
    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
