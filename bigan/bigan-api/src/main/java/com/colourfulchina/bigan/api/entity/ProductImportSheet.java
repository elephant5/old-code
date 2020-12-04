package com.colourfulchina.bigan.api.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@TableName("product_import_sheet")
public class ProductImportSheet extends Model<ProductImportSheet> {
    private static final long serialVersionUID = -7609224151025869905L;
    @TableId(value = "id",type = IdType.INPUT)
    private String id;

    @TableField(value = "batch_id")
    private String batchId;

    @TableField(value = "sheet_index")
    private int sheetIndex;

    @TableField(value = "service_code")
    private String serviceCode;

    @TableField(value = "sheet_name")
    private String sheetName;

    @TableField(value = "title")
    private String title;

    @TableField(exist = false)
    private List<String> titleList;

    @TableField(value = "state")
    private Integer state;//0未处理,1处理中,2成功,3失败

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
        return "ProductImportSheet{" +
                "id='" + id + '\'' +
                ", batchId='" + batchId + '\'' +
                ", sheetIndex=" + sheetIndex +
                ", sheetName='" + sheetName + '\'' +
                ", title='" + title + '\'' +
                ", titleList=" + titleList +
                ", state=" + state +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
