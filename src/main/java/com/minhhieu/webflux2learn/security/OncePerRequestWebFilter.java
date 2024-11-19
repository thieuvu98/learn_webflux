package com.minhhieu.webflux2learn.security;

import lombok.SneakyThrows;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.io.IOException;

public abstract class OncePerRequestWebFilter implements WebFilter {
    private final String filterApplied;

    public OncePerRequestWebFilter(String filterAlreadyApplied) {
        this.filterApplied = filterAlreadyApplied;
    }

    @SneakyThrows
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        if (exchange.getAttribute(filterApplied) != null) {
            return chain.filter(exchange);
        }
        exchange.getAttributes().put(filterApplied, true);
        return extendFilter(exchange, chain);
    }

    public abstract Mono<Void> extendFilter(ServerWebExchange exchange, WebFilterChain chain) throws IOException;
}
