package com.colourfulchina.god.door.api.vo;

import lombok.Data;

import java.util.List;

@Data
public class KltSysMenus extends KltSysFunction {
    private static final long serialVersionUID = 4003944635001851847L;

    private List<KltSysFunction> sonMenus;
}
