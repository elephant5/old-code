package com.colourfulchina.god.door.api.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class SysToken implements Serializable {
    private String token;
    private String refreshToken;
}
