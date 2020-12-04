package com.colourfulchina.bigan.api.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

@Data
@TableName("product_import_row")
public class ProductImportRow extends Model<ProductImportRow> {

    private static final long serialVersionUID = 3769098671102055064L;
    @TableId(value="id",type = IdType.INPUT)
    private String id;

    @TableField(value = "batch_id")
    private String batchId;

    @TableField(value = "sheet_index")
    private int sheetIndex;

    @TableField(value = "sheet_name")
    private String sheetName;


    @TableField(value = "row_index")
    private int rowIndex;

    @TableField(value = "value")
    private String value;

    @TableField(exist = false)
    private Map<String,String> rowMap;

    @TableField(value = "state")
    private Integer state;//0未处理,1处理中,2成功,3失败

    @TableField(value = "result")
    private String result;

    @TableField(value = "create_time")
    private Date createTime;

    @TableField(value = "update_time")
    private Date updateTime;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "ProductImportRow{" +
                "id='" + id + '\'' +
                ", batchId='" + batchId + '\'' +
                ", sheetIndex=" + sheetIndex +
                ", rowIndex=" + rowIndex +
                ", value='" + value + '\'' +
                ", state=" + state +
                ", result='" + result + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
