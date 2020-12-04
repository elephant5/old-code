package com.colourfulchina.pangu.taishang.api.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldStrategy;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 产品组
 */
@Data
@TableName("product_group")
public class ProductGroup extends Model<ProductGroup> {
    private static final long serialVersionUID = 8505823931994410016L;

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    /**
     * 产品组所属商品id
     */
    @TableField("goods_id")
    private Integer goodsId;
    /**
     * 产品组名称
     */
    @TableField("name")
    private String name;
    /**
     * 内部简称
     */
    @TableField("short_name")
    private String shortName;
    /**
     * 使用限制id
     */
    @TableField("use_limit_id")
    private String useLimitId;
    /**
     * 使用数量
     */
    @TableField("use_num")
    private BigDecimal useNum;
    /**
     * 折扣比例
     */
    @TableField("discount_rate")
    private BigDecimal discountRate;
    /**
     * 产品组block规则
     */
    @TableField(value = "block_rule",strategy = FieldStrategy.IGNORED)
    private String blockRule;
    /**
     * 最多提前N预定
     */
    @TableField(value = "max_book_days",strategy = FieldStrategy.IGNORED)
    private Integer maxBookDays;
    /**
     * 最少提前N预定
     */
    @TableField(value = "min_book_days",strategy = FieldStrategy.IGNORED)
    private Integer minBookDays;
    /**
     * 权益使用率
     */
    @TableField("use_rate")
    private BigDecimal useRate;
    /**
     * 周期重复时间
     */
    @TableField("cycle_time")
    private Integer cycleTime;
    /**
     * 周期类型（0-天 1-周 2-月 3-年）
     */
    @TableField("cycle_type")
    private Integer cycleType;
    /**
     * 周期重复数量
     */
    @TableField("cycle_num")
    private Integer cycleNum;
    /**
     * 使用类型（0-次数  1-点数）
     */
    @TableField("use_type")
    private Integer useType;
    /**
     * 商户类型
     */
    @TableField("shop_type")
    private String shopType;
    /**
     * 老系统产品组ID
     */
    @TableField("old_id")
    private Integer oldId;
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

    /**
     * 免费次数
     */
    @TableField("free_count")
    private Integer freeCount;
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
