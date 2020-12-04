package com.colourfulchina.bigan.api.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("product_import_workbook")
public class ProductImportWorkbook extends Model<ProductImportWorkbook> {

    private static final long serialVersionUID = -1406229227725407162L;
    @TableId(value = "batch_id",type = IdType.INPUT)
    private String batchId;//主键

    @TableField(value = "file_name")
    private String fileName;

    @TableField(value = "bank_id")
    private Integer bankId;

    @TableField(value = "project_id")
    private Integer projectId;

    @TableField(value = "total_sheet")
    private Integer totalSheet;

    @TableField(value = "total_row")
    private Integer totalRow;

    @TableField(value = "success_row")
    private Integer successRow;

    @TableField(value = "fail_row")
    private Integer failRow;

    @TableField(value = "state")
    private Integer state;//0未处理,1处理中,2成功,3失败

    @TableField(value = "create_time")
    private Date createTime;

    @TableField(value = "update_time")
    private Date updateTime;

    @Override
    protected Serializable pkVal() {
        return this.batchId;
    }

    @Override
    public String toString() {
        return "ProductImportWorkbook{" +
                "batchId='" + batchId + '\'' +
                ", fileName='" + fileName + '\'' +
                ", totalSheet=" + totalSheet +
                ", totalRow=" + totalRow +
                ", successRow=" + successRow +
                ", failRow=" + failRow +
                ", state=" + state +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
