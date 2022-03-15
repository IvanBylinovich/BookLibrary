package com.solbeg.BookLibrary.model;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

@Getter
public enum Authority implements GrantedAuthority {
    READ, WRITE, EDIT, DELETE, ALL;

    @Override
    public String getAuthority() {
        return name();
    }
}
