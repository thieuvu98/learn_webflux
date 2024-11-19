package com.minhhieu.webflux2learn.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Component
public class XAuthenticationDecoder {
    private final ObjectMapper objectMapper;

    public XAuthenticationDecoder(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public Authentication decode(String token) throws IOException {
        byte[] decoded = Base64.getUrlDecoder().decode(token);
        var principal = objectMapper.readValue(decoded, AuthAccount.class);
        return new UsernamePasswordAuthenticationToken(principal, token, List.of());
    }
}
