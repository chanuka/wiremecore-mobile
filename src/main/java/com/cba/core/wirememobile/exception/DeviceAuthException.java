package com.cba.core.wirememobile.exception;

import org.springframework.security.core.AuthenticationException;

public class DeviceAuthException extends AuthenticationException {
    public DeviceAuthException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public DeviceAuthException(String msg) {
        super(msg);
    }
}
