package com.minhhieu.webflux2learn.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AuthAccount {
    private Long id;
    private String email;
    private String jti;
    private Long iat;
    private Long exp;
    @JsonIgnore
    private String authorization;
}
