package com.colourfulchina.pangu.taishang.api.dto;

import com.colourfulchina.pangu.taishang.api.entity.SysFile;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

@Data
public class SysFileDto extends SysFile {
    private String type;
    private Integer objId;
    private Integer sort;
    private String erpCdnUrl;
    private String erpCdnHttpsUrl;
    private String erpCdnHttpUrl;
    private String pgCdnUrl;
    private String pgCdnHttpsUrl;
    private String pgCdnHttpUrl;
    private String pgCdnHttpsFullUrl;
    private String pgCdnHttpFullUrl;
    private String pgCdnNoHttpFullUrl;

    public String getErpCdnHttpsUrl() {
        if (StringUtils.isNotBlank(this.getErpCdnUrl())){
            return "https://"+this.getErpCdnUrl();
        }
        return erpCdnHttpsUrl;
    }

    public String getErpCdnHttpUrl() {
        if (StringUtils.isNotBlank(this.getErpCdnUrl())){
            return "http://"+this.getErpCdnUrl();
        }
        return erpCdnHttpUrl;
    }

    public String getPgCdnHttpsUrl() {
        if (StringUtils.isNotBlank(this.getPgCdnUrl())){
            return "https://"+this.getPgCdnUrl();
        }
        return pgCdnHttpsUrl;
    }

    public String getPgCdnHttpUrl() {
        if (StringUtils.isNotBlank(this.getPgCdnUrl())){
            return "http://"+this.getPgCdnUrl();
        }
        return pgCdnHttpUrl;
    }
    public String getPgCdnHttpFullUrl(){
        if (StringUtils.isNotBlank(this.getPgCdnNoHttpFullUrl())){
            return "http://"+this.getPgCdnNoHttpFullUrl();
        }
        return pgCdnHttpFullUrl;
    }
    public String getPgCdnHttpsFullUrl(){
        if (StringUtils.isNotBlank(this.getPgCdnNoHttpFullUrl())){
            return "https://"+this.getPgCdnNoHttpFullUrl();
        }
        return pgCdnHttpsFullUrl;
    }

    public String getPgCdnNoHttpFullUrl() {
        if (StringUtils.isNotBlank(this.getPgCdnUrl()) && StringUtils.isNotBlank(this.getGuid()) && StringUtils.isNotBlank(this.getExt())){
            return this.getPgCdnUrl()+"/"+this.getGuid()+"."+this.getExt();
        }
        return pgCdnNoHttpFullUrl;
    }
}
