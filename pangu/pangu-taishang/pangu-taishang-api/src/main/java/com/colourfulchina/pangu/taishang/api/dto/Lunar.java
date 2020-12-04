package com.colourfulchina.pangu.taishang.api.dto;

import lombok.Data;

@Data
public class Lunar {
    public boolean isleap;
    public int lunarDay;
    public int lunarMonth;
    public int lunarYear;
}
