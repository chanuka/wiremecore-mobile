package com.cba.core.wirememobile.exception;

import org.springframework.security.core.AuthenticationException;

public class AppSignAuthException extends AuthenticationException {
    public AppSignAuthException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public AppSignAuthException(String msg) {
        super(msg);
    }
}
