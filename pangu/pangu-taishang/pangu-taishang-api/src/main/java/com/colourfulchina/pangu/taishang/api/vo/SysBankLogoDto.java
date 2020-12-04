package com.colourfulchina.pangu.taishang.api.vo;

import com.colourfulchina.pangu.taishang.api.dto.SysFileDto;
import com.colourfulchina.pangu.taishang.api.entity.SysBankLogo;
import lombok.Data;

@Data
public class SysBankLogoDto extends SysBankLogo {
    private SysFileDto sysFileDto;
}
