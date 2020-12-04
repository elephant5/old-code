package com.colourfulchina.pangu.taishang.api.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("goods_portal_setting")
public class GoodsPortalSetting extends Model<GoodsPortalSetting> {
    private static final long serialVersionUID = 6005685969415443558L;
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private Integer goodsId;
    private String code;
    private String shortUrl;
    private String title;
    private Integer themeId;
    private Integer bankLogoId;
    private String styleBodyFill;
    private String styleBodyText;
    private String styleBtnFill;
    private String styleBtnText;
    private Integer styleEnableBgImage;
    private String styleBgImage;
    private String styleFontSize;
    private String styleLinkText;
    private String styleTopNavFill;
    private String styleTopNavText;
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
    /**
     * 删除标识（0-正常，1-删除）
     */
    @TableField(value = "del_flag")
    private Integer delFlag;
    @Override
    protected Serializable pkVal() {
        return id;
    }
}
