package com.mikul17.bazyDanych.Security;

import org.springframework.security.core.AuthenticationException;

public class UserUnderAgeAuthenticationException extends AuthenticationException {

    public UserUnderAgeAuthenticationException(final String msg) {
        super(msg);
    }

}
