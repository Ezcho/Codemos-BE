package com.tools.codemos.model;

import org.springframework.security.core.GrantedAuthority;

public final class SimpleGrantedAuthority implements GrantedAuthority {

    @Override
    public String getAuthority() {
        return null;
    }
}