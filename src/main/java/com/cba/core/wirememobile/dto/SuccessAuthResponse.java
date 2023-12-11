package com.cba.core.wirememobile.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SuccessAuthResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private String token;
    private String refreshToken;
    private boolean initialLogin;
    private int otpLifeTime;
    private boolean otpRequired;
    private DeviceConfigResponseDto profileData;
}
