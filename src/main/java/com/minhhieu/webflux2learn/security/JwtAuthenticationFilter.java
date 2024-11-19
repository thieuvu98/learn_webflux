package com.minhhieu.webflux2learn.security;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;


//@Component
@Log4j2
public class JwtAuthenticationFilter extends OncePerRequestWebFilter {
    private static final String BEARER_TOKEN_PREFIX = "Bearer ";
    private static final int TOKEN_START_INDEX = BEARER_TOKEN_PREFIX.length();
    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        super("JwtAuthenticationFilter");
        this.jwtService = jwtService;
    }

    @Override
    public Mono<Void> extendFilter(ServerWebExchange exchange, WebFilterChain chain) {
        var headers = exchange.getRequest().getHeaders();
        final var header = headers.get(HttpHeaders.AUTHORIZATION);
        String authHeader;
        if (header == null || header.isEmpty() || !((authHeader = header.getFirst()).startsWith(BEARER_TOKEN_PREFIX))) {
            return chain.filter(exchange);
        }
        final var token = authHeader.substring(TOKEN_START_INDEX);
        var authentication = jwtService.decode(token);
        if (authentication != null) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        return chain.filter(exchange);
    }

}
