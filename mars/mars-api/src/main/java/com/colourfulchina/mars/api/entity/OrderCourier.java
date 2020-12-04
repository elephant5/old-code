package com.colourfulchina.mars.api.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@TableName("order_courier")
@Data
public class OrderCourier extends Model<OrderCourier> {
    private static final long serialVersionUID = 2589665707831693146L;

    /**
    * 收货id
    */
    @ApiModelProperty("收货id")
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    /**
    * 销售单表id
    */
    @ApiModelProperty("销售单表id")
    @TableField("order_id")
    private Integer orderId;
    /**
    * 收货地址
    */
    @ApiModelProperty("收货地址")
    @TableField("receiv_address")
    private String receivAddress;
    /**
    * 收货邮编
    */
    @ApiModelProperty("收货邮编")
    @TableField("receiv_zipcode")
    private String receivZipcode;
    /**
    * 收货人姓名
    */
    @ApiModelProperty("收货人姓名")
    @TableField("receiv_real_name")
    private String receivRealName;
    /**
    * 收货人电话
    */
    @ApiModelProperty("收货人电话")
    @TableField("receiv_phone")
    private String receivPhone;
    /**
    * 收货人电话2
    */
    @ApiModelProperty("收货人电话2")
    @TableField("receiv_phone2")
    private String receivPhone2;
    /**
    * 快递公司名称
    */
    @ApiModelProperty("快递公司名称")
    @TableField("courier_name")
    private String courierName;
    /**
    * 快递编号
    */
    @ApiModelProperty("快递编号")
    @TableField("courier_no")
    private String courierNo;
    /**
    * 发货日期
    */
    @ApiModelProperty("发货日期")
    @TableField("receiv_time")
    private Date receivTime;
    /**
    * 发货状态 0未发货 1已发货 2已签收 3退货 4未知
    */
    @ApiModelProperty("发货状态 0未发货 1已发货 2已签收 3退货 4未知")
    @TableField("courier_status")
    private String courierStatus;
    /**
    * 批次id
    */
    @ApiModelProperty("批次id")
    @TableField("order_batch_id")
    private String orderBatchId;
    /**
    * 备注
    */
    @ApiModelProperty("备注")
    @TableField("remarks")
    private String remarks;
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
