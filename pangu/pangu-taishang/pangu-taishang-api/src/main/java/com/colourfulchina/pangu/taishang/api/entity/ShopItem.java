package com.colourfulchina.pangu.taishang.api.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldStrategy;
import com.baomidou.mybatisplus.enums.IdType;
import com.colourfulchina.pangu.taishang.api.enums.ResourceTypeEnums;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 商品规格表
 */
@Data
@TableName(value = "shop_item")
public class ShopItem extends Model<ShopItem> {
    private static final long serialVersionUID = -6136551332808313933L;
    /**
     * 主键
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    /**
     * 老系统shopItem主键
     */
    @TableField(value = "old_item_id")
    private Integer oldItemId;
    /**
     * 商户ID
     */
    @TableField(value = "shop_id")
    private Integer shopId;
    /**
     * 服务类型
     */
    @TableField(value = "service_type")
    private String serviceType;
    /**
     * 服务名称
     */
    @TableField(value = "service_name")
    private String serviceName;
    /**
     * 规格名称
     */
    @TableField("name")
    private String name;
    /**
     * 住宿床型
     */
    @TableField(value = "needs")
    private String needs;
    /**
     * 住宿早餐类型
     */
    @TableField(value = "addon")
    private String addon;
    /**
     * 开放起始时间
     */
    @TableField("open_time")
    private String openTime;
    /**
     * 开放结束时间
     */
    @TableField("close_time")
    private String closeTime;
    /**
     * 取消政策
     */
    @TableField("cancel_policy")
    private Integer cancelPolicy;
    /**
     * 保留房
     */
    @TableField("retain_room")
    private Integer retainRoom;
    /**
     * 携程最低价
     */
    @TableField("xc_min_price")
    private BigDecimal xcMinPrice;
    /**
     * 携程最高价
     */
    @TableField("xc_max_price")
    private BigDecimal xcMaxPrice;
    /**
     * 携程平均价
     */
    @TableField("xc_avg_price")
    private BigDecimal xcAvgPrice;

    /**
     * 适用城市
     */
    @TableField("citys")
    private String citys;
    /**
     * block规则
     */
    @TableField(value = "block_rule",strategy = FieldStrategy.IGNORED)
    private String blockRule;
    /**
     * 菜单
     */
    @TableField("menu_text")
    private String menuText;
    /**
     * 第三方产品编号
     */
    @TableField("third_cpn_num")
    private String thirdCpnNum;
    /**
     * 停售原因
     */
    @TableField("stop_reason")
    private String stopReason;
    /**
     * 第三方产品名称
     */
    @TableField("third_cpn_name")
    private String thirdCpnName;
    /**
     * 在售0  停售1
     */
    @TableField("enable")
    private Integer enable;
    /**
     * 删除标识（0-正常，1-删除）
     */
    @TableField(value = "del_flag")
    private Integer delFlag;
    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;
    /**
     * 创建人
     */
    @TableField(value = "create_user")
    private String createUser;
    /**
     * 更新时间
     */
    @TableField(value = "update_time",update = "CURRENT_TIMESTAMP()")
    private Date updateTime;
    /**
     * 更新人
     */
    @TableField(value = "update_user")
    private String updateUser;

    @TableField(value = "gift")
    private String gift;

    /**
     * 配送方式（0-无需配送，1-快递发货，2-及时送达，3-到店自取）
     */
    @TableField(value = "express_mode")
    private Integer expressMode;

    @TableField(exist = false)
    private List<String> giftList;

    @TableField(exist = false)
    private List<String> cityList;

    public String getServiceName() {
        String res = null ;
        if(StringUtils.isNotBlank(serviceType)){
            res = ResourceTypeEnums.findByCode(this.serviceType).getName();
        }

        return res;
    }



    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}