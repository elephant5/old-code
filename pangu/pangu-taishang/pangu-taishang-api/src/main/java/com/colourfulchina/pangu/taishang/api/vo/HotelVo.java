package com.colourfulchina.pangu.taishang.api.vo;

import com.baomidou.mybatisplus.annotations.TableField;
import com.colourfulchina.pangu.taishang.api.entity.Hotel;
import com.colourfulchina.pangu.taishang.api.enums.ShopAccountTypeEnums;
import lombok.Data;

/**
 * 酒店信息加上国家id
 */
@Data
public class HotelVo extends Hotel {
    private static final long serialVersionUID = -1562016145971822397L;
    /**
     * 国家id
     */
    private String countryId;
    /**
     * 账户类型(0商户1酒店2集团） 默认为商户
     */
    private Integer accountType ;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
}
