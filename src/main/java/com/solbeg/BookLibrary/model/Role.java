package com.solbeg.BookLibrary.model;

import lombok.Getter;

import java.util.Set;

@Getter
public enum Role {
    USER(Set.of(Authority.WRITE, Authority.READ, Authority.EDIT, Authority.DELETE)),
    ADMIN(Set.of(Authority.WRITE, Authority.READ, Authority.EDIT, Authority.DELETE, Authority.ALL));

    private final Set<Authority> authorities;

    Role(Set<Authority> authorities) {
        this.authorities = authorities;
    }
}
