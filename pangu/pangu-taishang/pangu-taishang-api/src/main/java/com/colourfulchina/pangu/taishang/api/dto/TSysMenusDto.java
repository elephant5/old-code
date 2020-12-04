package com.colourfulchina.pangu.taishang.api.dto;

import lombok.Data;

import java.util.List;

@Data
public class TSysMenusDto extends TSysFunction {
    private static final long serialVersionUID = 4003944635001851847L;

    private List<TSysFunction> sonMenus;
}
