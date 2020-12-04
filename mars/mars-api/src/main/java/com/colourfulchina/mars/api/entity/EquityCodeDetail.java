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
 * @Author: Nickal
 * @Description:
 * @Date: 2019/5/16 14:56
 */
@TableName("equity_code_detail")
@Data
public class EquityCodeDetail extends Model<EquityCodeDetail> {
    private static final long serialVersionUID = -1979377321183975459L;
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    @TableField(value = "order_item_id")
    private Integer orderItemId;
    @TableField(value = "gift_code_id")
    private Integer giftCodeId;
    @TableField(value = "goods_id")
    private Integer goodsId;
    @TableField(value = "member_id" )
    private Long memberId;
    @TableField(value = "product_group_id")
    private Integer productGroupId;
    @TableField(value = "gift_type")
    private String giftType;
    @TableField(value = "total_count")
    private Integer totalCount;
    @TableField(value = "use_count")
    private Integer useCount;
    @TableField(value = "total_free_count")
    private Integer totalFreeCount;
    @TableField(value = "use_free_count")
    private Integer useFreeCount;
    @TableField(value = "del_flag")
    private Integer delFlag;
    @TableField(value = "create_time")
    private String createTime;
    @TableField(value = "create_user")
    private String createUser;
    @TableField(value = "update_time",update = "CURRENT_TIMESTAMP()")
    private String updateTime;
    @TableField(value = "update_user")
    private String updateUser;
    @TableField(value = "start_time")
    private Date startTime;
    @TableField(value = "end_time")
    private Date endTime;
    /**
     * 类型，（0-普通 1-周期重复）
     */
    @TableField(value = "type")
    private Integer type;
    /**
     * 周期内总次数
     */
    @TableField(value = "cycle_count")
    private Integer cycleCount;
    /**
     * 产品组快照json字符串
     */
    @TableField(value = "group_detail")
    private String groupDetail;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
