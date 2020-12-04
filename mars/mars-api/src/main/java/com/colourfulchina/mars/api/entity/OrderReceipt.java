package com.colourfulchina.mars.api.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 发票
 */
@Data
@TableName(value = "order_receipt")
public class OrderReceipt  extends Model<OrderReceipt> {

    private static final long serialVersionUID = 5929382668924941491L;
        @TableId(value = "id" ,type = IdType.INPUT )
        private Integer id;//	发票id
        @TableField(value = "order_id")
        private Integer orderId	;//销售单id
        @TableField(value = "receipt_num")
        private String receiptNum	;//发票单号
        @TableField(value = "receipt_name")
        private String receiptName	;//发票名称
        @TableField(value = "receipt_content")
        private String receiptContent	;//发票内容
        @TableField(value = "receipt_time")
        private Date receiptTime	;//开票时间
        @TableField(value = "receipt_no")
        private String receiptNo	;//税号
        @TableField(value = "receipt_amt")
        private BigDecimal receiptAmt	;//开票金额
        @TableField(value = "receipt_type")
        private String receiptType	;//发票类型-企业，个人
        @TableField(value = "receipt_status")
        private String receiptStatus;//	发票状态
        @TableField(value = "receipt_remarks")
        private String receiptRemarks	;//发票备注
        @TableField(value = "receipt_title")
        private String receiptTitle	;//发票抬头
        @TableField(value = "order_batch_id")
        private Integer order_batch_id	;//批次id
        @TableField(value = "del_flag")
        private Integer delFlag	;//删除标识（0-正常，1-删除）
        @TableField(value = "create_user")
        private String createUser	;//创建人
        @TableField(value = "update_time",update = "CURRENT_TIMESTAMP()")
        private Date updateTime	;//更新时间
        @TableField(value = "update_user")
        private String updateuser	;//更新人

    /**
     * 主键值
     */
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
