//package com.minhhieu.webflux2learn.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
//import org.springframework.security.config.web.server.ServerHttpSecurity;
//import org.springframework.security.web.server.SecurityWebFilterChain;
//import org.springframework.security.web.server.csrf.CookieServerCsrfTokenRepository;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.reactive.CorsWebFilter;
//import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
//import org.springframework.web.util.pattern.PathPatternParser;
//
//import java.util.List;
//
//import static org.springframework.security.config.Customizer.withDefaults;
//
////@EnableWebFluxSecurity
//public class SecurityConfig {
//    @Bean
//    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
//        http
////                .csrf(csrf -> csrf
////                        .csrfTokenRepository(CookieServerCsrfTokenRepository.withHttpOnlyFalse()).disable()
////                )
//                .authorizeExchange(exchanges -> exchanges
////                        .pathMatchers(HttpMethod.GET, "/persons").permitAll()
////                        .pathMatchers(HttpMethod.POST, "/persons").permitAll()
//                        .pathMatchers("/**").permitAll()
//                        .anyExchange().authenticated()
//                )
////                .httpBasic(withDefaults())
////                .formLogin(withDefaults())
//
//                .cors(withDefaults())
//        ;
//        return http.build();
//    }
//
//    @Bean
//    public CorsWebFilter corsWebFilter() {
//        CorsConfiguration corsConfig = new CorsConfiguration();
//        corsConfig.setAllowedOrigins(List.of("http://localhost"));
//        corsConfig.setMaxAge(3600L);
//        corsConfig.addAllowedMethod("*");
//        corsConfig.addAllowedHeader("*");
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(new PathPatternParser());
//        source.registerCorsConfiguration("/**", corsConfig);
//        return new CorsWebFilter(source);
//    }
//
//}
