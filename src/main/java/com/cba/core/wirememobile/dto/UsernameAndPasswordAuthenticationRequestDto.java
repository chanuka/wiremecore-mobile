package com.cba.core.wirememobile.dto;

import lombok.Data;

@Data
public class UsernameAndPasswordAuthenticationRequestDto {
    private String username;
    private String password;
    private String deviceSerial;
    private String uuid;
    private String appVersion;
    private String signature;
    private Integer sysFileSize;

    public UsernameAndPasswordAuthenticationRequestDto() {
    }
}
