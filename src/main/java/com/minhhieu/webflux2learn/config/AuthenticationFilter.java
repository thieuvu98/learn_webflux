package com.minhhieu.webflux2learn.config;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        // Check for a specific header for authentication
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        // TODO: Handle jwt
//        if (authHeader != null && "secret-token".equals(authHeader)) {
//            return chain.filter(exchange); // User is authenticated, proceed with the request
//        } else {
//            // Authentication failed, deny access
//            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//            return exchange.getResponse().setComplete();
//        }
        return chain.filter(exchange);
    }
}