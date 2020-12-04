package com.colourfulchina.pangu.taishang.api.dto;

import com.colourfulchina.pangu.taishang.api.entity.GoodsPortalSetting;
import com.colourfulchina.pangu.taishang.api.vo.SysBankLogoDto;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

@Data
public class GoodsPortalSettingDto extends GoodsPortalSetting {
    private String giftUrl;
    private String giftFullUrl;
    private String bankCode;
    private SysBankLogoDto sysBankLogo;
    public String getGiftFullUrl() {
        if (StringUtils.isNotBlank(this.getGiftUrl()) && StringUtils.isNotBlank(this.getCode())){
            return this.getGiftUrl() + this.getCode();
        }
        return giftFullUrl;
    }
}
