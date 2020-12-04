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

@TableName("order_batch")
@Data
public class OrderBatch extends Model<OrderBatch> {
    private static final long serialVersionUID = -7647276367691741983L;

    /**
     * 销售表id
     */
    @ApiModelProperty("销售表id")
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    /**
     * 批次号
     */
    @ApiModelProperty("批次号")
    @TableField("order_batch_id")
    private String orderBatchId;
    /**
     * 项目所属id
     */
    @ApiModelProperty("项目所属id")
    @TableField("product_id")
    private Integer productId;
    /**
     * 所属项目名称
     */
    @ApiModelProperty("所属项目名称")
    @TableField("product_name")
    private String productName;
    /**
     * 销售渠道id
     */
    @ApiModelProperty("销售渠道id")
    @TableField("sales_channel_id")
    private Integer salesChannelId;
    /**
     * 销售渠道名称
     */
    @ApiModelProperty("销售渠道名称")
    @TableField("sales_channel_name")
    private String salesChannelName;
    /**
     * 销售日期
     */
    @ApiModelProperty("销售日期")
    @TableField("sales_time")
    private Date salesTime;
    /**
     * 导入的文件名
     */
    @ApiModelProperty("导入的文件名")
    @TableField("import_file_name")
    private String importFileName;
    /**
     * 导入的条数
     */
    @ApiModelProperty("导入的条数")
    @TableField("import_file_num")
    private Integer importFileNum;
    /**
     * 短信数量
     */
    @ApiModelProperty("短信数量")
    @TableField("sms_num")
    private Integer smsNum;
    /**
     * 模板id
     */
    @ApiModelProperty("模板id")
    @TableField("refer_templat_id")
    private Integer referTemplatId;
    /**
     * 引入模板名称
     */
    @ApiModelProperty("引入模板名称")
    @TableField("refer_templat_name")
    private String referTemplatName;
    /**
     * 导入状态"导入成功", "1" "已发短信", "2" "部分发短信", "3"  "删除", "4"  "其他", "5"
     */
    @ApiModelProperty("导入状态\"导入成功\", \"1\" \"已发短信\", \"2\" \"部分发短信\", \"3\"  \"删除\", \"4\"  \"其他\", \"5\"")
    @TableField("import_status")
    private String importStatus;
    /**
     * 导入日志
     */
    @ApiModelProperty("导入日志")
    @TableField("import_log")
    private String importLog;
    /**
     * 导入时长
     */
    @ApiModelProperty("导入时长")
    @TableField("import_time")
    private Date importTime;
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
